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
 * Created on Jan 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class ContractorBillDetail
{
	private String id = null;
	private String voucherHeaderId = null;
	private String contractorId = null;
	private String billDate = "1-Jan-1900";
	private String billNumber = null;
	private String otherRecoveries = "0";
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
	private String assetId =null;
	private String capRev=null;
	private String billId=null;
	private static final Logger LOGGER=Logger.getLogger(ContractorBillDetail.class);
	private String updateQuery="UPDATE contractorbilldetail SET";
	private boolean isId=false, isField=false;

	public ContractorBillDetail() {}

	public void setId(String aId){ id = aId;isId=true;  }
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setVoucherHeaderId(String aVoucherHeaderId){ voucherHeaderId = aVoucherHeaderId;  updateQuery = updateQuery + " voucherheaderid = " + voucherHeaderId + ","; isField = true; }
	public void setContractorId(String aContractorId){ contractorId = aContractorId;  updateQuery = updateQuery + " contractorid = " + contractorId + ","; isField = true; }
	public void setBilldate(String aBillDate){ billDate = aBillDate;  updateQuery = updateQuery + " billdate = '" + billDate + "',"; isField = true; }
	public void setBillNumber(String aBillNumber){ billNumber = aBillNumber;  updateQuery = updateQuery + " billnumber = '" + billNumber + "',"; isField = true; }
	public void setOtherRecoveries(String aOtherRecoveries){ otherRecoveries = aOtherRecoveries;  updateQuery = updateQuery + " otherrecoveries = " + otherRecoveries + ","; isField = true; }
	public void setPaidAmount(String aPaidAmount){ paidAmount = aPaidAmount;  updateQuery = updateQuery + " paidamount = " + paidAmount + ","; isField = true; }
	public void setAddedPaidAmount(String aPaidAmount){ paidAmount = aPaidAmount;  updateQuery = updateQuery + " paidamount =paidamount" + paidAmount + ","; isField = true; }
	public void setBillAmount(String aBillAmount){ billAmount = aBillAmount;  updateQuery = updateQuery + " billamount = " + billAmount + ","; isField = true; }
	public void setPassedAmount(String aPassedAmount){ passedAmount = aPassedAmount;  updateQuery = updateQuery + " passedamount = " + passedAmount + ","; isField = true; }
	public void setApprovedBy(String aApprovedBy){ approvedBy = aApprovedBy;  updateQuery = updateQuery + " approvedby = '" + approvedBy + "',"; isField = true; }
	public void setPayableAccount(String aPayableAccount){ payableAccount = aPayableAccount;  updateQuery = updateQuery + " payableaccount = " + payableAccount + ","; isField = true; }
	public void setNarration(String aNarration){ narration = aNarration;  updateQuery = updateQuery + " narration = '" + narration + "',"; isField = true; }
	public void setWorksDetailId(String aWorksDetailId){ worksDetailId = aWorksDetailId;  updateQuery = updateQuery + " worksdetailid = " + worksDetailId + ","; isField = true; }
	public void setTdsAmount(String aTdsAmount){ tdsAmount = aTdsAmount;  updateQuery = updateQuery + " tdsamount = " + tdsAmount + ","; isField = true; }
	public void setTdsPaidToIt(String aTdsPaidToIt){ tdsPaidToIt = aTdsPaidToIt;  updateQuery = updateQuery + " tdspaidtoit = " + aTdsPaidToIt + ","; isField = true; }
	public void setAdvAdjAmount(String aAdjAmount){ advAdjAmt = aAdjAmount;  updateQuery = updateQuery + " advAdjAmt = " + aAdjAmount + ","; isField = true; }
	public void setIsReversed(String aIsReversed){ isReversed = aIsReversed;  updateQuery = updateQuery + "isReversed = " + isReversed + ","; isField = true; }
	public void setAssetId(String aAssetId){ assetId = aAssetId;  updateQuery = updateQuery + " assetId = '" + assetId + "',"; isField = true; }
	public void setCapRev(String acapRev){ capRev = acapRev;  updateQuery = updateQuery + " cap_rev = '" + capRev + "',"; isField = true; }
	public void setBillId(String abillId){ billId = abillId;  updateQuery = updateQuery + " billId = '" + billId + "',"; isField = true; }
	@Transactional
	public void insert() throws SQLException
	{
		EGovernCommon commommethods = new EGovernCommon();
		narration = commommethods.formatString(narration);
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ContractorBillDetail")) );

		String insertQuery = "INSERT INTO ContractorBillDetail (Id, VoucherHeaderId, ContractorId, BillNumber, BillDate, " +
						"OtherRecoveries,   PaidAmount, " +
						"BillAmount, PassedAmount, ApprovedBy, PayableAccount, narration,worksdetailid,tdsamount,tdspaidtoit,advAdjAmt,isReversed,assetId,cap_rev,BILLID) " +
							"VALUES(" + id + ", " + voucherHeaderId + ", " + contractorId + ", '" + billNumber + "', '" + billDate
							+ "', " + otherRecoveries + ", " +
							paidAmount+ ", " + billAmount + ", " + passedAmount + ", '" + approvedBy + "', "
							+ payableAccount + ", '" + narration + "',"+
							worksDetailId+","+tdsAmount+","+tdsPaidToIt+","+advAdjAmt+","+isReversed+","+assetId+","+capRev+","+billId+")";
		if(LOGGER.isDebugEnabled())     LOGGER.debug(insertQuery);
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
		statement.executeUpdate();
	}
	@Transactional
	public void update () throws SQLException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			narration = commommethods.formatString(narration);
			updateQuery = updateQuery.substring(0,updateQuery.length()-1);
			updateQuery = updateQuery + " WHERE id = " + id;
			if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery   "+updateQuery);
			Query statement =HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			statement.executeUpdate();
			updateQuery="UPDATE contractorbilldetail SET";
		}
	}
	@Transactional
	public void reversePositive (double paidAmount ,double adjAmount,double passedAmount,double tdsAmount )throws SQLException
	{
			if(isId){
			//	EGovernCommon commommethods=new EGovernCommon();
				Query statement;

				String reversePositive="UPDATE contractorBillDetail SET paidAmount=paidAmount+"+paidAmount+
				", advAdjAmt=advAdjAmt+"+adjAmount+", passedAmount=passedAmount+"+passedAmount+", tdsAmount=tdsAmount+"+tdsAmount+"WHERE id="+ id;
				if(LOGGER.isDebugEnabled())     LOGGER.debug(reversePositive);
				statement = HibernateUtil.getCurrentSession().createSQLQuery(reversePositive);
				statement.executeUpdate();
			}
	}
	@Transactional
	public void reverseNegative (double paidAmount ,double adjAmount,double passedAmount,double tdsAmount )throws SQLException
	{
			if(isId){
			//	EGovernCommon commommethods=new EGovernCommon();
				Query statement;
				String reverseNegative="UPDATE contractorBillDetail SET paidAmount=paidAmount-"+paidAmount+
				", advAdjAmt=advAdjAmt-"+adjAmount+", passedAmount=passedAmount-"+passedAmount+", tdsAmount=tdsAmount-"+tdsAmount+"WHERE id="+ id;
				if(LOGGER.isDebugEnabled())     LOGGER.debug(reverseNegative);
				statement = HibernateUtil.getCurrentSession().createSQLQuery(reverseNegative);
				statement.executeUpdate();
			}
	}
	@Transactional
	public void reverse()throws SQLException
	{
			if(isId){
			//	EGovernCommon commommethods=new EGovernCommon();
				Query statement;
				String reverseQuery="UPDATE contractorBillDetail SET IsReversed=1 WHERE id="+id;
				if(LOGGER.isDebugEnabled())     LOGGER.debug(reverseQuery);
				statement = HibernateUtil.getCurrentSession().createSQLQuery(reverseQuery);
				statement.executeUpdate();
			}
	}
	@Transactional
	public void reversePaid (double paidAmount )throws SQLException
	{
		if(isId){
		//	EGovernCommon commommethods=new EGovernCommon();
			Query statement;
			String reverseNegative="UPDATE contractorBillDetail SET paidAmount=paidAmount-"+paidAmount+" WHERE id="+ id;
			if(LOGGER.isDebugEnabled())     LOGGER.debug(reverseNegative);
			statement = HibernateUtil.getCurrentSession().createSQLQuery(reverseNegative);
			statement.executeUpdate();
		}
	}

		/**
	 * This function will return the bill info for the specified Asset,Work order and type of work.
	 * Can be used for impovement
	 * @param assetid
	 * @param workorderid
	 * @param workType
	 * @param con
	 * @return billInfo
	 * @throws Exception
	 */
	public ArrayList getBillInfo(int assetid,String workorderid,int workType) throws Exception
	{
		ArrayList billDetail=new ArrayList();
		HashMap billValue=new HashMap(2);
		List<Object[]> rs=null;
		Query stmt=null;
		int i=0;
		String billNo="";
		double billAmt=0.0;
		java.util.Date billDate=null;


		String qryStr="Select sbd.id as \"billid\",sbd.billnumber ,sbd.billdate as \"billdate\",sbd.passedamount From contractorbilldetail sbd,worksdetail wd,egw_workstype worktype,voucherheader vh"+
						" Where assetid="+assetid+" and worksdetailid in ("+workorderid+") and vh.id=sbd.voucherheaderid and vh.status=0 and wd.id=sbd.WORKSDETAILID and worktype.id=wd.TYPE and worktype.id="+workType+" and (sbd.cap_rev IS NULL OR sbd.cap_rev=0)"+
						"UNION Select sbd.id as \"billid\",sbd.billnumber ,sbd.billdate as \"billdate\",sbd.passedamount From supplierbilldetail sbd,worksdetail wd,egw_workstype worktype,voucherheader vh"+
						" Where assetid="+assetid+" and worksdetailid in ("+workorderid+") and vh.id=sbd.voucherheaderid and vh.status=0 and wd.id=sbd.WORKSDETAILID and worktype.id=wd.TYPE and worktype.id="+workType+" and (sbd.cap_rev IS NULL OR sbd.cap_rev=0) order by  \"billdate\" desc";
		if(LOGGER.isInfoEnabled())     LOGGER.info("Query is :"+qryStr);
		rs=HibernateUtil.getCurrentSession().createSQLQuery(qryStr).list();

		for(Object[] element : rs){
			billValue=new HashMap();
			billNo=element[1].toString();
			billDate=new SimpleDateFormat("dd/MM/yyyy").parse(element[2].toString());
			billAmt=Double.parseDouble(element[3].toString());
			billValue.put("Billno",billNo);
			billValue.put("BillDate",billDate);
			billValue.put("BillAmt",billAmt);
			billDetail.add(i,billValue);
			i++;
		}
		//if(LOGGER.isDebugEnabled())     LOGGER.debug("Return Array List is :"+billDetail);
		return billDetail;
	}
	@Transactional
	public void updateForAsset(String assetId) throws Exception
	{

		String updateQuery="update contractorbilldetail set cap_rev=1 where assetid="+assetId+" and worksdetailid in (select id from worksdetail where type=1 or type=2)";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery   "+updateQuery);
		Query statement = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
		statement.executeUpdate();

	}
	/**
	 * To get map of assets
	 * @param catId
	 * @param status
	 * @param type
	 * @param consupId
	 * @param fundId
	 * @param assetType
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public Map getAssetList(String catId,String status,String type,String consupId,String fundId,String assetType) throws Exception
	{

		String categoryCheck= (catId.equalsIgnoreCase("")) ? "" : " and b.id="+catId;
		String statusCheck=(status.equalsIgnoreCase("")) ? "" : "and a.STATUSID="+status;
		String typeCheck=(type.equalsIgnoreCase("")) ? "" : "and assetType.name='"+type+"'";
		String relationCheck=(consupId.equalsIgnoreCase("")) ? "" : "and a.ASSETID in (select eg_asset.assetid from eg_asset,worksdetail c, relation d,eg_asset_po e "
				  +" where e.ASSETID=eg_asset.ASSETID and e.WORKORDERID=c.ID and c.RELATIONID=d.ID and  d.ID="+consupId+")";
		String fundCheck=(fundId.equalsIgnoreCase("")) ? "" : "and a.ASSETID in (select eg_asset.assetid from eg_asset,worksdetail c, eg_asset_po e "
				  +" where e.ASSETID=eg_asset.ASSETID and e.WORKORDERID=c.ID and c.fundid="+fundId+")";
		String assetTypeCheck=(assetType.equalsIgnoreCase("")) ? "" : "and assetType.id="+assetType;
		Map assetList=new LinkedHashMap();
		List<Object[]> rs=null;
		Query stmt=null;

		String qryStr="select a.assetid,a.code from eg_asset a,eg_assetcategory b,eg_asset_type assetType  where"
		+"  a.CATEGORYID=b.ID  and b.ASSETTYPE_ID=assetType.ID "+categoryCheck+" "+statusCheck+" "+typeCheck+" "+relationCheck+" "+fundCheck+" "+assetTypeCheck+" order by a.code";

		if(LOGGER.isInfoEnabled())     LOGGER.info("Query is :"+qryStr);
		rs=HibernateUtil.getCurrentSession().createSQLQuery(qryStr).list();
		for(Object[] element : rs){
				assetList.put(Integer.valueOf(element[0].toString()),element[1].toString());
		}
		
		return assetList;
	}
	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public ArrayList getConSupList() throws Exception
	{
		ArrayList<LabelValueBean> conSupList=new ArrayList<LabelValueBean>();
		LabelValueBean lvBean=null;
		List<Object[]> rs=null;
		Query stmt=null;

		String qryStr="select eg_asset.assetid,d.name from eg_asset,worksdetail c,relation d,eg_asset_po e  where e.ASSETID=eg_asset.ASSETID and e.WORKORDERID=c.ID and c.RELATIONID=d.ID order by eg_asset.code";
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Query is :"+qryStr);
		rs=HibernateUtil.getCurrentSession().createSQLQuery(qryStr).list();
		for(Object[] element : rs){
			lvBean=new LabelValueBean();
			lvBean.setId(Integer.parseInt(element[0].toString()));
			lvBean.setName(element[1].toString());
			conSupList.add(lvBean);
		}
		return conSupList;
	}

}
