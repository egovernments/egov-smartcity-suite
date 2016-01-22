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
<%@page  import="com.exilant.eGov.src.reports.*,org.apache.log4j.Logger,org.egov.infstr.utils.EGovConfig,java.io.*,com.exilant.eGov.src.transactions.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<html>
<head>
<title>Journal Book Report</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />

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
var strtDate="",endDate="";

function onBodyLoad() 
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('cgNumber');
	var beanVal= <%=request.getParameter("fromBean")%>;
	if(beanVal==2)
	{
		document.getElementById("row2").style.display="block";
		document.getElementById("row1").style.display="none";		
	}
	document.getElementById("startDate").focus();
}


function ButtonPress()
{
	if (!PageValidator.validateForm())
	{
		//document.getElementById('startDate').value='';
		//document.getElementById('endDate').value='';
		return;
	}
	document.getElementById('voucher_name_in').value = '';
	document.getElementById('fundSource_name_in').value = '';
	document.getElementById('dept_name_for').value = '';
	strtDate = document.getElementById('startDate').value;
	endDate = document.getElementById('endDate').value;
	var dbDate=document.getElementById('databaseDate').value;
	
	/*if(compareDate(formatDateToDDMMYYYY1(endDate),formatDateToDDMMYYYY1(dbDate)) == -1 )
	{
		bootbox.alert('End Date should be less than or equal to '+dbDate);
		document.getElementById('endDate').focus();
		return false;
	}*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
		bootbox.alert('Start Date cannot be greater than End Date');
			document.getElementById('startDate').value='';
			document.getElementById('endDate').value='';
			document.getElementById('startDate').focus();
			return false;
		}
	 document.getElementById('fromBean').value = 1;
	
	 var fObj = document.getElementById('fund_id');
	 document.getElementById('fundName').value=fObj.options[fObj.selectedIndex].text;
	 fObj=fObj.options[fObj.selectedIndex].value;
	 var fsObj = document.getElementById('fundSource_id');
	//bootbox.alert("fsObj....."+fsObj.selectedIndex);
	if(fsObj!='' && fsObj.selectedIndex!=-1 && fsObj.options[fsObj.selectedIndex].value!=''){
		 document.getElementById('fundSource_name_in').value=' in '+fsObj.options[fsObj.selectedIndex].text;	 
		 fsObj=fsObj.options[fsObj.selectedIndex].value;
	}
	 var fpObj = document.getElementById('voucher_name');
	if(fpObj!='' && fpObj.selectedIndex!=-1 && fpObj.options[fpObj.selectedIndex].value!=''){
		 fpObj=fpObj.options[fpObj.selectedIndex].value;
		 document.getElementById('voucher_name_in').value = fpObj+' in ';
	}
	var fqObj = document.getElementById('dept_name');
	if(fqObj!='' && fqObj.selectedIndex!=-1 && fqObj.options[fqObj.selectedIndex].value!=''){
		 document.getElementById('dept_name_for').value=' for '+fqObj.options[fqObj.selectedIndex].text;
		 fqObj=fqObj.options[fqObj.selectedIndex].value;
	}	 
	// cgn=document.getElementById("cgn").value;	 
		
	//forRE=document.getElementById("RevEntry").value; 	 
	 document.JournalBook.submit();
 
}


function buttonFlush1()
{
	window.location="JournalBook.jsp";

} 


function pageSetup()
{
document.body.leftMargin=0.75;
document.body.rightMargin=0.75;
document.body.topMargin=0.75;
document.body.bottomMargin=0.75;
}


//For Print Preview method
function buttonPrintPreview()
{
	document.getElementById('fromBean').value ="2";	   
	document.forms[0].submit();	
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

function beforeRefresh()
{

	bootbox.alert("hi");
}



</SCRIPT>


</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()" onbeforeunload="closeChilds(cWindows,count)">
 <jsp:useBean id = "jbReportBean" scope ="request" class="com.exilant.GLEngine.GeneralLedgerBean"/>
<jsp:setProperty name = "jbReportBean" property="*"/>  
<form name="JournalBook" action = "JournalBook.jsp?fund_id="+fObj+"&fundSource_id="+fsObj+"&voucher_name="+fpObj+"&dept_name="+fqObj+"&startDate="+strtDate+"&endDate="+endDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="databaseDate"  id="databaseDate">
<input type="hidden" id="fundName" name="fundName" value="">
<input type="hidden" id="voucher_name_in" name="voucher_name_in" value="">
<input type="hidden" id="fundSource_name_in" name="fundSource_name_in" value="">
<input type="hidden" id="dept_name_for" name="dept_name_for" value="">
<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3" style="width:810px">


<div class="tbl-header1" id="tbl-header1">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td class="tableheader" valign="center" colspan="4" width="100%"><span id="screenName">Journal Book</span></td>
	</tr>
	<tr>
		<td><div align="right" valign="center" class="labelcell">Starting Date<span class="leadon">*</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>
		<td><div align="right" valign="center" class="labelcell" >End Date<span class="leadon">*</span></div></td>
		<td class="smallfieldcell">
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilCalendar="true" exilMustEnter="true">
		</td>

	</tr>

	<tr>
		<td><div align="right" valign="center" class="labelcell">Fund<span class="leadon">*</span></div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id=fund_id name="fund_id" exilListSource="fundNameList" exilMustEnter="true"></SELECT>
		</td>

		<td><div align="right" valign="center" class="labelcell">Voucher name</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="voucher_name" name="voucher_name"  exilListSource="voucherList"></SELECT>
		</td>

	
	</tr>
	
	<tr>
		<td><div align="right" valign="center" class="labelcell">Financing Source</div></td>
		<td align="left" class="smallfieldcell">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id"  exilListSource="fundSourceNameList"></SELECT>
		</td>
		
		<td><div align="right" valign="center" class="labelcell">Department</div></td>
		<td align="left" width="260" class="smallfieldcell">
			<SELECT class="fieldinput" id="dept_name" name="dept_name"  exilListSource="departmentName"></SELECT>
		</td>
	</tr>
	<tr>
			<td></td>
		<td width="260"></td>
		<!--<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
		<input type="checkbox" name="RevEntry" id="RevEntry" ></td>
		</td>-->
	</tr>
	
	<tr id="row1" name="row1">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
					
					<td align="right">
					<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>
					
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
	
	<tr style="DISPLAY: none" id="row2" name="row2">
		<td colspan="4" align="middle"><!-- Buttons Start Here -->
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
					
					<td align="right">
					<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>
					
					<td align="right">
					<input type=button class=button onclick=window.close() href="#" value="Close"></td>
					
					<!-- Print Button start -->
					<td align="right">
					<input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
					<!-- Print end-->


				</tr>
			</table>
		</td>
	</tr>
		
</table>
</div>
<br><br>
</div></div></div>
</td></tr>
</table>


<%
	Logger LOGGER = Logger.getLogger("JournalBook.jsp");
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 JbReport jbReport = new JbReport();			   
		 request.setAttribute("links",jbReport.getJbReport(jbReportBean));
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
			
		<center><u><b><div class = "normaltext"> Journal Book<br><bean:message key="JourBook.Name"/><br><%= request.getParameter("voucher_name_in") %><%= request.getParameter("fundName") %><%= request.getParameter("fundSource_name_in") %><%= request.getParameter("dept_name_for") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %> </div></b></u></center>   		
		<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>		
		<div class="tbl2-container" id="tbl-container" >
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Journal Book
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="Voucher Date" style="width:70px" sortable="true"  headerClass="sortable"/>
		
		<display:column media="pdf" property="vouchernumber" title="Voucher Number" /> 	
		<display:column media="excel" property="vouchernumber" title="Voucher Number" />  
		<display:column media="xml" property="vouchernumber" title="Voucher Number" />  	
		<display:column media="csv" property="vouchernumber" title="Voucher Number" />  
		<display:column media="html" title="Voucher Number" sortable="true" style="width:90px" headerClass="sortable">
		
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
			<display:column property="voucherName" title="Voucher Name" class="textAlign3" />
			<display:column property="code" title="Account Code" style="width:45px"/>
			<display:column property="accName" title="Account Name" style="width:250px" />				 
			<display:column property="narration" title="Narration" style="width:200px" />			
			<display:column property="debitamount" title="Debit&nbsp(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp(Rs.)" class="textAlign3" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="JournalBook.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="JournalBook.xls"/>
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
    LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 LOGGER.info("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate"));
		try{
		 JbReport jbReport = new JbReport();			   
		 request.setAttribute("links",jbReport.getJbReport(jbReportBean));
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
	 		
		<center><u><b><div class = "normaltext"> Journal Book <br><bean:message key="JourBook.Name"/><br><%= request.getParameter("voucher_name_in") %><%= request.getParameter("fundName") %><%= request.getParameter("fundSource_name_in") %><%= request.getParameter("dept_name_for") %> from <%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>  </div></b></u></center>   		
		<div class = "alignleft"> <%= jbReportBean.getIsConfirmedCount() %> in <%= jbReportBean.getTotalCount() %> are unconfirmed   </div>		
		<div class="tbl5-container" id="tbl-container" > 
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" class="its" > 
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Journal Book
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("startDate") %> to <%= request.getParameter("endDate") %>
		</display:caption>
		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="Voucher Date" style="width:70px" sortable="true"  headerClass="sortable"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher Number" /> 	
		<display:column media="excel" property="vouchernumber" title="Voucher Number" />  
		<display:column media="xml" property="vouchernumber" title="Voucher Number" />  	
		<display:column media="csv" property="vouchernumber" title="Voucher Number" />  
		<display:column media="html" title="Voucher Number" sortable="true" style="width:90px" headerClass="sortable">

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
			<display:column property="voucherName" title="Voucher Name" class="textAlign3" />
			<display:column property="code" title="Account Code" style="width:45px"/>
			<display:column property="accName" title="Account Name" style="width:250px" />				 
			<display:column property="narration" title="Narration" style="width:100px" />			
			<display:column property="debitamount" title="Debit&nbsp(Rs.)" class="textAlign" />
			<display:column property="creditamount" title="Credit&nbsp(Rs.)" class="textAlign3" />
			
			<display:setProperty name="paging.banner.placement" value="false" />
			<display:setProperty name="export.pdf" value="true" />
			<display:setProperty name="export.pdf.filename" value="JournalBook.pdf" /> 
		    <display:setProperty name="export.excel" value="true" />
		    <display:setProperty name="export.excel.filename" value="JournalBook.xls"/>
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

	
</form>
</body>
</html>
