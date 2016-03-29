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
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@page  import="com.exilant.eGov.src.reports.LongAmountWrapper,com.exilant.eGov.src.reports.*,java.io.*,java.util.*,java.sql.*,javax.sql.*,javax.naming.InitialContext,com.exilant.GLEngine.*,org.egov.infstr.utils.EGovConfig"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="KEYWORDS" content="">
<meta http-equiv="DESCRIPTION" content="">
<META http-equiv=pragma content=no-cache>
<!--<link rel=stylesheet href="../exility/global.css" type="text/css">-->
<link rel=stylesheet href="../css/egov.css" type="text/css" media="screen" />
<link rel=stylesheet href="../css/print.css" type="text/css" media="print" />
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/ExilityParameters.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/CookieManager.js"></SCRIPT>
<SCRIPT LANGUAGE="javascript" SRC="../exility/PageValidator.js"></SCRIPT>
<script language="javascript" src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<SCRIPT type="text/javascript" src="../resources/javascript/calendar.js" type="text/javascript" ></SCRIPT>
<SCRIPT LANGUAGE="javascript">

var csNameTemp="";
var csCodeTemp="";
var name="",fdate1="",tdate1="";
var fb;
var status="";
var sIndex,sValue,sText;
function onLoadofPage()
{
	PageValidator.addCalendars();
	PageManager.ListService.callListService();
	PageManager.DataService.callDataService('getRelationType');
	var fromBean = <%=request.getParameter("fromBean")%>;
	if(fromBean==1)
	{
		var type=<%=request.getParameter("conSupType")%>;
		if(type==1)
			document.getElementById('conSupType').value=1;
		else
			document.getElementById('conSupType').value=2;

		var name='<%=request.getParameter("conSupName")%>';
		var code='<%=request.getParameter("code")%>';
		//if(code!=null)	document.getElementById('code').value=code;
		//if(name!=null) document.getElementById('conSupName').value=name;
		
		var t1 = '<%=request.getParameter("typeColHead")%>';
		var t2 = '<%=request.getParameter("nameColHead")%>';
		var t3 = '<%=request.getParameter("rptName")%>';
		
		if(t1!=null)
			document.getElementById('typeColHead').value =t1;
		if(t2!=null)
			document.getElementById('nameColHead').value =t2;
		if(t3!=null)
			document.getElementById('rptName').value =t3;
			
		var fromDate = '<%=request.getParameter("fromDate")%>';
		var toDate = '<%=request.getParameter("toDate")%>';
		
		var obj = document.getElementById('conSupName');
		obj.setAttribute('exilListSource','conSupList');
		PageManager.DataService.setQueryField('SupConTypeCode',type);
		PageManager.DataService.setQueryField('fromDate',fromDate);
		PageManager.DataService.setQueryField('toDate',toDate);
		PageManager.DataService.callDataService('getConSupNameReport');
		sIndex="",sValue="",sText="";
	}
}
function getConSupCodes()
{
	var fdate = document.getElementById('fromDate').value;
	var tdate = document.getElementById('toDate').value;
	if(fdate.length==0)
	{
		bootbox.alert("please select from date ");
		document.getElementById('conSupType').value="";
		return;
	}
	if(tdate.length==0)
	{
		bootbox.alert("please select to date ");
		document.getElementById('conSupType').value="";
		return;
	}
	if(compareDate(formatDate6(fdate),formatDate6(tdate)) == -1 )
	{
		bootbox.alert('From Date can not be greater than To Date');
		document.getElementById('conSupType').value="";
		document.getElementById('fdate').focus();
		return false;
	}
			
	var ctrl = document.getElementById('conSupType');
	
	if(ctrl.value==1)
	{	document.getElementById('typeColHead').value='Supplier Name';
		document.getElementById('nameColHead').value='PO No.';
		document.getElementById('rptName').value='Supplier-Purchase Order Detail Report';
	}
	else
	{
		document.getElementById('typeColHead').value='Contractor Name';
		document.getElementById('nameColHead').value='WO No.';
		document.getElementById('rptName').value='Contractor-Work order Detail Report';
	}
	
	
	sIndex=null;
	//var ctrl = document.getElementById('conSupType');
	document.getElementById('code').value='';
	var obj = document.getElementById('conSupName');
	obj.setAttribute('exilListSource','conSupList');
	PageManager.DataService.setQueryField('SupConTypeCode',ctrl.value);
	PageManager.DataService.setQueryField('fromDate',document.getElementById('fromDate').value);
	PageManager.DataService.setQueryField('toDate',document.getElementById('toDate').value);
	PageManager.DataService.callDataService('getConSupNameReport');
	
	
	
}
function getCode()
{
	//bootbox.alert('getcode sIndex'+sIndex+',sText='+sText+',sValue='+sValue);
	if(!(document.getElementById("conSupName").value !=''))
	{
		document.getElementById("code").value="";
		return;
	}
	var obj = document.getElementById('conSupName');
	var obj1=obj.options[obj.selectedIndex].innerHTML;
	obj1 = obj1.split('-');
	obj.options[obj.selectedIndex].innerHTML=obj1[0];
	document.getElementById('code').value=obj1[1];
	if(sIndex!=null)
	{
		document.forms[0].conSupName.options[sIndex]= new Option(sText,sValue);
	}
	//bootbox.alert('getcode');
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=obj1[0]+"-"+obj1[1];
}
function afterRefreshPage(dc)
{ 
	var afterSub="<%=(request.getParameter("fromBean"))%>";
	if(afterSub != "null")
	{
		if(dc.values['serviceID'] == 'getRelationType')
		{
			//getConSupCodes()
		}
		PageManager.DataService.removeQueryField('conSupType');
	}
	if(afterSub != "null")
	{
		if(dc.values['serviceID'] == 'getConSupNameReport')
		{
			PageManager.DataService.removeQueryField('SupConTypeCode');
			PageManager.DataService.removeQueryField('fromDate');
			PageManager.DataService.removeQueryField('toDate');
			PageManager.DataService.removeQueryField('conSupType');
			PageManager.DataService.removeQueryField('conSupName');
			
			PageManager.DataService.removeQueryField('typeColHead');
			PageManager.DataService.removeQueryField('nameColHead');
			PageManager.DataService.removeQueryField('rptName');
			
			var accnoObj=document.getElementById('conSupName');
			accntMod(accnoObj);
		}
	}
}

