package org.uofm.ot.knowledgeObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FedoraObject {

	private Metadata metadata ;
	
	private String URI;
	
	private String inputMessage;
	
	private String outputMessage;
	
	private Payload payload = new Payload();
	
	private String logData;
	
	@JsonIgnore
	private ArkId arkId;
	
	

	public FedoraObject(ArkId arkId) {
		this.arkId = arkId;
	}

	public FedoraObject(){}
	
    public FedoraObject(String URI) {
    	this.URI = URI;
    }

	public String getURI() {

		return arkId.getArkId();
	}

	public void setURI(String uRI) {
		URI = uRI;
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


	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "FedoraObject [metadata=" + metadata + ", URI=" + URI + ", inputMessage=" + inputMessage
				+ ", outputMessage=" + outputMessage + ", payload=" + payload + ", logData=" + logData + "]";
	}

	public ArkId getArkId() {
		return arkId;
	}

	public void setArkId(ArkId arkId) {
		this.arkId = arkId;
	}


}
