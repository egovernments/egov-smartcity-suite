/**
 * 
 */
package org.egov.ptis.actions.recovery;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.commons.dao.ModuleHibDao;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.models.State;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.address.model.Address;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.actions.view.ViewPropertyAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyStatus;
import org.egov.ptis.domain.entity.recovery.Recovery;
import org.egov.ptis.domain.entity.recovery.WarrantFee;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.FinancialUtil;
import org.egov.ptis.nmc.util.PropertyTaxNumberGenerator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

/**
 * @author manoranjan
 *
 */
public class BaseRecoveryAction extends PropertyTaxBaseAction {

	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(BaseRecoveryAction.class);
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
	protected ViewPropertyAction viewPropertyAction = new ViewPropertyAction();
	protected Map<String, Object> viewMap; 
	protected String ownerName;
	protected String propertyAddress;
	protected EisCommonsService eisCommonsService;
	protected ReportService reportService;
	protected Integer reportId = -1;
    protected ModuleHibDao moduleDAO;
	protected PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	protected PersistenceService<BasicProperty, Long> basicPrpertyService;
	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	private DocumentManagerService<DocumentObject> documentManagerService;
	protected FinancialUtil financialUtil;
	protected NoticeService noticeService;
	private  UserService userService;
	@Override
	public Object getModel() {
		
		return null;
	}
	
	protected BasicProperty getPropertyView(String propertyId){
		LOGGER.debug("BaseRecoveryAction | getPropertyView | Start");
		viewPropertyAction.setPropertyId(propertyId);
		viewPropertyAction.setPropertyTaxUtil(new PropertyTaxUtil());
		viewPropertyAction.setSession(this.getSession());
		viewPropertyAction.setPersistenceService(persistenceService);
		viewPropertyAction.viewForm();
		viewMap = viewPropertyAction.getViewMap();
		LOGGER.debug("BaseRecoveryAction | getPropertyView | End");
		return viewPropertyAction.getBasicProperty();
	}
	
