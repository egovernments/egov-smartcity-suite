/*
 * @(#)ContractorLicenseReportService.java 3.0, 29 Jul, 2013 1:24:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.egov.commons.Installment;
import org.egov.infstr.services.Page;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.license.utils.Constants;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Query;

public class ContractorLicenseReportService extends LicenseReportService {

	public EgovPaginatedList getCityReportList(final String pageNo, final String moduleName, final String licenseType) {
		final Installment currentInstallment = getCurrentInstallment(moduleName);
		this.query = constructQuery(Constants.CITY, licenseType, currentInstallment).toString();
		final Query hibQuery = HibernateUtil.getCurrentSession().createSQLQuery(String.valueOf(this.query));
		if (pageNo == null) {
			this.pageNum = 1;
		} else {
			this.pageNum = Integer.valueOf(pageNo);
		}
		final Integer fullSize = hibQuery.list().size();
		final Page page = new Page(hibQuery, this.pageNum, this.pageSize);
		Object[] objects;

		this.pageList = page.getList();
		this.paginateList = new EgovPaginatedList(page, fullSize);
		if (this.pageList != null) {
			final Iterator iterator = this.pageList.iterator();
			while (iterator.hasNext()) {
				objects = (Object[]) iterator.next();
				this.hashMap = new HashMap<String, Object>();
				this.hashMap.put(Constants.NEW_LICENSE_REGISTERED, objects[0]);
				this.hashMap.put(Constants.CANCELLED, objects[1]);
				this.hashMap.put(Constants.OBJECTED, objects[2]);
				this.hashMap.put(Constants.RENEWED, objects[3]);
				this.hashMap.put(Constants.PENDING_RENEWALS, getPendingRenewals(licenseType, Long.valueOf(String.valueOf(objects[5])), null));
				this.hashMap.put(Constants.TOTAL_LICENSES, Long.valueOf(String.valueOf(objects[0])) + Long.valueOf(String.valueOf(objects[3])));
				this.hashMap.put(Constants.ZONE_ID, objects[5]);
				this.hashMap.put(Constants.ZONE, objects[6]);
				this.hashMap.put(Constants.TOTAL_AMOUNT, objects[4]);

				this.licenseList.add(this.hashMap);
			}
		}

		this.paginateList.setList(this.licenseList);

		return this.paginateList;
	}

	private StringBuilder constructQuery(final String boundaryType, final String licenseType, final Installment currentInstallment) {
		final StringBuilder query = new StringBuilder(" select NVL(act, 0) AS act, NVL(can, 0) AS can, NVL(obj, 0) AS obj, NVL(ren, 0) AS ren, NVL(totalamount, 0) AS totalamount,egb.id_bndry bb , egb.name from ")
				.append(" (select boundary.id_bndry, boundary.name from eg_boundary boundary , eg_boundary_type boundarytype ").append(" where boundary.id_bndry_type= boundarytype.id_bndry_type").append(" and boundary.is_history = 'N'");

		query.append(") egb ").append(" left outer join ").append(" (select sum(issueCount) as act ,sum(canCount)as can,sum(objCount)as obj,sum(renCount)as ren ,sum(amount) as totalamount,bb from ")
				.append(" (select case when status.status_name='")
				.append(Constants.LICENSE_STATUS_ACTIVE)
				.append("' and ld.renewal_date is null and ld.id_installment=")
				.append// for Newly issued licenses in the current year
				(currentInstallment.getId()).append(" then 1 else 0 end as issueCount, ").append(" case when status.status_name='").append(Constants.LICENSE_STATUS_CANCELLED).append("' and ld.id_installment=").append(currentInstallment.getId())
				.append(" then 1 else 0 end as canCount, ").append(" case when status.status_name='").append(Constants.LICENSE_STATUS_OBJECTED).append("' and ld.id_installment=").append(currentInstallment.getId()).append(" then 1 else 0 end as objCount, ")
				.append(" case when status.status_name='").append(Constants.LICENSE_STATUS_ACTIVE).append("'and ld.renewal_date is not null and ld.id_installment=")
				.append// for renewed licenses in the current year
				(currentInstallment.getId()).append(" then 1 else 0 end as renCount, ").append(" case when status.status_name='").append(Constants.LICENSE_STATUS_ACTIVE).append("' and ld.id_installment=").append(currentInstallment.getId())
				.append(" then demand.base_demand else 0 end as amount, ");// to get the amount for new and renewed licenses in the current year
		if (boundaryType.equalsIgnoreCase(Constants.CITY)) {
			query.append(" boun.id_bndry as bb");
		}
		query.append(" from egl_license lic, egl_mstr_status status,eg_boundary boun  , egl_license_demand ld , eg_demand demand  where lic.id_status=status.id_status ").append(" and  status.status_name in('").append(Constants.LICENSE_STATUS_ACTIVE)
				.append("','").append(Constants.LICENSE_STATUS_CANCELLED).append("','").append(Constants.LICENSE_STATUS_OBJECTED).append("') and lic.license_type='").append(licenseType).append("' and boun.id_bndry= lic.id_adm_bndry")
				.append(" and boun.is_history = 'N'").append(" and lic.id= ld.id_license and ld.id_demand=demand.id )group by bb) t ").append(" on egb.ID_BNDRY = t.bb	order by LPAD(name,10) ");
		return query;

	}

	private Object getPendingRenewals(final String licenseType, final Long boundaryId, final Long subcategoryId) {
		final StringBuilder query = new StringBuilder(" select NVL(SUM(pren1)+SUM(pren2),0) from (SELECT ").append(" CASE WHEN expired = 0 AND months_between(dateofexpiry, sysdate)<1 THEN 1 ELSE 0 END AS pren1 , ")
				.append(" CASE WHEN expired = 1 AND months_between(dateofexpiry, sysdate)>-6 THEN 1 ELSE 0 END AS pren2  FROM ")
				.append(" (SELECT CASE WHEN sysdate<dateofexpiry THEN 0 ELSE 1 END AS expired,id_adm_bndry,dateofexpiry, license_type, id_status,id_sub_category ").append(" FROM egl_license) lic ,egl_mstr_status status,eg_boundary boun ")
				.append(" WHERE lic.id_status=status.id_status AND status.status_name ='").append(Constants.LICENSE_STATUS_ACTIVE).append("' AND lic.license_type='").append(licenseType).append("' ").append(" AND boun.id_bndry = lic.id_adm_bndry ");
		if (boundaryId != null && boundaryId > 0) {
			query.append(" and boun.id_bndry=").append(boundaryId);
		}
		if (subcategoryId != null && subcategoryId > 0) {
			query.append(" and lic.id_sub_category=").append(subcategoryId);
		}
		query.append(" )");
		final Query hibQuery = HibernateUtil.getCurrentSession().createSQLQuery(String.valueOf(query));
		final List result = hibQuery.list();

		return result.get(0);

	}

	public EgovPaginatedList getLateRenewalsListReportForContractor(final String pageNo, final String moduleName, final String licenseType) {
		final Installment currentInstallment = getCurrentInstallment(moduleName);
		this.query = constructQueryForLateRenewalsList(licenseType, currentInstallment).toString();
		final Query hibQuery = HibernateUtil.getCurrentSession().createSQLQuery(String.valueOf(this.query));
		if (pageNo == null) {
			this.pageNum = 1;
		} else {
			this.pageNum = Integer.valueOf(pageNo);
		}
		final Integer fullSize = hibQuery.list().size();
		final Page page = new Page(hibQuery, this.pageNum, this.pageSize);
		Object[] objects;

		this.pageList = page.getList();
		this.paginateList = new EgovPaginatedList(page, fullSize);
		if (this.pageList != null) {
			final Iterator iterator = this.pageList.iterator();
			while (iterator.hasNext()) {
				objects = (Object[]) iterator.next();
				this.hashMap = new HashMap<String, Object>();
				this.hashMap.put(Constants.NO_OF_LATE_RENEWALS, objects[0]);
				this.licenseList.add(this.hashMap);
			}
		}

		this.paginateList.setList(this.licenseList);
		return this.paginateList;
	}

	private StringBuilder constructQueryForLateRenewalsList(final String licenseType, final Installment currentInstallment) {
		final StringBuilder query = new StringBuilder(" select NVL(lateren, 0) AS lateren, egb.bndry_num,egb.id_bndry bb , egb.name from ")
				.append(" (select boundary.id_bndry,boundary.bndry_num, boundary.name from eg_boundary boundary , eg_boundary_type boundarytype ").append(" where boundarytype.name='").append(Constants.CITY)
				.append("' and boundary.id_bndry_type= boundarytype.id_bndry_type");

		query.append(") egb ").append(" left outer join ").append(" (select sum(laterenCount) as lateren ,bb from ").append(" (select case when status.status_name='").append(Constants.LICENSE_STATUS_ACTIVE)
				.append("' and ld.renewal_date is not null AND ld.is_laterenewal='1' and ld.id_installment=").append// for Lately renewed licenses in the current year
				(currentInstallment.getId()).append(" then 1 else 0 end as laterenCount, boun.id_bndry as bb");

		query.append(" from  egl_license lic, egl_mstr_status status,eg_boundary boun  , egl_license_demand ld  where lic.id_status=status.id_status ").append(" and  status.status_name in('").append(Constants.LICENSE_STATUS_ACTIVE)
				.append("') and lic.license_type='").append(licenseType).append("' and boun.id_bndry= lic.id_adm_bndry").append(" and lic.id= ld.id_license )group by bb) t ").append(" on egb.ID_BNDRY = t.bb	order by LPAD(name,10) ");

		return query;
	}

	public List<Map<String, Object>> getTotalsForCityReport(final String moduleName, final String licenseType) {
		final Installment currentInstallment = getCurrentInstallment(moduleName);
		this.query = constructQuery(Constants.CITY, licenseType, currentInstallment).toString();
		this.query = "Select sum(act),sum(can),sum(obj),sum(ren),sum(totalamount) from(" + this.query + ")";
		return getTotalList(this.query, licenseType);
	}
}
