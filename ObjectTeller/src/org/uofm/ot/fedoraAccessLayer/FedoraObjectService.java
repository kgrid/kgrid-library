package org.uofm.ot.fedoraAccessLayer;

import java.util.ArrayList;

public class FedoraObjectService {

	public ArrayList<FedoraObject> getAllObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		ArrayList<FedoraObject> queryObjects = getQueryObjects();
		ArrayList<FedoraObject> knowledgeObjects = getKnowledgeObjects();
		ArrayList<FedoraObject> resultObjects = getResultObjects();
		ArrayList<FedoraObject> tailoringObjects = getTailoringObjects();
		arrayList.addAll(queryObjects);
		arrayList.addAll(knowledgeObjects);
		arrayList.addAll(resultObjects);
		arrayList.addAll(tailoringObjects);
		
		return arrayList;
	}
	
	public ArrayList<FedoraObject> getKnowledgeObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		FedoraObject object1 = new FedoraObject("Lung Cancer Risk Calculator", false, "spark.171", 8, ObjectType.KNOWLEDGE);
		FedoraObject object2 = new FedoraObject("Lung Cancer Screening Guideline", false, "spark.193", 4, ObjectType.KNOWLEDGE);
		FedoraObject object3 = new FedoraObject("Acetaminophen Drug-Drug Interactions", true, "spark.223", 12, ObjectType.KNOWLEDGE);
		FedoraObject object4 = new FedoraObject("Breathing Performance Scale", false, "spark.891", 8, ObjectType.KNOWLEDGE);
		FedoraObject object5 = new FedoraObject("Lung Cancer Risk Categories", true, "spark.099", 7, ObjectType.KNOWLEDGE);
		FedoraObject object6 = new FedoraObject("Hazards of Smoking Cigarettes", false, "spark.241", 4, ObjectType.KNOWLEDGE);
		FedoraObject object7 = new FedoraObject("Hazards of Pipe Smoking", false, "spark.242", 6, ObjectType.KNOWLEDGE);
		FedoraObject object8 = new FedoraObject("Hazards of Asbestos", false, "spark.243", 9, ObjectType.KNOWLEDGE);
		arrayList.add(object1);
		arrayList.add(object2);
		arrayList.add(object3);
		arrayList.add(object4);
		arrayList.add(object5);
		arrayList.add(object6);
		arrayList.add(object7);
		arrayList.add(object8);

		return arrayList;
	}
	
	public ArrayList<FedoraObject> getQueryObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		FedoraObject object1 = new FedoraObject("Hazards of Cigar Smoking", false, "spark.224", 8, ObjectType.QUERY);
		FedoraObject object2 = new FedoraObject("Smoking Cessation Therapeutic Guideline", false, "spark.303", 11, ObjectType.QUERY);
		arrayList.add(object1);
		arrayList.add(object2);

		return arrayList;
	}
	public ArrayList<FedoraObject> getResultObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		FedoraObject object1 = new FedoraObject("Smoking Cessation Patient Education Course", false, "spark.304", 9, ObjectType.RESULT);
		FedoraObject object2 = new FedoraObject("Varenicline Drug-Drug Interarctions", true, "spark.305", 11, ObjectType.RESULT);
		FedoraObject object3 = new FedoraObject("Asthma Assessment Tool", false, "spark.306", 4, ObjectType.RESULT);
		FedoraObject object4 = new FedoraObject("Lung Cancer Screening Harm Model", false, "spark.111", 7, ObjectType.RESULT);
		arrayList.add(object1);
		arrayList.add(object2);
		arrayList.add(object3);
		arrayList.add(object4);
		return arrayList;
	}
	
	public ArrayList<FedoraObject> getTailoringObjects(){
		ArrayList<FedoraObject> arrayList = new ArrayList<FedoraObject>();
		return arrayList;
	}
	
	public FedoraObject getObject(String URI){
		FedoraObject object1 = new FedoraObject("Hazards of Cigar Smoking", false, "spark.224", 8, ObjectType.QUERY);
		return object1;
	}
}
