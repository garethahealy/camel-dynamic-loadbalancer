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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.ProcessorSelectorStrategy;

import org.apache.camel.Processor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MeanProcessingTimeProcessorSelectorStrategyTest {

    @Test
    public void getProcessorIndexCorrect() {
        ProcessorHolder processorHolder1 = new ProcessorHolder();
        processorHolder1.setProcessor(Mockito.mock(Processor.class));

        ProcessorHolder processorHolder2 = new ProcessorHolder();
        processorHolder2.setProcessor(Mockito.mock(Processor.class));

        RouteStatistics stat1 = new RouteStatistics();
        stat1.setProcessorHolder(processorHolder1);
        stat1.setMeanProcessingTime(12345L);

        RouteStatistics stat2 = new RouteStatistics();
        stat2.setProcessorHolder(processorHolder2);
        stat2.setMeanProcessingTime(54321L);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        stats.add(stat1);
        stats.add(stat2);

        List<Processor> processors = new LinkedList<Processor>();
        processors.add(processorHolder1.getProcessor());
        processors.add(processorHolder2.getProcessor());

        ProcessorSelectorStrategy strategy = new MeanProcessingTimeProcessorSelectorStrategy();
        Processor processor = strategy.getProcessor(stats);

        Assert.assertNotNull(processor);
    }

    @Test
    public void getProcessorIndexCorrectWithMultpleStats() {
        ProcessorHolder processorHolder1 = new ProcessorHolder();
        processorHolder1.setProcessor(Mockito.mock(Processor.class));

        ProcessorHolder processorHolder2 = new ProcessorHolder();
        processorHolder2.setProcessor(Mockito.mock(Processor.class));

        ProcessorHolder processorHolder3 = new ProcessorHolder();
        processorHolder3.setProcessor(Mockito.mock(Processor.class));

        RouteStatistics stat1 = new RouteStatistics();
        stat1.setProcessorHolder(processorHolder1);
        stat1.setMeanProcessingTime(12345L);

        RouteStatistics stat2 = new RouteStatistics();
        stat2.setProcessorHolder(processorHolder2);
        stat2.setMeanProcessingTime(1L);

        RouteStatistics stat3 = new RouteStatistics();
        stat3.setProcessorHolder(processorHolder3);
        stat3.setMeanProcessingTime(54321L);

        List<RouteStatistics> stats = new ArrayList<RouteStatistics>();
        stats.add(stat1);
        stats.add(stat2);
        stats.add(stat3);

        List<Processor> processors = new LinkedList<Processor>();
        processors.add(processorHolder1.getProcessor());
        processors.add(processorHolder2.getProcessor());
        processors.add(processorHolder3.getProcessor());

        ProcessorSelectorStrategy strategy = new MeanProcessingTimeProcessorSelectorStrategy();
        Processor processor = strategy.getProcessor(stats);

        Assert.assertNotNull(processor);
    }
}
