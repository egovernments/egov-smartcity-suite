<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="java.util.*,
		 org.egov.infstr.utils.*,
		 		 org.apache.log4j.Logger,
		 		 org.egov.pims.*,
		 		 org.egov.pims.utils.*,
		 		 org.egov.pims.empLeave.dao.*,
		 		 org.egov.pims.empLeave.model.*,
		 		 org.egov.pims.empLeave.service.*,
		 		 org.egov.pims.dao.*,
		 		 org.egov.pims.model.*,
		 		 org.egov.pims.service.*,
		 		 org.egov.commons.CFinancialYear,
		 		 org.egov.commons.dao.CommonsDaoFactory,
		 		 org.egov.commons.dao.FinancialYearDAO,
		 		 org.egov.infstr.commons.*,
		 		 org.egov.pims.commons.client.*,
		 		 org.egov.infstr.commons.dao.*,
		 		 org.egov.infstr.commons.service.*,
		 		 org.egov.lib.address.dao.AddressDAO,
		 		 org.egov.lib.address.dao.AddressTypeDAO,
		 		 org.egov.lib.address.model.*,
		 		 org.egov.lib.address.dao.*,
		 org.egov.pims.client.*"


%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Leave Application System</title>
	
	<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/Employeevalidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javascript/dateValidation.js" type="text/javascript"></SCRIPT>
    <SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/script/jsCommonMethods.js" type="text/javascript"></SCRIPT>
    <script language="text/JavaScript" src="${pageContext.request.contextPath}/Admin-Homepage_files/dhtml.js" type="text/JavaScript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/commonjs/ajaxCommonFunctions.js "></script>
    


<%

EmpLeaveServiceImpl empLeaveServiceImpl=new EmpLeaveServiceImpl();


//To get the values
boolean IsSelfApprover=EisManagersUtill.getEmpLeaveService().isSelfApproval();
boolean isLeaveAutoManualWf = EisManagersUtill.getEmpLeaveService().isLeaveWfAutoOrManaul();

%>
<script>
<jsp:useBean id="leaveForm" class="org.egov.pims.empLeave.client.LeaveForm" scope="request"/>
var isEncash ="";
var leaveTypeId ="";
function compDate()
{
	if(document.forms[0].toDate.value !=="")
	{
		if(compareDate(document.forms[0].toDate.value,document.forms[0].fromDate.value) == 1)
		{
			alert('<bean:message key="alertToGTFromDt"/>');
			document.forms[0].toDate.focus();
			document.forms[0].toDate.value="";
			return false;
		}
	}
}

function checkIfHoliday(obj,value)
{
   var enteredStrDate = obj.value;
   var empId = '<%=leaveForm.getEmpId()%>';
   if(enteredStrDate!=null && enteredStrDate!='' && enteredStrDate!=undefined)
   {
		   var http = initiateRequest();		   
		   var url = "<%=request.getContextPath()%>/leave/checkIfDateIsHolidayAjax.jsp?enteredDate="+enteredStrDate+"&empId="+empId;
			http.open("GET", url, true);
			http.onreadystatechange = function()
			{
				if (http.readyState == 4)
				{
					
					if (http.status == 200)
					{
					       var statusString =http.responseText.split("^");
		
					    if(statusString[0]=="true")
						{
						    if(value=='fromDate')
						    {
								alert('Entered From Date is a Holiday');
								document.leaveForm.fromDate.value = "";
								document.getElementById("workingDays").value='';
								document.leaveForm.fromDate.focus();
							}
							else 
							{
							   alert('Entered To Date is a Holiday');
								document.leaveForm.toDate.value = "";
								document.getElementById("workingDays").value='';
								document.leaveForm.toDate.focus();
							}
							
		                 }
					 } 
				}
			};
				http.send(null);
		}
}
function NoDesgDetails()
{
	<c:if test="${ empty leaveForm.desigId}">
				alert('No Assignment as on for current date');
	</c:if>		
}


