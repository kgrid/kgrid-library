package org.uofm.ot.ui.util;

public interface Menu {

	public enum TopMenuOptions {
		ACCOUNT("Account"), OBJECTS("Objects"), OBJECT_MAP("Object Map"), LIBRARY("Library"), SYSTEM("System");
		
		private String name;
		
		private TopMenuOptions(String name){
			this.name = name;
		}
		
		public String getName(){
	        return name;
	    }
	} 
	
	public enum LHAccountMenuOptions {
		VIEW("View"), EDIT("Edit");
		
		private String name;
		
		private LHAccountMenuOptions(String name){
			this.name = name;
		}
		
		public String getName(){
	        return name;
	    }
	}
	
	public enum LHSingleObjectMenuOptions {
		SUMMARY("Summary"), METADATA("Metadata Card*"), LICENSE("License*"), ANNOTATION("Annotation"), LINKAGES("Linkages"), CODE("Code"), API("APIs"), 
		REFERENCES("References"), HISTORY("History*");
		
		private String name;

		private LHSingleObjectMenuOptions(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}	
	}
	
	public enum LHSystemMenuOptions {
		VIEW("View"), EDIT("Edit");
		
		private String name;
		
		private LHSystemMenuOptions(String name){
			this.name = name;
		}
		
		public String getName(){
	        return name;
	    }
	}
}
