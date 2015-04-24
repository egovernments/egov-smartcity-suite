<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp" %>
<script>
function validateDetails(){
	if(document.getElementById('planSubmissionNumSearch').value==""){
		alert("Plan Submission Number is Mandatory");
		return false;
		}
}
</script>
<body>
	<s:form action="citizenSearch" theme="simple" name="searchForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr><span class="mandatory">Please Enter Complete and Correct Plan Submission Number</span></p>
			</tr>
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" ><s:text name="planSubmissionNo.lbl"/> <span class="mandatory">*</span></td>
				<td class="bluebox" ><s:textfield id="planSubmissionNumSearch" name="planSubmissionNumSearch" value="%{planSubmissionNumSearch}" /></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>					
				<td class="bluebox">&nbsp;</td>
			</tr>
			
	</table>
	<div class="buttonbottom" align="center">
			<table>

				<tr>
				
					<td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Send Sms/Mail"  method="sendSmsOrMail"  onclick="return validateDetails();" /></td>
			  	</tr>
	        </table>
	</div>	
 	</s:form>
  </body>