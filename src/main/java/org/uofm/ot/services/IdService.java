package org.uofm.ot.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.KnowledgeObject;

@Service
public class IdService {

//	@Autowired
//	ServletContext servletContext;

	@Autowired
	private EzidService ezidService;

	public IdService(EzidService ezidService) {
		this.ezidService = ezidService ;
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

	public KnowledgeObject bind(KnowledgeObject ko,  List<String> metadata, URI targetUrl) {

		ArkId arkId = ko.getArkId();

		ezidService.bind(arkId.getArkId(), metadata, targetUrl);

		return ko;
	}
	
	public List<String> createBasicMetadata(String who, String what){
		List<String> metadata = new ArrayList<String>();
		metadata.add("erc.who: "+ who); 
		metadata.add("erc.what: "+ what);
		metadata.add("erc.when: "+new Date().toString());
		return metadata;
	}
}
