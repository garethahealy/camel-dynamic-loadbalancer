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
package com.garethahealy.camel.dynamic.loadbalancer.statistics;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.DeterministicCollectorStrategy;

import org.junit.Assert;
import org.junit.Test;

public class EveryXDeterministicCollectorStrategyTest {

    @Test
    public void shouldCollectIsTrueWhenXIsZero() {
        DeterministicCollectorStrategy collector = new EveryXDeterministicCollectorStrategy(0, 0);

        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue(String.valueOf(i), collector.shouldCollect());
        }
    }

    @Test
    public void shouldCollectIsTrueAfterX() {
        DeterministicCollectorStrategy collector = new EveryXDeterministicCollectorStrategy(100, 1000);

        for (int i = 0; i < 1000; i++) {
            Assert.assertFalse(String.valueOf(i), collector.shouldCollect());
        }

        Assert.assertTrue(collector.shouldCollect());
    }

    @Test
    public void shouldCollectIsTrueAfterWarmup() {
        DeterministicCollectorStrategy collector = new EveryXDeterministicCollectorStrategy(1000, 100);

        for (int i = 0; i < 1000; i++) {
            Assert.assertFalse(String.valueOf(i), collector.shouldCollect());
        }

        Assert.assertTrue(collector.shouldCollect());
    }
}
