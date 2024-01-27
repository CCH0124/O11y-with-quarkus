package org.cch.router;

import org.apache.camel.builder.RouteBuilder;

public class KubernetesEventRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:list").
            to("kubernetes-events:///?kubernetesClient=#kubernetesClient&operation=listEvents")
            .log("${body}")
            .end();
    }
    
}