	protected void validateStartRecovery(Recovery recovery){
		if(getCurrentDate().after(recovery.getIntimationNotice().getPaymentDueDate())){
			
			throw new ValidationException(Arrays.asList(new ValidationError("paymentDueDate",getText("paymentDueDate.greaterThan.currentDate"))));
		}
		
	}
	protected void validateWarrantNotice(Recovery recovery){
		
		if(recovery.getIntimationNotice().getPaymentDueDate().after(recovery.getWarrantNotice().getWarrantReturnByDate())){
			
			throw new ValidationException(Arrays.asList(new ValidationError("warrantReturnByDate",getText("warrantReturnByDate.greaterThan.paymentDueDate"))));
		}
		
	}
	protected void validateCeaseNotice(Recovery recovery){
		
		if(recovery.getWarrantNotice().getWarrantReturnByDate().after(recovery.getCeaseNotice().getExecutionDate())){
			
			throw new ValidationException(Arrays.asList(new ValidationError("executionDate",getText("executionDate.greaterThan.warrantReturnByDate"))));
		}
		
	}
	protected String getNextState(String currectState){
		
		if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_NOTICE155CREATED)){
			return "Generate Notice 155";
		}
		else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_NOTICE155GENERATED)){
			return "Create Warrant";
		}else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTPREPARED)){
			return "Generate Warrant Application Pending";
		}
		else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTAPPROVED)){
			return "Create Notice 156";
		}
		else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICECREATED)){
			return "Issue Notice 156";
		}
		else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_WARRANTNOTICEISSUED)){
			return "Create Notice 159";
		}
		else if(currectState.equalsIgnoreCase(PropertyTaxConstants.RECOVERY_CEASENOTICECREATED)){
			return "Issue Notice 159";
		}
		else{
			return null;
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	protected EgBill getBil(String consumerId){
		
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment finYear = instalDao.getInsatllmentByModuleForGivenDate(module, new Date());
		// get the latest bill 
		StringBuffer query = new StringBuffer(100);
		query.append("select id from eg_bill where issue_date = ( select issue_date from (select issue_date from eg_bill where").
		append(" consumer_id='"+consumerId+"' and issue_date >=to_date('"+DDMMYYYYFORMATS.format(finYear.getFromDate())+"','dd/MM/yyyy')").
		append("  order by issue_date desc)  group by rownum,issue_date having rownum <=1)");
		LOGGER.debug("BaseRecoveryAction | getBilAmount | query >> "+query.toString());
		List list = persistenceService.getSession().createSQLQuery(query.toString()).list();
		if(list != null && list.size()>0){
			EgBill bill =(EgBill) persistenceService.find(" from EgBill where id="+list.get(0));
			return bill;
		}else{
			return null;
		}
	}
	
	public Long getDemandReason(String demandMaster){
		StringBuffer query = new StringBuffer();
		query.append(" from EgDemandReason where egDemandReasonMaster.reasonMaster='"+demandMaster+"'")
			.append(" and egInstallmentMaster.id='"+propertyTaxUtil.getCurrentInstallment().getId()+ "'");
		EgDemandReason demandReason = (EgDemandReason)persistenceService.find(query.toString());
		
		return demandReason.getId();
	}
	
	protected Map<String,Object>  getNotice156Param(Recovery recovery){
		
		Map<String,Object>  params= new HashMap<String,Object> ();
		params.put("currentDate",DateUtils.getFormattedDate(new Date(), "dd/MM/yyyy"));
		params.put("ownerFatherName", "-");
		params.put("taxName", "Property Tax");
		BigDecimal totalWarrantFees = BigDecimal.ZERO;
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalWarrantFees = totalWarrantFees.add(warrantFee.getAmount());
		}
		params.put("totalWarrantFees", totalWarrantFees.setScale(2).toString());
		params.put("warrantReturnByDate", DateUtils.convertToWords(recovery.getWarrantNotice().getWarrantReturnByDate()));
		return params;
	}
	
	protected Map<String,Object>  getNotice159Param(Recovery recovery){
		
		Map<String,Object>  params= new HashMap<String,Object> ();
		params.put("fatherName", "-");
		params.put("noticeDate", DDMMYYYYFORMATS.format(new Date()));
		BigDecimal totalWarrantFees = BigDecimal.ZERO;
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalWarrantFees = totalWarrantFees.add(warrantFee.getAmount());
		}
		params.put("totalWarrantFees", totalWarrantFees.setScale(2).toString());
	
		return params;
	}	
	
	@SuppressWarnings("unchecked")
	protected void updateDemand(Recovery recovery){
		StringBuffer consumerId = new StringBuffer();
		consumerId.append(recovery.getBasicProperty().getUpicNo()).append("(Zone:")
			.append(recovery.getBasicProperty().getPropertyID().getZone().getBoundaryNum())
			.append(" Ward:").append(recovery.getBasicProperty().getPropertyID().getWard().getBoundaryNum()).append(")");
		
		EgBill currentBill = getBil(consumerId.toString());
		EgDemand currentDemand = currentBill.getEgDemand();
		Set<EgDemandDetails> demandDetails = currentDemand.getEgDemandDetails();
		EgDemandDetails demandDetail = new EgDemandDetails();
		BigDecimal totalDemandAmt = currentDemand.getBaseDemand();
		for (WarrantFee warrantFee : recovery.getWarrant().getWarrantFees()) {
			totalDemandAmt = totalDemandAmt.add(warrantFee.getAmount());
			demandDetail = new EgDemandDetails();
			demandDetail.setAmount(warrantFee.getAmount());
			demandDetail.setAmtCollected(BigDecimal.ZERO);
			demandDetail.setAmtRebate(BigDecimal.ZERO);
			demandDetail.setEgDemandReason(warrantFee.getDemandReason());
			demandDetail.setCreateTimestamp(new Date());
			demandDetails.add(demandDetail);
		}
		currentDemand.setBaseDemand(totalDemandAmt);
		currentDemand.setEgDemandDetails(demandDetails);
		currentBill.setTotalAmount(totalDemandAmt);
		currentBill.setEgDemand(currentDemand);
		EgBillDao billDAO = DCBDaoFactory.getDaoFactory().getEgBillDao();
		billDAO.update(currentBill);
		
		Map<Installment, Map<String, BigDecimal>> amounts = new HashMap<Installment, Map<String,BigDecimal>>();
		Map<String, BigDecimal> voucherDemandMap = new HashMap<String, BigDecimal>(); // Map for create voucher
		voucherDemandMap.put(NMCPTISConstants.DEMANDRSN_CODE_RECOVERY_FEE, totalDemandAmt);
		amounts.put(currentDemand.getEgInstallmentMaster(), voucherDemandMap);
		
		financialUtil.createVoucher(recovery.getBasicProperty().getUpicNo(), amounts, "Recovery Fees");
		
	}
	
	protected EgwStatus getEgwStatusForModuleAndCode(String moduleName, String statusCode) {
		
		EgwStatus status = (EgwStatus) persistenceService.findByNamedQuery(PropertyTaxConstants.QUERY_STATUS_BY_MODULE_AND_CODE, 
																			moduleName, statusCode);
		return status;
	}
	protected PropertyStatus getPropStatusByStatusCode(String statusCode) {
		
		PropertyStatus status = (PropertyStatus) persistenceService.findByNamedQuery(PropertyTaxConstants.QUERY_PROP_STATUS_BY_STATUSCODE,statusCode);
		return status;
	}
	
	/**
	 * 
	 * @param state - The state of the work flow object
	 * @return TRUE - if  logged in user is the authorised user to view the inbox item.
	 *         FALSE - if  logged in user is not the authenticated to view the inbox item.
	 */
	public Boolean authenticateInboxItemRqst(State state){
		if(null == state || null == state.getOwner())
			return Boolean.FALSE;
		UserImpl authorisedUser = (UserImpl)eisCommonsService.getUserforPosition(state.getOwner());
		UserImpl loggedInUser =(UserImpl) new UserDAO().getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId()));
		return authorisedUser.equals(loggedInUser);
		
	}
	
	protected Integer addingReportToSession(ReportOutput reportOutput){
		 return	ReportViewerUtil.addReportToSession(reportOutput, getSession());
	}
	public void setEisCommonsManager(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public Map<String, Object> getViewMap() {
		return viewMap;
	}

	public void setViewPropertyAction(ViewPropertyAction viewPropertyAction) {
		this.viewPropertyAction = viewPropertyAction;
	}

	public void setViewMap(Map<String, Object> viewMap) {
		this.viewMap = viewMap;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setModuleDAO(ModuleHibDao moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	public void setPropertyTaxNumberGenerator(
			PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public void setBasicPrpertyService(
			PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setOwnerName(Property property) {
		this.ownerName = ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet());
	}

	public void setPropertyAddress(Address address) {
		this.propertyAddress = ptisCacheMgr.buildAddressByImplemetation(address);
	}
	 public Date getCurrentDate(){
	    	
	    	return new Date();
	    }

	public void setDocumentManagerService(
			DocumentManagerService<DocumentObject> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

	public void setFinancialUtil(FinancialUtil financialUtil) {
		this.financialUtil = financialUtil;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
