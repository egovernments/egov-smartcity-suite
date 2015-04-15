/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
		final Query qry = getCurrentSession().createQuery("from Chequedetail cd where cd.voucherheader =:voucherHeader");
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
		final Query qry = getCurrentSession().createQuery(qryStr.toString());

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
