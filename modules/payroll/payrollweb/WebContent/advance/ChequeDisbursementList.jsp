<%@ include file="/includes/taglibs.jsp" %>

<%@ page import="java.util.*,java.math.BigDecimal,
org.egov.payroll.model.Advance,
java.text.*"%>

<html>
<% NumberFormat nf=new DecimalFormat("##############.00"); 
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<title>View Advance Disbursement</title>
	
	<LINK rel="stylesheet" type="text/css" href="../css/egov.css">
	<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/script/calendar.js" type="text/javascript" ></SCRIPT>
	 
<script>
var emplId,vhId,mode;
function ButtonPress(arg)
{
	if(arg == "getChequeDisbursementList")
	{		
		document.AdvanceDisbursementByChequeForm.action = "${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=getChequeDisbursementList&mode="+mode;			
		document.AdvanceDisbursementByChequeForm.submit();
	}
	if(arg == "showDetails")
	{		
		if(mode=="modify")
		{			
			window.open("${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=beforeViewAndModifyAdvDisbByCheque&mode="+mode+"&vhId="+vhId+"&emplId="+emplId,"","height=650,width=1000,scrollbars=yes,left=30,top=30,status=yes");				
		}
		if(mode=="view")
		{
			window.open("${pageContext.request.contextPath}/salaryadvance/disbursementByCheque.do?submitType=beforeViewAndModifyAdvDisbByCheque&mode="+mode+"&vhId="+vhId+"&emplId="+emplId,"","height=650,width=1000,scrollbars=yes,left=30,top=30,status=yes");									
		}
	}
}
function setDefault()
{
	<%			
		ArrayList chequeDisbursementList=(ArrayList)request.getAttribute("chequeDisbursementList");
		if(chequeDisbursementList!=null && chequeDisbursementList.size()>0)
		{
	%>
		hideColumn();
	<%
		}
	%>
	
	mode="<%=(request.getParameter("mode"))%>";		
	if(mode=="view")
	{
		document.title="View Advance Disbursement By Cheque";
		document.getElementById('screenName').innerHTML="View Advance Disbursement By Cheque";
	}
	if(mode=="modify")
	{
		document.title="Modify Advance Disbursement By Cheque";
		document.getElementById('screenName').innerHTML="Modify Advance Disbursement By Cheque";
	}	
	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		alert("<%=request.getAttribute("alertMessage")%>");
	}	
}

function getEmplName(obj)
{
	var emplId=obj.value;
	if(emplId !='0')
	{
		<c:forEach var="as" items="${advDisbEmpList}">
			if(emplId=="${as.idPersonalInformation}")
			{
				document.AdvanceDisbursementByChequeForm.employeeName.value="${as.employeeName}";
			}		
		</c:forEach>
	}
	else
	{
		document.AdvanceDisbursementByChequeForm.employeeName.value="";
	}
}

function hideColumn()
{
	var table=document.getElementById('disbusementList');
   	for(var i=0;i<table.rows.length;i++)   	
   	{
   		table.rows[i].cells[0].style.display="none";
   		table.rows[i].cells[1].style.display="none";
   	}
}

function getDetails(obj)
{
	var rowobj=getRow(obj);
	var table=document.getElementById('disbusementList');
	vhId=getControlInBranch(table.rows[rowobj.rowIndex],"vhId").innerHTML;
	emplId=getControlInBranch(table.rows[rowobj.rowIndex],"emplId").innerHTML;
	ButtonPress("showDetails");
}	

</script>
</head>
<body onload="setDefault()">
<html:form  action="/salaryadvance/disbursementByCheque">

<center>
<br>

<table align='center' class="tableStyle" id="table3"> 
 <tr>
 <td colspan=4 class="tableheader" align="center"><span id="screenName">View Advance Disbursement By Cheque</span></td>
 </tr>
 <tr>
 <td class="labelcell" colspan="4">&nbsp;</td> 
</tr>
	<tr>
	 		<td class="labelcell" align="center" height="35" ><bean:message key="EmployeeCode"/> &nbsp;&nbsp;</td> 
			<td class="smallfieldcell" align="center" >
				<html:select  property="employee" styleClass="combowidth1" onchange="getEmplName(this)">
				<html:option value='0'>--Choose--</html:option>	
					<c:forEach var="as" items="${advDisbEmpList}" > 
						<html:option value="${as.idPersonalInformation}">${as.employeeCode}</html:option>
					</c:forEach> 
				</html:select>
			</td>	
			<td class="labelcell" align="center" height="35" ><bean:message key="EmployeeName"/>&nbsp;&nbsp;&nbsp;</td> 
			<td class="fieldcell" align="center" ><html:text  property="employeeName" readonly="true" tabindex="-1"/>	</td>				
	 	</tr> 	 	

<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr  id="row1" name="row1">
<td  colspan=4 align="center">
<html:button styleClass="button" value="Submit" property="b2" onclick="ButtonPress('getChequeDisbursementList')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" /></td>
</td>
</tr>
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<%
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	if(chequeDisbursementList!=null && chequeDisbursementList.size()>0)
	{
%>

<tr>
<td colspan=4 align="center">
<table  cellpadding="0" cellspacing="0" align="center" id="disbusementList" name="disbusementList" >
<tr>
<td class="thStlyle" width="15%"><div align="center">Empl Id</div></td>
<td class="thStlyle" width="15%"><div align="center">Vh Id</div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="EmployeeCode"/> </div></td>
<td class="thStlyle" width="15%"><div align="center"> <bean:message key="EmployeeName"/></div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="AdvanceType"/> </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="SanctionedAmount"/> </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="VoucherNo"/> </div></td>
<td class="thStlyle" width="15%"><div align="center"><bean:message key="VoucherDate"/> </div></td>
</tr>
	<%	
	for (Iterator it = chequeDisbursementList.iterator(); it.hasNext(); ) 
	{		
		Advance sa=(Advance)it.next();		
	%>
<tr>
	<td class="tdStlyle" width="15%"><div align="left" name="emplId" id="emplId"><%= sa.getEmployee().getIdPersonalInformation() %></div> </td>	
	<td class="tdStlyle" width="15%"><div align="left" name="vhId" id="vhId"><%= sa.getVoucherheader().getId() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="emplCode" id="emplCode"><%= sa.getEmployee().getEmployeeCode()%></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="emplName" id="emplName"><%= sa.getEmployee().getEmployeeName() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="advType" id="advType"><%= sa.getSalaryCodes().getHead() %></div> </td>
	<td class="tdStlyle" width="15%"><A style="text-decoration: none;" onClick="getDetails(this)" href="#"><div align="left" name="sancAmt" id="sancAmt" style="text-align:right" ><%= nf.format(sa.getAdvanceAmt()) %></div></A> </td>
		<td class="tdStlyle" width="15%"><div align="left" name="voucherNo" id="voucherNo"><%= sa.getVoucherheader().getVoucherNumber()%></div></td>
	<td class="tdStlyle" width="12%"><div align="left" name="voucherDate" id="voucherDate"><%=sdf.format(sa.getVoucherheader().getVoucherDate()) %></div> </td>
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
