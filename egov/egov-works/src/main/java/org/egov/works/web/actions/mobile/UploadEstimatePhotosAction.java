package org.egov.works.web.actions.mobile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.EstimatePhoto;
import org.egov.works.services.AbstractEstimateService;

@SuppressWarnings("serial")
public class UploadEstimatePhotosAction extends BaseFormAction {
	private String SEARCH = "search";
	private String SEARCH_LIST = "searchList";
	private AbstractEstimate abstractEstimate = new AbstractEstimate();
	private String fromDate;
	private String toDate;
	private Integer execDeptId;
	private List<AbstractEstimate> estimateList = new ArrayList<AbstractEstimate>();
	private Long estId;
	private AbstractEstimateService abstractEstimateService;
	private String latitude;
	private String longitude;
	private String UPLOAD = "upload";
	private String SUCCESS = "success";
	private String successMessage;
	private static final Logger LOGGER = Logger.getLogger(UploadEstimatePhotosAction.class);
	
	public void prepare()
	{
		 addDropdownData("typeList", getPersistenceService().findAllBy("from WorkType "));
		 addDropdownData("execDeptList",getPersistenceService().findAllBy("from DepartmentImpl "));
	}
	
	public String search()
	{
		return SEARCH;
	}
	
	@SuppressWarnings("unchecked")
	public String searchList()
	{
		StringBuffer query = new StringBuffer();
		query.append(" from AbstractEstimate where id is not null and upper(egwStatus.code) not in ('NEW','CANCELLED') and parent is null ");
		if(StringUtils.isNotBlank(abstractEstimate.getEstimateNumber()))
		{
			query.append(" and upper(estimateNumber) like '%"+abstractEstimate.getEstimateNumber().toUpperCase()+"%' ");
		}
		if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
			query.append(" and estimateDate between TO_DATE('"+fromDate+"','dd/mm/yyyy') and TO_DATE('"+toDate+"','dd/mm/yyyy')");
		}
		else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate))
		{
			query.append(" and estimateDate >= TO_DATE('"+fromDate+"','dd/mm/yyyy')");
		}
		else if(StringUtils.isNotBlank(toDate) && StringUtils.isBlank(fromDate))
		{
			query.append(" and estimateDate <=  TO_DATE('"+toDate+"','dd/mm/yyyy')");
		}
		if(execDeptId!=null && execDeptId!=-1)
		{
			query.append(" and executingDepartment.id= "+execDeptId);
		}
		if(abstractEstimate.getType()!=null && abstractEstimate.getType().getId()!=null
				&& abstractEstimate.getType().getId()!=-1)
		{
			query.append(" and type.id= "+abstractEstimate.getType().getId());
		}
		query.append(" order by id desc ");
		estimateList = (List<AbstractEstimate>) persistenceService.getSession().createQuery(query.toString()).setMaxResults(100).list();
		return SEARCH_LIST;
	}
	
	public String upload()
	{
		return UPLOAD;
	}
	
	public String savePhotos() 
	{
		AbstractEstimate ae =  abstractEstimateService.find(" from AbstractEstimate where id = ?",estId);
		if(abstractEstimate!=null && abstractEstimate.getPhotoList()!=null && abstractEstimate.getPhotoList().size()>0)
		{
			for(EstimatePhoto estPic:abstractEstimate.getPhotoList())
			{
				estPic.setLatitude(Double.parseDouble(latitude));
				estPic.setLongitude(Double.parseDouble(longitude));
				estPic.setDateOfCapture(new Date());
				if(estPic.getFileUpload()!=null)
				{
					byte[] bFile = new byte[(int) estPic.getFileUpload().length()];
				    try {
				    	FileInputStream fileInputStream = new FileInputStream(estPic.getFileUpload());
						fileInputStream.read(bFile);
						fileInputStream.close();
						estPic.setImage(bFile);
						estPic.setEstimate(ae);
					} catch (IOException e) {
						LOGGER.error("Error while uploading file - "+e.getMessage());
					}
				}
			}
			ae.getPhotoList().addAll(abstractEstimate.getPhotoList());
			abstractEstimateService.merge(ae);
			successMessage =abstractEstimate.getPhotoList().size()+" Photo(s) uploaded successfully";
		}
		return SUCCESS;
	}
	
	@Override
	public Object getModel() {
		return abstractEstimate;
	}
	
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public List<AbstractEstimate> getEstimateList() {
		return estimateList;
	}

	public Integer getExecDeptId() {
		return execDeptId;
	}

	public void setExecDeptId(Integer execDeptId) {
		this.execDeptId = execDeptId;
	}

	public Long getEstId() {
		return estId;
	}

	public void setEstId(Long estId) {
		this.estId = estId;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

}
