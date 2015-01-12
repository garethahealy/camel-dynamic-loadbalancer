/*
 * #%L
 * lb-core
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
package com.garethahealy.camel.dynamic.loadbalancer.core;

import java.util.ArrayList;
import java.util.List;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.DeterministicCollectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.ProcessorSelectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.RouteStatisticsCollector;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.loadbalancer.WeightedRoundRobinLoadBalancer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicWeightedRoundRobinLoadBalancer extends WeightedRoundRobinLoadBalancer {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicWeightedRoundRobinLoadBalancer.class);
    private DynamicLoadBalancerConfiguration config;

    public DynamicWeightedRoundRobinLoadBalancer(DynamicLoadBalancerConfiguration config) {
        super(new ArrayList<Integer>());

        this.config = config;
    }

    @Override
    protected Processor chooseProcessor(List<Processor> processors, Exchange exchange) {
        DeterministicCollectorStrategy deterministicCollectorStrategy = config.getDeterministicCollectorStrategy();

        boolean isFirstRun = getRuntimeRatios().size() <= 0;
        if (isFirstRun || deterministicCollectorStrategy.shouldCollect()) {
            RouteStatisticsCollector routeStatisticsCollector = config.getRouteStatisticsCollector();
            List<RouteStatistics> stats = routeStatisticsCollector.query(config.getRouteNames());
            if (stats.size() >= 0) {
                ProcessorSelectorStrategy selectorStrategy = config.getRouteStatsSelectorStrategy();
                List<Integer> found = selectorStrategy.getOrderedProcessorIndexs(stats);

                LOG.debug("Found processors ordered as '{}' with processors containing '{}'", found.toArray(), processors.size());

                getRuntimeRatios().clear();
                loadRuntimeRatios(found);
            }
        }

        //TODO?
        //- is processors ordered, i.e.: linkedlist

        return super.chooseProcessor(processors, exchange);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("config", config)
            .toString();
    }
}
