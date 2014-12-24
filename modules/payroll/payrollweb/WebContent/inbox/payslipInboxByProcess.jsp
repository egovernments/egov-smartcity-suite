<%@ include file="/includes/taglibs.jsp" %>
<%--
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
--%>

<%@ page import="java.util.*,org.egov.payroll.*,java.text.SimpleDateFormat,java.lang.reflect.Method,org.jbpm.JbpmConfiguration,org.jbpm.JbpmContext,org.jbpm.graph.def.ProcessDefinition,org.jbpm.db.GraphSession,org.jbpm.graph.exe.ProcessInstance,org.jbpm.graph.exe.Token,org.jbpm.taskmgmt.exe.TaskInstance,org.jbpm.JbpmException,org.jbpm.db.TaskMgmtSession,org.jbpm.graph.def.Transition,org.egov.lib.rjbac.user.User,org.egov.infstr.workflow.InfraWorkFlowVariables, org.egov.lib.rjbac.role.Role, org.egov.lib.rjbac.user.ejb.server.UserManagerBean,org.egov.infstr.utils.HibernateUtil,java.sql.*,java.math.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.egov.infstr.utils.database.utils.EgovDatabaseManager"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/css/egov.css"%>">
        <title>Inbox</title>
    </head>
    
	<script type="text/javascript" src="<%=request.getContextPath() +"/commonjs/ajaxCommonFunctions.js"%>"></script> 
	<!-- Dependency source files --> 
	<script src = "<c:url value='/commonyui/build/yahoo/yahoo-min.js'/>" ></script>  
	<script src = "<c:url value='/commonyui/build/event/event-min.js'/>" ></script> 
	<script type="text/javascript" src="<c:url value='/commonyui/build/connection/connection-min.js'/>"></script>
    
    <script>
    	  

	function drillDown(obj)
	{
	
		var table= document.getElementById("inboxDetails");
		var rowobj=getRow(obj).rowIndex; 
		var url = getControlInBranch(table.rows[rowobj],'subject').value;
		//var number = getControlInBranch(table.rows[rowobj],'objNumber').value;	
		
		if(url != "" )
		{
			window.open(" "+url+" ","","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes,resizable=yes");	
		}
	}
	
  	function refreshpage() 
  	{
		//window.location="InboxByProcess.jsp";	
		 window.location="Inbox.jsp";
	}
	
	function goBack()
	{
		window.location="payslipInbox.jsp";	
		
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

function initiateRequest1() 
{
	if (window.XMLHttpRequest) {
	return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
	isIE = true;
	return new ActiveXObject("Microsoft.XMLHTTP");
	}
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
			objId[m] =  getControlInBranch(table.rows[i],"objId").value;			
			transitionName[m] =getControlInBranch(table.rows[i],'rejectBox').value; 
			m++;
			}

			
		}	
		//alert(objId+"-"+taskInstanceId+"-"+transitionName);
		document.getElementById("objId").value=objId;
		document.getElementById("observation").value=observation;
		document.getElementById("taskInstanceId").value=taskInstanceId;
		document.getElementById("transitionName").value=transitionName;		
		var formObject = document.getElementById('inboxForm');
		//alert(formObject);
		YAHOO.util.Connect.setForm(formObject);
		//var params = "objId=" + objId+"&observation=" + observation+"&taskInstanceId=" + taskInstanceId+ "&transitionName=" + transitionName;
		var callback =   
		 {   
		   success: function(oResponse) {var response=oResponse.responseText;
								var values=response.split("^");
								result = values[0];
								//alert("result"+result);
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
									document.getElementById("submitId").disabled = false;
									document.getElementById("closeId").disabled = false;
									return false;
									//refreshpage();
								}
								else
								{
									alert('Error in processing!');
								}		
								},   
		   failure: function(oResponse) {alert('Failed to process payslips');},   
		   timeout: 3000000 //50 min timeout specified  
		 }   
		
		
		var link = "InboxAjaxDetails.jsp";
		YAHOO.util.Connect.asyncRequest('POST', link, callback);
		//alert(document.getElementById("submitId").value);	
		document.getElementById("submitId").disabled="true";
		document.getElementById("closeId").disabled= "true";
		
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
	   <form name="inboxForm" id="inboxForm"> 
			<input type="hidden" id="objId" name="objId" value="" />
			<input type="hidden" id="observation" name="observation" value="" />
			<input type="hidden" id="taskInstanceId" name="taskInstanceId" value="" />
			<input type="hidden" id="transitionName" name="transitionName" value="" />
	   </form>
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
		  
      </table>
       

      
      <table border=1 align="center" cellpadding="1" cellspacing="1" id="inboxDetails" name="inboxDetails" width="50%">            
            <tr>
            	<td class="thStlyle" align="center"><div align="center">Employee code</div></td>
            	<td class="thStlyle" align="center"><div align="center">Employee name</div></td>
				<td class="thStlyle" align="center"><div align="center">Designation</div></td>
				<td class="thStlyle" align="center"><div align="center">Fund</div></td>
				<td class="thStlyle" align="center"><div align="center">Function</div></td>
				<td class="thStlyle" align="center"><div align="center">Gross pay</div></td>
				<td class="thStlyle" align="center"><div align="center">Deduction</div></td>
				<td class="thStlyle" align="center"><div align="center">Net pay</div></td>
            
            
		<td class="thStlyle" align="center"><div align="center">Observation</div></td>
		<td class="thStlyle" align="center"><div align="center">History</div></td>
                <td class="thStlyle" align="center"><div align="center">Accept&nbsp;<input type="checkbox" id="acceptAll" name="acceptAll" onclick="selUnselAccept(this)"/></div></td>
                <td class="thStlyle" align="center"><div align="center">Reject&nbsp;<input type="checkbox" id="rejectAll" name="rejectAll" onclick="selUnselReject(this)"/></div></td>
                
            </tr>
            
	<%
	String formattedDate="";
	String linkColumn="";

	//	new implementation					
	
	String actorId = session.getAttribute("com.egov.user.LoginUserId").toString() ;
	String taskName = request.getParameter("taskInstanceValues");  
	String procName = request.getParameter("procName"); 
	//procName = procName.trim();
	System.out.println("process-----------"+procName);
	System.out.println("taskId-----------"+taskName);
	String taskNameObj[] = procName.split("/");	
	
	ResultSet rs=null;
	Statement stmt=null;
	Connection con=null;
	con = EgovDatabaseManager.openConnection();
	stmt=con.createStatement();
	String query="SELECT  empView.code as code, empView.name as name, desig.DESIGNATION_NAME as designation, fnd.NAME AS fundName, func.NAME AS funcName, pay.gross_pay as gross, pay.net_pay as net,(pay.gross_pay - pay.net_pay) as deductions, v.processinstance_ as processI, t.id_ as taskId, pay.id as payslipId, pay.month as month , v.stringvalue_  FROM jbpm_variableinstance v, egpay_emppayroll pay, eg_eis_employeeinfo empView,  jbpm_processinstance p, jbpm_taskinstance t, eg_emp_assignment a, fund fnd, function func, eg_designation desig WHERE v.PROCESSINSTANCE_=p.ID_ AND t.PROCINST_=p.ID_ AND v.name_ = 'objPK' and pay.ID =to_number(v.stringvalue_) and pay.ID_EMPLOYEE = empView.ID AND empView.ASS_ID = a.ID  AND a.ID_FUND = fnd.ID AND a.ID_FUNCTION = func.ID AND empView.DESIGNATIONID = desig.DESIGNATIONID AND empview.ASS_ID = pay.ID_EMP_ASSIGNMENT and  t.ID_ in (select t.id_ FROM jbpm_taskinstance t, jbpm_variableinstance v, jbpm_processdefinition p, jbpm_processinstance pi where v.processinstance_ = t.PROCINST_ AND p.ID_=pi.PROCESSDEFINITION_ AND t.PROCINST_=pi.id_ AND p.NAME_='"+taskNameObj[0]+"' and v.name_ = 'month' and v.stringvalue_='"+taskNameObj[1]+"' and t.ISSIGNALLING_ =1 and t.ISOPEN_=1 and t.ACTORID_="+actorId+")";
	System.out.println("qry-----------"+query);
	rs=stmt.executeQuery(query);
	int i=0;
	String acceptValue = "";
	String rejectValue = "";
	if(rs!=null)
	{
		while(rs.next()){
			String taskId = rs.getString("taskId");
			String payslipId = rs.getString("payslipId");
			String code = rs.getString("code");
			String name = rs.getString("name");
			String desig = rs.getString("designation");
			String gross = rs.getString("gross");
			String net = rs.getString("net");	
			String month = rs.getString("month");
			String processInstId = rs.getString("processI");
			String fund = rs.getString("fundName");
			String function = rs.getString("funcName");
			String deductions = rs.getString("deductions");
			String objIdentifier = name+"-"+month;
			String modifyLink = request.getContextPath()+"/payslip/modifyPaySlips.do?payslipId="+payslipId;
			//taskList.add(taskId+"-"+payslipId+"-"+code+"-"+name+"-"+desig+"-"+gross+"-"+net+"-"+month+"-"+processInstId+"-"+fund+"-"+function);	
			if (i == 0) {
				ResultSet rs1 = null;
				Statement stmt1=con.createStatement();
				List transitions = new ArrayList();
				String query1="select trans.NAME_ as name from jbpm_transition trans, jbpm_taskinstance ti,jbpm_task tsk where ti.ID_="+taskId+" AND ti.TASK_=tsk.ID_ AND tsk.TASKNODE_=trans.FROM_";
				rs1=stmt1.executeQuery(query1);
				if(rs1!=null){
					while(rs1.next()){
						transitions.add(rs1.getString("name"));
					}
				}

				if(transitions != null)
				{
					for(int q = 0; q < transitions.size(); q++)
					{

						String tempName = (String)transitions.get(q);

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
				EgovDatabaseManager.releaseConnection(stmt1);
			}
			i++;
			
			%>
			 <tr id=detailsRow name=detailsRow>		
					<td class="thStlyle" align="center"><div align="center"><%= code %></div></td>
					<td class="tdStlyle" align="center" id="modifyId"><a onclick="drillDown(this)" href="#"><div class="tdStlyle" align="center" id="subject" name="subject" value="<%= modifyLink %>" > <%= name %></div></a>
					<td class="thStlyle" align="center"><div align="center"><%= desig %></div></td>
					<td class="thStlyle" align="center"><div align="center"><%= fund %></div></td>
					<td class="thStlyle" align="center"><div align="center"><%= function %></div></td>
					<td class="thStlyle" align="center"><div align="center"><%= gross %></div></td>
					<td class="thStlyle" align="center"><div align="center"><%= deductions %></div></td>
					<td class="thStlyle" align="center"><div align="center"><%= net %></div></td>
					<td class="tdStlyle" width="19%"><div align="center"><input type="textarea" id="observation" name="observation" /></div></td>
					<td class="tdStlyle" width="7%"><a onclick="viewHistory(this)" href="#"><div class="tdStlyle" align="center" id="voucherhistory" name="voucherhistory">View</div></a>
					<input type="hidden" id="objId" name="objId" value="<%= payslipId %>"/>
					<input type="hidden" id="taskInstanceId" name="taskInstanceId" value="<%= taskId %>"/>
					<input type="hidden" id="objNumber" name="objNumber" value="<%= objIdentifier %>"/>
					<input type="hidden" id="processInstanceId" name="processInstanceId" value="<%= processInstId %>"/>
					
					</td>		
					<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="acceptBox" name="acceptBox" value="<%= acceptValue %>" onclick="markField(this)"/></div></td>
					<td class="tdStlyle" width="7%"><div align="center"><input type="checkbox" id="rejectBox" name="rejectBox" value="<%= rejectValue %>" onclick="markField(this)"/></div></td>
					
	    </tr>	
			
			<%
		}
		EgovDatabaseManager.releaseConnection(stmt);
	}
	


	%>       
	    
               
       </table>  	
      
       
             <table align="center"  class="tableStyle" width="100%"> 
              	   <tr><td colspan=2 class="thStlyle" align="right"></td></tr>
              	   <tr>
              	   <td class="thStlyle" align="right">
					<input type="button" id="submitId" class="button" value="Submit" onclick="endTaskInstance();" />
				   </td>	
<!--				   <input type="button" class="button" value="Back" onclick="goBack();" /></td>	-->
              	   <td class="thStlyle" align="left">
					<input type="button" id="closeId" class="button" value="Close" onclick="window.close()" />
					</td>
              	   </tr>
             	   <tr><td colspan=2>&nbsp;</td></tr>
             	   <tr><td colspan=2>&nbsp;</td></tr>	
             	   <tr><td colspan=2>&nbsp;</td></tr>
                   <tr><td  class="tableheader" colspan="2" align="center">  </td></tr>                
             </table>
	
         </div></div></div>  
        </td></tr>
        </table>
	
    </body>
</html>
