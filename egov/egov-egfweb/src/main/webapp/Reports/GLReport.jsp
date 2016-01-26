<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!-- <%@ page buffer = "16kb" %> -->
<%@page  import="com.exilant.eGov.src.reports.*,java.io.*,java.util.*,org.egov.infstr.utils.EGovConfig,java.io.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@page import="org.apache.log4j.Logger"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />

<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>

<SCRIPT LANGUAGE="javascript">

//var cWind;         // temporary window reference
var count=1;
var forRE="";
var cWindows=new Object();
//var flag='0', tbNames, tbIds,dbTotal=0,crTotal=0;
var strtDate="",endDate="",isDrillDown="";
var rptType;
var fObj;
var rptTitle='', rptStartDate='', rptEndDate='', rptGLCode='', rptAccName='',fundNameQ='', detail='',accCode_1='',accCode_2='',snapShotDateTime,forRevEntry,revEntry;

function onBodyLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	//PageManager.DataService.callDataService('cgNumber');
	if(PageManager.DataService.getQueryField('glCode1') != null)
	{
		document.getElementById('glCode1').value=PageManager.DataService.getQueryField('glCode1');
	}

	if(document.getElementById('glCode1').value)
	{
		if(isDrillDown!="Y")
			finHeader();
	}
}
function finHeader()
{
	var gLCode1 =document.getElementById('glCode1').value;
	if(gLCode1!="")
	{
		rptTitle = '<font size=3><b>Journal  Report for ' +gLCode1;
	}
}

function openSearch(obj,index){
	 var a = new Array(1);

		var screenName="report";
		var str="";

		if (obj.toLowerCase() == 'relation')
			str = "../HTML/Search.html?tableNameForCode="+obj;
		else if(obj.toLowerCase() == 'chartofaccounts'){
			if (rptType == "cb"){
				screenName="2";
				str = "Search.html?filterServiceID="+screenName;
			}
			else if(rptType == "bb"){
				//screenName="bankBook";
				screenName="13";
				str = "../HTML/Search.html?filterServiceID="+screenName;
			}else if(rptType == "sl"){
				str = "../HTML/Search.html?tableNameForCode=chartofaccounts_controlcodes";
			}else{
				str = "../HTML/Search.html?screenName="+screenName;
			}
		}
	 var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
     if (index==1){
		a = sRtn.split("`~`");
		var type = document.getElementById('glCode1');
		type.value = a[0];
		rptGLCode = a[0];
		//bootbox.alert(rptGLCode+" code");
		rptAccName = a[1];
		//bootbox.alert(rptAccName);
		//bootbox.alert("value "+a[2]);
		document.getElementById('chartOfAccounts_id1').value = a[2];
		document.getElementById('glCode1').value=a[0]+","+a[1];
		//getDetailData(a[0]);
	}


}

function ButtonPress()
{
	//bootbox.alert("search");
	if (!PageValidator.validateForm())
	{
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		return;
	}
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	var dbDate=document.getElementById('databaseDate').value;

	var aCode1 = document.getElementById('glCode1').value;
	accCode_1=aCode1;
	if(aCode1.length==0)
	{
		bootbox.alert("please select  Account Code");
		return;
	}
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
		bootbox.alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	//bootbox.alert("acc code "+accCode_1);
	PageManager.DataService.setQueryField('glCode1',accCode_1);
	 document.getElementById('fromBean').value = 1;

	 fObj = document.getElementById('fund_id');
	 fObj=fObj.options[fObj.selectedIndex].value;
	// bootbox.alert("fobj "+fObj);
	 var fsObj = document.getElementById('fundSource_id');
	 fsObj=fsObj.options[fsObj.selectedIndex].value;
	// cgn=document.getElementById("cgn").value;
		//gl1=document.getElementById("glCode1").value;
	forRE=document.getElementById("revEntry").value;
	//bootbox.alert(forRE);
	 document.JournalBook.submit();

}


function buttonFlush1()
{
	document.getElementById('glCode1').value='';
	window.location="GLReport.jsp";

}


function pageSetup()
{
document.body.leftMargin=0.75;
document.body.rightMargin=0.75;
document.body.topMargin=0.75;
document.body.bottomMargin=0.75;
}

//For print method
function buttonPrint()
{
	document.getElementById('fromBean').value ="2";
    var hide1,hide2;
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";
	document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	}
}


</SCRIPT>


</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()" onbeforeunload="closeChilds(cWindows,count)">
 <jsp:useBean id = "jbReportBean" scope ="request" class="com.exilant.GLEngine.GeneralLedgerBean"/>
