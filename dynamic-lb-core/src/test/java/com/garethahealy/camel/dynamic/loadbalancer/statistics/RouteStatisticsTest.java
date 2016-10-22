/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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

import org.junit.Assert;
import org.junit.Test;

public class RouteStatisticsTest {

    @Test
    public void canSetRouteName() {
        ProcessorHolder holder = new ProcessorHolder();

        RouteStatistics stat = new RouteStatistics();
        stat.setProcessorHolder(holder);

        Assert.assertNotNull(stat.getProcessorHolder());
        Assert.assertEquals(holder, stat.getProcessorHolder());
    }

    @Test
    public void canSetInflightExchange() {
        RouteStatistics stat = new RouteStatistics();
        stat.setInflightExchange(1);

        Assert.assertNotNull(stat.getInflightExchange());
        Assert.assertEquals(new Integer(1), stat.getInflightExchange());
    }

    @Test
    public void canSetMeanProcessingTime() {
        RouteStatistics stat = new RouteStatistics();
        stat.setMeanProcessingTime(2L);

        Assert.assertNotNull(stat.getMeanProcessingTime());
        Assert.assertEquals(new Long(2L), stat.getMeanProcessingTime());
    }

    @Test
    public void canSetLastProcessingTime() {
        RouteStatistics stat = new RouteStatistics();
        stat.setLastProcessingTime(3L);

        Assert.assertNotNull(stat.getLastProcessingTime());
        Assert.assertEquals(new Long(3L), stat.getLastProcessingTime());
    }

    @Test
    public void canSetLoad01() {
        RouteStatistics stat = new RouteStatistics();
        stat.setLoad01("1");

        Assert.assertNotNull(stat.getLoad01());
        Assert.assertEquals("1", stat.getLoad01());
    }

    @Test
    public void canSetLoad05() {
        RouteStatistics stat = new RouteStatistics();
        stat.setLoad05("5");

        Assert.assertNotNull(stat.getLoad05());
        Assert.assertEquals("5", stat.getLoad05());
    }

    @Test
    public void canSetLoad15() {
        RouteStatistics stat = new RouteStatistics();
        stat.setLoad15("15");

        Assert.assertNotNull(stat.getLoad15());
        Assert.assertEquals("15", stat.getLoad15());
    }
}
