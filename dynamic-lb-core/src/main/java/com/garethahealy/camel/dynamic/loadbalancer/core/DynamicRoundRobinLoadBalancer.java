/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
 * %%
 * Copyright (C) 2013 - 2017 Gareth Healy
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

import java.util.List;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.DeterministicCollectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.ProcessorSelectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.RouteStatisticsCollector;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic load balancer that selects a processor based on metrics, else fallback to default round-robin
 */
public class DynamicRoundRobinLoadBalancer extends RoundRobinLoadBalancer {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicRoundRobinLoadBalancer.class);

    private DynamicLoadBalancerConfiguration config;

    public DynamicRoundRobinLoadBalancer(DynamicLoadBalancerConfiguration config) {
        this.config = config;
    }

    @Override
    protected synchronized Processor chooseProcessor(List<Processor> processors, Exchange exchange) {
        Processor answer = null;
        Processor defaultSelection = super.chooseProcessor(processors, exchange);

        DeterministicCollectorStrategy deterministicCollectorStrategy = config.getDeterministicCollectorStrategy();
        if (deterministicCollectorStrategy.shouldCollect()) {
            RouteStatisticsCollector routeStatisticsCollector = config.getRouteStatisticsCollector();
            List<RouteStatistics> stats = routeStatisticsCollector.query(processors, exchange);
            if (stats.size() > 0) {
                ProcessorSelectorStrategy selectorStrategy = config.getRouteStatsSelectorStrategy();
                answer = selectorStrategy.getProcessor(stats);

                LOG.info("About to update processor to '{}'", answer);
            }
        }

        if (answer == null) {
            answer = defaultSelection;
        }

        return answer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("config", config)
            .toString();
    }
}
