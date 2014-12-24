<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>


<html>
	<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/recovery.js"></script>
		<title><s:text name="recovery"></s:text></title>
		<link href="<c:url value='/css/headertab.css'/>" rel="stylesheet" type="text/css" />
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
		   	<s:if test="userRole.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PTVALIDATOR_ROLE) || 
        				userRole.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@PTCREATOR_ROLE)">
		    	<td><s:submit value="Forward" name="forward" id="forward"  method="warrantNotice" cssClass="buttonsubmit" onclick="return validateWarrantNotice(this);" /></td>
		    	<td><s:submit value="Save" name="save" id="save"  method="warrantNotice" cssClass="buttonsubmit" onclick="return validateWarrantNotice(this);"/></td>
		    </s:if>
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