function populateAvailableLeaves(obj)
{

	var toDate=document.forms[0].toDate.value;
	var leaveType=document.forms[0].typeOfLeaveMstr.options[document.forms[0].typeOfLeaveMstr.selectedIndex].value;
	 
	var isEncashed=document.forms[0].encashment.value;
	var http = initiateRequest();	
	var empId = document.leaveForm.empId.value;
	obj=document.getElementById('typeOfLeaveMstr');
	var url = "<%=request.getContextPath()%>/leave/getAvailableLeavesAjax.jsp?fromDate="+document.forms[0].fromDate.value+"&type="+obj.options[obj.selectedIndex].value+"&empId="+empId+"&toDate="+toDate+"&isEncashed="+isEncashed;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");

			       if(statusString[0]!="false")
				{
					var popup = statusString[0];

					document.getElementById("availableLeaves").value = statusString[0];



			       }
			 } 
		}
	};
		http.send(null);
	//}
}
function populateWorkingDays(obj)
{
	var http = initiateRequest();	
	var empId = document.leaveForm.empId.value;
	var url = "<%=request.getContextPath()%>/leave/getWorkingDaysAjax.jsp?fromDate="+document.forms[0].fromDate.value+"&toDate="+document.forms[0].toDate.value+"&empId="+empId;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");

			       if(statusString[0]!="false")
				{
					var popup = eval(statusString[0]);

//					if(popup > 1 && document.forms[0].twoHdLeaves[0].checked == true)
//					{
//						alert('<bean:message key="alertCannotPut2HDaylv"/>');
//						document.getElementById("fromDate").value = "";
//						document.getElementById("toDate").value = "";
//						return false;
//					}
//removed popup==1
					if(document.forms[0].twoHdLeaves[0].checked == true)
					{

						document.getElementById("workingDays").value = 2 * popup;
						return true;
					}
					else
					{
						document.getElementById("workingDays").value = statusString[0];
						return true;
					}

			       }
			 }
		}
	};
		http.send(null);
}
function checkDate()
{

	var http = initiateRequest();	
	var empId = document.leaveForm.empId.value;
	var url = "<%=request.getContextPath()%>/leave/assDateCheckAjax.jsp?fromDate="+document.forms[0].fromDate.value+"&toDate="+document.forms[0].toDate.value+"&empId="+empId;
	http.open("GET", url, true);
	http.onreadystatechange = function()
	{
		if (http.readyState == 4)
		{
			if (http.status == 200)
			{
			       var statusString =http.responseText.split("^");
				      if("false" != trimAll(statusString[0]))
						{
							var popup = statusString[0];
							if(""!=trimAll(popup))
								{
								
								    alert(statusString[0]);
									document.forms[0].fromDate.value = "";
									document.forms[0].toDate.value = "";
									document.forms[0].fromDate.focus();
								}
					   }
				
				   
			 }
		}
	};
		http.send(null);
}

