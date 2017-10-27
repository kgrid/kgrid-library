package edu.umich.lhs.library.ezidGateway;

import edu.umich.lhs.library.knowledgeObject.ArkId;
import java.net.URI;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DummyIdService extends EzidService {

  @Value(value = "${naan}")
  private String naan;

  @Value(value = "${local.shoulder}")
  private String shoulder;

  public String mint() {
    String id = new Date().getTime() + "";
    String arkId = "ark:/" + naan + "/" + shoulder + id;
    return arkId;
  }

  public void create(String arkId) {

  }

  String modify(String id, List<String> metadata) {
    return id;
  }

  public String bind(String id, List<String> metadata, URI objectURL) {
    return id;
  }

  public String status(String id, List<String> metadata, ArkId.Status status) {
    return id;
  }
}
