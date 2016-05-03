<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>


<!-- 
	 * Report is modified by ilayaraja.p on 29-Aug-2008.
	 * Previously it was yearly/Half yearly/As on date wise.
	 * Now it is changed to Date wise.
-->	 
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page buffer = "16kb" %>
<%@ page  import="com.exilant.eGov.src.reports.IncomeExpenseReport,org.apache.log4j.Logger,org.egov.commons.Functionary,org.egov.commons.service.CommonsManager,org.egov.commons.service.CommonsManagerHome"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<html>
<head>
<title>INCOME AND EXPENDITURE STATEMENT</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>

<link rel=stylesheet href="../css/egov.css?rnd=${app_release_no}" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css?rnd=${app_release_no}" type="text/css" media="print" />
<%@ page import="org.egov.infstr.utils.ServiceLocator"%>
<%@ page import="org.egov.infstr.utils.database.utils.EgovDatabaseManager"%>
<%@ page import="org.egov.lib.rjbac.dept.DepartmentImpl"%>
<%@ page import="org.egov.lib.rjbac.dept.ejb.api.DepartmentManager"%>
<%@ page import="org.egov.lib.rjbac.dept.ejb.api.DepartmentManagerHome"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.text.SimpleDateFormat,java.util.HashMap" %>
<%@ page import="java.util.List" %>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<link rel="stylesheet" href="../css/lockRowHead.css" type="text/css" />
	<script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>

<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">
<link type="text/css" rel="stylesheet" href="../commonyui/examples/autocomplete/css/examples.css">


<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%}
	#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<SCRIPT LANGUAGE="javascript">
