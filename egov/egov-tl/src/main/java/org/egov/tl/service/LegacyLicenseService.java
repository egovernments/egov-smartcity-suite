/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.tl.service;

import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.repository.LicenseDocumentTypeRepository;
import org.egov.tl.utils.LicenseNumberUtils;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACTIVE;
import static org.egov.tl.utils.Constants.TL_FILE_STORE_DIR;

@Service
public class LegacyLicenseService extends LicenseService {

    private static final String ARREAR = "arrear";
    private static final String CURRENT = "current";

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private LicenseNumberUtils licenseNumberUtils;

    @Autowired
    private DemandGenericHibDao demandGenericDao;

    @Autowired
    private InstallmentHibDao installmentDao;

    @Autowired
    private LicenseUtils licenseUtils;

    @Autowired
    private LicenseDocumentTypeRepository licenseDocumentTypeRepository;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private ValidityService validityService;

    @Transactional
    public void createLegacy(TradeLicense license) {
        storeDocument(license);
        addLegacyDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        license.setLegacy(true);
        license.setActive(true);
        license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
        license.setUid(UUID.randomUUID().toString());
        validityService.applyLicenseValidity(license);
        update(license);
    }

    @Transactional
    public void updateLegacy(TradeLicense license) {
        storeDocument(license);
        updateLegacyDemand(license);
        validityService.applyLicenseValidity(license);
        update(license);
    }

    public Map<Integer, Integer> legacyInstallmentwiseFeesForCreate() {
        Map<Integer, Integer> legacyInstallmentwiseFees = new LinkedHashMap<>();
        for (Installment installment : getLastFiveYearInstallmentsForLicense())
            legacyInstallmentwiseFees.put(installment.getInstallmentNumber(), 0);
        return legacyInstallmentwiseFees;
    }

    public Map<Integer, Integer> legacyInstallmentwiseFees(final TradeLicense license) {
        final Map<Integer, Integer> legacyInstallmentwiseFees = legacyInstallmentwiseFeesForCreate();
        for (final EgDemandDetails demandDetail : license.getCurrentDemand().getEgDemandDetails())
            legacyInstallmentwiseFees.put(demandDetail.getEgDemandReason().getEgInstallmentMaster().getInstallmentNumber(),
                    demandDetail.getAmount().intValue());
        return sortByKey(legacyInstallmentwiseFees);
    }

    public Map<Integer, Boolean> legacyFeePayStatusForCreate() {
        Map<Integer, Boolean> legacyFeePayStatus = new LinkedHashMap<>();
        for (Installment installment : getLastFiveYearInstallmentsForLicense())
            legacyFeePayStatus.put(installment.getInstallmentNumber(), false);
        return legacyFeePayStatus;
    }

    public Map<Integer, Boolean> legacyFeePayStatus(final TradeLicense license) {
        final Map<Integer, Boolean> legacyFeePayStatus = legacyFeePayStatusForCreate();
        for (final EgDemandDetails demandDetail : license.getCurrentDemand().getEgDemandDetails())
            legacyFeePayStatus.put(demandDetail.getEgDemandReason().getEgInstallmentMaster().getInstallmentNumber(),
                    demandDetail.getAmtCollected().compareTo(ZERO) != 0 &&
                            demandDetail.getAmtCollected().compareTo(demandDetail.getAmount()) == 0);
        return sortByKey(legacyFeePayStatus);
    }

    public Map<Integer, Integer> legacyInstallmentfee(final TradeLicense license) {
        final List<Integer> installmentYears = license.getFinancialyear();
        final List<Integer> installmentFees = license.getLegacyInstallmentwiseFees();
        final Map<Integer, Integer> legacyInstallmentwiseFees = new LinkedHashMap<>();
        for (int index = 0; index < installmentYears.size(); index++) {
            if (installmentFees.get(index) == null)
                installmentFees.set(index, 0);
            legacyInstallmentwiseFees.put(installmentYears.get(index), installmentFees.get(index));
        }
        return legacyInstallmentwiseFees;
    }

