/*
 * #%L
 * lb-example
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
package com.garethahealy.camel.dynamic.loadbalancer.example1.routes;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class DoesLoadBalance extends BaseCamelBlueprintTestSupport {

    @Produce(uri = "direct:start")
    private ProducerTemplate startProducerTemplate;

    @EndpointInject(uri = "mock:readerOneEnd")
    private MockEndpoint mock1;

    @EndpointInject(uri = "mock:readerTwoEnd")
    private MockEndpoint mock2;

    @EndpointInject(uri = "mock:readerThreeEnd")
    private MockEndpoint mock3;

    @Test
    public void can() throws InterruptedException {
        mock1.expectedMessageCount(1);
        mock1.expectedBodiesReceived("Got message1 from readerOne");

        mock2.expectedMessageCount(1);
        mock2.expectedBodiesReceived("Got message2 from readerTwo");

        mock3.expectedMessageCount(1);
        mock3.expectedBodiesReceived("Got message3 from readerThree");

        startProducerTemplate.sendBody("message1");
        startProducerTemplate.sendBody("message2");
        startProducerTemplate.sendBody("message3");

        mock1.assertIsSatisfied();
        mock2.assertIsSatisfied();
        mock3.assertIsSatisfied();
    }
}
