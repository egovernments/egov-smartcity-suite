/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jan 7, 2005
 *
 */
package com.exilant.eGov.src.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFiscalPeriod;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.persistence.utils.DBSequenceGenerator;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.LabelValueBean;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.VoucherMIS;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 *
 * This class contains the common methods used for E-Governments applciation
 */
@Transactional(readOnly = true)
@Service
public class EGovernCommon extends AbstractTask {
    private static String vouNumber;
    private static String vouNumberCess;
    private static String revNumber;
    private static SimpleDateFormat sdfFormatddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private static final Logger LOGGER = Logger.getLogger(EGovernCommon.class);
    private static TaskFailedException taskExc;
    private static final String FUNDIDNSQL = "SELECT identifier as \"fund_identi\" from fund where id=?";
    private static final String EXILRPERROR = "exilRPError";
    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private DBSequenceGenerator dbSequenceGenerator;
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;

    @Override
    public void execute(final String taskName,
            final String gridName,
            final DataCollection datacol,
            final Connection con,
            final boolean errorData,
            final boolean gridHasCol, final String prefix) throws TaskFailedException
    {
        final EGovernCommon egobj = new EGovernCommon();
        datacol.addValue("voucherHeader_cgn", egobj.getCGNumber());
        if (datacol.getValue("hasSecondCGN").equalsIgnoreCase("true"))
            datacol.addValue("jv_cgn", egobj.getCGNumber());
        datacol.addValue("databaseDate", egobj.getCurrentDate());
    }

    public long getCGNumber()
    {
        return PrimaryKeyGenerator.getNextKey("voucherheader");
    }

    /**
     * This function returns the system date of the database server.
     * @param connection
     * @return
     * @throws TaskFailedException
     */
    public String getCurrentDate() throws TaskFailedException
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /**
     *
     * @param datacol
     * @param connection
     * @return
     */
    public boolean isValidData(final DataCollection datacol) throws TaskFailedException
    {
        final String[] fieldsToCheck = { "department_id", "organizationStructure_id", "taxes_code", "billCollector_id",
                "bankAccount_accountNumber", "bank_id", "bankBranch_id", "fund_id", "supplier_id", "contractor_id" };
        final List formList = getFormFields(datacol);
        String formControlName;
        for (int i = 0; i < formList.size(); i++) {
            formControlName = (String) formList.get(i);
            for (int j = 0; j < fieldsToCheck.length; j++)
                if (fieldsToCheck[j].equalsIgnoreCase(formControlName))
                    if (!isDataInDataBase(fieldsToCheck[j], datacol.getValue(fieldsToCheck[j])))
                        return false;
        }
        return true;
    }

    /**
     *
     * @param datacol
     * @return
     */
    public List getFormFields(final DataCollection datacol)
    {
        final Set formSet = datacol.values.keySet();
        final List formList = new ArrayList();
        final Iterator itr = formSet.iterator();
        while (itr.hasNext())
            formList.add(itr.next());
        return formList;
    }

    /**
     *
     * @param field
     * @param data
     * @param connection
     * @return
     */
    public boolean isDataInDataBase(final String fieldData,
            final String dbval
            ) throws TaskFailedException
    {
        String branchData;
        String dbval1 = "";
        List<Object[]> rset = null;
        Query pst = null;
        if (fieldData.equalsIgnoreCase("bank_id")) {
            branchData = dbval.substring(dbval.indexOf('-'), dbval.length());
            dbval1 = dbval.substring(0, dbval.indexOf('-'));
            if (!isDataInDataBase("bankBranch_id", branchData))
                return false;
        }
        try {
            final String table = fieldData.substring(0, fieldData.indexOf('_'));
            final String fieldData1 = fieldData.replaceAll("_", ".");
            final String query = "select * from " + table + " where " + fieldData1 + "=" + dbval1;
            // if(LOGGER.isInfoEnabled()) LOGGER.info(query);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            rset = pst.list();
            if (rset == null || rset.size() == 0)
                return false;
            else if (LOGGER.isDebugEnabled())
                LOGGER.debug("Inside the else block");

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
        return true;
    }

    public boolean noMatch(final String tableName,
            final String condition
            ) throws TaskFailedException {
        boolean noMatch = true;
        List<Object[]> resultset = null;
        Query pst = null;
        try {
            final String executeString = "select * from ? where 1=1 ?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(executeString);
            pst.setString(0, tableName);
            pst.setString(1, condition);
            resultset = pst.list();
            if (resultset == null || resultset.size() == 0)
                noMatch = true;
            else
                noMatch = false;

        } catch (final Exception sqlex) {
            LOGGER.error(sqlex.getMessage(), sqlex);
            throw taskExc;
        }

        return noMatch;
    }

    /**
     * This function is to handle the single quotes.
     * @param strToFormat
     * @return
     */
    public String formatString(final String strToFormat) {
        if (strToFormat != null)
        {
            if (strToFormat.equalsIgnoreCase(""))
                return " ";
            final String valn1 = strToFormat.replaceAll("\n", " ");
            final String formtStr = valn1.replaceAll("\r", " ");
            return formtStr.replaceAll("'", "''");
        } else
            return " ";
    }

    /**
     * This function will return the database date and time
     * @param connection
     * @return
     * @throws TaskFailedException
     */
    public void validateScheme(final String vdt, final String sid) throws TaskFailedException
    {
        Query pst = null;
        List<Object[]> resultset = null;
        try {
            final SimpleDateFormat sdf = sdfFormatddMMyyyy;
            final SimpleDateFormat formatter = dtFormat;// new SimpleDateFormat("dd-MMM-yyyy");
            final String vDate = formatter.format(sdf.parse(vdt));
            final String qry = "select code,name from scheme where isactive=true and  validFrom<=? and validTo>=? and id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("validating scheme" + qry);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(qry);
            pst.setString(0, vDate);
            pst.setString(1, vDate);
            pst.setString(2, sid);
            resultset = pst.list();
            for (final Object[] element : resultset)
                return;
            if (resultset == null || resultset.size() == 0)
                throw new TaskFailedException("Scheme is not valid for the date " + vDate);
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            throw new TaskFailedException(e.getMessage());
        }

    }

    public void validatesubScheme(final String vdt, final String ssid) throws TaskFailedException
    {
        Query pst = null;
        List<Object[]> resultset = null;
        try {
            final SimpleDateFormat sdf = sdfFormatddMMyyyy;
            final SimpleDateFormat formatter = dtFormat;
            final String vDate = formatter.format(sdf.parse(vdt));
            final String qry = "select code,name from sub_scheme where validFrom<=? and validTo>=? and id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("validating subscheme" + qry);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(qry);
            pst.setString(0, vDate);
            pst.setString(1, vDate);
            pst.setString(2, ssid);
            resultset = pst.list();
            for (final Object[] element : resultset)
                return;
            if (resultset == null || resultset.size() == 0)
                throw new TaskFailedException("the subscheme is not valid for the date " + vDate);
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }

    }

    public String getCurrentDateTime() throws TaskFailedException
    {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * This is to get the balance of bank account. Not used now.
     * @param con
     * @param trnAmount
     * @param accId
     * @return
     * @throws TaskFailedException
     */
    /*
     * public boolean bankBalanceAvaliable(Connection con,double trnAmount, String accId)throws TaskFailedException { Statement
     * statment1=null; ResultSet rset=null; if(accId==null || accId.length()<=0 && accId.equals("") || trnAmount==0){ return true;
     * } String sql="select currentbalance from bankaccount where id="+accId; try{ statment1=con.createStatement();
     * rset=statment1.list(sql); String balance=null; while(rset.next()){ balance=rset.getString(1); }
     * if(Double.parseDouble(balance)<trnAmount){ return false; } }catch(Exception e){ throw taskExc; } finally{ try{
     * rset.close(); statment1.close(); }catch(Exception e){ LOGGER.error("in getCurrentDateTime..."); } } return true; }
     */
    /**
     * This function will return the vouchernumber. Fund identifier+ voucher type +user entered number
     * @param datacol
     * @param conn
     * @param fund
     * @param voucherNum
     * @return
     * @throws TaskFailedException,Exception
     */
    public String vNumber(final DataCollection datacol, final String fund, final String voucherNum) throws TaskFailedException,
            Exception
    {
        String fType = "";
        Query pst = null;
        List<Object[]> rset = null;
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.parseInt(fund));
            rset = pst.list();
            for (final Object[] element : rset)
                fType = element[0].toString();
            final String tran = datacol.getValue("tType");
            vouNumber = fType + tran + voucherNum;
        } catch (final Exception e) {
            throw taskExc;
        }
        return vouNumber;
    }

