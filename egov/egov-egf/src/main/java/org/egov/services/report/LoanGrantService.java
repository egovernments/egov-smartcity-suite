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
package org.egov.services.report;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.egf.masters.model.LoanGrantBean;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mani
 */
public class LoanGrantService extends PersistenceService {

    final static Logger LOGGER = Logger.getLogger(LoanGrantService.class);

    public LoanGrantService() {
        super(null);
    }

    public LoanGrantService(Class type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    public List<Object> schemeUtilizationBy(final Integer schemeId, final Integer subSchemeId, final Date fromDate,
            final Date toDate,
            final List<Integer> projectCodeIdList, final Integer fundId) {
        final Accountdetailtype detailType = (Accountdetailtype) find("from Accountdetailtype where upper(name)=?", "PROJECTCODE");
        final StringBuffer schemeUtilSql = new StringBuffer(512);
        String pcStr = "";
        if (projectCodeIdList != null && projectCodeIdList.size() > 0)
        {
            pcStr = projectCodeIdList.toString();
            pcStr = pcStr.replace("[", "(");
            pcStr = pcStr.replace("]", ")");
        }
        if (subSchemeId != null) {
            schemeUtilSql
            .append("select ss.name as subScheme, pc.code as code, vh.voucherNumber as vouchernumber, vh.voucherDate as voucherdate, gld.amount as amount, gld.detailkeyid as id "
                    + "from voucherheader vh, generalledger gl, generalledgerdetail gld , egf_subscheme_project ssp, egw_projectcode pc"
                    + ",sub_scheme ss "
                    + "	where "
                    + " vh.id= gl.voucherheaderid  and vh.status not in (1,2,4) "
                    + " and gl.id= gld.generalledgerid ");
            if (fromDate != null)
                schemeUtilSql.append(" and vh.voucherdate>='" + Constants.DD_MON_YYYYFORMAT.format(fromDate) + "'");
            if (toDate != null)
                schemeUtilSql.append(" and vh.voucherdate<='" + Constants.DD_MON_YYYYFORMAT.format(toDate) + "'");
            schemeUtilSql.append(" and gld.detailtypeid=" + detailType.getId() + " and gld.detailkeyid= ssp.projectcodeid "
                    + " and ssp.subschemeid=" + subSchemeId + " and ss.id=" + subSchemeId + " and ss.id=ssp.subschemeid"
                    + " and pc.id= gld.detailkeyid "
                    + " and pc.id= ssp.projectcodeid and vh.fundid=" + fundId + " ");
            if (projectCodeIdList != null && projectCodeIdList.size() > 0)
                schemeUtilSql.append(" and ssp.projectcodeid in " + pcStr + " ");
            schemeUtilSql.append("ORDER by ss.name, pc.code,vh.voucherdate ");
        }
        else if (schemeId != null) {
            schemeUtilSql
            .append("select ss.name as subScheme, pc.code as code, vh.voucherNumber as vouchernumber, vh.voucherDate as voucherdate, gld.amount as amount, gld.detailkeyid as id "
                    + "from voucherheader vh, generalledger gl, generalledgerdetail gld , egf_subscheme_project ssp, egw_projectcode pc"
                    + ",sub_scheme ss,scheme s "
                    + "	where "
                    + " vh.id= gl.voucherheaderid  and vh.status not in (1,2,4) " + " and gl.id= gld.generalledgerid ");
            if (fromDate != null)
                schemeUtilSql.append(" and vh.voucherdate>='" + Constants.DD_MON_YYYYFORMAT.format(fromDate) + "'");
            if (toDate != null)
                schemeUtilSql.append(" and vh.voucherdate<='" + Constants.DD_MON_YYYYFORMAT.format(toDate) + "'");
            schemeUtilSql.append(" and gld.detailtypeid=" + detailType.getId() + " and gld.detailkeyid= ssp.projectcodeid "
                    + " and ssp.subschemeid=ss.id" + " and ss.schemeid=s.id" + " and s.id=" + schemeId
                    + " and ss.id=ssp.subschemeid"
                    + " and pc.id= gld.detailkeyid " + " and pc.id= ssp.projectcodeid  and vh.fundid=" + fundId + " ");
            if (projectCodeIdList != null && projectCodeIdList.size() > 0)
                schemeUtilSql.append(" and ssp.projectcodeid in " + pcStr + " ");
            schemeUtilSql.append(" ORDER by ss.name, pc.code,vh.voucherdate ");
        }
        final String schemeUtilSqlQry = schemeUtilSql.toString();
        final SQLQuery schemeUtilQry = getSession().createSQLQuery(schemeUtilSqlQry);
        schemeUtilQry.addScalar("subScheme").addScalar("code").addScalar("voucherNumber").addScalar("voucherDate")
        .addScalar("amount", BigDecimalType.INSTANCE)
        .addScalar("id", LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));

        final List<Object> projecCodeResultList = schemeUtilQry.list();

        return projecCodeResultList;
    }

