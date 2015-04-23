package org.egov.web.actions.voucher;


import org.apache.struts2.convention.annotation.Action;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.Vouchermis;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.egf.commons.VoucherSearchUtil;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.bills.EgBillregistermis;
import org.egov.services.payment.PaymentService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.Session;

import com.opensymphony.xwork2.validator.annotations.Validation;

@ParentPackage("egov")  
@Validation
public class CancelVoucherAction extends BaseFormAction  {

	private static final Logger	LOGGER	= Logger.getLogger(CancelVoucherAction.class);
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",LOCALE);
	private final List<String> headerFields = new ArrayList<String>();
	private final List<String> mandatoryFields = new ArrayList<String>();
	private CVoucherHeader voucherHeader = new CVoucherHeader();
	private Map<String, String> nameMap;
	private VoucherSearchUtil voucherSearchUtil;
	private PaymentService paymentService;
	private Date fromDate;
	private Date toDate;
	private Long[] selectedVhs;
	private static final String	SEARCH	= "search";
	Integer loggedInUser ;
	public List<CVoucherHeader> voucherSearchList=new ArrayList<CVoucherHeader>();
	private PersistenceService<CVoucherHeader, Long> cVoucherHeaderPersistanceService;
	List<CVoucherHeader> voucherList;
	List<String> voucherTypes=VoucherHelper.VOUCHER_TYPES;
	Map<String,List<String>> voucherNames=VoucherHelper.VOUCHER_TYPE_NAMES;
	private FinancialYearDAO financialYearDAO;

	public CancelVoucherAction()
	{            
		voucherHeader.setVouchermis(new Vouchermis());
		addRelatedEntity("vouchermis.departmentid", Department.class);
		addRelatedEntity("fundId", Fund.class);
		addRelatedEntity("vouchermis.schemeid", Scheme.class);
		addRelatedEntity("vouchermis.subschemeid", SubScheme.class);
		addRelatedEntity("vouchermis.functionary", Functionary.class);
		addRelatedEntity("vouchermis.divisionid", Boundary.class);  
		addRelatedEntity("fundsourceId", Fundsource.class);  
	}
	
	

	@Override
	public Object getModel() {
		return voucherHeader;
	}
	
	public void prepare()
	{
		
		loggedInUser= Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		super.prepare();
		getHeaderFields();
		loadDropDowns();
	}
	
	@SkipValidation
@Action(value="/voucher/cancelVoucher-beforeSearch")
	public String beforeSearch() {
		voucherHeader.reset();
		setFromDate(null);
		setToDate(null);
		return SEARCH;
	} 
	
