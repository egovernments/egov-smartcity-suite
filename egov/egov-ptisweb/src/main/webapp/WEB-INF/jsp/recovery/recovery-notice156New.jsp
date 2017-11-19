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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn"%>

<html>
	<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/recovery.js"></script>
		<title><s:text name="recovery"></s:text></title>
		<link href="<cdn:url value='/resources/css/headertab.css'/>" rel="stylesheet" type="text/css" />
	</head>
	<body class="yui-skin-sam">
	<s:form action="recovery" method="post" name="recoveryForm" theme="simple">
	<s:push value="model">
	<div class="errorstyle" id="lblError" style="display:none;"></div>
	<s:actionerror/>  <s:fielderror />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="propertyHeaderTab" class="First Active">
						<a id="header_1" href="#" onclick="showPropertyHeaderTab();"><s:text name="propDet"></s:text></a>
					</li>
					<li id="recoveryDetailTab" class="">
						<a id="header_2" href="#" onclick="showRecoveryHeaderTab();"><s:text name="recoveryDet"></s:text></a>
					</li>
					<li id="approvalTab" class="Last">
						<a id="header_3" href="#" onclick="showApprovalTab();"><s:text name="approval.details.title"></s:text></a>
					</li>
				</ul>
            </div></td>
          </tr>
     
           
          <tr>
            <td>
            <div id="property_header">
         			<jsp:include page="../view/viewProperty.jsp"/>
         			 <s:hidden name="basicProperty"   id="basicProperty" value="%{basicProperty.id}"></s:hidden>
            </div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="recovery_header" style="display:none;"> 
            	<jsp:include page="intimationView.jsp"/>
        		<jsp:include page="warrantApplicationView.jsp"/>
				<jsp:include page="warrantNotice.jsp"/>
            </div>
            </td>
          </tr>
          <tr>
            <td>
            <div id="approval_header" style="display:none;"> 
            <div id="wfHistoryDiv">
	  			<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	       			 <c:param name="stateId"  value="${recovery.state.id}"></c:param>
       			 </c:import>
       			</div>
         		<jsp:include page="../workflow/property-workflow.jsp"/>
            </div>
            </td>
          </tr>
	  </table> 
	  <div class="buttonbottom" align="center">
	  	<table>
		<tr>
		    	<td><s:submit value="Forward" name="forward" id="forward"  method="warrantNotice" cssClass="buttonsubmit" onclick="return validateWarrantNotice(this);" /></td>
		    	<td><s:submit value="Save" name="save" id="save"  method="warrantNotice" cssClass="buttonsubmit" onclick="return validateWarrantNotice(this);"/></td>
		    	<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		  
		</tr>             
		</table></div>
		<s:hidden name="model.id" id="model.id"/>      
		</s:push>
	</s:form>
<script>


</script>
</body>
</html>
