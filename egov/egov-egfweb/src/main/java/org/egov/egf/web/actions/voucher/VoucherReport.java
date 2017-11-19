/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.egf.web.actions.voucher;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.Fund;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.exception.ApplicationException;
import org.egov.infstr.services.PersistenceService;

import java.util.List;

public class VoucherReport {
    private CGeneralLedger generalLedger = new CGeneralLedger();
    private CGeneralLedger voucherDetail = new CGeneralLedger();
    private PersistenceService persistenceService;
    private Department department;
    private static final String MULTIPLE = "MULTIPLE";
    private static final Logger LOGGER = Logger.getLogger(VoucherReport.class);
    private  EgovCommon egovCommon;
    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public VoucherReport(final PersistenceService persistenceService, final Integer voucherId, final CGeneralLedger voucherDetail, EgovCommon egovCommon) {
        super();
        this.persistenceService = persistenceService;
        this.egovCommon=egovCommon;
        generalLedger = getGeneralLedger(voucherId, voucherDetail);
        this.voucherDetail = voucherDetail;
    }

    public CGeneralLedger getGeneralLedger() {
        return generalLedger;
    }

    public CGeneralLedger getVoucherDetail() {
        return voucherDetail;
    }

    public String getSlCode() {
        //persistenceService.setType(CGeneralLedgerDetail.class);
        if (generalLedger != null) {
            final List<CGeneralLedgerDetail> generalLedgerDetail = persistenceService.findAllBy(
                    "from CGeneralLedgerDetail where generalLedgerId.id=?",generalLedger.getId());
            if (generalLedgerDetail.size() > 1)
                return MULTIPLE;
            if (generalLedgerDetail.size() > 0) {
                final Integer detailTypeId = generalLedgerDetail.get(0).getDetailTypeId().getId();
                //persistenceService.setType(Accountdetailtype.class);
                final List detailType = persistenceService.findAllBy("from Accountdetailtype where id=?", detailTypeId);
                egovCommon.setPersistenceService(persistenceService);
                final Integer detailKeyId = generalLedgerDetail.get(0).getDetailKeyId();
                EntityType entityType = null;
                try {
                    entityType = egovCommon.getEntityType((Accountdetailtype) detailType.get(0), detailKeyId);
                } catch (final ApplicationException e) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Error" + e.getMessage(), e);
                }

                return entityType.getCode() + "/" + entityType.getEntityDescription();
            }
        }
        return "";
    }

    public String getFunctionName() {
        if (generalLedger != null) {
            //persistenceService.setType(CFunction.class);
            if (generalLedger.getFunctionId() != null) {
                final CFunction function = fetchFunction(generalLedger.getFunctionId());
                return function == null ? "" : function.getName();
            }
        } else if (voucherDetail != null && voucherDetail.getVoucherHeaderId() != null
                && voucherDetail.getVoucherHeaderId().getVouchermis().getFunction() != null) {
            final CFunction function = fetchFunction(generalLedger.getFunctionId());
            return function == null ? "" : function.getName();
        }
        return "";
    }

    private CFunction fetchFunction(final Integer functionId) {
        return (CFunction) persistenceService.findById(Long.valueOf(functionId), false);
    }

    public String getFundName() {
        if (voucherDetail != null && voucherDetail.getVoucherHeaderId().getFundId() != null) {
            //persistenceService.setType(Fund.class);
            final Fund fund = (Fund) persistenceService.findById(voucherDetail.getVoucherHeaderId().getFundId().getId(), false);
            return fund == null ? "" : fund.getName();
        }
        return "";
    }

    private CGeneralLedger getGeneralLedger(final Integer voucherId, final CGeneralLedger voucherLineId) {
        //persistenceService.setType(CGeneralLedger.class);
    	return (CGeneralLedger) persistenceService.find(
                "from CGeneralLedger where voucherHeaderId.id=? and glcode=? and voucherlineId=?", Long.valueOf(voucherId),
                voucherLineId.getGlcode(),voucherLineId.getVoucherlineId());
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public EgovCommon getEgovCommon() {
        return egovCommon;
    }

    public void setEgovCommon(EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

}
