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
package org.egov.works.master.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.autonumber.ContractorCodeGenerator;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ExemptionForm;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractorService extends PersistenceService<Contractor, Long> implements EntityTypeService {

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;
    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    public ContractorService() {
        super(Contractor.class);
    }

    private final Logger logger = Logger.getLogger(getClass());
    @Autowired
    private WorksService worksService;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @Override
    public List<Contractor> getAllActiveEntities(final Integer accountDetailTypeId) {
        return findAllBy(
                "select distinct contractorDet.contractor from ContractorDetail contractorDet "
                        + "where contractorDet.status.description=? and contractorDet.status.moduletype=?",
                "Active", "Contractor");
    }

    public static final Map<String, String> exemptionForm = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = 408579850562980945L;

        {
            put(ExemptionForm.INCOME_TAX.toString(), ExemptionForm.INCOME_TAX.toString().replace("_", " "));
            put(ExemptionForm.EARNEST_MONEY_DEPOSIT.toString(),
                    ExemptionForm.EARNEST_MONEY_DEPOSIT.toString().replace("_", " "));
            put(ExemptionForm.VAT.toString(), ExemptionForm.VAT.toString().replace("_", " "));
        }
    };

    @Override
    public List<Contractor> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final String param = "%" + filterKey.toUpperCase() + "%";
        final String qry = "select distinct cont from Contractor cont, ContractorDetail contractorDet "
                + "where cont.id=contractorDet.contractor.id and contractorDet.status.description=? and contractorDet.status.moduletype=? and (upper(cont.code) like ? "
                + "or upper(cont.name) like ?) order by cont.code,cont.name";
        return findPageBy(qry, 0, pageSize, "Active", "Contractor", param, param).getList();
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey) throws ValidationException {
        return null;
    }

    @Override
    public List<Contractor> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        List<Contractor> entities = null;
        final Query entitysQuery = getSession()
                .createQuery(" from Contractor where panNumber is null or bank is null and id in ( :IDS )");
        entitysQuery.setParameterList("IDS", idsList);
        entities = entitysQuery.list();
        return entities;

    }

    @Override
    public List<Contractor> getEntitiesById(final List<Long> idsList) throws ValidationException {

        List<Contractor> entities = null;
        final Query entitysQuery = getSession().createQuery(" from Contractor where id in ( :IDS )");
        entitysQuery.setParameterList("IDS", idsList);
        entities = entitysQuery.list();
        return entities;
    }

    public List<Contractor> getAllContractors() {
        List<Contractor> entities = null;
        final Query entityQuery = getSession().createQuery(" from Contractor con order by code asc");
        entities = entityQuery.list();
        return entities;
    }

    public List<Contractor> getContractorListForCriterias(final Map<String, Object> criteriaMap) {
        List<Contractor> contractorList = null;
        String contractorStr = null;
        final List<Object> paramList = new ArrayList<Object>();
        Object[] params;
        final String contractorName = (String) criteriaMap.get(WorksConstants.CONTRACTOR_NAME);
        final String contractorCode = (String) criteriaMap.get(WorksConstants.CONTRACTOR_CODE);
        final Long departmentId = (Long) criteriaMap.get(WorksConstants.DEPARTMENT_ID);
        final Integer statusId = (Integer) criteriaMap.get(WorksConstants.STATUS_ID);
        final Long gradeId = (Long) criteriaMap.get(WorksConstants.GRADE_ID);
        contractorStr = " select distinct contractor from Contractor contractor ";

        if (statusId != null || departmentId != null || gradeId != null)
            contractorStr = contractorStr + " left outer join fetch contractor.contractorDetails as detail ";

        if (statusId != null || departmentId != null || gradeId != null
                || contractorCode != null && !contractorCode.equals("")
                || contractorName != null && !contractorName.equals(""))
            contractorStr = contractorStr + " where contractor.code is not null";

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorCode)) {
            contractorStr = contractorStr + " and UPPER(contractor.code) like ?";
            paramList.add("%" + contractorCode.toUpperCase() + "%");
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorName)) {
            contractorStr = contractorStr + " and UPPER(contractor.name) like ?";
            paramList.add("%" + contractorName.toUpperCase() + "%");
        }

        if (statusId != null) {
            contractorStr = contractorStr + " and detail.status.id = ?";
            paramList.add(statusId);
        }

        if (departmentId != null) {
            contractorStr = contractorStr + " and detail.department.id = ?";
            paramList.add(departmentId);
        }

        if (gradeId != null) {
            contractorStr = contractorStr + " and detail.grade.id = ?";
            paramList.add(gradeId);
        }

        if (paramList.isEmpty())
            contractorList = findAllBy(contractorStr);
        else {
            params = new Object[paramList.size()];
            params = paramList.toArray(params);
            contractorList = findAllBy(contractorStr, params);
        }
        return contractorList;
    }

    public SearchQuery prepareQuery(final Map<String, Object> criteriaMap) {
        String contractorStr = null;
        final List<Object> paramList = new ArrayList<Object>();
        final String contractorName = (String) criteriaMap.get(WorksConstants.CONTRACTOR_NAME);
        final String contractorCode = (String) criteriaMap.get(WorksConstants.CONTRACTOR_CODE);
        final Long departmentId = (Long) criteriaMap.get(WorksConstants.DEPARTMENT_ID);
        final Integer statusId = (Integer) criteriaMap.get(WorksConstants.STATUS_ID);
        final Long gradeId = (Long) criteriaMap.get(WorksConstants.GRADE_ID);
        contractorStr = " from ContractorDetail detail ";

        if (statusId != null || departmentId != null || gradeId != null
                || contractorCode != null && !contractorCode.equals("")
                || contractorName != null && !contractorName.equals(""))
            contractorStr = contractorStr + " where detail.contractor.code is not null";

        if (contractorCode != null && !contractorCode.equals("")) {
            contractorStr = contractorStr + " and UPPER(detail.contractor.code) like ?";
            paramList.add("%" + contractorCode.toUpperCase() + "%");
        }

        if (contractorName != null && !contractorName.equals("")) {
            contractorStr = contractorStr + " and UPPER(detail.contractor.name) like ?";
            paramList.add("%" + contractorName.toUpperCase() + "%");
        }

        if (statusId != null) {
            contractorStr = contractorStr + " and detail.status.id = ? ";
            paramList.add(statusId);
        }

        if (departmentId != null) {
            contractorStr = contractorStr + " and detail.department.id = ? ";
            paramList.add(departmentId);
        }

        if (gradeId != null) {
            contractorStr = contractorStr + " and detail.grade.id = ? ";
            paramList.add(gradeId);
        }
        final String query = "select distinct detail.contractor " + contractorStr;

        final String countQuery = "select count(distinct detail.contractor) " + contractorStr;
        return new SearchQueryHQL(query, countQuery, paramList);
    }

    public void searchContractor(final Map<String, Object> criteriaMap) {
        if (logger.isDebugEnabled())
            logger.debug("Inside searchContractor");
        final String contractorName = (String) criteriaMap.get(WorksConstants.CONTRACTOR_NAME);
        final String contractorCode = (String) criteriaMap.get(WorksConstants.CONTRACTOR_CODE);
        final Long departmentId = (Long) criteriaMap.get(WorksConstants.DEPARTMENT_ID);
        final Long gradeId = (Long) criteriaMap.get(WorksConstants.GRADE_ID);
        final Date searchDate = (Date) criteriaMap.get(WorksConstants.SEARCH_DATE);
        final List<AppConfigValues> configList = worksService.getAppConfigValue("Works", "CONTRACTOR_STATUS");
        final String status = configList.get(0).getValue();

        final Criteria criteria = getSession().createCriteria(Contractor.class);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorCode))
            criteria.add(Restrictions.sqlRestriction("lower({alias}.code) like lower(?)",
                    "%" + contractorCode.trim() + "%", StringType.INSTANCE));

        if (org.apache.commons.lang.StringUtils.isNotEmpty(contractorName))
            criteria.add(Restrictions.sqlRestriction("lower({alias}.name) like lower(?)",
                    "%" + contractorName.trim() + "%", StringType.INSTANCE));

        criteria.createAlias("contractorDetails", "detail").createAlias("detail.status", "status");
        criteria.add(Restrictions.eq("status.description", status));
        if (departmentId != null)
            criteria.add(Restrictions.eq("detail.department.id", departmentId));

        if (gradeId != null)
            criteria.add(Restrictions.eq("detail.grade.id", gradeId));

        if (searchDate != null)
            criteria.add(Restrictions.le("detail.validity.startDate", searchDate))
                    .add(Restrictions.or(Restrictions.ge("detail.validity.endDate", searchDate),
                            Restrictions.isNull("detail.validity.endDate")));

        criteria.addOrder(Order.asc("name"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        criteria.list();
    }

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
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final String param = "%" + filterKey.toUpperCase() + "%";
        final String qry = "select distinct cont from Contractor cont, ContractorDetail contractorDet "
                + "where cont.id=contractorDet.contractor.id and contractorDet.status.description=? and contractorDet.status.moduletype=? and upper(cont.code) like ? "
                + "order by cont.code,cont.name";
        return findPageBy(qry, 0, pageSize, "Active", "Contractor", param).getList();
    }

    public String generateContractorCode(final Contractor contractor) {
        final ContractorCodeGenerator c = beanResolver.getAutoNumberServiceFor(ContractorCodeGenerator.class);
        return c.getNextNumber(contractor);
    }

    public String getContractorMasterAutoCodeGenerateValue() {
        final String autoGenerateContractorCodeValue = worksApplicationProperties.contractorMasterCodeAutoGenerated();
        if (autoGenerateContractorCodeValue != null)
            return autoGenerateContractorCodeValue;
        return null;
    }

    public String[] getContractorMasterCategoryValues() {
        final String[] contractorMasterCategoryValues = worksApplicationProperties.contractorMasterCategoryValues();
        if (contractorMasterCategoryValues != null && contractorMasterCategoryValues.length > 0)
            return contractorMasterCategoryValues;
        return null;
    }

    public String getContractorClassShortName(final String contractorGrade) {
        final String[] contractorMasterClassValues = worksApplicationProperties.contractorMasterClassValues();
        if (contractorMasterClassValues != null && contractorMasterClassValues.length > 0) {
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
        if (contractorMasterMandatoryFields != null && contractorMasterMandatoryFields.length > 0)
            return contractorMasterMandatoryFields;
        return contractorMasterMandatoryFields;
    }

    public String[] getcontractorMasterSetHiddenFields() {
        final String[] contractorMasterHiddenFields = worksApplicationProperties.getContractorMasterHideFields();
        if (contractorMasterHiddenFields != null && contractorMasterHiddenFields.length > 0)
            return contractorMasterHiddenFields;
        return contractorMasterHiddenFields;
    }

    public String[] getContractorMasterMandatoryFields() {
        final TreeSet<String> set = new TreeSet<>(Arrays.asList(getcontractorMasterSetMandatoryFields()));
        set.removeAll(Arrays.asList(getcontractorMasterSetHiddenFields()));
        return set.toArray(new String[set.size()]);
    }
}
