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
<!-- <%@ page buffer = "16kb" %> -->
<%@page  import="com.exilant.GLEngine.GeneralLedgerBean,org.egov.infstr.utils.EGovConfig"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<!-- <META http-equiv=pragma content=no-cache> -->
<!-- Inclusion of the CSS files that contains the styles -->

<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen, print" />

<!--
<link rel=stylesheet href="../exility/global.css" type="text/css">
-->
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js?rnd=${app_release_no}"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js?rnd=${app_release_no}"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js?rnd=${app_release_no}" type="text/javascript" ></SCRIPT>

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

function onLoad()
{
	PageValidator.addCalendars();
	var purposeId="4";	
	PageManager.DataService.setQueryField('cashCode',purposeId);
	PageManager.ListService.callListService();
	
	
}

function getAccCode()
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

function ButtonPress()
{
	
	if (!PageValidator.validateForm())
	{
		document.getElementById('startDate').value='';
		document.getElementById('endDate').value='';
		return;
	}
	document.getElementById('fromBean').value = 1;
	document.getElementById('glCode2').value=document.getElementById('glCode1').value;
	var fuObj=document.getElementById('fund_id');
	fuId=fuObj.options[fuObj.selectedIndex].value;
	var fuSrObj=document.getElementById('fundSource_id');
	fuSrId=fuSrObj.options[fuSrObj.selectedIndex].value;
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
	gl1=document.getElementById("glCode1").value;
	gl2=document.getElementById("glCode2").value;
	forRE=document.getElementById("forRevEntry").value;
	document.forms[0].submit();
	
	
}
function buttonFlush()
{
	
	window.location="CashBook.jsp";

}

/*function openSearch(obj)
{
	var a = new Array(2);
	var screenName="4";
	var str= "../HTML/Search.html?cashCode="+screenName;
	var sRtn = showModalDialog(str,"","dialogLeft=300;dialogTop=210;dialogWidth=305pt;dialogHeight=300pt;status=no;");
	a = sRtn.split("`~`");
	var type = document.getElementById('glCode1');
	type.value = a[0];
}*/

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
	// hide2 = document.getElementById("currentRowObject");
	document.forms[0].submit();
  	if(window.print)
  		{
  		window.print();
	  	} 
}

</SCRIPT>


</head> 
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoad()">
 <jsp:useBean id = "glReportBean" scope ="request" class="com.exilant.eGov.src.reports.GeneralLedgerReportBean"/>
<jsp:setProperty name = "glReportBean" property="*"/>  
<form name="CashBook" action = "CashBook.jsp?fund_id="+fuId+"&fundSource_id="+fuSrId+"&glCode1="+gl1+"&glCode2="+gl2+"&startDate="+sDate+"&endDate="+eDate method = "post">
<input type="hidden" name="fromBean" id="fromBean" value="0"> 
<input type="hidden" name="glCode2" id="glCode2" value=""> 
<div class="tbl-header1" id="tbl-header1">

