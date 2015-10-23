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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.Bank;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.master.services.ContractorGradeService;
import org.egov.works.master.services.ContractorService;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

@ParentPackage("egov")
@Results({ @Result(name = ContractorAction.NEW, location = "contractor-new.jsp"),
        @Result(name = ContractorAction.VIEW_CONTRACTOR, location = "contractor-viewContractor.jsp"),
        @Result(name = ContractorAction.SEARCH, location = "contractor-search.jsp"),
        @Result(name = ContractorAction.INDEX, location = "contractor-index.jsp"),
        @Result(name = ContractorAction.EDIT, location = "contractor-edit.jsp")
})
public class ContractorAction extends SearchFormAction {

    private static final long serialVersionUID = 3167651186547987956L;

    private static final Logger logger = Logger.getLogger(ContractorAction.class);

    public static final String VIEW_CONTRACTOR = "viewContractor";
    public static final String SEARCH = "search";
    public static final String INDEX = "index";
    public static final String EDIT = "edit";
    @Autowired
    private ContractorService contractorService;
    private Contractor contractor = new Contractor();

    private List<Contractor> contractorList = null;
    private List<ContractorDetail> actionContractorDetails = new LinkedList<ContractorDetail>();
    private Long id;
    private String mode;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailKeyHibDAO;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    private WorksService worksService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibDAO;
    @Autowired
    private ContractorGradeService contractorGradeService;
    @Autowired
    private BankHibernateDAO bankHibDAO;
    // -----------------------Search parameters----------------------------------
    private String contractorName;
    private String contractorCode;
    private Long departmentId;
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
    private boolean hasRoleMapped;

    public ContractorAction() {
        addRelatedEntity(WorksConstants.BANK, Bank.class);
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
    	contractorList = contractorService.getAllContractors();
        return INDEX;
    }

    @Action(value = "/masters/contractor-edit")
    public String edit() {
        contractor = contractorService.findById(contractor.getId(), false);
        return EDIT;
    }

    /*@Override
    @Action(value = "/masters/contractor-search")
    public String search() {
        return VIEW_CONTRACTOR;
    }*/
    
    @Action(value = "/masters/contractor-viewContractor")
    public String viewContractor() {
        return VIEW_CONTRACTOR;
    }

	@Action(value = "/masters/contractor-viewResult")
	public String viewResult() {
		setPageSize(WorksConstants.PAGE_SIZE);
		contractorList = contractorService.getContractorListForCriterias(contractorName, contractorCode, departmentId, statusId, gradeId);
		search();
		return VIEW_CONTRACTOR;
	}

    /* end listing contractor based on criteria */
    @Action(value = "/masters/contractor-save")
    public String save() {
        populateContractorDetails(mode);
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
        contractor = contractorService.persist(contractor);
        createAccountDetailKey(contractor);
        addActionMessage(getText("contractor.save.success"));
        return list();
    }

    /**
     * This method will take user to the search contractor screen.
     * @author prashant.gaurav
     */
    @Action(value = "/masters/contractor-searchPage")
    public String searchPage() {
        final String negDate = (String) request.get(WorksConstants.NEGOTIATION_DATE);
        if (negDate != null) {
            final SimpleDateFormat dftDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                searchDate = dftDateFormatter.parse(negDate);
            } catch (final ParseException e) {
                logger.error(WorksConstants.NEGOTIATION_DATE_FORMAT_INVALID);
            }
        }
        return SEARCH;
    }

    /**
     * This method witll return the list of contrator based on search criteria entered.
     * @author prashant.gaurav
     */
    @Action(value = "/masters/contractor-searchResult")
    public String searchResult() {
        contractorService.searchContractor(contractorName, contractorCode, departmentId, statusId, gradeId, null);
        return SEARCH;

    }

    protected void createAccountDetailKey(final Contractor cont) {
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("contractor");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(cont.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailKeyHibDAO.create(adk);
    }

    protected void populateContractorDetails(String mode) {
        contractor.getContractorDetails().clear();
        
        for (final ContractorDetail contractorDetail : actionContractorDetails)
            if (validContractorDetail(contractorDetail)) {
                contractorDetail.setDepartment(departmentService.getDepartmentById(contractorDetail.getDepartment().getId()));
                contractorDetail.setStatus((EgwStatus)egwStatusHibDAO.findById(contractorDetail.getStatus().getId(), false));
                if (contractorDetail.getGrade().getId() == null)
                    contractorDetail.setGrade(null);
                else
                	contractorGradeService.getContractorGradeById(contractorDetail.getGrade().getId());
                contractorDetail.setContractor(contractor);
                if(mode.equals("edit")) {
                	setPrimaryDetails(contractorDetail);
                }
                contractor.addContractorDetail(contractorDetail);
            } else if (contractorDetail != null) {
                if (contractorDetail.getDepartment() == null || contractorDetail.getDepartment().getId() == null)
                    contractorDetail.setDepartment(null);
                else
                	contractorDetail.setDepartment(departmentService.getDepartmentById(contractorDetail.getDepartment().getId()));
                if (contractorDetail.getStatus() == null || contractorDetail.getStatus().getId() == null)
                    contractorDetail.setStatus(null);
                else
                	contractorDetail.setStatus((EgwStatus)egwStatusHibDAO.findById(contractorDetail.getStatus().getId(), false));
                if (contractorDetail.getGrade() == null || contractorDetail.getGrade().getId() == null)
                    contractorDetail.setGrade(null);
                else
                	contractorGradeService.getContractorGradeById(contractorDetail.getGrade().getId());
                contractorDetail.setContractor(contractor);
                if(mode.equals("edit")) {
                	setPrimaryDetails(contractorDetail);
                }
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

    @Override
    public void prepare() {
        if (id != null)
            contractor = contractorService.findById(id, false);
        super.prepare();
        setupDropdownDataExcluding(WorksConstants.BANK);
        addDropdownData("departmentList", departmentService.getAllDepartments());
        addDropdownData("gradeList", contractorGradeService.getAllContractorGrades());
        addDropdownData("bankList", bankHibDAO.findAll());
        addDropdownData("statusList", egwStatusHibDAO.getStatusByModule(WorksConstants.STATUS_MODULE_NAME));
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

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(final String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorCode() {
        return contractorCode;
    }

    public void setContractorCode(final String contractorCode) {
        this.contractorCode = contractorCode;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
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
    	return contractorService.prepareQuery(contractorName, contractorCode, departmentId, statusId, gradeId);
    }

    public boolean getHasRoleMapped() {
        final Role role = roleService.getRoleByName(WorksConstants.EDIT_ENABLE_ROLE_NAME);
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
    //TODO: Need to remove this method after getting better alternate option
    private ContractorDetail setPrimaryDetails(ContractorDetail contractorDetail) {
    	User user = userService.getUserById(worksService.getCurrentLoggedInUserId());
    	contractorDetail.setCreatedBy(user);
    	contractorDetail.setCreatedDate(new Date());
    	return contractorDetail;
    }

	public ContractorGradeService getContractorGradeService() {
		return contractorGradeService;
	} 
}