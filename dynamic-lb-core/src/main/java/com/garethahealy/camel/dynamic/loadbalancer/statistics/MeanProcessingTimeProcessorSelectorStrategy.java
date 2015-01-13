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

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy.ProcessorSelectorStrategy;

import org.apache.camel.Processor;

public class MeanProcessingTimeProcessorSelectorStrategy implements ProcessorSelectorStrategy {

    @Override
    public Processor getProcessor(List<RouteStatistics> stats) {
        RouteStatistics best = null;
        for (RouteStatistics current : stats) {
            if (best == null) {
                best = current;
            }

            best = best.getMeanProcessingTime() <= current.getMeanProcessingTime() ? best : current;
        }

        return best.getProcessorHolder().getProcessor();
    }

    @Override
    public List<Integer> getWeightedProcessors(List<RouteStatistics> stats) {
        Collections.sort(stats, new RouteStatisticsComparator());

        //todo: need to check....
        List<Integer> indexes = new LinkedList<Integer>();
        for (int i = stats.size(); i > stats.size(); i--) {
            indexes.add(i);
        }

        return indexes;
    }

    private static class RouteStatisticsComparator implements Comparator<RouteStatistics>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final RouteStatistics stat1, RouteStatistics stat2) {
            return stat1.getMeanProcessingTime().compareTo(stat2.getMeanProcessingTime());
        }
    }
}
