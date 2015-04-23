package org.egov.services.masters;

import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.commons.Accountdetailkey;
import org.egov.deduction.model.EgRemittance;
import org.egov.eb.domain.master.entity.EBConsumer;

public class AccountdetailkeyService extends PersistenceService<Accountdetailkey, Integer> {
	 private PersistenceService persistenceService;
	 public AccountdetailkeyService() {
			super();
			setType(Accountdetailkey.class);
			//This fix is for Phoenix Migration.setSessionFactory(new SessionFactory());
			// TODO Auto-generated constructor stub
		}

		@Override
		public Accountdetailkey persist(Accountdetailkey model) {
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
