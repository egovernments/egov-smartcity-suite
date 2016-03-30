<!--
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
  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page  import="java.util.Date,java.text.SimpleDateFormat,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.reports.GeneralLedgerReport,com.exilant.eGov.src.reports.*,com.exilant.eGov.src.transactions.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ page import="org.apache.log4j.Logger"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<div class="formmainbox">
<div class="formheading"></div>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<html>
<head>
<title>Sub-Ledger Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache> 
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" /> 
<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>
 <script type="text/javascript" src="../commonyui/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="../commonyui/build/dom/dom.js" ></script>
<script type="text/javascript" src="../commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/event/event-debug.js"></script>
<script type="text/javascript" src="../commonyui/build/animation/animation.js"></script>
<link type="text/css" rel="stylesheet" href="../commonyui/build/reset/reset.css">
<link type="text/css" rel="stylesheet" href="../commonyui/build/fonts/fonts.css">
<script type="text/javascript" src="../resources/javascript/contra.js?rnd=${app_release_no}"></script>
<link href="/EGF/resources/css/budget.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<link href="/EGF/resources/css/commonegovnew.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
<link href="/EGF/resources/css/error.css" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="/egi/commonyui/yui2.7/yuiloader/yuiloader-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
<script type="text/javascript" src="/egi/commonyui/build/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/calenderNew.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/EGF/resources/javascript/jquery-ui-1.8.22.custom.min.js"></script>  
<script type="text/javascript" src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/ajax-script.js?rnd=${app_release_no}"></script>
<link rel="stylesheet" type="text/css" href="/EGF/resources/css/jquery-ui/css/ui-lightness/jquery-ui-1.8.4.custom.css" />
<script type="text/javascript" src="/EGF/resources/javascript/tdsReportHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/subLedgerReportHelper.js?rnd=${app_release_no}"></script>


<style type="text/css">
#codescontainer {position:absolute;left:11em;width:9%}
#codescontainer .yui-ac-content {position:absolute;width:100%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:60%;background:#a0a0a0;z-index:9049;}
#codescontainer ul {padding:5px 0;width:100%;}
#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
#codescontainer li.yui-ac-highlight {background:#ff0;}
#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>

<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->

<SCRIPT LANGUAGE="javascript">
<%
 	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	Date crntDate = new Date();
	String currDate = sdf.format(crntDate);
%>
var path="${pageContext.request.contextPath}";
var index=null;
var flag='0', tbNames, tbIds;
var rptType;
var accEntText='';
var rptTitle='', rptStartDate='', rptEndDate='', rptGLCode='', rptAccName='', detail='';
var fuId="";
var fuSrId="";
var sDate="";
var eDate="";
var gl="";
var gl2="";
var accEntityKey="";
var reportType="";
var accEntityId="";
var coaId="";
var entityDetailArray;           
var codeObj;
function onBodyLoad()
{
	PageValidator.addCalendars();	
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('cgNumber');
	var accCode=document.getElementById('glCode1');	
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row3").style.display="block";
		document.getElementById("row2").style.display="none";		
	}		
	var drillDownFromSchedule=<%=request.getParameter("drillDownFromSchedule")%>;
	if( drillDownFromSchedule == true)
	{
		document.getElementById('tbl-header1').style.display="none";
		document.getElementById('fund_id').style.display="none";
		//document.getElementById('fundSource_id').style.display="none";
	}
	var type='getSubLedgerScheduleCodes';
    var url = "../commons/Process.jsp?type="+type;
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
					acccodeArray=codes.split("+");
					codeObj = new YAHOO.widget.DS_JSArray(acccodeArray);
                }
            }
      };
      req2.open("GET", url, true);
      req2.send(null);	    
}

