package org.uofm.ot.FedoraAccessLayer;

import org.uofm.ot.services.FedoraConfiguration;

/**
 * Created by nggittle on 4/19/17.
 */
public class FedoraConfigurationBuilder {

  private FedoraConfiguration fconfig;

  public FedoraConfigurationBuilder() {
    fconfig = new FedoraConfiguration();
  }

  public FedoraConfigurationBuilder fcRepoUrl(String fcRepoUrl) {
    fconfig.setFcrepoUrl(fcRepoUrl);
    return this;
  }

  public FedoraConfigurationBuilder fusekiUrl(String fusekiUrl) {
    fconfig.setFusekiUrl(fusekiUrl);
    return this;
  }

  public FedoraConfigurationBuilder fcrepoUsername(String fcrepoUsername) {
    fconfig.setFcrepoUsername(fcrepoUsername);
    return this;
  }

  public FedoraConfigurationBuilder fcrepoPassword(String fcrepoPassword) {
    fconfig.setFcrepoPassword(fcrepoPassword);
    return this;
  }

  public FedoraConfiguration build() {
    return fconfig;
  }

}
