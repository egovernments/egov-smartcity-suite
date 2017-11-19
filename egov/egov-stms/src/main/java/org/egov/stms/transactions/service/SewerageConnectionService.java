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
package org.egov.stms.transactions.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.demand.model.EgDemand;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.stms.masters.service.DocumentTypeMasterService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetailsDocument;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.entity.SewerageDemandConnection;
import org.egov.stms.transactions.repository.SewerageConnectionRepository;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SewerageConnectionService {

    private final SewerageConnectionRepository sewerageConnectionRepository;

    @Autowired
    private DocumentTypeMasterService documentTypeMasterService;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    public SewerageConnectionService(final SewerageConnectionRepository sewerageConnectionRepository) {
        this.sewerageConnectionRepository = sewerageConnectionRepository;
    }

    public SewerageConnection findBy(final Long id) {
        return sewerageConnectionRepository.findOne(id);
    }

    public List<SewerageConnection> findAll() {
        return sewerageConnectionRepository.findAll(new Sort(Sort.Direction.ASC, "shscNumber"));
    }

    public SewerageConnection findByShscNumber(final String shscNumber) {
        return sewerageConnectionRepository.findByShscNumber(shscNumber);
    }

    public SewerageConnection load(final Long id) {
        return sewerageConnectionRepository.getOne(id);
    }

    public Page<SewerageConnection> getListSewerageConnections(final Integer pageNumber, final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "shscNumber");
        return sewerageConnectionRepository.findAll(pageable);
    }

    public List<SewerageConnection> findByPropertyIdentifier(final String propertyIdentifier) {
        // TODO : commented as part of design change. propertyIdentifier moved
        // to connectiondetail
        // return
        // sewerageConnectionRepository.findByPropertyIdentifier(propertyIdentifier);
        return null;
    }


    public List<SewerageApplicationDetailsDocument> processAndStoreApplicationDocuments(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> applicationDocsList = new ArrayList<SewerageApplicationDetailsDocument>();
        if (!sewerageApplicationDetails.getAppDetailsDocument().isEmpty())
            for (final SewerageApplicationDetailsDocument applicationDocument : sewerageApplicationDetails
                    .getAppDetailsDocument()) {
                applicationDocument.setDocumentTypeMaster(documentTypeMasterService.load(applicationDocument
                        .getDocumentTypeMaster().getId()));
                applicationDocument.setApplicationDetails(sewerageApplicationDetails);
                applicationDocument.setFileStore(addToFileStore(applicationDocument.getFiles()));
                applicationDocsList.add(applicationDocument);
            }
        return applicationDocsList;
    }

    /*public List<SewerageApplicationDetailsDocument> processAndStoreApplicationDocumentsForUpdate(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> applicationDocsList = new ArrayList<SewerageApplicationDetailsDocument>();
        if (!sewerageApplicationDetails.getAppDetailsDocument().isEmpty())
            for (final SewerageApplicationDetailsDocument applicationDocument : sewerageApplicationDetails
                    .getAppDetailsDocument()) {
                if (applicationDocument.getFiles()[0].getSize() != 0) {
                    applicationDocument.setDocumentTypeMaster(documentTypeMasterService.load(applicationDocument
                            .getDocumentTypeMaster().getId()));
                    applicationDocument.setApplicationDetails(sewerageApplicationDetails);
                    applicationDocument.setFileStore(addToFileStore(applicationDocument.getFiles()));
                    applicationDocsList.add(applicationDocument);

                }
            }
        return applicationDocsList;
    }*/

    /*public List<SewerageApplicationDetailsDocument> getSewerageApplicationDocForEditable(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        boolean found = false;
        final List<SewerageApplicationDetailsDocument> tempDocList = new ArrayList<SewerageApplicationDetailsDocument>(
                0);
        List<DocumentTypeMaster> documentTypeMasterList = documentTypeMasterService
                .getAllActiveDocumentTypeMasterByApplicationType(sewerageApplicationDetails.getApplicationType());
        if (sewerageApplicationDetails != null) {
            for (final DocumentTypeMaster dtm : documentTypeMasterList) {
                SewerageApplicationDetailsDocument sewerageDocument = new SewerageApplicationDetailsDocument();
                for (final SewerageApplicationDetailsDocument appDoc : sewerageApplicationDetails
                        .getAppDetailsDocument()) {
                    if (appDoc.getDocumentTypeMaster() != null
                            && dtm.getDescription().equals(appDoc.getDocumentTypeMaster().getDescription())) {
                        found = true;
                        appDoc.setDocumentDate(appDoc.getDocumentDate());
                        appDoc.setDocumentNumber(appDoc.getDocumentNumber());
                        tempDocList.add(appDoc);
                        break;
                    }
                }
                if (!found) {
                    sewerageDocument.setDocumentTypeMaster(dtm);
                    tempDocList.add(sewerageDocument);
                }
                found = false;
            }
        }
        return tempDocList;
    }*/

    public List<SewerageApplicationDetailsDocument> getSewerageApplicationDoc(
            final SewerageApplicationDetails sewerageApplicationDetails) {
        final List<SewerageApplicationDetailsDocument> tempDocList = new ArrayList<SewerageApplicationDetailsDocument>(
                0);
        if (sewerageApplicationDetails != null) {
            for (final SewerageApplicationDetailsDocument appDoc : sewerageApplicationDetails.getAppDetailsDocument()) {
                if (appDoc.getDocumentTypeMaster() != null) {
                    tempDocList.add(appDoc);
                }
            }
        }
        return tempDocList;
    }

    public Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files)) {
            return Arrays
                    .asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                                    file.getContentType(), SewerageTaxConstants.FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        } else
            return null;
    }

    public boolean validApplicationDocument(final SewerageApplicationDetailsDocument applicationDocument) {
        if (!applicationDocument.getDocumentTypeMaster().isMandatory()
                && applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() == null)
            return false;
        return true;
    }

   /* public void validateSewerageDocsOnUpdate(final List<SewerageApplicationDetailsDocument> applicationDocs,
            final BindingResult resultBinder, final int i,
            final SewerageApplicationDetailsDocument applicationDocument, final RedirectAttributes redirAttrib) {

        if (applicationDocument != null && applicationDocument.getDocumentTypeMaster().isMandatory()) {

            if (applicationDocument.getDocumentNumber() == null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentNumber";
                resultBinder.rejectValue(fieldError, "documentNumber.required");
            }
            if (applicationDocument.getDocumentDate() == null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentDate";
                resultBinder.rejectValue(fieldError, "documentDate.required");
            }

            Iterator<MultipartFile> stream = null;
            if (ArrayUtils.isNotEmpty(applicationDocument.getFiles()))
                stream = Arrays.asList(applicationDocument.getFiles()).stream().filter(file -> !file.isEmpty())
                        .iterator();
            if (ArrayUtils.isEmpty(applicationDocument.getFiles()) || stream == null || stream != null
                    && !stream.hasNext()) {
                final String fieldError = "appDetailsDocument[" + i + "].files";
                resultBinder.rejectValue(fieldError, "files.required");
            }

            if (validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        } else {
            if (validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        }
    }*/

    
    @SuppressWarnings("null")
    public void validateDocuments(final List<SewerageApplicationDetailsDocument> applicationDocs,
            final SewerageApplicationDetailsDocument applicationDocument, final int i, final BindingResult resultBinder) {

        if (applicationDocument != null && applicationDocument.getDocumentTypeMaster().isMandatory()) {

            if (applicationDocument.getDocumentNumber() == null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentNumber";
                resultBinder.rejectValue(fieldError, "documentNumber.required");
            }
            if (applicationDocument.getDocumentDate() == null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentDate";
                resultBinder.rejectValue(fieldError, "documentDate.required");
            }

            Iterator<MultipartFile> stream = null;
            if (ArrayUtils.isNotEmpty(applicationDocument.getFiles()))
                stream = Arrays.asList(applicationDocument.getFiles()).stream().filter(file -> !file.isEmpty())
                        .iterator();
            if (ArrayUtils.isEmpty(applicationDocument.getFiles()) || stream == null || stream != null
                    && !stream.hasNext()) {
                final String fieldError = "appDetailsDocument[" + i + "].files";
                resultBinder.rejectValue(fieldError, "files.required");
            } else if (validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        } else {
            if (applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() != null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentNumber";
                resultBinder.rejectValue(fieldError, "documentNumber.required");
            }
            if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() == null) {
                final String fieldError = "appDetailsDocument[" + i + "].documentDate";
                resultBinder.rejectValue(fieldError, "documentDate.required");
            }
            if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() != null) {
                Iterator<MultipartFile> stream = null;
                if (ArrayUtils.isNotEmpty(applicationDocument.getFiles()))
                    stream = Arrays.asList(applicationDocument.getFiles()).stream().filter(file -> !file.isEmpty())
                            .iterator();
                if (ArrayUtils.isEmpty(applicationDocument.getFiles()) || stream == null || stream != null
                        && !stream.hasNext()) {
                    final String fieldError = "appDetailsDocument[" + i + "].files";
                    resultBinder.rejectValue(fieldError, "files.required");
                }
            }
            if (validApplicationDocument(applicationDocument))
                applicationDocs.add(applicationDocument);
        }
    }

}
