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

package org.egov.mrs.application;

import org.apache.commons.io.FileUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.infra.utils.PdfUtils.appendFiles;

@Service
public class MarriageUtils {

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @Autowired
    private MarriageCertificateService marriageCertificateService;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private ReIssueService reIssueService;

    public boolean isLoggedInUserApprover() {
        final List<Role> approvers = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase(MarriageConstants.APPROVER_ROLE_NAME))
                .collect(Collectors.toList());

        return !approvers.isEmpty();
    }

    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleName, code);
    }

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null) {
            asignList = new ArrayList<>();
            asignList.add(assignment);
        } else
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return !asignList.isEmpty()
                ? asignList.get(0).getEmployee().getUsername().concat(":: " + asignList.get(0).getEmployee().getName()) : "";
    }

    public Boolean isDigitalSignEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME, MarriageConstants.APPCONFKEY_DIGITALSIGNINWORKFLOW).get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    public @ResponseBody
    ResponseEntity<byte[]> viewReport(@PathVariable final Long id, String objType, final HttpSession session,
                                      final HttpServletRequest request) throws IOException {
        ReportOutput reportOutput = null;
        final HttpHeaders headers = new HttpHeaders();
        if (objType != null && objType.equalsIgnoreCase(MarriageCertificateType.REISSUE.toString())) {
            final ReIssue reIssueObj = reIssueService.get(id);
            reportOutput = marriageCertificateService.generateCertificate(reIssueObj, objType, "");
        } else if (objType != null && objType.equalsIgnoreCase(MarriageCertificateType.REGISTRATION.toString())) {
            final MarriageRegistration marriageRegistrationObj = marriageRegistrationService.get(id);
            reportOutput = marriageCertificateService.generate(marriageRegistrationObj, "");
        }
        if (reportOutput != null && reportOutput.getReportOutputData() != null) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("content-disposition", "inline;filename=WorkOrderNotice.pdf");
            return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
        } else
            return redirect();
    }

    /**
     * @return
     * @description display error message if certificate preview fails
     */
    public ResponseEntity<byte[]> redirect() {
        String errorMessage = "Error Generating Certificate Preview.";
        errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15px;'>" + errorMessage
                + "</p></body></html>";
        final byte[] byteData = errorMessage.getBytes();
        return new ResponseEntity<>(byteData, HttpStatus.CREATED);
    }

    public void downloadSignedCertificate(String signedFileStoreId, final HttpServletResponse response) {
        final File file = fileStoreService.fetch(signedFileStoreId, MarriageConstants.FILESTORE_MODULECODE);
        try {
            final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
            final List<InputStream> pdfs = new ArrayList<>();
            final byte[] bFile = FileUtils.readFileToByteArray(file);
            pdfs.add(new ByteArrayInputStream(bFile));
            getServletResponse(response, pdfs, fileStoreMapper.getFileName());
        } catch (final FileNotFoundException fileNotFoundExcep) {
            throw new ApplicationRuntimeException("Exception while loading file : " + fileNotFoundExcep);
        } catch (final IOException ioExcep) {
            throw new ApplicationRuntimeException("Exception while generating work order : " + ioExcep);
        }
    }


    private HttpServletResponse getServletResponse(final HttpServletResponse response, final List<InputStream> pdfs,
                                                   final String filename) {
        try {
            final byte[] data = appendFiles(pdfs);
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".pdf");
            response.setContentType("application/pdf");
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
            return response;
        } catch (final IOException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public boolean isReassignEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                MarriageConstants.MODULE_NAME,
                MarriageConstants.APPCONFKEY_REASSIGN_BUTTONENABLED);
        return !appConfigValues.isEmpty() && "YES".equals(appConfigValues.get(0).getValue()) ? true : false;
    }

}