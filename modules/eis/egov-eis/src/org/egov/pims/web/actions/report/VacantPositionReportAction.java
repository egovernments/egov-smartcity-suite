package org.egov.pims.web.actions.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.Position.Bias;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.pagination.PaginatedList;
import org.egov.infstr.reporting.engine.ReportConstants.FileFormat;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.masters.model.BillNumberMaster;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.reports.services.EisReportService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.reporting.engine.ReportService;
import org.apache.struts2.dispatcher.StreamResult;

/*@Results({ 
	@Result(name = "reportview", type = StreamResult.class, value = "inputStream", params = { "contentType", "${contentType}", "contentDisposition", "attachment; filename=${fileName}" })
	})
*/
@Results({
	@Result(name = "reportView", type = "stream", location = "fileStream", params = {
	"contentType", "${contentType}", "contentDisposition",
	"attachment; filename=${fileName}" }) 
	})
@SuppressWarnings("serial")
@ParentPackage("egov")

public class VacantPositionReportAction extends SearchFormAction  {
	private static final Logger LOGGER = Logger
			.getLogger(VacantPositionReportAction.class);
	
	private EisReportService eisReportService;
	PersistenceService<BillNumberMaster, Integer> billNumberMasterService;
	protected static final String SEARCH = "search";
	
