package org.egov.tender.services.common;

import java.math.BigDecimal;
import java.util.Map;

import org.egov.tender.interfaces.TenderFile;
import org.egov.tender.interfaces.Tenderable;
import org.egov.tender.interfaces.TenderableGroup;
import org.egov.web.utils.EgovPaginatedList;



public interface TenderFileService {
	
	/**
	 * This API is to get paginated list of TenderFiles (for which tender notice needs to be created) from the Map values that are passed.
	 * @param paramMap
	 * Map<String,Object> paramMap will have the data to search tender files.
	 * <p> fromdate - The date from when the Tender files to be searched.
	 * <p> todate- The date upto which the Tender files to be searched
	 * <p> departmentid- This is the departmentid from the department master 
	 * <p> tenderfilenumber - This is the number of tenderfile to be searched
	 * <p> status - This is the status of tender file  to be searched
	 * <p> tenderFileType
	 * @param pageNumber -This is page number of the paginated list
	 * @param pageSize- This represents the no of elements preset per page.
	 * @return -It returns paginated list of TenderFiles for matching parameters  
	 */
	
	public EgovPaginatedList getAllTenderFilesToCreateTenderNotice(Map<String,Object> paramMap,int pageNumber,int pageSize);
	
	/**
	 * 
	 * @param id
	 * @return-It returns TenderFile by Passing Id
	 */
	
	public TenderFile getTenderFileById(Long id);
	
	/**
	 * Returns the already tendered quantity by looking at the tender files already created from this TenderableGroup and
	 * Tenderable. If the associated Tender Notice is not cancelled but units are cancelled, then that quantity is not considered.
	 * If the associated Tender Notice is cancelled, the Tender file can be reused, so the quantity will be considered from this tender file.
	 * If group is null, we assume that the TenderUnit of TenderNotice is the Tenderable entity.
	 * @param group
	 * @param unit
	 * @return
	 */
	
	public BigDecimal getTenderedQuantity(TenderableGroup group, Tenderable unit);
	

}