    /**
     * @param subSchemeId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LoanGrantBean> fundingPatternBy(final Integer subSchemeId, final Integer schemeId) {
        List<LoanGrantBean> fundingPatternList = null;
        final StringBuffer sql = new StringBuffer();
        sql.append(" select ss.name as subScheme, fa.name  as name ,  sum(lgd.percentage) as amount  from egf_LoanGrantDetail lgd,"
                + "egf_LoanGrantHeader lgh,egf_fundingAgency fa,sub_scheme ss ");
        if (schemeId != null && subSchemeId == null)
            sql.append(",Scheme s ");
        sql.append(" where lgd.headerid=lgh.id and fa.id=lgd.agencyid  and ss.id=lgh.subSchemeId ");
        if (schemeId != null && subSchemeId == null)
            sql.append(" and s.id=ss.schemeid and  s.id= " + schemeId);
        else
            sql.append(" and lgh.subSchemeId=" + subSchemeId);
        sql.append(" group by");
        /*
         * if(schemeId!=null && subSchemeId==null) { sql.append(" ss.name ,"); }
         */
        sql.append(" ss.name , fa.name order by ss.name,fa.name");
        final SQLQuery patternSql = getSession().createSQLQuery(sql.toString());
        patternSql.addScalar("subScheme", StringType.INSTANCE).addScalar("name", StringType.INSTANCE)
        .addScalar("amount", BigDecimalType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        fundingPatternList = patternSql.list();
        return fundingPatternList;
    }

