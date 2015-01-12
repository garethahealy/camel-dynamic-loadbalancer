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
package com.garethahealy.camel.dynamic.loadbalancer.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MeanProcessingTimeProcessorSelectorStrategyTest {

    @Test
    public void getProcessorIndexCorrect() {
        Map<String, Integer> routeNamesAndProcessors = new HashMap<String, Integer>();
        routeNamesAndProcessors.put("route1", 0);
        routeNamesAndProcessors.put("route2", 1);

        RouteStatistics stat1 = new RouteStatistics();
        stat1.setRouteName("route1");
        stat1.setMeanProcessingTime(12345L);

        RouteStatistics stat2 = new RouteStatistics();
        stat2.setRouteName("route2");
        stat2.setMeanProcessingTime(54321L);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        stats.add(stat1);
        stats.add(stat2);

        MeanProcessingTimeProcessorSelectorStrategy strategy = new MeanProcessingTimeProcessorSelectorStrategy(routeNamesAndProcessors);
        int index = strategy.getBestProcessorIndex(stats);

        Assert.assertEquals(0, index);
    }

    @Test
    public void getProcessorIndexCorrectWithMultpleStats() {
        Map<String, Integer> routeNamesAndProcessors = new HashMap<String, Integer>();
        routeNamesAndProcessors.put("route1", 0);
        routeNamesAndProcessors.put("route2", 1);
        routeNamesAndProcessors.put("route3", 2);

        RouteStatistics stat1 = new RouteStatistics();
        stat1.setRouteName("route1");
        stat1.setMeanProcessingTime(12345L);

        RouteStatistics stat2 = new RouteStatistics();
        stat2.setRouteName("route2");
        stat2.setMeanProcessingTime(1L);

        RouteStatistics stat3 = new RouteStatistics();
        stat3.setRouteName("route3");
        stat3.setMeanProcessingTime(54321L);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        stats.add(stat1);
        stats.add(stat2);
        stats.add(stat3);

        MeanProcessingTimeProcessorSelectorStrategy strategy = new MeanProcessingTimeProcessorSelectorStrategy(routeNamesAndProcessors);
        int index = strategy.getBestProcessorIndex(stats);

        Assert.assertEquals(1, index);
    }
}
