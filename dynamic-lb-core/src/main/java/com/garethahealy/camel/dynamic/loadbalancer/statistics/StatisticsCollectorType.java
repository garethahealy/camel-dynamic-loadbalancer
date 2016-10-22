/*
 * #%L
 * GarethHealy :: Camel Dynamic LoadBalance :: Core
 * %%
 * Copyright (C) 2013 - 2016 Gareth Healy
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

/**
 * Type of object to collect stats on
 */
public enum StatisticsCollectorType {

    ROUTE("route"),
    PROCESSOR("processor"),
    ALL_ROUTES("allroutes");

    private final String value;

    StatisticsCollectorType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static StatisticsCollectorType fromValue(String value) {
        for (StatisticsCollectorType item : StatisticsCollectorType.values()) {
            if (item.value.equalsIgnoreCase(value)) {
                return item;
            }
        }

        throw new UnsupportedOperationException("StatisticsCollectorType '" + value + "' is not supported");
    }
}
