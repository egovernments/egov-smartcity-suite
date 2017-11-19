<%@ include file="/includes/taglibs.jsp"%>
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

<head>

<title><s:text name="dishonorchequeReport.title"></s:text></title>
<script type="text/javascript">
<jsp:useBean id="now" class="java.util.Date" />

<fmt:formatDate var = "currDate" pattern="dd/MM/yyyy" value="${now}" />
	var currDate = "${currDate}";
	
	function validate() {
		var valid = false;
		if (document.getElementById('fromDate').value != ""
				&& document.getElementById('fromDate').value != null) {
			valid = true;
		} else if (document.getElementById('toDate').value != ""
				&& document.getElementById('toDate').value != null) {
			valid = true;
		} else if (document.getElementById("service").value != null
				&& document.getElementById("service").value != -1) {
			valid = true;
		} else if (document.getElementById("paymentMode").value != null
				&& document.getElementById("paymentMode").value != "ALL") {
			valid = true;
		} else if (document.getElementById("locationId").value != null
				&& document.getElementById("locationId").value != -1) {
			valid = true;
		} else if (document.getElementById("statusId").value != null
				&& document.getElementById("statusId").value != -1) {
			valid = true;
		} else if (document.getElementById("instrumentNumber").value != null
				&& document.getElementById("instrumentNumber").value != "") {
			valid = true;
		}

		if (!valid) {
			dom.get("atleastOneCriteria").style.display = "block";
			valid = false;
		} 

		var fromdate=dom.get("fromDate").value;
		var todate=dom.get("toDate").value;
		var valSuccess = true;
		document.getElementById("report_error_area").innerHTML = "";

			if (fromdate == "") {
				document.getElementById("report_error_area").style.display = "block";
				document.getElementById("report_error_area").innerHTML += '<s:text name="common.datemandatory.fromdate" />'
						+ '<br>';
				valSuccess = false;
			}

			if (todate == "") {
				document.getElementById("report_error_area").style.display = "block";
				document.getElementById("report_error_area").innerHTML += '<s:text name="common.datemandatory.todate" />'
						+ '<br>';
				valSuccess = false;
			}

			if (fromdate != "" && todate != "" && fromdate != todate) {
				if (!checkFdateTdate(fromdate, todate)) {
					document.getElementById("report_error_area").style.display = "block";
					document.getElementById("report_error_area").innerHTML += '<s:text name="common.comparedate.errormessage" />'
							+ '<br>';
					valSuccess = false;
				}
				if (!validateNotFutureDate(fromdate, currDate)) {
					document.getElementById("report_error_area").style.display = "block";
					document.getElementById("report_error_area").innerHTML += '<s:text name="reports.fromdate.futuredate.message" />'
							+ '<br>';
					valSuccess = false;
				}
				if (!validateNotFutureDate(todate, currDate)) {
					document.getElementById("report_error_area").style.display = "block";
					document.getElementById("report_error_area").innerHTML += '<s:text name="reports.todate.futuredate.message" />'
							+ '<br>';
					valSuccess = false;
				}
			}
			valid=valSuccess;

		return valid;
	}
</script>
</head>
<body>
<span align="center" style="display: none" id="atleastOneCriteria">
	<li><font size="2" color="red"><b> <s:text
					name="dishonorchequeReport.atleast.one.criteria" />
		</b></font></li>
</span>
<div class="errorstyle" id="report_error_area" style="display:none;"></div>
	<s:form theme="simple" name="dishonorChequeReportForm"
		action="dishonoredChequeReport-generateReport.action">
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="dishonorchequeReport.title" />
			</div>
			<div class="subheadsmallnew">
				<span class="subheadnew"><s:text
						name="dishonorchequeReport.criteria" /></span>
			</div>

			<input type="hidden" name="hostName" id="hostName"
				value="${hostName}" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.fromdate" /></td>
					<s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
					<td class="bluebox"><s:textfield id="fromDate" name="fromDate"
							value="%{cdFormat}" onfocus="javascript:vDateType='3';"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].fromDate');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;" placeholder="DD/MM/YYYY"><img
							src="/collection/resources/images/calendaricon.gif" alt="Date"
							width="18" height="18" border="0" align="absmiddle" /></a>
							<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.todate" /></td>
					<s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
					<td class="bluebox"><s:textfield id="toDate" name="toDate"
							value="%{cdFormat1}" onfocus="javascript:vDateType='3';"
							onkeyup="DateFormat(this,this.value,event,false,'3')" /><a
						href="javascript:show_calendar('forms[0].toDate');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;" placeholder="DD/MM/YYYY"><img
							src="/collection/resources/images/calendaricon.gif" alt="Date"
							width="18" height="18" border="0" align="absmiddle" /></a>
							<div class="highlight2" style="width: 80px">DD/MM/YYYY</div>
				</tr>

				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.service" /></td>
					<td class="bluebox"><s:select headerKey="-1"
							headerValue="%{getText('dishonorchequeReport.criteria.select')}"
							name="serviceId" id="service" cssClass="selectwk"
							list="dropdownData.servicetypeList" listKey="id" listValue="name"
							value="%{serviceId}" /></td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.payment.mode" /></td>
					<td class="bluebox"><s:select headerKey="ALL"
							headerValue="%{getText('dishonorchequeReport.criteria.select')}"
							name="paymentMode" id="paymentMode" cssClass="selectwk"
							list="paymentModes" value="%{paymentMode}" /></td>

				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.location" /></td>
					<td class="bluebox"><s:select headerKey="-1"
							headerValue="%{getText('dishonorchequeReport.collectionlocation.all')}"
							name="locationId" id="locationId" cssClass="selectwk"
							list="dropdownData.locationList" listKey="id" listValue="name"
							value="%{locationId}" /></td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.status" /></td>
					<td class="bluebox"><s:select headerKey="-1"
							headerValue="%{getText('dishonorchequeReport.criteria.select')}"
							name="statusId" id="statusId" cssClass="selectwk"
							list="dropdownData.statusList" listKey="id"
							listValue="description" value="%{statusId}" /></td>

				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text
							name="dishonorchequeReport.criteria.chequenumber" /></td>
					<td class="bluebox"><s:textfield id="instrumentNumber"
							type="text" name="instrumentNumber" maxlength="6" cssClass="patternvalidation" data-pattern="number" /></td>
					<td class="bluebox"></td>
					<td class="bluebox"></td>
				</tr>
			</table>
			<div id="loadingMask"
				style="display: none; overflow: hidden; text-align: center">
				<img id="removeImage"
					src="/collection/resources/images/bar_loader.gif" /> <span
					id="removeText" style="color: red">Please wait....</span>
			</div>


			<div class="buttonbottom">
				<label><s:submit type="submit" cssClass="buttonsubmit"
						id="button" value="%{getText('collectionReport.create')}"
						onclick="return validate();" />
				</label>&nbsp; <label> <s:reset type="submit" cssClass="button"
						value="%{getText('collectionReport.reset')}"
						onclick="return clearErrors();" />
				</label> <label> <input type="button" class="button"
					id="buttonClose" value="<s:text name='common.buttons.close'/>"
					onclick="window.close()" />
				</label>
			</div>
	</s:form>

</body>
</html>
