package org.egov.tender.services.common;

import java.util.List;
import java.util.Map;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.utils.TenderConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;



/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("rawtypes")
public class GenericTenderNoticeService {

	private PersistenceService persistenceService;
	public static final String DEPARTMENT = "department";
	public static final String FILENUMBER = "fileNumber";


	/**
	 * This Api returns list of tender notices for a particular department and tender file number.
	 * @param paramMap
	 * @return
	 */

	public List<TenderNotice> searchTenderNotice(Map<String,Object> paramMap)
	{
		Criteria noticeCriteria = persistenceService.getSession().createCriteria(TenderNotice.class);

		if(null != paramMap.get(DEPARTMENT))
			noticeCriteria.add(Restrictions.eq("department.id",(Integer)paramMap.get(DEPARTMENT)));

		if(null != paramMap.get(FILENUMBER) && StringUtils.isNotEmpty((String)paramMap.get(FILENUMBER)))
			noticeCriteria.add(Restrictions.eq("tenderFileRefNumber",(String)paramMap.get(FILENUMBER)));

		return noticeCriteria.list();
	}


	public TenderNotice getTenderNoticeByNumber(String number)
	{
		Criteria noticeCriteria = persistenceService.getSession().createCriteria(TenderNotice.class);
		noticeCriteria.createAlias("status", "statusObj");
		noticeCriteria.add(Restrictions.eq("number", number));
		noticeCriteria.add(Restrictions.eq("statusObj.code", TenderConstants.TENDERNOTICE_APPROVED));
		List<TenderNotice> noticeList = noticeCriteria.list();

		if(noticeList.isEmpty())
			return null;
		else
			return noticeList.get(0);
	}



	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
