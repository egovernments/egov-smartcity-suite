/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.works.web.actions.estimate;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.services.AbstractEstimateService;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Result(name = BaseFormAction.SUCCESS, type = "stream", location = "estimatePDF", params = { "inputName",
        "estimatePDF", "contentType", "application/pdf", "contentDisposition", "no-cache;filename=AbstractEstimatePDF.pdf" })
@ParentPackage("egov")
public class AbstractEstimatePDFAction extends BaseFormAction {

    private static final long serialVersionUID = 8202192351878784580L;
    private static final Logger logger = Logger.getLogger(AbstractEstimatePDFAction.class);
    private Long estimateID;
    private InputStream estimatePDF;
    private AbstractEstimateService abstractEstimateService;
    @Autowired
    private AssignmentService assignmentService;
    private WorksService worksService;

    @Override
    public Object getModel() {
        return null;
    }

    @Override
    public String execute() {
        if (estimateID != null) {
            final AbstractEstimate estimate = getAbstractEstimate();
            final Boundary b = getTopLevelBoundary(estimate.getWard());
            final ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 100);
            final EstimatePDFGenerator pdfGenerator = new EstimatePDFGenerator(estimate, b == null ? "" : b.getName(), out);
            pdfGenerator.setPersistenceService(getPersistenceService());
            pdfGenerator.setAssignmentService(assignmentService);
            pdfGenerator.setBudgetDetailsDAO(abstractEstimateService.getBudgetDetailsDAO());
            pdfGenerator.setAbstractEstimateService(abstractEstimateService);
            pdfGenerator.setWorksService(worksService);
            try {
                pdfGenerator.generatePDF();
            } catch (final ValidationException e) {

                logger.debug("exception " + e);
            }

            estimatePDF = new ByteArrayInputStream(out.toByteArray());
        }
        return SUCCESS;
    }

    private AbstractEstimate getAbstractEstimate() {
        return abstractEstimateService.findById(estimateID, false);
    }

    protected Boundary getTopLevelBoundary(final Boundary boundary) {
        Boundary b = boundary;
        while (b != null && b.getParent() != null)
            b = b.getParent();
        return b;
    }

    public void setEstimateID(final Long estimateID) {
        this.estimateID = estimateID;
    }

    public InputStream getEstimatePDF() {
        return estimatePDF;
    }

    public AbstractEstimateService getAbstractEstimateService() {
        return abstractEstimateService;
    }

    public void setAbstractEstimateService(
            final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public void setWorksService(final WorksService worksService) {
        this.worksService = worksService;
    }

}
