<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.*,java.text.SimpleDateFormat,org.jbpm.JbpmConfiguration,org.egov.infstr.workflow.InfraWorkFlowVariables,org.jbpm.JbpmContext,org.jbpm.graph.def.ProcessDefinition,org.jbpm.db.GraphSession,org.jbpm.graph.exe.ProcessInstance,org.jbpm.graph.exe.Token,org.jbpm.taskmgmt.exe.TaskInstance,org.jbpm.JbpmException,org.jbpm.db.TaskMgmtSession,org.jbpm.graph.def.Transition,org.egov.lib.rjbac.user.User, org.egov.lib.rjbac.role.Role, org.egov.lib.rjbac.user.ejb.server.UserManagerBean" %>

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
	
	for(int i=0; i < userTaskList.size(); i++)
	{
		TaskInstance taskInstance = (TaskInstance)userTaskList.get(i);
		
		String groupBy = (String) taskInstance.getVariable(InfraWorkFlowVariables.GROUP_BY);
		String groupByValue =  (String) taskInstance.getVariable(groupBy);
		
		className = (String) taskInstance.getVariable(InfraWorkFlowVariables.CLASS_NAME);
		returnClassName = (String) taskInstance.getVariable(InfraWorkFlowVariables.RETURNCLASS_NAME);
		headerNames = (String) taskInstance.getVariable(InfraWorkFlowVariables.HEADER_NAMES);
		methodName = (String) taskInstance.getVariable(InfraWorkFlowVariables.METHOD_NAME);
		
		if( groupByValue != null && !groupByValue.trim().equalsIgnoreCase(""))
			processDefNameList.add(taskInstance.getProcessInstance().getProcessDefinition().getName() + " - " + groupByValue);
		else
			processDefNameList.add(taskInstance.getProcessInstance().getProcessDefinition().getName());
		
		taskInstanceIdList.add(Long.toString(taskInstance.getId()));	
	}
	
	HashMap dataMap = new HashMap();
	
	for(int i=0; i < processDefNameList.size(); i++)
	{
		String pdname = (String) processDefNameList.get(i);
		String taskInstId =  (String) taskInstanceIdList.get(i);
		
		if(dataMap.get(pdname) == null)
		{
			ArrayList taskInsIdList = new ArrayList();	
			taskInsIdList.add(taskInstId);
			dataMap.put(pdname, taskInsIdList );
		}
		else
		{
			ArrayList taskInsIdList = (ArrayList) dataMap.get(pdname);
			dataMap.remove(pdname);
			taskInsIdList.add(taskInstId);
			dataMap.put(pdname, taskInsIdList );		
		}
	}

	for (Iterator iter = dataMap.entrySet().iterator(); iter.hasNext();)
	{ 
		Map.Entry entry = (Map.Entry)iter.next();

		String processName = (String) entry.getKey();
		ArrayList taskInstIdList = (ArrayList) entry.getValue();
	    	String totalProcesses = Integer.toString(taskInstIdList.size());
	    	StringBuffer taskInstanceIds=new StringBuffer();
		for(int i=0; i < taskInstIdList.size(); i++)
		{	
			taskInstanceIds.append((String) taskInstIdList.get(i));
			if(i+1 !=  taskInstIdList.size())
				taskInstanceIds.append(",");
		}

	
	%>             
	    <tr id=detailsRow name=detailsRow>
		<td class="tdStlyle" width="70%"><a onclick="viewInboxProcess(this)" href="#"><div align="center" id="processname" name="processname"> <%= processName %> </div></td></a>
		<td class="tdStlyle" width="15%"><div align="center" id="totalprocesses" name="totalprocesses"><%= totalProcesses %> </div></a>
		<input type="hidden" id="taskInstanceIds" name="taskInstanceIds" value="<%= taskInstanceIds.toString() %>"/>		
	    </tr>    
	<%
	}
	%>
               
       </table>  	
       
    <script>
    	  
    	

  	function refreshpage() 
  	{
		window.location="Inbox.jsp";	
	}
	
	function viewInboxProcess(obj)
	{
		var table= document.getElementById("inboxDetails");
		var rowobj= getRow(obj).rowIndex; 
		var taskInstanceIds = getControlInBranch(table.rows[rowobj],'taskInstanceIds').value;
		var processname = getControlInBranch(table.rows[rowobj],'processname').innerHTML;
		document.getElementById("taskInstanceValues").value = taskInstanceIds;
		document.getElementById("procName").value = processname;
		document.getElementById("className").value = "<%=className%>";
		document.getElementById("methodName").value = "<%=methodName%>";
		document.getElementById("returnClassName").value = "<%=returnClassName%>";
		document.getElementById("headerNames").value = "<%=headerNames%>";
		document.forms[0].action ='InboxByProcess.jsp';
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
