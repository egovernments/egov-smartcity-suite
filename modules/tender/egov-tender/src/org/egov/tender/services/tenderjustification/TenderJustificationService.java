package org.egov.tender.services.tenderjustification;

import org.egov.infstr.services.PersistenceService;
import org.egov.tender.model.TenderJustification;


/**
 * 
 * @author pritiranjan
 *
 */


public class TenderJustificationService extends PersistenceService<TenderJustification,Long>{
	
	
	public TenderJustification save(TenderJustification justification)
	{
		return persist(justification);
	}
	
	public TenderJustification getTenderJustificationById(Long id)
	{
		return findById(id,Boolean.TRUE);
	}
	

}
