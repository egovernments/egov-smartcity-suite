package org.egov.web.actions.brs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.EgwStatus;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.services.receipt.ReceiptService;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;

import com.exilant.eGov.src.domain.BankBranch;
import com.exilant.exility.common.TaskFailedException;

@ParentPackage("egov")
@Results({
    @Result(name = DishonoredChequeAction.SEARCH, location = "dishonoredCheque-search.jsp"),
    @Result(name = DishonoredChequeAction.SUCCESS, location = "dishonoredCheque-success.jsp")
   })
public class DishonoredChequeAction extends SearchFormAction {
    
    public static final String SEARCH = "search";
    private static final Logger LOGGER = Logger.getLogger(DishonoredChequeAction.class);
    private String bankBranchId;
    private Long accountCodes;
    private String instrumentMode;
    private String chequeNo;
    private Date chqDDDate;
    @Autowired
    private ReceiptService receiptService;
    private String installmentIds;
    
    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }
   
    public void prepare()
    {
        super.prepare();
        addDropdownData("bankBranchList", persistenceService.findAllBy("select bb from Bankbranch bb,Bank b "
                + "where b.isactive=1 and bb.isactive=1 and b.id=bb.bank.id order by bb.bank.name"));   
        final AjaxDishonoredAction ajaxDishonoredAction = new AjaxDishonoredAction();
        ajaxDishonoredAction.setPersistenceService(getPersistenceService());
        populateAccountCodes(ajaxDishonoredAction);
    }
    
    public List getBankBranch(){
        BankBranch bb=new BankBranch();
        try{
            return (List) bb.getBankBranch();
        }catch(TaskFailedException tf){
            LOGGER.error(tf. getMessage());
        }
        return null;
    }
    
    private void populateAccountCodes(final AjaxDishonoredAction ajaxDishonoredAction) {
        if (bankBranchId != null && bankBranchId != "-1" && bankBranchId!="") {
            ajaxDishonoredAction.setBankBranchId(bankBranchId);
            ajaxDishonoredAction.populateAccountCodes();
            addDropdownData("accountCodeList", ajaxDishonoredAction.getBankAccountList());
        } else
            addDropdownData("accountCodeList", Collections.emptyList());
    }
    
    @Actions({
        @Action(value="/brs/dishonoredCheque-search")
        })
    public String show(){
        return SEARCH;
    }
    
    @Action(value = "/brs/dishonoredCheque-list")
    public String list() throws Exception {
        setPageSize(30);
        search();
        return SEARCH;
    }
    
    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
       
        Long bankId = null;
        if (bankBranchId != "-1" && bankBranchId != null && bankBranchId!=""){
            String id[] = bankBranchId.split("-");
            bankId = Long.parseLong(id[0]);
        }
        String mode=null;
        if(instrumentMode.equals("1")){
            mode="dd";
        }
        else if(instrumentMode.equals("2")){
            mode="cheque";
        }
        HashMap<String, Object> searchQuery=receiptService.getReceiptHeaderforDishonor(mode,accountCodes,bankId,chequeNo,chqDDDate.toString());
        return new SearchQueryHQL(searchQuery.get("query").toString(),
                "select count(*) " + searchQuery.get("query"), (List<Object>) searchQuery.get("paramList"));
      
    }
    
    @Action(value = "/brs/dishonoredCheque-dishonorCheque")
    public String dishonorCheque() throws Exception {
        final String installmentIdsStr[] = installmentIds.split(",");
        EgwStatus instrumentCancelledStatus = (EgwStatus) persistenceService.find(
                "from EgwStatus where upper(moduletype)=upper('Instrument') and upper(description)=upper(?)",
                FinancialConstants.INSTRUMENT_CANCELLED_STATUS);
        if(instrumentCancelledStatus!=null){
            for (final String installmentIdStr : installmentIdsStr) {
                InstrumentHeader ih= new InstrumentHeader();
                 ih= (InstrumentHeader)getPersistenceService().find("from InstrumentHeader where id=?",Long.valueOf(installmentIdStr));
                if(ih!=null){
                    ih.setStatusId(instrumentCancelledStatus);
                }
            }
        }
        return SUCCESS;
    }

    public String getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(String bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    public Long getAccountCodes() {
        return accountCodes;
    }

    public void setAccountCodes(Long accountCodes) {
        this.accountCodes = accountCodes;
    }

    public String getInstrumentMode() {
        return instrumentMode;
    }

    public void setInstrumentMode(String instrumentMode) {
        this.instrumentMode = instrumentMode;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public Date getChqDDDate() {
        return chqDDDate;
    }

    public void setChqDDDate(Date chqDDDate) {
        this.chqDDDate = chqDDDate;
    }

    public String getInstallmentIds() {
        return installmentIds;
    }

    public void setInstallmentIds(String installmentIds) {
        this.installmentIds = installmentIds;
    }

    

}
