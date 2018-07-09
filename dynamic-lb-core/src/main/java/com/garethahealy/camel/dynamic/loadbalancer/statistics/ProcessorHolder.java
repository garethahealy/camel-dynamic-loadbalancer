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
package com.garethahealy.camel.dynamic.loadbalancer.statistics;

import org.apache.camel.Processor;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Information about a processor
 */
public class ProcessorHolder {

    private Processor processor;
    private String uri;
    private String camelContextName;
    private String routeName;

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCamelContextName() {
        return camelContextName;
    }

    public void setCamelContextName(String camelContextName) {
        this.camelContextName = camelContextName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("processor", processor)
            .append("uri", uri)
            .append("camelContextName", camelContextName)
            .append("routeName", routeName)
            .toString();
    }
}
