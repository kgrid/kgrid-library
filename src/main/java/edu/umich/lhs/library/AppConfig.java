package edu.umich.lhs.library;

import edu.umich.lhs.library.controller.AccountController;
import edu.umich.lhs.library.fedoraGateway.FCRepoService;
import edu.umich.lhs.library.fusekiGateway.FusekiService;
import edu.umich.lhs.library.services.FedoraConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
public class AppConfig {

  @Bean
  public FedoraConfiguration fedoraConfiguration() {
    return new FedoraConfiguration();
  }

  @Bean
  public FCRepoService fcRepoService(FedoraConfiguration fedoraConfiguration) {
    return new FCRepoService(fedoraConfiguration);
  }

  @Bean
  public FusekiService fusekiService(FedoraConfiguration fedoraConfiguration) {
    return new FusekiService(fedoraConfiguration);
  }

  @Bean
  public AccountController accountController() {
    return new AccountController();
  }
}