function ButtonPressNew(arg)
{



		var leaveType=document.forms[0].typeOfLeaveMstr.options[document.forms[0].typeOfLeaveMstr.selectedIndex].value;


		if(document.leaveForm.typeOfLeaveMstr.value=="0" )
		{
			alert('<bean:message key="alertChoosetypeOfLv"/>');
			document.leaveForm.typeOfLeaveMstr.focus();
			return false;
		}
 		if(document.getElementById("encashment").checked==false)
		{
		

			if(document.getElementById("reason").value==null || trimAll(document.getElementById("reason").value)=="" )
			{
				alert('<bean:message key="alertFillReason"/>');
				document.leaveForm.reason.focus();
				return false;
			}

			if(document.leaveForm.fromDate.value=="" )
			{
				alert('<bean:message key="alertEnterFromDate"/>');
				document.leaveForm.fromDate.focus();
				return false;
			}
			if(document.leaveForm.toDate.value=="" )
			{
				alert('<bean:message key="alertFillToDate"/>');
				document.leaveForm.toDate.focus();
				return false;
			}


			
		}

		else
		{
			if(document.leaveForm.workingDays.value=="" )
			{
				alert("Please fill the Applied days");
				document.leaveForm.reason.focus();
				return false;
			}
			if(document.forms[0].availableLeaves.value !=="")
			{
				var iChars = "0123456789";
				var obj=document.getElementById("workingDays").value;
				 for (var i = 0; i < obj.length; i++)
				{

				if (iChars.indexOf(obj.charAt(i)) == -1)
					{
						alert('Please Enter a valid number');
						document.forms[0].workingDays.focus();
						document.forms[0].workingDays.value="";
						return false;
					}
				}


				if(eval(document.getElementById("workingDays").value) > eval(document.getElementById("availableLeaves").value))
				{
					alert("Applied days should be Less than Available Leaves");
					document.forms[0].workingDays.focus();
					document.forms[0].workingDays.value="";
					return false;
				}

				if(eval(document.getElementById("workingDays").value) < 1)
				{
					alert("Applied days should not be Negative value");
					document.forms[0].workingDays.focus();
					document.forms[0].workingDays.value="";
					return false;
				}

			}
		}
		
	


	if(arg == "savenew")
	{


		var submitType="";
		<%
			String mode1=((String)(session.getAttribute("viewMode"))).trim();
			if(mode1.equalsIgnoreCase("modify"))
			{

		%>

			var answer = confirm ('Please check if the payslip is generated before cancelling else continue?')
		if (answer)
		{

			document.getElementById("typeOfLeaveMstr").disabled=false;
			document.getElementById("fromDate").disabled=false;
			document.getElementById("toDate").disabled=false;
			document.getElementById("encashment").disabled=false;
			submitType="modifyLeaveApplication";
		}
		else
				{
			return true;
		}

		<%
		 }
		 else if(mode1.equalsIgnoreCase("create"))
		 {
		 %>
		 	submitType="saveLeaveApplication";
		 <%
		 }
		 %>


		populateWorkingDays(arg);
		                    <%
								if(!IsSelfApprover)
									{
	 									  if(isLeaveAutoManualWf)
	 										  {
	 										  
	 							 if(session.getAttribute("viewMode") != null && !((String)session.getAttribute("viewMode")).equalsIgnoreCase("modify"))
	 										  {
	   
	 								  
	   
	 						%>
						if(validateForMandatory() == "false"){
					 		return false;
					 	}
						  <%}
						  }
						  }%>
						  
						
		document.getElementById("workingDays").disabled=false;
		document.leaveForm.action = "${pageContext.request.contextPath}/leave/AfterLeaveAction.do?submitType="+submitType;
		document.leaveForm.submit();
	}
	if(arg == "close")
	{
		window.close();
	}

}




function goindex(arg)
{

	if(arg == "Index")
	{
		document.leaveForm.action = "${pageContext.request.contextPath}/staff/index.jsp";
		document.leaveForm.submit();
	}


}

function CheckAssignment(obj)
{
	
	var fromdate=obj.value;
	
	var empId=document.getElementById("empId");
	var url = "${pageContext.request.contextPath}/leave/checkAssignmentAjax.jsp?fromDate="+fromdate+"&empId="+empId.value;
	var http = initiateRequest();
			http.onreadystatechange = function()
			{
					
					if (http.readyState == 4)
					{
						
						if (http.status == 200)
						{
								
							   var statusString =http.responseText.split("^");
							  
							   if('false'==trimAll(statusString[0]))
							{
								   alert('No Assignment for the given Date');
									obj.focus();
								   obj.value="";
							   }
							  

						}
					}
			};

			http.open("GET", url, true);
			http.send(null);


}
function setHalfDay(obj)
{

var a=document.forms[0].typeOfLeaveMstr.options[document.forms[0].typeOfLeaveMstr.selectedIndex].value;
var  leaveType = obj.value;

	<%
	
if(leaveForm.getDesigId()!=null )
		{
			System.out.println("desigId=dsgbfhdsghs="+leaveForm.getDesigId());
	Map leaveType = empLeaveServiceImpl.getleaveTypesForDesignation(Integer.valueOf(leaveForm.getDesigId()));
	for (Iterator it = leaveType.entrySet().iterator(); it.hasNext(); )
	{
		Map.Entry entry = (Map.Entry) it.next();

		%>
		if(leaveType =='<%=entry.getKey().toString()%>')
		{

			var url = "<%=request.getContextPath()%>/leave/isHalfDayCheckAjax.jsp?leaveid="+leaveType;

			var http = initiateRequest();
			http.onreadystatechange = function()
			{

					if (http.readyState == 4)
					{

						if (http.status == 200)
						{

							   var statusString =http.responseText.split("^");
							   if(statusString!=null && statusString!="")
							{
								if(statusString==0)
								{
									document.getElementById("halfDayId").style.display="none";
								}
								else
								{
									document.getElementById("halfDayId").style.display="";
								}
							}

						}
					}
			};

			http.open("GET", url, true);
			http.send(null);

		}


	<%
	}}
	%>


}