function accntMod(obj)
{
	//bootbox.alert('accntMod=sIndex'+sIndex+',sText='+sText+',sValue='+sValue)
	if(obj.selectedIndex==-1)	 return;
	if(sIndex!=null)
	{
		obj.options[sIndex]= new Option(sText,sValue);
	}

	var text=obj.options[obj.selectedIndex].innerHTML;
    var temp=text.split("-");
	sIndex=obj.selectedIndex;
	sValue=obj.options[obj.selectedIndex].value;
	sText=text;
	if(!temp[1])
	{
		temp[1]=" ";
		temp[0]="";
	}
	document.getElementById("code").value=temp[1];
	try{
	   if(temp[1]!=null && temp[1]!="" )
	   {
	    obj.options[obj.selectedIndex].text=temp[0];
	   // bootbox.alert('acc');
	   }
	}catch(err){}
}
function getData1()
{ 	
	if(!PageValidator.validateForm())
	{
		return;
	}
	fdate1 = document.getElementById('fromDate').value;
	tdate1 = document.getElementById('toDate').value;
	if(compareDate(formatDate6(fdate1),formatDate6(tdate1)) == -1 )
	{
		bootbox.alert('From Date can not be greater than To Date');
		document.getElementById('fromDate').focus();
		return;
	}
	
	obj=document.getElementById('conSupType');
	csTypeTemp=obj.options[obj.selectedIndex].value;
	csCodeTemp=document.getElementById('code').value;
	csNameTemp=document.getElementById('conSupName').value;
	document.getElementById('fromBean').value = 1;
	document.conSupRpt.submit();
}
function buttonFlush1()
{
	window.location="rptContractorSupplier.jsp";
}

</SCRIPT>
<title>Contractor/Supplier Report</title>
</head>
<body bgcolor="#ffffff" bottommargin="0" topmargin="0" rightmargin="0" leftmargin="0" marginheight="0" marginwidth="0" onLoad="onLoadofPage()" >
<jsp:useBean id = "csReportBean" scope ="request" class="com.exilant.eGov.src.reports.ConSupReportBean"/>
<jsp:setProperty name = "csReportBean" property="*"/>
<jsp:setProperty name = "csReportBean" property="fundId"/>

<center>
<br>
<table align='center' id="table2">
<tr><td>
<div id="main"><div id="m2"><div id="m3">	
<form name="conSupRpt"  action = "rptContractorSupplier.jsp?code="+csCodeTemp+"&conSupType="+csTypeTemp+"&conSupName="+csNameTemp+"&fromDate="+fdate1+"&toDate="+tdate1 method = "post">

