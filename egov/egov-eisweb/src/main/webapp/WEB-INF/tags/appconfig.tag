<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="key" required="true" %>
<%@ attribute name="moduleName" required="true" %>
<%=org.egov.infstr.utils.AppConfigTagUtil.getAppConfigValue(key,moduleName,application.getContext("/egi"))%>
