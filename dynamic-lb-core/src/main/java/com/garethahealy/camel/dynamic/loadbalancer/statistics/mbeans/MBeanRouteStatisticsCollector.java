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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;

import org.apache.camel.CamelContext;

public class MBeanRouteStatisticsCollector extends BaseMBeanAttributeCollector {

    public MBeanRouteStatisticsCollector(CamelContext camelContext, MBeanServer mBeanServer) {
        super(camelContext, mBeanServer);
    }

    public List<RouteStatistics> query(Set<String> routes) {
        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        for (String routeName : routes) {
            stats.add(query(routeName));
        }

        return stats;
    }

    private RouteStatistics query(String routeName) {
        RouteStatistics stats = null;

        Set<ObjectName> set = queryNames(routeName);
        Iterator<ObjectName> iterator = set.iterator();
        if (iterator.hasNext()) {
            ObjectName routeMBean = iterator.next();

            String camelId = getStringAttribute(routeMBean, "CamelId");
            if (camelId != null && camelId.equals(camelContextName)) {
                stats = new RouteStatistics();
                stats.setRouteName(routeName);
                stats.setInflightExchange(getIntegerAttribute(routeMBean, "InflightExchanges"));
                stats.setMeanProcessingTime(getLongAttribute(routeMBean, "MeanProcessingTime"));
                stats.setLastProcessingTime(getLongAttribute(routeMBean, "LastProcessingTime"));
                stats.setLoad01(getStringAttribute(routeMBean, "Load01"));
                stats.setLoad05(getStringAttribute(routeMBean, "Load05"));
                stats.setLoad15(getStringAttribute(routeMBean, "Load15"));
            }
        }

        return stats;
    }
}
