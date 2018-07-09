/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
 * %%
 * Copyright (C) 2013 - 2018 Gareth Healy
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
package com.garethahealy.camel.dynamic.loadbalancer.statistics.strategy;

import java.util.List;

import com.garethahealy.camel.dynamic.loadbalancer.statistics.RouteStatistics;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Strategy to determine how stats should be collected
 */
public interface RouteStatisticsCollector {

    List<RouteStatistics> query(List<Processor> processors, Exchange exchange);
}
