
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
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="challan.title.workflowsuccess" /></title>
</head>
<body>

<s:form theme="simple" name="challan">
<s:push value="model">
	<div class="subheadnew">
	<s:if test="%{sourcePage=='cancelReceipt'}">
	
				  <!--  copy from receipt index start -->
				 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
					<tr align="center">
					<font size="2" color="red"><b>
						<div align="center"><s:text name="billreceipt.cancel.confirmatiommessage"/></div>
					</b></font>
					</tr>
					<tr>
						<th class="bluebgheadtd" width="20%" ><s:text name="billreceipt.challannumber.confirmation"/></th>
						<th class="bluebgheadtd" width="30%" ><s:text name="billreceipt.receiptnumber.confirmation"/></th>
						<th class="bluebgheadtd" width="25%" ><s:text name="billreceipt.receiptdate.confirmation"/></th>
						<th class="bluebgheadtd" width="25%" ><s:text name="billreceipt.receiptstatus.confirmation"/></th>
					</tr>
					<tr>
						<td class="blueborderfortd"><div align="center"><s:property value="%{challan.challanNumber}" /></div></td>
						<td class="blueborderfortd"><div align="center"><s:property value="%{receiptnumber}" /></div></td>
						<td class="blueborderfortd"><div align="center"><s:date name="createdDate" var="cdFormat" format="dd/MM/yyyy"/><s:property value="%{cdFormat}" /></div></td>
						<td class="blueborderfortd"><div align="center"><s:property value="%{status.description}" /></div></td>
					</tr>
				</table>
				 <!--  copy end -->
	</s:if>
		<s:hidden id="sourcePage" name="sourcePage" value="%{sourcePage}"/>
    <s:if test="%{sourcePage==null || sourcePage=='inbox' }"> 
		<s:if test="%{challan.state.value=='CHECKED'}">
			<s:text name="challan.checksuccess.message" />
		</s:if> 
		<s:elseif test="%{challan.state.value=='APPROVED'}">
			<s:text name="challan.approvesucces.message" />
		</s:elseif>
		<s:elseif test="%{challan.state.value=='END'  && challan.status.code=='VALIDATED' && sourcePage!='cancelReceipt'}">
			<s:text name="challan.validatesuccess.message" />
		</s:elseif> 
		<s:elseif test="%{challan.state.value=='REJECTED'}">
			<s:text name="challan.rejectsuccess.message" /> <s:property value="%{approverName}" />
		</s:elseif>
		<s:elseif test="%{challan.state.value=='END' && challan.status.code=='CANCELLED'}">
			<s:text name="challan.cancelsuccess.message" />
		</s:elseif>
		</s:if>
		
	</div>
	<br />

	<div class="buttonbottom">
	<input name="buttonClose" type="button" class="button"
		id="buttonClose" value="Close" onclick="window.close()" />
	</div>
</s:push>
</s:form>
<script src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
</body>
</html>
