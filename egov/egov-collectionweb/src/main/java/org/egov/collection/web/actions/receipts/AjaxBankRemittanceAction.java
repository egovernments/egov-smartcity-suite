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
package org.egov.collection.web.actions.receipts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.Fund;
import org.egov.commons.dao.BankBranchHibernateDAO;
import org.egov.commons.dao.BankaccountHibernateDAO;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({
        @Result(name = AjaxBankRemittanceAction.BANKBRANCHLIST, location = "ajaxBankRemittance-bankBranchList.jsp"),
        @Result(name = AjaxBankRemittanceAction.ACCOUNTLIST, location = "ajaxBankRemittance-accountList.jsp"),
        @Result(name = AjaxBankRemittanceAction.USERLIST, location = "ajaxBankRemittance-userList.jsp"),
        @Result(name = AjaxBankRemittanceAction.DESIGNATIONLIST, location = "ajaxBankRemittance-designationList.jsp"),
        @Result(name = AjaxBankRemittanceAction.SERVICENAMELIST, location = "ajaxBankRemittance-serviceListOfAccount.jsp"),
        @Result(name = AjaxBankRemittanceAction.BANKACCOUNTLIST, location = "ajaxBankRemittance-bankAccountList.jsp") })
public class AjaxBankRemittanceAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    protected static final String BANKBRANCHLIST = "bankBranchList";
    protected static final String ACCOUNTLIST = "accountList";
    protected static final String BANKACCOUNTLIST = "bankAccountList";
    private String serviceName;
    private String fundName;
    protected static final String USERLIST = "userList";
    protected static final String DESIGNATIONLIST = "designationList";
    private Long designationId;
    private Long approverDeptId;
    private List<EmployeeView> postionUserList = new ArrayList<EmployeeView>(0);
    private final List<Designation> designationMasterList = new ArrayList<Designation>(0);
    private CollectionsUtil collectionsUtil;
    protected static final String SERVICENAMELIST = "serviceNameList";
    @Autowired
    private BankBranchHibernateDAO bankBranchHibernateDAO;
    @Autowired
    private BankaccountHibernateDAO bankaccountHibernateDAO;
    private PersistenceService<ServiceDetails, Long> serviceDetailsService;

    /**
     * A <code>Long</code> representing the fund id. The fund id is arriving from the miscellanoeus receipt screen
     */
    private Integer fundId;
    private Integer branchId;
    private Integer bankAccountId;
    private Long serviceId;
    private Integer bankId;
    private List<Bankbranch> bankBranchArrayList = new ArrayList<Bankbranch>(0);
    private List<Bankaccount> bankAccountArrayList;
    private List<ServiceDetails> serviceNameList;

    @Action(value = "/receipts/ajaxBankRemittance-bankBranchList")
    public String bankBranchList() {
        if (getFundId() != null) {
            final Fund fund = (Fund) persistenceService.find("from Fund where id=?", fundId);
            if (fund == null)
                throw new ValidationException(Arrays.asList(new ValidationError("fund.not.found",
                        "Fund information not available")));
            setFundName(fund.getName());
        }
        if (serviceName == null && serviceId != null && serviceId != -1) {
            final ServiceDetails serviceDetails = (ServiceDetails) persistenceService.find(
                    "from ServiceDetails where id=?", serviceId);
            setServiceName(serviceDetails.getName());
        }
        final String bankBranchQueryString = "select distinct(bb.id) as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from BANK b,BANKBRANCH bb, BANKACCOUNT ba,"
                + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.bankaccount=ba.ID and asm.servicedetails=sd.ID and "
                + "ba.BRANCHID=bb.ID and bb.BANKID=b.ID and fd.ID=ba.FUNDID and sd.NAME='"
                + serviceName
                + "' and fd.NAME='" + getFundName() + "'";

        final Query bankBranchQuery = persistenceService.getSession().createSQLQuery(bankBranchQueryString);
        final List<Object[]> queryResults = bankBranchQuery.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            final Bankbranch newBankbranch = new Bankbranch();
            newBankbranch.setId(Integer.valueOf(arrayObjectInitialIndex[0].toString()));
            newBankbranch.setBranchname(arrayObjectInitialIndex[1].toString());
            bankBranchArrayList.add(newBankbranch);
        }
        return BANKBRANCHLIST;
    }

    @Action(value = "/receipts/ajaxBankRemittance-bankBranchListOfService")
    public String bankBranchListOfService() {
        String bankBranchQueryString;
        if (collectionsUtil.isBankCollectionRemitter(collectionsUtil.getLoggedInUser()))
            bankBranchQueryString = "select distinct(bb.id) as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from BANK b,BANKBRANCH bb, BANKACCOUNT ba,"
                    + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd,EGCL_BRANCHUSER_MAP bu where asm.bankaccount=ba.ID and asm.servicedetails=sd.ID and "
                    + "ba.BRANCHID=bb.ID and bb.BANKID=b.ID and  bu.isActive=true and bb.id=bu.bankbranch and bu.bankuser="
                    + collectionsUtil.getLoggedInUser().getId();
        else
            bankBranchQueryString = "select distinct(bb.id) as branchid,b.NAME||'-'||bb.BRANCHNAME as branchname from BANK b,BANKBRANCH bb, BANKACCOUNT ba,"
                    + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.bankaccount=ba.ID and asm.servicedetails=sd.ID and "
                    + "ba.BRANCHID=bb.ID and bb.BANKID=b.ID";

        final Query bankBranchQuery = persistenceService.getSession().createSQLQuery(bankBranchQueryString);
        final List<Object[]> queryResults = bankBranchQuery.list();

        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            final Bankbranch newBankbranch = new Bankbranch();
            newBankbranch.setId(Integer.valueOf(arrayObjectInitialIndex[0].toString()));
            newBankbranch.setBranchname(arrayObjectInitialIndex[1].toString());
            bankBranchArrayList.add(newBankbranch);
        }
        return BANKBRANCHLIST;
    }

    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/receipts/ajaxBankRemittance-accountList")
    public String accountList() {
        if (fundId != null && fundId != -1) {
            final Fund fund = (Fund) persistenceService.find("from Fund where id=?", fundId);
            if (fund == null)
                throw new ValidationException(Arrays.asList(new ValidationError("fund.not.found",
                        "Fund information not available")));
            setFundName(fund.getName());
        }
        if (serviceName == null && serviceId != null && serviceId != -1) {
            final ServiceDetails serviceDetails = (ServiceDetails) persistenceService.find(
                    "from ServiceDetails where id=?", serviceId);
            setServiceName(serviceDetails.getName());
        }
        final String bankAccountQueryString = "select ba.id as accountid,ba.accountnumber as accountnumber from BANKACCOUNT ba,"
                + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNT=ba.ID and asm.servicedetails=sd.ID and fd.ID=ba.FUNDID and "
                + "ba.BRANCHID=" + branchId + " and sd.NAME='" + serviceName + "' and fd.NAME='" + fundName + "'";

        final Query bankAccountQuery = persistenceService.getSession().createSQLQuery(bankAccountQueryString);
        final List<Object[]> queryResults = bankAccountQuery.list();

        bankAccountArrayList = new ArrayList<Bankaccount>();
        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            final Bankaccount newBankaccount = new Bankaccount();
            newBankaccount.setId(Long.valueOf(arrayObjectInitialIndex[0].toString()));
            newBankaccount.setAccountnumber(arrayObjectInitialIndex[1].toString());
            getBankAccountArrayList().add(newBankaccount);
        }

        return ACCOUNTLIST;

    }

    @Action(value = "/receipts/ajaxBankRemittance-accountListOfService")
    public String accountListOfService() {
        final String bankAccountQueryString = "select distinct ba.id as accountid,ba.accountnumber as accountnumber from BANKACCOUNT ba,"
                + "EGCL_BANKACCOUNTSERVICEMAPPING asm,EGCL_SERVICEDETAILS sd,FUND fd where asm.BANKACCOUNT=ba.ID and asm.servicedetails=sd.ID and fd.ID=ba.FUNDID and "
                + "ba.BRANCHID=" + branchId;

        final Query bankAccountQuery = persistenceService.getSession().createSQLQuery(bankAccountQueryString);
        final List<Object[]> queryResults = bankAccountQuery.list();

        bankAccountArrayList = new ArrayList<Bankaccount>();
        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            final Bankaccount newBankaccount = new Bankaccount();
            newBankaccount.setId(Long.valueOf(arrayObjectInitialIndex[0].toString()));
            newBankaccount.setAccountnumber(arrayObjectInitialIndex[1].toString());
            getBankAccountArrayList().add(newBankaccount);
        }

        return ACCOUNTLIST;

    }

    @Action(value = "/receipts/ajaxBankRemittance-serviceListOfAccount")
    public String serviceListOfAccount() {
        final String serviceAccountQueryString = "select sd.id as serviceid,sd.name as servicename from EGCL_SERVICEDETAILS sd,EGCL_BANKACCOUNTSERVICEMAPPING asm where sd.id=asm.servicedetails and asm.bankaccount="
                + bankAccountId;
        final Query serviceListQuery = persistenceService.getSession().createSQLQuery(serviceAccountQueryString);
        final List<Object[]> queryResults = serviceListQuery.list();

        serviceNameList = new ArrayList<ServiceDetails>();
        for (int i = 0; i < queryResults.size(); i++) {
            final Object[] arrayObjectInitialIndex = queryResults.get(i);
            final ServiceDetails newServiceNameList = new ServiceDetails();
            newServiceNameList.setId(Long.valueOf(arrayObjectInitialIndex[0].toString()));
            newServiceNameList.setName(arrayObjectInitialIndex[1].toString());
            getServiceNameList().add(newServiceNameList);
        }

        return SERVICENAMELIST;

    }

    @Action(value = "/receipts/ajaxBankRemittance-positionUserList")
    public String positionUserList() {
        if (designationId != null && approverDeptId != null)
            try {
                postionUserList = collectionsUtil.getPositionBySearchParameters(null, designationId.intValue(),
                        approverDeptId.intValue(), null, null, new Date(), 0);
            } catch (final NoSuchObjectException e) {
                throw new ApplicationRuntimeException("Designation Postion not found", e);
            }
        return USERLIST;

    }

    @Action(value = "/receipts/ajaxBankRemittance-serviceListNotMappedToAccount")
    public String serviceListNotMappedToAccount() {
        final String serviceAccountQueryString = "select distinct sd from ServiceDetails sd where sd.isEnabled='true' and sd.serviceCategory.id=? ";
        final Query serviceListQuery = persistenceService.getSession().createQuery(serviceAccountQueryString);
        serviceListQuery.setParameter(0, serviceId);
        serviceNameList = serviceListQuery.list();
        return SERVICENAMELIST;
    }

    @Action(value = "/receipts/ajaxBankRemittance-bankBranchsByBankForReceiptPayments")
    public String bankBranchsByBankForReceiptPayments() {
        bankBranchArrayList = bankBranchHibernateDAO.getAllBankBranchsByBankForReceiptPayments(bankId);
        return BANKBRANCHLIST;
    }

    @Action(value = "/receipts/ajaxBankRemittance-bankAccountByBankBranch")
    public String bankAccountByBankBranch() {
        if (serviceId != -1) {
            ServiceDetails sd = serviceDetailsService.findById(serviceId, false);
            fundId = sd.getFund().getId();
        }
        bankAccountArrayList = bankaccountHibernateDAO.getBankAccountByBankBranchForReceiptsPayments(branchId, fundId);
        return BANKACCOUNTLIST;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(final Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the bankBranchArrayListList
     */
    public List getBankBranchArrayList() {
        return bankBranchArrayList;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return the bankAccountArrayList
     */
    public List<Bankaccount> getBankAccountArrayList() {
        return bankAccountArrayList;
    }

    public List<ServiceDetails> getServiceNameList() {
        return serviceNameList;
    }

    public void setServiceNameList(final List<ServiceDetails> serviceNameList) {
        this.serviceNameList = serviceNameList;
    }

    /**
     * @return the fundName
     */
    public String getFundName() {
        return fundName;
    }

    /**
     * @param fundName the fundName to set
     */
    public void setFundName(final String fundName) {
        this.fundName = fundName;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(final Integer fundId) {
        this.fundId = fundId;
    }

    /**
     * @param designationId the designationId to set
     */
    public void setDesignationId(final Long designationId) {
        this.designationId = designationId;
    }

    /**
     * @return the approverDeptId
     */
    public Long getApproverDeptId() {
        return approverDeptId;
    }

    /**
     * @param approverDeptId the approverDeptId to set
     */
    public void setApproverDeptId(final Long approverDeptId) {
        this.approverDeptId = approverDeptId;
    }

    /**
     * @return the postionUserList
     */
    public List<EmployeeView> getPostionUserList() {
        return postionUserList;
    }

    /**
     * @return the designationMasterList
     */
    public List<Designation> getDesignationMasterList() {
        return designationMasterList;
    }

    /**
     * @param collectionsUtil the collectionsUtil to set
     */
    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(final Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(final Long serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(final Integer bankId) {
        this.bankId = bankId;
    }

    public void setServiceDetailsService(PersistenceService<ServiceDetails, Long> serviceDetailsService) {
        this.serviceDetailsService = serviceDetailsService;
    }
}
