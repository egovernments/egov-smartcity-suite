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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>
<s:if test="%{wfType.equals(@org.egov.ptis.constants.PropertyTaxConstants@WFLOW_ACTION_NAME_GRP)}">
					<s:text name="objection.GRPView.title"></s:text>
			    </s:if>
			    <s:else>
<s:text name='objection.record.message.title' />
</s:else>
</title>
</head>

<body onload="refreshInbox()">
	<s:push value="model">
		<div class="formmainbox">
			<div class="text-center" style="padding:20px;">
				<s:if test="%{hasActionMessages()}">
					<font style='color: green; font-weight: bold'> <s:actionmessage />
					</font>
				</s:if>
			</div>

		<s:if test="isShowAckMessage == true">			
			<div align="center" style="font-size:15px;">
				<p>
					<span class="mandatory">
						No valid assessment data exists for property 
					</span>
					<a href='../view/viewProperty-viewForm.action?propertyId=<s:property value="%{propertyId}" />' style="fond-size:15px;">
						<s:property value="%{propertyId}" /> 
					</a> 
					<span class="mandatory"> 
						, Please update the data before proceeding with Objection 
					</span>
				</p>
			</div>
		</s:if>
		<div class="buttonbottom" align="center">
			<table style="width:100%;text-align:center;">				
				<tr>
					<s:if test="isShowAckMessage == true">
						<td>
							<input type="button" class="button" name="SearchProperty"
								id="SearchProperty" value="Search Property" 
								onclick="window.location='../search/searchProperty-searchForm.action';" />	
						</td>
					</s:if>
					<td>
						<input type="button" name="button2" id="button2"
						value="Close" class="button" onclick="window.close();" />
					</td>

				</tr>
			</table>
		</div>
	</s:push>
	</div>
	<script>
 
		function refreshInbox() {
			if (opener && opener.top.document.getElementById('inboxframe'))
				opener.top.document.getElementById('inboxframe').contentWindow.egovInbox
						.refresh();
		}
	</script>
</body>

</html>
