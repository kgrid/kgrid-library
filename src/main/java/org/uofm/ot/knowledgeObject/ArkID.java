package org.uofm.ot.knowledgeObject;

public class ArkID {

    private String arkId;

	public ArkID(String string) {
		this.arkId = string;
	}

	public String getArkId() {
		return arkId;
	}

	@Override
    public String toString() {
        return this.arkId;
    }

    // Fake/sample Ids (for Testing)
    static final String FAKE_ARKID = "ark:/99999/12345";

	public static ArkID FAKE_ARKID() {
        return new ArkID(FAKE_ARKID);
    }

    // Status flags for setting state in external services
    // (ArkId has no status itself
    public static enum Status {

        PUBLIC("public"),
        RESERVED("reserved"),
        UNAVAILABLE("unavailable");

        private String status;

        private Status(String status) {
            this.status = status ;
        }

        @Override
        public String toString(){
            return this.status;
        }
    }
}
