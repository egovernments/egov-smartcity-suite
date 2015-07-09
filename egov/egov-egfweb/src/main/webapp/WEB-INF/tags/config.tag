<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="key" required="true" %>
<%@ attribute name="category" required="true" %>
<%@ attribute name="configFile" required="true" %>
<%@ attribute name="variable" required="true" %>

<%
request.setAttribute(variable, org.egov.infstr.utils.EGovConfig.getProperty(configFile,key,"",category));%>
