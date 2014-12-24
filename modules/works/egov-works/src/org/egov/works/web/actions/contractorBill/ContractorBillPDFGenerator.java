package org.egov.works.web.actions.contractorBill;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.State;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.NumberToWord;
import org.egov.model.bills.EgBilldetails;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.models.contractorBill.ContractorBillRegister;
import org.egov.works.models.contractorBill.DeductionTypeForBill;
import org.egov.works.models.contractorBill.StatutoryDeductionsForBill;
import org.egov.works.models.measurementbook.MBForCancelledBill;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.services.ContractorBillService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class ContractorBillPDFGenerator extends AbstractPDFGenerator{	
	private static final Logger logger = Logger.getLogger(ContractorBillPDFGenerator.class);
	private PersistenceService persistenceService = new PersistenceService();
	private EmployeeService employeeService;
	private final Map<String, String> pdfLabel;
	private final ContractorBillRegister egBillRegister;
	
	private final MBHeader mbHeader;
	private String deptName="";
	private String contactorName="";
	private String contractorAddress="";
	private String billNumber="";
	private String billGenNumber="";
	private String billDate="";
	private String billType="";
	private String workDescription="";
	private String workcommencedOn="";
	private String workCompletedOn="";
	private String estimateNumber="";
	private String projectCode="";
	public static final String newLine="\n";
	private Long workOrderId;

	private List<MBHeader> mbHeaderList=new ArrayList<MBHeader>();
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	private boolean flag=false;
	public static final String CONTRACTOR_PDF_ERROR="egBillRegister.pdf.error";
	private final ContractorBillService contractorBillService;
	
	public static final String blankSpace="   ";

	public static final String blankSpace8="        ";
	public static final String blankSpace15="               ";
	public static final String blankSpace20="                    ";
	public static final String tab2="\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t";
	public static final String dateLabel="contractorbill.pdf.date";
	private List<StatutoryDeductionsForBill> sortedStatutorySortedList;
	private List<DeductionTypeForBill> sortedStandardDeductionList;
	private List<EgBilldetails> customDeductionList;	
	private List<AssetForBill> assetForBillList;	
	private List<EgBilldetails> releaseWithHeldList;
	private CommonsService commonsService;
	private WorksService worksService;
	private BigDecimal advanceAdjustment;
	private List<BigDecimal> glcodeIdList;
	private List<BigDecimal> retentionMoneyglcodeIdList ;
	private static final String WORKS_NETPAYABLE_CODE ="WORKS_NETPAYABLE_CODE";
	private BigDecimal netPayableAmount=BigDecimal.ZERO;
	private static final String RETENTION_MONEY_PURPOSE="RETENTION_MONEY_PURPOSE";
	
	public ContractorBillPDFGenerator(ContractorBillRegister egBillRegister, MBHeader mbHeader, OutputStream out,Map<String,String> pdfLabel,ContractorBillService contractorBillService){
		super(out, "landscape");		
		this.pdfLabel=pdfLabel;
		this.egBillRegister = egBillRegister;		
		this.mbHeader=mbHeader;
		this.contractorBillService=contractorBillService;
		 
	}
	
	public void generatePDF() throws EGOVException,IOException{
		logger.debug("FA1---inside generate pdf ");
		generateDisplayData(mbHeader,egBillRegister);		
		try{
			// start header Part
			PdfPTable contractorBillMainTable = new PdfPTable(11);
			contractorBillMainTable.setWidthPercentage(100);
			//contractorBillMainTable.setWidths(new float[] {2f,2f,2f,2f,2f,2f,2f,2f,2f,2f});			
			contractorBillMainTable.setWidths(new float[] {1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f,1.5f});
			contractorBillMainTable.getDefaultCell().setPadding(4);
			contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
			createHeaderRow(contractorBillMainTable);
			createDetailsRows(contractorBillMainTable);			
			document.add(contractorBillMainTable);
			document.add(spacer());			
			
			//---approval details for workflow		
			PdfPTable approvaldetailsTable = createApprovalDetailsTable(egBillRegister);
			
			if (approvaldetailsTable.getRows().size()!=1) {
				document.add(makePara("Approval Details"));
				document.add(spacer());
				document.add(approvaldetailsTable);
				document.add(spacer());
			}	
			if(contractorBillMainTable!=null && contractorBillMainTable.getRows().size()>11)
				document.newPage();			
			createFooter();			
			//create certificate page
			document.newPage();
			createCertificate();			
			document.close();			
		}catch (DocumentException e) {
			throw new EGOVRuntimeException(CONTRACTOR_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	 throw new EGOVRuntimeException(CONTRACTOR_PDF_ERROR,ex);
	    }		
	}	
	
	protected void createCertificate()throws DocumentException {
		Paragraph headerTextPara = new Paragraph(new Chunk(pdfLabel.get("contractorbill.pdf.contractorbill"),new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD)));
		headerTextPara.setAlignment(Element.ALIGN_CENTER);
		document.add(headerTextPara);		
		Paragraph certificateheaderTextPara = new Paragraph(new Chunk(pdfLabel.get("contractorbill.pdf.certificate"),new Font(Font.COURIER, LARGE_FONT, Font.BOLD)));
		certificateheaderTextPara.setAlignment(Element.ALIGN_CENTER);
		document.add(certificateheaderTextPara);
		document.add(spacer());
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent1")));
		document.add(spacer());
		document.add(spacer());
		
		document.add(rightPara(pdfLabel.get("contractorbill.pdf.juniorengineer")+"\t \t\t \t \t \t\t \t\t \t \t \t \t"));
		document.add(spacer());
		document.add(rightPara(pdfLabel.get(dateLabel)+"\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t"+
				 									"\t \t\t \t\t \t \t \t \t\t \t\t\t\t \t\t \t\t"));
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent2")));
		document.add(spacer());
		document.add(spacer());
		
		document.add(rightPara(pdfLabel.get("contractorbill.pdf.exeasstengineer")+"\t \t"));
		document.add(spacer());
		document.add(rightPara(pdfLabel.get(dateLabel)+"\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t"+
				"\t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t\t \t \t\t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t \t \t \t \t\t \t\t"));
		
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent3")));
		document.add(spacer());
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent4")));
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent5")));
		document.add(spacer());
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.certificatecontent6")));

		document.add(spacer());
		document.add(spacer());
		
		document.add(makePara(pdfLabel.get("contractorbill.pdf.juniorengineer")+"\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t "+
				tab2+
				tab2+
				"\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t"+
				pdfLabel.get("contractorbill.pdf.exeasstengineer")));
		document.add(spacer());
		document.add(makePara(pdfLabel.get(dateLabel)+"\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t"+
				tab2+
				tab2+
				"\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t\t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t \t"+
				"\t \t \t \t \t \t \t \t "+
				pdfLabel.get(dateLabel)));
	}
	
	protected void createFooter() throws DocumentException{
		document.add(makePara("Received( Rs---------------) Rupees-------------------------------------------------------------------------------------------\n"+
				"only as a final payment in settlement of all demands in( Vernacular)---------------------------------------------------\n"+
				"Witness:-   1.\n"+
				"                      2."));			
				logger.debug("FC---inside generate pdf add document");		
	}
	
	//1---header part of code 	
	protected void createHeaderRow(PdfPTable contractorBillMainTable) throws DocumentException, EGOVException{
		PdfPTable contractorBillLeftHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.leftheader"),0);	
		contractorBillLeftHeader.getDefaultCell().setBorderWidth(0);
		PdfPCell contractorBillLeftHeaderCell = new PdfPCell(contractorBillLeftHeader);			
		contractorBillLeftHeaderCell.setBorderWidth(0);
		contractorBillLeftHeaderCell.setColspan(4);
		contractorBillMainTable.addCell(contractorBillLeftHeaderCell);		
		PdfPTable contractorBillMainHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.mainheader"),1);
		contractorBillMainHeader.getDefaultCell().setBorderWidth(0);
		PdfPCell contractorBillMainHeaderCell = new PdfPCell(contractorBillMainHeader);
		contractorBillMainHeaderCell.setBorderWidth(0);
		contractorBillMainHeaderCell.setColspan(3);
		contractorBillMainTable.addCell(contractorBillMainHeaderCell);	
		PdfPTable contractorBillRightHeader = createContractorBillHeader(pdfLabel.get("contractorbill.pdf.rightheader"),2);		
		contractorBillMainHeader.getDefaultCell().setBorderWidth(0);
		PdfPCell contractorBillRightHeaderCell = new PdfPCell(contractorBillRightHeader);		
		contractorBillRightHeaderCell.setBorderWidth(0);
		contractorBillRightHeaderCell.setColspan(4);
		contractorBillMainTable.addCell(contractorBillRightHeaderCell);
	}
	
	protected PdfPTable createContractorBillHeader(String title,int i)throws DocumentException,EGOVException{		
		PdfPTable contractorBillHeaderTable =new PdfPTable(3);
		contractorBillHeaderTable.getDefaultCell().setBorderWidth(0);		
		if(i==0){			
			contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			contractorBillHeaderTable.getDefaultCell().setColspan(4);
			contractorBillHeaderTable.addCell(title);			
		}else if(i==1){
			contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);			
			Phrase headerTextPara=new Phrase(title, new Font(Font.UNDEFINED, LARGE_FONT, Font.BOLD));		
			contractorBillHeaderTable.getDefaultCell().setColspan(3);
			contractorBillHeaderTable.addCell(headerTextPara);			
		}else if(i==2){		
			contractorBillHeaderTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);		
			contractorBillHeaderTable.getDefaultCell().setColspan(4);
			contractorBillHeaderTable.addCell(title +" "+deptName);			
		}		
		return contractorBillHeaderTable;		
	}
	
	// def creatreDetailsRow(contractorBillMainTable)	
	 protected void createDetailsRows(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException,IOException{	
		 createContractorRow(contractorBillMainTable);		 
		 createWorkDescRow(contractorBillMainTable);		 
		 createDetailsForWorkOrder(contractorBillMainTable);		 //project code row
		 createWorkValueLabel(contractorBillMainTable);	//value of work done row
		 createWorkValueData(contractorBillMainTable);		
		 createReleaseWithHeldAmountData(contractorBillMainTable);
		 createDeductionTypeLabel(contractorBillMainTable);	//deductions label row
		 createDeductionTypeData(contractorBillMainTable);   //deductions data row
		 
		 createNetPayable(contractorBillMainTable);
	 }
	 
	 
	 // row7   createDeductionTypeLabel	 
	 protected void createDeductionTypeLabel(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable deductionTypeTable = createDeductionTypeLabelTable(contractorBillMainTable);
		deductionTypeTable.getDefaultCell().setBorderWidth(1);
		PdfPCell deductionTypeCell = new PdfPCell(deductionTypeTable);			
		deductionTypeCell.setColspan(11);
		contractorBillMainTable.addCell(deductionTypeCell);			 
	 }
	 
	 protected PdfPTable createDeductionTypeLabelTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		PdfPTable deductionTypeLabel =new PdfPTable(11);
		deductionTypeLabel.getDefaultCell().setBorderWidth(1);
		deductionTypeLabel.getDefaultCell().setColspan(6);
		deductionTypeLabel.addCell(makePara(pdfLabel.get("contractorbill.pdf.deductions")));	
		deductionTypeLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		deductionTypeLabel.getDefaultCell().setColspan(1);		
		deductionTypeLabel.addCell(makePara(pdfLabel.get("contractorbill.pdf.percentage")));
		deductionTypeLabel.getDefaultCell().setColspan(1);		
		deductionTypeLabel.addCell("");	
		deductionTypeLabel.addCell("");	
		deductionTypeLabel.addCell("");	
		deductionTypeLabel.addCell("");	
		return deductionTypeLabel;		
	 }
	 
	 // row8  createDeductionTypeData
	 protected void createDeductionTypeData(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable createDeductionTypeDataTable = createDeductionTypeDataTable(contractorBillMainTable);
		createDeductionTypeDataTable.getDefaultCell().setBorderWidth(1);
		PdfPCell createWorkValueDataCell = new PdfPCell(createDeductionTypeDataTable);			
		createWorkValueDataCell.setColspan(11);
		contractorBillMainTable.addCell(createWorkValueDataCell);
	 }
	 
	 protected PdfPTable createDeductionTypeDataTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		PdfPTable createcreateDeductionTypeDataTable =new PdfPTable(11);
		createcreateDeductionTypeDataTable.getDefaultCell().setBorderWidth(1);

		//statutory
		if(!sortedStatutorySortedList.isEmpty()){
			for(StatutoryDeductionsForBill egBillPayeedetail:sortedStatutorySortedList){
				//get tot amt for dedcution for all bill for workorder till bill date
				BigDecimal totStatutoryAmt=getTotStatoryAmountForDeduction(egBillPayeedetail);
				BigDecimal creditAmount = egBillPayeedetail.getEgBillPayeeDtls().getCreditAmount();
				String resultTotStatuAmt=getIntDecimalParts(totStatutoryAmt);
				String[] resultTotStatuAry=resultTotStatuAmt.split(":");	
				
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				String resultAmt=getIntDecimalParts(creditAmount);
				String[] resultAry=resultAmt.split(":");				
				BigDecimal percentage = createPercentageDataTable(creditAmount);
				String percent=getIntDecimalParts(percentage);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(6);
				createcreateDeductionTypeDataTable.addCell(egBillPayeedetail.getEgBillPayeeDtls().getRecovery().getType());			
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(percent.replace(":", "."));//Percentage of Deduction
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(resultTotStatuAry[0]);// Rs. amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultTotStatuAry[1]);//Pa, amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultAry[0]); //Rs. amt for this deduction specific to bill	
				createcreateDeductionTypeDataTable.addCell(resultAry[1]);//pa. amt for this deduction specific to bill
			}
		}
		String type="advanceAjustment";
		if("advanceAjustment".equalsIgnoreCase(type)){			
			BigDecimal totAmt=getTotAmountForAdvanceAdjustment();			
			String resultTotAmt=getIntDecimalParts(totAmt);
			String[] resultTotAry=resultTotAmt.split(":");	
			createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			String resultAmt=getIntDecimalParts(advanceAdjustment);
			String[] resultAry=resultAmt.split(":");
			BigDecimal percentage = createPercentageDataTable(totAmt);
			String percent=getIntDecimalParts(percentage);
			createcreateDeductionTypeDataTable.getDefaultCell().setColspan(6);
			createcreateDeductionTypeDataTable.addCell("Advance adjustment ");	
			createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
			createcreateDeductionTypeDataTable.addCell(percent.replace(":", "."));//Percentage of Deduction
			createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
			
			createcreateDeductionTypeDataTable.addCell(resultTotAry[0]);	
			createcreateDeductionTypeDataTable.addCell(resultTotAry[1]);
			
			createcreateDeductionTypeDataTable.addCell(resultAry[0]); //Rs. amt for this deduction specific to bill	
			createcreateDeductionTypeDataTable.addCell(resultAry[1]);//pa. amt for this deduction specific to bill	
		}
		
		//standard deduction		
		if(!sortedStandardDeductionList.isEmpty()){
			for(DeductionTypeForBill deductionTypeForBill:sortedStandardDeductionList){					
				BigDecimal totStandardAmt=getTotStandardAmountForDeduction(deductionTypeForBill);
				BigDecimal creditAmount = deductionTypeForBill.getCreditamount();
				String resultTotStandardAmt=getIntDecimalParts(totStandardAmt);
				String[] resultTotStandardAry=resultTotStandardAmt.split(":");	
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				String resultAmt=getIntDecimalParts(creditAmount);
				String[] resultAry=resultAmt.split(":");				
				BigDecimal percentage = createPercentageDataTable(creditAmount);
				String percent = getIntDecimalParts(percentage);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(6);
				createcreateDeductionTypeDataTable.addCell(deductionTypeForBill.getDeductionType());	
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(percent.replace(":", "."));//Percentage of Deduction
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(resultTotStandardAry[0]);// Rs. amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultTotStandardAry[1]);//Pa, amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultAry[0]); //Rs. amt for this deduction for this bill	
				createcreateDeductionTypeDataTable.addCell(resultAry[1]);//Pa. amt for this deduction for this bill	
			}
		}
		
		if(!customDeductionList.isEmpty()){
			for(EgBilldetails egBilldetails:customDeductionList){
				BigDecimal totCustomAmt=getTotStandardAmountForDeduction(egBilldetails);
				BigDecimal creditAmount = egBilldetails.getCreditamount();
				String resultTotCustomAmt=getIntDecimalParts(totCustomAmt);
				String[] resultTotCustomAry=resultTotCustomAmt.split(":");	
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				String resultAmt=getIntDecimalParts(egBilldetails.getCreditamount());
				String[] resultAry=resultAmt.split(":");				
				BigDecimal percentage = createPercentageDataTable(creditAmount);
				String percent = getIntDecimalParts(percentage);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(6);
				createcreateDeductionTypeDataTable.addCell(commonsService.getCChartOfAccountsById(Long.valueOf(egBilldetails.getGlcodeid().toString())).getName());
				createcreateDeductionTypeDataTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(percent.replace(":", "."));//Percentage of Deduction
				createcreateDeductionTypeDataTable.getDefaultCell().setColspan(1);	
				createcreateDeductionTypeDataTable.addCell(resultTotCustomAry[0]);// Rs. amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultTotCustomAry[1]);//Pa, amt all bill for workorder till billdate	
				createcreateDeductionTypeDataTable.addCell(resultAry[0]); //amt for this deduction specific to bill	
				createcreateDeductionTypeDataTable.addCell(resultAry[1]);		
			}
		}
		
		return createcreateDeductionTypeDataTable;		
	}
	 
	 /**
	  * @author Sangamesh
	  * To Calculate percentage for all deductions
	  * @param BigDecimal creditAmount
	  * @return percentage
	  */
	 protected BigDecimal createPercentageDataTable(BigDecimal creditAmount){
		 Double amount = creditAmount.doubleValue();
		 // Previously total amount(excluding withheld amount) was considered as the base
		 //BigDecimal totalBillAmtUptBill=contractorBillService.getWoValueExcludingWitheldReleaseAmt(egBillRegister.getBilldate(), workOrderId,mbHeader.getWorkOrderEstimate().getId());
		 //Double totalBillAmount = totalBillAmtUptBill.doubleValue();
		 Double totalBillAmount = egBillRegister.getWorkRecordedAmount().doubleValue();
		 Double percent = (amount * 100.0)/totalBillAmount;
		 BigDecimal percentage = new BigDecimal(percent);
		 return percentage;
	 }
	 
	 //row 9th
	 protected void createNetPayable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{	
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);		
		PdfPTable createNetPayableTable = createNetPayableTable(contractorBillMainTable);
		createNetPayableTable.getDefaultCell().setBorderWidth(1);
		PdfPCell createNetPayableCell = new PdfPCell(createNetPayableTable);			
		createNetPayableCell.setColspan(11);		
		contractorBillMainTable.addCell(createNetPayableCell);
	 }
	 
	 protected PdfPTable createNetPayableTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{
	 	String resultAmt=getIntDecimalParts(netPayableAmount);
		String[] resultAry=resultAmt.split(":");			
		PdfPTable createNetPayableData =new PdfPTable(11);
		createNetPayableData.getDefaultCell().setBorderWidth(1);
		createNetPayableData.getDefaultCell().setColspan(9);
		createNetPayableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		createNetPayableData.addCell(makePara(pdfLabel.get("contractorbill.pdf.netamount")+":\t"+getNetPayAmtInWords(),Font.UNDERLINE));
		createNetPayableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		createNetPayableData.getDefaultCell().setColspan(1);
		createNetPayableData.addCell(resultAry[0]);
		createNetPayableData.addCell(resultAry[1]);
		return createNetPayableData;		
	}
	 
	// row6  createWorkValueData
	 protected void createReleaseWithHeldAmountData(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable createReleaseWithHeldAmountDataTable = createReleaseWithHeldAmountDataTable(contractorBillMainTable);
		createReleaseWithHeldAmountDataTable.getDefaultCell().setBorderWidth(1);
		PdfPCell createReleaseWithHeldAmountDataTableCell = new PdfPCell(createReleaseWithHeldAmountDataTable);			
		createReleaseWithHeldAmountDataTableCell.setColspan(11);
	 
		contractorBillMainTable.addCell(createReleaseWithHeldAmountDataTableCell);
	 }
	 
	 protected PdfPTable createReleaseWithHeldAmountDataTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
			PdfPTable createWithHoldReleaseData =new PdfPTable(11);
			if(!releaseWithHeldList.isEmpty()){
				for(EgBilldetails egBilldetails:releaseWithHeldList){
					BigDecimal totCustomAmt=getTotalReleasedWithHeldAmount(egBilldetails);
					String resultTotCustomAmt=getIntDecimalParts(totCustomAmt);
					String[] resultTotCustomAry=resultTotCustomAmt.split(":");	
					createWithHoldReleaseData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					String resultAmt=getIntDecimalParts(egBilldetails.getDebitamount());
					String[] resultAry=resultAmt.split(":");				
					createWithHoldReleaseData.getDefaultCell().setColspan(7);
					createWithHoldReleaseData.addCell(makePara(pdfLabel.get("contractorbill.pdf.withholdrelease")));
					createWithHoldReleaseData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					createWithHoldReleaseData.getDefaultCell().setColspan(1);	
					createWithHoldReleaseData.addCell(resultTotCustomAry[0]);// Rs. amt all bill for workorder till billdate	
					createWithHoldReleaseData.addCell(resultTotCustomAry[1]);//Pa, amt all bill for workorder till billdate	
					createWithHoldReleaseData.addCell(resultAry[0]); //amt for this deduction specific to bill	
					createWithHoldReleaseData.addCell(resultAry[1]);		
				}
			}
			return createWithHoldReleaseData;		
	}
	 
	 
	 // row6  createWorkValueData
	 protected void createWorkValueData(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable createWorkValueDataTable = createWorkValueDataTable(contractorBillMainTable);
		createWorkValueDataTable.getDefaultCell().setBorderWidth(1);
		PdfPCell createWorkValueDataCell = new PdfPCell(createWorkValueDataTable);			
		createWorkValueDataCell.setColspan(11);

		contractorBillMainTable.addCell(createWorkValueDataCell);
	 }
	 
	 protected PdfPTable createWorkValueDataTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
			PdfPTable createWorkValueData =new PdfPTable(11);
			createWorkValueData.getDefaultCell().setBorderWidth(1);
			createWorkValueData.getDefaultCell().setColspan(7);
			createWorkValueData.addCell(makePara(pdfLabel.get("contractorbill.pdf.valueofworkdone")));
				
			createWorkValueData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			createWorkValueData.getDefaultCell().setColspan(1);	
			BigDecimal totalBillAmtUptBill=contractorBillService.getWoValueExcludingWitheldReleaseAmt(egBillRegister.getBilldate(), workOrderId,mbHeader.getWorkOrderEstimate().getId());
			
										
			if(totalBillAmtUptBill.compareTo(BigDecimal.ZERO) > 0){
				 String totalBillAmt=toCurrency(totalBillAmtUptBill.doubleValue());
				 try{
					 createWorkValueData.addCell(rightPara(blankSpace+totalBillAmt.substring(0, totalBillAmt.indexOf('.'))));	
					 createWorkValueData.addCell(centerPara(blankSpace+totalBillAmt.substring(totalBillAmt.indexOf('.')+1,totalBillAmt.length())));	
				 }catch(StringIndexOutOfBoundsException e){
						createWorkValueData.addCell(centerPara(blankSpace+totalBillAmt));
						createWorkValueData.addCell("00");
				}
				 
			}else{
				 createWorkValueData.addCell(" ");	
				 createWorkValueData.addCell(" ");	
	 		}
				 
				 
				 
			BigDecimal billAmount=BigDecimal.ZERO;
			
			if(egBillRegister!=null && egBillRegister.getWorkRecordedAmount()!=null)
				billAmount=egBillRegister.getWorkRecordedAmount();
							
			if(billAmount.compareTo(BigDecimal.ZERO) > 0){		
				 String billAmt= toCurrency(billAmount.doubleValue());					
				try{
				 createWorkValueData.addCell(centerPara(blankSpace+billAmt.substring(0, billAmt.indexOf('.'))));
				 createWorkValueData.addCell(centerPara(blankSpace+billAmt.substring(billAmt.indexOf('.')+1,billAmt.length())));
				}catch(StringIndexOutOfBoundsException e){
					createWorkValueData.addCell(centerPara(blankSpace+billAmt));
					createWorkValueData.addCell("");
				}
			}else{
				 createWorkValueData.addCell(" ");	
				 createWorkValueData.addCell(" ");	
	 		}
			return createWorkValueData;		
	}
	 	 
	 // row5   createWorkValueLabe	 
	 protected void createWorkValueLabel(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable WorkValueLabelTable = createWorkValueLabelTable(contractorBillMainTable);
		WorkValueLabelTable.getDefaultCell().setBorderWidth(1);
		PdfPCell WorkValueLabelCell = new PdfPCell(WorkValueLabelTable);			
		WorkValueLabelCell.setColspan(11);
		contractorBillMainTable.addCell(WorkValueLabelCell);	
		 
	 }
	 
	 protected PdfPTable createWorkValueLabelTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		PdfPTable createWorkValueLabel =new PdfPTable(11);
		createWorkValueLabel.getDefaultCell().setBorderWidth(1);
		createWorkValueLabel.getDefaultCell().setColspan(7);
		createWorkValueLabel.addCell(" ");
		createWorkValueLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		createWorkValueLabel.getDefaultCell().setColspan(2);		
		createWorkValueLabel.addCell(centerPara(pdfLabel.get("contractorbill.pdf.todate")+"\n"+" Rs.       P."));	
		createWorkValueLabel.addCell(centerPara(pdfLabel.get("contractorbill.pdf.lastbill")+"\n"+" Rs.       P."));	
		return createWorkValueLabel;		
	 }
	 
	 // row3   and row4 ---createDetailForWorkOrder	 
	 protected void createDetailsForWorkOrder(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{	
		createDetailsForWorkOrderLabel(contractorBillMainTable);
		createDetailsForWorkOrderData(contractorBillMainTable);
	 }
	 
	 // row3  
	 protected void createDetailsForWorkOrderLabel(PdfPTable contractorBillMainTable) throws DocumentException, EGOVException{
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable detailsForWorkOrderTable = createDetailsForWorkOrderLabelTable(contractorBillMainTable);
		detailsForWorkOrderTable.getDefaultCell().setBorderWidth(1);
		PdfPCell detailsForWorkOrderCell = new PdfPCell(detailsForWorkOrderTable);			
		detailsForWorkOrderCell.setColspan(11);
		contractorBillMainTable.addCell(detailsForWorkOrderCell);		 
	 }
	 
	 // row4  ---createDetailsForWorkOrderData	 
	 protected void createDetailsForWorkOrderData(PdfPTable contractorBillMainTable) throws DocumentException, EGOVException{		 
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		contractorBillMainTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		if(!mbHeaderList.isEmpty()) {
			int listLen=0;
			int mbLen=mbHeaderList.size();
			int assetLen=0;
			
			if(flag && assetForBillList.size() > mbHeaderList.size()){
				listLen= assetForBillList.size();
			}else{
				listLen=mbHeaderList.size();
			}
			
			if(flag)
				assetLen=assetForBillList.size();
			
			for(int i=0;i<listLen;i++){	
				contractorBillMainTable.getDefaultCell().setColspan(2);
				if(i==0)
					contractorBillMainTable.addCell(centerPara(projectCode));
				else
					contractorBillMainTable.addCell("");
								
				if(flag && i<assetLen){
					contractorBillMainTable.addCell(assetForBillList.get(i).getAsset().getCode() +"-"+assetForBillList.get(i).getAsset().getName());
				}else{
					contractorBillMainTable.addCell("");
				}
				contractorBillMainTable.getDefaultCell().setColspan(1);
				if(i<mbLen){
					String mbRefNo="";
					String mbFrmPgNo="";
					String mbToPgNo="";
					
					if(mbHeaderList.get(i).getMbRefNo()!=null)
						mbRefNo=mbHeaderList.get(i).getMbRefNo();
					
					if(mbHeaderList.get(i).getFromPageNo()!=null)
						mbFrmPgNo=mbHeaderList.get(i).getFromPageNo().toString();
					
					if(mbHeaderList.get(i).getToPageNo()!=null)
						mbToPgNo=mbHeaderList.get(i).getToPageNo().toString();
					
					contractorBillMainTable.addCell(centerPara("  "+mbRefNo));
					contractorBillMainTable.addCell(centerPara(blankSpace+mbFrmPgNo));
					contractorBillMainTable.addCell(centerPara(blankSpace+mbToPgNo));
				}else{
					contractorBillMainTable.addCell(centerPara(blankSpace));
					contractorBillMainTable.addCell(centerPara(blankSpace));
					contractorBillMainTable.addCell(centerPara(blankSpace));
				}
				contractorBillMainTable.getDefaultCell().setColspan(4);
				
				if(i==0){					
					contractorBillMainTable.addCell(makePara(pdfLabel.get("contractorbill.pdf.estimateamt") +toCurrency(mbHeader.getWorkOrderEstimate().getEstimate().getWorkValueIncludingTaxes().getValue()),Element.ALIGN_LEFT));					
				}
				else{
					contractorBillMainTable.addCell("");
				}
			}
		}
	 }
	 
	
	 // row3  def---createDetailForWorkOrder	 
	 protected  PdfPTable createDetailsForWorkOrderLabelTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{		
		PdfPTable detailsForWorkOrderLabel =new PdfPTable(11);
		detailsForWorkOrderLabel.getDefaultCell().setBorderWidth(1);		
		detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		detailsForWorkOrderLabel.getDefaultCell().setColspan(2);	
		detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.projectcode"));
		detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.assetcode"));
		detailsForWorkOrderLabel.getDefaultCell().setColspan(1);
		detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.Mbno"));
		detailsForWorkOrderLabel.getDefaultCell().setColspan(2);
		detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
		//detailsForWorkOrderLabel.addCell("\t\t\t\t\t"+pdfLabel.get("contractorbill.pdf.pages")+newLine+pdfLabel.get("contractorbill.pdf.from")+"\t\t\t\t\t"+pdfLabel.get("contractorbill.pdf.to"));		
		//detailsForWorkOrderLabel.addCell("\t \t \t\t \t \t \t \t \t \t \t \t \t \t \t "+pdfLabel.get("contractorbill.pdf.pages")+"\n \t \t \t \t\t \t \t \t"+pdfLabel.get("contractorbill.pdf.from")+"\t \t \t \t \t \t \t \t \t \t \t"+pdfLabel.get("contractorbill.pdf.to"));
		detailsForWorkOrderLabel.addCell(blankSpace15+pdfLabel.get("contractorbill.pdf.pages")+
				"\n"+blankSpace8
				+pdfLabel.get("contractorbill.pdf.from")+blankSpace8+pdfLabel.get("contractorbill.pdf.to"));
		detailsForWorkOrderLabel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		detailsForWorkOrderLabel.getDefaultCell().setColspan(4);		
		detailsForWorkOrderLabel.addCell(pdfLabel.get("contractorbill.pdf.estimateno")+" "+estimateNumber);	
		return detailsForWorkOrderLabel;	
	 }
	
	 // row2  --- workorder row
	 protected void createWorkDescRow(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{	
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		PdfPTable workDescTable = createWorkDescTable(contractorBillMainTable);
		workDescTable.getDefaultCell().setBorderWidth(1);
		PdfPCell workDescCell = new PdfPCell(workDescTable);			
		workDescCell.setColspan(11);
		contractorBillMainTable.addCell(workDescCell);
	 }
	 
	 protected  PdfPTable createWorkDescTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException{	
		PdfPTable workDescTable =new PdfPTable(11);
		workDescTable.getDefaultCell().setBorderWidth(1);		
		workDescTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		workDescTable.getDefaultCell().setColspan(7);		
		workDescTable.addCell(pdfLabel.get("contractorbill.pdf.workdescription")+newLine+workDescription +newLine);
		workDescTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		workDescTable.getDefaultCell().setColspan(4);		
		workDescTable.addCell(pdfLabel.get("contractorbill.pdf.workcommencedon") + workcommencedOn+newLine+pdfLabel.get("contractorbill.pdf.workcompleteon") + workCompletedOn+newLine);	
		return workDescTable;
	 }
	 
	 //  row1  --- createContractorRow
	 
	 protected void createContractorRow(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException,IOException{	
		contractorBillMainTable.getDefaultCell().setBorderWidth(1);	
		
		PdfPTable contractorTable = createContractorTable(contractorBillMainTable);
		contractorTable.getDefaultCell().setBorderWidth(1);
		PdfPCell contractorCell = new PdfPCell(contractorTable);			
		contractorCell.setColspan(11);
		contractorBillMainTable.addCell(contractorCell);
	 }
	
	 protected  PdfPTable createContractorTable(PdfPTable contractorBillMainTable)throws DocumentException,EGOVException, IOException{
		PdfPTable contractorTable =new PdfPTable(11);
		Font font = new Font(BaseFont.createFont("/fonts/ARIALUNI.TTF" ,BaseFont.IDENTITY_H ,BaseFont.EMBEDDED ));
		contractorTable.getDefaultCell().setBorderWidth(1);		
		contractorTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		contractorTable.getDefaultCell().setColspan(7);
		contractorTable.addCell(new Phrase(pdfLabel.get("contractorbill.pdf.contractoraddress")+newLine+contactorName +newLine+contractorAddress+newLine,font));
		contractorTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		contractorTable.getDefaultCell().setColspan(4);		
		contractorTable.addCell(pdfLabel.get("contractorbill.pdf.billno") + billGenNumber+newLine+pdfLabel.get("contractorbill.pdf.dateofbill") + billDate+newLine+pdfLabel.get("contractorbill.pdf.typeofbill") + billType+newLine);	
		return contractorTable;
	 }
	 
	 protected String getColumnLabel(String label){
		 Phrase columnlabel=new Phrase(label, new Font(Font.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD));	
		 return columnlabel.getContent();
	 }
	 
	 // display data generation block
	 protected void generateDisplayData(MBHeader mbHeader,ContractorBillRegister egBillRegister) throws EGOVException{	
		 assetForBillList=contractorBillService.getAssetForBill(egBillRegister.getId());
		 if(!assetForBillList.isEmpty()){
			flag=true;	
		 }
		 List<String> requiredStatutoryList=null;
		 requiredStatutoryList=contractorBillService.getSortedDeductionsFromConfig("StatutoryDeductionKey");
		 List<StatutoryDeductionsForBill> currentStatutoryList=contractorBillService.getStatutoryListForBill(egBillRegister.getId());	
		 sortedStatutorySortedList=contractorBillService.getStatutoryDeductionSortedOrder(requiredStatutoryList, currentStatutoryList);
		 List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue("EGF","CONTRACTOR_ADVANCE_CODE");
		if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && appConfigValuesList.get(0).getValue()!=null){
			advanceAdjustment=contractorBillService.getAdvanceAdjustmentAmountForBill(egBillRegister.getId());
		}
		 //standard deduction
		 List<String> requiredStandardList=contractorBillService.getSortedDeductionsFromConfig("StandardDeductionKey");
		 getStandardDeductionList(egBillRegister.getId(),requiredStandardList);
		 getCustomDeductionList(egBillRegister);		 
		
		 if(mbHeader!=null){
			deptName=mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName();
			contactorName=mbHeader.getWorkOrder().getContractor().getName();
			contractorAddress=mbHeader.getWorkOrder().getContractor().getCorrespondenceAddress()==null?"":mbHeader.getWorkOrder().getContractor().getCorrespondenceAddress();			
			workDescription=mbHeader.getWorkOrderEstimate().getEstimate().getDescription();
			workcommencedOn=sdf.format(mbHeader.getWorkOrder().getCreatedDate());						
			workOrderId=mbHeader.getWorkOrder().getId();
			if(egBillRegister.getBillstatus().equals("CANCELLED")){
			  for(MBForCancelledBill mbCancelBillObj : contractorBillService.getMbListForCancelBill(egBillRegister.getId()))
			    {
					if(!mbHeaderList.contains(mbCancelBillObj.getMbHeader()))
			    		mbHeaderList.add(mbCancelBillObj.getMbHeader());
			    }
			}
			else{
			for(MBHeader mbObj : contractorBillService.getMbListForBillAndWorkordrId(workOrderId,egBillRegister.getId())) {
		       	if(!mbHeaderList.contains(mbObj))
		    		mbHeaderList.add(mbObj);
		     }
		  }
			
			
			
			estimateNumber=mbHeader.getWorkOrderEstimate().getEstimate().getEstimateNumber();
					
			if(mbHeader.getWorkOrder()!=null && mbHeader.getWorkOrderEstimate().getEstimate()!=null 
					&& mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode()!=null
					&& mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode().getCode()!=null)
				projectCode=mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode().getCode();
			 
		}		
		 
		 if(egBillRegister.getBillnumber()!=null)
			 billGenNumber=egBillRegister.getBillnumber();
		 
		//partbillNo
		if(egBillRegister.getBillnumber()!=null && egBillRegister.getPartbillNo()!=null)
		 billNumber=egBillRegister.getPartbillNo().toString();
		
		if(egBillRegister.getBilldate()!=null)
			billDate=sdf.format(egBillRegister.getBilldate());
		
		if(egBillRegister.getBilltype()!=null){
			if("Running".equalsIgnoreCase(billType))
				billType=billNumber+"-"+egBillRegister.getBilltype();
			else
				billType=egBillRegister.getBilltype();			
		}
		
		if(egBillRegister.getBilldate()!=null)
			billDate=sdf.format(egBillRegister.getBilldate());	
		
		if("Final Bill".equalsIgnoreCase(egBillRegister.getBilltype())){
		  if(mbHeader!=null && mbHeader.getWorkOrderEstimate()!=null && mbHeader.getWorkOrderEstimate().getWorkCompletionDate()!=null){
			  workCompletedOn=sdf.format(mbHeader.getWorkOrderEstimate().getWorkCompletionDate());	
		  }
		 }else
			workCompletedOn="in progress";	
	 }
	 
	 //for statutory deduction
	 public void getStandardDeductionList(Long billId, List<String> requiredStandardList){
		 List<DeductionTypeForBill> currentStandardDeductionList =contractorBillService.getStandardDeductionForBill(billId);
		 sortedStandardDeductionList=contractorBillService.getStandardDeductionSortedOrder(requiredStandardList, currentStandardDeductionList);
		
	 }
	
	 public  void getCustomDeductionList(ContractorBillRegister egBillRegister) throws EGOVException{
		customDeductionList = new ArrayList<EgBilldetails>();
		releaseWithHeldList = new ArrayList<EgBilldetails>();
		glcodeIdList=new ArrayList<BigDecimal>();		
		retentionMoneyglcodeIdList=new ArrayList<BigDecimal>();
		getStatutoryDeductionGlcode();
		getStandardDeductionGlcode();
		String advanceAdjstglCodeId=getAdvanceAdjustmentGlcode();
		getGlCodeForNetPayable();
		glcodeIdList.add(new BigDecimal(advanceAdjstglCodeId));
		getRetentionMoneyGlCodeList();
		customDeductionList=contractorBillService.getCustomDeductionListforglcodes(glcodeIdList,egBillRegister.getId());
		releaseWithHeldList=contractorBillService.getReleaseWithHoldAmountListforglcodes(retentionMoneyglcodeIdList,egBillRegister.getId());
	 }
	 
	 public void getRetentionMoneyGlCodeList() throws NumberFormatException, EGOVException{
		List<CChartOfAccounts> retentionMoneyAccountList =new ArrayList<CChartOfAccounts>();
		if(StringUtils.isNotBlank(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE))){
				retentionMoneyAccountList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(RETENTION_MONEY_PURPOSE)));
		}
		for(CChartOfAccounts coa:retentionMoneyAccountList){
				retentionMoneyglcodeIdList.add(BigDecimal.valueOf(coa.getId()));
		}
		
	 }
	 public void getStatutoryDeductionGlcode(){
			if(!sortedStatutorySortedList.isEmpty()){
				for(StatutoryDeductionsForBill bpd:sortedStatutorySortedList){
					if(bpd!=null && bpd.getEgBillPayeeDtls().getRecovery()!=null && bpd.getEgBillPayeeDtls().getRecovery().getId()!=null &&
							bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts()!=null && bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()!=null){
						glcodeIdList.add(new BigDecimal(bpd.getEgBillPayeeDtls().getRecovery().getChartofaccounts().getId()));
					}
			}
		}
	 }
	 
	 public void getStandardDeductionGlcode(){
		if(!sortedStandardDeductionList.isEmpty()){
			for(DeductionTypeForBill deductionTypeForBill:sortedStandardDeductionList){
				if(deductionTypeForBill.getCoa()!=null && deductionTypeForBill.getCoa().getId()!=null){
					glcodeIdList.add(new BigDecimal(deductionTypeForBill.getCoa().getId()));
				}
			}
		}
	 }
	 	 
	 public void getGlCodeForNetPayable() throws NumberFormatException, EGOVException{
		 List<CChartOfAccounts> coaPayableList = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue(WORKS_NETPAYABLE_CODE)));
		// if(!coaPayableList.isEmpty()){
		 if(coaPayableList!=null){
			for(CChartOfAccounts coa :coaPayableList){
				if(coa.getId()!=null){
					//netPayableAmount=contractorBillService.getNetPayableAmountForGlCodeId(coa.getId(),egBillRegister.getId());
					netPayableAmount=contractorBillService.getNetPayableAmountForGlCodeId(egBillRegister.getId());					
					glcodeIdList.add(new BigDecimal(coa.getId()));
				}
			}	
		 }
	 }
	 
	 public String getAdvanceAdjustmentGlcode(){
		 String glCode="";
		 List<AppConfigValues> appConfigValuesList=worksService.getAppConfigValue("EGF","CONTRACTOR_ADVANCE_CODE");
			if(appConfigValuesList!=null && !appConfigValuesList.isEmpty() && 
					appConfigValuesList.get(0).getValue()!=null){
				glCode=appConfigValuesList.get(0).getValue();
			}
			List<CChartOfAccounts>  coaList=getPersistenceService().findAllBy("from CChartOfAccounts coa where coa.glcode=?",glCode);
			if(!coaList.isEmpty()){
				glCode=coaList.get(0).getId().toString();
			}
			return glCode;
	 }
	 
	 protected String getIntDecimalParts(BigDecimal totalAmount){
	   String result="";
	   String totalAmt="";
	   if(totalAmount==null){
		   totalAmt="0:00";
	   }else{
		  totalAmt= toCurrency(totalAmount.doubleValue());
		  String intPart="0";
		  String decimalPart="0";			
		  try{
		 	 intPart=totalAmt.substring(0, totalAmt.indexOf('.'));	
			 decimalPart=totalAmt.substring(totalAmt.indexOf('.')+1,totalAmt.length());	
			 result=intPart+":"+decimalPart;
		 }catch(StringIndexOutOfBoundsException e){
			 	result=totalAmt+":"+"00";			
		 }			 
	
	   }
		 return result;
	 }
	 
	 
	 public String getNetPayAmtInWords(){
		String netPayAmtStr="";
		try{
		netPayAmtStr=NumberToWord.convertToWord(toCurrency(netPayableAmount.doubleValue()));
		}catch(Exception e){
			logger.debug("error -----"+e);
			netPayAmtStr="";
		}
		return netPayAmtStr;
	 }
	 
	 private PdfPTable createApprovalDetailsTable(ContractorBillRegister egBillRegister)	throws DocumentException {
			try {
				PdfPTable approvaldetailsTable = new PdfPTable(6);
				approvaldetailsTable.setWidthPercentage(100);
				approvaldetailsTable.setWidths(new float[] { 1.5f, 1f, 1.1f, 1.3f,1f,2f });
				addRow(approvaldetailsTable, true, makePara("Approval Step"),	centerPara("Name"), centerPara("Designation"), centerPara("Approved on"),centerPara("Signature"), centerPara("Remarks"));
				
				List<State> history=null;
				if(egBillRegister!=null && egBillRegister.getCurrentState()!=null && egBillRegister.getCurrentState().getHistory()!=null)
				history = egBillRegister.getCurrentState().getHistory();
				
				if(history!=null){
					Collections.reverse(history);
					for (State ad : history){
						displayHistory(ad,approvaldetailsTable);
					}
				}
				return approvaldetailsTable;
			}catch (Exception e) {
				throw new EGOVRuntimeException("Exception occured while getting approval details "+e);
			}
		} 
		  
		public void displayHistory(State ad,PdfPTable approvaldetailsTable) throws Exception{
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
					DeptDesig deptdesig= ad.getPrevious().getOwner().getDeptDesigId();
					desgName = deptdesig.getDesigId().getDesignationName();
				}
				else{
					positionId =ad.getPrevious().getOwner().getId();
					DeptDesig deptdesig= ad.getPrevious().getOwner().getDeptDesigId();
					desgName = deptdesig.getDesigId().getDesignationName();
					
				}
				List<PersonalInformation> empList=employeeService.getEmpListForPositionAndDate(ad.getPrevious().getCreatedDate(), positionId);
				for(PersonalInformation emp : empList){
				if(emp.getUserMaster()!=null && emp.getUserMaster().getUserSignature()!=null){
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
	 
	 //calculate total amt for each deduction for a workorder upto bill date
	 public BigDecimal getTotAmountForAdvanceAdjustment(){
		 BigDecimal totalDeductionAmt=BigDecimal.ZERO;
		 totalDeductionAmt=contractorBillService.getTotAmtForAdvanceAdjustment(egBillRegister.getBilldate(),workOrderId,mbHeader.getWorkOrderEstimate().getId());
		 return totalDeductionAmt;
	 }
	 
	 
	 public BigDecimal getTotStatoryAmountForDeduction(StatutoryDeductionsForBill egBillPayeedetail){
		 BigDecimal totalStatoryAmount=BigDecimal.ZERO;
		 totalStatoryAmount=contractorBillService.getTotAmtForStatutory(egBillRegister.getBilldate(), workOrderId,egBillPayeedetail,mbHeader.getWorkOrderEstimate().getId());
		 return totalStatoryAmount;
	 }
	 
	 
	 public BigDecimal getTotStandardAmountForDeduction(DeductionTypeForBill deductionTypeForBill){
		 BigDecimal totStandardAmt=BigDecimal.ZERO;
		 totStandardAmt=contractorBillService.getTotAmtForStandard(egBillRegister.getBilldate(), workOrderId,deductionTypeForBill,mbHeader.getWorkOrderEstimate().getId());
		 return totStandardAmt;
	 }
	 
	 public BigDecimal getTotStandardAmountForDeduction(EgBilldetails egBilldetails){
		 BigDecimal totCustomAmt=BigDecimal.ZERO;
		 totCustomAmt=contractorBillService.getTotAmtForCustom(egBillRegister.getBilldate(), workOrderId,egBilldetails,mbHeader.getWorkOrderEstimate().getId());
		 return totCustomAmt;
	 }
	 
	 public BigDecimal getTotalReleasedWithHeldAmount(EgBilldetails egBilldetails){
		 BigDecimal totalReleaseWHAmount=BigDecimal.ZERO;
		 totalReleaseWHAmount=contractorBillService.getTotReleasedWHAmt(egBillRegister.getBilldate(), workOrderId,egBilldetails,mbHeader.getWorkOrderEstimate().getId());
		 return totalReleaseWHAmount;
	 }
	
	
	//setter and getter
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
	
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
}
