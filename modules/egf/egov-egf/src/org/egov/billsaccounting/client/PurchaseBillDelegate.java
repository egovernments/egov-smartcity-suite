package org.egov.billsaccounting.client;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.RBACException;
import org.egov.billsaccounting.dao.BillsAccountingDAOFactory;
import org.egov.billsaccounting.dao.EgwWorksMisHibernateDAO;
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

public class PurchaseBillDelegate {
	private CommonsService commonsService;
	private UserService userService;
	private DepartmentService departmentService;  
	private BillsService billsService;
	double totaltdsAmount = 0.0d;
	double totalotherRecoveries = 0.0d;
	private  WorksBillService worksBillService = (WorksBillService) GetEgfManagers.getWorksBillService();
	Timestamp curDate = new Timestamp(System.currentTimeMillis());
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	NumberFormat nf = new DecimalFormat("##############.00");
	List transactions = new ArrayList();
	List netPayListString = null;
	private RecoveryService recoveryService;
	private EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao;
	private TdsHibernateDAO tdsHibernateDAO;
	
	public PurchaseBillDelegate() {
		try {
			recoveryService = new RecoveryService();
			recoveryService.setSessionFactory(new SessionFactory());
			egDeductionDetHibernateDao = RecoveryDAOFactory.getDAOFactory().getEgDeductionDetailsDAO();
			tdsHibernateDAO = RecoveryDAOFactory.getDAOFactory().getTdsDAO();
			recoveryService.setEgDeductionDetHibernateDao(egDeductionDetHibernateDao);
			recoveryService.setTdsHibernateDAO(tdsHibernateDAO);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			
		}

	}

	public final static Logger LOGGER = Logger.getLogger(PurchaseBillDelegate.class);

