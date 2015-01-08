/*
 * @(#)ChequedetailHibernateDAO.java 3.0, 11 Jun, 2013 2:16:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.dao;

import java.util.Date;
import java.util.List;

import org.egov.commons.CVoucherHeader;
import org.egov.commons.Chequedetail;
import org.egov.commons.Functionary;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Query;
import org.hibernate.Session;

public class ChequedetailHibernateDAO extends GenericHibernateDAO {

	public ChequedetailHibernateDAO() {
		super(Chequedetail.class, null);
	}

	public ChequedetailHibernateDAO(final Class persistentClass, final Session session) {
		super(persistentClass, session);

	}

	public List<Chequedetail> getChequedetailByVoucherheader(final CVoucherHeader voucherHeader) {
		final Query qry = getSession().createQuery("from Chequedetail cd where cd.voucherheader =:voucherHeader");
		qry.setEntity("voucherHeader", voucherHeader);
		return qry.list();
	}

	public List<Chequedetail> getChequedetailFilterBy(final String vhNo, final Date vhDateFrom, final Date vhDateTo, final String chqNo, final String mode, final String pymntType, final String dept, final Functionary functionary) {
		final StringBuffer qryStr = new StringBuffer();
		qryStr.append(" from Chequedetail cd where cd.voucherheader.name='" + pymntType + "' and cd.voucherheader.type='Payment'");

		if (mode.equalsIgnoreCase("view")) {
			qryStr.append("  and cd.voucherheader.status in(0,1,2)");
		} else if (mode.equalsIgnoreCase("reverse")) {

			qryStr.append("  and cd.voucherheader.status=0 and cd.voucherheader.isConfirmed=1 and cd.voucherheader.id in(select br.voucherheader.id from Bankreconciliation br where br.isreconciled!=1 and cd.voucherheader.id = br.voucherheader.id)");
		} else {

			qryStr.append("  and cd.voucherheader.status=0 and cd.voucherheader.isConfirmed=0 and cd.voucherheader.id in(select br.voucherheader.id from Bankreconciliation br where br.isreconciled!=1 and cd.voucherheader.id = br.voucherheader.id)");
		}
		if (vhDateFrom != null) {
			qryStr.append(" and cd.voucherheader.voucherDate >=:DateFrom");
		}
		if (vhDateFrom != null) {
			qryStr.append(" and cd.voucherheader.voucherDate <=:DateTo");
		}
		if (vhNo != null) {
			qryStr.append(" and cd.voucherheader.voucherNumber =:vhNo");
		}

		if (chqNo != null) {
			qryStr.append(" and cd.chequenumber=:chqNo");
		}
		if (dept != null) {
			qryStr.append(" and cd.voucherheader.id in (select vm.voucherheaderid from Vouchermis vm where  vm.departmentid=:dept ) ");

		}
		if (functionary != null) {
			qryStr.append(" and cd.voucherheader.id in (select vm.voucherheaderid from Vouchermis vm where  vm.functionary=:functionary ) ");
		}
		qryStr.append(" order by cd.voucherheader.voucherDate asc");
		final Query qry = getSession().createQuery(qryStr.toString());

		if (vhDateFrom != null) {
			qry.setDate("DateFrom", vhDateFrom);
		}
		if (vhDateTo != null) {
			qry.setDate("DateTo", vhDateTo);
		}
		if (vhNo != null) {
			qry.setString("vhNo", vhNo);
		}
		if (chqNo != null) {
			qry.setString("chqNo", chqNo);
		}
		if (dept != null) {
			qry.setInteger("dept", Integer.parseInt(dept));
		}
		if (functionary != null) {
			qry.setEntity("functionary", functionary);
		}

		return qry.list();
	}

}