<table align='center' class="tableStyle" id="table3"> 
	<tr>
		<td  class="tableheader" valign="center"  colspan="6"><span >Contractor/Supplier Report</span></td>
	</tr>
	<tr class="row1">
		<td class="labelcell"><div align="right" valign="center" >PO From Date<span class="leadon">*&nbsp;&nbsp; </span></div></td>
		<td class="smallfieldcell">
			<input name="fromDate" id="fromDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
		<td class="labelcell"><div align="right" valign="center" >To Date<span class="leadon">*&nbsp;&nbsp; </span></div></td>
		<td class="smallfieldcell">
			<input name="toDate" id="toDate" class="datefieldinput" onkeyup="DateFormat(this,this.value,event,false,'3')"  exilCalendar="true" exilMustEnter="true">
		</td>
	</tr>
		<tr class="row1">
			<td class="labelcell"><div align="right">Fund&nbsp;&nbsp;</div></td>
			<td class="smallfieldcell"><div><SELECT class=fieldcell id="fundId" name="fundId"  exilListSource="fundNameList"></SELECT></div></td>
			<td class="labelcell"></td>
			<td class="labelcell"></td>
		</tr>
			<tr class="row1">
		<td class="labelcell"><div align="right">Type <span class="leadon">*&nbsp;&nbsp; </span> </div></td>
		<td align="left" valign="center" class="labelcell"  >
			<div align="left" class="smallfieldcell" >
				<SELECT  id="conSupType"  name="conSupType" onChange="getConSupCodes()" class="smallfieldinput" exilListSource="getRelationType" exilMustEnter="true" />
			</div>
		</td>
		<td align="right" valign="center" class="labelcell" width="5%">Name</td>
		<td class="smallfieldcell">
			<span><select  name="conSupName" id="conSupName" class="combowidth1" onChange="getCode()"></select></span>
		</td>
		<td align="right" valign="center" class="labelcell" width="5%" style="display:none">Name</td>
		<td  class="smallfieldcell ">
			<input readonly type="text" class="fieldinputlarge" style="width: 210" name="code" id="code"   />
		</td>
	</tr>
	

	<tr class="row2" height="5"><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr class="row2">
		<td colspan="6" align="middle">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
			<input type=button class=button onclick=getData1() href="#" value="Submit"></td>
			
			<td align="right">
			<input type=button class=button onclick=buttonFlush1(); href="#" value="Cancel"></td>
			
			<td align="right">
			<input type=button class=button onclick=window.close() href="#" value="Close"></td>
			
		</tr>
		</table>
		</td>
	</tr>
</table>


<input type="hidden" name="fromBean" id="fromBean" value="0">
<input type="hidden" name="rptName" id="rptName" value="Contractor-Work order Detail Report">
<input type="hidden" name="nameColHead" id="nameColHead" value="work Order">
<input type="hidden" name="typeColHead" id="typeColHead" value="Contractor Name">

</div></div></div>
</td></tr>
</table>
</center>


