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
package org.egov.works.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.dao.AccountdetailtypeHibernateDAO;
import org.egov.commons.exception.NoSuchObjectException;
import org.egov.commons.service.EntityTypeService;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AssetsForEstimate;
import org.egov.works.models.estimate.ProjectCode;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("deprecation")
@Service
public class ProjectCodeService extends PersistenceService<ProjectCode, Long> implements EntityTypeService {

    @Autowired
    private PersistenceService<AssetsForEstimate, Long> assetsForEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private AccountdetailtypeHibernateDAO accountdetailtypeHibernateDAO;

    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;

    @PersistenceContext
    private EntityManager entityManager;

    public ProjectCodeService() {
        super(ProjectCode.class);
    }

    @Override
    public List<ProjectCode> getAllActiveEntities(final Integer accountDetailTypeId) {
        return entityManager.createQuery("from ProjectCode where active = true", ProjectCode.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectCode> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final String param = "%" + filterKey.toUpperCase() + "%";
        final String qry = "select distinct pc from ProjectCode pc where active = true and upper(pc.code) like ?1 order by code";
        return findPageBy(qry, 0, pageSize, param).getList();
    }

    @Override
    public List<String> getAssetCodesForProjectCode(final Integer accountDetailKey) throws ValidationException {

        if (accountDetailKey == null || accountDetailKey <= 0)
            throw new ValidationException(Arrays.asList(new ValidationError("projectcode.invalid",
                    "Invalid Account Detail Key")));

        final ProjectCode projectCode = entityManager.find(ProjectCode.class, accountDetailKey.longValue());

        if (projectCode == null)
            throw new ValidationException(Arrays.asList(new ValidationError("projectcode.doesnt.exist",
                    "No Project Code exists for given Account Detail Key")));

        if (projectCode.getEstimates() == null || projectCode.getEstimates().size() == 0)
            throw new ValidationException(Arrays.asList(new ValidationError("projectcode.no.link.abstractEstimate",
                    "Estimate is not linked with given Account Detail Key")));

        final List<AbstractEstimate> estimates = new ArrayList<>(projectCode.getEstimates());

        final List<AssetsForEstimate> assetValues = estimates.get(0).getAssetValues();

        if (assetValues == null || assetValues.size() == 0)
            return Collections.emptyList();
        else {
            final List<String> assetCodes = new ArrayList<>();
            for (final AssetsForEstimate asset : assetValues)
                assetCodes.add(asset.getAsset().getCode());
            return assetCodes;
        }
    }

    public List<ProjectCode> getAllActiveProjectCodes(final int fundId, final Long functionId, final int functionaryId,
            final int fieldId, final int deptId) {
        final Map<String, Object> params = new HashMap<>();

        final StringBuffer projectCodeQry = new StringBuffer("select pc ")
                .append("from ProjectCode pc ")
                .append("where pc in (select ae.projectCode from AbstractEstimate as ae inner join ae.financialDetails as fd")
                .append(" where ae.state.value not in('CANCELLED')");

        if (fundId != 0) {
            projectCodeQry.append(" and fd.fund.id = :fundId");
            params.put("fundId", fundId);
        }

        if (functionId != 0) {
            projectCodeQry.append(" and fd.function.id = :functionId");
            params.put("functionId", functionId);
        }

        if (functionaryId != 0) {
            projectCodeQry.append(" and fd.functionary.id = :functionaryId");
            params.put("functionaryId", functionaryId);
        }

        if (fieldId != 0) {
            projectCodeQry.append(" and ae.ward.id = :wardId");
            params.put("wardId", fieldId);
        }

        if (deptId != 0) {
            projectCodeQry.append(" and ae.executingDepartment.id = :deptId");
            params.put("deptId", deptId);
        }
        projectCodeQry.append(")");

        final TypedQuery<ProjectCode> typedQuery = entityManager.createQuery(projectCodeQry.toString(), ProjectCode.class);
        params.entrySet().forEach(entry -> typedQuery.setParameter(entry.getKey(), entry.getValue()));

        return typedQuery.getResultList();
    }

    public List<String> getAssetListByProjectCode(final Long projectCodeId) throws NoSuchObjectException {
        final List<String> assetCodeList = new ArrayList<>();
        final ProjectCode pc = entityManager.find(ProjectCode.class, projectCodeId);
        if (pc == null)
            throw new NoSuchObjectException("projectcode.notfound");
        final List<AssetsForEstimate> assetsForEstimateList = assetsForEstimateService.findAllByNamedQuery(
                "ASSETS_FOR_PROJECTCODE", projectCodeId);
        if (assetsForEstimateList.isEmpty())
            throw new NoSuchObjectException("assetsforestimate.projectcode.asset.notfound");
        else
            for (final AssetsForEstimate assetsForEstimate : assetsForEstimateList)
                assetCodeList.add(assetsForEstimate.getAsset().getCode());
        return assetCodeList;
    }

    @Override
    public List<ProjectCode> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        return null;

    }

    @Override
    public List<ProjectCode> getEntitiesById(final List<Long> idsList) throws ValidationException {

        return null;

    }

    public ProjectCode findByCode(final String code) {
        final List<ProjectCode> results = entityManager
                .createQuery("from ProjectCode as p where upper(p.code) = :code", ProjectCode.class)
                .setParameter("code", code.toUpperCase())
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public ProjectCode findActiveProjectCodeByCode(final String code) {
        final List<ProjectCode> results = entityManager
                .createQuery("from ProjectCode as p where active=true and upper(p.code) = :code", ProjectCode.class)
                .setParameter("code", code.toUpperCase())
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createProjectCode(final String code, final String name) {
        final ProjectCode projectCode = new ProjectCode();
        projectCode.setCode(code);
        projectCode.setCodeName(name);
        projectCode.setDescription(name);
        projectCode.setActive(true);
        projectCode.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                ProjectCode.class.getSimpleName(), WorksConstants.DEFAULT_PROJECTCODE_STATUS));
        persist(projectCode);
        createAccountDetailKey(projectCode);
    }

    protected void createAccountDetailKey(final ProjectCode proj) {
        final Accountdetailtype accountdetailtype = accountdetailtypeHibernateDAO
                .getAccountdetailtypeByName(WorksConstants.PROJECTCODE);
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(proj.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);

    }

}
