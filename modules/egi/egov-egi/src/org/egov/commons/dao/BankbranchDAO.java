package org.egov.commons.dao;

import org.egov.commons.Bankbranch;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class BankbranchDAO {

	private Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	public void createBankbranch(final Bankbranch bankbranch) {
		try {
			getSession().save(bankbranch);

		} catch (final Exception e) {
			throw new EGOVRuntimeException((new StringBuilder("Hibernate Exception : ")).append(e.getMessage()).toString());
		}
	}

	public void updateBankbranch(final Bankbranch bankbranch) {
		try {
			getSession().saveOrUpdate(bankbranch);
		} catch (final Exception e) {
			throw new EGOVRuntimeException((new StringBuilder("Hibernate Exception : ")).append(e.getMessage()).toString());
		}
	}

	public void removeBankbranch(final Bankbranch bankbranch) {
		try {
			getSession().delete(bankbranch);
		} catch (final Exception e) {
			throw new EGOVRuntimeException((new StringBuilder("Hibernate Exception : ")).append(e.getMessage()).toString());
		}
	}

	public Bankbranch getBankbranchById(final int bankbranch) {
		try {

			return (Bankbranch) getSession().get(Bankbranch.class, bankbranch);
		} catch (final Exception e) {
			throw new EGOVRuntimeException((new StringBuilder("Hibernate Exception : ")).append(e.getMessage()).toString());
		}
	}

}
