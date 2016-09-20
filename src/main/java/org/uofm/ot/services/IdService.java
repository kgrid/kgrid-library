package org.uofm.ot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by pboisver on 9/15/16.
 */
@Service
public class IdService {
	
	@Autowired
	private EzidService ezidService;

	public IdService(EzidService ezidService) {
		this.ezidService = ezidService ;
	}

	public String mint() {
        return ezidService.mint();
    }    
    
	public void publish(String id) {
		ezidService.status(id, IDStatus.PUBLIC);
	}
	
	public void retract(String id){
		ezidService.status(id, IDStatus.UNAVAILABLE);
	}
	
	public void resolve (String arkId){
		// TODO: 1. fuseki 2. ezid
	}
}
