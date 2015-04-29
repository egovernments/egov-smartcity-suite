
package org.egov.works.services.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CommonsService;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetUsage;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.AbstractEstimateAppropriation;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.RevisionEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * This class will expose all Revision Estimate related operations.
 * @author Julian 
 *
 */
public class RevisionEstimateServiceImpl extends BaseServiceImpl<RevisionAbstractEstimate, Long>
										implements RevisionEstimateService{

	private AbstractEstimateService abstractEstimateService;
	private WorksService worksService;
	private EgovCommon egovCommon;
	private DepositWorksUsageService depositWorksUsageService;
	@Autowired
	private CommonsService commonsService;
	private PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService;
	private BudgetDetailsDAO budgetDetailsDAO;
	
	private static final String MODULE_NAME = "Works";
	private static final String KEY_NAME = "SKIP_BUDGET_CHECK";

	
	public RevisionEstimateServiceImpl(
			PersistenceService<RevisionAbstractEstimate, Long> persistenceService) {
		super(persistenceService);
	}

	@Override
	public void consumeBudget(RevisionAbstractEstimate revisionEstimate) {
		AbstractEstimate parentEstimate = revisionEstimate.getParent();
		revisionEstimate.setBudgetApprNo(abstractEstimateService.getBudgetAppropriationNumber(parentEstimate));
		boolean isBudgetConsumptionSuccessful = false;
		if(isDepositWorksType(parentEstimate))
			isBudgetConsumptionSuccessful = checkForBudgetaryAppropriationForDepositWorks(revisionEstimate);
		else
			isBudgetConsumptionSuccessful = consumeBudgetForNormalWorks(revisionEstimate);
		if(!isBudgetConsumptionSuccessful)
			revisionEstimate.setBudgetApprNo(null);
	}
	
	@Override
	public void releaseBudget(RevisionAbstractEstimate revisionEstimate) {
		AbstractEstimate parentEstimate = revisionEstimate.getParent();
		revisionEstimate.setBudgetRejectionNo("BC/"+revisionEstimate.getBudgetApprNo());
		
		//Financial details of the parent
		FinancialDetail  financialDetail = parentEstimate.getFinancialDetails().get(0);
		boolean isBudgetReleaseSuccessful = false;
		if(isDepositWorksType(parentEstimate))
			isBudgetReleaseSuccessful = releaseDepositWorksAmountOnReject(revisionEstimate, financialDetail);
		else
			isBudgetReleaseSuccessful = releaseBudgetOnReject(revisionEstimate,financialDetail);
		if(!isBudgetReleaseSuccessful)
			revisionEstimate.setBudgetRejectionNo(null);
	}
	
	private boolean releaseDepositWorksAmountOnReject(RevisionAbstractEstimate revisionEstimate, FinancialDetail financialDetail) 
			throws ValidationException{			
				boolean flag=false;
				Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
				AbstractEstimateAppropriation estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",revisionEstimate.getId());
				//No budget appropriation was done in the first place
				if(estimateAppropriation==null)
					return flag;
				BigDecimal creditBalance=egovCommon.getDepositAmountForDepositCode(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());
				double releaseAmount=estimateAppropriation.getDepositWorksUsage().getConsumedAmount().doubleValue();
				DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
				depositWorksUsage.setTotalDepositAmount(creditBalance);
				depositWorksUsage.setConsumedAmount(BigDecimal.ZERO);
				depositWorksUsage.setReleasedAmount(new BigDecimal(releaseAmount));
				depositWorksUsage.setAppropriationNumber(revisionEstimate.getBudgetRejectionNo());
				depositWorksUsage.setAbstractEstimate(revisionEstimate);
				depositWorksUsage.setAppropriationDate(new Date());
				depositWorksUsage.setFinancialYearId(estimateAppropriation.getDepositWorksUsage().getFinancialYearId());
				depositWorksUsage.setCoa(financialDetail.getCoa());
				depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
				depositWorksUsage=depositWorksUsageService.persist(depositWorksUsage);
				persistReleaseDepositWorksAmountDetails(revisionEstimate,financialDetail, depositWorksUsage);
				flag=true;
				return flag;
	}
	
	private void persistReleaseDepositWorksAmountDetails(RevisionAbstractEstimate revisionEstimate, FinancialDetail financialDetail, DepositWorksUsage depositWorksUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		BigDecimal creditBalance=depositWorksUsage.getTotalDepositAmount();
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,new Date());
		BigDecimal balance=creditBalance.subtract(utilizedAmt);
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",revisionEstimate.getId());
		estimateAppropriation.setBalanceAvailable(balance);
		estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		estimateAppropriationService.persist(estimateAppropriation);
	}
	private boolean consumeBudgetForNormalWorks(RevisionAbstractEstimate revisionEstimate) {
		boolean flag=false;
		Long finYearId=commonsService.getFinancialYearByDate(new Date()).getId();
		List<Long> budgetHeadId = new ArrayList<Long>();
		FinancialDetail financialDetail = revisionEstimate.getParent().getFinancialDetails().get(0);
		budgetHeadId.add(financialDetail.getBudgetGroup().getId());
		BudgetUsage budgetUsage=budgetDetailsDAO.consumeEncumbranceBudget(
				revisionEstimate.getBudgetApprNo()==null? null:revisionEstimate.getBudgetApprNo(),
				finYearId, Integer.valueOf(11), 
				revisionEstimate.getEstimateNumber(), 
				Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()), 
				(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
				(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
				(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
				(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
				(financialDetail.getAbstractEstimate().getWard()==null? null:Integer.parseInt(financialDetail.getAbstractEstimate().getWard().getId().toString())),
				(financialDetail.getBudgetGroup()==null? null:budgetHeadId), 
				(financialDetail.getFund()==null? null:financialDetail.getFund().getId()),
				revisionEstimate.getTotalAmount().getValue()
				);
		if(budgetUsage==null)
			throw new ValidationException(Arrays.asList(new ValidationError("changeFDHeader.budget.consumption.failed","Insufficient funds available")));
		persistBudgetAppropriationDetails(revisionEstimate,revisionEstimate.getParent(),budgetUsage);
		flag = true;
		return flag;
	}
	
	private void persistBudgetAppropriationDetails(RevisionAbstractEstimate revisionEstimate,AbstractEstimate parentEstimate,BudgetUsage budgetUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		Integer finYearId=budgetUsage.getFinancialYearId();
		Date endingDate=commonsService.getFinancialYearById(finYearId.longValue()).getEndingDate();
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getBudgetUsageForEstimateByFinYear",revisionEstimate.getId(),finYearId.intValue());

		if(estimateAppropriation!=null){
			estimateAppropriation.setBalanceAvailable(abstractEstimateService.getBudgetAvailable(parentEstimate,endingDate));
			estimateAppropriation.setBudgetUsage(budgetUsage);
		}else{
			estimateAppropriation=new AbstractEstimateAppropriation();
			estimateAppropriation.setAbstractEstimate(revisionEstimate);
			estimateAppropriation.setBalanceAvailable(abstractEstimateService.getBudgetAvailable(parentEstimate,endingDate));
			estimateAppropriation.setBudgetUsage(budgetUsage);
		}
		estimateAppropriationService.persist(estimateAppropriation);
	}
	private boolean checkForBudgetaryAppropriationForDepositWorks(RevisionAbstractEstimate revisionEstimate) 
	throws ValidationException{			
		boolean flag=false;
		Date appDate=new Date();
		double depApprAmnt=0.0;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE");
		depApprAmnt=revisionEstimate.getTotalAmount().getValue();
		
		FinancialDetail financialDetail = revisionEstimate.getParent().getFinancialDetails().get(0);
		BigDecimal creditBalance=egovCommon.getDepositAmountForDepositCode(new Date(),financialDetail.getCoa().getGlcode(),financialDetail.getFund().getCode(), accountdetailtype.getId(), financialDetail.getAbstractEstimate().getDepositCode().getId().intValue());

		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,appDate);
		BigDecimal balance=BigDecimal.ZERO;
		if(utilizedAmt==null){
			balance=creditBalance;
			utilizedAmt=BigDecimal.ZERO;
		}
		else{
			balance=creditBalance.subtract(utilizedAmt);
		}
		
		if(balance.doubleValue()>=depApprAmnt) {
			DepositWorksUsage depositWorksUsage=new DepositWorksUsage();
			CFinancialYear budgetApprDate_finYear=commonsService.getFinancialYearByDate(appDate);
			depositWorksUsage.setTotalDepositAmount(creditBalance);
			depositWorksUsage.setConsumedAmount(new BigDecimal(depApprAmnt));
			depositWorksUsage.setReleasedAmount(BigDecimal.ZERO);
			depositWorksUsage.setAppropriationNumber(revisionEstimate.getBudgetApprNo());
			depositWorksUsage.setAbstractEstimate(revisionEstimate);
			depositWorksUsage.setAppropriationDate(appDate);
			depositWorksUsage.setFinancialYearId(budgetApprDate_finYear.getId().intValue());
			depositWorksUsage.setDepositCode(financialDetail.getAbstractEstimate().getDepositCode());
			depositWorksUsage.setCoa(financialDetail.getCoa());
			depositWorksUsage=depositWorksUsageService.persist(depositWorksUsage);
			persistDepositCodeAppDetails(depositWorksUsage, financialDetail);
			flag=true;
		}
		if (!flag) {
			throw new ValidationException(Arrays.asList(new ValidationError("reEstimate.estimate.validate.budget.amount","Insufficient funds available")));
		}
		return flag;
	}
	
	private void persistDepositCodeAppDetails(DepositWorksUsage depositWorksUsage, FinancialDetail financialDetail){
		AbstractEstimateAppropriation estimateAppropriation=null;
		int finYearId=commonsService.getFinancialYearByDate(new Date()).getId().intValue();
		BigDecimal creditBalance=depositWorksUsage.getTotalDepositAmount();
		AbstractEstimate abstractEstimate=depositWorksUsage.getAbstractEstimate();
		BigDecimal utilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail,depositWorksUsage.getCreatedDate());
		BigDecimal balance=BigDecimal.ZERO;
		if(utilizedAmt==null){
			balance=creditBalance;
		    utilizedAmt=BigDecimal.ZERO;
		}
		else{
			balance=creditBalance.subtract(utilizedAmt);
		}

		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getDepositWorksUsageForEstimateByFinYear",abstractEstimate.getId(),finYearId);
		if(estimateAppropriation!=null){
			estimateAppropriation.setBalanceAvailable(balance);
			estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		}else{
			estimateAppropriation=new AbstractEstimateAppropriation();
			estimateAppropriation.setAbstractEstimate(abstractEstimate);
			estimateAppropriation.setBalanceAvailable(balance);
			estimateAppropriation.setDepositWorksUsage(depositWorksUsage);
		}
		estimateAppropriationService.persist(estimateAppropriation);
	}
	
	private boolean releaseBudgetOnReject(RevisionAbstractEstimate revisionEstimate,FinancialDetail financialDetail) 
	throws ValidationException{
		boolean flag=false;
		AbstractEstimateAppropriation estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",revisionEstimate.getId());
		//No budget appropriation was done for this revision estimate
		if(estimateAppropriation==null)
			return flag;
		List<Long> budgetheadid=new ArrayList<Long>();
		budgetheadid.add(financialDetail.getBudgetGroup().getId());
		BudgetUsage budgetUsage=null;
		
		budgetUsage=budgetDetailsDAO.releaseEncumbranceBudget(
			revisionEstimate.getBudgetRejectionNo()==null? null:revisionEstimate.getBudgetRejectionNo(),	
			estimateAppropriation.getBudgetUsage().getFinancialYearId().longValue(), Integer.valueOf(11), 
			revisionEstimate.getEstimateNumber(), 
			Integer.parseInt(financialDetail.getAbstractEstimate().getUserDepartment().getId().toString()), 
			(financialDetail.getFunction()==null? null:financialDetail.getFunction().getId()), 
			(financialDetail.getFunctionary()==null? null:financialDetail.getFunctionary().getId()), 
			(financialDetail.getScheme()==null? null:financialDetail.getScheme().getId()), 
			(financialDetail.getSubScheme()==null? null:financialDetail.getSubScheme().getId()), 
			(financialDetail.getAbstractEstimate().getWard()==null? null:Integer.parseInt(financialDetail.getAbstractEstimate().getWard().getId().toString())),
			(financialDetail.getBudgetGroup()==null? null:budgetheadid), 
			(financialDetail.getFund()==null? null:financialDetail.getFund().getId()), 
			estimateAppropriation.getBudgetUsage().getConsumedAmount()
			);
		
		if(financialDetail.getAbstractEstimate()!=null){
			persistBudgetReleaseDetails(revisionEstimate,financialDetail.getAbstractEstimate(),budgetUsage);
		}
		flag = true;
		return flag;
	}
	
	private void persistBudgetReleaseDetails(RevisionAbstractEstimate revisionEstimate,AbstractEstimate parentEstimate, BudgetUsage budgetUsage){
		AbstractEstimateAppropriation estimateAppropriation=null;
		estimateAppropriation=estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",revisionEstimate.getId());
		Integer finYearId =estimateAppropriation.getBudgetUsage().getFinancialYearId();
		Date endingDate=commonsService.getFinancialYearById(finYearId.longValue()).getEndingDate();
		estimateAppropriation.setBalanceAvailable(abstractEstimateService.getBudgetAvailable(parentEstimate,endingDate));
		estimateAppropriation.setBudgetUsage(budgetUsage);
		estimateAppropriationService.persist(estimateAppropriation);
	}
	
	@Override
	public boolean isDepositWorksType(AbstractEstimate estimate)
	{
		boolean isDepositWorks=false;
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();			
		for(String type:depositTypeList){
			if(type.equals(estimate.getType().getName())){
				isDepositWorks=true;
			}
		}
		return isDepositWorks;
	}

	@Override
	public boolean getShowBudgetFolio(AbstractEstimate revisionEstimate){
		if(revisionEstimate!=null && revisionEstimate.getEgwStatus()!=null 
				&& revisionEstimate.getParent()!=null && !isDepositWorksType(revisionEstimate.getParent()))
			return estimateAppropriationService.findByNamedQuery("getLatestBudgetUsageForEstimate",revisionEstimate.getId())!=null?true:false;
		else
			return false;
	}

	@Override
	public boolean getShowDepositFolio(AbstractEstimate revisionEstimate){
		if(revisionEstimate!=null && revisionEstimate.getEgwStatus()!=null 
				&& revisionEstimate.getParent()!=null && isDepositWorksType(revisionEstimate.getParent()))
			return estimateAppropriationService.findByNamedQuery("getLatestDepositWorksUsageForEstimate",revisionEstimate.getId())!=null?true:false;
		else
			return false;
	}
	
	private List<String> getAppConfigValuesToSkipBudget(){
		return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public void setDepositWorksUsageService(
			DepositWorksUsageService depositWorksUsageService) {
		this.depositWorksUsageService = depositWorksUsageService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setEstimateAppropriationService(
			PersistenceService<AbstractEstimateAppropriation, Long> estimateAppropriationService) {
		this.estimateAppropriationService = estimateAppropriationService;
	}

	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}

	
}
