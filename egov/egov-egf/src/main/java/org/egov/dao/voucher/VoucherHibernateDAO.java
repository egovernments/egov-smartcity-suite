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
/**
 *
 */
package org.egov.dao.voucher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CGeneralLedger;
import org.egov.commons.CGeneralLedgerDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.HibernateException;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Manoranjan
 */
@Transactional(readOnly = true)
public class VoucherHibernateDAO extends PersistenceService<CVoucherHeader, Long> {

    private static final Logger LOGGER = Logger.getLogger(VoucherHibernateDAO.class);

    @Autowired
    private AppConfigValueService appConfigValuesService;
    private PersistenceService persistenceService;

    public VoucherHibernateDAO() {
        super(CVoucherHeader.class);
    }

    public VoucherHibernateDAO(Class<CVoucherHeader> type) {
        super(type);
    }

    public List<CVoucherHeader> getVoucherList(final CVoucherHeader voucherHeader,
                                               final Map<String, Object> searchFilterMap) throws ApplicationException, ParseException {

        final StringBuffer sql = new StringBuffer(500);
        sql.append(" and vh.type = 'Journal Voucher' ");
        sql.append(" and vh.isConfirmed != 1 ");
        if (null != voucherHeader.getVoucherNumber() && StringUtils.isNotEmpty(voucherHeader.getVoucherNumber())) {
            sql.append(" and vh.voucherNumber like ?1");
        }
        if (null != searchFilterMap.get(Constants.VOUCHERDATEFROM) && StringUtils.isNotEmpty
                (searchFilterMap.get(Constants.VOUCHERDATEFROM).toString()))
            sql.append(" and vh.voucherDate >= ?2");
        if (null != searchFilterMap.get(Constants.VOUCHERDATETO) && StringUtils.isNotEmpty
                (searchFilterMap.get(Constants.VOUCHERDATETO).toString()))
            sql.append(" and vh.voucherDate <= ?3");

        if (null != voucherHeader.getFundId())
            sql.append(" and vh.fundId = ?4");
        if (null != voucherHeader.getVouchermis().getFundsource())
            sql.append(" and vh.fundsourceId = ?5");

        if (null != voucherHeader.getVouchermis().getDepartmentid())
            sql.append(" and vh.vouchermis.departmentid = ?6");

        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vh.vouchermis.schemeid = ?7");

        if (null != voucherHeader.getVouchermis().getSubschemeid())
            sql.append(" and vh.vouchermis.subschemeid = ?8");
        if (null != voucherHeader.getVouchermis().getFunctionary())
            sql.append(" and vh.vouchermis.functionary = ?9");
        if (null != voucherHeader.getVouchermis().getDivisionid())
            sql.append(" and vh.vouchermis.divisionid = ?10");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("sql====================" + sql.toString());
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("finance",
                "statusexcludeReport");
        final String statusExclude = appList.get(0).getValue();

        final List<CVoucherHeader> list = findAllBy(new StringBuilder(" from CVoucherHeader vh where vh.status not in (")
                        .append(statusExclude)
                        .append(") ")
                        .append(sql.toString())
                        .append(" order by vh.cgn,vh.voucherNumber,vh.voucherDate ").toString(), "%".concat(voucherHeader.getVoucherNumber()).concat("%"),
                Constants.DDMMYYYYFORMAT1.format(Constants.DDMMYYYYFORMAT2.
                        parse(searchFilterMap.get(Constants.VOUCHERDATEFROM).toString())), Constants.DDMMYYYYFORMAT1.format(Constants.DDMMYYYYFORMAT2.
                        parse(searchFilterMap.get(Constants.VOUCHERDATETO).toString())), voucherHeader.getFundId().getId(), voucherHeader.getVouchermis().getFundsource().getId(),
                voucherHeader.getVouchermis().getDepartmentid().getId(), voucherHeader.getVouchermis().getSchemeid().getId(), voucherHeader.getVouchermis().getSubschemeid().getId(),
                voucherHeader.getVouchermis().getFunctionary().getId(), voucherHeader.getVouchermis().getDivisionid().getId());
        return list;
    }

