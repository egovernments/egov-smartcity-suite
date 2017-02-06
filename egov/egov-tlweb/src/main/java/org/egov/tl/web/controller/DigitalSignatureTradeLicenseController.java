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
package org.egov.tl.web.controller;

import org.apache.commons.io.FileUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping(value = "/digitalSignature")
public class DigitalSignatureTradeLicenseController {

    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    @Qualifier("tradeLicenseWorkflowService")
    private SimpleWorkflowService tradeLicenseWorkflowService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private FileStoreMapperRepository fileStoreMapperRepository;

    @RequestMapping(value = "/tradeLicense/transitionWorkflow")
    public String transitionWorkflow(final HttpServletRequest request, final Model model) {
        final String fileStoreIds = request.getParameter("fileStoreId");
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        final HttpSession session = request.getSession();

        final Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(Constants.FILE_STORE_ID_APPLICATION_NUMBER);
        final User user = securityUtils.getCurrentUser();
        for (final String fileStoreId : fileStoreIdArr) {
            final String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
            if (null != applicationNumber && !applicationNumber.isEmpty()) {
                License license = (License) persistenceService.find("from License where applicationNumber=?",
                        applicationNumber);
                final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy()
                        .getId());
                final DateTime currentDate = new DateTime();
                license = licenseUtils.applicationStatusChange(license, Constants.APPLICATION_STATUS_APPROVED_CODE);
                WorkFlowMatrix wfmatrix;
                if (license.isReNewApplication())
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(Constants.TRADELICENSE, null, null,
                            Constants.RENEW_ADDITIONAL_RULE,
                            Constants.WF_DIGI_SIGNED, null);
                else
                    wfmatrix = tradeLicenseWorkflowService.getWfMatrix(Constants.TRADELICENSE, null, null,
                            Constants.NEW_ADDITIONAL_RULE,
                            Constants.WF_DIGI_SIGNED, null);

                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(Constants.WF_DIGI_SIGNED).withStateValue(wfmatrix.getCurrentState())
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(wfmatrix.getCurrentStatus());
                persistenceService.persist(license);

            }
        }
        model.addAttribute("successMessage", "Digitally Signed Successfully");
        model.addAttribute("fileStoreId", fileStoreIdArr.length == 1 ? fileStoreIdArr[0] : "");
        return "digitalSignature-success";
    }

    @RequestMapping(value = "/tradeLicense/downloadSignedLicenseCertificate")
    public void downloadSignedLicenseCertificate(final HttpServletRequest request, final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        final File file = fileStoreService.fetch(signedFileStoreId, Constants.FILESTORE_MODULECODE);
        final FileStoreMapper fileStoreMapper = fileStoreMapperRepository.findByFileStoreId(signedFileStoreId);
        response.setContentType("application/pdf");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition",
                "attachment; filename=\"" + (fileStoreMapper != null ? fileStoreMapper.getFileName() : null) + "\"");

        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {
            int bytesRead;
            final byte[] buffer = FileUtils.readFileToByteArray(file);
            while ((bytesRead = inStream.read(buffer)) != -1)
                outStream.write(buffer, 0, bytesRead);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Exception while downloading license certificate file", e);
        }
    }

}
