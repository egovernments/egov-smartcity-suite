/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.application.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.wtms.application.entity.WaterChargeMaterlizeView;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArrearRegisterReportService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<WaterChargeMaterlizeView> prepareQueryforArrearRegisterReport(final Long wardId, final Long areaId) {
        // Get current installment
        final Installment currentInst = connectionDemandService
                .getCurrentInstallment(WaterTaxConstants.WATER_RATES_NONMETERED_PTMODULE, null, new Date());
        final StringBuilder query = new StringBuilder();
        List<WaterChargeMaterlizeView> propertyViewList;

        query.append(
                "select pmv  from WaterChargeMaterlizeView pmv,InstDmdCollResponse idc,Installment instmallment where "
                        + "pmv.connectiondetailsid = idc.waterMatView.connectiondetailsid and pmv.connectionstatus = 'ACTIVE'"
                        + " and idc.installment.id =instmallment.id  " + " and instmallment.fromDate not between  ('"
                        + currentInst.getFromDate() + "') and ('" + currentInst.getToDate() + "') ");

        // Query that retrieves all the properties that has arrears.
        /*
         * query.append(
         * "select distinct pmv  as \"hscno\" from egwtr_mv_dcb_view pmv,egwtr_mv_inst_dem_coll idc,EG_INSTALLMENT_MASTER instmallment where "
         * +
         * "pmv.connectiondetailsid = idc.connectiondetailsid and pmv.connectionstatus = 'ACTIVE'"
         * + " and idc.id_installment =instmallment.id  " +
         * " and instmallment.start_date not between  ('" +
         * currentInst.getFromDate() + "') and ('" + currentInst.getToDate() +
         * "') ");
         */

        /*--
         * if (localityId != null && localityId != -1) query.append(
         * " and pmv.locality.id= :localityId ");
         */
        /*
         * if (zoneId != null && zoneId != -1) query.append(
         * " and pmv.zone.id= :zoneId ");
         */
        /*
         * if (wardId != null && wardId != -1) query.append(
         * "  and pmv.ward.id= :wardId "); if (areaId != null && areaId != -1)
         * query.append("  and pmv.block.id= :areaId ");
         */

         query.append(" order by pmv.waterChargeViewEmbedded.connectiondetailsid ");
        final Query qry = getCurrentSession().createQuery(query.toString());
        propertyViewList = qry.list();
        /*
         * if (localityId != null && localityId != -1)
         * qry.setParameter("localityId", localityId); if (zoneId != null &&
         * zoneId != -1) qry.setParameter("zoneId", zoneId);
         */
        /*
         * if (wardId != null && wardId != -1) qry.setParameter("wardId",
         * wardId); if (areaId != null && areaId != -1)
         * qry.setParameter("areaId", areaId);
         */

        // propertyViewList=finalQuery.list();
        /*
         * propertyViewList = qry
         * .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).
         * list();
         */
        return propertyViewList;
    }
}
