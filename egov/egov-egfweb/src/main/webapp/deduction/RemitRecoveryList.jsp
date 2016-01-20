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
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="java.util.*,java.math.BigDecimal,
org.egov.deduction.model.*,
java.text.*,
org.egov.infstr.utils.EgovMasterDataCaching,
org.egov.deduction.client.RemitRecoveryForm,
org.egov.deduction.dao.*"%>

<html>
<% NumberFormat nf=new DecimalFormat("##############.00"); 
%>
<head>
	<title>View Remit Recovery</title>
	
	
<script>
var id,mode;
function ButtonPress(arg)
{
	if(arg == "getRemittanceList")
	{
		if(document.RemitRecoveryForm.recovery.value == "0")
		{
			bootbox.alert("Select Recovery Name");		
			var temp="document.RemitRecoveryForm.recovery.focus();";
			setTimeout(temp,0);	
			return;
		}
		document.RemitRecoveryForm.action = "../deduction/remitRecovery.do?submitType=getRemittanceList&mode="+mode;			
		document.RemitRecoveryForm.submit();
	}
	if(arg == "showDetails")
	{		
		if(mode=="modify")
		{			
			window.open("../deduction/remitRecovery.do?submitType=beforeViewAndModifyRemitRecovery&mode="+mode+"&id="+id,"","height=650,width=980,scrollbars=yes,left=30,top=30,status=yes");				
		}
		if(mode=="view")
		{
			window.open("../deduction/remitRecovery.do?submitType=beforeViewAndModifyRemitRecovery&mode="+mode+"&id="+id,"","height=650,width=980,scrollbars=yes,left=30,top=30,status=yes");									
		}
	}
}
function setDefault()
{
	<% 
		ArrayList fundList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-fund");
		ArrayList recoveryList=(ArrayList)EgovMasterDataCaching.getInstance().get("egi-recovery");	
		ArrayList remittanceList=(ArrayList)request.getAttribute("remittanceList");
		if(remittanceList!=null && remittanceList.size()>0)
		{
	%>
		hideColumn();
	<%
		}
	%>
	
	mode="<%=(request.getParameter("mode"))%>";		
	if(mode=="view")
	{
		document.title="View Remit Recovery";
		//document.getElementById('screenName').innerHTML="View Remit Recovery";
	}
	if(mode=="modify")
	{
		document.title="Modify Remit Recovery";
	//	document.getElementById('screenName').innerHTML="Modify Remit Recovery";
	}	
	
	var target="<%=(request.getAttribute("alertMessage"))%>";
	if(target!="null")
	{
		bootbox.alert("<%=request.getAttribute("alertMessage")%>");
	}	
}
function hideColumn()
{
	var table=document.getElementById('remmitanceList');
   	for(var i=0;i<table.rows.length;i++)   	
   	{
   		table.rows[i].cells[0].style.display="none";
   	}
}
function getDetails(obj)
{
	var rowobj=getRow(obj);
	var table=document.getElementById('remmitanceList');
	id=getControlInBranch(table.rows[rowobj.rowIndex],"id").innerHTML;
	ButtonPress("showDetails");
}	
</script>
</head>
<body onload="setDefault()">
<html:form  action="/deduction/remitRecovery">
<!--<html:hidden property="mode" value="${mode}"/> -->
<table align='center' class="tableStyle" id="table3"> 
 
 <tr>
 <td class="labelcell" colspan="4">&nbsp;</td> 
</tr>
	<tr>
 		<td class="labelcell" align="right" height="35" >Fund&nbsp;&nbsp;</td> 
		<td class="smallfieldcell" align="left" >
			<html:select  property="fund" styleClass="combowidth1">
			<html:option value='0'>--Choose--</html:option>	
			<c:forEach var="fund" items="<%=fundList%>" > 
				<html:option value="${fund.id}">${fund.name}</html:option>
			</c:forEach> 
			</html:select>
		</td>			
		<td class="labelcell" align="right" height="35" >Recovery Name<SPAN class="leadon">*</SPAN>&nbsp;</td> 
		<td class="smallfieldcell" align="left" >
			<html:select  property="recovery" styleClass="combowidth1" >
			<html:option value='0'>--Choose--</html:option>	
				<c:forEach var="rec" items="<%=recoveryList%>" > 
					<html:option value="${rec.id}">${rec.type}</html:option>
				</c:forEach> 
			</html:select>
		</td>			
	 </tr> 
