package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class EzidServiceHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private IdService idService;

    @Value("${EZID_BASE_URL}")
    String ezidBaseUrl;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder
            .withDetail("Ezid Base Url", ezidBaseUrl)
            .up();
        try {
            idService.ping();
        } catch (Exception e) {
            builder
                    .down(e);
        }
    }
}
