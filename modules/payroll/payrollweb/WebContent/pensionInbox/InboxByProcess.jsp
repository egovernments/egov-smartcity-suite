<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.*,org.egov.payroll.*,java.text.SimpleDateFormat,java.lang.reflect.Method,org.jbpm.JbpmConfiguration,org.jbpm.JbpmContext,org.jbpm.graph.def.ProcessDefinition,org.jbpm.db.GraphSession,org.jbpm.graph.exe.ProcessInstance,org.jbpm.graph.exe.Token,org.jbpm.taskmgmt.exe.TaskInstance,org.jbpm.JbpmException,org.jbpm.db.TaskMgmtSession,org.jbpm.graph.def.Transition,org.egov.lib.rjbac.user.User,org.egov.infstr.workflow.InfraWorkFlowVariables, org.egov.lib.rjbac.role.Role, org.egov.lib.rjbac.user.ejb.server.UserManagerBean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/css/egov.css"%>">
        <title>Inbox</title>
    </head>
    
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script> 
    
    <script>
    	  

	function drillDown(obj)
	{
	
		var table= document.getElementById("inboxDetails");
		var rowobj=getRow(obj).rowIndex; 
		var url = getControlInBranch(table.rows[rowobj],'subject').value;
		//var number = getControlInBranch(table.rows[rowobj],'objNumber').value;	
		
		if(url != "" )
		{
			window.open(" "+url+" ","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");	
		}
	}
	
  	function refreshpage() 
  	{
		//window.location="InboxByProcess.jsp";	
		 window.location="Inbox.jsp";
	}
	
	function goBack()
	{
		window.location="Inbox.jsp";	
	}
	
	function markField(obj)
	{
		var rowobj=getRow(obj);
		var table= document.getElementById("inboxDetails");
		if(getControlInBranch(table.rows[rowobj.rowIndex],'acceptBox').checked)
		{
			getControlInBranch(table.rows[rowobj.rowIndex],'rejectBox').checked = false;
		}
		else if(getControlInBranch(table.rows[rowobj.rowIndex],'rejectBox').checked)
		{
			getControlInBranch(table.rows[rowobj.rowIndex],'acceptBox').checked = false;
		}
		else
		{
			getControlInBranch(table.rows[rowobj.rowIndex],'acceptBox').checked = false;
			getControlInBranch(table.rows[rowobj.rowIndex],'rejectBox').checked = false;		
		}
	}
	
	function viewHistory(obj)
	{
		var rowobj= getRow(obj).rowIndex; 
		var table= document.getElementById("inboxDetails");
		var processInstanceId = getControlInBranch(table.rows[rowobj],"processInstanceId").value;
		var objNumber =  getControlInBranch(table.rows[rowobj],"subject").innerHTML;
		showModalDialog("InboxHistory.jsp?processInstanceId="+processInstanceId+"&objNumber="+objNumber,"","dialogLeft=500;dialogTop=5;dialogWidth=400pt;dialogHeight=200pt;status=no;");
				
	}

	function endTaskInstance()
	{
		var table= document.getElementById("inboxDetails");
		var result = "";
		var transitionName,decisionFlag;
		var acceptcheckboxflag = false;
		var rejectcheckboxflag = false;
		
		var taskInstanceId = new Array();
		var observation = new Array();
		var objId = new Array();
		var transitionName = new Array();
		var m = 0;
		for(var i=1;i<table.rows.length;i++)
		{
	
			if(getControlInBranch(table.rows[i],'acceptBox').checked)
			{
			acceptcheckboxflag = true;
			taskInstanceId[m]= getControlInBranch(table.rows[i],"taskInstanceId").value;
			observation[m]= getControlInBranch(table.rows[i],"observation").value.replace(/,/g," ");
			if(observation[m] == "")
				observation[m] = " ";
				observation[m]="Accepted: "+observation[m];
			objId[m] = getControlInBranch(table.rows[i],"objId").value;			
			transitionName[m] =getControlInBranch(table.rows[i],'acceptBox').value;
			m++;
			}
			
			else if(getControlInBranch(table.rows[i],'rejectBox').checked)
			{
			rejectcheckboxflag = true;
			taskInstanceId[m]= getControlInBranch(table.rows[i],"taskInstanceId").value;
			observation[m]= getControlInBranch(table.rows[i],"observation").value.replace(/,/g," ");
			if(observation[m] == "")
				observation[m] = " ";
				observation[m]="Rejected: "+observation[m];
			objId[m] =  getControlInBranch(table.rows[i],"objId").value;			
			transitionName[m] =getControlInBranch(table.rows[i],'rejectBox').value; 
			m++;
			}

			
		}	
		
		var link = "InboxAjaxDetails.jsp?objId=" + objId+"&observation=" + observation+"&taskInstanceId=" + taskInstanceId+ "&transitionName=" + transitionName+ " ";
		var request = initiateRequest();
		request.onreadystatechange = function() 
		{
		if (request.readyState == 4) 
		{
		if (request.status == 200) 
		{
		var response=request.responseText;
		var values=response.split("^");
		result = values[0];
		}
		}
		};
		request.open("GET", link, false);
		request.send(null);		
		
		if(result != "" && (acceptcheckboxflag == true && rejectcheckboxflag == true))
		{
			alert("Tasks are completed successfully");
			refreshpage();
		}		
		else if(result != "")
		{
			alert(result);			
			refreshpage();
		}
		else if(acceptcheckboxflag == false && rejectcheckboxflag == false)
		{
			alert('Click on the Accept or Reject checkbox');
			return false;
			//refreshpage();
		}
		else
		{
			alert('Error in processing!');
		}		
	}
	
	function selUnselAccept(obj)
	{
		var table= document.getElementById("inboxDetails");
		
		if(obj.checked)
		{
			document.getElementById('rejectAll').checked = false;
			for(var i = 1; i < table.rows.length; i++)
			{
				getControlInBranch(table.rows[i],'acceptBox').checked = true;
				getControlInBranch(table.rows[i],'rejectBox').checked = false;
			}
		}
		else
		{
			for(var i = 1; i < table.rows.length; i++)
			{
				getControlInBranch(table.rows[i],'acceptBox').checked = false;
			}		
		}
	
	}
	
	function selUnselReject(obj)
	{
	var table= document.getElementById("inboxDetails");

		if(obj.checked)
		{
			document.getElementById('acceptAll').checked = false;
			for(var i = 1; i < table.rows.length; i++)
			{
				getControlInBranch(table.rows[i],'rejectBox').checked = true;
				getControlInBranch(table.rows[i],'acceptBox').checked = false;
			}
		}
		else
		{
			for(var i = 1; i < table.rows.length; i++)
			{
				getControlInBranch(table.rows[i],'rejectBox').checked = false;
			}		
		}
	}
	

	if (document.all)
	{	 	
		document.onkeydown = function ()
		{
			var key_f5 = 116; // 116 = F5		

			if (key_f5==event.keyCode)
			{
				refreshpage();
				alert("Loading tasks");	
			}
		}
	}
	
	
       </script>
    
      <body bgcolor="#ffffff">
	<table>
	<tr><td>
	<div id="main"><div id="m2"><div id="m3">      
                     
      
      <table align="center"  class="tableStyle" >
            <tr>
                <td  class="tableheader" colspan="4" align="center">  <%= request.getParameter("procName").toUpperCase()%> </td>
            </tr>
	    <tr><td colspan=4>&nbsp;</td></tr>       
	   <tr><td class="thStlyle"  align="left"> Welcome <%=session.getAttribute("com.egov.user.LoginUserName").toString()%></td>
	   <!--<td class="thStlyle" align="right"><input type="button" class="button" value="Refresh" onclick="refreshpage();" /></td>-->
	   </tr> 
	<tr><td colspan=4>&nbsp;</td></tr> 	   
      </table>
       
      <div style="overflow-x: scroll; overflow-y: hidden;" >
      
      <table border=1 align="center" cellpadding="1" cellspacing="1" id="inboxDetails" name="inboxDetails" width="100%">
            
            <tr>
            
            
            
            <%
            JbpmConfiguration jbpmConfiguration = null;
        	String jBpmConfigXml = "org.jbpm.default.jbpm.cfg.xml";			
        	jbpmConfiguration = JbpmConfiguration.getInstance(jBpmConfigXml);

        	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
        	
        	String taskInstanceIds = request.getParameter("taskInstanceValues");
        	System.out.println("saaaaaa-----"+taskInstanceIds);
        	
        	String taskInstanceId[] = taskInstanceIds.split(",");
            String headerNames = null;        	
        	for(int i=0; i < taskInstanceId.length; i++)
        	{
        		TaskInstance taskInstance = jbpmContext.getTaskInstance(Long.parseLong(taskInstanceId[i]));
        		headerNames = (String) taskInstance.getVariable(InfraWorkFlowVariables.HEADER_NAMES);
        		break;
        	}	

            if(headerNames != null)
            {
            String headerName[] = headerNames.split(",");
            for(int g = 0; g < headerName.length; g++)
            {
            %>
            	<td class="thStlyle" align="center"><div align="center"><%= headerName[g] %></div></td>
            
            <%
            }
            }
            %>
            
		<td class="thStlyle" align="center"><div align="center">Observation</div></td>
		<td class="thStlyle" align="center"><div align="center">History</div></td>
                <td class="thStlyle" align="center"><div align="center">Accept&nbsp;<input type="checkbox" id="acceptAll" name="acceptAll" onclick="selUnselAccept(this)"/></div></td>
                <td class="thStlyle" align="center"><div align="center">Reject&nbsp;<input type="checkbox" id="rejectAll" name="rejectAll" onclick="selUnselReject(this)"/></div></td>
                
            </tr>
            
            
            
            <!--<tr>
            	<td class="thStlyle" align="center" width="5%"><div align="center">S.No</div></td>
            	<td class="thStlyle" align="center" width="13%"><div align="center">From</div></td>
                <td class="thStlyle" align="center" width="32%"><div align="center">Subject</div></td>
                <td class="thStlyle" align="center" width="7%"><div align="center">History</div></td>                
                <td class="thStlyle" align="center" width="10%"><div align="center">Date</div></td>
                <td class="thStlyle" align="center" width="19%"><div align="center">Observation</div></td>
                <td class="thStlyle" align="center" width="7%"><div align="center">Accept</div></td>
                <td class="thStlyle" align="center" width="7%"><div align="center">Reject</div></td>
            </tr>-->
            
            
            
	<%
	String formattedDate="";
	String linkColumn="";
	//String observation="";
	
   
	
	for(int i=0; i < taskInstanceId.length; i++)
	{
		TaskInstance taskInstance = jbpmContext.getTaskInstance(Long.parseLong(taskInstanceId[i]));
	
		String acceptValue = "";
		String rejectValue = "";
		linkColumn = (String) taskInstance.getVariable(InfraWorkFlowVariables.LINK);
		Transition transitionObj = null;
		List transitions = new ArrayList();
		transitions = taskInstance.getAvailableTransitions();

		if(transitions != null)
		{
			for(int q = 0; q < transitions.size(); q++)
			{
				transitionObj = (Transition) transitions.get(q);
				String tempName = transitionObj.getName();

				if(!tempName.equalsIgnoreCase(""))
				{
				String nameArray[] = tempName.split("-");

				if(nameArray[0].equalsIgnoreCase("ACCEPT"))
					acceptValue = tempName;
				else if(nameArray[0].equalsIgnoreCase("REJECT"))
					rejectValue = tempName;			
				}


			}
		}
	
	%>       
	
	  <tr id=detailsRow name=detailsRow>
		
		
		<%
		
		System.out.println("task id----"+taskInstance.getId());
		String	className = (String) taskInstance.getVariable(InfraWorkFlowVariables.CLASS_NAME);
		System.out.println("class in proces----"+className);
		String returnClassName = (String) taskInstance.getVariable(InfraWorkFlowVariables.RETURNCLASS_NAME);
		System.out.println("return class name in process---"+returnClassName);
		String methodName = (String) taskInstance.getVariable(InfraWorkFlowVariables.METHOD_NAME);
		System.out.println("method name in process---"+methodName);		
		String headernames = (String) taskInstance.getVariable(InfraWorkFlowVariables.HEADER_NAMES);		
		System.out.println("header name in process---"+headerNames);
		
		if(headernames != null && className != null && methodName != null && returnClassName != null)
		{
		String headername[] = headernames.split(",");
		
		for(int g = 0; g < headername.length; g++)
		{
				
		Class cls = Class.forName(className);
		
		Class[] class_name = new Class[1];
		class_name[0] = Class.forName("java.lang.Integer");
		
		Object[] obj_name = new Object[1];
		obj_name[0] = (Integer) Integer.valueOf((String) taskInstance.getVariable("objPK"));
		
		Method method = cls.getMethod(methodName,class_name);
		
		Object obj = method.invoke(cls.newInstance(),obj_name);
		
		
		
		Class aClass = obj.getClass();
		
		Class[] nullarg = new Class[] {};
		
		Object[] nullargs = new Object[] {};
		
		String methodname = "get" + headername[g]; 
		
		Method method1 = aClass.getMethod(methodname,nullarg);
		
		String value = (String)  method1.invoke(obj,nullargs);
		

		if(!headername[g].equalsIgnoreCase(linkColumn))
		{
		%>
		<td class="thStlyle" align="center"><div align="center"><%= value %></div></td>

		<%
		}
		else
		{
		%>
		<td class="tdStlyle" align="center"><a onclick="drillDown(this)" href="#"><div class="tdStlyle" align="center" id="subject" name="subject" value="<%= taskInstance.getVariable("url") %>" > <%= value %></div></a>
		<input type="hidden" id="objId" name="objId" value="<%= taskInstance.getVariable("objPK") %>"/>
		<input type="hidden" id="taskInstanceId" name="taskInstanceId" value="<%= taskInstance.getId() %>"/>
		<input type="hidden" id="objNumber" name="objNumber" value="<%= taskInstance.getVariable("objIdentifier") %>"/>
		<input type="hidden" id="processInstanceId" name="processInstanceId" value="<%= taskInstance.getProcessInstance().getId() %>"/></td>
		<%		
		}
		
		}
		}
		%>		
		
		
		<td class="tdStlyle" width="19%"><div align="center"><input type="textarea" id="observation" name="observation" /></div></td>
		<td class="tdStlyle" width="7%"><a onclick="viewHistory(this)" href="#"><div class="tdStlyle" align="center" id="voucherhistory" name="voucherhistory">View</div></a>
		<input type="hidden" id="processInstanceId" name="processInstanceId" value="<%= taskInstance.getProcessInstance().getId() %>"/>
		</td>		
		<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="acceptBox" name="acceptBox" value="<%= acceptValue %>" onclick="markField(this)"/></div></td>
		<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="rejectBox" name="rejectBox" value="<%= rejectValue %>" onclick="markField(this)"/></div></td>
		
	    </tr>	
	
	    <!--<tr id=detailsRow name=detailsRow>
		<td class="tdStlyle" width="5%"><div align="center" id="serialNo" name="serialNo"> <%= i + 1 %></div>
		<input type="hidden" id="taskInstanceId" name="taskInstanceId" value="<%= taskInstance.getId() %>"/></td>
		<td class="tdStlyle" width="13%"><div align="center" id="fromuser" name="fromuser"> <%= taskInstance.getVariable("userName") %> </div></td>
		<td class="tdStlyle" width="32%"><a onclick="drillDown(this)" href="#"><div class="tdStlyle" align="center" id="subject" name="subject" value="<%= taskInstance.getVariable("url") %>" ><%= taskInstance.getTask().getName() %> <%= taskInstance.getVariable("objIdentifier") %></div></a>
		<input type="hidden" id="objId" name="objId" value="<%= taskInstance.getVariable("objPK") %>"/></td>
		<%
		SimpleDateFormat formatter=null;
		formatter = new SimpleDateFormat("dd/MM/yyyy");
		formattedDate = formatter.format(taskInstance.getCreate());
		//if(taskInstance.getDescription() != null)
		  // observation = taskInstance.getDescription();
		%>
		<td class="tdStlyle" width="7%"><a onclick="viewHistory(this)" href="#"><div class="tdStlyle" align="center" id="voucherhistory" name="voucherhistory">View</div></a>
		<input type="hidden" id="objNumber" name="objNumber" value="<%= taskInstance.getVariable("objIdentifier") %>"/>
		<input type="hidden" id="processInstanceId" name="processInstanceId" value="<%= taskInstance.getProcessInstance().getId() %>"/>
		</td>
		<td class="tdStlyle" width="10%"><div align="center" id="recdate" name="recdate"><%= formattedDate %></div></td>
		<td class="tdStlyle" width="19%"><div align="center"><input type="textarea" id="observation" name="observation" /></div></td>
		<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="acceptBox" name="acceptBox" value="<%= acceptValue %>" onclick="markField(this)"/></div></td>
		<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="rejectBox" name="rejectBox" value="<%= rejectValue %>" onclick="markField(this)"/></div></td>
		
	    </tr>-->    
	<%
	}
	%>	    
               
       </table>  	
       </div>
       
             <table align="center"  class="tableStyle" width="100%"> 
            
              	   <tr>
              	    <td  align="center"><input type="button" class="button" value="Back" onclick="goBack();" />
              	    <input type="button" class="button" value="Close" onclick="window.close()" />
              	    <input type="button" class="button" value="Submit" onclick="endTaskInstance();" /></td>
              	 	 </tr>
              	  
             	   <tr><td>&nbsp;</td></tr>  
             	   <tr><td>&nbsp;</td></tr>	
             	   <tr><td>&nbsp;</td></tr>
                   <tr><td  class="tableheader"  align="center">  </td></tr>                
             </table>

         </div></div></div>
        </td></tr>
        </table>
    </body>
</html>
