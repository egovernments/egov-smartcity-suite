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
package org.egov.works.masters.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.egf.commons.bank.service.CreateBankService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.autonumber.ContractorCodeGenerator;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.letterofacceptance.entity.SearchRequestContractor;
import org.egov.works.masters.entity.Contractor;
import org.egov.works.masters.entity.ContractorDetail;
import org.egov.works.masters.entity.ExemptionForm;
import org.egov.works.masters.repository.ContractorRepository;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service("contractorService")
@Transactional(readOnly = true)
public class ContractorService implements EntityTypeService {

    @Autowired
    private WorksService worksService;

    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @PersistenceContext
    private EntityManager entityManager;

    private final ContractorRepository contractorRepository;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ContractorGradeService contractorGradeService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibDAO;

    @Autowired
    private CreateBankService createBankService;

    @Autowired
    public ContractorService(final ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    public Contractor getContractorById(final Long contractorId) {
        return contractorRepository.findOne(contractorId);
    }

    @Override
    public List<Contractor> getAllActiveEntities(final Integer accountDetailTypeId) {
        final Query query = entityManager
                .createQuery("select distinct contractorDet.contractor from ContractorDetail contractorDet "
                        + "where contractorDet.status.description = :statusDescription and contractorDet.status.moduletype = :moduleType");
        query.setParameter("statusDescription", "Active");
        query.setParameter("moduleType", "Contractor");
        final List list = query.getResultList();
        return list;
    }

    @Override
    public List<Contractor> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        List<Contractor> contractorList = null;
        filterKey.toUpperCase();
        final String qryString = "select distinct cont from Contractor cont, ContractorDetail contractorDet "
                + "where cont.id=contractorDet.contractor.id and contractorDet.status.description = :statusDescription and contractorDet.status.moduletype = :moduleType and (upper(cont.code) like :contractorCodeOrName "
                + "or upper(cont.name) like :contractorCodeOrName) order by cont.code,cont.name";
        final Query query = entityManager.createQuery(qryString);
        query.setParameter("statusDescription", "Active");
        query.setParameter("moduleType", "Contractor");
        query.setParameter("contractorCodeOrName", "%" + filterKey.toUpperCase() + "%");
        contractorList = query.getResultList();
        return contractorList;
    }

    public List<Contractor> getAllContractors() {
        return contractorRepository.findAll(new Sort(Sort.Direction.ASC, "code"));
    }

    @Transactional
    public void createAccountDetailKey(final Contractor cont) {
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("contractor");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(cont.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);
    }

    public List<Contractor> getContractorsByCodeOrName(final String queryString) {
        return filterActiveEntities(queryString, 0, null);
    }

    public List<Contractor> getContractorsByCode(final String queryString) {
        return filterActiveEntitiesByCode(queryString, 0, null);
    }

    public List<Contractor> filterActiveEntitiesByCode(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        List<Contractor> contractorList = null;
        final String qryString = "select distinct cont from Contractor cont, ContractorDetail contractorDet "
                + "where cont.id=contractorDet.contractor.id and contractorDet.status.description = :statusDescription and contractorDet.status.moduletype = :moduleType and upper(cont.code) like :contractorCode "
                + "order by cont.code,cont.name";
        final Query query = entityManager.createQuery(qryString);
        query.setParameter("statusDescription", "Active");
        query.setParameter("moduleType", "Contractor");
        query.setParameter("contractorCode", "%" + filterKey.toUpperCase() + "%");
        contractorList = query.getResultList();
        return contractorList;
    }

    @Transactional
    public Contractor save(final Contractor contractor) {
        if (WorksConstants.YES
                .equalsIgnoreCase(worksApplicationProperties.contractorMasterCodeAutoGenerated().toLowerCase())) {
            contractor.setCode(generateContractorCode(contractor));
        }
        final Contractor cont = contractorRepository.save(contractor);
        createAccountDetailKey(cont);
        return cont;
    }

    public String generateContractorCode(final Contractor contractor) {
        final ContractorCodeGenerator c = beanResolver.getAutoNumberServiceFor(ContractorCodeGenerator.class);
        return c.getNextNumber(contractor);
    }

    public String getContractorMasterAutoCodeGenerateValue() {
        final String autoGenerateContractorCodeValue = worksApplicationProperties.contractorMasterCodeAutoGenerated();
        if (!org.apache.commons.lang.StringUtils.isBlank(autoGenerateContractorCodeValue))
            return autoGenerateContractorCodeValue;
        return null;
    }

    public String[] getContractorMasterCategoryValues() {
        final String[] contractorMasterCategoryValues = worksApplicationProperties.contractorMasterCategoryValues();
        if (contractorMasterCategoryValues != null && !Arrays.asList(contractorMasterCategoryValues).contains(""))
            return contractorMasterCategoryValues;
        return contractorMasterCategoryValues;
    }

    public String getContractorClassShortName(final String contractorGrade) {
        final String[] contractorMasterClassValues = worksApplicationProperties.contractorMasterClassValues();
        if (contractorMasterClassValues != null && !Arrays.asList(contractorMasterClassValues).contains("")) {
            final HashMap<String, String> contractorClassKeyValuePair = new HashMap<String, String>();
            for (final String s : contractorMasterClassValues)
                contractorClassKeyValuePair.put(s.split(":")[0], s.split(":")[1]);
            return contractorClassKeyValuePair.get(contractorGrade);
        }
        return null;
    }

