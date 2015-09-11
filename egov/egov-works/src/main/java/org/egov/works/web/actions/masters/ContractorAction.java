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
package org.egov.works.web.actions.masters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.ContractorGrade;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Result(name = ContractorAction.NEW, location = "contractor-new.jsp")
public class ContractorAction extends SearchFormAction {

    private static final long serialVersionUID = 3167651186547987956L;

    private static final Logger logger = Logger.getLogger(ContractorAction.class);

    private PersistenceService<Contractor, Long> contractorService;

    private Contractor contractor = new Contractor();

    private List<Contractor> contractorList = null;
    private List<ContractorDetail> actionContractorDetails = new LinkedList<ContractorDetail>();
    private Long id;
    private String mode;
    @Autowired
    private CommonsService commonsService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    private WorksService worksService;

    // -----------------------Search parameters----------------------------------
    private String contractorName;
    private String contractorcode;
    private Integer departmentId;
    private Long gradeId;
    private Date searchDate;
    private boolean sDisabled;
    // -----------------------------prashant--------------------------------------

    /*
     * added on 28th october for view contractor based on criteria
     */
    private Integer statusId;
    private List<ContractorDetail> contractorDetailList = null;
    private PersistenceService<ContractorDetail, Long> contractorDetailService;
    private Integer rowId;
    private final String EDIT_ENABLE_ROLE_NAME = "Edit Contractor Bank Info";
    private boolean hasRoleMapped;

    public ContractorAction() {
        addRelatedEntity("bank", Bank.class);
    }

    @Override
    public String execute() {
        return list();
    }

    @Action(value = "/masters/contractor-newform")
    public String newform() {
        return NEW;
    }

    public String list() {
        contractorList = contractorService.findAllBy(" from Contractor con order by code asc");
        return INDEX;
    }

    public String edit() {
        contractor = contractorService.findById(contractor.getId(), false);
        return EDIT;
    }

    /*
     * on 28th 2009 for listing contractor based on criteria
     */
    public String viewContractor() {
        return "viewContractor";
    }

    public String viewResult() {
        logger.debug("Inside viewResult");
        // getContractorListForCriterias();
        setPageSize(WorksConstants.PAGE_SIZE);
        search();
        return "viewContractor";
    }

