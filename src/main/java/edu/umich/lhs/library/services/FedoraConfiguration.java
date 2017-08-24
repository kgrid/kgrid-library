package edu.umich.lhs.library.services;

import edu.umich.lhs.library.model.ServerDetails;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by nggittle on 4/11/17.
 */
@Configuration
@ConfigurationProperties(prefix = "fedora")
public class FedoraConfiguration {

  private String fcrepoUrl;
  private String fusekiUrl;
  private String fcrepoUsername;
  private String fcrepoPassword;
  private String fusekiUsername;
  private String fusekiPassword;


  public void setFcrepoUrl(String fcrepoUrl) {
    this.fcrepoUrl = fcrepoUrl;
  }

  public void setFusekiUrl(String fusekiUrl) {
    this.fusekiUrl = fusekiUrl;
  }

  public void setFcrepoUsername(String fcrepoUsername) {
    this.fcrepoUsername = fcrepoUsername;
  }

  public void setFcrepoPassword(String fcrepoPassword) {
    this.fcrepoPassword = fcrepoPassword;
  }

  public void setFusekiUsername(String fusekiUsername) {
    this.fusekiUsername = fusekiUsername;
  }

  public void setFusekiPassword(String fusekiPassword) {
    this.fusekiPassword = fusekiPassword;
  }


  public ServerDetails getFedoraServerConfiguration() {
    ServerDetails fedoraConf = new ServerDetails(fcrepoUrl,
        fcrepoUsername, fcrepoPassword);

    return fedoraConf;
  }

  public ServerDetails getFusekiServerConfiguration() {
    ServerDetails fusekiConf = new ServerDetails(fusekiUrl,
        fusekiUsername, fusekiPassword);

    return fusekiConf;
  }



}