function ButtonPress()
{
	if (!PageValidator.validateForm())
		return;	
	var accEntity = document.getElementById('accEntityList');
	var accCode = document.getElementById('glCode1');
	sDate=document.getElementById("startDate").value;
	eDate=document.getElementById("endDate").value;	
	var accEntityText = document.getElementById('accEntityList');
	if(document.getElementById('accEntityId').value!='')
	{
		document.forms[0].accEntityText.value=accEntityText.value;
	} 	
	if(accCode.value.length==0)
	{
		bootbox.alert("please select Account Code");
		return;
	}
	if(sDate.length==0||eDate.length==0)
	{
		bootbox.alert("please select start dates and end dates");
		return;
	}
	if(document.getElementById('accEntityKey').value=='')
	{
		bootbox.alert('please select a valid Accounting Entity');
		return;
	}  	
		document.getElementById('fromBean').value = 1;
		document.getElementById('glCode2').value=document.getElementById('glCode1').value;
		var fuObj=document.getElementById('fund_id');
		if(fuObj.value!='' && fuObj.selectedIndex!=-1){
			//fuId=fuObj.options[fuObj.selectedIndex].value;
			//bootbox.alert(fuObj.value);       
			document.getElementById("fundName").value=" under "+fuObj.options[fuObj.selectedIndex].text;
		}else{
			document.getElementById("fundName").value='';
		}
		fuId=fuObj.options[fuObj.selectedIndex].value;
		//var fuSrObj=document.getElementById('fundSource_id');
		//if(fuSrObj.value !='')
		//	fuSrId=fuSrObj.options[fuSrObj.selectedIndex].value;	
		reportType=document.getElementById('reportType').value;			
		accEntityId=document.getElementById('accEntityId').value;		
		var accEntityObj=document.getElementById('accEntityList');
		document.getElementById("accEntityId").value=document.getElementById("subLedgerList").value;
		
		/*to check whether start date is greater than the end date*/
		if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 )
		{
			bootbox.alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
		gl=document.getElementById("glCode1").value;
		gl2=document.getElementById("glCode2").value;

		if(rptType == "sl")	
		{
			var accEntObj=document.getElementById('accEntityList');
			getDescription(accEntObj);
			accEntText=document.getElementById('accEntityText').value;
		}			
		document.getElementById('chartOfAccounts_id').value=coaId;

		 /* checking Fiscal year start Date with End Date of Fiscal year */	
		var tMonth=eDate.substr(eDate.length-7,2);
		if(tMonth<4)
		var fiscalYearStartDate="01/04/"+(eDate.substr(eDate.length-4,4)-1);
		else
		var fiscalYearStartDate="01/04/"+eDate.substr(eDate.length-4,4);
		if(compareDate(fiscalYearStartDate,sDate) == -1 )
		{ 
			bootbox.alert("Start Date and End Date should be in same financial year");
		   document.getElementById('startDate').focus();
		   return;
		}
		 /*to check whether the End Date is greater than the Current Date*/
		if( compareDate(formatDate6("<%=currDate%>"),formatDate6(eDate)) == 1 )
		{
			bootbox.alert('End Date cannot be greater than Current Date');
			document.getElementById('endDate').value='';
			document.getElementById('endDate').value='';
			return;
		}
			
		document.forms[0].submit();		
} 

</SCRIPT>
</head>

<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onBodyLoad()">
<jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>  
<form name="subLedger" action="SubLedgerReport.jsp?reportType=sl&fund_id="+fuId+"&glcode="+gl+"&glCode2="+gl2+"&startDate="+sDate+"&endDate="+eDate+ "&reportType="+reportType+ "&accEntityId="+accEntityId+ "&accEntityKey=" +accEntityKey method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="index" id="index" value="null"> 

<input type="hidden" name="databaseDate" id="databaseDate" value="">
<input type=hidden id=reportType name=reportType value="sl">
<input type="hidden" name="glCode2" id="glCode2" value=""> 
<input type="hidden" name="fundName" id="fundName" value=""> 