<%
    System.out.println("before submit "+request.getParameter("fromBean")+" Contractor/Supplier Type "+request.getParameter("conSupType")+" code1 "+request.getParameter("code")+" Name  "+request.getParameter("conSupName"));
       if(request.getParameter("fromBean") !=null && request.getParameter("fromBean").equals("1"))
       {
   	 System.out.println("after submit "+request.getParameter("fromBean")+" Contractor/Supplier Type "+request.getParameter("conSupType")+" Code "+request.getParameter("code")+" Name  "+request.getParameter("conSupName"));
   	 ConSupReportList conSupList = new ConSupReportList();
   	 try{
   	   	request.setAttribute("links",conSupList.getConSupReportList(csReportBean));
	    } catch(Exception e) { System.out.println("Error:"+"connecting to databse failed");}

%>
<!--<div class="tbl-bill" id="tbl-container" >-->
		 <div class = "normaltext"><b><%= request.getParameter("rptName") %>  </b></div>
<div class="tbl2-container" id="tbl-container" >
 <display:table  name="links" id="currentRowObject" cellspacing="0" export="true" sort="list" class="its" >

	 <display:caption media="pdf">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<%= request.getParameter("rptName") %> 
		</display:caption> 

	 <display:column media="html" class="widthAlign" title='<%=request.getParameter("typeColHead") %>'  >

	 	 <% String code2 =((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getRelCode();
	 	String relId =((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getRelId();
	 	      		String link1="";
	 	      		String url=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getName();
	 	      		String link="javascript:"+"void(window.open('../HTML/VMC/Relation_VMC.jsp?relationcodeToSearch="+relId+"&showMode=view"+"'"+",'','height=650,width=900,scrollbars=yes,left=20,top=20,status=no'"+"))";%>
	 	      		
	 	      		<a href="<%=link%>"><FONT face="times new roman"><%=url%></font></a>

	 	</display:column>
	 	<display:column media="pdf" property="name" title='<%=request.getParameter("typeColHead")%>' />
	 	<display:column media="excel" property="name" title='<%=request.getParameter("typeColHead")%>'/>
	 	<display:column media="xml" property="name" title='<%=request.getParameter("typeColHead")%>' />
	<display:column media="csv" property="name" title='<%=request.getParameter("typeColHead")%>' />

	 <display:column media="html"  title='<%=request.getParameter("nameColHead")%>' class="widthAlignSmall">
	     		<% String code2 =((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getWorksDetailId();
	 	      		String url=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getWorkOrderNo();
	 	      		String link="javascript:"+"void(window.open('../HTML/VMC/WorksDetailEnq_VMC.jsp?worksDetail_code="+code2+"&showMode=view"+"'"+",'','height=650,width=900,scrollbars=yes,left=20,top=20,status=no'"+"))";%>
	 	      		<a href="<%=link%>"><%=url%></a>
	     	</display:column>

	 	<display:column media="pdf" property="workOrderNo" title='<%=request.getParameter("typeColHead")%>' />
	 	 <display:column media="excel" property="workOrderNo" title='<%=request.getParameter("typeColHead")%>'/>
	 	 <display:column media="xml" property="workOrderNo" title='<%=request.getParameter("typeColHead")%>' />
	<display:column media="csv" property="workOrderNo" title='<%=request.getParameter("typeColHead")%>' />

	 <display:column property="workname" class="widthAlign" title="Work Name" />
	 	 <display:column  property="orderDate" class="widthAlignSmall" title="Order Date" />
	 <display:column property="orderValue" class="textAlign" title="Order Value"  />

	 <display:column property="maxAdv" class="textAlign" title="Max Advance"  />
	 	 <display:column property="advPaid" class="textAlign"  title="Advance Paid" />
	 <display:column property="advAdj" class="textAlign" title="Advance Adjusted"  />

	 <display:column media="html" class="textAlign" title="Amount Incurred"  >

	 	  	  <%
	 	  		String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	 	  	  String wId =((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getWorksDetailId();
	 	  	  	  	      		String link1="";
	 	  	  	  	      		String type=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getRelationTypeId();
	 	  	  	  	      		String url=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getBillAmount();
	 	  	  	  	      		String link="javascript:"+"void(window.open('../Reports/BillDetails.htm?type="+type+"&woksDetailId="+wId+"&deployment="+deployment+"'"+",'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no'"+"))";
	 	  	  	  	      		%>

	 	  	  			<a href="<%=link%>"><%=url%></a>
	 	  	  </display:column>
	 	  	 <display:column media="pdf" property="billAmount"class="textAlign"  title='Amount Incurred' />
	 	  	 <display:column media="excel" property="billAmount" class="textAlign" title='Amount Incurred'/>
	 	  	 <display:column media="xml" property="billAmount"class="textAlign"  title='Amount Incurred' />
	 <display:column media="csv" property="billAmount"class="textAlign"  title='Amount Incurred' />

	 <display:column  media="html" class="textAlign3" title="Amount Paid"   >
	 	  	<%
	 	  	String deployment=EGovConfig.getProperty("egf_config.xml", "DEPLOYMENT","", "general");
	 	  	String wId =((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getWorksDetailId();
	 	  	      		String link1="";
	 	  	      		String type=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getRelationTypeId();
	 	  	      		String url=((ConSupReportBean)pageContext.findAttribute("currentRowObject")).getAmtPaid();
	 	  	      		String link="javascript:"+"void(window.open('../Reports/PaymentDetails.htm?type="+type+"&woksDetailId="+wId+"&deployment="+deployment+"'"+",'','height=650,width=900,scrollbars=yes,left=30,top=30,status=no'"+"))";
	 	  	      		%>

	 			<a href="<%=link%>"><%=url%></a>
	 	  </display:column>
		  <display:column property="dedAmount" title='Amount Deducted'  class="textAlign3"/>
	 	  <display:column media="pdf" property="amtPaid" title='Amount Paid'  />
	 	  <display:column media="excel" property="amtPaid" title='Amount Paid'/>
	 	   <display:column media="xml" property="amtPaid" title='Amount Paid' />
	  <display:column media="csv" property="amtPaid" title='Amount Paid' />
	 	<display:setProperty name="export.pdf" value="true" />
		<display:setProperty name="export.pdf.filename" value="rptContractorSupplier.pdf" /> 
	    <display:setProperty name="export.excel" value="true" />
	    <display:setProperty name="export.excel.filename" value="rptContractorSupplier.xls"/>
	    <display:setProperty name="export.csv" value="false" />
		<display:setProperty name="export.xml" value="false" />	

</display:table>
</div>
<%
	}
%>
</form>

</body>
</html>