function setEncashment(obj)
{
	var  leaveType = obj.value;
	var http = initiateRequest();
	 var url = "<%=request.getContextPath()%>/leave/isEncashCheckAjax.jsp?leaveTypeId="+leaveType;
			http.onreadystatechange = function()
			{

					if (http.readyState == 4)
					{

						if (http.status == 200)
						{						
							var isEncash = http.responseText;
							isEncash=isEncash.replace(/\s*\n\s*/g,' ');
							document.getElementById("encashment").checked=false;
							document.leaveForm.encashment.value=0;
							document.getElementById("workingDays").disabled=true;
							if(isEncash==0)
							{
							document.getElementById("IsEncashment").style.display="none";
							document.getElementById("encashment").style.display="none";
							document.getElementById("reason").style.display="";
							document.getElementById("outerId").style.display="";
				
							document.getElementById("datevalue").style.display="";
							document.getElementById("workValue").style.display="";
				
							document.getElementById("workDays").style.display="";
							document.getElementById("appliedDay").style.display="none";
							
							}
							else
							{	
								
								document.getElementById("IsEncashment").style.display="";
								document.getElementById("encashment").style.display="";
								document.getElementById("halfDayId").style.display="none";
								document.getElementById("reason").style.display="";
								document.getElementById("outerId").style.display="";
								document.getElementById("workDays").style.display="";
								document.getElementById("appliedDay").style.display="none";
							}
							
							  
						}
					}
			};

			http.open("GET", url, true);
			http.send(null);
			
		
}

function show()
{
	if(document.getElementById("encashment").checked==true)
	{
			document.getElementById("fromDate").value="";
			document.getElementById("toDate").value="";
			document.getElementById("reason").value="";
			document.getElementById("datevalue").style.display="none";
			//change
			//document.getElementById("workingDays").value="";

			document.getElementById("reason").style.display="none";
			document.getElementById("outerId").style.display="none";
			document.leaveForm.encashment.value=1;
			document.getElementById("workDays").style.display="none";
			document.getElementById("appliedDay").style.display="";
			document.getElementById("workingDays").disabled=false;
			populateAvailableLeaves(this);


	}
	else
	{			
			document.getElementById("workingDays").value="";
			document.getElementById("datevalue").style.display="";
			document.getElementById("reason").style.display="";
			document.getElementById("outerId").style.display="";
			document.getElementById("workDays").style.display="";
			document.getElementById("appliedDay").style.display="none";
			document.getElementById("workingDays").disabled=true;
			document.leaveForm.encashment.value=0;
			populateAvailableLeaves(this);
	}


}

function disableHalfDay()
{
	document.getElementById("halfDayId").style.display="none";
}
function showAlertMsg()
{
<%
	if(request.getAttribute("alertMessage")!=null && request.getAttribute("alertMessage") != "")
	{
		String msg = (String)request.getAttribute("alertMessage");
%>
		alert("<%=msg%>");
<%
	}
%>
}

function disableEncash()
{


			<%

				if(leaveForm.getTypeOfLeaveMstr()!=null){

				if(leaveForm.getTypeOfLeaveMstr()=="1" && leaveForm.getEncashment()!=1 )
	{%>

				document.getElementById("IsEncashment").style.display="";
				document.getElementById("encashment").style.display="";
				document.getElementById("halfDayId").style.display="none";
				document.getElementById("reason").style.display="";
				document.getElementById("outerId").style.display="";


				document.getElementById("workDays").style.display="";
				document.getElementById("appliedDay").style.display="none";



			<%}else if(leaveForm.getTypeOfLeaveMstr()=="1" && leaveForm.getEncashment()==1){
				%>


			document.getElementById("fromDate").value="";
			document.getElementById("toDate").value="";
			document.getElementById("datevalue").style.display="none";
			document.getElementById("encashment").checked=true;

			document.getElementById("reason").style.display="none";
			document.getElementById("outerId").style.display="none";

			document.getElementById("workDays").style.display="none";
			document.getElementById("appliedDay").style.display="";


<% } else if(leaveForm.getTypeOfLeaveMstr()!="1"){ %>

			document.getElementById("IsEncashment").style.display="none";
			document.getElementById("encashment").style.display="none";
			document.getElementById("reason").style.display="";
			document.getElementById("outerId").style.display="";

			document.getElementById("datevalue").style.display="";
			document.getElementById("workValue").style.display="";

			document.getElementById("workDays").style.display="";
			document.getElementById("appliedDay").style.display="none";

<%}} else{ %>
			document.getElementById("IsEncashment").style.display="none";
			document.getElementById("encashment").style.display="none";
			document.getElementById("appliedDay").style.display="none";
			document.getElementById("workingDays").disabled=true;

			<%}%>


}