<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


	<table width="100%" border=0 cellpadding="3" cellspacing="0"  id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="tableheader" valign="center" width="100%" colspan="4"><span id="screenName">Books of Accounts-SubLedgerReport</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>
		
		<!-- row-1 -->
		<tr>
			<td align="right"><div  valign="center" class="labelcell" >Account Code<span class="leadon">* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input type="hidden" name="chartOfAccounts_id" id="chartOfAccounts_id" value="">
				<input class="fieldinput" name="glCode1" id="glCode1" exilMustEnter="true" autocomplete="off" onkeyup="autocompletecode(this,event);" onblur="fillNeibrAfterSplit(this,'chartOfAccounts_id')" ><IMG id=IMG1 onclick="openAccountCodeSearch()" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
			</td>
			<td align="right"><div  valign="center" class="labelcell">Fund<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell"> 
				<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
			</td>
		</tr>
		<tr>
			<td align="right"><div  valign="center" class="labelcell">Starting 
				Date<span class="leadon">* &nbsp;</span></div></td>
			<td align="left" class="smallfieldcell">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
			<td align="right"> <div  valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
			<td class="smallfieldcell">
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilAnyDate" exilCalendar="true" exilMustEnter="true">
			</td>
		</tr>
		
		<!-- row-4 -->
		<tr>
			<td  align=right><div class="labelcell">Sub Ledger Type<span class="leadon">* &nbsp;</span></td></div>
			<td align="left" class="smallfieldcell"><input type="hidden" name="accEntityId" id="accEntityId">
			
			 <SELECT class="fieldinput" name="subLedgerList" id="subLedgerList" exilMustEnter="true" onChange="getDescription(this)" ></SELECT>
		
			</td>


			<td  align=right><div class="labelcell"> Entity Detail<span class="leadon">* &nbsp;</span></td></div>
			<td class="smallfieldcell">
			<input class="fieldinput" type="hidden" name="accEntityText" id="accEntityText"> 
			<input class="fieldinput" type="hidden" name="accEntityKey" id="accEntityKey">			
			<input class="fieldinput" name="accEntityList" id="accEntityList" autocomplete="off" onblur="splitEntitiesDetailCode(this)" onfocus="onFocusDetailCode();" onkeyup="autocompleteEntities1(this,event)"/>
			<IMG id=IMG1 onclick="openEntitySearch()" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
			<input class="fieldinput"  name="entityName" id="entityName"    readonly tabIndex=-1>
			</td>
			
		</tr>
		<tr>
		<td align="right"><div align="right" valign="center" class="labelcell" id="departmentLevel">Department</div></td>
			<td align="left" width="260" class="smallfieldcell">
				<SELECT class="fieldinput" id="departmentId" name="departmentId" exilListSource="departmentNameList" ></SELECT>
			</td>

		<td>
		<div id="codescontainer"></div>
		</td>
		</tr>
		<td>
		<div id="codescontainer"></div>
		</td>
		
		
	<tr id="row2" name="row2">
		<td colspan="4" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
			
			<td align="right">
			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
			<!-- Print end-->

		</tr>
		</table>
		</td>
	</tr>
	
	<tr style="DISPLAY: none" id="row3" name="row3">
		<td colspan="4" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search"></td>
			
			<td align="right">
			<input type=button class=button onclick="buttonFlush()" href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="pageSetup(); buttonPrint()" href="#" value="Print"></td>
			<!-- Print end-->

		</tr>
		</table>
		</td>
	</tr>


	</table>

<br>
</div></div></div>
</td></tr>
</table>
</center>
</div>

