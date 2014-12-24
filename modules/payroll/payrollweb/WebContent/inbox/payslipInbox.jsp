<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<%@ page import="java.util.*,java.text.SimpleDateFormat,org.jbpm.JbpmConfiguration,org.egov.infstr.workflow.WorkFlowMatrixService,org.egov.lib.rjbac.user.User, org.egov.lib.rjbac.role.Role, org.egov.lib.rjbac.user.ejb.server.UserServiceImpl,java.sql.*,org.egov.infstr.utils.HibernateUtil" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/css/egov.css"%>">
        <title>Inbox</title>
    </head>
    
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script> 
    

    
      <body bgcolor="#ffffff">
      <form name="inboxform" action="" method="post">
	<table>
	<tr><td>
	<div id="main"><div id="m2"><div id="m3">      
                     
      
      <table align="center"  class="tableStyle" >
            <tr>
                <td  class="tableheader" colspan="4" align="center"> Inbox</td>
            </tr>
	    <tr><td colspan=4>&nbsp;</td></tr>       
	   <tr><td class="thStlyle"  align="left"> Welcome <%=session.getAttribute("com.egov.user.LoginUserName").toString()%></td>
	   <td class="thStlyle" align="right"><input type="button" class="button" value="Refresh" onclick="refreshpage();" /></td>
	   </tr>
	<tr><td colspan=4>&nbsp;</td></tr> 	   
      </table>
      
      
      <%
      
      	JbpmConfiguration jbpmConfiguration = null;
	String jBpmConfigXml = "org.jbpm.default.jbpm.cfg.xml";			
	jbpmConfiguration = JbpmConfiguration.getInstance(jBpmConfigXml);

	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
	String actorId = session.getAttribute("com.egov.user.LoginUserId").toString() ;
	List userTaskList = null;
      
	try
	{
	String userId = session.getAttribute("com.egov.user.LoginUserId").toString() ;
	
	userTaskList = jbpmContext.getTaskList(userId);	
	System.out.println("size-----"+userTaskList.size());
	
	}
	catch(JbpmException ex)
	{
	System.out.println("JbpmException -----> "+ex.getMessage());
	throw new Exception("JbpmException - cannot get the tasklist for a user");
	}
      
      
      
      %>
      

      <table border=1 align="center" cellpadding="1" cellspacing="1" id="inboxDetails" name="inboxDetails" width="100%">
            <tr>
            	<td class="thStlyle" align="center" width="70%"><div align="center">Task folder</div></td>
                <td class="thStlyle" align="center" width="15%"><div align="center">Tasks</div></td>
            </tr>
            
            
	<%
	String formattedDate="";
	//String observation="";
	String className = "";
	String returnClassName = "";
	String methodName = "";
	String headerNames = "";

	
	ArrayList processDefNameList = new ArrayList();
	ArrayList taskInstanceIdList = new ArrayList();
	ResultSet rs=null;
	Statement stmt=null;
	Connection con=null;
	List taskList = new ArrayList();
	con = HibernateUtil.getCurrentSession().connection();
	stmt=con.createStatement();
	String query="SELECT count(t.id_) as noOfTask, p.NAME_||'/'||v.stringvalue_ as taskName FROM jbpm_taskinstance t, jbpm_variableinstance v, jbpm_processdefinition p, jbpm_processinstance pi WHERE v.processinstance_ = pi.id_ AND t.PROCINST_=pi.id_ AND   pi.PROCESSDEFINITION_=p.id_ and v.name_ = 'month' and t.actorid_ = "+actorId+" and t.ISSIGNALLING_=1 GROUP BY (v.stringvalue_,p.NAME_)";
	rs=stmt.executeQuery(query);
	if(rs!=null)
	{
		while(rs.next()){
			String noOfTask = rs.getString("noOfTask");
			String taskname = rs.getString("taskName");
			taskList.add(noOfTask +"_"+ taskname);
		}
	}

	for(int i=0; i < taskList.size(); i++){
		System.out.println("processname-------"+taskList.get(i));
		String taskObj = (String)taskList.get(i);
		String task[] = taskObj.split("_");
	%>             
	    <tr id=detailsRow name=detailsRow>
		<td class="tdStlyle" width="70%"><a onclick="viewInboxProcess(this)" href="#"><div align="center" id="processname" name="processname"> <%= task[1] %>  </div></td></a>
		<td class="tdStlyle" width="15%"><div align="center" id="totalprocesses" name="totalprocesses"><%= task[0] %></div></a>
		<input type="hidden" id="taskInstanceIds" name="taskInstanceIds" value="<%= task[1] %>"/>		
	    </tr>    
	<%
	}
	%>
               
       </table>  	
       
    <script>
    	  
    	

  	function refreshpage() 
  	{
		window.location="payslipInbox.jsp";	
	}
	
	function viewInboxProcess(obj)
	{
		var table= document.getElementById("inboxDetails");
		var rowobj= getRow(obj).rowIndex; 
		var taskInstanceIds = getControlInBranch(table.rows[rowobj],'taskInstanceIds').value;
		var processname = getControlInBranch(table.rows[rowobj],'processname').innerHTML;
		document.getElementById("taskInstanceValues").value = taskInstanceIds;
		document.getElementById("procName").value = processname;
		
		document.forms[0].action ='payslipInboxByProcess.jsp';
		document.forms[0].submit();		
	}
	
	
	

       </script>       
       
       
             <table align="center"  class="tableStyle" width="100%"> 
              	   <tr><td class="thStlyle" align="center"><input type="button" class="button" value="Close" onclick="window.close()" /></td></tr>
             	   <tr><td>&nbsp;<input type="hidden" id="taskInstanceValues" name="taskInstanceValues"/><input type="hidden" id="className" name="className"/></td></tr>
             	   <tr><td>&nbsp;<input type="hidden" id="procName" name="procName"/><input type="hidden" id="headerNames" name="headerNames"/><input type="hidden" id="methodName" name="methodName"/><input type="hidden" id="returnClassName" name="returnClassName"/></td></tr>	
             	   <tr><td>&nbsp;</td></tr>
                   <tr><td  class="tableheader" colspan="4" align="center">  </td></tr>                
             </table>

         </div></div></div>
        </td></tr>
        </table>
        </form>
    </body>
</html>
