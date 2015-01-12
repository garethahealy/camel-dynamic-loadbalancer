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

import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.RouteStatisticsCollector;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.ManagementAgent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseMBeanAttributeCollector implements RouteStatisticsCollector {

    private static final Logger LOG = LoggerFactory.getLogger(BaseMBeanAttributeCollector.class);

    protected String camelContextName;
    private ManagementAgent agent;
    private MBeanServer mBeanServer;

    public BaseMBeanAttributeCollector(CamelContext camelContext, MBeanServer mBeanServer) {
        this.camelContextName = camelContext.getName();
        this.mBeanServer = mBeanServer;
        this.agent = camelContext.getManagementStrategy().getManagementAgent();
    }

    //public abstract List<RouteStatistics> query(Collection<String> routes);

    protected Set<ObjectName> queryNames(String routeName) {
        return mBeanServer.queryNames(createRouteObjectName(routeName), null);
    }

    private ObjectName createRouteObjectName(String route) {
        ObjectName name = null;
        try {
            name = new ObjectName(agent.getMBeanObjectDomainName() + ":type=routes,name=\"" + route + "\",*");
        } catch (MalformedObjectNameException ex) {
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }

        return name;
    }

    protected Long getLongAttribute(ObjectName routeMBean, String attribute) {
        return (Long)getAttribute(routeMBean, attribute);
    }

    protected Integer getIntegerAttribute(ObjectName routeMBean, String attribute) {
        return (Integer)getAttribute(routeMBean, attribute);
    }

    protected String getStringAttribute(ObjectName routeMBean, String attribute) {
        return (String)getAttribute(routeMBean, attribute);
    }

    private Object getAttribute(ObjectName routeMBean, String attributeName) {
        Object attribute = null;
        if (routeMBean != null) {
            try {
                attribute = mBeanServer.getAttribute(routeMBean, attributeName);
            } catch (MBeanException ex) {
                LOG.error(ExceptionUtils.getStackTrace(ex));
            } catch (AttributeNotFoundException ex) {
                LOG.error(ExceptionUtils.getStackTrace(ex));
            } catch (InstanceNotFoundException ex) {
                LOG.error(ExceptionUtils.getStackTrace(ex));
            } catch (ReflectionException ex) {
                LOG.error(ExceptionUtils.getStackTrace(ex));
            }
        }

        return attribute;
    }
}
