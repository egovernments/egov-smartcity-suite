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
package org.egov.services.deduction;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.utils.EntityType;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.egf.model.AutoRemittanceBeanReport;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.deduction.RemittanceBean;
import org.egov.utils.Constants;
import org.egov.utils.VoucherHelper;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author manoranjan
 */
public class RemitRecoveryService {

    private static final Logger LOGGER = Logger.getLogger(RemitRecoveryService.class);
    private static final SimpleDateFormat DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private PersistenceService persistenceService;
    private VoucherHibernateDAO voucherHibDAO;

    public List<RemittanceBean> getPendingRecoveryDetails(final RemittanceBean remittanceBean,
                                                          final CVoucherHeader voucherHeader,
                                                          final Integer detailKeyId) throws ValidationException {
        final List<RemittanceBean> listRemitBean = new ArrayList<RemittanceBean>();
        final StringBuffer query = new StringBuffer(200);
        final Map.Entry<String, Map<String, Object>> queryMapEntry = VoucherHelper.getMisQuery(voucherHeader).entrySet().iterator().next();
        final String queryString = queryMapEntry.getKey();
        final Map<String, Object> queryParams = queryMapEntry.getValue();
        query.append("select vh.name,vh.voucherNumber ,vh.voucherDate,egr.gldtlamt,gld.detailTypeId.id,gld.detailKeyId,egr.id ")
                .append(" from CVoucherHeader vh ,Vouchermis mis, CGeneralLedger gl ,CGeneralLedgerDetail gld , EgRemittanceGldtl egr , Recovery rec ")
                .append(" where rec.chartofaccounts.id = gl.glcodeId.id and gld.id = egr.generalledgerdetail.id and  gl.id = gld.generalLedgerId.id and vh.id = gl.voucherHeaderId.id ")
                .append(" and mis.voucherheaderid.id = vh.id  and vh.status=0  and vh.fundId.id=:fundId  and  egr.gldtlamt - (select  case when sum(egd.remittedamt) is null then 0")
                .append(" else sum(egd.remittedamt) end  from EgRemittanceGldtl egr1, EgRemittanceDetail egd,EgRemittance  eg,CVoucherHeader vh")
                .append(" where vh.status not in (1,2,4) and  eg.voucherheader.id=vh.id and egd.egRemittance.id=eg.id and egr1.id=egd.egRemittanceGldtl.id and egr1.id=egr.id) != 0")
                .append(" and rec.id = :recoveryId and ( egr.recovery.id = :recoveryId OR egr.recovery.id is null ) and vh.voucherDate <= :voucherDate");
        if (detailKeyId != null && detailKeyId != -1)
            query.append(" and egr.generalledgerdetail.detailkeyid = :detailKeyId");
        query.append(queryString).append(" order by vh.voucherNumber,vh.voucherDate");

        final Query qry = persistenceService.getSession().createQuery(query.toString())
                .setParameter("fundId", voucherHeader.getFundId().getId(), LongType.INSTANCE)
                .setParameter("recoveryId", remittanceBean.getRecoveryId(), LongType.INSTANCE)
                .setParameter("voucherDate", Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate()), StringType.INSTANCE);
        queryParams.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
        if (detailKeyId != null && detailKeyId != -1)
            qry.setParameter("detailKeyId", detailKeyId, IntegerType.INSTANCE);

