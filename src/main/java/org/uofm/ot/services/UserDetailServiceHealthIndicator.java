package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.uofm.ot.CustomizedUserManager;

@Component
public class UserDetailServiceHealthIndicator extends AbstractHealthIndicator {

    @Autowired
    private CustomizedUserManager userDetailService;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        try {
            int numberOfUsers = userDetailService.ping();
            builder
                    .withDetail("number of users", numberOfUsers)
                    .up();
        } catch (Exception e) {
            builder
                    .down(e);
        }
    }
}
