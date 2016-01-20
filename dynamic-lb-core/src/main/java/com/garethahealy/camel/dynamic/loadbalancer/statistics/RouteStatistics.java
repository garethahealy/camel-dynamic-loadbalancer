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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Stats on a route
 */
public class RouteStatistics {

    private ProcessorHolder processorHolder;
    private Integer inflightExchange;
    private Long meanProcessingTime;
    private Long lastProcessingTime;
    private String load01;
    private String load05;
    private String load15;

    public ProcessorHolder getProcessorHolder() {
        return processorHolder;
    }

    public void setProcessorHolder(ProcessorHolder processorHolder) {
        this.processorHolder = processorHolder;
    }

    public Integer getInflightExchange() {
        return inflightExchange;
    }

    public void setInflightExchange(Integer inflightExchange) {
        this.inflightExchange = inflightExchange;
    }

    public Long getMeanProcessingTime() {
        return meanProcessingTime;
    }

    public void setMeanProcessingTime(Long meanProcessingTime) {
        this.meanProcessingTime = meanProcessingTime;
    }

    public Long getLastProcessingTime() {
        return lastProcessingTime;
    }

    public void setLastProcessingTime(Long lastProcessingTime) {
        this.lastProcessingTime = lastProcessingTime;
    }

    public String getLoad01() {
        return load01;
    }

    public void setLoad01(String load01) {
        this.load01 = load01;
    }

    public String getLoad05() {
        return load05;
    }

    public void setLoad05(String load05) {
        this.load05 = load05;
    }

    public String getLoad15() {
        return load15;
    }

    public void setLoad15(String load15) {
        this.load15 = load15;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("processorHolder", processorHolder)
            .append("inflightExchange", inflightExchange)
            .append("meanProcessingTime", meanProcessingTime)
            .append("lastProcessingTime", lastProcessingTime)
            .append("load01", load01)
            .append("load05", load05)
            .append("load15", load15)
            .toString();
    }
}