<tr>
	 		<td class="labelcell" align="right" height="35" >Month&nbsp;&nbsp;</td>
	 		<td class="smallfieldcell">
	 			<html:select  property="month" styleClass="combowidth1">
					<html:option value='0'>--Choose--</html:option>					
					<html:option value='1'>January</html:option>
					<html:option value='2'>February</html:option>	
					<html:option value='3'>March</html:option>
					<html:option value='4'>April</html:option>	
					<html:option value='5'>May</html:option>
					<html:option value='6'>June</html:option>
					<html:option value='7'>July</html:option>
					<html:option value='8'>August</html:option>
					<html:option value='9'>September</html:option>	
					<html:option value='10'>October</html:option>
					<html:option value='11'>November</html:option>
					<html:option value='12'>December</html:option>				
				</html:select>
			</td>	 		
	 		<td class="labelcell" align="right" height="35" >Year&nbsp;&nbsp;</td>
	 		<td class="smallfieldcell">
	 			<html:select  property="year" styleClass="combowidth1" >
				<html:option value='0'>--Choose--</html:option>	
					<c:forEach var="fy" items="${finYearList}" > 
						<html:option value="${fy.id}">${fy.finYearRange}</html:option>
					</c:forEach> 
				</html:select>
			</td>
		</tr>
<!--
<tr>	
	<td class="labelcell" align="right">Voucher No&nbsp;&nbsp;</td> 
	<td   class="fieldcell"><html:text property="vhNo" /></td>
	</td>
	
</tr>
<tr>
	<td class="labelcell" align="right" width="35%">From Date&nbsp;&nbsp;</td> 
		<td   class="smallfieldcell" width="35%" align="middle"><html:text property="fromDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
		<a href="javascript:show_calendar('RemitRecoveryForm.fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
	</td> 
	<td class="labelcell" align="right" width="35%">To Date&nbsp;&nbsp;</td> 
		<td   class="smallfieldcell" width="35%" align="middle"><html:text property="toDate" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
		<a href="javascript:show_calendar('RemitRecoveryForm.toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="/egi/resources/erp2/images/calendar.gif" width=24 height=22 border=0></a>
	</td> 
</tr>-->
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr  id="row1" name="row1">
<td  colspan=4 align="center">
<html:button styleClass="button" value="Submit" property="b2" onclick="ButtonPress('getRemittanceList')" />
<html:button styleClass="button" value="Close" property="b3" onclick="window.close();" /></td>
</td>
</tr>
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<tr><td class="labelcell5" colspan=4>&nbsp;</td></tr> 
<%
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	if(remittanceList!=null && remittanceList.size()>0)
	{
%>
	

<tr>
<td colspan=4 align="center">
<table  cellpadding="0" cellspacing="0" align="center" id="remmitanceList" name="remmitanceList" >
<tr>
<td class="thStlyle" width="15%"><div align="center">Id</div></td>
<td class="thStlyle" width="15%"><div align="center">Month</div></td>
<td class="thStlyle" width="15%"><div align="center">Payment Voucher Number</div></td>
<td class="thStlyle" width="15%"><div align="center">Voucher Date</div></td>
<td class="thStlyle" width="15%"><div align="center">Fund</div></td>
<td class="thStlyle" width="15%"><div align="center">Recovery Name</div></td>
<td class="thStlyle" width="15%"><div align="center">Remitted Amount</div></td>
</tr>
	<%	
	EgRemittanceDetailHibernateDAO egRmtDtlDAO=DeductionDAOFactory.getDAOFactory().getEgRemittanceDetailDAO();
	for (Iterator it = remittanceList.iterator(); it.hasNext(); ) 
	{		
		EgRemittance rmt=(EgRemittance)it.next();	
		ArrayList rmtDtlList=(ArrayList)egRmtDtlDAO.getEgRemittanceDetailByEgRmt(rmt);
		BigDecimal remittedAmt=new BigDecimal(0);
		for (Iterator it1 = rmtDtlList.iterator(); it1.hasNext(); ) 
		{
			EgRemittanceDetail rmtDtl=(EgRemittanceDetail)it1.next();	
			remittedAmt=remittedAmt.add(rmtDtl.getRemittedamt());			
		}
		
		String[] m_names = {"","January", "February", "March","April", "May", "June", "July", "August", "September","October", "November", "December"};
		String month=m_names[Integer.parseInt(rmt.getMonth().toString())];		
	%>
<tr>
	<td class="tdStlyle" width="15%"><div align="left" name="id" id="id"><%= rmt.getId() %></div> </td>
	<td class="tdStlyle" width="15%"><A style="text-decoration: none;" onClick="getDetails(this)" href="#"><div align="left" name="month" id="month"><%= month%></div></A> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="voucherNo" id="voucherNo"><%= rmt.getVoucherheader().getVoucherNumber()%></div></td>
	<td class="tdStlyle" width="12%"><div align="left" name="voucherDate" id="voucherDate"><%=sdf.format(rmt.getVoucherheader().getVoucherDate()) %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="fund" id="fund"><%= rmt.getFund().getName() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="recovery" id="recovery"><%= rmt.getTds().getType() %></div> </td>
	<td class="tdStlyle" width="15%"><div align="left" name="remittedAmt" id="remittedAmt" style="text-align:right" ><%= nf.format(remittedAmt) %></div> </td>
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
</html:form>

</body>
</html>