	/*
	 * create billRegister add data to billRegister billRegistermis billDetails
	 * billPayeedetails Complete BillCreation
	 */
	public Integer postInEgBillRegister(final PurchaseBillForm sbForm) throws Exception {
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Integer>() {

			@Override
			public Integer execute(Connection conn) throws SQLException {
				

				Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(
						Integer.parseInt(sbForm.getCodeList())));
				/*
				 * Check For Total Workorder amount and bill amount
				 */
				try {
					BigDecimal woTotalValue = wd.getTotalvalue();
					BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(wd)
							.get(0);// get All billed Amount from billRegister
					if (totalbillvalue == null)
						{totalbillvalue = new BigDecimal(0);}
					totalbillvalue = totalbillvalue.add(wd.getPassedamount());// add  total passed  amt from wororder
					totalbillvalue = totalbillvalue.add(new BigDecimal(sbForm
							.getPassedAmount()));// add bill amount
					int amtComp = woTotalValue.compareTo(totalbillvalue);
					if (amtComp == -1)
						{return 1;}
					wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(Integer
							.parseInt(sbForm.getCodeList())));

					EgBillregister reg = new EgBillregister();
					reg.setWorksdetailId(sbForm.getCodeList());
					Date bill_Date = sdf.parse(sbForm.getBillDate());
					reg.setBilldate(bill_Date);
					reg.setBillamount(new BigDecimal(sbForm.getBill_Amount()));
					reg.setPassedamount(new BigDecimal(sbForm.getPassedAmount()));
					if (sbForm.getAdjustmentAmount() != null)
					{reg.setAdvanceadjusted(new BigDecimal(sbForm
								.getAdjustmentAmount()));}
					reg.setBillstatus("PENDING");
					reg.setBilltype(sbForm.getBill_Type());
					reg.setExpendituretype(sbForm.getExpenditure_Type());
					reg.setNarration(sbForm.getNarration());
					EgwStatus invStatus = null;
					EGovernCommon cm = new EGovernCommon();
					
					invStatus = commonsService.findEgwStatusById(Integer.valueOf(cm.getEGWStatusId(
							conn, "PURCHBILL", "Pending")));
					reg.setStatus(invStatus);
					reg.setCreatedBy(userService.getUserByID(sbForm.getUserId()));
					Date crntDate = new Date();
					reg.setCreatedDate(crntDate);
					// Generate BillNo And Sanction Number
					String sanctionNo = null;
					CommonMethodsImpl cmImpl = new CommonMethodsImpl();
					reg.setBillnumber(cmImpl.getTxnNumber("SBILL", sbForm.billDate,
							conn));
					sanctionNo = cmImpl.getTxnNumber("SAN", sbForm.getBillDate(), conn);
					sbForm.setBillNo(reg.getBillnumber());
					sbForm.setSanctionNo(sanctionNo);
					EgBillregistermis egmis = new EgBillregistermis();
					egmis.setEgBillregister(reg);
					egmis.setFund(wd.getFund());
					egmis.setFundsource(wd.getFundsource());
					egmis.setMbRefNo(sbForm.getMBrefNo());
					egmis.setSanctiondetail(sanctionNo);
					egmis.setSanctiondate(bill_Date);
					Relation rel = commonsService.getRelationById(Integer.valueOf(sbForm.getCSId()));
					egmis.setPayto(rel.getName());
					CFinancialYear financialyear = commonsService.getFinancialYearByFinYearRange(sbForm.getBillDate());
					egmis.setFinancialyear(financialyear);
					if (!sbForm.getDept().equalsIgnoreCase("0")) {
						DepartmentImpl egDept = (DepartmentImpl) departmentService.getDepartment(Integer.valueOf(Integer.parseInt(sbForm.getDept())));
						egmis.setEgDepartment(egDept);
					}
					if (!sbForm.getFunctionaryId().equals("0"))
					{
						Functionary functionary = commonsService.getFunctionaryById(Integer.valueOf(sbForm.getFunctionaryId()));
						if(functionary!=null)
							egmis.setFunctionaryid(functionary);
					}
					else
					{
						egmis.setFunctionaryid(null);
					}
					reg.setEgBillregistermis(egmis);
			//		Set<EgBilldetails> billDetailsList = new HashSet();
					reg.setEgBilldetailes(postInBilldetails(sbForm, reg));
					billsService.createBillRegister(reg);
					sbForm.setBillId(reg.getId().toString());
					// LOGGER.debug("Calling WorkFlow---->");
					User user = (User) userService.getUserByID(sbForm.getUserId());
					String username = user.getUserName();
					sbForm.setUserName(username);
					postInEGActionDetails("PURCHBILL", reg.getId().toString(),"Bill Created", sbForm.getUserId());
					createWorkFlow(sbForm);
					
				} catch (Exception e) {
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
	public Set<EgBilldetails> postInBilldetails(final PurchaseBillForm sbForm,
			final EgBillregister reg) throws EGOVRuntimeException,
			TaskFailedException, SQLException {
		// insertDebits
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<Set<EgBilldetails>>() {

			@Override
			public Set<EgBilldetails> execute(Connection conn) throws SQLException {
				
			
		Set<EgBilldetails> billDetailsList = new HashSet<EgBilldetails>();
		String advGlcode =  EGovConfig.getProperty("egf_config.xml","SupAdvCode", "", "SupplierCodes");;
		CChartOfAccounts net_glAcct = null;
		List netPayList = getNetPayList();

		try {

			String net_glAccStr = sbForm.getNet_chartOfAccounts_glCode();
			String acc[] = net_glAccStr.split("~");
			net_glAcct = commonsService.getCChartOfAccountsByGlCode(acc[1]);
	
			for (int i = 0; i < sbForm.getDeb_chartOfAccounts_glCode().length; i++) {
				EgBilldetails egBilldet = new EgBilldetails();
				Set billPayeeSet = new HashSet();
				EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
				if (!sbForm.deb_chartOfAccounts_glCode[i].equals("") ) {
					if (sbForm.deb_chartOfAccounts_glCode[i]
							.equalsIgnoreCase(advGlcode)
							|| netPayListString
									.contains(sbForm.deb_chartOfAccounts_glCode[i])) {
						throw new EGOVRuntimeException(
								"Internal Codes Are Used" + advGlcode + " Or "
										+ net_glAcct.getGlcode());
					}
					if (!sbForm.debitAmount[i].equals("")) {
						egBilldet.setEgBillregister(reg);
						egBilldet.setDebitamount(new BigDecimal(
								sbForm.debitAmount[i]));
						CChartOfAccounts cc = (CChartOfAccounts) commonsService.getCChartOfAccountsByGlCode(sbForm.deb_chartOfAccounts_glCode[i]);
						Long glcodeid = cc.getId();
						egBilldet.setGlcodeid(new BigDecimal(glcodeid));
						if (sbForm.deb_cv_fromFunctionCodeId[i] != null
								&& !sbForm.deb_cv_fromFunctionCodeId[i]
										.equals("")){
							egBilldet.setFunctionid(new BigDecimal(
									sbForm.deb_cv_fromFunctionCodeId[i]));}
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails Add Record If it is a detailed code
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();

						try {
							detailCode = commonsService.getAccountdetailtypeListByGLCode(sbForm.deb_chartOfAccounts_glCode[i]);
						} catch (Exception e) {
							HibernateUtil.rollbackTransaction();
								}
						if (detailCode!=null && !detailCode.isEmpty()) {
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeedet.setAccountDetailKeyId(Integer.valueOf(
									sbForm.getCSId()));
							String detid_name = commonsService.getAccountdetailtypeAttributename(conn,"Creditor");
							String[] ids = detid_name.split("#");
							int id = Integer.parseInt(ids[0]);
							billPayeedet
									.setAccountDetailTypeId(Integer.valueOf(id));
							billPayeedet.setDebitAmount(new BigDecimal(
									sbForm.debitAmount[i]));
							billPayeedet.setLastUpdatedTime(new Date());
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0)
							{egBilldet.setEgBillPaydetailes(billPayeeSet);}
						billDetailsList.add(egBilldet);

					}
				}

			}
			// LOGGER.debug("The No of credit
			// records"+sbForm.getDed_chartOfAccounts_glCode().length);
			for (int i = 0; i < sbForm.getDed_chartOfAccounts_glCode().length; i++) {
				EgBilldetails egBilldet = new EgBilldetails();
				Set billPayeeSet = new HashSet();
				EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
				// LOGGER.debug("sbForm.ded_chartOfAccounts_glCode[i]"+sbForm.ded_chartOfAccounts_glCode[i]);
				if (!sbForm.ded_chartOfAccounts_glCode[i].equals("") ) {
					if (sbForm.ded_chartOfAccounts_glCode[i]
							.equalsIgnoreCase(advGlcode)
							|| netPayListString
									.contains((sbForm.ded_chartOfAccounts_glCode[i]))) {
						throw new EGOVRuntimeException(
								"Internal Codes Are Used" + advGlcode + " Or "
										+ net_glAcct.getGlcode());
					}
					if (!sbForm.creditAmount[i].equals("")) {
						// LOGGER.debug("sbForm.ded_chartOfAccounts_glCode[i]"+sbForm.ded_chartOfAccounts_glCode[i]);
						// LOGGER.debug("sbForm.creditAmount[i]"+sbForm.creditAmount[i]);
						egBilldet.setEgBillregister(reg);
						egBilldet.setCreditamount(new BigDecimal(
								sbForm.creditAmount[i]));
						CChartOfAccounts cc = (CChartOfAccounts) commonsService.getCChartOfAccountsByGlCode(sbForm.ded_chartOfAccounts_glCode[i]);
						Long glcodeid = cc.getId();
						egBilldet.setGlcodeid(new BigDecimal(glcodeid));
						if (sbForm.ded_cv_fromFunctionCodeId[i] != null
								&& !sbForm.ded_cv_fromFunctionCodeId[i]
										.equals(""))
							egBilldet.setFunctionid(new BigDecimal(
									sbForm.ded_cv_fromFunctionCodeId[i]));
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();
						try {
							detailCode = commonsService.getAccountdetailtypeListByGLCode(sbForm.ded_chartOfAccounts_glCode[i]);
						} catch (Exception e) {
							throw new EGOVRuntimeException(
									"Exception in getting the detail code");
						}
						if (detailCode!=null && !detailCode.isEmpty()) {
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeedet.setAccountDetailKeyId(Integer.valueOf(
									sbForm.getCSId()));
							String detid_name = commonsService.getAccountdetailtypeAttributename(conn,"Creditor");
							String[] ids = detid_name.split("#");
							int id = Integer.parseInt(ids[0]);
							billPayeedet
									.setAccountDetailTypeId(Integer.valueOf(id));
							billPayeedet.setCreditAmount(new BigDecimal(
									sbForm.creditAmount[i]));
							billPayeedet.setLastUpdatedTime(new Date());
							// billPayeedet.setEgBilldetailsId(egBilldet);
							Recovery tds = null;
							if (sbForm.tds_code[i] != null
									&& !sbForm.tds_code[i].equals("0")) {
								String tandglid = sbForm.tds_code[i];
								int loc = tandglid.indexOf('`');
								String tid = tandglid.substring(0, loc);
								// LOGGER.debug("TdsId is "+tid);
								tds = (Recovery) recoveryService.getTdsById(Long.valueOf(Long.parseLong(tid)));
								// tds=(Tds)
								// HibernateUtil.getCurrentSession().(Tds.class,new
								// Integer(Integer.parseInt(tid)));
								billPayeedet.setRecovery(tds);
							}
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0)
							egBilldet.setEgBillPaydetailes(billPayeeSet);
						billDetailsList.add(egBilldet);
					}
				}

			}
			// Advance Payed
			sbForm.net_Amount = sbForm.totalAmount;
			if (sbForm.getAdjustmentAmount() != null) {
				double advAmount = Double.parseDouble(sbForm
						.getAdjustmentAmount());
				if (advAmount > 0.0f) {

					EgBilldetails egBilldet = new EgBilldetails();
					Set billPayeeSet = new HashSet();
					EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
					// String code1=null;
					CChartOfAccounts chartofacct = null;
					// CChartOfAccounts net_chartofacct=null;
					
					if (sbForm.getExpenditure_Type().equalsIgnoreCase(
							"Purchase")) {
						// chartofacct=comm.findCodeByPurposeId();
						// chartofacct=comm.getCChartOfAccountsByGlCode("4604001");
						 
						chartofacct = commonsService.getCChartOfAccountsByGlCode(advGlcode);
						// net_chartofacct=comm.findCodeByPurposeId(27);

					} 

					if (!(chartofacct.getGlcode()).equalsIgnoreCase(net_glAcct
							.getGlcode())) {
						egBilldet.setCreditamount(new BigDecimal(advAmount));
						sbForm.net_Amount = sbForm.totalAmount;// set the
																// net_Amount
																// here
						egBilldet.setEgBillregister(reg);
						egBilldet.setGlcodeid(new BigDecimal(chartofacct
								.getId()));
						// egBilldet.setLastupdatedtime(new Date());
						// BillPayeeDetails
						List<Accountdetailtype> detailCode = new ArrayList<Accountdetailtype>();

						try {
							detailCode = commonsService.getAccountdetailtypeListByGLCode(chartofacct.getGlcode());
						} catch (Exception e) {
								throw new EGOVRuntimeException(
									"Exception in getting the detail code");
						}
						if (!detailCode.isEmpty()) {
							billPayeedet.setAccountDetailKeyId(Integer.valueOf(
									sbForm.getCSId()));
							billPayeedet.setAccountDetailTypeId(Integer.valueOf(1));
							billPayeedet.setCreditAmount(new BigDecimal(
									advAmount));
							billPayeedet.setLastUpdatedTime(new Date());
							billPayeedet.setEgBilldetailsId(egBilldet);
							billPayeeSet.add(billPayeedet);
						}
						if (billPayeeSet.size() > 0)
							egBilldet.setEgBillPaydetailes(billPayeeSet);
						billDetailsList.add(egBilldet);

					} else
						sbForm.net_Amount = new Double(Double
								.parseDouble(sbForm.totalAmount)
								+ Double.parseDouble(sbForm.adjustmentAmount))
								.toString();
				}
			}

			// defaultGrid ie suppler or Contractor payable
			EgBilldetails egBilldet = new EgBilldetails();
			Set billPayeeSet = new HashSet();
			EgBillPayeedetails billPayeedet = new EgBillPayeedetails();
			// String code1=null;
			// CChartOfAccounts chartofacct=null;
			// if(sbForm.getExpenditure_Type().equalsIgnoreCase("Purchase"))
			// {
			// chartofacct=comm.findCodeByPurposeId(27);
			// }
			// else if(sbForm.getExpenditure_Type().equalsIgnoreCase("Works"))
			// {
			// chartofacct=comm.findCodeByPurposeId(26);
			// }
			egBilldet.setCreditamount(new BigDecimal(sbForm.net_Amount));
			egBilldet.setEgBillregister(reg);
			egBilldet.setGlcodeid(new BigDecimal(net_glAcct.getId()));
			if (sbForm.getNet_cv_fromFunctionCodeId() != null
					&& !sbForm.getNet_cv_fromFunctionCodeId().equals(""))
				egBilldet.setFunctionid(new BigDecimal(sbForm
						.getNet_cv_fromFunctionCodeId()));
			// egBilldet.setLastupdatedtime(new Date());
			// BillPayeeDetails
			// sbForm.net_Amount=sbForm.totalAmount;
			billPayeedet.setAccountDetailKeyId(Integer.valueOf(sbForm.getCSId()));
			String detid_name = commonsService.getAccountdetailtypeAttributename(conn,"Creditor");
				String[] ids = detid_name.split("#");
					int id = Integer.parseInt(ids[0]);
			billPayeedet.setAccountDetailTypeId(Integer.valueOf(id));
			billPayeedet.setCreditAmount(new BigDecimal(sbForm.net_Amount));
			billPayeedet.setLastUpdatedTime(new Date());
			billPayeedet.setEgBilldetailsId(egBilldet);
			billPayeeSet.add(billPayeedet);
			egBilldet.setEgBillPaydetailes(billPayeeSet);
			billDetailsList.add(egBilldet);

		} catch (Exception e) {
			LOGGER.error("Exception Occured While Creating Bill List");
				throw new EGOVRuntimeException(e.getMessage());
		}
		return billDetailsList;
			}
		});
	}

	public void postInEGActionDetails(final String moduleType, final String billId,
			final String desc, final int usrId) throws Exception {
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection conn) throws SQLException {
				
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					String txndate = commonsService.getCurrentDate(conn);
					EgActiondetails egActiondetails = new EgActiondetails();
					User user = (User) userService.getUserByID(Integer.valueOf(usrId));
					egActiondetails.setActionDoneBy((UserImpl) user);
					egActiondetails.setActionDoneOn(formatter.parse(txndate));
					egActiondetails.setModuleid(Integer.valueOf(Integer.parseInt(billId)));
					egActiondetails.setActiontype(desc);
					egActiondetails.setModuletype(moduleType);
					egActiondetails.setCreatedby(Integer.valueOf(usrId));
					commonsService.createEgActiondetails(egActiondetails);
					// LOGGER.debug("Inserted");
				} catch (Exception e) {
					LOGGER.error("Exception While Creating the Bill");
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
			throw new EGOVRuntimeException(e.getMessage());
		}
		return al;
	}

