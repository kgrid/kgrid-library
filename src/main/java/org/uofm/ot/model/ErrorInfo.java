package org.uofm.ot.model;


/**
 * A wrapper so that errors can be serialized and returned in correct json format
 * Created by nggittle on 7/12/17.
 */
public class ErrorInfo {

  private String ex;

  private String exMessage;

  private String url;

  public ErrorInfo(String ex, String exMessage, String url) {
    this.ex = ex;
    this.exMessage = exMessage;
    this.url = url;
  }
}