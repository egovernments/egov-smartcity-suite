package org.egov.works.web.actions.tender; 

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.MarketRate;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.TenderResponseContractors;
import org.egov.works.models.tender.TenderResponseQuotes;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;


public class TenderNegotiationPDFGenerator extends AbstractPDFGenerator{	 
	private static final Logger LOGGER = Logger.getLogger(TenderNegotiationPDFGenerator.class);
	private PersistenceService persistenceService = new PersistenceService();
	@Autowired
        private EmployeeService employeeService;
	private final TenderResponse tenderResponse;		
	public static final String TENDER_PDF_ERROR="tenderresponse.pdf.error";
	private final String cityName;		
	private double totalAmt=0.0;
	private double totalBefNegAmt=0.0;
	private double totalAftNegAmt=0.0;
	private double totalMarketRateAmt=0.0;
	private final NumberFormat formatter = new DecimalFormat("#0.00");
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	private final Map<String,String> pdfLabel;
	public static final String TENDERNEGOTIATION_AMOUNT="tenderNegotiationpdf.amount";
	public static final String TENDERNEGOTIATION_RATE="tenderNegotiationpdf.rate";
	private String worksPackgeReq="no";
	private WorksPackage worksPackage;	
	private static final String YES="yes";
	public static final String POSITIVE_SIGN = "(+)";
	public static final String NEGATIVE_SIGN = "(-)";
	private String percTenderType="";
	private NumberFormat nf=null;
	private WorksService worksService;
	
	public TenderNegotiationPDFGenerator(TenderResponse tenderResponse,
			String cityName, OutputStream out,Map<String,String> pdfLabel){		
		super(out, "landscape");
		this.pdfLabel=pdfLabel;
		this.tenderResponse = tenderResponse;
		this.cityName = cityName;
	}
	
