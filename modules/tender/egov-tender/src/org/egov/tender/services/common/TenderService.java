package org.egov.tender.services.common;

import java.util.Map;

/**
 * 
 * @author pritiranjan
 *
 */

public interface TenderService {
		
	//public EgovPaginatedList getAcceptedTenderResponse(Map<String,Object> paramMap,int pageNumber);
	public Map<String,String> getApprovedEntityForBidResponse(String bidResponseNumber);
}
