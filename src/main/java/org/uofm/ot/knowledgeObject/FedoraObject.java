package org.uofm.ot.knowledgeObject;

public class FedoraObject {

	private Metadata metadata ;
	
	private String URI;
	
	private String inputMessage;
	
	private String outputMessage;
	
	private Payload payload = new Payload();
	
	private String logData;
	
	private ArkID arkID ;
	
	

	public FedoraObject(ArkID arkID) {
		this.arkID = arkID;
	}

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

	public ArkID getArkID() {
		return arkID;
	}

	public void setArkID(ArkID arkID) {
		this.arkID = arkID;
	}


}
