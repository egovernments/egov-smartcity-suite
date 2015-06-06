/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this 
 *         Legal Notice.
 * 
 *      2) Any misrepresentation of the origin of the material is prohibited. It 
 *         is required that all modified versions of this material be marked in 
 *         reasonable ways as different from the original version.
 * 
 *      3) This license does not grant any rights to any user of the program 
 *         with regards to rights under trademark law for use of the trade names 
 *         or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.voucher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.commons.Relation;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.utils.EntityType;
import org.egov.egf.commons.EgovCommon;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.masters.model.AccountEntity;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.contra.ContraJournalVoucher;
import org.egov.model.receipt.ReceiptVoucher;
import org.egov.model.voucher.PreApprovedVoucher;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.services.bills.BillsService;
import org.egov.services.contra.ContraService;
import org.egov.services.receipt.ReceiptService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.transactions.VoucherTypeForULB;

@Results( {
        @Result(name = "editVoucher", type = "redirectAction", location = "journalVoucherModify", params = {"namespace", "/voucher", "method", "beforeModify" })})
@Transactional(readOnly=true)
@ParentPackage("egov")
public class PreApprovedVoucherAction extends BaseFormAction
{
        private static final String EGF = "EGF";
        private static final String EMPTY_STRING="";
        private final String                    BILLPAYMENT                     = "billpayment";
        private static final long serialVersionUID = 1L;
        private VoucherService voucherService;
        private CVoucherHeader voucherHeader = new CVoucherHeader();
        private EgBillregister egBillregister = new EgBillregister();
        private SimpleWorkflowService<CVoucherHeader> voucherWorkflowService;
        protected EisCommonService eisCommonService;
        @Autowired
        private EgwStatusHibernateDAO egwStatusDAO; 
        private List<EgBillregister> preApprovedVoucherList;
        protected List<String> headerFields = new ArrayList<String>();
        protected List<String> mandatoryFields = new ArrayList<String>();
        protected EisUtilService eisService;
        private static  BillsService billsMngr=null;
        private GenericHibernateDaoFactory genericDao;
        private BillsAccountingService billsAccountingService;
        private BillsService billsManager;
        private static final Logger LOGGER = Logger.getLogger(PreApprovedVoucherAction.class);
        protected FinancialYearHibernateDAO financialYearDAO;
        private final PreApprovedVoucher preApprovedVoucher = new PreApprovedVoucher();
        private List<PreApprovedVoucher> billDetailslist;
        private List<PreApprovedVoucher> subLedgerlist;
        private ReceiptVoucher receiptVoucher;
        private ContraJournalVoucher contraVoucher;
        private static final String ERROR="error";

        private static final String BILLID="billid";
        private static final String VOUCHEREDIT="voucheredit";
        private static final String VHID="vhid";
        private static final String CGN="cgn";
        private static final String VOUCHERQUERY=" from CVoucherHeader where id=?";
        private static final String VOUCHERQUERYBYCGN=" from CVoucherHeader where cgn=?";
        private static final String ACCDETAILTYPEQUERY=" from Accountdetailtype where id=?";
        private static final String ACTIONNAME="actionName";
        private String values="",from="";
        private String methodName="";
        private Integer departmentId;
        private String type;
        private String wfitemstate;
        private String finConstExpendTypeContingency;
        private String voucherNumber;
        private Boolean displayVoucherNumber = true;
        SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService;
        SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService;
        private SequenceGenerator sequenceGenerator;
        private Map<String, Object> billDetails;
        //private Long vhid;
        private VoucherHelper voucherHelper;
        private JournalVoucherModifyAction journalvouchermodify; 
        private boolean showVoucherDate;
        private ScriptService scriptService;
        
        

        @Override
        public Object getModel() {
                return preApprovedVoucher;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void prepare() {
                super.prepare();
                addDropdownData("departmentList", Collections.EMPTY_LIST);
                addDropdownData("designationList", Collections.EMPTY_LIST);
                addDropdownData("userList", Collections.EMPTY_LIST);
        }




        @SkipValidation
        public String list()
        {
                if(getValidActions("designation").size()==0)
                {
                        addActionError(getText("pjv.designation.notmatching"));
                }
                else
                {
                        final EgwStatus egwStatus = egwStatusDAO.getStatusByModuleAndCode("SBILL", "Approved");
                        getHeaderMandateFields();
                        if(isFieldMandatory("department"))
                                preApprovedVoucherList = getPersistenceService().findAllBy(" from EgBillregister br where br.status=? and br.egBillregistermis.egDepartment.id=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ", egwStatus,getCurrentDepartment().getId());
                        else
                                preApprovedVoucherList = getPersistenceService().findAllBy(" from EgBillregister br where br.status=? and ( br.egBillregistermis.voucherHeader is null or br.egBillregistermis.voucherHeader in (from CVoucherHeader vh where vh.status =4 )) ", egwStatus);

                        if(LOGGER.isDebugEnabled())     LOGGER.debug(preApprovedVoucherList);
                }
                return "list";
        }
        @SkipValidation
        @Action(value="/voucher/preApprovedVoucher-voucher")
        public String voucher()
        {
                egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
                if(LOGGER.isDebugEnabled())     LOGGER.debug("egBillregister=="+egBillregister);
                final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","pjv_saveasworkingcopy_enabled");
                final String pjv_wc_enabled = appList.get(0).getValue();
                // loading aprover user info
                preApprovedVoucher.setVoucherDate(new Date());
                type = egBillregister.getExpendituretype();
                getHeaderMandateFields();
                String purposeValueVN="",purposeValue="";
                try{
                        List<AppConfigValues> configValues =genericDao.getAppConfigValuesDAO().
                                        getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,"VOUCHERDATE_FROM_UI"); 
                        
                        for (AppConfigValues appConfigVal : configValues) {
                                          purposeValueVN=appConfigVal.getValue();
                                         }
                        } catch (Exception e) {
                                 throw new EGOVRuntimeException("Appconfig value for VOUCHERDATE_FROM_UI is not defined in the system");
                        }
                if(purposeValueVN.equals("Y")){
                        showVoucherDate=true;
                }
                
                loadApproverUser(type);
                if("Y".equals(pjv_wc_enabled)){
                        try {
                                // loading the bill detail info.
                                getMasterDataForBillVoucher();
                        } catch (Exception e) {
                                 List<ValidationError> errors=new ArrayList<ValidationError>();
                                 errors.add(new ValidationError("exp",e.getMessage()));
                                 throw new ValidationException(errors);
                        }
                        return VOUCHEREDIT;
                }
                else{
                        try {
                                // loading the bill detail info.
                                getMasterDataForBill();
                        } catch (Exception e) {
                                 List<ValidationError> errors=new ArrayList<ValidationError>();
                                 errors.add(new ValidationError("exp",e.getMessage()));
                                 throw new ValidationException(errors);
                        }
                        return "billview";
                }

        }

