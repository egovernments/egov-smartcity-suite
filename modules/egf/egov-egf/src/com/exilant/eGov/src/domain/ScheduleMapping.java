/*
 * Created on Jan 4, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScheduleMapping 
{ 
	private static final Logger LOGGER=Logger.getLogger(ScheduleMapping.class);
    private String id = null;
    private String reportType =null;
    private String schedule = "0";
    private String scheduleName=null;
    private String createdBy=null;
    private String createdDate="";
    private String lastModifiedBy=null;
    private String lastModifiedDate="";
    private String repSubType=null; 
    private String isRemission=null;
    private String updateQuery="UPDATE schedulemapping SET"; 
    private boolean isId=false, isField=false;
    private static TaskFailedException taskExc;
    EGovernCommon cm = new EGovernCommon();

    public void setId(String aId){ id = aId;  isId=true; }
    public int getId() {return Integer.valueOf(id).intValue(); }
    public void setReportType(String aReportType){ reportType = cm.assignValue(aReportType,reportType);  updateQuery = updateQuery + " reportType = " + reportType + ","; isField = true; }
    public void setSchedule(String aSchedule){ schedule = cm.assignValue(aSchedule,schedule);  updateQuery = updateQuery + " schedule = " + schedule + ","; isField = true; }
    public void setScheduleName(String aScheduleName){ scheduleName = cm.assignValue(aScheduleName,scheduleName);  updateQuery = updateQuery + " scheduleName = " + scheduleName + ","; isField = true; }
    public void setCreatedBy(String aCreatedBy){ createdBy = cm.assignValue(aCreatedBy,createdBy);  updateQuery = updateQuery + " createdBy = " + createdBy + ","; isField = true; }
    public void setCreatedDate(String aCreatedDate){ createdDate = cm.assignValue(aCreatedDate,createdDate);  updateQuery = updateQuery + " createdDate = " + createdDate + ","; isField = true; }
    public void setLastModifiedBy(String aLastModifiedBy){ lastModifiedBy = cm.assignValue(aLastModifiedBy,lastModifiedBy);  updateQuery = updateQuery + " lastModifiedBy = " + lastModifiedBy + ","; isField = true; }
    public void setLastModifiedDate(String aLastModifiedDate){ lastModifiedDate = cm.assignValue(aLastModifiedDate,lastModifiedDate);  updateQuery = updateQuery + " lastModifiedDate = " + lastModifiedDate + ","; isField = true; }
    public void setRepSubType(String aRepSubType){ repSubType = cm.assignValue(aRepSubType,repSubType);  updateQuery = updateQuery + " repSubType = " + repSubType + ","; isField = true; }
    public void setIsRemission(String aIsRemission){ isRemission = cm.assignValue(aIsRemission,isRemission);  updateQuery = updateQuery + " isRemission = " + isRemission + ","; isField = true; }

    public void insert(Connection con) throws SQLException,TaskFailedException
    {
      //  EGovernCommon commommethods = new EGovernCommon();
         
        setId( String.valueOf(PrimaryKeyGenerator.getNextKey("schedulemapping")) );
        if(isId && isField)
        {
            EGovernCommon common=new EGovernCommon();
            Statement statement = null;
            try
            { 
                createdDate=common.getCurrentDate(con);
                // Formatting Date
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy");
                createdDate=formatter.format(sdf.parse(createdDate));
                setCreatedDate(createdDate);
                lastModifiedDate=null;
            
            setCreatedDate(createdDate);
            setLastModifiedDate(lastModifiedDate);
    
            //scheduleName=common.formatString(scheduleName);
            String insertQuery = "INSERT INTO schedulemapping (id, reportType,schedule, scheduleName, createdBy, createdDate, " +
                                "lastModifiedBy,lastModifiedDate,repSubType,isRemission) " +
                                "VALUES (" + id + ", " + reportType + ", " +  schedule + ", " 
                                + scheduleName + ", " + createdBy + ", " + createdDate + ", " + lastModifiedBy + ", " 
                                + lastModifiedDate+","+repSubType+", "+isRemission+")"; 
            LOGGER.info(insertQuery);
            statement = con.createStatement();
            statement.executeUpdate(insertQuery);
            }
            catch(Exception e){throw taskExc;}
            finally{
            	statement.close();
            }
        }
        
    }

    public void update (Connection con) throws SQLException,TaskFailedException
    {
     //   EGovernCommon commommethods = new EGovernCommon();      
        
        if(isId && isField)
        {
            EGovernCommon common=new EGovernCommon();
            try
            {
                lastModifiedDate=common.getCurrentDate(con);
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy");
             //   Date dt=new Date();
             //   dt=sdf.parse(lastModifiedDate);
                lastModifiedDate=formatter.format(sdf.parse(lastModifiedDate));
            }
            catch(Exception e){throw new TaskFailedException(e.getMessage());}
            setLastModifiedDate(lastModifiedDate);
            updateQuery = updateQuery.substring(0,updateQuery.length()-1);
            updateQuery = updateQuery + " WHERE id = " + id;
            LOGGER.info(updateQuery);
            Statement statement = con.createStatement();
            statement.executeUpdate(updateQuery);
            statement.close();
            updateQuery="UPDATE schedulemapping SET";
        }
    }
}