</script>
</head>
<div align="center">
<table align='center'>
<tr>
<td style="buffer:0pt; padding:0pt; margin:0pt">

</table>
</div>

<Center>



			<!-- Tab Navigation Begins -->
				<table align='center'>
				<tr>
				<td align="center">
				<!-- Tab Navigation Begins -->

<center>
</center>
<!-- Tab Navigation Ends -->
				</td>
				</tr>
				</table>
		<!-- Tab Navigation Ends -->




<!-- Header Section Begins -->

<!-- Header Section Ends -->


<!-- Tab Navigation Begins -->

<!-- Tab Navigation Ends -->

<!-- Body Begins -->



<!-- Body Begins -->
<body onLoad = "showAlertMsg();disableHalfDay();disableEncash();NoDesgDetails()" />
 

<div class="formmainbox"><div class="insidecontent">
  <div class="rbroundbox2">
	<div class="rbtop2"><div></div></div>
	  <div class="rbcontent2">
		<div align="center">
		<center>
			<html:form  action="/leave/AfterLeaveAction.do?submitType=saveLeaveApplication" >
				<input type=hidden name="applicationNumber" id="applicationNumber" value="<%= empLeaveServiceImpl.getApplicationNumber() %>" />
				<input type=hidden name="empId" id="empId" value="<%=leaveForm.getEmpId() %>"/>