    public String[] getcontractorMasterSetMandatoryFields() {
        final String[] contractorMasterMandatoryFields = worksApplicationProperties
                .getContractorMasterMandatoryFields();
        if (contractorMasterMandatoryFields != null && !Arrays.asList(contractorMasterMandatoryFields).contains(""))
            return contractorMasterMandatoryFields;
        return contractorMasterMandatoryFields;
    }

    public String[] getcontractorMasterSetHiddenFields() {
        final String[] contractorMasterHiddenFields = worksApplicationProperties.getContractorMasterHideFields();
        if (contractorMasterHiddenFields != null && !Arrays.asList(contractorMasterHiddenFields).contains(""))
            return contractorMasterHiddenFields;
        return contractorMasterHiddenFields;
    }

    public String[] getContractorMasterMandatoryFields() {
        final TreeSet<String> set = new TreeSet<>(Arrays.asList(getcontractorMasterSetMandatoryFields()));
        set.removeAll(Arrays.asList(getcontractorMasterSetHiddenFields()));
        return set.toArray(new String[set.size()]);
    }

    public Contractor getContractorByCode(final String code) {
        return contractorRepository.findByCodeIgnoreCase(code.toUpperCase());

    }

    @Transactional
    public Contractor createContractor(final Contractor contractor) {
        final Contractor savedContractor = contractorRepository.save(contractor);
        createAccountDetailKey(savedContractor);
        return savedContractor;
    }

    @Transactional
    public Contractor updateContractor(final Contractor contractor) {
        return contractorRepository.save(contractor);
    }

    public List<Contractor> searchContractor(final SearchRequestContractor searchRequestContractor) {
        final Criteria criteria = entityManager.unwrap(Session.class).createCriteria(Contractor.class)
                .createAlias("contractorDetails", "contractorDetail")
                .createAlias("contractorDetail.department", "department")
                .createAlias("contractorDetail.status", "status");
        if (searchRequestContractor != null) {
            if (StringUtils.isNotBlank(searchRequestContractor.getNameOfAgency()))
                criteria.add(Restrictions.ilike("name", searchRequestContractor.getNameOfAgency(), MatchMode.ANYWHERE));
            if (StringUtils.isNotBlank(searchRequestContractor.getContractorCode()))
                criteria.add(
                        Restrictions.ilike("code", searchRequestContractor.getContractorCode(), MatchMode.ANYWHERE));
            if (searchRequestContractor.getDepartment() != null)
                criteria.add(Restrictions.eq("department.id", searchRequestContractor.getDepartment()));
            if (searchRequestContractor.getStatus() != null)
                criteria.add(Restrictions.eq("status.id", searchRequestContractor.getStatus()));
            if (searchRequestContractor.getContractorClass() != null)
                criteria.add(Restrictions.eq("contractorDetail.grade.id", searchRequestContractor.getContractorClass()));
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void loadModelValues(final Model model) {
        model.addAttribute("exemptionFormValues", ExemptionForm.values());
        model.addAttribute("bankList", createBankService.getByIsActiveTrueOrderByName());
        model.addAttribute("departmentList", departmentService.getAllDepartments());
        model.addAttribute("contractorClassList", contractorGradeService.getAllContractorGrades());
        model.addAttribute("statusList", egwStatusHibDAO.getStatusByModule(WorksConstants.STATUS_MODULE_NAME));
        model.addAttribute("contractorDetailsCategoryValues", Arrays.asList(getContractorMasterCategoryValues()));
        model.addAttribute("contractorMasterMandatoryFields", Arrays.asList(getcontractorMasterSetMandatoryFields()));
        model.addAttribute("contractorMasterHiddenFields", Arrays.asList(getcontractorMasterSetHiddenFields()));
        model.addAttribute("codeAutoGeneration", getContractorMasterAutoCodeGenerateValue());
    }

    public void createContractorDetails(final Contractor contractor) {
        ContractorDetail contractorDetail;
        contractor.getContractorDetails().clear();
        for (final ContractorDetail cd : contractor.getTempContractorDetails()) {
            contractorDetail = new ContractorDetail();
            contractorDetail.setDepartment(cd.getDepartment());
            contractorDetail.setRegistrationNumber(cd.getRegistrationNumber());
            contractorDetail.setCategory(cd.getCategory());
            contractorDetail.setGrade(cd.getGrade());
            contractorDetail.setStatus(cd.getStatus());
            contractorDetail.setValidity(cd.getValidity());
            contractorDetail.setContractor(contractor);
            contractor.getContractorDetails().add(contractorDetail);
        }

    }

    @Transactional
    public Contractor update(final Contractor contractor) {
        return contractorRepository.save(contractor);
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey) throws ValidationException {
        return null;
    }

    @Override
    public List<Contractor> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        final Query entitysQuery = entityManager
                .createQuery(" from Contractor where panNumber is null or bank is null and id in ( :IDS )");
        entitysQuery.setParameter("IDS", idsList);
        return entitysQuery.getResultList();

    }

    @Override
    public List<Contractor> getEntitiesById(final List<Long> idsList) throws ValidationException {

        final Query entitysQuery = entityManager.createQuery(" from Contractor where id in ( :IDS )");
        entitysQuery.setParameter("IDS", idsList);
        return entitysQuery.getResultList();

    }

}