	@ValidationErrorPage(value=SEARCH)
	public String search() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("...Searching for voucher of type " + voucherHeader.getType());
		voucherSearchList = getVouchersForCancellation();
		return SEARCH;
	}
	

    private boolean isSuperUser(){
    	Query queryFnd = HibernateUtil.getCurrentSession().createSQLQuery(" SELECT usrr.ID_USER FROM EG_USERROLE usrr,  EG_ROLES r WHERE " +
    			" usrr.ID_ROLE       =r.ID_ROLE AND (usrr.FROMDATE      IS NULL OR usrr.FROMDATE        <='"+DDMMYYYYFORMATS.format(new Date())+"') " +
    			" AND (usrr.TODATE  IS NULL OR usrr.TODATE >='"+DDMMYYYYFORMATS.format(new Date())+"'"+") AND " +
     			" usrr.ID     ="+loggedInUser+ " AND  lower(r.ROLE_NAME)='"+FinancialConstants.SUPERUSER+"'");
		List<Object> superUserList=queryFnd.list();   
    	if(superUserList!=null && superUserList.size()>0)
    		return true;
    	else
    		return false ;
    }
    
	@SuppressWarnings("unchecked")
	private List<CVoucherHeader> getVouchersForCancellation() {
		String voucheerWithNoPayment,allPayment,noChequePaymentQry,cancelledChequePaymentQry,contraVoucherQry;
		String filterQry="";      
		boolean validateFinancialYearForPosting = voucherSearchUtil.validateFinancialYearForPosting(fromDate,toDate);
    	if(!validateFinancialYearForPosting)
    	    throw new ValidationException(Arrays.asList( new ValidationError("Financial Year  Not active for Posting(either year or date within selected date range)","Financial Year  Not active for Posting(either year or date within selected date range)")));		

		String filter =voucherSearchUtil.voucherFilterQuery(voucherHeader,fromDate,toDate,"");
		String userCond="";
		voucherList = new ArrayList<CVoucherHeader>();
		List<CVoucherHeader> toBeRemovedList=new ArrayList<CVoucherHeader>();
		if(isSuperUser())
			userCond=" ";
		else
			userCond=" and vh.createdBy="+loggedInUser;	
		filterQry=filter+userCond+"  order by vh.voucherNumber";   
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("......Searching voucher for cancelllation...");
		if(voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL))
		{
			// Voucher for which payment is not generated 
			voucheerWithNoPayment="from CVoucherHeader vh where vh not in ( select billVoucherHeader from Miscbilldetail) and vh.status in ("
							 +FinancialConstants.CREATEDVOUCHERSTATUS+")  and vh.isConfirmed != 1 ";
			// Filters vouchers for which payments are generated and are in cancelled state 
			allPayment ="select distinct(vh) from  Miscbilldetail misc left join misc.billVoucherHeader vh where misc.billVoucherHeader is not null"
						+" and vh.isConfirmed != 1 and vh.status in ("+FinancialConstants.CREATEDVOUCHERSTATUS+")"; 
			
			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(voucheerWithNoPayment + filterQry ));
			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(allPayment + filterQry ));
			
			//editModeQuery3 :-check for voucher for for which payments are active 0,5 this will be removed from above two list
			String	editModeQuery3=	" select misc.billVoucherHeader.id from CVoucherHeader ph, Miscbilldetail misc,CVoucherHeader vh  where "
									+" misc.payVoucherHeader=ph and   misc.billVoucherHeader is not null and misc.billVoucherHeader=vh " 
									+" and ph.status  in ("+FinancialConstants.CREATEDVOUCHERSTATUS+","+FinancialConstants.PREAPPROVEDVOUCHERSTATUS
									+") and vh.isConfirmed != 1 ";
			List<Long> vouchersHavingActivePayments =(List<Long>) persistenceService.findAllBy(editModeQuery3+filterQry );	
			
			// If remittance payment is there and are in cancelled state  
			String uncancelledRemittances = " SELECT distinct(vh.id) FROM EgRemittanceDetail r, EgRemittanceGldtl rgd, Generalledgerdetail gld, "
								+" CGeneralLedger gl, EgRemittance rd, CVoucherHeader vh ,Vouchermis billmis, CVoucherHeader remittedvh  WHERE "
								+" r.egRemittanceGldtl=rgd AND rgd.generalledgerdetail=gld AND gld.generalledger=gl AND r.egRemittance=rd AND"
								+" rd.voucherheader=remittedvh AND gl.voucherHeaderId =vh  and vh.isConfirmed != 1 AND "
								+" remittedvh =billmis.voucherheaderid and remittedvh.status!="+ FinancialConstants.CANCELLEDVOUCHERSTATUS +" ";
			List<Long> remittanceBillVhIdList = (List<Long>) persistenceService.findAllBy(uncancelledRemittances + filter+ userCond);
			remittanceBillVhIdList.addAll(vouchersHavingActivePayments);
			
			//If remmittacnce payment is generated remove the voucher from the list 
		 	if (voucherList != null && voucherList.size() != 0	&& remittanceBillVhIdList != null && remittanceBillVhIdList.size() != 0) {
				     for (int i = 0; i < voucherList.size(); i++) 
				     {
		                   if (remittanceBillVhIdList.contains(voucherList.get(i).getId())){
		                       toBeRemovedList.add(voucherList.get(i));
		                   }
		             }
				     for(CVoucherHeader vh:toBeRemovedList)
				     {
		                   voucherList.remove(vh);
		             }   
			}    
		}
		else if(voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT))
		{
			String qryStr=filter;
			String filterQuerySql="";
			String misTab="";
			String VoucherMisJoin="";
			if(qryStr.contains(" and vh.vouchermis")){
				misTab=", vouchermis mis ";
				VoucherMisJoin=" and vh.id=mis.voucherheaderid";
				filterQuerySql=qryStr.replace("and vh.vouchermis.", " and mis.");
			}else
				filterQuerySql=filter;
			//filterQuerySql=qryStr;
			// BPVs for which no Cheque is issued   
			noChequePaymentQry="from CVoucherHeader vh where vh.status not in ("+FinancialConstants.PREAPPROVEDVOUCHERSTATUS+","+ FinancialConstants.CANCELLEDVOUCHERSTATUS+
								") and vh.isConfirmed != 1  "+filter	+"  and not Exists(select 'true' from InstrumentVoucher iv where iv.voucherHeaderId=vh.id) order by vh.voucherNumber)";
			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(noChequePaymentQry));	
			
			// Query for cancelling BPVs for which cheque is assigned and cancelled 
			Query	query1 = HibernateUtil.getCurrentSession().createSQLQuery("SELECT distinct vh.id FROM egw_status status"+misTab+", voucherheader vh 	" 
					+" LEFT JOIN EGF_INSTRUMENTVOUCHER IV ON VH.ID=IV.VOUCHERHEADERID"
					+"	LEFT JOIN EGF_INSTRUMENTHEADER IH ON IV.INSTRUMENTHEADERID=IH.ID INNER JOIN (SELECT MAX(iv1.instrumentheaderid) AS maxihid,"
					+" iv1.voucherheaderid               AS iv1vhid   FROM egf_instrumentvoucher iv1 GROUP BY iv1.voucherheaderid) ON maxihid=IH.ID "
					+" WHERE	IV.VOUCHERHEADERID  IS NOT NULL	AND status.description   IN ('"+FinancialConstants.INSTRUMENT_CANCELLED_STATUS+"','"+
					FinancialConstants.INSTRUMENT_SURRENDERED_STATUS+"','"+FinancialConstants.INSTRUMENT_SURRENDERED_FOR_REASSIGN_STATUS+"')"
					+" and status.id=ih.id_status and vh.status not in ("+FinancialConstants.PREAPPROVEDVOUCHERSTATUS+","+ FinancialConstants.CANCELLEDVOUCHERSTATUS+
						") and vh.isConfirmed != 1 "+VoucherMisJoin+filterQuerySql);
			Iterator<BigDecimal> payList= query1.list().iterator();          
			persistenceService.setType(CVoucherHeader.class);
			while (payList.hasNext()) {				       
				voucherList.add((CVoucherHeader)persistenceService.findById((payList.next()).longValue(), false));			
			}
			                                                           
		}             
		else if(voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA))
		{
			contraVoucherQry="from CVoucherHeader vh where vh.status ="+FinancialConstants.CREATEDVOUCHERSTATUS+" and vh.isConfirmed != 1  ";
			voucherList.addAll((List<CVoucherHeader>) persistenceService.findAllBy(contraVoucherQry+ filterQry ));
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("......No of voucher found in search for is cancellation ..."+voucherList.size());
		return voucherList;
	} 
	
	@SkipValidation
	public void validateBeforeCancel(CVoucherHeader voucherObj){
		try {
			financialYearDAO.getFinancialYearByDate(voucherObj.getVoucherDate());
		} catch (Exception e) {
			addActionError("Voucher Cancellation failed for "+voucherObj.getVoucherNumber());
			throw	new ValidationException(Arrays.asList(new ValidationError(e.getMessage(),e.getMessage())));
		}
	}
	@ValidationErrorPage(value=SEARCH)
	@SkipValidation
