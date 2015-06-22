<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
	The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->

<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.egov.ptis.constants.PropertyTaxConstants" %>

<html>
	<head>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/objection.js"></script>
		<title><s:text name="objectionView.title" /></title>
		<script type="text/javascript">
			jQuery.noConflict();
			jQuery("#loadingMask").remove();
		</script>
		<link href="<c:url value='/css/headertab.css'/>" rel="stylesheet" type="text/css" />
	</head>
	<body class="yui-skin-sam">
	<s:form action="objection" method="post" name="objectionViewForm" theme="simple">
	<s:push value="model">
	<div class="errorstyle" id="lblError" style="display:none;"></div>
	<s:actionerror/>  <s:fielderror />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td><div id="header">
				<ul id="Tabs">
					<li id="propertyHeaderTab" class="First Active"><a id="header_1" href="#" onclick="showPropertyHeaderTab();"><s:text name="propDet"></s:text></a></li>
					<li id="objectionDetailTab" class=""><a id="header_2" href="#" onclick="showObjectionHeaderTab();"><s:text name="objection.details.heading"></s:text></a></li>
					<li id="approvalTab" class="Last"><a id="header_3" href="#" onclick="showApprovalTab();"><s:text name="approval.details.title"></s:text></a></li>
				</ul>
            </div></td>
          </tr>

          <tr>
            <td>
            <div id="property_header">
         			<jsp:include page="../view/viewProperty.jsp"/>
          			
            </div>            
            </td> 
          </tr>            
          <tr>
            <td>
            <div id="objection_header" style="display:none;"> 
        	
        	<jsp:include page="objectionDetailsCommonView.jsp"/>
        	
        			<s:if test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED) &&
        		state.text1.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_RECORD_SAVED)">
        				<jsp:include page="recordObjection.jsp"/>	
        		</s:if>
        
					<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) && 
						egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED)">
						<jsp:include page="addHearingDate.jsp"/>	
					</s:elseif>
					<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED)">
							<jsp:include page="recordHearingDetails.jsp"/>	
					</s:elseif>
					<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED)
							&& hearings[hearings.size()-1].inspectionRequired == true">
							<jsp:include page="recordInspecationDetails.jsp"/>	
					</s:elseif>
					<s:elseif test="(egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED))
							|| hearings[hearings.size()-1].inspectionRequired == false">
							<jsp:include page="objectionOutcome.jsp"/>	
					</s:elseif>
		
            </div>
            </td>
          </tr>
          <tr>
            <td>
            <div id="approval_header" style="display:none;"> 
         		<div id="wfHistoryDiv">
	  			<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	       			 <c:param name="stateId"  value="${objection.state.id}"></c:param>
       			 </c:import>
       			 <s:if test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED) ||
							egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED)">
       			 	<jsp:include page="../workflow/property-workflow.jsp"/>
       			 </s:if>
       			 <s:elseif test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED)
							&& hearings[hearings.size()-1].inspectionRequired == true">
       			 		<jsp:include page="../workflow/property-workflow.jsp"/>
       			 </s:elseif> 

       			 <s:else>
       			 <div align="center" ><br>
	 			  <table width="100%" border="0" cellspacing="0" cellpadding="0" >
       			 	<tr>
						<td class="bluebox" width="6%">&nbsp;</td>
				    	<td class="bluebox" width="10%"><s:text name='approver.comments'/></td>
						<td class="bluebox" width="8%"><s:textarea name="workflowBean.comments" id="comments"rows="3" cols="80" onblur="checkLength(this);" /></td>
						<td class="bluebox" width="15%" colspan="2"></td>
					</tr>	
       			 		
       			 		<s:hidden name="workflowBean.actionName" id="workflowBean.actionName"/>
       			 		</table>
       			 	</div>
       			 </s:else>
				</div>
            </div>
            </td>
          </tr>
	  </table> 
	  <div id="loadingMask" style="display:none"><p align="center"><img src="/egi/images/bar_loader.gif"> <span id="message"><p style="color: red">Please wait....</p></span></p></div>
	  <div class="buttonbottom" align="center">
	  	<table>
		<tr>
			<s:if test="egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED) &&
        		state.text1.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_RECORD_SAVED)">
        				<td><s:submit value="Forward" name="forward" id="forward"  method="updateRecordObjection" cssClass="buttonsubmit" onClick="return validateRecordObjection(this)"/></td>
		    			<td><s:submit value="Save" name="save" id="save"  method="updateRecordObjection" cssClass="buttonsubmit" onClick="return validateRecordObjection(this)"/></td>
	
        		</s:if>
				<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) && 
						egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_CREATED)">
		   				<td><s:submit value="Forward" name="forward" id="forward"  method="addHearingDate" cssClass="buttonsubmit" onClick="return validateHearingDate(this)"/></td>
		    			<td><s:submit value="Save" name="save" id="save"  method="addHearingDate" cssClass="buttonsubmit" onClick="return validateHearingDate(this)"/></td>
		   		</s:elseif>
		   		<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_FIXED)">
		   				<td><s:submit value="Forward" name="forward" id="forward"  method="recordHearingDetails" cssClass="buttonsubmit" onClick="return validateRecordHearing(this)"/></td>
		    			<td><s:submit value="Save" name="save" id="save"  method="recordHearingDetails" cssClass="buttonsubmit" onClick="return validateRecordHearing(this)"/></td>
		   		</s:elseif>
		   		<s:elseif test="egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_HEARING_COMPLETED)
							&& hearings[hearings.size()-1].inspectionRequired == true">
		   				<td><s:submit value="Forward" name="forward" id="forward"  method="recordInspectionDetails" cssClass="buttonsubmit" onClick="return validateRecordInspection(this)"/></td>
		    			<td><s:submit value="Save" name="save" id="save"  method="recordInspectionDetails" cssClass="buttonsubmit" onClick="return validateRecordInspection(this)"/></td>
		   		</s:elseif>
		   		<s:elseif test="(egwStatus.moduletype.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_MODULE) 
							&& egwStatus.code.equalsIgnoreCase(@org.egov.ptis.constants.PropertyTaxConstants@OBJECTION_INSPECTION_COMPLETED))
							|| hearings[hearings.size()-1].inspectionRequired == false">
		    			<td><s:submit value="SAVE" name="save" id="save"  method="recordObjectionOutcome" cssClass="buttonsubmit" onClick="return validateObjectionOutcome(this)"/></td>
		   		</s:elseif>

		    	<td><input type="button" name="closeButton" id="closeButton" value="Close" class="button" onclick="window.close();"/></td>
		</tr>             
		</table></div>      
		<s:hidden name="model.id" id="model.id"/>
		</s:push>
	</s:form>

</body>
</html>
