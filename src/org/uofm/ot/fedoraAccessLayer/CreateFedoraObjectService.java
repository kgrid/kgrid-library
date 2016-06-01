package org.uofm.ot.fedoraAccessLayer;

import java.util.Date;

import org.apache.log4j.Logger;
import org.uofm.ot.dao.ObjectIDDAO;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fusekiAccessLayer.FusekiConstants;
import org.uofm.ot.model.ObjectId;
import org.uofm.ot.model.User;

public class CreateFedoraObjectService extends FedoraObjectService {

	private ObjectIDDAO objectIDDAO;

	public void setObjectIDDAO(ObjectIDDAO objectIDDAO) {
		this.objectIDDAO = objectIDDAO;
	}

	private static final Logger logger = Logger.getLogger(CreateFedoraObjectService.class);
	
	private static final String ID_PREFIX = "OT";


	public FedoraObject createObject(FedoraObject fedoraObject, User loggedInUser) throws ObjectTellerException {

		String errorMessage = null; 
		if(baseURI!= null){

			ObjectId newObjId = objectIDDAO.retrieveNewId();

			if(newObjId != null ) {

				String uri = ID_PREFIX + newObjId.getNewObjectId();

				createContainer(uri);
				createContainer(uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());
				addProvMetadataStart(uri,loggedInUser);

				addHEMetadataProperties(fedoraObject, uri);

				putBinary(fedoraObject.getPayload(), uri, ChildType.PAYLOAD.getChildType());

				putBinary(fedoraObject.getInputMessage(), uri, ChildType.INPUT.getChildType());

				putBinary(fedoraObject.getOutputMessage(), uri , ChildType.OUTPUT.getChildType());

				addPayloadMetadataProperties(fedoraObject.getPayloadDescriptor(), uri);

				fedoraObject.setURI(uri);
				addProvMetadataEnd(uri);
				
				logger.info("Successfully created object "+fedoraObject);
				
				objectIDDAO.updateNewId(newObjId);

			} else
				errorMessage = "Unable to generate unique id for the object "+fedoraObject;

		} else 
			errorMessage = "Exception: Objects can not be created until fedora server is configured.";


		if(errorMessage != null) {
			logger.error(errorMessage);
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage(errorMessage);
			throw exception;
		}

		return fedoraObject;
	}

	private void addProvMetadataStart ( String uri, User loggedInUser ) throws ObjectTellerException {

		String properties = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+baseURI+uri+"> prov:isA \"http://www.w3.org/ns/prov#Entity\" ; \n"+
				" prov:wasAttributedTo  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" ; \n"+
				" prov:wasGeneratedBy  \""+ baseURI+uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType() +"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(properties, uri);

		Date today = new Date();
		String provStart = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+baseURI+uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:isA \"http://www.w3.org/ns/prov#Activity\" ; \n"+
				" prov:Used \""+baseURI+uri+"\" ; \n"+
				" prov:startedAtTime \""+ today.toString()+"\" ; \n"+
				" prov:wasAssociatedWith  \""+loggedInUser.getFirst_name()+" "+loggedInUser.getLast_name()+"\" . \n"+"} ";

		sendPatchRequestForUpdatingTriples(provStart, uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());



	}

	private void addProvMetadataEnd ( String uri) throws ObjectTellerException {
		Date today = new Date();
		String provEnd = FusekiConstants.PREFIX_PROV+" \n "+	
				"INSERT DATA \n"+
				"{ 	<"+baseURI+uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType()+"> prov:endedAtTime \""+today.toString()+"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(provEnd, uri+"/"+ChildType.LOG.getChildType()+"/"+ChildType.CREATEACTIVITY.getChildType());
	}


	private void addHEMetadataProperties(FedoraObject fedoraObject, String uri) throws ObjectTellerException {
		String properties = FusekiConstants.PREFIX_OT +"\n "+	
				FusekiConstants.PREFIX_DC +"\n "+
				"INSERT DATA \n"+
				"{ 	<"+baseURI+uri+"> ot:owner  \""+fedoraObject.getOwner()+"\" ; \n"+
				" ot:description  \""+fedoraObject.getDescription()+"\" ; \n"+
				" ot:contributors  \""+fedoraObject.getContributors()+"\" ; \n"+
				" dc:title \""+fedoraObject.getTitle()+"\" ; \n"+
				" ot:keywords  \""+fedoraObject.getKeywords()+"\" ; \n"+ 
				" ot:published  \"no\" . \n"+"} ";

		sendPatchRequestForUpdatingTriples(properties, uri);

	}

	public void addPayloadMetadataProperties(PayloadDescriptor payload, String uri) throws ObjectTellerException {
		String properties = FusekiConstants.PREFIX_OT +"\n "+	
				"INSERT DATA \n"+
				"{ 	<"+baseURI+uri+"/"+ ChildType.PAYLOAD.getChildType() +">   ot:functionName   \""+payload.getFunctionName()+"\" ; \n"+
				" ot:executorType  \""+payload.getEngineType()+"\" . \n"+"} ";


		sendPatchRequestForUpdatingTriples(properties, uri+"/"+ ChildType.PAYLOAD.getChildType()+"/fcr:metadata");

	}
}