<%
if(request.getParameter("Id")!=null)
{
%>
				<input type=hidden name="Id" id="Id" value="<%= request.getParameter("Id").trim() %>" />
<%
}
if(request.getParameter("leaveapplicationId")!=null)
{
%>

				<input type=hidden name="LeaveapplicationId" id="LeaveapplicationId" value="<%= request.getParameter("leaveapplicationId").trim() %>" />
<%
}
%>



 					<div class="datewk"><div class="estimateno">
 
 <%
			if( !mode1.equalsIgnoreCase("create"))
			{
		%>
		Leave Application No:<%=leaveForm.getApplicationNumber()%>
		<%}else{%>
 Leave Application No: &lt;Not Assigned&gt;
 <%}%>
 
 					</div>
 

	    			</div>

      				<table width="95%" border="0" cellspacing="0" cellpadding="0">
			          
						<tr><td>&nbsp;</td></tr>

						<tr><td><table  width="100%" border="0" cellspacing="0" cellpadding="0" >
							<tbody>
							<tr>
  								<td colspan="4" class="headingwk">
  									<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>

									<div class="headplacer"><bean:message key="LeaveApplication"/>&nbsp;&nbsp;&nbsp;</div></td>
  							</tr>

							<tr>
							<html:hidden property="ess"/>
	  							<td width="16%" class="whiteboxwk" >
	  								<bean:message key="EmployeeName"/><%=leaveForm.getEmployeeName()%></td> 

	  							<td class="whitebox2wk" ></td>
	  							<td width="16%" class="whiteboxwk" >
				  					<bean:message key="EmployeeCode"/><%=leaveForm.getEmpCode()%></td> 

	  							<td  class="whitebox2wk" ></td>
							</tr>


							 <tr id = "halfDayId" >
							
							    <td  class="labelcell" width="149"><bean:message key="Taking2halfLeave"/></td>
							
							    <td  colspan="3" class="labelcellforsingletd" width="305"> <bean:message key="Yes"/>
							    <input type="radio" value="1" name="twoHdLeaves" id="twoHdLeaves"  > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="No"/><input type="radio" value="0" checked name="twoHdLeaves" id="twoHdLeaves">
							    </td>
							
							  </tr>


							  <tr id="datevalue">
								    <td  class="greyboxwk"><span class="mandatory">*</span>From Date</td>
								    <td   class="greybox2wk">


		<%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView") || mode1.equalsIgnoreCase("modify")) 
			{
		%>
		
    									<input   type="text" class="selectwk"  disabled name="fromDate" id="fromDate"  value="<fmt:formatDate value="${leaveForm.dateFromDate}" pattern="dd/MM/yyyy" />" onBlur = "validateDateFormat(this);compDate();checkDate();populateWorkingDays(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3');">
<%}else {%>

 										<input   type="text" class="selectwk"  name="fromDate" id="fromDate"  onBlur = "validateDateFormat(this);compDate();checkDate();checkIfHoliday(this,'fromDate');populateWorkingDays(this);CheckAssignment(this);populateAvailableLeaves(this);" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3');">
 										<a href="javascript:show_calendar('leaveForm.fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>

 <%}%>
									</td>
									<td  class="greyboxwk"><span class="mandatory">*</span>To Date</td>
    								<td   class="greybox2wk" >
	<%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView")|| mode1.equalsIgnoreCase("modify"))
			{
		%>

    									<input   type="text"  class="selectwk" disabled name="toDate" id="toDate" onBlur = "validateDateFormat(this);compDate();populateAvailableLeaves(this);checkDate();populateWorkingDays(this)" value="<fmt:formatDate value="${leaveForm.dateToDate}" pattern="dd/MM/yyyy" />" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
<%}else {%>
 										<input   type="text"  class="selectwk" name="toDate" id="toDate" onBlur = "validateDateFormat(this);compDate();checkDate();checkIfHoliday(this,'toDate');populateAvailableLeaves(this);CheckAssignment(this);populateWorkingDays(this)" value="" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')">
 										<a href="javascript:show_calendar('leaveForm.toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png' />" border="0"></a>

 <%}%>
									</td>
  								</tr>
								<tr>
								    <td  class="whiteboxwk">Leave Type</td>
								    <td   class="whitebox2wk" width="10%" >

	<%

			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView") || mode1.equalsIgnoreCase("modify"))
			{
		%>
									    <select class="selectwk" disabled name="typeOfLeaveMstr" id="typeOfLeaveMstr" onChange = "setHalfDay(this);setEncashment(this);populateAvailableLeaves(this);">
									    	<option value='0'><bean:message key="Choose"/></option>
    		<%
		if(leaveForm.getDesigId()!=null)
		{
    		Map leaveTypeMap1 = empLeaveServiceImpl.getleaveTypesForDesignation(Integer.valueOf(leaveForm.getDesigId()));
    		for (Iterator it = leaveTypeMap1.entrySet().iterator(); it.hasNext(); )
    		{
    			Map.Entry entry = (Map.Entry) it.next();
    		%>
    										<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue() == (leaveForm.getTypeOfLeaveMstr()==null?0:Integer.parseInt(leaveForm.getTypeOfLeaveMstr()))? "selected":"")%>><%= entry.getValue() %></option>

    		<%
    		}
		}
    		%>



										</select>

	 <%}else {%>

 										<select class="selectwk" name="typeOfLeaveMstr" id="typeOfLeaveMstr" onChange = "setHalfDay(this);setEncashment(this);populateAvailableLeaves(this);">
    									<option value='0'>----choose----</option>
    		<%
if(leaveForm.getDesigId()!=null){
    		Map leaveTypeMap1 = empLeaveServiceImpl.getleaveTypesForDesignation(Integer.valueOf(leaveForm.getDesigId())); 
    		for (Iterator it = leaveTypeMap1.entrySet().iterator(); it.hasNext(); )
    		{
    			Map.Entry entry = (Map.Entry) it.next();
    		%>
    									<option  value = "<%= entry.getKey().toString() %>"<%=(((Integer)entry.getKey()).intValue() == (leaveForm.getTypeOfLeaveMstr()==null || leaveForm.getTypeOfLeaveMstr().trim().equals("")?0:Integer.parseInt(leaveForm.getTypeOfLeaveMstr()))? "selected":"")%>><%= entry.getValue() %></option>

    		<%
    		}
		}
    		%>



										</select>


 <%}%>
									</td>


									<td  class="whiteboxwk" id="outerId"><bean:message key="Reason"/><SPAN class="mandatory">*</SPAN></td>
      								<td   class="whitebox2wk">

	  <%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView")|| mode1.equalsIgnoreCase("modify"))
			{
				
		%>

								      <TEXTAREA   name="reason" id="reason" maxlength="256" onkeyup="return ismaxlength(this)" readonly="yes" wrap="soft">${leaveForm.reason}</TEXTAREA>

	  <%
	  }else {%>

										 <TEXTAREA  name="reason" id="reason" maxlength="256" onkeyup="return ismaxlength(this)" wrap="soft">${leaveForm.reason}</TEXTAREA>

 <%}%>
  									</td>




  								</tr>
								<tr id="IsEncashment">
									<td  class="whiteboxwk" >Is Encashment?</td>
									<td   colspan="3" class="whitebox2wk">

	<%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView")|| mode1.equalsIgnoreCase("modify"))
			{
		%>
      									<input   type="checkbox" class="selectwk" name="encashment" id="encashment" value="" onClick=show() disabled >
<%}else {%>
 										<input   type="checkbox" class="selectwk"  name="encashment" id="encashment" value="" onClick=show() >
 <%}%>
									</td>
								</tr>
								<tr id="workValue">



      								<td  class="greyboxwk" id="workDays">Working Days</td>
	  								<td  class="greybox2wk"  id="appliedDay">Applied Days<SPAN class="mandatory">*</SPAN></td>
      								<td   class="greybox2wk">


		<%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView")|| mode1.equalsIgnoreCase("modify"))
			{
		%>

      									<input   type="text"  class="selectwk" readonly name="workingDays" id="workingDays" value="${leaveForm.workingDays}" >
	<%}else {%>
										<input   type="text"  class="selectwk" name="workingDays" id="workingDays"  value="${leaveForm.workingDays}" >
	 <%}%>
  									</td>

  									<td  class="greyboxwk" ><bean:message key="AvailableLeaves"/></td>
      								<td   class="greybox2wk"  >
	  <%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView")|| mode1.equalsIgnoreCase("modify"))
			{
		%>
      									<input   type="text" class="selectwk"  readonly name="availableLeaves" id="availableLeaves" >
	  <%}else {%>

		 								<input   type="text" class="selectwk" readonly name="availableLeaves" id="availableLeaves" >

		 <%}%>

									</td>




  								</tr>





								<tr></tr>

							</table>
							<%
								if(!IsSelfApprover)
								{
	 								if(isLeaveAutoManualWf)
	 								{

				 						if(session.getAttribute("viewMode")!=null && ((String)(session.getAttribute("viewMode"))).trim().equalsIgnoreCase("create"))
										{
				 						%>
										 <%@include file='/leave/ManualWfApproverSelection.jsp'%>	
										 
										<%
										} 
									}
								}
							%>
							
							</td></tr>
							
							


							<tr><td><table id = "submit" style="width: 800;"  cellpadding ="0" cellspacing ="0" border = "0" value = "submit">
								<tr >
		

								</tr>
							</table></td></tr>
							<tr>
							</tr>
							
							
							
							<tr>
					            <td>
					               <div align="right" class="mandatory">* Mandatory Fields
					               </div>
              					</td>
          					</tr>
          
						</table>
 </html:form>

