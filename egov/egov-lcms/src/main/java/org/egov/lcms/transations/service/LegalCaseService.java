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
package org.egov.lcms.transations.service;

import java.math.BigDecimal;

import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.dao.FunctionaryHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.lcms.masters.entity.GovernmentDept;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.Legalcase;
import org.egov.lcms.transactions.repository.GovernmentDeptRepository;
import org.egov.lcms.transactions.repository.LegalcaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LegalCaseService {

    private final LegalcaseRepository legalCaseRepository;
    

    @Autowired
    private GovernmentDeptRepository governmentDeptRepository;
    
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;


    @Autowired
    private FunctionaryHibernateDAO functionaryDAO;
    

    @Autowired
    public LegalCaseService(final LegalcaseRepository legalCaseRepository) {
        this.legalCaseRepository = legalCaseRepository;

    }

    public Legalcase findBy(final Long Id) {
        return legalCaseRepository.findOne(Id);
    }

    public Legalcase getLegalCseByLcNumber(final String lcnumber) {
        return legalCaseRepository.findByLcNumber(lcnumber);
    }
    
    public Legalcase getLegalCseByCaseNumber(final String caseNumber) {
        return legalCaseRepository.findByCasenumber(caseNumber);
    }

    @Transactional
    public Legalcase createLegalCase(final Legalcase legalcase) {
        legalcase.setCasenumber(legalcase.getCasenumber()+legalcase.getWpYear());
        final String[] funcString = legalcase.getFunctionaryCode().split("LC");
        final Functionary funcObj = getFunctionaryByCode(funcString);
        legalcase.setFunctionary(funcObj);
        legalcase.setStatus(getStatusByCodeAndModuleType("CREATED", "LCMS"));
        prepareBipartsanDetails(legalcase);
        return legalCaseRepository.save(legalcase);
    }
    private void prepareBipartsanDetails(final Legalcase legalcase) {
        final BipartisanDetails bipartObj = new BipartisanDetails();
        final GovernmentDept govtDept = governmentDeptRepository.findByCode("L011");
        bipartObj.setGovernmentDept(govtDept);
        bipartObj.setIsrepondent(Boolean.TRUE);
        bipartObj.setIsrespondentgovernment(Boolean.TRUE);
        bipartObj.setName("bipart1");
        bipartObj.setSerialNumber(231232l);
        bipartObj.setLegalcase(legalcase);
        legalcase.getBipartisanDetails().add(bipartObj);
    }
    @Transactional
    public Legalcase updateLegalCase(final Legalcase legalCase) {
        return legalCaseRepository.save(legalCase);
    }
    
    public Functionary getFunctionaryByCode(final String[] funcString) {
        final Functionary funcObj = functionaryDAO.getFunctionaryByCode(new BigDecimal(funcString[1]));
        return funcObj;
    }

    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and code=?", moduleName, code);
    }
}