    public void getContractorListForCriterias() {
        logger.debug("Inside getContractorListForCriterias");
        String contractorStr = null;
        final List<Object> paramList = new ArrayList<Object>();
        Object[] params;
        // if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals(""))
        // || (contractorName != null && !contractorName.equals("")))
        contractorStr = " select distinct contractor from Contractor contractor ";

        if (statusId != null || departmentId != null || gradeId != null)
            contractorStr = contractorStr + " left outer join fetch contractor.contractorDetails as detail ";

        if (statusId != null || departmentId != null || gradeId != null || contractorcode != null && !contractorcode.equals("")
                || contractorName != null && !contractorName.equals(""))
            contractorStr = contractorStr + " where contractor.code is not null";

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorcode)) {
            contractorStr = contractorStr + " and UPPER(contractor.code) like ?";
            paramList.add("%" + contractorcode.toUpperCase() + "%");
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorName)) {
            contractorStr = contractorStr + " and UPPER(contractor.name) like ?";
            paramList.add("%" + contractorName.toUpperCase() + "%");
        }

        if (statusId != null) {
            contractorStr = contractorStr + " and detail.status.id = ?";
            paramList.add(statusId);
        }

        if (departmentId != null) {
            contractorStr = contractorStr + " and detail.department.id = ?";
            paramList.add(departmentId);
        }

        if (gradeId != null) {
            contractorStr = contractorStr + " and detail.grade.id = ?";
            paramList.add(gradeId);
        }

        if (paramList.isEmpty())
            contractorList = contractorService.findAllBy(contractorStr);
        else {
            params = new Object[paramList.size()];
            params = paramList.toArray(params);
            contractorList = contractorService.findAllBy(contractorStr, params);
        }
    }

    /* end listing contractor based on criteria */

    public String save() {
        populateContractorDetails();
        getHasRoleMapped();
        if (mode.equalsIgnoreCase("") && !hasRoleMapped) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(contractor.getPanNumber()) && contractor.getBank() != null &&
                    org.apache.commons.lang.StringUtils.isNotBlank(contractor.getBankAccount())
                    && org.apache.commons.lang.StringUtils.isNotBlank(contractor.getIfscCode()))
                contractor.setIsEditEnabled(false);
            else
                contractor.setIsEditEnabled(true);
        } else if (mode.equalsIgnoreCase("edit") && contractor.getIsEditEnabled() && !hasRoleMapped) {
            if (org.apache.commons.lang.StringUtils.isNotBlank(contractor.getPanNumber()) && contractor.getBank() != null &&
                    org.apache.commons.lang.StringUtils.isNotBlank(contractor.getBankAccount())
                    && org.apache.commons.lang.StringUtils.isNotBlank(contractor.getIfscCode()))
                contractor.setIsEditEnabled(false);
            else
                contractor.setIsEditEnabled(true);
        } else
            contractor.setIsEditEnabled(contractor.getIsEditEnabled());
        contractor = contractorService.merge(contractor);
        createAccountDetailKey(contractor);
        final String messageKey = "contractor.save.success";
        addActionMessage(getText(messageKey, "The Contractor was saved successfully"));
        return list();
    }

    /**
     * This method will take user to the search contractor screen.
     * @author prashant.gaurav
     */
    public String searchPage() {
        final String negDate = (String) request.get("negDate");
        logger.debug("Negotiation date found :----------" + negDate);
        if (negDate != null) {
            final SimpleDateFormat dftDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                searchDate = dftDateFormatter.parse(negDate);
            } catch (final ParseException e) {
                logger.debug("Negotiation date is not valid, should be in dd/MM/yyyy format");
            }
        }

        return "search";
    }

    /**
     * This method witll return the list of contrator based on search criteria entered.
     * @author prashant.gaurav
     */
    public String searchResult() {
        searchContractor();
        return "search";

    }

    /**
     * This method will see if search parameters are in request. If yes, will search contractor as per criteria and set add it to
     * result list.
     * @author prashant.gaurav
     */
    private void searchContractor() {
        logger.debug("Inside searchContractor");
        // Get default status to be searched from app config values
        final List<AppConfigValues> configList = worksService.getAppConfigValue("Works", "CONTRACTOR_STATUS");
        // Assuming that status is inserted using db script
        final String status = configList.get(0).getValue();
        logger.debug("CONTRACTOR_STATUS for the module Works in appconfig table, Found ------" + status
                + " || Expected ------- 'Active'");

        final Criteria criteria = contractorService.getSession().createCriteria(Contractor.class);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorcode))
            criteria.add(Restrictions.sqlRestriction("lower({alias}.code) like lower(?)", "%" + contractorcode.trim() + "%",
                    StringType.INSTANCE));

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorName))
            criteria.add(Restrictions.sqlRestriction("lower({alias}.name) like lower(?)", "%" + contractorName.trim() + "%",
                    StringType.INSTANCE));

        criteria.createAlias("contractorDetails", "detail").createAlias("detail.status", "status");
        criteria.add(Restrictions.eq("status.description", status));
        if (departmentId != null)
            criteria.add(Restrictions.eq("detail.department.id", departmentId));

        if (gradeId != null)
            criteria.add(Restrictions.eq("detail.grade.id", gradeId));

        if (searchDate != null)
            criteria.add(Restrictions.le("detail.validity.startDate", searchDate))
                    .add(Restrictions.or(Restrictions.ge("detail.validity.endDate", searchDate),
                            Restrictions.isNull("detail.validity.endDate")));

        criteria.addOrder(Order.asc("name"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        contractorList = criteria.list();
    }

    protected void createAccountDetailKey(final Contractor cont) {
        // Accountdetailtype accountdetailtype=mastersMgr.getAccountdetailtypeByName("contractor");
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("contractor");

        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(cont.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        commonsService.createAccountdetailkey(adk);
    }

    protected void populateContractorDetails() {
        contractor.getContractorDetails().clear();
        for (final ContractorDetail contractorDetail : actionContractorDetails)
            if (validContractorDetail(contractorDetail)) {
                contractorDetail.setDepartment((Department) getPersistenceService().find("from Department where id = ?",
                        contractorDetail.getDepartment().getId()));
                contractorDetail.setStatus((EgwStatus) getPersistenceService().find("from EgwStatus where id = ?",
                        contractorDetail.getStatus().getId()));
                if (contractorDetail.getGrade().getId() == null)
                    contractorDetail.setGrade(null);
                else
                    contractorDetail.setGrade((ContractorGrade) getPersistenceService().find("from ContractorGrade where id = ?",
                            contractorDetail.getGrade().getId()));
                contractorDetail.setContractor(contractor);
                contractor.addContractorDetail(contractorDetail);
            } else if (contractorDetail != null) {
                if (contractorDetail.getDepartment() == null || contractorDetail.getDepartment().getId() == null)
                    contractorDetail.setDepartment(null);
                else
                    contractorDetail.setDepartment((Department) getPersistenceService().find("from Department where id = ?",
                            contractorDetail.getDepartment().getId()));
                if (contractorDetail.getStatus() == null || contractorDetail.getStatus().getId() == null)
                    contractorDetail.setStatus(null);
                else
                    contractorDetail.setStatus((EgwStatus) getPersistenceService().find("from EgwStatus where id = ?",
                            contractorDetail.getStatus().getId()));
                if (contractorDetail.getGrade() == null || contractorDetail.getGrade().getId() == null)
                    contractorDetail.setGrade(null);
                else
                    contractorDetail.setGrade((ContractorGrade) getPersistenceService().find("from ContractorGrade where id = ?",
                            contractorDetail.getGrade().getId()));
                contractorDetail.setContractor(contractor);
                contractor.addContractorDetail(contractorDetail);
            }
    }

    protected boolean validContractorDetail(final ContractorDetail contractorDetail) {
        if (contractorDetail != null && contractorDetail.getDepartment() != null && contractorDetail.getStatus() != null
                && contractorDetail.getDepartment().getId() != null && contractorDetail.getStatus().getId() != null)
            return true;
        return false;
    }

    @Override
    public Object getModel() {
        return contractor;
    }

    public List<Contractor> getContractorList() {
        return contractorList;
    }

    public void setContractorService(final PersistenceService<Contractor, Long> service) {
        contractorService = service;
    }

    @Override
    public void prepare() {
        if (id != null)
            contractor = contractorService.findById(id, false);
        super.prepare();
        setupDropdownDataExcluding("bank");
        addDropdownData("departmentList", getPersistenceService().findAllBy("from DepartmentImpl order by upper(deptName)"));
        addDropdownData("gradeList", getPersistenceService().findAllBy("from ContractorGrade order by upper(grade)"));
        addDropdownData("bankList", getPersistenceService().findAllBy("from Bank where isactive=1 order by upper(name)"));
        addDropdownData("statusList", getPersistenceService().findAllBy("from EgwStatus where moduletype='Contractor'"));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(final Contractor contractor) {
        this.contractor = contractor;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<ContractorDetail> getActionContractorDetails() {
        return actionContractorDetails;
    }

    public void setActionContractorDetails(
            final List<ContractorDetail> actionContractorDetails) {
        this.actionContractorDetails = actionContractorDetails;
    }

    public CommonsService getCommonsService() {
        return commonsService;
    }

    public void setCommonsService(final CommonsService commonsService) {
        this.commonsService = commonsService;
    }

    // public MastersManager getMastersMgr() {
    // return mastersMgr;
    // }
    //
    //
    // public void setMastersMgr(MastersManager mastersMgr) {
    // this.mastersMgr = mastersMgr;
    // }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorcode() {
        return contractorcode;
    }

    public void setContractorcode(final String contractorcode) {
        this.contractorcode = contractorcode;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(final Long gradeId) {
        this.gradeId = gradeId;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(final Date searchDate) {
        this.searchDate = searchDate;
    }

    /**
     * @return the statusId
     */
    public Integer getStatusId() {
        return statusId;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
    }

    /**
     * @return the contractorDetailList
     */
    public List<ContractorDetail> getContractorDetailList() {
        return contractorDetailList;
    }

    /**
     * @param contractorDetailList the contractorDetailList to set
     */
    public void setContractorDetailList(final List<ContractorDetail> contractorDetailList) {
        this.contractorDetailList = contractorDetailList;
    }

    /**
     * @return the contractorDetailService
     */
    public PersistenceService<ContractorDetail, Long> getContractorDetailService() {
        return contractorDetailService;
    }

    /**
     * @param contractorDetailService the contractorDetailService to set
     */
    public void setContractorDetailService(
            final PersistenceService<ContractorDetail, Long> contractorDetailService) {
        this.contractorDetailService = contractorDetailService;
    }

    public boolean isSDisabled() {
        return sDisabled;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {

        String contractorStr = null;
        final List<Object> paramList = new ArrayList<Object>();
        // if(statusId !=null || departmentId != null || gradeId != null || (contractorcode != null && !contractorcode.equals(""))
        // || (contractorName != null && !contractorName.equals("")))
        contractorStr = " from ContractorDetail detail ";

        if (statusId != null || departmentId != null || gradeId != null || contractorcode != null && !contractorcode.equals("")
                || contractorName != null && !contractorName.equals(""))
            contractorStr = contractorStr + " where detail.contractor.code is not null";

        if (contractorcode != null && !contractorcode.equals("")) {
            contractorStr = contractorStr + " and UPPER(detail.contractor.code) like ?";
            paramList.add("%" + contractorcode.toUpperCase() + "%");
        }

        if (contractorName != null && !contractorName.equals("")) {
            contractorStr = contractorStr + " and UPPER(detail.contractor.name) like ?";
            paramList.add("%" + contractorName.toUpperCase() + "%");
        }

        if (statusId != null) {
            contractorStr = contractorStr + " and detail.status.id = ? ";
            paramList.add(statusId);
        }

        if (departmentId != null) {
            contractorStr = contractorStr + " and detail.department.id = ? ";
            paramList.add(departmentId);
        }

        if (gradeId != null) {
            contractorStr = contractorStr + " and detail.grade.id = ? ";
            paramList.add(gradeId);
        }
        final String query = "select distinct detail.contractor " + contractorStr;

        final String countQuery = "select count(distinct detail.contractor) " + contractorStr;
        return new SearchQueryHQL(query, countQuery, paramList);
    }

    public boolean getHasRoleMapped() {
        final Role role = roleService.getRoleByName(EDIT_ENABLE_ROLE_NAME);
        final User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
        if (role != null && user != null && !user.getRoles().isEmpty())
            for (final Role userRoleObj : user.getRoles())
                if (role.getId() == userRoleObj.getId())
                    hasRoleMapped = true;
        return hasRoleMapped;
    }

    public void setHasRoleMapped(final boolean hasRoleMapped) {
        this.hasRoleMapped = hasRoleMapped;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(final Integer rowId) {
        this.rowId = rowId;
    }

}