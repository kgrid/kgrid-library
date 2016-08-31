package org.uofm.ot.services;

import java.util.ArrayList;
import java.util.List;

import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.CreateFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.DeleteFedoraResourceService;
import org.uofm.ot.fedoraAccessLayer.EditFedoraObjectService;
import org.uofm.ot.fedoraAccessLayer.GetFedoraObjectService;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.FedoraObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.PayloadDescriptor;


public class KnowledgeObjectService {
	
	private FusekiService fusekiService;
	
	private GetFedoraObjectService getFedoraObjectService;
	
	private EditFedoraObjectService editFedoraObjectService;
	
	private DeleteFedoraResourceService deleteFedoraResourceService;
	
	private CreateFedoraObjectService createFedoraObjectService;
	
	public void setCreateFedoraObjectService(CreateFedoraObjectService createFedoraObjectService) {
		this.createFedoraObjectService = createFedoraObjectService;
	}

	public void setFusekiService(FusekiService fusekiService) {
		this.fusekiService = fusekiService;
	}
	
	public void setGetFedoraObjectService(GetFedoraObjectService getFedoraObjectService) {
		this.getFedoraObjectService = getFedoraObjectService;
	}


	public void setEditFedoraObjectService(EditFedoraObjectService editFedoraObjectService) {
		this.editFedoraObjectService = editFedoraObjectService;
	}
	
	

	public void setDeleteFedoraResourceService(DeleteFedoraResourceService deleteFedoraResourceService) {
		this.deleteFedoraResourceService = deleteFedoraResourceService;
	}

	public FedoraObject getKnowledgeObject(String uri) throws ObjectTellerException {
		FedoraObject object = new FedoraObject();
		object.setURI(uri);
		object = fusekiService.getKnowledgeObject(object);
		List<Citation> citations = fusekiService.getObjectCitations(uri);	
		object.getMetadata().setCitations(citations);
		return object;
	}
	
	public FedoraObject addOrEditObject(FedoraObject newObject) throws ObjectTellerException {
		return null;
	}
	
	public FedoraObject deleteObject(String uri) throws ObjectTellerException {
		return null;
	}
	
	public List<FedoraObject> getKnowledgeObjects(boolean published) throws ObjectTellerException {
		List<FedoraObject> fedoraObjects = null;
		fedoraObjects = fusekiService.getFedoraObjects(published);
		return fedoraObjects;
	}
	
	public Integer getNumberOfPublishedObjects() throws ObjectTellerException {
		return fusekiService.getNumberOfPublishedObjects();
	}
	
	public FedoraObject getCompleteKnowledgeObject(String uri) throws ObjectTellerException {
		
		FedoraObject object = getKnowledgeObject(uri);	
		
		PayloadDescriptor payloadD = fusekiService.getPayloadProperties(uri);

		object.setPayloadDescriptor(payloadD);

		String provDataPart1 = fusekiService.getObjectProvProperties(uri);

		String provDataPart2 = fusekiService.getObjectProvProperties(uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());

		object.setLogData(provDataPart1+provDataPart2);

		object.setPayload(getFedoraObjectService.getObjectContent(uri, ChildType.PAYLOAD.getChildType()));

		object.setInputMessage(getFedoraObjectService.getObjectContent(uri, ChildType.INPUT.getChildType()));

		object.setOutputMessage(getFedoraObjectService.getObjectContent(uri, ChildType.OUTPUT.getChildType()));
		
		return object;
	}
	
	public Metadata addOrEditMetadata(String uri, Metadata newMetadata) throws ObjectTellerException {
	
		List<Citation> oldCitations = fusekiService.getObjectCitations(uri);
		
		editFedoraObjectService.editObjectMetadata(newMetadata,uri);
		
		
		if(newMetadata != null && newMetadata.getCitations() != null) {
			List<Citation> editCitations = new ArrayList<Citation>();

			boolean firstCitation = true ; 
			// To add new citations
			for (Citation citation : newMetadata.getCitations()) {
				if(citation.getCitation_id() == null && citation.getCitation_title() != null && citation.getCitation_at() != null ) {

					if(firstCitation) {
						boolean citationParentExist = createFedoraObjectService.checkIfObjectExists(uri+"/"+ChildType.CITATIONS.getChildType());

						if(!citationParentExist) 
							createFedoraObjectService.createContainer(uri+"/"+ChildType.CITATIONS.getChildType(), null);

						firstCitation = false ;

					}
					
					String location = createFedoraObjectService.createContainerWithAutoGeneratedName(uri+"/"+ChildType.CITATIONS.getChildType(), null);

					citation.setCitation_id(location);

					createFedoraObjectService.addCitationProperties(citation, uri+"/"+ChildType.CITATIONS.getChildType()+"/"+location, null);
				} else {
					if(citation.getCitation_id() != null )
						editCitations.add(citation);
				}
			}
			
			editFedoraObjectService.editCitations(uri,editCitations);
			oldCitations.removeAll(editCitations);
		}
		
		if(oldCitations.isEmpty() == false){
			for (Citation citation : oldCitations) {
				deleteFedoraResourceService.deleteObjectCitation(uri, citation.getCitation_id());
			}		
		}
		
		return getKnowledgeObject(uri).getMetadata() ;
	}
	

}