<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>
<head>
<title>Voucher Search</title>
</head>
<body>
	<s:form action="approvePreapprovedVouchers" theme="simple">
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Approve pre-approval Voucher Search" />
		</jsp:include>
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="formheading"></div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="greybox"><s:text name="voucher.number" /></td>
					<td class="greybox"><s:textfield name="voucherNumber"
							id="voucherNumber" maxlength="25" value="%{voucherNumber}" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="bluebox"><s:text name="voucher.type" /></td>
					<td class="bluebox"><s:select name="type" id="type"
							list="dropdownData.typeList" headerKey="-1"
							headerValue="----Choose----"
							onchange="loadVoucherNames(this.value)" /></td>
					<td class="bluebox"><s:text name="voucher.name" /></td>
					<td class="bluebox"><s:select name="name" id="name"
							list="%{nameList}" headerKey="-1" headerValue="----Choose----" /></td>
				</tr>
				<tr>
					<td class="greybox"><s:text name="voucher.fromdate" /></td>
					<td class="greybox"><s:textfield name="fromDate" id="fromDate"
							maxlength="20" value="%{fromDate}" /><a
						href="javascript:show_calendar('forms[0].fromDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a><br />(dd/mm/yyyy)</td>
					<td class="greybox"><s:text name="voucher.todate" /></td>
					<td class="greybox"><s:textfield name="toDate" id="toDate"
							maxlength="20" value="%{toDate}" /><a
						href="javascript:show_calendar('forms[0].toDate');"
						style="text-decoration: none">&nbsp;<img
							src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)</td>
				</tr>
				<jsp:include page="voucher-filter.jsp" />
				<tr>
					<td align="right"></td>
					<td><s:submit method="search" value="Search"
							cssClass="buttonsubmit" /></td>
					<td><input type="submit" value="Close"
						onclick="javascript:window.close()" class="button" /></td>
				</tr>
				<s:hidden name="mode" value="%{mode}" id="mode" />
			</table>
			<br />
			<div id="listid" style="display: none">
				<table width="80%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom">
					<tr>
						<th class="bluebgheadtd">Sl.No.</th>
						<th class="bluebgheadtd">Voucher Number</th>
						<th class="bluebgheadtd">Type</th>
						<th class="bluebgheadtd">Voucher Date</th>
						<th class="bluebgheadtd">Fund Name</th>
						<th class="bluebgheadtd">Amount</th>
						<th class="bluebgheadtd">Approve</th>
					</tr>
					<c:set var="trclass" value="greybox" />

					<s:iterator var="p" value="voucherList" status="s">
						<tr>
							<td class="<c:out value="${trclass}"/>"><s:property
									value="#s.index+1" /></td>
							<td align="left" class="<c:out value="${trclass}"/>"><a
								href="#"
								onclick="javascript:window.open('preApprovedVoucher!loadvoucherview.action?vhid=<s:property value='%{id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700')"><s:property
										value="%{vouchernumber}" /> </a></td>
							<td align="left" class="<c:out value="${trclass}"/>"><s:property
									value="%{type}" /></td>
							<td class="<c:out value="${trclass}"/>"><s:date
									name="%{voucherdate}" format="dd/MM/yyyy" /></td>
							<td align="left" class="<c:out value="${trclass}"/>"><s:property
									value="%{fundname}" /></td>
							<td style="text-align: right" class="<c:out value="${trclass}"/>">
								<s:property value="%{amount}" />
							</td>
							<td class="<c:out value="${trclass}"/>"><s:checkbox
									name="approveList" fieldValue="%{id}" /></td>
							<c:choose>
								<c:when test="${trclass=='greybox'}">
									<c:set var="trclass" value="bluebox" />
								</c:when>
								<c:when test="${trclass=='bluebox'}">
									<c:set var="trclass" value="greybox" />
								</c:when>
							</c:choose>
						</tr>
					</s:iterator>
					<s:hidden name="targetvalue" value="%{target}" id="targetvalue" />
				</table>
				<table align="center">
					<tr align="center">
						<td><s:submit method="approve" value="Approve"
								cssClass="buttonsubmit" /></td>
						<td><input type="button" value="Select All"
							onclick="javascript:selectAll()" class="button" /></td>
						<td><input type="button" value="Deselect All"
							onclick="javascript:deselectAll()" class="button" /></td>
						</td>
					</tr>
				</table>
			</div>
			<br />
			<div id="msgdiv" style="display: none">
				<table align="center" class="tablebottom" width="80%">
					<tr>
						<td class="bluebgheadtd" colspan="7">No Records Found</td>
					</tr>
				</table>
			</div>
			<br /> <br />
	</s:form>
	<script>
		
		function loadVoucherNames(selected)
		{
			var s="";  
			if(selected==-1)
				{
				document.getElementById('name').options.length=0;
				document.getElementById('name').options[0]= new Option('--------Choose--------','0');
				}
		<s:iterator value="voucherTypes" var="obj">
		  s='<s:property value="#obj"/>';
		 if(selected==s)
		 {
		document.getElementById('name').options.length=0;
		document.getElementById('name').options[0]= new Option('--------Choose--------','0');
		
		 <s:iterator value="voucherNames[#obj]" status="stat" var="names">
		 document.getElementById('name').options[<s:property value="#stat.index+1"/>]= new Option('<s:property value="#names"/>','<s:property value="#names"/>');
		 </s:iterator>   
		 }
		 </s:iterator>
			  
			
		}
		
	   function selectAll()
	   {
	    var list=document.getElementsByName("approveList");
	    for (var i=0;i<list.length;i++)
	    {
	     list[i].checked="checked";
	    }
	    
	   }
	   	
	 function deselectAll()
	   {
	    var list=document.getElementsByName("approveList");
	    for (var i=0;i<list.length;i++)
	    {
	     list[i].checked="";
	    }
	    
	   }
	   	
			
		
		<s:if test="%{voucherList.size==0}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{voucherList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>	
	
		</script>
</body>
</html>
