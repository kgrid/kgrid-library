package org.kgrid.library.services;

import org.kgrid.library.CustomizedUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

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
