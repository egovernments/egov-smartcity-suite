package org.egov.demand.mocks;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgDemandReasonMasterDao;
import org.egov.demand.dao.EgReasonCategoryDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.BillReceipt;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.infstr.commons.Module;
import org.egov.infstr.utils.DateUtils;

public class MocksUtils {
    

    public DemandGenericDao mockDemandGenDAO() {
        DemandGenericDao dmdGenDao = mock(DemandGenericHibDao.class);
        List<EgDemandReasonMaster> reasonMasters = new ArrayList<EgDemandReasonMaster>();
        reasonMasters.add(prepareReasonMaster());
        when(dmdGenDao.getDemandReasonMasterByModule(prepareModule())).thenReturn(reasonMasters);
        when(dmdGenDao.getDemandReasonMasterByCode("GENTAX-RESD", prepareModule())).thenReturn(
                prepareReasonMaster());
        when(dmdGenDao.getReasonCategoryByCode(anyString())).thenReturn(
                prepareEgCategoryReason());

        List demandMasterid = new ArrayList();
        demandMasterid.add(Long.valueOf(1));

        when(dmdGenDao.getEgDemandReasonMasterIds(prepareEgDemand())).thenReturn(demandMasterid);

        List<Object> dcbList = new ArrayList<Object>();

        Object a[] = new Object[6];

        a[0] = Integer.valueOf(1);
        a[1] = BigDecimal.valueOf(1000);
        a[2] = BigDecimal.valueOf(800);
        a[3] = Long.valueOf(3);
        a[4] = Long.valueOf(3);

        dcbList.add(a);
        List<EgDemand> demands=new ArrayList<EgDemand>();
        demands.add(prepareEgDemand());
        when(dmdGenDao.getDCB(prepareEgDemand(), prepareModule())).thenReturn(dcbList);
        List<BillReceipt> billRct = new ArrayList<BillReceipt>();
        billRct.add(prepareBillReceipt());
        when(dmdGenDao.getBillReceipts(demands)).thenReturn(billRct);

        return dmdGenDao;
    }

    public InstallmentDao mockInstallmentDAO() {
        InstallmentDao installDao = mock(InstallmentHibDao.class);
        when(installDao.findById(Integer.valueOf(1), true)).thenReturn(prepareInstallment());
        when(installDao.getInsatllmentByModuleForGivenDate(prepareModule(),prepareDate()))
                .thenReturn(prepareInstallment());

        return installDao;
    }

    public EgDemandReasonMaster prepareReasonMaster() {
        EgDemandReasonMaster reasonMaster = new EgDemandReasonMaster();
        reasonMaster.setCode("GENTAX-RESD");
        reasonMaster.setId(Long.valueOf(1));
        reasonMaster.setEgModule(prepareModule());
        reasonMaster.setReasonMaster(" GENERAL TAX - RESEDENTIAL ");
        reasonMaster.setEgReasonCategory(prepareEgCategoryReason());
        return reasonMaster;
    }

    public EgReasonCategory prepareEgCategoryReason() {
        EgReasonCategory reasonCategory = new EgReasonCategory();
        reasonCategory.setCode("TAX");
        reasonCategory.setIdType(Long.valueOf(1));
        reasonCategory.setName("TAX");
        return reasonCategory;
    }

    public EgDemand prepareEgDemand() {
        EgDemand dmd = new EgDemand();
        dmd.setId(Long.valueOf(1));
        dmd.setBaseDemand(BigDecimal.TEN);
        dmd.setAmtCollected(BigDecimal.ONE);
        dmd.setIsHistory("N");
        dmd.setEgInstallmentMaster(prepareInstallment());
        Set<EgDemandDetails> dmdDetSet = new HashSet<EgDemandDetails>();
        dmdDetSet.add(prepareEgDemandDetails());
        dmd.setEgDemandDetails(dmdDetSet);
        return dmd;
    }

    public EgDemandDetails prepareEgDemandDetails() {
        EgDemandDetails dmdDet = new EgDemandDetails();
        dmdDet.setId(Long.valueOf(1));
        dmdDet.setAmount(BigDecimal.TEN);
        dmdDet.setAmtCollected(BigDecimal.ONE);
        dmdDet.setEgDemandReason(prepareEgDemandReason());
        return dmdDet;
    }

    public EgDemandReason prepareEgDemandReason() {
        EgDemandReason egDmdReason = new EgDemandReason();
        egDmdReason.setId(Long.valueOf(1));
        egDmdReason.setEgDemandReasonMaster(prepareReasonMaster());
        egDmdReason.setEgInstallmentMaster(prepareInstallment());

        return egDmdReason;
    }

    public Installment prepareInstallment() {
        Installment installment = new Installment();
        installment.setId(Integer.valueOf(1));
        installment.setFromDate(DateUtils.getFinancialYear().getStartOnDate());
        installment.setToDate(DateUtils.getFinancialYear().getEndOnOnDate());
        installment.setModule(prepareModule());

        return installment;
    }

    public EgBill prepareEgBill() {
        EgBill egBill = new EgBill();
        egBill.setId(Long.valueOf(1));
        egBill.setEgDemand(prepareEgDemand());
        egBill.setTotalAmount(BigDecimal.TEN);
        egBill.setTotalCollectedAmount(BigDecimal.ONE);
        Set<EgBillDetails> billDetSet=new HashSet<EgBillDetails>();
        billDetSet.add(prepareEgBillDetails());
        egBill.setEgBillDetails(billDetSet);
        return egBill;
    }
    
    public EgBillDetails prepareEgBillDetails()
    {
    	EgBillDetails billDet=new EgBillDetails();
    	//billDet.setDemandReason(prepareEgDemandReason());
    	billDet.setCrAmount(BigDecimal.TEN);
    	billDet.setCollectedAmount(BigDecimal.ONE);
    	billDet.setDrAmount(BigDecimal.TEN);
    	billDet.setEgInstallmentMaster(prepareInstallment());
    	return billDet;
    }

    public BillReceipt prepareBillReceipt() {
        BillReceipt billRct = new BillReceipt();
        billRct.setBillId(prepareEgBill());
        billRct.setReceiptAmt(BigDecimal.ONE);
        billRct.setReceiptDate(prepareDate());
        billRct.setReceiptNumber("TEST_RECEIPT");
        billRct.setIsCancelled(Boolean.FALSE);
        return billRct;

    }

    public EgDemandReasonMasterDao mockEgDemandReasonMasterDao() {
        EgDemandReasonMasterDao reasonMasterDAO = mock(EgDemandReasonMasterDao.class);
        when(reasonMasterDAO.findById(anyInt(), anyBoolean())).thenReturn(prepareReasonMaster());
        return reasonMasterDAO;

    }

    public Module prepareModule() {
        Module mockModule = new Module();
        mockModule.setId(Integer.valueOf(113));
        return mockModule;
    }
    
    public Billable prepareBillable() {
      Billable billable =mock(Billable.class);
      List<EgDemand> demands=new ArrayList<EgDemand>();
      demands.add(prepareEgDemand());
      
      return billable;
    }


    public EgReasonCategoryDao mockEgReasonCategoryDao() {
        EgReasonCategoryDao reasoncategoryDAO = mock(EgReasonCategoryDao.class);
        when(reasoncategoryDAO.findById(anyInt(), anyBoolean())).thenReturn(
                prepareEgCategoryReason());

        return reasoncategoryDAO;
    }
    
    public Date prepareDate()
    {
        Date date =new Date();
        
        date.setMonth(00);
        date.setYear(date.getYear());
        
        return date;
        
    }

}
