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
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<!--
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" />
-->
<SCRIPT LANGUAGE="javascript">

var fuId="";
var fuSrId="";
var sDate="";
var eDate="";
var gl1="";
var gl2="";
var forRE="";
reportType="";

function onLoad()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	var list=document.getElementById('boundary_id');
	list.setAttribute('exilListSource', 'getboundaryNames');
	PageManager.DataService.callDataService('getboundaryNames');
	document.getElementById('fund_id').focus();

}


/*function getAccCode()
{
	var obj=document.getElementById('glcode1');
	var text=obj.options[obj.selectedIndex].innerHTML;
    	var temp=text.split(" -- ");
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=text;
	if(!temp[1])
		temp[1]=" ";

	if(temp[1]!=null && temp[1]!="" )
	{
		obj.options[obj.selectedIndex].text=temp[0];
	}
}
*/


function ButtonPress()
{
obj=document.getElementById("boundary_id");
document.getElementById("boundary").value=obj.options[obj.selectedIndex].value;
	if (!PageValidator.validateForm())
	{
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	var obj=document.getElementById("reportType");
//bootbox.alert("obj.checked:"+obj.checked);
if(obj.checked==true)
	document.getElementById('fromBean').value = 3;
else
	document.getElementById('fromBean').value = 1;
	//document.getElementById('glCode2').value=document.getElementById('glCode1').value;
	var fuObj=document.getElementById('fund_id');
	fuId=fuObj.options[fuObj.selectedIndex].value;
	//var fuSrObj=document.getElementById('fundSource_id');
	//fuSrId=fuSrObj.options[fuSrObj.selectedIndex].value;
	document.getElementById("fundname").value=fuObj.options[fuObj.selectedIndex].text;
	sDate=document.getElementById("startDate").value;
	eDate=document.getElementById("endDate").value;
	if( compareDate(formatDate6(sDate),formatDate6(eDate)) == -1 )
	{
		bootbox.alert('Start Date cannot be greater than End Date');
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		document.getElementById('startDate').focus();
		return false;
	}
	gl1=document.getElementById("boundary").value;
	gl2=document.getElementById("glCode2").value;
	//forRE=document.getElementById("forRevEntry").value;
	reportType=document.getElementById("reportType").value;
	deptObj=document.getElementById('departmentId');
	 if(deptObj.value!='' && deptObj.selectedIndex!=-1){
			//deptName=deptObj.options[deptObj.selectedIndex].text;
			//bootbox.alert("DepartmentName="+deptName);
			 document.getElementById('dept_name_for').value=' under '+deptObj.options[deptObj.selectedIndex].text+ ' Department';
			deptObj=deptObj.options[deptObj.selectedIndex].value;
		}else{
		document.getElementById('dept_name_for').value='';
		}
		
		functionaryObj=document.getElementById('functionaryId');
		if(functionaryObj.value!='' && functionaryObj.selectedIndex!=-1){
			//functionaryName=functionaryObj.options[functionaryObj.selectedIndex].text;
			//bootbox.alert("functionaryName="+functionaryName);
			//subtitle=" in "+functionaryName+" Functionary";
			document.getElementById('functionary_name_in').value=' in '+functionaryObj.options[functionaryObj.selectedIndex].text+' functionary';
			functionaryObj=functionaryObj.options[functionaryObj.selectedIndex].value;
		}else{
		document.getElementById('functionary_name_in').value='';
		}
		fieldObj=document.getElementById('fieldId');
		if(fieldObj.value!='' && fieldObj.selectedIndex!=-1){
			//fieldObj=fieldObj.options[fieldObj.selectedIndex].value;
			document.getElementById('field_name_in').value=' in '+fieldObj.options[fieldObj.selectedIndex].text+' field';
			//fieldName=fieldObj.options[fieldObj.selectedIndex].text;
			//bootbox.alert("fieldName="+fieldName);
			//subtitle=" under "+fieldName+" Field";
		}else{
		document.getElementById('field_name_in').value='';
		}
		//functionCodeObj=document.getElementById('functionCodeId');
		functionCodeIdObj=document.getElementById('functionCode');
		
		if(functionCodeIdObj.value!=''){
			document.getElementById('functionCode_name').value=' in '+functionCodeIdObj.value+' Function Code';
		}else{
		document.getElementById('functionCode_name').value='';
		}
	document.forms[0].submit();


}
function ChangeReportType()
{

var frombean=document.getElementById('fromBean').value
if(frombean!=0)
{ButtonPress();
 }

}
function buttonFlush()
{

	window.location="CashBookRcptPmt.jsp";

}

/*
function openSearch(obj)
{
	var a = new Array(2);
	var screenName="4";
	var str= "../HTML/Search.html?cashCode="+screenName;
	var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	a = sRtn.split("`~`");
	var type = document.getElementById('glCode1');
	type.value = a[0];
}
*/
function pageSetup()
{
document.body.leftMargin=0.25;
document.body.rightMargin=0.25;
document.body.topMargin=0.25;
document.body.bottomMargin=0.25;
//document.body.clientWidth=600;
}

//For print method
function buttonPrint()
{
	var frmbean=document.getElementById('fromBean').value;
	if(frmbean==0)
	{bootbox.alert("Generate Report before Printing"); Return}

	document.getElementById('fromBean').value ="2";
    var hide1,hide2;
    hide1 = document.getElementById("tbl-header1");
	hide1.style.display = "none";
	// hide2 = document.getElementById("currentRowObject");
	document.forms[0].submit();
  	if(window.print)
  		{  window.print(); 	}
}



function beforeRefreshPage(dc)
{




}

function afterRefreshPage(dc)
{

	if(dc.values['serviceID']=='getboundaryNames')
	{
		obj=document.getElementById("boundary_id");
		obj.value=obj.options[0].value;
		obj.selectedIndex=0;
		if(obj.length==1);
		{
			//document.getElementById("boundary").value=obj.options[0].value;
			obj.disabled=true;
		}
	}
}



</SCRIPT>


</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
 <jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>
<form name="CashBook"  action = "CashBookRcptPmt.jsp?fund_id="+fuId+"&boundary_id="+gl1+"&glCode2="+gl2+"&startDate="+sDate+"&endDate="+eDate"&reportType="+reportType method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="fundname" id="fundname" value="">
<input type="hidden" name="glCode2" id="glCode2" value="">
<input type="hidden" name="boundary" id="boundary" value="">
<input type="hidden" id="functionCode_name" name="functionCode_name" value="">
<input type="hidden" id="functionCodeId" name="functionCodeId" value="">
<input type="hidden" id="field_name_in" name="field_name_in" value="">
<input type="hidden" id="functionary_name_in" name="functionary_name_in" value="">
<input type="hidden" id="dept_name_for" name="dept_name_for" value="">

<div class="tbl-header1" id="tbl-header1">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<table width="100%" border=0 cellpadding="3" cellspacing="0" >
<tr >
		<td class="tableheader" valign="center" colspan="6" width="100%"><span id="screenName">Cash Book</span></td>
	</tr>
	<tr>
		<td align="right"><div  valign="center" class="labelcell"  >Boundary <span class="leadon">* &nbsp;</span></div></td>
		<td class="smallfieldcell">

		<!-- 	<input class="fieldinput" name="glCode1" id="glCode1" exilMustEnter="true" ><IMG id=IMG1 onclick="openSearch('chartofaccounts');" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0> -->
			<SELECT class="combowidth1" id="boundary_id" name="boundary_id" exilMustEnter="true" ></SELECT>

		</td>
		<td align="right"><div  valign="center" class="labelcell">Fund <span class="leadon">* &nbsp;</span></div></td>
		<td align="left" class="smallfieldcell">
			<SELECT class="fieldinput" id="fund_id" name="fund_id" exilListSource="fundNameList" exilMustEnter="true" tabindex=1></SELECT>
		</td>
	</tr>

	<tr>
		<td align="right"><div  valign="center" class="labelcell">Starting Date<span class="leadon">* &nbsp;</span></div></td>
		<td align="left" class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true" tabindex=1>
		</td>
		<td align="right" ><div valign="center" class="labelcell" >End Date<span class="leadon">* &nbsp;</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true" tabindex=1>
		</td>
	</tr>
	<%@ include file="reportParameter.jsp" %>
	<tr>
	<!--<td><div align="right" valign="center" class="normaltext">Financing Source</div></td>
		<td align="left">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id" exilListSource="fundSourceNameList"></SELECT>
		</td>
		<td width=35%><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td width=15%>
			<input type="checkbox" name="forRevEntry" id="forRevEntry" ></td>
		</td > -->
		<td width=35%  align="right"><div  valign="center" class="labelcell">
		Show Condensed Report &nbsp;</div></td><td  width=15%><input type="checkbox" name="reportType" id="reportType" value="off" onclick="ChangeReportType()" tabindex=1 ></td></tr>
	</tr>
	<!--<tr height="5"><td><div align="right" valign="center" class="normaltext">
		Show Condensed Report</div></td><td><input type="checkbox" name="reportType" id="reportType" value="off" onclick="ChangeReportType()" ></td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>-->
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick="ButtonPress()" href="#" value="Search" tabindex=1></td>

			<td align="right">
			<input type=button class=button onclick=buttonFlush(); href="#" value="Cancel" tabindex=1></td>

			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close" tabindex=1></td>

			<!-- Print Button start -->
			<td align="right">
			<input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print" tabindex=1></td>
			<!-- Print end-->
		</tr>
		</table>
		</td>
	</tr>
</table>

</div></div></div>
</td></tr>
</table>
</div>
<%
    //System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
    	//&& request.getParameter("reportType")==null & !request.getParameter("reportType").equals("on")
	  //System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
		try{
		 CashBook generalLedgerList = new CashBook();
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 //System.out.println("after from java file");
		 }
		 catch(Exception e){
		 //System.out.println("Exception in Jsp Page "+ e.getMessage()); 
%>
		 <script>
		 bootbox.alert("Error1 :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		<% 
		}
%>

			
		<center><u><b><div class = "normaltext"> <%= glReportBean.getUlbName() %><bean:message key="CashBook.Name"/><br>  Cash Book for <%=request.getParameter("fundname")%></br> <br>  <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %></br></div></b></u></center>
		<div class = "alignleft">   </div>	
		<table border="1"    width="100%">
					<tr colspan=2 >
								<td width=50% align="center"><B><center>Debits(Dr)</center></B></td>
								<td width=50% align="center"><B><center>Credits(Cr)</center></B></td>
					</tr></center>
				</table>
	
		<div class="tbl-container" id="tbl-container">
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list"  class="its" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= glReportBean.getUlbName() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Cash Book for <%=request.getParameter("fundname")%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <%= request.getParameter("dept_name_for") %> <%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate")%> 
		</display:caption>
			<display:column property="rcptVchrDate" title="VoucherDate" sortable="true"  headerClass="sortable" />
			<display:column media="pdf" property="rcptVchrNo" title="Voucher#" />
			<display:column media="excel" property="rcptVchrNo" title="Voucher#" />
			<display:column media="xml" property="rcptVchrNo" title="Voucher#" />
			<display:column media="csv" property="rcptVchrNo" title="Voucher#" />
			<display:column media="html" title="&nbsp;&nbsp;&nbsp;&nbsp;Voucher#&nbsp;&nbsp;&nbsp;&nbsp;" sortable="true"  headerClass="sortable">

							  		<% String cgn =((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getCGN();
							  		//String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getRcptVchrNo();
							     		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
							     		String cgn1="",link="";
							     		////System.out.println("check1");
							     		////System.out.println("check2"+cgn+"url:"+url);
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
							     		
							     		String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getRcptVchrNo();
 %>
									<a href="<%=link%>"><%=url%></a>
				</display:column>
				<display:column property="rcptAccCode" style="width:50px" title="Account Code" />
				<display:column property="rcptFuncCode" title="Function Code" />
				<display:column property="rcptBgtCode" style="width:50px;border-left:1px solid #000000;" title="Budget Code" />
				<display:column property="rcptParticulars" style="width:150px"  title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />
				<display:column property="rcptcashInHandAmt" style="text-align:right" title="<center>CashInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471100)&nbsp;</center><center>CashInHand(Rs)</center>" />
				<display:column property="rcptChqInHandAmt"  style="text-align:right" title="<center>ChequeInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471200)&nbsp;</center><center>ChequeInHand(Rs)</center>" />
				<display:column property="rcptSrcOfFinance" title="Source of Financing code" />

				<display:column property="pmtVchrDate" title="VoucherDate" sortable="true"  headerClass="sortable"/>
					<display:column media="pdf" property="pmtVchrNo" title="Voucher#" />
					<display:column media="excel" property="pmtVchrNo" title="Voucher#" />
					<display:column media="xml" property="pmtVchrNo" title="Voucher#" />
					<display:column media="csv" property="pmtVchrNo" title="Voucher#" />
					<display:column media="html" title="&nbsp;&nbsp;&nbsp;&nbsp;Voucher#&nbsp;&nbsp;&nbsp;&nbsp;" sortable="true"  headerClass="sortable">

									  		<% String cgn =((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getCGN();
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
											String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getPmtVchrNo();%>

											<a href="<%=link%>"><%=url%></a>
						</display:column>
						
						<display:column property="pmtAccCode" style="width:50px" title="Account Code" />
						<display:column property="pmtFuncCode" style="width:50px" title="Function Code" />
						<display:column property="pmtBgtCode" style="width:50px" title="Budget Code" />
						<display:column property="pmtParticulars" style="width:150px" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />
						<display:column property="pmtCashInHandAmt"  style="text-align:right" title="<center>CashInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471100)&nbsp;</center><center>CashInHand(Rs)</center>" />
						<display:column property="pmtChqInHandAmt"  style="text-align:right" title="<center>ChequeInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471200)&nbsp;</center><center>ChequeInHand(Rs)</center>" />
					<display:column property="pmtSrcOfFinance" class="textAlign3" title="Source of Financing code" />

			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="CashBookRcptPmt.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="CashBookRcptPmt.xls"/>
		    <display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.xml" value="false" />

</div>
		<display:footer>

					<tr>
						<td style="border-left:0px solid #000000" colspan="20"><div style="border-top:1px solid #000000">&nbsp;</div></td>
					</tr>
		</display:footer>
</display:table>


<%
}	


if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("3") )
    {
	 //System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
		try{
		 CashBook generalLedgerList = new CashBook();
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 //System.out.println("after from java file");
		 }
		 catch(Exception e){
		 //System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error3 :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
<center><u><b><div class = "normaltext"> <%= glReportBean.getUlbName() %> <bean:message key="CashBook.Name"/><br>  Cash Book for <%=request.getParameter("fundname")%> <br>  <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>
		<div class = "alignleft">   </div>

	<div STYLE="display:table-header-group">
	<table border="1"    width="100%">
			<tr colspan=2 >
						<td width=50% align="center"><B><center>Debits</center></B></td>
						<td width=50% align="center"><B><center>Credits </center></B></td>
			</tr></center>
		</table>
		<div class="tbl-container" id="tbl-container">
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list"  class="its" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= glReportBean.getUlbName() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Cash Book for <%=request.getParameter("fundname")%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %>from<%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>
		</display:caption>
		
		<display:column property="rcptVchrDate" title="VoucherDate" sortable="true"  headerClass="sortable" />
		<display:column property="rcptAccCode" style="width:50px" title="Account Code" />
		<display:column property="rcptParticulars" style="width:150px" headerClass="combowidthforGLCode" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />
		<display:column property="rcptcashInHandAmt" style="text-align:right" title="<center>CashInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471100)&nbsp;</center><center>CashInHand(Rs)</center>" />
		<display:column property="rcptChqInHandAmt"  style="text-align:right" title="<center>ChequeInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471200)&nbsp;</center><center>ChequeInHand(Rs)</center>" />
		<display:column property="pmtVchrDate" title="VoucherDate" sortable="true"  headerClass="sortable" />
		<display:column property="pmtAccCode" style="width:50px" title="Account Code" />
		<display:column property="pmtParticulars" style="width:150px" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" />
		<display:column property="pmtCashInHandAmt"  style="text-align:right" title="<center>CashInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471100)&nbsp;</center><center>CashInHand(Rs)</center>" />
		<display:column property="pmtChqInHandAmt"  style="text-align:right" title="<center>ChequeInHand</center><center>&nbsp;(Account&nbsp;Code&nbsp;471200)&nbsp;</center><center>ChequeInHand(Rs)</center>" />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="CashBookRcptPmt.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="CashBookRcptPmt.xls"/>
	    <display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />
 </div>
<display:footer>

					<tr>
						<td style="border-left:0px solid #000000" colspan="20"><div style="border-top:1px solid #000000">&nbsp;</div></td>
					</tr>
		</display:footer>
</display:table>

 </div>
<%
	}
%>

				<!--  Added by Sathish for Print purpose -->

<%
    //System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 //System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
		try{
		 CashBook generalLedgerList = new CashBook();
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		 //System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %>
		 <script>
		 bootbox.alert("Error2 :<%=e.getMessage()%>");
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>
 <center><u><b><div class = "normaltext">  <%= glReportBean.getUlbName() %><bean:message key="CashBook.Name"/> </br><br> Cash Book for <%=request.getParameter("fundname")%> <br>  <%= request.getParameter("dept_name_for") %> 
			<%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>
 	<div class="alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed </div>
 <div class="tbl4-container" id="tbl-container" >
 <table border="1"    width="100%">
			<tr colspan=2 >
						<td width=50% align="center"><B><center>Debits(Dr)</center></B></td>
						<td width=50% align="center"><B><center>Credits(Cr) </center></B></td>
			</tr></center>
</table>
 
 	<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= glReportBean.getUlbName() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Cash Book for <%=request.getParameter("fundname")%> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		 <%= request.getParameter("dept_name_for") %> <%= request.getParameter("functionCode_name") %>	<%= request.getParameter("functionary_name_in") %> <%= request.getParameter("field_name_in") %> from <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %>
		</display:caption>
			
			<div STYLE="display:table-header-group">
				<display:column property="rcptVchrDate" class="textAlign9" title="<font size=1 face=sans-serif>VoucherDate</font>" sortable="true"  headerClass="sortable" />
				<display:column media="pdf" property="rcptVchrNo"  title="Voucher#" />
				<display:column media="excel" property="rcptVchrNo" title="Voucher#" />
				<display:column media="xml" property="rcptVchrNo" title="Voucher#" />
				<display:column media="csv" property="rcptVchrNo" title="Voucher#" />
				<display:column media="html" class="textAlign9" title="<font size=1 face=sans-serif>Voucher#</font>" sortable="true"  headerClass="sortable" >

								  		<% String cgn =((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getCGN();
								  		//String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getRcptVchrNo();
								     		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
								     		String cgn1="",link="";
								     		////System.out.println("check1");
								     		////System.out.println("check2"+cgn+"url:"+url);
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

										String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getRcptVchrNo();
										%>
											

										<a href="<%=link%>"><font size=1 face=sans-serif><%=url%></font></a>
							</display:column>
					<display:column property="rcptAccCode" style="width:50px;border-right:0px solid #000000" class="textAlign9" title="<font size=1 face=sans-serif>Account Code</font>" />
					<display:column property="rcptFuncCode" style="width:50px;border-right:0px solid #000000" class="textAlign9" title="<font size=1 face=sans-serif>Function Code</font>" />
					<display:column property="rcptBgtCode" style="width:50px" class="textAlign9" title="<font size=1 face=sans-serif>Budget Code</font>" />
					<display:column property="rcptParticulars" style="width:150px" headerClass="combowidthforGLCode" class="textAlign9" title="<font size=1 face=sans-serif>&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;</font>" />
					<display:column property="rcptcashInHandAmt" style="text-align:right" class="textAlign9" title="<font size=1 face=sans-serif><center>CashInHand</center><center>(Account&nbsp;Code&nbsp;471100)</center><center>CashInHand(Rs)</center></font>" />
					<display:column property="rcptChqInHandAmt"  style="text-align:right" class="textAlign9" title="<font size=1 face=sans-serif><center>ChequeInHand</center><center>(Account&nbsp;Code&nbsp;471200)</center><center>ChequeInHand(Rs)</center></font>" />
					<display:column property="rcptSrcOfFinance" class="textAlign9" title="<font size=1 face=sans-serif>Source of Financing code</font>" />
					
					    <display:column property="pmtVchrDate" class="textAlign9" title="<font size=0.5 face=sans-serif>VoucherDate</font>" sortable="true"  headerClass="sortable" />
						<display:column media="pdf" property="pmtVchrNo" title="Voucher#" />
						<display:column media="excel" property="pmtVchrNo" title="Voucher#" />
						<display:column media="xml" property="pmtVchrNo" title="Voucher#" />
						<display:column media="csv" property="pmtVchrNo" title="Voucher#" />
						<display:column media="html" class="textAlign9" title="<font size=0.5 face=sans-serif>Voucher#</font>" sortable="true"  headerClass="sortable">

										  		<% String cgn =((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getCGN();
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
												String url = ((GeneralLedgerReportBean)pageContext.findAttribute("currentRowObject")).getPmtVchrNo();%>

												<a href="<%=link%>"><font size=1 face=sans-serif><%=url%></font></a>
							</display:column>
							
							<display:column property="pmtAccCode" style="width:50px" class="textAlign9" title="<font size=0.5  face=sans-serif>Account Code</font>" />
							<display:column property="pmtFuncCode" style="width:50px" class="textAlign9" title="<font size=0.5  face=sans-serif>Function Code</font>" />
							<display:column property="pmtBgtCode" style="width:50px" class="textAlign9" title="<font size=0.5  face=sans-serif>Budget Code</font>" />
							<display:column property="pmtParticulars" style="width:150px" class="textAlign9" title="<font size=0.5 face=sans-serif>&nbsp;&nbsp;&nbsp;&nbsp;Particulars&nbsp;&nbsp;&nbsp;&nbsp;</font>" />
							<display:column property="pmtCashInHandAmt"  style="text-align:right" class="textAlign9" title="<font size=0.5  face=sans-serif><center>CashInHand</center><center>(Account&nbsp;Code&nbsp;471100)</center><center>CashInHand(Rs)</center></font>" />
							<display:column property="pmtChqInHandAmt"  style="text-align:right" class="textAlign9" title="<font size=0.5  face=sans-serif><center>ChequeInHand</center><center>(Account&nbsp;Code&nbsp;471200)</center><center>ChequeInHand(Rs)</center></font>" />
							<display:column property="pmtSrcOfFinance" style="width:50px; border-right:1px solid #000000" class="textAlign9" title="<font size=0.5  face=sans-serif>Source of Financing code</font>" />

			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="CashBookRcptPmt.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="CashBookRcptPmt.xls"/>
		    <display:setProperty name="export.csv" value="false" />
			<display:setProperty name="export.xml" value="false" />

</div>
<display:footer>

					<tr>
						<td style="border-left:0px solid #000000" colspan="20"><div style="border-top:1px solid #000000">&nbsp;</div></td>
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
