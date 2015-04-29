/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
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
