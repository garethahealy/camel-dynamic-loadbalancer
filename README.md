camel-dynamic-loadbalancer
=============================
Sample code which implements a dynamic load balancer.

Idea
=====
- The idea is to use the RoundRobinLoadBalancer, but select the processor based on stats collected on the routes
- Stats will be collected via MBeans - thus work across CamelContexts
- Stats only collected based on a collection strategy, i.e.: we dont spam MBeans
- Strategy to define which stat is regarded as the "best", which then returns the processor

Very much work in progress...
