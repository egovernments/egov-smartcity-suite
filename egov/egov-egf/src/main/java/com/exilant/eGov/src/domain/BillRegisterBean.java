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
package com.exilant.eGov.src.domain;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
* @author Iliyaraja
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/
@Transactional(readOnly=true)
public class BillRegisterBean
{

	private static final Logger LOGGER=Logger.getLogger(BillRegisterBean.class);
	private String id=null;
	private String billNumber=null;
	private String billDate=null;
	private String billStatus=null;
	private String fieldId=null;
	private String worksDetailId=null;
	private double billAmount=0.0;
	private String billNarration="";
	private String expenditureType=null;
	private String billType=null;
	private double passedAmount=0.0;
	private double advanceAdjusted=0.0;
	private int createdby;
	private int lastModifiedBy;
	private String createdDate="";
	private String lastModifiedDate="";
	private String billStatusId=null;
	private TaskFailedException taskExc;
	private String updateQuery="UPDATE EG_BILLREGISTER SET";
	private boolean isId=false,isField=false;
	private boolean isSelected=false;
	private String billDeptName = null;
	public BillRegisterBean() {}


  

			/**
			 * This Function is for insertion
			 * @param connection
			 * @throws TaskFailedException
			 */
	@Transactional
			public void insert() throws TaskFailedException
			{
				Query psmt=null;
				Query psmt1=null;
			   try{
					SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
					Date billDatee = (Date)formatter.parse(this.billDate); 
				
					
					setId(String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLREGISTER")));
							
					String insertQuery = "INSERT INTO EG_BILLREGISTER (ID, BILLNUMBER, BILLDATE, BILLAMOUNT,FIELDID,WORKSDETAILID,BILLSTATUS, NARRATION,PASSEDAMOUNT,BILLTYPE,EXPENDITURETYPE,ADVANCEADJUSTED,CREATEDBY,CREATEDDATE,StatusId)"
					+"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					psmt = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
					psmt.setString(1,this.id);
					psmt.setString(2,this.billNumber);
					psmt.setDate(3,billDatee);
					psmt.setDouble(4,this.billAmount);
					psmt.setString(5,this.fieldId);
					psmt.setString(6,this.worksDetailId);
					psmt.setString(7,this.billStatus);
					psmt.setString(8,this.billNarration);
					psmt.setDouble(9,this.passedAmount);
					psmt.setString(10,this.billType);
					psmt.setString(11,this.expenditureType);
					psmt.setDouble(12,this.advanceAdjusted);
					psmt.setInteger(13,this.createdby);
					psmt.setDate(14,getTodayDate());
					psmt.setString(15,this.billStatusId);
					psmt.executeUpdate();
			    if(LOGGER.isDebugEnabled())     LOGGER.debug("INSERT QUERY IS:"+insertQuery);
				}
				catch(Exception e){
					LOGGER.error("Exception inserting to eg_billregister."+e);
					throw taskExc;
				}
							}

			/**
			 * This function is to update the status alone
			 * @param status
			 * @param connection
			 * @param id
			 * @throws TaskFailedException
			 */
	@Transactional
		public void updateStatus (String status,String id) throws TaskFailedException
		{
			Query psmt=null;
			try{
				
				String updateQuery="UPDATE eg_billregister SET BILLSTATUS=? where id=?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(updateQuery);
				psmt = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
				psmt.setString(1,status);
				psmt.setString(2,id);
				psmt.executeUpdate();
			}catch(Exception e){LOGGER.error("Exception in updatestatus"+e);
				throw taskExc;
			}

		}

	/**
	 * This function is to update using code
	 * @param connection
	 * @param code
	 * @throws TaskFailedException
	 */
		@Transactional
		public void update(String code)throws TaskFailedException
		{
			Query psmt=null;
			Query psmt1=null;
			String updateQuery="";
			List<Object[]> resultset=null;
			updateQuery = "UPDATE EG_BILLREGISTER SET BILLNUMBER = ?,BILLDATE = ?,BILLAMOUNT =?" +
				",FIELDID = ?,WORKSDETAILID = ?,BILLSTATUS = ?,NARRATION =?,PASSEDAMOUNT = ?" +
				",BILLTYPE = ?,EXPENDITURETYPE =?,ADVANCEADJUSTED =?"+
				",LASTMODIFIEDBY =?,LASTMODIFIEDDATE =?  where id =?";
			try{
				
				
				psmt = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
			    psmt.setString(1,this.billNumber);
			    psmt.setString(2,this.billDate);
			    psmt.setDouble(3,this.billAmount);
			    psmt.setString(4,this.fieldId);
			    psmt.setString(5,this.worksDetailId);
			    psmt.setString(6,this.billStatus);
			    psmt.setString(7,this.billNarration);
			    psmt.setDouble(8,this.passedAmount);
			    psmt.setString(9,this.billType);
			    psmt.setString(10,this.expenditureType);
			    psmt.setDouble(11,this.advanceAdjusted);
			    psmt.setInteger(12,this.lastModifiedBy);
			    psmt.setDate(13,getTodayDate());
			    psmt.setString(14,id);
			   
			    psmt.executeUpdate();
			    if(LOGGER.isDebugEnabled())     LOGGER.debug("updateQuery:"+updateQuery);
			}catch(Exception ex){
				LOGGER.error("Exception in Updating EG_BILLREGISTER:"+ex);
				throw taskExc;
			}

	}

/**
 * This function is to update using id
 * @param connection
 * @throws TaskFailedException
 */
		@Transactional
		public void update () throws TaskFailedException
		{
			Query psmt=null;
			try{
				if(isId && isField)
				{
					String updateQuery1="UPDATE EG_BILLREGISTER SET  CREATEDBY=?,billAmount=?, billDate=?,Narration=?,billNumber=?,"
						+"advanceAdjusted=?, billType=?, expenditureType=?, passedAmount=?,billStatus=?, worksDetailId=?,fieldId=?,"
						+"lastModifiedBy=?, createdDate=?,lastModifiedDate= to_date(?,'dd-Mon-yyyy HH24:MI:SS'), StatusId=?"
						 +"where id =?";
					psmt = HibernateUtil.getCurrentSession().createSQLQuery(updateQuery1);
				    psmt.setInteger(1,createdby);
				    psmt.setDouble(2,billAmount);
				    psmt.setString(3,billDate);
				    psmt.setString(4,billNarration);
				    psmt.setString(5,billNumber);
				    psmt.setDouble(6,advanceAdjusted);
				    psmt.setString(7,billType);
				    psmt.setString(8,expenditureType);
				    psmt.setDouble(9,passedAmount);
				    psmt.setString(10,billStatus);
				    psmt.setString(11,worksDetailId);
				    psmt.setString(12,fieldId);
				    psmt.setInteger(13,lastModifiedBy);
				    psmt.setString(14,createdDate);
				    psmt.setString(15,lastModifiedDate);
				    psmt.setString(16,billStatusId);
				
				    psmt.setString(17,id);
				   
				    psmt.executeUpdate();
					
				}
			}catch(Exception e){
				LOGGER.error("Exception in update.."+e);
				throw taskExc;
			}
		}
	

		public Date getTodayDate(){
			String currentDate=null;
			List<Object[]> resultset=null;
			Query psmt=null;
			Date today=null;
			try{
				SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
				resultset = HibernateUtil.getCurrentSession().createSQLQuery("select to_char(sysdate,'dd/MM/yyyy') as \"currentDate\" from dual").list();
				for(Object[] element : resultset){
				currentDate = element[0].toString();
				}
				today = (Date)formatter.parse(currentDate);
			}
			catch(Exception e){
				LOGGER.error("Error while getting Todays Date");
			}
			return today;
		}
		  /**
		 * @return Returns the createdby.
		 */
		public int getCreatedby() {
			return createdby;
		}
		/**
		 * @param createdby The createdby to set.
		 */
		public void setCreatedby(int createdby) {
			this.createdby = createdby;
			
		}
		/**
		 * @return Returns the billAmount.
		 */

		public double getBillAmount() {
			return billAmount;
		}
		/**
		 * @param billAmount The billAmount to set.
		 */
		public void setBillAmount(double billAmount) {
			this.billAmount = billAmount;
			
		}
		/**
		 * @return Returns the billDate.
		 */
		public String getBillDate() {
			return billDate;
		}
		/**
		 * @param billDate The billDate to set.
		 */
		public void setBillDate(String billDate) {
			this.billDate = billDate;
			
		}
		/**
		 * @return Returns the billNarration.
		 */
		public String getBillNarration() {
			return billNarration;
		}
		/**
		 * @param billNarration The billNarration to set.
		 */
		public void setBillNarration(String billNarration) {
			this.billNarration = billNarration;
			
		}
		/**
		 * @return Returns the billNumber.
		 */
		public String getBillNumber() {
			return billNumber;
		}
		/**
		 * @param billNumber The billNumber to set.
		 */
		public void setBillNumber(String billNumber) {
			this.billNumber = billNumber;
			
		}

		/**
		 * @return Returns the id.
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id The id to set.
		 */
		public void setId(String id) {
			this.id = id;isField = true; isId=true;
	}
		/**
			 * @return Returns the advanceAdjusted.
			 */
			public double getAdvanceAdjusted() {
				return advanceAdjusted;
			}
			/**
			 * @param advanceAdjusted The advanceAdjusted to set.
			 */
			public void setAdvanceAdjusted(double advanceAdjusted) {
				this.advanceAdjusted = advanceAdjusted;
				
			}
			/**
			 * @return Returns the billType.
			 */
			public String getBillType() {
				return billType;
			}
			/**
			 * @param billType The billType to set.
			 */
			public void setBillType(String billType) {
				this.billType = billType;
				
			}
			/**
			 * @return Returns the expenditureType.
			 */
			public String getExpenditureType() {
				return expenditureType;
			}
			/**
			 * @param expenditureType The expenditureType to set.
			 */
			public void setExpenditureType(String expenditureType) {
				this.expenditureType = expenditureType;
				
			}
			/**
			 * @return Returns the passedAmount.
			 */
			public double getPassedAmount() {
				return passedAmount;
			}
			/**
			 * @param passedAmount The passedAmount to set.
			 */
			public void setPassedAmount(double passedAmount) {
				this.passedAmount = passedAmount;
				
			}
			/**
			 * @return Returns the billStatus.
			 */
			public String getBillStatus() {
				return billStatus;
			}
			/**
			 * @param billStatus The billStatus to set.
			 */
			public void setBillStatus(String billStatus) {
				this.billStatus = billStatus;
				
			}
			/**
			 * @return Returns the worksDetailId.
			 */
			public String getWorksDetailId() {
				return worksDetailId;
			}
			/**
			 * @param worksDetailId The worksDetailId to set.
			 */
			public void setWorksDetailId(String worksDetailId) {
				this.worksDetailId = worksDetailId;
			}
			/**
			 * @return Returns the fieldId.
			 */
			public String getFieldId() {
				return fieldId;
			}
			/**
			 * @param fieldId The fieldId to set.
			 */
			public void setFieldId(String fieldId) {
				this.fieldId = fieldId;
				
			}

			public int getLastModifiedBy() {
				return lastModifiedBy;
			}
			/**
			 * @param lastModifiedBy The lastModifiedBy to set.
			 */
			public void setLastModifiedBy(int lastModifiedBy) {
				this.lastModifiedBy = lastModifiedBy;
			
			}

			public String getCreatedDate() {
				return createdDate;
			}
			/**
			 * @param createdDate The createdDate to set.
			 */
			public void setCreatedDate(String createdDate) {
				this.createdDate = createdDate;
				
			}
			/**
			 * @return Returns the lastModifiedDate.
			 */
			public String getLastModifiedDate() {
				return lastModifiedDate;
			}
			/**
			 * @param lastModifiedDate The lastModifiedDate to set.
			 */
			public void setLastModifiedDate(String lastModifiedDate) {
				this.lastModifiedDate = lastModifiedDate;
				
			}
			/**
				 * @return Returns the billStatusId.
				 */
			public String getBillStatusId() {
					return billStatusId;
			}
			/**
			 * @param billStatusId The billStatusId to set.
			 */
			public void setBillStatusId(String billStatusId) {
				this.billStatusId = billStatusId;
				
			}




			public boolean getIsSelected() {
				return isSelected;
			}




			public void setIsSelected(boolean isSelected) {
				this.isSelected = isSelected;
			}




			public String getBillDeptName() {
				return billDeptName;
			}




			public void setBillDeptName(String billDeptName) {
				this.billDeptName = billDeptName;
			}
}



