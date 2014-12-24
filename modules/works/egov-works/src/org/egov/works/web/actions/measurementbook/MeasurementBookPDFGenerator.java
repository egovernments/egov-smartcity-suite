package org.egov.works.web.actions.measurementbook;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.State;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DeptDesig;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.tender.BidType;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.works.models.estimate.Activity;
import org.egov.works.models.revisionEstimate.RevisionType;
import org.egov.works.models.measurementbook.MBDetails;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrderActivity;
import org.egov.works.services.MeasurementBookService;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.AbstractPDFGenerator;
import org.egov.works.utils.WorksConstants;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author prashanth
 *
 */

public class MeasurementBookPDFGenerator extends AbstractPDFGenerator{
	private static final Logger logger = Logger.getLogger(MeasurementBookPDFGenerator.class);
	private PersistenceService persistenceService = new PersistenceService();
	private MeasurementBookService measurementBookService;
	private WorkOrderService workOrderService;
	public static final String MEASUREMENTBOOK_PDF_ERROR="measurementbook.pdf.error";
	private EmployeeService employeeService;
	private final Map<String,String> pdfLabel;
	private final MBHeader mbHeader;
	private final NumberFormat formatter = new DecimalFormat("#0.00");
	private final NumberFormat rateFormatter = new DecimalFormat("#0.0000000");
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
	private WorksService worksService;
	private GenericTenderService genericTenderService;
	private String mbPercentagelevel;
	private boolean isTenderPercentageType;
	private BigDecimal tenderPercentage;
	private BigDecimal totalCurrentCost;
	private BigDecimal totalCostForExtraItems;

	public MeasurementBookPDFGenerator(MBHeader mbHeader,
			OutputStream out,Map<String,String> pdfLabel){
		super(out, "landscape");
		this.pdfLabel=pdfLabel;
		this.mbHeader = mbHeader;
	}

