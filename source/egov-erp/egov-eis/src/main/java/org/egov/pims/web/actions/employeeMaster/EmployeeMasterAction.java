package org.egov.pims.web.actions.employeeMaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.BloodGroupMaster;
import org.egov.pims.model.CommunityMaster;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.EmployeeGroupMaster;
import org.egov.pims.model.EmployeeStatusMaster;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.LanguagesKnownMaster;
import org.egov.pims.model.LanguagesQulifiedMaster;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.ReligionMaster;
import org.egov.pims.service.PersonalInformationService;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Vaibhav.K
 *
 */
@ParentPackage("egov")
@SuppressWarnings("serial")


public class EmployeeMasterAction extends BaseFormAction
{

        public final static Logger LOGGER = Logger.getLogger(EmployeeMasterAction.class);
        private UserService eisUserMgr;
        private RoleService roleMgr;
        private CommonsService commonsMgr;
        private transient PersonalInformationService personalInformationServiceOld;
        //private transient AuditEventService auditEventService;
        private User user ;
        PersonalInformation employee = new PersonalInformation();
        private String permanentAddress="";
        private String correspondenceAddress="";
        private String mode;
        List<Integer> langKnownList = new ArrayList<Integer>();
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        private Integer empId;
        private boolean isEmpActive;
        private boolean empCodeAutogen;
        private String empCodeUniqueCheck;
        private String panNoUniqueCheck;
        private String remarks;
        
        private static final String FIRSTNAME="First Name";
        private static final String LASTNAME="Last Name";
        private static final String MIDDLENAME="Middle Name";
        private static final String EMP_SELF_SERVICE = "Employee Self Service";
        
        
        @Override
        public Object getModel() {
                return employee;
        }
        
