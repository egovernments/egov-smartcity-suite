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


<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page buffer = "16kb" %>
<%@page import="org.apache.log4j.Logger"%>
<html>
<head>
	<title>Deposit Register Report</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<meta http-equiv="KEYWORDS" content="">
	<meta http-equiv="DESCRIPTION" content="">

	<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print"/>

	<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>

<SCRIPT LANGUAGE="javascript">
	var  rptStartDate, rptEndDate;
	var rptfundId='';
	
	function getBills(billId,segment,dt1,dt2,type,vhrNo,fieldId)
	{
		var showmode='';
		if(mode=='approveCBillPmt')
			showmode='approve';
		else if(mode=='modifyCBillPmt')
			showmode='modify';
		else
			showmode='view';
		
		cWind=window.open("CBillPayment.jsp?showMode="+showmode+"&billId="+billId+"&disbursement_type="+type+"&field_id="+fieldId+"&fromDate="+dt1+"&toDate="+dt2+"&segment_id="+segment+"&payVchrNo="+vhrNo,"","height=650,width=900,scrollbars=yes,left=20,top=20,status=yes");	
	}
	
	function getVoucher(cgn) 
	{    
		var showmode='';
		//window.open("../../HTML/NDMC/DirectBankPaymentNDMC.htm?cgNumber="+cgn+"&showMode=viewBank","","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
		window.open("../../HTML/NDMC/ViewCBillPayment.jsp?cgNumber="+cgn,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
	}
	
	function onBodyLoad()
	{
		PageValidator.addCalendars();
		if(PageManager.DataService.getQueryField('startDate') != null || PageManager.DataService.getQueryField('endDate') != null)
		{
			document.getElementById('startDate').value=PageManager.DataService.getQueryField('startDate');
			document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');
		}
		PageManager.ListService.callListService();
		var beanVal= <%=request.getParameter("fromBean")%>;
		if(beanVal==2)
		{
			document.getElementById("row3").style.display="block";
			document.getElementById("row2").style.display="none";		
		}
	}
	function ButtonPress()
	{
		if (!PageValidator.validateForm())
		{
			//document.getElementById('startDate').value='';
			//document.getElementById('endDate').value='';
			//document.getElementById('fundLst').value='';
			//document.getElementById('fieldLst').value='';
			//document.getElementById('funcLst').value='';
			return;
		}
		var fundObj=document.getElementById('fundLst');
		document.getElementById('fundName').value=fundObj.options[fundObj.selectedIndex].text;
		
		var strtDate = document.getElementById('startDate').value;
		rptStartDate = strtDate;
		var endDate = document.getElementById('endDate').value;
		if (endDate=="")
		{
			rptEndDate=strtDate;
			endDate=strtDate;
			document.getElementById('endDate').value=strtDate;
		}
		else
		{
			rptEndDate = endDate;
		}
		var rptfundId= document.getElementById('fundLst').value;
		if(rptfundId=='')
			document.getElementById('fundId').value="0";
		else
			document.getElementById('fundId').value=rptfundId;
	
		var rptfieldId= document.getElementById('fieldLst').value;
		if(rptfieldId=='')
			document.getElementById('fieldLst').value="0";
		else
			document.getElementById('fieldLst').value=rptfieldId;
	
		/*var rptfuncId= document.getElementById('funcLst').value;
		if(rptfuncId=='')
			document.getElementById('funcLst').value="0";
		else
			document.getElementById('funcLst').value=rptfuncId;
			*/
	
		if(compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
		{
			bootbox.alert('Start Date can not be greater than End Date');
			document.getElementById('startDate').focus();
			return false;
		}
		document.getElementById('fromBean').value = 1;
		document.forms[0].submit();	
	}
	function buttonFlush()
	{
		window.location="DepositeRegisterReport.jsp";
	}
	function pageSetup()
	{
		document.body.leftMargin=0.75;
		document.body.rightMargin=0.75;
		document.body.topMargin=0.75;
		document.body.bottomMargin=0.75;
	}
	
	function buttonPrintPreview()
	{
		document.getElementById('fromBean').value ="2";	 
		var rptfieldId= document.getElementById('fieldLst').value;
		
		if(rptfieldId=='')
			document.getElementById('fieldLst').value="0";
		else
			document.getElementById('fieldLst').value=rptfieldId;
		document.forms[0].submit();	
	}
	
	function buttonPrint()
	{
		document.getElementById("tbl-header1").style.display = "none";     
		if(window.print)
	 	{
	  		window.print();
	  	}
	}
</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()"><!------------------ Header Begins Begins--------------------->
 

<form name="Deposite Register Report" action = "DepositeRegisterReport.jsp?startDate="+rptStartDate+"&endDate="+rptEndDate+"&fundLst="+rptfundId+" &fieldLst="+rptfieldId method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="fundId" id="fundId" value="0">
<input type="hidden" id="fundName" name="fundName" value="">
<input type="hidden" name="funcId" id="funcId" value="0">
<input type="hidden" id="funcName" name="funcName" value="">

<br>
<table align='center' id="table2">
	<tr>
		<td>
			<div id="main">
				<div id="m2">
					<div id="m3" style="width:810px">
						<div class="tbl-header1" id="tbl-header1"> 		<!--       Div for hiding purpose    -->
							<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
								<tr>
									<td class="rowheader" valign="center" width="100%" colspan="6"><span id="screenName" class="headerwhite2">Deposit Register Report-GEN 18</span><span id="partyName" class="headerwhite2"></span></td>
								</tr>
								<tr class="row1" >
									<td class="labelcell"><div align="right" valign="center">Start Date<font size="2" color="red">*</font></div></td>
									<td align="left" class="smallfieldcell"><input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true"></td>
									<td class="labelcell"><div align="right" valign="center" >End Date</div></td>
									<td class="smallfieldcell"><input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true"></td>
									<tr class="row1" >
									<td class="labelcell"><div align="right" valign="center">Fund&nbsp;<font size="2" color="red"></font></div></td>
									<td class="smallfieldcell"><SELECT CLASS="bigcombowidth" NAME="fundLst" ID="fundLst" exilListSource="fundNameList" > </SELECT></td>
									<td class="labelcell"><div align="right" valign="center">Field</div></td>
									<td class="smallfieldcell"><SELECT CLASS="bigcombowidth" NAME="fieldLst" ID="fieldLst" exilListSource="field_name"></td>
								</tr>	
								<tr class="row1" style="display:none">
									<td class="labelcell"><div align="right" valign="center" >Functionary&nbsp;<font size="2" color="red"></font></div></td>
									<td class="smallfieldcell"><SELECT CLASS="bigcombowidth" NAME="funcLst" ID="funcLst" exilListSource="functionNameLst"> </SELECT></td>
									<td><div ></div></td>
									<td></td>
								</tr>	
								<tr class="row2">
									<td colspan=6></td>
								</tr>
								<tr class="row2" id="row2" name="row2">
									<td colspan="6" align="middle"><!-- Buttons Start Here -->
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td align="right"><input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
												<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
												<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
												<td align="right"><input type=button class=button onclick="buttonPrintPreview()" href="#" value="Print Preview"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr style="DISPLAY: none" class="row3" id="row3" name="row3">
									<td colspan="6" align="middle"><!-- Buttons Start Here -->
										<table border="0" cellpadding="0" cellspacing="0">
											<tr>
												<td align="right"><input type=button class=button onclick=ButtonPress() href="#" value="Search"></td>
												<td align="right"><input type=button class=button onclick=buttonFlush(); href="#" value="Cancel"></td>
												<td align="right"><input type=button class=button onclick=window.close() href="#" value="Close"></td>
												<td align="right"><input type=button class=button onclick="pageSetup();buttonPrint()" href="#" value="Print"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
	 					</div>		
	 				</div>
				</div>
			</div>
		</td>
	</tr>
</table>			
</form>
	<%
	Logger LOGGER = Logger.getLogger("DepositeRegisterReport.jsp");
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	 	try
		{
			DepositeRegisterReport tbReport = new  DepositeRegisterReport();
			request.setAttribute("tbreport",tbReport.getDepositRegisterReport(request.getParameter("startDate"),request.getParameter("endDate"),request.getParameter("fundId"),request.getParameter("fieldLst"),""));
		}
		catch(Exception e)
	    {
			LOGGER.info("Exception in Jsp Page:"+e.getMessage());
			%>
			 <script>
			 bootbox.alert("Error :<%=e.getMessage()%>");
		 		//PageManager.DataService.setQueryField('endDate','');
		 </script>
		 <%
		}
	%>
	<u><b><div class = "normaltext" align="right" ></div></b></u>
	<center><u><b><div class = "normaltext"> DEPOSIT REGISTER REPORT FROM <%= request.getParameter("startDate") %>   TO <%= request.getParameter("endDate") %> </div></b></u></center>
	<div class="tbl5-container" id="tbl-container" >

	<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="tbreport"  export="true" sort="list" id="currentRowObject" class="its" >
		<display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		DEPOSIT REGISTER REPORT FROM <%= request.getParameter("startDate") %> TO <%= request.getParameter("endDate") %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</display:caption>
		<div STYLE="display:table-header-group">
			<tr class="rowheader">
 				<display:column title="Date #" property="voucherDate"  class="textAlign" style="width:12%;" />
  				<display:column title="Payee Name" property="payeeName" class="textAlign" style="width:8%;" />
				<display:column title="Nature/ Type/ Kind of Deposit" property="mode"  class="textAlign" style="width:2%;"/>
				<display:column title="Receipt Number" property="reciptnumber"  class="textAlign"  style="width:17%;"/>
				<display:column title="Amount (Rs.)" property="amount" class="textAlign" style="width:4%;"/></td>
				<display:column property="voucherNumber" title="<br><hr>Voucher Number & Date" class="textAlign" style="width:10%;"/>
				<display:column property="income" title="Refund Or<br><hr> Income*" class="textAlign" style="width:3%;"/>
				<display:column property="year" title="Adjustments<br><hr>Year"  class="textAlign" style="width:1%;"  />
				<display:column property="chequeAmount" title="<br><hr>Amount"  class="textAlign" style="width:2%;"  />
 				<display:column property="bdeposite"title="Balance Deposit" style="display:nowrap;width:100%;border-right: solid 1px #000000" class="textAlign" />
				<display:setProperty name="export.pdf" value="true" />
				<display:setProperty name="export.pdf.filename" value="DepositeRegisterReport.pdf" /> 
				<display:setProperty name="export.csv" value="false" />
			    <display:setProperty name="export.xml" value="false" />
			    <display:setProperty name="export.excel" value="true" />
			    <display:setProperty name="export.excel.filename" value="DepositeRegisterReport.xls"/>
			</tr>
		</div>	    
	</display:table>
	</div>
	<%
	}
    else if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	 	try
	 	{
			DepositeRegisterReport tbReport = new  DepositeRegisterReport();
			request.setAttribute("tbreport",tbReport.getDepositRegisterReport(request.getParameter("startDate"),request.getParameter("endDate"),request.getParameter("fundId"),request.getParameter("fieldLst"),""+" fieldname  "+request.getParameter("fieldLst")+" funcname  "+""));
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in Jsp Page:"+e.getMessage());
			%>
			 <script>
			 bootbox.alert("Error :<%=e.getMessage()%>");
		 		//PageManager.DataService.setQueryField('endDate','');
		 	</script>
			<%
		}
	%>
	<u><b><div class = "normaltext" align="right" ></div></b></u>
	<center><u><b><div class = "normaltext"> DEPOSIT REGISTER REPORT FROM <%= request.getParameter("startDate") %>  TO <%= request.getParameter("endDate") %> </div></b></u></center>
	<div class="tbl5-container" id="tbl-container" >
	<display:table cellspacing="0" decorator="org.displaytag.decorator.TotalTableDecorator" name="tbreport"  export="false" sort="list" id="currentRowObject" class="its" >
		<div STYLE="display:table-header-group">
			<display:column title="Date #" property="voucherDate"  class="textAlign" style="width:12%;" />
 			<display:column title="Payee Name" property="payeeName" class="textAlign" style="width:8%;" />
			<display:column title="Nature/ Type/ Kind of Deposit" property="mode"  class="textAlign" style="width:2%;"/>
			<display:column title="Receipt Number" property="reciptnumber"  class="textAlign"  style="width:17%;"/>
			<display:column title="Amount (Rs.)" property="amount" class="textAlign" style="width:4%;"/></td>
			<display:column property="voucherNumber" title="<br><hr>Voucher Number<br>& Date" class="textAlign" style="width:10%;"/>
			<display:column property="income" title="Refund Or<br><hr> Income*" class="textAlign" style="width:3%;"/>
			<display:column property="year" title="Adjustments<br><hr>Year"  class="textAlign" style="width:1%;"  />
			<display:column property="chequeAmount" title="<br><hr>Amount"  class="textAlign" style="width:2%;"  />
			<display:column property="bdeposite"title="Balance Deposit" style="display:nowrap;width:48%;border-right: solid 1px #000000" class="textAlign" />
		</div>	    
	</display:table>
	</div>
	<%
	}
	%>
</form>
</body>
</head>
</html>
