package org.uofm.ot.knowledgeObject;

public class FedoraObject {

	private Metadata metadata ;
	
	private String URI;

	private String payload;
	
	private String inputMessage;
	
	private String outputMessage;
	
	private PayloadDescriptor payloadDescriptor;
	
	private String logData; 
	
	public FedoraObject(){}
	
    public FedoraObject(String URI) {
    	this.URI = URI;
    }


	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}
	
	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
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


	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}


	@Override
	public String toString() {
		return "FedoraObject [metadata=" + metadata + ", URI=" + URI + ", payload=" + payload + ", inputMessage="
				+ inputMessage + ", outputMessage=" + outputMessage + ", payloadDescriptor=" + payloadDescriptor
				+ ", logData=" + logData + "]";
	}

}
