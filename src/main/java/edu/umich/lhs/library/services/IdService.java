package edu.umich.lhs.library.services;

import edu.umich.lhs.library.ezidGateway.EzidService;
import edu.umich.lhs.library.ezidGateway.DummyIdService;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.umich.lhs.library.exception.LibraryException;
import edu.umich.lhs.library.knowledgeObject.ArkId;

@Service
public class IdService {

	@Value("${ezid.mock}")
	private String mockEzid;

	private EzidService ezidService;
	private DummyIdService dummyIdService;

	@Autowired
	public IdService(EzidService ezidService, DummyIdService dummyIdService) {
	 this.ezidService = ezidService;
	 this.dummyIdService = dummyIdService;
	}

	@PostConstruct
	private void initService() {
		if (("true").equals(this.mockEzid)) {
			this.ezidService = this.dummyIdService;
		}
	}

	public ArkId mint() {
        return new ArkId(ezidService.mint());
    }

	public void create(ArkId arkId) {
		ezidService.create(arkId.getArkId());
	}
    
	public void publish(ArkId arkId, List<String> metadata) {
		ezidService.status(arkId.getArkId(), metadata ,ArkId.Status.PUBLIC);
	}
	
	public void retract(ArkId arkId,  List<String> metadata){
		ezidService.status(arkId.getArkId(), metadata ,ArkId.Status.UNAVAILABLE);
	}
	
	public String resolve (ArkId arkId){
		// TODO: 1. fuseki 2. ezid
		return ezidService.get(arkId.getArkId());
	}

	public boolean ping() throws LibraryException {
		return ezidService.ping();
	}

	public void bind(ArkId arkId,  List<String> metadata, URI targetUrl) {

		ezidService.bind(arkId.getArkId(), metadata, targetUrl);
	}
	
	public List<String> createBasicMetadata(String who, String what){
		List<String> metadata = new ArrayList<String>();
		metadata.add("erc.who: "+ who); 
		metadata.add("erc.what: "+ what);
		metadata.add("erc.when: "+new Date().toString());
		return metadata;
	}
}
