package org.uofm.ot.knowledgeObject;

public class License {
	
	private String licenseName;
	
	private String licenseLink;

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getLicenseLink() {
		return licenseLink;
	}

	public void setLicenseLink(String licenseLink) {
		this.licenseLink = licenseLink;
	}

	@Override
	public String toString() {
		return "License [licenseName=" + licenseName + ", licenseLink=" + licenseLink + "]";
	} 
	
	

}
