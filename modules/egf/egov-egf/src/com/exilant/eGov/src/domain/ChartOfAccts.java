/*
 * Created on Mar 4, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class ChartOfAccts {
	private String id = null;
	private String glCode = null;
	private String name = null;
	private String description = null;
	private String isActiveForPosting = "0";
	private String parentId = null;
	private String lastModified = "";
	private String modifiedBy = null;
	private String created = "";
	private String purposeid=null;
	private String operation = null;
	private String type = null;
	private String classname= "0";
	private String classification=null;
	private String functionreqd = "0";
    private String scheduleId=null;
    private String receiptscheduleid=null;
    private String receiptoperation=null;
    private String paymentscheduleid=null;
    private String paymentoperation=null;
    private String budgetCheckReqd = null;
 	private String updateQuery="UPDATE ChartOfAccounts SET ";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(ChartOfAccts.class);
	private final GenericHibernateDaoFactory genericDao = new GenericHibernateDaoFactory();
	EGovernCommon cm=new EGovernCommon();
	private TaskFailedException taskExc;
	private SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());

	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setGLCode(String aGLCode){ glCode = cm.assignValue(aGLCode,glCode); updateQuery = updateQuery + " glCode=" + glCode + ","; isField = true;}
	public void setName(String aName){ name = cm.assignValue(aName,name); updateQuery = updateQuery + " name=" + name + ","; isField = true;}
	public void setDescription(String aDescription){ description = cm.assignValue(aDescription,description); updateQuery = updateQuery + " description=" + description + ","; isField = true;}
	public void setIsActiveForPosting(String aIsActiveForPosting){ isActiveForPosting = cm.assignValue(aIsActiveForPosting,isActiveForPosting); updateQuery = updateQuery + " isActiveForPosting=" + isActiveForPosting + ","; isField = true;}
	public void setParentId(String aParentId){ parentId = cm.assignValue(aParentId,parentId); updateQuery = updateQuery + " parentId=" + parentId + ","; isField = true;}
	public void setLastModified(String aLastModified){ lastModified = cm.assignValue(aLastModified,lastModified); updateQuery = updateQuery + " lastModified=" + lastModified + ","; isField = true;}
	public void setModifiedBy(String aModifiedBy){ modifiedBy = cm.assignValue(aModifiedBy,modifiedBy); updateQuery = updateQuery + " modifiedBy=" + modifiedBy + ","; isField = true;}
	public void setCreated(String aCreated){ created = cm.assignValue(aCreated,created); updateQuery = updateQuery + " created=" + created + ","; isField = true;}
	public void setOperation(String aOperation){ operation = cm.assignValue(aOperation,operation); updateQuery = updateQuery + " operation=" + operation + ","; isField = true;}
	public void setType(String aType){ type = cm.assignValue(aType,type); updateQuery = updateQuery + " type=" + type + ","; isField = true;}
	public void setClass(String aclass){ classname = cm.assignValue(aclass,classname); updateQuery = updateQuery + " class=" + classname + ","; isField = true;}
	public void setPurposeId(String aPurposeId){purposeid= cm.assignValue(aPurposeId,purposeid); updateQuery=updateQuery+"purposeid=" + purposeid+","; isField=true;}
	public void setFunctionReqd(String aFunctionReqd){ functionreqd = cm.assignValue(aFunctionReqd,functionreqd); updateQuery = updateQuery + " functionreqd=" + functionreqd + ","; isField = true;}
	public void setClassification(String aClassification){classification= cm.assignValue(aClassification,classification); updateQuery=updateQuery+"classification=" + classification+","; isField=true;}
    public void setScheduleId(String aScheduleId){ scheduleId = cm.assignValue(aScheduleId,scheduleId); updateQuery=updateQuery+"scheduleId=" + scheduleId+","; isField=true;}
    public void setReceiptscheduleid(String aReceiptscheduleid){ receiptscheduleid =  cm.assignValue(aReceiptscheduleid,receiptscheduleid); updateQuery = updateQuery + " receiptscheduleid=" + receiptscheduleid + ","; isField = true;}
    public void setReceiptoperation(String aReceiptoperation){receiptoperation= cm.assignValue(aReceiptoperation,receiptoperation); updateQuery=updateQuery+"receiptoperation=" + receiptoperation+","; isField=true;}
    public void setPaymentscheduleid(String aPaymentscheduleid){ paymentscheduleid =  cm.assignValue(aPaymentscheduleid,paymentscheduleid); updateQuery = updateQuery + " paymentscheduleid=" + paymentscheduleid + ","; isField = true;}
    public void setPaymentoperation(String aPaymentoperation){paymentoperation= cm.assignValue(aPaymentoperation,paymentoperation); updateQuery=updateQuery+"paymentoperation=" + paymentoperation+","; isField=true;}
    public void setBudgetCheckReqd(String aBudgetCheckReqd){ budgetCheckReqd =  cm.assignValue(aBudgetCheckReqd,budgetCheckReqd); updateQuery = updateQuery + " budgetCheckReq =" + budgetCheckReqd + ","; isField = true;}
 	
    public void insert(Connection connection) throws SQLException,TaskFailedException
	{
		EGovernCommon commommethods = new EGovernCommon();
		created = commommethods.getCurrentDate(connection);
		Statement statement=null;
		try
   		{
			created = cm.assignValue(formatter.format(sdf.parse(created)),created);
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-activeCoaCodes");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodes");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-chartOfAccounts");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaPurposeId10");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-accountCodes");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-liabilityCOACodes");
		    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");
			setLastModified(created);
			setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ChartOfAccounts")) );
	
			String insertQuery = "INSERT INTO ChartOfAccounts (id, glCode, name, description, isActiveForPosting, " +
									" parentId, lastModified, modifiedBy, " +
									"created,  purposeid,functionreqd, operation,type,classification,class,budgetCheckReq,majorcode)" +
									"VALUES(" + id + ", " + glCode + ", " + name + ", " + description + ", "
									+ isActiveForPosting + ", " + parentId + ", "
									 + lastModified + ", "
									+ modifiedBy + ", " + created + ", " + purposeid +"," +functionreqd +"," + operation + ","+type+","+ classification + "," +classname+","+ budgetCheckReqd +","+getMajorCode(glCode)+")";
	        LOGGER.debug(insertQuery);
			statement = connection.createStatement();
		    statement.executeUpdate(insertQuery);
   		}
		catch(Exception e){
			LOGGER.error("Exp in insert:"+e.getMessage());
			throw taskExc;
		}
	    finally{
	    	statement.close();
	    }
	}

	/**
	 * glcode contains comma also
	 * @param glcode
	 * @return
	 * @throws TaskFailedException
	 */
	private String getMajorCode(String glcode) throws TaskFailedException
	{
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF","coa_majorcode_length");
		if(appList==null || appList.isEmpty())
			throw new TaskFailedException("coa_majorcode_length is not defined in appconfig");
		final int majorcodelength = Integer.valueOf(appList.get(0).getValue());
		String glcode1 = glcode.substring(1,glcode.length()-1);
		return glcode1.length()>=majorcodelength?"'"+glcode1.substring(0,majorcodelength)+"'":"''";
	}
	public void update (Connection connection) throws SQLException,TaskFailedException
	{
		if(isId && isField)
		{
			EGovernCommon commommethods = new EGovernCommon();
			created = commommethods.getCurrentDate(connection);
			Statement statement = null;
			try
	   		{
	   			created = formatter.format(sdf.parse( created ));
				setCreated(created);
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-activeCoaCodes");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodes");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-chartOfAccounts");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaPurposeId10");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-accountCodes");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-liabilityCOACodes");
			    EgovMasterDataCaching.getInstance().removeFromCache("egi-coaCodesForLiability");
				setLastModified(created);
				updateQuery = updateQuery.substring(0,updateQuery.length()-1);
				updateQuery = updateQuery + " WHERE id = " + id;
				statement = connection.createStatement();
				LOGGER.debug(updateQuery);
				statement.executeUpdate(updateQuery);
	   		}
			catch(Exception e)
			{
				LOGGER.error("Exp in update:"+e.getMessage());
				throw taskExc;
			}
			finally{
				statement.close();	
			}
			
			updateQuery="UPDATE ChartOfAccounts SET ";
		}
	}
}
