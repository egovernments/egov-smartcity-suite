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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>
			<s:text name='ptis.collectionReport.title' />
		</title>
		<link href="<cdn:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<cdn:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
        <script type="text/javascript">

        jQuery(function ($) {
        	try { 
        		$(".datepicker").datepicker({
        			format: "dd/mm/yyyy"
        		}); 
        		}catch(e){
        		console.warn("No Date Picker "+ e);
        	}

       		$('.datepicker').on('changeDate', function(ev){
       		    $(this).datepicker('hide');
       		});
        		
            	
        });
        	
    
		function checkBeforeSubmit() {
			var fromDate = document.getElementById("fromDate").value;
			var toDate = document.getElementById("toDate").value;
			var userId = document.getElementById("userId").value;

			if (fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY') {
				bootbox.alert('From Date is mandatory');
				return false;
			}
			
			if (toDate == null || toDate == "" || toDate == 'DD/MM/YYYY') {
				bootbox.alert('To Date is mandatory');
				return false;
			}

			if (userId == null || userId == "" || userId == "-1") {
				bootbox.alert('Select Operator');
				return false;
			}
			
			if ((fromDate != null && fromDate != "") && (toDate != null && toDate != "")) {
				if (validateFromAndToDate(fromDate,toDate))	{
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
			
		}

		
		
		</script>
	</head>

	<body>
		<s:form name="dailyCollectionReportSearch" action="dailyCollectionReport!generateReport.action" theme="simple">
			<s:actionerror/>
			<div class="formmainbox">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="100%" colspan="6" class="headingbg">												
							<div class="headingbg">					
								<s:text name="ptis.collectionReport.title" />									
							</div>									
						</td>
					</tr>
					<tr>
						<td colspan="6" class="greybox">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox" >&nbsp;</td>
						<td class="bluebox" >
							<s:text name="fromDate"/><span class="mandatory1">*</span>
						</td>
						<s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
						<td class="bluebox">
							<s:textfield id="fromDate"
							name="fromDate" value="%{cdFormat}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY"
							cssClass="datepicker"
							onblur="validateDateFormat(this);" />
							<!-- a href="javascript:show_calendar('forms[0].fromDate');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;">
							<img src="${pageContext.request.contextPath}/images/calendaricon.gif"
							alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
							<div class="highlight2" style="width: 80px">DD/MM/YYYY</div-->
						</td>
						<td class="bluebox">
							<div style="text-align:center">
								<s:text name="toDate" /><span class="mandatory1">*</span>
							</div>
						</td>
						<s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
						<td class="bluebox">
							<s:textfield id="toDate" name="toDate" value="%{cdFormat1}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY"
							cssClass="datepicker"
							onblur="validateDateFormat(this);" />
							<!-- a href="javascript:show_calendar('forms[0].toDate');"
							onmouseover="window.status='Date Picker';return true;"
							onmouseout="window.status='';return true;">
							<img src="${pageContext.request.contextPath}/images/calendaricon.gif"
							alt="Date" width="18" height="18" border="0" align="absmiddle" /></a>
								<div class="highlight2" style="width: 80px">DD/MM/YYYY</div-->
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
					<tr>
						<td class="greybox">&nbsp;</td>
						<td class="greybox">
							<s:text name="operator"/><span class="mandatory1">*</span>
						</td>
						<td class="greybox">
							<s:select list="dropdownData.userList" listKey="id" listValue="userName" cssClass="selectnew"
								headerKey="-1" headerValue="%{getText('default.select')}" name="userId" value="%{userId}" id="userId"/>
						</td>
						<td class="greybox">
							&nbsp;
						</td>
						<td colspan="2" class="greybox">&nbsp;</td>
					</tr>
			</table>
			<br/>
			<tr>
        		<font size="2"><div align="left" class="mandatory1"> &nbsp;&nbsp; <s:text name="mandtryFlds"/></div></font>
        	</tr>
		</div>
		<div class="buttonbottom" align="center">
			<tr>
				<td>
					<s:submit name="submit" value="Generate Report" id="search" method="generateReport" 
						cssClass="buttonsubmit" onclick="return checkBeforeSubmit();"></s:submit>
					<input name="buttonClose" type="button" class="button"
						id="buttonClose" value="Close" onclick="window.close()" />
						&nbsp;
				</td>
			</tr>
		</div>
		</s:form>
		<s:if test="%{!searchForm}">
			<div>
				<s:text name="noRecsFound"></s:text>	
			</div>		
		</s:if>
	</body>
</html>
