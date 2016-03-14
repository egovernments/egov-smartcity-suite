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
package com.exilant.eGov.src.domain;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Sathish
 * @version 1.1
 */
@Transactional(readOnly = true)
public class EgRemittanceGldtl
{
    private String id = null;
    private String gldtlId = null;
    private double gldtlAmt =0;
    private String lastModifiedDate = "1-Jan-1900";
    private double remittedAmt = 0;
    private String tdsId = null;
    private static final Logger LOGGER = Logger.getLogger(EgRemittanceGldtl.class);
    private static TaskFailedException taskExc;

    public void setId(final String aId) {
        id = aId;
    }

    public void setGldtlId(final String aGldtlId) {
        gldtlId = aGldtlId;
    }

    public void setGldtlAmt(final double aGldtlAmt) {
        gldtlAmt = aGldtlAmt;
    }

    public void setLastModifiedDate(final String aLastModifiedDate) {
        lastModifiedDate = aLastModifiedDate;
    }

    public void setRemittedAmt(final double aRemittedAmt) {
        remittedAmt = aRemittedAmt;
    }

    public void setTdsId(final String aTdsId) {
        tdsId = aTdsId;
    }

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    public String getGldtlId() {
        return gldtlId;
    }

    public double getGldtlAmt() {
        return gldtlAmt;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public double getRemittedAmt() {
        return remittedAmt;
    }

    public String getTdsId() {
        return tdsId;
    }

    @Transactional
    public void insert() throws SQLException, TaskFailedException
    {
        Query pstmt = null;
        try
        {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            lastModifiedDate = formatter.format(new Date());
            setLastModifiedDate(lastModifiedDate);
        } catch (final Exception e)
        {
            LOGGER.error("Exp in insert to remittance detail" + e.getMessage(), e);
            throw taskExc;
        }
        setId(String.valueOf(PrimaryKeyGenerator.getNextKey("eg_remittance_gldtl")));

        final String insertQuery = "INSERT INTO eg_remittance_gldtl (id, gldtlid, gldtlamt, lastmodifieddate, remittedamt, tdsid) "
                +
                "VALUES (?,?,?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'),?,?)";

        pstmt = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
        pstmt.setBigInteger(0,new BigInteger(id));
        pstmt.setBigInteger(1, new BigInteger(gldtlId));
        pstmt.setDouble(2, gldtlAmt);
        pstmt.setString(3, lastModifiedDate);
        pstmt.setDouble(4, remittedAmt);
        if(tdsId!=null)
            pstmt.setBigInteger(5, new BigInteger(tdsId));
        else
            pstmt.setBigInteger(5, null);

        pstmt.executeUpdate();

    }
}
