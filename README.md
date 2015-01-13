camel-dynamic-loadbalancer
=============================
Sample code which implements a dynamic load balancer.

Idea
=====
- The idea is to use the RoundRobinLoadBalancer, but select the processor based on stats collected on the routes
- Stats will be collected via MBeans - currently only works in same CamelContext, but could be expanded
- Stats only collected based on a collection strategy, i.e.: we dont spam MBeans
- Strategy to define which stat is regarded as the "best", which then returns the processor

Very much work in progress...

MessageHelper.dumpMessageHistoryStacktrace(exchange, new DefaultExchangeFormatter(), false)


limitations
==========
stats work on routes, but what if we write to a file or use a cxf endpoint? can the collector work off different things?

how the link routes/processors -> to stats??
when we collect the stats, maybe we can get the FROM uri...which means we have the stats for a route, and know the from uri,
which means we can select the to uri, based on the processor list....?

----- or
we go with the index route...but this is not 100% guarnteed to be ordered, id of thought not anyways


