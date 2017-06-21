package org.uofm.ot.services;


import com.complexible.pinto.Identifiable;
import com.complexible.pinto.RDFMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraAccessLayer.ChildType;
import org.uofm.ot.fedoraAccessLayer.FCRepoService;
import org.uofm.ot.fusekiAccessLayer.FusekiService;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.knowledgeObject.ProvenanceLogData;
import org.uofm.ot.model.OTUser;
import org.uofm.ot.model.UserProfile;


@Service
public class KnowledgeObjectService {

	@Autowired
	private IdService idService;

	@Autowired
	private FusekiService fusekiService;

	@Autowired
	private FCRepoService fcRepoService;

	private static final Logger logger = Logger.getLogger(KnowledgeObjectService.class);

	private static final String OT_NAMESPACE_PREFIX = "ot";

	private static final String OT_NAMESPACE_URL = "http://uofm.org/objectteller/";

	private static final String FEDORA_NAMESPACE_PREFIX = "fedora";

	private static final String FEDORA_NAMESPACE_URL = "http://fedora.info/definitions/v4/repository#";

	private static final String PROV_NAMESPACE_PREFIX = "prov";

	private static final String PROV_NAMESPACE_URL = "http://www.w3.org/ns/prov#";

	// We can use namespace objects (like below) instead of passing namespace strings to the serializer
	// when this pinto bug is fixed: https://github.com/stardog-union/pinto/issues/21
	//private static final Namespace OT_NAMESPACE = new SimpleNamespace("ot", "http://uofm.org/objectteller/");


	public KnowledgeObject getKnowledgeObject(ArkId arkId) throws ObjectTellerException {

		KnowledgeObject object = fusekiService.getKnowledgeObject(arkId);
		if (object != null) {
			List<Citation> citations = fusekiService.getObjectCitations(arkId);
			object.getMetadata().setCitations(citations);
		}
		return object;
	}

	public KnowledgeObject editObject(KnowledgeObject newObject, ArkId arkId)
			throws ObjectTellerException, URISyntaxException {

		addOrEditMetadataToArkId(arkId, newObject.getMetadata());
		editPayload(arkId, newObject.getPayload());
		editInputMessageContent(arkId, newObject.getInputMessage());
		editOutputMessageContent(arkId, newObject.getOutputMessage());
		return getCompleteKnowledgeObject(arkId);
	}

	public void deleteObject(ArkId arkId) throws ObjectTellerException {
		try {
			fcRepoService
					.deleteFedoraResource(new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException(
					"Incorrect url syntax when trying to delete object with arkID " + arkId.getArkId() + ". "
							+ e);
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

		KnowledgeObject knowledgeObject = getKnowledgeObject(arkId);

		if (knowledgeObject != null) {

			Payload payload = fusekiService.getPayloadProperties(uri);

			payload.setContent(getPayloadContent(uri));

			knowledgeObject.setPayload(payload);

			knowledgeObject.setLogData(getProvData(arkId));

			knowledgeObject.setInputMessage(getInputMessageContent(arkId));

			knowledgeObject.setOutputMessage(getOutputMessageContent(arkId));
		}

		return knowledgeObject;
	}

	public String getInputMessageContent(ArkId arkId) throws ObjectTellerException {
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.INPUT.getChildType());
	}

	public String getOutputMessageContent(ArkId arkId) throws ObjectTellerException {
		return fcRepoService.getObjectContent(arkId.getFedoraPath(), ChildType.OUTPUT.getChildType());
	}

	public String getPayloadContent(String objectURI) throws ObjectTellerException {
		return fcRepoService.getObjectContent(objectURI, ChildType.PAYLOAD.getChildType());
	}

	public String getProvData(ArkId arkId) throws ObjectTellerException {
		String provDataPart1 = fusekiService.getObjectProvProperties(arkId.getFedoraPath());

		String provDataPart2 = fusekiService.getObjectProvProperties(
				arkId.getFedoraPath() + "/" + ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY
						.getChildType());

		return provDataPart1 + provDataPart2;
	}

	public void editInputMessageContent(ArkId arkId, String inputMessage)
			throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(inputMessage, arkId, ChildType.INPUT.getChildType());
	}