        @SkipValidation
        @Action(value="/voucher/preApprovedVoucher-loadvoucher")
        public String loadvoucher()
        {
                String result = null;
                voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
                boolean ismodifyJv=false; 
                if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherHeader=="+voucherHeader);
                if(voucherHeader != null && voucherHeader.getState() != null){
                        if(!validateOwner(voucherHeader.getState())){
                                 List<ValidationError> errors=new ArrayList<ValidationError>();
                                 errors.add(new ValidationError("exp","Invalid User"));
                                 throw new ValidationException(errors);
                        }
                }
                List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","pjv_saveasworkingcopy_enabled");
                String pjv_wc_enabled = appList.get(0).getValue();
                
                type = billsManager.getBillTypeforVoucher(voucherHeader);
                if(null == type ){
                        // when the work flow started internally then define the type value to "default".systems using the API CreateVoucher.createPreApprovedVoucher()
                        type = "default";
                }
                try {
                        // loading the bill detail info.
                        getMasterDataForBillVoucher();
                } catch (Exception e) {
                         List<ValidationError> errors=new ArrayList<ValidationError>();
                         errors.add(new ValidationError("exp",e.getMessage()));
                         throw new ValidationException(errors);
                }
                getHeaderMandateFields(); 
        getSession().put("voucherId", parameters.get(VHID)[0]);       
                
                if(voucherHeader.getState()!=null && voucherHeader.getState().getValue().contains("REJECTED"))
                {        
                        if(voucherHeader.getModuleId()==null){
                                EgBillregistermis billMis =(EgBillregistermis) persistenceService.find("from EgBillregistermis where voucherHeader.id=?",voucherHeader.getId() );
                                if(billMis!=null)
                                {
                                        State billWorkFlowState = billMis.getEgBillregister().getState();
                                        if(billWorkFlowState==null)
                                        {
                                                result="editVoucher"; 
                                                ismodifyJv=true;
                                        }
                                
                                }else if(voucherHeader.getName().equalsIgnoreCase(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL))
                                {
                                        type=FinancialConstants.JOURNALVOUCHER_NAME_GENERAL;  
                                        result="editVoucher";
                                        ismodifyJv=true;
                                }
                        }  
                }
                        loadApproverUser(type); 
                        if(!ismodifyJv){
                                if("Y".equals(pjv_wc_enabled))
                                        result=VOUCHEREDIT;
                                else   
                                        result= "voucherview";
                        }
                   
                //vhid = voucherHeader.getId();
                /*}*/return result;
        }

