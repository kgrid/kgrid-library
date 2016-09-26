package org.uofm.ot.knowledgeObject;

public class ArkId {

    private String arkId;

    private String naan;

    private String name;

    public ArkId() {}

	public ArkId(String id_value) {

        if (id_value.contains("ark:/")) {
            this.arkId = id_value; // full id
            String[] path = id_value.substring("ark:/".length()).split("/");
            naan = path[0];
            name = path[1];
        } else {
            this.arkId = this.name = id_value; // old-style id, e.g. "OT42"
        }
	}

	public String getArkId() {

        if (arkId == null )
            this.arkId = String.format("ark:/%s", getPathOnly());
        return arkId;
	}

	@Override
    public String toString() {
        return getArkId();
    }

    // Fake/sample Ids (for Testing)
    static final String FAKE_ARKID = "ark:/99999/12345";

	public static ArkId FAKE_ARKID() {
        return new ArkId(FAKE_ARKID);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNaan(String naan) {
        this.naan = naan;
    }

    public String getPathOnly() {

        if (naan == null) {
            return name;
        } else {
            return naan + "/" + name;
        }
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
