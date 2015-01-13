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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;

import org.apache.camel.Processor;
import org.apache.camel.management.DefaultManagementAgent;
import org.apache.camel.management.DefaultManagementStrategy;
import org.apache.camel.test.junit4.ExchangeTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MBeanRouteStatisticsCollectorTest extends ExchangeTestSupport {

    private void setupManagementStrategy() {
        DefaultManagementAgent managementAgent = new DefaultManagementAgent(context);
        managementAgent.setMBeanObjectDomainName("org.apache");

        DefaultManagementStrategy managementStrategy = new DefaultManagementStrategy(context);
        managementStrategy.setManagementAgent(managementAgent);

        context.setManagementStrategy(managementStrategy);
    }

    @Test
    public void queryGets1ValueBack() throws MalformedObjectNameException, MBeanException, AttributeNotFoundException,
        InstanceNotFoundException, ReflectionException {
        setupManagementStrategy();

        Map<String, Integer> routeNamesAndProcessors = new HashMap<String, Integer>();
        routeNamesAndProcessors.put("route1", 0);

        Set<ObjectName> objectNames = new HashSet<ObjectName>();
        objectNames.add(new ObjectName(""));

        MBeanServer mBeanServerMocked = Mockito.mock(MBeanServer.class);
        Mockito.when(mBeanServerMocked.queryNames(Mockito.any(ObjectName.class), Mockito.any(QueryExp.class))).thenReturn(objectNames);
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("CamelId"))).thenReturn(context.getName());
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("InflightExchanges"))).thenReturn(1);
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("MeanProcessingTime"))).thenReturn(20L);
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("LastProcessingTime"))).thenReturn(30L);
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("Load01"))).thenReturn("1");
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("Load05"))).thenReturn("5");
        Mockito.when(mBeanServerMocked.getAttribute(Mockito.any(ObjectName.class), Mockito.eq("Load15"))).thenReturn("15");

        List<Processor> processors = new LinkedList<Processor>();
        processors.add(Mockito.mock(Processor.class));
        processors.add(Mockito.mock(Processor.class));

        MBeanRouteStatisticsCollector collector = new MBeanRouteStatisticsCollector(context, mBeanServerMocked, "route");
        List<RouteStatistics> stats = collector.query(processors, createExchange());

        Assert.assertNotNull(stats);
        Assert.assertEquals(1, stats.size());

        RouteStatistics first = stats.get(0);
        Assert.assertEquals(new Long(1), new Long(first.getInflightExchange()));
        Assert.assertEquals(new Long(20L), new Long(first.getMeanProcessingTime()));
        Assert.assertEquals(new Long(30L), new Long(first.getLastProcessingTime()));
        Assert.assertEquals("1", first.getLoad01());
        Assert.assertEquals("5", first.getLoad05());
        Assert.assertEquals("15", first.getLoad15());
    }
}
