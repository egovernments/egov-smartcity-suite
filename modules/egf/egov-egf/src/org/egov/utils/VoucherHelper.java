/**
 * 
 */
package org.egov.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.model.bills.EgBillregister;
import org.egov.model.voucher.VoucherDetails;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.hibernate.Query;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.transactions.CommonMethodsI;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author msahoo
 * 
 */
public class VoucherHelper {
	private static final Logger LOGGER = Logger.getLogger(VoucherHelper.class);
	@SuppressWarnings("unchecked")
	private PersistenceService persistenceService;
	private EmployeeService employeeService;
	private EisUtilService eisUtilService;

	@SuppressWarnings("unchecked")
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	@SuppressWarnings("unchecked")
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public static String getMisQuery(CVoucherHeader voucherHeader) {
		StringBuffer misQuery = new StringBuffer();
		if (null != voucherHeader && null != voucherHeader.getVouchermis()) {
			if (null != voucherHeader.getVouchermis().getDepartmentid()) {
				misQuery.append(" and mis.departmentid.id=");
				misQuery.append(voucherHeader.getVouchermis().getDepartmentid()
						.getId());
			}
			if (null != voucherHeader.getVouchermis().getFunctionary()) {
				misQuery.append(" and mis.functionary.id=");
				misQuery.append(voucherHeader.getVouchermis().getFunctionary()
						.getId());
			}
			if (null != voucherHeader.getVouchermis().getSchemeid()) {
				misQuery.append(" and mis.schemeid.id=");
				misQuery.append(voucherHeader.getVouchermis().getSchemeid()
						.getId());
			}
			if (null != voucherHeader.getVouchermis().getSubschemeid()) {
				misQuery.append(" and mis.subschemeid.id=");
				misQuery.append(voucherHeader.getVouchermis().getSubschemeid()
						.getId());
			}
			if (null != voucherHeader.getVouchermis().getFundsource()) {
				misQuery.append(" and mis.fundsource.id=");
				misQuery.append(voucherHeader.getVouchermis().getFundsource()
						.getId());
			}
			if (null != voucherHeader.getVouchermis().getDivisionid()) {
				misQuery.append(" and mis.divisionid.id=");
				misQuery.append(voucherHeader.getVouchermis().getDivisionid()
						.getId());
			}
		}
		return misQuery.toString();
	}

