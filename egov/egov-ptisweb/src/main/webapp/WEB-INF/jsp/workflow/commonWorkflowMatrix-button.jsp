 <%@ include file="/includes/taglibs.jsp" %>
<div class="buttonbottom" align="center">
	<table>
		<tr>
			<td><s:iterator value="%{getValidActions()}" var="name">
					<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonsubmit" value="%{name}"
							id="%{name}" name="%{name}"
							onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');" />
					</s:if>
				</s:iterator> <input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" /></td>
		</tr>
	</table>
</div>