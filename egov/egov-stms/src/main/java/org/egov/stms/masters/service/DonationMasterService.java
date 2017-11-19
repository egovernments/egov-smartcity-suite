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
package org.egov.stms.masters.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.stms.masters.entity.DonationDetailMaster;
import org.egov.stms.masters.entity.DonationMaster;
import org.egov.stms.masters.entity.enums.PropertyType;
import org.egov.stms.masters.pojo.DonationMasterSearch;
import org.egov.stms.masters.repository.DonationMasterRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DonationMasterService {

    SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOG = LoggerFactory.getLogger(DonationMasterService.class);

    @Autowired
    private final DonationMasterRepository donationMasterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource stmsMessageSource;

    @Autowired
    public DonationMasterService(final DonationMasterRepository donationMasterRepository) {
        this.donationMasterRepository = donationMasterRepository;
    }

    public DonationMaster findById(final Long id) {
        return donationMasterRepository.findOne(id);
    }

    @Transactional
    public DonationMaster create(final DonationMaster donationMaster) {
        return donationMasterRepository.save(donationMaster);
    }

    @Transactional
    public void update(final DonationMaster donationMaster) {
        donationMasterRepository.save(donationMaster);
    }

    public List<DonationMaster> findAll() {
        return donationMasterRepository.findAll(new Sort(Sort.Direction.DESC, "propertyType", "fromDate"));
    }

    public List<DonationMaster> findAllByPropertyType(final PropertyType propertyType) {
        return donationMasterRepository.findAllByPropertyType(propertyType);
    }

    public DonationMaster load(final Long id) {
        return donationMasterRepository.getOne(id);
    }

    public DonationMaster findByPropertyTypeAndFromDateAndActive(final PropertyType propertyType, final Date fromDate,
            final boolean active) {
        return donationMasterRepository.findByPropertyTypeAndFromDateAndActive(propertyType, fromDate, active);
    }

    public DonationMaster findByPropertyTypeAndActive(final PropertyType propertyType, final boolean active) {
        return donationMasterRepository.findByPropertyTypeAndActive(propertyType, active);
    }

    public BigDecimal getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(final Integer noOfClosetsResidential,
            final PropertyType propertyType) {
        return donationMasterRepository.getDonationAmountByNoOfClosetsAndPropertytypeForCurrentDate(noOfClosetsResidential,
                propertyType);
    }

    public List<DonationMaster> getLatestActiveRecordByPropertyTypeAndActive(final PropertyType propertyType,
            final boolean active) {
        return donationMasterRepository.getLatestActiveRecordByPropertyTypeAndActive(propertyType, active, new Date());
    }

    // search record as per search parameters and set the values to variables of helper class
    public List<DonationMasterSearch> getDonationMasters(final PropertyType propertyType, final String date,
            final String status) {
        final List<DonationMasterSearch> donationMasterSearchRecords = new ArrayList<>();
        final List<DonationMaster> donationMasterRecords = searchConnectionRecordsBySearchParams(propertyType, date, status);
        for (final DonationMaster donationMasterRecord : donationMasterRecords) {
            final DonationMasterSearch dmsearch = new DonationMasterSearch();
            dmsearch.setPropertyType(donationMasterRecord.getPropertyType().toString());
            dmsearch.setSize(donationMasterRecord.getDonationDetail().size());
            dmsearch.setFromDate(donationMasterRecord.getFromDate().toString());
            dmsearch.setModifiedDate(donationMasterRecord.getLastModifiedDate().toString());
            dmsearch.setId(donationMasterRecord.getId());
            dmsearch.setActive(donationMasterRecord.isActive());

            try {
                final Date effectivedate = myFormat.parse(myFormat.format(donationMasterRecord.getFromDate()));
                dmsearch.setEditable(false);
                if (effectivedate.compareTo(myFormat.parse(myFormat.format(new Date()))) >= 0)
                    dmsearch.setEditable(true);

            } catch (final ParseException e) {
                LOG.error("Parse Exception " + e);
            }
            donationMasterSearchRecords.add(dmsearch);
        }
        return donationMasterSearchRecords;
    }

    @Transactional
    public DonationMaster createDonationRate(final DonationMaster donationMaster) {
        return donationMasterRepository.save(donationMaster);
    }

    @Transactional
    public void delete(final DonationMaster donationMaster) {
        donationMasterRepository.delete(donationMaster);
    }

    public List<DonationMaster> searchConnectionRecordsBySearchParams(final PropertyType propertyType, final String date,
            final String status) {

        final Criteria connectionCriteria = entityManager.unwrap(Session.class)
                .createCriteria(DonationMaster.class, "donation");

        if (null != propertyType)
            connectionCriteria.add(Restrictions.eq("propertyType", propertyType));
        if (null != date) {
            String formattedDate = null;

            Date fDate = null;
            try {
                formattedDate = formatter.format(myFormat.parse(date));
                fDate = formatter.parse(formattedDate);

            } catch (final ParseException e) {
                LOG.error("Parse Exception " + e);
            }
            connectionCriteria.add(Restrictions.eq("fromDate", fDate));
        }
        if (null != status && !status.equals("ACTIVE"))
            connectionCriteria.add(Restrictions.eq("active", false));
        else
            connectionCriteria.add(Restrictions.eq("active", true));

        connectionCriteria.addOrder(Order.asc("propertyType"));
        connectionCriteria.addOrder(Order.desc("fromDate"));
        connectionCriteria.addOrder(Order.desc("lastModifiedDate"));

        return connectionCriteria.list();
    }

    public List<Date> findFromDateByPropertyType(final PropertyType propertyType) {
        return donationMasterRepository.findFromDateByPropertyType(propertyType);
    }

    public String checkClosetsPresentForGivenCombination(final PropertyType propertyType, final Integer noofclosets) {
        String validationMessage = "";
        final DonationDetailMaster donationDetailMaster = donationMasterRepository
                .getDonationDetailMasterByNoOfClosetsAndPropertytypeForCurrentDate(propertyType, noofclosets);
        if (donationDetailMaster == null)
            validationMessage = stmsMessageSource.getMessage("err.validate.sewerage.closets.isPresent", new String[] {
                    propertyType.toString(), noofclosets.toString() }, null);

        return validationMessage;
    }
}