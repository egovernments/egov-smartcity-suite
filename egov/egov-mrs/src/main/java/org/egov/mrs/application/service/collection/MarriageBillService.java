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
package org.egov.mrs.application.service.collection;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MarriageBillService extends BillServiceInterface {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RegistrationBillable billableRegistration;

    @Autowired
    private ReIssueBillable billableReIssue;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EgBillDao egBillDAO;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     *
     * Generates Bill for Marriage fee collection
     *
     * @param registration
     * @return xml representation of the bill
     */
    public String generateBill(final MarriageRegistration registration) {

        billableRegistration.setRegistration(registration);
        billableRegistration.setUserId(securityUtils.getCurrentUser().getId());
        billableRegistration.setBillType(egBillDAO.getBillTypeByCode(RegistrationBillable.BILLTYPE_AUTO));
        billableRegistration.setReferenceNumber(registration.getApplicationNo());

        final String billXml;

        try {
            billXml = URLEncoder.encode(getBillXML(billableRegistration), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return billXml;
    }

    /**
     *
     * Generates Bill for Re-Issue fee collection
     *
     * @param registration
     * @return xml representation of the bill
     */
    public String generateBill(final ReIssue reIssue) {

        billableReIssue.setReIssue(reIssue);
        billableReIssue.setUserId(securityUtils.getCurrentUser().getId());
        billableReIssue.setBillType(egBillDAO.getBillTypeByCode(RegistrationBillable.BILLTYPE_AUTO));
        billableReIssue.setReferenceNumber(reIssue.getRegistration().getApplicationNo());

        final String billXml;

        try {
            billXml = URLEncoder.encode(getBillXML(billableRegistration), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return billXml;
    }

    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
        final EgDemand demand = billObj.getCurrentDemand();
        final Date currentDate = new Date();
        final StringBuilder descriptionBuilder = new StringBuilder();

        int i = 1;
        for (final EgDemandDetails demandDetail : demand.getEgDemandDetails()) {

            final EgDemandReason reason = demandDetail.getEgDemandReason();
            final Installment installment = reason.getEgInstallmentMaster();

            final boolean thereIsBalanceFee = demandDetail.getAmount() != null
                    && demandDetail.getAmount().compareTo(demandDetail.getAmtCollected()) > 0 ? true : false;
            final EgDemandReasonMaster demandReasonMaster = reason.getEgDemandReasonMaster();

            if (demandReasonMaster.getIsDebit().equalsIgnoreCase("N") && thereIsBalanceFee) {

                final EgBillDetails billdetail = new EgBillDetails();

                billdetail.setDrAmount(BigDecimal.ZERO);
                billdetail.setCrAmount(demandDetail.getAmount().subtract(demandDetail.getAmtCollected()));

                if (reason.getGlcodeId() == null)
                    LOGGER.info("MarriageBillService.getBilldetails - GLCODE does not exists for reason="
                            + demandReasonMaster.getReasonMaster());
                else
                    billdetail.setGlcode(reason.getGlcodeId().getGlcode());

                billdetail.setEgDemandReason(reason);
                billdetail.setAdditionalFlag(Integer.valueOf(1));
                billdetail.setCreateDate(currentDate);
                billdetail.setModifiedDate(currentDate);
                billdetail.setOrderNo(i++);

                descriptionBuilder.append(demandReasonMaster.getReasonMaster())
                        .append(" - ")
                        .append(installment.getDescription())
                        .append(" # ")
                        .append(billObj.getCurrentDemand().getEgInstallmentMaster().getDescription());

                billdetail.setDescription(descriptionBuilder.toString());
                billDetails.add(billdetail);
            }
        }

        return billDetails;
    }

    @Override
    public void cancelBill() {
    }

    EgBillDetails createBillDet(final Integer orderNo, final BigDecimal billDetailAmount, final String glCode,
            final String description, final Integer addlFlag) {
        if (orderNo == null || billDetailAmount == null || glCode == null)
            throw new ApplicationRuntimeException("Exception in createBillDet....");
        final EgBillDetails billdetail = new EgBillDetails();
        billdetail.setFunctionCode("FUNCTION_CODE");
        billdetail.setOrderNo(orderNo);
        billdetail.setCreateDate(new Date());
        billdetail.setModifiedDate(new Date());
        billdetail.setCrAmount(billDetailAmount);
        billdetail.setDrAmount(BigDecimal.ZERO);
        billdetail.setGlcode(glCode);
        billdetail.setDescription(description);
        billdetail.setAdditionalFlag(addlFlag);
        LOGGER.info("Bill Detail object created with amount " + billDetailAmount);
        return billdetail;
    }

}