        @SuppressWarnings("rawtypes")
        @SkipValidation
        public void prepare()
        {
                empId=56;
                if(empId!=null)
                {
                        employee = (PersonalInformation)persistenceService.getSession().get(PersonalInformation.class, empId);
                }
                super.prepare();
                //employee = (PersonalInformation)persistenceService.getSession().merge(employee);
                addDropdownData("statusList", (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EgwStatus"));
                addDropdownData("empTypeMstrList", (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeStatusMaster"));
                addDropdownData("empGroupMstrList", (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-EmployeeGroupMaster"));
            addDropdownData("bloodGroupList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-BloodGroupMaster"));
            addDropdownData("langKnownMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesKnownMaster"));
            addDropdownData("religionMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-ReligionMaster"));
            addDropdownData("commMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-CommunityMaster"));
            addDropdownData("langQualiMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-LanguagesQulifiedMaster"));
            //For assignment tab
            addDropdownData("deptMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egi-department"));
            addDropdownData("fundlist",(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund"));
            addDropdownData("functionlist",(ArrayList)EgovMasterDataCaching.getInstance().get("egi-function"));
            addDropdownData("designationMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-DesignationMaster"));
            addDropdownData("posList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Position"));
            addDropdownData("functionaryMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Functionary"));
            addDropdownData("gradeMasterList",(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-GradeMaster"));
            
            
            isEmpActive = employee.getIsActive()==1?true:false;
            empCodeAutogen = checkAutoGenerateCode();
            
        }
        
        public EmployeeMasterAction(){
                addRelatedEntity("groupCatMstr",EmployeeGroupMaster.class);
                addRelatedEntity("languagesKnownMstr", LanguagesKnownMaster.class);
                addRelatedEntity("bloodGroupMstr", BloodGroupMaster.class);
                addRelatedEntity("employeeTypeMaster", EmployeeStatusMaster.class);
                addRelatedEntity("StatusMaster", EgwStatus.class);
                addRelatedEntity("religionMstr", ReligionMaster.class);
                addRelatedEntity("langQulMstr", LanguagesQulifiedMaster.class);
                addRelatedEntity("communityMstr", CommunityMaster.class);
                addRelatedEntity("userMaster",User.class);
                addRelatedEntity("egpimsLangKnowns", LangKnown.class);
        }
        
        @SkipValidation
        //@Action(value="/employeeMaster/employeeMaster.action",results=@Result(location="/employeeMaster/employeeMaster-new.jsp"))     
        public String newForm()
        {
                this.mode = "Create";
                return NEW;
        }
        
        public String execute()
        {
                loadEmployee();
                
                return NEW;
        }
        
        @ValidationErrorPage(NEW)
        @SuppressWarnings("unchecked")
        public String save()
        {
                user = eisUserMgr.getUserByID(Long.valueOf(EGOVThreadLocals.getUserId()));
                
                if(mode.equalsIgnoreCase("create"))
                {
                        employee.setIsActive(Integer.valueOf(1));
                }
                else
                {
                        employee.setIsActive(isEmpActive?1:0);
                }
                if(employee.getStatusMaster().getCode().equalsIgnoreCase("deceased"))
                {
                        employee.setIsActive(Integer.valueOf(0));
                }
                if(empCodeAutogen && mode.equalsIgnoreCase("create"))
                {
                        employee.setEmployeeCode(personalInformationServiceOld.generateEmployeeCode(employee));
                }
                
                setEmpPersonalDetails();
                setEmpLanguagesKnown();
                setEmpUserMaster();
                setEmpAssignments();
                persistenceService.getSession().saveOrUpdate(employee);
                if(mode.equalsIgnoreCase("create"))
                {
                  //FIXME 
                        //doAuditing(AuditEntity.EIS_EMPLOYEE,AuditEvent.CREATED,employee,assignmentList);
                        createAccountDetailKeyForEmployee(employee);
                }
                else
                {
                        //FIXME 
                    //doAuditing(AuditEntity.EIS_EMPLOYEE,AuditEvent.MODIFIED,employee,assignmentList);
                }
                addActionMessage(getText("emp.created.updated.successfully"));
                this.mode="View";
                return NEW;
        }
        
        @SuppressWarnings("unchecked")
        @SkipValidation
        @ValidationErrorPage(NEW)
        @Action(value="/employeeMaster/employeeMaster.action",results=@Result(location="/employeeMaster/employeeMaster-new.jsp"))
        public String loadEmployee()
        {
                assignmentList = (List<Assignment>)persistenceService.findAllBy("from Assignment where employee.idPersonalInformation=?", empId);
                populateLangKnown();
                return NEW;
        }
        
        private void populateLangKnown()
        {
                if(employee.getEgpimsLangKnowns()!=null)
                {
                        for(LangKnown lang:employee.getEgpimsLangKnowns())
                        {
                                langKnownList.add(lang.getLangKnown().getId());
                        }
                }
        }
        
        private void setEmpPersonalDetails()
        {
                // Employee name
                if(!employee.getEmployeeFirstName().isEmpty() && employee.getEmployeeFirstName().equalsIgnoreCase(FIRSTNAME))
                {
                        employee.setEmployeeFirstName(null);
                }
                if(!employee.getEmployeeMiddleName().isEmpty() && employee.getEmployeeMiddleName().equalsIgnoreCase(MIDDLENAME))
                {
                        employee.setEmployeeMiddleName(null);
                }
                if(!employee.getEmployeeLastName().isEmpty() && employee.getEmployeeLastName().equalsIgnoreCase(LASTNAME))
                {
                        employee.setEmployeeLastName(null);
                }
                
                //setting employee full name
                        employee.setEmployeeName((employee.getEmployeeFirstName()!=null?employee.getEmployeeFirstName()+" ":"")+""
                                                                         +(employee.getEmployeeMiddleName()!=null?employee.getEmployeeMiddleName()+" ":"")+""
                                                         +(employee.getEmployeeLastName()!=null?employee.getEmployeeLastName():""));

                //Employee father or husband name
                
                if(!employee.getFatherHusbandFirstName().isEmpty() && employee.getFatherHusbandFirstName().equalsIgnoreCase(FIRSTNAME))
                {
                        employee.setFatherHusbandFirstName(null);
                }
                if(!employee.getFatherHusbandMiddleName().isEmpty() && employee.getFatherHusbandMiddleName().equalsIgnoreCase(MIDDLENAME))
                {
                        employee.setFatherHusbandMiddleName(null);
                }
                if(!employee.getFatherHusbandLastName().isEmpty() && employee.getFatherHusbandLastName().equalsIgnoreCase(LASTNAME))
                {
                        employee.setFatherHusbandLastName(null);
                }
        }
        
        @SuppressWarnings("unchecked")
        private void setEmpAssignments()
        {
                Set<EmployeeDepartment> hodDeptFromDB = new HashSet<EmployeeDepartment>();
                
                for(Assignment empAssign:assignmentList)
                {
                        
                        Fund fund =  (Fund)persistenceService.getSession().load(Fund.class, empAssign.getFundId().getId());
                        empAssign.setFundId(fund);
                        
                        CFunction function = (CFunction)persistenceService.getSession().load(CFunction.class, empAssign.getFunctionId().getId());
                        empAssign.setFunctionId(function);
                        
                        Functionary functionary  = (Functionary)persistenceService.getSession().load(Functionary.class, empAssign.getFunctionary().getId());
                        empAssign.setFunctionary(functionary);
                        
                        Position position = (Position)persistenceService.getSession().load(Position.class, empAssign.getPosition().getId());
                        empAssign.setPosition(position);
                        
                        DesignationMaster designation = (DesignationMaster)persistenceService.getSession().load(DesignationMaster.class, empAssign.getDesigId().getDesignationId());
                        empAssign.setDesigId(designation);
                        
                        DepartmentImpl department = (DepartmentImpl)persistenceService.getSession().load(DepartmentImpl.class, empAssign.getDeptId().getId());
                        empAssign.setDeptId(department);
                        
                        if(empAssign.getCreatedBy().getId()!=null)
                        {       
                                User createdBy = (User)persistenceService.getSession().load(User.class, empAssign.getCreatedBy().getId());
                                empAssign.setCreatedBy( createdBy);
                        }       
                        
                        EmployeeDepartment hodDept;
                        
                        if(empAssign.getId()!=null)
                        {
                                //get all the hod depts for the current assignment
                                hodDeptFromDB.addAll((Collection<? extends EmployeeDepartment>)persistenceService.findAllBy("select assign.deptSet from " +
                                                                                "Assignment assign where assign.id=?",empAssign.getId()));

                        }
                        if(empAssign.getHodDeptIds().size()>0)
                        {
                                
                                for(Integer hodId:empAssign.getHodDeptIds())
                                {
                                        if(employee.getIdPersonalInformation()!=null)
                                        {       
                                                hodDept = (EmployeeDepartment)persistenceService.find("from EmployeeDepartment where assignment." +
                                                        "employeeId.idPersonalInformation=? and hodept.id=?",employee.getIdPersonalInformation(),hodId);
                                        }
                                        else
                                                hodDept = null;
                                        if(hodDept!=null && !hodDeptFromDB.isEmpty())
                                                hodDeptFromDB.remove(hodDept);// mark the hod dept for removal
                                        else
                                        {
                                                hodDept = new EmployeeDepartment();
                                                Department dept =(DepartmentImpl) persistenceService.getSession().load(DepartmentImpl.class, hodId);
                                                hodDept.setAssignment(empAssign);
                                                hodDept.setDept(dept);
                                                hodDept.setHodept(dept);                                        
                                                empAssign.getDeptSet().add(hodDept);
                                        }
                                                
                                }
                        }
                        for(EmployeeDepartment hodDeptToBeDeleted:hodDeptFromDB)
                        {
                                empAssign.getDeptSet().remove(hodDeptToBeDeleted);// delete marked hod depts
                        }
                        if(mode.equalsIgnoreCase("create"))
                        {       
                                empAssign.setCreatedDate(new Date());
                                empAssign.setCreatedBy((User) user);
                        }       
                        empAssign.setModifiedBy((User) user);
                        empAssign.setModifiedDate(new Date());
                        persistenceService.getSession().saveOrUpdate(empAssign);
        }
}               
        
        private void setEmpLanguagesKnown()
        {
                
                Map<Integer,LangKnown> langKnownFromDB = new HashMap<Integer,LangKnown>();
                if(employee.getEgpimsLangKnowns()!=null)
                {
                        for(LangKnown lang:employee.getEgpimsLangKnowns())
                        {
                                langKnownFromDB.put(lang.getLangKnown().getId(),lang );
                        }
                }
                
                
                if(langKnownList.size()>0)
                {
                        
                        for(Integer langId:langKnownList)
                        {
                                if(!langKnownFromDB.containsKey(langId))
                                {
                                        LangKnown languagesKnown = new LangKnown();
                                        LanguagesKnownMaster langMaster = (LanguagesKnownMaster)persistenceService.getSession().load(LanguagesKnownMaster.class, langId);
                                        languagesKnown.setEmployeeId(employee);
                                        languagesKnown.setLangKnown(langMaster);
                                        employee.getEgpimsLangKnowns().add(languagesKnown);
                                }
                                else
                                        langKnownFromDB.remove(langId);
                                
                        }
                }
                
                for(Entry<Integer,LangKnown> entry:langKnownFromDB.entrySet())
                {
                        employee.getEgpimsLangKnowns().remove(entry.getValue());
                }
        }
        
        private void setEmpUserMaster()
        {
                if(employee.getUserMaster().getUsername().trim().isEmpty())
                {
                        employee.setUserMaster(null);
                }
                else
                {
                        try
                        {
                                User existingUser= (User) eisUserMgr.getUserByUserName(employee.getUserMaster().getUsername().trim());// to check if the entered user obj is present in db
                                if(existingUser==null)
                                {
                                        Role essRole = roleMgr.getRoleByName(EMP_SELF_SERVICE);
                                        /*Set<UserRole> roles = new HashSet<UserRole>();
                                        UserRole userrole = new UserRole();*/
                                        /*User user = new User();
                                        userrole.setRole(essRole);
                                        userrole.setIsHistory('N');
                                        userrole.setUser(user);
                                        userrole.setFromDate(new Date());
                                        userrole.setToDate(employee.getRetirementDate());// egi screen 'Search user' can be used to update the todate, in case retirement date is changed
                                        roles.add(userrole);*/
                                        //user.setUserRoles(roles);
                                        
                                        user.setUsername(employee.getUserMaster().getUsername().trim());
                                        //user.setPwdReminder(employee.getEmployeeFirstName()+"_"+employee.getCode());
                                        user.setPassword(employee.getEmployeeFirstName()+"_"+employee.getCode());//usermanager.createUser will take plain pwd and encrypt it and set the encrypted value back
                                        //user.setIsSuspended('N');
                                        user.setPwdExpiryDate(new Date());
                                        user.setName(employee.getEmployeeFirstName());
                                        user.setActive(employee.getUserMaster().isActive());
                                        
                                        eisUserMgr.createUser(user);
                                        employee.setUserMaster(user);
                                }       
                                else
                                {
                                        existingUser.setActive(employee.getUserMaster().isActive());
                                        employee.setUserMaster(existingUser);
                                }
                        }
                        catch(Exception ex){
                                LOGGER.error(ex.getMessage());
                                   throw new EGOVRuntimeException(ex.getMessage(),ex);
                        }
                }
        }
        
        public void validateSave()
        {
                if(employee.getStatusMaster()==null || employee.getStatusMaster().getId()==0)
                {
                        addFieldError("StatusMaster", getText("alertStatus"));
                }
                if(!empCodeAutogen && (employee.getEmployeeCode().isEmpty() || employee.getEmployeeCode().equals("")))
                {
                        addFieldError("employeeCode", getText("alert.enter.empcode"));
                }
                if(!mode.equalsIgnoreCase("create") && (employee.getEmployeeCode().isEmpty() || employee.getEmployeeCode().equals("")))
                {
                        addFieldError("employeeCode", getText("alert.enter.empcode"));
                }
                if(employee.getEmployeeTypeMaster()==null || employee.getEmployeeTypeMaster().getId()==0)
                {
                        addFieldError("employeeTypeMaster", getText("alertEnterEmpType"));
                }
                if((employee.getEmployeeFirstName().isEmpty() && employee.getEmployeeFirstName().equalsIgnoreCase(FIRSTNAME)) || employee.getEmployeeFirstName().equals(""))
                {
                        addFieldError("employeeFirstName", getText("alertEnterFirstName"));
                }
                if(employee.getDateOfBirth()==null || employee.getDateOfBirth().toString().isEmpty())
                {
                        addFieldError("dateOfBirth", getText("alertEnterDob"));
                }
                else if (employee.getDateOfBirth().compareTo(new Date())>1) 
                {
                        addFieldError("dateOfBirth", getText("alertProperDOB"));
                }
                
                if(permanentAddress.isEmpty() || permanentAddress.equals(""))
                {
                        addFieldError("permanentAddress", getText("alertPermAddr"));
                }
                if(employee.getDateOfFirstAppointment()==null || employee.getDateOfFirstAppointment().toString().isEmpty())
                {
                        addFieldError("dateOfFirstAppointment", getText("alertDoApp"));
                }
                if(employee.getDateOfjoin()==null || employee.getDateOfjoin().toString().isEmpty())
                {
                        addFieldError("dateOfjoin", getText("alert.enter.doj"));
                }
                if((employee.getStatusMaster()!=null && employee.getStatusMaster().getCode().equalsIgnoreCase("deceased")) && 
                                (employee.getDeathDate()==null || employee.getDeathDate().toString().isEmpty()))
                {
                        addFieldError("deathDate",getText("alertEnterDeathDate"));
                }
                if((employee.getStatusMaster()!=null && employee.getStatusMaster().getCode().equalsIgnoreCase("retired")) && 
                                (employee.getDeathDate()==null || employee.getDeathDate().toString().isEmpty()))
                {
                        addFieldError("deathDate",getText("alertRetiredDate"));
                }
                if(employee.getRetirementAge()!=null && employee.getRetirementAge().compareTo(Integer.valueOf(50))<-1)
                {
                        addFieldError("retirementAge",getText("alertRetirementDateLess"));
                        
                }
                if(assignmentList.size()<1)
                {
                        addActionError(getText("alertEmpPrimaryAssign"));
                }
                else
                {
                        boolean isPrimaryExists=false;
                        for(Assignment assignment:assignmentList)
                        {
                                if(assignment.getIsPrimary()=='Y')
                                {
                                        isPrimaryExists=true;
                                        break;
                                }
                        }
                        if(!isPrimaryExists)
                        {
                                addActionError(getText("alertEmpPrimaryAssign"));
                        }
                }
                if(mode.equalsIgnoreCase("modify") && remarks.isEmpty())
                {
                        addFieldError("remarks",getText("alert.remarks"));
                }
        }
        
        private boolean checkAutoGenerateCode()
          {
                  return EisManagersUtill.getEisCommonsService().isEmployeeAutoGenerateCodeYesOrNo();
          }
        
        @SkipValidation
        public String checkEmpCodeForUniqueness()
        {
                return "empCodeUniCheck";
        }

        public Boolean getEmpCodeUniCheck()
        {
                Criteria empCriteria = persistenceService.getSession().createCriteria(PersonalInformation.class);
                empCriteria.add(Restrictions.eq("employeeCode",empCodeUniqueCheck));

                if(null!=empId && !empId.equals(""))
                        empCriteria.add(Restrictions.ne("idPersonalInformation",empId));

                return !empCriteria.list().isEmpty();
        }
        
        @SkipValidation
        public String checkPanNoForUniqueness()
        {
                return "panNoUniCheck";
        }

        public Boolean getPanNoUniCheck()
        {
                Criteria empCriteria = persistenceService.getSession().createCriteria(PersonalInformation.class);
                empCriteria.add(Restrictions.eq("panNumber",panNoUniqueCheck));

                if(null!=empId && !empId.equals(""))
                        empCriteria.add(Restrictions.ne("idPersonalInformation",empId));

                return !empCriteria.list().isEmpty();
        }
        
        /*//FIXME 
         * private void doAuditing(AuditEntity auditEntity, String action,PersonalInformation employee,List<Assignment> assign) {
                final String details1 = new StringBuffer("[ Employee code : ").
                                                                append(employee.getEmployeeCode()).
                                                                append(",  Status : ").append(employee.getStatusMaster().getDescription().toString()).
                                                                append(",  Employee Type : ").append(employee.getEmployeeTypeMaster().getName().toString()).
                                                                append(",  Is Active : ").append(employee.getIsActive()==1?"Yes":"No").
                                                                append(",  DOB : ").append(DateUtils.getDefaultFormattedDate(employee.getDateOfBirth())).
                                                                append(",  DOA : ").append(DateUtils.getDefaultFormattedDate(employee.getDateOfFirstAppointment())).
                                                                append(",  Retirement Date : ").append(DateUtils.getDefaultFormattedDate(employee.getRetirementDate())).
                                                                append(",  Retirement Age : ").append(employee.getRetirementAge()).
                                                                append(",  Pan Number : ").append(employee.getPanNumber()).
                                                                append(",  User Name : ").
                                                                append(employee.getUserMaster()==null?"":employee.getUserMaster().getUserName().toString()).
                                                                append(" ]").toString();
                StringBuffer details2=new StringBuffer(2000);
                for(Assignment assignment:assign)
                {
                        if(details2.length() != 0)
                                details2.append(" / ");//for next record
                        else
                                details2.append(" [ ");//for first record
                        
                        details2=details2.append("From Date: ").append(DateUtils.getDefaultFormattedDate(assignment.getFromDate())).append(" ,To Date: ")
                                        .append(DateUtils.getDefaultFormattedDate(assignment.getToDate())).append(",Dept:").append(assignment.getDeptId().getDeptName())
                                        .append(",Designation: ").append(assignment.getDesigId().getDesignationName()).
                                        append(",Position: ").append(assignment.getPosition().getName());
                        details2=details2.append(",Assignment Type: ").append(assignment.getIsPrimary()=='Y'?"Primary":"Temporary");
                        
                }
                if(mode.equalsIgnoreCase("modify"))
                        details2.append(",  Remarks : ").append(remarks).append(" ] ");
                else
                        details2.append(" ] ");//for last record
                final AuditEvent auditEvent = new AuditEvent(AuditModule.EIS, auditEntity, action, employee.getEmployeeCode(), details1);               
                auditEvent.setDetails2(details2.toString());
                auditEvent.setPkId(employee.getId().longValue());
                getAuditEventService().createAuditEvent(auditEvent, PersonalInformation.class);
        }*/
        
        private void createAccountDetailKeyForEmployee(PersonalInformation employee)
        {
                Accountdetailtype type = (Accountdetailtype)persistenceService.find("from Accountdetailtype where name=?", EisConstants.MODULE_EMPLOYEE);
                Accountdetailkey adk=new Accountdetailkey();
                adk.setAccountdetailtype(type);
                adk.setGroupid(1);
                adk.setDetailkey(employee.getIdPersonalInformation());
                adk.setDetailname(type.getAttributename());
                commonsMgr.createAccountdetailkey(adk);
        }
        
        public String getMode() {
                return mode;
        }

        public void setMode(String mode) {
                this.mode = mode;
        }

        public String getPermanentAddress() {
                return permanentAddress;
        }

        public void setPermanentAddress(String permanentAddress) {
                this.permanentAddress = permanentAddress;
        }

        public String getCorrespondenceAddress() {
                return correspondenceAddress;
        }

        public void setCorrespondenceAddress(String correspondenceAddress) {
                this.correspondenceAddress = correspondenceAddress;
        }

        public List<Integer> getLangKnownList() {
                return langKnownList;
        }

        public void setLangKnownList(List<Integer> langKnownList) {
                this.langKnownList = langKnownList;
        }

        public List<Assignment> getAssignmentList() {
                return assignmentList;
        }

        public void setAssignmentList(List<Assignment> assignmentList) {
                this.assignmentList = assignmentList;
        }

        public UserService getEisUserMgr() {
                return eisUserMgr;
        }

        public void setEisUserMgr(UserService eisUserMgr) {
                this.eisUserMgr = eisUserMgr;
        }

        public RoleService getRoleMgr() {
                return roleMgr;
        }

        public void setRoleMgr(RoleService roleMgr) {
                this.roleMgr = roleMgr;
        }

        public PersonalInformationService getPersonalInformationService() {
                return personalInformationServiceOld;
        }

        public void setPersonalInformationService(
                        PersonalInformationService personalInformationService) {
                this.personalInformationServiceOld = personalInformationService;
        }

        public Integer getEmpId() {
                return empId;
        }

        public void setEmpId(Integer empId) {
                this.empId = empId;
        }

        public boolean getIsEmpActive() {
                return isEmpActive;
        }

        public void setIsEmpActive(boolean isEmpActive) {
                this.isEmpActive = isEmpActive;
        }

        public boolean isEmpCodeAutogen() {
                return empCodeAutogen;
        }

        public void setEmpCodeAutogen(boolean empCodeAutogen) {
                this.empCodeAutogen = empCodeAutogen;
        }

        public String getEmpCodeUniqueCheck() {
                return empCodeUniqueCheck;
        }

        public void setEmpCodeUniqueCheck(String empCodeUniqueCheck) {
                this.empCodeUniqueCheck = empCodeUniqueCheck;
        }

        public String getPanNoUniqueCheck() {
                return panNoUniqueCheck;
        }

        public void setPanNoUniqueCheck(String panNoUniqueCheck) {
                this.panNoUniqueCheck = panNoUniqueCheck;
        }

        public String getRemarks() {
                return remarks;
        }

        public void setRemarks(String remarks) {
                this.remarks = remarks;
        }

        public CommonsService getCommonsMgr() {
                return commonsMgr;
        }

        public void setCommonsMgr(CommonsService commonsMgr) {
                this.commonsMgr = commonsMgr;
        }

        /*public AuditEventService getAuditEventService() {
                return auditEventService;
        }

        public void setAuditEventService(AuditEventService auditEventService) {
                this.auditEventService = auditEventService;
        }*/
        
}