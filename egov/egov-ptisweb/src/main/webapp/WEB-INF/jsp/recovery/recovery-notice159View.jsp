<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="org.egov.ptis.constants.PropertyTaxConstants" %>

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
					<li id="propertyHeaderTab" class="First Active"><a id="header_1" href="#" onclick="showPropertyHeaderTab();"><s:text name="propDet"></s:text></a></li>
					<li id="recoveryDetailTab" class=""><a id="header_2" href="#" onclick="showRecoveryHeaderTab();"><s:text name="recoveryDet"></s:text></a></li>
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
            <div id="recovery_header" style="display:none;"> 
            	<jsp:include page="intimationView.jsp"/>
        		<jsp:include page="warrantApplicationView.jsp"/>
				<jsp:include page="notice156View.jsp"/>
				<jsp:include page="notice159View.jsp"/>
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
       			<s:if test="! state.nextAction.equalsIgnoreCase('Issue Notice 159')">
         			<jsp:include page="../workflow/property-workflow.jsp"/>
         		</s:if>
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
            </td>
          </tr>
	  </table> 
	  <div class="buttonbottom" align="center">
	  	<table>
		<tr>
			
			<s:if test="state.nextAction.equalsIgnoreCase('Issue Notice 159')">
				<td><s:submit value="Notice 159" name="notice159" id="notice159"  method="generateCeaseNotice"  cssClass="buttonsubmit" /></td>
			</s:if>
			<s:else>
		   			<td><s:submit value="Approve" name="approve" id="approve"  method="updateWf" cssClass="buttonsubmit" onClick="return validateApproval(this)"/></td>
		    		<td><s:submit value="Forward" name="forward" id="forward"  method="updateWf" cssClass="buttonsubmit" onClick="return validateApproval(this)"/></td>
		    		<td><s:submit value="Save" name="save" id="save"  method="updateWf" cssClass="buttonsubmit"  onClick="return validateApproval(this)"/></td>
		    	<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();" /></td>
		  </s:else>
		</tr>             
		</table></div>
		<s:hidden name="model.id" id="model.id"/>      
		</s:push>
	</s:form>
<script>


</script>
</body>
</html>