</center>
</div>
</div>
<div class="rbbot2"><div></div></div>
		</div>
</div></div>
</td>
</tr>
<!-- Body Section Ends -->

</table>
<div><%
			if(request.getParameter("submitType") != null && ((String)request.getParameter("submitType")).equalsIgnoreCase("setIdForDetailsView") )
			{
				if(request.getParameter("ess")== null || request.getParameter("ess").trim().isEmpty() )
				{
		%>





									<td align="center"><html:button styleClass="buttonfinal" value="Search Again" property="b2" onclick="window.location = '${pageContext.request.contextPath}/pims/BeforeSearchAction.do?module=Employee&masters=LeaveApplication&mode=View';" /></td>

		<%		}
		}else if(mode1.equalsIgnoreCase("modify"))
			{%>





									<td align="center"><html:button styleClass="buttonfinal" value="Cancel" property="b2" onclick="ButtonPressNew('savenew')"/></td>


 		<%
			}else{

		%>
									<%if(IsSelfApprover){ %>
									<td align="center"><html:button styleClass="buttonfinal" value="Approve" property="b2" onclick="ButtonPressNew('savenew')"/></td>
									<%}else{ %>
									<td align="center"><html:button styleClass="buttonfinal" value="Apply" property="b2" onclick="ButtonPressNew('savenew')"/></td>
									<% }%>
		<%}%>
  		 <input type="button" name="button" id="button" value="CLOSE"  class="buttonfinal" onclick="window.close();"/></div>
</body>