<jsp:setProperty name = "jbReportBean" property="*"/>
<form name="JournalBook" action = "GLReport.jsp?fund_id="+fObj+"&fundSource_id="+fsObj+"&accountCodes="+accountCodes+"&startDate="+strtDate+"&endDate="+endDate method "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="databaseDate"  id="databaseDate">
<div class="tbl-header1" id="tbl-header1">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td class="rowheader" valign="center" colspan="4" width="100%"><span id="screenName" class="headerwhite2">GeneralLedger Report</span></td>
	</tr>
	<tr class="row1">
		<td><div align="right" valign="center" class="normaltext" >Account Code<span class="leadon">*</span></div></td>

		<td>
						<input type="hidden" name="chartOfAccounts_id1" id="chartOfAccounts_id1">
						<input class="fieldinput" name="glCode1" id="glCode1" ><IMG id=IMG1 onclick="openSearch('chartofaccounts',1);" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0>
					</td>
		<td colspan="2">&nbsp;</td>
	</tr>


	<tr class="row2">
		<td><div align="right" valign="center" class="normaltext">Fund<span class="leadon">*</span></div></td>
		<td align="left" width="260">
			<SELECT class="fieldinput" id="fund_id" name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
		</td>

		<td><div align="right" valign="center" class="normaltext">Financing Source</div></td>
		<td align="left">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id"  exilListSource="fundSourceNameList"></SELECT>
		</td>
	</tr>
	<tr class="row1">
		<td><div align="right" valign="center" class="normaltext">Starting Date<span class="leadon">*</span></div></td>
		<td align="left" width="260">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>
		<td><div align="right" valign="center" class="normaltext" >End Date<span class="leadon">*</span></div></td>
		<td>
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>

	</tr>
	<tr class ="row1">
			<td></td>
		<td width="260"></td>
		<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
		<input type="checkbox" name="revEntry" id="revEntry"></td>
		</td>
	</tr>

	<tr class="row1">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
					<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick=ButtonPress() href="#">Search</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush1(); href="#">Cancel</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>

					<!-- Print Button start -->
					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=8></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick="pageSetup();buttonPrint()" href="#">Print</A></td>
					<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=8></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>
					<!-- Print end-->


				</tr>
			</table>
		</td>
	</tr>

</table>
</div>
<br><br>


<%
	Logger LOGGER = Logger.getLogger("GLReport.jsp");
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 GLReport jbReport = new GLReport();
		 request.setAttribute("links",jbReport.getGLReport(jbReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
	<center><u><b><div class = "normaltext"> GeneralLedger Report<br> For Account <%= jbReportBean.getAccountCode() %> <br>
			Fund - <%= jbReportBean.getFundName() %> <br>
			From <%= request.getParameter("startDate") %> To <%= request.getParameter("endDate") %>  </div></b></u></center>
			<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>

	<div class="tbl2-container" id="tbl-container" >


		<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject" class="its"  export="false" sort="list" >

		<div STYLE="display:table-header-group">

		<display:column property="voucherdate" title="Date" sortable="true"   headerClass="sortable"  style="width:100px;"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable">

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
			<display:column property="debitparticular" title="Particulars" style="width:125px;" />
			<display:column property="debitamount" title="Amount (Rs.)" class="textAlign" />
			<display:column property="creditdate" title="Date" sortable="true"  headerClass="sortable" style="width:100px;" />
			<display:column media="pdf" property="creditvouchernumber" title="Voucher No." />
			<display:column media="excel" property="creditvouchernumber" title="Voucher No." />
			<display:column media="xml" property="creditvouchernumber" title="Voucher No." />
			<display:column media="csv" property="creditvouchernumber" title="Voucher No." />
			<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable">

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
				<display:column property="creditparticular" title="Particulars" style="width:130px;" />

				<display:column property="creditamount" title="Amount (Rs.)" class="textAlign3" />
				<display:setProperty name="export.pdf" value="true" />

		</div>

		<display:footer>

			<tr>
				<td colspan="10"><hr></td>
			</tr>
		</display:footer>
</display:table>

</div>
<%
	}
%>

	<%
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 GLReport jbReport = new GLReport();
		 request.setAttribute("links",jbReport.getGLReport(jbReportBean));
		 }
		 catch(Exception e){
		 LOGGER.error("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
	 <div class="tbl5-container" id="tbl-container" >

		<center><u><b><div class = "normaltext"> Journal Book <br><%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </div></b></u></center>
		<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="false" sort="list" class="its" >

		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="Date" sortable="true"  headerClass="sortable"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher No." />
		<display:column media="excel" property="vouchernumber" title="Voucher No." />
		<display:column media="xml" property="vouchernumber" title="Voucher No." />
		<display:column media="csv" property="vouchernumber" title="Voucher No." />
		<display:column media="html" title="Voucher No." sortable="true"  headerClass="sortable">

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

			<display:column property="code" title="Account Code" />
			<display:column property="accName" title="Account Name" style="width:250px" maxLength="50" />
			<display:column property="narration" title="Narration" />
			<display:column property="debitamount" title="Debit" class="textAlign" />
			<display:column property="creditamount" title="Credit" class="textAlign3" />
			<display:setProperty name="paging.banner.placement" value="true" />

		</div>
		<display:footer>

			<tr>
				<td colspan="10"><hr></td>
			</tr>
		</display:footer>
	</display:table>
	</div>
<%
	}
%>


</form>
</body>
</html>
