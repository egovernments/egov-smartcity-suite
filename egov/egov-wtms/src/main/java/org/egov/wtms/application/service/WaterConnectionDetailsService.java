/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.wtms.application.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.utils.ApplicationNumberGenerator;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WaterConnectionDetailsService {

    protected WaterConnectionDetailsRepository waterConnectionDetailsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Autowired
    private ApplicationProcessTimeService applicationProcessTimeService;

    @Autowired
    public WaterConnectionDetailsService(final WaterConnectionDetailsRepository waterConnectionDetailsRepository) {
        this.waterConnectionDetailsRepository = waterConnectionDetailsRepository;
    }

    public WaterConnectionDetails findBy(final Long complaintTypeId) {
        return waterConnectionDetailsRepository.findOne(complaintTypeId);
    }

    public List<WaterConnectionDetails> findAll() {
        return waterConnectionDetailsRepository.findAll(new Sort(Sort.Direction.ASC, "applicationNumber"));
    }

    public WaterConnectionDetails findByApplicationNumber(final String applicationNumber) {
        return waterConnectionDetailsRepository.findByApplicationNumber(applicationNumber);
    }

    public WaterConnectionDetails load(final Long id) {
        return waterConnectionDetailsRepository.getOne(id);
    }

    public Page<WaterConnectionDetails> getListWaterConnectionDetails(final Integer pageNumber,
            final Integer pageSize) {
        final Pageable pageable = new PageRequest(pageNumber - 1, pageSize, Sort.Direction.ASC, "applicationNumber");
        return waterConnectionDetailsRepository.findAll(pageable);
    }

    // TODO - Simple Save. Need to handle different use cases and validations
    @Transactional
    public WaterConnectionDetails createNewWaterConnection(final WaterConnectionDetails waterConnectionDetails) {
        // TODO - Application number logic needs to be confirmed by BA
        if (waterConnectionDetails.getApplicationNumber() == null)
            waterConnectionDetails.setApplicationNumber(applicationNumberGenerator.generate());

        if (waterConnectionDetails.getState() != null
                && waterConnectionDetails.getState().getValue().equals("APPROVED"))
            waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
        final Integer appProcessTime = applicationProcessTimeService.getApplicationProcessTime(
                waterConnectionDetails.getApplicationType(), waterConnectionDetails.getCategory());
        if (appProcessTime != null) {
            final Calendar c = Calendar.getInstance();
            c.setTime(waterConnectionDetails.getApplicationDate());
            c.add(Calendar.DATE, appProcessTime);
            waterConnectionDetails.setDisposalDate(c.getTime());
        }
        final WaterConnectionDetails savedWaterConnectionDetails = waterConnectionDetailsRepository
                .save(waterConnectionDetails);
        return savedWaterConnectionDetails;
    }

    public List<ConnectionType> getAllConnectionTypes() {
        return Arrays.asList(ConnectionType.values());
    }

}
