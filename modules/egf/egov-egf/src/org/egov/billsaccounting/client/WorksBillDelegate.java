package org.egov.billsaccounting.client;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.EgwWorksMis;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.billsaccounting.services.WorksBillService;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgActiondetails;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Relation;
import org.egov.commons.service.CommonsService;
import org.egov.dao.recoveries.EgDeductionDetailsHibernateDAO;
import org.egov.dao.recoveries.RecoveryDAOFactory;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.RBACException;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.model.recoveries.Recovery;
import org.egov.services.bills.BillsService;
import org.egov.services.recoveries.RecoveryService;
import org.egov.utils.FinancialConstants;
import org.egov.utils.GetEgfManagers;
import org.hibernate.HibernateException;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.Transaxtion;
import com.exilant.GLEngine.TransaxtionParameter;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.ActionDetails;
import com.exilant.eGov.src.domain.EgfStatusChange;
import com.exilant.eGov.src.domain.VoucherDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.VoucherMIS;
import com.exilant.eGov.src.transactions.CommonMethodsImpl;
import com.exilant.exility.common.TaskFailedException;

public class WorksBillDelegate {
	private CommonsService commonsService;
	private UserService userService;
	private DepartmentService departmentService;
	double totaltdsAmount = 0.0d;
	double totalotherRecoveries = 0.0d;

	private BillsService billsService;
	private static WorksBillService worksBillService = (WorksBillService) GetEgfManagers
			.getWorksBillService();
	Timestamp curDate = new Timestamp(System.currentTimeMillis());
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	NumberFormat nf = new DecimalFormat("##############.00");
	ArrayList transactions = new ArrayList();
	ArrayList netPayListString = null;
	public final BigDecimal BIGDECIMAL0 = new BigDecimal(0);
	private RecoveryService recoveryService;
	private EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao;
	private TdsHibernateDAO tdsHibernateDAO;