	public void editOutputMessageContent(ArkId arkId, String outputMessage)
			throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(outputMessage, arkId, ChildType.OUTPUT.getChildType());
	}

	public Payload getPayload(ArkId arkId) throws ObjectTellerException {
		Payload payload = fusekiService.getPayloadProperties(arkId.getFedoraPath());
		payload.setContent(getPayloadContent(arkId.getFedoraPath()));
		return payload;
	}

	public void editPayload(ArkId arkId, Payload payload) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(payload.getContent(), arkId, ChildType.PAYLOAD.getChildType());
		insertRDFData(payload, new URI(fcRepoService.getBaseURI() + arkId.getFedoraPath() + "/Payload"));
	}

	@Deprecated
	public void patchKnowledgeObject(KnowledgeObject knowledgeObject, ArkId arkId)
			throws ObjectTellerException, URISyntaxException {
		if (knowledgeObject != null && knowledgeObject.getMetadata() != null) {
			boolean param = knowledgeObject.getMetadata().isPublished();
			togglePublishedStatus(arkId, knowledgeObject.getMetadata(), param);
		}
	}

	public boolean exists(ArkId arkId) throws ObjectTellerException {
		try {
			return fcRepoService.checkIfObjectExists(
					new URI(fcRepoService.getBaseURI() + "/" + arkId.getFedoraPath()));
		} catch (URISyntaxException e) {
			throw new ObjectTellerException(
					"Error building URI for arkID " + arkId.getArkId() + ". " + e);
		}
	}

	public void publishKnowledgeObject(ArkId arkId, boolean isToBePublished, OTUser loggedInUser)
			throws ObjectTellerException, URISyntaxException {

		KnowledgeObject ko = getKnowledgeObject(arkId);

		if (ko == null) {
			throw new ObjectTellerException("Unable to retrieve knowledge object: " + arkId.getArkId());
		}

		togglePublishedStatus(arkId, ko.getMetadata(), isToBePublished);

		String name;
		if (loggedInUser != null) {
			name = loggedInUser.getFullName();
		} else {
			name = "Anonymous User";
		}

		List<String> metadata = idService.createBasicMetadata(name, ko.getMetadata().getTitle());
		if (isToBePublished) {
			idService.publish(arkId, metadata);
		} else {
			idService.retract(arkId, metadata);
		}
	}

	private void togglePublishedStatus(ArkId arkId, Metadata metadata, boolean value)
			throws ObjectTellerException, URISyntaxException {

		metadata.setPublished(value);
		addOrEditMetadataToArkId(arkId, metadata);
	}

	public void addOrEditMetadataToArkId(ArkId arkID, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {
		URI objectURI = new URI(fcRepoService.getBaseURI() + arkID.getFedoraPath());
		addOrEditMetadataToURI(objectURI, metadata);
	}

	private void addOrEditMetadataToURI(URI objectURI, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {

		if (metadata == null) {
			return;
		}

		if (metadata.getLicense() != null) {
			IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
			metadata.getLicense().id(iri);
		}

		addCitations(metadata.getCitations(), objectURI);

		// Clearing the citations after they've all been inserted so that we don't try to insert them
		// again when the metadata object is added
		metadata.setCitations(null);

		// Need to set these dates to null for now otherwise the RDF serializer will try to edit them
		// and we're not allowed to set these values directly through a put request
		// There's currently no way to set the fields in the object to not be serialized
		// see pinto issue https://github.com/stardog-union/pinto/issues/14 for updates
		metadata.setLastModified(null);
		metadata.setCreatedOn(null);

		insertRDFData(metadata, objectURI);
	}

	private void addCitations(List<Citation> citations, URI objectURI)
			throws URISyntaxException, ObjectTellerException {
		if (citations != null && !citations.isEmpty()) {
			URI citationParentURI = new URI(objectURI + "/" + ChildType.CITATIONS.getChildType());

			// Rather than comparing the existing list of citations vs the ones being added and edited
			// just delete all of the citations in fedora and re-add them all
			// if this is a performance issue we can go back to looping over everything comparing existing
			// citations vs the ones being added/edited/removed.
			if (fcRepoService.checkIfObjectExists(citationParentURI)) {
				fcRepoService.deleteFedoraResource(citationParentURI);
				fcRepoService.deleteFedoraResource(new URI(citationParentURI + "/fcr:tombstone"));
			}

			fcRepoService.createContainer(objectURI, ChildType.CITATIONS.getChildType());

			for (Citation citation : citations) {

				URI citationURI = fcRepoService.createContainerWithAutoGeneratedName(citationParentURI);
				insertRDFData(citation, citationURI);
			}
		}
	}

	public KnowledgeObject createObject(KnowledgeObject knowledgeObject, OTUser loggedInUser,
			String libraryURL, ArkId existingArkId) throws ObjectTellerException, URISyntaxException {

		URI transactionURI = null;
		String errorMessage = null;
		if (fcRepoService.getBaseURI() != null) {

			ArkId arkId = existingArkId;
			if (arkId == null) {
				arkId = idService.mint();
			} try {
				idService.resolve(arkId);
			} catch (HttpClientErrorException e) {
				try {
					idService.create(arkId);
				} catch (HttpClientErrorException ex) {
					throw new ObjectTellerException("Invalid ark id specified for creating/editing an object. " + arkId.getArkId() + " is not valid. " + ex, ex);
				}
			}

			knowledgeObject.setArkId(arkId);

			String arkIDPath = knowledgeObject.getArkId().getFedoraPath();

			if (arkIDPath != null) {

				transactionURI = fcRepoService.createTransaction();

				URI koParentURI = fcRepoService.createContainer(transactionURI, arkIDPath);

				List<String> metadata = idService.createBasicMetadata(
						(loggedInUser != null ? loggedInUser.getFirst_name() : "AnonymousUser"),
						knowledgeObject.getMetadata().getTitle());
				URI targetURI = new URI(libraryURL + "/" + arkId.getFedoraPath());
				idService.bind(knowledgeObject, metadata, targetURI);

				URI provLogURI = fcRepoService.createContainer(koParentURI,
						ChildType.LOG.getChildType() + "/" + ChildType.CREATEACTIVITY.getChildType());

				addProvMetadataStart(provLogURI, loggedInUser);

				addOrEditMetadataToURI(koParentURI, knowledgeObject.getMetadata());

				fcRepoService.putBinary(knowledgeObject.getPayload().getContent(), koParentURI,
						ChildType.PAYLOAD.getChildType());

				fcRepoService.putBinary(knowledgeObject.getInputMessage(), koParentURI,
						ChildType.INPUT.getChildType());

				fcRepoService.putBinary(knowledgeObject.getOutputMessage(), koParentURI,
						ChildType.OUTPUT.getChildType());

				insertRDFData(knowledgeObject.getPayload(), new URI(koParentURI + "/Payload"));

				addProvMetadataEnd(provLogURI);

				fcRepoService.commitTransaction(transactionURI);

				logger.info("Successfully created object " + knowledgeObject);

			} else
				errorMessage = "Unable to generate unique id for the object " + knowledgeObject;

		} else
			errorMessage = "Exception: Objects can not be created until fedora server is configured.";

		if (errorMessage != null) {
			logger.error(errorMessage);
			ObjectTellerException exception = new ObjectTellerException();
			exception.setErrormessage(errorMessage);
			if (transactionURI != null)
				fcRepoService.rollbackTransaction(transactionURI);
			throw exception;
		}
		return knowledgeObject;
	}

	private void addProvMetadataStart(URI uri, OTUser loggedInUser) throws ObjectTellerException {
		String username = loggedInUser == null ? null : loggedInUser.getFullName();
		ProvenanceLogData provLogData = new ProvenanceLogData(username,
				uri.toString(), username, uri.toString(), new Date(), null);
		insertRDFData(provLogData, uri);
	}

	private void addProvMetadataEnd(URI uri) throws ObjectTellerException {
		ProvenanceLogData provLogData = new ProvenanceLogData(null,
				null, null, null, null, new Date());
		insertRDFData(provLogData, uri);
	}

	private void insertRDFData(Identifiable data, URI objectURI) throws ObjectTellerException {
		IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
		data.id(iri);
		Model dataModel = RDFMapper.builder()
				.namespace(OT_NAMESPACE_PREFIX, OT_NAMESPACE_URL)
				.namespace(FEDORA_NAMESPACE_PREFIX, FEDORA_NAMESPACE_URL)
				.namespace(PROV_NAMESPACE_PREFIX, PROV_NAMESPACE_URL)
				.build().writeValue(data);
		fcRepoService.putRDFData(dataModel, objectURI);
	}
}


