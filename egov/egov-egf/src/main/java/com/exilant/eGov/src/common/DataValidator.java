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
 * Created on Jan 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

//import java.sql.*;
/**
 * @author siddhu
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class DataValidator {
	private static final Logger LOGGER = Logger.getLogger(DataValidator.class);

	public boolean checkDepartmentId(String deptId, Connection connection) {
		try {
			Query pstmt = null;
			String strQry = "select dept_name from eg_department where id_dept= ?";
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(strQry);
			pstmt.setString(1, deptId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkDepartmentId: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkFunctionId(String funcId, Connection connection) {
		String str = "select name from function where id=?";
		Query pstmt = null;
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(str);
			pstmt.setString(1, funcId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFunctionId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkDepartmentName(String deptName, Connection connection) {
		String str = "select dept_name from eg_department where dept_name=?";
		try {
			Query pstmt = HibernateUtil.getCurrentSession().createSQLQuery(str);
			pstmt.setString(1, deptName);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkDepartmentName: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkFunctionName(String funcName, Connection connection) {
		Query pstmt = null;
		String str = "select name from function where name= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(str);
			pstmt.setString(1, funcName);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFunctionName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkOrganizationStructureId(String orgId,
			Connection connection) {
		Query pstmt = null;
		String valQry = "select name from organizationStructure where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, orgId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}

		} catch (Exception e) {
			LOGGER.error("Exp in checkOrganizationStructureId: "
					+ e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean taxCode(String taxCode, Connection connection) {
		Query pstmt = null;
		String valQry = "select name from taxes where code= ?";
		try {
			// Statement statement=connection.createStatement();
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, taxCode);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in taxCode: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkBankAccount(String branchId, String accNumber,
			Connection connection) {
		Query pstmt = null;
		String valQry = "select id from bankAccount  where branchId= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, branchId);
			List<Object[]> rset = pstmt.list();
			// " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkBankAccount: " + e.getMessage(), e);
			return false;
		}
		return true;
	}

	public boolean checkAccount(String id, Connection connection) {
		Query pstmt = null;
		String valQry = "select id from bankAccount  where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, id);
			List<Object[]> rset = pstmt.list();
			// " where branchId="+branchId+" and accountNumber='"+accNumber+"'");
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkAccount: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkBank(String bankId, Connection connection) {
		Query pstmt = null;
		String valQry = "select name from bank where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, bankId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkBank: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundName(String fundName, Connection connection) {
		Query pstmt = null;
		String valQry = "select name from fund where  name= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, fundName);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundId(String fundId, Connection connection) {
		Query pstmt = null;
		String valQry = "select name from fund where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(valQry);
			pstmt.setString(1, fundId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundSourceName(String fundSourceName,
			Connection connection) {
		Query pstmt = null;
		String fndSrc = "select name from fundsource where name=?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(fndSrc);
			pstmt.setString(1, fundSourceName);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundSourceName: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkFundSourceId(String fundSourceId, Connection connection) {
		Query pstmt = null;
		String srtQry = "select name from fundsource where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(srtQry);
			pstmt.setString(1, fundSourceId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkFundSourceId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkSupplierId(String supplierId, Connection connection) {
		Query pstmt = null;
		String srtQry = "select name from supplier where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(srtQry);
			pstmt.setString(1, supplierId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkSupplierId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkContractorId(String contractorId, Connection connection) {
		Query pstmt = null;
		String srtQry = "select name from contractor where id= ?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(srtQry);
			pstmt.setString(1, contractorId);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkContractorId: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean checkBillCollectorId(String id, String type,
			Connection connection) {
		Query pstmt = null;
		String srtQry = "select name from billCollector  where id=? and type=?";
		try {
			pstmt = HibernateUtil.getCurrentSession().createSQLQuery(srtQry);
			pstmt.setString(1, id);
			pstmt.setString(2, type);
			List<Object[]> rset = pstmt.list();
			if(rset == null || rset.size() == 0) {
				return false;
			}
		} catch (Exception e) {
			LOGGER.error("Exp in checkBillCollectorId: " + e.getMessage());
			return false;
		}
		return true;
	}
}
