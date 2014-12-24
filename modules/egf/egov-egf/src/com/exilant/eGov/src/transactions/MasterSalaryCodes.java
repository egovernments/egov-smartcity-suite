/* Created on July 17, 2007
*
* TODO To change the template for this generated file go to
* Window - Preferences - Java - Code Style - Code Templates
*/
package com.exilant.eGov.src.transactions;


import java.sql.*;
import java.text.SimpleDateFormat;
import com.exilant.eGov.src.common.EGovernCommon;
import java.util.*;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import com.exilant.eGov.src.common.*;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.common.DataCollection;

import com.exilant.eGov.src.domain.EGSalaryCodes;


import org.apache.log4j.Logger;
/**
* @author Iliyaraja
*
* TODO To change the template for this generated type comment go to
* Window - Preferences - Java - Code Style - Code Templates
*/

public class MasterSalaryCodes extends AbstractTask {

private final static Logger LOGGER=Logger.getLogger(MasterSalaryCodes.class);
	private DataCollection dc;
	private Connection con=null;
	private Statement st=null;
	private ResultSet rs=null;
	String today;
	private int id;

	EGovernCommon commonmethod = new EGovernCommon();

	public MasterSalaryCodes(){
								}

public void execute (String nameTask,
						String dataTask,
						DataCollection dataCollection,
						Connection conn,
						boolean errorOnNoData,
						boolean gridHasColumnHeading, String prefix) throws TaskFailedException {

		this.dc=dataCollection;
		this.con=conn;
		boolean temp=false;	/*This value is to Check the completion of Function*/

	  	int flag=0;
	try{
			LOGGER.debug("MODE OF EXECUTION VALUE IS:"+dc.getValue("modeOfExec"));
				//TEST START
		 	String[][] salaryCodes2=(String[][])dc.getGrid("salaryCodesTable");

			LOGGER.info("salaryCodes2 Length is:"+salaryCodes2.length);

			 for(int i=0;i<salaryCodes2.length;i++)
			{
				LOGGER.info("salaryCodes i value is :"+i);

			LOGGER.info("salaryCodes2[i][0]"+salaryCodes2[i][0]);// salary_CodeId
			LOGGER.info("salaryCodes2[i][1]"+salaryCodes2[i][1]);// salary_accountHead
			LOGGER.info("salaryCodes2[i][2]"+salaryCodes2[i][2]);// glCode_Id
			LOGGER.info("salaryCodes2[i][3]"+salaryCodes2[i][3]);// salary_Type

			}

		 	// TEST END

			 if(dc.getValue("modeOfExec").equalsIgnoreCase("new"))
			 {
			 	LOGGER.debug("Create Mode------------------>");


			 	flag=1;
			 	temp=postInEGSalaryCodes();
				if(temp!=true && flag==1){
						dc.addMessage("userMessege","New Salary Codes Insertion Failed");
						throw new TaskFailedException("postInEGSalaryCodes Function Fails");
				}

			 } // new mode
			else if(dc.getValue("modeOfExec").equalsIgnoreCase("modify"))
			 {
				LOGGER.debug("Modify Mode------------------>");
			 	flag=2;
			 	temp=updateEGSalaryCodes();
				if(temp!=true && flag==2){
						dc.addMessage("userMessege","Salary Codes Updation Failed");
						throw new TaskFailedException("updateEGSalaryCodes Function Fails");
				}
			 } // modify mode

			if(flag==1)
			{
			dc.addMessage("masterInsertUpdate");
			}
			if(flag==2)
			{
			dc.addMessage("masterInsertUpdate");
			}

	}catch(Exception ex){
						throw new TaskFailedException(ex);
	}
	finally
	{
			try
			{}
			catch(Exception close){}
	}
} // execute method end

private boolean postInEGSalaryCodes() throws Exception
		{
			LOGGER.info("Inside postInEGSalaryCodes");
				EGSalaryCodes salcode=new EGSalaryCodes();
				boolean answer=false;

				String[][] salaryCodesGrid=(String[][])dc.getGrid("salaryCodesTable");

				LOGGER.info("salaryCodesGrid Length is:"+salaryCodesGrid.length);

		 for(int j=0;j<salaryCodesGrid.length;j++)
		  {
//0. salary_CodeId     1. salary_accountHead     2. glCode_Id   3. salary_Type

			if(salaryCodesGrid[j][2]!="")
			{
					salcode=new EGSalaryCodes();

					String aHead=salaryCodesGrid[j][1];
					String aGlcodeId=salaryCodesGrid[j][2];

			       int answer1=checkHeadUniqueInsert(aHead);
				  	LOGGER.debug("ANSWER1:"+answer1);

					try
					{
						if(answer1!=1)
						{
						dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
						throw new TaskFailedException("checkHeadUniqueInsert Function Failed");
						}

					}
					catch(Exception ex)
					{
					throw new TaskFailedException(ex);
					}

					int answer2=checkGlcodeIdUniqueInsert(aGlcodeId);
				  	LOGGER.debug("ANSWER2:"+answer2);
					try
					{
						if(answer2!=1)
						{
						dc.addMessage("userFailure","Duplicate GLCode Not Allowed");
						throw new TaskFailedException("checkGlcodeIdUniqueInsert Function Failed");
						}

					}
					catch(Exception ex)
					{
					throw new TaskFailedException(ex);
					}

					salcode.setHead(salaryCodesGrid[j][1]);
					salcode.setGlcodeId(salaryCodesGrid[j][2]);
					salcode.setSalType(salaryCodesGrid[j][3]);

					salcode.setCreatedby(dc.getValue("egUser_id"));
					today=commonmethod.getCurrentDateTime(con);
			   		salcode.setCreatedDate(commonmethod.getSQLDateTimeFormat(today));

					if(answer1!=0 && answer2!=0)
					{

						 salcode.insert(con);
						answer=true;
					}
					else
					{
					answer=false;

					}
			} // main if
		 } // for j
		 return answer;
	} // End postInEGSalaryCode

private boolean updateEGSalaryCodes() throws Exception
	{
		LOGGER.info("Inside updateEGSalaryCodes()");

	  			EGSalaryCodes salcode=new EGSalaryCodes();
	  				boolean answer=false;

	  			String[][] salaryCodesGrid=(String[][])dc.getGrid("salaryCodesTable");

	  			LOGGER.info("salaryCodesGrid Length is:"+salaryCodesGrid.length);

	  		 for(int j=0;j<salaryCodesGrid.length;j++)
	  		  {
	  //0. salary_CodeId     1. salary_accountHead     2. glCode_Id   3. salary_Type

	  			if(salaryCodesGrid[j][0]!="" && salaryCodesGrid[j][2]!="")
	  			{
					LOGGER.info("MODIFY MODE------------>Inside salaryCodesGrid UPDATE");
	  					salcode=new EGSalaryCodes();

	  					String aHeadId=salaryCodesGrid[j][0];
	  					String aHead=salaryCodesGrid[j][1];
	  					String aGlcodeId=salaryCodesGrid[j][2];

	  			       int answer1=checkHeadUniqueUpdate(aHeadId,aHead);
	  				  	LOGGER.debug("ANSWER1:"+answer1);

	  					try
	  					{
	  						if(answer1!=1)
	  						{
	  						dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
	  						throw new TaskFailedException("checkHeadUniqueUpdate Function Failed");
	  						}

	  					}
	  					catch(Exception ex)
	  					{
	  					throw new TaskFailedException(ex);
	  					}

	  					int answer2=checkGlcodeIdUniqueUpdate(aHeadId,aGlcodeId);
	  				  	LOGGER.debug("ANSWER2:"+answer2);
	  					try
	  					{
	  						if(answer2!=1)
	  						{
	  						dc.addMessage("userFailure","Duplicate GLCode Not Allowed");
	  						throw new TaskFailedException("checkGlcodeIdUniqueUpdate Function Failed");
	  						}

	  					}
	  					catch(Exception ex)
	  					{
	  					throw new TaskFailedException(ex);
	  					}

	  					salcode.setId(salaryCodesGrid[j][0]);
	  					salcode.setHead(salaryCodesGrid[j][1]);
	  					salcode.setGlcodeId(salaryCodesGrid[j][2]);
	  					salcode.setSalType(salaryCodesGrid[j][3]);

	  					salcode.setLastModifiedBy(dc.getValue("egUser_id"));
	  					today=commonmethod.getCurrentDateTime(con);
	  			   		salcode.setLastModifiedDate(commonmethod.getSQLDateTimeFormat(today));

	  					if(answer1!=0 && answer2!=0)
	  					{

	  						 salcode.update(con);
	  						answer=true;
	  					}
	  					else
	  					{
	  					answer=false;

	  					}

	  				} // main if for UPDATE

	if(salaryCodesGrid[j][0]=="" && salaryCodesGrid[j][2]!="")
		{
			LOGGER.info("MODIFY MODE------------>Inside salaryCodesGrid INSERT");
				salcode=new EGSalaryCodes();

				String aHead=salaryCodesGrid[j][1];
				String aGlcodeId=salaryCodesGrid[j][2];

			   int answer1=checkHeadUniqueInsert(aHead);
				LOGGER.debug("ANSWER3:"+answer1);

				try
				{
					if(answer1!=1)
					{
					dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
					throw new TaskFailedException("checkHeadUniqueInsert Function Failed");
					}

				}
				catch(Exception ex)
				{
				throw new TaskFailedException(ex);
				}

				int answer2=checkGlcodeIdUniqueInsert(aGlcodeId);
				LOGGER.debug("ANSWER4:"+answer2);
				try
				{
					if(answer2!=1)
					{
					dc.addMessage("userFailure","Duplicate GLCode Not Allowed");
					throw new TaskFailedException("checkGlcodeIdUniqueInsert Function Failed");
					}

				}
				catch(Exception ex)
				{
				throw new TaskFailedException(ex);
				}

				salcode.setHead(salaryCodesGrid[j][1]);
				salcode.setGlcodeId(salaryCodesGrid[j][2]);
				salcode.setSalType(salaryCodesGrid[j][3]);

				salcode.setCreatedby(dc.getValue("egUser_id"));
				today=commonmethod.getCurrentDateTime(con);
				salcode.setCreatedDate(commonmethod.getSQLDateTimeFormat(today));

				if(answer1!=0 && answer2!=0)
				{
    				 salcode.insert(con);
					answer=true;
				}
				else
				{
				answer=false;

				}

			} // main if for INSERT
	 } // for j
	 return answer;
} // updateEGSalaryCodes method

// ----------These Methods and Queries are used for Only Validation purpose-------------

