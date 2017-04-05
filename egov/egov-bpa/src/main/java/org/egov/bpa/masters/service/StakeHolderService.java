/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.bpa.masters.service;
import static org.egov.bpa.utils.BpaConstants.FILESTORE_MODULECODE;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.bpa.application.entity.CheckListDetail;
import org.egov.bpa.application.entity.StakeHolder;
import org.egov.bpa.application.entity.StakeHolderDocument;
import org.egov.bpa.application.service.BPADocumentService;
import org.egov.bpa.masters.repository.StakeHolderAddressRepository;
import org.egov.bpa.masters.repository.StakeHolderRepository;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.persistence.entity.Address;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class StakeHolderService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StakeHolderRepository stakeHolderRepository;
    @Autowired
    private StakeHolderAddressRepository stakeHolderAddressRepository;
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private BPADocumentService bpaDocumentService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<StakeHolder> findAll() {
        return stakeHolderRepository.findAll();
    }

    @Transactional
    public StakeHolder save(final StakeHolder stakeHolder) {
        persistStakeHolderDocuments(stakeHolder);
        return stakeHolderRepository.save(stakeHolder);
    }

    private void persistStakeHolderDocuments(final StakeHolder stakeHolder) {
        final Map<Long, CheckListDetail> generalDocumentAndId = new HashMap<>();
        bpaDocumentService.getStakeHolderDocuments()
                .forEach(document -> generalDocumentAndId.put(document.getId(), document));
        addDocumentsToFileStore(stakeHolder, generalDocumentAndId);
    }

    @Transactional
    public StakeHolder update(final StakeHolder stakeHolder) {
        persistStakeHolderDocuments(stakeHolder);
        return stakeHolderRepository.save(stakeHolder);
    }

    @Transactional
    public void removeAddress(List<Address> address) {
        stakeHolderAddressRepository.deleteInBatch(address);
    }

    public StakeHolder findById(Long id) {
        return stakeHolderRepository.findOne(id);
    }

    @SuppressWarnings("unchecked")
    public List<StakeHolder> search(final StakeHolder stakeHolder) {
        final Criteria criteria = buildSearchCriteria(stakeHolder);
        return criteria.list();
    }

    /**
     * Adds the uploaded stake holder document to file store and associates with the stake holder
     *
     * @param registration
     */
    private void addDocumentsToFileStore(final StakeHolder stakeHolder,
            final Map<Long, CheckListDetail> documentAndId) {
        final List<CheckListDetail> documents = stakeHolder.getCheckListDocuments();
        documents.stream()
                .filter(document -> !document.getFile().isEmpty() && document.getFile().getSize() > 0)
                .map(document -> {
                    final StakeHolderDocument stakeHolderDocument = new StakeHolderDocument();
                    stakeHolderDocument.setStakeHolder(stakeHolder);
                    stakeHolderDocument.setCheckListDetail(documentAndId.get(document.getId()));
                    stakeHolderDocument.setDocumentId(addToFileStore(document.getFile()));
                    stakeHolderDocument.setIsAttached(true);
                    return stakeHolderDocument;
                }).collect(Collectors.toList())
                .forEach(doc -> stakeHolder.addStakeHolderDocument(doc));
    }

    private FileStoreMapper addToFileStore(final MultipartFile file) {
        FileStoreMapper fileStoreMapper = null;
        try {
            fileStoreMapper = fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                    file.getContentType(), FILESTORE_MODULECODE);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
        }
        return fileStoreMapper;
    }

    public Criteria buildSearchCriteria(final StakeHolder stakeHolder) {
        final Criteria criteria = getCurrentSession().createCriteria(StakeHolder.class, "stakeHolder");

        if (stakeHolder.getName() != null)
            criteria.add(Restrictions.ilike("stakeHolder.name", stakeHolder.getName(),
                    MatchMode.ANYWHERE));

        if (stakeHolder.getAadhaarNumber() != null)
            criteria.add(Restrictions.ilike("stakeHolder.aadhaarNumber", stakeHolder.getAadhaarNumber(),
                    MatchMode.ANYWHERE));

        if (stakeHolder.getPan() != null)
            criteria.add(Restrictions.ilike("stakeHolder.pan", stakeHolder.getPan(),
                    MatchMode.ANYWHERE));

        if (stakeHolder.getBusinessLicenceNumber() != null)
            criteria.add(Restrictions.ilike("stakeHolder.businessLicenceNumber", stakeHolder.getBusinessLicenceNumber(),
                    MatchMode.ANYWHERE));

        if (stakeHolder.getCoaEnrolmentNumber() != null)
            criteria.add(Restrictions.ilike("stakeHolder.coaEnrolmentNumber", stakeHolder.getCoaEnrolmentNumber(),
                    MatchMode.ANYWHERE));

        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }
}