        final List<Object[]> list = qry.list();
        populateDetails(list, listRemitBean);
        return listRemitBean;
    }

    public List<RemittanceBean> getRecoveryDetails(final RemittanceBean remittanceBean, final CVoucherHeader voucherHeader)
            throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | Start");
        final List<RemittanceBean> listRemitBean = new ArrayList<>();
        final StringBuilder dateQry = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        if (remittanceBean.getFromVhDate() != null && voucherHeader.getVoucherDate() != null) {
            dateQry.append(" and vh.VOUCHERDATE >= :fromVhDate and vh.VOUCHERDATE <= :voucherDate ");
            params.put("fromVhDate", remittanceBean.getFromVhDate());
            params.put("voucherDate", voucherHeader.getVoucherDate());
        } else {
            dateQry.append(" and vh.VOUCHERDATE <= :voucherDate ");
            params.put("voucherDate", voucherHeader.getVoucherDate());
        }
        if (remittanceBean.getBank() != null && remittanceBean.getBankBranchId() != null
                && remittanceBean.getBankAccountId() != null) {
            query = getRecoveryListForSelectedBank(remittanceBean, voucherHeader, dateQry, params);
        } else {
            query.append(" SELECT vh.NAME  AS col_0_0_,  vh.VOUCHERNUMBER AS col_1_0_,  vh.VOUCHERDATE   AS col_2_0_,")
                    .append(" egr.GLDTLAMT   AS col_3_0_,  gld.DETAILTYPEID  AS col_4_0_,  gld.DETAILKEYID   AS col_5_0_,")
                    .append(" egr.ID    AS col_6_0_, (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end")
                    .append(" from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,voucherheader vh")
                    .append(" where vh.status!=4 and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid ")
                    .append(" and egr1.id=egr.id) As col_7_0 , mis.departmentid as col_8_0,mis.functionid as col_9_0")
                    .append("  FROM VOUCHERHEADER vh,  VOUCHERMIS mis,  GENERALLEDGER gl,  GENERALLEDGERDETAIL gld,  EG_REMITTANCE_GLDTL egr,  TDS recovery5_")
                    .append(" WHERE recovery5_.GLCODEID  =gl.GLCODEID AND gld.ID =egr.GLDTLID AND gl.ID =gld.GENERALLEDGERID AND vh.ID =gl.VOUCHERHEADERID")
                    .append(" AND mis.VOUCHERHEADERID  =vh.ID AND vh.STATUS    =0 AND vh.FUNDID  = :fundId")
                    .append(" AND egr.GLDTLAMT-")
                    .append(" (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,")
                    .append("eg_remittance  eg,voucherheader vh")
                    .append(" where vh.status not in (1,2,4) and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid and egr1.id=egr.id)")
                    .append(" <>0 AND recovery5_.ID  = :recoveryId AND (egr.TDSID = :recoveryId OR egr.TDSID  IS NULL) ")
                    .append(dateQry)
                    .append(getMisSQlQuery(voucherHeader, params))
                    .append(" ORDER BY vh.VOUCHERNUMBER,  vh.VOUCHERDATE");

            params.put("fundId", voucherHeader.getFundId().getId());
            params.put("recoveryId", remittanceBean.getRecoveryId());
        }


        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | query := " + query.toString());

        final NativeQuery searchNativeQuery = persistenceService.getSession().createNativeQuery(query.toString());
        params.entrySet().forEach(entry -> searchNativeQuery.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> list = searchNativeQuery.list();

        populateDetailsBySQL(voucherHeader, listRemitBean, list);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | listRemitBean size : " + listRemitBean.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | End");
        return listRemitBean;
    }

    public StringBuilder getRecoveryListForSelectedBank(final RemittanceBean remittanceBean, final CVoucherHeader voucherHeader,
                                                        final StringBuilder dateQuery, final Map<String, Object> params) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT vh.NAME  AS col_0_0_,  vh.VOUCHERNUMBER AS col_1_0_,  vh.VOUCHERDATE   AS col_2_0_,")
                .append(" egr.GLDTLAMT   AS col_3_0_,  gld.DETAILTYPEID  AS col_4_0_,  gld.DETAILKEYID   AS col_5_0_,")
                .append(" egr.ID    AS col_6_0_, (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end")
                .append(" from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,voucherheader vh")
                .append(" where vh.status not in (4) and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid ")
                .append(" and egr1.id=egr.id) As col_7_0 , mis.departmentid as col_8_0,mis.functionid as col_9_0")
                .append("  FROM VOUCHERHEADER vh,  VOUCHERMIS mis,  GENERALLEDGER gl,  GENERALLEDGERDETAIL gld,  EG_REMITTANCE_GLDTL egr,  TDS recovery5_ ,PAYMENTHEADER ph,")
                .append("miscbilldetail misbill")
                .append(" WHERE recovery5_.GLCODEID  =gl.GLCODEID AND gld.ID =egr.GLDTLID AND gl.ID =gld.GENERALLEDGERID AND vh.ID =gl.VOUCHERHEADERID")
                .append(" AND mis.VOUCHERHEADERID  =vh.ID AND vh.STATUS    =0 and misbill.billvhid=vh.id and misbill.payvhid=ph.voucherheaderid")
                .append(" and (select status from voucherheader where id=misbill.payvhid )=0  AND ph.bankaccountnumberid = :accountNumberId")
                .append(" and vh.FUNDID    = :fundId")
                .append(" AND egr.GLDTLAMT-")
                .append(" (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,")
                .append("eg_remittance  eg,voucherheader vh")
                .append(" where vh.status not in (1,2,4) and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid and egr1.id=egr.id)")
                .append(" <>0 AND recovery5_.ID  = :recoveryId AND (egr.TDSID = :recoveryId OR egr.TDSID  IS NULL) ")
                .append(dateQuery)
                .append(getMisSQlQuery(voucherHeader, params))
                .append(" ORDER BY vh.VOUCHERNUMBER,  vh.VOUCHERDATE");

        params.put("accountNumberId", remittanceBean.getBankAccountId());
        params.put("fundId", voucherHeader.getFundId().getId());
        params.put("recoveryId", remittanceBean.getRecoveryId());
        return query;
    }

    public List<RemittanceBean> getRecoveryDetails(final String selectedRows)
            throws ValidationException {
        String [] items = selectedRows.split(",");
        List<Long> selectedRowId = new ArrayList<>();
        for(int i=0; i< items.length;i++){
            selectedRowId.add(Long.valueOf(items[i]));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | Start");
        final List<RemittanceBean> listRemitBean = new ArrayList<RemittanceBean>();

        StringBuilder query = new StringBuilder();
        query.append(" SELECT vh.NAME  AS col_0_0_,  vh.VOUCHERNUMBER AS col_1_0_,  vh.VOUCHERDATE   AS col_2_0_,")
                .append(" egr.GLDTLAMT   AS col_3_0_,  gld.DETAILTYPEID  AS col_4_0_,  gld.DETAILKEYID   AS col_5_0_,")
                .append(" egr.ID    AS col_6_0_, (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end")
                .append(" from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,voucherheader vh")
                .append(" where vh.status!=4 and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid ")
                .append(" and egr1.id=egr.id) As col_7_0 , mis.departmentid as col_8_0,mis.functionid as col_9_0")
                .append("  FROM VOUCHERHEADER vh,  VOUCHERMIS mis,  GENERALLEDGER gl,  GENERALLEDGERDETAIL gld,  EG_REMITTANCE_GLDTL egr,  TDS recovery5_")
                .append(" WHERE recovery5_.GLCODEID  =gl.GLCODEID AND gld.ID =egr.GLDTLID AND gl.ID =gld.GENERALLEDGERID AND vh.ID =gl.VOUCHERHEADERID")
                .append(" AND mis.VOUCHERHEADERID  =vh.ID AND vh.STATUS    =0 ")
                .append(" and egr.id in ( :selectedRows) ")
                .append(" AND egr.GLDTLAMT-")
                .append(" (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,")
                .append("voucherheader vh")
                .append(" where vh.status not in (1,2,4) and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid and egr1.id=egr.id)")
                .append(" <>0  ")
                .append(" ORDER BY vh.VOUCHERNUMBER,  vh.VOUCHERDATE");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | query := " + query.toString());

        final NativeQuery searchNativeQuery = persistenceService.getSession().createNativeQuery(query.toString());
        searchNativeQuery.setParameterList("selectedRows", selectedRowId, LongType.INSTANCE);
        final List<Object[]> list = searchNativeQuery.list();

        populateDetailsBySQL(null, listRemitBean, list);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | listRemitBean size : " + listRemitBean.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | End");
        return listRemitBean;
    }

    public List<RemittanceBean> getRecoveryDetailsForReport(final RemittanceBean remittanceBean,
                                                            final CVoucherHeader voucherHeader,
                                                            final Integer detailKeyId) throws ValidationException {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | Start");
        final List<RemittanceBean> listRemitBean = new ArrayList<RemittanceBean>();
        StringBuilder query = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();
        query.append(" SELECT vh.NAME     AS col_0_0_,  vh.VOUCHERNUMBER AS col_1_0_,  vh.VOUCHERDATE   AS col_2_0_,")
                .append(" egr.GLDTLAMT      AS col_3_0_,  gld.DETAILTYPEID  AS col_4_0_,  gld.DETAILKEYID   AS col_5_0_,")
                .append(" egr.ID            AS col_6_0_, (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end")
                .append(" from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,voucherheader vh")
                .append(" where vh.status!=4 and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid ")
                .append(" and egr1.id=egr.id) As col_7_0, mis.departmentid as col_8_0,mis.functionid as col_9_0")
                .append("  FROM VOUCHERHEADER vh,  VOUCHERMIS mis,  GENERALLEDGER gl,  GENERALLEDGERDETAIL gld,  EG_REMITTANCE_GLDTL egr,  TDS recovery5_")
                .append(" WHERE recovery5_.GLCODEID  =gl.GLCODEID AND gld.ID =egr.GLDTLID AND gl.ID =gld.GENERALLEDGERID AND vh.ID =gl.VOUCHERHEADERID")
                .append(" AND mis.VOUCHERHEADERID  =vh.ID AND vh.STATUS    =0 AND vh.FUNDID    = :fundId")
                .append(" AND egr.GLDTLAMT-")
                .append(" (select  case when sum(egd.remittedamt) is null then 0 else sum(egd.remittedamt) end from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,")
                .append("voucherheader vh")
                .append(" where vh.status not in (1,2,4) and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid and egr1.id=egr.id)")
                .append(" <>0 AND recovery5_.ID  = :recoveryId AND (egr.TDSID  = :recoveryId OR egr.TDSID  IS NULL) AND vh.VOUCHERDATE <= :voucherDate ");
        params.put("fundId", voucherHeader.getFundId().getId());
        params.put("recoveryId", remittanceBean.getRecoveryId());
        params.put("voucherDate", voucherHeader.getVoucherDate());
        if (remittanceBean.getFromDate() != null && !remittanceBean.getFromDate().isEmpty()) {
            query.append("  and vh.VoucherDate >= :fromDate");
            params.put("fromDate", remittanceBean.getFromDate());
        }
        if (detailKeyId != null && detailKeyId.intValue() != 0) {
            query.append(" and gld.detailkeyid = :detailkeyid");
            params.put("detailkeyid", detailKeyId);
        }
        query.append("   ").append(getMisSQlQuery(voucherHeader, params)).append(" ORDER BY vh.VOUCHERNUMBER,  vh.VOUCHERDATE");

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | query := " + query.toString());

        final NativeQuery searchNativeQuery = persistenceService.getSession().createNativeQuery(query.toString());
        params.entrySet().forEach(entry -> searchNativeQuery.setParameter(entry.getKey(), entry.getValue()));
        final List<Object[]> list = searchNativeQuery.list();

        populateDetailsBySQL(voucherHeader, listRemitBean, list);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | listRemitBean size : " + listRemitBean.size());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("RemitRecoveryService | getRecoveryDetails | End");
        return listRemitBean;
    }

    /**
     * @param voucherHeader
     * @return
     */
    private Object getMisSQlQuery(final CVoucherHeader voucherHeader, final Map<String, Object> params) {
        final StringBuilder misQuery = new StringBuilder();
        if (null != voucherHeader && null != voucherHeader.getVouchermis()) {
            if (null != voucherHeader.getVouchermis().getDepartmentid()
                    && null != voucherHeader.getVouchermis().getDepartmentid().getId()
                    && -1 != voucherHeader.getVouchermis().getDepartmentid().getId()) {
                misQuery.append("and  mis.departmentid = :departmentId");
                params.put("departmentId", voucherHeader.getVouchermis().getDepartmentid().getId());
            }
            if (null != voucherHeader.getVouchermis().getFunctionary()
                    && null != voucherHeader.getVouchermis().getFunctionary().getId()
                    && -1 != voucherHeader.getVouchermis().getFunctionary().getId()) {
                misQuery.append(" and mis.functionaryid = :functionaryId");
                params.put("functionaryId", voucherHeader.getVouchermis().getFunctionary().getId());
            }
            if (null != voucherHeader.getVouchermis().getFunction()
                    && null != voucherHeader.getVouchermis().getFunction().getId()
                    && -1 != voucherHeader.getVouchermis().getFunction().getId()) {
                misQuery.append(" and mis.functionid = :functionId");
                params.put("functionId", voucherHeader.getVouchermis().getFunction().getId());
            }
            if (null != voucherHeader.getVouchermis().getSchemeid()
                    && null != voucherHeader.getVouchermis().getSchemeid().getId()
                    && -1 != voucherHeader.getVouchermis().getSchemeid().getId()) {
                misQuery.append(" and mis.schemeid = :schemeId");
                params.put("schemeId", voucherHeader.getVouchermis().getSchemeid().getId());
            }
            if (null != voucherHeader.getVouchermis().getSubschemeid()
                    && null != voucherHeader.getVouchermis().getSubschemeid().getId()
                    && -1 != voucherHeader.getVouchermis().getSubschemeid().getId()) {
                misQuery.append(" and mis.subschemeid = :subschemeId");
                params.put("subschemeId", voucherHeader.getVouchermis().getSubschemeid().getId());
            }
            if (null != voucherHeader.getVouchermis().getFundsource()
                    && null != voucherHeader.getVouchermis().getFundsource().getId()
                    && -1 != voucherHeader.getVouchermis().getFundsource().getId()) {
                misQuery.append(" and mis.fundsourceid = :fundsourceId");
                params.put("fundsourceId", voucherHeader.getVouchermis().getFundsource().getId());
            }
            if (null != voucherHeader.getVouchermis().getDivisionid()
                    && null != voucherHeader.getVouchermis().getDivisionid().getId()
                    && -1 != voucherHeader.getVouchermis().getDivisionid().getId()) {
                misQuery.append(" and mis.divisionid = :divisionId");
                params.put("divisionId", voucherHeader.getVouchermis().getDivisionid().getId());
            }
        }
        return misQuery.toString();
    }

    private void populateDetailsBySQL(final CVoucherHeader voucherHeader, final List<RemittanceBean> listRemitBean, final List<Object[]> list) {
        RemittanceBean remitBean;
        for (final Object[] element : list) {
            remitBean = new RemittanceBean();
            remitBean.setVoucherName(element[0].toString());
            remitBean.setVoucherNumber(element[1].toString());
            try {
                remitBean.setVoucherDate(DDMMYYYY.format(YYYYMMDD.parse(element[2].toString())));
            } catch (final ParseException e) {
                LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
            }
            remitBean.setDeductionAmount(BigDecimal.valueOf(Double.parseDouble(element[3].toString())));
            if (element[7] != null)
                remitBean.setEarlierPayment(BigDecimal.valueOf(Double.parseDouble(element[7].toString())));
            else
                remitBean.setEarlierPayment(BigDecimal.ZERO);
            if (remitBean.getEarlierPayment() != null && remitBean.getEarlierPayment().compareTo(BigDecimal.ZERO) != 0)
                remitBean.setAmount(remitBean.getDeductionAmount().subtract(remitBean.getEarlierPayment()));
            else
                remitBean.setAmount(remitBean.getDeductionAmount());
            remitBean.setDepartmentId(Long.valueOf(element[8].toString()));
            remitBean.setFunctionId(Long.valueOf(element[9].toString()));
            final EntityType entity = voucherHibDAO.getEntityInfo(Integer.valueOf(element[5].toString()),
                    Integer.valueOf(element[4].toString()));
            if (entity == null) {
                LOGGER.error("Entity Might have been deleted........................");
                LOGGER.error("The detail key " + Integer.valueOf(element[5].toString()) + " of detail type "
                        + Integer.valueOf(element[4].toString())
                        + "Missing in voucher" + remitBean.getVoucherNumber());
                throw new ValidationException(Arrays.asList(new ValidationError("Entity information not available for voucher "
                        + remitBean.getVoucherNumber(), "Entity information not available for voucher "
                        + remitBean.getVoucherNumber())));
            }
            // Exception here
            if (voucherHeader == null)
                remitBean.setPartialAmount(remitBean.getDeductionAmount());
            remitBean.setPartyCode(entity.getCode());
            remitBean.setPartyName(entity.getName());
            remitBean.setPanNo(entity.getPanno());
            remitBean.setDetailTypeId(Integer.valueOf(element[4].toString()));
            remitBean.setDetailKeyid(Integer.valueOf(element[5].toString()));
            remitBean.setRemittance_gl_dtlId(Integer.valueOf(element[6].toString()));
            listRemitBean.add(remitBean);
        }
    }

    private void populateDetails(final List<Object[]> list, final List<RemittanceBean> listRemitBean) {
        RemittanceBean remitBean;
        for (final Object[] element : list) {
            remitBean = new RemittanceBean();
            remitBean.setVoucherName(element[0].toString());
            remitBean.setVoucherNumber(element[1].toString());
            try {
                remitBean.setVoucherDate(DDMMYYYY.format(YYYYMMDD.parse(element[2].toString())));
            } catch (final ParseException e) {
                LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
            }
            remitBean.setAmount(BigDecimal.valueOf(Double.parseDouble(element[3].toString())));
            final EntityType entity = voucherHibDAO.getEntityInfo(Integer.valueOf(element[5].toString()),
                    Integer.valueOf(element[4].toString()));
            remitBean.setPartyCode(entity.getCode());
            remitBean.setPartyName(entity.getName());
            remitBean.setPanNo(entity.getPanno());
            remitBean.setDetailTypeId(Integer.valueOf(element[4].toString()));
            remitBean.setDetailKeyid(Integer.valueOf(element[5].toString()));
            remitBean.setRemittance_gl_dtlId(Integer.valueOf(element[6].toString()));
            listRemitBean.add(remitBean);
        }
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setVoucherHibDAO(final VoucherHibernateDAO voucherHibDAO) {
        this.voucherHibDAO = voucherHibDAO;
    }

    public List<AutoRemittanceBeanReport> populateAutoRemittanceDetailbySQL(final Query sqlQuery) {
        final List<AutoRemittanceBeanReport> remittanceList = sqlQuery.list();
        final List<AutoRemittanceBeanReport> autoRemittance = new ArrayList<AutoRemittanceBeanReport>(0);

        final StringBuffer voucherQueryOne = new StringBuffer("SELECT  remgldtl.REMITTEDAMT AS remittedAmount, (SELECT SUM(creditamount)")
                .append(" FROM GENERALLEDGER gld1")
                .append(" WHERE gld1.voucherheaderid =gld.voucherheaderid) AS billAmount, vh.VOUCHERNUMBER AS voucherNumber, miscbilldtl.billnumber AS billNumber,")
                .append(" remdtl.id as remittanceDTId, gldtl.DETAILTYPEID  as detailKeyTypeId ,  gldtl.DETAILKEYID as detailKeyId,vh.id as voucherId,billmis.BILLID as billId")
                .append(" FROM EG_REMITTANCE_DETAIL remdtl,EG_REMITTANCE_GLDTL remgldtl,  GENERALLEDGERDETAIL gldtl,GENERALLEDGER gld,VOUCHERHEADER vh, MISCBILLDETAIL miscbilldtl,")
                .append(" eg_billregistermis billmis ")
                .append(" WHERE  remdtl.REMITTANCEGLDTLID = remgldtl.id AND gldtl.ID = remgldtl.GLDTLID  AND gldtl.GENERALLEDGERID = gld.id AND gld.VOUCHERHEADERID =vh.id")
                .append(" AND miscbilldtl.billvhid =vh.id AND billmis.VOUCHERHEADERID=vh.id ");
        StringBuffer inquery = new StringBuffer(" AND remdtl.id in ( ");
        int i = 1;
        if (null != remittanceList && !remittanceList.isEmpty()) {

            for (final AutoRemittanceBeanReport remittance : remittanceList) {
                if (i % 1000 == 0) {
                    inquery.append(")");
                    final StringBuffer voucherQueryTwo = new StringBuffer(voucherQueryOne)
                            .append(inquery.toString())
                            .append(" GROUP BY  vh.vouchernumber, miscbilldtl.billnumber , remgldtl.remittedamt, remdtl.ID,  gldtl.detailtypeid , gldtl.detailkeyid,vh.id,")
                            .append("gld.voucherheaderid,billmis.BILLID");
                    final Query sqlVoucherQuery = persistenceService.getSession().createNativeQuery(voucherQueryTwo.toString())
                            .addScalar("remittedAmount").addScalar("billAmount").addScalar("voucherNumber")
                            .addScalar("billNumber").addScalar("remittanceDTId")
                            .addScalar("detailKeyTypeId").addScalar("detailKeyId").addScalar("voucherId").addScalar("billId")
                            .setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
                    autoRemittance.addAll(sqlVoucherQuery.list());
                    inquery = new StringBuffer(" AND remdtl.id in ( ").append(remittance.getRemittanceDTId().toString());
                } else {
                    if (i != 1)
                        inquery.append(",");
                    inquery.append(remittance.getRemittanceDTId().toString());
                }
                i++;
            }
            inquery.append(")");
            final StringBuffer voucherQueryTwo = new StringBuffer(voucherQueryOne)
                    .append(inquery.toString())
                    .append(" GROUP BY  vh.vouchernumber, miscbilldtl.billnumber , remgldtl.remittedamt,    gldtl.detailtypeid , gldtl.detailkeyid,")
                    .append(" remdtl.ID,vh.id,gld.voucherheaderid,billmis.BILLID");
            final Query sqlVoucherQuery = persistenceService.getSession().createNativeQuery(voucherQueryTwo.toString())
                    .addScalar("remittedAmount").addScalar("billAmount").addScalar("voucherNumber")
                    .addScalar("billNumber").addScalar("remittanceDTId")
                    .addScalar("detailKeyTypeId").addScalar("detailKeyId").addScalar("voucherId").addScalar("billId")
                    .setResultTransformer(Transformers.aliasToBean(AutoRemittanceBeanReport.class));
            autoRemittance.addAll(sqlVoucherQuery.list());
        }
        final ArrayList<AutoRemittanceBeanReport> autoRemittanceList = new ArrayList<AutoRemittanceBeanReport>();
        for (final AutoRemittanceBeanReport remittance : remittanceList)
            for (final AutoRemittanceBeanReport autoremit : autoRemittance)
                if (autoremit.getRemittanceDTId().intValue() == remittance.getRemittanceDTId().intValue()) {
                    final AutoRemittanceBeanReport autoRemittanceBeannReport = new AutoRemittanceBeanReport();

                    autoRemittanceBeannReport.setRemittancePaymentNo(remittance.getRemittancePaymentNo());
                    autoRemittanceBeannReport.setRtgsNoDate(remittance.getRtgsNoDate());
                    autoRemittanceBeannReport.setRtgsAmount(remittance.getRtgsAmount());
                    autoRemittanceBeannReport.setDepartment(remittance.getDepartment());
                    autoRemittanceBeannReport.setDrawingOfficer(remittance.getDrawingOfficer());
                    autoRemittanceBeannReport.setFundName(remittance.getFundName());
                    autoRemittanceBeannReport.setBankbranchAccount(remittance.getBankbranchAccount());
                    autoRemittanceBeannReport.setRemittanceCOA(remittance.getRemittanceCOA());
                    autoRemittanceBeannReport.setPaymentVoucherId(remittance.getPaymentVoucherId());
                    autoRemittanceBeannReport.setBillId(remittance.getBillId());

                    autoRemittanceBeannReport.setVoucherNumber(autoremit.getVoucherNumber());
                    autoRemittanceBeannReport.setBillAmount(autoremit.getBillAmount());
                    autoRemittanceBeannReport.setBillNumber(autoremit.getBillNumber());
                    autoRemittanceBeannReport.setRemittedAmount(autoremit.getRemittedAmount());
                    autoRemittanceBeannReport.setVoucherId(autoremit.getVoucherId());
                    autoRemittanceBeannReport.setBillId(autoremit.getBillId());
                    final EntityType entity = voucherHibDAO.getEntityInfo(new Integer(autoremit.getDetailKeyId().toString()),
                            new Integer(autoremit.getDetailKeyTypeId().toString()));
                    if (entity == null) {
                        LOGGER.error("Entity Might have been deleted........................");
                        LOGGER.error("The detail key " + Integer.valueOf(autoremit.getDetailKeyId().toString())
                                + " of detail type " + Integer.valueOf(autoremit.getDetailKeyTypeId().toString())
                                + "Missing in voucher" + autoremit.getVoucherNumber());
                        throw new ValidationException(Arrays.asList(new ValidationError(
                                "Entity information not available for voucher " + autoremit.getVoucherNumber(),
                                "Entity information not available for voucher " + autoremit.getVoucherNumber())));
                    }
                    autoRemittanceBeannReport.setPartyName(entity.getName());
                    autoRemittanceList.add(autoRemittanceBeannReport);
                }
        return autoRemittanceList;
    }
}