	private int checkHeadUniqueInsert(String aHead)throws TaskFailedException{
				int result1=0;
				String sql1="select esal.HEAD from  eg_salarycodes esal where  upper(esal.HEAD) like upper('"+aHead+"')";
			    try{
					Statement st=con.createStatement();
					ResultSet rset2=st.executeQuery(sql1);
					LOGGER.debug("sql1:"+sql1);
								if(rset2.next())
								result1=0;
								else
								result1=1;
				    }catch(Exception ex){
					dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
					throw new TaskFailedException(ex);
					}
				return result1;
	}
	private int checkHeadUniqueUpdate(String aHeadId,String aHead)throws TaskFailedException{
					int result1=0;
					String sql1="select esal.HEAD from  eg_salarycodes esal where  esal.id !="+aHeadId+" and upper(esal.HEAD) like upper('"+aHead+"')";
				    try{
						Statement st=con.createStatement();
						ResultSet rset2=st.executeQuery(sql1);
						LOGGER.debug("sql1:"+sql1);
									if(rset2.next())
									result1=0;
									else
									result1=1;
					    }catch(Exception ex){
						dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
						throw new TaskFailedException(ex);
						}
					return result1;
	}
	private int checkGlcodeIdUniqueInsert(String aGlcodeId)throws TaskFailedException{
					int result2=0;
					String sql2="select esal.GLCODEID from  eg_salarycodes esal where  esal.GLCODEID="+aGlcodeId+"";
				    try{
						Statement st=con.createStatement();
						ResultSet rset2=st.executeQuery(sql2);
						LOGGER.debug("sql2:"+sql2);
									if(rset2.next())
									result2=0;
									else
									result2=1;
					    }catch(Exception ex){
						dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
						throw new TaskFailedException(ex);
						}
					return result2;
	}
	private int checkGlcodeIdUniqueUpdate(String aHeadId,String aGlcodeId)throws TaskFailedException{
						int result2=0;
						String sql2="select esal.GLCODEID from  eg_salarycodes esal where  esal.id !="+aHeadId+" and esal.GLCODEID="+aGlcodeId+"";
					    try{
							Statement st=con.createStatement();
							ResultSet rset2=st.executeQuery(sql2);
							LOGGER.debug("sql2:"+sql2);
										if(rset2.next())
										result2=0;
										else
										result2=1;
						    }catch(Exception ex){
							dc.addMessage("userFailure","Duplicate Salary Head Not Allowed");
							throw new TaskFailedException(ex);
							}
						return result2;
	}

 } // class