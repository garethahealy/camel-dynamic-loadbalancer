/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
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

import javax.management.MBeanServer;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.EveryXDeterministicCollectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.MeanProcessingTimeProcessorSelectorStrategy;
import com.garethahealy.camel.dynamic.loadbalancer.statistics.mbeans.MBeanRouteStatisticsCollector;

import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DynamicLoadBalancerConfigurationTest {

    @Test
    public void canUseSetters() {
        DynamicLoadBalancerConfiguration config = new DynamicLoadBalancerConfiguration();
        config.setDeterministicCollectorStrategy(new EveryXDeterministicCollectorStrategy(1, 1));
        config.setRouteStatisticsCollector(new MBeanRouteStatisticsCollector(new DefaultCamelContext(), Mockito.mock(MBeanServer.class), "route", false, false));
        config.setRouteStatsSelectorStrategy(new MeanProcessingTimeProcessorSelectorStrategy());

        Assert.assertNotNull(config.getDeterministicCollectorStrategy());
        Assert.assertNotNull(config.getRouteStatisticsCollector());
        Assert.assertNotNull(config.getRouteStatsSelectorStrategy());
    }

    @Test
    public void canUseConstructor() {
        DynamicLoadBalancerConfiguration config = new DynamicLoadBalancerConfiguration(new MeanProcessingTimeProcessorSelectorStrategy(),
                                                                                       new EveryXDeterministicCollectorStrategy(1, 1),
                                                                                       new MBeanRouteStatisticsCollector(new DefaultCamelContext(), Mockito.mock(MBeanServer.class),
                                                                                                                         "route", false, false));

        Assert.assertNotNull(config.getDeterministicCollectorStrategy());
        Assert.assertNotNull(config.getRouteStatisticsCollector());
        Assert.assertNotNull(config.getRouteStatsSelectorStrategy());
    }

    @Test
    public void canUseToString() {
        DynamicLoadBalancerConfiguration config = new DynamicLoadBalancerConfiguration();
        config.setDeterministicCollectorStrategy(new EveryXDeterministicCollectorStrategy(1, 1));
        config.setRouteStatisticsCollector(new MBeanRouteStatisticsCollector(new DefaultCamelContext(), Mockito.mock(MBeanServer.class), "route", false, false));
        config.setRouteStatsSelectorStrategy(new MeanProcessingTimeProcessorSelectorStrategy());

        String answer = config.toString();

        Assert.assertNotNull(answer);
        Assert.assertTrue(answer.contains("routeStatsSelectorStrategy"));
        Assert.assertTrue(answer.contains("deterministicCollectorStrategy"));
        Assert.assertTrue(answer.contains("routeStatisticsCollector"));
    }
}