	private Integer department;
	private String billNumber;
	private Integer billNumberId;
	private String designation;
	private Integer designationId;
	Date primaryAssignmentDate;
	private List<DesignationMaster> designationList = new ArrayList<DesignationMaster>();
	private static final String DESIGNATION_RESULTS   = "designationResults";
	private String query ;
	private PaginatedList vacantPosPaginatedList;	
	private String contentType;
	private String fileName;
	private String fileFormat;
	private InputStream fileStream;
	private ReportService reportService;
	private ReportOutput ro;
	private static final String VANCANT_POSITION = "VacantPositionReport";
	private List<BillNumberMaster> billNoList;
	private Integer departmentId ;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		// TODO Auto-generated method stub
		List<Object> paramList = null;
		String query=null;
		String countQuery=null;
        Map map=prepareVacantPositionQuery(department,primaryAssignmentDate,designationId,billNumberId);
        paramList=(List<Object>)map.get("params");
        query=(String) map.get("query");
        return new  SearchQuerySQL(query,"select count(*) from ( "+query+" )",(List)map.get("params"));
	}
	
	public void prepare() {

		LOGGER.info("Prepare");
		addDropdownData("departmentList", eisReportService.getAllDepartments());
		addDropdownData("billNoList",Collections.EMPTY_LIST);
	}  
	@SkipValidation
	public String searchForm() {
		LOGGER.info("Inside search Form");
		return SEARCH;

	}
	
	public String exportToRequestedFile()
	{
		super.search();      
		List<VacantPositionDTO> dedList=getDTOList(searchResult.getList()); 
		searchResult.getList().clear();
		searchResult.getList().addAll(dedList);
		setVacantPosPaginatedList(searchResult);
		if(dedList.size() >0)
		{
		if (fileFormat != null && (!FileFormat.HTM.name().equals(fileFormat)) ) 
		{   
			String header= null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
			if(billNumberId != 0 )
	        {
				reportParams.put("billNo","Bill Number : "+billNumber);
	        }else{
	        	reportParams.put("billNo","");
	        }
		
			ReportRequest reportInput = new ReportRequest(VANCANT_POSITION, getVacantPosPaginatedList().getList(),reportParams);
			LOGGER.debug("fileFormat>>"+fileFormat);
			reportInput.setReportFormat(FileFormat.valueOf(fileFormat));
			this.contentType = ReportViewerUtil.getContentType(FileFormat.valueOf(fileFormat));
			this.fileName = VANCANT_POSITION+"." + fileFormat.toLowerCase();
			ro = reportService.createReport(reportInput);
			return "reportView";
		}
		}
		loadBillNoByDepartment(department);
		return SEARCH;
	}
	
	
	private List getDTOList(List<Object[]> objArrList)
	{
		List<VacantPositionDTO> dtoList = new ArrayList<VacantPositionDTO>();
		String empcode="";
		for (Object[] objArray : objArrList){
			
			VacantPositionDTO vacantPositionHelper = new VacantPositionDTO() ;
			vacantPositionHelper.setDesignationName((String) objArray[0]);
			vacantPositionHelper.setSanctionPosts( (BigDecimal) objArray[1]);
			vacantPositionHelper.setWorkingPosts((BigDecimal) objArray[2]);
			vacantPositionHelper.setVacantPosts((BigDecimal)objArray[3]);
			dtoList.add(vacantPositionHelper);
		}
		return dtoList;
	}
	
	
	private Map<String,Object > prepareVacantPositionQuery(Integer  departmentId, Date assDate, Integer designationId , Integer billNumberId )
	{
		Map<String,Object > queryMap=new HashMap<String,Object >();
		StringBuffer vacantPosQuery = null ;
		if(billNumberId == 0 )
		  vacantPosQuery = new StringBuffer(getQueryWithoutBillNO());
		if(billNumberId != 0)
		  vacantPosQuery = new StringBuffer(getQueryWithBillNO());
		
		List<Object> paramList = new ArrayList<Object>();
		
		paramList.add(departmentId);
		paramList.add( new java.sql.Date(assDate.getTime()));
		paramList.add( new java.sql.Date(assDate.getTime()));
		paramList.add( new java.sql.Date(assDate.getTime()));
		if(designationId != null)
			paramList.add(Integer.valueOf(designationId));
		if(billNumberId != 0 )
			paramList.add(Integer.valueOf(billNumberId));
		paramList.add(departmentId);
		if(designationId != null)
			paramList.add(Integer.valueOf(designationId));
		if(billNumberId != 0 )
			paramList.add(Integer.valueOf(billNumberId));
		queryMap.put("query",vacantPosQuery.toString());
		queryMap.put("params", paramList);
		return queryMap;
	}
	
	private String getQueryWithoutBillNO()
	{
		
	    String designationCond1 ="";
	    String designationCond2 ="";
		
		 if(designationId != null)
		 {
			 designationCond1 = "  AND empinf.designationid= ? ";
			 designationCond2  = " and d.DESIG_ID = ?  ";
		 }
		StringBuffer vacantPosQuery = new StringBuffer("select designation,sum(sactionpos) as sactioned,sum(post) as working,(sum(sactionpos)-sum(post)) as vacant " +
				" from(" +
				" SELECT   desig.designation_name AS designation," +
				"          0 AS sactionpos,COUNT (empinf.ID) AS post" +
				"    FROM eg_eis_employeeinfo empinf," +
				"         eg_position pos," +
				"         egeis_deptdesig deptdesig," +
				"         eg_designation desig" +
				"   WHERE empinf.pos_id = pos.ID" +
				"     AND pos.id_deptdesig = deptdesig.ID" +
				"     AND desig.designationid = empinf.designationid" +
				"     AND empinf.dept_id = ?" +
				"     AND (   (empinf.TO_DATE IS NULL AND empinf.from_date <= ?)" +
				"           OR (empinf.from_date <= ? AND empinf.TO_DATE >= ?)" +
				"     )" +
				"     AND empinf.is_primary = 'Y'" +
				"	 and empinf.isactive=1" 
			    + designationCond1
				+"  GROUP BY desig.designation_name " +
				"  union" +
				"  select desig.designation_name AS designation,d.sanctioned_posts AS sactionpos, 0 as post" +
				"  from egeis_deptdesig d,eg_designation desig" +
				"  where  desig.DESIGNATIONID=d.DESIG_ID" +
				"        and d.DEPT_ID=?" 
				+ designationCond2
				+ ")" +
				" group by designation" +
				" order by designation");
	 return vacantPosQuery.toString();	
	}
	
	private String getQueryWithBillNO()
	{
		String billNumberCond1 = "";
	    String billNumberCond2 = "";
	    String designationCond1 ="";
	    String designationCond2 ="";
		 if(billNumberId != 0){
	        	billNumberCond1 = " and pos.id_billnumber = ?";
	        	billNumberCond2 =" and p.id_billnumber= ? ";
	        	
	        }
		 if(designationId != null)
		 {
			 designationCond1 = " AND empinf.designationid = ?";
			 designationCond2  = " and depdes.desig_id= ? ";
		 }
		StringBuffer vacantPosQuery = new StringBuffer("select designation,sum(sactionpos) as sactioned,sum(post) as working,(sum(sactionpos)-sum(post)) as vacant" +
				" from(" +
				" SELECT   desig.designation_name AS designation," +
				"          0 AS sactionpos,COUNT (empinf.ID) AS post" +
				"  FROM eg_eis_employeeinfo empinf," +
				"         eg_position pos," +
				"         egeis_deptdesig deptdesig," +
				"         eg_designation desig" +
				"   WHERE empinf.pos_id = pos.ID" +
				"     AND pos.id_deptdesig = deptdesig.ID" +
				"     AND desig.designationid = empinf.designationid" +
				"      AND empinf.dept_id = ?" +
				"     AND (   (empinf.TO_DATE IS NULL AND empinf.from_date <= ?)" +
				"           OR (empinf.from_date <= ? AND empinf.TO_DATE >= ?)" +
				"     )" +
				"     AND empinf.is_primary = 'Y' and empinf.isactive=1" 
				+  designationCond1 
				+  billNumberCond1
			    + "  GROUP BY desig.designation_name" +
				"  union" +
				" select desig.designation_name AS designation,count(*) AS sanctionpos, 0 as post" +
				" from eg_position p,eg_designation desig,egeis_deptdesig depdes" +
				" where   depdes.id=p.ID_DEPTDESIG" +
				"   and depdes.DESIG_ID=desig.DESIGNATIONID" +
				"   and depdes.DEPT_ID=?"
				+ designationCond2
				+ billNumberCond2
				+" group by desig.designation_name,p.id_billnumber" +
				" )" +
				" group by designation" +
				" order by designation");
		return vacantPosQuery.toString() ;
	}
	
	private  void loadBillNoByDepartment(Integer deptId)
	{
		if(deptId != 0)
		  addDropdownData("billNoList",persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				deptId));
	   else
		  addDropdownData("billNoList",Collections.EMPTY_LIST);
		
	}
	
	
	public String getDesignations() {

		if (StringUtils.isNotBlank(query)) {
			designationList=eisReportService.getDesignationList(query);
		}

		return DESIGNATION_RESULTS;
	}
	
	@SkipValidation
	public String getBillNumberListByDepartment()
	{
		billNoList = persistenceService.findAllBy("from BillNumberMaster b where  b.department.id = ? ",
				departmentId);
		return  "result";
	}
	
	public Integer getDepartment() {
		return department;
	}

	public void setDepartment(Integer department) {
		this.department = department;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getBillNumberId() {
		return billNumberId;
	}

	public void setBillNumberId(Integer billNumberId) {
		this.billNumberId = billNumberId;
	}

	
	public Date getPrimaryAssignmentDate() {
		return primaryAssignmentDate;
	}

	public void setPrimaryAssignmentDate(Date primaryAssignmentDate) {
		this.primaryAssignmentDate = primaryAssignmentDate;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public List<DesignationMaster> getDesignationList() {
		return designationList;
	}

	public void setDesignationList(List<DesignationMaster> designationList) {
		this.designationList = designationList;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public PaginatedList getVacantPosPaginatedList() {
		return vacantPosPaginatedList;
	}

	public void setVacantPosPaginatedList(PaginatedList vacantPosPaginatedList) {
		this.vacantPosPaginatedList = vacantPosPaginatedList;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	public InputStream getFileStream() {
		 return new ByteArrayInputStream(ro.getReportOutputData());
	}


	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	public ReportOutput getRo() {
		return ro;
	}

	public void setRo(ReportOutput ro) {
		this.ro = ro;
	}
	
	public EisReportService getEisReportService() {
		return eisReportService;
	}
	public void setEisReportService(EisReportService eisReportService) {
		this.eisReportService = eisReportService;
	}
	
	
	public PersistenceService<BillNumberMaster, Integer> getBillNumberMasterService() {
		return billNumberMasterService;
	}

	public void setBillNumberMasterService(
			PersistenceService<BillNumberMaster, Integer> billNumberMasterService) {
		this.billNumberMasterService = billNumberMasterService;
	}

	public List<BillNumberMaster> getBillNoList() {
		return billNoList;
	}

	public void setBillNoList(List<BillNumberMaster> billNoList) {
		this.billNoList = billNoList;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}


}
