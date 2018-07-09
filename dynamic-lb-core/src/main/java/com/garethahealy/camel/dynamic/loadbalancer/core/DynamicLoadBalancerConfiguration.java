/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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

import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.DeterministicCollectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.ProcessorSelectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.RouteStatisticsCollector;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Configuration for the dynamic load balancer
 */
public class DynamicLoadBalancerConfiguration {

    private ProcessorSelectorStrategy routeStatsSelectorStrategy;
    private DeterministicCollectorStrategy deterministicCollectorStrategy;
    private RouteStatisticsCollector routeStatisticsCollector;

    public DynamicLoadBalancerConfiguration(ProcessorSelectorStrategy routeStatsSelectorStrategy, DeterministicCollectorStrategy deterministicCollectorStrategy,
                                            RouteStatisticsCollector routeStatisticsCollector) {
        this.routeStatsSelectorStrategy = routeStatsSelectorStrategy;
        this.deterministicCollectorStrategy = deterministicCollectorStrategy;
        this.routeStatisticsCollector = routeStatisticsCollector;
    }

    public DynamicLoadBalancerConfiguration() {

    }

    public ProcessorSelectorStrategy getRouteStatsSelectorStrategy() {
        return routeStatsSelectorStrategy;
    }

    public void setRouteStatsSelectorStrategy(ProcessorSelectorStrategy routeStatsSelectorStrategy) {
        this.routeStatsSelectorStrategy = routeStatsSelectorStrategy;
    }

    public DeterministicCollectorStrategy getDeterministicCollectorStrategy() {
        return deterministicCollectorStrategy;
    }

    public void setDeterministicCollectorStrategy(DeterministicCollectorStrategy deterministicCollectorStrategy) {
        this.deterministicCollectorStrategy = deterministicCollectorStrategy;
    }

    public RouteStatisticsCollector getRouteStatisticsCollector() {
        return routeStatisticsCollector;
    }

    public void setRouteStatisticsCollector(RouteStatisticsCollector routeStatisticsCollector) {
        this.routeStatisticsCollector = routeStatisticsCollector;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("routeStatsSelectorStrategy", routeStatsSelectorStrategy)
            .append("deterministicCollectorStrategy", deterministicCollectorStrategy)
            .append("routeStatisticsCollector", routeStatisticsCollector)
            .toString();
    }
}