var endDate="";
var fuid="";
function onLoad()
{
	
	PageManager.ListService.callListService();
	PageValidator.addCalendars();
	PageManager.DataService.callDataService('companyDetailData');
	document.getElementById('amountIn').value ='<%=request.getParameter("amountIn")%>'
	
	var fromBean="<%=request.getParameter("fromBean")%>";
	if(fromBean!='null')
	{
		document.getElementById('startDate').value ='<%=request.getParameter("startDate")%>'
		document.getElementById('endDate').value ='<%=request.getParameter("endDate")%>'
		document.getElementById('companyDetail_name').value='<%=request.getParameter("companyDetail_name")%>';
		
	}
	
	if(fromBean!=null && fromBean==1){
		document.getElementById('viewAllSchedulesButton').style.display='block';
	}
	document.getElementById('startDate').focus();

	<% 
		Logger LOGGER = Logger.getLogger("IncomeExpenseReport.jsp");
		ServiceLocator 	serviceloc  = ServiceLocator.getInstance();
		DepartmentManagerHome	depthome	=	(DepartmentManagerHome)serviceloc.getLocalHome("DepartmentManagerHome");
		DepartmentManager deptservice	=	depthome.create();
		List deptList=(List) deptservice.getAllDepartments();
		CommonsManagerHome	commHome	=	(CommonsManagerHome)serviceloc.getLocalHome("CommonsManagerHome");
		CommonsManager comm	=	commHome.create();
		List functionaryList = comm.getActiveFunctionaries();
		Connection connection = EgovDatabaseManager.openConnection();
		Statement statement=connection.createStatement();
		String sql = "SELECT id_bndry,name  FROM EG_BOUNDARY WHERE ID_BNDRY_TYPE = (SELECT id_bndry_type FROM EG_BOUNDARY_TYPE WHERE LOWER(name)= 'ward')ORDER BY name";
		ResultSet rs = statement.executeQuery(sql);
	%>

}
function validateBeforeSubmit(){
	if (!PageValidator.validateForm())
	{
		return false;
	}
	 /* checking Fiscal year start Date with End Date of Fiscal year */	
	var startDate =  document.getElementById('startDate').value;
	var endDate =  document.getElementById('endDate').value;
	var tMonth = endDate.split('/')[1];
	if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(endDate.split('/')[2]-1);
	else
		var fiscalYearStartDate="01/04/"+endDate.split('/')[2];
	if(compareDate(fiscalYearStartDate,startDate) == -1 )
	{ 
		bootbox.alert("Start Date and End Date should be in same financial year");
	   document.getElementById('startDate').focus();
	   return false;
	}
	if(compareDate(formatDate6(startDate),formatDate6(endDate)) == -1 )
	{
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}
	if(document.getElementById('amountIn').value==0)
	{
		bootbox.alert("Choose Rupees");
		return false;
	}
	return true;
}
function ButtonPress()
{
	if(!validateBeforeSubmit()) return;
	document.getElementById('fromBean').value = 1;
	document.getElementById('scheduleId').value="";	
	document.forms[0].submit();
}
function buttonFlush()
{
	window.location="IncomeExpenseReports.jsp";
}
function buttonPrint()
{ 
	document.getElementById("tbl-header1").style.display = "none";
	document.getElementById("msgRow").style.display = "none";
	document.getElementById("submitRow").style.display = "none";
  	if(window.print)
  	{
  		window.print();
	}
	document.getElementById("tbl-header1").style.display = "block";
}
function setURL(scheduleId)
{
	if(!validateBeforeSubmit()) return;
	var amountIn=document.getElementById('amountIn').value;
	document.getElementById('scheduleId').value=scheduleId;
	var startDate = document.getElementById('startDate').value;
	var endDate = document.getElementById('endDate').value;
	var compName=document.getElementById('companyDetail_name').value;
	var departmentId=document.getElementById('departmentid').value;
	var functionaryId=document.getElementById('functionaryId').value;
	var functionCode = document.getElementById('functionCode').value;
	var fieldId = document.getElementById('fieldId').value;
	
	//window.open('../Reports/IESchedules.jsp?period='+period+'&amountIn='+amountIn+'&fin_Year='+fin_Year+'&date='+date+'&scheduleId='+scheduleId,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
	window.open('../Reports/IESchedules.jsp?amountIn='+amountIn+'&startDate='+startDate+'&endDate='+endDate+'&scheduleId='+scheduleId+'&compName='+compName+'&departmentId='+departmentId+'&functionaryId='+functionaryId+'&functionCode='+functionCode+'&fieldId='+fieldId,'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
function printAllSchedules()
{
	setURL('allSchedules');
}
var yuiflag = new Array();
loadDropDownFuncNames();
function autocompletecodeFunction(obj)
 {
	
   			 // set position of dropdown
   			var src = obj;
		
   			var target = document.getElementById('codescontainer');
			
   			//target.style.position="absolute";
   			var posSrc=findPos(src);
		
   			target.style.left=posSrc[0];
			
   			target.style.top=posSrc[1]+25;
			
   			target.style.width=250;

   			var currRow=getRow(obj);
   			var coaCodeObj = getControlInBranch(currRow,obj.name);
			
   			//40 --> Down arrow, 38 --> Up arrow
   		if(yuiflag[currRow.rowIndex] == undefined)
   		{ 
   			if(event.keyCode != 40 )
   			{
   				if(event.keyCode != 38 )
   				{
   						var oAutoComp = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', funcObj);
						
   						oAutoComp.queryDelay = 0;
   						oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
   						oAutoComp.useShadow = true;
   						oAutoComp.maxResultsDisplayed = 15;
						
   						oAutoComp.useIFrame = true;
						
   				}
   			}
   		yuiflag[currRow.rowIndex] = 1;
   		}
}
function fillNeibrAfterSplitFunction(obj,neibrObjName) 
 {
 	if(obj.value==''){
		document.getElementById("functionCodeId").value='';
		return;
	}
 	var temp = obj.value; 
 	temp = temp.split("`-`");
 	obj.value=temp[0];
	var currRow=getRow(obj);
	yuiflag[currRow.rowIndex] = undefined;
	neibrObj=getControlInBranch(currRow,neibrObjName);
 	if(temp.length==1)
 	{
		var url="../commons/Process.jsp?type=validateFunctionName&functionName="+obj.value;
		var req3 = initiateRequest();
		req3.onreadystatechange = function()
		{
			  if (req3.readyState == 4)
			  {
				  if (req3.status == 200)
				  {
					var codes2=req3.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					codes = codes.split("`-`");
					if(codes.length>1)
					{
						neibrObj.value=codes[1];
					}
					else
					{
						bootbox.alert('Invalid Code\nPlease use autocomplete option');
						neibrObj.value='';
						obj.value='';
						return;
					}
				 }
			}
		};
		req3.open("GET", url, true);
		req3.send(null);
 	}
 	else
 	{
		if(obj.value!='' && temp.length<2)
		{
			bootbox.alert('Invalid Code\nPlease use autocomplete option');
			neibrObj.value='';
			obj.value='';	
			return;
		}
		if(temp[1]==null) return; else 	neibrObj.value = temp[1];
	}
 }
 	function funClear(obj, tableName)
		 {
		   if(tableName.toLowerCase() == 'chartofaccounts')
			{
				var v =getControlInBranch(obj.parentNode.parentNode,'function_code')
				var w =getControlInBranch(obj.parentNode.parentNode,'cv_fromFunctionCodeId')
				if(v.value=="")
				{
				v.value = "";
				w.value = "";
				}
			}
		}
function loadDropDownFuncNames()
	 {

	 	var type='getAllFunctionName';
		var url = "../commons/Process.jsp?type=" +type+ " ";
		var req2 = initiateRequest();
		req2.onreadystatechange = function()
		{
			  if (req2.readyState == 4)
			  {
				  if (req2.status == 200)
				  {
					var codes2=req2.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					funcArray=codes.split("+");
					funcObj= new YAHOO.widget.DS_JSArray(funcArray);


				  }
			  }
		};
		req2.open("GET", url, true);
		req2.send(null);
	 }

function openSearch1(obj, tableName){
		var a = new Array(2);
		var sRtn;
		var str="";
		
		str = "../HTML/Search.html?tableNameForCode=function";
		sRtn= showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
		if(sRtn != '')
		{
				a = sRtn.split("`~`");
				getControlInBranch(obj.parentNode,'functionCodeId').value=a[2];
				getControlInBranch(obj.parentNode,'functionCode').value=a[1];
	
		}
  }

</SCRIPT>
</head>
<body onLoad="onLoad()">

<jsp:useBean id = "ieBean" scope ="request" class="com.exilant.eGov.src.reports.IEBean"/>
<jsp:setProperty name = "ieBean" property="*"/>
<form name="IEReport" action = "IncomeExpenseReports.jsp" method = "post">

<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type=hidden name="finYear" id="finYear" value="">
<input type=hidden name="rptTittle" id="rptTittle" value="">
<input type=hidden name="scheduleId" id="scheduleId" value="">
<input type="hidden" name="companyDetail_name" id="companyDetail_name" value="">
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<div class="tbl-header1" id="tbl-header1">
<table align='center' class="tableStyle" id="table3"> 
	<tr >
		<td colspan="6" class="rowheader" valign="center"  ><span id="screenName" class="headerwhite2">
		INCOME AND EXPENDITURE STATEMENT</span></td>
	</tr>
	<tr>
		<td><div align="right" class="labelcell">From Date<font size="2" color="red">*</font></div></td>
		<td align="left" class="smallfieldcell" style="width:190px">
			<input name="startDate" id="startDate" class="datefieldinput" style="width:90px" size="15" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilDataType="exilAnyDate" exilCalendar="true"  exilMustEnter="true" tabindex=1>
		</td>
		<td ><div align="right" class="labelcell">To Date<font size="2" color="red">*</font></div></td>
		<td class="smallfieldcell" style="width:190px">
			<input name="endDate" id="endDate" class="datefieldinput" style="width:90px" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true"  exilMustEnter="true" tabindex=1>
		</td>
	</tr>
	<tr>
		<td class="labelcell" ><div align="right" valign="center" class="labelcell">Rupees<span class="leadon">*</span></div></td>
		<td class="smallFieldCell"  >
			<SELECT class="fieldinput" id="amountIn" name="amountIn" exilMustEnter="true" tabindex=1 >
				<option value="Rs" >Rupees</option>
				<option value="thousand" >Thousands</option>
				<option value="lakhs" >Lakhs</option>
			</SELECT>
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" id="departmentLevel">Department</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="departmentId" name="departmentId" >
			<option value=""></option>
			<% DepartmentImpl dept;
			
			for (Object object : deptList) { dept  =(DepartmentImpl)object;%>
			
				<option value='<%= dept.getId() %>' <% if(dept.getId().equals(Integer.valueOf(ieBean.getDepartmentId()))){%>selected <%}%> ><%= dept.getDeptName() %></option>
			<%}
			%>
			</SELECT>
			
		</td>
	</tr>
	<tr>
			<td align="right"><div align="right" valign="center" class="labelcell" id="functionaryLevel">Functionary</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="functionaryId" name="functionaryId" >
			<option value=""></option>
			<% Functionary functionary;
			
			for (Object object : functionaryList) { functionary  =(Functionary)object;%>
			
				<option value='<%= functionary.getId() %>' <% if(functionary.getId().equals(Integer.valueOf(ieBean.getFunctionaryId()))){%>selected <%}%> ><%= functionary.getName() %></option>
			<%}
			%>
			</SELECT>
			
		</td>
		<td align="right"><div align="right" valign="center" class="labelcell" id="functionLevel">Function &nbsp; </div></td>
                       
                        <td class=fieldcelldesc >
                        <input type="hidden" name="functionCodeId" id="functionCodeId" >
                        <input type="text" styleClass="fieldinputlarge"  id="functionCode" name="functionCode"  value="<%=ieBean.getFunctionCode()%>"  onkeyup="autocompletecodeFunction(this);"   onblur="fillNeibrAfterSplitFunction(this,'functionCodeId')" /><IMG   id=IMG1 onclick=openSearch1(this,'functionCodeId') src="/egi/resources/erp2/images/plus1.gif"  >
						</td><div id="codescontainer"></div>
	
	</tr>
	<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" id="functionaryLevel">Field </div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="fieldId" name="fieldId" >
			<option value=""></option>
				<%
					while(rs.next()){ %>
						<option value='<%= rs.getString(1) %>'  <% if(rs.getString(1).equals(ieBean.getFieldId())){%>selected <%}%> ><%= rs.getString(2) %></option>
					<% }%>
				
			</SELECT>
		</td>
	</tr>
		
	<tr><td>&nbsp;</td></tr>
		<tr id="msgRow">
	    	<td align="left" class="labelcellforsingletd" style="FONT-SIZE: 12px;" colspan="10">
	    	To&nbsp;print&nbsp;the&nbsp;report,&nbsp;please&nbsp;ensure&nbsp;the&nbsp;following&nbsp;settings:<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. Paper size: A4<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2. Paper Orientation: Landscape
	    	</td>
	    </tr>
	    	<tr id="submitRow">
	    		<td colspan="10" align="middle">
	    		<table border="0" cellpadding="0" cellspacing="0"  >
	    		<tr>	
	    		<td>		
	    			<input type=button class=button onclick="ButtonPress()" href="#" value="Submit" tabindex=1>
	    			<input type=button class=button onclick="buttonPrint()" href="#" value="Print"  tabindex=1>
	    			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel" tabindex=1>
	    			<input type=button class=button onclick="window.close();" href="#" value="Close" tabindex=1>
	    		    <input type="button" name="viewAllSchedulesButton" id="viewAllSchedulesButton" class="button" tabindex=1 onclick="printAllSchedules();" value="View All Schedules" style="display:none" />    
	    		</td>
	    		</tr>
	    		 </table>
	    		 </td>
	    	</tr>

	</table>
	</div>
	</div></div></div>
	</td></tr>
	</table>
	<%
	    LOGGER.info("after submit "+request.getParameter("fromBean")+" year "+request.getParameter("finYear"));
	    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
	    {
		 try
		 {		
			IncomeExpenseReport ieRpt= new IncomeExpenseReport();
			request.setAttribute("links",ieRpt.getIEReport(ieBean));
			String[] fundNameList=ieRpt.reqFundName;
			String[] fundIdList=ieRpt.reqFundId;
			String title=request.getParameter("rptTittle");
			String amountIn=request.getParameter("amountIn");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			java.util.Date t1  = new java.util.Date();
			t1 = sdf.parse(request.getParameter("startDate"));
			String startDate = formatter.format(t1);
			java.util.Date t2  = new java.util.Date();
			t2 = sdf.parse(request.getParameter("endDate"));
			String endDate = formatter.format(t2);
			
			title = " from "+startDate+" to "+endDate;
			String compName = request.getParameter("companyDetail_name");
			int temp=1;
			String tempStyle="";
			
			String value[] = ieRpt.getPreviousYearDate(ieBean.getStartDate(),ieBean.getEndDate(),true);
			String tempheading="Total for "+value[0]+" to "+value[1];
		%>
	<center><u><b><div class = "normaltext"><%=request.getParameter("companyDetail_name")%></div></b></u></center>
	<center><u><b><div class = "normaltext">INCOME AND EXPENDITURE STATEMENT <%= title %><br><bean:message key="IncExp.Name"/></br></div></b></u></center>
	<b><div class = "normaltext" align="right">Amount in:<%= amountIn %></div></b></u>
	<div class="tbl2-container" id="tbl-container" style="width:970px;">
			<display:table cellspacing="0" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
			<display:caption media="pdf"><%=compName%>&nbsp;-&nbsp;INCOME AND EXPENDITURE STATEMENT<%= title %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in:<%= amountIn %>
			</display:caption>
			<display:caption media="excel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INCOME AND EXPENDITURE STATEMENT<%= title %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount in:<%= amountIn %>
			</display:caption>
			<div STYLE="display:table-header-group">
			<%
				temp++;
				tempStyle="";
				if(temp==2)
					tempStyle="background-color:#69c;text-align:center"; 
			 %>
			<display:column property="accCode" title="Account Code" style="<%=tempStyle%>"/>
			<display:column property="scheduleName" title="Head&nbsp;Of&nbsp;Account" style="<%=tempStyle%>"/>
			<display:column   title="Schedule No" style="<%=tempStyle%>">
			<%!
				String url="";
				String scheduleId="";
			%>
			<%
				scheduleId=((HashMap)pageContext.getAttribute("currentRowObject")).get("scheduleId").toString();
			%>
			<span onMouseOver="this.style.cursor='hand'" onclick="setURL(<%=scheduleId%>)"><%=((HashMap)pageContext.getAttribute("currentRowObject")).get("scheduleNo")%></span>
			</display:column>
			<%  
			for(int i=0;i<fundIdList.length;i++)
			{
				Object fund=((HashMap)pageContext.getAttribute("currentRowObject")).get(fundIdList[i]);	
					
				%>
				<display:column title="<%=fundNameList[i]%>" class="textAlign" style="{whitespace: nowrap; }" style="<%=tempStyle%>">	
				<%=fund %>
				</display:column>
				<%
			}
			%>	
			<display:column property="currentYearTotal" title="Total" class="textAlign" style="<%=tempStyle%>"/>	
			<display:column property="previousYearTotal" title="<%=tempheading%>"  class="textAlign3" style="<%=tempStyle%>"/>	
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="IncomeExpenseReport.pdf" /> 
			<display:setProperty name="export.csv" value="false" />
		    <display:setProperty name="export.xml" value="false" />
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="IncomeExpenseReport.xls"/> 			
			</div>
			<display:footer>
				<tr>
					<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
				<tr>
	  		</display:footer>
			</display:table>
	</div>
	<%
		}
		 catch(Exception e)
		 {
		 	LOGGER.error("Exception in Jsp Page "+ e.getMessage());		 
		 }
	}
%>
</form>
</body>
</html>