<%
	Logger LOGGER = Logger.getLogger("SubLedgerReport.jsp");
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
		 String[] accName=null;
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		accName = glReportBean.getAccountCode().split(" : ");
		 }
		 catch(Exception e){
		 LOGGER.info("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
	<center><u><b><div class = "normaltext">Sub Ledger for <%= request.getParameter("entityName") %><br><bean:message key="SL.Name"/></br>
	in <%=accName[1] %> <%= request.getParameter("fundName") %> <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>
			
	<table border="1" style="border: solid 1px #000000" style="width:970px;">
		<tr colspan=2 >
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Debit</center></B></td>
			<td width=50% class="normaltext" style="border: solid 1px #000000" align="center"><B><center>Credit</center></B></td>
		</tr>
	</table>

	<div class="tbl2-container" id="tbl-container" style="width:970px;" >
		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Sub Ledger Report for &nbsp;<%= request.getParameter("entityName") %>&nbsp;&nbsp;in  &nbsp;<%=accName[1] %><%= request.getParameter("fundName") %>&nbsp;&nbsp;from<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> 
		</display:caption>	
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date"  style="width:95px;"   />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
	     		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))
		     			link="javascript:callme('"+cgn+"','"+cgn1+"')";
		     		else 	
	     				link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitparticular" title="Particulars" style="width:130px;"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date"  style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No."  style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
   					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))	
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 	
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditparticular" title="Particulars" style="width:130px;"/>

				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="SubLedgerReport.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="SubLedgerReport.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

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
%>

	<%
      if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 String[] accName=null;
		try{
		 GeneralLedgerReport glReport = new GeneralLedgerReport ();
		 request.setAttribute("links",glReport.getGeneralLedgerList(glReportBean));
		 accName = glReportBean.getAccountCode().split(" : ");
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>


	<% if(null != accName){ %>

    <center><u><b><div class = "normaltext">Sub Ledger-<%= request.getParameter("entityName") %><br><bean:message key="SL.Name"/><br>
        <%= accName[1]%> <br>
    <%}%>
	
	<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>  </div></b></u></center>

	 <table border="1"  class="normaltext" cellspacing="0"  style="width:970px;">
		<tr colspan=2 >
			<td width=49%  align="center"><B><center>Debit</center></B></td>
			<td width=49%  align="center"><B><center>Credit</center></B></td>
		</tr></center>
	</table>

	 <div class="tbl4-container" id="tbl-container" style="width:970px;">

		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="true" sort="list" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Sub Ledger Report for<%= request.getParameter("entityName") %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		in <%=accName[1] %><%= request.getParameter("fundName") %>from<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> 
		</display:caption>
		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Voucher&nbsp;Date"  style="width:95px;"   />
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No."  style="width:100px;">

	  		<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
		  		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	     		String cgn1="",link="";
	     		if(cgn.length()>0)
	     		{
	     			cgn1=cgn.substring(0,3);
	     		}
	     		if(cgn.length()>0)
	     		{
	     			if(deployment.equalsIgnoreCase("NN"))	
	     				link="javascript:callme('"+cgn+"','"+cgn1+"')";
	     			else 	
	     				link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";	
	     		}
			String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getVouchernumber();%>

			<a href="<%=link%>"><%=url%></a>
			</display:column>
			<display:column property="debitVoucherTypeName" title="Voucher Type- Name"/>
			<display:column property="debitparticular" title="Particulars" style="width:130px;"/>
			<display:column property="debitamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign" />
			<display:column property="creditdate" title="Voucher&nbsp;Date" style="width:90px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No."  style="width:100px;" >

				<% String cgn =((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCGN();
					String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
					String cgn1="",link="";
					if(cgn.length()>0)
					{
						cgn1=cgn.substring(0,3);
					}
					if(cgn.length()>0)
					{
						if(deployment.equalsIgnoreCase("NN"))
							link="javascript:callme('"+cgn+"','"+cgn1+"')";
						else 	
	     					link="javascript:callmeAP('"+cgn+"','"+cgn1+"')";		
					}
				String url = ((GeneralLedgerBean)pageContext.findAttribute("currentRowObject")).getCreditvouchernumber();%>

				<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="creditVoucherTypeName" title="Voucher Type- Name"/>
				<display:column property="creditparticular" title="Particulars" style="width:130px;"/>
				<display:column property="creditamount" title="&nbsp;&nbsp;&nbsp;Amount&nbsp;(Rs.)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="SubLedgerReport.pdf" /> 
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="SubLedgerReport.xls"/>
			    <display:setProperty name="export.csv" value="false" />
				<display:setProperty name="export.xml" value="false" />

		</div>

		<display:footer>
			<tr>
				<td style="border-left: solid 0px #000000" colspan="20"><div style="border-top: solid 1px #000000 ">&nbsp;</div></td>
			</tr> 
  		</display:footer>
  	</display:table>
	</div>
<%
	}
%>
<div id="codescontainer"/>
<div id="loading" style="position:absolute; left:25%; top:70%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
    <div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
        <img src="/egi/resources/erp2/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Loading...
    </div>
</div>
</form>
</body>
</html>


