package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.exilant.exility.common.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
* @author Iliyaraja
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/

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
	Statement statement = null;
	public BillRegisterBean() {}


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
			updateQuery = updateQuery + " CREATEDBY=" + createdby + ","; isField = true;
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
			updateQuery = updateQuery + " billAmount=" + billAmount + ","; isField = true;
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
			updateQuery = updateQuery + " billDate='" + billDate + "',"; isField = true;
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
			updateQuery = updateQuery + " Narration='" + billNarration + "',"; isField = true;
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
			updateQuery = updateQuery + " billNumber='" + billNumber + "',"; isField = true;
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
				updateQuery = updateQuery + " advanceAdjusted=" + advanceAdjusted + ","; isField = true;
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
				updateQuery = updateQuery + " billType='" + billType + "',"; isField = true;
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
				updateQuery = updateQuery + " expenditureType='" + expenditureType + "',"; isField = true;
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
				updateQuery = updateQuery + " passedAmount=" + passedAmount + ","; isField = true;
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
				updateQuery = updateQuery + " billStatus='" + billStatus + "',"; isField = true;
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
				updateQuery = updateQuery + " worksDetailId=" + worksDetailId + ","; isField = true;
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
				updateQuery = updateQuery + " fieldId=" + fieldId + ","; isField = true;
			}

			public int getLastModifiedBy() {
				return lastModifiedBy;
			}
			/**
			 * @param lastModifiedBy The lastModifiedBy to set.
			 */
			public void setLastModifiedBy(int lastModifiedBy) {
				this.lastModifiedBy = lastModifiedBy;
				updateQuery = updateQuery + " lastModifiedBy=" + lastModifiedBy + ","; isField = true;
			}

			public String getCreatedDate() {
				return createdDate;
			}
			/**
			 * @param createdDate The createdDate to set.
			 */
			public void setCreatedDate(String createdDate) {
				this.createdDate = createdDate;
				updateQuery = updateQuery + " createdDate='" + createdDate + "',"; isField = true;
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
				updateQuery = updateQuery + " lastModifiedDate= to_date('" + lastModifiedDate + "','dd-Mon-yyyy HH24:MI:SS')"+","; isField = true;
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
				updateQuery = updateQuery + " StatusId="+billStatusId+",";isField = true;
			}

			/**
			 * This Function is for insertion
			 * @param connection
			 * @throws TaskFailedException
			 */
			public void insert(Connection connection) throws TaskFailedException
			{
				setId(String.valueOf(PrimaryKeyGenerator.getNextKey("EG_BILLREGISTER")));
				try{

				String insertQuery = "INSERT INTO EG_BILLREGISTER (ID, BILLNUMBER, BILLDATE, BILLAMOUNT,FIELDID,WORKSDETAILID,BILLSTATUS, NARRATION,PASSEDAMOUNT,BILLTYPE,EXPENDITURETYPE,ADVANCEADJUSTED,CREATEDBY,CREATEDDATE,StatusId)"
					+ "VALUES("+this.id+",'"+this.billNumber+"', to_date('" + this.billDate + "','dd-Mon-yyyy HH24:MI:SS'),"+
					this.billAmount+","+this.fieldId +","+this.worksDetailId+",'"+this.billStatus+"','"+this.billNarration+"',"+this.passedAmount+
					",'"+this.billType+"','"+this.expenditureType+"',"+this.advanceAdjusted+","+this.createdby+",to_date('"+this.createdDate+
					"','dd-Mon-yyyy HH24:MI:SS'),"+this.billStatusId +")";

				statement = connection.createStatement();
			    statement.executeUpdate(insertQuery);
			    LOGGER.debug("INSERT QUERY IS:"+insertQuery);
				}
				catch(Exception e){
					LOGGER.error("Exception inserting to eg_billregister."+e);
					throw taskExc;
				}finally{
		   			try{
		   				statement.close();
		   			}catch(Exception e){LOGGER.error("Inside finally block of insert");}
		   		}
			}

			/**
			 * This function is to update the status alone
			 * @param status
			 * @param connection
			 * @param id
			 * @throws TaskFailedException
			 */
		public void updateStatus (String status,Connection connection,String id) throws TaskFailedException
		{
			try{
				statement=connection.createStatement();
				String updateQuery="UPDATE eg_billregister SET BILLSTATUS='"+status+"' where id="+id;
				LOGGER.debug(updateQuery);
				statement.executeQuery(updateQuery);
				
			}catch(Exception e){LOGGER.error("Exception in updatestatus"+e);
				throw taskExc;
			}finally{
	   			try{
	   				statement.close();
	   			}catch(Exception e){LOGGER.error("Inside finally block of insert");}
	   		}

		}

	/**
	 * This function is to update using code
	 * @param connection
	 * @param code
	 * @throws TaskFailedException
	 */

		public void update(Connection connection,String code)throws TaskFailedException
		{
			String updateQuery="";
				updateQuery = "UPDATE EG_BILLREGISTER SET BILLNUMBER = '"+ this.billNumber +
				"',BILLDATE = '"+ this.billDate +"',BILLAMOUNT = "+ this.billAmount +
				",FIELDID = "+ this.fieldId +",WORKSDETAILID = "+ this.worksDetailId +
				",BILLSTATUS = '"+ this.billStatus +"',NARRATION ='"+ this.billNarration+
				"',PASSEDAMOUNT = "+ this.passedAmount +",BILLTYPE = '"+ this.billType +
				"',EXPENDITURETYPE ='"+ this.expenditureType+"',ADVANCEADJUSTED ="+ this.advanceAdjusted+
				",LASTMODIFIEDBY ="+ this.lastModifiedBy+",LASTMODIFIEDDATE =sysdate  where id ="+this.id+" ";
			try{
				statement = connection.createStatement();
			    statement.executeUpdate(updateQuery);
			    LOGGER.debug("updateQuery:"+updateQuery);
			}catch(Exception ex){
				LOGGER.error("Exception in Updating EG_BILLREGISTER:"+ex);
				throw taskExc;
			}finally{
	   			try{
	   				statement.close();
	   			}catch(Exception e){LOGGER.error("Inside finally block of update");}
	   		}

	}

/**
 * This function is to update using id
 * @param connection
 * @throws TaskFailedException
 */

		public void update (Connection connection) throws TaskFailedException
		{
			try{
				if(isId && isField)
				{
					updateQuery = updateQuery.substring(0,updateQuery.length()-1);
					updateQuery = updateQuery + " WHERE id = " + id;
					LOGGER.debug(updateQuery);
					statement = connection.createStatement();
					statement.executeUpdate(updateQuery);
					updateQuery="UPDATE EG_BILLREGISTER SET ";
				}
			}catch(Exception e){
				LOGGER.error("Exception in update.."+e);
				throw taskExc;
			}finally{
				try{
					statement.close();
				}catch(Exception e){LOGGER.error("Inside finally block of update");}
			}
		}


public void setIsSelected(boolean isSelected) {
	this.isSelected = isSelected;
}


public boolean getIsSelected() {
	return isSelected;
}
		
}



