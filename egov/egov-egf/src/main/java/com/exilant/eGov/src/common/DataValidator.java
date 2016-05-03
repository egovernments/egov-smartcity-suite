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
/*
 * Created on Jan 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

//import java.sql.*;
/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class DataValidator {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private static final Logger LOGGER = Logger.getLogger(DataValidator.class);

    public boolean checkDepartmentId(final String deptId, final Connection connection) {
        try {
            Query pstmt = null;
            final String strQry = "select dept_name from eg_department where id_dept= ?";
            pstmt = persistenceService.getSession().createSQLQuery(strQry);
            pstmt.setString(0, deptId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkDepartmentId: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean checkFunctionId(final String funcId, final Connection connection) {
        final String str = "select name from function where id=?";
        Query pstmt = null;
        try {
            pstmt = persistenceService.getSession().createSQLQuery(str);
            pstmt.setString(0, funcId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFunctionId: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkDepartmentName(final String deptName, final Connection connection) {
        final String str = "select dept_name from eg_department where dept_name=?";
        try {
            final Query pstmt = persistenceService.getSession().createSQLQuery(str);
            pstmt.setString(0, deptName);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkDepartmentName: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean checkFunctionName(final String funcName, final Connection connection) {
        Query pstmt = null;
        final String str = "select name from function where name= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(str);
            pstmt.setString(0, funcName);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFunctionName: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkOrganizationStructureId(final String orgId,
            final Connection connection) {
        Query pstmt = null;
        final String valQry = "select name from organizationStructure where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, orgId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;

        } catch (final Exception e) {
            LOGGER.error("Exp in checkOrganizationStructureId: "
                    + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean taxCode(final String taxCode, final Connection connection) {
        Query pstmt = null;
        final String valQry = "select name from taxes where code= ?";
        try {
            // Statement statement=connection.createStatement();
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, taxCode);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in taxCode: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean checkBankAccount(final String branchId, final String accNumber,
            final Connection connection) {
        Query pstmt = null;
        final String valQry = "select id from bankAccount  where branchId= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, branchId);
            final List<Object[]> rset = pstmt.list();
            // " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkBankAccount: " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    public boolean checkAccount(final String id, final Connection connection) {
        Query pstmt = null;
        final String valQry = "select id from bankAccount  where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, id);
            final List<Object[]> rset = pstmt.list();
            // " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkAccount: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkBank(final String bankId, final Connection connection) {
        Query pstmt = null;
        final String valQry = "select name from bank where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, bankId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkBank: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkFundName(final String fundName, final Connection connection) {
        Query pstmt = null;
        final String valQry = "select name from fund where  name= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, fundName);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFundName: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkFundId(final String fundId, final Connection connection) {
        Query pstmt = null;
        final String valQry = "select name from fund where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(valQry);
            pstmt.setString(0, fundId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFundId: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkFundSourceName(final String fundSourceName,
            final Connection connection) {
        Query pstmt = null;
        final String fndSrc = "select name from fundsource where name=?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(fndSrc);
            pstmt.setString(0, fundSourceName);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFundSourceName: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkFundSourceId(final String fundSourceId, final Connection connection) {
        Query pstmt = null;
        final String srtQry = "select name from fundsource where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(srtQry);
            pstmt.setString(0, fundSourceId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkFundSourceId: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkSupplierId(final String supplierId, final Connection connection) {
        Query pstmt = null;
        final String srtQry = "select name from supplier where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(srtQry);
            pstmt.setString(0, supplierId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkSupplierId: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkContractorId(final String contractorId, final Connection connection) {
        Query pstmt = null;
        final String srtQry = "select name from contractor where id= ?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(srtQry);
            pstmt.setString(0, contractorId);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkContractorId: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean checkBillCollectorId(final String id, final String type,
            final Connection connection) {
        Query pstmt = null;
        final String srtQry = "select name from billCollector  where id=? and type=?";
        try {
            pstmt = persistenceService.getSession().createSQLQuery(srtQry);
            pstmt.setString(0, id);
            pstmt.setString(1, type);
            final List<Object[]> rset = pstmt.list();
            if (rset == null || rset.size() == 0)
                return false;
        } catch (final Exception e) {
            LOGGER.error("Exp in checkBillCollectorId: " + e.getMessage());
            return false;
        }
        return true;
    }
}