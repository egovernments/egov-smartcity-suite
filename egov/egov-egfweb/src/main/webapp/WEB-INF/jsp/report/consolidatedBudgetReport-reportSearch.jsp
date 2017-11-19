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
<title><s:text name="consolidate.budget.report" /></title>

</head>

<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="consolidate.budget.report" />
		</div>

		<s:form name="consolidatedBudgetReportForm"
			action="consolidatedBudgetReport" theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox" width="15%"></td>
					<td class="bluebox" width="15%"><s:text
							name="report.financialYear.report" /><span class="mandatory"></span>
					</td>
					<td class="bluebox"><s:select
							list="dropdownData.financialYearList" listKey="id"
							listValue="finYearRange" name="financialYear.id" headerKey="0"
							headerValue="--- Select ---" value="financialYear.id"
							id="financialYear"></s:select></td>
					<td class="bluebox" width="15%"></td>
					<td class="bluebox" width="15%"><s:text
							name="report.fund.type" /><span class="mandatory"></span></td>
					<td class="bluebox" width="15%"><s:select name="fundType"
							id="fundType"
							list="#{'Select':'---Choose---','REVENUE':'REVENUE','CAPITAL':'CAPITAL'}"
							value="%{fundType}" /></td>
					<td class="bluebox" width="15%"></td>
				</tr>
				<tr>
					<td class="bluebox" width="15%"></td>
					<td class="bluebox" width="15%"><s:text
							name="report.budget.type" /><span class="mandatory"></span></td>
					<td class="bluebox" width="15%"><s:select name="budgetType"
							id="budgetType"
							list="#{'Select':'---Choose---','RECEIPTS':'RECEIPTS','EXPENDITURE':'EXPENDITURE'}"
							value="%{budgetType}" /></td>
				</tr>

			</table>

			<br />
			<br />

			<div class="buttonbottom">
				<s:submit method="search" value="Search" cssClass="buttonsubmit"
					onclick="return validate();" />
				<!--<s:submit method="generatePdf" value="Save As Pdf" cssClass="buttonsubmit" id="generatePdf" />
			<s:submit method="generateXls" value="Save As Xls" cssClass="buttonsubmit" id="generateXls" />-->
				<input type="button" class="buttonsubmit" value="EXPORT PDF"
					id="exportpdf" name="exportpdf" onclick="return exportPDF();" /> <input
					type="button" class="buttonsubmit" value="EXPORT EXCEL"
					id="exportpdf" name="exportpdf" onclick="return exportExcel();" />
				<input type="submit" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
	</div>
	<s:if test="%{bpBeanMajList.size!=0}">
		<div align="center" class="extracontent">
			<h4>
				<s:property value="fundType" />
				<s:property value="budgetType" />
			</h4>
		</div>
		<div align="right" class="extracontent">
			<b>Amount in Thousands</b>
		</div>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">

			<tr>

				<th class="bluebgheadtd" style="width: 6%; text-align: center"
					align="center">Sl.No</th>
				<th class="bluebgheadtd" style="width: 42%; text-align: center"
					align="center">Account Head</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Actuals<br />
				<s:property value="prevFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Budget Estimate<br />
				<s:property value="currentFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Revised Estimate<br />
				<s:property value="currentFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Budget Estimate<br />
				<s:property value="nextFinYearRange" />
				</th>
			</tr>
			<c:set var="trclass" value="greybox" />
			<s:iterator var="bp" value="bpBeanMajList" status="f">
				<s:if test="%{rowType=='detail'}">
					<tr>
						<td class="blueborderfortd"><s:property value="#f.index+1" />&nbsp;</td>
						<td class="blueborderfortd"><s:property value="budgetGroup" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="previousYearActuals" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="currentYearBE" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="approvedRE" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="approvedBE" />&nbsp;</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td class="blueborderfortd"></td>
						<td class="blueborderfortd">
							<h4>
								<s:property value="budgetGroup" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="previousYearActuals" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="currentYearBE" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="approvedRE" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="approvedBE" />
								&nbsp;
							</h4>
						</td>
					</tr>
				</s:else>

			</s:iterator>

		</table>
	</s:if>

	<br></br>

	<s:if test="%{bpBeanDetList.size!=0}">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tablebottom">

			<tr>

				<th class="bluebgheadtd" style="width: 10%; text-align: center"
					align="center">D.P.Code</th>
				<th class="bluebgheadtd" style="width: 38%; text-align: center"
					align="center">Account Head</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Actuals<br />
				<s:property value="prevFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Budget Estimate<br />
				<s:property value="currentFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Revised Estimate<br />
				<s:property value="currentFinYearRange" />
				</th>
				<th class="bluebgheadtd" style="width: 13%; text-align: center"
					align="center">Budget Estimate<br />
				<s:property value="nextFinYearRange" />
				</th>
			</tr>
			<c:set var="trclass" value="greybox" />
			<s:iterator var="bp" value="bpBeanDetList" status="f">
				<s:if test="%{rowType=='detail'}">
					<tr>
						<td class="blueborderfortd"><s:property value="accountCode" />&nbsp;</td>
						<td class="blueborderfortd"><s:property value="budgetGroup" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="previousYearActuals" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="currentYearBE" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="approvedRE" />&nbsp;</td>
						<td class="blueborderfortd" style="text-align: right"><s:property
								value="approvedBE" />&nbsp;</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td class="blueborderfortd">
							<h4>
								<s:property value="accountCode" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd">
							<h4>
								<s:property value="budgetGroup" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="previousYearActuals" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="currentYearBE" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="approvedRE" />
								&nbsp;
							</h4>
						</td>
						<td class="blueborderfortd" style="text-align: right">
							<h4>
								<s:property value="approvedBE" />
								&nbsp;
							</h4>
						</td>
					</tr>
				</s:else>
			</s:iterator>

		</table>
	</s:if>
	<s:if test="%{bpBeanMajList.size==1}">
		<div id="msgdiv" style="display: block">
			<table align="center" class="tablebottom" width="80%">
				<tr>
					<th class="bluebgheadtd" colspan="7">No Records Found
					</td>
				</tr>
			</table>
		</div>
	</s:if>


	</s:form>
	<script>

	function validate(){
	 	 var finYr=document.getElementById("financialYear").value;
	 	 var fundType=document.getElementById("fundType").value;
	 	 var budgetType=document.getElementById("budgetType").value;
	 		if(finYr == 0 || fundType == 'Select' || budgetType == 'Select'){
		 		bootbox.alert("Select all fields");
		 		return false;
			}
	 		return true;
	}

	 function exportPDF() {
	 	 var finYr=document.getElementById("financialYear").value;
	 	 var fundType=document.getElementById("fundType").value;
	 	 var budgetType=document.getElementById("budgetType").value;
	 		if(finYr == 0 || fundType == 'Select' || budgetType == 'Select'){
		 		bootbox.alert("Select all fields");
		 		return false;
			}
		 var url="${pageContext.request.contextPath}/report/consolidatedBudgetReport!exportPDF.action?financialYear.id="+finYr+"&fundType="+fundType+"&budgetType="+budgetType;
		 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		 }
	 function exportExcel() {
	 	 var finYr=document.getElementById("financialYear").value;
	 	 var fundType=document.getElementById("fundType").value;
	 	 var budgetType=document.getElementById("budgetType").value;
	 		if(finYr == 0 || fundType == 'Select' || budgetType == 'Select'){
		 		bootbox.alert("Select all fields");
		 		return false;
			}
		 var url="${pageContext.request.contextPath}/report/consolidatedBudgetReport!exportExcel.action?financialYear.id="+finYr+"&fundType="+fundType+"&budgetType="+budgetType;
		 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
		 }

	 </script>
</body>
</html>
