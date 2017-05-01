package org.uofm.ot.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.*;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.*;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.UserProfile;

import java.util.ArrayList;
import java.util.List;


@Service
public class KnowledgeObjectService {

	@Autowired
	private IdService idService;
	
	@Autowired
	private FusekiService fusekiService;

	@Autowired
	private FCRepoService fcRepoService;

	private static final Logger logger = Logger.getLogger(KnowledgeObjectService.class);
	
	public KnowledgeObject getKnowledgeObject(ArkId arkId) throws ObjectTellerException {

		KnowledgeObject object = fusekiService.getKnowledgeObject(arkId);
		if(object != null) {
			List<Citation> citations = fusekiService.getObjectCitations(arkId);
			object.getMetadata().setCitations(citations);
		}
		return object;
	}
	
	public KnowledgeObject editObject(KnowledgeObject newObject, ArkId arkId) throws ObjectTellerException, URISyntaxException {

		addOrEditMetadata(arkId, newObject.getMetadata());
		editPayload(arkId, newObject.getPayload());
		editInputMessageContent(arkId, newObject.getInputMessage());
		editOutputMessageContent(arkId, newObject.getOutputMessage());
		KnowledgeObject updatedObject = getCompleteKnowledgeObject(arkId);
		return updatedObject ; 
	}
	
	public void deleteObject(ArkId arkId) throws ObjectTellerException {
		try {
			fcRepoService.deleteFedoraResource(new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException("Incorrect url syntax when trying to delete object with arkID " + arkId.getArkId() + ". " + e);
		}
	}
	
	public List<KnowledgeObject> getKnowledgeObjects(boolean published) throws ObjectTellerException {
		return fusekiService.getFedoraObjects(published);
	}
	
	public Integer getNumberOfPublishedObjects() throws ObjectTellerException {
		return fusekiService.getNumberOfPublishedObjects();
	}
	
	public KnowledgeObject getCompleteKnowledgeObject(ArkId arkId) throws ObjectTellerException {

		String uri = arkId.getFedoraPath();
		
		KnowledgeObject object = getKnowledgeObject(arkId);
		
		if(object != null ) {

			Payload payload = fusekiService.getPayloadProperties(uri);

			payload.setContent(getPayloadContent(uri));

			object.setPayload(payload);

			object.setLogData(getProvData(arkId));

			object.setInputMessage(getInputMessageContent(arkId));

			object.setOutputMessage(getOutputMessageContent(arkId));
		}
		
		return object;
	}
	
	public Metadata addOrEditMetadata(ArkId arkId, Metadata newMetadata) throws ObjectTellerException {

		String arkIDPath = arkId.getFedoraPath();
		try {
			URI arkUri = new URI(fcRepoService.getBaseURI() + arkIDPath);

			editObjectMetadata(newMetadata, arkIDPath);

			List<Citation> oldCitations = fusekiService.getObjectCitations(arkId);

			if (newMetadata != null && newMetadata.getCitations() != null) {
				List<Citation> newCitations = new ArrayList<Citation>();

				boolean firstCitation = true;
				// To add new citations
				for (Citation citation : newMetadata.getCitations()) {
					if (citation.getCitation_id() == null && citation.getCitation_title() != null
							&& citation.getCitation_at() != null) {

						if (firstCitation) {
							boolean citationParentExist = fcRepoService
									.checkIfObjectExists(new URI(arkUri + "/" + ChildType.CITATIONS.getChildType()));

							if (!citationParentExist)
								fcRepoService.createContainer(arkUri, ChildType.CITATIONS.getChildType());
							firstCitation = false;

						}
						URI citationLocation = fcRepoService
								.createContainerWithAutoGeneratedName(
										new URI(fcRepoService.getBaseURI() + arkIDPath + "/" + ChildType.CITATIONS.getChildType()));
						citation.setCitation_id(citationLocation.getPath());

						addCitationProperties(citation, citationLocation);
					} else {
						if (citation.getCitation_id() != null)
							newCitations.add(citation);
					}
				}

				editCitations(arkIDPath, newCitations);
				oldCitations.removeAll(newCitations);
			}

			if (!oldCitations.isEmpty()) {
				for (Citation citation : oldCitations) {
					fcRepoService.deleteFedoraResource(new URI(arkUri + "/" + ChildType.CITATIONS.getChildType() + "/" + citation.getCitation_id()));
				}
			}

		}catch (URISyntaxException e) {
			throw new ObjectTellerException("Error creating URI for arkID " + arkId.getArkId() + ". " + e);
		}
		return getKnowledgeObject(arkId).getMetadata();
	}
	
	public KnowledgeObject createKnowledgeObject(KnowledgeObject knowledgeObject, OTUser loggedInUser, String libraryURL) throws ObjectTellerException, URISyntaxException {
		return createObject(knowledgeObject, loggedInUser, libraryURL, null);
	}

	public KnowledgeObject createFromExistingArkId(KnowledgeObject knowledgeObject, OTUser loggedInUser, String libraryURL, ArkId existingArkId) throws ObjectTellerException, URISyntaxException {

		// TODO: Check other option for Dummy User
		UserProfile profile = new UserProfile("MANUAL", "IMPORT");
		//OTUser loggedInUser = new OTUser(null, null, null, profile);

		return createObject(knowledgeObject, loggedInUser, libraryURL, existingArkId);

	}

	public String getInputMessageContent(ArkId arkId) throws ObjectTellerException{
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.INPUT.getChildType());
	}
	
