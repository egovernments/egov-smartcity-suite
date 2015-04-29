package org.egov.works.web.actions.reports;
/**
 * 
 */
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infstr.models.GeoLatLong;
import org.egov.infstr.models.GeoLocation;
import org.egov.infstr.models.Money;
import org.egov.infstr.services.GeoLocationConstants;
import org.egov.infstr.utils.NumberUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.tender.SetStatus;
import org.egov.works.models.tender.TenderEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.TenderResponseActivity;
import org.egov.works.models.tender.WorksPackageDetails;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.services.WorkOrderService;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class WorksGISReportAction extends BaseFormAction {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER=Logger.getLogger(WorksGISReportAction.class);
	private String AFTER_SEARCH = "afterSearch";
	private String GMAP = "gmap";
	private Integer zoneId;
	private Integer wardId;
	private Long parentCategory;
	private Long category;
	private Long expenditureType;
	private Long contractorId;
	private WorkOrderService workOrderService;
	private List<GeoLocation> locationList;
	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String resultStatus="beforeSearch";
	private String estimatenumber;
	private WorksService worksService;
	private List<String> tenderTypeList=null;
	@Autowired
        private BoundaryService boundaryService;
	
	public Long getContractorId() {
		return contractorId;
	}
	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	@Override
	public Object getModel() {
		return null;
	}
	public void prepare()
	{
		super.prepare();	
		List<Boundary> zoneList=(List<Boundary>)persistenceService.findAllBy("from BoundaryImpl BI  where upper(BI.boundaryType.name) = 'ZONE' order by BI.id");
		addDropdownData("zoneList",zoneList);
		addDropdownData("wardList",Collections.emptyList());
		addDropdownData("typeList",  getPersistenceService().findAllBy("from WorkType dt"));
		addDropdownData("parentCategoryList", getPersistenceService().findAllBy("from EgwTypeOfWork etw1 where etw1.parentid is null"));
		addDropdownData("categoryList", Collections.emptyList());
	}

	public String beforeSearch()
	{
		return GMAP;
	}
	
	/** Return values in the order - estimate id[0], lat[1], lon[2], estimate Number[3], name[4], work value[5],overheads[6],wpno[7]
	 *  tender document released date[8] , type of work[9], workSubType[10], projcode.id[11]
	 *  In case contractor is selected in search criteria, 
	 *  the following work order related values are returned from this query:
	 *  work order date[12], contract period[13], contractor name[14], work commenced date[15] 
	 * @return
	 */
	private String generateQuery()
	{
		StringBuffer query=new StringBuffer(1024);
		String columnsToShow=" absEst.id, absEst.lat, absEst.lon, absEst.estimateNumber,absEst.name, coalesce(absEst.workValue,0), " +
				" (select sum(coalesce(ovr.amount,0)) from OverheadValue ovr where ovr.abstractEstimate=absEst)  , " +
				" (select wpd.worksPackage.wpNumber from WorksPackageDetails wpd where wpd.estimate=absEst and wpd.worksPackage.egwStatus.code not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"') ), " +
				" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from SetStatus offLineStatus where offLineStatus.objectId = ( select wpd.worksPackage.id from WorksPackageDetails wpd where wpd.estimate=absEst and wpd.worksPackage.egwStatus.code not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"')  )" +
				" and offLineStatus.egwStatus.code='"+WorksConstants.TENDER_DOCUMENT_RELEASED+"' and objectType='WorksPackage' ), " +
				" absEst.parentCategory.description, workSubType.description, projcode.id ";
		String columnsToShowWhenWOEIsJoined=" absEst.id, absEst.lat, absEst.lon, absEst.estimateNumber,absEst.name, coalesce(absEst.workValue,0)," +
				" (select sum(coalesce(ovr.amount,0)) from OverheadValue ovr where ovr.abstractEstimate=absEst)  ," +
				" (select wpd.worksPackage.wpNumber from WorksPackageDetails wpd where wpd.estimate=absEst  and wpd.worksPackage.egwStatus.code not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"') ), " +
				" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from SetStatus offLineStatus where offLineStatus.objectId = ( select wpd.worksPackage.id from WorksPackageDetails wpd where wpd.estimate=absEst and wpd.worksPackage.egwStatus.code not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"') )" +
				" and offLineStatus.egwStatus.code='"+WorksConstants.TENDER_DOCUMENT_RELEASED+"' and objectType='WorksPackage' ), " +
				" absEst.parentCategory.description, workSubType.description , projcode.id,  " +
				" to_char(wo.workOrderDate,'dd/MM/YYYY'), wo.contractPeriod, wo.contractor.name, " +
				" (select to_char(offLineStatus.statusDate,'dd/MM/YYYY') from SetStatus offLineStatus where offLineStatus.objectId= wo.id  " +
				"    and offLineStatus.egwStatus.code='"+WorksConstants.WO_STATUS_WOCOMMENCED+"' and objectType='WorkOrder') ";
			if(contractorId!=null && contractorId!=-1)
			{
				query.append("select "+columnsToShowWhenWOEIsJoined+" from AbstractEstimate absEst left join absEst.category workSubType left join absEst.projectCode projcode , WorkOrder wo, WorkOrderEstimate woe ");
				query.append(" where  absEst.id=woe.estimate.id and wo.id=woe.workOrder.id  and wo.contractor.id="+contractorId);
				query.append(" and absEst.parent is null "  );
			}
			else
			{
				query.append("select "+columnsToShow+" from AbstractEstimate absEst left join absEst.category workSubType left join absEst.projectCode projcode ");
				query.append(" where absEst.parent is null "  );
			}
			//Consider only estimates which have lat long
			query.append(" and absEst.lat is not null and absEst.lon is not null and absEst.egwStatus.code not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"') "  );
			if(zoneId!=null && zoneId!=-1)
			{
				query.append(" and absEst.ward.parent.id="+zoneId);
			}
			if(wardId!=null && wardId!=-1)
			{
				query.append(" and absEst.ward.id="+wardId);
			}
			if(category!=null && category!=-1)
			{
				query.append(" and absEst.category.id="+category);
			}else if(parentCategory!=null && parentCategory!=-1)
			{
				query.append(" and absEst.parentCategory.id="+parentCategory);
			}
			if(expenditureType!=null && expenditureType!=-1)
			{
				query.append(" and absEst.type.id="+expenditureType);
			}
			if(StringUtils.isNotBlank(estimatenumber))
			{
				query.append(" and UPPER(absEst.estimateNumber) like '%"+estimatenumber.toUpperCase()+"%'");
			}
			query.append(" order by absEst.id ");
			return query.toString();
	}
	
	public String search()
	{
		List<Object[]> findAll=null;
		locationList = new ArrayList<GeoLocation>();
		GeoLatLong latlong=new GeoLatLong();
		Map<String,Object> markerdata=null;
		String query=generateQuery();
		try {
			LOGGER.info("HQl query="+query.toString());
			findAll = persistenceService.findAllBy(query.toString());
			LOGGER.info("HQl query RESULT "+findAll.size());
			Long estId, projcodeId, contractPeriod;
			BigDecimal estLat , estLon;
			String  estNumber,estWorkName,wpno,tenderDocReleasedDt,typeOfWork, subTypeOfWork, workOrderDate,contractorName, workCommencedDt;
			Money workValue;
			Double ovrheads;
			//Start format and push the data
			GeoLocation geoLocation=null;
			for(Object[] columnOutput:findAll)
			{
				//Reset the values
				estId= projcodeId= contractPeriod=null;
				estLat = estLon =null;
				estNumber= estWorkName=wpno=tenderDocReleasedDt=typeOfWork= subTypeOfWork=workOrderDate=contractorName=workCommencedDt=null;
				workValue=null;
				ovrheads=null;
				
				//Set the values
				estId = (Long)columnOutput[0];
				estLat = (BigDecimal)columnOutput[1];
				estLon = (BigDecimal)columnOutput[2];
				estNumber=(String)columnOutput[3];
				estWorkName=(String)columnOutput[4];
				workValue=(Money)columnOutput[5];
				ovrheads=(Double)columnOutput[6];
				wpno=(String)columnOutput[7];
				tenderDocReleasedDt=(String)columnOutput[8];
				typeOfWork=(String)columnOutput[9];
				subTypeOfWork =(String)columnOutput[10];
				projcodeId=(Long)columnOutput[11];
				
				geoLocation=new GeoLocation();
				if(null !=estLat && null !=estLon)  
				{
				    latlong = new GeoLatLong();
				    latlong.setLatitude(estLat);
				    latlong.setLongitude(estLat);
				}
				geoLocation.setGeoLatLong(latlong);
				//**geoLocation.setUseNiceInfoWindow(true);
				geoLocation.setUrlRedirect("../estimate/abstractEstimate!edit.action?id="+estId+"&sourcepage=search~"+estNumber);
				String max50CharName="";
				if(null!=estWorkName && estWorkName.length()>=50)
				{
					max50CharName=estWorkName.substring(0, 50)+"...";
				}else
				{
					max50CharName=estWorkName;
				}
				geoLocation.setInfo2("Work Name="+max50CharName);
				if(workValue!=null ||ovrheads!=null)
				{
					BigDecimal amt = workValue!=null?new BigDecimal(workValue.getValue()):BigDecimal.ZERO;
					amt=amt.add(ovrheads!=null?new BigDecimal(ovrheads):BigDecimal.ZERO);
					geoLocation.setInfo3("Estimate Value(Rs)="+NumberUtil.formatNumber(amt,NumberUtil.NumberFormatStyle.CRORES));
				}
				else
					geoLocation.setInfo3("Estimate Value(Rs)=0.00");
				if(wpno!=null )
					geoLocation.setInfo4("Works Package Number="+wpno);
				markerdata=new HashMap<String,Object>();
				if(typeOfWork!=null )
				{
					geoLocation.appendToInfo5("Type of Work="+typeOfWork);
					if(typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_BRIDGES))
						markerdata.put("icon",GeoLocationConstants.MARKEROPTION_ICON_PURPLE);
					else if(typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_BUILDINGS))
						markerdata.put("icon",GeoLocationConstants.MARKEROPTION_ICON_YELLOW);
					else if(typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_ELECTRICAL))
						markerdata.put("icon",GeoLocationConstants.MARKEROPTION_ICON_GREEN);
					else if(typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_ROADS))
						markerdata.put("icon",GeoLocationConstants.MARKEROPTION_ICON_ORANGE);
					else if(typeOfWork.equalsIgnoreCase(WorksConstants.TYPE_OF_WORK_STORMWATER_DRAIN))
						markerdata.put("icon",GeoLocationConstants.MARKEROPTION_ICON_BLUE);
				}
				if(subTypeOfWork!=null )
					geoLocation.appendToInfo5("Subtype of Work="+subTypeOfWork);
				if(tenderDocReleasedDt!=null)
					geoLocation.appendToInfo5("Tender Document Released Date="+tenderDocReleasedDt);
				String tenderAmount = getTenderAmount(estId);
				if(StringUtils.isNotBlank(tenderAmount))
					geoLocation.appendToInfo5("Tender Finalized Value(Rs)="+tenderAmount);
				if(contractorId!=null && contractorId!=-1)
				{
					workOrderDate =(String)columnOutput[12];
					if(columnOutput[13]!=null)
						contractPeriod=Long.parseLong(columnOutput[13].toString());
					contractorName=(String)columnOutput[14];
					workCommencedDt=(String)columnOutput[15];
					if(workOrderDate!=null )
						geoLocation.appendToInfo5("Work Order Date="+workOrderDate);
					if(workCommencedDt!=null )
						geoLocation.appendToInfo5("Work Commenced Date="+workCommencedDt);
					if(contractPeriod!=null )
						geoLocation.appendToInfo5("Contract Period In Days="+contractPeriod);
					if(workCommencedDt!=null  && contractPeriod!=null )
					{
						Date woDate = new Date(workCommencedDt);
						Calendar cal = Calendar.getInstance();
						cal.setTime(woDate);
						cal.add(Calendar.DATE, new Long(contractPeriod).intValue());
						geoLocation.appendToInfo5("Expected Date Of Completion ="+sdf.format(cal.getTime()));
					}
					if(projcodeId!=null  )
					{
						String paymentAmt = getPaymentAmount(projcodeId);
						if(paymentAmt !=null)
							geoLocation.appendToInfo5("Payment Released(Rs)="+paymentAmt );
					}
					if(contractorName!=null)
						geoLocation.appendToInfo5("Contractor Name="+contractorName);
				}
				else
				{
					List<WorkOrder> woList = persistenceService.findAllBy("  select woe.workOrder from WorkOrderEstimate woe where woe.estimate.id=? and upper(woe.workOrder.egwStatus.code) not in ('"+WorksConstants.NEW+"','"+WorksConstants.CANCELLED_STATUS+"') ", estId);
					if(woList!=null && woList.size()>0)
					{
						StringBuffer  workOrderDatesBuf = new StringBuffer("");
						StringBuffer  workCommencedDateBuf = new StringBuffer("");
						StringBuffer  contractorNameBuf =  new StringBuffer("");
						StringBuffer  workExpectedCompletionDateBuf = new StringBuffer("");
						StringBuffer  workOrderContractPeriodBuf = new StringBuffer("");
						for(WorkOrder wo :woList)
						{
							Long  workOrderContractPeriod = null;
							workOrderDatesBuf.append( sdf.format(wo.getWorkOrderDate())+",");
							contractorNameBuf.append(wo.getContractor().getName()+",");
							if(wo.getContractPeriod()!=null)
							{
								workOrderContractPeriod = Long.parseLong(wo.getContractPeriod());
								workOrderContractPeriodBuf.append(wo.getContractPeriod()+",");
							}
							else
								workOrderContractPeriodBuf.append("NA,");
							boolean workCommencedPresent=false;
							boolean workExpectedCompletedPresent=false;
							for(SetStatus ss:wo.getSetStatuses())
							{
								if(ss.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.WO_STATUS_WOCOMMENCED))
								{
									workCommencedDateBuf.append(sdf.format(ss.getStatusDate())+",");
									workCommencedPresent=true;
									if(workOrderContractPeriod!=null)
									{
										Calendar cal = Calendar.getInstance();
										cal.setTime(wo.getWorkOrderDate());
										cal.add(Calendar.DATE, workOrderContractPeriod.intValue());
										workExpectedCompletionDateBuf.append(sdf.format(cal.getTime())+",");
										workExpectedCompletedPresent=true;
									}
								}
							}
							if(!workCommencedPresent)
								workCommencedDateBuf.append("NA,");
							if(!workExpectedCompletedPresent)
								workExpectedCompletionDateBuf.append("NA,");
						}
						geoLocation.appendToInfo5("Work Order Date="+workOrderDatesBuf.deleteCharAt(workOrderDatesBuf.lastIndexOf(",")).toString());
						if(StringUtils.isNotBlank(workCommencedDateBuf.toString()))
							geoLocation.appendToInfo5("Work Commenced Date="+workCommencedDateBuf.deleteCharAt(workCommencedDateBuf.lastIndexOf(",")).toString());
						if(StringUtils.isNotBlank(workOrderContractPeriodBuf.toString()))
							geoLocation.appendToInfo5("Contract Period In Days="+workOrderContractPeriodBuf.deleteCharAt(workOrderContractPeriodBuf.lastIndexOf(",")).toString());
						if(StringUtils.isNotBlank(workExpectedCompletionDateBuf.toString()))
							geoLocation.appendToInfo5("Expected Date Of Completion="+workExpectedCompletionDateBuf.deleteCharAt(workExpectedCompletionDateBuf.lastIndexOf(",")).toString());
						if(projcodeId!=null  )
						{
							String paymentAmt = getPaymentAmount(projcodeId);
							if(paymentAmt !=null)
								geoLocation.appendToInfo5("Payment Released(Rs)="+paymentAmt );
						}
						geoLocation.appendToInfo5("Contractor Name="+ contractorNameBuf.deleteCharAt(contractorNameBuf.lastIndexOf(",")).toString());
					}
				}
				geoLocation.setMarkerOptionData(markerdata);
				locationList.add(geoLocation);
			}
			ServletActionContext.getRequest().setAttribute("kmlfilename","coczone");
			ServletActionContext.getRequest().setAttribute(GeoLocationConstants.GEOLOCATIONLIST_ATTRIBUTE, locationList);
			resultStatus = AFTER_SEARCH;
			if(locationList!=null && locationList.size()>=1)
			{
				return GMAP;   
			}
		} catch (Exception e) {
			LOGGER.error(e,e);
		}
		return GMAP;
	}
	
	private String getTenderAmount(Object estimateId) {
		List<WorksPackageDetails> wpdDetailsList = persistenceService.findAllBy("select wpd from WorksPackageDetails wpd where wpd.estimate.id=?  ",(Long) estimateId);
		if(tenderTypeList==null || tenderTypeList.size()==0) 
			tenderTypeList= worksService.getTendertypeList();
		double totalAmt=0;
		if(wpdDetailsList!=null)
		{
			for(WorksPackageDetails wpd : wpdDetailsList) {
				for(TenderEstimate te : wpd.getWorksPackage().getTenderEstimateSet()) {
					for(TenderResponse tr : te.getTenderResponseSet()) {
						//Consider only approved Tender Response
						if(WorksConstants.APPROVED.equals(tr.getEgwStatus().getCode())) {
							List<TenderResponseActivity> trAct = 
									persistenceService.findAllBy("from TenderResponseActivity trAct where trAct.activity.abstractEstimate.id=? and trAct.tenderResponse.id=? and trAct.tenderResponse.egwStatus.code='"+WorksConstants.APPROVED+"' "
											, wpd.getEstimate().getId(), tr.getId());
							
							for(TenderResponseActivity act : trAct) {
									if(tr.getTenderEstimate().getTenderType().equals(tenderTypeList.get(1))) { //Item Rate
										totalAmt+=act.getNegotiatedQuantity() * act.getNegotiatedRate() * act.getActivity().getConversionFactor();
									}
							}
							
							if(tr.getTenderEstimate().getTenderType().equals(tenderTypeList.get(0))) { //Percentage Rate
								   totalAmt+=wpd.getEstimate().getWorkValue().getValue()+((wpd.getEstimate().getWorkValue().getValue() * tr.getPercNegotiatedAmountRate())/100);
							}
							break;
						}
					}
				}
			}
		}
		if(totalAmt!=0)
			return NumberUtil.formatNumber(new BigDecimal(totalAmt),NumberUtil.NumberFormatStyle.CRORES);
		else
			return null;
	}
	private String getPaymentAmount(Object object) {
		try {
			BigDecimal amt = worksService.getTotalPaymentForProjectCode((Long)object);
			if(amt!=null)
				return NumberUtil.formatNumber(amt,NumberUtil.NumberFormatStyle.CRORES);
			else
				return null;
		} catch (EGOVException e) {
			return null;
		}
	}
	public Map<String,Object> getContractorForApprovedWorkOrder() {
		Map<String,Object> contractorsWithWOList = new HashMap<String, Object>();		
		if(workOrderService.getContractorsWithWO()!=null) {
			for(Contractor contractor :workOrderService.getContractorsWithWO()){ 
				contractorsWithWOList.put(contractor.getId()+"", contractor.getName()+" - "+contractor.getCode());
			}			
		}
		return contractorsWithWOList; 
	}
	
	public void setWorkOrderService(WorkOrderService workOrderService) {
		this.workOrderService = workOrderService;
	}

	public Integer getZoneId() {
		return zoneId;
	}
	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}
	public Integer getWardId() {
		return wardId;
	}
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public Long getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Long parentCategory) {
		this.parentCategory = parentCategory;
	}
	public Long getCategory() {
		return category;
	}
	public void setCategory(Long category) {
		this.category = category;
	}
	public Long getExpenditureType() {
		return expenditureType;
	}
	public void setExpenditureType(Long expenditureType) {
		this.expenditureType = expenditureType;
	}
	public List<GeoLocation> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<GeoLocation> locationList) {
		this.locationList = locationList;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public String getEstimatenumber() {
		return estimatenumber;
	}
	public void setEstimatenumber(String estimatenumber) {
		this.estimatenumber = estimatenumber;
	}
	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
}