	public void generatePDF() {
		nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(10);
		List<String> tenderTypeList=worksService.getTendertypeList();
		if(tenderTypeList!=null && !tenderTypeList.isEmpty()){
			percTenderType=tenderTypeList.get(0);
		}
		final String headerText = pdfLabel.get("tenderNegotiationpdf.header");		
		try {			 
			Paragraph headerTextPara = new Paragraph(new Chunk(headerText,
					new Font(Font.UNDEFINED, LARGE_FONT, Font.BOLD)));
			headerTextPara.setAlignment(Element.ALIGN_CENTER);
			document.add(headerTextPara);
			document.add(makePara(cityName, Element.ALIGN_RIGHT));			
			if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getWorksPackage()!=null){			
			   worksPackgeReq=YES;
			   worksPackage=tenderResponse.getTenderEstimate().getWorksPackage();
			}			
			String deptName="";			
			if(YES.equalsIgnoreCase(worksPackgeReq)){	
				deptName=tenderResponse.getTenderEstimate().getWorksPackage().getUserDepartment().getName(); 				
				document.add(makePara(deptName, Element.ALIGN_RIGHT));				
				if(getWardList(worksPackage)!=null){
					 document.add(makePara(pdfLabel.get("tenderNegotiationpdf.ward")+"/"+pdfLabel.get("tenderNegotiationpdf.zone")+getWardList(worksPackage),Element.ALIGN_LEFT));
				}				
			}else{			
				if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment()!=null 
						&& tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getName()!=null)
					deptName=tenderResponse.getTenderEstimate().getAbstractEstimate().getExecutingDepartment().getName();				
					document.add(makePara(deptName, Element.ALIGN_RIGHT));	
				if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getAbstractEstimate().getWard().getParent()!=null
						&& tenderResponse.getTenderEstimate().getAbstractEstimate().getWard()!=null)
					document.add(makePara(pdfLabel.get("tenderNegotiationpdf.ward")+"/"+pdfLabel.get("tenderNegotiationpdf.zone") + tenderResponse.getTenderEstimate().getAbstractEstimate().getWard().getName()+"/"+ tenderResponse.getTenderEstimate().getAbstractEstimate().getWard().getParent().getName(),Element.ALIGN_LEFT));
			}			
			if(YES.equalsIgnoreCase(worksPackgeReq)){				
				document.add(makePara(pdfLabel.get("tenderNegotiationpdf.nameofwork")+ tenderResponse.getTenderEstimate().getWorksPackage().getName(), Element.ALIGN_LEFT));
			}else if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null){				
				document.add(makePara(pdfLabel.get("tenderNegotiationpdf.nameofwork")+ tenderResponse.getTenderEstimate().getAbstractEstimate().getName(), Element.ALIGN_LEFT));
			}			
			if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getTenderHeader()!=null
					&& tenderResponse.getTenderEstimate().getTenderHeader().getTenderNo()!=null){
				document.add(makePara(pdfLabel.get("tenderNumber")+ tenderResponse.getTenderEstimate().getTenderHeader().getTenderNo(), Element.ALIGN_LEFT));
				
			}
			if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getWorksPackage()!=null){
				document.add(makePara(pdfLabel.get("tenderFileNo")+ tenderResponse.getTenderEstimate().getWorksPackage().getTenderFileNumber(), Element.ALIGN_LEFT));
			}	
			document.add(spacer());
			String tenderDate="";
			if(tenderResponse!=null && tenderResponse.getTenderEstimate()!=null && tenderResponse.getTenderEstimate().getTenderHeader()!=null 
					&& tenderResponse.getTenderEstimate().getTenderHeader().getTenderDate()!=null)
				tenderDate=sdf.format(tenderResponse.getTenderEstimate().getTenderHeader().getTenderDate());			
				document.add(makePara(pdfLabel.get("tenderNegotiationpdf.tenderdate")+ tenderDate,Element.ALIGN_RIGHT));	
			document.add(spacer());
			PdfPTable contractorTable=null;
			if(tenderResponse!=null){
				contractorTable = createContractorTable(tenderResponse);			
				document.add(contractorTable);
			}
			document.add(spacer());	
			
			if(tenderResponse!=null && tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType)){
				PdfPTable negotiationTable=null;
				negotiationTable = createNegotiationTable(tenderResponse,tenderResponse.getTenderResponseContractors().get(0).getContractor());			
				document.add(negotiationTable);
				document.add(spacer());	
				if(negotiationTable!=null && negotiationTable.getRows().size()>8)
					document.newPage();
				
			}
			else if(tenderResponse!=null && !tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType)){
				createNegotiationTableForContractors(tenderResponse);
			}
						
			if(tenderResponse!=null && tenderResponse.getNegotiationPreparedBy()!=null && tenderResponse.getNegotiationPreparedBy().getEmployeeName()!=null)
				document.add(makePara(pdfLabel.get("tenderNegotiationpdf.preparedby")+" "+tenderResponse.getNegotiationPreparedBy().getEmployeeName()));
			document.add(spacer());
			document.add(spacer());
			document.add(makePara(pdfLabel.get("tenderNegotiationpdf.checkedby")));			
			document.newPage();			
			
			PdfPTable approvaldetailsTable =null;
			if(tenderResponse!=null)
			  approvaldetailsTable = createApprovalDetailsTable(tenderResponse);
			
			if (approvaldetailsTable!=null && approvaldetailsTable.getRows().size()!=1) {
			 document.add(makePara(pdfLabel.get("tenderNegotiationpdf.approvaldetails")));	
			 document.add(spacer());
			 document.add(approvaldetailsTable);			
			}
			document.close();
		} catch (DocumentException e) {
			throw new EGOVRuntimeException(TENDER_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	 throw new EGOVRuntimeException(TENDER_PDF_ERROR,ex);
	    }
	}
	
	private PdfPTable createApprovalDetailsTable(TenderResponse tenderResponse)	throws DocumentException {
		try{
			PdfPTable approvaldetailsTable = new PdfPTable(5);
			approvaldetailsTable.setWidthPercentage(100);
			approvaldetailsTable.setWidths(new float[] { 1f, 1f, 2f, 1.5f, 2f });
			addRow(approvaldetailsTable, true, makePara(pdfLabel.get("tenderNegotiationpdf.aprvalstep")),centerPara(pdfLabel.get("tenderNegotiationpdf.name")),centerPara(pdfLabel.get("tenderNegotiationpdf.designation")),  
						centerPara(pdfLabel.get("tenderNegotiationpdf.aprvdon")),centerPara(pdfLabel.get("tenderNegotiationpdf.remarks")));
			List<StateHistory> history =null;				
			if(tenderResponse.getCurrentState()!=null && tenderResponse.getCurrentState().getHistory()!=null)
				history=tenderResponse.getCurrentState().getHistory();
			if(history!=null){
				Collections.reverse(history);
				StateHistory previous = null;
				for (StateHistory ad : history) {
					if(!ad.getValue().equals("NEW") && !ad.getValue().equals("APPROVAL_PENDING") && !ad.getValue().equals("END") && previous != null){					       
						EgwStatus status =(EgwStatus) getPersistenceService().find("from EgwStatus where code=?",ad.getValue());
						PersonalInformation emp=employeeService.getEmpForPositionAndDate(ad.getCreatedDate(), Integer.parseInt(previous.getOwnerPosition().getId().toString()));
						addRow(approvaldetailsTable, true, makePara(status.getDescription()), makePara(emp.getEmployeeName()),makePara(previous.getOwnerPosition().getDeptDesigId().getDesigId().getDesignationName()), makePara(getDateInFormat(ad.getCreatedDate().toString())), rightPara(ad.getComments()));						
					}
					previous = ad;
				}
			}				
			  return approvaldetailsTable;
		}catch (Exception e) {
				throw new DocumentException("Exception occured while getting approval details "+e);
		}
	}	
	
	/**
	 * create negotiation table main table 
	 * @param tenderResponse
	 * @return
	 * @throws DocumentException
	 * @throws EGOVException
	 */
	private PdfPTable createNegotiationTable(TenderResponse tenderResponse,Contractor contractor) throws DocumentException,EGOVException{	
		PdfPTable negotiationTable = new PdfPTable(13);
		negotiationTable.setWidthPercentage(100);
		negotiationTable.setWidths(new float[] { 0.5f, 1f, 3.6f,1.5f,1.1f,
												 0.9f,1.5f,1.7f,1.7f,1.7f,
												 1.7f,1.7f,1.7f});
		try {
			negotiationTable.getDefaultCell().setPadding(5);
			negotiationTable.getDefaultCell().setBorderWidth(1);
			negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			negotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.slno"));		
			negotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.scheduleno"));
			negotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.descofwork"));
			negotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.quantity"));
								
			/**
			 * start creating tables for Estimate, before negotion, after negotiation and marketRate
			 */
			
			PdfPTable estimateTable = createAsPerEstimateTable(tenderResponse);	
			estimateTable.setWidths(new float[] { 0.45f, 0.37f, 0.62f}); 
			PdfPCell estimateCell = new PdfPCell(estimateTable);			
			estimateCell.setColspan(3);	
			negotiationTable.addCell(estimateCell);			
			
			PdfPTable beforeNegotiationTable = createBeforeNegotiationTable(tenderResponse);		
			PdfPCell beforeNegotiationCell = new PdfPCell(beforeNegotiationTable);			
			beforeNegotiationCell.setColspan(2);	
			negotiationTable.addCell(beforeNegotiationCell);
			
			PdfPTable afterNegotiationTable = createAfterNegotiationTable(tenderResponse);
			PdfPCell afterNegotiationCell = new PdfPCell(afterNegotiationTable);
			afterNegotiationCell.setColspan(2);
			negotiationTable.addCell(afterNegotiationCell);			
			PdfPTable marketRateTable = createMarketRateTable(tenderResponse);
			PdfPCell marketRateCell = new PdfPCell(marketRateTable);
			marketRateCell.setColspan(2);		
			negotiationTable.addCell(marketRateCell);
			/**
			 *  end creating tables for before negotion, after negotiation and marketRate		
			 */			
			if(YES.equalsIgnoreCase(worksPackgeReq)){		
				createNegotiationTableDataForWp(tenderResponse,negotiationTable,contractor);	
			}else{
				createNegotiationTableData(tenderResponse,negotiationTable,contractor);	
			}
			createNegotiationTableFooter(negotiationTable);
			addRowFooter(negotiationTable);	
			addTotalQuotedFooter(negotiationTable);
			addFinalRow(negotiationTable,tenderResponse);
		} catch (DocumentException e) {
			throw new EGOVRuntimeException(TENDER_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	throw new EGOVRuntimeException(TENDER_PDF_ERROR,ex);
	    }
		return negotiationTable;
	}
	
	/**
	 * view workpackage pdf
	 */	
	public void createNegotiationTableDataForWp(TenderResponse tenderResponse,PdfPTable negotiationTable,Contractor contractor)
	throws DocumentException,EGOVException{	
		Date asOnDate=null;
		if(tenderResponse.getNegotiationDate()!=null)
			 asOnDate=tenderResponse.getNegotiationDate();	
		
		int i=0;	
		Map<String,Integer> exceptionaSorMap = worksService.getExceptionSOR();
		totalBefNegAmt=0;
		totalAmt=0;
		totalAftNegAmt=0;
		totalMarketRateAmt=0;
		for(TenderResponseActivity tenderResponseActivity : tenderResponse.getTenderResponseActivities()){
			String schNo="";
			String description="";
			Double quantity=0.0;
			double rate=0.0;
			//double uomFactor=0.0;
			String per="";
			double befNegRate=0.0;
			double befNegAmount=0.0;
			double aftNegRate=0.0;
			double aftNegAmount=0.0;
			MarketRate marketRateObj=null;
			double marketRate=0.0;		
			double marketRateAmount=0.0;
			double uomFactor=1;
			
			if(tenderResponseActivity!=null){	
				if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null
						&& tenderResponseActivity.getActivity().getSchedule().getCode()!=null && exceptionaSorMap.containsKey(tenderResponseActivity.getActivity().getUom().getUom())){
					uomFactor = exceptionaSorMap.get(tenderResponseActivity.getActivity().getUom().getUom());					
				}
				
				Map<String,Object> rateQtyMap=processLatestRateAndQtyForLineItem(worksPackage.getActivitiesForEstimate(),
						tenderResponseActivity.getActivity());		
				if(rateQtyMap!=null && !rateQtyMap.isEmpty()){
					negotiationTable.addCell(makePara(++i));
					schNo=(String)rateQtyMap.get("sorcode");
					negotiationTable.addCell(centerPara(schNo));
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					description=(String)rateQtyMap.get("desc");
					negotiationTable.addCell(makePara(description,Element.ALIGN_LEFT));	
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					quantity=(Double)rateQtyMap.get("qty");
					negotiationTable.addCell(centerPara(formatter.format(quantity)));	
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					rate=(Double)rateQtyMap.get("rate");
					negotiationTable.addCell(rightPara(formatter.format(rate)));		
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					per=(String)rateQtyMap.get("per");
					negotiationTable.addCell(centerPara(per));
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					double amount=(Double)rateQtyMap.get("amt");
					negotiationTable.addCell(rightPara(formatter.format(amount)));
						totalAmt=totalAmt+amount;
				}
				
				if(rateQtyMap.get("slno")!=null){		
					/**
					 * before negotiation 
					 */	
					negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);	
					for(TenderResponseQuotes tenderResponseQuotes:tenderResponseActivity.getTenderResponseQuotes()){
						if(tenderResponseQuotes.getContractor().getId()==contractor.getId()){
							if(tenderResponseQuotes.getQuotedRate()!=0.0)
								befNegRate=tenderResponseQuotes.getQuotedRate();
							negotiationTable.addCell(rightPara(formatter.format(befNegRate)));
						
							befNegAmount=(quantity*befNegRate)/uomFactor;
							totalBefNegAmt=totalBefNegAmt+befNegAmount;
							negotiationTable.addCell(rightPara(formatter.format(befNegAmount)));				
						}
					}	
					if(tenderResponseActivity.getNegotiatedRate()!=0.0)
						aftNegRate=tenderResponseActivity.getNegotiatedRate();
						//double negotiatedQty=tenderResponseActivity.getNegotiatedQuantity();
						negotiationTable.addCell(rightPara(formatter.format(aftNegRate)));
					
						aftNegAmount=(quantity*aftNegRate)/uomFactor;
						totalAftNegAmt=totalAftNegAmt+aftNegAmount;
						negotiationTable.addCell(rightPara(formatter.format(aftNegAmount)));
					
								
					if(tenderResponseActivity.getActivity()!=null 
							&& tenderResponseActivity.getActivity().getSchedule()!=null
							&& asOnDate!=null && tenderResponseActivity.getActivity().getSchedule().hasValidMarketRateFor(asOnDate)){
						marketRateObj= tenderResponseActivity.getActivity().getSchedule().getMarketRateOn(asOnDate);
						marketRate=marketRateObj.getMarketRate().getValue();
						marketRateAmount=(quantity*marketRate)/uomFactor;
					}
						
					if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null && asOnDate!=null 
								&& !tenderResponseActivity.getActivity().getSchedule().hasValidMarketRateFor(asOnDate)){
						marketRate=tenderResponseActivity.getActivity().getRate().getValue();
						marketRateAmount=quantity*marketRate;					
					}
						
					if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getNonSor()!=null){
						marketRate=tenderResponseActivity.getActivity().getRate().getValue();
						double marketQty=tenderResponseActivity.getActivity().getQuantity();
						marketRateAmount=marketQty*marketRate;
					}
					negotiationTable.addCell(rightPara(formatter.format(marketRate)));	
					totalMarketRateAmt=totalMarketRateAmt+marketRateAmount;
					negotiationTable.addCell(rightPara(formatter.format(marketRateAmount)));
			   }
				
			 }
	   }
	}
	
	/**
	 * view estimate pdf	
	 */
	public void createNegotiationTableData(TenderResponse tenderResponse,PdfPTable negotiationTable,Contractor contractor)
	throws DocumentException,EGOVException{		
		
		Date asOnDate=null;
		if(tenderResponse.getNegotiationDate()!=null)
			 asOnDate=tenderResponse.getNegotiationDate();	
		int i=0;	
		Map<String,Integer> exceptionaSorMap = worksService.getExceptionSOR();
		
		for(TenderResponseActivity tenderResponseActivity : tenderResponse.getTenderResponseActivities()){
			String schNo="";
			String description="";
			Double quantity=0.0;
			double rate=0.0;
		//	double uomFactor=0.0;
			String per="";
			double befNegRate=0.0;
			double befNegAmount=0.0;
			double aftNegRate=0.0;
			double aftNegAmount=0.0;
			MarketRate marketRateObj=null;
			double marketRate=0.0;		
			double marketRateAmount=0.0;
			negotiationTable.addCell(makePara(++i));
			double uomFactor=1;
			
		if(tenderResponseActivity!=null){	
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null
					&& tenderResponseActivity.getActivity().getSchedule().getCode()!=null && exceptionaSorMap.containsKey(tenderResponseActivity.getActivity().getUom().getUom())){
				uomFactor = exceptionaSorMap.get(tenderResponseActivity.getActivity().getUom().getUom());
			}
			
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null && tenderResponseActivity.getActivity().getSchedule().getCode()!=null)
				schNo=tenderResponseActivity.getActivity().getSchedule().getCode();
				negotiationTable.addCell(centerPara(schNo));
				negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);				
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null && tenderResponseActivity.getActivity().getSchedule().getDescription()!=null){
				description=tenderResponseActivity.getActivity().getSchedule().getDescription();
			}
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getNonSor()!=null
					&& tenderResponseActivity.getActivity().getNonSor().getDescription()!=null){
				description=tenderResponseActivity.getActivity().getNonSor().getDescription();				
			}			
			negotiationTable.addCell(makePara(description,Element.ALIGN_LEFT));
			negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);				
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getQuantity()!=null) 
				quantity=tenderResponseActivity.getActivity().getQuantity();
				negotiationTable.addCell(centerPara(quantity));
				negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);				
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null
						&& tenderResponseActivity.getActivity().getRate()!=null && tenderResponseActivity.getActivity().getSORCurrentRate()!=null)
				rate=tenderResponseActivity.getActivity().getSORCurrentRate().getValue();
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getNonSor()!=null
						&& tenderResponseActivity.getActivity().getRate()!=null)
				rate=tenderResponseActivity.getActivity().getRate().getValue();
				negotiationTable.addCell(rightPara(formatter.format(rate)));
				negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			if(tenderResponseActivity.getActivity().getSchedule()!=null && tenderResponseActivity.getActivity().getSchedule().getUom()!=null
					&& tenderResponseActivity.getActivity().getSchedule().getUom().getUom()!=null){	
				per=tenderResponseActivity.getActivity().getSchedule().getUom().getUom();
			}				
			if(tenderResponseActivity.getActivity().getNonSor()!=null && tenderResponseActivity.getActivity().getNonSor().getUom()!=null
						&& tenderResponseActivity.getActivity().getNonSor().getUom().getUom()!=null){
				per=tenderResponseActivity.getActivity().getNonSor().getUom().getUom();
			}
			negotiationTable.addCell(centerPara(per));
			double amount=(quantity*rate)/uomFactor;
			totalAmt=totalAmt+amount;
			negotiationTable.addCell(rightPara(formatter.format(amount)));
			/** before negotiation*/	
			negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);	
			for(TenderResponseQuotes tenderResponseQuotes:tenderResponseActivity.getTenderResponseQuotes()){
				if(tenderResponseQuotes.getContractor().getId()==contractor.getId()){
					if(tenderResponseQuotes.getQuotedRate()!=0.0)
						befNegRate=tenderResponseQuotes.getQuotedRate();
					double quotedQty=tenderResponseQuotes.getQuotedQuantity();
					negotiationTable.addCell(rightPara(formatter.format(befNegRate)));				
					befNegAmount=(quotedQty*befNegRate)/uomFactor;
					totalBefNegAmt=totalBefNegAmt+befNegAmount;
					negotiationTable.addCell(rightPara(formatter.format(befNegAmount)));
					
				}
			}
			if(tenderResponseActivity.getNegotiatedRate()!=0.0)
				aftNegRate=tenderResponseActivity.getNegotiatedRate();
				double negotiatedQty=tenderResponseActivity.getNegotiatedQuantity();
				negotiationTable.addCell(rightPara(formatter.format(aftNegRate)));			
				aftNegAmount=(negotiatedQty*aftNegRate)/uomFactor;
				totalAftNegAmt=totalAftNegAmt+aftNegAmount;
				negotiationTable.addCell(rightPara(formatter.format(aftNegAmount)));
			if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null
						&& asOnDate!=null && tenderResponseActivity.getActivity().getSchedule().hasValidMarketRateFor(asOnDate)){
				marketRateObj= tenderResponseActivity.getActivity().getSchedule().getMarketRateOn(asOnDate);
				marketRate=marketRateObj.getMarketRate().getValue();
				marketRateAmount=(quantity*marketRate)/uomFactor;
			}
			else if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getSchedule()!=null){
				marketRate=tenderResponseActivity.getActivity().getSORCurrentRate().getValue();
				double marketQty=tenderResponseActivity.getActivity().getQuantity();
				marketRateAmount=(marketQty*marketRate)/uomFactor;
			}
			else if(tenderResponseActivity.getActivity()!=null && tenderResponseActivity.getActivity().getNonSor()!=null){
				marketRate=tenderResponseActivity.getActivity().getRate().getValue();
				double marketQty=tenderResponseActivity.getActivity().getQuantity();
				marketRateAmount=marketQty*marketRate;
			}
			negotiationTable.addCell(rightPara(formatter.format(marketRate)));	
			totalMarketRateAmt=totalMarketRateAmt+marketRateAmount;
			negotiationTable.addCell(rightPara(formatter.format(marketRateAmount)));
		 }
	   }		
	}
	
	public void createNegotiationTableFooter(PdfPTable negotiationTable)
	throws DocumentException,EGOVException{		
		negotiationTable.addCell(" ");		
		negotiationTable.addCell(" ");		
		PdfPTable tenderTotalTable = createTenderTotalTable();		
		PdfPCell tenderTotalTableCell = new PdfPCell(tenderTotalTable);
		tenderTotalTableCell.setColspan(3);	
		negotiationTable.addCell(tenderTotalTableCell);		
		negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		negotiationTable.addCell(" ");		
		negotiationTable.addCell(rightPara(formatter.format(totalAmt)));
		negotiationTable.addCell(" ");
		negotiationTable.addCell(rightPara(formatter.format(totalBefNegAmt)));
		negotiationTable.addCell(" ");
		negotiationTable.addCell(rightPara(formatter.format(totalAftNegAmt)));
		negotiationTable.addCell(" ");
		negotiationTable.addCell(rightPara(formatter.format(totalMarketRateAmt)));		
	}
	
	//second row for footer	
	public void addRowFooter(PdfPTable negotiationTable)
	throws DocumentException,EGOVException{		
		negotiationTable.addCell(" ");
		negotiationTable.addCell(" ");			
		PdfPTable tenderPercentageTable = createTenderPercentageTable();		
		PdfPCell tenderPercentageTableCell = new PdfPCell(tenderPercentageTable);
		tenderPercentageTableCell.setColspan(5);	
		negotiationTable.addCell(tenderPercentageTableCell);		
		negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		PdfPTable befTenderNegFormulaTable = createBefTenderNegFormulaTable();		
		PdfPCell befTenderNegFormulaTableCell = new PdfPCell(befTenderNegFormulaTable);
		befTenderNegFormulaTableCell.setColspan(2);	
		negotiationTable.addCell(befTenderNegFormulaTableCell);		
		PdfPTable aftTenderNegFormulaTable = createAftTenderNegFormulaTable();		
		PdfPCell aftTenderNegFormulaTableCell = new PdfPCell(aftTenderNegFormulaTable);
		aftTenderNegFormulaTableCell.setColspan(2);	
		negotiationTable.addCell(aftTenderNegFormulaTableCell);		
		PdfPTable marketTenderNegFormulaTable = createMarketTenderNegFormulaTable();		
		PdfPCell marketTenderNegFormulaTableCell = new PdfPCell(marketTenderNegFormulaTable);
		marketTenderNegFormulaTableCell.setColspan(2);	
		negotiationTable.addCell(marketTenderNegFormulaTableCell);
	}	
	//adding total quoted value footer
	public void addTotalQuotedFooter(PdfPTable negotiationTable)throws DocumentException,EGOVException{		
		negotiationTable.addCell(" ");
		negotiationTable.addCell(" ");			
		PdfPTable tenderQuotedTable = createTotalQuotedValueTable();		
		PdfPCell tenderQuotedTableCell = new PdfPCell(tenderQuotedTable);
		tenderQuotedTableCell.setColspan(5);	
		negotiationTable.addCell(tenderQuotedTableCell);		
		negotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		PdfPTable befTenderNegQuotedTable = createBefTenderNegQuotedTable();		
		PdfPCell befTenderNegQuotedTableCell = new PdfPCell(befTenderNegQuotedTable);
		befTenderNegQuotedTableCell.setColspan(2);	
		negotiationTable.addCell(befTenderNegQuotedTableCell);		
		PdfPTable aftTenderNegQuotedTable = createAftTenderNegQuotedTable();		
		PdfPCell aftTenderNegQuotedTableCell = new PdfPCell(aftTenderNegQuotedTable);
		aftTenderNegQuotedTableCell.setColspan(2);	
		negotiationTable.addCell(aftTenderNegQuotedTableCell);		
		PdfPTable marketTenderNegTable = createMarketTenderNegTable();		
		PdfPCell marketTenderNegTableCell = new PdfPCell(marketTenderNegTable);
		marketTenderNegTableCell.setColspan(2);	
		negotiationTable.addCell(marketTenderNegTableCell);
	}	
   //start for second footer row
	public PdfPTable createTenderPercentageTable(){	
		PdfPTable tenderPercentageTable =new PdfPTable(5);
		tenderPercentageTable.getDefaultCell().setBorderWidth(1);
		tenderPercentageTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		tenderPercentageTable.getDefaultCell().setColspan(5);
		tenderPercentageTable.addCell(pdfLabel.get("tenderNegotiationpdf.percentage"));
		return tenderPercentageTable;
	}

	//start for second footer row
	public PdfPTable createTotalQuotedValueTable(){	
		PdfPTable tenderQuotedTable =new PdfPTable(5);
		tenderQuotedTable.getDefaultCell().setBorderWidth(1);
		tenderQuotedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		tenderQuotedTable.getDefaultCell().setColspan(5);
		tenderQuotedTable.addCell(pdfLabel.get("tenderNegotiationpdf.quoted.total"));
		return tenderQuotedTable;
	}

	
	public PdfPTable createBefTenderNegFormulaTable(){		
		double totTenderBefNegPer=tenderResponse.getPercQuotedRate();	
		PdfPTable befTenderNegFormulaTable =new PdfPTable(2);
		befTenderNegFormulaTable.getDefaultCell().setBorderWidth(1);
		befTenderNegFormulaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		befTenderNegFormulaTable.getDefaultCell().setColspan(2);
		String formated_perc = null;
		if(tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType))
			formated_perc = nf.format(totTenderBefNegPer);
		else
			formated_perc = formatter.format(Math.abs(totTenderBefNegPer));
			
		befTenderNegFormulaTable.addCell(makePara((totTenderBefNegPer>0?POSITIVE_SIGN:NEGATIVE_SIGN)+formated_perc+"%"));
		return befTenderNegFormulaTable;
	}
	
	public PdfPTable createBefTenderNegQuotedTable(){		
		double totTenderBefNegPer=tenderResponse.getPercQuotedRate();	
		PdfPTable befTenderNegQuotedTable =new PdfPTable(2);
		befTenderNegQuotedTable.getDefaultCell().setBorderWidth(1);
		befTenderNegQuotedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		befTenderNegQuotedTable.getDefaultCell().setColspan(2);
		String perc_amt = null;
		if(tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType)) {
			if(totTenderBefNegPer>0) {
				perc_amt=formatter.format(totalBefNegAmt+(totalBefNegAmt*(Math.abs(totTenderBefNegPer)/100)));
			}
			else{
				perc_amt=formatter.format(totalBefNegAmt-(totalBefNegAmt*(Math.abs(totTenderBefNegPer)/100)));
			}

		}
		else {
			perc_amt = formatter.format(Math.abs(totalBefNegAmt));
		}
		befTenderNegQuotedTable.addCell(makePara(perc_amt));
		return befTenderNegQuotedTable;
	}
	
	public PdfPTable createAftTenderNegFormulaTable(){		
		double totTenderAftNegPer=tenderResponse.getPercNegotiatedAmountRate();
		PdfPTable aftTenderNegFormulaTable =new PdfPTable(2);
		aftTenderNegFormulaTable.getDefaultCell().setBorderWidth(1);
		aftTenderNegFormulaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		aftTenderNegFormulaTable.getDefaultCell().setColspan(2);
		String formated_perc = null;
		if(tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType))
			formated_perc = nf.format(totTenderAftNegPer);
		else
			formated_perc = formatter.format(Math.abs(totTenderAftNegPer));
			
		aftTenderNegFormulaTable.addCell(makePara((totTenderAftNegPer>0?POSITIVE_SIGN:NEGATIVE_SIGN)+formated_perc+"%"));
		return aftTenderNegFormulaTable;
	}
	
	public PdfPTable createAftTenderNegQuotedTable(){		
		double totTenderAftNegPer=tenderResponse.getPercNegotiatedAmountRate();
		PdfPTable aftTenderNegQuotedTable =new PdfPTable(2);
		aftTenderNegQuotedTable.getDefaultCell().setBorderWidth(1);
		aftTenderNegQuotedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		aftTenderNegQuotedTable.getDefaultCell().setColspan(2);
		String perc_amt = null;

		if(tenderResponse.getTenderEstimate().getTenderType().equalsIgnoreCase(percTenderType)) {
			if(totTenderAftNegPer>0) {
				perc_amt=formatter.format(totalAftNegAmt+(totalAftNegAmt*(Math.abs(totTenderAftNegPer)/100)));
			}
			else{
				perc_amt=formatter.format(totalAftNegAmt-(totalAftNegAmt*(Math.abs(totTenderAftNegPer)/100)));
			}

			}
		else {
				perc_amt = formatter.format(Math.abs(totalAftNegAmt));
		}
			
		aftTenderNegQuotedTable.addCell(makePara(perc_amt));
		return aftTenderNegQuotedTable;
	}
	
	public PdfPTable createMarketTenderNegFormulaTable(){
		//double totTenderNegMarketPer=calculatPercentage(totalAftNegAmt,totalMarketRateAmt,2);	
		double totTenderNegMarketPer=calculateMarketRate();	
		PdfPTable marketTenderNegFormulaTable =new PdfPTable(2);
		marketTenderNegFormulaTable.getDefaultCell().setBorderWidth(1);
		marketTenderNegFormulaTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		marketTenderNegFormulaTable.getDefaultCell().setColspan(2);
		String formated_perc = null;
		formated_perc = nf.format(totTenderNegMarketPer);
		marketTenderNegFormulaTable.addCell(makePara((totTenderNegMarketPer>0?POSITIVE_SIGN:NEGATIVE_SIGN)+formated_perc+"%"));
		return marketTenderNegFormulaTable;
	}
	
	public PdfPTable createMarketTenderNegTable(){
		double totTenderNegMarketPer=calculateMarketRate();	
		PdfPTable marketTenderNegQuotedTable =new PdfPTable(2);
		marketTenderNegQuotedTable.getDefaultCell().setBorderWidth(1);
		marketTenderNegQuotedTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
		marketTenderNegQuotedTable.getDefaultCell().setColspan(2);
		String perc_amt = null;
		if(totTenderNegMarketPer>=0) {
			perc_amt =formatter.format(totalMarketRateAmt+(totalMarketRateAmt*(Math.abs(totTenderNegMarketPer)/100)));
		}
		else {
			perc_amt =formatter.format(totalMarketRateAmt-(totalMarketRateAmt*(Math.abs(totTenderNegMarketPer)/100)));
		}
		marketTenderNegQuotedTable.addCell(makePara(perc_amt));
		return marketTenderNegQuotedTable;
	}
	
	private double calculateMarketRate(){
		double percentage=0.0;
		double totTenderAftNegPer=tenderResponse.getPercNegotiatedAmountRate();
		try{
			percentage = (((totalAmt*(1+totTenderAftNegPer/100))-totalMarketRateAmt)/totalMarketRateAmt)*100;
		}catch(Exception e){
			percentage=0.00;
			LOGGER.info("Exception while calculating totTenderNegMarketPer"+e);
		}
		return percentage;
	}
	
	public double calculatPercentage(double amt1,double am2,int type){
		double percentage=0.0;
		try{
			if(type==1){
				percentage=(amt1-am2)/amt1*100;
			}
			else if(type==2){
				percentage=(amt1-am2)/am2*100;
			}
			
			if(Double.isInfinite(percentage))
				percentage=0.00;			
		}catch(Exception e){
			percentage=0.00;
			LOGGER.info("Exception while calculating totTenderNegMarketPer"+e);
		}
		return Math.abs(percentage);
	}	
	//ends second footer row
	
	public void addFinalRow(PdfPTable negotiationTable,TenderResponse tenderResponse)
	throws DocumentException,EGOVException{	
		PdfPTable tenderNarrationTable = createTenderNarrationTable(tenderResponse);		
		PdfPCell tenderNarrationTableCell = new PdfPCell(tenderNarrationTable);
		tenderNarrationTableCell.setColspan(13);	
		negotiationTable.addCell(tenderNarrationTableCell);
	}
	
	/**
	 * start final row for narration 
	 * @param tenderResponse
	 * @return
	 */
	public PdfPTable createTenderNarrationTable(TenderResponse tenderResponse){
		PdfPTable tenderNarrationTable =new PdfPTable(13);
		tenderNarrationTable.getDefaultCell().setBorderWidth(1);
		tenderNarrationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		tenderNarrationTable.getDefaultCell().setColspan(13);
		tenderNarrationTable.addCell("\t"+(tenderResponse.getNarration()==null?""
				:tenderResponse.getNarration()));
		return tenderNarrationTable;
	}
	/** start for first footer row */
	public PdfPTable createTenderTotalTable(){
		PdfPTable tenderTotalTable =new PdfPTable(3);
		tenderTotalTable.getDefaultCell().setBorderWidth(1);
		tenderTotalTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		tenderTotalTable.getDefaultCell().setColspan(3);
		tenderTotalTable.addCell(pdfLabel.get("tenderNegotiationpdf.tendertotal"));
		return tenderTotalTable;
	}
	
	
	public PdfPTable createAsPerEstimateTable(TenderResponse tenderResponse)
	throws DocumentException,EGOVException{
		PdfPTable estimateTable =new PdfPTable(3);
		estimateTable.getDefaultCell().setBorderWidth(1);
		estimateTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		estimateTable.getDefaultCell().setColspan(3);
		estimateTable.addCell(pdfLabel.get("tenderNegotiationpdf.asPerEstimate"));
		estimateTable.getDefaultCell().setColspan(1);
		estimateTable.addCell(pdfLabel.get(TENDERNEGOTIATION_RATE));
		estimateTable.addCell(pdfLabel.get("tenderNegotiationpdf.Per"));
		estimateTable.addCell(pdfLabel.get(TENDERNEGOTIATION_AMOUNT));	
		return estimateTable;
	}
	
	/**creating tables for before negotion*/
	public PdfPTable createBeforeNegotiationTable(TenderResponse tenderResponse)
	throws DocumentException,EGOVException{
		PdfPTable beforeNegotiationTable =new PdfPTable(2);
		beforeNegotiationTable.getDefaultCell().setBorderWidth(1);
		beforeNegotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		beforeNegotiationTable.getDefaultCell().setColspan(2);
		beforeNegotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.asPerTender"));
		beforeNegotiationTable.getDefaultCell().setColspan(1);
		beforeNegotiationTable.addCell(pdfLabel.get(TENDERNEGOTIATION_RATE));		
		beforeNegotiationTable.addCell(pdfLabel.get(TENDERNEGOTIATION_AMOUNT));		
		return beforeNegotiationTable;		
	}	
	//creating marketRate table 
	public PdfPTable createAfterNegotiationTable(TenderResponse tenderResponse)
	throws DocumentException,EGOVException{
		PdfPTable afterNegotiationTable =new PdfPTable(2);
		afterNegotiationTable.getDefaultCell().setBorderWidth(1);
		afterNegotiationTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		afterNegotiationTable.getDefaultCell().setColspan(2);
		String negDate="";
		if(tenderResponse!=null && tenderResponse.getNegotiationDate()!=null)
			negDate=sdf.format(tenderResponse.getNegotiationDate());
		afterNegotiationTable.addCell(pdfLabel.get("tenderNegotiationpdf.aftneg")+negDate);
		afterNegotiationTable.getDefaultCell().setColspan(1);
		afterNegotiationTable.addCell(pdfLabel.get(TENDERNEGOTIATION_RATE));		
		afterNegotiationTable.addCell(pdfLabel.get(TENDERNEGOTIATION_AMOUNT));
		return afterNegotiationTable;
	}	
	
	// creating tables for after negotiation 
	public PdfPTable createMarketRateTable(TenderResponse tenderResponse)
	throws DocumentException,EGOVException{		
		String asOnDate="";		
		if(tenderResponse!=null && tenderResponse.getNegotiationDate()!=null)
			asOnDate=sdf.format(tenderResponse.getNegotiationDate());		
		PdfPTable marketRateTable =new PdfPTable(2);		
		marketRateTable.getDefaultCell().setBorderWidth(1);
		marketRateTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		marketRateTable.getDefaultCell().setColspan(2);
		marketRateTable.addCell(pdfLabel.get("tenderNegotiationpdf.marketratedate")+asOnDate);	
		marketRateTable.getDefaultCell().setColspan(1);
		marketRateTable.addCell(pdfLabel.get(TENDERNEGOTIATION_RATE));		
		marketRateTable.addCell(pdfLabel.get(TENDERNEGOTIATION_AMOUNT));
		return marketRateTable;
	}
	
	private String getDateInFormat(String date) throws DocumentException{
		String dateInFormat=null;
		try {
			dateInFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.US).format(new SimpleDateFormat("yyyy-MM-dd",Locale.US).parse(date));
		} catch (Exception e) {
			throw new DocumentException("Exception occured while parsing date := "+e);
		}
		return dateInFormat;		
	}
	
	//setter and getter
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public EmployeeService getemployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public Map<String,Object> processLatestRateAndQtyForLineItem(Collection<EstimateLineItemsForWP> estimateLinItems,Activity act){
		Map<String,Object> rateQtyMap=new HashMap<String,Object>();
		for(EstimateLineItemsForWP lineItem:estimateLinItems){
			if(lineItem.getActivity()!=null && lineItem.getActivity().getId()!=null && lineItem.getActivity().getId().longValue()==act.getId().longValue()){
				rateQtyMap.put("slno", lineItem.getSrlNo());
				rateQtyMap.put("desc", lineItem.getDescription());
				rateQtyMap.put("sorcode", lineItem.getCode());
				rateQtyMap.put("qty", lineItem.getQuantity());
				rateQtyMap.put("rate", lineItem.getRate());
				rateQtyMap.put("per", lineItem.getUom());				
				rateQtyMap.put("amt", lineItem.getAmt());
			}
		}
		return rateQtyMap;
	}	
	public String getWardList(WorksPackage wp){	
		Map<String,String> resultMap = new HashMap<String, String>();		
		List<String> wardnameList=new ArrayList<String>();
		for(AbstractEstimate ae:wp.getAllEstimates()){			
			if(ae.getWard()!=null && ae.getWard().getParent()!=null && ae.getWard().getParent().getName() !=null 
				&& !resultMap.containsKey(ae.getWard().getName())){					
				wardnameList.add(ae.getWard().getName()+"/"+ae.getWard().getParent().getName());
				resultMap.put(ae.getWard().getName(), ae.getWard().getParent().getName());
			}
		} 
		return StringUtils.join(wardnameList.toArray(),",");
	}

	public String getWorksPackgeReq() {
		return worksPackgeReq;
	}

	public void setWorksPackgeReq(String worksPackgeReq) {
		this.worksPackgeReq = worksPackgeReq;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
	
	
	/**
	 * create contractor table 
	 * @param tenderResponse
	 * @return
	 * @throws DocumentException
	 * @throws EGOVException
	 */
	private PdfPTable createContractorTable(TenderResponse tenderResponse) throws DocumentException,EGOVException{	
		PdfPTable contractorTable = new PdfPTable(3);
		contractorTable.setWidthPercentage(100);
		contractorTable.setWidths(new float[] {1.6f,3.6f,6.6f});
		try{
			contractorTable.getDefaultCell().setPadding(5);
			contractorTable.getDefaultCell().setBorderWidth(1);
			contractorTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			contractorTable.addCell(pdfLabel.get("tenderNegotiationpdf.contractorcode"));		
			contractorTable.addCell(pdfLabel.get("tenderNegotiationpdf.contractorname"));
			contractorTable.addCell(pdfLabel.get("tenderNegotiationpdf.contractoraddress"));
			
			for(TenderResponseContractors tenderResponseContractors:tenderResponse.getTenderResponseContractors()){
				contractorTable.addCell(centerPara(tenderResponseContractors.getContractor().getCode()));
				contractorTable.addCell(centerPara(tenderResponseContractors.getContractor().getName()));
				contractorTable.addCell(centerPara(tenderResponseContractors.getContractor().getCorrespondenceAddress()));
			}
	
		
		} 
		catch(Exception e){
			LOGGER.info("Exception while creating contractor table"+e);
	    }
		return contractorTable;
	}
	
	/**
	 * create negotiation table for every contractor 
	 * @param tenderResponse
	 * @return void
	 * @throws DocumentException
	 * @throws EGOVException
	 */
	private void createNegotiationTableForContractors(TenderResponse tenderResponse) throws DocumentException,EGOVException{	
		PdfPTable negotiationTable=null;
		int count=0;
		for(TenderResponseContractors tenderResponseContractors:tenderResponse.getTenderResponseContractors()){
			document.add(makePara(pdfLabel.get("tenderNegotiationpdf.contractorname")+" - " +tenderResponseContractors.getContractor().getName()+"("+tenderResponseContractors.getContractor().getCode()+" ) ",Element.ALIGN_LEFT));
			document.add(spacer());	
			negotiationTable = createNegotiationTable(tenderResponse,tenderResponseContractors.getContractor());			
			document.add(negotiationTable);
			count++;
			if(!(tenderResponse.getTenderResponseContractors().size()==count)){
				document.newPage();
			}
			else{
				document.add(spacer());	
			}
		}
	
	}

	public String getPercTenderType() {
		return percTenderType;
	}

	public void setPercTenderType(String percTenderType) {
		this.percTenderType = percTenderType;
	}
	
}

	
   
