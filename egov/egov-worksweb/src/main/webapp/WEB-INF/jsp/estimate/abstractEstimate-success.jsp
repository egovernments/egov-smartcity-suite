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

<%@ include file="/includes/taglibs.jsp" %>
<html>

<title><s:text name='page.title.estimate'/></title>
<body>
<script>
</script>
	<s:if test="%{estimateId != null &&estimateId != ''}">  
		<s:property value="%{model.estimateNumber}"/> &nbsp;: <s:text name="%{getText(messageKey)}" />
	</s:if>
	<s:else>
		<s:if test="%{model.currentState.nextAction != '' && model.currentState.nextAction != 'END'}">
			<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />(<s:property value="%{model.egwStatus.description}"/>) - <s:text name="%{model.currentState.nextAction}"/>
		</s:if>
		<s:elseif test="%{model.currentState.nextAction != '' && model.currentState.nextAction == 'END'">
			<s:property value="%{model.estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />(<s:property value="%{model.egwStatus.description}"/>)
		</s:elseif>
		<s:else>
			<s:property value="%{estimateNumber}"/> &nbsp; <s:text name="%{getText(messageKey)}" />
		</s:else>
		<br>
		<s:if test="%{projectCode}">
				<s:if test="%{model.egwStatus.code=='CANCELLED'}">			
				</s:if>
				<s:else>
					The project code for the estimate is <s:property value="%{projectCode.code}" />
					<br/>
					<input type="button" onclick="window.open('${pageContext.request.contextPath}/estimate/abstractEstimatePDF.action?estimateID=<s:property value='%{model.id}'/>');" class="buttonpdf" value="PRINT Estimate PDF" id="pdfButton" name="pdfButton"/>
				</s:else>
		</s:if>
	    <s:else>
		 <br />
				<%-- <s:if test="%{model.egwStatus.code=='REJECTED' && model.currentState.nextAction=='Pending Budgetary Appropriation'}">
					Budgetary cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" />	
				<br />
				</s:if>	 
				<s:if test="%{model.egwStatus.code=='REJECTED' && model.currentState.nextAction=='Pending Deposit Code Appropriation'}">
					Deposit Code cancellation number for the estimate is <s:property value="%{budgetRejectionNo}" />	
				<br />
				</s:if>	 	 --%>				
				<s:if test="%{model.egwStatus.code=='ADMIN_SANCTIONED' || model.egwStatus.code=='CANCELLED'}">
		 		</s:if>
				<s:else>	
					The File has been forwarded to <s:property value="%{employeeName}" />(<s:property value="%{designation}" />)
				</s:else>	
		</s:else>
	</s:else>
</body>
</html>