    /**
     * To generate Voucher number based on recordid [ uesd for Integeration ]
     * @param type
     * @param conn
     * @param fund
     * @param recordId
     * @return
     * @throws TaskFailedException,Exception
     */
    public String vNumber(final String type, final String fund, final int recordId) throws TaskFailedException, Exception
    {
        String fType = "";
        Query pstmt = null;
        List<Object[]> rset = null;
        try {
            // String query = FUNDIDNSQL+fund;
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pstmt.setInteger(0, Integer.valueOf(fund));
            rset = pstmt.list();

            for (final Object[] element : rset)
                fType = element[0].toString();
            final String tran = type;
            String rcdId = String.valueOf(recordId);
            for (int i = rcdId.length(); i < 5; i++)
                rcdId = "0" + rcdId;
            vouNumber = fType + tran + rcdId;
        } catch (final Exception e) {
            throw taskExc;
        }

        return vouNumber;
    }

    /**
     * This function will return the vouchernumber. Fund identifier+ voucher type +user entered number
     * @param fund
     * @param voucherNum
     * @return
     * @throws TaskFailedException,Exception
     */
    public String vNumber(final String fund, final String voucherNum) throws TaskFailedException, Exception
    {
        String vouNumber = "";
        String fType = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            // String query = FUNDIDNSQL+"?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fund));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();
            vouNumber = fType + "P" + voucherNum;
        } catch (final Exception e) {
            throw taskExc;
        }

