/*
 * #%L
 * dynamic-lb-core
 * %%
 * Copyright (C) 2013 - 2015 Gareth Healy
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.garethahealy.camel.dynamic.loadbalancer.statistics.mbeans;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.ProcessorHolder;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteHolder;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.StatisticsCollectorType;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ToDefinition;
import org.apache.camel.processor.interceptor.DefaultChannel;
import org.apache.camel.util.URISupport;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collects stats on routes or processors used by the load balancer,
 * so that we can select the next processor which a message is sent to
 *
 * @version
 */
public class MBeanRouteStatisticsCollector extends BaseMBeanAttributeCollector {

    private static final Logger LOG = LoggerFactory.getLogger(MBeanRouteStatisticsCollector.class);

    private StatisticsCollectorType statisticsCollectorType;
    private boolean shouldCacheProcessorHolders;
    private boolean shouldCacheRouteHolders;
    private Map<String, ProcessorHolder> processorHoldersCache;
    private List<RouteHolder> routeHoldersCache;

    public MBeanRouteStatisticsCollector(CamelContext camelContext, MBeanServer mBeanServer, String statisticsCollectorType,
                                         boolean shouldCacheProcessorHolders, boolean shouldCacheRouteHolders) {
        super(camelContext, mBeanServer);

        this.statisticsCollectorType = StatisticsCollectorType.fromValue(statisticsCollectorType);
        this.shouldCacheProcessorHolders = shouldCacheProcessorHolders;
        this.shouldCacheRouteHolders = shouldCacheRouteHolders;
    }

    /**
     * Query stats for processors
     *
     * @param processors
     * @param exchange
     * @return
     */
    public List<RouteStatistics> query(List<Processor> processors, Exchange exchange) {
        Map<String, ProcessorHolder> processorHolders = getProcessorHolders(processors, exchange);
        List<RouteHolder> routeHolders = getRouteNames(processorHolders);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        for (RouteHolder current : routeHolders) {
            stats.add(query(processorHolders, current.getCamelContextName(), current.getRouteName()));
        }

        return stats;
    }

    /**
     * Get the route info which is matched against the processor info. i.e.: Processor URI -> Route FROM URI
     *
     * @param processorHolders
     * @return
     */
    private List<RouteHolder> getRouteNames(Map<String, ProcessorHolder> processorHolders) {
        if (!shouldCacheRouteHolders || routeHoldersCache == null || routeHoldersCache.size() <= 0) {
            routeHoldersCache = new ArrayList<RouteHolder>();

            Set<ObjectName> set = queryNames(null, null, StatisticsCollectorType.ALL_ROUTES);
            Iterator<ObjectName> iterator = set.iterator();
            while (iterator.hasNext()) {
                ObjectName foundMBean = iterator.next();

                String uri = normalizeUri(getStringAttribute(foundMBean, "EndpointUri"));
                if (processorHolders.containsKey(uri)) {
                    RouteHolder holder = new RouteHolder();
                    holder.setCamelContextName(getStringAttribute(foundMBean, "CamelId"));
                    holder.setRouteName(getStringAttribute(foundMBean, "RouteId"));
                    holder.setUri(uri);

                    routeHoldersCache.add(holder);
                }
            }

            if (routeHoldersCache.size() <= 0) {
                throw new IllegalStateException("Found no route holders based on keys '" + Arrays.toString(processorHolders.keySet().toArray()) + "'");
            }

            LOG.debug("Found '{}' routes which match the processors", routeHoldersCache.toArray());
        }

        return routeHoldersCache;
    }

    /**
     * Get the stats for a MBean object, either a route or processor
     *
     * @param processorHolders
     * @param camelContextName
     * @param name
     * @return
     */
    private RouteStatistics query(Map<String, ProcessorHolder> processorHolders, String camelContextName, String name) {
        RouteStatistics stats = null;

        Set<ObjectName> set = queryNames(camelContextName, name, statisticsCollectorType);
        Iterator<ObjectName> iterator = set.iterator();
        if (iterator.hasNext()) {
            ObjectName foundMBean = iterator.next();

            String camelId = getStringAttribute(foundMBean, "CamelId");
            if (camelId != null && camelId.equalsIgnoreCase(camelContextName)) {
                stats = new RouteStatistics();
                stats.setProcessorHolder(processorHolders.get(normalizeUri(getStringAttribute(foundMBean, "EndpointUri"))));
                stats.setInflightExchange(getIntegerAttribute(foundMBean, "InflightExchanges"));
                stats.setMeanProcessingTime(getLongAttribute(foundMBean, "MeanProcessingTime"));
                stats.setLastProcessingTime(getLongAttribute(foundMBean, "LastProcessingTime"));
                stats.setLoad01(getStringAttribute(foundMBean, "Load01"));
                stats.setLoad05(getStringAttribute(foundMBean, "Load05"));
                stats.setLoad15(getStringAttribute(foundMBean, "Load15"));

                LOG.debug("Found '{}' stats for '{}' '{}'", stats, camelContextName, name);
            }
        }

        return stats;
    }

    /**
     * Get a map which contains the processor URI (key) and processor info (value)
     *
     * @param processors
     * @param exchange
     * @return
     */
    private Map<String, ProcessorHolder> getProcessorHolders(List<Processor> processors, Exchange exchange) {
        if (!shouldCacheProcessorHolders || processorHoldersCache == null || processorHoldersCache.size() <= 0) {
            processorHoldersCache = new HashMap<String, ProcessorHolder>();

            for (Processor current : processors) {
                String uri = getUriFromProcessor(current);

                ProcessorHolder holder = new ProcessorHolder();
                holder.setCamelContextName(exchange.getContext().getName());
                holder.setRouteName(exchange.getFromRouteId());
                holder.setUri(uri);
                holder.setProcessor(current);

                processorHoldersCache.put(uri, holder);
            }

            if (processorHoldersCache.size() <= 0) {
                throw new IllegalStateException("Found no processor holders based on processors '" + Arrays.toString(processors.toArray()) + "'");
            }

            LOG.debug("Found '{}' processors'", processorHoldersCache.values().toArray());
        }

        return processorHoldersCache;
    }

    /**
     * Get the uri from the processor (NOTE: Current impl uses reflection, so could fail easily)
     * @param current
     * @return
     */
    private String getUriFromProcessor(Processor current) {
        String uri = "";

        //NOTE: What if camel uses different 'Channels', this wont work.
        // How can i get the URI from the processor in a nice way?

        if (current instanceof DefaultChannel) {
            DefaultChannel currentChannel = (DefaultChannel)current;

            Object outputValue = null;
            try {
                //NOTE: Shouldnt really be using reflection...
                Field outputField = FieldUtils.getField(DefaultChannel.class, "childDefinition", true);
                outputValue = FieldUtils.readField(outputField, currentChannel, true);
            } catch (IllegalAccessException ex) {
                //ignore
            }

            //NOTE: What if the definition isnt a To, its another type...
            if (outputValue != null && outputValue instanceof ToDefinition) {
                ToDefinition to = (ToDefinition)outputValue;

                uri = normalizeUri(to.getUri());
            }
        }

        return uri;
    }

    /**
     * Normalize the URI so we can match easily
     * @param uri
     * @return
     */
    private String normalizeUri(String uri) {
        String normalizeUri = "";
        try {
            normalizeUri = URISupport.normalizeUri(uri);
        } catch (URISyntaxException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        } catch (UnsupportedEncodingException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return normalizeUri;
    }
}
