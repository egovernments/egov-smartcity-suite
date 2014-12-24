/*
 * Created on Jan 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.exilant.eGov.src.domain;

import java.sql.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;

public class SupplierBillDetail
{
	private String id = null;
	private String voucherHeaderId = null;
	private String supplierId = null;
	private String billDate = "1-Jan-1900";
	private String billNumber = null;
	private String otherRecoveries = "0";
	private String mrnNumber = null;
	private String mrnId = null;
	private String mrnDate = "1-Jan-1900";
	private String billAmount = "0";
	private String passedAmount = "0";
	private String approvedBy = null;
	private String payableAccount = null;
	private String narration = null;
	private String worksDetailId = null;
	private String tdsAmount = "0";
	private String tdsPaidToIt = "0";
	private String paidAmount = "0";
	private String advAdjAmt= "0";
	private String isReversed = "0";
	private String assetId = null;
	private String capRev=null;
	private String billId=null;

	private String updateQuery="UPDATE SupplierBillDetail SET";
	private boolean isId = false ;
	private boolean isField = false ;
	private TaskFailedException taskExc;
	private static final Logger LOGGER = Logger.getLogger(SupplierBillDetail.class);
	public void setId(String aId){ id = aId;isId=true;  }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setVoucherHeaderId(String aVoucherHeaderId){ voucherHeaderId = aVoucherHeaderId;  updateQuery = updateQuery + " voucherheaderid = " + voucherHeaderId + ","; isField = true; }
	public void setSupplierId(String aSupplierId){ supplierId = aSupplierId;  updateQuery = updateQuery + " supplierid = " + supplierId + ","; isField = true; }
	public void setbilldate(String aBillDate){ billDate = aBillDate;  updateQuery = updateQuery + " billdate = '" + billDate + "',"; isField = true; }
	public void setBillNumber(String aBillNumber){ billNumber = aBillNumber;  updateQuery = updateQuery + " billnumber = '" + billNumber + "',"; isField = true; }
	public void setOtherRecoveries(String aOtherRecoveries){ otherRecoveries = aOtherRecoveries;  updateQuery = updateQuery + " otherrecoveries = " + otherRecoveries + ","; isField = true; }
	public void setPaidAmount(String aPaidAmount){ paidAmount = aPaidAmount;  updateQuery = updateQuery + " paidamount = " + paidAmount + ","; isField = true; }
	public void setAddedPaidAmount(String aPaidAmount){ paidAmount = aPaidAmount;  updateQuery = updateQuery + " paidamount = paidamount" + paidAmount + ","; isField = true; }
	public void setMRNNumber(String aMRNNumber){ mrnNumber = aMRNNumber;  updateQuery = updateQuery + " mrnnumber = '" + mrnNumber + "',"; isField = true; }
	public void setMrnId(String amrnId){ mrnId = amrnId;  updateQuery = updateQuery + " mrnid = '" + mrnId + "',"; isField = true; }
	public void setMRNDate(String aMRNDate){ mrnDate = aMRNDate;  updateQuery = updateQuery + " mrndate = '" + mrnDate + "',"; isField = true; }
	public void setBillAmount(String aBillAmount){ billAmount = aBillAmount;  updateQuery = updateQuery + " billamount = " + billAmount + ","; isField = true; }
	public void setPassedAmount(String aPassedAmount){ passedAmount = aPassedAmount;  updateQuery = updateQuery + " passedamount = " + passedAmount + ","; isField = true; }
	public void setApprovedBy(String aApprovedBy){ approvedBy = aApprovedBy;  updateQuery = updateQuery + " approvedby = '" + approvedBy + "',"; isField = true; }
	public void setPayableAccount(String aPayableAccount){ payableAccount = aPayableAccount;  updateQuery = updateQuery + " payableaccount = " + payableAccount + ","; isField = true; }
	public void setNarration(String aNarration){ narration = aNarration;  updateQuery = updateQuery + " narration = '" + narration + "',"; isField = true; }
	public void setWorksDetailId(String aWorksDetailId){ worksDetailId = aWorksDetailId;  updateQuery = updateQuery + " worksdetailid = " + worksDetailId + ","; isField = true; }
	public void setTdsAmount(String aTdsAmount){ tdsAmount = aTdsAmount;  updateQuery = updateQuery + " tdsamount = " + tdsAmount + ","; isField = true; }
	public void setTdsPaidToIt(String aTdsPaidToIt){ tdsPaidToIt = aTdsPaidToIt;  updateQuery = updateQuery + " tdspaidtoit = " + aTdsPaidToIt + ","; isField = true; }
	public void setAdvAdjAmount(String aAdjAmount){advAdjAmt = aAdjAmount;  updateQuery = updateQuery + " advAdjAmt = " + aAdjAmount + ","; isField = true; }
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed;  updateQuery = updateQuery + "isReversed = " + isReversed + ","; isField = true; }
	public void setAssetId(String aAssetId){ assetId = aAssetId;  updateQuery = updateQuery + " assetId = '" + assetId + "',"; isField = true; }
	public void setCapRev(String acapRev){ capRev = acapRev;  updateQuery = updateQuery + " cap_rev = '" + capRev + "',"; isField = true; }
	public void setBillId(String abillId){ billId = abillId;  updateQuery = updateQuery + " billId = '" + billId + "',"; isField = true; }

	public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		Statement statement=null;
		try{
			
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("SupplierBillDetail")) );

		String insertQuery = "INSERT INTO SupplierBillDetail (id, voucherheaderid, supplierid, billdate, " +
					"billnumber, otherrecoveries, mrnnumber, mrndate, " +
					"billamount, passedamount, approvedby, payableaccount, narration, WORKSDETAILID,TDSAMOUNT,TDSPAIDTOIT,PAIDAMOUNT,ADVADJAMT,isReversed,assetid,cap_rev,mrnid,BILLID) " +
					"VALUES (" + id + ", " + voucherHeaderId + ", " + supplierId + ", '" + billDate + "', '"
					+ billNumber + "', " + otherRecoveries + ",' "+
					mrnNumber + "', '" + mrnDate + "', " + billAmount + ", "
					+ passedAmount + ", '" + approvedBy + "', " + payableAccount + ", '" + narration
					+ "', " + worksDetailId +","+ tdsAmount+","+tdsPaidToIt+","+paidAmount+","+advAdjAmt+","+isReversed+","+assetId+","+capRev+","+mrnId+","+billId+")";
		LOGGER.info(insertQuery);
		statement = connection.createStatement();
		statement.executeUpdate(insertQuery);
		}catch(Exception e){
			LOGGER.error("Inside exp insert"+e.getMessage());
			throw taskExc;
		}finally{
			statement.close();
		}
		

	}

	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			Statement statement = null;
			try{
				
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				LOGGER.info(updateQuery);
				statement = connection.createStatement();
				statement.executeUpdate(updateQuery);
			}catch(Exception e){
				LOGGER.error("Inside exp update"+e.getMessage());
				throw taskExc;
			}finally{
				statement.close();
			}	
			
			updateQuery="UPDATE SupplierBillDetail SET";
		}
	}
	
	public void reversePositive (Connection connection,double paidAmount ,double adjAmount,double passedAmount )throws SQLException,TaskFailedException
	{
			if(isId){
				Statement statement= null;
				try{
				statement=connection.createStatement();

				String reversePositive="UPDATE supplierBillDetail SET paidAmount=paidAmount+"+paidAmount+
				", advAdjAmt=advAdjAmt+"+adjAmount+", passedAmount=passedAmount+"+passedAmount+"WHERE id="+ id;
				LOGGER.info(reversePositive);
				statement.executeQuery(reversePositive);
				}catch(Exception e){
					LOGGER.error("Inside exp reverse positive"+e.getMessage());
					throw taskExc;
				}finally{
					statement.close();
				}
			}
		}

	public void reverseNegative (Connection connection,double paidAmount ,double adjAmount,double passedAmount )throws SQLException,TaskFailedException
	{
		if(isId){
			Statement statement= null;
			try{

				statement=connection.createStatement();

				String reverseNegative="UPDATE supplierBillDetail SET paidAmount=paidAmount-"+paidAmount+
				", advAdjAmt=advAdjAmt-"+adjAmount+", passedAmount=passedAmount-"+passedAmount+"WHERE id="+ id;
				LOGGER.info(reverseNegative);
				statement.executeQuery(reverseNegative);
			}catch(Exception e){
				LOGGER.error("Inside exp reverse negative :"+e.getMessage());
				throw taskExc;
			}finally{
				statement.close();
			}
		}
	}
	
	public void reverse(Connection connection)throws SQLException,TaskFailedException
	{
		if(isId){
			Statement statement=null;
			try{
				statement=connection.createStatement();	
				String reverseQuery="UPDATE supplierBillDetail SET IsReversed='1' WHERE id="+id;
				LOGGER.info(reverseQuery);
				statement.executeQuery(reverseQuery);
			}catch(Exception e){
				LOGGER.error("Inside exp reverse"+e.getMessage());
				throw taskExc;
			}finally{
				statement.close();
			}
		}
	}
	
	public void reversePaid (Connection connection,double paidAmount )throws SQLException,TaskFailedException
	{
			if(isId){
				Statement statement= null;
				try{
					statement=connection.createStatement();
					String reverseNegative="UPDATE supplierBillDetail SET paidAmount=paidAmount-"+paidAmount+" WHERE id="+ id;
					LOGGER.info(reverseNegative);
					statement.executeQuery(reverseNegative);
					statement.close();
					}catch(Exception e){
						LOGGER.error("Inside exp reversePaid"+e.getMessage());
						throw taskExc;
					}finally{
						statement.close();
					}
			}
	}

	/**
	 * This function will be called in Asset Capitalization and Improvement for checking the bills as used
	 * @param assetId
	 * @param con
	 * @throws Exception
	 */
	public void updateForAsset(String assetId,Connection con) throws Exception
	{
		
		Statement statement = null;
		try{
			String updateQuery="update supplierBillDetail set cap_rev=1 where assetid="+assetId;
			LOGGER.info("updateQuery   "+updateQuery);
			statement = con.createStatement();
			statement.executeUpdate(updateQuery);
		}catch(Exception e){
			LOGGER.error("Inside exp update for Asset"+e.getMessage());
			throw taskExc;
		}finally{
			statement.close();
		}

	}
	/**
	 * This function will get the Supplierbill voucher number associated with a Material Receipt Note.
	 * @param mrnno
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public HashMap getMrnBillInfo(String mrnno,Connection con) throws Exception
	{
		HashMap billInfo=new HashMap();
		String getBillInfo="Select a.billnumber,a.billdate from supplierbilldetail a "+
			"where a.mrnid=(select b.id from egf_mrnheader b where b.id="+mrnno+" and b.BILLRECEIVED=1)";
		LOGGER.info("updateQuery   "+getBillInfo);
		Statement statement = null;
		ResultSet rs=null;
		try{
			statement = con.createStatement();
			rs=statement.executeQuery(getBillInfo);
			if(rs.next())
			{
				billInfo.put("billNo",rs.getString(1));
				billInfo.put("billDate", rs.getString(2));
			}
		}catch(Exception e){
			LOGGER.error("Inside exp reversePaid"+e.getMessage());
			throw taskExc;
		}finally{
			rs.close();
			statement.close();
		}
		LOGGER.info("Bill Informatioms for "+mrnno+" is:"+billInfo);
		return billInfo;

	}



}
