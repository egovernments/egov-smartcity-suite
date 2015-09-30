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
package org.egov.adtax.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.adtax.entity.Hoarding;
import org.egov.adtax.repository.HoardingRepository;
import org.egov.adtax.search.contract.HoardingSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HoardingService {

    private final HoardingRepository hoardingRepository;

    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @Autowired
    public HoardingService(final HoardingRepository hoardingRepository) {
        this.hoardingRepository = hoardingRepository;
    }

    @Transactional
    public Hoarding createHoarding(final Hoarding hoarding) {
        if (hoarding != null && hoarding.getId() == null)
            hoarding.setDemandId(advertisementDemandService.createDemand(hoarding));
        return hoardingRepository.save(hoarding);
    }

    @Transactional
    public Hoarding updateHoarding(final Hoarding hoarding) {
        return hoardingRepository.saveAndFlush(hoarding);
    }

    public List<Object[]> searchBySearchType(final Hoarding hoarding, final String searchType) {
        return hoardingRepository.fetchHoardingsBySearchType(hoarding, searchType);
    }

    public Hoarding getHoardingByHoardingNumber(final String hoardingNumber) {
        return hoardingRepository.findByHoardingNumber(hoardingNumber);
    }

    public List<HoardingSearch> getHoardingSearchResult(final HoardingSearch hoardingSearch) {
        final List<Hoarding> hoardings = hoardingRepository.fetchHoardingsLike(hoardingSearch);
        final List<HoardingSearch> hoardingSearchResults = new ArrayList<>();
        hoardings.forEach(result -> {
            final HoardingSearch hoardingSearchResult = new HoardingSearch();
            hoardingSearchResult.setHoardingNumber(result.getHoardingNumber());
            hoardingSearchResult.setApplicationNumber(result.getApplicationNumber());
            hoardingSearchResult.setApplicationFromDate(result.getApplicationDate());
            hoardingSearchResult.setAgencyName(result.getAgency().getName());
            hoardingSearchResult.setStatus(result.getStatus());
            hoardingSearchResults.add(hoardingSearchResult);
        });
        return hoardingSearchResults;
    }
    public Hoarding findByHoardingNumber(final String hoardingNumber) {
        return hoardingRepository.findByHoardingNumber(hoardingNumber);
    }
    
}
