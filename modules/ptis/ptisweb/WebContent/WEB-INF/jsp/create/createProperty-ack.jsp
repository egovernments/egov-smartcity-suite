<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><s:text name='CreateAck.title'/></title>
  <script type="text/javascript">
  </script>
  </head>
  
  <body onload=" refreshParentInbox();" >
  <s:form name="CreatePropertyAckForm" theme="simple">
  <s:push value="model">
  <s:token />
  <div class="formmainbox">
  <div class="formheading"></div>
		<div class="headingbg"><s:text name="CreatePropertyAckHeader"/></div>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<s:if test="%{basicProperty.upicNo!=null && !basicProperty.upicNo.isEmpty()}">
				<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold"><s:property value="%{ackMessage}"/></span>
		        	<a href='../view/viewProperty!viewForm.action?propertyId=<s:property value="%{basicProperty.upicNo}"/>' >
		        		<s:property value="%{basicProperty.upicNo}"/>
		        	</a>
		        </td>		    
		   	</s:if>
	        <s:else>
		       	<td colspan="5" style="background-color: #FDF7F0;font-size: 15px;" align="center">
		        	<span class="bold"><s:property value="%{ackMessage}"/></span>
		        </td>
	        </s:else>
		</tr>
		</table>
	</div>
	<div class="buttonbottom" align="center">
			<s:if test="%{userDesgn.equalsIgnoreCase(@org.egov.ptis.nmc.constants.NMCPTISConstants@ASSISTANT_DESGN)}">
				<td><s:submit value="Create" name="Create" id="Create"  method="newForm" cssClass="buttonsubmit" /></td>
			</s:if>
			<s:if test="%{basicProperty.upicNo!=null && !basicProperty.upicNo.isEmpty()}">
				<td>
					<input type="button" name="button2" id="button2"
							value="GenerateCalSheet" class="button"
							onclick="window.location='../notice/propertyIndividualCalSheet!generateCalSheet.action?indexNum=<s:property value="%{basicProperty.upicNo}"/>';" />
				</td>
			</s:if>
			<td><input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();"/></td>
		</div>
  </s:push>
  </s:form>
  </body>
</html>