    public Map<Integer, Boolean> legacyFeeStatus(final TradeLicense license) {
        final List<Boolean> legacyFeePaymentStatus = license.getLegacyFeePayStatus();
        final List<Integer> installmentYears = license.getFinancialyear();
        final Map<Integer, Boolean> legacyFeePayStatus = new LinkedHashMap<>();
        for (int index = 0; index < installmentYears.size(); index++) {
            if (legacyFeePaymentStatus.size() - 1 < index)
                legacyFeePaymentStatus.add(false);
            else if (legacyFeePaymentStatus.get(index) != null && legacyFeePaymentStatus.get(index).equals(true)
                    && license.getLegacyInstallmentwiseFees().get(index) > 0)
                legacyFeePaymentStatus.set(index, true);
            else
                legacyFeePaymentStatus.set(index, false);
            legacyFeePayStatus.put(installmentYears.get(index), legacyFeePaymentStatus.get(index));
        }
        return legacyFeePayStatus;
    }

    public Map<Integer, Boolean> legacyInstallmentStatus(final TradeLicense tradeLicense) {
        Map<Integer, Boolean> legacyFeePayStatus = new LinkedHashMap<>();
        final List<Boolean> legacyFeePaymentStatus = tradeLicense.getLegacyFeePayStatus();
        for (int index = 0; index < tradeLicense.getFinancialyear().size(); index++) {
            if (legacyFeePaymentStatus.size() - 1 < index)
                legacyFeePaymentStatus.add(false);
            else if (legacyFeePaymentStatus.get(index) != null && legacyFeePaymentStatus.get(index).equals(true) &&
                    tradeLicense.getLegacyInstallmentwiseFees().get(index) > 0)
                legacyFeePaymentStatus.set(index, true);
            else
                legacyFeePaymentStatus.set(index, false);
            legacyFeePayStatus.put(tradeLicense.getFinancialyear().get(index),
                    legacyFeePaymentStatus.get(index));
        }
        return legacyFeePayStatus;
    }

