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
<%@page import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- Inclusion of the CSS files that contains the styles -->
<link rel=stylesheet href="../exility/global.css" type="text/css">
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js"></script>
<link rel="stylesheet" href="../exility/screen.css" type="text/css" media="screen, print" /> 
<SCRIPT LANGUAGE="javascript">
var flag='0', tbNames, tbIds;
var rptType;
var rptTitle='', rptStartDate, rptEndDate, rptGLCode='', rptAccName='', detail='',bill_PoVar='',bill_CreditorVar='',snapShotDateTime,forRevEntry;
//onLoad

function onBodyLoad()
{
	PageValidator.addCalendars();
	document.getElementById('bill_Creditor').disabled=true;
	document.getElementById('bill_Po').value=PageManager.DataService.getQueryField('bill_Po');
	document.getElementById('bill_Creditor').value=PageManager.DataService.getQueryField('bill_Creditor');
	PageManager.ListService.callListService();
	if(PageManager.DataService.getQueryField('startDate') != null || PageManager.DataService.getQueryField('endDate') != null)
	{
		document.getElementById('startDate').value=PageManager.DataService.getQueryField('startDate');
		document.getElementById('endDate').value=PageManager.DataService.getQueryField('endDate');	
	}	
}
function ButtonPress(){

	if (!PageValidator.validateForm())
		return;
	//bootbox.alert('HI');
	var strtDate = document.getElementById('startDate').value;
	rptStartDate = strtDate;
	//bootbox.alert(rptStartDate);
	var endDate = document.getElementById('endDate').value;
	rptEndDate = endDate;
	
	/* if(strtDate.length==0||endDate.length==0)
	{
		bootbox.alert("please select start dates and end dates");
		return;
	}*/	
	/*if( compareDate(formatDateToDDMMYYYY(strtDate),formatDateToDDMMYYYY(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}
//bootbox.alert(document.getElementById('bill_Po').value);
//bill_PoVar=document.getElementById('bill_Po').value;
//bill_CreditorVar=document.getElementById('bill_Creditor').value;
//bootbox.alert(bill_PoVar);
//bootbox.alert(bill_CreditorVar);
document.getElementById('searchB').value = 1;
//PageManager.DataService.setQueryField('bill_Po',bill_PoVar);
//PageManager.DataService.setQueryField('bill_Creditor',bill_CreditorVar);		
//bootbox.alert(document.getElementById('searchB').value);
document.forms[0].submit();
	//var aCode1 = document.getElementById('glCode1').value;
	//var aCode2 = document.getElementById('glCode2').value;
				
}
function ButtonPressAll(){

	if (!PageValidator.validateForm())
		return;
	//bootbox.alert('HI');
	var strtDate = document.getElementById('startDate').value;
	rptStartDate = strtDate;
	//bootbox.alert(rptStartDate);
	var endDate = document.getElementById('endDate').value;
	rptEndDate = endDate;
	
	/* if(strtDate.length==0||endDate.length==0)
	{
		bootbox.alert("please select start dates and end dates");
		return;
	}*/	
	/*if( compareDate(formatDateToDDMMYYYY(strtDate),formatDateToDDMMYYYY(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}*/
	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 ){
		bootbox.alert('Start Date can not be greater than End Date');
		document.getElementById('startDate').focus();
		return false;
	}
//bootbox.alert(document.getElementById('searchB').value);
document.getElementById('searchB').value = 2;

//bootbox.alert(document.getElementById('searchB').value);
document.forms[0].submit();
	//var aCode1 = document.getElementById('glCode1').value;
	//var aCode2 = document.getElementById('glCode2').value;
				
}
function buttonFlush()
{
	
	//   ***1 only FO***,  ***2 FO***,  ***Admin, 3 All***    //
	if(!isValidUser(2, CookieManager.getCookie('userRole')) ) return false;
	
	window.location="BillRegisterReport.jsp";

}

</SCRIPT>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onload="onBodyLoad()"><!------------------ Header Begins Begins--------------------->
 <jsp:useBean id = "bRReportBean" scope ="page" class ="com.exilant.eGov.src.reports.BillRegisterReportBean"/>
<jsp:setProperty name = "bRReportBean" property="*"/>
<%! 	
	public double dAmount=0.0;
	public double cAmount=0.0;
	public LongAmountWrapper lAmount= new LongAmountWrapper();

%>

<%	
	try{
	BillRegisterList billRegisterList = new BillRegisterList();	
	request.setAttribute("links",billRegisterList.getBillRegisterList(bRReportBean));
	LinkedList linksHeader = new LinkedList();
	request.setAttribute("linksHeader",linksHeader);
	Hashtable map=new Hashtable();
		map.put("procurementOrder",request.getParameter("bill_Po"));
		map.put("totalPayAmount",billRegisterList.getTotalBillAmount());	
		map.put("totalBills",billRegisterList.getTotalBill());
		map.put("billPassed",billRegisterList.getTotalPassed());
		map.put("totalPending",billRegisterList.getTotalPending());
		map.put("billRejected",billRegisterList.getTotalRejected());
		linksHeader.add(map);
	/*cAmount = dayBookList.getCAmount();
	dAmount = dayBookList.getDAmount();*/
	}catch(Exception e){
	//System.out.println("Exception in Jsp Page:"+e);
	}