@Action(value="/voucher/cancelVoucher-update")
	public String update(){
		CVoucherHeader voucherObj;
		
		Date modifiedDate=new Date();
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside CancelVoucher| cancelVoucherSubmit | Selected No of Vouchers for cancellation  ="+selectedVhs.length);
		String cancelVhQuery="Update CVoucherHeader vh set vh.status="+FinancialConstants.CANCELLEDVOUCHERSTATUS+",vh.modifiedBy.id=:modifiedby " +
				"			, vh.modifiedDate=:modifiedDate   where vh.id=:vhId" ;
		String cancelVhByCGNQuery="Update CVoucherHeader vh set vh.status="+FinancialConstants.CANCELLEDVOUCHERSTATUS+",vh.modifiedBy.id=:modifiedby " +
				"			, vh.modifiedDate=:modifiedDate where vh.cgn=:CGN" ;
		String cancelVhByRefCGNQuery="Update CVoucherHeader vh set vh.status="+FinancialConstants.CANCELLEDVOUCHERSTATUS+",vh.modifiedBy.id=:modifiedby " +
				"			, vh.modifiedDate=:modifiedDate where vh.refcgNo=:REFCGN" ;
		Session session=HibernateUtil.getCurrentSession();
		for(int i=0;i<selectedVhs.length;i++){
			voucherObj = (CVoucherHeader) persistenceService.find("from CVoucherHeader vh where vh.id=?",selectedVhs[i]);
			validateBeforeCancel(voucherObj);
			
			if(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL.equalsIgnoreCase(voucherObj.getType()))
			{	
				Query query = session.createQuery(cancelVhQuery);
				
				query.setLong("modifiedby",loggedInUser);
				query.setDate("modifiedDate",modifiedDate);
				query.setLong("vhId",selectedVhs[i]);
				query.executeUpdate();
				//for old vouchers when workflow was not implemented
				if (voucherObj.getState() == null && !voucherObj.getName().equals(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL)) {
					cancelBill(selectedVhs[i]);
				}
				// for workflow implementation
				else if (voucherObj.getState() != null && !voucherObj.getName().equals(FinancialConstants.JOURNALVOUCHER_NAME_GENERAL)) {
					cancelBill(selectedVhs[i]);
				}              
				continue;                                
			}
			else if(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT.equalsIgnoreCase(voucherObj.getType()))
			{
				Query query = session.createQuery(cancelVhQuery);
				query.setLong("vhId",selectedVhs[i]); 
				query.setLong("modifiedby",loggedInUser);
				query.setDate("modifiedDate",modifiedDate);
				query.executeUpdate();
				if(FinancialConstants.PAYMENTVOUCHER_NAME_REMITTANCE.equalsIgnoreCase(voucherObj.getName()))
				{
					int count =paymentService.backUpdateRemittanceDateInGL(voucherHeader.getId());
				}
				continue;
			}
			else if(FinancialConstants.STANDARD_VOUCHER_TYPE_CONTRA.equalsIgnoreCase(voucherObj.getType()))
			{
				Query query = session.createQuery(cancelVhQuery);
				query.setLong("vhId",selectedVhs[i]); 
				query.setLong("modifiedby",loggedInUser);
				query.setDate("modifiedDate",modifiedDate);
				query.executeUpdate();
				if(FinancialConstants.CONTRAVOUCHER_NAME_INTERFUND.equalsIgnoreCase(voucherObj.getName())){
					String refcgNo="";
					if(voucherObj.getRefcgNo()!=null) {
						refcgNo = voucherObj.getRefcgNo();
						Query queryFnd = session.createQuery(cancelVhByCGNQuery);
						queryFnd.setString("CGN",refcgNo); 
						queryFnd.setLong("modifiedby",loggedInUser);
						queryFnd.setDate("modifiedDate",modifiedDate);
						queryFnd.executeUpdate();
					}
					else{
						refcgNo=voucherObj.getCgn();
						Query queryFnd = session.createQuery(cancelVhByRefCGNQuery);
						
						queryFnd.setLong("modifiedby",loggedInUser);
						queryFnd.setDate("modifiedDate",modifiedDate);
						queryFnd.setString("REFCGN",refcgNo); 
						queryFnd.executeUpdate();
					}
				}continue;
			}
			else if(FinancialConstants.STANDARD_VOUCHER_TYPE_RECEIPT.equalsIgnoreCase(voucherObj.getType()))
			{
						Query query = session.createQuery(cancelVhQuery);
						query.setLong("vhId",selectedVhs[i]);
						query.setLong("modifiedby",loggedInUser);
						query.setDate("modifiedDate",modifiedDate);
						query.executeUpdate();
						continue;
			}
		}         
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" Cancel Voucher | CancelVoucher | Vouchers Cancelled ");
		addActionMessage(getText("Vouchers Cancelled Succesfully"));
		return SEARCH;
	}
            
	private void cancelBill(Long vhId) {
		StringBuffer billQuery=new StringBuffer();
		String statusQuery="(select stat.id from  EgwStatus  stat where stat.moduletype=:module and stat.description=:description)";
		String cancelQuery="Update EgBillregister set billstatus=:billstatus , status.id ="+statusQuery+" where  id=:billId";
		String moduleType="",description="",billstatus="";   
		EgBillregistermis billMis =(EgBillregistermis) persistenceService.find("from  EgBillregistermis  mis where voucherHeader.id=?",vhId);
		
		
		if(billMis!=null && billMis.getEgBillregister().getState()==null){  
		if(LOGGER.isDebugEnabled())     LOGGER.debug("....Cancelling Bill Associated with the Voucher....");
		billQuery.append("select bill.expendituretype,bill.id,bill.state.id from CVoucherHeader vh,EgBillregister bill ,EgBillregistermis mis")
				.append(" where vh.id=mis.voucherHeader and bill.id=mis.egBillregister and vh.id="+vhId);
		Object[] bill=(Object[]) persistenceService.find(billQuery.toString()); // bill[0] contains expendituretype and bill[1] contaons billid
		
		if(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY.equalsIgnoreCase(bill[0].toString())){
			billstatus=FinancialConstants.SALARYBILL;
			description=FinancialConstants.SALARYBILL_CANCELLED_STATUS;
			moduleType=FinancialConstants.SALARYBILL;
		}
		else if(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT.equalsIgnoreCase(bill[0].toString()))
		{
			for (String retval: FinancialConstants.EXCLUDED_BILL_TYPES.split(",")){
				retval = retval.replace("'", "");
				if(billMis.getEgBillSubType()!=null && billMis.getEgBillSubType().getName().equalsIgnoreCase(retval)){
						return;
				}
			}
				billstatus=FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS;
				description=FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS;
				moduleType=FinancialConstants.CONTINGENCYBILL_FIN;
		}
		
		else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE.equalsIgnoreCase(bill[0].toString()))
		{
			billstatus=FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS;
			description=FinancialConstants.SUPPLIERBILL_CANCELLED_STATUS;
			moduleType=FinancialConstants.SUPPLIERBILL;
		}
		else if (FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS.equalsIgnoreCase(bill[0].toString()))
		{
			billstatus=FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS;
			description=FinancialConstants.CONTRACTORBILL_CANCELLED_STATUS;
			moduleType=FinancialConstants.CONTRACTORBILL;
		}
		// pension vouchers created fron financials cancel bill also
		else if (FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION.equalsIgnoreCase(bill[0].toString())) {
			if ((Integer) bill[2] == null) {
				billstatus = FinancialConstants.PENSIONBILL_CANCELLED_STATUS;
				description = FinancialConstants.PENSIONBILL_CANCELLED_STATUS;
				moduleType = FinancialConstants.PENSIONBILL;
			}
		}
	                             
		Query billQry=HibernateUtil.getCurrentSession().createQuery(cancelQuery.toString());
		billQry.setString("module",moduleType);          
	 	billQry.setString("description",description);
	 	billQry.setString("billstatus",billstatus);
	 	billQry.setLong("billId", (Long)bill[1]);                       
		billQry.executeUpdate();               
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Bill Cancelled Successfully"+bill[1]);
		}
	}               
	                         


	private void loadDropDowns() {

		if(headerFields.contains("department")){
			addDropdownData("departmentList", persistenceService.findAllBy("from Department order by deptName"));
		}
		if(headerFields.contains("functionary")){
			addDropdownData("functionaryList", persistenceService.findAllBy(" from Functionary where isactive=1 order by name"));
		}
		if(headerFields.contains("fund")){
			addDropdownData("fundList", persistenceService.findAllBy(" from Fund where isactive=1 and isnotleaf=0 order by name"));
		}
		if(headerFields.contains("fundsource")){
			addDropdownData("fundsourceList", persistenceService.findAllBy(" from Fundsource where isactive=1 and isnotleaf=0 order by name"));
		}
		if(headerFields.contains("field")){
			addDropdownData("fieldList", persistenceService.findAllBy(" from Boundary b where lower(b.boundaryType.name)='ward' "));
		}
		if(headerFields.contains("scheme")){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains("subscheme")){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		//addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh  order by vh.type")); //where vh.status!=4
		addDropdownData("typeList",VoucherHelper.VOUCHER_TYPES);
		nameMap=new LinkedHashMap<String, String> ();
	}                                
	
	public void validate() {
		if(fromDate==null){
			addFieldError("From Date", getText("Please Enter From Date"));
		}if(toDate==null){
			addFieldError("To Date", getText("Please Enter To Date"));
		}
		if(voucherHeader.getType()==null || voucherHeader.getType().equals("-1") ){
			addFieldError("Voucher Type", getText("Please Select Voucher Type"));
		}
		if(voucherHeader.getName()==null || voucherHeader.getName().equals("-1") || voucherHeader.getName().equals("0")){
			addFieldError("Voucher Type", getText("Please Select Voucher Name"));
		}
		int checKDate = 0;
		if(fromDate != null && toDate != null){
			checKDate=fromDate.compareTo(toDate);
		}
		if(checKDate>0){
			addFieldError("To Date", getText("Please Enter To Date Greater than From Date"));
		}
		checkMandatoryField("fundId","fund",voucherHeader.getFundId(),"voucher.fund.mandatory");
		checkMandatoryField("vouchermis.departmentid","department",voucherHeader.getVouchermis().getDepartmentid(),"voucher.department.mandatory");
		checkMandatoryField("vouchermis.schemeid","scheme",voucherHeader.getVouchermis().getSchemeid(),"voucher.scheme.mandatory");
		checkMandatoryField("vouchermis.subschemeid","subscheme",voucherHeader.getVouchermis().getSubschemeid(),"voucher.subscheme.mandatory");
		checkMandatoryField("vouchermis.functionary","functionary",voucherHeader.getVouchermis().getFunctionary(),"voucher.functionary.mandatory");
		checkMandatoryField("fundsourceId","fundsource",voucherHeader.getVouchermis().getFundsource(),"voucher.fundsource.mandatory");
		checkMandatoryField("vouchermis.divisionId","field",voucherHeader.getVouchermis().getDivisionid(),"voucher.field.mandatory");
	}

	
	protected void getHeaderFields() 
	{
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULT_SEARCH_MISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) 
		{
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) 
			{
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf('|'));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf('|')+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
		
	}
                            
	protected void checkMandatoryField(String objectName,String fieldName,Object value,String errorKey) 
	{
		if(mandatoryFields.contains(fieldName) && ( value == null || value.equals(-1) ))
		{
			addFieldError(objectName, getText(errorKey));
		}
	}
	
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}

	public boolean shouldShowHeaderField(String field){
		return  headerFields.contains(field);
	}

	public CVoucherHeader getVoucherHeader() {
		return voucherHeader;
	}

	public void setVoucherHeader(CVoucherHeader voucherHeader) {
		this.voucherHeader = voucherHeader;
	}

	public Map<String, String> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String, String> nameMap) {
		this.nameMap = nameMap;
	}


	public Date getFromDate() {
		return fromDate;
	}


	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}


	public Date getToDate() {
		return toDate;
	}


	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public VoucherSearchUtil getVoucherSearchUtil() {
		return voucherSearchUtil;
	}

	public void setVoucherSearchUtil(VoucherSearchUtil voucherSearchUtil) {
		this.voucherSearchUtil = voucherSearchUtil;
	}
                                    
 	public List<CVoucherHeader> getVoucherSearchList() {
		return voucherSearchList;
	}
               
	public void setVoucherSearchList(List<CVoucherHeader> voucherSearchList) {
		this.voucherSearchList = voucherSearchList;
	}

	


	public Long[] getSelectedVhs() {
		return selectedVhs;
	}

	public void setSelectedVhs(Long[] selectedVhs) {
		this.selectedVhs = selectedVhs;
	}

	public List<CVoucherHeader> getVoucherList() {
		return voucherList;
	}

	public void setVoucherList(List<CVoucherHeader> voucherList) {
		this.voucherList = voucherList;
	}

	public List<String> getVoucherTypes() {
		return voucherTypes;
	}

	public void setVoucherTypes(List<String> voucherTypes) {
		this.voucherTypes = voucherTypes;
	}

	public Map<String, List<String>> getVoucherNames() {
		return voucherNames;
	}

	public void setVoucherNames(Map<String, List<String>> voucherNames) {
		this.voucherNames = voucherNames;
	}



	public PaymentService getPaymentService() {
		return paymentService;
	}



	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}



	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}


}