    public void storeDocument(TradeLicense license) {
        List<LicenseDocument> documents = license.getLicenseDocuments();
        for (LicenseDocument document : documents) {
            List<MultipartFile> files = document.getMultipartFiles();
            document.setType(licenseDocumentTypeRepository.findOne(document.getType().getId()));
            for (MultipartFile file : files) {
                try {
                    if (!file.isEmpty()) {
                        document.getFiles()
                                .add(fileStoreService.store(
                                        file.getInputStream(),
                                        file.getOriginalFilename(),
                                        file.getContentType(), TL_FILE_STORE_DIR));
                        document.setEnclosed(true);
                        document.setDocDate(license.getApplicationDate());
                    } else if (document.getType().isMandatory() && file.isEmpty() && documents.isEmpty()) {
                        document.getFiles().clear();
                    }
                } catch (IOException exp) {
                    throw new ApplicationRuntimeException("Error occurred while storing files ", exp);
                }
                document.setLicense(license);
            }
        }
        documents.removeIf(licenseDocument -> licenseDocument.getFiles().isEmpty());
        license.getDocuments().addAll(documents);
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(TradeLicense license) {
        Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        EgDemand egDemand = license.getCurrentDemand();
        for (EgDemandDetails demandDetail : egDemand.getEgDemandDetails()) {
            String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put(ARREAR, ZERO);
                feeByTypes.put(CURRENT, ZERO);
            }
            BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(egDemand.getEgInstallmentMaster()))
                feeByTypes.put(CURRENT, demandAmount);
            else
                feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }
        return outstandingFee;
    }

    private void addLegacyDemand(final TradeLicense license) {
        final Map<Integer, Integer> legacyInstallmentwiseFees = legacyInstallmentfee(license);
        final Map<Integer, Boolean> legacyFeePayStatus = legacyFeeStatus(license);
        final EgDemand egDemand = new EgDemand();
        egDemand.setIsHistory("N");
        egDemand.setCreateDate(new Date());
        egDemand.setModifiedDate(new Date());
        Module module = licenseUtils.getModule();
        for (Entry<Integer, Integer> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet())
            if (legacyInstallmentwiseFee.getValue() != null && legacyInstallmentwiseFee.getValue() > 0) {
                Installment installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());

                egDemand.setEgInstallmentMaster(installment);
                BigDecimal demandAmount = BigDecimal.valueOf(legacyInstallmentwiseFee.getValue()).setScale(0,
                        RoundingMode.HALF_UP);
                BigDecimal amtCollected = legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) == null
                        || !legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) ? ZERO : demandAmount;
                egDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
                egDemand.setBaseDemand(demandAmount.add(egDemand.getBaseDemand()).setScale(0, RoundingMode.HALF_UP));
                egDemand.setAmtCollected(amtCollected.add(egDemand.getAmtCollected()).setScale(0, RoundingMode.HALF_UP));
            }
        license.setDemand(egDemand);

    }

    private void updateLegacyDemand(TradeLicense license) {
        Map<Integer, Integer> updatedInstallmentFees = legacyInstallmentfee(license);
        Map<Integer, Boolean> legacyFeePayStatus = legacyFeeStatus(license);
        EgDemand egDemand = license.getCurrentDemand();

        // Update existing demand details
        Iterator<EgDemandDetails> demandDetails = egDemand.getEgDemandDetails().iterator();
        while (demandDetails.hasNext()) {
            EgDemandDetails demandDetail = demandDetails.next();
            Integer installmentNumber = demandDetail.getEgDemandReason().getEgInstallmentMaster().getInstallmentNumber();
            Integer updatedFee = updatedInstallmentFees.get(installmentNumber);
            Boolean feePaymentStatus = legacyFeePayStatus.get(installmentNumber);
            if (updatedFee == null) {
                demandDetails.remove();
            } else {
                BigDecimal updatedDemandAmt = BigDecimal.valueOf(updatedFee).setScale(0, RoundingMode.HALF_UP);
                demandDetail.setAmount(updatedDemandAmt);
                if (feePaymentStatus != null && feePaymentStatus)
                    demandDetail.setAmtCollected(updatedDemandAmt);
                else
                    demandDetail.setAmtCollected(ZERO);
            }
            updatedInstallmentFees.put(installmentNumber, 0);
        }
        // Create demand details which is newly entered
        updateNewLegacyDemand(updatedInstallmentFees, legacyFeePayStatus, egDemand, license);
    }

    private void updateNewLegacyDemand(Map<Integer, Integer> updatedInstallmentFees,
                                       Map<Integer, Boolean> legacyFeePayStatus, EgDemand egDemand, TradeLicense license) {

        Module module = licenseUtils.getModule();
        for (Entry<Integer, Integer> updatedInstallmentFee : updatedInstallmentFees.entrySet())
            if (updatedInstallmentFee.getValue() != null && updatedInstallmentFee.getValue() > 0) {

                Installment installment = installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        updatedInstallmentFee.getKey());
                BigDecimal demandAmount = BigDecimal.valueOf(updatedInstallmentFee.getValue()).setScale(0, RoundingMode.HALF_UP);
                BigDecimal amtCollected = legacyFeePayStatus.get(updatedInstallmentFee.getKey()) == null
                        || !legacyFeePayStatus.get(updatedInstallmentFee.getKey()) ? ZERO : demandAmount;
                egDemand.getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(demandAmount,
                        demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                installment, module),
                        amtCollected));
            }
        // Recalculating BasedDemand
        license.recalculateBaseDemand();
    }

    private List<Installment> getLastFiveYearInstallmentsForLicense() {
        List<Installment> installmentList = installmentDao
                .fetchInstallments(licenseUtils.getModule(), new Date(), 6);
        Collections.reverse(installmentList);
        return installmentList;
    }

    private <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {

        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> mapInStream = map.entrySet().stream();

        mapInStream.sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

        return result;
    }
}