	/*
	 * get Complete Bill With BillRegister BillRegistermis BillDetails
	 * BillPayeeDetails table entries
	 */
	public PurchaseBillForm getEgBillRegister(PurchaseBillForm sbForm)
			throws EGOVRuntimeException, Exception {

		try {
			Worksdetail wd = null;
			Integer billRegId = Integer.valueOf(sbForm.getBillId());
			EgBillregister reg = billsService.getBillRegisterById(billRegId);
			EgBillregistermis billmis = (EgBillregistermis) reg
					.getEgBillregistermis();
			if (billmis.getEgDepartment() != null)
				sbForm.setDept(billmis.getEgDepartment().getId().toString());
			if(billmis.getFunctionaryid()!=null)
				sbForm.setFunctionaryId(billmis.getFunctionaryid().toString());
			sbForm.setMBrefNo(billmis.getMbRefNo());
			sbForm.sanctionNo = billmis.getSanctiondetail();
			sbForm.bill_Amount = reg.getBillamount().toString();
			sbForm.bill_Type = reg.getBilltype();
			sbForm.expenditure_Type = reg.getExpendituretype();
			Date dt = (Date) reg.getBilldate();
			sbForm.billDate = sdf.format(dt);
			// LOGGER.debug(sbForm.billDate);
			sbForm.billNo = reg.getBillnumber();
			sbForm.narration = reg.getNarration();
			sbForm.setBillId(reg.getId().toString());
			BigDecimal advadjd = reg.getAdvanceadjusted();
			double advadjdInt = 0.0d;
			if (advadjd == null)
				sbForm.adjustmentAmount = "0";
			else {
				advadjdInt = reg.getAdvanceadjusted().doubleValue();
				sbForm.adjustmentAmount = reg.getAdvanceadjusted().toString();
			}
			BigDecimal passamt = reg.getPassedamount();
			if (passamt != null)
				sbForm.passedAmount = passamt.toString();
			else
				sbForm.passedAmount = "0";
			String wid = reg.getWorksdetailId();
			// LOGGER.debug(wid);
			// WorksBillManager
			// wm=(WorksBillManager)GetEgfManagers.getWorksBillManager();
			if (worksBillService != null) {
				wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(wid));
				sbForm.codeList = wd.getId().toString();
				sbForm.codeName = wd.getCode();
				sbForm.CSId = wd.getRelation().getId().toString();
				sbForm.CSName = wd.getRelation().getName();
				sbForm.worksName = wd.getName();
				sbForm.totalWorkOrder = wd.getTotalvalue().toString();
				sbForm.setWorkOrderDate(wd.getOrderdate().toString());
				BigDecimal passdAmt = wd.getPassedamount();
				if (passdAmt == null)
					passdAmt = new BigDecimal(0);
				BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(
						wd).get(0);
				if (totalbillvalue == null)
					totalbillvalue = new BigDecimal(0);
				totalbillvalue = totalbillvalue.add(passdAmt);

				totalbillvalue = totalbillvalue.subtract(new BigDecimal(
						sbForm.passedAmount));
				sbForm.setTotalBilledAmount(totalbillvalue.toString());
				// List tdsList=(List)wm.getTdsForWorkOrder(wd);
				// sbForm.setTdsList(tdsList);

				BigDecimal totalAdvAdj = (BigDecimal) worksBillService.getTotalAdvAdj(wd)
						.get(0);
				if (totalAdvAdj == null)
					totalAdvAdj = new BigDecimal(0);

				BigDecimal advamt = wd.getAdvanceamount();
				BigDecimal advadj = wd.getAdvanceadj();
				if (advadj == null)
					advadj = new BigDecimal(0);
				advadj = totalAdvAdj.add(advadj);
				advadj = advadj.subtract(reg.getAdvanceadjusted());
				double advamtInt = 0;
				double advadjInt = 0;
				double advance = 0;
				if (advamt != null)
					advamtInt = advamt.doubleValue() - advadj.doubleValue();
				if (advamtInt < 0)
					advamtInt = 0;
				sbForm.setAdvanceAmount(String.valueOf(advamtInt));
			}
			 

			// read ConSup payable
			String netPayableCode = null;
			String advanceCode = null;
			CChartOfAccounts net_chartofacct = null;
			CChartOfAccounts dedchartOfAcc = null;
			// if(sbForm.getExpenditure_Type().equalsIgnoreCase("Purchase"))
			// {
			// net_chartofacct=comm.findCodeByPurposeId(27);
			advanceCode = EGovConfig.getProperty("egf_config.xml",
					"SupAdvCode", "", "SupplierCodes");
			//				
			// }
			// else if(sbForm.getExpenditure_Type().equalsIgnoreCase("Works"))
			// {
			// net_chartofacct=comm.findCodeByPurposeId(26);
			advanceCode = EGovConfig.getProperty("egf_config.xml",
					"ConAdvCode", "", "ContractorCodes");
			// }
			// getAllBillDetailEntries
			// netPayableCode=net_chartofacct.getGlcode();
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

			sbForm.debitAmount = new String[dbRows];
			sbForm.deb_cv_fromFunctionCodeId = new String[dbRows];
			sbForm.deb_function_code = new String[dbRows];
			sbForm.deb_chartOfAccounts_glCode = new String[dbRows];
			sbForm.deb_chartOfAccounts_name = new String[dbRows];
			if (crRows - 1 > 0) {
				sbForm.creditAmount = new String[crRows - 1];
				sbForm.ded_cv_fromFunctionCodeId = new String[crRows - 1];
				sbForm.ded_function_code = new String[crRows - 1];
				sbForm.ded_chartOfAccounts_glCode = new String[crRows - 1];
				sbForm.ded_chartOfAccounts_name = new String[crRows - 1];
				sbForm.tds_code = new String[crRows - 1];
			} else {
				sbForm.creditAmount = null;
				sbForm.ded_cv_fromFunctionCodeId = null;
				sbForm.ded_function_code = null;
				sbForm.ded_chartOfAccounts_glCode = null;
				sbForm.ded_chartOfAccounts_name = null;
				sbForm.tds_code = null;
			}

			Iterator billdetItr = billdetailsSet.iterator();
			while (billdetItr.hasNext()) {
				EgBilldetails billDetails = (EgBilldetails) billdetItr.next();
				if (billDetails.getDebitamount() != null) {
					sbForm.debitAmount[i] = "" + billDetails.getDebitamount();
					BigDecimal funcId = billDetails.getFunctionid();
					if (funcId != null) {
						Long fid = Long.valueOf(funcId.intValue());
						CFunction cFunction = (CFunction) commonsService.getCFunctionById(fid);
						if (cFunction != null) {
							sbForm.deb_cv_fromFunctionCodeId[i] = cFunction
									.getId().toString();
							sbForm.deb_function_code[i] = ""
									+ cFunction.getName();
						}
					} else {
						sbForm.deb_cv_fromFunctionCodeId[i] = "";
						sbForm.deb_function_code[i] = "";
					}
					// load glcode and name
					BigDecimal glCodeid = billDetails.getGlcodeid();
					Long glId = Long.valueOf(glCodeid.longValue());
					CChartOfAccounts chartOfAcc = commonsService.getCChartOfAccountsById(glId);
					sbForm.deb_chartOfAccounts_glCode[i] = chartOfAcc
							.getGlcode();
					sbForm.deb_chartOfAccounts_name[i] = chartOfAcc.getName();
					sbForm.debitAmount[i] = "" + billDetails.getDebitamount();
					i++;
				} else {
					// load glcode and name
					BigDecimal glCodeid = billDetails.getGlcodeid();
					Long glId = Long.valueOf(glCodeid.longValue());
					dedchartOfAcc = commonsService.getCChartOfAccountsById(glId);
					// check For NetPay Also
					String netGlCode = dedchartOfAcc.getGlcode();
					// LOGGER.debug(netGlCode+""+netPayableCode);
					// for advance Amount
					if (netGlCode.equalsIgnoreCase(advanceCode)) {
						if (!advanceCode.equalsIgnoreCase(netPayableCode))

						{
							// LOGGER.debug("Inside advance Fetch");
							sbForm.ded_chartOfAccounts_glCode[j] = null;
							sbForm.ded_chartOfAccounts_name[j] = null;
							j++;
							continue;
						}

					}

					// end of advance amount
					List netPayList = getNetPayList();
					if (netPayList.contains(dedchartOfAcc)) {
						// LOGGER.debug("Inside net payable ");
						sbForm.net_Amount = billDetails.getCreditamount()
								.toString();
						// load function name
						BigDecimal funcId1 = billDetails.getFunctionid();
						if (funcId1 != null) {
							Long fid = Long.valueOf(funcId1.intValue());
							CFunction cFunction = (CFunction) commonsService.getCFunctionById(fid);
						}
						sbForm.net_chartOfAccounts_glCode = dedchartOfAcc
								.getId()
								+ "~"
								+ dedchartOfAcc.getGlcode()
								+ "~"
								+ dedchartOfAcc.getName();
						sbForm.net_chartOfAccounts_name = dedchartOfAcc
								.getName();
						sbForm.net_Amount = billDetails.getCreditamount()
								.toString();
					} else if (!netGlCode.equalsIgnoreCase(advanceCode)) {
						// LOGGER.debug("Inside Deductions");
						sbForm.creditAmount[j] = ""
								+ billDetails.getCreditamount();
						// load function name
						BigDecimal funcId = billDetails.getFunctionid();
						if (funcId != null) {
							Long fid = Long.valueOf(funcId.intValue());
							CFunction cFunction = (CFunction) commonsService.getCFunctionById(fid);
							if (cFunction != null) {
								sbForm.ded_cv_fromFunctionCodeId[j] = ""
										+ cFunction.getId();
								sbForm.ded_function_code[j] = ""
										+ cFunction.getName();
							}

						} else {
							sbForm.ded_cv_fromFunctionCodeId[j] = "";
							sbForm.ded_function_code[j] = "";
						}

						sbForm.ded_chartOfAccounts_glCode[j] = dedchartOfAcc
								.getGlcode();
						sbForm.ded_chartOfAccounts_name[j] = dedchartOfAcc
								.getName();
						Set payeeList = billDetails.getEgBillPaydetailes();
						if (payeeList != null && !payeeList.isEmpty()) {
							Iterator payeeItr = payeeList.iterator();
							while (payeeItr.hasNext()) {
								EgBillPayeedetails payeeDetails = (EgBillPayeedetails) payeeItr
										.next();
								if (payeeDetails != null) {
									Recovery tdsid = (Recovery) payeeDetails.getRecovery();
									// LOGGER.debug("tds"+tdsid);
									if (tdsid != null) {
										if (payeeDetails.getCreditAmount() != null) {
											// Tds
											// tdsid=payeeDetails.getTdsId();
											// LOGGER.debug(tdsid.getType());
											// sbForm.ded_chartOfAccounts_name[j]=tdsid.getType();
											sbForm.tds_code[j] = ""
													+ tdsid.getId()
													+ "`~`"
													+ sbForm.ded_chartOfAccounts_glCode[j];
										}
									}
								}
							}
						} else {
							sbForm.tds_code[j] = "0";
						}

						// LOGGER.debug(sbForm.ded_chartOfAccounts_glCode[j]);
						// LOGGER.debug(sbForm.ded_chartOfAccounts_name[j]);
						// LOGGER.debug(sbForm.tds_code[j]);
						// LOGGER.debug(sbForm.creditAmount[j]+" "+i);
						j++;
					}
				}

			}
			if (wd.getWorkcategory() != null)
				sbForm.setWorkType(wd.getWorkcategory().toString());
			if (wd.getSubcategory() != null)
				sbForm.setWorkSubType(wd.getSubcategory().toString());
			if (wd.getRelation().getRelationtype() != null)
				sbForm.setTdsList(recoveryService.getAllTdsByPartyType(wd.getRelation()
						.getRelationtype().getName()));
			List l = sbForm.getTdsList();
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
			sbForm.setTdsList(l);

		} catch (Exception e) {
			LOGGER.error("Error occured while getting the records");
			throw new EGOVRuntimeException(e.getMessage());
		}
		return sbForm;
	}

	public PurchaseBillForm getTds(PurchaseBillForm sbForm)
			throws EGOVRuntimeException {
		List tdsList = null;
		WorksBillService worksBillService = null;
		try {
			worksBillService = (WorksBillService) GetEgfManagers.getWorksBillService();
			String worksdtlId = sbForm.getCodeList();
			Worksdetail wd = worksBillService.getWorksDetailById(Integer.valueOf(worksdtlId));
			sbForm.setCSId(wd.getRelation().getId().toString());
			sbForm.setCSName(wd.getRelation().getName().toString());
			sbForm.setTotalWorkOrder(wd.getTotalvalue().toString());
			sbForm.setWorkOrderDate(wd.getOrderdate().toString());
			BigDecimal passdAmt = wd.getPassedamount();
			if (passdAmt == null)
				passdAmt = new BigDecimal(0);
			BigDecimal totalbillvalue = (BigDecimal) worksBillService.getTotalBillValue(wd)
					.get(0);
			if (totalbillvalue == null)
				totalbillvalue = new BigDecimal(0);
			totalbillvalue = totalbillvalue.add(passdAmt);
			sbForm.setTotalBilledAmount(totalbillvalue.toString());
			if(null != wd.getExecdeptid()){
				LOGGER.debug("The department id for this PO order---->>>>>"+wd.getExecdeptid());
				sbForm.setDept(wd.getExecdeptid().toString());
			}
			
			// LOGGER.debug(sbForm.getCSId());
			// LOGGER.debug(sbForm.getCSName());
			// getAdvanceAmount
			BigDecimal totalAdvAdj = (BigDecimal) worksBillService.getTotalAdvAdj(wd).get(0);
			if (totalAdvAdj == null)
				totalAdvAdj = new BigDecimal(0);
			BigDecimal advamt = wd.getAdvanceamount();
			BigDecimal advadj = wd.getAdvanceadj();
			if (advadj == null)
				advadj = new BigDecimal(0);
			advadj = totalAdvAdj.add(advadj);
			double advamtInt = 0;
			double advadjInt = 0;
			double advance = 0;
			if (advamt != null)
				advamtInt = advamt.doubleValue();
			if (advadj != null)
				advadjInt = advadj.doubleValue();
			advance = advamtInt - advadjInt;
			sbForm.setAdvanceAmount("" + advance);
			if (wd.getWorkcategory() != null)
				sbForm.setWorkType(wd.getWorkcategory().toString());
			if (wd.getSubcategory() != null)
				sbForm.setWorkSubType(wd.getSubcategory().toString());

			if (wd.getRelation().getRelationtype() != null)
				tdsList = recoveryService.getAllTdsByPartyType(wd.getRelation()
						.getRelationtype().getName());
			// (List)wm.getTdsForWorkOrder(wd);
			// LOGGER.debug(tdsList.size());
			sbForm.setTdsList(tdsList);
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
		} catch (Exception e) {
			LOGGER.error("Error While creating WorksBill manager");
			throw new EGOVRuntimeException(e.getMessage());
		}
		return sbForm;
	}

	public int modify(PurchaseBillForm sbForm) throws EGOVRuntimeException {
		EgBillregister reg = null;

		String billregId = sbForm.getBillId();
		reg = billsService.getBillRegisterById(Integer.valueOf(billregId));
		try {
			Date bill_Date = sdf.parse(sbForm.getBillDate());
			reg.setBilldate(bill_Date);
			reg.setBillamount(new BigDecimal(sbForm.getBill_Amount()));
			reg.setPassedamount(new BigDecimal(sbForm.getPassedAmount()));
			reg.setBillstatus("PENDING");
			reg.setBilltype(sbForm.getBill_Type());
			reg.setExpendituretype(sbForm.getExpenditure_Type());
			reg.setNarration(sbForm.getNarration());
			EgwStatus invStatus = null;
			EGovernCommon cm = new EGovernCommon();
			

			if (sbForm.getExpenditure_Type().equalsIgnoreCase("Works"))
				// invStatus = comm.findEgwStatusById(33);
				invStatus = commonsService.findEgwStatusById(Integer.valueOf(cm.getEGWStatusId( "WORKSBILL", "Pending")));
			else
				// invStatus = comm.findEgwStatusById(37);
				invStatus = commonsService.findEgwStatusById(Integer.valueOf(cm.getEGWStatusId( "PURCHBILL", "Pending")));
			reg.setStatus(invStatus);
			reg.setBillnumber(sbForm.getBillNo());
			reg.setCreatedBy(userService.getUserByID(sbForm.getUserId()));
			Date crntDate = new Date();
			reg.setCreatedDate(crntDate);
			if (sbForm.getAdjustmentAmount() != null && !sbForm.getAdjustmentAmount().equalsIgnoreCase("0"))
				reg.setAdvanceadjusted(new BigDecimal(sbForm.getAdjustmentAmount()));

			reg.setWorksdetailId(sbForm.getCodeList());
			EgBillregistermis egmis = reg.getEgBillregistermis();
			egmis.setMbRefNo(sbForm.getMBrefNo());
			if (!sbForm.getDept().equalsIgnoreCase("0")) {
				DepartmentImpl egDept = (DepartmentImpl) departmentService
						.getDepartment(Integer.valueOf(Integer.parseInt(sbForm
								.getDept())));
				egmis.setEgDepartment(egDept);
			} else
				egmis.setEgDepartment(null);
			if (!sbForm.getFunctionaryId().equalsIgnoreCase("0"))	
			{
				Functionary functionary = commonsService.getFunctionaryById(Integer.valueOf(sbForm.getFunctionaryId()));
				if(functionary!=null)
					egmis.setFunctionaryid(functionary);
			}
			else
			{
				egmis.setFunctionaryid(null);
			}
			CFinancialYear financialyear = commonsService.getFinancialYearByFinYearRange(sbForm.getBillDate());
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
					// LOGGER.debug(" billDet "+ billDet.getId());
					billDetItr.remove();
				} catch (Exception e) {
					HibernateUtil.rollbackTransaction();
				}
			}   

			reg.setEgBilldetailes(EgBillSet);
			Set<EgBilldetails> EgBillSet1 = new HashSet();
			EgBillSet1.addAll(postInBilldetails(sbForm, reg));
			EgBillSet.addAll(EgBillSet1);
			HibernateUtil.getCurrentSession().flush();
		} catch (Exception e) {
			LOGGER.error("Error while Saving the Changes to Bill");
			throw new EGOVRuntimeException(e.getMessage());
		}
		return 0;
	}

	public EgBillregister gnerateVoucher(final int billId, final int usrId,
			final String voucherHeaderNarration,final String approvalDate) throws EGOVRuntimeException,
			SQLException {
		
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
					String vdt=null; 
					String voucherHeader_voucherDate = null;
					if (null != approvalDate && approvalDate.trim().length()!=0) {	
						vdt = formatter.format(new SimpleDateFormat("dd/MM/yyyy").parse(approvalDate));
						voucherHeader_voucherDate = approvalDate ;
					}else{
						vdt=formatter.format(dt); 
						voucherHeader_voucherDate = sdf.format(dt);
					}
					String fiscalPeriod = null;
					// CFinancialYear
					// financialyear=comm.getFinancialYearByFinYearRange(voucherHeader_voucherDate);
					fiscalPeriod = cm.getFiscalPeriod(vdt);
					if(null == fiscalPeriod){
						throw new RBACException("Voucher Date not within an open period or Financial year not open for posting, fiscalPeriod := "+ fiscalPeriod);
					}
					// LOGGER.debug("fiscalPeriod"+fiscalPeriod);
					HibernateUtil.getCurrentSession().flush();
					reg = billsService.getBillRegisterById(Integer.valueOf(billId));
					String wdId = reg.getWorksdetailId();
					Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(
							Integer.parseInt(wdId)));
					String fId = null;
					if (wd.getFund() != null)
						fId = wd.getFund().getId().toString();
					// LOGGER.debug("fiscalPeriodId"+fiscalPeriod);
					//find the jv type from egw_works_mis table
					EgwWorksMisHibernateDAO worksmisDAO=  BillsAccountingDAOFactory.getDAOFactory().getEgwWorksMisDAO();
					EgwWorksMis wmis=(EgwWorksMis)worksmisDAO.findByWorkOrderId(wd.getId().toString());
					String tType="";
					LOGGER.info("Purchae Type"+wmis.getIsFixedAsset());
					if(wmis.getIsFixedAsset()==1)
					{
						LOGGER.info("Purchae Type IS fIXED ASSET");
						tType=FinancialConstants.FIXEDASSET_VOUCHERNO_TYPE;
						
					}
					else
					{
						LOGGER.info("Purchae Type IS fIXED ASSET");
						tType=FinancialConstants.PURCHBILL_VOUCHERNO_TYPE;
					}
					String voucherNumber = cm.vNumber(tType, conn, fId, reg.getBillnumber());
					// LOGGER.debug("voucherNumber"+voucherNumber);
					String vType = voucherNumber.substring(0,Integer.parseInt( FinancialConstants.VOUCHERNO_TYPE_LENGTH));
					// LOGGER.debug("vType"+vType);
					String eg_voucher = cm.getEg_Voucher(vType, fiscalPeriod, conn);
					// LOGGER.debug("eg_voucher"+eg_voucher);

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
					if (vNumRptrs.next())
						noOftimesVoucherCreated = vNumRptrs.getInt(1);
					if (noOftimesVoucherCreated != 0)
						voucherNumber = voucherNumber + "/" + (noOftimesVoucherCreated);
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
					if (reg.getEgBillregistermis().getEgDepartment() != null)
						dept = reg.getEgBillregistermis().getEgDepartment().getId();
					if (dept != null){
						vh.setDepartmentId(dept.toString());
					}else if( EGovConfig.getProperty("egf_config.xml","deptRequired","","general").equalsIgnoreCase("Y") && null == dept  ){
						throw new RBACException("Department value is missing");
					}
						
					vh.setFiscalPeriodId(fiscalPeriod);

					String cgn = "SJV" + cm.getCGNumber();
					vh.setCgn(cgn);
					vh.setName("Supplier Journal");
					vh.setType("Journal Voucher");

					vh.insert(conn);
					int vid = vh.getId();
					LOGGER.debug("voucherheaderid" + vid);
					Long voucherheaderid = Long.valueOf(vid);
					updateVoucherMIS(voucherHeader_voucherDate, reg, conn, vid,"/EGF/HTML/VMC/SupplierJournal_VMC.jsp?cgNumber="+vh.getCgn()+"&showMode=view");
					transactions = postInVoucherDetail(reg, transactions, conn,
							voucherheaderid);
					// postInAssetStatus(sbForm);
					postInSupplierBillDetail(reg, voucherheaderid, wd);
					cm.UpdateVoucherStatus("Supplier Journal", conn, vid, usrId);
					String passedStatusId = cm.getEGWStatusId(conn, "PURCHBILL",
							"Passed");
					// postInEGWStatusChange(String.valueOf(billId),"37","38",conn,usrId,"PURCHBILL");
					postInEGWStatusChange(String.valueOf(billId), cm.getEGWStatusId(
							conn, "PURCHBILL", "Pending"), passedStatusId, conn, usrId,
							"PURCHBILL");
					insertEGActionDetails("PURCHBILL", String.valueOf(billId),
							"Voucher created", conn, usrId);
					// EgwStatus sts=comm.findEgwStatusById(38);
					EgwStatus sts = commonsService.findEgwStatusById(Integer.valueOf(passedStatusId));
					reg.setStatus(sts);
					postInWorksDetail(reg, wd);
					ChartOfAccounts engine = ChartOfAccounts.getInstance();
					Transaxtion txnList[] = new Transaxtion[transactions.size()];
					txnList = (Transaxtion[]) transactions.toArray(txnList);
					if (!engine.postTransaxtions(txnList, conn, vdt)) {
						throw new EGOVRuntimeException("Engine Validation failed");
					}
					reg.setBillstatus("PASSED");
					reg.getEgBillregistermis().setVoucherHeader(commonsService.findVoucherHeaderById(voucherheaderid));
					billsService.updateBillRegister(reg);
				} catch (Exception e) {
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
			//BigDecimal oldAdvAmt = wd.getAdvanceamount();
			BigDecimal oldAdvAdj = wd.getAdvanceadj();
			oldPassAmt = oldPassAmt.add(reg.getPassedamount());
			/*
			 * if(reg.getAdvanceadjusted()!=null)
			 * oldAdvAmt=oldAdvAmt.subtract(reg.getAdvanceadjusted());
			 */
			if (reg.getAdvanceadjusted() != null)
				oldAdvAdj = oldAdvAdj.add(reg.getAdvanceadjusted());
			wd.setPassedamount(oldPassAmt);
			wd.setAdvanceadj(oldAdvAdj);
			// wd.setAdvanceamount(oldAdvAmt);
			worksBillService.updateWorksdetail(wd);
		} catch (Exception e) {
			LOGGER.error("error while Updating worksdetail");
			HibernateUtil.rollbackTransaction();
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
			throw new EGOVRuntimeException(e.getMessage());
		} // TODO Auto-generated method stub

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
			CVoucherHeader voucherheader = (CVoucherHeader) commonsService.findVoucherHeaderById(vhid);
			sbd.setVoucherheader(voucherheader);
			worksBillService.createSupplierbilldetail(sbd);
			// TODO Auto-generated method stub
		} catch (Exception e) {
			LOGGER.error("Error While Inserting Into SupplierBillDeatil");
			throw new EGOVRuntimeException(e.getMessage());
		}
	}

	private List postInVoucherDetail(EgBillregister reg,
			List transactions, Connection conn, Long vhid)
			throws EGOVRuntimeException {
		try {
			double otherRecoveries = 0.0d, tdsAmount = 0.0d;
			Set billdetSet = reg.getEgBilldetailes();
			Iterator billIter = billdetSet.iterator();
			int lineId = 1;
			String net_glcode = "0";
			String adv_glcode = "0";
			if (reg.getExpendituretype().equalsIgnoreCase("Purchase")) {
				// net_glcode=(comm.findCodeByPurposeId(27)).getGlcode();
				adv_glcode = EGovConfig.getProperty("egf_config.xml",
						"SupAdvCode", "", "SupplierCodes");
			}
			while (billIter.hasNext()) {
				EgBilldetails billdet = (EgBilldetails) billIter.next();
				VoucherDetail vd = new VoucherDetail();
				BigDecimal glcodeid = billdet.getGlcodeid();
				Long glid = Long.valueOf(glcodeid.intValue());
				CChartOfAccounts chartOfAcc = commonsService.getCChartOfAccountsById(glid);
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
					// LOGGER.debug("-------------------------otherRecoveries"+billdet.getGlcodeid()+"
					// "+billdet.getCreditamount()+" "+otherRecoveries);
					if (!netPayListString.contains(chartOfAcc.getGlcode())
							&& !chartOfAcc.getGlcode().equalsIgnoreCase(
									adv_glcode)){
						otherRecoveries = otherRecoveries
								+ billdet.getCreditamount().doubleValue();}
				} else {
					transaction
							.setDrAmount(billdet.getDebitamount().toString());
					transaction.setCrAmount("0");
				}
				Set eGbillpaSet = billdet.getEgBillPaydetailes();
				Iterator payeeItr = eGbillpaSet.iterator();
				List reqParams = new ArrayList();
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
									net_glcode)){
								reqData.setTdsId(payeedet.getRecovery().getId()
										.toString());}
							tdsAmount = +payeedet.getCreditAmount()
									.floatValue();
							// LOGGER.debug("Deducting");
							otherRecoveries = -payeedet.getCreditAmount()
									.floatValue();
						}
					} else
						reqData.setDetailAmt(payeedet.getDebitAmount()
								.toString());
					if (payeedet.getRecovery() != null)
						{reqData.setTdsId(payeedet.getRecovery().getId().toString());}
					reqParams.add(reqData);
					// LOGGER.debug(reqData.getDetailAmt());
				}
				try {
					// LOGGER.debug(reqParams.size());

				} catch (Exception e) {

				}
				if (reqParams != null && reqParams.size() > 0)
					{transaction.setTransaxtionParam((ArrayList)reqParams);}
				transactions.add(transaction);
			}

			totalotherRecoveries = otherRecoveries;
			totaltdsAmount = tdsAmount;

		} catch (Exception e) {
			LOGGER.error("Error while Inserting vocherDetail");
			throw new EGOVRuntimeException(e.getMessage());
		}

		return transactions;
	}

	private void updateVoucherMIS(String voucherHeader_voucherDate,EgBillregister reg, Connection conn, int vid,String sourcepath)
			throws EGOVRuntimeException {
		try {
			String wdId = reg.getWorksdetailId();

			Worksdetail wd = (Worksdetail) worksBillService.getWorksDetailById(Integer.valueOf(
					wdId));
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
			if (reg.getEgBillregistermis().getFunctionaryid() != null)
			{
				LOGGER.debug("Functionary id is :::: "+ reg.getEgBillregistermis()
						.getFunctionaryid().getId());
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
			List<PurchaseBillForm> approvedBills, String userId)
			throws Exception {
		List<EgBillregister> billRegisters = new ArrayList();
		for (PurchaseBillForm sbForm : approvedBills) {
			int billId = Integer.parseInt(sbForm.getBillId());
			String voucherHeaderNarraion = "";
			int usrId = Integer.parseInt(userId);
			try {

				EgBillregister reg = gnerateVoucher(billId, usrId,
						voucherHeaderNarraion,sbForm.getBillAprvalDate());
				billRegisters.add(reg);
			} catch (Exception e) {
				LOGGER.error("Error while Generating Voucher");
				throw new EGOVRuntimeException(e.getMessage());

			}
		}
		return billRegisters;
	}

	public PurchaseBillForm getSupplierBillById(Integer billId) {

		PurchaseBillForm sbForm = new PurchaseBillForm();
		sbForm.setBillId(billId.toString());
		try {
			sbForm = getEgBillRegister(sbForm);
		} catch (Exception e) {
			LOGGER.error("Error while Generating Voucher");
		}
		return sbForm;
	}

	public void createWorkFlow(PurchaseBillForm sbForm) throws Exception {
		LOGGER.debug(" Supplier Bill   Creating Workflow----> ");
		try {
			HashMap infrastructureHashMap = new HashMap();
			HashMap applContextInstanceHashMap = new HashMap();
			HashMap applExecutionContextHashMap = new HashMap();
			infrastructureHashMap
					.put("xmlResourceName",
							"org/egov/billsaccounting/workflow/processdefinations/Purchase.xml");
			infrastructureHashMap.put("processDefinitionName", "Purchase");
			applContextInstanceHashMap.put("userId", String.valueOf(sbForm
					.getUserId()));
			applContextInstanceHashMap.put("userName", sbForm.getUserName());
			applContextInstanceHashMap.put("objPK", sbForm.getBillId());
			applContextInstanceHashMap.put("objIdentifier", sbForm.getCSId());

			applExecutionContextHashMap.put("userId", String.valueOf(sbForm
					.getUserId()));
			applExecutionContextHashMap.put("billId", sbForm.getBillId());
			applExecutionContextHashMap.put("objectType", "PurchaseBill");
			LOGGER.debug(" Purchase Bill  Work Flow End ---> ");
		} catch (Exception e) {
			LOGGER.error("Error while Creating work Flow ");
			throw new EGOVRuntimeException("WorkFlow Creation Exception");
		}
	}

	public List<EgBillregister> cancelBill(
			List<PurchaseBillForm> rejectedBills, String userId)
			throws EGOVRuntimeException {

		List<EgBillregister> billRegisters = new ArrayList();
		for (PurchaseBillForm sbForm : rejectedBills) {
			int billId = Integer.parseInt(sbForm.getBillId());

			int usrId = Integer.parseInt(userId);
			try {
				EgBillregister reg = (EgBillregister) billsService.getBillRegisterById(Integer.valueOf(billId));
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
					String cancelledStatusId = cm.getEGWStatusId(conn, "PURCHBILL",
							"Cancelled");
					// postInEGWStatusChange(String.valueOf(billReg.getId().intValue()),"37","39",conn,usrId,"PURCHBILL");
					postInEGWStatusChange(String.valueOf(billReg.getId().intValue()),
							cm.getEGWStatusId(conn, "PURCHBILL", "Pending"),
							cancelledStatusId, conn, usrId, "PURCHBILL");
					insertEGActionDetails("PURCHBILL", String.valueOf(billReg.getId()
							.intValue()), "Bill Cancelled", conn, usrId);
					// EgwStatus sts=comm.findEgwStatusById(39);
					EgwStatus sts = commonsService.findEgwStatusById(Integer.valueOf(
							cancelledStatusId));
					billReg.setStatus(sts);

					billReg.setBillstatus("Cancelled");

				} catch (Exception e) {
					LOGGER.error("Error in Cncellation of Bill");
					throw new EGOVRuntimeException(e.getMessage());
				}

				return billReg;
			
			}
		});
		
	}

	public List getNetPayList() {
		String netPayPurpose = EGovConfig.getProperty("egf_config.xml",
				"purchaseBillPurposeIds", "", "defaultValues");
		// TODO Auto-generated method stub
		String[] netPayPurposeArr = netPayPurpose.split(",");
		CChartOfAccounts netPayAcc;
		netPayListString = new ArrayList();
		List netPayList = new ArrayList();
		try {
			for (int i = 0; i < netPayPurposeArr.length; i++) {
				if (netPayPurposeArr[i] != null) {
					int purposeId = Integer.parseInt(netPayPurposeArr[i]);
					netPayAcc = commonsService.getAccountCodeByPurpose(purposeId).get(0);
					netPayListString.add(netPayAcc.getGlcode());
					LOGGER.info("GlCode is" + netPayAcc.getGlcode());
					netPayList.add(netPayAcc);
				}
			}
		} catch (Exception e) {

		}

		return netPayList;
	}

	public void setBillsService(BillsService billsService) {
		this.billsService = billsService;
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
	
}
