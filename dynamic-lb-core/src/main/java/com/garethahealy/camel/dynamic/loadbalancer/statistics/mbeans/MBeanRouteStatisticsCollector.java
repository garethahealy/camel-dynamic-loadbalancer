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

import java.lang.reflect.Field;
import java.util.ArrayList;
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
import org.apache.camel.processor.SendProcessor;
import org.apache.camel.processor.interceptor.DefaultChannel;
import org.apache.commons.lang3.reflect.FieldUtils;

public class MBeanRouteStatisticsCollector extends BaseMBeanAttributeCollector {

    private StatisticsCollectorType statisticsCollectorType;

    public MBeanRouteStatisticsCollector(CamelContext camelContext, MBeanServer mBeanServer, String statisticsCollectorType) {
        super(camelContext, mBeanServer);

        this.statisticsCollectorType = StatisticsCollectorType.fromValue(statisticsCollectorType);
    }

    public List<RouteStatistics> query(List<Processor> processors, Exchange exchange) {
        Map<String, ProcessorHolder> processorHolders = getProcessorHolders(processors, exchange);
        List<RouteHolder> routeHolders = getRouteNames(processorHolders);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        for (RouteHolder current : routeHolders) {
            stats.add(query(processorHolders, current.getRouteName()));
        }

        return stats;
    }

    private List<RouteHolder> getRouteNames(Map<String, ProcessorHolder> processorHolders) {
        List<RouteHolder> holders = new ArrayList<RouteHolder>();

        Set<ObjectName> set = queryNames(null, StatisticsCollectorType.ALL_ROUTES);
        Iterator<ObjectName> iterator = set.iterator();
        while (iterator.hasNext()) {
            ObjectName foundMBean = iterator.next();

            String uri = getStringAttribute(foundMBean, "EndpointUri");
            if (processorHolders.containsKey(uri)) {
                RouteHolder holder = new RouteHolder();
                holder.setCamelContextName(getStringAttribute(foundMBean, "CamelId"));
                holder.setRouteName(getStringAttribute(foundMBean, "RouteId"));
                holder.setUri(uri);

                holders.add(holder);
            }
        }

        return holders;
    }

    private RouteStatistics query(Map<String, ProcessorHolder> processorHolders, String name) {
        RouteStatistics stats = null;

        Set<ObjectName> set = queryNames(name, statisticsCollectorType);
        Iterator<ObjectName> iterator = set.iterator();
        if (iterator.hasNext()) {
            ObjectName foundMBean = iterator.next();

            String camelId = getStringAttribute(foundMBean, "CamelId");
            if (camelId != null && camelId.equalsIgnoreCase(camelContextName)) {
                stats = new RouteStatistics();
                stats.setProcessorHolder(processorHolders.get(getStringAttribute(foundMBean, "EndpointUri")));
                stats.setInflightExchange(getIntegerAttribute(foundMBean, "InflightExchanges"));
                stats.setMeanProcessingTime(getLongAttribute(foundMBean, "MeanProcessingTime"));
                stats.setLastProcessingTime(getLongAttribute(foundMBean, "LastProcessingTime"));
                stats.setLoad01(getStringAttribute(foundMBean, "Load01"));
                stats.setLoad05(getStringAttribute(foundMBean, "Load05"));
                stats.setLoad15(getStringAttribute(foundMBean, "Load15"));
            }
        }

        return stats;
    }

    private Map<String, ProcessorHolder> getProcessorHolders(List<Processor> processors, Exchange exchange) {
        Map<String, ProcessorHolder> answer = new HashMap<String, ProcessorHolder>();

        for (Processor current : processors) {
            if (current instanceof DefaultChannel) {
                DefaultChannel currentChannel = (DefaultChannel)current;

                Object outputValue = null;
                try {
                    Field outputField = FieldUtils.getField(DefaultChannel.class, "output", true);
                    outputValue = FieldUtils.readField(outputField, currentChannel, true);
                } catch (IllegalAccessException ex) {
                    //ignore
                }

                if (outputValue != null && outputValue instanceof SendProcessor) {
                    SendProcessor sendProcessor = (SendProcessor)outputValue;

                    String uri = sendProcessor.getDestination().getEndpointUri();

                    ProcessorHolder holder = new ProcessorHolder();
                    holder.setCamelContextName(exchange.getContext().getName());
                    holder.setRouteName(exchange.getFromRouteId());
                    holder.setUri(uri);
                    holder.setProcessor(current);

                    answer.put(uri, holder);
                }
            }
        }

        return answer;
    }
}
