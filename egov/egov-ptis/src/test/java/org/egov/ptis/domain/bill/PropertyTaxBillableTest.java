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
package org.egov.ptis.domain.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.builder.entities.ModuleBuilder;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.InstallmentBuilder;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.ptis.builder.entity.property.BasicPropertyBuilder;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.service.PenaltyCalculationService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.service.property.RebateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PropertyTaxBillableTest {
 
    @Mock
    private PenaltyCalculationService penaltyCalculationService;
    @Mock
    private PtDemandDao ptDemandDAO;
    @Mock
    private InstallmentDao installmentDao;
    @Mock
    private ModuleService moduleDao;
    @Mock
    private PropertyTaxUtil propertyTaxUtil; 
    @Mock
    private RebateService rebateService;
    private PropertyTaxBillable billable= new PropertyTaxBillable(); 
    private Boundary locality;
    private Module module;
    private  Installment currentInstallment;
    private Property property;
    private BasicProperty basicProperty;
    private Map<Installment, PenaltyAndRebate> installmentPenaltyAndRebate = new TreeMap<Installment, PenaltyAndRebate>();
    private Map<String, Map<Installment, BigDecimal>> installmentDemandAndCollection = new TreeMap<String, Map<Installment, BigDecimal>>();
    private Ptdemand egDemand = new Ptdemand();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar fromDate;
    private Calendar toDate;
    
    @Before
    public void before() {
            initMocks(this); 
            MockitoAnnotations.initMocks(this);
            initMasters();
    }
    
    private void initMasters() {
        locality = new BoundaryBuilder().withDefaults().build();
        module= new ModuleBuilder().withName(PTMODULENAME).build();
        currentInstallment = new InstallmentBuilder().withCurrentHalfPeriod(module).build();
    }

    private void initServicesForPTBillable() {
        billable.setLevyPenalty(true);
        billable.setBasicProperty(basicProperty);
        billable.setPtDemandDAO(ptDemandDAO);
        billable.setPenaltyCalculationService(penaltyCalculationService);
        billable.setModuleDao(moduleDao);
        billable.setPropertyTaxUtil(propertyTaxUtil);
        billable.setRebateService(rebateService);
    }
    
    private void initBasicProperty(String assessmentEffectiveDate) {
        basicProperty = new BasicPropertyBuilder().withDefaults().build();
        property = basicProperty.getProperty();
        try {
            Date date = dateFormat.parse(assessmentEffectiveDate); 
            basicProperty.setAssessmentdate(date);
        } catch (ParseException e) {


        }
    }
    
    private void initDataForCurInstallment(){
        Map<Installment, BigDecimal> temp = new TreeMap<Installment, BigDecimal>();
        temp.put(currentInstallment, new BigDecimal(2000));
        installmentDemandAndCollection.put("DEMAND", temp);
        temp = new TreeMap<Installment, BigDecimal>();
        temp.put(currentInstallment, new BigDecimal(0));
        installmentDemandAndCollection.put("COLLECTION", temp);
    }
    
    public void setInstallmentDates(int today_minus_year){
        fromDate = Calendar.getInstance();
        toDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -today_minus_year);
        
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        
        if (month >= 4 && month <= 9) {
            fromDate.set(Calendar.DATE, 1);
            fromDate.set(Calendar.MONTH, 3);
            fromDate.set(Calendar.YEAR, year);

            toDate.set(Calendar.DATE, 30);
            toDate.set(Calendar.MONTH, 8);
            toDate.set(Calendar.YEAR, year);

        } else {
            fromDate.set(Calendar.DATE, 1);
            fromDate.set(Calendar.MONTH, 9);
            fromDate.set(Calendar.YEAR, year-1);

            toDate.set(Calendar.DATE, 31);
            toDate.set(Calendar.MONTH, 2);
            toDate.set(Calendar.YEAR, year);
        }
    }
    
    
    /**
     * Use Case 1 : Collection happened with in 3 months from the assessmentdate
     * Penalty Not Applicable
     */
    @Test
    public void calculatePenalty_noPenalty() {
        initBasicProperty(dateFormat.format(new Date())); 
        initServicesForPTBillable();  
        initDataForCurInstallment();    
        /*installmentPenaltyAndRebate = billable.getCalculatedPenalty();
        assertTrue(installmentPenaltyAndRebate.isEmpty());*/
    }
    
    /**
     * Use Case 2 :  Collection happened with in 5 months from the assessmentdate
     * Penalty Applicable from 4th month onwards. Hence 2months of penalty calculated.
     */
    @Test
    public void calculatePenalty_for2Months() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -4);
        Date assessmentDate = cal.getTime();
        initBasicProperty(dateFormat.format(assessmentDate));
        initServicesForPTBillable();  
        initDataForCurInstallment();    
        egDemand.setEgInstallmentMaster(currentInstallment);
        when(ptDemandDAO.getNonHistoryCurrDmdForProperty(Matchers.any())).thenReturn(egDemand);
        when(penaltyCalculationService.getInstallmentDemandAndCollection(Matchers.any(),Matchers.any())).thenReturn(installmentDemandAndCollection);
        when(installmentDao.getInsatllmentByModuleForGivenDate(Matchers.any(),Matchers.any())).thenReturn(currentInstallment);
        when(rebateService.isEarlyPayRebateActive(new Date())).thenReturn(false);
        
        installmentPenaltyAndRebate = billable.getCalculatedPenalty();
        if (installmentPenaltyAndRebate != null && !installmentPenaltyAndRebate.isEmpty()) {
            assertEquals(installmentPenaltyAndRebate.get(currentInstallment).getPenalty(),new BigDecimal(80));
        }
    } 
    
    
    /**
     * Use Case 3 :  Collection happening after an year from assessmentDate
     * Penalty calculated installmentwise
     * 
     */
    @Test
    public void calculatePenalty_acrossInstallments() {
        setInstallmentDates(1);
        initBasicProperty(dateFormat.format(fromDate.getTime()));
        initServicesForPTBillable();  
        Map<Installment, BigDecimal> demandMap = new TreeMap<Installment, BigDecimal>(); 
        Map<Installment, BigDecimal> collectionMap = new TreeMap<Installment, BigDecimal>(); 
      
        Installment arrearInstallment = new InstallmentBuilder().withModule(module).withFromDate(fromDate.getTime()).
            withToDate(toDate.getTime()).build(); 
        when(installmentDao.getInsatllmentByModuleForGivenDate(Matchers.any(),Matchers.any())).thenReturn(arrearInstallment);
        demandMap.put(arrearInstallment,  new BigDecimal(3074));
        collectionMap.put(arrearInstallment,  new BigDecimal(0));
        
        
        fromDate=Calendar.getInstance();
        fromDate.setTime(toDate.getTime());  
        fromDate.add(Calendar.MONTH, 1);  
        fromDate.set(Calendar.DAY_OF_MONTH, 1);  
        arrearInstallment = new InstallmentBuilder().withModule(module).withFromDate(fromDate.getTime()).build();   
        demandMap.put(arrearInstallment,  new BigDecimal(3074));
        collectionMap.put(arrearInstallment,  new BigDecimal(0));
            
        // adding current installment
        demandMap.put(currentInstallment,  new BigDecimal(6148));
        collectionMap.put(currentInstallment,  new BigDecimal(0));
        
        installmentDemandAndCollection.put("DEMAND", demandMap);
        installmentDemandAndCollection.put("COLLECTION", collectionMap);
      
        egDemand.setEgInstallmentMaster(currentInstallment);
        when(ptDemandDAO.getNonHistoryCurrDmdForProperty(Matchers.any())).thenReturn(egDemand);
        when(penaltyCalculationService.getInstallmentDemandAndCollection(Matchers.any(),Matchers.any())).thenReturn(installmentDemandAndCollection);
        when(rebateService.isEarlyPayRebateActive(new Date())).thenReturn(false);
        
        installmentPenaltyAndRebate = billable.getCalculatedPenalty();
        //assertTrue(!installmentPenaltyAndRebate.isEmpty());
        for (Map.Entry<Installment, PenaltyAndRebate> entry : installmentPenaltyAndRebate.entrySet())
        {
            assertTrue(entry.getValue().getPenalty().compareTo(new BigDecimal(0))==1 || 
                    entry.getValue().getPenalty().compareTo(new BigDecimal(0))==0);
        } 
    }
}
