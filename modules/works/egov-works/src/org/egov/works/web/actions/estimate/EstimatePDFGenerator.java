package org.egov.works.web.actions.estimate;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CFinancialYear;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.State;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.budget.BudgetUsage;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.estimate.DepositWorksUsage;
import org.egov.works.models.estimate.FinancialDetail;
import org.egov.works.models.estimate.MultiYearEstimate;
import org.egov.works.models.estimate.OverheadValue;
import org.egov.works.models.estimate.TechnicalSanction;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.DepositWorksUsageService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.egov.works.utils.DateConversionUtil;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class EstimatePDFGenerator extends AbstractPDFGenerator  { 
	private static final Logger logger = Logger.getLogger(EstimatePDFGenerator.class);
	private PersistenceService persistenceService = new PersistenceService(); 
	private final AbstractEstimate estimate; 
	private final CFinancialYear financialYear;
	private String headerText;
	private EmployeeService employeeService;
	private BudgetDetailsDAO budgetDetailsDAO;
	private AbstractEstimateService abstractEstimateService;
	private static final String space1="\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t";
	
	private static final String PERCENTAGE_GRANT="percentage_grant";
	private WorksService worksService;
	private String appValue ="";
	private DepositWorksUsageService depositWorksUsageService;
	private static final String MODULE_NAME = "Works";
	private static final String KEY_NAME = "SKIP_BUDGET_CHECK";
	private boolean skipBudget=false;

	
	public void setBudgetDetailsDAO(BudgetDetailsDAO budgetDetailsDAO) {
		this.budgetDetailsDAO = budgetDetailsDAO;
	}	
	
	public EstimatePDFGenerator(AbstractEstimate estimate, String headerText,
			CFinancialYear financialYear, OutputStream out) {
		super(out,"portrait");
		this.estimate = estimate;
		this.headerText = headerText;
		this.financialYear = financialYear;
	}
	
	public void generatePDF() throws ValidationException {
		try {
			Paragraph headerTextPara = new Paragraph(new Chunk(headerText, new Font(Font.UNDEFINED, LARGE_FONT,Font.BOLD)));
			String projectCode=null;
			String techSanctionNumber="";
			String AdminsanctionNo="";
			HeaderFooter hf=null;
			headerTextPara.setAlignment(Element.ALIGN_CENTER);
			document.add(headerTextPara);
			document.add(makePara(estimate.getExecutingDepartment().getDeptName(), Element.ALIGN_CENTER));
			CFinancialYear estimateFinancialYear = estimate.getMultiYearEstimates().isEmpty()?financialYear:estimate.getMultiYearEstimates().get(0).getFinancialYear();
			addZoneYearHeader(estimate, estimateFinancialYear);
			setHeader();
			document.add(makePara("Name of Work: " + estimate.getName(),Element.ALIGN_LEFT));
			document.add(makePara("Description: " + estimate.getDescription(),Element.ALIGN_LEFT));
			
			if(estimate.getTechSanctionNumber()!=null){
        		techSanctionNumber="Technical Sanction Number : "+estimate.getTechSanctionNumber();
        		document.add(makePara(techSanctionNumber,Element.ALIGN_LEFT));
        		techSanctionNumber="";
			}
			
			if(estimate.getProjectCode()!=null){
	        	projectCode="Project Code : "+estimate.getProjectCode().getCode();
	        	document.add(makePara(projectCode,Element.ALIGN_LEFT));
	         }
			
			if(estimate.getAdminsanctionNo()!=null){
	        	AdminsanctionNo="Admin Sanction Number: "+estimate.getAdminsanctionNo();
	        	document.add(makePara(AdminsanctionNo,Element.ALIGN_LEFT));
	         }
			
			PdfPTable overheadsTable = createOverheadsTable(estimate);
			document.add(spacer());
			document.add(overheadsTable);
			document.add(spacer());
			PdfPTable multiyearTable = createMultiYearTable(estimate);
			document.add(makePara("Year-wise Estimate"));
			document.add(spacer());
			document.add(multiyearTable);
			document.add(spacer());
			document.add(makePara("Prepared By: "+ estimate.getEstimatePreparedBy().getEmployeeName()));
			if(estimate.getEstimatePreparedBy().getUserMaster()!=null && estimate.getEstimatePreparedBy().getUserMaster().getUserSignature()!=null){
				try{
					Image image=Image.getInstance(CryptoHelper.decrypt(estimate.getEstimatePreparedBy().getUserMaster().getUserSignature().getSignature(), CryptoHelper.decrypt(estimate.getEstimatePreparedBy().getUserMaster().getPwd())));
					if(image.getScaledHeight()>50 || image.getScaledWidth()>150 ){
						image.scaleToFit(50, 150);
				}
					document.add(image);
				}
				catch(Exception e){
					throw new DocumentException("Exception occured while getting signature "+e);
				}
			}
			document.add(spacer());
			document.add(spacer());
			document.add(makePara("Checked By: "));
			document.newPage();
			addZoneYearHeaderWithOutEstimateNo(estimate, estimateFinancialYear);
			document.add(createActivitiesTable(estimate));
			List<AbstractEstimate> ae=new LinkedList<AbstractEstimate>() ;
			ae= getPersistenceService().findAllByNamedQuery("HAS_REVISION_ESTIMATES", estimate.getId());
			 if(ae!=null && !ae.isEmpty()){
				 String reProjectCode=null;
				 String reTechSanctionNumber="";
				 for(AbstractEstimate aeObj : ae){  
					  reTechSanctionNumber="";
							TechnicalSanction ts=(TechnicalSanction)getPersistenceService().findByNamedQuery("getLatestTechSanctionNumber",aeObj.getId());
							if(ts!=null){
								String techSanctionNo=ts.getTechSanctionNumber(); 
								if(techSanctionNo!=null) 
									aeObj.setTechSanctionNumber(techSanctionNo); 
								}
					 if(aeObj.getTechSanctionNumber()!=null){
						 	reTechSanctionNumber="Technical Sanction Number : "+aeObj.getTechSanctionNumber();
			        		document.add(makePara(techSanctionNumber,Element.ALIGN_LEFT));
						}
				        	 hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t" +
				                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
				                    "\t  \t  \t  \t \t\t  \t  \t  \t \t"+headerText.concat("\n").concat("\t  \t  \t  \t \t \t  \t  \t  \t \t" +
				                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
				                    "\t  \t  \t  \t \t\t  \t  \t  \t \t REVISION ESTIMATE").concat("\n\n").
				                    concat("\n").concat("Revised Estimate Number: " + aeObj.getEstimateNumber()+"               Revised Estimate Date: "+aeObj.getEstimateDate()).
				                    concat("\n").concat("Notes: ").
				                    concat("\n").concat(reTechSanctionNumber)),false); 
					 hf.setLeft(HeaderFooter.ALIGN_LEFT);
					 hf.disableBorderSide(HeaderFooter.TOP);
					 hf.disableBorderSide(HeaderFooter.BOTTOM);
				 	 document.setHeader(hf);
					 document.newPage();
					 document.add(createREActivitiesTable(aeObj));
			document.add(spacer());
				 	 document.newPage();
				 	 
				 	PdfPTable approvaldetailsTable = createApprovalDetailsTable(aeObj);
				 	if (approvaldetailsTable.getRows().size()!=1) {
						document.add(makePara("Approval Details"));
						document.add(spacer());
						document.add(approvaldetailsTable);
					}
				 	
				 	if(isSkipBudgetCheck()){
					      PdfPTable depositWorksAppropriationTable = createDepositAppropriationTable(aeObj);
						  if (depositWorksAppropriationTable.getRows().size()!=1) {
							 if(aeObj.getBudgetApprNo()!=null){
								document.newPage();
								document.add(spacer());
								document.add(makePara("Deposit Code Appropriation Details"));
								document.add(spacer());
								document.add(depositWorksAppropriationTable);
							    }
							 }
						  }
					else{
						PdfPTable BudgetaryAppropriationTable = createBudgetaryAppropriationTable(aeObj);
						String estimateNumber=aeObj.getEstimateNumber();
					    if (BudgetaryAppropriationTable.getRows().size()!=1) {
					      if(getBudgetDetailUsage(estimateNumber).size()!=0 && aeObj.getBudgetApprNo()!=null){
							 document.newPage();
							 document.add(spacer());
							 document.add(makePara("Budgetary Appropriation"));
							 document.add(spacer());
							 document.add(BudgetaryAppropriationTable); 
					      }
					    }
					  }
					document.newPage();
					document.add(spacer());
					document.add(spacer());
					document.add(makePara("EXECUTIVE ENGINEER'S OFFICE,  ZONE.......................................................................",Element.ALIGN_LEFT));
					document.add(spacer());
					document.add( makePara("Est No.                                                Unit:                                                 Dept." ,Element.ALIGN_LEFT));
					document.add(spacer());
					Paragraph budgetheadTextPara = new Paragraph(new Chunk("BUDGET HEAD", new Font(Font.UNDEFINED, LARGE_FONT,Font.BOLD)));
					budgetheadTextPara.setAlignment(Element.ALIGN_CENTER);
					document.add(budgetheadTextPara);
					document.add(spacer());
					document.add(makePara("____________________________________________________________________________",Element.ALIGN_LEFT));
					document.add( makePara("Rs.                                            " ,Element.ALIGN_LEFT));
					document.add(makePara("____________________________________________________________________________",Element.ALIGN_LEFT));
					document.add( makePara("Works:                                          " ,Element.ALIGN_LEFT));
					document.add(spacer());
					document.add(spacer());
					Paragraph memoTextPara = new Paragraph(new Chunk("MEMO", new Font(Font.UNDEFINED, LARGE_FONT,Font.BOLD)));
					memoTextPara.setAlignment(Element.ALIGN_CENTER);
					document.add(memoTextPara);
					document.add( makePara("Budget Grant                               " ,Element.ALIGN_LEFT));
					document.add( makePara("Amount Appropriated:__________________________________________________________" ,Element.ALIGN_LEFT));
					document.add( makePara("Balance on Hand:                                " ,Element.ALIGN_LEFT));
					document.add( makePara("Amount of this estimate_________________________________________________________" ,Element.ALIGN_LEFT));			
					document.add( makePara("Balance forward_______________________________________________________________" ,Element.ALIGN_LEFT));
					document.add( makePara("Submitted for favour of sanction                           " ,Element.ALIGN_LEFT));
					document.add(spacer());
					document.add(spacer());	
					document.add( makePara("A.E.E.Unit "+space1+
					"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t \t \t \t"+"Exe.Eng.Zone....................." ,Element.ALIGN_LEFT));
					document.add(spacer());
					document.add( makePara("Sanctioned" ,Element.ALIGN_CENTER));			
					document.add(spacer());
					//document.add(spacer());
					document.add( makePara("DATE:"+space1+
							"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+"Asst.Commissioner Zone..............." ,Element.ALIGN_LEFT));
					document.add(spacer());
					document.add(spacer());
					document.add( makePara(space1+
							"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+"APPROPRIATION No." ,Element.ALIGN_LEFT));
					document.add(spacer()); 
					document.add( makePara(space1+"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+
							"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t"+"Date:" ,Element.ALIGN_LEFT));
			 }
			 }
			
			setHeader();
			document.newPage();
			setHeader();
			document.newPage();
			
			PdfPTable approvaldetailsTable = createApprovalDetailsTable(estimate);
			
			if (approvaldetailsTable.getRows().size()!=1) {
				document.add(makePara("Approval Details"));
				document.add(spacer());
				document.add(approvaldetailsTable);
			}
			
		
			if(isSkipBudgetCheck()){
			      PdfPTable depositWorksAppropriationTable = createDepositAppropriationTable(estimate);
				  if (depositWorksAppropriationTable.getRows().size()!=1) {
					 if(estimate.getBudgetApprNo()!=null){
						document.newPage();
						document.add(spacer());
						document.add(makePara("Deposit Code Appropriation Details"));
						document.add(spacer());
						document.add(depositWorksAppropriationTable);
					    }
					 }
				  }
			else{
				PdfPTable BudgetaryAppropriationTable = createBudgetaryAppropriationTable(estimate);
				String estimateNumber=estimate.getEstimateNumber();
			    if (BudgetaryAppropriationTable.getRows().size()!=1) {
			      if(getBudgetDetailUsage(estimateNumber).size()!=0 && estimate.getBudgetApprNo()!=null){
					 document.newPage();
					 document.add(spacer());
					 document.add(makePara("Budgetary Appropriation"));
					 document.add(spacer());
					 document.add(BudgetaryAppropriationTable); 
			      }
			    }
			  }
			document.newPage();
			document.add(spacer());
			document.add(spacer());
			document.add(makePara("EXECUTIVE ENGINEER'S OFFICE,  ZONE.......................................................................",Element.ALIGN_LEFT));
			document.add(spacer());
			document.add( makePara("Est No.                                                Unit:                                                 Dept." ,Element.ALIGN_LEFT));
			document.add(spacer());
			Paragraph budgetheadTextPara = new Paragraph(new Chunk("BUDGET HEAD", new Font(Font.UNDEFINED, LARGE_FONT,Font.BOLD)));
			budgetheadTextPara.setAlignment(Element.ALIGN_CENTER);
			document.add(budgetheadTextPara);
			document.add(spacer());
			document.add(makePara("____________________________________________________________________________",Element.ALIGN_LEFT));
			document.add( makePara("Rs.                                            " ,Element.ALIGN_LEFT));
			document.add(makePara("____________________________________________________________________________",Element.ALIGN_LEFT));
			document.add( makePara("Works:                                          " ,Element.ALIGN_LEFT));
			document.add(spacer());
			document.add(spacer());
			Paragraph memoTextPara = new Paragraph(new Chunk("MEMO", new Font(Font.UNDEFINED, LARGE_FONT,Font.BOLD)));
			memoTextPara.setAlignment(Element.ALIGN_CENTER);
			document.add(memoTextPara);
			document.add( makePara("Budget Grant                               " ,Element.ALIGN_LEFT));
			document.add( makePara("Amount Appropriated:__________________________________________________________" ,Element.ALIGN_LEFT));
			document.add( makePara("Balance on Hand:                                " ,Element.ALIGN_LEFT));
			document.add( makePara("Amount of this estimate_________________________________________________________" ,Element.ALIGN_LEFT));			
			document.add( makePara("Balance forward_______________________________________________________________" ,Element.ALIGN_LEFT));
			document.add( makePara("Submitted for favour of sanction                           " ,Element.ALIGN_LEFT));
			document.add(spacer());
			document.add(spacer());	
			document.add( makePara("A.E.E.Unit "+space1+
			"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t \t \t \t"+"Exe.Eng.Zone....................." ,Element.ALIGN_LEFT));
			document.add(spacer());
			document.add( makePara("Sanctioned" ,Element.ALIGN_CENTER));			
			document.add(spacer());
			document.add(spacer());
			document.add( makePara("DATE:"+space1+
					"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+"Asst.Commissioner Zone..............." ,Element.ALIGN_LEFT));
			document.add(spacer());
			//document.add(spacer());
			document.add( makePara(space1+
					"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+"APPROPRIATION No." ,Element.ALIGN_LEFT));
			document.add(spacer()); 
			document.add( makePara(space1+"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t"+
					"\t  \t  \t  \t \t \t  \t  \t  \t \t \t \t \t \t \t"+"Date:" ,Element.ALIGN_LEFT));
			document.close();
		} catch (DocumentException e) {
			throw new EGOVRuntimeException("estimate.pdf.error",e);
		}catch (EGOVException ex) {
	    	 throw new EGOVRuntimeException("estimate.pdf.error",ex);
	    }
	}
	
	private void setHeader() throws DocumentException{
		HeaderFooter hf=null;
		String projectCode=null;
		String techSanctionNumber="";
		String AdminsanctionNo="";
		if(estimate.getTechSanctionNumber()!=null){
    		techSanctionNumber="Technical Sanction Number : "+estimate.getTechSanctionNumber();
		}
		
		if(estimate.getProjectCode()!=null){
	       	projectCode="Project Code : "+estimate.getProjectCode().getCode();
        	hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t" +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t"+headerText.concat("\n").concat("\t  \t  \t  \t \t \t  \t  \t  \t \t" +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE").concat("\n\n").
                    concat("Name of Work: " + estimate.getName()).concat("\n").concat("Description: " + estimate.getDescription()).
                    concat("\n").concat("Estimate Number: " + estimate.getEstimateNumber()).
                    concat("\n").concat(techSanctionNumber).
                    concat("\n").concat(projectCode).
                    concat("\n").concat("Admin Sanction Number: " +estimate.getAdminsanctionNo())),false); 
         }
        else{
        		hf = new HeaderFooter(new Phrase("\t  \t  \t  \t \t \t  \t  \t  \t \t \t  \t  \t  \t \t" +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t"+headerText.concat("\n").concat("\t  \t  \t  \t \t \t  \t  \t  \t \t" +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  \t \t\t  \t  \t  " +
                    "\t  \t  \t  \t \t\t  \t  \t  \t \t ABSTRACT ESTIMATE").concat("\n\n").
                    concat("Name of Work: " + estimate.getName()).concat("\n").concat("Description: " + estimate.getDescription()).
                    concat("\n").concat("Estimate Number: " + estimate.getEstimateNumber()).
                    concat("\n").concat(techSanctionNumber)),false); 
         }
        	
		
		hf.disableBorderSide(HeaderFooter.TOP);
		hf.disableBorderSide(HeaderFooter.BOTTOM);
		hf.setLeft(HeaderFooter.ALIGN_LEFT);
		document.setHeader(hf); 
	}

	private PdfPTable createMultiYearTable(AbstractEstimate estimate) throws DocumentException {
		PdfPTable multiyearTable = new PdfPTable(3);	
		multiyearTable.setWidthPercentage(100);
		multiyearTable.setWidths(new float[] { 1f, 2f, 2f });
		addRow(multiyearTable, true, makePara("Sl No"),	centerPara("Year"), centerPara("Percentage"));
		int i = 0;
		for (MultiYearEstimate year : estimate.getMultiYearEstimates()) {
			addRow(multiyearTable, true,	makePara(++i), makePara(year.getFinancialYear().getFinYearRange(),Element.ALIGN_CENTER), rightPara(year.getPercentage()));
		}
		return multiyearTable;
	}

	private PdfPTable createOverheadsTable(AbstractEstimate estimate)	throws DocumentException {
		PdfPTable overheadsTable = new PdfPTable(3);
		overheadsTable.setWidthPercentage(100);
		overheadsTable.setWidths(new float[] { 1f, 4f, 2f });
		addRow(overheadsTable, true, makePara("Sl No"),	centerPara("Description"), centerPara("Amount"));
		addRow(overheadsTable, true, makePara("1"),	makePara("Work Value"), rightPara(toCurrency(estimate.getWorkValue())));
		int i = 1;
		for (OverheadValue oh : estimate.getOverheadValues()) {
			addRow(overheadsTable, true,	makePara(++i), makePara(getOverheadDescription(oh)), rightPara(toCurrency(oh.getAmount())));
		}
		addRow(overheadsTable, true, centerPara(""),makePara("TOTAL"), rightPara(toCurrency(estimate.getTotalAmount())));
		return overheadsTable;
	}
	
	  /*
	  1 Deposit Code Appropriation number,2 Deposit Code,3 Account Code,4 Function Center,5 Department,6 Total Deposit Amount,	
	  7 Amount Appropriated so far= totalUtilizedAmt- estimate amt
	  8 Balance on hand = Amount Appropriated so far - estimat amt
	  9 Amount of the Estimate = estimate amt
	  10 Balance after Appropriation of this estimate =balOnHand - ESTIMATE AMT
	  */
	private PdfPTable createDepositAppropriationTable(AbstractEstimate estimate)throws DocumentException,EGOVException,ValidationException {		  
		List<FinancialDetail> financialdetails=estimate.getFinancialDetails();	
		depositWorksUsageService=abstractEstimateService.getDepositWorksUsageService();
		String appropriationNumber=estimate.getBudgetApprNo();
		DepositWorksUsage depositWorksUsage=depositWorksUsageService.getDepositWorksUsage(estimate, appropriationNumber);
		BigDecimal totalUtilizedAmt=BigDecimal.ZERO;
		BigDecimal amtAppropriated=BigDecimal.ZERO;
		BigDecimal totalDepositAmt=BigDecimal.ZERO;
		BigDecimal balOnHand=BigDecimal.ZERO;
		BigDecimal balAftApropriation=BigDecimal.ZERO;
		Accountdetailtype accountdetailtype=worksService.getAccountdetailtypeByName("DEPOSITCODE"); 
				
		PdfPTable depositWorksAppropriationTable = new PdfPTable(2);
		depositWorksAppropriationTable.setWidthPercentage(100);
		depositWorksAppropriationTable.setWidths(new float[] {4f,4f});		
	
		if(estimate.getBudgetApprNo()!=null && estimate.getTotalAmount()!=null){
			for(FinancialDetail financialDetail:financialdetails){
				if(financialDetail.getCoa()!=null) {
					 totalUtilizedAmt=depositWorksUsageService.getTotalUtilizedAmountForDepositWorks(financialDetail);
					 if(totalUtilizedAmt == null){
						 totalUtilizedAmt=BigDecimal.ZERO;
						 
					 }
					 amtAppropriated=new BigDecimal(totalUtilizedAmt.doubleValue()-estimate.getTotalAmount().getValue());
					 totalDepositAmt=depositWorksUsageService.getTotalDepositWorksAmount(estimate.getDepositCode().getFund(), financialDetail.getCoa(), accountdetailtype, estimate.getDepositCode().getId(), depositWorksUsage.getAppropriationDate());
					 if(totalDepositAmt == null){
						 totalDepositAmt=BigDecimal.ZERO;
					 }
					 balOnHand=new BigDecimal(totalDepositAmt.doubleValue()-amtAppropriated.doubleValue());
					 balAftApropriation=new BigDecimal(balOnHand.doubleValue()-estimate.getTotalAmount().getValue());
				     addRow(depositWorksAppropriationTable, true,makePara("Deposit Code Appropriation Number"),makePara(estimate.getBudgetApprNo()));
				     addRow(depositWorksAppropriationTable, true,centerPara("Deposit Code"),centerPara(estimate.getDepositCode().getCode()));
					 addRow(depositWorksAppropriationTable, true,centerPara("Account Code"),centerPara(financialDetail.getCoa().getGlcode()+"-"+financialDetail.getCoa().getName()));	 
					 addRow(depositWorksAppropriationTable, true,makePara("Function Center"),centerPara(financialDetail.getFunction().getName()));
					 addRow(depositWorksAppropriationTable, true,makePara("Department"),centerPara(estimate.getExecutingDepartment().getDeptName()));
					 addRow(depositWorksAppropriationTable, true,makePara("Total Deposit Amount"),rightPara(toCurrency(totalDepositAmt.doubleValue())));
					 addRow(depositWorksAppropriationTable, true,makePara("Amount Appropriated so far"),rightPara(toCurrency(amtAppropriated.doubleValue())));
					 addRow(depositWorksAppropriationTable, true,makePara("Balance on Hand"),rightPara(toCurrency(balOnHand.doubleValue())));
					 addRow(depositWorksAppropriationTable, true,makePara("Amount of this Estimate "),rightPara(toCurrency(estimate.getTotalAmount())));
					 addRow(depositWorksAppropriationTable, true,makePara("Balance After Appropriation of this Estimate"),rightPara(toCurrency(balAftApropriation.doubleValue())));
			  }
		   }
		
	    }
		return depositWorksAppropriationTable;
	  }
	    
	 /*
	  1 Budget head,2 Function Center,3 Department,4 Total grant,	
	  6 Balance on hand = Budget Available - estimat amt
	  5 Amount Appropriated=  oneFifthTimesTotGrant-balOnHand
	  7 Amount of the Estimate = estimate amt
	  8 Balance after Appropriation =balOnHand - ESTIMATE AMT
	  */
	private PdfPTable createBudgetaryAppropriationTable(AbstractEstimate estimate)throws DocumentException,EGOVException,ValidationException {		  
		List<FinancialDetail> financialdetails=estimate.getFinancialDetails();		
		BigDecimal totalGrant=BigDecimal.ZERO;	
		BigDecimal budgetAvailable=BigDecimal.ZERO;	
		BigDecimal balOnHand=BigDecimal.ZERO;
		BigDecimal amtAppropriated=BigDecimal.ZERO;
		BigDecimal balAftApropriation=BigDecimal.ZERO;		
		BigDecimal oneFifthTimesTotGrant=BigDecimal.ZERO;		
		
		
		if(financialdetails!=null && !financialdetails.isEmpty()){
			totalGrant=abstractEstimateService.getTotalGrantForYear(financialdetails.get(0));
			//budgetAvailable=getBudgetAvailable();
			//budgetAvailable=abstractEstimateService.getBudgetAvailable(estimate);
			budgetAvailable=estimate.getBudgetAvailable();
		}
		
		if(budgetAvailable==null)
			budgetAvailable=BigDecimal.ZERO;	

		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(PERCENTAGE_GRANT)))
		 appValue=worksService.getWorksConfigValue(PERCENTAGE_GRANT);
		
		if(StringUtils.isNotEmpty(appValue))
		oneFifthTimesTotGrant=totalGrant.multiply(new BigDecimal(appValue));

		balOnHand=budgetAvailable.add(new BigDecimal(estimate.getTotalAmount().getValue()));
		amtAppropriated=oneFifthTimesTotGrant.subtract(balOnHand);
		//amtAppropriated=totalGrant.subtract(balOnHand);
		balAftApropriation=balOnHand.subtract(new BigDecimal(estimate.getTotalAmount().getValue()));		
		PdfPTable BudgetaryAppropriationTable = new PdfPTable(2);
		BudgetaryAppropriationTable.setWidthPercentage(100);
		BudgetaryAppropriationTable.setWidths(new float[] {4f,4f});		
	
		if(estimate.getBudgetApprNo()!=null && estimate.getTotalAmount()!=null){
			for(FinancialDetail financialDetail:financialdetails){
				if(financialDetail.getBudgetGroup()!=null) {
				addRow(BudgetaryAppropriationTable, true,makePara("Budgetary Appropriation Number"),makePara(estimate.getBudgetApprNo()));
				addRow(BudgetaryAppropriationTable, true,centerPara("Budget Head"),centerPara(financialDetail.getBudgetGroup().getName()));	 
				addRow(BudgetaryAppropriationTable, true,makePara("Function Center"),centerPara(financialDetail.getFunction().getName()));
				addRow(BudgetaryAppropriationTable, true,makePara("Department"),centerPara(estimate.getExecutingDepartment().getDeptName()));
				addRow(BudgetaryAppropriationTable, true,makePara("Total Grant"),rightPara(toCurrency(totalGrant.doubleValue())));
				addRow(BudgetaryAppropriationTable, true,makePara(appValue + " Times Total Grant"),rightPara(toCurrency(oneFifthTimesTotGrant.doubleValue())));
				addRow(BudgetaryAppropriationTable, true,makePara("Amount Appropriated"),rightPara(toCurrency(amtAppropriated.doubleValue())));
				addRow(BudgetaryAppropriationTable, true,makePara("Balance on Hand"),rightPara(toCurrency(balOnHand.doubleValue())));
				addRow(BudgetaryAppropriationTable, true,makePara("Amount of the Estimate "),rightPara(toCurrency(estimate.getTotalAmount())));
				addRow(BudgetaryAppropriationTable, true,makePara("Balance After Appropriation"),rightPara(toCurrency(balAftApropriation.doubleValue())));
			}
		}
	  }
		return BudgetaryAppropriationTable;
	  }
	  
	 
	  
	  private PdfPTable createApprovalDetailsTable(AbstractEstimate estimate)	throws DocumentException {
		try {
			PdfPTable approvaldetailsTable = new PdfPTable(6);
			approvaldetailsTable.setWidthPercentage(100);
			approvaldetailsTable.setWidths(new float[] { 1.5f, 1f, 1.1f, 1.3f,1f,2f });
			addRow(approvaldetailsTable, true, makePara("Approval Step"),	centerPara("Name"), centerPara("Designation"), centerPara("Approved on"), centerPara("Signature"),centerPara("Remarks"));
			
			List<State> history=null;
			if(estimate!=null && estimate.getCurrentState()!=null && estimate.getCurrentState().getHistory()!=null)
			history = estimate.getCurrentState().getHistory();
			
			if(history!=null){
				Collections.reverse(history);
				for (State ad : history){
					displayHistory(ad,approvaldetailsTable, estimate);
				}
			}
			return approvaldetailsTable;
		}catch (Exception e) {
			throw new EGOVRuntimeException("Exception occured while getting approval details "+e);
		}
	} 
	  
	public void displayHistory(State ad,PdfPTable approvaldetailsTable, AbstractEstimate estimate) throws Exception{
		if(!ad.getValue().equals("NEW") && !ad.getValue().equals("END")){
			String nextAction="";
			if(ad.getNextAction()!=null)
				nextAction=ad.getNextAction();
			//EgwStatus status =(EgwStatus) getPersistenceService().find("from EgwStatus where code=?",ad.getValue());
			String state=ad.getValue();
			if(!nextAction.equalsIgnoreCase(""))
				state=ad.getValue()+" - "+nextAction;
			Integer positionId =null;
			String desgName=null;
			if(ad.getPrevious()==null){
				
				positionId = ad.getOwner().getId();
				/*DeptDesig deptdesig= ad.getOwner().getDeptDesigId();
				desgName = deptdesig.getDesigId().getDesignationName();*/
				desgName = ad.getOwner().getDesigId().getDesignationName();
			}
			else{
				
				positionId =ad.getPrevious().getOwner().getId();
				/*DeptDesig deptdesig= ad.getPrevious().getOwner().getDeptDesigId();
				desgName = deptdesig.getDesigId().getDesignationName();*/
				desgName = ad.getPrevious().getOwner().getDesigId().getDesignationName();
				
			}
			PersonalInformation emp = null;
			try
			{
				emp=employeeService.getEmpForPositionAndDate(ad.getCreatedDate(), positionId);
				if(emp.getUserMaster().getUserSignature()!=null){
					Image image=Image.getInstance(CryptoHelper.decrypt(emp.getUserMaster().getUserSignature().getSignature(), CryptoHelper.decrypt(emp.getUserMaster().getPwd())));
					if(image.getScaledHeight()>50 || image.getScaledWidth()>150){
						image.scaleToFit(50, 150);
					}
					PdfPCell sign=new PdfPCell(image);
					sign.setHorizontalAlignment(Element.ALIGN_CENTER);
					addRowWithSignature(approvaldetailsTable, true,new PdfPCell(makePara(state)), new PdfPCell(makePara(emp.getEmployeeName())),new PdfPCell(makePara(desgName)),new PdfPCell(makePara(getDateInFormat(ad.getCreatedDate().toString()))),sign,new PdfPCell(rightPara(ad.getText1())));
				}else{
					addRowWithSignature(approvaldetailsTable, true,new PdfPCell(makePara(state)), new PdfPCell(makePara(emp.getEmployeeName())),new PdfPCell(makePara(desgName)),new PdfPCell(makePara(getDateInFormat(ad.getCreatedDate().toString()))),new PdfPCell(makePara(" ")),new PdfPCell(rightPara(ad.getText1())));
			}
			}
			catch(Exception e)
			{
				//Code review changes implemented. Dynamic values in the message are added, so we cant get message from package.properties here
				throw new EGOVRuntimeException(" The position  "+ad.getOwner().getName()+" does not have any employee attached to it in "+ estimate.getExecutingDepartment().getDeptName());
			}

			
	}
	}
	  
	
	
	private String getDateInFormat(String date) throws DocumentException
	{
		String dateInFormat=null;
		try {
			dateInFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH).format(new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).
					parse(date));
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception occured while parsing date := "+date+ e);
		}
		return dateInFormat;	
	}
	
	public static StringBuffer numberFormat(final String strNumberToConvert) {
        String strNumber="",signBit="";
        if(strNumberToConvert.startsWith("-")) {
            strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
            signBit="-";
        }
        else strNumber=""+strNumberToConvert;
        DecimalFormat dft = new DecimalFormat("##############0.#####");
        String strtemp=""+dft.format(Double.parseDouble(strNumber));
        StringBuffer strbNumber=new StringBuffer(strtemp);
       if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
        return strbNumber;
    }

	private PdfPTable createActivitiesTable(AbstractEstimate estimate)
			throws DocumentException {
		PdfPTable activitiesTable = new PdfPTable(7);
		activitiesTable.setWidthPercentage(100);
		activitiesTable.setWidths(new float[] { 0.5f, 1f, 3.1f, 1.2f, 0.8f, 1.1f, 1.5f});
		addRow(activitiesTable, true, makePara("Sl No"),centerPara("Quantity"), centerPara("Description"),centerPara("Sch. No"),
				 centerPara("Unit"), centerPara("Rate"),centerPara("Amount"));
		Collection<Activity> activities = estimate.getSORActivities();
		int index=1;
		for (Activity activity : activities) {
			String estimateUom="";
			if(activity.getUom()==null)
				estimateUom=activity.getSchedule().getUom().getUom();
			else
				estimateUom=activity.getUom().getUom();
			addRow(activitiesTable, true, makePara(index++),rightPara(numberFormat(Double.toString(activity.getQuantity())).toString()), makePara(activity.getSchedule().getDescription()), centerPara(activity.getSchedule().getCode()), 
					 centerPara(estimateUom), rightPara(activity.getSORCurrentRate()), rightPara(toCurrency(activity.getAmount())));
		}
		activities = estimate.getNonSORActivities();
		for (Activity activity : activities) {
			addRow(activitiesTable, true, makePara(index++),rightPara(numberFormat(Double.toString(activity.getQuantity())).toString()),makePara(activity.getNonSor().getDescription()),centerPara(""), 
					 centerPara(activity.getNonSor().getUom().getUom()), rightPara(activity.getRate()),
					rightPara(toCurrency(activity.getAmount())));
		}
		addRow(activitiesTable, true, centerPara(""),centerPara(""), makePara(""),
				rightPara(""), centerPara(""), centerPara("TOTAL"),rightPara(toCurrency(estimate.getWorkValue())));
		addRow(activitiesTable, true, centerPara(""),centerPara(""), makePara(""),
				rightPara(""), centerPara(""), centerPara("WORK VALUE"),rightPara(toCurrency(estimate.getWorkValueIncludingTaxes())));
		
		return activitiesTable;
	}
	
	private PdfPTable createREActivitiesTable(AbstractEstimate estimate)
	throws DocumentException {
		PdfPTable reActivitiesTable = new PdfPTable(8);
		reActivitiesTable.setWidthPercentage(100);
		reActivitiesTable.setWidths(new float[] { 0.5f, 1f, 3.1f, 1.2f,1.2f,1.0f, 1.1f, 1.5f});
		addRow(reActivitiesTable, true, makePara("Sl No"),centerPara("Quantity"), centerPara("Description"),centerPara("Sch. No"),centerPara("Revision Type"),
				 centerPara("Unit"), centerPara("Rate"),centerPara("Amount"));
		Collection<Activity> activities = estimate.getSORActivities();
		
		
		int index=1;
		for (Activity activity : activities) {
			String estimateUom="";
			if(activity.getUom()==null)
				estimateUom=activity.getSchedule().getUom().getUom();
			else
				estimateUom=activity.getUom().getUom();
			addRow(reActivitiesTable, true, makePara(index++),rightPara(numberFormat(Double.toString(activity.getQuantity())).toString()), makePara(activity.getSchedule().getDescription()), centerPara(activity.getSchedule().getCode()), centerPara(activity.getRevisionType()),
					 centerPara(estimateUom), rightPara(activity.getSORCurrentRate()), rightPara(toCurrency(activity.getAmount())));
		}
		activities = estimate.getNonSORActivities();
		for (Activity activity : activities) {
			addRow(reActivitiesTable, true, makePara(index++),rightPara(numberFormat(Double.toString(activity.getQuantity())).toString()),makePara(activity.getNonSor().getDescription()),centerPara(""),centerPara(activity.getRevisionType()), 
					 centerPara(activity.getNonSor().getUom().getUom()), rightPara(activity.getRate()),
					rightPara(toCurrency(activity.getAmount())));
		}
		addRow(reActivitiesTable, true, centerPara(""),centerPara(""), makePara(""),
				rightPara(""), centerPara(""),centerPara(""), centerPara("TOTAL"),rightPara(toCurrency(estimate.getWorkValue())));
		addRow(reActivitiesTable, true, centerPara(""),centerPara(""), makePara(""),
				rightPara(""), centerPara(""),centerPara(""),centerPara("WORK VALUE"),rightPara(toCurrency(estimate.getWorkValueIncludingTaxes())));
		
		return reActivitiesTable; 
	}

	private String getOverheadDescription(OverheadValue oh) {
		if(oh.getOverhead().getOverheadRates().get(0).getPercentage()>0){
		return oh.getOverhead().getDescription()+'-'+oh.getOverhead().getOverheadRates().get(0).getPercentage()+'%';
		}else{
			return oh.getOverhead().getDescription();	
		}
	}
	private void addZoneYearHeader(AbstractEstimate estimate,CFinancialYear financialYear) throws DocumentException {
		document.add(spacer());
		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setWidthPercentage(100);
		headerTable.setWidths(new float[] { 1f,1f});
		Paragraph financialYearPara = new Paragraph();
		Paragraph rateYearPara = new Paragraph();
		if(financialYear!=null){
			financialYearPara=makePara("Budget Year: " +financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
			//rateYearPara=makePara("Rate: " + financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
			rateYearPara=makePara("Schedule Rate Year: "+getSORYear(), Element.ALIGN_RIGHT);
		}
		addRow(headerTable, false,makePara("Estimate Number: " +estimate.getEstimateNumber(), Element.ALIGN_LEFT), financialYearPara);
		addRow(headerTable, false,new Paragraph(), rateYearPara);
		document.add(headerTable);
		if(estimate.getWard()!=null && "Ward".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName()) && estimate.getWard().getParent()!=null && estimate.getWard().getParent().getName() !=null){
			document.add(makePara("Ward: " + estimate.getWard().getName()+ " / Zone: " + estimate.getWard().getParent().getName(),
					Element.ALIGN_RIGHT));
		}else if(estimate.getWard()!=null){
			document.add(makePara("Jurisdiction: " + estimate.getWard().getName()+ "(" + estimate.getWard().getBoundaryType().getName()+ ")",
					Element.ALIGN_RIGHT));
		}
		document.add(spacer());
	}

	private void addZoneYearHeaderWithOutEstimateNo(AbstractEstimate estimate,CFinancialYear financialYear) throws DocumentException {
		PdfPTable headerTable = new PdfPTable(2);
		headerTable.setWidthPercentage(100);
		headerTable.setWidths(new float[] { 1f,1f});
		Paragraph financialYearPara = new Paragraph();
		Paragraph rateYearPara = new Paragraph();
		if(financialYear!=null){
			financialYearPara=makePara("Budget Year: " +financialYear.getFinYearRange(), Element.ALIGN_RIGHT);
			rateYearPara=makePara("Schedule Rate Year: "+getSORYear(), Element.ALIGN_RIGHT);
		}
		addRow(headerTable, false,new Paragraph(), financialYearPara);
		addRow(headerTable, false,new Paragraph(), rateYearPara);
		document.add(headerTable);
		if(estimate.getWard()!=null && "Ward".equalsIgnoreCase(estimate.getWard().getBoundaryType().getName()) && estimate.getWard().getParent()!=null && estimate.getWard().getParent().getName() !=null){
			document.add(makePara("Ward: " + estimate.getWard().getName()+ " / Zone: " + estimate.getWard().getParent().getName(),
					Element.ALIGN_RIGHT));
		}else if(estimate.getWard()!=null){
			document.add(makePara("Jurisdiction: " + estimate.getWard().getName()+ "(" + estimate.getWard().getBoundaryType().getName()+ ")",
					Element.ALIGN_RIGHT));
		}
		document.add(spacer());
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public BudgetDetailsDAO getBudgetDetailsDAO() {
		return budgetDetailsDAO;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	private String getSORYear() {
		String year="";
		List<AppConfigValues> appConfigList = worksService.getAppConfigValue("Works","SCHEDULE_RATE_YEAR");
		for(AppConfigValues configValue:appConfigList){
			String value[] = configValue.getValue().split(",");
			String date[] = value[0].split("-");
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","IN"));
			try {
				Date startDate=df.parse(date[0]);
				Date endDate=df.parse(date[1]);
				Date estDate=df.parse(df.format(estimate.getEstimateDate()));
				if(DateConversionUtil.isWithinDateRange(estDate, startDate, endDate))
					year=value[1];
			}
			catch (ParseException pe) {
				logger.error("Error in parsing date"+pe.getMessage());
			}			
		}
		return year;
	}

  public List<String> getAppConfigValuesToSkipBudget(){
		return worksService.getNatureOfWorkAppConfigValues(MODULE_NAME, KEY_NAME);
	 }

  public Boolean isSkipBudgetCheck(){
		List<String> depositTypeList=getAppConfigValuesToSkipBudget();
		if(estimate!=null && estimate.getId()!=null){
			for(String type:depositTypeList){
				if(type.equals(estimate.getType().getName())){
					skipBudget=true;
				}
			}
		}	
		return skipBudget;
	}

  public List<BudgetUsage> getBudgetDetailUsage(String estimateNumber){
	   List<BudgetUsage> budgetUsageList=(List<BudgetUsage>) persistenceService.findAllBy("from BudgetUsage bu where bu.referenceNumber=?", estimateNumber);
	   return budgetUsageList;
}
  
}
