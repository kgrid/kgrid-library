package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uofm.ot.knowledgeObject.FedoraObject;

/**
 * Created by pboisver on 9/15/16.
 */
@Service
public class IdService {

//	@Autowired
//	ServletContext servletContext;

	@Autowired
	private EzidService ezidService;

	public IdService(EzidService ezidService) {
		this.ezidService = ezidService ;
	}

	public String mint() {
        return ezidService.mint();
    }    
    
	public void publish(FedoraObject ko) {
		ezidService.status(ko.getURI(), IDStatus.PUBLIC);
	}
	
	public void retract(FedoraObject ko){
		ezidService.status(ko.getURI(), IDStatus.UNAVAILABLE);
	}
	
	public void resolve (String arkId){
		// TODO: 1. fuseki 2. ezid
	}

	public FedoraObject bind(FedoraObject ko) {

		String arkId = ezidService.mint();

		ko.setURI(arkId);

		ezidService.bind(arkId,ko.getURL());

		return ko;
	}
}
