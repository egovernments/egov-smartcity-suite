package org.egov.pims.empLeave.client;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.Position;
import org.egov.pims.dao.StatusMasterDAO;
import org.egov.pims.empLeave.model.Attendence;
import org.egov.pims.empLeave.model.AttendenceType;
import org.egov.pims.empLeave.model.CompOff;
import org.egov.pims.empLeave.model.LeaveApplication;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;
import org.egov.pims.utils.EisConstants;
import org.egov.pims.utils.EisManagersUtill;
import org.egov.pims.workflow.compOff.CompOffService;

/*
 * deepak yn
 *
 *
 *
 */
public class AfterAttendenceMasterAction extends DispatchAction
{
	public final static Logger LOGGER = Logger.getLogger(AfterAttendenceMasterAction.class.getClass());
	private CompOffService compOffService;
	
	public CompOffService getCompOffService() {
		return compOffService;
	}
	public void setCompOffService(CompOffService compOffService) {
		this.compOffService = compOffService;
	}
	public ActionForward saveOrUpdateAttendence(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
	
		String target =null;
		String alertMessage=null;
		AttendenceForm attendenceForm = (AttendenceForm)form;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		StatusMasterDAO statusMasterDAO =new StatusMasterDAO();
		Connection con=null;
		ActionMessages messages = new ActionMessages();
		
		  //String query  = "INSERT INTO EGEIS_ATTENDENCE(ID,ATT_DATE,EMP_ID,MONTH,FIN_YEAR_ID,TYPE_ID) VALUES (?,?,?,?,?,?)";
		//String querymod = "UPDATE EGEIS_ATTENDENCE SET TYPE_ID=? where ID =?";
		//String queryLeaveUpDate = "UPDATE EGEIS_LEAVE_APPLICATION SET WORKINGDAYS = ? where ID = ? ";
		try
		{
			java.util.Date toDate = new java.util.Date();
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			String attendence ="attendence"+day;
			int crrMon = Calendar.getInstance().get(Calendar.MONTH)+1;
			int crrYer = Calendar.getInstance().get(Calendar.YEAR);
			String currDateCrt = day+"/"+crrMon+"/"+crrYer;
			String[]  attendenceAry = req.getParameterValues(attendence);
			String[]  employeeId = attendenceForm.getEmployeeId();
			String  month = req.getParameter("month");
			String  finYear = req.getParameter("finYear");
			Map mpFYMap = EisManagersUtill.getFYMap();
			Map mpFiY = (Map)mpFYMap.get(Long.valueOf(finYear));
			Map attTypeMap = AttendenceTypeVsName.getAttendenceTypeVsName();
			Integer year = Integer.valueOf((String)mpFiY.get(Integer.valueOf(month.trim()))).intValue();
			SimpleDateFormat sdfformat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
			//PreparedStatement stmtCreate=con.prepareStatement(query);
			//PreparedStatement stmtModifyToDate=con.prepareStatement(querymod);
			//PreparedStatement stmtModifyLeaveAppToDate = con.prepareStatement(queryLeaveUpDate);
			if(crrMon==Integer.parseInt(month.trim()) && crrYer == year)
			{
				
				
				if(!EisManagersUtill.getHolidaySetForMonth(year, Integer.valueOf(month.trim())).contains(sdfformat.format(sdf.parse(currDateCrt.trim()))))
				{
					try
					{
						for (int len = 0; len < attendenceAry.length; len++)
						{
			if(attendenceAry[len].toUpperCase().trim().equals("P")||attendenceAry[len].toUpperCase().trim().equals("A"))
			{
			 Attendence attMod = EisManagersUtill.getEmpLeaveService().checkAttendenceByEmpAndDte(Integer.valueOf(employeeId[len].trim()),toDate);
					
			     if(attMod==null)
					{
			    	   attMod=new Attendence();
						/*stmtCreate.setInt(1, EisManagersUtill.getEmpLeaveService().getNextValForAttId());
						stmtCreate.setDate(2, new java.sql.Date(sdf.parse(currDateCrt.trim()).getTime()));
						stmtCreate.setInt(3, Integer.valueOf(Integer.valueOf(employeeId[len].trim())));
						stmtCreate.setInt(4, Integer.valueOf(month.trim()));
						stmtCreate.setInt(5,Integer.valueOf(finYear.trim()));
						stmtCreate.setInt(6, Integer.valueOf(getTypeIdForBatch(attendenceAry[len].toUpperCase().trim())));
						stmtCreate.addBatch();*/
						attMod.setAttDate(new java.sql.Date(sdf.parse(currDateCrt.trim()).getTime()));
						AttendenceType atType = EisManagersUtill.getEmpLeaveService().
						                       getAttendenceTypeId(Integer.valueOf(getTypeIdForBatch(attendenceAry[len].toUpperCase().trim())));
						attMod.setAttendenceType(atType);
						PersonalInformation employee = EisManagersUtill.getEmployeeService().getEmloyeeById(Integer.valueOf(employeeId[len].trim()));
						attMod.setEmployee(employee);
						CFinancialYear cfyr = EisManagersUtill.getCommonsService().getFinancialYearById(Long.valueOf(finYear.trim()));
						attMod.setFinancialId(cfyr);
						attMod.setMonth(Integer.valueOf(month.trim()));
						
						PersistenceService pst = new PersistenceService();
						pst.setType(Attendence.class);
						pst.setSessionFactory(new SessionFactory());
						//pst.setType(Attendence);
						Attendence cmp =(Attendence)pst.create(attMod);
						
					}
					else
					{
						AttendenceType attype = (AttendenceType)attTypeMap.get(getTypeId(attendenceAry[len].toString()));
						if(attMod.getAttendenceType().getId()!=attype.getId())
						{
							attMod.setAttendenceType(attype);
							EisManagersUtill.getEmpLeaveService().updateAttendence(attMod);
							PersistenceService pst = new PersistenceService();
							pst.setType(Attendence.class);
							pst.setSessionFactory(new SessionFactory());
							//pst.setType(Attendence);
							Attendence cmp =(Attendence)pst.update(attMod);
							//stmtModifyToDate.setInt(1,attype.getId());
							//stmtModifyToDate.setInt(2,attMod.getId());
							//stmtModifyToDate.addBatch();
							
							Map map = EisManagersUtill.getEmpLeaveService().getMapOfLeaveType(attMod.getAttDate(),attMod.getEmployee());
							if(!map.isEmpty()&& map.containsKey(STR_LEAVE)&& (attendenceAry[len].toUpperCase().trim().equals("P")))
							{
								
								Integer lveAppId = (Integer)map.get(STR_LEAVE);
								LeaveApplication leaveApplication = EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(lveAppId);
								if(leaveApplication.getTwoHdLeaves().equals('1'))
								{
									int wd = leaveApplication.getWorkingDays()-2;
									leaveApplication.setWorkingDays(wd);
									
								}
								else
								{
									int wd = leaveApplication.getWorkingDays()-1;
									leaveApplication.setWorkingDays(wd);
								}
								
								//stmtModifyLeaveAppToDate.setInt(2, Integer.valueOf((int)leaveApplication.getId().longValue()));
								//stmtModifyLeaveAppToDate.addBatch();
								
								EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
							}
							if(!map.isEmpty()&& map.containsKey(STR_LEAVE)&& (attendenceAry[len].toUpperCase().trim().equals("HP")))
							{
								
								Integer lveAppId = (Integer)map.get(STR_LEAVE);
								LeaveApplication leaveApplication = EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(lveAppId);
								if(leaveApplication.getTwoHdLeaves().equals('1'))
								{
									int wd = leaveApplication.getWorkingDays()-1;
									leaveApplication.setWorkingDays(wd);
									//stmtModifyLeaveAppToDate.setInt(1, wd);
								}
								//stmtModifyLeaveAppToDate.setInt(2, Integer.valueOf((int)leaveApplication.getId().longValue()));
								//stmtModifyLeaveAppToDate.addBatch();
								
								EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
							}
							
							
						}
	
										
										}
							    }
	
						 }
					//stmtCreate.executeBatch();
					//stmtModifyToDate.executeBatch();
					//stmtModifyLeaveAppToDate.executeBatch();
					}
					catch(Exception e) {
				        
				        throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
				    }
				    finally
				    {
				    	//stmtCreate.close();
				    	//stmtModifyToDate.close();
				    	//stmtModifyLeaveAppToDate.close();
				        
				    }
				}
				
			}
			
			//PreparedStatement stmtModify=con.prepareStatement(querymod);
			//PreparedStatement stmtModifyLeaveApp = con.prepareStatement(queryLeaveUpDate);
			Map attendenceMapModify =(Map) req.getSession().getAttribute("AttendenceMapModify");
			//LOGGER.info("attendenceMapModify"+attendenceMapModify);
			try
			{
				if(attendenceMapModify != null && !attendenceMapModify.isEmpty())
				{				
					for (Iterator iter = attendenceMapModify.keySet().iterator();iter.hasNext();)
					{
					
						Integer  attId =(Integer)iter.next();
						String val = (String)attendenceMapModify.get(attId);
						AttendenceType attype = (AttendenceType)attTypeMap.get(getTypeId(val));
						Attendence att  = EisManagersUtill.getEmpLeaveService().getAttendenceById(attId);
						//LOGGER.info("getAttendenceType().getId()dddddddddddd"+att.getAttendenceType().getId());
						if(val.toUpperCase().trim().equals("P") || val.toUpperCase().trim().equals("A") || 
								val.toUpperCase().trim().equals("HP") || val.toUpperCase().trim().equals("H") || 
								val.toUpperCase().trim().equals("CE") || val.toUpperCase().trim().equals("OT"))
						{
									
								if(att.getAttendenceType().getId()!=attype.getId())
								{
									att.setAttendenceType(attype);
									PersistenceService pst = new PersistenceService();
									pst.setType(Attendence.class);
									pst.setSessionFactory(new SessionFactory());
									//pst.setType(Attendence);
									Attendence cmp =(Attendence)pst.update(att);
									//LOGGER.info(">>>>>>>>>>getAttendenceType().getId()"+cmp.getAttendenceType().getId());
									Map map = EisManagersUtill.getEmpLeaveService().getMapOfLeaveType(att.getAttDate(),att.getEmployee());
									if(!map.isEmpty()&& map.containsKey(STR_LEAVE)&& (val.toUpperCase().trim().equals("P")))
									{
										//LOGGER.info("111111111111111111111111111111111111");
	
										Integer lveAppId = (Integer)map.get(STR_LEAVE);
										LeaveApplication leaveApplication = EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(lveAppId);
										if(leaveApplication.getTwoHdLeaves().equals('1'))
										{
											int wd = leaveApplication.getWorkingDays()-2;
											leaveApplication.setWorkingDays(wd);
											//stmtModifyLeaveApp.setInt(1, wd);
											
										}
										else
										{
											int wd = leaveApplication.getWorkingDays()-1;
											leaveApplication.setWorkingDays(wd);
											//stmtModifyLeaveApp.setInt(1, wd);
										}
										
										//stmtModifyLeaveApp.setInt(2, Integer.valueOf((int)leaveApplication.getId().longValue()));
										//stmtModifyLeaveApp.addBatch();
									
										EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
									}
									if(!map.isEmpty()&& map.containsKey(STR_LEAVE)&& (val.toUpperCase().trim().equals("HP")))
									{
										//LOGGER.info("111111111111111111111111111111111111");
										Integer lveAppId = (Integer)map.get(STR_LEAVE);
										LeaveApplication leaveApplication = EisManagersUtill.getEmpLeaveService().getLeaveApplicationById(lveAppId);
										if(leaveApplication.getTwoHdLeaves().equals('1'))
										{
											int wd = leaveApplication.getWorkingDays()-1;
											leaveApplication.setWorkingDays(wd);
											//stmtModifyLeaveApp.setInt(1, wd);
										}
										//stmtModifyLeaveApp.setInt(2, Integer.valueOf((int)leaveApplication.getId().longValue()));
										//stmtModifyLeaveApp.addBatch();
										
										EisManagersUtill.getEmpLeaveService().updateLeaveApplication(leaveApplication);
									}
									//stmtModify.setInt(1,attype.getId());
									//stmtModify.setInt(2,att.getId());
								
									//stmtModify.addBatch();
									
								}
						}
				    }		
					
					//stmtModify.executeBatch();
					//stmtModifyLeaveApp.executeBatch();
				}
			}
			catch(Exception e) {
		        
		        throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		    }
		    finally
		    {
		    	//stmtModify.close();
		    	//stmtModifyLeaveApp.close();
		    }
		    Set<String> ketSetAttendenceMapCreate = new HashSet<String>();
		    Set<String> ketSetAttendenceMapCompOff = new HashSet<String>();
			Map attendenceMapCreate =(Map) req.getSession().getAttribute("AttendenceMapCreate");
			Map attendenceMapCompOff =(Map) req.getSession().getAttribute("AttendenceMapCompOff");
			if(attendenceMapCreate != null && !attendenceMapCreate.isEmpty()){
				 ketSetAttendenceMapCreate = attendenceMapCreate.keySet();
			}
			if(attendenceMapCompOff != null && !attendenceMapCompOff.isEmpty()){
				ketSetAttendenceMapCompOff = attendenceMapCompOff.keySet();
			}
			for(String keyObj : ketSetAttendenceMapCompOff){
				if(ketSetAttendenceMapCompOff.contains(keyObj)){
					attendenceMapCreate.remove(keyObj);
				}
			}
			
			int mon = Integer.parseInt(month.trim());
			Map mpFY =new HashMap();
			mpFY = (Map)mpFYMap.get(Long.valueOf(finYear));
			int yer = Integer.valueOf((String)mpFY.get(Integer.valueOf(month.trim()))).intValue();
			
			// query = "INSERT INTO EGEIS_ATTENDENCE(ID,ATT_DATE,EMP_ID,MONTH,FIN_YEAR_ID,TYPE_ID) VALUES (?,?,?,?,?,?)";
			//con.setAutoCommit(false); 
			 //LOGGER.info("attendenceMapCreate"+attendenceMapCreate);
			  PreparedStatement stmt = null;
			//stmt=con.prepareStatement(query);
			try
			{
				if(attendenceMapCreate != null && !attendenceMapCreate.isEmpty())
				{
					for (Iterator iter = attendenceMapCreate.keySet().iterator();iter.hasNext();)
					{
						    Attendence att = new Attendence();
							String  attId =(String)iter.next();
							
							String val = ((String)attendenceMapCreate.get(attId)).trim();
							
							StringTokenizer stringTokenizer = new StringTokenizer(attId);
							int dayCre = Integer.valueOf(stringTokenizer.nextElement().toString()).intValue();
							String currDate = dayCre+"/"+mon+"/"+yer;
							att.setAttDate(new java.sql.Date(sdf.parse(currDate.trim()).getTime()));
							AttendenceType atType = EisManagersUtill.getEmpLeaveService().
							                       getAttendenceTypeId(Integer.valueOf(getTypeIdForBatch(val.toUpperCase().trim())));
							att.setAttendenceType(atType);
							PersonalInformation employee = EisManagersUtill.getEmployeeService().
							                        getEmloyeeById(Integer.valueOf(stringTokenizer.nextElement().toString().trim()));
							att.setEmployee(employee);
							CFinancialYear cfyr = EisManagersUtill.getCommonsService().getFinancialYearById(Long.valueOf(finYear.trim()));
							att.setFinancialId(cfyr);
							att.setMonth(Integer.valueOf(month.trim()));
							
							PersistenceService pst = new PersistenceService();
							pst.setType(Attendence.class);
							pst.setSessionFactory(new SessionFactory());
							//pst.setType(Attendence);
							Attendence cmp =(Attendence)pst.create(att);
							//LOGGER.info(cmp);
					}
					//int[] updateCounts = stmt.executeBatch();
				}
			}
			catch(Exception e) {
	        
	        throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		    }
		    finally
		    {
		       // stmt.close();
		        
		    }
		    
		    //Creating new entry in attendence table as comp off
		    attendenceMapCompOff =(Map) req.getSession().getAttribute("AttendenceMapCompOff");
		    //Position position = EisManagersUtill.getEisCommonsManager().getPositionByUserId(employeeId[len].getEmployeeId().getUserMaster().getId());
		    //query  = "INSERT INTO EGEIS_ATTENDENCE(ID,ATT_DATE,EMP_ID,MONTH,FIN_YEAR_ID,TYPE_ID) VALUES (?,?,?,?,?,?)";
			//String compOffquery  = "INSERT INTO EGEIS_COMPOFF(ID,APPROVED_BY,STATUS_ID,COMPOFF_DATE,CREATED_BY,ATT_ID) VALUES (?,?,?,?,?,?)";
			//PreparedStatement stmtCompOffCreate=con.prepareStatement(query);
			//PreparedStatement compOff=con.prepareStatement(compOffquery);
			try
			{
				if(attendenceMapCompOff != null && !attendenceMapCompOff.isEmpty())
				{
					//StatusMaster statusMaster =statusMasterDAO.getStatusMaster(EisConstants.STATUS_APPLIED);
					Integer i = null;
					int attIdFk = 0;
					for (Iterator iter = attendenceMapCompOff.keySet().iterator();iter.hasNext();)
					{
						//chech for holiday and create a compoff object	
						Attendence attCmp = new Attendence();
						String  attId =(String)iter.next();
						String val = ((String)attendenceMapCompOff.get(attId)).trim();
						StringTokenizer stringTokenizer = new StringTokenizer(attId);
						int dayCre = Integer.valueOf(stringTokenizer.nextElement().toString()).intValue();
						String currDate = dayCre+"/"+mon+"/"+yer;
						attIdFk = EisManagersUtill.getEmpLeaveService().getNextValForAttId();
						attCmp.setAttDate(new java.sql.Date(sdf.parse(currDate.trim()).getTime()));
						AttendenceType atType = EisManagersUtill.getEmpLeaveService().
						                       getAttendenceTypeId(Integer.valueOf(getTypeIdForBatch(val.toUpperCase().trim())));
						attCmp.setAttendenceType(atType);
						PersonalInformation employee = EisManagersUtill.getEmployeeService().
						                        getEmloyeeById(Integer.valueOf(stringTokenizer.nextElement().toString().trim()));
						
						attCmp.setEmployee(employee);
						CFinancialYear cfyr = EisManagersUtill.getCommonsService().getFinancialYearById(Long.valueOf(finYear.trim()));
						attCmp.setFinancialId(cfyr);
						attCmp.setMonth(Integer.valueOf(month.trim()));
						PersistenceService pstaT = new PersistenceService();
						pstaT.setType(Attendence.class);
						pstaT.setSessionFactory(new SessionFactory());
						Attendence atTmpCmp =(Attendence)pstaT.create(attCmp);
						/*stmtCompOffCreate.setInt(1, attIdFk);
						stmtCompOffCreate.setDate(2, new java.sql.Date(sdf.parse(currDate.trim()).getTime()));
						stmtCompOffCreate.setInt(3, Integer.valueOf(stringTokenizer.nextElement().toString().trim()));
						stmtCompOffCreate.setInt(4, Integer.valueOf(month.trim()));
						stmtCompOffCreate.setInt(5, Integer.valueOf(finYear.trim()));
						stmtCompOffCreate.setInt(6, Integer.valueOf(getTypeIdForBatch(val.toUpperCase().trim())));
						stmtCompOffCreate.execute();stmtCompOffCreate.close();*/
						//Integer userId =(Integer) req.getSession().getAttribute("com.egov.user.LoginUserId");
						//compOff.setInt(1, EisManagersUtill.getEmpLeaveService().getNextValForCompOffId());
						//compOff.setNull(2, Types.INTEGER);
						//compOff.setInt(3,statusMaster.getId());
						//compOff.setNull(4, Types.DATE);
						//compOff.setInt(5, userId);
						//compOff.setInt(6, attIdFk);
						//compOff.addBatch();
						Position pos=EisManagersUtill.getEisCommonsService().getPositionByUserId(employee.getUserMaster().getId());
						
						CompOff cmpOff = new CompOff();
						cmpOff.setAttObj(atTmpCmp);
						//cmpOff.setCompOffDate(new java.sql.Date(sdf.parse(currDate.trim()).getTime()));
						//cmpOff.setId(Long.valueOf(EisManagersUtill.getEmpLeaveService().getNextValForCompOffId()));
						//cmpOff.setStatus(statusMaster);
						//cmpOff.setState(null);
						//CompOff cmp =getCompOffService().createcompOffWorkFlow(cmpOff,pos);
						getCompOffService().create(cmpOff);
						
						
					}
					//stmtCompOffCreate.executeBatch();
					//compOff.executeBatch();
				}
			}	
			catch(Exception e) {
		        
		        throw new EGOVRuntimeException(e.getMessage(),e);
		    }
		    finally
		    {
		    	//stmtCompOffCreate.close();
		    	//compOff.close();
		        
		    }
		    
		    //Modify attendance for comp-off
		    Map attendenceMapCompOffModify =(Map) req.getSession().getAttribute("AttendenceMapCompOffModify");		   
			try
			{
				if(attendenceMapCompOffModify != null && !attendenceMapCompOffModify.isEmpty())
				{				
					for (Iterator iter = attendenceMapCompOffModify.keySet().iterator();iter.hasNext();)
					{					
						Integer  attId =(Integer)iter.next();
						String val = (String)attendenceMapCompOffModify.get(attId);
						AttendenceType attype = (AttendenceType)attTypeMap.get(getTypeId(val));
						Attendence att  = EisManagersUtill.getEmpLeaveService().getAttendenceById(attId);
						if(att.getAttendenceType().getId()!=attype.getId())
						{
							att.setAttendenceType(attype);
							PersistenceService pst = new PersistenceService();
							pst.setType(Attendence.class);
							pst.setSessionFactory(new SessionFactory());									
							Attendence cmp = (Attendence)pst.update(att);
							if(!EisManagersUtill.getEmpLeaveService().checkForCompOffApprove(attId)){
								CompOff cmpOff = new CompOff();
								cmpOff.setAttObj(att);
								getCompOffService().create(cmpOff);
							}
						}
				    }
				}
			}
			catch(Exception e) {		        
		        throw new EGOVRuntimeException(e.getMessage(),e);
		    }		   
		    //FIXME:Delete is not allowed. once Attendance given can only be modified. 
		    //commented by divya. has to be reviewed once
		    
			/*Set attendencesetDelete =(Set) req.getSession().getAttribute("AttendencesetDelete");
			for (Iterator iter = attendencesetDelete.iterator();iter.hasNext();)
			{
				Integer  attId =(Integer)iter.next();
				Attendence att = EisManagersUtill.getEmpLeaveService().getAttendenceById(attId);
				EisManagersUtill.getEmpLeaveService().deleteAttendence(att);
				//delete.setInt(1,attId);
				//delete.addBatch();
			}
			LOGGER.info("111111111111111111111111111111111111"+attendencesetDelete);
			String querydelete = "delete FROM EGEIS_ATTENDENCE  where ID =?";
			PreparedStatement delete=con.prepareStatement(querydelete);
			try
			{
				for (Iterator iter = attendencesetDelete.iterator();iter.hasNext();)
				{
					Integer  attId =(Integer)iter.next();
					delete.setInt(1,attId);
					delete.addBatch();
				}
				delete.executeBatch();
				
				}	
				catch(SQLException e) {
			        
			        throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
			    }
			    finally
			    {
			    	delete.close();
			    }*/
			target = "success";
			alertMessage="Executed saving Attendence successfully";

	}catch(Exception ex)
	{
	   ActionMessage message = new ActionMessage("errors.message",ex.getMessage());			
       messages.add( ActionMessages.GLOBAL_MESSAGE, message );
       saveMessages( req, messages );
	   throw new EGOVRuntimeException(ex.getMessage(),ex);
	}

	req.setAttribute("alertMessage", alertMessage);
	return mapping.findForward(target);
	}
	private String getTypeId(String type)
	{
		String typeStr = null;
		try {
			
			if("P".equals(type))
			{
				typeStr = EisConstants.PRESENT;
			}
			else if("A".equals(type))
			{
				typeStr = EisConstants.ABSENT;
			}
			else if("HP".equals(type))
			{
				typeStr = EisConstants.HALFPRESENT;
			}
			else if("H".equals(type)){
				typeStr = EisConstants.HOLIDAY;
			}
			else if("CE".equals(type)){
				typeStr = EisConstants.COMPOFF_ELIG;
			}
			else if("OT".equals(type)){
				typeStr = EisConstants.OVER_TIME;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return typeStr;
	}
	private String getTypeIdForBatch(String type)
	{
		String typeStr = null;
		try {
			if("P".equals(type))
			{
				typeStr = "1";
			}
			else if("A".equals(type))
			{
				typeStr = "2";
			}
			else if("HP".equals(type))
			{
				typeStr = "5";
			}
			else if("OT".equals(type))
			{
				typeStr = "11";
			}
			else if("CE".equals(type)){
				typeStr = "12";
			}
			else if("H".equals(type)){
				typeStr = "13";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return typeStr;
	}

	private final static String STR_EXCEPTION="Exception:";
	
	private final static String STR_LEAVE="Leave";

}