        @SuppressWarnings("unchecked")
        private void loadApproverUser(String type){
                String scriptName = "billvoucher.nextDesg";
                departmentId = voucherService.getCurrentDepartment().getId().intValue();
                EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
                Map<String, Object>  map = voucherService.getDesgByDeptAndType(type, scriptName);
                if(null == map.get("wfitemstate")){
                //  If the department is mandatory show the logged in users assigned department only.
                        if(mandatoryFields.contains("department")){
                                addDropdownData("departmentList", voucherHelper.getAllAssgnDeptforUser());
                        }else{
                                addDropdownData("departmentList", masterCache.get("egi-department"));
                        }
                        addDropdownData("designationList", (List<Designation>)map.get("designationList"));
                        wfitemstate="";
                }else{
                        wfitemstate = map.get("wfitemstate").toString();
                }


        }
        @SkipValidation
        @Action(value="/voucher/preApprovedVoucher-loadvoucherview")
        public String loadvoucherview() throws EGOVException
        {
                billDetails = new HashMap<String, Object>();
                if(parameters.get("from")!=null && FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT.equals(parameters.get("from")[0]))
                {
                        receiptVoucher= (ReceiptVoucher) persistenceService.find("from ReceiptVoucher where id=?", Long.valueOf(parameters.get(VHID)[0]));
                        voucherHeader = receiptVoucher.getVoucherHeader();
                        from=FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT;
                }
                else if(parameters.get("from")!=null && FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0]))
                {
                        contraVoucher = (ContraJournalVoucher) persistenceService.find(" from ContraJournalVoucher where id=?",Long.valueOf(parameters.get(VHID)[0]));
                        voucherHeader = contraVoucher.getVoucherHeaderId();
                        from=FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA;
                }
                else
                {
                        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
                        from=FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL;
                }
                /*List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
                voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
                billDetails.put("voucherDetailList", voucherDetailList);*/
                getMasterDataForBillVoucher();
                getHeaderMandateFields();
                return "view";
        }
        @SkipValidation
        public List<org.egov.infstr.workflow.Action> getValidActions(){
                if(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT.equals(parameters.get("from")[0]))
                        return receiptWorkflowService.getValidActions(receiptVoucher);
                else if(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equals(parameters.get("from")[0]))
                        return contraWorkflowService.getValidActions(contraVoucher);
                else
                        return null;
        }
        @SkipValidation
        public String approve() throws EGOVException
        {
                ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
                                 "classpath:org/serviceconfig-Bean.xml","classpath:org/egov/infstr/beanfactory/globalApplicationContext.xml","classpath:org/egov/infstr/beanfactory/applicationContext-egf.xml" });
                voucherHeader = (CVoucherHeader) persistenceService.find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
                if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT))
                {
                        receiptVoucher = (ReceiptVoucher) persistenceService.find("from ReceiptVoucher where id=?", Long.valueOf(parameters.get(VHID)[0]));
                        ReceiptService receiptService = (ReceiptService) applicationContext.getBean("receiptService");
                        receiptWorkflowService.transition(parameters.get(ACTIONNAME)[0], receiptVoucher, parameters.get("comments")[0]);
                        receiptService.persist(receiptVoucher);
                        addActionMsg(receiptVoucher.getState().getValue(), receiptVoucher.getState().getOwnerPosition());
                }
                else if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
                {
                        contraVoucher = (ContraJournalVoucher) persistenceService.find("from ContraJournalVoucher where id=?", Long.valueOf(parameters.get("contraId")[0]));
                        ContraService contraService = (ContraService) applicationContext.getBean("contraService");
                        contraWorkflowService.transition(parameters.get(ACTIONNAME)[0], contraVoucher, parameters.get("comments")[0]);
                        contraService.persist(contraVoucher); 
                        addActionMsg(contraVoucher.getState().getValue(), contraVoucher.getState().getOwnerPosition());
                }
                else if(voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL) || voucherHeader.getType().equals(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
                {
                        voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0], voucherHeader,parameters.get("comments")[0]);
                        voucherService.persist(voucherHeader);
                        addActionMsg(voucherHeader.getState().getValue(), voucherHeader.getState().getOwnerPosition());
                }
                methodName="approval";
                getMasterDataForBillVoucher();
                return "view";
        }
        private void addActionMsg(final String stateValue,final Position pos)
        {
                if(parameters.get(ACTIONNAME)[0].contains("approve"))
                        if("END".equals(stateValue))
                                addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
                        else
                                addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));
                else if(parameters.get(ACTIONNAME)[0].contains("co_reject") || parameters.get(ACTIONNAME)[0].contains("aa_reject") || "END".equals(stateValue) )
                        addActionMessage(getText("voucher.cancelled"));
                else
                        addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(pos)}));

        }
        @SkipValidation
        @Action(value="/voucher/preApprovedVoucher-loadvoucherviewByCGN")
        public String loadvoucherviewByCGN() throws EGOVException
        {
                billDetails = new HashMap<String, Object>();
                voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERYBYCGN, parameters.get(CGN)[0]);
                /*List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
                voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
                billDetails.put("voucherDetailList", voucherDetailList);*/
                getHeaderMandateFields();
                getMasterDataForBillVoucher();
                return "view";
        }

        public String save() throws ValidationException
        {
                try
                {
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("bill id======="+parameters.get(BILLID)[0]);
                        methodName="save";
                        // check budgetary check
                        egBillregister=billsMngr.getBillRegisterById(Integer.valueOf(parameters.get(BILLID)[0]));
                        //egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
                        if(!financialYearDAO.isSameFinancialYear(egBillregister.getBilldate(), preApprovedVoucher.getVoucherDate()))
                        {
                                throw new ValidationException("Voucher could not be permitted in the current year for the Bill prepared in the previous financial year/s","Voucher could not be permitted in the current year for the Bill prepared in the previous financial year/s");
                        }
                        getMasterDataForBill();
                        //Check if budgetary Appropriation is enabled for the application. Only if required we need to do the check.
                        List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetCheckRequired");
                        Long vhid = null;
                        if(list.isEmpty())
                                throw new ValidationException(EMPTY_STRING,"budgetCheckRequired is not defined in AppConfig");
                        boolean checkReq=false;
                                vhid =billsAccountingService.createPreApprovedVoucherFromBill(Integer.parseInt(parameters.get(BILLID)[0]), voucherNumber, preApprovedVoucher.getVoucherDate());
                                if(LOGGER.isDebugEnabled())     LOGGER.debug("voucher id======="+vhid);
                        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
                        voucherHeader.setLastModifiedDate(new DateTime());
                        voucherHeader.start().withOwner(getPosition()).withComments(parameters.get("comments")[0]);
                        sendForApproval();
                        addActionMessage(getText(egBillregister.getExpendituretype()+".voucher.created",new String[]{voucherHeader.getVoucherNumber(),voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                        
                }catch(ValidationException e)
                {
                        LOGGER.error(e.getErrors());
                        voucher();
                        throw new ValidationException(e.getErrors());
                }
                catch(Exception e)
                {

                        if(e.getCause().getClass().equals(ValidationException.class))
                        {
                                ValidationException s =(ValidationException) e;
                                throw new ValidationException(s.getErrors());
                        }
                        LOGGER.error(e.getMessage());
                        List<ValidationError> errors=new ArrayList<ValidationError>();
                        errors.add(new ValidationError("exception",e.getCause().getMessage()));
                        loadApproverUser(type);
                        throw new ValidationException(errors);
                }
                
                displayVoucherNumber = false;
                return "billview";
        }

        public String update() throws ValidationException
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("voucher id======="+parameters.get(VHID)[0]);
                methodName="update";
                sendForApproval();
                type = billsManager.getBillTypeforVoucher(voucherHeader);
                if(null == type ){
                        type = "default";
                }
                if(parameters.get(ACTIONNAME)[0].contains("aa_reject")){
                        addActionMessage(getText("billVoucher.file.canceled"));
                }else{
                        if(parameters.get(ACTIONNAME)[0].contains("approve")){
                                if("END".equals(voucherHeader.getState().getValue()))
                                        addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
                                else
                                        addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                        }
                        else{
                                addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                        }
                }


                return "message";
        }

        public String saveAsWorkingCopy() throws ValidationException
        {
                methodName="saveAsWorkingCopy";
                if(parameters.get(VHID)[0].equals(""))
                {
                        egBillregister = (EgBillregister) getPersistenceService().find(" from EgBillregister where id=?", Long.valueOf(parameters.get(BILLID)[0]));
                        final boolean budgetcheck = voucherService.budgetaryCheck(egBillregister);
                        if(budgetcheck)
                        {
                                if(LOGGER.isDebugEnabled())     LOGGER.debug("saveAsWorkingCopy==========="+billDetailslist+subLedgerlist);
                        //TO-DO : User create voucher Api
                                final Long vhid =billsAccountingService.createPreApprovedVoucherFromBillForPJV(Integer.valueOf(parameters.get(BILLID)[0]),billDetailslist,subLedgerlist);
                            if(LOGGER.isDebugEnabled())     LOGGER.debug("voucher id======="+vhid);
                            voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
                                voucherHeader.start().withOwner(getPosition());
                                addActionMessage(getText("pjv.voucher.wc.created",new String[]{voucherHeader.getVoucherNumber()}));
                        }
                        else
                                addActionError(getText("pjv.budgetcheck.failed"));
                }
                else
                {
                        final Long vhid = Long.valueOf(parameters.get(VHID)[0]);
                        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, vhid);
                        billsAccountingService.updatePJV(voucherHeader, billDetailslist, subLedgerlist);
                        addActionMessage(getText("pjv.voucher.updated"));
                }
                return "voucheredit";
        }
        public void sendForApproval()
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PreApprovedVoucherAction | sendForApproval | Start");
                if(voucherHeader.getId()==null)
                        voucherHeader = (CVoucherHeader) getPersistenceService().find(VOUCHERQUERY, Long.valueOf(parameters.get(VHID)[0]));
                if(voucherHeader != null && voucherHeader.getState() != null){
                        if(!validateOwner(voucherHeader.getState())){
                                 List<ValidationError> errors=new ArrayList<ValidationError>();
                                 errors.add(new ValidationError("exp","Invalid User"));
                                 throw new ValidationException(errors);
                        }
                }
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Voucherheader=="+voucherHeader.getId()+", actionname="+parameters.get(ACTIONNAME)[0]);
                Integer userId = null;
                if(parameters.get("actionName")[0].contains("approve")){
                         userId = parameters.get("approverUserId")!=null?Integer.valueOf(parameters.get("approverUserId")[0]):
                                                                                                                        EgovThreadLocals.getUserId().intValue();
                }
                else{
                        userId = voucherHeader.getCreatedBy().getId().intValue();
                }

                if(LOGGER.isDebugEnabled())     LOGGER.debug("User selected id is : "+userId);
                voucherWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, voucherHeader,parameters.get("comments")[0]);
                voucherService.persist(voucherHeader);
        }

        public String sendForApprovalForWC() throws ValidationException
        {
                methodName="sendForApprovalForWC";
                sendForApproval();
                if(parameters.get(ACTIONNAME)[0].contains("approve"))
                        if("END".equals(voucherHeader.getState().getValue()))
                                addActionMessage(getText("pjv.voucher.final.approval",new String[]{"The File has been approved"}));
                        else
                                addActionMessage(getText("pjv.voucher.approved",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                else
                        addActionMessage(getText("pjv.voucher.rejected",new String[]{voucherService.getEmployeeNameForPositionId(voucherHeader.getState().getOwnerPosition())}));
                return "message";
        }

        public void setEisCommonService(final EisCommonService eisCommonService) {
                this.eisCommonService = eisCommonService;
        }

        public List<EgBillregister> getPreApprovedVoucherList() {
                return preApprovedVoucherList;
        }

        public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
                this.sequenceGenerator = sequenceGenerator;
        }

        public void validate()
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("validate==============");
        /*      if(parameters.get("comments")[0]!=null && parameters.get("comments")[0].length()>1024)
                        addFieldError("comments", "Max. length exceeded");*/
        }

        public void setEgBillregister(final EgBillregister egBillregister) {
                this.egBillregister = egBillregister;
        }
        public Department getCurrentDepartment()
        {
                return voucherService.getCurrentDepartment();
        }
        public String getMasterName(final String name)
        {
                String val="";
                if(voucherHeader.getVoucherDetail().size()>0)
                {
                        if(name.equals("fund") && voucherHeader.getFundId()!=null)
                                val = voucherHeader.getFundId().getName();
                        else if(name.equals("fundsource") && voucherHeader.getVouchermis().getFundsource()!=null)
                                val = voucherHeader.getVouchermis().getFundsource().getName();
                        else if(name.equals("department") && voucherHeader.getVouchermis().getDepartmentid()!=null)
                                val = voucherHeader.getVouchermis().getDepartmentid().getName();
                        else if(name.equals("scheme") && voucherHeader.getVouchermis().getSchemeid()!=null)
                                val = voucherHeader.getVouchermis().getSchemeid().getName();
                        else if(name.equals("subscheme") && voucherHeader.getVouchermis().getSubschemeid()!=null)
                                val = voucherHeader.getVouchermis().getSubschemeid().getName();
                        else if(name.equals("field") && voucherHeader.getVouchermis().getDivisionid()!=null)
                                val = voucherHeader.getVouchermis().getDivisionid().getName();
                        else if(name.equals("functionary") && voucherHeader.getVouchermis().getFunctionary()!=null)
                                val = voucherHeader.getVouchermis().getFunctionary().getName();
                        else if("narration".equals(name))
                                val = voucherHeader.getDescription();
                        else if("billnumber".equals(name))
                                val = ((EgBillregister)getPersistenceService().find(" from EgBillregister br where br.egBillregistermis.voucherHeader=?",voucherHeader)).getBillnumber();
                }
                else
                {
                        if(name.equals("fund") && egBillregister.getEgBillregistermis().getFund()!=null)
                                val = egBillregister.getEgBillregistermis().getFund().getName();
                        else if(name.equals("fundsource") && egBillregister.getEgBillregistermis().getFundsource()!=null)
                                val = egBillregister.getEgBillregistermis().getFundsource().getName();
                        else if(name.equals("department") && egBillregister.getEgBillregistermis().getEgDepartment()!=null)
                                val = egBillregister.getEgBillregistermis().getEgDepartment().getName();
                        else if(name.equals("scheme") && egBillregister.getEgBillregistermis().getScheme()!=null)
                                val = egBillregister.getEgBillregistermis().getScheme().getName();
                        else if(name.equals("subscheme") &&  egBillregister.getEgBillregistermis().getSubScheme()!=null)
                                val = egBillregister.getEgBillregistermis().getSubScheme().getName();
                        else if(name.equals("field") && egBillregister.getEgBillregistermis().getFieldid()!=null)
                                val = egBillregister.getEgBillregistermis().getFieldid().getName();
                        else if(name.equals("functionary") && egBillregister.getEgBillregistermis().getFunctionaryid()!=null)
                                val = egBillregister.getEgBillregistermis().getFunctionaryid().getName();
                        else if("narration".equals(name))
                                val = egBillregister.getEgBillregistermis().getNarration();
                        else if("billnumber".equals(name))
                                val = egBillregister.getBillnumber();
                }
                return val;
        }
        public String getSourcePath(){
                String sourcePath;
                if(voucherHeader.getVoucherDetail().size()>0)
                {
                        sourcePath = voucherHeader.getVouchermis().getSourcePath();
                }else{
                        sourcePath = egBillregister.getEgBillregistermis().getSourcePath();
                }
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Source Path = "+ sourcePath);
                return sourcePath;
        }
        public Map<String, Object> getMasterName() throws EGOVException{
                if(LOGGER.isDebugEnabled())     LOGGER.debug("getmastername===============");
                final Map<String, Object> names = new HashMap<String, Object>();
                // to get the ledger.
                List<CGeneralLedger> gllist = getPersistenceService().findAllBy(" from CGeneralLedger where voucherHeaderId.id=? order by voucherlineId", Long.valueOf(voucherHeader.getId()+""));
                Map<String,Object> temp = null;
                List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
                for(CGeneralLedger gl :gllist)
                {
                        if(gl.getFunctionId()!=null)
                                names.put(gl.getGlcodeId().getGlcode(), ((CFunction)getPersistenceService().find(" from CFunction where id=?",Long.valueOf(gl.getFunctionId()))).getName());

                        // get subledger.
                        List<CGeneralLedgerDetail> gldetailList = getPersistenceService().findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", Integer.valueOf(gl.getId()+""));
                        if(gldetailList!=null && !gldetailList.isEmpty())
                        {
                                for(CGeneralLedgerDetail gldetail:gldetailList)
                                {
                                        temp = new HashMap<String,Object>();
                                        temp = getAccountDetails(gldetail.getDetailTypeId(),gldetail.getDetailKeyId(),temp);
                                        temp.put(Constants.GLCODE, gl.getGlcode());
                                        temp.put("amount", gldetail.getAmount());
                                        tempList.add(temp);
                                }
                                names.put("tempList", tempList);
                        }
                }
                if(LOGGER.isDebugEnabled())     LOGGER.debug("voucher details =="+names);
                return names;
        }

        public void getMasterDataForBill() throws EGOVException
        {
                billDetails = new HashMap<String, Object>();
                CChartOfAccounts coa=null;
                Map<String,Object> temp = null;
                Map<String,Object> payeeMap= null;
                List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
                List<Map<String,Object>> payeeList=new ArrayList<Map<String,Object>>();

                List<EgBilldetails> egBillDetails = persistenceService.findAllBy("from EgBilldetails where  egBillregister.id=? order by  decode(debitamount,null,0, debitamount) desc ,decode(creditamount,null,0, creditamount) asc", egBillregister.getId());

                for(EgBilldetails billdetails:egBillDetails)
                {
                        temp = new HashMap<String,Object>();
                        if(billdetails.getFunctionid()!=null)
                                temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(billdetails.getFunctionid()+""))).getName());
                        coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?", Long.valueOf(billdetails.getGlcodeid()+""));
                        temp.put(Constants.GLCODE, coa.getGlcode());
                        temp.put("accounthead", coa.getName());
                        temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount()==null?0:billdetails.getDebitamount());
                        temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount()==null?0:billdetails.getCreditamount());
                        temp.put("billdetailid", billdetails.getId());
                        tempList.add(temp);

                        for(EgBillPayeedetails payeeDetails:billdetails.getEgBillPaydetailes())
                        {
                                payeeMap = new HashMap<String,Object>();
                                payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(),payeeDetails.getAccountDetailKeyId(),payeeMap);
                                payeeMap.put(Constants.GLCODE, coa.getGlcode());
                                payeeMap.put(Constants.DEBITAMOUNT, payeeDetails.getDebitAmount()==null?0:payeeDetails.getDebitAmount());
                                payeeMap.put(Constants.CREDITAMOUNT, payeeDetails.getCreditAmount()==null?0:payeeDetails.getCreditAmount());
                                payeeList.add(payeeMap);
                        }
                }
                billDetails.put("tempList", tempList);
                billDetails.put("payeeList", payeeList);
        }

        public void getMasterDataForBillVoucher() throws EGOVException
        {
                billDetails = new HashMap<String, Object>();
                CChartOfAccounts coa=null;
                Map<String,Object> temp = null;
                Map<String,Object> payeeMap= null;
                List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();
                List<PreApprovedVoucher> payeeList=new ArrayList<PreApprovedVoucher>();
                List<Long> glcodeIdList=new ArrayList<Long>();
                List<Accountdetailtype> detailtypeIdList=new ArrayList<Accountdetailtype>();
                PreApprovedVoucher subledger = null;

                if(voucherHeader.getVoucherDetail().size()>0)
                {
                        List<CGeneralLedger> gllist = getPersistenceService().findAllBy(" from CGeneralLedger where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc", Long.valueOf(voucherHeader.getId()+""));

                        for(CGeneralLedger gl:gllist)
                        {
                                temp = new HashMap<String,Object>();
                                if(gl.getFunctionId()!=null)
                                {
                                        temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(gl.getFunctionId()))).getName());
                                        temp.put("functionid",gl.getFunctionId());
                                }
                                coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where glcode=?",gl.getGlcode());
                                temp.put("glcodeid", coa.getId());
                                glcodeIdList.add(coa.getId());
                                temp.put(Constants.GLCODE, coa.getGlcode());
                                temp.put("accounthead", coa.getName());
                                temp.put(Constants.DEBITAMOUNT, gl.getDebitAmount()==null?0:gl.getDebitAmount());
                                temp.put(Constants.CREDITAMOUNT, gl.getCreditAmount()==null?0:gl.getCreditAmount());
                                temp.put("billdetailid", gl.getId());
                                tempList.add(temp);

                                List<CGeneralLedgerDetail> gldetailList = getPersistenceService().findAllBy("from CGeneralLedgerDetail where generalLedgerId=?", Integer.valueOf(gl.getId()+""));
                                for(CGeneralLedgerDetail gldetail:gldetailList)
                                {
                                        subledger = new PreApprovedVoucher();
                                        subledger.setGlcode(coa);
                                        Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,gldetail.getDetailTypeId());
                                        detailtypeIdList.add(detailtype);
                                        subledger.setDetailType(detailtype);
                                        payeeMap = new HashMap<String,Object>();
                                        payeeMap = getAccountDetails(gldetail.getDetailTypeId(),gldetail.getDetailKeyId(),payeeMap);
                                        subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY)+"");
                                        if((payeeMap.get(Constants.DETAILKEY)+"").contains(Constants.MASTER_DATA_DELETED))
                                        {
                                                addActionError(Constants.VOUCHERERRORMESSAGE);
                                        }
                                        subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE)+"");
                                        subledger.setDetailKeyId(gldetail.getDetailKeyId());
                                        subledger.setAmount(gldetail.getAmount());
                                        subledger.setFunctionDetail(temp.get("function")!=null?temp.get("function").toString():"");
                                        if(gl.getDebitAmount()==null || gl.getDebitAmount()==0)
                                        {
                                                subledger.setDebitAmount(BigDecimal.ZERO);
                                                subledger.setCreditAmount(gldetail.getAmount());
                                        }
                                        else
                                        {
                                                subledger.setDebitAmount(gldetail.getAmount());
                                                subledger.setCreditAmount(BigDecimal.ZERO);
                                        }
                                        payeeList.add(subledger);
                                }
                        }
                        /*// this is for only vouchers, which do not have the bills
                        List<VoucherDetail> voucherDetailList=new ArrayList<VoucherDetail>();
                        voucherDetailList = persistenceService.findAllBy("from VoucherDetail where voucherHeaderId.id=? order by decode(debitAmount,null,0, debitAmount) desc ,decode(creditAmount,null,0, creditAmount) asc",voucherHeader.getId());
                        billDetails.put("voucherDetailList", voucherDetailList);*/
                }
                else
                {
                        for(EgBilldetails billdetails:egBillregister.getEgBilldetailes())
                        {
                                temp = new HashMap<String,Object>();
                                if(billdetails.getFunctionid()!=null)
                                {
                                        temp.put(Constants.FUNCTION, ((CFunction)getPersistenceService().find("from CFunction where id=?", Long.valueOf(billdetails.getFunctionid()+""))).getName());
                                        temp.put("functionid",billdetails.getFunctionid());
                                }
                                coa = (CChartOfAccounts) getPersistenceService().find("from CChartOfAccounts where id=?", Long.valueOf(billdetails.getGlcodeid()+""));
                                temp.put("glcodeid", coa.getId());
                                glcodeIdList.add(coa.getId());
                                temp.put(Constants.GLCODE, coa.getGlcode());
                                temp.put("accounthead", coa.getName());
                                temp.put(Constants.DEBITAMOUNT, billdetails.getDebitamount()==null?0:billdetails.getDebitamount());
                                temp.put(Constants.CREDITAMOUNT, billdetails.getCreditamount()==null?0:billdetails.getCreditamount());
                                temp.put("billdetailid", billdetails.getId());
                                tempList.add(temp);

                                for(EgBillPayeedetails payeeDetails:billdetails.getEgBillPaydetailes())
                                {
                                        subledger = new PreApprovedVoucher();
                                        subledger.setGlcode(coa);
                                        Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,payeeDetails.getAccountDetailTypeId());
                                        detailtypeIdList.add(detailtype);
                                        subledger.setDetailType(detailtype);
                                        payeeMap = new HashMap<String,Object>();
                                        payeeMap = getAccountDetails(payeeDetails.getAccountDetailTypeId(),payeeDetails.getAccountDetailKeyId(),payeeMap);
                                        subledger.setDetailKey(payeeMap.get(Constants.DETAILKEY)+"");
                                        subledger.setDetailCode(payeeMap.get(Constants.DETAILCODE)+"");
                                        subledger.setDetailKeyId(payeeDetails.getAccountDetailKeyId());
                                        if(payeeDetails.getDebitAmount()==null)
                                        {
                                                subledger.setDebitAmount(BigDecimal.ZERO);
                                                subledger.setCreditAmount(payeeDetails.getCreditAmount());
                                        }
                                        else
                                        {
                                                subledger.setDebitAmount(payeeDetails.getDebitAmount());
                                                subledger.setCreditAmount(BigDecimal.ZERO);
                                        }
                                        payeeList.add(subledger);
                                }
                        }
                }
                billDetails.put("tempList", tempList);
                billDetails.put("subLedgerlist", payeeList);
                setupDropDownForSL(glcodeIdList);
                setupDropDownForSLDetailtype(detailtypeIdList);
        }

        public Map<String,Object> getAccountDetails(final Integer detailtypeid,final Integer detailkeyid,Map<String,Object> tempMap) throws EGOVException
        {
                Accountdetailtype detailtype = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,detailtypeid);
                tempMap.put("detailtype", detailtype.getDescription());
                tempMap.put("detailtypeid", detailtype.getId());
                tempMap.put("detailkeyid", detailkeyid);

                EgovCommon common = new EgovCommon();
                common.setPersistenceService(persistenceService);
                EntityType entityType = common.getEntityType(detailtype,detailkeyid);
                if(entityType==null)
                {
                        tempMap.put(Constants.DETAILKEY,detailkeyid+" "+Constants.MASTER_DATA_DELETED);
                        tempMap.put(Constants.DETAILCODE,Constants.MASTER_DATA_DELETED);
                }else
                {
                tempMap.put(Constants.DETAILKEY,entityType.getName());  
                tempMap.put(Constants.DETAILCODE,entityType.getCode());
                }
                return tempMap;
        }

        public void setupDropDownForSL(final List<Long> glcodeIdList)
        {
                if(glcodeIdList.isEmpty())
                        dropdownData.put("glcodeList",Collections.EMPTY_LIST);
                else
                        dropdownData.put("glcodeList", getPersistenceService().findAllByNamedQuery("getSubLedgerCodes", glcodeIdList));
        }

        public void setupDropDownForSLDetailtype(final List<Accountdetailtype> detailtypeIdList)
        {
                dropdownData.put("detailTypeList",detailtypeIdList);
        }

        public EgBillregister getEgBillregister() {
                return egBillregister;
        }
        protected void getHeaderMandateFields()
        {
                List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
                for (AppConfig appConfig : appConfigList)
                {
                        for (AppConfigValues appConfigVal : appConfig.getAppDataValues())
                        {
                                String value = appConfigVal.getValue();
                                String header=value.substring(0, value.indexOf("|"));
                                headerFields.add(header);
                                String mandate = value.substring(value.indexOf("|")+1);
                                if(mandate.equalsIgnoreCase("M")){
                                        mandatoryFields.add(header);
                                }
                        }
                }
        }
        public boolean shouldShowHeaderField(String field)
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside shouldShowHeaderField menthod");
                if(field.equals("vouchernumber"))
                {
                        String  vNumGenMode="Manual";
                        vNumGenMode= new VoucherTypeForULB().readVoucherTypes("Journal");
                        if(!"Auto".equalsIgnoreCase(vNumGenMode)){
                                mandatoryFields.add("vouchernumber");
                                return true     ;
                        }else
                        {
                        return false;
                        }
                
                }
                else
                {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Header field contains = "+ headerFields.contains(field));
                return  headerFields.contains(field);
                }
        }
        public boolean isFieldMandatory(String field){
                return mandatoryFields.contains(field);
        }
        public Position getPosition()throws EGOVRuntimeException
        {
                Position pos;
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("getPosition===="+EgovThreadLocals.getUserId());
                        pos =eisCommonService.getPositionByUserId(EgovThreadLocals.getUserId());
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("position==="+pos.getId());
                return pos;
        }

        public List<Action> getValidActions(String purpose){
                List<Action> validButtons = new ArrayList<Action>();
                List<String> list = (List<String>) scriptService.executeScript("pjv.validbuttons",ScriptService.createContext("eisCommonServiceBean", eisCommonService,"userId",EgovThreadLocals.getUserId().intValue(),"date",new Date(),"purpose",purpose));
                for(Object s:list)
                {
                        if("invalid".equals(s))
                                break;
                        Action action = (Action) getPersistenceService().find(" from org.egov.infstr.workflow.Action where type='CVoucherHeader' and name=?", s.toString());
                        validButtons.add(action);
                }
                return validButtons;
        }

        public String ajaxValidateDetailCode()
        {
                String code = parameters.get("code")[0];
                String index = parameters.get("index")[0];
                Accountdetailtype adt = (Accountdetailtype) getPersistenceService().find(ACCDETAILTYPEQUERY,Integer.valueOf(parameters.get("detailtypeid")[0]));
                if(adt==null)
                {
                        values=index+"~"+ERROR;
                        return "result";
                }
                if(adt.getTablename().equalsIgnoreCase("EG_EMPLOYEE"))
                {
                        PersonalInformation information = (PersonalInformation) getPersistenceService().find(" from PersonalInformation where employeeCode=? and isActive=1", code);
                        if(information==null)
                                values=index+"~"+ERROR;
                        else
                                values=index+"~"+information.getIdPersonalInformation()+"~"+information.getEmployeeFirstName();
                }
                else if(adt.getTablename().equalsIgnoreCase("RELATION"))
                {
                        Relation relation = (Relation) getPersistenceService().find(" from Relation where code=? and isactive=1", code);
                        if(relation==null)
                                values=index+"~"+ERROR;
                        else
                                values=index+"~"+relation.getId()+"~"+relation.getName();
                }
                else if(adt.getTablename().equalsIgnoreCase("ACCOUNTENTITYMASTER"))
                {
                        AccountEntity accountEntity = (AccountEntity) getPersistenceService().find(" from AccountEntity where code=? and isactive=1 ", code);
                        if(accountEntity==null)
                                values=index+"~"+ERROR;
                        else
                                values=index+"~"+accountEntity.getId()+"~"+accountEntity.getCode();
                }
                return "result";
        }
        protected Boolean validateOwner(State state)
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("validating owner for user "+EgovThreadLocals.getUserId());
                List<Position> positionsForUser=null;
                positionsForUser = null;//eisService.getPositionsForUser(Integer.valueOf(EgovThreadLocals.getUserId()), new Date());
                if(positionsForUser.contains(state.getOwnerPosition()))      
                {
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("Valid Owner :return true");
                        return true;
                }else
                {
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("Invalid  Owner :return false");
                        return false;
                }
        }

        public void setVoucherService(final VoucherService voucherService) {
                this.voucherService = voucherService;
        }

        public void setVoucherWorkflowService(final SimpleWorkflowService<CVoucherHeader> voucherWorkflowService) {
                this.voucherWorkflowService = voucherWorkflowService;
        }
        public CVoucherHeader getVoucherHeader() {
                return voucherHeader;
        }
        public void setVoucherHeader(final CVoucherHeader voucherHeader) {
                this.voucherHeader = voucherHeader;
        }
        public void setBillsAccountingService(final BillsAccountingService mngr )
        {
                this.billsAccountingService = mngr;
        }
        public List<PreApprovedVoucher> getSubLedgerlist() {
                return subLedgerlist;
        }
        public void setSubLedgerlist(final List<PreApprovedVoucher> subLedgerlist) {
                this.subLedgerlist = subLedgerlist;
        }
        public List<PreApprovedVoucher> getBillDetailslist() {
                return billDetailslist;
        }
        public void setBillDetailslist(final List<PreApprovedVoucher> billDetailslist) {
                this.billDetailslist = billDetailslist;
        }

        public String getValues() {
                return values;
        }

        public void setValues(final String values) {
                this.values = values;
        }

        public String getMethodName() {
                return methodName;
        }

        public void setMethodName(String methodName) {
                this.methodName = methodName;
        }

        public List<String> getHeaderFields() {
                return headerFields;
        }

        public void setHeaderFields(List<String> headerFields) {
                this.headerFields = headerFields;
        }

        public List<String> getMandatoryFields() {
                return mandatoryFields;
        }

        public void setMandatoryFields(List<String> mandatoryFields) {
                this.mandatoryFields = mandatoryFields;
        }
        public Integer getDepartmentId() {
                return departmentId;
        }

        public void setDepartmentId(Integer departmentId) {
                this.departmentId = departmentId;
        }
        public void setBillsService(BillsService billsManager) {
                this.billsManager = billsManager;
        }
        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getWfitemstate() {
                return wfitemstate;
        }

        public void setWfitemstate(String wfitemstate) {
                this.wfitemstate = wfitemstate;
        }
        public ReceiptVoucher getReceiptVoucher() {
                return receiptVoucher;
        }

        public void setReceiptVoucher(ReceiptVoucher receiptVoucher) {
                this.receiptVoucher = receiptVoucher;
        }
        public ContraJournalVoucher getContraVoucher() {
                return contraVoucher;
        }

        public void setContraVoucher(ContraJournalVoucher contraVoucher) {
                this.contraVoucher = contraVoucher;
        }
        public String getFrom() {
                return from;
        }

        public void setFrom(String from) {
                this.from = from;
        }

        public Map<String, Object> getBillDetails() {
                return billDetails;
        }

        public void setBillDetails(Map<String, Object> billDetails) {
                this.billDetails = billDetails;
        }

        public String getFinConstExpendTypeContingency() {
                return finConstExpendTypeContingency = FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT;
        }

        public VoucherHelper getVoucherHelper() {
                return voucherHelper;
        }

        public void setVoucherHelper(VoucherHelper voucherHelper) {
                this.voucherHelper = voucherHelper;
        }

        public String getVoucherNumber() {
                return voucherNumber;
        }

        public void setVoucherNumber(String voucherNumber) {
                this.voucherNumber = voucherNumber;
        }

        public Boolean getDisplayVoucherNumber() {
                return displayVoucherNumber;
        }

        public void setDisplayVoucherNumber(Boolean displayVoucherNumber) {
                this.displayVoucherNumber = displayVoucherNumber;
        }


        public void setEisService(EisUtilService eisService) {
                this.eisService = eisService;
        }


        public JournalVoucherModifyAction getJournalvouchermodify() {
                return journalvouchermodify;
        }

        public void setJournalvouchermodify(
                        JournalVoucherModifyAction journalvouchermodify) {
                this.journalvouchermodify = journalvouchermodify;
        }
        public void setContraWorkflowService(SimpleWorkflowService<ContraJournalVoucher> contraWorkflowService) {
                this.contraWorkflowService = contraWorkflowService;
        }

        public void setReceiptWorkflowService(SimpleWorkflowService<ReceiptVoucher> receiptWorkflowService) {
                this.receiptWorkflowService = receiptWorkflowService;
        }

        public void getGenericHibDao(final GenericHibernateDaoFactory genericDao) {
                this.genericDao = genericDao;
        }

        public PreApprovedVoucherAction(){
                try{
                addRelatedEntity(VoucherConstant.GLCODE, CChartOfAccounts.class);
                addRelatedEntity("detailType", Accountdetailtype.class);
                }
                catch(Exception e)
                {
                        LOGGER.error("Exception in PreApprovedVoucher",e);
                        throw new EGOVRuntimeException(e.getMessage());
                }

        }

        public boolean isShowVoucherDate() {
                return showVoucherDate;
        }

        public void setShowVoucherDate(boolean showVoucherDate) {
                this.showVoucherDate = showVoucherDate;
        }

        public void setGenericDao(GenericHibernateDaoFactory genericDao) {
                this.genericDao = genericDao;
        }
        public FinancialYearHibernateDAO getFinancialYearDAO() {
                return financialYearDAO;
        }

        public void setFinancialYearDAO(FinancialYearHibernateDAO financialYearDAO) {
                this.financialYearDAO = financialYearDAO;
        }
        
}