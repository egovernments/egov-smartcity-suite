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

package org.egov.adtax.service;

import org.apache.log4j.Logger;
import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementBatchDemandGenerate;
import org.egov.adtax.repository.AdvertisementBatchDemandGenRepository;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgDemandDao;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdvertisementBatchDemandGenService {

    private static final Logger LOGGER = Logger.getLogger(AdvertisementBatchDemandGenService.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private EgDemandDao egDemandDao;
    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private AdvertisementBatchDemandGenRepository batchDemandGenRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public AdvertisementBatchDemandGenerate getBatchDemandGenById(final Long id) {
        return batchDemandGenRepository.findOne(id);
    }

    public List<AdvertisementBatchDemandGenerate> findActiveBatchDemands() {
        return batchDemandGenRepository.findActiveBatchDemands();
    }

    @Transactional
    public AdvertisementBatchDemandGenerate createAdvertisementBatchDemandGenerate(
            final AdvertisementBatchDemandGenerate advBatchDmd) {
        return batchDemandGenRepository.save(advBatchDmd);
    }

    @Transactional
    public AdvertisementBatchDemandGenerate updateAdvertisementBatchDemandGenerate(
            final AdvertisementBatchDemandGenerate advBatchDmd) {
        return batchDemandGenRepository.save(advBatchDmd);
    }

    public int generateDemandForNextFinYear() {
        int totalRecordsProcessed = 0;

        final List<AdvertisementBatchDemandGenerate> advBatchDmdGenResult = findActiveBatchDemands();

        List<Advertisement> advertisements = new ArrayList<>();
        if (advBatchDmdGenResult != null && !advBatchDmdGenResult.isEmpty()) {
            LOGGER.info("advBatchDmdGenResult " + advBatchDmdGenResult.size());
            final AppConfigValues totalRecordToFeatch = appConfigValuesService.getConfigValuesByModuleAndKey(
                    AdvertisementTaxConstants.MODULE_NAME, AdvertisementTaxConstants.TOTALRESULTTOBEFETCH).get(0);
            LOGGER.info("*************************************** totalRecordToFeatch records "
                    + totalRecordToFeatch.getValue());

            final AdvertisementBatchDemandGenerate advDmdGen = advBatchDmdGenResult.get(0);

            /*
             * GET LIST OF DEMANDS WHICH ARE PERMANENT AND BASED ON FINANCIAL YEAR, GET ADVERTISEMENTS. Check count, if count
             * greater than 300 then
             */
            if (advDmdGen != null && advDmdGen.getInstallment() != null) {

                final List<Installment> previousInstallment = advertisementDemandService.getPreviousInstallment(advDmdGen
                        .getInstallment().getToDate());

                final Installment advDmdGenerationInstallment = advertisementDemandService
                        .getInsatllmentByModuleForGivenDate(advDmdGen.getInstallment().getToDate());

                /*
                 * Assumption : selected installment data not present in advertisement demand.
                 */
                if (advDmdGenerationInstallment != null && previousInstallment != null && !previousInstallment.isEmpty()) {
                    advertisements = advertisementService
                            .findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
                                    previousInstallment.get(0), Integer.valueOf(totalRecordToFeatch.getValue()));

                    for (final Advertisement advertisement : advertisements)
                        advertisement.setDemandId(egDemandDao.findById(advertisement.getDemandId().getId(), false));

                    totalRecordsProcessed = advertisementDemandService.generateDemandForNextInstallment(
                            advertisements, previousInstallment, advDmdGenerationInstallment);

                }
                advDmdGen.setActive(false);
                advDmdGen.setTotalRecords(totalRecordsProcessed);
            }

            transactionTemplate.execute(result -> {
                updateAdvertisementBatchDemandGenerate(advDmdGen);
                return Boolean.TRUE;
            });

            // commented: Update advertisement index not required at this point. There is no demand updation happening in permit
            // index update.
            /*
             * for (final Advertisement advertisement : advertisements) advertisementPermitDetailUpdateIndexService
             * .updateAdvertisementPermitDetailIndexes(advertisement.getActiveAdvertisementPermit());
             */
        }
        return totalRecordsProcessed;
    }
}
