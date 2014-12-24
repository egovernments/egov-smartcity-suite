<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,java.math.BigDecimal,
org.egov.payroll.model.Advance,
org.egov.infstr.utils.EgovMasterDataCaching,
java.text.*,org.egov.commons.Fund,
org.egov.commons.service.CommonsService,
org.egov.payroll.utils.PayrollManagersUtill"%>

<html>
<% NumberFormat nf=new DecimalFormat("##############.00"); 
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>View Advance Disbursement</title>			
	<SCRIPT type="text/javascript" src="<%=request.getContextPath() +"/script/calendar.js"%>" type="text/javascript" ></SCRIPT>	 
	
<script>
var vhId,mode;
function ButtonPress(arg)
{	
	if(arg == "getDisbursementList")
	{
		if(document.AdvanceDisbursementForm.disbMethod.value == "0")
		{
			alert('<bean:message key="alertDisbursementMethod"/>');
			var temp="document.AdvanceDisbursementForm.disbMethod.focus();";
			setTimeout(temp,0);		
			return;
		}
		if(document.AdvanceDisbursementForm.advanceType.value == "0")
		{
			alert('<bean:message key="alertAdvanceType"/>');
			var temp="document.AdvanceDisbursementForm.advanceType.focus();";
			setTimeout(temp,0);		
			return;
		}
		
		document.AdvanceDisbursementForm.action = "${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=getDisbursementList&mode="+mode;			
		document.AdvanceDisbursementForm.submit();
	}
	if(arg == "showDetails")
	{		
		if(mode=="modify")
		{			
			window.open("${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=beforeViewAndModifyAdvDisb&mode="+mode+"&vhId="+vhId,"","height=650,width=1000,scrollbars=yes,left=30,top=30,status=yes");				
		}
		if(mode=="view")
		{
			window.open("${pageContext.request.contextPath}/salaryadvance/advanceDisbursement.do?submitType=beforeViewAndModifyAdvDisb&mode="+mode+"&vhId="+vhId,"","height=650,width=1000,scrollbars=yes,left=30,top=30,status=yes");									
		}
	}
}
function setDefault()
{
	<%		
		ArrayList fundList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund");	
		ArrayList disbursementList=(ArrayList)request.getAttribute("disbursementList");
		if(disbursementList!=null && disbursementList.size()>0)
		{
	%>
		hideColumn();
	<%
		}
	%>
	
	mode="<%=(request.getParameter("mode"))%>";		
	if(mode=="view")
	{
		document.title="View Advance Disbursement By Cash/Direct Transfer";
		document.getElementById('screenName').innerHTML="View Advance Disbursement By Cash/Direct Transfer";
	}
	if(mode=="modify")
	{
		document.title="Modify Advance Disbursement By Cash/Direct Transfer";
		document.getElementById('screenName').innerHTML="Modify Advance Disbursement By Cash/Direct Transfer";
	}	
	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");
	}	
}

function hideColumn()
{
	var table=document.getElementById('disbusementList');
   	for(var i=0;i<table.rows.length;i++)   	
   	{
   		table.rows[i].cells[0].style.display="none";
   	}
}

function getDetails(obj)
{
	var rowobj=getRow(obj);
	var table=document.getElementById('disbusementList');
	vhId=getControlInBranch(table.rows[rowobj.rowIndex],"vhId").innerHTML;	
	ButtonPress("showDetails");
}	

</script>
</head>

<body onload="setDefault()">
<html:form  action="/salaryadvance/advanceDisbursement">

<center>
<br>

<table align='center' class="tableStyle" id="table3"> 
 <tr>
 <td colspan=4 class="tableheader" align="center"><span id="screenName">View Advance Disbursement By Cash/Direct Transfer</span></td>
 </tr>
 <tr>
 <td class="labelcell" colspan="4">&nbsp;</td> 
</tr>
		<tr>			
	 		<td class="labelcell" align="right" height="35" ><bean:message key="DisbursementMethod"/> &nbsp;<SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="disbMethod" styleClass="combowidth1">
				<html:option value='0'>--Choose--</html:option>					
					<html:option value="cash">Cash</html:option>
					<html:option value="dbt">Direct Bank Transfer</html:option>
				</html:select>
			</td>
			<td class="labelcell" align="right" height="35" ><bean:message key="AdvanceType"/>  <SPAN class="leadon">*</SPAN>&nbsp;</td> 
			<td class="smallfieldcell" align="left" >
				<html:select  property="advanceType" styleClass="combowidth1">
				<html:option value='0'>--Choose--</html:option>
				<c:forEach var="sc" items="${salCodesList}" > 
					<html:option value="${sc.id}">${sc.head}</html:option>
				</c:forEach> 
				</html:select>
			</td>					
	 	</tr> 
	 	
		<tr>	 		
			<td class="labelcell" align="right" height="35" ><bean:message key="Fund"/> Fund</td>
			<td class="smallfieldcell" align="left" >
				<html:select  property="fund" styleClass="combowidth1" >
				<html:option value='0'>--Choose--</html:option>
				<c:forEach var="fu" items="<%=fundList%>" > 
					<html:option value="${fu.id}">${fu.name}</html:option>
				</c:forEach> 
				</html:select>
			</td>						
	 		<td></td>	
	 		<td></td> 				
	 	</tr> 	 	

<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr  id="row1" name="row1">
<td  colspan=4 align="center">
<html:button styleClass="button" value="Submit" property="b2" onclick="ButtonPress('getDisbursementList')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" /></td>
</td>
</tr>
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<%
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	if(disbursementList!=null && disbursementList.size()>0)
	{
%>

<tr>
<td colspan=4 align="center">
<table  cellpadding="0" cellspacing="0" align="center" id="disbusementList" name="disbusementList" >
<tr>
<td class="thStlyle"><div align="center">Vh Id</div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="EmployeeName"/>  </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="AdvanceType"/>  </div></td>
<td class="thStlyle" width="17%"><div align="center"><bean:message key="Fund"/> </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="SanctionedAmount"/>  </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="VoucherNo"/>  </div></td>
<td class="thStlyle" width="11%"><div align="center"><bean:message key="VoucherDate"/>  </div></td>
</tr>
	<%	
	CommonsService cm = PayrollManagersUtill.getCommonsService();	
	for (Iterator it = disbursementList.iterator(); it.hasNext(); ) 
	{		
		Advance sa=(Advance)it.next();	
		Fund fund=cm.getFundById(sa.getVoucherheader().getFundId());	
	%>
<tr>	
	<td class="tdStlyle"><div align="left" name="vhId" id="vhId"><%= sa.getVoucherheader().getId() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="emplName" id="emplName"><%= sa.getEmployee().getEmployeeName() %></div> </td>
		<td class="tdStlyle" width="15%"><A style="text-decoration: none;" onClick="getDetails(this)" href="#"><div align="left" name="advType" id="advType"><%= sa.getSalaryCodes().getHead() %></div></A> </td>
	<td class="tdStlyle" width="17%"><div align="left" name="fund" id="fund"><%= fund.getName() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="sancAmt" id="sancAmt" style="text-align:right" ><%= nf.format(sa.getAdvanceAmt()) %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="voucherNo" id="voucherNo"><%= sa.getVoucherheader().getVoucherNumber()%></div></td>
	<td class="tdStlyle" width="11%"><div align="left" name="voucherDate" id="voucherDate"><%=sdf.format(sa.getVoucherheader().getVoucherDate()) %></div> </td>
</tr>
	<%
	}
	%>      
</table>
</td>
</tr>
<%
	}
%>
</table>

</center>
</html:form>
</body>
</html>