%>
<form name="billRegister" action = "BillRegisterReport.jsp?startDate="+rptStartDate+"&endDate="+rptEndDate method = "post">
<input type="hidden" name="searchB" id="searchB" value="0"> 
<input  type="hidden" name="mode" id="mode" > 
<!-- <input type="hidden" name="bill_Po1" id="bill_Po1" > 
<input type="hidden" name="bill_Creditor1" id="bill_Creditor1" >  -->
<table width="100%" border=0 cellpadding="3" cellspacing="0" id="mainTable">
	<!-- row-0 -->
		<tr>
			<td class="rowheader" valign="center" width="100%" colspan="4"><span id="screenName" class="headerwhite2">Bill Register</span><span id="partyName" class="headerwhite2"></span></td>
		</tr>
		<tr class="row1" >

		</tr>
		<tr class="row2">
			<td><div align="right" valign="center" class="normaltext">Procurement Order&nbsp;</div></td>
			<td >
				<SELECT  id="bill_Po" name="bill_Po" class="fieldinput"  exilListSource="billRegister"></SELECT>
			</td>
			<td><div align="right" valign="center" class="normaltext">Creditor&nbsp;</div></td>
			<td >
				<SELECT class="fieldinput" id="bill_Creditor" name="bill_Creditor" exilListSource="getCreditor" ></SELECT>
			</td>
		</tr>
		<tr class="row1">
			<td><div align="right" valign="center" class="normaltext">Bill Status&nbsp;</div></td>
			<td>
			<select class="fieldinput" name="bill_Status" id="bill_Status"   >
						<option value="" selected >&nbsp;</option>             
						<option value="Running Bill"  >Running Bill</option>           
						<option value="Final Bill">Final Bill</option>						
			</select></td>

			<td><div align="right" valign="center" class="normaltext">Bill Approval Status&nbsp;</div></td>
			
			<td >
				<select class="fieldinput" name="bill_AppStaus" id="bill_AppStaus" >
				<option value="" selected>&nbsp;</option>
					<option value="PENDING" >PENDING</option>	
					<option value="REJECTED" >REJECTED</option> 
					<option value="PASSED" >PASSED</option> 
				</SELECT>
			</td>
		</tr>
		<tr class="row2" >
			<td><div align="right" valign="center" class="normaltext">Starting Date&nbsp;</div></td>
			<td align="left">
				<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilPastDate" exilCalendar="true">
			</td>
			<td><div align="right" valign="center" class="normaltext" >End Date&nbsp;</div></td>
			<td>
				<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')" exilDataType="exilPastDate" exilCalendar="true">
			</td>

		</tr>
		<tr class="row1">
			<td colspan=4>
			</td>
		</tr>

                             <tr class="row1">
						<td height="25" colspan="4" valign="bottom" class="smalltext" width="898"><p class="smalltext">&nbsp;</p>
						</td>
						
						</tr>
						                   
		<tr class="row2">
			<td colspan="4" align="middle">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="right">
							<IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
						<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick="ButtonPress()" href="#">Search</A></td>
						<td>
						<IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>	
					<td align="right">
							<IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
						<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick="ButtonPressAll()" href="#">Show All</A></td>
						<td>
						<IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
					<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>	
					<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
					<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush(); href="#">Cancel</A></td>
						<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>						
						<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>								
						<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
						<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=window.close() href="#">Close</A></td>
						<td><IMG height=18 src="/egi/resources/erp2/images/Button_second_rightside.gif" width=6></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

