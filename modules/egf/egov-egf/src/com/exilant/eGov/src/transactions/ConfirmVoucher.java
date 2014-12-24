/*
 * Created on May 25, 2006
 * @author Raja 
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.egfRecordStatus;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;

public class ConfirmVoucher extends AbstractTask {	
	private static final Logger LOGGER = Logger.getLogger(ConfirmVoucher.class);
	public ConfirmVoucher(){}
	
	public void execute(String taskName, 
							String gridName, 
							DataCollection dc, 
							Connection conn,
							boolean erroOrNoData,
							boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		try{
	//	this.connection = conn;
		confirmVouchers(dc, conn);
		dc.addMessage("masterInsertUpdate");
		} catch(Exception e) {
			LOGGER.error("Exp="+e.getMessage());			
			dc.addMessage("userFailure", "Transaction Failed");
			throw new TaskFailedException();
		}
	}
	
	/**
	 * This function will confirm the vouchers selected from the input screen.
	 * It will confirm the related vouchers also (Inter fund payment and PT Cess Journal)
	 * @param dc
	 * @param conn
	 * @throws TaskFailedException
	 */
		private void confirmVouchers(DataCollection dc,Connection conn) throws TaskFailedException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String today;		
		String voucherSearchGrid[][] = (String[][])dc.getGrid("voucherSearchGrid");
		VoucherHeader vh = new VoucherHeader();
		String getRefVoucher="SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "+
					"WHERE a.CGN=b.REFCGNO AND b.id=?";
		try{
			 ps=conn.prepareStatement(getRefVoucher);
		}catch(Exception e){
			LOGGER.error("Exp="+e.getMessage());
			throw new TaskFailedException();
		}
		for(int i=0; i < voucherSearchGrid.length; i++) {			
			if(voucherSearchGrid[i][0].equalsIgnoreCase("") || voucherSearchGrid[i][1].equalsIgnoreCase("0")) continue;			
			vh.setId(voucherSearchGrid[i][0]);
			try{
					egfRecordStatus egfstatus= new egfRecordStatus();
					//stmt=conn.createStatement();
					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					EGovernCommon cm=new EGovernCommon();
					today=cm.getCurrentDateTime(conn);			
					egfstatus.setEffectiveDate(formatter.format(sdf.parse(today)));
					egfstatus.setStatus("1");
					egfstatus.setVoucherheaderId(voucherSearchGrid[i][0]);			
					
					//++Sapna
					EGovernCommon egCommon = new EGovernCommon();
					VoucherHeader vhObj = egCommon.getVoucherHeader(conn ,voucherSearchGrid[i][0]);
					egfstatus.setRecord_Type(vhObj.getName()+" "+vhObj.getType());
					egfstatus.setUserId(dc.getValue("current_UserID"));
					//egfstatus.update(conn);
					egfstatus.insert(conn);
					//--Sapna
					
					//Update the original voucher
					vh.setIsConfirmed(""+1);
					vh.update(conn);
					
					//Check if there is any related vouchers
					ps.clearParameters();
					ps.setString(1,voucherSearchGrid[i][0]);
					rs=ps.executeQuery();
					//if any related vouchers exist then we need to confirm that also.
					if(rs.next()){
						egfRecordStatus egfstatusRef= new egfRecordStatus();
						String refVhid=rs.getString(1);
						vh.setId(refVhid);
						egfstatusRef.setEffectiveDate(formatter.format(sdf.parse(today)));
						egfstatusRef.setStatus("1");
						egfstatusRef.setVoucherheaderId(refVhid);
						VoucherHeader vhObjRef = egCommon.getVoucherHeader(conn ,refVhid);
						LOGGER.info("vhObjRef.getName() vhObjRef.getType()::"+vhObjRef.getName()+" "+vhObjRef.getType());
						egfstatusRef.setRecord_Type(vhObjRef.getName()+" "+vhObjRef.getType());
						egfstatusRef.setUserId(dc.getValue("current_UserID"));
						egfstatusRef.insert(conn);	
						vh.setIsConfirmed(""+1);
						vh.update(conn);
					}
			}catch(Exception e){
				LOGGER.error("Exp="+e.getMessage());
				throw new TaskFailedException();
			}
		}
	}
		
		
}
