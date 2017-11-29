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
<title><s:text name="Fixed Search" /></title>

</head>
<script type="text/javascript">
  function onLoadTask(obj){
 // document.getElementById('msgdiv').style.display ='none';
  
	
	}
</script>
<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="Search Fixed Deposit" />
		</div>
		<body onload="onLoadTask(this);">
			<br />
			<br />

			<s:form name="fixedDepositForm" action="fixedDeposit" theme="simple">
				<s:hidden name="mode" id="mode" value="%{mode}"></s:hidden>
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="greybox" width="15%">&nbsp;</td>
						<td class="greybox" width="10%">From Date:</td>
						<td class="greybox"><s:textfield name="fromDate"
								id="fromDate" cssStyle="width:100px"
								value='%{getFormattedDate(this.value)}'
								onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
							href="javascript:show_calendar('concurrenceReport.fromDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
						</td>

						<td class="greybox" width="10%">To Date</td>
						<td class="greybox"><s:textfield name="toDate" id="toDate"
								cssStyle="width:100px" value='%{getFormattedDate()}'
								onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
							href="javascript:show_calendar('concurrenceReport.toDate');"
							style="text-decoration: none">&nbsp;<img
								src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)<br />
						</td>
					</tr>
				</table>

				<br />
				<br />

				<div class="buttonbottom">
					<s:submit method="search" value="Search" cssClass="buttonsubmit" />
					<input type="button" value="Close"
						onclick="javascript:window.close()" class="button" />
				</div>


				<s:if test="%{fixedDepositList.size()>0}">
					<jsp:include page="../revenue/fixedDeposit-modifyForm.jsp"></jsp:include>
					<div align="center" class="buttonbottom">
						<input type="button" value="Close"
							onclick="javascript:window.close()" class="button" />
						<s:submit value="Export Excel" method="exportXls"
							cssClass="button" />
						<s:submit value="Export Pdf" method="exportPdf" cssClass="button" />
					</div>
					<s:else>
						<div id="msgdiv" style="display: block">
							<table align="center" class="tablebottom" width="80%">
								<tr>
									<th class="bluebgheadtd" colspan="7">No Records Found
									</td>
								</tr>
							</table>
						</div>

					</s:else>
				</s:if>
				<script>
		for ( var i = 0; i < document.forms[0].length; i++) {
		//bootbox.alert(document.forms[0].elements[i].name);
			if (document.forms[0].elements[i].name == 'fromDate'
					|| document.forms[0].elements[i].name == 'toDate'
					|| document.forms[0].elements[i].type == 'submit' || document.forms[0].elements[i].name == 'mode') {
				
			} else {
				document.forms[0].elements[i].disabled = true;
			}
		}
		</script>
			</s:form>
		</body>
</html>