</form>
<%
	String submitted="";
	submitted = (String) request.getParameter("searchB");
	//System.out.println("#######################:"+request.getParameter("searchB"));
	if(submitted != null && submitted.equals("1"))
    { 
		//System.out.println("#######################:"+request.getParameter("searchB"));
	
	%>
<% if(!request.getParameter("bill_Po").equals("")){%>
<display:table name="linksHeader" id="currentRowObjectHeader"  class="its"> 
		<display:column property="procurementOrder" title="Procurement Order" group="1"  />
		  <display:column property="totalBills" title="Total Number of Bills" group="2" class="textAlign" />
		  <display:column property="billPassed" title="Total Bills Passed" group="3" class="textAlign" />		  
		
		  <display:column property="totalPending" title="Total Bills Pending" group="4" class="textAlign" />
		  <display:column property="billRejected" title="Total  Bills Rejected" group="5"  class="textAlign"/>	
		  <display:column property="totalPayAmount" title="Total Payable Amount" group="6"  class="textAlign"/>
</display:table> 
<%}%>		
<display:table name="links" id="currentRowObject" export="true" sort="list" defaultsort="1" pagesize = "14" class="its" > 
		
		 <display:column  property="po" title="PO#"  group="1" sortable="true" headerClass="sortable"/>  
		 	
	 <display:column media="pdf" property ="billNumber" title="BillNumber" />
		 <display:column media="excel" property="billNumber" title="BillNumber" />  
		 <display:column media="xml" property="billNumber" title="BillNumber" />  	
		 <display:column media="csv" property="billNumber" title="BillNumber" />  	
		 <display:column media="html" title="Bill#" group="2"  > 
    		<% String cgn = ((BillRegister)pageContext.findAttribute("currentRowObject")).getBillNumber();
     			 String link = "javascript:"+"void(window.open('../HTML/BillRegisterAdd.htm?worksDetail_code="+cgn+ "&mode=view"+"'"+",'mywindow','width=760,height=500,scrollbars=no'"+"))";

			String url = ((BillRegister)pageContext.findAttribute("currentRowObject")).getBillNumber();%>	
			<a href="<%=link%>"><%=url%></a></display:column>
		 <display:column property="billDate" title="BillDate" group="3" />
		
		<display:column property="billAmount" title="BillAmount" group="4" class="textAlign"/>
			
			<display:column property="billApprovalStatus" title="BillStatus"  /> 
		 

		  <display:column property="voucherNumbaer" title="Voucher#" />
		  <display:column property="voucherDate" title="VoucherDate" />
		  <display:column property="passedAmount" title="PassedAmount"  />		  
		
		  <display:column property="paymentDate" title="PaymentDate" />
		  <display:column property="paidAmount" title="PaidAmount"  class="textAlign"/>	
		  <display:setProperty name="export.pdf" value="true" />
		  
	</display:table>
<%}%>
<%
	
	submitted = (String) request.getParameter("searchB");
	//System.out.println("#######################:"+request.getParameter("searchB"));
	if(submitted != null && submitted.equals("2"))
    { 
		//System.out.println("#######################:"+request.getParameter("searchB"));
	
	%>
<% if(!request.getParameter("bill_Po").equals("")){%>

<display:table name="linksHeader" id="currentRowObjectHeader"  class="its"> 
		<display:column property="procurementOrder" title="Procurement Order" group="1"  />
		  <display:column property="totalBills" title="Total Number of Bills" group="2" class="textAlign" />
		  <display:column property="billPassed" title="Total Bills Passed" group="3" class="textAlign" />		  
		
		  <display:column property="totalPending" title="Total Bills Pending" group="4" class="textAlign" />
		  <display:column property="billRejected" title="Total  Bills Rejected" group="5"  class="textAlign"/>	
		  <display:column property="totalPayAmount" title="Total Payable Amount" group="6"  class="textAlign"/>
</display:table> 
<%}%>		
<display:table name="links" id="currentRowObject" export="true" sort="list" defaultsort="1" class="its"> 
		
		 <display:column  property="po" title="PO#"  group="1" sortable="true" headerClass="sortable"/>  
		 	
		
		  <display:column media="pdf" property ="billNumber" title="BillNumber" />
		 <display:column media="excel" property="billNumber" title="BillNumber" />  
		 <display:column media="xml" property="billNumber" title="BillNumber" />  	
		 <display:column media="csv" property="billNumber" title="BillNumber" />  	
		  <display:column media="html" title="Bill#" group="2" > 
    		<% String cgn = ((BillRegister)pageContext.findAttribute("currentRowObject")).getBillNumber();
     			 String link = "javascript:"+"void(window.open('../HTML/BillRegisterAdd.htm?worksDetail_code="+cgn+ "&mode=view"+"'"+",'mywindow','width=760,height=500,scrollbars=no'"+"))";

			String url = ((BillRegister)pageContext.findAttribute("currentRowObject")).getBillNumber();%>	
			<a href="<%=link%>"><%=url%></a></display:column>
		 <display:column property="billDate" title="BillDate" group="3" />
		
		<display:column property="billAmount" title="BillAmount" group="4" class="textAlign"/>
			
			<display:column property="billApprovalStatus" title="BillStatus"  /> 
		 

		  <display:column property="voucherNumbaer" title="Voucher#" />
		  <display:column property="voucherDate" title="VoucherDate" />
		  <display:column property="passedAmount" title="PassedAmount"  />		  
		
		  <display:column property="paymentDate" title="PaymentDate" />
		  <display:column property="paidAmount" title="PaidAmount"  class="textAlign"/>	
		  <display:setProperty name="export.pdf" value="true" />
		  
	</display:table>
	<%}%>
</body>
</html>
