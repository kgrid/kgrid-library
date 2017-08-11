package edu.umich.lhs.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class EzidServiceHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private IdService idService;

    @Value("${ezid.base.url}")
    String ezidBaseUrl;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder
            .withDetail("ezid.base.url", ezidBaseUrl)
            .up();
        try {
            idService.ping();
        } catch (Exception e) {
            builder
                    .down(e);
        }
    }
}
