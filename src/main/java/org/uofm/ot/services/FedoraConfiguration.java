package org.uofm.ot.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.uofm.ot.model.ServerDetails;

/**
 * Created by nggittle on 4/11/17.
 */
@ConfigurationProperties(prefix = "fedora")
public class FedoraConfiguration {

  private String fcrepoUrl;
  private String fusekiUrl;
  private String fcrepoUsername;
  private String fcrepoPassword;
  private String fusekiUsername;
  private String fusekiPassword;
  private String fusekiPrefix;


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

  public void setFusekiPrefix(String fusekiPrefix) {
    this.fusekiPrefix = fusekiPrefix;
  }

  public ServerDetails getFedoraServerConfiguration() {
    ServerDetails fedoraConf = new ServerDetails(fcrepoUrl,
        fcrepoUsername, fcrepoPassword, null);

    return fedoraConf;
  }

  public ServerDetails getFusekiServerConfiguration() {
    ServerDetails fusekiConf = new ServerDetails(fusekiUrl,
        fusekiUsername, fusekiPassword, fusekiPrefix);

    return fusekiConf;
  }



}