	public WorksBillDelegate() {
		try {
			recoveryService = new RecoveryService();
			recoveryService.setSessionFactory(new SessionFactory());
			egDeductionDetHibernateDao = RecoveryDAOFactory.getDAOFactory()
					.getEgDeductionDetailsDAO();
			tdsHibernateDAO = RecoveryDAOFactory.getDAOFactory().getTdsDAO();
			recoveryService
					.setEgDeductionDetHibernateDao(egDeductionDetHibernateDao);
			recoveryService.setTdsHibernateDAO(tdsHibernateDAO);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());

		}

	}

	public final static Logger LOGGER = Logger
			.getLogger(WorksBillDelegate.class);

	/*
	 * create billRegister add data to billRegister billRegistermis billDetails
	 * billPayeedetails Complete BillCreation
	 */
	public Integer postInEgBillRegister(final WorksBillForm wbForm)
			throws EGOVRuntimeException {
		
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Integer>() {

			@Override
			public Integer execute(Connection conn) throws SQLException {
				

				Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer
						.valueOf(Integer.parseInt(wbForm.getCodeList())));
				/*
				 * Check For Total Workorder amount and bill amount
				 */
				String postInEgBillRegisterExpMsg = "Exception in postInEgBillRegister...";

				try {
					BigDecimal woTotalValue = wd.getTotalvalue();
					BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(wd)
							.get(0);// get All billed Amount from billRegister
					if (totalbillvalue == null)
						totalbillvalue = BIGDECIMAL0;
					totalbillvalue = totalbillvalue.add(wd.getPassedamount());// add
																				// total
																				// passed
																				// amt
																				// from
																				// wororder
					totalbillvalue = totalbillvalue.add(new BigDecimal(wbForm
							.getPassedAmount()));// add bill amount
					int amtComp = woTotalValue.compareTo(totalbillvalue);
					if (amtComp == -1) {
						return 1;
					}
					wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(Integer
							.parseInt(wbForm.getCodeList())));

					EgBillregister reg = new EgBillregister();
					reg.setWorksdetailId(wbForm.getCodeList());
					Date bill_Date = sdf.parse(wbForm.getBillDate());
					reg.setBilldate(bill_Date);
					reg.setBillamount(new BigDecimal(wbForm.getBill_Amount()));
					reg.setPassedamount(new BigDecimal(wbForm.getPassedAmount()));
					if (wbForm.getAdjustmentAmount() != null) {
						reg.setAdvanceadjusted(new BigDecimal(wbForm
								.getAdjustmentAmount()));
					}
					reg.setBillstatus("PENDING");
					reg.setBilltype(wbForm.getBill_Type());
					reg.setExpendituretype(wbForm.getExpenditure_Type());
					reg.setNarration(wbForm.getNarration());
					EgwStatus invStatus = null;
					EGovernCommon cm = new EGovernCommon();
					
					invStatus = commonsService.findEgwStatusById(Integer.valueOf(cm
							.getEGWStatusId(conn, "WORKSBILL", "Pending")));
					reg.setStatus(invStatus);
					reg.setCreatedBy(userService.getUserByID(wbForm.getUserId()));
					Date crntDate = new Date();
					reg.setCreatedDate(crntDate);
					// Generate BillNo And Sanction Number
					String sanctionNo = null;
					CommonMethodsImpl cmImpl = new CommonMethodsImpl();
					reg.setBillnumber(cmImpl.getTxnNumber("WBILL", wbForm.billDate,
							conn));
					sanctionNo = cmImpl.getTxnNumber("SAN", wbForm.getBillDate(), conn);
					wbForm.setBillNo(reg.getBillnumber());
					wbForm.setSanctionNo(sanctionNo);
					EgBillregistermis egmis = new EgBillregistermis();
					egmis.setEgBillregister(reg);
					egmis.setFund(wd.getFund());
					egmis.setFundsource(wd.getFundsource());
					egmis.setMbRefNo(wbForm.getMBrefNo());
					egmis.setSanctiondetail(sanctionNo);
					egmis.setSanctiondate(bill_Date);
					Relation rel = commonsService.getRelationById(Integer
							.valueOf(wbForm.getCSId()));
					egmis.setPayto(rel.getName());
					CFinancialYear financialyear = commonsService
							.getFinancialYearByFinYearRange(wbForm.getBillDate());
					egmis.setFinancialyear(financialyear);
					if (!wbForm.getDept().equals("0")) {
						DepartmentImpl egDept = (DepartmentImpl) departmentService
								.getDepartment(Integer.valueOf(Integer.parseInt(wbForm
										.getDept())));
						egmis.setEgDepartment(egDept);
					}
					LOGGER.info("functionary :" + wbForm.getFunctionaryId());
					if (wbForm.getFunctionaryId() != null
							&& !wbForm.getFunctionaryId().equals("0")) {
						LOGGER.info("functionary setting " + wbForm.getFunctionaryId());
						Functionary functionary = commonsService
								.getFunctionaryById(Integer.valueOf(wbForm
										.getFunctionaryId()));
						if (functionary != null)
							egmis.setFunctionaryid(functionary);
					} else {
						LOGGER.info("functionary setting null");
						egmis.setFunctionaryid(null);
					}
					reg.setEgBillregistermis(egmis);
					reg.setEgBilldetailes(postInBilldetails(wbForm, reg));
					billsService.createBillRegister(reg);
					wbForm.setBillId(reg.getId().toString());
					User user = (User) userService.getUserByID(wbForm.getUserId());
					String username = user.getUserName();
					// LOGGER.debug(username);
					wbForm.setUserName(username);
					postInEGActionDetails("WORKSBILL", reg.getId().toString(),
							"Bill Created", wbForm.getUserId());
					createWorkFlow(wbForm);
					// LOGGER.debug("WorkFlow end");
				} catch (NumberFormatException e) {
					LOGGER.error(postInEgBillRegisterExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (ParseException e) {
					LOGGER.error(postInEgBillRegisterExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (TaskFailedException e) {
					LOGGER.error(postInEgBillRegisterExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (SQLException e) {
					LOGGER.error(postInEgBillRegisterExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (Exception e) {
					LOGGER.error(postInEgBillRegisterExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				}

				return 0;

			
				
			}
			
			
		});
	}

	/*
	 * create BillDetails and BillPayeedetails Entry only detailed Code will go
	 * To BillPayee So Tds Also
	 */
	public Set<EgBilldetails> postInBilldetails(final WorksBillForm wbForm,
			final EgBillregister reg) throws EGOVRuntimeException,
			TaskFailedException, SQLException {
		// insertDebits
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Set<EgBilldetails>>() {

			@Override
			public Set<EgBilldetails> execute(Connection conn) throws SQLException {
		Set<EgBilldetails> billDetailsList = new HashSet<EgBilldetails>();
		String advGlcode = EGovConfig.getProperty("egf_config.xml",
				"ConAdvCode", "", "ContractorCodes");
		List netPayList = getNetPayList();
		CChartOfAccounts net_glAcct = null;
		String postInBilldetailsExpMsg = "Exception in postInBilldetails...";

		try {
			String net_glAccStr = wbForm.getNet_chartOfAccounts_glCode();
			String acc[] = net_glAccStr.split("~");
			net_glAcct = commonsService.getCChartOfAccountsByGlCode(acc[1]);
			// LOGGER.debug("The No of Debit records"+wbForm.getDeb_chartOfAccounts_glCode().length);
			for (int i = 0; i < wbForm.getDeb_chartOfAccounts_glCode().length; i++) {
				EgBilldetails egBilldet = new EgBilldetails();
				Set billPayeeSet = new HashSet();
				EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
				if (!wbForm.deb_chartOfAccounts_glCode[i].equals("")) {
					if (wbForm.deb_chartOfAccounts_glCode[i]
							.equalsIgnoreCase(advGlcode)
							|| netPayListString
									.contains(wbForm.deb_chartOfAccounts_glCode[i])) {
						throw new EGOVRuntimeException(
								"Internal Codes Are Used" + advGlcode + " Or "
										+ net_glAcct.getGlcode());
					}
					if (!wbForm.debitAmount[i].equals("")) {
						egBilldet.setEgBillregister(reg);
						egBilldet.setDebitamount(new BigDecimal(
								wbForm.debitAmount[i]));
						CChartOfAccounts cc = (CChartOfAccounts) commonsService
								.getCChartOfAccountsByGlCode(wbForm.deb_chartOfAccounts_glCode[i]);
						Long glcodeid = cc.getId();
						egBilldet.setGlcodeid(new BigDecimal(glcodeid));
						if (wbForm.deb_cv_fromFunctionCodeId[i] != null
								&& !wbForm.deb_cv_fromFunctionCodeId[i]
										.equals("")) {
							egBilldet.setFunctionid(new BigDecimal(
									wbForm.deb_cv_fromFunctionCodeId[i]));
						}
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails Add Record If it is a detailed code
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();

						try {
							detailCode = commonsService
									.getAccountdetailtypeListByGLCode(wbForm.deb_chartOfAccounts_glCode[i]);
						} catch (Exception e) {
							HibernateUtil.rollbackTransaction();
							LOGGER.error(e.getMessage());
						}
						if (detailCode != null && !detailCode.isEmpty()) {
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeedet.setAccountDetailKeyId(Integer
									.valueOf(wbForm.getCSId()));
							String detid_name = commonsService
									.getAccountdetailtypeAttributename(conn,"Creditor");
							String[] ids = detid_name.split("#");
							int id = Integer.parseInt(ids[0]);
							billPayeedet.setAccountDetailTypeId(Integer
									.valueOf(id));
							billPayeedet.setDebitAmount(new BigDecimal(
									wbForm.debitAmount[i]));
							billPayeedet.setLastUpdatedTime(new Date());
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0) {
							egBilldet.setEgBillPaydetailes(billPayeeSet);
						}
						billDetailsList.add(egBilldet);

					}
				}

			}
			// LOGGER.debug("The No of credit records"+wbForm.getDed_chartOfAccounts_glCode().length);
			for (int i = 0; i < wbForm.getDed_chartOfAccounts_glCode().length; i++) {
				EgBilldetails egBilldet = new EgBilldetails();
				Set billPayeeSet = new HashSet();
				EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
				// LOGGER.debug("wbForm.ded_chartOfAccounts_glCode[i]"+wbForm.ded_chartOfAccounts_glCode[i]);
				if (wbForm.ded_chartOfAccounts_glCode[i] != "") {
					if (wbForm.ded_chartOfAccounts_glCode[i]
							.equalsIgnoreCase(advGlcode)
							|| netPayListString
									.contains((wbForm.ded_chartOfAccounts_glCode[i]))) {
						throw new EGOVRuntimeException(
								"Internal Codes Are Used" + advGlcode + " Or "
										+ net_glAcct.getGlcode());
					}
					if (wbForm.creditAmount[i] != "") {
						// LOGGER.debug("wbForm.ded_chartOfAccounts_glCode[i]"+wbForm.ded_chartOfAccounts_glCode[i]);
						// LOGGER.debug("wbForm.creditAmount[i]"+wbForm.creditAmount[i]);
						egBilldet.setEgBillregister(reg);
						egBilldet.setCreditamount(new BigDecimal(
								wbForm.creditAmount[i]));
						CChartOfAccounts cc = (CChartOfAccounts) commonsService
								.getCChartOfAccountsByGlCode(wbForm.ded_chartOfAccounts_glCode[i]);
						Long glcodeid = cc.getId();
						egBilldet.setGlcodeid(new BigDecimal(glcodeid));
						if (wbForm.ded_cv_fromFunctionCodeId[i] != null
								&& !wbForm.ded_cv_fromFunctionCodeId[i]
										.equals("")) {
							egBilldet.setFunctionid(new BigDecimal(
									wbForm.ded_cv_fromFunctionCodeId[i]));
						}
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();
						try {
							detailCode = commonsService
									.getAccountdetailtypeListByGLCode(wbForm.ded_chartOfAccounts_glCode[i]);
						} catch (Exception e) {
							// HibernateUtil.rollbackTransaction();
							LOGGER.error(e.getMessage());
							// HibernateUtil.rollbackTransaction();
							throw new EGOVRuntimeException(
									"Exception in getting the detail code");
						}
						if (null != detailCode && !detailCode.isEmpty()) {
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeedet.setAccountDetailKeyId(Integer
									.valueOf(wbForm.getCSId()));
							String detid_name = commonsService
									.getAccountdetailtypeAttributename(conn,"Creditor");
							String[] ids = detid_name.split("#");
							int id = Integer.parseInt(ids[0]);
							billPayeedet.setAccountDetailTypeId(Integer
									.valueOf(id));
							billPayeedet.setCreditAmount(new BigDecimal(
									wbForm.creditAmount[i]));
							billPayeedet.setLastUpdatedTime(new Date());
							// billPayeedet.setEgBilldetailsId(egBilldet);
							Recovery tds = null;
							if (wbForm.tds_code[i] != null
									&& !wbForm.tds_code[i].equals("0")) {
								String tandglid = wbForm.tds_code[i];
								int loc = tandglid.indexOf('`');
								String tid = tandglid.substring(0, loc);
								// LOGGER.debug("TdsId is "+tid);
								tds = (Recovery) recoveryService
										.getTdsById(Long.valueOf(Long
												.parseLong(tid)));
								// tds=(Tds)
								// HibernateUtil.getCurrentSession().(Tds.class,Integer.valueOf(Integer.parseInt(tid)));
								billPayeedet.setRecovery(tds);
							}
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0) {
							egBilldet.setEgBillPaydetailes(billPayeeSet);
						}
						billDetailsList.add(egBilldet);
					}
				}

			}
			// Advance Payed
			wbForm.net_Amount = wbForm.totalAmount;
			if (wbForm.getAdjustmentAmount() != null) {
				double advAmount = Double.parseDouble(wbForm
						.getAdjustmentAmount());
				if (advAmount > 0.0f) {

					EgBilldetails egBilldet = new EgBilldetails();
					Set billPayeeSet = new HashSet();
					EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
					// String code1=null;
					CChartOfAccounts chartofacct = null;
					// CChartOfAccounts net_chartofacct=null;

					chartofacct = commonsService
							.getCChartOfAccountsByGlCode(advGlcode);
					if (!(chartofacct.getGlcode()).equalsIgnoreCase(net_glAcct
							.getGlcode())) {
						egBilldet.setCreditamount(new BigDecimal(advAmount));
						wbForm.net_Amount = wbForm.totalAmount;// set the
																// net_Amount
																// here
						egBilldet.setEgBillregister(reg);
						egBilldet.setGlcodeid(new BigDecimal(chartofacct
								.getId()));
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();

						try {
							detailCode = commonsService
									.getAccountdetailtypeListByGLCode(chartofacct
											.getGlcode());
						} catch (Exception e) {
							// HibernateUtil.rollbackTransaction();
							LOGGER.error(e.getMessage());
							throw new EGOVRuntimeException(
									"Exception in getting the detail code");
						}
						if (null != detailCode && !detailCode.isEmpty()) {
							billPayeedet.setAccountDetailKeyId(Integer
									.valueOf(wbForm.getCSId()));
							billPayeedet.setAccountDetailTypeId(Integer
									.valueOf(1));
							billPayeedet.setCreditAmount(new BigDecimal(
									advAmount));
							billPayeedet.setLastUpdatedTime(new Date());
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0) {
							egBilldet.setEgBillPaydetailes(billPayeeSet);
						}
						billDetailsList.add(egBilldet);

					} else
						wbForm.net_Amount = new Double(
								Double.parseDouble(wbForm.totalAmount)
										+ Double.parseDouble(wbForm.adjustmentAmount))
								.toString();
				}
			}

			// defaultGrid ie suppler or Contractor payable
			EgBilldetails egBilldet = new EgBilldetails();
			Set billPayeeSet = new HashSet();
			EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
			egBilldet.setCreditamount(new BigDecimal(wbForm.net_Amount));
			egBilldet.setEgBillregister(reg);
			egBilldet.setGlcodeid(new BigDecimal(net_glAcct.getId()));
			if (wbForm.getNet_cv_fromFunctionCodeId() != null
					&& !wbForm.getNet_cv_fromFunctionCodeId().equals("")) {
				egBilldet.setFunctionid(new BigDecimal(wbForm
						.getNet_cv_fromFunctionCodeId()));
			}
			// egBilldet.setLastupdatedtime(new Date());
			// BillPayeeDetails
			// wbForm.net_Amount=wbForm.totalAmount;
			billPayeedet
					.setAccountDetailKeyId(Integer.valueOf(wbForm.getCSId()));
			String detid_name = commonsService.getAccountdetailtypeAttributename(conn,"Creditor");
			String[] ids = detid_name.split("#");
			int id = Integer.parseInt(ids[0]);
			billPayeedet.setAccountDetailTypeId(Integer.valueOf(id));
			billPayeedet.setCreditAmount(new BigDecimal(wbForm.net_Amount));
			billPayeedet.setLastUpdatedTime(new Date());
			billPayeedet.setEgBilldetailsId(egBilldet);
			billPayeeSet.add(billPayeedet);
			egBilldet.setEgBillPaydetailes(billPayeeSet);
			billDetailsList.add(egBilldet);
		} catch (NumberFormatException e) {
			LOGGER.error(postInBilldetailsExpMsg + "" + e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(postInBilldetailsExpMsg + "" + e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}

		return billDetailsList;
			}
		});
	}

	public void postInEGActionDetails(final String moduleType,final String billId,
			final String desc, final int usrId) throws EGOVRuntimeException {
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection conn) throws SQLException {
				
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					String txndate = commonsService.getCurrentDate(conn);
					EgActiondetails egActiondetails = new EgActiondetails();
					User user = (User) userService.getUserByID(Integer.valueOf(usrId));

					// LOGGER.debug(user.getUserName());
					egActiondetails.setActionDoneBy((UserImpl) user);
					egActiondetails.setActionDoneOn(formatter.parse(txndate));
					egActiondetails.setModuleid(Integer.valueOf(Integer
							.parseInt(billId)));
					egActiondetails.setActiontype(desc);
					egActiondetails.setModuletype(moduleType);
					egActiondetails.setCreatedby(Integer.valueOf(usrId));
					commonsService.createEgActiondetails(egActiondetails);
					// LOGGER.debug("Inserted");
				} catch (Exception e) {
					LOGGER.error("Exception While Creating the Bill");
					// HibernateUtil.rollbackTransaction();
					LOGGER.error(e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				}
				
			}
		});

	}

	// load all department based on userid
	public List getAllActiveDepartments() throws EGOVRuntimeException {
		List al = null;
		try {
			// Commented as per Bug #10492 - Changes for refactoring the Commons
			// manager in EGI
			// al=(List) comm.getDepartmentList();
			al = (List) departmentService.getAllDepartments();
		} catch (Exception e) {
			LOGGER.error("Exception Occured While Getting Department List");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return al;
	}

	/*
	 * get Complete Bill With BillRegister BillRegistermis BillDetails
	 * BillPayeeDetails table entries
	 */
	public WorksBillForm getEgBillRegister(WorksBillForm wbForm)
			throws EGOVRuntimeException, Exception {

		try {
			Worksdetail wd = null;
			Integer billRegId = Integer.valueOf(wbForm.getBillId());
			EgBillregister reg = billsService.getBillRegisterById(billRegId);
			EgBillregistermis billmis = (EgBillregistermis) reg
					.getEgBillregistermis();
			if (billmis.getEgDepartment() != null) {
				wbForm.setDept(billmis.getEgDepartment().getId().toString());
			}
			if (billmis.getFunctionaryid() != null) {
				wbForm.setFunctionaryId(billmis.getFunctionaryid().toString());
			}
			wbForm.setMBrefNo(billmis.getMbRefNo());
			wbForm.sanctionNo = billmis.getSanctiondetail();
			wbForm.bill_Amount = reg.getBillamount().toString();
			wbForm.bill_Type = reg.getBilltype();
			wbForm.expenditure_Type = reg.getExpendituretype();
			Date dt = (Date) reg.getBilldate();
			wbForm.billDate = sdf.format(dt);
			// LOGGER.debug(wbForm.billDate);
			wbForm.billNo = reg.getBillnumber();
			wbForm.narration = reg.getNarration();
			wbForm.setBillId(reg.getId().toString());
			BigDecimal advadjd = reg.getAdvanceadjusted();
			double advadjdInt = 0.0d;
			if (advadjd == null) {
				wbForm.adjustmentAmount = "0";
			} else {
				advadjdInt = reg.getAdvanceadjusted().doubleValue();
				wbForm.adjustmentAmount = reg.getAdvanceadjusted().toString();
			}
			BigDecimal passamt = reg.getPassedamount();
			if (passamt != null)
				wbForm.passedAmount = passamt.toString();
			else
				wbForm.passedAmount = "0";
			String wid = reg.getWorksdetailId();
			// LOGGER.debug(wid);
			// WorksBillManager
			// wm=(WorksBillManager)GetEgfManagers.getWorksBillManager();
			if (worksBillService != null) {
				wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(wid));
				wbForm.codeList = wd.getId().toString();
				wbForm.codeName = wd.getCode();
				wbForm.CSId = wd.getRelation().getId().toString();
				wbForm.CSName = wd.getRelation().getName();
				wbForm.worksName = wd.getName();
				wbForm.totalWorkOrder = wd.getTotalvalue().toString();
				wbForm.setWorkOrderDate(wd.getOrderdate().toString());
				BigDecimal passdAmt = wd.getPassedamount();
				if (passdAmt == null) {
					passdAmt = BIGDECIMAL0;
				}
				BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(
						wd).get(0);
				if (totalbillvalue == null) {
					totalbillvalue = BIGDECIMAL0;
				}
				totalbillvalue = totalbillvalue.add(passdAmt);

				totalbillvalue = totalbillvalue.subtract(new BigDecimal(
						wbForm.passedAmount));
				wbForm.setTotalBilledAmount(totalbillvalue.toString());
				// List tdsList=(List)wm.getTdsForWorkOrder(wd);
				// wbForm.setTdsList(tdsList);

				BigDecimal totalAdvAdj = (BigDecimal) worksBillService.getTotalAdvAdj(wd)
						.get(0);
				if (totalAdvAdj == null) {
					totalAdvAdj = BIGDECIMAL0;
				}

				BigDecimal advamt = wd.getAdvanceamount();
				BigDecimal advadj = wd.getAdvanceadj();
				if (advadj == null)
					advadj = new BigDecimal(0);
				advadj = totalAdvAdj.add(advadj);
				advadj = advadj.subtract(reg.getAdvanceadjusted());
				double advamtInt = 0;
				double advadjInt = 0;
				double advance = 0;
				if (advamt != null) {
					advamtInt = advamt.doubleValue() - advadj.doubleValue();
				}
				if (advamtInt < 0)
					advamtInt = 0;
				{
					wbForm.setAdvanceAmount(String.valueOf(advamtInt));
				}
			}
			// ReadDept

			// read ConSup payable
			String netPayableCode = null;
			String advanceCode = null;
			CChartOfAccounts net_chartofacct = null;
			CChartOfAccounts dedchartOfAcc = null;
			// LOGGER.debug("NetPayable Code "+netPayableCode);
			Set billdetailsSet = reg.getEgBilldetailes();
			int len = billdetailsSet.size();
			// LOGGER.debug("Total No Of records "+len);
			Iterator itr = billdetailsSet.iterator();
			// find out how many Records are there then Inialize the array
			int i = 0, j = 0;
			int dbRows = 0, crRows = 0;
			while (itr.hasNext()) {
				EgBilldetails billDetails = (EgBilldetails) itr.next();
				if (billDetails.getDebitamount() != null)
					dbRows++;
				else if (billDetails.getCreditamount() != null)
					crRows++;
			}
			// LOGGER.debug("No of Debit Rows "+dbRows);
			// LOGGER.debug("No of Credit Rows "+crRows);

			wbForm.debitAmount = new String[dbRows];
			wbForm.deb_cv_fromFunctionCodeId = new String[dbRows];
			wbForm.deb_function_code = new String[dbRows];
			wbForm.deb_chartOfAccounts_glCode = new String[dbRows];
			wbForm.deb_chartOfAccounts_name = new String[dbRows];
			if (crRows - 1 > 0) {
				wbForm.creditAmount = new String[crRows - 1];
				wbForm.ded_cv_fromFunctionCodeId = new String[crRows - 1];
				wbForm.ded_function_code = new String[crRows - 1];
				wbForm.ded_chartOfAccounts_glCode = new String[crRows - 1];
				wbForm.ded_chartOfAccounts_name = new String[crRows - 1];
				wbForm.tds_code = new String[crRows - 1];
			} else {
				wbForm.creditAmount = null;
				wbForm.ded_cv_fromFunctionCodeId = null;
				wbForm.ded_function_code = null;
				wbForm.ded_chartOfAccounts_glCode = null;
				wbForm.ded_chartOfAccounts_name = null;
				wbForm.tds_code = null;
			}

			Iterator billdetItr = billdetailsSet.iterator();
			while (billdetItr.hasNext()) {
				EgBilldetails billDetails = (EgBilldetails) billdetItr.next();
				if (billDetails.getDebitamount() != null) {
					wbForm.debitAmount[i] = "" + billDetails.getDebitamount();
					BigDecimal funcId = billDetails.getFunctionid();
					if (funcId != null) {
						Long fid = Long.valueOf(funcId.intValue());
						CFunction cFunction = (CFunction) commonsService
								.getCFunctionById(fid);
						if (cFunction != null) {
							wbForm.deb_cv_fromFunctionCodeId[i] = cFunction
									.getId().toString();
							wbForm.deb_function_code[i] = ""
									+ cFunction.getName();
						}
					} else {
						wbForm.deb_cv_fromFunctionCodeId[i] = "";
						wbForm.deb_function_code[i] = "";
					}
					// load glcode and name
					BigDecimal glCodeid = billDetails.getGlcodeid();
					Long glId = Long.valueOf(glCodeid.longValue());
					CChartOfAccounts chartOfAcc = commonsService
							.getCChartOfAccountsById(glId);
					wbForm.deb_chartOfAccounts_glCode[i] = chartOfAcc
							.getGlcode();
					wbForm.deb_chartOfAccounts_name[i] = chartOfAcc.getName();
					wbForm.debitAmount[i] = "" + billDetails.getDebitamount();
					i++;
				} else {
					// load glcode and name
					BigDecimal glCodeid = billDetails.getGlcodeid();
					Long glId = Long.valueOf(glCodeid.longValue());
					dedchartOfAcc = commonsService
							.getCChartOfAccountsById(glId);
					// check For NetPay Also
					String netGlCode = dedchartOfAcc.getGlcode();
					// LOGGER.debug(netGlCode+""+netPayableCode);
					// for advance Amount
					if (netGlCode.equalsIgnoreCase(advanceCode)) {
						if (!advanceCode.equalsIgnoreCase(netPayableCode))

						{
							// LOGGER.debug("Inside advance Fetch");
							wbForm.ded_chartOfAccounts_glCode[j] = null;
							wbForm.ded_chartOfAccounts_name[j] = null;
							j++;
							continue;
						}

					}

					// // end of advance amount
					List netPayList = getNetPayList();
					if (netPayList.contains(dedchartOfAcc)) {
						// LOGGER.debug("Inside net payable ");
						wbForm.net_Amount = billDetails.getCreditamount()
								.toString();
						wbForm.net_chartOfAccounts_glCode = dedchartOfAcc
								.getId()
								+ "~"
								+ dedchartOfAcc.getGlcode()
								+ "~" + dedchartOfAcc.getName();
						wbForm.net_chartOfAccounts_name = dedchartOfAcc
								.getName();
						wbForm.net_Amount = billDetails.getCreditamount()
								.toString();
					} else if (!netGlCode.equalsIgnoreCase(advanceCode)) {
						// LOGGER.debug("Inside Deductions");
						wbForm.creditAmount[j] = ""
								+ billDetails.getCreditamount();
						// load function name
						BigDecimal funcId = billDetails.getFunctionid();
						if (funcId != null) {
							Long fid = Long.valueOf(funcId.intValue());
							CFunction cFunction = (CFunction) commonsService
									.getCFunctionById(fid);
							if (cFunction != null) {
								wbForm.ded_cv_fromFunctionCodeId[j] = ""
										+ cFunction.getId();
								wbForm.ded_function_code[j] = ""
										+ cFunction.getName();
							}

						} else {
							wbForm.ded_cv_fromFunctionCodeId[j] = "";
							wbForm.ded_function_code[j] = "";
						}

						wbForm.ded_chartOfAccounts_glCode[j] = dedchartOfAcc
								.getGlcode();
						wbForm.ded_chartOfAccounts_name[j] = dedchartOfAcc
								.getName();
						Set payeeList = billDetails.getEgBillPaydetailes();
						if (payeeList != null && !payeeList.isEmpty()) {
							Iterator payeeItr = payeeList.iterator();
							while (payeeItr.hasNext()) {
								EgBillPayeedetails payeeDetails = (EgBillPayeedetails) payeeItr
										.next();
								if (payeeDetails != null) {
									Recovery tdsid = (Recovery) payeeDetails
											.getRecovery();
									// LOGGER.debug("tds"+tdsid);
									if (tdsid != null) {
										if (payeeDetails.getCreditAmount() != null) {
											// Tds
											// tdsid=payeeDetails.getTdsId();
											// LOGGER.debug(tdsid.getType());
											// wbForm.ded_chartOfAccounts_name[j]=tdsid.getType();
											wbForm.tds_code[j] = ""
													+ tdsid.getId()
													+ "`~`"
													+ wbForm.ded_chartOfAccounts_glCode[j];
										}
									}
								}
							}
						} else {
							wbForm.tds_code[j] = "0";
						}

						// LOGGER.debug(wbForm.ded_chartOfAccounts_glCode[j]);
						// LOGGER.debug(wbForm.ded_chartOfAccounts_name[j]);
						// LOGGER.debug(wbForm.tds_code[j]);
						// LOGGER.debug(wbForm.creditAmount[j]+"      "+i);
						j++;
					}
				}

			}
			if (wd.getWorkcategory() != null) {
				wbForm.setWorkType(wd.getWorkcategory().toString());
			}
			if (wd.getSubcategory() != null) {
				wbForm.setWorkSubType(wd.getSubcategory().toString());
			}
			if (wd.getRelation().getRelationtype() != null) {
				wbForm.setTdsList(recoveryService.getAllTdsByPartyType(wd
						.getRelation().getRelationtype().getName()));
			}
			List l = wbForm.getTdsList();
			for (int k = 0; k < l.size(); k++) {
				Recovery t = (Recovery) l.get(k);
				if (t.getIsEarning() != null) {
					if (t.getIsEarning().equalsIgnoreCase("1")) {
						l.remove(k);
						continue;
					}
				}
				LOGGER.info(t.getType() + ":   TDS   :" + t.getId());
			}
			wbForm.setTdsList(l);

		} catch (Exception e) {
			LOGGER.error("Error occured while getting the records");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return wbForm;
	}

	public WorksBillForm getTds(WorksBillForm wbForm)
			throws EGOVRuntimeException {
		List tdsList = null;
		WorksBillService worksBillService = null;
		String getTdsExpMsg = "Exception in getTds...";

		try {
			worksBillService = (WorksBillService) GetEgfManagers.getWorksBillService();
			String worksdtlId = wbForm.getCodeList();
			Worksdetail wd = worksBillService.getWorksDetailById(Integer.valueOf(worksdtlId));
			wbForm.setCSId(wd.getRelation().getId().toString());
			wbForm.setCSName(wd.getRelation().getName().toString());
			wbForm.setTotalWorkOrder(wd.getTotalvalue().toString());
			BigDecimal passdAmt = wd.getPassedamount();
			wbForm.setWorkOrderDate(wd.getOrderdate().toString());
			if (passdAmt == null) {
				passdAmt = BIGDECIMAL0;
			}
			BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(wd)
					.get(0);
			if (totalbillvalue == null)
				totalbillvalue = BIGDECIMAL0;
			totalbillvalue = totalbillvalue.add(passdAmt);
			wbForm.setTotalBilledAmount(totalbillvalue.toString());
			if (null != wd.getExecdeptid()) {
				LOGGER.debug("The department id for this work order---->>>>>"
						+ wd.getExecdeptid());
				wbForm.setDept(wd.getExecdeptid().toString());
			}

			// LOGGER.debug(wbForm.getCSId());
			// LOGGER.debug(wbForm.getCSName());
			BigDecimal totalAdvAdj = (BigDecimal) worksBillService.getTotalAdvAdj(wd).get(0);
			if (totalAdvAdj == null)
				totalAdvAdj = BIGDECIMAL0;
			BigDecimal advamt = wd.getAdvanceamount();
			BigDecimal advadj = wd.getAdvanceadj();
			if (advadj == null) {
				advadj = BIGDECIMAL0;
			}
			advadj = totalAdvAdj.add(advadj);
			double advamtInt = 0;
			double advadjInt = 0;
			double advance = 0;
			if (advamt != null) {
				advamtInt = advamt.doubleValue();
			}
			if (advadj != null) {
				advadjInt = advadj.doubleValue();
			}
			advance = advamtInt - advadjInt;
			wbForm.setAdvanceAmount("" + advance);
			if (wd.getWorkcategory() != null) {
				wbForm.setWorkType(wd.getWorkcategory().toString());
			}
			if (wd.getSubcategory() != null) {
				wbForm.setWorkSubType(wd.getSubcategory().toString());
			}

			if (wd.getRelation().getRelationtype() != null) {
				tdsList = recoveryService.getAllTdsByPartyType(wd.getRelation()
						.getRelationtype().getName());
			}
			if (tdsList != null) {

				for (int k = 0; k < tdsList.size(); k++) {
					Recovery t = (Recovery) tdsList.get(k);
					if (t.getIsEarning() != null) {
						if (t.getIsEarning().equalsIgnoreCase("1")) {
							tdsList.remove(k);
							continue;
						}
					}
					LOGGER.info(t.getType() + ":   TDS   :" + t.getId());
				}
				wbForm.setTdsList(tdsList);
			}
		} catch (NumberFormatException e1) {
			LOGGER.error(getTdsExpMsg + "" + e1.getMessage());
			throw new EGOVRuntimeException(e1.getMessage());
		}

		return wbForm;
	}

	public Integer modify(final WorksBillForm wbForm) throws EGOVRuntimeException,
			EGOVException {
		
		return HibernateUtil.getCurrentSession().doReturningWork( new ReturningWork<Integer>() {

			@Override
			public Integer execute(Connection conn) throws SQLException {
				

				EgBillregister reg = null;

				String billregId = wbForm.getBillId();
				reg = billsService.getBillRegisterById(Integer.valueOf(billregId));
				String modifyExpMsg = "Exception in modify ...";

				try {
					Date bill_Date = sdf.parse(wbForm.getBillDate());
					reg.setBilldate(bill_Date);
					reg.setBillamount(new BigDecimal(wbForm.getBill_Amount()));
					reg.setPassedamount(new BigDecimal(wbForm.getPassedAmount()));
					reg.setBillstatus("PENDING");
					reg.setBilltype(wbForm.getBill_Type());
					reg.setExpendituretype(wbForm.getExpenditure_Type());
					reg.setNarration(wbForm.getNarration());
					EgwStatus invStatus = null;
					EGovernCommon cm = new EGovernCommon();
					
					// invStatus = comm.findEgwStatusById(33);
					invStatus = commonsService.findEgwStatusById(Integer.valueOf(cm
							.getEGWStatusId(conn, "WORKSBILL", "Pending")));
					reg.setStatus(invStatus);
					reg.setBillnumber(wbForm.getBillNo());
					reg.setCreatedBy(userService.getUserByID(wbForm.getUserId()));
					Date crntDate = new Date();
					reg.setCreatedDate(crntDate);
					if (wbForm.getAdjustmentAmount() != null
							&& !wbForm.getAdjustmentAmount().equalsIgnoreCase("0")) {
						reg.setAdvanceadjusted(new BigDecimal(wbForm
								.getAdjustmentAmount()));
					}

					reg.setWorksdetailId(wbForm.getCodeList());
					EgBillregistermis egmis = new EgBillregistermis();
					egmis.setMbRefNo(wbForm.getMBrefNo());
					if (!wbForm.getDept().equalsIgnoreCase("0")) {
						DepartmentImpl egDept = (DepartmentImpl) departmentService
								.getDepartment(Integer.valueOf(Integer.parseInt(wbForm
										.getDept())));
						egmis.setEgDepartment(egDept);
					} else {
						egmis.setEgDepartment(null);
					}
					// LOGGER.info("functionary :"+wbForm.getFunctionaryId());
					if (wbForm.getFunctionaryId() != null
							&& !wbForm.getFunctionaryId().equals("0")) {
						Functionary functionary = commonsService
								.getFunctionaryById(Integer.valueOf(wbForm
										.getFunctionaryId()));
						if (functionary != null)
							egmis.setFunctionaryid(functionary);
					} else { // LOGGER.info("functionary null :"+wbForm.getFunctionaryId());
						egmis.setFunctionaryid(null);
					}
					CFinancialYear financialyear = commonsService
							.getFinancialYearByFinYearRange(wbForm.getBillDate());
					egmis.setFinancialyear(financialyear);
					// end of Checking
					Set<EgBilldetails> EgBillSet = reg.getEgBilldetailes();
					// LOGGER.debug(HibernateUtil.getCurrentSession().getFlushMode());
					Iterator billDetItr = EgBillSet.iterator();
					int i = 0;
					EgBilldetails billDet = null;
					for (; billDetItr.hasNext();) {
						try {
							billDet = (EgBilldetails) billDetItr.next();
							// LOGGER.debug("  billDet "+ billDet.getId());
							billDetItr.remove();
						} catch (Exception e) {
							HibernateUtil.rollbackTransaction();
							LOGGER.error(e.getMessage());
						}
					}

					reg.setEgBilldetailes(EgBillSet);
					Set<EgBilldetails> EgBillSet1 = new HashSet();
					EgBillSet1.addAll(postInBilldetails(wbForm, reg));
					EgBillSet.addAll(EgBillSet1);
					HibernateUtil.getCurrentSession().flush();
				} catch (NumberFormatException e) {
					LOGGER.error(modifyExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (HibernateException e) {
					LOGGER.error(modifyExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				}  catch (ParseException e) {
					LOGGER.error(modifyExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (TaskFailedException e) {
					LOGGER.error(modifyExpMsg + "" + e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				} catch (SQLException e) {
					LOGGER.error(e.getMessage());
					throw new EGOVRuntimeException(modifyExpMsg + "" + e.getMessage());
				}catch (Exception e) {
					LOGGER.error(e.getMessage());
					throw new EGOVRuntimeException(modifyExpMsg + "" + e.getMessage());
				}
				return 0;
			
				
				
			}
		});
		
		
	}

	public EgBillregister gnerateVoucher(final int billId,final int usrId,
			final String voucherHeaderNarration, final String approvalDate)
			throws EGOVRuntimeException, SQLException {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<EgBillregister>() {

			@Override
			public EgBillregister execute(Connection conn) throws SQLException {
				


				EGovernCommon cm = new EGovernCommon();
				
				LOGGER.debug(" ---------------Generating Voucher");
				EgBillregister reg = null;
				try {
					if (transactions.size() > 0)
						transactions.clear();

					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					Date dt = new Date();
					String vdt = null;
					String voucherHeader_voucherDate = null;
					if (null != approvalDate && approvalDate.trim().length() != 0) {
						vdt = formatter.format(new SimpleDateFormat("dd/MM/yyyy")
								.parse(approvalDate));
						voucherHeader_voucherDate = approvalDate;
					} else {
						vdt = formatter.format(dt);
						voucherHeader_voucherDate = sdf.format(dt);
					}
					String fiscalPeriod = null;
					// CFinancialYear
					// financialyear=comm.getFinancialYearByFinYearRange(voucherHeader_voucherDate);
					fiscalPeriod = cm.getFiscalPeriod(vdt);
					if (null == fiscalPeriod) {
						throw new RBACException(
								"Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := ");
					}
					// LOGGER.debug("fiscalPeriod"+fiscalPeriod);
					HibernateUtil.getCurrentSession().flush();
					reg = billsService.getBillRegisterById(Integer.valueOf(billId));
					if (null == reg) {
						throw new RBACException(
								"There is no EgBillregister object exists for the given billId: "
										+ billId);
					}
					String wdId = reg.getWorksdetailId();
					Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer
							.valueOf(Integer.parseInt(wdId)));
					String fId = null;
					if (wd.getFund() != null) {
						fId = wd.getFund().getId().toString();
					}
					// LOGGER.debug("fiscalPeriodId"+fiscalPeriod);
					String voucherNumber = cm.vNumber(
							FinancialConstants.WORKSBILL_VOUCHERNO_TYPE, conn, fId,
							reg.getBillnumber());
					LOGGER.debug("voucherNumber" + voucherNumber);
					String vType = voucherNumber.substring(0,
							Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
					LOGGER.debug("vType" + vType);
					String eg_voucher = cm.getEg_Voucher(vType, fiscalPeriod, conn);
					LOGGER.debug("eg_voucher" + eg_voucher);

					for (int i = eg_voucher.length(); i < 5; i++) {
						eg_voucher = "0" + eg_voucher;
					}
					String cgNum = vType + eg_voucher;
					// LOGGER.debug("Inside Auto Mode");
					String vNumRptsql = "select  count(*) from voucherheader where vouchernumber like '"
							+ voucherNumber + "%'";
					Statement vNumRptst = conn.createStatement();
					ResultSet vNumRptrs = vNumRptst.executeQuery(vNumRptsql);
					int noOftimesVoucherCreated = 0;
					if (vNumRptrs.next()) {
						noOftimesVoucherCreated = vNumRptrs.getInt(1);
					}
					if (vNumRptrs != null) {
						vNumRptrs.close();
					}
				

					if (noOftimesVoucherCreated != 0) {
						voucherNumber = voucherNumber + "/" + (noOftimesVoucherCreated);
					}
					LOGGER.debug("VoucherNumber" + voucherNumber);
					// UniqueCheckinng
					if (!cm.isUniqueVN(voucherNumber, vdt, conn))
						throw new EGOVRuntimeException("Duplicate Voucher Number");
					VoucherHeader vh = new VoucherHeader();
					vh.setVoucherNumber(voucherNumber);
					vh.setFundId(fId);
					String fsrc = null;
					if (wd.getFundsource() != null)
						fsrc = wd.getFundsource().getId().toString();
					vh.setFundSourceId(fsrc);
					vh.setVoucherDate(vdt);
					// LOGGER.debug(vdt);
					vh.setCgvn(cgNum);
					vh.setCgDate(formatter.format(new Date()));
					vh.setDescription(voucherHeaderNarration);
					vh.setCreatedby(String.valueOf(usrId));
					Integer dept = null;
					if (reg.getEgBillregistermis().getEgDepartment() != null) {
						dept = reg.getEgBillregistermis().getEgDepartment().getId();
					}
					if (dept != null) {
						vh.setDepartmentId(dept.toString());
					} else if (null == dept
							&& EGovConfig.getProperty("egf_config.xml", "deptRequired",
									"", "general").equalsIgnoreCase("Y")) {
						throw new RBACException("Department value is missing");
					}
					vh.setFiscalPeriodId(fiscalPeriod);
					if (reg.getExpendituretype().equalsIgnoreCase("Works")) {
						String cgn = "CJV" + cm.getCGNumber();
						vh.setCgn(cgn);
						vh.setName("Contractor Journal");
					}
					vh.setType("Journal Voucher");
					vh.insert(conn);
					int vid = vh.getId();
					LOGGER.debug("voucherheaderid" + vid);
					Long voucherheaderid = Long.valueOf(vid);
					updateVoucherMIS(
							voucherHeader_voucherDate,
							reg,
							conn,
							vid,
							"/EGF/HTML/VMC/ContractorJournal_VMC.jsp?cgNumber="
									+ vh.getCgn() + "&showMode=view");
					transactions = postInVoucherDetail(reg, transactions, conn,
							voucherheaderid);
					// postInAssetStatus(wbForm);
					String passedStatusId = cm.getEGWStatusId(conn, "WORKSBILL",
							"Passed");
					postInContractorBillDetail(reg, voucherheaderid, wd);
					cm.UpdateVoucherStatus("Contractor Journal ", conn, vid, usrId);// change
																					// here
					// postInEGWStatusChange(String.valueOf(billId),"33","34",conn,usrId,"WORKSBILL");
					postInEGWStatusChange(String.valueOf(billId),
							cm.getEGWStatusId(conn, "WORKSBILL", "Pending"),
							passedStatusId, conn, usrId, "WORKSBILL");
					insertEGActionDetails("WORKSBILL", String.valueOf(billId),
							"Voucher Created ", conn, usrId);
					// EgwStatus sts=comm.findEgwStatusById(34);
					EgwStatus sts = commonsService.findEgwStatusById(Integer
							.valueOf(passedStatusId));

					reg.setStatus(sts);

					postInWorksDetail(reg, wd);
					ChartOfAccounts engine = ChartOfAccounts.getInstance();
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = (Transaxtion[]) transactions.toArray(txnList);
					if (!engine.postTransaxtions(txnList, conn, vdt)) {
						throw new EGOVRuntimeException("Engine Validation failed");
					}
					reg.setBillstatus("PASSED");
					reg.getEgBillregistermis().setVoucherHeader(
							commonsService.findVoucherHeaderById(voucherheaderid));
					billsService.updateBillRegister(reg);
				} catch (Exception e) {
					// HibernateUtil.rollbackTransaction();
					LOGGER.error(e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				}
				return reg;

			
				
			}
		});
		
	}

	private void postInWorksDetail(EgBillregister reg, Worksdetail wd)
			throws SQLException, TaskFailedException {
		try {
			BigDecimal oldPassAmt = wd.getPassedamount();
			// BigDecimal oldAdvAmt=wd.getAdvanceamount();
			BigDecimal oldAdvAdj = wd.getAdvanceadj();
			oldPassAmt = oldPassAmt.add(reg.getPassedamount());
			/*
			 * if(reg.getAdvanceadjusted()!=null)
			 * oldAdvAmt=oldAdvAmt.subtract(reg.getAdvanceadjusted());
			 */
			if (reg.getAdvanceadjusted() != null && oldAdvAdj != null)
				oldAdvAdj = reg.getAdvanceadjusted().add(oldAdvAdj);
			else if (reg.getAdvanceadjusted() != null)
				oldAdvAdj = reg.getAdvanceadjusted();
			else
				oldAdvAdj = BigDecimal.ZERO;
			wd.setPassedamount(oldPassAmt);
			wd.setAdvanceadj(oldAdvAdj);
			// wd.setAdvanceamount();
			worksBillService.updateWorksdetail(wd);
		} catch (Exception e) {
			LOGGER.error("error while Updating worksdetail");
			HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
		}
	}

	private void postInContractorBillDetail(EgBillregister reg, Long vhid,
			Worksdetail wd) throws EGOVRuntimeException {

		try {
			Contractorbilldetail cbd = new Contractorbilldetail();
			cbd.setWorksdetail(wd);
			Relation relation = wd.getRelation();
			cbd.setRelation(relation);
			cbd.setBillamount(reg.getBillamount());
			cbd.setPassedamount(reg.getPassedamount());
			cbd.setBillnumber(reg.getBillnumber());
			cbd.setAdvadjamt(reg.getAdvanceadjusted());
			cbd.setTdsamount(new BigDecimal(totaltdsAmount));
			cbd.setOtherrecoveries(new BigDecimal(totalotherRecoveries));
			cbd.setEgBillregister(reg);
			cbd.setBilldate(reg.getBilldate());
			// LOGGER.debug("999999999999999999999999999otherRecoveries"+totalotherRecoveries);

			cbd.setVoucherHeaderId(vhid);
			worksBillService.createContractorbilldetail(cbd);
		} catch (Exception e) {
			LOGGER.error("Error While Inserting Into SupplierBillDeatil");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}

	}

	private void postInSupplierBillDetail(EgBillregister reg, Long vhid,
			Worksdetail wd) throws EGOVRuntimeException {

		try {
			Supplierbilldetail sbd = new Supplierbilldetail();
			sbd.setWorksdetailid(wd.getId());
			Relation relation = wd.getRelation();
			sbd.setRelation(relation);
			sbd.setBillamount(reg.getBillamount());
			sbd.setPassedamount(reg.getPassedamount());
			sbd.setBillnumber(reg.getBillnumber());
			sbd.setAdvadjamt(reg.getAdvanceadjusted());
			sbd.setTdsamount(new BigDecimal(totaltdsAmount));
			// LOGGER.debug(totalotherRecoveries);
			// LOGGER.debug(totaltdsAmount);
			sbd.setOtherrecoveries(new BigDecimal(totalotherRecoveries));

			sbd.setEgBillregister(reg);
			sbd.setBilldate(reg.getBilldate());
			CVoucherHeader voucherheader = (CVoucherHeader) commonsService
					.findVoucherHeaderById(vhid);
			sbd.setVoucherheader(voucherheader);
			worksBillService.createSupplierbilldetail(sbd);
			// TODO Auto-generated method stub
		} catch (Exception e) {
			LOGGER.error("Error While Inserting Into SupplierBillDeatil");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
	}

	private ArrayList postInVoucherDetail(EgBillregister reg,
			ArrayList transactions, Connection conn, Long vhid)
			throws EGOVRuntimeException {
		try {
			double otherRecoveries = 0.0d, tdsAmount = 0.0d;
			Set billdetSet = reg.getEgBilldetailes();
			Iterator billIter = billdetSet.iterator();
			int lineId = 1;
			String net_glcode = "0";
			String adv_glcode = "0";
			adv_glcode = EGovConfig.getProperty("egf_config.xml", "ConAdvCode",
					"", "ContractorCodes");
			while (billIter.hasNext()) {
				EgBilldetails billdet = (EgBilldetails) billIter.next();
				VoucherDetail vd = new VoucherDetail();
				BigDecimal glcodeid = billdet.getGlcodeid();
				Long glid = Long.valueOf(glcodeid.intValue());
				CChartOfAccounts chartOfAcc = commonsService
						.getCChartOfAccountsById(glid);
				vd.setGLCode(chartOfAcc.getGlcode());
				vd.setAccountName(chartOfAcc.getName());
				if (billdet.getCreditamount() != null) {
					vd.setCreditAmount(billdet.getCreditamount().toString());
					vd.setDebitAmount("0");
				} else {
					vd.setDebitAmount(billdet.getDebitamount().toString());
					vd.setCreditAmount("0");
				}
				vd.setVoucherHeaderID(vhid.toString());
				vd.setLineID(String.valueOf(lineId));
				// DetailEntry
				vd.insert(conn);
				lineId++;
				Transaxtion transaction = new Transaxtion();
				transaction.setGlCode(chartOfAcc.getGlcode());
				transaction.setGlName(chartOfAcc.getName());
				transaction.setVoucherLineId(String.valueOf(vd.getId()));
				transaction.setVoucherHeaderId(vhid.toString());
				// transaction.setVoucherDetailId(""+vd.getId());
				if (billdet.getCreditamount() != null) {
					transaction.setCrAmount(billdet.getCreditamount()
							.toString());
					transaction.setDrAmount("0");
					// LOGGER.debug("-------------------------otherRecoveries"+billdet.getGlcodeid()+"         "+billdet.getCreditamount()+"         "+otherRecoveries);
					if (!netPayListString.contains(chartOfAcc.getGlcode())
							&& !chartOfAcc.getGlcode().equalsIgnoreCase(
									adv_glcode))
						otherRecoveries = otherRecoveries
								+ billdet.getCreditamount().doubleValue();
				} else {
					transaction
							.setDrAmount(billdet.getDebitamount().toString());
					transaction.setCrAmount("0");
				}
				Set eGbillpaSet = billdet.getEgBillPaydetailes();
				Iterator payeeItr = eGbillpaSet.iterator();
				ArrayList reqParams = new ArrayList();
				while (payeeItr.hasNext()) {

					EgBillPayeedetails payeedet = (EgBillPayeedetails) payeeItr
							.next();
					TransaxtionParameter reqData = new TransaxtionParameter();
					reqData.setDetailKey(payeedet.getAccountDetailKeyId()
							.toString());
					Integer adtInt = payeedet.getAccountDetailTypeId();
					// LOGGER.debug(adtInt);
					Integer detailTypeId = 1;
					detailTypeId = adtInt;
					reqData.setDetailName("relation_id");
					reqData.setGlcodeId(glcodeid.toString());
					reqData.setDetailTypeId(detailTypeId.toString());
					if (payeedet.getCreditAmount() != null) {
						reqData.setDetailAmt(payeedet.getCreditAmount()
								.toString());
						if (payeedet.getRecovery() != null) {
							if (!chartOfAcc.getGlcode().equalsIgnoreCase(
									net_glcode))
								reqData.setTdsId(payeedet.getRecovery().getId()
										.toString());
							tdsAmount = +payeedet.getCreditAmount()
									.floatValue();
							// LOGGER.debug("Deducting");
							otherRecoveries = -payeedet.getCreditAmount()
									.floatValue();
						}
					} else {
						reqData.setDetailAmt(payeedet.getDebitAmount()
								.toString());
					}
					if (payeedet.getRecovery() != null) {
						reqData.setTdsId(payeedet.getRecovery().getId()
								.toString());
					}
					reqParams.add(reqData);
					// LOGGER.debug(reqData.getDetailAmt());
				}
				if (reqParams != null && reqParams.size() > 0) {
					transaction.setTransaxtionParam(reqParams);
				}
				transactions.add(transaction);
			}

			totalotherRecoveries = otherRecoveries;
			totaltdsAmount = tdsAmount;

		} catch (EGOVRuntimeException e) {
			LOGGER.error("Error while Inserting vocherDetail");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		} catch (SQLException e) {
			LOGGER.error("Error while Inserting vocherDetail");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		} catch (TaskFailedException e) {
			LOGGER.error("Error while Inserting vocherDetail");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}

		return transactions;
	}

	private void updateVoucherMIS(String voucherHeader_voucherDate,
			EgBillregister reg, Connection conn, int vid, String sourcepath)
			throws EGOVRuntimeException {
		try {
			String wdId = reg.getWorksdetailId();

			Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer
					.valueOf(wdId));
			EGovernCommon cm = new EGovernCommon();
			EgwWorksMis worksmis = worksBillService.findByWorkOrderId(wdId);
			VoucherMIS misobj = new VoucherMIS();
			if (worksmis != null) {
				// LOGGER.debug("Before Scheme Validation");
				if (worksmis.getSchemeId() != null)
					cm.validateScheme(voucherHeader_voucherDate, worksmis
							.getSchemeId().toString(), conn);
				// LOGGER.debug("Before Scheme Validation1");
				if (worksmis.getSubSchemeId() != null)
					cm.validatesubScheme(voucherHeader_voucherDate, worksmis
							.getSubSchemeId().toString(), conn);
				LOGGER.debug("Inside the VoucherMIS update Fn()..");
				if (worksmis.getSchemeId() != null)
					misobj.setScheme(worksmis.getSchemeId().toString());
				if (worksmis.getSubSchemeId() != null)
					misobj.setSubscheme(worksmis.getSubSchemeId().toString());

			}
			if (reg.getEgBillregistermis().getFunctionaryid() != null) {
				LOGGER.debug("Functionary Id is :"
						+ reg.getEgBillregistermis().getFunctionaryid().getId());
				misobj.setFunctionary(reg.getEgBillregistermis()
						.getFunctionaryid().getId().toString());
			}

			String strVHID = String.valueOf(vid);
			misobj.setVoucherheaderid(strVHID);
			if (wd.getFundsource() != null)
				misobj.setFundsourceid((String) wd.getFundsource().getId()
						.toString());
			if (reg.getEgBillregistermis().getEgDepartment() != null)
				misobj.setDepartmentId(reg.getEgBillregistermis()
						.getEgDepartment().getId().toString());
			misobj.setSourcePath(sourcepath);
			misobj.insert(conn);

		} catch (Exception e) {

			LOGGER.error("Error While Inserting into Vouchermis");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}

	}

	public boolean postInEGWStatusChange(String abillId, String afromstatus,
			String atostatus, Connection connection, int usrid,
			String moduleType) throws TaskFailedException, EGOVRuntimeException {
		LOGGER.debug("Inside postInEGWStatusChange");
		boolean result = false;
		EGovernCommon cm = new EGovernCommon();

		EgfStatusChange egfstatus = new EgfStatusChange();
		String todayTime = cm.getCurrentDateTime(connection);

		egfstatus.setModuletype(moduleType);
		egfstatus.setModuleid(abillId);
		egfstatus.setFromstatus(afromstatus);
		egfstatus.setTostatus(atostatus);

		egfstatus.setCreatedby(String.valueOf(usrid));
		egfstatus.setLastmodifieddate(cm.getSQLDateTimeFormat(todayTime));

		try {
			egfstatus.insert(connection);
			result = true;
		} catch (Exception e) {
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		return result;
	}

	public boolean insertEGActionDetails(String amoduleType, String abillId,
			String aType, Connection connection, int usrid)
			throws TaskFailedException, EGOVRuntimeException {
		LOGGER.debug("Inside postInEGActionDetails");
		boolean result = false;
		EGovernCommon cm = new EGovernCommon();
		ActionDetails acdetail = new ActionDetails();
		String todayTime = cm.getCurrentDateTime(connection);

		acdetail.setModuletype(amoduleType);
		acdetail.setModuleid(abillId);
		acdetail.setActionDoneBy(String.valueOf(usrid));

		acdetail.setActionDoneOn(cm.getSQLDateTimeFormat(todayTime));
		acdetail.setLastmodifieddate(cm.getSQLDateTimeFormat(todayTime));

		acdetail.setActionType(aType);
		acdetail.setCreatedby(String.valueOf(usrid));
		try {
			acdetail.insert(connection);
			result = true;
		} catch (Exception e) {
			LOGGER.error("Error While Updating Eg_ActionDeatail ");
			throw new EGOVRuntimeException(e.getMessage());
		}
		return result;
	}

	public List<EgBillregister> generateVoucher(
			List<WorksBillForm> approvedBills, String userId) throws Exception {
		List<EgBillregister> billRegisters = new ArrayList();
		for (WorksBillForm wbForm : approvedBills) {
			int billId = Integer.parseInt(wbForm.getBillId());
			String voucherHeaderNarraion = "";
			int usrId = Integer.parseInt(userId);
			try {

				EgBillregister reg = gnerateVoucher(billId, usrId,
						voucherHeaderNarraion, wbForm.getBillAprvalDate());
				billRegisters.add(reg);
			} catch (Exception e) {
				LOGGER.error("Error while Generating Voucher");
				throw new EGOVRuntimeException(e.getMessage());

			}
		}
		return billRegisters;
	}

	public WorksBillForm getWorksBillById(Integer billId) {

		WorksBillForm wbForm = new WorksBillForm();
		wbForm.setBillId(billId.toString());
		try {
			wbForm = getEgBillRegister(wbForm);
		} catch (Exception e) {
			LOGGER.error("Error while Generating Voucher");
			LOGGER.debug("Error", e);
		}
		return wbForm;
	}

	public void createWorkFlow(WorksBillForm wbForm) throws Exception {
		LOGGER.debug(" WORKSBILL  Creating Workflow----> ");
		try {
			HashMap infrastructureHashMap = new HashMap();
			HashMap applContextInstanceHashMap = new HashMap();
			HashMap applExecutionContextHashMap = new HashMap();
			infrastructureHashMap
					.put("xmlResourceName",
							"org/egov/billsaccounting/workflow/processdefinations/Works.xml");
			infrastructureHashMap.put("processDefinitionName", "Works");
			applContextInstanceHashMap.put("userId",
					String.valueOf(wbForm.getUserId()));
			applContextInstanceHashMap.put("userName", wbForm.getUserName());
			applContextInstanceHashMap.put("objPK", wbForm.getBillId());
			applContextInstanceHashMap.put("objIdentifier", wbForm.getCSId());

			applExecutionContextHashMap.put("userId",
					String.valueOf(wbForm.getUserId()));
			applExecutionContextHashMap.put("billId", wbForm.getBillId());
			applExecutionContextHashMap.put("objectType", "WorksBill");
			LOGGER.debug(" WorksBill  Work Flow End ---> ");
		} catch (Exception e) {
			LOGGER.error("Error while Creating work Flow ");
			// HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException("WorkFlow Creation Exception");
		}
	}

	public List<EgBillregister> cancelBill(List<WorksBillForm> rejectedBills,
			String userId) throws EGOVRuntimeException {

		List<EgBillregister> billRegisters = new ArrayList();
		for (WorksBillForm wbForm : rejectedBills) {
			int billId = Integer.parseInt(wbForm.getBillId());

			int usrId = Integer.parseInt(userId);
			try {
				EgBillregister reg = (EgBillregister) billsService
						.getBillRegisterById(Integer.valueOf(billId));
				if (usrId == reg.getCreatedBy().getId()) {
					EgBillregister reg1 = cancel(reg, usrId);
					billRegisters.add(reg1);
				}
			} catch (Exception e) {
				LOGGER.error("Error while Cancelling Bills");
				throw new EGOVRuntimeException(e.getMessage());
			}
		}
		return billRegisters;

	}

	private EgBillregister cancel(final EgBillregister billReg,final int usrId)
			throws EGOVRuntimeException {

		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<EgBillregister>() {

			@Override
			public EgBillregister execute(Connection conn) throws SQLException {
				
				try {
					EGovernCommon cm = new EGovernCommon();
					String cancelledStatusId = cm.getEGWStatusId(conn, "WORKSBILL",
							"Cancelled");
					// postInEGWStatusChange(String.valueOf(billReg.getId().intValue()),"33","35",conn,usrId,"WORKSBILL");
					postInEGWStatusChange(String.valueOf(billReg.getId().intValue()),
							cm.getEGWStatusId(conn, "WORKSBILL", "Pending"),
							cancelledStatusId, conn, usrId, "WORKSBILL");
					insertEGActionDetails("WORKSBILL",
							String.valueOf(billReg.getId().intValue()),
							"Bill Cancelled", conn, usrId);
					// EgwStatus sts=comm.findEgwStatusById(35);
					EgwStatus sts = commonsService.findEgwStatusById(Integer
							.valueOf(cancelledStatusId));
					billReg.setStatus(sts);
					billReg.setBillstatus("Cancelled");

				} catch (Exception e) {
					// HibernateUtil.rollbackTransaction();
					LOGGER.error(e.getMessage());
					throw new EGOVRuntimeException(e.getMessage());
				}

				return billReg;

			}
		});
		
		
	}

	
	public List getNetPayList() {
		String netPayPurpose = EGovConfig.getProperty("egf_config.xml",
				"worksBillPurposeIds", "", "defaultValues");
		// TODO Auto-generated method stub
		String[] netPayPurposeArr = netPayPurpose.split(",");
		CChartOfAccounts netPayAcc;
		netPayListString = new ArrayList();
		ArrayList netPayList = new ArrayList();
		try {
			for (int i = 0; i < netPayPurposeArr.length; i++) {
				if (netPayPurposeArr[i] != null) {
					int purposeId = Integer.parseInt(netPayPurposeArr[i]);
					netPayAcc = commonsService.getAccountCodeByPurpose(
							purposeId).get(0);
					netPayListString.add(netPayAcc.getGlcode());
					LOGGER.info("GlCode is" + netPayAcc.getGlcode());
					netPayList.add(netPayAcc);
				}
			}
		} catch (Exception e) {

		}

		return netPayList;
	}
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	public void setBillsService(BillsService billsService) {
		this.billsService = billsService;
	}

}
