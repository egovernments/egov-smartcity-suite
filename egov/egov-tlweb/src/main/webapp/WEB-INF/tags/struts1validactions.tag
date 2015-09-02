<%@ tag isELIgnored="false" import="java.util.List,org.springframework.context.ApplicationContext,org.egov.infstr.workflow.WorkflowService,org.springframework.web.servlet.support.RequestContextUtils,org.egov.infstr.workflow.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ attribute name="formName" required="true" %>
<%@ attribute name="workflowServiceName" required="true" %>
<%@ attribute name="workflowItem" required="true" type="org.egov.infstr.models.StateAware" %>

<%
ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
WorkflowService workflowService = (WorkflowService)context.getBean(workflowServiceName);
List<Action> validActions=(List<Action>)workflowService.getValidActions(workflowItem);
%>

<c:set var="validActions" value="<%=validActions%>" scope="page" />

<div class="buttonholderwk">
<html:hidden  property="actionName" />	
<logic:iterate name="validActions" id="action">
  <html:submit  value="${action.description}" property="${action.name}"  onclick="document.${formName}.actionName.value='${action.name}'"/>
</logic:iterate>

  <html:button  value="CLOSE" property="closeButton"  onclick="window.close();"/>
</div>
