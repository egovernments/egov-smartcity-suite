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
package org.egov.lcms.transactions.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.lcms.transactions.entity.LcInterimOrderDocuments;
import org.egov.lcms.transactions.entity.LegalCaseInterimOrder;
import org.egov.lcms.transactions.repository.LegalCaseInterimOrderRepository;
import org.egov.lcms.transactions.repository.LegalCaseRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class LegalCaseInterimOrderService {

    private final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    private LegalCaseRepository legalCaseRepository;

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    public LegalCaseInterimOrderService(final LegalCaseInterimOrderRepository legalCaseInterimOrderRepository) {
        this.legalCaseInterimOrderRepository = legalCaseInterimOrderRepository;
    }

    @Transactional
    public LegalCaseInterimOrder persist(final LegalCaseInterimOrder legalCaseInterimOrder) {
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_IN_PROGRESS);
        legalCaseInterimOrder.getLegalCase().setStatus(statusObj);
        legalCaseRepository.save(legalCaseInterimOrder.getLegalCase());
        processAndStoreApplicationDocuments(legalCaseInterimOrder);
        return legalCaseInterimOrderRepository.save(legalCaseInterimOrder);
    }

    public List<LcInterimOrderDocuments> getLcInterimOrderDocList(final LegalCaseInterimOrder legalCaseInterimOrder) {
        final List<LcInterimOrderDocuments> lcInterimOrderDOc = new ArrayList<LcInterimOrderDocuments>();
        for (final LcInterimOrderDocuments lcInterimOrderDoc : legalCaseInterimOrder.getLcInterimOrderDocuments())
            lcInterimOrderDOc.add(lcInterimOrderDoc);
        return lcInterimOrderDOc;
    }

    public List<LegalCaseInterimOrder> findAll() {
        return legalCaseInterimOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public LegalCaseInterimOrder findById(final Long id) {
        return legalCaseInterimOrderRepository.findById(id);
    }

    protected void processAndStoreApplicationDocuments(final LegalCaseInterimOrder legalCaseInterimOrder) {
        if (!legalCaseInterimOrder.getLcInterimOrderDocuments().isEmpty())
            for (final LcInterimOrderDocuments applicationDocument : legalCaseInterimOrder
                    .getLcInterimOrderDocuments()) {
                applicationDocument.setLegalCaseInterimOrder(legalCaseInterimOrder);
                applicationDocument.setDocumentName("LcInterimOrder");
                applicationDocument.setSupportDocs(addToFileStore(applicationDocument.getFiles()));
            }
    }

    protected Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays.asList(files).stream().filter(file -> !file.isEmpty()).map(file -> {
                try {
                    return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                            file.getContentType(), LcmsConstants.FILESTORE_MODULECODE);
                } catch (final Exception e) {
                    throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                }
            }).collect(Collectors.toSet());
        else
            return null;
    }

    public List<LegalCaseInterimOrder> findBYLcNumber(final String lcNumber) {
        return legalCaseInterimOrderRepository.findByLegalCase_lcNumber(lcNumber);
    }

}