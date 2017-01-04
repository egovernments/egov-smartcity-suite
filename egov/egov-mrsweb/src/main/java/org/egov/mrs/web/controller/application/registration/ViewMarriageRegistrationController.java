/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.web.controller.application.registration;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Shows a Marriage Registration with read only fields
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/registration")
public class ViewMarriageRegistrationController extends MarriageRegistrationController {

    @Autowired
    private FileStoreService fileStoreService;

    private static final Logger LOG = Logger.getLogger(ViewMarriageRegistrationController.class);

    @RequestMapping(value = "/view/{registrationId}", method = RequestMethod.GET)
    public String viewRegistration(@PathVariable final Long registrationId, @RequestParam(required = false) String mode,
            final Model model) throws IOException {
        final MarriageRegistration registration = marriageRegistrationService.get(registrationId);

        model.addAttribute("registration", registration);
        model.addAttribute("mode", mode);

        if (registration.getMarriagePhotoFileStore() != null) {
            final File file = fileStoreService.fetch(registration.getMarriagePhotoFileStore().getFileStoreId(),
                    MarriageConstants.FILESTORE_MODULECODE);
            try {
                registration.setEncodedMarriagePhoto(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
            } catch (final Exception e) {
                LOG.error("Error while preparing the document for view", e);
            }
        }

        marriageRegistrationService.prepareDocumentsForView(registration);
        marriageApplicantService.prepareDocumentsForView(registration.getHusband());
        marriageApplicantService.prepareDocumentsForView(registration.getWife());

        registration.getWitnesses().forEach(witness -> {
            try {
                if (witness.getPhotoFileStore() != null) {
                    final File file = fileStoreService.fetch(witness.getPhotoFileStore().getFileStoreId(),
                            MarriageConstants.FILESTORE_MODULECODE);
                    witness.setEncodedPhoto(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
                }
            } catch (final Exception e) {
                LOG.error("Error while preparing the document for view", e);
            }
        });
        return "registration-view";
    }

}