<table width="100%" border=0 cellpadding="3" cellspacing="0">
<tr >
		<td class="rowheader" valign="center" colspan="6" width="100%"><span id="screenName" class="headerwhite2">Cash Book</span></td>
	</tr>
	<tr class="row1">
		<td><div align="right" valign="center" class="normaltext" >Account Code<span class="leadon">*</span></div></td>
		<td>
		<!-- 	<input class="fieldinput" name="glCode1" id="glCode1" exilMustEnter="true" ><IMG id=IMG1 onclick="openSearch('chartofaccounts');" height=22 src="/egi/resources/erp2/images/plus1.gif" width=25 align=top border=0> -->
			<SELECT class="combowidth1" id="glCode1" name="glCode1" exilListSource="mappedCodes2" exilMustEnter="true" onChange="getAccCode()"></SELECT>
		</td>
		<td><div align="right" valign="center" class="normaltext">Fund</div></td>
		<td align="left">
			<SELECT class="fieldinput" id="fund_id" name="fund_id" exilListSource="fundNameList"></SELECT>
		</td>
	</tr>
	<tr class="row2">
	<td><div align="right" valign="center" class="normaltext">Financing Source</div></td>
		<td align="left">
			<SELECT class="fieldinput" id="fundSource_id" name="fundSource_id" exilListSource="fundSourceNameList"></SELECT>
		</td>
		<td><div align="right" valign="center" class="normaltext">Show Reversed Entry</div></td>
		<td >
			<input type="checkbox" name="forRevEntry" id="forRevEntry" ></td>
		</td>
	</tr>
	<tr class="row1">
		<td><div align="right" valign="center" class="normaltext">Starting Date<span class="leadon">*</span></div></td>
		<td align="left">
			<input name="startDate" id="startDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
		<td><div align="right" valign="center" class="normaltext" >End Date<span class="leadon">*</span></div></td>
		<td>
			<input name="endDate" id="endDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
	</tr>
	<tr class="row2" height="5"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2">
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_leftside.gif" width=7></td>
			<td bgcolor="#fe0000" valign="center" nowrap><A class=buttonprimary onclick="ButtonPress()" href="#">Search</A></td>
			<td><IMG height=18 src="/egi/resources/erp2/images/Button_rightside.gif" width=7></td>
			<td><IMG src="/egi/resources/erp2/images/spacer.gif" width=8></td>	
			
			<td align="right"><IMG height=18 src="/egi/resources/erp2/images/Button_second_leftside.gif" width=6></td>
			<td bgcolor="#ffffff" valign="center" nowrap background="/egi/resources/erp2/images/Button_second_middle.gif"><A class=buttonsecondary onclick=buttonFlush(); href="#">Cancel</A></td>
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
   // System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
    {
	// System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
		try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		// System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
	 <div class="tbl2-container" id="tbl-container" > 
		
		<center><u><b><div class = "normaltext"> Cash Book <br> <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>   
		<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
		<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="true" sort="list" pagesize="15" class="its" > 
		
		<div STYLE="display:table-header-group">
		<display:column property="voucherdate" title="VoucherDate" sortable="true"  headerClass="sortable"/>
		<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
		<display:column media="excel" property="vouchernumber" title="Voucher#" />  
		<display:column media="xml" property="vouchernumber" title="Voucher#" />  	
		<display:column media="csv" property="vouchernumber" title="Voucher#" />  
		<display:column media="html" title="Voucher#" sortable="true"  headerClass="sortable">
		
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
			
			<display:column property="name" style="width:150px" title="Particulars" />
			<display:column property="amount" title="Amount" />				 
			<display:column property="narration" title="Narration"  />
			<display:column property="type" style="width:130px" title="Transaction" />
			<display:column property="debitamount" title="Debit" class="textAlign" />
			<display:column property="creditamount" title="Credit" class="textAlign3" />
			<display:setProperty name="export.pdf" value="true" />		

		</div>		
</display:table>
 </div> 
<%
	}
%>	

				<!--  Added by Sathish for Print purpose -->

<%
   // System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
    if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("2"))
    {
	// System.out.println("after submit "+request.getParameter("fromBean")+" startdate "+request.getParameter("startDate")+" enddate "+request.getParameter("endDate")+" glCode1  "+request.getParameter("glCode1")+" glCode2  "+request.getParameter("glCode2"));
		try{
		 GeneralLedgerReportList generalLedgerList = new GeneralLedgerReportList();			   
		 request.setAttribute("links",generalLedgerList.getGeneralLedgerList(glReportBean));
		 }
		 catch(Exception e){
		// System.out.println("Exception in Jsp Page "+ e.getMessage());
		 %> 
		 <script>
		 bootbox.alert("Error :<%=e.getMessage()%>"); 
		 PageManager.DataService.setQueryField('endDate',null);
		 </script>
		 <%
		 }
%>	 
  <div class="tbl5-container" id="tbl-container" >   
	
	<center><u><b><div class = "normaltext"> Cash Book <br> <%= glReportBean.getStartDate() %> to <%= request.getParameter("endDate") %> </div></b></u></center>   
	<div class = "alignleft"> <%= glReportBean.getIsConfirmedCount() %> in <%= glReportBean.getTotalCount() %> are unconfirmed   </div>
		
	<display:table cellspacing="0"  decorator="org.displaytag.decorator.TotalTableDecorator" name="links" id="currentRowObject"  export="false" sort="list" class="its" > 
			
	<div STYLE="display:table-header-group">
	
	<display:column property="voucherdate" title="VoucherDate" sortable="true"  headerClass="sortable" />
	<display:column media="pdf" property="vouchernumber" title="Voucher#" /> 	
	<display:column media="excel" property="vouchernumber" title="Voucher#" />  
	<display:column media="xml" property="vouchernumber" title="Voucher#" />  	
	<display:column media="csv" property="vouchernumber" title="Voucher#" />  
	<display:column media="html" title="Voucher#" sortable="true"  headerClass="sortable"> 
	
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
		
		<display:column property="name" style="width:150px" title="Particulars" />
		<display:column property="amount" title="Amount" />				 
		<display:column property="narration" title="Narration"  />
		<display:column property="type" style="width:130px" title="Transaction" />
		<display:column property="debitamount" title="Debit" class="textAlign" />
		<display:column property="creditamount" title="Credit" class="textAlign3" />
		<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="paging.banner.placement" value="false" />
	</div>		
	</display:table>
 	</div>  
<%
	}
%>

	
</form>
</body>
</html>
