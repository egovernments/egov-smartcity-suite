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
/**
 * 
 */
package org.egov.web.actions.contra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.BankDAO;
import org.egov.commons.dao.BankaccountDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.CommonsService;
import org.egov.egf.commons.EgovCommon;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.contra.ContraBean;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.model.voucher.PayInBean;
import org.egov.model.voucher.VoucherTypeBean;
import org.egov.services.contra.ContraService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.voucher.BaseVoucherAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;

/**
 * @author msahoo
 * This class is used for the Payin Slip(Cheque Deposit).
 */
@Transactional(readOnly=true)
public class PayInSlipAction extends BaseVoucherAction {
        private static final Logger     LOGGER  = Logger.getLogger(PayInSlipAction.class);
        private static final long serialVersionUID = 1L;
        private String selectedInstr;
        private String totalAmount;
        private List<PayInBean> iHeaderList;
        @Autowired
        private EgwStatusHibernateDAO egwStatusDAO;
        @Autowired
        private BankaccountDAO bankAccountDAO;
        private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        private VoucherTypeBean voucherTypeBean;
        private  ContraBean contraBean; 
        private ContraService contraService;
        private EgovCommon egovCommon ;
        private VoucherService voucherService;
        

        public PayInSlipAction(){
        }
        @SkipValidation
        @Action(value="/contra/payInSlip-newform")
        public String newform()
        {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Return the New Pay in slip Form");
                voucherHeader.reset();
                contraBean.reset();
                voucherHeader.setType("Contra");
                loadDefalutDates();
                Map<String, Object> boundaryMap = egovCommon.getCashChequeInfoForBoundary();
                contraBean.setChequeInHand(boundaryMap.get("chequeInHand").toString());
                return NEW;
        }
        @Override
        public void prepare() {
                
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PayInSlipAction |prepare | Start");
                super.prepare();
                addDropdownData("bankList", Collections.EMPTY_LIST);
                addDropdownData("accNumList", Collections.EMPTY_LIST);
        }
        /**
         * @description - This method displays the payin slip voucher when the user go through the source path.
         * @return - returns the payinslip-view jsp file 
         */
        public String view(){
                
                preparePayinVoucher();
                 return "view";
        }
        /**
         * @description -displays the payinslip voucher details where the user can edit the same payin slip.
         * @return - returns the payinslip edit,where the user can edit the payin slip voucher.
         */
        public String beforeEdit(){
                
                preparePayinVoucher();
                
                return EDIT;
        }
        /**
         *  @description - All the instruments that are of type “Cheque” and are of type collections and that are not yet DEPOSITED
         *                 should come for cheque deposit.User should be able to select one or more cheques using a check box and SAVE. On SAVE,
         *                 a payinslip contra voucher gets generated and the instrument status gets changed to “DEPOSITED”.
         * @return - return to  the payinslip new page after the payinslip voucher is created.
         */
        public String create() {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PayinSlipAction | Create | start");
                createOrEdit("create");
                 addActionMessage("Payin slip number "+voucherHeader.getVoucherNumber()+"  created sucessfully ");
                return NEW;
        }
        /**
         * @description - modify the pay-in slip voucher that has already been created.
         * @return- return to  the payinslip edit  page after the payinslip voucher is modified.
         */
        
