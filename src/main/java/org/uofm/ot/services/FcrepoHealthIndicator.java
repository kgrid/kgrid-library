package org.uofm.ot.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.uofm.ot.fedoraGateway.FCRepoService;

@Component
public class FcrepoHealthIndicator extends AbstractHealthIndicator {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    FedoraConfiguration fedoraConfiguration;

    @Autowired
    FCRepoService fcrepoService;

    @Value("${library.name:ObjectTeller3000}")
    String libraryName;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder
                .withDetail("library.name", libraryName)
                .withDetail("library.fusekiUrl", fedoraConfiguration.getFusekiServerConfiguration().getUrl())
                .withDetail("library.fedoraUrl", fedoraConfiguration.getFedoraServerConfiguration().getUrl())
                .up();

        try {
            fcrepoService.ping();
        } catch (Exception e){
            builder.down(e);
        }
    }
}