    @SuppressWarnings("unchecked")
    public CVoucherHeader getVoucherHeaderById(final Long voucherId) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherHibernateDAO | getVoucherHeaderById | Start ");
        final List<CVoucherHeader> vhList = getSession().createQuery("from CVoucherHeader where id = :id")
                .setParameter("id", voucherId, LongType.INSTANCE)
                .list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("numer of voucher with voucherheaderid " + voucherId + "=" + vhList.size());
        return vhList.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<CGeneralLedger> getGLInfo(final Long voucherId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherHibernateDAO | getGLInfo | Start ");
        return getSession().createQuery("from CGeneralLedger where voucherHeaderId.id = :id")
                .setParameter("id", voucherId, LongType.INSTANCE)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<CGeneralLedgerDetail> getGeneralledgerdetail(final Long gledgerId) {
        return getSession().createQuery("from CGeneralLedgerDetail where generalLedgerId.id = :id")
                .setParameter("id", gledgerId, LongType.INSTANCE)
                .list();
    }

    public Accountdetailtype getAccountDetailById(final Integer accDetailTypeId) {
        return (Accountdetailtype) getSession().createQuery("from Accountdetailtype where id = :id")
                .setParameter("id", accDetailTypeId, IntegerType.INSTANCE)
                .list().get(0);
    }

    public EntityType getEntityInfo(final Integer detailKeyId, final Integer detailtypeId) throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherHibernateDAO | getDetailCodeName | start");
        EntityType entity = null;
        try {
            final Accountdetailtype accountdetailtype = getAccountDetailById(detailtypeId);
            final Class<?> service = Class.forName(accountdetailtype.getFullQualifiedName());
            // getting the entity type service.
            final String detailTypeName = service.getSimpleName();
            String dataType = "";
            final java.lang.reflect.Method method = service.getMethod("getId");
            dataType = method.getReturnType().getSimpleName();
            if (dataType.equals("Long"))
                entity = (EntityType) persistenceService.find(
                        new StringBuilder("from ").append(detailTypeName).append(" where id = ?1 order by name").toString(), detailKeyId.longValue());
            else
                entity = (EntityType) persistenceService.find(
                        new StringBuilder("from ").append(detailTypeName).append(" where id = ?1 order by name").toString(), detailKeyId);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VoucherHibernateDAO | getDetailCodeName | End");
        return entity;

    }

    /*
     * public void deleteVoucherDetailByVHId(final Object voucherHeaderId){ try { Query qry
     * =getSession().createQuery("delete from VoucherDetail where voucherHeaderId.id=:vhid");
     * qry.setLong("vhid", Long.parseLong(voucherHeaderId.toString())); qry.executeUpdate(); } catch (HibernateException e) {
     * throw new HibernateException("exception in voucherHibDao while deleting from voucher detail"+e); }catch
     * (ApplicationRuntimeException e) { throw new
     * ApplicationRuntimeException("exception in voucherHibDao while deleting from voucher detail"+e); } }
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public void deleteGLDetailByVHId(final Object voucherHeaderId) {

        try {
            /**
             * Deleting record from general ledger detail.
             */
            final List<CGeneralLedger> glList = getGLInfo(Long.parseLong(voucherHeaderId.toString()));
            for (final CGeneralLedger generalLedger : glList) {
                final List<CGeneralLedgerDetail> glDetailList = getSession().createQuery("from CGeneralLedgerDetail where generalLedgerId.id = :id")
                        .setParameter("id", generalLedger.getId())
                        .list();
                for (final CGeneralLedgerDetail generalLedgerDetail : glDetailList) {
                    getSession().createNativeQuery("delete from EG_REMITTANCE_GLDTL where GLDTLID = :gldetailId")
                            .setParameter("gldetailId", Integer.valueOf(generalLedgerDetail.getId().toString()), IntegerType.INSTANCE)
                            .executeUpdate();
                }
            }
            /**
             * Deleting record from general ledger .
             */
            /*
             * Query qry
             * =getSession().createQuery("delete from CGeneralLedger where voucherHeaderId.id=:vhid");
             * qry.setInteger("vhid", Integer.valueOf(voucherHeaderId.toString())); qry.executeUpdate();
             */

        } catch (final HibernateException e) {
            throw new HibernateException("exception in voucherHibDao while deleting from general ledger", e);
        } catch (final ApplicationRuntimeException e) {
            throw new ApplicationRuntimeException("exception in voucherHibDao while deleting from general ledger", e);
        }

    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}