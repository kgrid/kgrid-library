package org.kgrid.library.knowledgeObject;

import com.complexible.pinto.annotations.RdfProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class KnowledgeObject {

	private Metadata metadata ;
	
	private String URI;
	
	private String inputMessage;
	
	private String outputMessage;
	
	private Payload payload = new Payload();
	
	private String logData;
	
	@JsonIgnore
	private ArkId arkId;

	public KnowledgeObject(ArkId arkId) {
		this.arkId = arkId;
	}

	public KnowledgeObject(){}
	
	public KnowledgeObject(String URI) {
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

	@RdfProperty(value = "ot:arkId")
	public ArkId getArkId() {
		return arkId;
	}

	public void setArkId(ArkId arkId) {
		this.arkId = arkId;
		if(metadata == null) {
			metadata = new Metadata();
		}
		metadata.setArkId(arkId.getArkId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		KnowledgeObject that = (KnowledgeObject) o;

		if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) {
			return false;
		}
		if (URI != null ? !URI.equals(that.URI) : that.URI != null) {
			return false;
		}
		if (inputMessage != null ? !inputMessage.equals(that.inputMessage)
				: that.inputMessage != null) {
			return false;
		}
		if (outputMessage != null ? !outputMessage.equals(that.outputMessage)
				: that.outputMessage != null) {
			return false;
		}
		if (payload != null ? !payload.equals(that.payload) : that.payload != null) {
			return false;
		}
		if (logData != null ? !logData.equals(that.logData) : that.logData != null) {
			return false;
		}
		return arkId != null ? arkId.equals(that.arkId) : that.arkId == null;
	}

	@Override
	public int hashCode() {
		int result = metadata != null ? metadata.hashCode() : 0;
		result = 31 * result + (URI != null ? URI.hashCode() : 0);
		result = 31 * result + (inputMessage != null ? inputMessage.hashCode() : 0);
		result = 31 * result + (outputMessage != null ? outputMessage.hashCode() : 0);
		result = 31 * result + (payload != null ? payload.hashCode() : 0);
		result = 31 * result + (logData != null ? logData.hashCode() : 0);
		result = 31 * result + (arkId != null ? arkId.hashCode() : 0);
		return result;
	}
}