        public String edit(){
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PayinSlipAction | Edit | start");
                voucherHeader.setType("Contra");
                createOrEdit("edit");
                 addActionMessage("Payin slip number "+voucherHeader.getVoucherNumber()+"  modified sucessfully ");
                return EDIT;
        }
        /**
         * 
         * @return - 
         */
        public String beforeReverse(){
                preparePayinVoucher();
                return "reverse";
        }
        /**
         * @description - Reverse a contra payinslip after it has been confirmed.If the payin-slip has been confirmed by mistake, 
         *                then then the user can reverse it
         * @return
         */
        @ValidationErrorPage (value="reverse")
        public String reverse(){
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PayinSlipAction | Reverse | start");
                loadSchemeSubscheme();
                loadBankBranchForFund();
                loadBankAccountNumber(contraBean);
                final CreateVoucher cv = new CreateVoucher();
                CVoucherHeader reversalVoucher = null;
                final HashMap<String, Object> reversalVoucherMap = new HashMap<String, Object>();
                reversalVoucherMap.put("Original voucher header id", voucherHeader.getId());
                reversalVoucherMap.put("Reversal voucher type", "Contra");
                reversalVoucherMap.put("Reversal voucher name", "Pay In Slip");
                try {
                        reversalVoucherMap.put("Reversal voucher date", new SimpleDateFormat("dd/MM/yyyy", Constants.LOCALE).parse(getReversalVoucherDate()));
                } catch (final ParseException e1) {
                        throw new ValidationException(Arrays.asList(new ValidationError("reversalVocuherDate", "reversalVocuherDate.notinproperformat")));
                }
                reversalVoucherMap.put("Reversal voucher number", getReversalVoucherNumber());
                final List<HashMap<String, Object>> reversalList = new ArrayList<HashMap<String, Object>>();
                reversalList.add(reversalVoucherMap);
                try {
                        reversalVoucher = cv.reverseVoucher(reversalList);
                        contraService.editInstruments(voucherHeader.getId());
                } catch (ValidationException e) {
                        preparePayinVoucher();
                         List<ValidationError> errors=new ArrayList<ValidationError>();
                         errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
                         throw new ValidationException(errors);
                }
                catch (final Exception e) {
                        preparePayinVoucher();
                        throw new ValidationException(Arrays.asList(new ValidationError("Failed while Reversing", e.getMessage())));
                } 
                voucherHeader = reversalVoucher;
                 contraBean.setResult("success");
                addActionMessage("Contra Payinslip Reversal Successful : " + reversalVoucher.getVoucherNumber());
                return "reverse";
        }
        public String search() {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("PayInSlipAction | search | Start");
                loadSchemeSubscheme();
                loadBankBranchForFund();
                loadBankAccountNumber(contraBean);
                StringBuffer query = new StringBuffer(500);
                query.append("select distinct iHeader.id ,iHeader.instrumentNumber,iHeader.instrumentDate,iHeader.instrumentAmount,vh.voucherNumber,vh.voucherDate")
                                .append("  from InstrumentHeader iHeader,InstrumentVoucher iVoucher,InstrumentType iType , CVoucherHeader vh ,Vouchermis mis ") 
                                .append(" where  iHeader.id=iVoucher.instrumentHeaderId and iHeader.instrumentType =iType.id  and vh.id = iVoucher.voucherHeaderId ")
                                .append(" and vh.id=mis.voucherheaderid and iHeader.isPayCheque=0 and vh.status =0 and vh.fundId.id= ?" )
                                .append(" and iType.type='").append(FinancialConstants.INSTRUMENT_TYPE_CHEQUE).append("'").
                                append(" and iHeader.statusId.id = ").append(egwStatusDAO.getStatusByModuleAndCode("Instrument",FinancialConstants.INSTRUMENT_CREATED_STATUS).getId());
                query.append(VoucherHelper.getMisQuery(voucherHeader));
                query.append(VoucherHelper.getVoucherNumDateQuery(voucherTypeBean.getVoucherNumFrom(), voucherTypeBean.getVoucherNumTo(), 
                                voucherTypeBean.getVoucherDateFrom(), voucherTypeBean.getVoucherDateTo()));
                        if(LOGGER.isDebugEnabled())     LOGGER.debug("********Query *******"+query);
                final List<Object[]> iSearchList = getPersistenceService().findAllBy(query.toString(),voucherHeader.getFundId().getId());
                if(LOGGER.isDebugEnabled())     LOGGER.debug(" Instrument List Size = "+iSearchList.size());
                
                if(iSearchList.size() != 0){
                        iHeaderList = new ArrayList<PayInBean>();
                        PayInBean payInBean;
                        int index = 0;
                        final SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                        for(Object[] element : iSearchList){
                                payInBean = new PayInBean();
                                payInBean.setInstId(Long.valueOf(element[0].toString()));
                                payInBean.setInstrumentNumber(element[1].toString());
                                try {
                                        payInBean.setInstrumentDate(formatter.format(formatter1.parse(element[2].toString())));
                                        payInBean.setVoucherDate(formatter.format(formatter1.parse(element[5].toString())));
                                } catch (ParseException e) {
                                        LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
                                }
                                payInBean.setInstrumentAmount(element[3].toString());
                                payInBean.setVoucherNumber(element[4].toString());
                        
                                payInBean.setSerialNo(++index);
                                iHeaderList.add(payInBean);
                        }
                }else {
                        addActionMessage("No cheques available");
                }
                return NEW;
        }
        
