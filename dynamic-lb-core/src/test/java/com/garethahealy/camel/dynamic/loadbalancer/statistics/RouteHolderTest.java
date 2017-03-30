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
package com.garethahealy.camel.dynamic.loadbalancer.statistics;

import org.junit.Assert;
import org.junit.Test;

public class RouteHolderTest {

    @Test
    public void canSetCamelContextName() {
        RouteHolder holder = new RouteHolder();
        holder.setCamelContextName("context");

        Assert.assertNotNull(holder.getCamelContextName());
        Assert.assertEquals("context", holder.getCamelContextName());
    }

    @Test
    public void canSetRouteName() {
        RouteHolder holder = new RouteHolder();
        holder.setRouteName("route");

        Assert.assertNotNull(holder.getRouteName());
        Assert.assertEquals("route", holder.getRouteName());
    }

    @Test
    public void canSetUri() {
        RouteHolder holder = new RouteHolder();
        holder.setUri("uri");

        Assert.assertNotNull(holder.getUri());
        Assert.assertEquals("uri", holder.getUri());
    }

    @Test
    public void canUseToString() {
        RouteHolder holder = new RouteHolder();
        holder.setCamelContextName("context");
        holder.setRouteName("route");
        holder.setUri("uri");

        String answer = holder.toString();

        Assert.assertNotNull(answer);
        Assert.assertTrue(answer.contains("camelContextName"));
        Assert.assertTrue(answer.contains("routeName"));
        Assert.assertTrue(answer.contains("uri"));
    }
}
