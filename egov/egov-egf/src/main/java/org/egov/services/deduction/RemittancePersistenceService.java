package org.egov.services.deduction;

import org.egov.deduction.model.EgRemittance;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;

public class RemittancePersistenceService  extends PersistenceService<EgRemittance, Long>{
   private PersistenceService persistenceService;
public RemittancePersistenceService() {
		super();
		setType(EgRemittance.class);
		//This fix is for Phoenix Migration.setSessionFactory(new SessionFactory());
		// TODO Auto-generated constructor stub
	}

	@Override
	public EgRemittance persist(EgRemittance model) {
		// TODO Auto-generated method stub
		return super.persist(model);
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	

}
