  <%@ include file="/includes/taglibs.jsp" %>
  <% System.out.println("added.commonWorkflowMatrix-button.jsp................"); %>
<div class="buttonbottom" align="center">
	<table>
		<tr>
			<td><s:iterator value="%{getValidActions()}" var="name">
			name:<s:property value="%{name}"/>
					<s:if test="%{name!=''}">
						<s:submit type="submit" cssClass="buttonsubmit" value="%{name}"
							id="%{name}" name="%{name}" method="save"
							onclick="return validateWorkFlowApprover('%{name}','jsValidationErrors');" />
					</s:if>
				</s:iterator> <input type="button" name="button2" id="button2" value="Close"
				class="button" onclick="window.close();" /></td>
		</tr>
	</table>
</div>