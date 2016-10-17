package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uofm.ot.knowledgeObject.ArkId;
import org.uofm.ot.knowledgeObject.FedoraObject;

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
    
	public void publish(ArkId arkId) {
		ezidService.status(arkId.getArkId(), ArkId.Status.PUBLIC);
	}
	
	public void retract(ArkId arkId){

		ezidService.status(arkId.getArkId(), ArkId.Status.UNAVAILABLE);
	}
	
	public void resolve (String arkId){
		// TODO: 1. fuseki 2. ezid
	}

	public FedoraObject bind(FedoraObject ko, String targetUrl) {

		String arkId = ezidService.mint();

		ko.setURI(arkId);

		ezidService.bind(arkId,targetUrl);

		return ko;
	}
}
