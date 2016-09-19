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

	public String mint() {
        return ezidService.mint();
    }

	public void setEzidService(EzidService ezidService) {
		this.ezidService = ezidService;
	}
    
    
}
