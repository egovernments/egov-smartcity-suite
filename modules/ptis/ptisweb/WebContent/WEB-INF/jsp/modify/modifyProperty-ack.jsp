<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <s:if test="modifyRsn=='AMALG'">
		   <title><s:text name='AmalgProp.title'/></title>
	</s:if>
	<s:if test="modifyRsn=='BIFURCATE'">
		   <title><s:text name='BifurProp.title'/></title>
	</s:if>
	<s:if test="modifyRsn=='MODIFY' || modifyRsn=='OBJ'">
		   <title><s:text name='ModProp.title'/></title>
	</s:if>
	<s:elseif test="modifyRsn == 'DATA_UPDATE'">
		   <title><s:text name='assessmentDataUpdate'/></title>
	</s:elseif>
  </head>
  
  <body onload=" refreshParentInbox();" >
  <s:form name="ModifyPropertyForm" theme="simple">
  <s:push value="model">
  <s:token/>
  <div class="formmainbox">
  <div class="formheading"></div>
  
  		<s:if test="modifyRsn == 'DATA_UPDATE'">
  			<div class="headingbg"><s:text name="assessmentDataUpdate"/></div>
  		</s:if>
  		<s:else>
  			<div class="headingbg"><s:text name="ModifyPropAckHeader"/></div>
  		</s:else>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		        <td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold"><s:property value="%{ackMessage}"/></span>
		        	<a href='../view/viewProperty!viewForm.action?propertyId=<s:property value="%{basicProp.upicNo}"/>' >
		        		<s:property value="%{basicProp.upicNo}"/>
		        	</a>
		        </td>
		</tr>
		</table>
	</div>
	<div class="buttonbottom" align="center">
			<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</div>
  </s:push>
  </s:form>
  </body>
</html>