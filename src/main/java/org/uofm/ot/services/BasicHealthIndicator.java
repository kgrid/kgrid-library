package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class BasicHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    FedoraConfiguration fedoraConfiguration;

    @Value("${library.name:ObjectTeller3000}")
    String libraryName;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder
            .withDetail("library.name", libraryName)
            .withDetail("library.fusekiUrl", fedoraConfiguration.getFusekiServerConfiguration().getUrl())
            .withDetail("library.fedoraUrl", fedoraConfiguration.getFedoraServerConfiguration().getUrl())
            .up()
//            .withException(new IndexOutOfBoundsException("Uh-oh!"))
        ;
    }
}