	public String getOutputMessageContent(ArkId arkId) throws ObjectTellerException{
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.OUTPUT.getChildType());
	}
	
	public String getPayloadContent(String objectURI) throws ObjectTellerException{
		return fcRepoService.getObjectContent(objectURI, ChildType.PAYLOAD.getChildType());
	}
	
	public String getProvData(ArkId arkId) throws ObjectTellerException{
		String provDataPart1 = fusekiService.getObjectProvProperties(arkId.getFedoraPath());

		String provDataPart2 = fusekiService.getObjectProvProperties(arkId.getFedoraPath()+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());

		return provDataPart1 + provDataPart2 ; 
	}
	
	public void editInputMessageContent(ArkId arkId,String inputMessage) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(inputMessage, arkId, ChildType.INPUT.getChildType());
	}
	
	public void editOutputMessageContent(ArkId arkId,String outputMessage) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(outputMessage, arkId, ChildType.OUTPUT.getChildType());
	}
	
	public Payload getPayload(ArkId arkId) throws ObjectTellerException {
		Payload payload = fusekiService.getPayloadProperties(arkId.getFedoraPath());
		payload.setContent(getPayloadContent(arkId.getFedoraPath()));
		return payload ;
	}
	
	public void editPayload(ArkId arkId,Payload payload) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary( payload.getContent(), arkId, ChildType.PAYLOAD.getChildType());
		editPayloadMetadata(payload,arkId.getFedoraPath());
	}
	
	public void patchKnowledgeObject(KnowledgeObject knowledgeObject,ArkId arkId) throws ObjectTellerException, URISyntaxException {
		if(knowledgeObject != null){
			if(knowledgeObject.getMetadata() != null ) {
				String param = knowledgeObject.getMetadata().isPublished() ? "yes":"no";
				toggleObject(arkId.getFedoraPath(), param);
			}
		}
	}

	public boolean exists(ArkId arkId) throws ObjectTellerException {
		try {
			return fcRepoService.checkIfObjectExists(
					new URI(fcRepoService.getBaseURI() + "/" + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException("Error building URI for arkID " + arkId.getArkId() + ". " + e);
		}
	}

	public void publishKnowledgeObject(ArkId arkId, boolean isToBePublished, OTUser loggedInUser) throws ObjectTellerException, URISyntaxException {

		KnowledgeObject ko = getKnowledgeObject(arkId);

		if ( ko == null ) {
			throw new ObjectTellerException("Unable to retrieve knowledge object: " + arkId.getArkId());
		}

		toggleObject(arkId.getFedoraPath(), isToBePublished? "yes" : "no" );

		List<String> metadata = idService.createBasicMetadata(loggedInUser.getFullName(), ko.getMetadata().getTitle());
		if(isToBePublished) {
			idService.publish(arkId, metadata);
		} else {
			idService.retract(arkId, metadata);
		}

	}

	public void editObjectMetadata(Metadata metadata,String objectURI) throws ObjectTellerException, URISyntaxException {

			String baseQuery = "%s { "+"\n "+
					"<"+fcRepoService.getBaseURI()+objectURI+">  dc:title  %s;"+ "\n"+
				 "ot:contributors %s; "+ " \n"+
				 "ot:description %s; "+ " \n"+
				 "ot:owner %s; "+ " \n"+
				 "ot:keywords %s; "+ " \n"+
				 "%s ot:licenseName %s; "+  " \n"+
				 "ot:licenseLink %s . %s"+ "\n" +
				 "}"+"\n" ;

			String deleteClause = String.format(baseQuery, new Object [] {"DELETE","?o0","?o1","?o2","?o3","?o4","","?o5","?o6",""});

			String licenseName = "";
			if(metadata.getLicense() != null)
				licenseName = metadata.getLicense().getLicenseName();

			String licenseLink = "";
			if(metadata.getLicense() != null)
				licenseLink = metadata.getLicense().getLicenseLink();


			String insertClause = String.format(baseQuery, new Object [] {"INSERT",quote(metadata.getTitle()),quote(metadata.getContributors()),quote(metadata.getDescription()),
					quote(metadata.getOwner()),quote(metadata.getKeywords()),"",quote(licenseName),quote(licenseLink),""});

			String whereClause = String.format(baseQuery, new Object [] {"WHERE","?o0","?o1","?o2","?o3","?o4","OPTIONAL { <"+fcRepoService.getBaseURI()+objectURI+">","?o5","?o6","}"});

			String data = FusekiConstants.PREFIX_DC +"\n "+
					FusekiConstants.PREFIX_OT +"\n "
					+ deleteClause + insertClause + whereClause;

			System.out.println(data);

		fcRepoService.sendPatchRequestForUpdatingTriples(data, objectURI);
	}

	public void toggleObject(String objectURI, String value) throws ObjectTellerException, URISyntaxException {

		String query = getGetSingleParamQuery(objectURI, "published", value);

		fcRepoService.sendPatchRequestForUpdatingTriples(query, objectURI);
	}


	private String getGetSingleParamQuery(String objectURI, String param, String value) {
		return FusekiConstants.PREFIX_OT +"\n "+
				"	DELETE \n" +
				"	{ \n" +
				"	  <"+fcRepoService.getBaseURI()+objectURI+ ">   ot:" + param + " ?o . \n" +
				"	} \n"+
				"	INSERT \n"+
				"	{ \n"+
				"	  <"+fcRepoService.getBaseURI()+objectURI+ ">   ot:" + param + "  \"" +value+"\" . \n"+
				"	} \n"+
				"	WHERE \n"+
				"	{  \n"+
				"	 <"+fcRepoService.getBaseURI()+objectURI+ "> 	 ot:" + param + " ?o . \n" +
				"	} ";
	}

	public void editPayloadMetadata(Payload payload, String objectURI) throws ObjectTellerException, URISyntaxException {

		String data =
			FusekiConstants.PREFIX_OT +"\n "+
			"	DELETE \n" +
			"	{ \n" +
			"	  <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:functionName  ?o0 . \n"+
			"	  <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:executorType  ?o1 . \n"+
			"	} \n"+
			"	INSERT \n"+
			"	{ \n"+
			"	  <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName   \""+payload.getFunctionName()+"\"  .  \n"+
			"	  <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  \""+payload.getEngineType()+"\"  .  \n"+
			"	} \n"+
			"	WHERE \n"+
			"	{  \n"+
			"	 <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:functionName  ?o0 . \n"+
			"	 <"+fcRepoService.getBaseURI()+objectURI+"/"+ ChildType.PAYLOAD.getChildType()+">   ot:executorType  ?o1 .\n"+
			"	} ";


		fcRepoService.sendPatchRequestForUpdatingTriples(data, objectURI+"/"+ ChildType.PAYLOAD.getChildType()+"/fcr:metadata");

	}

	public void editCitations(String knowledgeObjectURI , List<Citation> citations) throws ObjectTellerException, URISyntaxException {

		for (Citation citation : citations) {
			if(citation.getCitation_id() != null) {
				String citationObject = "<"+fcRepoService.getBaseURI()+knowledgeObjectURI+"/"+ ChildType.CITATIONS.getChildType() +"/"+citation.getCitation_id()+">" ;
				String data =
						FusekiConstants.PREFIX_OT +"\n "+
						"	DELETE \n" +
						"	{ \n" +
						"	  "+citationObject+"  ot:citationAt  ?o0 . \n"+
						"	  "+citationObject+"  ot:citationTitle  ?o1 . \n"+
						"	} \n"+
						"	INSERT \n"+
						"	{ \n"+
						"	  "+citationObject+"  ot:citationAt   \""+citation.getCitation_at()+"\"  .  \n"+
						"	 "+citationObject+"   ot:citationTitle  \""+citation.getCitation_title()+"\"  .  \n"+
						"	} \n"+
						"	WHERE \n"+
						"	{  \n"+
						"	 "+citationObject+"   ot:citationAt  ?o0 . \n"+
						"	 "+citationObject+"   ot:citationTitle  ?o1 .\n"+
						"	} ";

				fcRepoService.sendPatchRequestForUpdatingTriples(data, knowledgeObjectURI+"/"+ ChildType.CITATIONS.getChildType() +"/"+citation.getCitation_id());
			}
		}
	}

	private String quote(String str){
		return "\""+str+"\"";
	}

	public KnowledgeObject createObject(KnowledgeObject knowledgeObject, OTUser loggedInUser, String libraryURL , ArkId existingArkId) throws ObjectTellerException, URISyntaxException {

		URI transactionURI = null;
		String errorMessage = null;
		if(fcRepoService.getBaseURI() != null){

			ArkId arkId = existingArkId;
			if (arkId == null) {
				arkId = idService.mint();
			}

			knowledgeObject.setArkId(arkId);

			String arkIDPath = knowledgeObject.getArkId().getFedoraPath();

			if(arkIDPath != null ) {

				transactionURI = fcRepoService.createTransaction();

				URI koParentURI = fcRepoService.createContainer(transactionURI, arkIDPath);

				List<String> metadata = idService.createBasicMetadata((loggedInUser != null ? loggedInUser.getFirst_name() : "AnonymousUser"), knowledgeObject.getMetadata().getTitle());
				URI targetURI = new URI(libraryURL + "/" + arkId.getFedoraPath());
				idService.bind(knowledgeObject, metadata, targetURI);

				fcRepoService.createContainer(koParentURI, ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType());

				addProvMetadataStart(koParentURI, loggedInUser);

				addHEMetadataProperties(knowledgeObject, koParentURI);

				URI citationsCollectionURI = fcRepoService.createContainer(koParentURI, ChildType.CITATIONS.getChildType());

				if(knowledgeObject.getMetadata() != null && knowledgeObject.getMetadata().getCitations() != null) {
					for (Citation citation : knowledgeObject.getMetadata().getCitations()) {
						URI citationURI = fcRepoService.createContainerWithAutoGeneratedName(citationsCollectionURI);
						citation.setCitation_id(citationURI.getPath());
						addCitationProperties(citation, citationURI);
					}
				}

				fcRepoService.putBinary(knowledgeObject.getPayload().getContent(), koParentURI, ChildType.PAYLOAD.getChildType());

				fcRepoService.putBinary(knowledgeObject.getInputMessage(), koParentURI, ChildType.INPUT.getChildType());

				fcRepoService.putBinary(knowledgeObject.getOutputMessage(), koParentURI , ChildType.OUTPUT.getChildType());

				addPayloadMetadataProperties(knowledgeObject.getPayload(), koParentURI);

				addProvMetadataEnd(koParentURI);

				fcRepoService.commitTransaction(transactionURI);

				logger.info("Successfully created object "+knowledgeObject);

			} else
				errorMessage = "Unable to generate unique id for the object "+knowledgeObject;

		} else
			errorMessage = "Exception: Objects can not be created until fedora server is configured.";

		if(errorMessage != null) {
			logger.error(errorMessage);
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage(errorMessage);
			if(transactionURI != null)
				fcRepoService.rollbackTransaction(transactionURI);
			throw exception;
		}
		return knowledgeObject;
	}

	private void addProvMetadataStart(URI uri, OTUser loggedInUser) {

		String properties = FusekiConstants.PREFIX_PROV+" \n "+
				"INSERT DATA \n"+
				"{ 	<"+ uri +"> prov:isA \"http://www.w3.org/ns/prov#Entity\" ; \n"+
				" prov:wasAttributedTo  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" ; \n"+
				" prov:wasGeneratedBy  \""+ uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() +"\" . \n"+"} ";

		try {
			fcRepoService.sendPatchRequestForUpdatingTriples(properties, uri);
		} catch (ObjectTellerException e) {
			logger.error("Error inserting metadata at start " + e);
		}
		Date today = new Date();
		String provStart = FusekiConstants.PREFIX_PROV+" \n "+
				"INSERT DATA \n"+
				"{ 	<"+uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:isA \"http://www.w3.org/ns/prov#Activity\" ; \n"+
				" prov:Used \""+fcRepoService.getBaseURI()+uri+"\" ; \n"+
				" prov:startedAtTime \""+ today.toString()+"\" ; \n"+
				" prov:wasAssociatedWith  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" . \n"+"} ";

		try {
			fcRepoService.sendPatchRequestForUpdatingTriples(provStart, new URI(
					uri + "/" + ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType()));
		}catch (URISyntaxException|ObjectTellerException e) {
			logger.error("Error inserting activity record for inserting metadata at start " + e);
		}
	}

	private void addProvMetadataEnd (URI objectURI) throws ObjectTellerException {;

		Date today = new Date();
		String provEnd = FusekiConstants.PREFIX_PROV+" \n "+
				"INSERT DATA \n"+
				"{ 	<"+objectURI+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:endedAtTime \""+today.toString()+"\" . \n"+"} ";

		try {
			fcRepoService.sendPatchRequestForUpdatingTriples(provEnd,
					new URI(objectURI + "/" + ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType()));
		}catch (URISyntaxException|ObjectTellerException e) {
			logger.error("Error inserting activity record for inserting metadata at end " + e);
		}
	}

	private void addHEMetadataProperties(KnowledgeObject knowledgeObject, URI uri) throws ObjectTellerException {

		if(knowledgeObject.getMetadata() != null) {

			String description = knowledgeObject.getMetadata().getDescription();
			if(description == null)
				description = "";

			String owner = knowledgeObject.getMetadata().getOwner();
			if(owner == null)
				owner = "";

			String contributors = knowledgeObject.getMetadata().getContributors();
			if(contributors == null)
				contributors = "";

			String keywords = knowledgeObject.getMetadata().getKeywords();
			if(keywords == null)
				keywords = "";


			String properties = FusekiConstants.PREFIX_OT +"\n "+
					FusekiConstants.PREFIX_DC +"\n "+
					FusekiConstants.PREFIX_RDF +"\n "+
					"INSERT DATA \n"+
					"{ 	<"+uri+"> ot:owner  \""+owner+"\" ; \n"+
					" ot:description  \""+description+"\" ; \n"+
					" ot:contributors  \""+contributors+"\" ; \n"+
					" dc:title \""+knowledgeObject.getMetadata().getTitle()+"\" ; \n"+
					" ot:keywords  \""+keywords+"\" ; \n"+
					" rdf:type "+FusekiConstants.OT_TYPE_KNOWLEDGE_OBJECT+" ; \n"+
					" ot:arkId \""+knowledgeObject.getArkId().getArkId()+"\" ; \n"+
					" ot:published  \"no\" ; \n";

			if(knowledgeObject.getMetadata().getLicense() != null) {
				properties = properties +
						" ot:licenseName  \""+knowledgeObject.getMetadata().getLicense().getLicenseName()+"\" ; \n"+
						" ot:licenseLink  \""+knowledgeObject.getMetadata().getLicense().getLicenseLink()+"\" ; \n" ;
			}

			properties = properties + "} ";

			fcRepoService.sendPatchRequestForUpdatingTriples(properties, uri);
		}

	}

	public void addPayloadMetadataProperties(Payload payload, URI uri) throws ObjectTellerException {

		String properties = FusekiConstants.PREFIX_OT + "\n " +
				"INSERT DATA \n" +
				"{ 	<" + uri + "/" + ChildType.PAYLOAD.getChildType() + ">   ot:functionName   \""
				+ payload.getFunctionName() + "\" ; \n" +
				" ot:executorType  \"" + payload.getEngineType() + "\" . \n" + "} ";
		try {
			fcRepoService.sendPatchRequestForUpdatingTriples(properties, new URI(uri + "/" + ChildType.PAYLOAD.getChildType() + "/fcr:metadata"));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException("Error constructing url for updating payload metadata at " + uri + " Error: " + e);
		}
	}

	public void addCitationProperties(Citation citation, URI uri) throws ObjectTellerException {

		String properties = FusekiConstants.PREFIX_OT + "\n " +
				"INSERT DATA \n" +
				"{ 	<" + uri + ">   ot:citationTitle   \"" + citation.getCitation_title() + "\" ; \n" +
				" ot:citationAt \"" + citation.getCitation_at() + "\" . \n" + "} ";

		fcRepoService.sendPatchRequestForUpdatingTriples(properties, uri);
	}
}