        private void createOrEdit(String mode){
                loadSchemeSubscheme();
                loadBankBranchForFund();
                loadBankAccountNumber(contraBean);
                
                if(null == selectedInstr &&  StringUtils.isEmpty(selectedInstr)){
                        throw new ValidationException(Arrays.asList(new ValidationError("payin.atleastselect.onecheque","payin.atleastselect.onecheque")));
                }
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Serial numbers of instruments to be deposited "+selectedInstr);
                
                try{
                        if(mode.equalsIgnoreCase("create")){
                                 voucherHeader = callCreateVoucher();
                        }else{
                                // edit Payin slip voucher,update the voucher ,then update the instrument ,
                                voucherHeader = voucherService.updateVoucherHeader(voucherHeader,voucherTypeBean); 
                                contraService.editInstruments(voucherHeader.getId());
                                voucherService.deleteGLDetailByVHId(voucherHeader.getId());
                                createLedgerAndPost();
                        }
                        
                         String[] iHeaderId = StringUtils.splitByWholeSeparator(selectedInstr, ",");
                         // updating instrument.
                         for (String instHeaderId : iHeaderId) {
                                 InstrumentHeader instrumentHeader = (InstrumentHeader) persistenceService.find("from InstrumentHeader where id=?",
                                                                                                                                                                                                Long.valueOf(instHeaderId));
                                Map valuesMap = contraService.prepareForUpdateInstrumentDeposit(bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())).getChartofaccounts().getGlcode());
                                 contraService.updateCheque_DD_Card_Deposit(voucherHeader.getId(), 
                                 bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId())).getChartofaccounts().getGlcode(), instrumentHeader,valuesMap);
                                 
                        }       
                        
                         contraBean.setResult("success");
                }
                catch (ValidationException e) {
                         List<ValidationError> errors=new ArrayList<ValidationError>();
                         errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
                         throw new ValidationException(errors);
                }
                catch (Exception e) {
                         List<ValidationError> errors=new ArrayList<ValidationError>();
                         errors.add(new ValidationError("exp",e.getMessage()));
                         throw new ValidationException(errors);
                }
        }
        
        private void preparePayinVoucher(){
                 Map<String, Object> voucherMap = contraService.getpayInSlipVoucher(voucherHeader.getId(),contraBean, iHeaderList);
                 voucherHeader = (CVoucherHeader)voucherMap.get(Constants.VOUCHERHEADER);
                 contraBean = (ContraBean)voucherMap.get("contraBean");
                 Map<String, Object> boundaryMap = egovCommon.getCashChequeInfoForBoundary();
                 contraBean.setChequeInHand(boundaryMap.get("chequeInHand").toString());
                 iHeaderList = (List<PayInBean> )voucherMap.get("iHeaderList");
                 totalAmount = voucherMap.get("totalInstrAmt").toString();
                 loadSchemeSubscheme();
                 loadBankBranchForFund();
                 loadBankAccountNumber(contraBean);
        }
        

        private CVoucherHeader callCreateVoucher() {
                try {
                         Bankaccount bankaccount = bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId()));
                        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails();
                        // update ContraBTB source path
                        headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/contra/payInSlip!view.action?voucherHeader.id=");
                        
                        HashMap<String, Object> detailMap = null;
                        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
                        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
                        
                        detailMap = new HashMap<String, Object>();
                        detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
                        detailMap.put(VoucherConstant.DEBITAMOUNT, totalAmount);
                        detailMap.put(VoucherConstant.GLCODE, bankaccount.getChartofaccounts().getGlcode());
                        detailMap.put(VoucherConstant.NARRATION, voucherHeader.getDescription());
                        
                        accountdetails.add(detailMap);
                        
                        detailMap = new HashMap<String, Object>();
                        detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
                        detailMap.put(VoucherConstant.CREDITAMOUNT,totalAmount);
                        detailMap.put(VoucherConstant.GLCODE, contraBean.getChequeInHand());
                        detailMap.put(VoucherConstant.NARRATION, voucherHeader.getDescription());
                        accountdetails.add(detailMap);
                        final CreateVoucher cv = new CreateVoucher();
                        voucherHeader = cv.createVoucher(headerDetails, accountdetails, subledgerDetails);
                        
                }  catch (final ValidationException e) {
                         List<ValidationError> errors=new ArrayList<ValidationError>();
                         errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
                         throw new ValidationException(errors);
                } catch (final Exception e) {
                        throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
                }
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
                return voucherHeader;
                
        }
        private void createLedgerAndPost(){
                
                 Bankaccount bankaccount = bankAccountDAO.getBankaccountById(Integer.valueOf(contraBean.getAccountNumberId()));
                        HashMap<String, Object> detailMap = null;
                        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
                        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();
                        
                        detailMap = new HashMap<String, Object>();
                        detailMap.put(VoucherConstant.CREDITAMOUNT, "0");
                        detailMap.put(VoucherConstant.DEBITAMOUNT, totalAmount);
                        detailMap.put(VoucherConstant.GLCODE, bankaccount.getChartofaccounts().getGlcode());
                        detailMap.put(VoucherConstant.NARRATION, voucherHeader.getDescription());
                        
                        accountdetails.add(detailMap);
                        
                        detailMap = new HashMap<String, Object>();
                        detailMap.put(VoucherConstant.DEBITAMOUNT, "0");
                        detailMap.put(VoucherConstant.CREDITAMOUNT,totalAmount);
                        detailMap.put(VoucherConstant.GLCODE, contraBean.getChequeInHand());
                        detailMap.put(VoucherConstant.NARRATION, voucherHeader.getDescription());
                        accountdetails.add(detailMap);
                        final CreateVoucher cv = new CreateVoucher();
                        final List<Transaxtion> transactions = cv.createTransaction(null,accountdetails, subledgerDetails, voucherHeader);
                HibernateUtil.getCurrentSession().flush();
                        try{
                                final ChartOfAccounts engine = ChartOfAccounts.getInstance();
                                Transaxtion txnList[] = new Transaxtion[transactions.size()];
                                txnList = transactions.toArray(txnList);
                                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                                if (!engine.postTransaxtions(txnList, formatter.format(voucherHeader.getVoucherDate()))) {
                                        throw new ValidationException(Arrays.asList(new ValidationError("Exception while saving Data", "Transaction failed")));
                                }
                        }catch (ValidationException e) {
                                 List<ValidationError> errors=new ArrayList<ValidationError>();
                                 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
                                 throw new ValidationException(errors);
                        }
                        catch (Exception e) {
                                LOGGER.error(e.getMessage());
                                throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
                        }
                        
                
        }
        public List<PayInBean> getIHeaderList() {
                return iHeaderList;
        }
        public void setIHeaderList(List<PayInBean> headerList) {
                iHeaderList = headerList;
        }
        
        public String getSelectedInstr() {
                return selectedInstr;
        }
        public void setSelectedInstr(String selectedInstr) {
                this.selectedInstr = selectedInstr;
        }
        public String getTotalAmount() {
                return totalAmount;
        }
        public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
        }
        public ContraService getContraService() {
                return contraService;
        }
        public void setContraService(ContraService contraService) {
                this.contraService = contraService;
        }
        public ContraBean getContraBean() {
                return contraBean;
        }
        public void setContraBean(ContraBean contraBean) {
                this.contraBean = contraBean;
        }
        public VoucherTypeBean getVoucherTypeBean() {
                return voucherTypeBean;
        }
        public void setVoucherTypeBean(VoucherTypeBean voucherTypeBean) {
                this.voucherTypeBean = voucherTypeBean;
        }
        public EgovCommon getEgovCommon() {
                return egovCommon;
        }
        public void setEgovCommon(EgovCommon egovCommon) {
                this.egovCommon = egovCommon;
        }
        public VoucherService getVoucherService() {
                return voucherService;
        }
        public void setVoucherService(VoucherService voucherService) {
                this.voucherService = voucherService;
        }
}