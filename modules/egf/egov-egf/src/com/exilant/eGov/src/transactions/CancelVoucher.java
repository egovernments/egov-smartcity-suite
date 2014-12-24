/*
 * Created on May 25, 2006
 * @author 
 */

package com.exilant.eGov.src.transactions;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.client.WorksBillDelegate;
import org.egov.billsaccounting.client.WorksBillForm;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.billsaccounting.services.WorksBillService;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.model.bills.EgBillregister;
import org.egov.services.bills.BillsService;
import org.egov.services.contra.ContraService;
import org.egov.services.instrument.InstrumentService;
import org.egov.utils.GetEgfManagers;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.BillRegisterBean;
import com.exilant.eGov.src.domain.ContractorBillDetail;
import com.exilant.eGov.src.domain.EgfStatusChange;
import com.exilant.eGov.src.domain.SalaryBillDetail;
import com.exilant.eGov.src.domain.SupplierBillDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.WorksDetail;
import com.exilant.eGov.src.domain.egfRecordStatus;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class CancelVoucher extends AbstractTask {	
    private final static Logger LOGGER=Logger.getLogger(CancelVoucher.class);
	private Connection connection;
	private UserService userService;
//	private String id = "0";//Added by sumit for EGF_RECORD_STATUS table
	private static	BillsService billsService;
	//public void setId(String aId){ id = aId; }
	public CancelVoucher(){}
	
	public void execute(String taskName, 
							String gridName, 
							DataCollection dc, 
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		try{ 
		this.connection = conn;
		cancelVouchers(dc, conn);
		dc.addMessage("eGovSuccess", "Cancel Voucher");
		} catch(Exception e) {
			LOGGER.error("Exp="+e.getMessage());			
			dc.addMessage("eGovFailure", "Cancel Voucher");
			throw new TaskFailedException();
		}
	}
	
	/**
	 * This function will cancel the vouchers selected from the input screen.
	 * It will cancel the related vouchers also (Inter fund payment and PT Cess Journal)
	 * @param dc
	 * @param conn
	 * @throws TaskFailedException
	 */
		private void cancelVouchers(DataCollection dc,Connection conn) throws TaskFailedException{
		this.connection = conn;
        PreparedStatement ps=null;
		ResultSet rs=null;
		String today;		
		String voucherSearchGrid[][] = (String[][])dc.getGrid("voucherSearchGrid");
		VoucherHeader vh = new VoucherHeader();
		Statement stmt=null;
		LOGGER.info("before preparedstatment");
		String getRefVoucher="SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "+
					"WHERE a.CGN=b.REFCGNO AND b.id=?";
		try{ LOGGER.info("before preparedstatment 1");
			 ps=conn.prepareStatement(getRefVoucher);
		}catch(Exception e){LOGGER.error("in prepared statement exception"+e.getMessage());
			throw new TaskFailedException();
		}
		LOGGER.info("CANCEL VOUCHER 1:  "+voucherSearchGrid.length);
		 
		for(int i=0; i < voucherSearchGrid.length; i++) {	LOGGER.info(" i :"+ i);		
			if(voucherSearchGrid[i][0].equalsIgnoreCase("") || voucherSearchGrid[i][1].equalsIgnoreCase("0")) continue;			
			vh.setId(voucherSearchGrid[i][0]);
			try{
				egfRecordStatus egfstatus= new egfRecordStatus();
				stmt=conn.createStatement();
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				EGovernCommon cm=new EGovernCommon();
				today=cm.getCurrentDate(conn);			
				//String dateformat = formatter.format(sdf.parse(today));
				LOGGER.info("Update the egf_record_status table of original voucher");
				egfstatus.setEffectiveDate(formatter.format(sdf.parse(today)));
				egfstatus.setStatus("4");
				egfstatus.setVoucherheaderId(voucherSearchGrid[i][0]);		
				egfstatus.update(conn);	
				LOGGER.info("Update the original voucher");
				vh.setStatus(""+4);
				vh.update(conn);
				
				//Check if there is any related vouchers
				ps.clearParameters();
				ps.setString(1,voucherSearchGrid[i][0]);
				rs=ps.executeQuery();
				//LOGGER.info("if any related vouchers exist then we need to  that also.");
				if(rs.next()){
					egfRecordStatus egfstatusRef= new egfRecordStatus();
					String refVhid=rs.getString(1);
					vh.setId(refVhid);
					egfstatusRef.setEffectiveDate(formatter.format(sdf.parse(today)));
					egfstatusRef.setStatus("4");
					egfstatusRef.setVoucherheaderId(refVhid);			
					egfstatusRef.update(conn);	
					vh.setStatus(""+4); LOGGER.info("before voucher update");
					vh.update(conn);
				}
				stmt.close();
			}catch(Exception e){
				LOGGER.error("Exp in cancel voucher"+e.getMessage());
				throw new TaskFailedException();
			}
			
		}
		//cancel(dc,conn);
	}
		
		private void cancel(DataCollection dc,Connection conn) throws TaskFailedException
		{
			String voucherSearchGrid[][] = (String[][])dc.getGrid("voucherSearchGrid");
			ResultSet rs=null;
			ResultSet rs1=null,billRs=null;
			Statement stmt1=null;
			String bill="";//,vchrNo="",vchrDate="";
			try{
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date dt=new Date();
				EGovernCommon cm=new EGovernCommon();
				String today=cm.getCurrentDate(conn);			
				dt = sdf.parse( today );
				String dateformat = formatter.format(dt);
				LOGGER.debug(dateformat);
				
			for(int i=0; i < voucherSearchGrid.length; i++)
			{
				if(voucherSearchGrid[i][0].equalsIgnoreCase("") || voucherSearchGrid[i][1].equalsIgnoreCase("0")) continue;
					String vhId=voucherSearchGrid[i][0];
					String query="";
					/*query="select type,name,vouchernumber,VoucherDate from voucherheader where id="+vhId;
					LOGGER.info(query);
						stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						rs1=stmt.executeQuery(query);

						if(rs1.next())
						{	bill=rs1.getString("name");
							rs1.close();
						}*/
					PersistenceService<CVoucherHeader, Long> vhpersistenceSer = new PersistenceService<CVoucherHeader, Long>();
					vhpersistenceSer.setSessionFactory(new SessionFactory());
					vhpersistenceSer.setType(CVoucherHeader.class);
					CVoucherHeader voucherHeader= vhpersistenceSer.findById(Long.valueOf(vhId), false);
					bill = voucherHeader.getName();
						LOGGER.debug("bill:"+bill);

					stmt1=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					if(bill.equalsIgnoreCase("Contractor Journal"))
					{
						WorksDetail wd=new WorksDetail();
						com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
						LOGGER.debug("inside Contractor Journal" );
						billRs=stmt1.executeQuery("SELECT passedamount,advadjamt,worksdetailid,BILLID FROM CONTRACTORBILLDETAIL where voucherheaderid="+vhId);
						LOGGER.info("SELECT passedamount,advadjamt,worksdetailid FROM CONTRACTORBILLDETAIL where voucherheaderid="+vhId);
						LOGGER.debug("Check1");
						if(billRs.next()){

							wd.setId(billRs.getString("worksdetailid"));
						//	wd.setPassedAmount("-"+billRs.getString("passedamount"));
						//	wd.setAdvanceAdjustment("-"+billRs.getString("advadjamt"));
							double advadjamt=0;
							double passedamount=0;
							if(billRs.getString("advadjamt")!=null &&! billRs.getString("advadjamt").equalsIgnoreCase("0"))
								advadjamt=Double.parseDouble(billRs.getString("advadjamt"));
							if(billRs.getString("passedamount")!=null && !billRs.getString("passedamount").equalsIgnoreCase("0"))
								passedamount=Double.parseDouble(billRs.getString("passedamount"));
							wd.reverseNegative(conn,0,advadjamt, passedamount, 0);
							//wd.update(conn);
			 				b.setBillStatus("PENDING");
							//b.setBillStatusId("33");
			 				b.setBillStatusId(cm.getEGWStatusId(conn, "WORKSBILL", "Pending"));
							b.setId(billRs.getString("BILLID"));
							b.update(conn);
							createNewWorkFlow(billRs.getString("BILLID"));
							billRs.close();
						}  
					}
					else if(bill.equalsIgnoreCase("Supplier Journal"))
					{
						LOGGER.debug("inside Supplier Journal" );
						WorksDetail wd=new WorksDetail();
						com.exilant.eGov.src.domain.BillRegisterBean b=new com.exilant.eGov.src.domain.BillRegisterBean();
						billRs=stmt1.executeQuery("SELECT passedamount,advadjamt,worksdetailid,BILLID FROM SUPPLIERBILLDETAIL where voucherheaderid="+vhId);
						if(billRs.next()){
							wd.setId(billRs.getString("worksdetailid"));
						//	wd.setPassedAmount("-"+billRs.getString("passedamount"));
						//	wd.setAdvanceAdjustment("-"+billRs.getString("advadjamt"));
							double advadjamt=0;
							double passedamount=0;
							if(billRs.getString("advadjamt")!=null &&! billRs.getString("advadjamt").equalsIgnoreCase("0"))
								advadjamt=Double.parseDouble(billRs.getString("advadjamt"));
							if(billRs.getString("passedamount")!=null &&! billRs.getString("passedamount").equalsIgnoreCase("0"))
								passedamount=Double.parseDouble(billRs.getString("passedamount"));
							
							wd.update(conn);
							wd.reverseNegative(conn,0,advadjamt, passedamount, 0);
							b.setBillStatus("PENDING");
							//b.setBillStatusId("37");
							b.setBillStatusId(cm.getEGWStatusId(conn, "PURCHBILL", "Pending"));
							b.setId(billRs.getString("BILLID"));
							b.update(conn);
							createNewWorkFlow(billRs.getString("BILLID"));
							billRs.close();
						}
					}
					else if(bill.equalsIgnoreCase("Salary Journal"))
					{ /** 
					  * 1.This method will Delete the entry From SalryBilldetail Table
					  * 2.Adds a Record in the egwstatuschange
					  * 3.updates eg_billregister sttus to approved and statusid to 29
					  */
						
					  
						LOGGER.debug("Inside Salary Journal Cancel Voucher");
						//SalaryBillDetail sbd=new SalaryBillDetail();
						String billIdQuery="Select billid from SalaryBillDetail Where VoucherHeaderid="+vhId;
						String sbDeletelQuery="delete from SalaryBillDetail where VoucherHeaderId= "+vhId;
						BillRegisterBean billBean=new BillRegisterBean();
						LOGGER.debug(billIdQuery);
						Statement st=conn.createStatement();
						ResultSet brs=st.executeQuery(billIdQuery);
						if(brs.next())
						{
						billBean.setId(brs.getString(1));
						}
						LOGGER.debug(billBean.getId());
					    if(billBean.getId()!=null && ! billBean.getId().equalsIgnoreCase(""))
					    {
					//	egfRecordStatus ers=new egfRecordStatus();
						EgfStatusChange egfst=new EgfStatusChange();
						//egfst.setFromstatus("30");
						//egfst.setTostatus("29");
						egfst.setFromstatus(cm.getEGWStatusId(connection, "SALBILL", "Passed"));
						egfst.setTostatus(cm.getEGWStatusId(connection, "SALBILL", "Approved"));
						egfst.setModuleid(billBean.getId());
						
						egfst.setLastmodifieddate(dateformat);
						egfst.setModuletype("Salary Journal");
						egfst.setCreatedby(dc.getValue("egUser_id"));
						//egfst.setCreatedby()
						egfst.insert(conn);
						
						billBean.setBillStatus("Approved");
						//billBean.setBillStatusId("29");
						billBean.setBillStatusId(cm.getEGWStatusId(connection, "SALBILL", "Approved"));
						billBean.update(conn);
						
						LOGGER.debug(sbDeletelQuery);
						st.executeUpdate(sbDeletelQuery);
					    }
					    
					}
					else if(bill.equalsIgnoreCase("Pay In Slip"))
					{
						//ChequeDetail cd=new ChequeDetail();
						//cd.cancel(conn,vchrNo,vchrDate);
						InstrumentService instrumentService=new InstrumentService();
						instrumentService.setServicesForLegacyClasses();
						ContraService contraService = new ContraService();
						PersistenceService persistenceService = new PersistenceService();
						persistenceService.setSessionFactory(new SessionFactory());
						contraService.setPersistenceService(persistenceService);
						contraService.setInstrumentService(instrumentService);
						contraService.editInstruments(Long.valueOf(vhId));
						HibernateUtil.getCurrentSession().flush();
					}
					else
					{
						//Back updation  for Remittance Recovery tables
						String remittanceQuery="SELECT id, remittancegldtlid, remittedamt FROM EG_REMITTANCE_DETAIL WHERE remittanceid IN(SELECT id FROM EG_REMITTANCE WHERE paymentvhid="+vhId+")";
						LOGGER.info(remittanceQuery);
						Statement rmtSt=conn.createStatement();
						ResultSet rmtrs=rmtSt.executeQuery(remittanceQuery);						
						while(rmtrs.next())
						{							
							int rmtGldtlId=rmtrs.getInt("remittancegldtlid");
							String remittedAmt=rmtrs.getString("remittedamt");
							String updateRmtGlDtlQuery="update EG_REMITTANCE_GLDTL set remittedamt=remittedamt-'"+remittedAmt+"' where id="+rmtGldtlId;
							Statement updateRmtSt=conn.createStatement();
							LOGGER.info(updateRmtGlDtlQuery);
							updateRmtSt.executeUpdate(updateRmtGlDtlQuery);
						}
						
						//Back updation  for Advance disbursement - payroll integration
						String salAdvQuery="SELECT id FROM EGPAY_SALADVANCES where voucher_header_id="+vhId;
						String updateSalAdvQuery="update EGPAY_SALADVANCES set voucher_header_id=null, status=52 where voucher_header_id="+vhId;
						LOGGER.info(salAdvQuery);
						Statement saSt=conn.createStatement();
						ResultSet sars=saSt.executeQuery(salAdvQuery);
						while(sars.next())
						{
							EgfStatusChange egfst=new EgfStatusChange();
							//egfst.setFromstatus("53");
							//egfst.setTostatus("52");
							egfst.setFromstatus(cm.getEGWStatusId(conn,"Salaryadvance","Disbursed"));
							egfst.setTostatus(cm.getEGWStatusId(conn,"Salaryadvance","Sanctioned"));
							egfst.setModuleid(sars.getString("ID"));
							egfst.setLastmodifieddate(dateformat);
							egfst.setModuletype("Salaryadvance");
							egfst.setCreatedby(dc.getValue("egUser_id"));
							egfst.insert(conn);
						}
						LOGGER.info(updateSalAdvQuery);
						saSt.executeUpdate(updateSalAdvQuery);
						
						//Back updation for C Bills
						String otherBillQuery="select billid from OTHERBILLDETAIL where payvhid="+vhId;
						String updateOtherBillQuery="update OTHERBILLDETAIL set payvhid=null where payvhid= "+vhId;
						BillRegisterBean billReg=new BillRegisterBean();
						LOGGER.info(otherBillQuery);
						Statement st1=conn.createStatement();
						ResultSet obrs=st1.executeQuery(otherBillQuery);
						while(obrs.next())
						{
							billReg.setId(obrs.getString(1));
							//billReg.setBillStatus("PASSED");
							billReg.setBillStatus("Voucher Created");
							billReg.setBillStatusId(cm.getEGWStatusId(conn,"CBILL","Voucher Created"));
							billReg.setLastModifiedBy(Integer.parseInt(dc.getValue("egUser_id")));
							billReg.setLastModifiedDate(dateformat);
							billReg.update(conn);
							
							EgfStatusChange egfst=new EgfStatusChange();
							egfst.setFromstatus(cm.getEGWStatusId(connection, "CBILL", "PAYMENT APPROVED"));
							egfst.setTostatus(cm.getEGWStatusId(connection, "CBILL", "Voucher Created"));
							egfst.setModuleid(obrs.getString(1));
							egfst.setLastmodifieddate(dateformat);
							egfst.setModuletype("CBILL");
							egfst.setCreatedby(dc.getValue("egUser_id"));
							egfst.insert(conn);
						}				
						LOGGER.info(updateOtherBillQuery);
						st1.executeUpdate(updateOtherBillQuery);
						
						WorksDetail wd=new WorksDetail();
						SalaryBillDetail sb=new SalaryBillDetail();
						SupplierBillDetail sbd=new SupplierBillDetail();
						ContractorBillDetail cbd =new ContractorBillDetail();
						query="SELECT worksdetailid,paidamount,type,salarybillid,contractorbillid,supplierbillid FROM SUBLEDGERPAYMENTHEADER WHERE voucherheaderid="+vhId+"  and  isreversed<>1";
						rs=stmt1.executeQuery(query);
						LOGGER.info(query);
						while(rs.next())
						{
							double advanceAmt=0.00,paidAmt=0.00;
							wd.setId(rs.getString("worksdetailid"));
							LOGGER.debug("iside while"+rs.getString("type"));
							
							if(rs.getString("type").equalsIgnoreCase("Salary"))
							{
								LOGGER.debug("salary payment");
								sb.setId(rs.getString("salarybillid"));
								sb.setpaidAmount("-"+rs.getString("paidamount"));
								sb.update(conn);
								String billIdQuery="Select billid from SALARYBILLDETAIL Where id="+rs.getString("salarybillid");
								BillRegisterBean billBean=new BillRegisterBean();
								LOGGER.info(billIdQuery);
								Statement st=conn.createStatement();
								ResultSet brs=st.executeQuery(billIdQuery);
								if(brs.next())
								{
									billBean.setId(brs.getString(1));
								}
								LOGGER.debug(billBean.getId());
							    if(billBean.getId()!=null && ! billBean.getId().equalsIgnoreCase(""))
							    {
									EgfStatusChange egfst=new EgfStatusChange();
									//egfst.setFromstatus("31");
									//egfst.setTostatus("30");
									egfst.setFromstatus(cm.getEGWStatusId(connection, "SALBILL", "Paid"));
									egfst.setTostatus(cm.getEGWStatusId(connection, "SALBILL", "Passed"));
									egfst.setModuleid(billBean.getId());
									egfst.setLastmodifieddate(dateformat);
									egfst.setModuletype("SALBILL");
									egfst.setCreatedby(dc.getValue("egUser_id"));
									egfst.insert(conn);
									
									billBean.setBillStatus("PASSED");
									//billBean.setBillStatusId("30");
									billBean.setBillStatusId(cm.getEGWStatusId(connection, "SALBILL", "Passed"));
									billBean.setLastModifiedBy(Integer.parseInt(dc.getValue("egUser_id")));
									billBean.setLastModifiedDate(dateformat);
									billBean.update(conn);
							    }		
							}
							else if(rs.getString("type").equalsIgnoreCase("Contractor"))
							{
								LOGGER.debug("Contractor payment");
								paidAmt=rs.getDouble("paidamount");
								wd.reverseNegative(conn,paidAmt,0,0,advanceAmt);
								cbd.setId(rs.getString("contractorbillid"));
								cbd.setAddedPaidAmount("-"+rs.getString("paidamount"));
								cbd.update(conn);
								
								// Added by Ilayaraja.p on 01-sep-2008
								String billIdQuery="Select billid from contractorbilldetail Where id="+rs.getString("contractorbillid");
								BillRegisterBean billBean=new BillRegisterBean();
								LOGGER.info(billIdQuery);
								Statement st=conn.createStatement();
								ResultSet brs=st.executeQuery(billIdQuery);
								if(brs.next())
								{
									billBean.setId(brs.getString(1));
								}
								LOGGER.debug(billBean.getId());
							    if(billBean.getId()!=null && ! billBean.getId().equalsIgnoreCase(""))
							    {
									EgfStatusChange egfst=new EgfStatusChange();
									egfst.setFromstatus(cm.getEGWStatusId(connection, "WORKSBILL", "Paid"));
									egfst.setTostatus(cm.getEGWStatusId(connection, "WORKSBILL", "Passed"));
									egfst.setModuleid(billBean.getId());
									egfst.setLastmodifieddate(dateformat);
									egfst.setModuletype("WORKSBILL");
									egfst.setCreatedby(dc.getValue("egUser_id"));
									egfst.insert(conn);
									
									billBean.setBillStatus("PASSED");
									billBean.setBillStatusId(cm.getEGWStatusId(connection, "WORKSBILL", "Passed"));
									billBean.setLastModifiedBy(Integer.parseInt(dc.getValue("egUser_id")));
									billBean.setLastModifiedDate(dateformat);
									billBean.update(conn);
							    }
							}
							else if (rs.getString("type").equalsIgnoreCase("Supplier"))
							{
								LOGGER.debug("Supplier payment");
								paidAmt=rs.getDouble("paidamount");
								wd.reverseNegative(conn,paidAmt,0,0,advanceAmt);
								sbd.setId(rs.getString("supplierbillid"));
								sbd.setAddedPaidAmount("-"+rs.getString("paidamount"));
								sbd.update(conn);
								// Added by Ilayaraja.p on 01-sep-2008
								String billIdQuery="Select billid from supplierbilldetail Where id="+rs.getString("supplierbillid");
								BillRegisterBean billBean=new BillRegisterBean();
								LOGGER.info(billIdQuery);
								Statement st=conn.createStatement();
								ResultSet brs=st.executeQuery(billIdQuery);
								if(brs.next())
								{
									billBean.setId(brs.getString(1));
								}
								LOGGER.debug(billBean.getId());
							    if(billBean.getId()!=null && ! billBean.getId().equalsIgnoreCase(""))
							    {
									EgfStatusChange egfst=new EgfStatusChange();
									egfst.setFromstatus(cm.getEGWStatusId(connection, "PURCHBILL", "Paid"));
									egfst.setTostatus(cm.getEGWStatusId(connection, "PURCHBILL", "Passed"));
									egfst.setModuleid(billBean.getId());
									egfst.setLastmodifieddate(dateformat);
									egfst.setModuletype("PURCHBILL");
									egfst.setCreatedby(dc.getValue("egUser_id"));
									egfst.insert(conn);
									
									billBean.setBillStatus("PASSED");
									billBean.setBillStatusId(cm.getEGWStatusId(connection, "PURCHBILL", "Passed"));
									billBean.setLastModifiedBy(Integer.parseInt(dc.getValue("egUser_id")));
									billBean.setLastModifiedDate(dateformat);
									billBean.update(conn);
							    }
							}
							else if(rs.getString("type").equalsIgnoreCase("Advance"))
							{		LOGGER.debug("Advance payment");
									advanceAmt=rs.getDouble("paidamount");
									wd.reverseNegative(conn,paidAmt,0,0,advanceAmt);
							}
							else
								LOGGER.info("in else...... all condition failed");
						}
					}

		 }
			}catch(Exception e){
				LOGGER.error("Exception in payment:"+e);				
				throw new TaskFailedException();
				}
		}
		
		private void createNewWorkFlow(String billRegId)throws TaskFailedException {
			WorksBillDelegate wDelegate=new WorksBillDelegate();
			WorksBillForm wbForm=new WorksBillForm();
			 
			wbForm.setBillId(billRegId);
			
			try{
				
				LOGGER.info(" i am in CommonsManagerHome");
				WorksBillService worksBillService=(WorksBillService)GetEgfManagers.getWorksBillService();
				EgBillregister billReg=billsService.getBillRegisterById(Integer.valueOf(billRegId));
				wbForm=(WorksBillForm)wDelegate.getEgBillRegister(wbForm);
				String wid=billReg.getWorksdetailId();
				Worksdetail wd =(Worksdetail)worksBillService.getWorksDetailById(Integer.valueOf(wid));
				wbForm.CSId=wd.getRelation().getId().toString(); 
				int userId=billReg.getCreatedBy().getId();
				wbForm.setUserId(userId);
				try{  
					User user=(User)userService.getUserByID(userId);
					 String  username=user.getUserName();
					 LOGGER.info(username);
					 wbForm.setUserName(username);
				}
				catch(Exception e)
				{
					LOGGER.error("Exp in User Manager:"+e.getMessage());
					throw new TaskFailedException(e.getMessage());
				}
				wDelegate.createWorkFlow(wbForm);
			}catch(Exception e)
			{				
				LOGGER.error("Exp in createNewWorkFlow:"+e.getMessage());
				throw new TaskFailedException(e.getMessage());
			}
			
			
			// TODO Auto-generated method stub
			
		}

		public void setUserService(UserService userService) {
			this.userService = userService;
		}

		public static void setBillsService(BillsService billsService) {
			CancelVoucher.billsService = billsService;
		}

		
}
