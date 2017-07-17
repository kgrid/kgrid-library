package org.uofm.ot.services;


import com.complexible.pinto.Identifiable;
import com.complexible.pinto.RDFMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.SimpleValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.uofm.ot.exception.ObjectNotFoundException;
import org.uofm.ot.exception.ObjectTellerException;
import org.uofm.ot.fedoraGateway.ChildType;
import org.uofm.ot.fedoraGateway.FCRepoService;
import org.uofm.ot.fusekiGateway.FusekiService;
import org.uofm.ot.fusekiGateway.NamespaceConstants;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.Citation;
import org.uofm.ot.knowledgeObject.KnowledgeObject;
import org.uofm.ot.knowledgeObject.Metadata;
import org.uofm.ot.knowledgeObject.Payload;
import org.uofm.ot.knowledgeObject.ProvenanceLogData;
import org.uofm.ot.knowledgeObject.Version;
import org.uofm.ot.model.OTUser;


@Service
public class KnowledgeObjectService {

	@Autowired
	private IdService idService;

	@Autowired
	private FusekiService fusekiService;

	@Autowired
	private FCRepoService fcRepoService;

	@Autowired
	private VersioningService versioningService;

	private static final Logger logger = Logger.getLogger(KnowledgeObjectService.class);


	// We can use namespace objects (like below) or a list of namespaces instead of passing namespace
	// strings to the serializer when this pinto bug is fixed: https://github.com/stardog-union/pinto/issues/21
	//private static final Namespace OT_NAMESPACE = new SimpleNamespace("ot", "http://uofm.org/objectteller/");

	public KnowledgeObject getKnowledgeObject(ArkId arkId) throws ObjectTellerException, URISyntaxException{
		URI metadataURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		Metadata metadata;
		try {
			metadata = fetchAndDeserializeRDFData(Metadata.class, metadataURI,
					metadataURI.toString());
		} catch (NullPointerException e) {
			throw new ObjectNotFoundException("Cannot find knowledge object with ark id " + arkId);
		}

		URI citationContainerURI = constructURI(metadataURI, ChildType.CITATIONS.getChildType());
		List<URI> citationURIs = fcRepoService.getChildrenURIs(citationContainerURI);
		ArrayList<Citation> citations = new ArrayList<>();
		for (URI citationURI : citationURIs) {
			citations.add(fetchAndDeserializeRDFData(Citation.class, citationURI, citationURI.toString()));
		}

		metadata.setCitations(citations);
		KnowledgeObject object = new KnowledgeObject();
		object.setMetadata(metadata);
		object.setURI(arkId.toString());
		object.setArkId(arkId);
		return object;
	}

	public List<KnowledgeObject> getKnowledgeObjects(boolean onlyPublished) throws ObjectTellerException {
		return fusekiService.getKnowledgeObjects(onlyPublished);
	}

	public KnowledgeObject getCompleteKnowledgeObject(ArkId arkId) throws ObjectTellerException, URISyntaxException {

		KnowledgeObject knowledgeObject = getKnowledgeObject(arkId);

		if (knowledgeObject != null) {

			knowledgeObject.setPayload(getPayload(arkId));

			knowledgeObject.setLogData(getProvData(arkId).toString());

			knowledgeObject.setInputMessage(getInputMessageContent(arkId));

			knowledgeObject.setOutputMessage(getOutputMessageContent(arkId));
		}

		return knowledgeObject;
	}

