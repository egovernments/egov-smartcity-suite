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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.repository.AdvertisementRepository;
import org.egov.collection.integration.services.CollectionIntegrationService;
import org.egov.commons.Installment;
import org.egov.demand.model.EgDemand;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    protected CollectionIntegrationService collectionIntegrationService;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Transactional
    public Advertisement createAdvertisement(final Advertisement hoarding) {
        if (hoarding != null && hoarding.getId() == null)
            hoarding.setDemandId(advertisementDemandService.createDemand(hoarding));
        roundOfAllTaxAmount(hoarding);
        return advertisementRepository.save(hoarding);
    }

    public Advertisement updateAdvertisement(final Advertisement advertisement) {

        return advertisementRepository.saveAndFlush(advertisement);
    }

    private void roundOfAllTaxAmount(final Advertisement hoarding) {
        /*
         * if(hoarding.getCurrentEncroachmentFee()!=null)
         * hoarding.setCurrentEncroachmentFee(hoarding.getCurrentEncroachmentFee().setScale(2, BigDecimal.ROUND_HALF_UP));
         * if(hoarding.getCurrentTaxAmount()!=null) hoarding.setCurrentTaxAmount( hoarding.getCurrentTaxAmount().setScale(2,
         * BigDecimal.ROUND_HALF_UP)); if(hoarding.getPendingTax()!=null) hoarding.setPendingTax(
         * hoarding.getPendingTax().setScale(2, BigDecimal.ROUND_HALF_UP));
         */ }

    public List<Object[]> searchBySearchType(final Advertisement hoarding, final String searchType) {
        return advertisementRepository.fetchAdvertisementBySearchType(hoarding, searchType);
    }

    public int getActivePermanentAdvertisementsByCurrentInstallment(final Installment installment) {
        return advertisementRepository.findActivePermanentAdvertisementsByCurrentInstallment(installment);
    }

    @Transactional
    public List<Advertisement> findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
            final Installment installment, final int noOfResultToFetch) {
        return advertisementRepository.findActivePermanentAdvertisementsByCurrentInstallmentAndNumberOfResultToFetch(
                installment, noOfResultToFetch);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Advertisement getHoardingByAdvertisementNumber(final String hoardingNumber) {
        return advertisementRepository.findByAdvertisementNumber(hoardingNumber);
    }

    public Advertisement findByAdvertisementNumber(final String hoardingNumber) {
        return advertisementRepository.findByAdvertisementNumber(hoardingNumber);
    }

    public Advertisement findBy(final Long hoardingId) {
        return advertisementRepository.findOne(hoardingId);
    }

    public Advertisement getAdvertisementByDemand(final EgDemand demand) {
        return advertisementRepository.findByDemandId(demand);
    }

}
