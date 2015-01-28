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

import org.junit.Assert;
import org.junit.Test;

public class StatisticsCollectorTypeTest {

    @Test
    public void canGetValue() {
        StatisticsCollectorType type = StatisticsCollectorType.ALL_ROUTES;

        Assert.assertNotNull(type.value());
        Assert.assertTrue(type.value().length() > 0);
    }

    @Test
    public void fromValueReturnsItem() {
        StatisticsCollectorType type = StatisticsCollectorType.fromValue("route");

        Assert.assertEquals(StatisticsCollectorType.ROUTE, type);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fromValueThrowsExceptionForMissing() {
        StatisticsCollectorType.fromValue("route1");
    }
}
