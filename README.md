[![Build Status](https://travis-ci.org/garethahealy/camel-dynamic-loadbalancer.svg?branch=master)](https://travis-ci.org/garethahealy/camel-dynamic-loadbalancer)

# camel-dynamic-loadbalancer
Sample code which implements a dynamic load balancer within camel

# Idea
- Extend the RoundRobinLoadBalancer, but select the processor based on stats collected
- Extend the WeightedRoundRobinLoadBalancer, but set the weights based on stats collected

Provide the following strategies so that the load balancer can be adapted for different needs:
- DeterministicCollectorStrategy : if the stats should be collected
- RouteStatisticsCollector : how the stats are collected
- ProcessorSelectorStrategy : what processor is selected based on the stats

# Implementation of Strategies
- DeterministicCollectorStrategy is implemented by EveryXDeterministicCollectorStrategy
- RouteStatisticsCollector is implemented by MBeanRouteStatisticsCollector
- ProcessorSelectorStrategy is implemented by MeanProcessingTimeProcessorSelectorStrategy

# Implementation of LBs
1. DynamicRoundRobinLoadBalancer
2. DynamicWeightedRoundRobinLoadBalancer

# Limitations
1. MBeanRouteStatisticsCollector.getUriFromProcessor - this method uses reflection, needs improving.
2. If we cant match the Processor -> Route in MBeanRouteStatisticsCollector, we fail fast. But what if its in another bundle/machine that has not started?
2. We can only get stats on routes on the same machine due to MBeans. Above point would cause a failure

# Build and Install
- mvn clean install
- features:addurl mvn:com.garethahealy.camel/dynamic-lb-features/1.0.0-SNAPSHOT/xml/features
- features:install com.garethahealy.camel-dynamic-lb-core

And then either of the below:
- features:install com.garethahealy.camel-dynamic-lb-examples1
- features:install com.garethahealy.camel-dynamic-lb-examples2

