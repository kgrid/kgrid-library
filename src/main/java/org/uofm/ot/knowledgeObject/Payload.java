package org.uofm.ot.knowledgeObject;

public class Payload {
	
	private String payload;
	
	private PayloadDescriptor payloadDescriptor;

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

	@Override
	public String toString() {
		return "Payload [payload=" + payload + ", payloadDescriptor=" + payloadDescriptor + "]";
	}
	
	
	
}
