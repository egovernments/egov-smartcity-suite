/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.mobile;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.EstimatePhotographs;
import org.egov.works.services.AbstractEstimateService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
@Result(name = UploadEstimatePhotosAction.SEARCH, location = "uploadEstimatePhotos-search.jsp")
public class UploadEstimatePhotosAction extends BaseFormAction {

    private static final long serialVersionUID = -8691126456751483363L;
    public static final String SEARCH = "search";
    private final String SEARCH_LIST = "searchList";
    private AbstractEstimate abstractEstimate = new AbstractEstimate();
    private String fromDate;
    private String toDate;
    private Integer execDeptId;
    private List<AbstractEstimate> estimateList = new ArrayList<AbstractEstimate>();
    private Long estId;
    private AbstractEstimateService abstractEstimateService;
    private String latitude;
    private String longitude;
    private final String UPLOAD = "upload";
    private final String SUCCESS = "success";
    private String successMessage;
    private static final Logger LOGGER = Logger.getLogger(UploadEstimatePhotosAction.class);

    @Override
    public void prepare() {
        addDropdownData("typeList", getPersistenceService().findAllBy("from NatureOfWork "));
        addDropdownData("execDeptList", getPersistenceService().findAllBy("from Department "));
    }

    @Action(value = "/mobile/uploadEstimatePhotos-newform")
    public String search() {
        return SEARCH;
    }

    @SuppressWarnings("unchecked")
    public String searchList() {
        final StringBuffer query = new StringBuffer();
        query.append(
                " from AbstractEstimate where id is not null and upper(egwStatus.code) not in ('NEW','CANCELLED') and parent is null ");
        if (StringUtils.isNotBlank(abstractEstimate.getEstimateNumber()))
            query.append(" and upper(estimateNumber) like '%" + abstractEstimate.getEstimateNumber().toUpperCase()
                    + "%' ");
        if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate))
            query.append(" and estimateDate between TO_DATE('" + fromDate + "','dd/mm/yyyy') and TO_DATE('" + toDate
                    + "','dd/mm/yyyy')");
        else if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate))
            query.append(" and estimateDate >= TO_DATE('" + fromDate + "','dd/mm/yyyy')");
        else if (StringUtils.isNotBlank(toDate) && StringUtils.isBlank(fromDate))
            query.append(" and estimateDate <=  TO_DATE('" + toDate + "','dd/mm/yyyy')");
        if (execDeptId != null && execDeptId != -1)
            query.append(" and executingDepartment.id= " + execDeptId);
        if (abstractEstimate.getType() != null && abstractEstimate.getType().getId() != null
                && abstractEstimate.getType().getId() != -1)
            query.append(" and type.id= " + abstractEstimate.getType().getId());
        query.append(" order by id desc ");
        estimateList = persistenceService.getSession().createQuery(query.toString()).setMaxResults(100).list();
        return SEARCH_LIST;
    }

    public String upload() {
        return UPLOAD;
    }

    public String savePhotos() {
        final AbstractEstimate ae = abstractEstimateService.find(" from AbstractEstimate where id = ?", estId);
        if (abstractEstimate != null && abstractEstimate.getEstimatePhotographsList() != null
                && abstractEstimate.getEstimatePhotographsList().size() > 0) {
            for (final EstimatePhotographs estPic : abstractEstimate.getEstimatePhotographsList()) {
                estPic.setLatitude(Double.parseDouble(latitude));
                estPic.setLongitude(Double.parseDouble(longitude));
                estPic.setDateOfCapture(new Date());
                if (estPic.getFileUpload() != null) {
                    final byte[] bFile = new byte[(int) estPic.getFileUpload().length()];
                    try {
                        final FileInputStream fileInputStream = new FileInputStream(estPic.getFileUpload());
                        fileInputStream.read(bFile);
                        fileInputStream.close();
                        estPic.setImage(bFile);
                        estPic.setEstimate(ae);
                    } catch (final IOException e) {
                        LOGGER.error("Error while uploading file - " + e.getMessage());
                    }
                }
            }
            ae.getEstimatePhotographsList().addAll(abstractEstimate.getEstimatePhotographsList());
            abstractEstimateService.merge(ae);
            successMessage = abstractEstimate.getEstimatePhotographsList().size() + " Photo(s) uploaded successfully";
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

    public void setAbstractEstimate(final AbstractEstimate abstractEstimate) {
        this.abstractEstimate = abstractEstimate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public List<AbstractEstimate> getEstimateList() {
        return estimateList;
    }

    public Integer getExecDeptId() {
        return execDeptId;
    }

    public void setExecDeptId(final Integer execDeptId) {
        this.execDeptId = execDeptId;
    }

    public Long getEstId() {
        return estId;
    }

    public void setEstId(final Long estId) {
        this.estId = estId;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

}
