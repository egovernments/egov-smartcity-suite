/*
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class VoucherHeader {
	private static final Logger LOGGER = Logger.getLogger(VoucherHeader.class);
	private String id = null;
	/* CG number - unique identifier for the project */
	private String cgn = null;
	/* The system date when the transaction was created */
	private String cgDate = "1-Jan-1900";
	private String name = null;
	private String type = null;
	private String description = null;
	private String effectiveDate = "1-Jan-1900";
	private String voucherNumber = null;
	private String voucherDate = "1-Jan-1900";
	private String departmentId = null;
	private String functionId = null;
	private String fundSourceId = null;
	private String fundId = null;
	private String fiscalPeriodId = null;
	private String status = null;
	private String originalVcId = null;
	private String isConfirmed = null;
	private String createdby = null;
	private String refCgno = null;   
	private String cgvn = null;
	private String lastModifiedBy = null;
	private String lastModifiedDate = "1-Jan-1900";
	private String moduleId = null;
	private boolean isId = false, isField = false;
	private static TaskFailedException taskExc;
	EGovernCommon commonmethods = new EGovernCommon();

	public VoucherHeader() {
	}

	public void setId(String aId) {
		id = aId;
		isId = true;
	}

	public void setCgn(String aCgn) {
		cgn = aCgn;
		isField = true;
	}

	public void setRefCgn(String aRefCgn) {
		refCgno = aRefCgn;
		isField = true;
	}

	public void setCgDate(String aCgDate) {
		cgDate = aCgDate;
		isField = true;
	}

	public void setName(String aName) {
		name = aName;
		isField = true;
	}

	public void setType(String aType) {
		type = aType;
		isField = true;
	}

	public void setDescription(String aDescription) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherheader Description Before:" + aDescription);
		description = aDescription != null ? commonmethods
				.formatString(aDescription) : "";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("voucherheader Description After:" + description);
		if (description.length() == 0)
			description = "";
			isField = true;
	}

	public void setEffectiveDate(String aEffectiveDate) {
		effectiveDate = aEffectiveDate;
		isField = true;
	}

	public void setVoucherNumber(String aVoucherNumber) {
		voucherNumber = aVoucherNumber;
		
		isField = true;
	}

	public void setVoucherDate(String aVoucherDate) {
		voucherDate = aVoucherDate;
		isField = true;
	}

	public void setDepartmentId(String aDepartmentId) {
		departmentId = aDepartmentId;
		if (departmentId.length() == 0)
			departmentId = null;
			isField = true;
	}

	public void setFunctionId(String aFunctionId) {
		functionId = aFunctionId;
		if (functionId.length() == 0)
			functionId = null;
			isField = true;
	}

	public void setCreatedby(String aCreatedBy) {
		createdby = aCreatedBy;
		isField = true;
	}

	public void setFundSourceId(String aFundSourceId) {
		fundSourceId = aFundSourceId;
		if (aFundSourceId != null) {
			if (fundSourceId.length() == 0
					|| (fundSourceId.trim()).equalsIgnoreCase(""))
				fundSourceId = "null";
			isField = true;
		}
	}

	public void setLastModifiedBy(String aLastModifiedBy) {
		lastModifiedBy = aLastModifiedBy;
		isField = true;
	}

	public void setLastModifiedDate(String aLastModifiedDate) {
		lastModifiedDate = aLastModifiedDate;
		isField = true;
	}

	public void setFundId(String aFundId) {
		fundId = aFundId;
		
		isField = true;
	}

	public void setFiscalPeriodId(String afiscalPeriodId) {
		fiscalPeriodId = afiscalPeriodId;
		isField = true;
	}

	public void setStatus(String aStatus) {
		status = aStatus;
		isField = true;
	}

	public void setOriginalVcId(String aOriginalVcId) {
		originalVcId = aOriginalVcId;
		isField = true;
	}

	public void setIsConfirmed(String aIsConfirmed) {
		isConfirmed = aIsConfirmed;
		isField = true;
	}

	public void setCgvn(String aCgvn) {
		cgvn = aCgvn;
		isField = true;
	}

	public void setModuleId(String aModuleid) {
		moduleId = aModuleid;
		isField = true;
	}

	public int getId() {
		return Integer.valueOf(id).intValue();
	}

	public String getCgn() {
		return cgn;
	}

	public String getRefCgn() {
		return refCgno;
	}

	public String getCgDate() {
		return cgDate;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public String getVoucherDate() {
		return voucherDate;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getFundId() {
		return fundId;
	}

	public String getFundSourceId() {
		return fundSourceId;
	}

	public String getFiscalPeriodId() {
		return fiscalPeriodId;
	}

	public String getStatus() {
		return status;
	}

	public String getOriginalVcId() {
		return originalVcId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public String getCgvn() {
		return cgvn;
	}

	public String getlastModifiedBy() {
		return lastModifiedBy;
	}

	public String getlastModifiedDate() {
		return lastModifiedDate;
	}

	public String getModuleId() {
		return moduleId;
	}

	

	public void insert(Connection connection) throws SQLException,
			TaskFailedException {
		String code = EGovConfig.getProperty("egf_config.xml",
				"confirmoncreate", "", "JournalVoucher");// get from the config
		// file
		// If its N we need to confirm on create.
		if (!this.getStatus().equals("2")) {
			if(LOGGER.isInfoEnabled())     LOGGER.info("status   " + getStatus());
			if (code.equalsIgnoreCase("N"))
				isConfirmed = "1";
			else
				isConfirmed = "0";
		}

		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh departmentId   " + departmentId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh functionId   " + functionId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh fundId   " + fundId);

		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh fundSourceId   " + fundSourceId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh fiscalPeriodId   " + fiscalPeriodId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh originalVcId  " + originalVcId);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Inside vh moduleId  " + moduleId);

		if (departmentId == null || departmentId.length() == 0)
			departmentId = null;

		if (functionId == null || functionId.length() == 0)
			functionId = null;

		if (fundId == null || fundId.length() == 0)
			fundId = null;

		// if(fundSourceId != null && fundSourceId.length()==0)
		if (fundSourceId == null || fundSourceId.length() == 0)
			fundSourceId = null;

		if (fiscalPeriodId == null || fiscalPeriodId.length() == 0)
			fiscalPeriodId = null;

		if (originalVcId == null || originalVcId.length() == 0)
			originalVcId = null;

		if (moduleId == null || moduleId.length() == 0)
			moduleId = null;

		effectiveDate = commonmethods.getCurrentDateTime(connection);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss");
			effectiveDate = formatter.format(sdf.parse(effectiveDate));
			setCgDate(effectiveDate);
		} catch (Exception e) {
			LOGGER.error("Exp in insert" + e.getMessage(),e);
			throw taskExc;
		}

		setId(String.valueOf(PrimaryKeyGenerator.getNextKey("VoucherHeader")));

		description = commonmethods.formatString(description);
		name = commonmethods.formatString(name);
		PreparedStatement pst = null;
		try {
			lastModifiedDate = commonmethods.getCurrentDateTime(connection);
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy HH:mm:ss");
			dt = sdf.parse(lastModifiedDate);
			lastModifiedDate = formatter.format(dt);

			String insertQuery = "INSERT INTO voucherheader (Id, CGN, CGDate, Name, Type, Description, "
					+ "EffectiveDate,VoucherNumber,VoucherDate,DepartmentId, FundId,fiscalPeriodId,status,originalVcId,fundSourceid, isConfirmed,createdby, FunctionId,refcgno,cgvn,moduleid,LASTMODIFIEDDATE) "
					+ "VALUES ( ?, ?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'))";

			if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
			pst = connection.prepareStatement(insertQuery);
			pst.setString(1, id);
			pst.setString(2, cgn);
			pst.setString(3, cgDate);
			pst.setString(4, name);
			pst.setString(5, type);
			pst.setString(6, description);
			pst.setString(7, effectiveDate);
			pst.setString(8, voucherNumber.toUpperCase());
			pst.setString(9, voucherDate);
			pst.setString(10, departmentId);
			pst.setString(11, fundId);
			pst.setString(12, fiscalPeriodId);
			pst.setString(13, status);
			pst.setString(14, originalVcId);
			pst.setString(15, fundSourceId);
			pst.setString(16, isConfirmed);
			pst.setString(17, createdby);
			pst.setString(18, functionId);
			pst.setString(19, refCgno);
			pst.setString(20, cgvn);
			pst.setString(21, moduleId);
			pst.setString(22, lastModifiedDate);

			pst.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception in insert " + e.getMessage(),e);
			throw taskExc;
		} finally {
			pst.close();
		}

	}

	public void update(Connection connection) throws SQLException,
			TaskFailedException {
			lastModifiedDate = commonmethods.getCurrentDateTime(connection);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MMM-yyyy");
			lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
		} catch (Exception e) {
			LOGGER.error("Exception in update " + e.getMessage(),e);
			throw taskExc;
		}

		if (isId && isField) {
/*			description = commonmethods.formatString(description);
			name = commonmethods.formatString(name);*/
			newUpdate(connection);
			
		}
	}

	public void newUpdate(Connection con) throws TaskFailedException,
			SQLException {
		EGovernCommon commommethods = new EGovernCommon();
		Query pstmt = null;
		if(description != null){
			description = commonmethods.formatString(description);
		}
		/*name = commonmethods.formatString(name);*/
		setLastModifiedDate(lastModifiedDate);
		StringBuilder query = new StringBuilder(500);
		query.append("update VoucherHeader set ");
		if (cgn != null)
			query.append("cgn=?,");
		if (cgDate != null && !cgDate.equalsIgnoreCase("1-Jan-1900"))
			query.append("cgDate=?,");
		if (name != null)
			query.append("name=?,");
		if (type != null)
			query.append("type=?,");
		if (description != null)
			query.append("description=?,");
		if (effectiveDate != null && !effectiveDate.equalsIgnoreCase("1-Jan-1900") )
			query.append("effectiveDate=?,");
		if (voucherNumber != null)
			query.append("voucherNumber=?,");
		if (voucherDate != null &&  !voucherDate.equalsIgnoreCase("1-Jan-1900"))
			query.append("voucherDate=?,");
		if (departmentId != null)
			query.append("departmentId=?,");
		if (functionId != null)
			query.append("functionId=?,");
		if (fundSourceId != null)
			query.append("fundSourceId=?,");
		if (fundId != null)
			query.append("fundId=?,");
		if (fiscalPeriodId != null)
			query.append("fiscalPeriodId=?,");
		if (status != null)
			query.append("status=?,");
		if (originalVcId != null)
			query.append("originalVcId=?,");
		if (isConfirmed != null)
			query.append("isConfirmed=?,");
		if (createdby != null)
			query.append("createdby=?,");
		if (refCgno != null)
			query.append("refCgno=?,");
		if (cgvn != null)
			query.append("cgvn=?,");
		if (lastModifiedBy != null)
			query.append("lastModifiedBy=?,");
		if (lastModifiedDate != null && !lastModifiedDate.equalsIgnoreCase("1-Jan-1900"))
			query.append("lastModifiedDate=?,");
		if (moduleId != null)
			query.append("moduleId=?,");
		int lastIndexOfComma = query.lastIndexOf(",");
		query.deleteCharAt(lastIndexOfComma);
		query.append(" where id=?");
		try {
			int i = 1;
			pstmt =HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
			if (cgn != null)
				pstmt.setString(i++, cgn);
			if (cgDate != null  && !cgDate.equalsIgnoreCase("1-Jan-1900"))
				pstmt.setString(i++, cgDate);
			if (name != null)
				pstmt.setString(i++, name);
			if (type != null)
				pstmt.setString(i++, type);
			if (description != null)
				pstmt.setString(i++, description);
			if (effectiveDate != null && !effectiveDate.equalsIgnoreCase("1-Jan-1900"))
				pstmt.setString(i++, effectiveDate);
			if (voucherNumber != null)
				pstmt.setString(i++, voucherNumber);
			if (voucherDate != null && !voucherDate.equalsIgnoreCase("1-Jan-1900"))
				pstmt.setString(i++, voucherDate);
			if (departmentId != null)
				pstmt.setString(i++, departmentId);
			if (functionId != null)
				pstmt.setString(i++, functionId);
			if (fundSourceId != null)
				pstmt.setString(i++, fundSourceId);
			if (fundId != null)
				pstmt.setString(i++, fundId);
			if (fiscalPeriodId != null)
				pstmt.setString(i++, fiscalPeriodId);
			if (status != null)
				pstmt.setString(i++, status);
			if (originalVcId != null)
				pstmt.setString(i++, originalVcId);
			if (isConfirmed != null)
				pstmt.setString(i++, isConfirmed);
			if (createdby != null)
				pstmt.setString(i++, createdby);
			if (refCgno != null)
				pstmt.setString(i++, refCgno);
			if (cgvn != null)
				pstmt.setString(i++, cgvn);
			if (lastModifiedBy != null)
				pstmt.setString(i++, lastModifiedBy);
			if (lastModifiedDate != null && !lastModifiedDate.equalsIgnoreCase("1-Jan-1900"))
				pstmt.setString(i++, lastModifiedDate);
			if (moduleId != null)
				pstmt.setString(i++, moduleId);
			pstmt.setString(i++, id);

			pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exp in update: " + e.getMessage(),e);
			throw taskExc;
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Inside finally block of update"+e.getMessage());
			}
		}
	}

	public void cancelVouchers(String cgvn, Connection conn) throws Exception {

		String query = "select id from voucherheader where cgvn= ?";
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, cgvn);
		if(LOGGER.isInfoEnabled())     LOGGER.info(query);
		ResultSet resultset = pst.executeQuery();
		String vid = "";
		if (resultset.next())
			vid = resultset.getString(1);
		else
			throw new Exception("Voucher does not exist.");
		resultset.close();
		pst.close();

		PreparedStatement ps = null;
		ResultSet rs = null;
		String today;
		VoucherHeader vh = new VoucherHeader();
		String getRefVoucher = "SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "
				+ "WHERE a.CGN=b.REFCGNO AND b.id=?";

		if(LOGGER.isInfoEnabled())     LOGGER.info("getRefVoucher   " + getRefVoucher);
		ps = conn.prepareStatement(getRefVoucher);
		vh.setId(vid);
		egfRecordStatus egfstatus = new egfRecordStatus();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		EGovernCommon cm = new EGovernCommon();
		today = cm.getCurrentDate(conn);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Update the egf_record_status table of original voucher");
		egfstatus.setEffectiveDate(formatter.format(sdf.parse(today)));
		egfstatus.setStatus("4");
		egfstatus.setVoucherheaderId(vid);
		egfstatus.update(conn);
		if(LOGGER.isInfoEnabled())     LOGGER.info("Update the original voucher");
		vh.setStatus("" + 4);
		vh.update(conn);

		// Check if there is any related vouchers
		ps.clearParameters();
		ps.setString(1, vid);
		rs = ps.executeQuery();
		// if(LOGGER.isInfoEnabled())     LOGGER.info("if any related vouchers exist then we need to  that also.");
		if (rs.next()) {
			egfRecordStatus egfstatusRef = new egfRecordStatus();
			String refVhid = rs.getString(1);
			vh.setId(refVhid);
			egfstatusRef.setEffectiveDate(formatter.format(sdf.parse(today)));
			egfstatusRef.setStatus("4");
			egfstatusRef.setVoucherheaderId(refVhid);
			egfstatusRef.update(conn);
			vh.setStatus("" + 4);
			if(LOGGER.isInfoEnabled())     LOGGER.info("before voucher update");
			vh.update(conn);
		}
	}

}
