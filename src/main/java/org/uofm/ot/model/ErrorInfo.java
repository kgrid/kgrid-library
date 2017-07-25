package org.uofm.ot.model;


import java.util.Date;

/**
 * A wrapper so that errors can be serialized and returned in correct json format
 * Created by nggittle on 7/12/17.
 */
public class ErrorInfo {

  private String message;

  private String url;

  private String timestamp;

  public ErrorInfo(String exMessage, String url, String timestamp) {
    this.message = exMessage;
    this.url = url;
    this.timestamp = timestamp;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}