        return vouNumber;
    }

    /**
     * This function will return the vouchernumber. Fund identifier+ voucher type +user entered number
     * @param type
     * @param fund
     * @param voucherNum
     * @return
     * @throws TaskFailedException,Exception
     */
    public String vNumber(final String type, final String fund, final String vhNum) throws TaskFailedException, Exception
    {
        String vouNumber = "";
        String fType = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            // String query = FUNDIDNSQL+fund;
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fund));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();
            final String tran = type;
            vouNumber = fType + tran + vhNum;
        } catch (final Exception e) {
            throw taskExc;
        }
        return vouNumber;
    }

    /**
     * To generate Voucher number based on type(P for payment, R for receipt and others), fundid and max(vouchernumber)+1
     * @param type
     * @param conn
     * @param fund
     * @return
     * @throws TaskFailedException,Exception
     */
    public String maxVoucherNumber(final String type, final String fund) throws TaskFailedException, Exception
    {
        String fType = "";
        String vNum = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fund));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();

            final String vType = fType + type;
            final String query = " select max(to_number(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'),'FM9999999999'))+1 "
                    + " as \"vounumber\" from voucherheader where  vouchernumber like '"
                    + vType
                    + "%' and "
                    + " length(translate(vouchernumber,'1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ;/-_()','1234567890'))<=10 ";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query  " + query);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            rs = pst.list();
            for (final Object[] element : rs)
                vNum = element[0].toString();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Voucher Number from the query is :" + vNum);
            if (StringUtils.isNotBlank(vNum))
                for (int i = vNum.length(); i < 5; i++)
                    vNum = "0" + vNum;
            else
                vNum = "00001";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Voucher Number after the zero addition is :" + vNum);
            vouNumber = vType + vNum;
        } catch (final Exception e) {
            throw taskExc;
        }

        return vouNumber;
    }

    /**
     * This function will generate the voucher number for the cess JV
     * @param datacol
     * @param conn
     * @param fund
     * @return
     * @throws TaskFailedException,Exception
     */
    public String vNumberCess(final DataCollection datacol, final String fund) throws TaskFailedException, Exception
    {
        String fType = "";
        Query pst = null;
        List<Object[]> rs = null;
        pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
        pst.setInteger(0, Integer.valueOf(fund));
        rs = pst.list();
        for (final Object[] element : rs)
            fType = element[0].toString();
        final String tran = datacol.getValue("cvType");
        vouNumberCess = fType + tran + datacol.getValue("jv_voucherNumber");
        return vouNumberCess;
    }

    /**
     * Common method for getting the cgvn
     * @param datacol
     * @param conn
     * @param fund
     * @return
     * @throws TaskFailedException,Exception
     */
    public String getCgvnNo(final String fund, final String vnum, final String vType) throws TaskFailedException, Exception
    {
        String fType = "", cgvnNumber = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fund));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();
            cgvnNumber = fType + vType + vnum;
        } catch (final Exception e) {
            throw taskExc;
        }
        return cgvnNumber;
    }

    /**
     * This function will generate the reverse voucher number
     * @param datacol
     * @param conn
     * @param fund
     * @return
     * @throws TaskFailedException,Exception
     */
    public String rvNumber(final DataCollection datacol, final String fund) throws TaskFailedException, Exception
    {
        String fType = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fund));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();
            final String tran = datacol.getValue("tType");
            revNumber = fType + tran + datacol.getValue("voucherHeader_newVcNo");
        } catch (final Exception e) {
            throw taskExc;
        }
        return revNumber;
    }

    /**
     * Function to check if the voucher number is Unique
     * @param datacol
     * @param conn
     * @param vouNum
     * @return
     */
    public boolean isUniqueVN(final DataCollection datacol, final String vouNum) throws TaskFailedException, Exception,
            ParseException {
        final String vcNum = vouNum;
        String vcDate = datacol.getValue("voucherHeader_voucherDate");
        final SimpleDateFormat sdf = sdfFormatddMMyyyy;
        final SimpleDateFormat formatter = dtFormat;
        vcDate = formatter.format(sdf.parse(vcDate));

        return isUniqueVN(vcNum, vcDate, datacol);
    }

    /**
     * Function to check if the voucher number is Unique
     * @param datacol
     * @param conn
     * @param vcNoField
     * @param vcDateField
     * @return
     * @throws TaskFailedException,Exception
     */
    public boolean isUniqueVN(final DataCollection datacol, final String vcNoField, final String vcDateField)
            throws TaskFailedException,
            Exception, ParseException {
        final String vcNum = vcNoField;
        String vcDate = datacol.getValue(vcDateField);
        final SimpleDateFormat sdf = sdfFormatddMMyyyy;
        final SimpleDateFormat formatter = dtFormat;
        vcDate = formatter.format(sdf.parse(vcDate));

        return isUniqueVN(vcNum, vcDate, datacol);
    }

    /**
     * Function to check if the voucher number is Unique
     * @param vcNum
     * @param vcDate
     * @param datacol
     * @param conn
     * @return
     */
    public boolean isUniqueVN(String vcNum, final String vcDate, final DataCollection datacol) throws TaskFailedException,
            Exception {
        boolean isUnique = false;
        vcNum = vcNum.toUpperCase();
        Query pst = null;
        List<Object[]> rs = null;
        String fyEndDate = "";
        try {
            final String query1 = "SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"
                    + vcDate + "' AND endingDate >= '" + vcDate + "'";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query1);
            rs = pst.list();
            for (final Object[] element : rs) {
                element[0].toString();
                fyEndDate = element[1].toString();
            }
            final String query2 = "SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='"
                    + vcDate
                    + "' AND voucherDate<='" + fyEndDate + "' and status!=4";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query2);
            rs = pst.list();
            for (final Object[] element : rs)
                datacol.addMessage(EXILRPERROR, "duplicate voucher number");
            if (rs == null || rs.size() == 0)
                isUnique = true;
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueVN) : " + ex.toString());
            throw new TaskFailedException();
        }
        return isUnique;
    }

    /**
     *
     * @param vcNum
     * @param vcDate
     * @param conn
     * @return
     * @throws TaskFailedException,Exception
     */
    public boolean isUniqueVN(String vcNum, final String vcDate) throws Exception, TaskFailedException {
        boolean isUnique = false;
        String fyStartDate = "", fyEndDate = "";
        vcNum = vcNum.toUpperCase();
        Query pst = null;
        List<Object[]> rs = null;
        try {
            final String query1 = "SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"
                    + vcDate + "' AND endingDate >= '" + vcDate + "'";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query1);
            rs = pst.list();
            if (rs != null && rs.size() > 0)
                for (final Object[] element : rs) {
                    fyStartDate = element[0].toString();
                    fyEndDate = element[1].toString();
                }
            final String query2 = "SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='"
                    + fyStartDate
                    + "' AND voucherDate<='" + fyEndDate + "' and status!=4";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query2);
            rs = pst.list();
            if (rs != null && rs.size() > 0) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Duplicate Voucher Number");
            } else
                isUnique = true;
        } catch (final Exception ex) {
            LOGGER.error("error in finding unique VoucherNumber");
            throw taskExc;
        } finally {
            try {
            } catch (final Exception e) {
                LOGGER.error("isUniqueVN....");
            }
        }
        return isUnique;
    }

    /**
     * This function will check if the cheque no is unique
     */
    public boolean isUniqueChequeNo(final String Chequeno, final String BankAccId, final DataCollection datacol)
            throws TaskFailedException,
            Exception {
        boolean isUnique = true;
        Query pst = null;
        List<Object[]> rs = null;
        try {
            final String sql = " SELECT cd.id FROM chequedetail cd,voucherheader vh WHERE cd.accountnumberid = ? AND cd.chequenumber=? AND cd.ispaycheque=1 AND cd.chequetype='C' AND vh.id=cd.VOUCHERHEADERID AND vh.status<>4 ";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            pst.setString(0, BankAccId);
            pst.setString(1, Chequeno);
            rs = pst.list();
            for (final Object[] element : rs) {
                datacol.addMessage(EXILRPERROR, "duplicate Cheque number");
                isUnique = false;
            }
            if (rs == null || rs.size() == 0)
                isUnique = true;
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueChequeNo) : " + ex.toString());
            throw taskExc;
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("isUnique:" + isUnique);
        return isUnique;
    }

    /**
     * This function will check if the cheque no is unique without DataCollection
     * @param Chequeno
     * @param BankAccId
     * @param conn
     * @return
     */
    public boolean isUniqueChequeNo(final String Chequeno, final String BankAccId) throws TaskFailedException, Exception
    {
        boolean isUnique = false;
        Query pst = null;
        List<Object[]> rs = null;
        try
        {
            final String sql = " SELECT cd.id FROM chequedetail cd,voucherheader vh WHERE cd.accountnumberid = ? AND cd.chequenumber=? AND cd.ispaycheque=1 AND cd.chequetype='C' AND vh.id=cd.VOUCHERHEADERID AND vh.status<>4 "
                    +
                    "  UNION SELECT egsc.id FROM EG_SURRENDERED_CHEQUES egsc,voucherheader vh WHERE egsc.BANKACCOUNTID = ? AND egsc.chequenumber=? AND vh.id=egsc.vhid AND vh.status<>4 ";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            pst.setString(0, BankAccId);
            pst.setString(1, Chequeno);
            pst.setString(2, BankAccId);
            pst.setString(3, Chequeno);
            rs = pst.list();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Query:::" + sql);
            for (final Object[] element : rs)
                throw new ApplicationException("Error: duplicate Cheque number");
            if (rs == null || rs.size() == 0)
                isUnique = true;
        } catch (final Exception e)
        {
            throw taskExc;
        }
        return isUnique;
    }

    /**
     * Checking for Cheque number is withing the Range
     * @param Chequeno
     * @param BankAccId
     * @param datacol
     * @param conn
     * @return
     */
    public boolean isChqNoWithinRange(final String Chequeno, final String BankAccId, final DataCollection datacol)
            throws TaskFailedException,
            Exception {
        boolean isWithinRange = false;
        Query pst = null;
        List<Object[]> rs = null;
        try {
            final String query = "SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = ? AND fromchequenumber<=TO_NUMBER(?,'999999') and TO_NUMBER(?,'999999')<= tochequenumber";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, BankAccId);
            pst.setString(1, Chequeno);
            pst.setString(2, Chequeno);
            rs = pst.list();
            for (final Object[] element : rs)
                isWithinRange = true;
            if (rs == null || rs.size() == 0)
                datacol.addMessage(EXILRPERROR, "Invalid Cheque number");
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isWithinRange) : " + ex.toString());
            throw taskExc;
        }
        return isWithinRange;
    }

    /**
     * Checking for Cheque number is withing the Range without DataCollection
     * @param Chequeno
     * @param BankAccId
     * @param conn
     * @return
     */
    public boolean isChqNoWithinRange(final String Chequeno, final String BankAccId) throws TaskFailedException, Exception
    {
        boolean isWithinRange = false;
        Query pst = null;
        List<Object[]> rs = null;
        try {
            final String query = "SELECT id FROM EGF_ACCOUNT_CHEQUES WHERE BANKACCOUNTID = ? AND fromchequenumber<=TO_NUMBER(?,'999999') and TO_NUMBER(?,'999999')<= tochequenumber";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, BankAccId);
            pst.setString(1, Chequeno);
            pst.setString(2, Chequeno);
            rs = pst.list();
            for (final Object[] element : rs)
                isWithinRange = true;
            if (rs == null || rs.size() == 0)
                throw new TaskFailedException("Error: Invalid Cheque number");
        } catch (final Exception e)
        {
            LOGGER.error("Exception in isChqNoWithinRange" + e.getMessage());
            throw taskExc;
        }

        return isWithinRange;
    }

    /**
     * This function will check if the date enetered is within the todays' date
     * @param datacol
     * @param conn
     * @param VDate
     * @return
     */
    public int isCurDate(final DataCollection datacol, final String VDate) throws TaskFailedException, Exception {
        int isCurDate = 0;
        try {
            final String today = getCurrentDate();
            final String[] dt2 = today.split("/");
            final String[] dt1 = VDate.split("/");
            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1 : Integer.parseInt(dt2[2]) < Integer
                    .parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer.parseInt(dt1[1]) ? 1 : Integer
                    .parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1
                    : Integer.parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                            .parseInt(dt1[0]) ? -1 : 0;
            if (ret == -1)
                datacol.addMessage(EXILRPERROR, "Date Should be within the today date");
            else
                isCurDate = 1;

        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(iscurDate) : " + ex.toString());
            throw taskExc;
        }
        return isCurDate;
    }

    /**
     * This function will check if the Reverse date is prior or on the same date as that of the original voucher date
     * @param datacol
     * @param conn
     * @param VDate
     * @param RVDate
     * @return
     */
    public int isValidDate(final DataCollection datacol, final String VDate, final String RVDate) throws TaskFailedException,
            Exception {
        int isCurDate = 0;
        try {
            final String[] dt1 = VDate.split("/");
            final String[] dt2 = RVDate.split("/");
            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1 : Integer.parseInt(dt2[2]) < Integer
                    .parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer.parseInt(dt1[1]) ? 1 : Integer
                    .parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1
                    : Integer.parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                            .parseInt(dt1[0]) ? -1 : 0;
            if (ret == -1)
                datacol.addMessage(EXILRPERROR, "Date Should be more than original voucher date");
            else
                isCurDate = 1;
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isValidDate) : " + ex.toString());
            throw taskExc;
        }
        return isCurDate;
    }

    /**
     * This function will check if the Reverse date is prior or on the same date as that of the original voucher date
     * @param conn
     * @param VDate
     * @param RVDate
     * @return
     */
    public int isValidDate(final String VDate, final String RVDate) throws TaskFailedException, Exception
    {
        int isCurDate = 0;
        try
        {
            final String[] dt1 = VDate.split("/");
            final String[] dt2 = RVDate.split("/");
            final int ret = Integer.parseInt(dt2[2]) > Integer.parseInt(dt1[2]) ? 1 : Integer.parseInt(dt2[2]) < Integer
                    .parseInt(dt1[2]) ? -1 : Integer.parseInt(dt2[1]) > Integer.parseInt(dt1[1]) ? 1 : Integer
                    .parseInt(dt2[1]) < Integer.parseInt(dt1[1]) ? -1
                    : Integer.parseInt(dt2[0]) > Integer.parseInt(dt1[0]) ? 1 : Integer.parseInt(dt2[0]) < Integer
                            .parseInt(dt1[0]) ? -1 : 0;
            if (ret == -1)
                throw new TaskFailedException("Date Should be more than original voucher date");
            isCurDate = 1;
        } catch (final Exception ex)
        {
            LOGGER.error("Inside the isValidDate.. " + ex.getMessage());
            throw taskExc;
        }
        return isCurDate;
    }

    /**
     *
     * @param vouType Eg - U/DBP/CGVN
     * @param fiscialPeriod
     * @param conn
     * @return
     * @throws TaskFailedException,Exception
     */
    public String getEg_Voucher(final String vouType, final String fiscalPeriodIdStr) throws TaskFailedException, Exception
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" In EGovernCommon :getEg_Voucher method ");
        final CFiscalPeriod fiscalPeriod = (CFiscalPeriod) persistenceService.find("from CFiscalPeriod where id=?",
                Long.parseLong(fiscalPeriodIdStr));
        BigInteger cgvn = null;
        String sequenceName = "";
        // Sequence name will be SQ_U_DBP_CGVN_FP7 for vouType U/DBP/CGVN and fiscalPeriodIdStr 7
        try {
            sequenceName   = VoucherHelper.sequenceNameFor(vouType, fiscalPeriod.getName());
            cgvn = (BigInteger)sequenceNumberGenerator.getNextSequence(sequenceName);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("----- CGVN : " + cgvn);

        } catch (final SQLGrammarException e)
        {
            cgvn = (BigInteger)dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            LOGGER.error("Error in generating CGVN" + e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final Exception e)
        {
            LOGGER.error("Error in generating CGVN" + e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return cgvn.toString();

    }

    /**
     * This function will check the balance amount in a particular Bank Account(bankAccountId)
     * @param datacol
     * @param con
     * @param bankAccountId
     * @return
     * @throws TaskFailedException First we need get the opening balance from transaction summary and then the
     * sum(debitamount)-sum(creditamount) from the general ledger.
     */
    public BigDecimal getAccountBalance(final DataCollection datacol, final String bankAccountId) throws TaskFailedException
    {

        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        Query pst = null;
        List<Object[]> resultset = null;
        List<Object[]> resultset1 = null;
        try {
            String vcDate = datacol.getValue("voucherHeader_voucherDate");
            final SimpleDateFormat sdf = sdfFormatddMMyyyy;
            final SimpleDateFormat formatter = dtFormat;
            vcDate = formatter.format(sdf.parse(vcDate));

            final String str = "SELECT case when sum(openingDebitBalance)=null then 0 else sum(openingDebitBalance) end - case when sum(openingCreditBalance)= null then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                    +
                    "FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=?" +
                    "AND endingDate >=?)  AND glCodeId =(select glcodeid from bankaccount where id=?)";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str);
            pst.setString(0, vcDate);
            pst.setString(1, vcDate);
            pst.setString(2, bankAccountId);
            resultset = pst.list();
            for (final Object[] element : resultset)
                opeAvailable = new BigDecimal(element[0].toString());
            if (resultset == null || resultset.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);

            final String str1 = "SELECT ((case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end ) - (case when sum(gl.creditAmount) = null  then 0 else sum(gl.creditAmount) end)) + "
                    + opeAvailable
                    +
                    " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id= ?) AND  "
                    +
                    " vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str1);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str1);
            pst.setString(0, bankAccountId);
            pst.setString(1, vcDate);
            pst.setString(2, vcDate);
            pst.setString(3, vcDate);
            resultset1 = pst.list();
            for (final Object[] element : resultset1) {
                totalAvailable = new BigDecimal(element[0].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("total balance  " + totalAvailable);
            }
            if (resultset1 == null || resultset1.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance...");
            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);
            return totalAvailable;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
    }

    public BigDecimal getAccountBalance(final Date VoucherDate, final String bankAccountId) throws TaskFailedException
    {
        BigDecimal totalAvailable = BigDecimal.ZERO;
        BigDecimal opeAvailable = BigDecimal.ZERO;
        Query pst = null;
        List<Object[]> resultset = null;
        List<Object[]> resultset1 = null;
        try {
            final SimpleDateFormat formatter = dtFormat;
            final String vcDate = formatter.format(VoucherDate);

            final String str = "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end-  case when sum(openingCreditBalance) = null  then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                    +
                    "FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=?" +
                    "AND endingDate >= ?)  AND glCodeId =(select glcodeid from bankaccount where id=?)";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str);
            pst.setString(0, vcDate);
            pst.setString(1, vcDate);
            pst.setString(2, bankAccountId);
            resultset = pst.list();
            for (final Object[] element : resultset)
                opeAvailable = new BigDecimal(element[0].toString());
            if (resultset == null || resultset.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);
            // resultset.close();

            final String str1 = "SELECT (case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end) - (case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end) + "
                    + opeAvailable
                    +
                    " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id=?) AND  "
                    +
                    " vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str1);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str1);
            pst.setString(0, bankAccountId);
            pst.setString(1, vcDate);
            pst.setString(2, vcDate);
            pst.setString(3, vcDate);
            resultset1 = pst.list();
            for (final Object[] element : resultset1) {
                totalAvailable = new BigDecimal(element[0].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("total balance  " + totalAvailable);
            }
            if (resultset1 == null || resultset1.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance...");

            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
        return totalAvailable;
    }

    /*
     * overloaded method with different argument type(without datacol)
     */
    public BigDecimal getAccountBalance(final String recDate, final String bankAccountId) throws TaskFailedException
    {

        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        Query pst = null;
        List<Object[]> resultset = null;
        List<Object[]> resultset1 = null;
        try {
            final String str = "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end - case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                    +
                    "FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=?" +
                    "AND endingDate >=?)  AND glCodeId =(select glcodeid from bankaccount where id=?)";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str);
            pst.setString(0, recDate);
            pst.setString(1, recDate);
            pst.setString(2, bankAccountId);
            resultset = pst.list();
            for (final Object[] element : resultset)
                opeAvailable = new BigDecimal(element[0].toString());
            if (resultset == null || resultset.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getAccountBalance...");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);

            final String str1 = "SELECT (case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  = null then 0 else sum(gl.creditAmount) end ) + "
                    + opeAvailable
                    +
                    " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId and gl.glCodeid = (select glcodeid from bankaccount where id=?) AND  "
                    +
                    " vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str1);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str1);
            pst.setString(0, bankAccountId);
            pst.setString(1, recDate);
            pst.setString(2, recDate);
            pst.setString(3, recDate);
            resultset1 = pst.list();
            for (final Object[] element : resultset1) {
                totalAvailable = new BigDecimal(element[0].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("total balance  " + totalAvailable);
            }
            if (resultset1 == null || resultset1.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getAccountBalance...");

            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);
            return totalAvailable;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
    }

    /**
     * This function will check the cash balance amount
     * @param datacol
     * @param con
     * @param cashIdValue
     * @return
     * @throws TaskFailedException First we need get the opening balance from transaction summary and then the
     * sum(debitamount)-sum(creditamount) from the general ledger.
     */
    public BigDecimal getCashBalance(final String vcDate, final String cashIdValue, final String fundId)
            throws TaskFailedException
    {

        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        Query pst = null;
        List<Object[]> resultset = null;
        try {
            String vcDateFmt = "";
            final SimpleDateFormat sdf = sdfFormatddMMyyyy;
            final SimpleDateFormat formatter = dtFormat;
            vcDateFmt = formatter.format(sdf.parse(vcDate));

            final String str = "SELECT case when sum(openingDebitBalance) = null  then 0 else sum(openingDebitBalance) end - case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                    +
                    "FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=?" +
                    " AND endingDate >=?)  AND glCodeId =? and fundid=?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(str);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str);
            pst.setString(0, vcDateFmt);
            pst.setString(1, vcDateFmt);
            pst.setString(2, cashIdValue);
            pst.setString(3, fundId);
            resultset = pst.list();
            for (final Object[] element : resultset)
                opeAvailable = new BigDecimal(element[0].toString());
            if (resultset == null || resultset.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getCashAccountBalance...");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);
            final String str1 = "SELECT (case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end  - case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end) ? "
                    +
                    " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid =? AND  "
                    +
                    " vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4 and fundid=?";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(str1);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(str1);
            pst.setBigDecimal(0, opeAvailable);
            pst.setString(1, cashIdValue);
            pst.setString(2, vcDateFmt);
            pst.setString(3, vcDateFmt);
            pst.setString(4, vcDateFmt);
            pst.setString(5, fundId);
            resultset = pst.list();
            for (final Object[] element : resultset) {
                totalAvailable = new BigDecimal(element[0].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("total cash balance  " + totalAvailable);
            }
            if (resultset == null || resultset.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getCashAccountBalance");
            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total cash balance  " + totalAvailable);
            return totalAvailable;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }

    }

    public String getEffectiveDateFilter(final String val) throws TaskFailedException, Exception {
        String returnVal = "";
        if (val == null) {
            returnVal = getCurrentDateTime();
            returnVal = " and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
                    " TO_DATE('" + returnVal + "','dd-Mon-yyyy HH24:MI:SS')";

        } else
            returnVal = " and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
                    " TO_DATE('" + val + "','dd-Mon-yyyy HH24:MI:SS')";
        return returnVal;
    }

    public String getCurDateTime() throws TaskFailedException, Exception {
        return new SimpleDateFormat("dd-Mon-yyyy HH:mm:ss").format(new Date());
    }

    /**
     * Function to get the Fundid
     * @param datacol
     * @param con
     * @return
     * @author Elzan Mathew
     */
    public String getFund(final DataCollection datacol) throws TaskFailedException {
        String retVal = "";
        Query pstmt = null;
        List<Object[]> rs = null;
        try {
            final String query = "select fundid from voucherheader where id= ?";
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pstmt.setString(0, datacol.getValue("voucherHeader_id"));
            rs = pstmt.list();
            for (final Object[] element : rs)
                retVal = element[0].toString();
            if (rs == null || rs.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside if for getFund");
        } catch (final Exception e) {
            LOGGER.error("Exception in getting fund.." + e);
            throw taskExc;
        }
        return retVal;
    }

    /**
     * @author Elzan Mathew This function will be called from all the transactions for inserting into the status table
     */
    // private String id = "0";//Added by sumit for EGF_RECORD_STATUS table
    // public void setId(String aId){ id = aId; }//Added by sumit for EGF_RECORD_STATUS table

    public void UpdateVoucherStatus(final DataCollection datacol, final String type, final int voucherHeaderId)
            throws TaskFailedException {
        try
        {
            final String code = EGovConfig.getProperty("egf_config.xml", "confirmoncreate", "", "JournalVoucher");
            if (code.equalsIgnoreCase("N")) {
            } else {
            }
            final EGovernCommon cm = new EGovernCommon();
            cm.getCurrentDateTime();

            /*
             * egfstatus.setEffectiveDate(formatter.format(sdf.parse( today )));
             * egfstatus.setVoucherheaderId(String.valueOf(voucherHeaderId)); egfstatus.setRecord_Type(type);
             * egfstatus.setStatus(String.valueOf(recStatus)); egfstatus.setUserId(datacol.getValue("current_UserID"));
             * egfstatus.insert();
             */
        } catch (final Exception e) {
            datacol.addMessage(EXILRPERROR, "Voucher Status not inserted");
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
    }

    /**
     * Overloaded UpdateVoucherStatus method without datacol parameter
     */
    /*
     * public void UpdateVoucherStatus(String type,int voucherHeaderId,int userId)throws
     * TaskFailedException,Exception,ParseException { try { egfRecordStatus egfstatus = new egfRecordStatus(); String today; int
     * recStatus =0; String code=EGovConfig.getProperty("egf_config.xml","confirmoncreate","","JournalVoucher");
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("code:"+code); if(code.equalsIgnoreCase("N")){ recStatus=1; } else{ recStatus=0; }
     * SimpleDateFormat sdf =sdfFormatddMMyyyy; SimpleDateFormat formatter = dtFormat; EGovernCommon cm=new EGovernCommon();
     * today=cm.getCurrentDate(); egfstatus.setEffectiveDate(formatter.format(sdf.parse( today )));
     * egfstatus.setVoucherheaderId(String.valueOf(voucherHeaderId)); egfstatus.setRecord_Type(type);
     * egfstatus.setStatus(String.valueOf(recStatus)); egfstatus.setUserId(String.valueOf(userId)); egfstatus.insert(); }
     * catch(Exception e) { LOGGER.error(e.getMessage(), e); throw taskExc; } }
     */

    public String getStartDate(final String finId) throws TaskFailedException
    {
        Query pst = null;
        List<Object[]> resultset = null;
        String startDate = null;
        try
        {
            final String query = "select startingdate as \"startDate\" from financialyear where id = ?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, finId);
            resultset = pst.list();
            for (final Object[] element : resultset)
                startDate = element[0].toString();
        } catch (final Exception sqlex)
        {
            LOGGER.error("EGovernCommon->getStartDate " + sqlex.getMessage());
            throw taskExc;
        }
        return startDate;
    }

    /**
     * @author Lakshmi This function will be called from those transactions that need balance amount from bank during modification
     * of the BankPayment
     * @param datacol
     * @param con
     * @param balAvailable
     * @return accountBalanceCr
     * @throws TaskFailedException Get the accountBalanceCr amount as totalBalance+oldTransactionAmout-newAmount if accountNumber
     * is same, else accountBalanceCr=totalBalance+oldTransactionAmout-newAmount.
     * @Date 08-May-2006
     */
    public BigDecimal getAccountBalanceInModify(final DataCollection datacol, final String bankAccId,
            final String accountBalance, final int voucherId)
            throws TaskFailedException
    {
        Query pst = null;
        List<Object[]> rs = null;

        BigDecimal oldAmt = BigDecimal.ZERO;
        final BigDecimal accBalance = new BigDecimal(accountBalance);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("  accBalance  " + accBalance);
        final BigDecimal balAvailable = getAccountBalance(datacol, bankAccId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("  balAvailable  " + balAvailable);
        BigDecimal accountBalanceCr = balAvailable.subtract(accBalance);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("  accountBalanceCr  " + accountBalanceCr);
        final String query = "select bankaccountid,amount from bankreconciliation where voucherheaderid= ? and transactionType='Cr'";
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setInteger(0, voucherId);
            rs = pst.list();
            for (final Object[] element : rs)
                oldAmt = new BigDecimal(element[1].toString());
            if (rs == null || rs.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Inside the else block for getbalance in modify");
            accountBalanceCr = accountBalanceCr.add(oldAmt);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("accountBalanceCr(if a/c same): " + accountBalanceCr);
        } catch (final Exception e) {
            throw taskExc;
        }

        accountBalanceCr = accountBalanceCr.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Before return accountBalanceCr :" + accountBalanceCr);
        return accountBalanceCr;
    }

    public String getBillNumber() throws TaskFailedException, Exception
    {
        throw new TaskFailedException("Method Not Supported Exception");
    }

    /**
     * This method will insert/update the informations to egf_vouchermis table based on the value of mode.
     * @param conn
     * @param mode
     * @param voucherheaderid
     * @param field
     * @throws TaskFailedException
     */
    public void updateVoucherMIS(final String mode, final int voucherheaderid, final String field, final String fundSourceId,
            final String schemeid,
            final String subschemeid, final String vdt) throws TaskFailedException
    {
        if (schemeid != null && schemeid.length() > 0)
            validateScheme(vdt, schemeid);
        if (subschemeid != null && subschemeid.length() > 0)
            validatesubScheme(vdt, subschemeid);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside the VoucherMIS update Fn()..");
        final String strVHID = voucherheaderid + "";
        final VoucherMIS misobj = new VoucherMIS();
        misobj.setDivisionId(field);
        misobj.setVoucherheaderid(strVHID);
        misobj.setFundsourceid(fundSourceId);
        misobj.setScheme(schemeid);
        misobj.setSubscheme(subschemeid);
        if ("I".equals(mode))
            misobj.insert();
        else if ("U".equals(mode))
            misobj.update();
    }

    /**
     * This method will insert/update the informations to egf_vouchermis table based on the value of mode.
     * @param conn
     * @param mode
     * @param voucherheaderid
     * @param field
     * @throws TaskFailedException
     */
    public void updateVoucherMIS(final String mode, final int voucherheaderid, final String field, final String fundSourceId,
            final String schemeid,
            final String subschemeid, final String vdt, final String departmentId, final String functionary,
            final String sourcepath)
            throws TaskFailedException
    {
        if (schemeid != null && schemeid.length() > 0)
            validateScheme(vdt, schemeid);
        if (subschemeid != null && subschemeid.length() > 0)
            validatesubScheme(vdt, subschemeid);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside the VoucherMIS update Fn()..");
        final String strVHID = voucherheaderid + "";
        final VoucherMIS misobj = new VoucherMIS();
        misobj.setDivisionId(field);
        misobj.setVoucherheaderid(strVHID);
        misobj.setFundsourceid(fundSourceId);
        misobj.setScheme(schemeid);
        misobj.setSubscheme(subschemeid);
        misobj.setDepartmentId(departmentId);
        misobj.setFunctionary(functionary);
        misobj.setSourcePath(sourcepath);
        if ("I".equals(mode))
            misobj.insert();
        else if ("U".equals(mode))
            misobj.update();
    }

    /*
     * input finYear, type is dummy or empty string
     */
    public String getFinYearID(final String finYear, final String type) throws Exception, TaskFailedException
    {
        String finyearid = "";
        Query pst = null;
        List<Object[]> rs = null;
        try {
            if (!StringUtils.isNotBlank(finYear))
                throw new TaskFailedException("financialyear is empty or null");
            final String sql = "select ID as \"financialYearID\" from financialyear where financialyear= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            pst.setString(0, finYear);
            rs = pst.list();
            for (final Object[] element : rs)
                finyearid = element[0].toString();
        } catch (final Exception e) {
            throw taskExc;
        }
        return finyearid;
    }

    public int getDetailTypeId(final String glCode) throws TaskFailedException, Exception
    {
        int detailTypeId = 0;
        List<Object[]> rs = null;
        Query pst = null;
        try {
            final String qryDetailType = "Select detailtypeid from chartofaccountdetail where glcodeid=(select id from chartofaccounts where glcode= ?)";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(qryDetailType);
            pst.setString(0, glCode);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("  qryDetailType  " + qryDetailType);
            rs = pst.list();
            for (final Object[] element : rs)
                detailTypeId = Integer.parseInt(element[0].toString());
        } catch (final Exception e) {
            throw taskExc;
        }
        return detailTypeId;
    }

    public int getDetailTypeIdById(final String glCodeid) throws TaskFailedException, Exception
    {
        int detailTypeId = 0;
        List<Object[]> rs = null;
        Query pst = null;
        try {
            final String qryDetailType = "Select detailtypeid from chartofaccountdetail where glcodeid= ?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(qryDetailType);
            pst.setString(0, glCodeid);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("  qryDetailType  " + qryDetailType);
            rs = pst.list();
            for (final Object[] element : rs)
                detailTypeId = Integer.parseInt(element[0].toString());
        } catch (final Exception e) {
            throw taskExc;
        }
        return detailTypeId;
    }

    public void cancelVouchers(final String voucherHeaderId) throws TaskFailedException, Exception, ParseException
    {
        Query ps = null;
        List<Object[]> rs = null;
        final VoucherHeader vh = new VoucherHeader();
        final String getRefVoucher = "SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b " +
                "WHERE a.CGN=b.REFCGNO AND b.id=?";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getRefVoucher  " + getRefVoucher);
        try {
            ps = HibernateUtil.getCurrentSession().createSQLQuery(getRefVoucher);
            vh.setId(voucherHeaderId);
            final EGovernCommon cm = new EGovernCommon();
            cm.getCurrentDate();

            /*
             * if(LOGGER.isDebugEnabled()) LOGGER.debug("Update the egf_record_status table of original voucher");
             * egfstatus.setEffectiveDate(formatter.format(sdf.parse( today ))); egfstatus.setStatus("4");
             * egfstatus.setVoucherheaderId(voucherHeaderId); egfstatus.update();
             */if (LOGGER.isDebugEnabled())
                LOGGER.debug("Update the original voucher");
            vh.setStatus("" + 4);
            vh.update();

            // Check if there is any related vouchers
            ps.setString(0, voucherHeaderId);
            rs = ps.list();
            for (final Object[] element : rs) {
                // egfRecordStatus egfstatusRef= new egfRecordStatus();
                final String refVhid = element[0].toString();
                vh.setId(refVhid);
                /*
                 * egfstatusRef.setEffectiveDate(formatter.format(sdf.parse( today ))); egfstatusRef.setStatus("4");
                 * egfstatusRef.setVoucherheaderId(refVhid); egfstatusRef.update();
                 */
                vh.setStatus("" + 4);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("before voucher update");
                vh.update();
            }
        } catch (final Exception e) {
            throw taskExc;
        }
    }

    public String getAccountdetailtypeAttributename() throws TaskFailedException, Exception
    {
        List<Object[]> rs = null;
        Query pst = null;
        String retval = "";
        StringBuffer attrName = new StringBuffer();
        try {
            final String accQuery = "select id,attributename from accountdetailtype where name='Creditor' ";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(accQuery);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("  accQuery  " + accQuery);
            rs = pst.list();
            for (final Object[] element : rs) {
                attrName = attrName.append(element[0].toString());
                attrName = attrName.append("#").append(element[1].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(">>>attrName " + attrName);
            }
        } catch (final Exception e)
        {
            LOGGER.error("Exception in payment:" + e);
            throw taskExc;
        }
        retval = attrName.toString();
        return retval;
    }

    /**
     * This method will retrieve the VoucherHeader Object based on the voucherheaderid passed
     * @author Sapna
     * @param conn
     * @param vhId
     * @return
     */
    public VoucherHeader getVoucherHeader(final String vhId) throws TaskFailedException
    {
        final VoucherHeader vhrHdr = new VoucherHeader();
        Query pst;
        List<Object[]> resultset;
        try
        {
            final String query = "select type as \"recType\",cgn as \"cgn\",name as \"name\",vouchernumber as \"voucherNum\",status as \"status\",fundid as \"fundid\",lastmodifieddate as \"lastModDate\",createdby as \"createdby\" from voucherheader where id= ?";
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, vhId);
            resultset = pst.list();
            for (final Object[] element : resultset) {
                vhrHdr.setId(vhId);
                vhrHdr.setType(element[0].toString());
                vhrHdr.setCgn(element[1].toString());
                vhrHdr.setName(element[2].toString());
                vhrHdr.setVoucherNumber(element[3].toString());
                vhrHdr.setStatus(element[4].toString());
                vhrHdr.setFundId(element[5].toString());
                vhrHdr.setLastModifiedDate(element[6].toString());
                vhrHdr.setCreatedby(element[7].toString());
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("vhrHdr type- " + vhrHdr.getType() + " vhrHdr.getId()- " + vhrHdr.getId());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("vhrHdr.setCgn()-" + vhrHdr.getCgn() + " vhrHdr.setName-" + vhrHdr.getName()
                        + " vhrHdr.setVoucherNumber-" + vhrHdr.getVoucherNumber());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(" vhrHdr.setStatus-" + vhrHdr.getStatus() + " vhrHdr.setFundId -" + vhrHdr.getFundId()
                        + " vhrHdr.setLastModifiedDate-  " + vhrHdr.getlastModifiedBy() + " vhrHdr.setCreatedby()-"
                        + vhrHdr.getCreatedby());
        } catch (final Exception sqlex)
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sqlex.getMessage(), sqlex);
            throw new TaskFailedException(sqlex.getMessage());
        }
        return vhrHdr;
    }

    /**
     * This api returns the SQL date format .
     * @author Iliyaraja
     * @param bill_date
     * @return
     */
    public String getSQLDateFormat(final String bill_date)
    {
        final SimpleDateFormat sdf = sdfFormatddMMyyyy;
        final SimpleDateFormat formatter = dtFormat;
        // Date dt=new Date();
        String dateFormat = null;
        try
        {
            // dt = sdf.parse( bill_date );
            dateFormat = formatter.format(sdf.parse(bill_date));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("dateFormat ---" + dateFormat);
        } catch (final Exception e)
        {
            LOGGER.error(e.getMessage(), e);

        }
        return dateFormat;
    }

    /**
     * This api returns the SQL date & time format .
     * @author Iliyaraja
     * @param bill_date
     * @return
     */
    public String getSQLDateTimeFormat(final String bill_date)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        // Date dt=new Date();
        String dateFormat = null;
        try
        {
            // dt = sdf.parse(bill_date);
            dateFormat = formatter.format(sdf.parse(bill_date));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("dateFormat ---" + dateFormat);
        } catch (final Exception e)
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(e.getMessage(), e);
        }
        return dateFormat;
    }

    /**
     * This API returns the fiscialperiodid for the date passed
     * @param vDate
     * @param con
     * @return
     * @throws TaskFailedException
     */
    public String getFiscalPeriod(final String vDate) throws TaskFailedException {
        BigInteger fiscalPeriod = null;
        final String sql = "select id from fiscalperiod  where '" + vDate + "' between startingdate and endingdate";
        try {
            final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            final List<BigInteger> rset = pst.list();
            fiscalPeriod = rset != null ? rset.get(0) : BigInteger.ZERO;
        } catch (final Exception e) {
            LOGGER.error("Exception..." + e.getMessage());
            throw new TaskFailedException(e.getMessage());
        }
        return fiscalPeriod.toString();
    }

    /**
     * This API returns the Name of the accountcode when passing the glcode.
     * @param code
     * @param con
     * @return
     * @throws TaskFailedException
     */
    public String getCodeName(final String code) throws TaskFailedException
    {
        String name = "";
        try {
            final String query = "select name from chartofaccounts where glcode= ?";
            final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, code);
            final List<Object[]> rset = pst.list();
            for (final Object[] element : rset)
                name = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("error  " + e.toString());
            throw new TaskFailedException(e.getMessage());
        }
        return name;
    }

    /**
     * This function will return value if data!=null && data is not emtpy string; else return defaultValue
     * @param data
     * @param defaultValue
     * @return
     */

    public String getCodeNameById(final String codeid) throws TaskFailedException
    {
        String name = "";
        try {
            final String query = "select name from chartofaccounts where id= ?";
            final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, codeid);
            final List<Object[]> rset = pst.list();
            for (final Object[] element : rset)
                name = element[0].toString();
        } catch (final Exception e) {
            LOGGER.error("error  " + e.toString());
            throw new TaskFailedException(e.getMessage());
        }
        return name;
    }

    public String assignValue(final String data, final String defaultValue)
    {
        if (StringUtils.isNotBlank(data))
            return "'" + trimChar(formatString(data), "'".charAt(0)) + "'";
        else if (StringUtils.isNotBlank(defaultValue))
            return "'" + trimChar(defaultValue, "'".charAt(0)) + "'";
        else
            return defaultValue;
    }

    /**
     * this function trims ch in string
     * @param str
     * @param ch
     * @return
     */
    public String trimChar(String str, final char ch)
    {
        Boolean b = true, e = true;
        str = str.trim();
        while (str.length() > 0 && (b || e))
        {
            if (str.charAt(0) == ch)
                str = str.substring(1, str.length());
            else
                b = false;
            if (str.charAt(str.length() - 1) == ch)
                str = str.substring(0, str.length() - 1);
            else
                e = false;
        }
        return str;
    }

    /**
     * Return next available cheque number
     * @param con
     * @param accId
     * @param noChqs--number cheque number need to be generated
     * @return
     * @throws TaskFailedException
     */
    public String[] getNextChequeNo(final Connection con, final String accId, final int noChqs, final int allotid)
            throws TaskFailedException
    {
        /*
         * Map chqRange=new HashMap(); long availChqs[]=new long[noChqs+1];
         */
        final String strChqs[] = new String[noChqs + 1];
        /*
         * Statement stmt=null; ResultSet rs=null; long availChqNo=0; int iNumChqBooks=0; String
         * query="select fromchequenumber,tochequenumber  from egf_account_cheques where bankaccountid="
         * +accId+" and allotedto="+allotid+" order by id"; try{ stmt= con.createStatement(); if(LOGGER.isDebugEnabled())
         * LOGGER.debug(query); rs=stmt.list(query); while(rs.next()) { iNumChqBooks++; Map temp=new HashMap();
         * temp.put(FROMCHQNO,rs.getString(1)); temp.put(TOCHQNO,rs.getString(2)); chqRange.put(iNumChqBooks,temp); } rs.close();
         * stmt.close(); if(LOGGER.isDebugEnabled()) LOGGER.debug("chequeRange:"+chqRange); //decode if cheque number is null, it
         * should be set to the first cheque number of the chq book //Also checking thhat the first character of the cheque number
         * is a number query=
         * "select decode(max(to_number(cd.chequenumber)),null,?,max(to_number(cd.chequenumber))) as \"ChqNo\" from chequedetail cd where cd.accountnumberid="
         * +accId +
         * " and cd.chequenumber between ? and ? and ascii(chequenumber)<=57 and ispaycheque=1 AND LENGTH(TO_CHAR(CD.CHEQUENUMBER))=LENGTH(TO_CHAR(?))"
         * + " UNION " +
         * " select decode(max(to_number(sc.chequenumber)),null,?,max(to_number(sc.chequenumber))) from eg_surrendered_cheques sc"
         * + " where sc.bankaccountid="+accId+
         * " and sc.chequenumber between ? and ? and ascii(chequenumber)<=57 AND LENGTH(TO_CHAR(sc.CHEQUENUMBER))=LENGTH(TO_CHAR(?)) order by \"ChqNo\" desc"
         * ; PreparedStatement ps= HibernateUtil.getCurrentSession().createSQLQuery(query); if(LOGGER.isDebugEnabled())
         * LOGGER.debug("maxChqNo query:"+query); int count=1; int j=1; if(LOGGER.isDebugEnabled())
         * LOGGER.debug("check1:"+j+":"+iNumChqBooks+":"+count+":"+noChqs); String toCheqeNo="",fromChequeNo="",StrAvailChqNo="";
         * while(j<=iNumChqBooks && count<=noChqs) { if(LOGGER.isDebugEnabled())
         * LOGGER.debug("inside while:"+j+":"+iNumChqBooks+":"+count+":"+noChqs); int
         * firstNo=Integer.parseInt(((HashMap)chqRange.get(j)).get(FROMCHQNO).toString())-1;
         * fromChequeNo=((HashMap)chqRange.get(j)).get(FROMCHQNO).toString();
         * toCheqeNo=((HashMap)chqRange.get(j)).get(TOCHQNO).toString(); ps.setString(1,firstNo+"");
         * ps.setString(2,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
         * ps.setString(3,((HashMap)chqRange.get(j)).get(TOCHQNO).toString());
         * ps.setString(4,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString()); ps.setString(5,firstNo+"");
         * ps.setString(6,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString());
         * ps.setString(7,((HashMap)chqRange.get(j)).get(TOCHQNO).toString());
         * ps.setString(8,((HashMap)chqRange.get(j)).get(FROMCHQNO).toString()); ResultSet res=ps.list(); if(res.next()){
         * availChqNo=Integer.parseInt(res.getString(1))+1; } if(LOGGER.isDebugEnabled())
         * LOGGER.debug("availChqNo:count:noChqs"+availChqNo+":"+Integer.parseInt(toCheqeNo)+":"+count+":"+noChqs);
         * while((availChqNo<=Integer.parseInt(toCheqeNo)) && (count<=noChqs)) { if(LOGGER.isDebugEnabled())
         * LOGGER.debug("availChqNo:count:noChqs"+availChqNo+":"+Integer.parseInt(toCheqeNo)+":"+count+":"+noChqs);
         * if(LOGGER.isDebugEnabled())
         * LOGGER.debug("From Cheque No. len "+fromChequeNo.length()+" Available chq len.."+(availChqNo+"").length());
         * StrAvailChqNo=availChqNo+""; int diff=(fromChequeNo.length()-(availChqNo+"").length()); if(diff>0) for(int
         * k=0;k<diff;k++){ StrAvailChqNo="0"+StrAvailChqNo; } strChqs[count]=StrAvailChqNo; availChqs[count]=availChqNo;
         * if(count!=noChqs)availChqNo++; count++; } j++; res.close(); } ps.close(); if(toCheqeNo !=null && !toCheqeNo.equals(""))
         * if(LOGGER.isDebugEnabled()) LOGGER.debug(availChqNo+":"+Integer.parseInt(toCheqeNo)); for(int
         * k=1;k<=availChqs.length-1;k++){if(LOGGER.isDebugEnabled())
         * LOGGER.debug("chqs["+k+"]:"+availChqs[k]+" String cheque no :"+strChqs[k]);} if(toCheqeNo !=null &&
         * !toCheqeNo.equals("")) { if((count<noChqs) || (availChqNo>Integer.parseInt(toCheqeNo))) {
         * //datacol.addValue("success",false); datacol.addMessage("eGovFailure","Sufficient no.of cheques are not available");
         * throw new ApplicationException("Sufficient no.of cheques are not available for this account"); } } } catch(Exception e)
         * { LOGGER.error("Exception in creating statement "); throw new
         * TaskFailedException("Exception in generating Cheque no:"+e.getMessage()); } //return availChqs;
         */
        return strChqs;
    }

    public List getBankAndBranch(final String fundId) throws TaskFailedException, Exception
    {
        Query pst = null;
        List<Object[]> rs = null;
        String query = null;
        final List bankBranchList = new ArrayList();
        String fundCondition = "";
        if (fundId != null && !fundId.equals(""))
            fundCondition = " and branch.id in(select branchid from bankaccount where fundid=" + fundId + ")";

        query = "select branch.ID as \"bankBranchId\", concat(concat(ba.name, ' - '),branch.branchName) as \"bankBranchName\" "
                + " FROM bank ba, bankBranch branch WHERE branch.bankId=ba.ID AND ba.isActive=true AND branch.isActive = true "
                + fundCondition + " order by LOWER(ba.name) ";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query:" + query);
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            rs = pst.list();
            LabelValueBean obj = null;
            if (rs != null)
                for (final Object[] element : rs) {
                    obj = new LabelValueBean();
                    obj.setId(Integer.parseInt(element[0].toString()));
                    obj.setName(element[1].toString());
                    bankBranchList.add(obj);
                }
        } catch (final Exception e)
        {
            LOGGER.error("Exception in creating statement:" + pst);
            throw new TaskFailedException(e.getMessage());
        }

        return bankBranchList;
    }

    public List getCashierNameList() throws TaskFailedException, Exception
    {
        Query pst = null;
        List<Object[]> rs = null;
        String query = null;
        final List cashierList = new ArrayList();
        query = "SELECT id, name FROM billcollector WHERE lower(type) = 'cashier' and isactive=1";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query:" + query);
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            rs = pst.list();
            LabelValueBean obj = null;
            if (rs != null)
                for (final Object[] element : rs) {
                    obj = new LabelValueBean();
                    obj.setId(Integer.parseInt(element[0].toString()));
                    obj.setName(element[1].toString());
                    cashierList.add(obj);
                }
        } catch (final Exception e)
        {
            LOGGER.error("Exception in creating statement:" + pst);
            throw new TaskFailedException(e.getMessage());
        }

        return cashierList;
    }

    public String getBillCollectorName(final String billCollectorId) throws TaskFailedException, Exception
    {
        Query pst = null;
        List<Object[]> rs = null;
        String query = null;
        String name = null;
        query = "SELECT name FROM billcollector WHERE id = ?";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query:" + query);
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(0, billCollectorId);
            rs = pst.list();
            for (final Object[] element : rs)
                name = element[0].toString();
        } catch (final Exception e)
        {
            LOGGER.error("Exception in creating statement:" + pst);
            throw taskExc;
        }

        return name;
    }

    /*
     * public String getDeptIdFromVoucherMis(Connection con, String voucherId) throws TaskFailedException,Exception { Statement
     * st=null; ResultSet rs=null; String query=null; String deptId=null;
     * query="SELECT departmentid FROM vouchermis WHERE voucherheaderid = "+voucherId; if(LOGGER.isDebugEnabled())
     * LOGGER.debug("query:"+query); try { st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
     * rs = st.list(query); if(rs!=null && rs.next()) { deptId= rs.getString("DEPARTMENTID"); } } catch(Exception e) {
     * LOGGER.error("Exception in creating statement:"+st); throw taskExc; } finally { rs.close(); } return deptId; }
     */

    /**
     * checking for duplicate account code and Function Code Combination
     * @param tableObj
     * @param funIdIdx
     * @param glCodeIdx
     * @return true/false
     */
    public boolean checkDuplicatesForFunction_AccountCode(final DataCollection datacol, final String[][] tableObj,
            final int funIdIdx, final int glCodeIdx) {
        String funcIdtemp1, glcodeTemp1, funcIdtemp2, glcodeTemp2;
        for (int i = 0; i < tableObj.length; i++) {
            funcIdtemp1 = tableObj[i][funIdIdx];
            glcodeTemp1 = tableObj[i][glCodeIdx];
            if (funcIdtemp1.equalsIgnoreCase("") && glcodeTemp1.equalsIgnoreCase(""))
                continue;
            if (glcodeTemp1.trim().equalsIgnoreCase("")) {
                datacol.addMessage(EXILRPERROR, "delete " + i + "th row in " + tableObj + " grid");
                return false;
            }
            for (int j = i + 1; j < tableObj.length; j++) {
                funcIdtemp2 = tableObj[j][funIdIdx];
                glcodeTemp2 = tableObj[j][glCodeIdx];
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("funcIdtemp1:" + funcIdtemp1);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("glcodeTemp1:" + glcodeTemp1);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("funcIdtemp2:" + funcIdtemp2);
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("glcodeTemp2:" + glcodeTemp2);
                if (glcodeTemp1.equalsIgnoreCase(glcodeTemp2) && funcIdtemp1.equalsIgnoreCase(funcIdtemp2)) {
                    datacol.addMessage(EXILRPERROR,
                            "Same Account Code & Function Name can not appear more than once...CHECK ACCOUNT : " + glcodeTemp1);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * add blank row to the grid at 0th position
     * @param grid
     * @return
     */
    public String[][] insertBlankRow(final String[][] grid)
    {
        int length = 0;
        boolean hasFirstEmptyRow = true;
        if (grid.length > 0)
            length = grid[0].length;
        else
            return grid;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("entered file>>>");
        for (int i = 0; i < length; i++)
            if (!grid[0][i].trim().equalsIgnoreCase("")) {
                hasFirstEmptyRow = false;
                break;
            }
        if (hasFirstEmptyRow)
            return grid;

        // adding empty row
        final String[][] newGrid = new String[grid.length + 1][length];
        for (int i = 0; i < length; i++)
            newGrid[0][i] = "";
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < length; j++)
                newGrid[i + 1][j] = grid[i][j];
        return newGrid;
    }

    // to Get Scheme List for Particular Fund
    public List getSchemeList(final int fundId) throws TaskFailedException, Exception
    {
        Query pst = null;
        List<Object[]> rs = null;
        String query = null;
        final List schemeList = new ArrayList();
        query = "select id, name from scheme where fundid= ? order by deptName";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("query:" + query);
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setInteger(0, fundId);
            rs = pst.list();
            LabelValueBean obj = null;
            if (rs != null)
                for (final Object[] element : rs) {
                    obj = new LabelValueBean();
                    obj.setId(Integer.parseInt(element[0].toString()));
                    obj.setName(element[1].toString());
                    schemeList.add(obj);
                }
        } catch (final Exception e)
        {
            LOGGER.error("Exception in creating statement:" + pst);
            throw new TaskFailedException(e.getMessage());
        }

        return schemeList;
    }

    /**
     * To get the EGW_STATUS id
     * @param con
     * @param moduleType
     * @param description
     * @return statusId
     */
    public String getEGWStatusId(final String moduleType, final String description) throws TaskFailedException
    {
        String statusId = "0";
        Query pstmt = null;
        List<Object[]> rs = null;
        try
        {
            final String sql = " select distinct id from egw_status where upper(moduletype)= ? and upper(description)= ? ";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("statement" + sql);
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            pstmt.setString(0, moduleType.toUpperCase());
            pstmt.setString(1, description.toUpperCase());
            rs = pstmt.list();
            for (final Object[] element : rs)
                statusId = element[0].toString();

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("$$$$$$$$$$$$$$$$$$$statusId===" + statusId);
            if (statusId == null || statusId.equals("0"))
                throw taskExc;

        } catch (final Exception e)
        {
            LOGGER.error("Exception in getEGWStatusId=====:" + e.getMessage());
            throw taskExc;
        }
        return statusId;
    }

    public String getVoucherNumber(final String fundId, final String type, final String voucherno) throws TaskFailedException,
            Exception
    {
        if (type == null || type.equals(""))
            throw new TaskFailedException("Configuration setting for voucher numbering is not done");

        String vouchernumber = "", fType = "";
        Query pst;
        List<Object[]> rs;
        try
        {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(FUNDIDNSQL);
            pst.setInteger(0, Integer.valueOf(fundId));
            rs = pst.list();
            for (final Object[] element : rs)
                fType = element[0].toString();

            if (fType == null || fType.trim().equals(""))
                throw new TaskFailedException("Fund identiefier is null or empty");

            vouchernumber = fType + type + voucherno;
        } catch (final Exception e)
        {
            throw new TaskFailedException("Exp in getVoucherNumber()==" + e.getMessage());
        }
        return vouchernumber;
    }

    /**
     * This function is used to delete a Journal from the system
     * @param cgn
     * @param con
     * @throws TaskFailedException
     */
    public void deleteJournal(final String cgn) throws TaskFailedException
    {
        try {
            Query pstmt = null;
            Query pstmt1 = null;
            Query pstmt2 = null;
            Query pstmt3 = null;
            Query pstmt4 = null;
            Query pstmt5 = null;
            Query pstmt6 = null;
            Query pstmt7 = null;
            int vhid = 0;

            pstmt1 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from egf_record_status where voucherheaderid=?");
            pstmt2 = HibernateUtil
                    .getCurrentSession()
                    .createSQLQuery(
                            "Delete from generalledgerdetail where generalledgerid in (select id from generalledger where voucherheaderid=?)");
            pstmt3 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from generalledger where voucherheaderid=?");
            pstmt4 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from voucherdetail where voucherheaderid=?");
            pstmt5 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from vouchermis where voucherheaderid=?");
            pstmt6 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from vouchermis where voucherheaderid=?");
            pstmt7 = HibernateUtil.getCurrentSession().createSQLQuery("Delete from voucherheader where id=?");

            pstmt = HibernateUtil.getCurrentSession().createSQLQuery("Select id from voucherheader where cgn=?");
            pstmt.setString(0, cgn);
            final List<Object[]> rs = pstmt.list();
            for (final Object[] element : rs)
                vhid = Integer.parseInt(element[0].toString());
            if (rs == null || rs.size() == 0)
                throw new TaskFailedException("Cannot find the journal voucher");

            pstmt1.setInteger(0, vhid);
            pstmt2.setInteger(0, vhid);
            pstmt3.setInteger(0, vhid);
            pstmt4.setInteger(0, vhid);
            pstmt5.setInteger(0, vhid);
            pstmt6.setInteger(0, vhid);
            pstmt7.setInteger(0, vhid);

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
            pstmt4.executeUpdate();
            pstmt5.executeUpdate();
            pstmt6.executeUpdate();
            pstmt7.executeUpdate();

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}