	public void deleteObject(ArkId arkId) throws ObjectTellerException {
		fcRepoService.deleteFedoraResource(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
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

	public ProvenanceLogData getProvData(ArkId arkId) throws ObjectTellerException {
		URI provDataURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), ChildType.LOG.getChildType(), ChildType.CREATEACTIVITY.getChildType());
		return  fetchAndDeserializeRDFData(ProvenanceLogData.class, provDataURI, provDataURI.toString());
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
		URI payloadURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType());
		URI payloadMetadataURI = constructURI(payloadURI, "fcr:metadata");
		return fetchAndDeserializeRDFData(Payload.class, payloadMetadataURI, payloadURI.toString());
	}

	public void editPayload(ArkId arkId, Payload payload) throws ObjectTellerException, URISyntaxException {
		fcRepoService.putBinary(payload.getContent(), arkId, ChildType.PAYLOAD.getChildType());
		// Clear the content so it doesn't appear in fedora as a triple and not a binary
		payload.setContent(null);
		serializeAndInsertRDFData(payload, constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), ChildType.PAYLOAD.getChildType()));
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
		return fcRepoService.checkIfObjectExists(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
	}

	public boolean exists(ArkId arkId, Version version) throws ObjectTellerException {
		return fcRepoService.checkIfObjectExists(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), version.getFedoraVersion()));
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

		metadata.setPublished(value ? "yes" : "no");
		addOrEditMetadataToArkId(arkId, metadata);
	}

	public void addOrEditMetadataToArkId(ArkId arkID, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {
		URI objectURI = constructURI(fcRepoService.getBaseURI(), arkID.getFedoraPath());
		addOrEditMetadataToURI(objectURI, metadata);
	}

	public void addOrEditMetadataToArkId(ArkId arkID, Version version, Metadata metadata)
			throws ObjectTellerException, URISyntaxException {
		URI objectURI = constructURI(fcRepoService.getBaseURI(), arkID.getFedoraPath(), version.getFedoraVersion());
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

		if (metadata.getVersion() == null || metadata.getVersion().equals("")) {
			metadata.setVersion(new Version());
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

		serializeAndInsertRDFData(metadata, objectURI);
	}

	private void addCitations(List<Citation> citations, URI objectURI)
			throws URISyntaxException, ObjectTellerException {
		if (citations != null && !citations.isEmpty()) {
			URI citationParentURI = constructURI(objectURI, ChildType.CITATIONS.getChildType());

			// Rather than comparing the existing list of citations vs the ones being added and edited
			// just delete all of the citations in fedora and re-add them all
			// if this is a performance issue we can go back to looping over everything comparing existing
			// citations vs the ones being added/edited/removed.
			if (fcRepoService.checkIfObjectExists(citationParentURI)) {
				fcRepoService.deleteFedoraResource(citationParentURI);
				fcRepoService.deleteFedoraResource(constructURI(citationParentURI, "fcr:tombstone"));
			}

			fcRepoService.createContainer(objectURI, ChildType.CITATIONS.getChildType());

			for (Citation citation : citations) {

				URI citationURI = fcRepoService.createContainerWithAutoGeneratedName(citationParentURI);
				serializeAndInsertRDFData(citation, citationURI);
			}
		}
	}

	public KnowledgeObject createVersion(ArkId arkId, String versionString) throws ObjectTellerException, URISyntaxException {
		KnowledgeObject ko = getKnowledgeObject(arkId);
		Version oldVersion = new Version(ko.getMetadata().getVersion());
		Version version;
		if(versionString == null || versionString.equals("")) {
			if(ko.getMetadata() != null && ko.getMetadata().getVersion() != null) {
				version = new Version(ko.getMetadata().getVersion()).incPatch();
			} else {
				version = new Version();
			}
		} else {
			version = new Version(versionString);
			if(version.usesSemVer() && oldVersion.usesSemVer() && version.compareTo(oldVersion) < 0) {
				throw new ObjectTellerException("Cannot create a version with a number before the current version. " +
				versionString + " is not after " + ko.getMetadata().getVersion());
			}
		}
		try {
			ko.getMetadata().setVersion(version);
			editObject(ko, arkId);
			versioningService.createNewVersionSnapshot(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()),
							version.toString());
		} catch (ObjectTellerException e) {
			ko.getMetadata().setVersion(oldVersion);
			editObject(ko, arkId);
			throw new ObjectTellerException(e);
		}
		return ko;
	}

	public List<String> getVersions(ArkId arkId) throws ObjectTellerException, URISyntaxException {
		Model versionModel = versioningService.listVersions(constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath()));
		List<String> versionList = new ArrayList<>();
		for (Statement statement : versionModel) {
			if(statement.getPredicate().toString().endsWith("hasVersionLabel")) {
				String versionName = statement.getObject().stringValue();
				if (new Version(versionName).usesSemVer()) {
					versionName = versionName.replace(".", "-");
				}
				versionList.add(versionName);
			}
		}
		return versionList;
	}

	public KnowledgeObject getVersionSnapshot(ArkId arkId, Version version) throws ObjectTellerException, URISyntaxException {
		URI metadataURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath(), "fcr:versions", version.toString());

		Metadata metadata = fetchAndDeserializeRDFData(Metadata.class, metadataURI, metadataURI.toString());

		URI citationContainerURI = constructURI(metadataURI, ChildType.CITATIONS.getChildType());
		List<URI> citationURIs = fcRepoService.getChildrenURIs(citationContainerURI);
		ArrayList<Citation> citations = new ArrayList<>();
		for (URI citationURI : citationURIs) {
			citations.add(fetchAndDeserializeRDFData(Citation.class, citationURI, citationURI.toString()));
		}

		metadata.setCitations(citations);
		KnowledgeObject object = new KnowledgeObject();
		object.setMetadata(metadata);
		object.setURI(arkId.toString());
		object.setArkId(arkId);
		return object;
	}

	public void rollbackToPriorVersionSnapshot(ArkId arkId, Version version) throws ObjectTellerException, URISyntaxException {
		URI koVersionURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		versioningService.revertToPreviousVersion(koVersionURI, version.toString());
	}

	public void deleteVersion(ArkId arkId, Version version) throws ObjectTellerException, URISyntaxException {
		URI versionURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
		versioningService.deleteVersion(versionURI, version.toString());
	}

	public KnowledgeObject editObject(KnowledgeObject newObject, ArkId arkId)
			throws ObjectTellerException, URISyntaxException {

		addOrEditMetadataToArkId(arkId, newObject.getMetadata());
		editPayload(arkId, newObject.getPayload());
		editInputMessageContent(arkId, newObject.getInputMessage());
		editOutputMessageContent(arkId, newObject.getOutputMessage());
		return getCompleteKnowledgeObject(arkId);
	}

	public KnowledgeObject createNewKnowledgeObject(KnowledgeObject knowledgeObject, OTUser loggedInUser)
			throws ObjectTellerException, URISyntaxException {

		ArkId arkId = idService.mint();

		knowledgeObject.setArkId(arkId);
		Version version = new Version();
		knowledgeObject.getMetadata().setVersion(new Version());

		return createKnowledgeObject(arkId, version, loggedInUser, knowledgeObject);
	}

	public KnowledgeObject copyKnowledgeObject(KnowledgeObject knowledgeObject, OTUser loggedInUser,
			boolean newVersion, boolean incrementMajor, boolean incrementMinor, boolean incrementPatch, String tagLabel)
			throws ObjectTellerException, URISyntaxException {
		ArkId arkId = knowledgeObject.getArkId();
		try {
			idService.resolve(arkId);
		} catch (HttpClientErrorException e) {
			try {
				idService.create(arkId);
			} catch (HttpClientErrorException ex) {
				throw new ObjectTellerException(
						"Invalid ark id specified for editing an object. " + arkId.getArkId()
								+ " is not valid. " + ex, ex);
			}
		}

		Version version = knowledgeObject.getMetadata().getVersion() == null ? new Version() : new Version(knowledgeObject.getMetadata().getVersion());
		if (newVersion) {
			if (incrementMajor)
				version = version.incMajor();
			if (incrementMinor)
				version = version.incMinor();
			if (incrementPatch)
				version = version.incPatch();
			if (tagLabel != null && !tagLabel.equals(""))
				version = version.setTag(tagLabel);
		}

		return createKnowledgeObject(arkId, version, loggedInUser, knowledgeObject);
	}

	private KnowledgeObject createKnowledgeObject(ArkId arkId, Version version, OTUser loggedInUser, KnowledgeObject knowledgeObject)
			throws ObjectTellerException, URISyntaxException {
		URI transactionURI = fcRepoService.createTransaction();
		try {
			URI koURI = fcRepoService.createContainer(constructURI(transactionURI, arkId.getFedoraPath()));

			List<String> metadata = idService.createBasicMetadata(
					(loggedInUser != null ? loggedInUser.getFirst_name() : "AnonymousUser"), knowledgeObject.getMetadata().getTitle());
			URI targetURI = constructURI(fcRepoService.getBaseURI(), arkId.getFedoraPath());
			idService.bind(arkId, metadata, targetURI);

			URI provLogURI = fcRepoService.createContainer(
					constructURI(koURI, ChildType.LOG.getChildType(), ChildType.CREATEACTIVITY.getChildType()));

			addProvMetadataStart(provLogURI, loggedInUser);
			knowledgeObject.setArkId(arkId);
			knowledgeObject.getMetadata().setVersion(new Version());
			addOrEditMetadataToURI(koURI, knowledgeObject.getMetadata());

			fcRepoService.putBinary(knowledgeObject.getPayload().getContent(), koURI,
					ChildType.PAYLOAD.getChildType());
			fcRepoService.putBinary(knowledgeObject.getInputMessage(), koURI,
					ChildType.INPUT.getChildType());
			fcRepoService.putBinary(knowledgeObject.getOutputMessage(), koURI,
					ChildType.OUTPUT.getChildType());
			// Clear the content so it doesn't appear in fedora as a triple and not a binary
			knowledgeObject.getPayload().setContent(null);
			serializeAndInsertRDFData(knowledgeObject.getPayload(),
					constructURI(koURI, ChildType.PAYLOAD.getChildType()));
			addProvMetadataEnd(provLogURI);

		} catch (ObjectTellerException e) {
			fcRepoService.rollbackTransaction(transactionURI);
		}
		fcRepoService.commitTransaction(transactionURI);
		logger.info("Successfully created object " + knowledgeObject);
		return knowledgeObject;
	}

	private void addProvMetadataStart(URI uri, OTUser loggedInUser) throws ObjectTellerException {
		String username = loggedInUser == null ? null : loggedInUser.getFullName();
		ProvenanceLogData provLogData = new ProvenanceLogData(username,
				uri.toString(), username, uri.toString(), new Date(), null);
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void addProvMetadataEnd(URI uri) throws ObjectTellerException {
		ProvenanceLogData provLogData = new ProvenanceLogData(null,
				null, null, null, null, new Date());
		serializeAndInsertRDFData(provLogData, uri);
	}

	private void serializeAndInsertRDFData(Identifiable data, URI objectURI) throws ObjectTellerException {
		IRI iri = SimpleValueFactory.getInstance().createIRI(objectURI.toString());
		data.id(iri);
		Model dataModel = RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().writeValue(data);
		fcRepoService.putRDFData(dataModel, objectURI);
	}

	private <T> T fetchAndDeserializeRDFData(Class<T> clazz, URI objectURI, String iri) throws ObjectTellerException{
		Model model = fcRepoService.getRDFData(objectURI);
		return RDFMapper.builder()
				.namespace(NamespaceConstants.OT_NAMESPACE_PREFIX, NamespaceConstants.OT_NAMESPACE_URL)
				.namespace(NamespaceConstants.FEDORA_NAMESPACE_PREFIX, NamespaceConstants.FEDORA_NAMESPACE_URL)
				.namespace(NamespaceConstants.PROV_NAMESPACE_PREFIX, NamespaceConstants.PROV_NAMESPACE_URL)
				.build().readValue(model, clazz, SimpleValueFactory.getInstance().createIRI(iri));
	}

	private static URI constructURI(String... parts) {
		StringBuilder uriString = new StringBuilder("");
		URI uri = null;
		for (String part : parts) {
			if(uriString.length() < 1 || uriString.toString().endsWith("/"))
				uriString.append(part);
			else
				uriString.append("/").append(part);
		}
		try {
			uri = new URI(uriString.toString());
		} catch (URISyntaxException e) {
			logger.error("Invalid uri " + uriString);
		}
		return uri;
	}

	private static URI constructURI(URI rootURI, String... parts) {
		String[] url = new String[parts.length + 1];
		url[0] = rootURI.toString();
		System.arraycopy(parts, 0, url, 1, parts.length);
		return constructURI(url);
	}

}


