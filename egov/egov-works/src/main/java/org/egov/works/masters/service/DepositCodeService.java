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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.egov.commons.Accountdetailkey;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.dao.AccountdetailkeyHibernateDAO;
import org.egov.commons.service.EntityTypeService;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.masters.entity.DepositCode;
import org.egov.works.services.WorksService;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("depositCodeService")
@Transactional(readOnly = true)
public class DepositCodeService extends PersistenceService<DepositCode, Long> implements EntityTypeService {

    @Autowired
    private WorksService worksService;
    @Autowired
    private AccountdetailkeyHibernateDAO accountdetailkeyHibernateDAO;
    @PersistenceContext
    private EntityManager entityManager;

    public DepositCodeService() {
        super(DepositCode.class);
    }

    public DepositCodeService(final Class<DepositCode> type) {
        super(type);
    }

    public DepositCode getDepositCodeById(final Long DepositCodeId) {
        final DepositCode depositCode = entityManager.find(DepositCode.class, DepositCodeId);
        return depositCode;
    }

    public List<DepositCode> getAllDepositCodes() {
        final Query query = entityManager.createQuery("from DepositCode");
        final List<DepositCode> depositCodeList = query.getResultList();
        return depositCodeList;
    }

    @Transactional
    public void createAccountDetailKey(final DepositCode dc) {
        final Accountdetailtype accountdetailtype = worksService.getAccountdetailtypeByName("DEPOSITCODE");
        final Accountdetailkey adk = new Accountdetailkey();
        adk.setGroupid(1);
        adk.setDetailkey(dc.getId().intValue());
        adk.setDetailname(accountdetailtype.getAttributename());
        adk.setAccountdetailtype(accountdetailtype);
        accountdetailkeyHibernateDAO.create(adk);
    }

    /*
     * (non-Javadoc)
     * @see org.egov.commons.service.EntityTypeService#filterActiveEntities(java. lang.String, int, java.lang.Integer)
     */
    @Override
    public List<DepositCode> filterActiveEntities(final String filterKey, final int maxRecords,
            final Integer accountDetailTypeId) {
        final Integer pageSize = maxRecords > 0 ? maxRecords : null;
        final String param = "%" + filterKey.toUpperCase() + "%";

        final Page page = findPageByNamedQuery(WorksConstants.QUERY_GETACTIVEDEPOSITCODES_BY_CODE_OR_DESC, 1, pageSize,
                param, param);

        return page.getList();
    }

    /*
     * (non-Javadoc)
     * @see org.egov.commons.service.EntityTypeService#getAllActiveEntities(java. lang.Integer)
     */
    @Override
    public List<DepositCode> getAllActiveEntities(final Integer accountDetailTypeId) {
        return findAllByNamedQuery(WorksConstants.QUERY_GETACTIVEDEPOSITCODES);
    }

    @Override
    public List getAssetCodesForProjectCode(final Integer accountdetailkey) throws ValidationException {

        return null;
    }

    @Override
    public List<DepositCode> validateEntityForRTGS(final List<Long> idsList) throws ValidationException {

        return null;

    }

    @Override
    public List<DepositCode> getEntitiesById(final List<Long> idsList) throws ValidationException {

        return null;

    }
}
