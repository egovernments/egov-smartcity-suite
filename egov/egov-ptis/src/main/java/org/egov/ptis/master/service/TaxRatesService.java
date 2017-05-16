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
package org.egov.ptis.master.service;

import static org.egov.ptis.constants.PropertyTaxConstants.TAX_RATES;
import static org.egov.ptis.constants.PropertyTaxConstants.TAX_RATES_TEMP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.repository.AppConfigValueRepository;
import org.egov.infra.admin.master.repository.ModuleRepository;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.repository.master.taxrates.TaxRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;

@Service
@Transactional(readOnly = true)
public class TaxRatesService {

    private final TaxRatesRepository taxRatesRepository;

    @Autowired
    public AppConfigValueRepository appConfigValueRepository;
    
    @Autowired
    public ModuleRepository moduleRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    @Qualifier("egDemandReasonDetailsService")
    private PersistenceService<EgDemandReasonDetails, Long> egDemandReasonDetailsService;
    
    protected static final Map<String, String> TAX_RATES_MAP = ImmutableMap.of("VAC_LAND_TAX", PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX,
            "TOT_RESD_TAX", "Total Residential Tax", "LIB_CESS", PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS, "TOT_NR_RESD_TAX",
            "Total Non Residential Tax", "EDU_CESS", PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS);
    
    @Autowired
    public TaxRatesService(final TaxRatesRepository taxRatesRepository) {
        this.taxRatesRepository = taxRatesRepository;
    }

    public List<AppConfigValues> getAllTaxRates() {
        return taxRatesRepository.findAll();
    }

    public Map<String, Double> getTaxDetails() {
        List<AppConfigValues> taxRates = getAllTaxRates();
        Map<String, Double> taxRatesMap = new HashMap<>();
        for (AppConfigValues appConfig : taxRates) {
            String[] rows = appConfig.getValue().split("\n");
            for (String row : rows) {
                String[] value = row.split("=");
                if (!TAX_RATES_TEMP.contains(value[0])) {
                    taxRatesMap.put(TAX_RATES.get(value[0]), Double.parseDouble(value[1]));
                }
            }

        }
        Map<String, Double> taxRatesDisplayMap = new LinkedHashMap<>();
        taxRatesDisplayMap.put("General Tax Residential", taxRatesMap.get("General Tax Residential"));
        taxRatesDisplayMap.put("General Tax Non Residential", taxRatesMap.get("General Tax Non Residential"));
        taxRatesDisplayMap.put("Education Cess", taxRatesMap.get("Education Cess"));
        taxRatesDisplayMap.put("Vacant Land Tax", taxRatesMap.get("Vacant Land Tax"));
        taxRatesDisplayMap.put("Library Cess", taxRatesMap.get("Library Cess"));
        return taxRatesDisplayMap;
    }
    
    @Transactional
    public void updateTaxRateAppconfig(AppConfigValues appConfigValue) {
        appConfigValueRepository.save(appConfigValue);
    }
    
    @SuppressWarnings("unchecked")
    public List<EgDemandReasonDetails> getTaxRates() {
        final Query query = entityManager.createNamedQuery("DEMANDREASONDETAILS_BY_MOUDULEID");
        query.setParameter(1, moduleRepository.findByName(PropertyTaxConstants.PTMODULENAME));
        return query.getResultList();
    }

    public List<EgDemandReasonDetails> excludeOldTaxHeads(List<EgDemandReasonDetails> demandReasonDetailsList) {
        Iterator<EgDemandReasonDetails> excludedList = demandReasonDetailsList.iterator();
        while (excludedList.hasNext()) {
            EgDemandReasonDetails egDemandReasonDetails = excludedList.next();

            if (TAX_RATES_MAP.containsValue(
                    egDemandReasonDetails.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster()))
                excludedList.remove();
        }
        return demandReasonDetailsList;
    }

    @Transactional
    public void updateTaxRates(EgDemandReasonDetails egDemandReasonDetails) {
        egDemandReasonDetailsService.persist(egDemandReasonDetails);
    }

    public EgDemandReasonDetails getDemandReasonDetailsById(Long id) {
        final Query qry = entityManager.createNamedQuery("DEMANDREASONDETAILS_BY_ID");
        qry.setParameter(1, id);
        return (EgDemandReasonDetails) qry.getSingleResult();
    }
}