	public void generatePDF(){
		mbPercentagelevel=worksService.getWorksConfigValue(WorksConstants.MBPERCENTAPPCONFIGKEY);
		totalCurrentCost=new BigDecimal(0);
		totalCostForExtraItems=new BigDecimal(0);
		GenericTenderResponse tenderResponse=null;
		if(mbHeader!=null && mbHeader.getWorkOrder()!=null){
		 tenderResponse=genericTenderService.getGenericResponseByNumber(mbHeader.getWorkOrder().getNegotiationNumber());
		}
		if(tenderResponse!=null && tenderResponse.getBidType().equals(BidType.PERCENTAGE)){
			isTenderPercentageType=true;
			tenderPercentage=tenderResponse.getPercentage();
		}
		else{
			isTenderPercentageType=false;
			tenderPercentage=BigDecimal.ZERO;
		}
		String headerText = pdfLabel.get("mbpdf.header");
		try {
			// start header Part
			Paragraph headerTextPara = new Paragraph(new Chunk(headerText,new Font(Font.UNDEFINED, LARGE_FONT, Font.BOLD)));
			headerTextPara.setAlignment(Element.ALIGN_CENTER);
			document.add(headerTextPara);
			document.add(spacer());
			if(mbHeader!=null) {
				String toPageno="";
				if(mbHeader.getToPageNo()==null || mbHeader.getToPageNo().intValue()==0)
					toPageno=mbHeader.getFromPageNo().toString();
				else
					toPageno=mbHeader.getToPageNo().toString();

				document.add(makePara(" \t  \t  \t  \t \t "+pdfLabel.get("mbpdf.refno")+mbHeader.getMbRefNo()
						+" \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t "
						+pdfLabel.get("mbpdf.pageno")+" : "+mbHeader.getFromPageNo()
						+ " to "
						+ toPageno
						+" \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t  \t \t  \t  \t "
						+ pdfLabel.get("mbpdf.date")
						+sdf.format(mbHeader.getMbDate()),Element.ALIGN_LEFT));

			}
			document.add(spacer());
			//creating label row
			PdfPTable mbTable=createMbTable();
			PdfPTable revisedActivityTable=createRevisedActivityTable();
			if(mbHeader!=null){
				mbTable=createMbData(mbTable,mbHeader);
				//if(mbHeader.getRevisionEstimate()!=null){
					revisedActivityTable=createRevisedActivityTable(revisedActivityTable,mbHeader);
			//}
				
			}
			document.add(mbTable);
			if((mbHeader!=null) && !revisedActivityTable.getRows().isEmpty() && revisedActivityTable.getRows().size()>1){
				document.newPage();
				if(mbHeader.getRevisionEstimate()!=null)
					document.add(makePara(" \t  \t  \t  \t \t Revised Estimate Number : "+mbHeader.getRevisionEstimate().getEstimateNumber()));
			document.add(spacer());
				document.add(revisedActivityTable);
				document.add(spacer());
			}else{
				document.add(spacer());
			}
			if(mbHeader!=null && tenderPercentage!=null &&  isTenderPercentageType==true && mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE) && tenderPercentage.doubleValue()!=0){
				document.add(makePara(pdfLabel.get("mbpdf.percentagerate")+" "+	tenderPercentage));
				document.add(spacer());
				if(tenderPercentage.compareTo(BigDecimal.ZERO)>=1){
					totalCurrentCost=totalCurrentCost.add(totalCurrentCost.multiply(tenderPercentage.divide(new BigDecimal(100)))).add(totalCostForExtraItems);
				}
				else{
					totalCurrentCost=totalCurrentCost.subtract(totalCurrentCost.multiply(tenderPercentage.multiply(new BigDecimal(-1)).divide(new BigDecimal(100)))).add(totalCostForExtraItems);
				}
				
				document.add(makePara(pdfLabel.get("mbpdf.netmbamount")+" "+	totalCurrentCost.setScale(2, BigDecimal.ROUND_HALF_UP)));
				document.add(spacer());
			}
			if(mbHeader!=null && mbHeader.getMbPreparedBy()!=null){
			document.add(makePara(pdfLabel.get("mbpdf.preparedby")+" "+
					mbHeader.getMbPreparedBy().getEmployeeName()));
				if(mbHeader.getMbPreparedBy().getUserMaster().getUserSignature()!=null){
					try{
						Image image=Image.getInstance(CryptoHelper.decrypt(mbHeader.getMbPreparedBy().getUserMaster().getUserSignature().getSignature(), CryptoHelper.decrypt(mbHeader.getMbPreparedBy().getUserMaster().getPwd())));
						if(image.getScaledHeight()>50 || image.getScaledWidth()>150 ){
							image.scaleToFit(50, 150);
					}
						document.add(image);
					}
					catch(Exception e){
						throw new DocumentException("Exception occured while getting signature "+e);
					}
				}
			}
			document.newPage();
			//approval details table
			
			PdfPTable approvaldetailsTable = null;
			if(mbHeader!=null)
			approvaldetailsTable = createApprovalDetailsTable(mbHeader);
			document.add(makePara(pdfLabel.get("mbpdf.approvaldetails")));	
			document.add(spacer());
			document.add(approvaldetailsTable);			
			
			document.close();
		} catch (DocumentException e) {
			throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	 throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,ex);
	    }
	}
	
	private PdfPTable createApprovalDetailsTable(MBHeader mbHeader)	throws DocumentException {
		try {
			PdfPTable approvaldetailsTable = new PdfPTable(6);
			approvaldetailsTable.setWidthPercentage(100);
			approvaldetailsTable.setWidths(new float[] { 1.5f, 1f, 1.1f, 1.3f,1f,2f });
			addRow(approvaldetailsTable, true, makePara(pdfLabel.get("mbpdf.aprvalstep")),
					centerPara(pdfLabel.get("mbpdf.name")), 
					centerPara(pdfLabel.get("mbpdf.designation")), 
					centerPara(pdfLabel.get("mbpdf.aprvdon")), 
					centerPara(pdfLabel.get("mbpdf.signature")),
					centerPara(pdfLabel.get("mbpdf.remarks")));	
			List<State> history =null;		
			String code="";
			if(mbHeader.getCurrentState()!=null && mbHeader.getCurrentState().getHistory()!=null)
				history=mbHeader.getCurrentState().getHistory();
			if(history!=null){
				Collections.reverse(history);
				for (State ad : history) {
					if(!ad.getValue().equals("NEW") && !ad.getValue().equals("END")){
						String nextAction="";
						if(ad.getNextAction()!=null)
							nextAction=ad.getNextAction();
						Integer positionId =null;
						String desgName=null;
						if(ad.getPrevious()==null){
							
							positionId = ad.getOwner().getId();
							DeptDesig deptdesig= ad.getOwner().getDeptDesigId();
							desgName = deptdesig.getDesigId().getDesignationName();
						}
						else{
							
							positionId =ad.getPrevious().getOwner().getId();
							DeptDesig deptdesig= ad.getPrevious().getOwner().getDeptDesigId();
							desgName = deptdesig.getDesigId().getDesignationName();
																		
						}
						try
						{
							PersonalInformation emp=employeeService.getEmpForPositionAndDate(ad.getCreatedDate(), positionId);
							if(ad.getValue().equals("END"))
								code = ad.getPrevious().getValue();
							else
								code = ad.getValue();
							EgwStatus status =(EgwStatus) getPersistenceService().find("from EgwStatus where moduletype='MBHeader' and code=?",code);
							String state=status.getDescription();
							if(!nextAction.equalsIgnoreCase(""))
								state=status.getDescription()+" - "+nextAction;
	
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
						catch(NullPointerException e)
						{
							// Code review implemented. But cannot get message from package.properties since we are passing two dynamic values in the message in helper class
							throw new EGOVRuntimeException(" The position  "+ad.getOwner().getName()+" does not have any employee attached to it in "+mbHeader.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getDeptName());
						}
					}
				}
			}
			return approvaldetailsTable;
		}catch (EGOVRuntimeException er) {
			throw er;
		}
		catch (Exception e) {
			throw new DocumentException("Exception occured while getting approval details "+e);
		}
	}
	
	private String getDateInFormat(String date) throws DocumentException
	{
		String dateInFormat=null;
		try {
			dateInFormat = new SimpleDateFormat("dd-MMM-yyyy",Locale.US).format(new SimpleDateFormat("yyyy-MM-dd",Locale.US).parse(date));
		} catch (Exception e) {
			throw new DocumentException("Exception occured while parsing date := "+e);
		}
		return dateInFormat;
		
	}

	// label row method definition
	private PdfPTable createMbTable() throws DocumentException,EGOVException{

		// main table
		PdfPTable mbTable = new PdfPTable(13);
		mbTable.setWidthPercentage(100);
		mbTable.setWidths(new float[] { 1f,1.5f,4f,1.9f,1.6f,1.4f,
				1.8f,1.4f,1.4f,1.9f,1.9f,1.9f,1.6f});
		try {
			mbTable.getDefaultCell().setPadding(5);
			mbTable.getDefaultCell().setBorderWidth(1);
			mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			mbTable.addCell(pdfLabel.get("mbpdf.slno"));
			mbTable.addCell(pdfLabel.get("mbpdf.schno"));
			mbTable.addCell(pdfLabel.get("mbpdf.descofwork"));
			mbTable.addCell(pdfLabel.get("mbpdf.completedmeasurement"));
			mbTable.addCell(pdfLabel.get("mbpdf.unitrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.unit"));
			mbTable.addCell(pdfLabel.get("mbpdf.reducedorpartrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.newrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.totalvalueofcomplwork"));

			// start creating tables for previous measurements
			PdfPTable previousMbTable = createPreviousMbTable();
			PdfPCell previousMbCell = new PdfPCell(previousMbTable);
			previousMbCell.setColspan(2);
			mbTable.addCell(previousMbCell);

			mbTable.addCell(pdfLabel.get("mbpdf.currentmeasurement"));

			//last column
			mbTable.addCell(pdfLabel.get("mbpdf.currentcost"));
		} catch (DocumentException e) {
			throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,ex);
	    }
		return mbTable;
	}

	private PdfPTable createRevisedActivityTable() throws DocumentException,EGOVException{

		// main table
		PdfPTable mbTable = new PdfPTable(14);
		mbTable.setWidthPercentage(100);
		mbTable.setWidths(new float[] { 1f,1.5f,4f,1.5f,1.9f,1.6f,1.4f,
				1.4f,1.4f,1.8f,1.9f,1.9f,1.9f,1.6f});
		try {
			mbTable.getDefaultCell().setPadding(5);
			mbTable.getDefaultCell().setBorderWidth(1);
			mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			mbTable.addCell(pdfLabel.get("mbpdf.slno"));
			mbTable.addCell(pdfLabel.get("mbpdf.schno"));
			mbTable.addCell(pdfLabel.get("mbpdf.descofwork"));
			mbTable.addCell(pdfLabel.get("mbpdf.revisionType"));
			mbTable.addCell(pdfLabel.get("mbpdf.completedmeasurement"));
			mbTable.addCell(pdfLabel.get("mbpdf.unitrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.unit"));
			mbTable.addCell(pdfLabel.get("mbpdf.reducedorpartrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.newrate"));
			mbTable.addCell(pdfLabel.get("mbpdf.totalvalueofcomplwork"));

			// start creating tables for previous measurements
			PdfPTable previousMbTable = createPreviousMbTable();
			PdfPCell previousMbCell = new PdfPCell(previousMbTable);
			previousMbCell.setColspan(2);
			mbTable.addCell(previousMbCell);

			mbTable.addCell(pdfLabel.get("mbpdf.currentmeasurement"));

			//last column
			mbTable.addCell(pdfLabel.get("mbpdf.currentcost"));
		} catch (DocumentException e) {
			throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,e);
		}catch (EGOVException ex) {
	    	throw new EGOVRuntimeException(MEASUREMENTBOOK_PDF_ERROR,ex);
	    }
		return mbTable;
	}

	
	// creating table for previous mb
	public PdfPTable createPreviousMbTable()
	throws DocumentException,EGOVException{
		PdfPTable previousMbTable =new PdfPTable(2);
		previousMbTable.getDefaultCell().setBorderWidth(1);
		previousMbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		previousMbTable.getDefaultCell().setColspan(2);
		previousMbTable.addCell(pdfLabel.get("mbpdf.previousmeasurement"));
		previousMbTable.getDefaultCell().setColspan(1);
		previousMbTable.addCell(pdfLabel.get("mbpdf.pageno"));
		previousMbTable.addCell(pdfLabel.get("mbpdf.measurements"));
		return previousMbTable;
	}


	//for creating mbheader data
	private PdfPTable createMbData(PdfPTable mbTable,MBHeader mbHeader)
	throws DocumentException,EGOVException{
		int i=0;
		double uomFactor=0.0;

		//iterating mbdetails
		for(MBDetails mbDetails : mbHeader.getMbDetails()){
			RevisionType revisionType=mbDetails.getWorkOrderActivity().getActivity().getRevisionType();
			if(revisionType==null){
			String description="";
			String per="";
			String schNo="";
			double currentMeasurement=0.0;
			currentMeasurement=mbDetails.getQuantity();
			mbTable.addCell(makePara(++i));
			//if(mbDetails!=null){
				WorkOrderActivity workOrderActivity=mbDetails.getWorkOrderActivity();
				Activity activity=workOrderActivity.getActivity();
				//peformActivity();
				if(activity!=null){
					if(activity.getSchedule()!=null
							&& activity.getSchedule().getCode()!=null)
						schNo=activity.getSchedule().getCode();
						mbTable.addCell(centerPara(schNo));
	
	//start  sor/non sor description
					mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
					if(activity.getSchedule()!=null
							&& activity.getSchedule().getDescription()!=null)
						description=activity.getSchedule().getDescription();
	
					if(activity.getNonSor()!=null
							&& activity.getNonSor().getDescription()!=null)
						description=activity.getNonSor().getDescription();
	
					mbTable.addCell(makePara(description,Element.ALIGN_LEFT));
	//end sor/non sor description
				}
				//for completedMeasurement area --------------->Cumulative quantity including current entry= Cumulative upto previous entry + Current MB entry
				//( cumulative MB  measurement  for line item) for selected MB including  MB entry
	
				double completedMeasurement=0.0;
				double cumlPrevMb=0.0;
				try{
				
					long woaId=0l;
					if(workOrderActivity.getId()!=null)
						woaId=workOrderActivity.getId();
					
						cumlPrevMb=measurementBookService.prevCumulativeQuantity(woaId,mbHeader.getId(),workOrderActivity.getActivity().getId());
	
				}catch(Exception e){					
					cumlPrevMb=0.0;
				}			
				completedMeasurement=cumlPrevMb + currentMeasurement;
				mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				mbTable.addCell(rightPara((completedMeasurement)));
				
				double approveRateWo=0.0;
					if(isTenderPercentageType==true && mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE)){
						if(mbDetails.getReducedRate()>0){
							approveRateWo=mbDetails.getReducedRate();
						}else{
							approveRateWo=workOrderActivity.getActivity().getRate().getValue();
						}
					}
					else{
						if(mbDetails.getReducedRate()>0){
							approveRateWo=mbDetails.getReducedRate();
						}else{
				approveRateWo=workOrderActivity.getApprovedRate();
						}
					}
				mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				mbTable.addCell(rightPara(approveRateWo));
				
	//start unit
				if(activity!=null){
				//  umofactor for conversion of rate and amount
					uomFactor =activity.getConversionFactor();
					logger.debug("----------uomFactor------------"+uomFactor);
						
					mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					if(activity.getSchedule()!=null
							&& activity.getSchedule().getUom()!=null
							&& activity.getSchedule().getUom().getUom()!=null)
						per=activity.getSchedule().getUom().getUom();
					if(activity.getNonSor()!=null
							&& activity.getNonSor().getUom()!=null
							&& activity.getNonSor().getUom().getUom()!=null)
						per=activity.getNonSor().getUom().getUom();
						mbTable.addCell(centerPara(per));
	//end start unit
				} //end of if activity
	
				//if(mbDetails.getPartRate()>0 || mbDetails.getReducedRate()>0){
					String newrate="N/A";
					double newrateValue=0.0;
					if(mbDetails.getPartRate()>0){
						newrate="PR";
						newrateValue=mbDetails.getPartRate();
					}else if(mbDetails.getReducedRate()>0){
						newrate="RR";
						newrateValue=mbDetails.getReducedRate();
					}
					mbTable.addCell(centerPara(newrate));
					mbTable.addCell(rightPara(formatter.format(newrateValue)));
				//}
				
			
	
	/*
	 measurementBookService.prevCumulativeAmount(workOrderActivity.getId());
	total work completed------->(completed mesurement(col 5) * rate) here rate is wo.getAprovedrate
	added uom factor on april4th 2010 
	*/
					double workCompleted=completedMeasurement*approveRateWo * uomFactor;
					mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					mbTable.addCell(rightPara(formatter.format(workCompleted)));
					
	//previous measurements a)pageno and b)measurements
	//a)Page no: for last  MB entry for  forline item---->page-no call api
					Integer frompageNo = null;
					Integer topageNo = null;
	
					MBHeader resultHeader = workOrderService.findLastMBPageNoForLineItem(workOrderActivity,mbHeader.getId());
					if(resultHeader != null) {
						frompageNo 	= resultHeader.getFromPageNo();
						topageNo 	= resultHeader.getToPageNo();
					}
	
					String pageNoInfo = "";
					if(frompageNo != null)
						pageNoInfo = resultHeader.getMbRefNo() + "/" +frompageNo.toString();
					if(topageNo != null)
						pageNoInfo = pageNoInfo +"-" + topageNo;
	
					mbTable.addCell(rightPara(pageNoInfo));
	//b)Cumulative measurement recorded for the previous MB entry for line item( Cumulative measurements-current MB entry)
					mbTable.addCell(rightPara(cumlPrevMb));
	
	//Current Finalised Measurements  a)Current MB entry  and b) Column6 Estimate Percentage
	//a)Current MB entry---->Measurements (Col5-8) i.e (area-previous measurement)
					//double finalCurMeasurement=area-prevMeasurement;
	
					mbTable.addCell(rightPara(currentMeasurement));
	
					//current cost
					double currentCost=0.0;
					currentCost=currentMeasurement*approveRateWo * uomFactor;
					totalCurrentCost=totalCurrentCost.add(new BigDecimal(currentCost));
					mbTable.addCell(rightPara(formatter.format(currentCost)));
				//} //end of if mbDetails
			}//end of for loop
		}
			return mbTable;
	}

	//for creating Revised Activity data
	private PdfPTable createRevisedActivityTable(PdfPTable mbTable,MBHeader mbHeader)
	throws DocumentException,EGOVException{
		int i=0;
		double uomFactor=0.0;

		//iterating mbdetails
		for(MBDetails mbDetails : mbHeader.getMbDetails()){
			RevisionType revisionType=mbDetails.getWorkOrderActivity().getActivity().getRevisionType();
			if(revisionType!=null){
				String description="";
				String per="";
				String schNo="";
				double currentMeasurement=0.0;
				currentMeasurement=mbDetails.getQuantity();
				mbTable.addCell(makePara(++i));
				//if(mbDetails!=null){
					WorkOrderActivity workOrderActivity=mbDetails.getWorkOrderActivity();
					Activity activity=workOrderActivity.getActivity();
					//peformActivity();
					if(activity!=null){
						if(activity.getSchedule()!=null
								&& activity.getSchedule().getCode()!=null)
							schNo=activity.getSchedule().getCode();
							mbTable.addCell(centerPara(schNo));
			
	//start  sor/non sor description
						mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
						if(activity.getSchedule()!=null
								&& activity.getSchedule().getDescription()!=null)
							description=activity.getSchedule().getDescription();
		
						if(activity.getNonSor()!=null
								&& activity.getNonSor().getDescription()!=null)
							description=activity.getNonSor().getDescription();
	
						mbTable.addCell(makePara(description,Element.ALIGN_LEFT));
	//end sor/non sor description
						mbTable.addCell(makePara(activity.getRevisionType().name(),Element.ALIGN_LEFT));

					}
	
					//for completedMeasurement area --------------->Cumulative quantity including current entry= Cumulative upto previous entry + Current MB entry
					//( cumulative MB  measurement  for line item) for selected MB including  MB entry
	
					double completedMeasurement=0.0;
					double cumlPrevMb=0.0;
					try{
					
						long woaId=0l;
						if(workOrderActivity.getId()!=null)
							woaId=workOrderActivity.getId();
						
						cumlPrevMb=measurementBookService.prevCumulativeQuantity(woaId,mbHeader.getId(),workOrderActivity.getActivity().getId());
	
					}catch(Exception e){					
						cumlPrevMb=0.0;
					}			
					completedMeasurement=cumlPrevMb + currentMeasurement;
					mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					mbTable.addCell(rightPara((completedMeasurement)));
					
					double approveRateWo=0.0;
					if(isTenderPercentageType==true && mbPercentagelevel.equalsIgnoreCase(WorksConstants.MBPERCENTCONFIGVALUE)){
						if(mbDetails.getReducedRate()>0){
							approveRateWo=mbDetails.getReducedRate();
						}else{
							approveRateWo=workOrderActivity.getActivity().getRate().getValue();
						}
					}
					else{
						if(mbDetails.getReducedRate()>0){
							approveRateWo=mbDetails.getReducedRate();
						}else{
					approveRateWo=workOrderActivity.getApprovedRate();
						}
					}

					mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
					mbTable.addCell(rightPara(approveRateWo));
					
	//start unit
					if(activity!=null){
					//  umofactor for conversion of rate and amount
						if(mbDetails.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.ADDITITONAL_QUANTITY) || mbDetails.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.REDUCED_QUANTITY)){
							uomFactor=1.0;
						}else{
						uomFactor =activity.getConversionFactor();
						}
							
						logger.debug("----------uomFactor------------"+uomFactor);
							
						mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						if(activity.getSchedule()!=null
								&& activity.getSchedule().getUom()!=null
								&& activity.getSchedule().getUom().getUom()!=null)
							per=activity.getSchedule().getUom().getUom();
						if(activity.getNonSor()!=null
								&& activity.getNonSor().getUom()!=null
								&& activity.getNonSor().getUom().getUom()!=null)
							per=activity.getNonSor().getUom().getUom();
							mbTable.addCell(centerPara(per));
	//end start unit
					} //end of if activity
	
				
					String newrate="N/A";
					double newrateValue=0.0;
					if(mbDetails.getPartRate()>0){
						newrate="PR";
						newrateValue=mbDetails.getPartRate();
					}else if(mbDetails.getReducedRate()>0){
						newrate="RR";
						newrateValue=mbDetails.getReducedRate();
					}
					mbTable.addCell(centerPara(newrate));
					mbTable.addCell(rightPara(formatter.format(newrateValue)));
			
	
	/*
 measurementBookService.prevCumulativeAmount(workOrderActivity.getId());
	total work completed------->(completed mesurement(col 5) * rate) here rate is wo.getAprovedrate
	added uom factor on april4th 2010 
	*/
				double workCompleted=completedMeasurement*approveRateWo * uomFactor;
				mbTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				mbTable.addCell(rightPara(formatter.format(workCompleted)));
				
	//previous measurements a)pageno and b)measurements
	//a)Page no: for last  MB entry for  forline item---->page-no call api
				Integer frompageNo = null;
				Integer topageNo = null;
	
				MBHeader resultHeader = workOrderService.findLastMBPageNoForLineItem(workOrderActivity,mbHeader.getId());
				if(resultHeader != null) {
					frompageNo 	= resultHeader.getFromPageNo();
					topageNo 	= resultHeader.getToPageNo();
				}
	
				String pageNoInfo = "";
				if(frompageNo != null)
					pageNoInfo = resultHeader.getMbRefNo() + "/" +frompageNo.toString();
				if(topageNo != null)
					pageNoInfo = pageNoInfo +"-" + topageNo;
	
				mbTable.addCell(rightPara(pageNoInfo));
	//b)Cumulative measurement recorded for the previous MB entry for line item( Cumulative measurements-current MB entry)
				mbTable.addCell(rightPara(cumlPrevMb));
	
	//Current Finalised Measurements  a)Current MB entry  and b) Column6 Estimate Percentage
	//a)Current MB entry---->Measurements (Col5-8) i.e (area-previous measurement)
				//double finalCurMeasurement=area-prevMeasurement;
	
				mbTable.addCell(rightPara(currentMeasurement));
	
				//current cost
				double currentCost=0.0;

					if((mbDetails.getWorkOrderActivity().getActivity().getParent()==null) || (mbDetails.getWorkOrderActivity().getActivity().getRevisionType()!=null && mbDetails.getWorkOrderActivity().getActivity().getRevisionType().equals(RevisionType.EXTRA_ITEM))){ 
				currentCost=currentMeasurement*approveRateWo * uomFactor;
						totalCostForExtraItems=totalCostForExtraItems.add(new BigDecimal(currentCost));
					}
					else{
						currentCost=currentMeasurement*approveRateWo;
						totalCurrentCost=totalCurrentCost.add(new BigDecimal(currentCost));
					}
					
				mbTable.addCell(rightPara(formatter.format(currentCost)));
			//} //end of if mbDetails
		}//end of for loop
		}
		return mbTable;
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
	public void setMeasurementBookService(
			MeasurementBookService measurementBookService) {
		this.measurementBookService = measurementBookService;
	}
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}

	public GenericTenderService getGenericTenderService() {
		return genericTenderService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public BigDecimal getTotalCurrentCost() {
		return totalCurrentCost;
	}

	public void setTotalCurrentCost(BigDecimal totalCurrentCost) {
		this.totalCurrentCost = totalCurrentCost;
	}

	public BigDecimal getTenderPercentage() {
		return tenderPercentage;
	}

	public void setTenderPercentage(BigDecimal tenderPercentage) {
		this.tenderPercentage = tenderPercentage;
	}

	public BigDecimal getTotalCostForExtraItems() {
		return totalCostForExtraItems;
	}

	public void setTotalCostForExtraItems(BigDecimal totalCostForExtraItems) {
		this.totalCostForExtraItems = totalCostForExtraItems;
	}
}