	public static String getVoucherNumDateQuery(String voucherNumFrom,
			String voucherNumTo, String voucherDateFrom, String voucherDateTo) {
		StringBuffer numDateQuery = new StringBuffer();
		try {
			if (null != voucherNumFrom
					&& StringUtils.isNotEmpty(voucherNumFrom)) {
				numDateQuery.append(" and vh.voucherNumber >'");
				numDateQuery.append(voucherNumFrom).append("'");
			}
			if (null != voucherNumTo && StringUtils.isNotEmpty(voucherNumTo)) {
				numDateQuery.append(" and vh.voucherNumber <'");
				numDateQuery.append(voucherNumTo).append("'");
			}

			if (null != voucherDateFrom
					&& StringUtils.isNotEmpty(voucherDateFrom)) {
				numDateQuery
						.append(" and vh.voucherDate>='")
						.append(Constants.DDMMYYYYFORMAT1
								.format(Constants.DDMMYYYYFORMAT2
										.parse(voucherDateFrom))).append("'");
			}
			if (null != voucherDateTo && StringUtils.isNotEmpty(voucherDateTo)) {
				numDateQuery
						.append(" and vh.voucherDate<='")
						.append(Constants.DDMMYYYYFORMAT1
								.format(Constants.DDMMYYYYFORMAT2
										.parse(voucherDateTo))).append("'");

			}
		} catch (ParseException e) {
			LOGGER.debug("Exception occured while parsing date" + e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new EGOVRuntimeException(
					"Error occured while executing search instrument query");
		}
		return numDateQuery.toString();
	}

	public static String getBillMisQuery(EgBillregister egBillRegister) {

		StringBuffer misQuery = new StringBuffer(300);

		if (null != egBillRegister
				&& null != egBillRegister.getEgBillregistermis()) {

			if (null != egBillRegister.getEgBillregistermis().getFund()) {
				misQuery.append(" and billmis.fund.id=")
						.append(egBillRegister.getEgBillregistermis().getFund()
								.getId());
			}

			if (null != egBillRegister.getEgBillregistermis().getEgDepartment()) {
				misQuery.append(" and billmis.egDepartment.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getEgDepartment().getId());
			}
			if (null != egBillRegister.getEgBillregistermis()
					.getFunctionaryid()) {
				misQuery.append(" and billmis.functionaryid.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getFunctionaryid().getId());
			}
			if (null != egBillRegister.getEgBillregistermis().getScheme()) {
				misQuery.append(" and billmis.scheme.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getScheme().getId());
			}
			if (null != egBillRegister.getEgBillregistermis().getSubScheme()) {
				misQuery.append(" and billmis.subScheme.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getSubScheme().getId());
			}
			if (null != egBillRegister.getEgBillregistermis().getFundsource()) {
				misQuery.append(" and billmis.fundsource.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getFundsource().getId());
			}
			if (null != egBillRegister.getEgBillregistermis().getFieldid()) {
				misQuery.append(" and billmis.fieldid.id=");
				misQuery.append(egBillRegister.getEgBillregistermis()
						.getFieldid().getId());
			}
		}
		return misQuery.toString();

	}

	public static String getBillDateQuery(String billDateFrom, String billDateTo) {
		StringBuffer numDateQuery = new StringBuffer();
		try {

			if (null != billDateFrom && StringUtils.isNotEmpty(billDateFrom)) {
				numDateQuery
						.append(" and br.billdate>='")
						.append(Constants.DDMMYYYYFORMAT1
								.format(Constants.DDMMYYYYFORMAT2
										.parse(billDateFrom))).append("'");
			}
			if (null != billDateTo && StringUtils.isNotEmpty(billDateTo)) {
				numDateQuery
						.append(" and br.billdate<='")
						.append(Constants.DDMMYYYYFORMAT1
								.format(Constants.DDMMYYYYFORMAT2
										.parse(billDateTo))).append("'");

			}
		} catch (ParseException e) {
			LOGGER.debug("Exception occured while parsing date" + e);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new EGOVRuntimeException(
					"Error occured while executing search instrument query");
		}
		return numDateQuery.toString();
	}

	public static String getGeneratedVoucherNumber(Integer fundId,
			String voucherType, Date voucherDate, String vNumGenMode,
			String voucherNumber) throws Exception {		
				LOGGER.debug("fundId | in getGeneratedVoucherNumber      :" + fundId);
				LOGGER.debug("voucherType | in getGeneratedVoucherNumber :"
						+ voucherType);
				LOGGER.debug("voucherDate | in getGeneratedVoucherNumber :"
						+ voucherDate);
				PersistenceService persistenceService = new PersistenceService();
				persistenceService.setSessionFactory(new SessionFactory());
				final EGovernCommon cm = new EGovernCommon();
		
				CommonMethodsI cmImpl = new CommonMethodsImpl();
				Query query = HibernateUtil.getCurrentSession().createQuery(
						"select f.identifier from Fund f where id=:fundId");
				query.setInteger("fundId", fundId);
		
				Fund vFund = (Fund) persistenceService.find("from Fund where  id=?",
						fundId);
				String fundIdentifier = vFund.getIdentifier().toString();
				String vDate = Constants.DDMMYYYYFORMAT2.format(voucherDate);
				final String vDateTemp = Constants.DDMMYYYYFORMAT1.format(voucherDate);
		
				String transNumber = "";
				if (vNumGenMode == null)
					vNumGenMode = "Auto";
				if (voucherNumber == null)
					voucherNumber = "";
				if (voucherType == null)
					voucherType = "";
				String fVoucherNumber = null;
		
				if (vNumGenMode.equalsIgnoreCase("Auto")) {
					LOGGER.info(" before transNumber................" + transNumber);
					/*
					 * if(vNumGenMode.equalsIgnoreCase("Auto")) transNumber =
					 * cmImpl.getTransRunningNumber
					 * (fundId.toString(),voucherType,vDate,conn);
					 * LOGGER.info("after transNumber..........................."
					 * +transNumber);
					 */String monthArr[] = vDate.split("/");
					String month = (String) monthArr[1];
					// new ScriptService(2,5,10,10);
					ScriptService scriptExecutionService = new ScriptService(0, 0, 0, 0); // initialized
																							// as
																							// per
																							// the
																							// globalApplicationContext.xml
																							// bean
																							// def.
					scriptExecutionService.setSessionFactory(new SessionFactory());
		
					String scriptName = "voucherheader.vouchernumber";
					// Script
					// voucherNumberScript=(Script)persistenceService.findAllByNamedQuery(Script.BY_NAME,
					// scriptName).get(0);
					ScriptContext scriptContext = ScriptService.createContext(
							"fundIdentity", fundIdentifier, "voucherType", voucherType,
							"transNumber", transNumber, "vNumGenMode", vNumGenMode,
							"date", voucherDate, "month", month, "voucherNumber",
							voucherNumber, "cmImpl", cmImpl, "persistenceService",
							persistenceService);
					fVoucherNumber = (String) scriptExecutionService.executeScript(
							scriptName, scriptContext);
		
				} else {
					fVoucherNumber = fundIdentifier + "/" + voucherType + voucherNumber;
				}
				LOGGER.debug("fVoucherNumber | fVoucherNumber in getGeneratedVoucherNumber :"
						+ fVoucherNumber);
		
				// unique checking...
				LOGGER.debug("unique checking for voucher number :- " + fVoucherNumber);
				final String fVoucherNo=fVoucherNumber;
				HibernateUtil.getCurrentSession().doWork(new Work() {		
					@Override
					public void execute(Connection conn) throws SQLException {
						LOGGER.debug("unique checking conn :- " + conn);
						try {
							if (!cm.isUniqueVN(fVoucherNo, vDateTemp, conn)) {
								LOGGER.debug("unique checking conn :- " + conn);
								throw new EGOVRuntimeException(
										"Trying to create Duplicate Voucher Number");
							}
						} catch (TaskFailedException e) {
							LOGGER.debug("Error in finding unique VoucherNumber");
							throw new EGOVRuntimeException(
									"Error in finding unique VoucherNumber");
						}
					}});
				return fVoucherNumber;
			
	}

	/**
	 * return the glcodes that are repeated. e.g [1,2,2,2,3,3] returns [2,3]
	 * 
	 * @param billDetailslist
	 * @return
	 */
	public static List<String> getRepeatedGlcodes(
			List<VoucherDetails> billDetailslist) {

		List<String> list = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (VoucherDetails voucherDetail : billDetailslist) {
			String glCodeIdDetail = voucherDetail.getGlcodeIdDetail()
					.toString();
			if (map.containsKey(glCodeIdDetail)) {
				list.add(glCodeIdDetail);
			} else {
				map.put(glCodeIdDetail, glCodeIdDetail);
			}
		}
		return list;
	}

	public List<Department> getAllAssgnDeptforUser() {
		// load the primary and secondary assignment departments of the logged
		// in user
		PersonalInformation employee = employeeService.getEmpForUserId(Integer
				.valueOf(EGOVThreadLocals.getUserId()));
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("code", employee.getCode());
		List<EmployeeView> listEmployeeView = eisUtilService
				.getEmployeeInfoList(paramMap);
		List<Department> departmentList = new ArrayList<Department>();
		for (EmployeeView employeeView : listEmployeeView) {
			departmentList.add(employeeView.getDeptId());
		}
		return departmentList;
	}

	public void setEisUtilService(EisUtilService eisUtilService) {
		this.eisUtilService = eisUtilService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

}
