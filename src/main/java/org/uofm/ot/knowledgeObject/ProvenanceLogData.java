package org.uofm.ot.knowledgeObject;

import com.complexible.pinto.Identifiable;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.impl.IdentifableImpl;
import java.util.Date;
import org.openrdf.model.Resource;

/**
 * Created by nggittle on 5/11/17.
 */

public class ProvenanceLogData implements Identifiable {

  private Identifiable mIdentifiable = new IdentifableImpl();

  private String wasAttributedTo;

  private String wasGeneratedBy;

  private String wasAssociatedWith;

  private String used;

  private Date startedAtTime;

  private Date endedAtTime;

  public ProvenanceLogData(String wasAttributedTo, String wasGeneratedBy,
      String wasAssociatedWith, String used, Date startedAtTime, Date endedAtTime) {
    this.wasAttributedTo = wasAttributedTo;
    this.wasGeneratedBy = wasGeneratedBy;
    this.wasAssociatedWith = wasAssociatedWith;
    this.used = used;
    this.startedAtTime = startedAtTime;
    this.endedAtTime = endedAtTime;
  }

  @RdfProperty("prov:wasAttributedTo")
  public String getWasAttributedTo() {
    return wasAttributedTo;
  }

  public void setWasAttributedTo(String wasAttributedTo) {
    this.wasAttributedTo = wasAttributedTo;
  }

  @RdfProperty("prov:wasGeneratedBy")
  public String getWasGeneratedBy() {
    return wasGeneratedBy;
  }

  public void setWasGeneratedBy(String wasGeneratedBy) {
    this.wasGeneratedBy = wasGeneratedBy;
  }

  @RdfProperty("prov:wasAssociatedWith")
  public String getWasAssociatedWith() {
    return wasAssociatedWith;
  }

  public void setWasAssociatedWith(String wasAssociatedWith) {
    this.wasAssociatedWith = wasAssociatedWith;
  }

  @RdfProperty("prov:used")
  public String getUsed() {
    return used;
  }

  public void setUsed(String used) {
    this.used = used;
  }

  @RdfProperty("prov:startedAtTime")
  public Date getStartedAtTime() {
    return startedAtTime;
  }

  public void setStartedAtTime(Date startedAtTime) {
    this.startedAtTime = startedAtTime;
  }

  @RdfProperty("prov:endedAtTime")
  public Date getEndedAtTime() {
    return endedAtTime;
  }

  public void setEndedAtTime(Date endedAtTime) {
    this.endedAtTime = endedAtTime;
  }

  @Override
  public String toString() {
    return "ProvenanceLogData{" +
        "wasAttributedTo='" + wasAttributedTo + '\'' +
        ", wasGeneratedBy='" + wasGeneratedBy + '\'' +
        ", wasAssociatedWith='" + wasAssociatedWith + '\'' +
        ", used='" + used + '\'' +
        ", startedAtTime=" + startedAtTime +
        ", endedAtTime=" + endedAtTime +
        '}';
  }

  @Override
  public Resource id() {
    return mIdentifiable.id();
  }

  @Override
  public void id(final Resource resource) {
    mIdentifiable.id(resource);
  }

}