    /**
     * @param schemeId
     * @param subSchemeId
     * @param fromDate
     * @param toDate
     * @param agencyId
     * @param faTypeId Funding Agency Detail Type Id
     * @param pcTypeid Project Code Detail Type Id
     * @return List<LoanGrantBean> SCHEME SUBSCHEME FUNDINGAGENCY RESULT YES YES YES Get the GJVs having both FA And PC as
     * subledgers and PC ids are mapped for the SUBSCHEME and for the selected FA
     *
     * YES NO YES Get the GJVs having both FA And PC as subledgers and PC ids are mapped for the the SUBSCHEMES under selected
     * SCHEME and for the selected FA
     *
     * YES YES NO Get the GJVs having both FA And PC as subledgers and PC ids are mapped for the SUBSCHEME and for all FA under
     * the selected SUBSCHEME
     *
     * YES NO NO Get the GJVs having both FA And PC as subledgers and PC ids are mapped for the SUBSCHEME under selected Scheme
     * and for all FA under the selected SCHEME NOTE: 1.If more than one FA is there in the JV the AgencyAmount will not match the
     * projectwiseAmount since report is by Agency 2.If a GJV uses project codes of different SUBSCHEMES report by scheme only
     * will give the correct result 3.If a GJV uses project codes of Different schemes itself then Total amount will never match
     * since widest search is by Scheme
     *
     *
     */
    @SuppressWarnings("unchecked")
    public List<Object> searchGC(final Integer schemeId, final Integer subSchemeId, final Date fromDate, final Date toDate,
            final Long agencyId,
            final Integer pcTypeId, final Integer faTypeId, final Integer fundId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting searchGC");
        List<Object> grantContribList = new ArrayList<Object>();
        if (agencyId != null & agencyId != -1) {
            if (subSchemeId != null)
                grantContribList = getDataByAgency(schemeId, subSchemeId, fromDate, toDate, agencyId, pcTypeId, faTypeId, fundId);
            else
                grantContribList = getDataByAgency(schemeId, null, fromDate, toDate, agencyId, pcTypeId, faTypeId, fundId);

        }
        else {
            List<Long> fundingAgencyList = null;
            if (subSchemeId != null)
                fundingAgencyList = findAllBy(
                        "select distinct fundingAgency.id from LoanGrantDetail lgd  where lgd.header.subScheme.id=? ",
                        subSchemeId);
            else
                fundingAgencyList = findAllBy(
                        "select distinct lgd.fundingAgency.id from LoanGrantDetail lgd ,LoanGrantHeader lg,SubScheme ss,Scheme s where lg.subScheme.id=ss.id and s.id=ss.scheme.id and lg.id=lgd.header.id and s.id=?",
                        schemeId);

            for (final Long faId : fundingAgencyList) {

                List<Object> grantContribListByAgency = new ArrayList<Object>();
                if (subSchemeId != null)
                    grantContribListByAgency = getDataByAgency(schemeId, subSchemeId, fromDate, toDate, faId, pcTypeId, faTypeId,
                            fundId);
                else
                    grantContribListByAgency = getDataByAgency(schemeId, null, fromDate, toDate, faId, pcTypeId, faTypeId, fundId);
                grantContribList.addAll(grantContribListByAgency);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("exiting from searchGC  and result size" + grantContribList.size());
        return grantContribList;
    }

    /**
     * @param subSchemeId
     * @param fromDate
     * @param toDate
     * @param agencyId
     * @param faTypeId
     * @param pcTypeId
     * @param sql
     * @return
     */
    private List<Object> getDataByAgency(final Integer schemeId, final Integer subSchemeId, final Date fromDate,
            final Date toDate, final Long agencyId,
            final Integer pcTypeId, final Integer faTypeId, final Integer fundId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getDataByAgency for agencyId:" + agencyId);
        final StringBuffer sql = new StringBuffer();
        sql
        .append(" select * from (select distinct vh.vouchernumber as voucherNumber, gld1.amount as amount, null as agencyAmount,pc.code as code, gld1.detailkeyid as detailKey, gld1.detailtypeid detailType "
                + "from voucherheader vh, generalledger gl1,generalledger gl2, generalledgerdetail gld1, generalledgerdetail gld2, "
                + "egf_subscheme_project ssp,egw_projectcode pc ");
        if (subSchemeId == null)
            sql.append(",scheme s,sub_scheme ss ");
        sql.append(" where vh.id= gl1.voucherheaderid and gl1.id= gld1.generalledgerid "
                + " and gl2.id=gld2.generalledgerid and gl2.voucherheaderid=vh.id and gl1.creditamount>0 "
                + " and gl2.debitamount>0 and gld1.detailtypeid=" + pcTypeId + " and gld2.detailtypeid=" + faTypeId
                + " and ssp.projectcodeid=gld1.detailkeyid and pc.id=gld1.detailkeyid and pc.id=ssp.projectcodeid "
                + " and vh.type='Journal Voucher' and vh.name='JVGeneral' and vh.fundid=" + fundId + " ");
        if (subSchemeId != null)
            sql.append(" and ssp.subschemeId=" + subSchemeId);
        else
            sql.append(" and ss.schemeid=s.id and s.id=" + schemeId + " and ssp.subschemeid=ss.id");
        if (fromDate != null)
            sql.append(" and vh.voucherdate>='" + Constants.DD_MON_YYYYFORMAT.format(fromDate) + "' ");
        if (toDate != null)
            sql.append(" and vh.voucherdate<='" + Constants.DD_MON_YYYYFORMAT.format(toDate) + "' ");
        sql.append(" and  gld2.detailkeyid=" + agencyId);
        sql.append(" union ");

        sql.append(" select distinct vh.vouchernumber as voucherNumber,null as amount,gld2.amount as agencyAmount,null as code, gld2.detailKeyid as detailKey, gld2.detailtypeid detailType "
                + "from voucherheader vh, generalledger gl1,generalledger gl2, generalledgerdetail gld1, generalledgerdetail gld2 ");
        sql.append(",egf_loangrantdetail lgd,egf_loanGrantHeader lg,egf_subscheme_project ssp ");
        // check here if u have errors
        if (subSchemeId == null)
            sql.append(",scheme s,sub_scheme ss ");
        sql.append(" where vh.id= gl1.voucherheaderid and gl1.id= gld1.generalledgerid "
                + "and gl2.id=gld2.generalledgerid and gl2.voucherheaderid=vh.id and gl1.creditamount>0 "
                + "and gl2.debitamount>0 and gld1.detailtypeid="
                + pcTypeId
                + " and gld2.detailtypeid="
                + faTypeId
                + "and vh.type='Journal Voucher' and vh.name='JVGeneral' and lg.id=lgd.headerid and ssp.projectcodeid=gld1.detailkeyid and vh.fundid="
                + fundId + " ");
        if (subSchemeId != null)
            sql.append("  and lg.subschemeId=" + subSchemeId + " and ssp.subschemeid=" + subSchemeId);
        else
            sql.append(" and ss.schemeid=s.id and s.id=" + schemeId + " and lg.subschemeId=ss.id and ssp.subschemeid=ss.id");
        if (fromDate != null)
            sql.append(" and vh.voucherdate>='" + Constants.DD_MON_YYYYFORMAT.format(fromDate) + "' ");
        if (toDate != null)
            sql.append(" and vh.voucherdate<='" + Constants.DD_MON_YYYYFORMAT.format(toDate) + "' ");
        sql.append(" and  gld2.detailkeyid=" + agencyId);
        if (subSchemeId != null)
            sql.append(" and lgd.agencyId= " + agencyId);
        sql.append(" ) order by  voucherNumber,detailType desc,detailKey");
        final SQLQuery gcSql = getSession().createSQLQuery(sql.toString());
        if (LOGGER.isInfoEnabled())
            LOGGER.info("sql:  " + sql.toString());
        gcSql.addScalar("voucherNumber").addScalar("code").addScalar("amount", BigDecimalType.INSTANCE)
        .addScalar("agencyAmount", BigDecimalType.INSTANCE)
        .addScalar("detailKey", IntegerType.INSTANCE).addScalar("detailType", IntegerType.INSTANCE).setResultTransformer(
                Transformers.aliasToBean(LoanGrantBean.class));
        // Grant Contribution List
        final List<Object> gcList = gcSql.list();
        if (gcList.size() > 0) {
            final List<LoanGrantBean> grantAmountList = getGrantAmountBy(schemeId, subSchemeId, agencyId);
            if (grantAmountList != null && grantAmountList.size() > 0) {
                // check if fa is used in loan header else ommit
                ((LoanGrantBean) gcList.get(0)).setAgencyName(grantAmountList.get(0).getAgencyName());
                ((LoanGrantBean) gcList.get(0)).setGrantAmount(grantAmountList.get(0).getGrantAmount());
            }

        }
        else {

            final List<LoanGrantBean> grantAmountList = getGrantAmountBy(schemeId, subSchemeId, agencyId);
            if (grantAmountList != null && grantAmountList.size() > 0)
                if (grantAmountList.get(0).getGrantAmount() != null
                && grantAmountList.get(0).getGrantAmount().compareTo(BigDecimal.ZERO) != 0)
                {
                    gcList.add(0, new LoanGrantBean());
                    ((LoanGrantBean) gcList.get(0)).setAgencyName(grantAmountList.get(0).getAgencyName());
                    ((LoanGrantBean) gcList.get(0)).setGrantAmount(grantAmountList.get(0).getGrantAmount());
                }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("exiting getDataByAgency ");
        return gcList;
    }

    /**
     * @param agencyId
     * @param subSchemeId
     * @param schemeId
     * @return
     */
    private List<LoanGrantBean> getGrantAmountBy(final Integer schemeId, final Integer subSchemeId, final Long agencyId) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getGrantAmountBy for" + agencyId);
        final StringBuffer gaSql = new StringBuffer();
        gaSql.append("select fa.name as agencyName, sum( case when lgd.grantamount = null THEN 0 else lgd.grantamount end)*100000 as grantAmount");
        gaSql.append(" from egf_loangrantheader lg, egf_loangrantdetail lgd, egf_fundingagency fa");
        if (subSchemeId == null)
            gaSql.append(", sub_scheme ss,scheme s");
        gaSql.append(" where lg.id= lgd.headerid and lgd.agencyid=fa.id ");
        if (subSchemeId == null)
            gaSql.append(" and lg.subschemeid=ss.id and ss.schemeid=s.id and s.id=" + schemeId);
        else
            gaSql.append(" and lg.subschemeid=" + subSchemeId);
        gaSql.append(" and lgd.agencyid=" + agencyId);
        gaSql.append(" group by fa.name");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("GrantAmoountSql for Schemeid" + schemeId + " SubSchemeId " + subSchemeId + "  agencyId" + agencyId + ":"
                    + gaSql.toString());
        final SQLQuery gaSQLQuery = getSession().createSQLQuery(gaSql.toString());
        gaSQLQuery.addScalar("agencyName").addScalar("grantAmount", BigDecimalType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        final List<LoanGrantBean> galist = gaSQLQuery.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from  getGrantAmountBy for" + agencyId);
        return galist;

    }

    /**
     * @param schemeId
     * @param agencyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object> getLoanBy(final Integer schemeId, final Long agencyId, final Integer faTypeId, final Integer fundId) {
        List<Object> loanByAgencyList = null;
        if (agencyId != null && agencyId != -1)
            loanByAgencyList = getLoanByAgency(schemeId, agencyId, faTypeId, fundId);
        else
        {
            final StringBuffer ss = new StringBuffer(256);
            ss.append("select distinct lgd.fundingAgency.id from LoanGrantDetail lgd ,LoanGrantHeader lg,SubScheme ss," +
                    "Scheme s where lg.subScheme.id=ss.id and s.id=ss.scheme.id and lg.id=lgd.header.id and s.id=?");
            final List<Long> agencyList = findAllBy(ss.toString(), schemeId);
            loanByAgencyList = new ArrayList<Object>();
            for (final Long id : agencyList)
                loanByAgencyList.addAll(getLoanByAgency(schemeId, id, faTypeId, fundId));
        }

        return loanByAgencyList;
    }

    public List<Object> getLoanByAgency(final Integer schemeId, final Long agencyId, final Integer faTypeId, final Integer fundId) {
        final StringBuffer sql = new StringBuffer(512);
        sql.append("SELECT DISTINCT vh.vouchernumber AS voucherNumber,  gld.amount    AS amount,  " +
                " gld.detailkeyid               AS detailKey,   gld.detailtypeid detailType ,vh.voucherdate  " +
                " FROM voucherheader vh,   vouchermis vmis,    generalledger gl,   generalledgerdetail gld  " +
                " WHERE vh.id            = gl.voucherheaderid  " +
                " AND gl.id             = gld.generalledgerid  " +
                " AND gl.debitamount    >0  " +
                " AND gld.detailtypeid  =  " + faTypeId +
                " AND vh.type            ='Payment'  " +
                " AND vh.name            ='Direct Bank Payment'  " +
                " and vh.status in (0,5)   " +
                " and vmis.schemeid=  " + schemeId +
                " and vh.fundid=" + fundId +
                " and vmis.voucherheaderid=vh.id");
        if (agencyId != null && agencyId != -1)
            sql.append(" and  gld.detailkeyid =" + agencyId);
        sql.append(" order by vh.voucherdate ");
        final SQLQuery loanSql = getSession().createSQLQuery(sql.toString());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getLoanByAgency sql:" + sql.toString());
        loanSql.addScalar("voucherNumber")
        .addScalar("amount", BigDecimalType.INSTANCE)
        .addScalar("detailKey", IntegerType.INSTANCE)
        .addScalar("detailType", IntegerType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        final List<Object> repayedList = loanSql.list();

        final List<LoanGrantBean> loanAmountList = getLoanAmountBy(schemeId, agencyId);
        if (loanAmountList != null && loanAmountList.size() > 0)
            if (loanAmountList.get(0).getLoanAmount() != null
            && loanAmountList.get(0).getLoanAmount().compareTo(BigDecimal.ZERO) != 0)
            {
                repayedList.add(0, new LoanGrantBean());
                ((LoanGrantBean) repayedList.get(0)).setAgencyName(loanAmountList.get(0).getAgencyName());
                ((LoanGrantBean) repayedList.get(0)).setLoanAmount(loanAmountList.get(0).getLoanAmount());
                final BigDecimal loanPaidSoFar = getLoanPaidSoFar(schemeId, agencyId);
                ((LoanGrantBean) repayedList.get(0)).setAgencyAmount(loanPaidSoFar);
                ((LoanGrantBean) repayedList.get(0)).setBalance(loanAmountList.get(0).getLoanAmount().subtract(loanPaidSoFar));
            }
        return repayedList;
    }

    /**
     * @param schemeId
     * @param agencyId
     * @return
     */
    private BigDecimal getLoanPaidSoFar(final Integer schemeId, final Long agencyId) {
        BigDecimal amount = BigDecimal.ZERO;
        final SQLQuery query = getSession().createSQLQuery(
                "select amount as amount from egf_loan_paid where schemeid=" +
                        schemeId + " and agencyid=" + agencyId);
        query.addScalar("amount", BigDecimalType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        final List<LoanGrantBean> list = query.list();
        if (list != null && list.size() > 0)
            if (list.get(0).getAmount() != null)
                amount = list.get(0).getAmount();
        return amount;
    }

    /**
     * @param schemeId
     * @param agencyId is mandatory
     * @return
     */
    private List<LoanGrantBean> getLoanAmountBy(final Integer schemeId, final Long agencyId) {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Starting getLoanAmountBy for" + agencyId);
        final StringBuffer loanSql = new StringBuffer(256);
        loanSql.append("select fa.name as agencyName, sum( case when lgd.loanamount  = null then 0 else lgd.loanamount)*100000 as loanAmount");
        loanSql.append(" from egf_loangrantheader lg, egf_loangrantdetail lgd, egf_fundingagency fa,sub_scheme ss,scheme s");
        loanSql.append(" where lg.id= lgd.headerid and lgd.agencyid=fa.id ");
        loanSql.append(" and lgd.agencyid=" + agencyId);
        loanSql.append(" and lg.subSchemeId=ss.id");
        loanSql.append(" and s.id=" + schemeId);
        loanSql.append(" and s.id=ss.schemeid ");
        loanSql.append(" group by fa.name");
        if (LOGGER.isInfoEnabled())
            LOGGER.info("GrantAmoountSql for Schemeid" + schemeId + "  agencyId" + agencyId + ":" + loanSql.toString());
        final SQLQuery gaSQLQuery = getSession().createSQLQuery(loanSql.toString());
        gaSQLQuery.addScalar("agencyName")
        .addScalar("loanAmount", BigDecimalType.INSTANCE)
        .setResultTransformer(Transformers.aliasToBean(LoanGrantBean.class));
        final List<LoanGrantBean> galist = gaSQLQuery.list();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exiting from  getLoanAmountBy for" + agencyId);
        return galist;

    }
}