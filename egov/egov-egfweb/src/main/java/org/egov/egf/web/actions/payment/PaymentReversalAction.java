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
package org.egov.egf.web.actions.payment;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.*;
import org.egov.egf.commons.EgovCommon;
import org.egov.egf.commons.VoucherSearchUtil;
import org.egov.egf.web.actions.voucher.BaseVoucherAction;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.payment.Paymentheader;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.*;


@Results({
    @Result(name = "reversalVouchers", location = "paymentReversal-reversalVouchers.jsp")
})
public class PaymentReversalAction extends BaseVoucherAction {
    /**
     *
     */
    private static final long serialVersionUID = -5576463753189498214L;
    private VoucherSearchUtil voucherSearchUtil;
    private Date fromDate;
    private Date toDate;
    private List<CVoucherHeader> voucherHeaderList = new ArrayList<CVoucherHeader>();
    private boolean close = false;
    private String message = "";
    private EgovCommon egovCommon;
    Bankaccount bankAccount;
    private List<Paymentheader> paymentHeaderList = new ArrayList<Paymentheader>();
    private Paymentheader paymentHeader = new Paymentheader();
    private @Autowired AppConfigValueService appConfigValuesService;

    @Override
    public void prepare() {
        super.prepare();
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("finance",
                "statusexcludeReport");
        final String statusExclude = appList.get(0).getValue();
        if ("".equalsIgnoreCase(statusExclude) || statusExclude == null)
            throw new ValidationException(Arrays.asList(new ValidationError("voucher.excludestatus.not.set",
                    "voucher.excludestatus.not.set")));
        addDropdownData("bankList", egovCommon.getBankBranchForActiveBanks());
        addDropdownData("accNumList", Collections.EMPTY_LIST);
        addDropdownData("voucherNameList",
                persistenceService
                        .findAllBy(new StringBuilder("select distinct vh.name from CVoucherHeader vh where vh.type='Payment' and status not in (?1)")
                                .append(" order by vh.name").toString(), statusExclude));
    }

    public PaymentReversalAction() {
        voucherHeader.setVouchermis(new Vouchermis());
        addRelatedEntity("vouchermis.departmentid", Department.class);
        addRelatedEntity("fundId", Fund.class);
        addRelatedEntity("vouchermis.schemeid", Scheme.class);
        addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
        addRelatedEntity("vouchermis.functionary", Functionary.class);
        addRelatedEntity("fundsourceId", Fundsource.class);
        addRelatedEntity("vouchermis.divisionid", Boundary.class);
    }

    public String reverse() {
        if (paymentHeader.getId() != null) {
            paymentHeader = (Paymentheader) persistenceService.find("from Paymentheader where id=?1", paymentHeader.getId());
            voucherHeader = paymentHeader.getVoucherheader();
        }
        return "reverse";
    }

    public String saveReverse() {
        if (voucherHeader.getId() != null)
            voucherHeader = (CVoucherHeader) persistenceService.find("from CVoucherHeader where id=?1", voucherHeader.getId());
        saveReverse(voucherHeader.getName(), "Receipt");
        message = getText("transaction.success") + voucherHeader.getVoucherNumber();
        return "reverse";
    }

    public String saveReverseAndClose() {
        close = true;
        return saveReverse();
    }

    @Action(value = "/payment/paymentReversal-vouchersForReversal")
    public String vouchersForReversal() {
        return "reversalVouchers";
    }

    public String searchVouchersForReversal() throws ApplicationException, ParseException {
        voucherHeader.setType("Payment");
        voucherHeaderList = voucherSearchUtil.search(voucherHeader, getFromDate(), getToDate(), "reverse");
        final Map.Entry<String, Map<String, Object>> queryMapEntry = formQuery(voucherHeaderList).entrySet().iterator().next();
        final String queryString = queryMapEntry.getKey();
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        final Query query;
        if (voucherHeaderList != null && voucherHeaderList.size() > 0) {
            if (bankAccount != null && bankAccount.getId() != null) {
                query = persistenceService.getSession().createQuery(queryString.concat(" and bankaccount.id=:accId)"));
                query.setParameter("accId", bankAccount.getId(), LongType.INSTANCE);
            } else
                query = persistenceService.getSession().createQuery(queryString);
            queryParams.entrySet().forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
            paymentHeaderList = query.list();
        }
        if (paymentHeaderList.size() == 0)
            message = getText("no.records");
        return "reversalVouchers";
    }

    private Map<String, Map<String, Object>> formQuery(final List<CVoucherHeader> voucherHeaderList) {
        final Map<String, Map<String, Object>> queryMap = new HashMap<>();
        final Map<String, Object> queryParams = new HashMap<>();
        final StringBuffer query = new StringBuffer("from Paymentheader where voucherheader.id in (:ids)");
        List<Long> ids = new ArrayList<>();
        voucherHeaderList.forEach(cVoucherHeader -> ids.add(cVoucherHeader.getId()));
        queryParams.put("ids", ids);
        queryMap.put(query.toString(), queryParams);
        return queryMap;
    }

    public void setVoucherSearchUtil(final VoucherSearchUtil voucherSearchUtil) {
        this.voucherSearchUtil = voucherSearchUtil;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public Date getToDate() {
        return toDate;
    }

    @Override
    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    @Override
    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public List<CVoucherHeader> getVoucherHeaderList() {
        return voucherHeaderList;
    }

    public void setClose(final boolean close) {
        this.close = close;
    }

    public boolean isClose() {
        return close;
    }

    public String getMessage() {
        return message;
    }

    public void setEgovCommon(final EgovCommon egovCommon) {
        this.egovCommon = egovCommon;
    }

    public List<Paymentheader> getPaymentHeaderList() {
        return paymentHeaderList;
    }

    public Paymentheader getPaymentHeader() {
        return paymentHeader;
    }

    @Override
    public CVoucherHeader getVoucherHeader() {
        return super.getVoucherHeader();
    }
}
