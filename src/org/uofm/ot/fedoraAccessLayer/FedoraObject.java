package org.uofm.ot.fedoraAccessLayer;

import java.util.Date;

public class FedoraObject {

	private String title;
	
	private String owner;
	
	private String description;
	
	private String contributors;
	
	private String keywords; 
	
	private boolean published;
	
	private String URI;
	
	private Date lastModified;
	
	private Date createdOn;
	
	private String objectType;

	private String payload;
	
	private String inputMessage;
	
	private String outputMessage;
	
	private PayloadDescriptor payloadDescriptor;
	
	private String logData; 
	
	public FedoraObject(){}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContributors() {
		return contributors;
	}

	public void setContributors(String contributors) {
		this.contributors = contributors;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}


	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public PayloadDescriptor getPayloadDescriptor() {
		return payloadDescriptor;
	}

	public void setPayloadDescriptor(PayloadDescriptor payloadDescriptor) {
		this.payloadDescriptor = payloadDescriptor;
	}

	public String getInputMessage() {
		return inputMessage;
	}

	public void setInputMessage(String inputMessage) {
		this.inputMessage = inputMessage;
	}

	public String getOutputMessage() {
		return outputMessage;
	}

	public void setOutputMessage(String outputMessage) {
		this.outputMessage = outputMessage;
	}
	

	public String getLogData() {
		return logData;
	}

	public void setLogData(String logData) {
		this.logData = logData;
	}

	@Override
	public String toString() {
		return "FedoraObject [title=" + title + ", owner=" + owner + ", description=" + description + ", contributors="
				+ contributors + ", keywords=" + keywords + ", published=" + published + ", URI=" + URI
				+ ", lastModified=" + lastModified + ", createdOn=" + createdOn + ", objectType=" + objectType
				+ ", payload=" + payload + ", inputMessage=" + inputMessage + ", outputMessage=" + outputMessage
				+ ", payloadDescriptor=" + payloadDescriptor + ", logData=" + logData + "]";
	}

	


}
