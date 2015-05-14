<%@ tag dynamic-attributes="true" isELIgnored="false"%>
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld" %>  

<%@ attribute name="formName" required="true" %>

<div class="buttonholderwk">
<input type="hidden" name="actionName" id="actionName"/>	
<s:iterator value="%{validActions}">
  <s:submit type="submit" cssClass="buttonfinal" value="%{description}" id="%{name}" name="%{name}" method="moveEstimate" onclick="document.${formName}.actionName.value='%{name}'"/>
</s:iterator>

<s:if test="%{model.id==null}">
	  <input type="reset" class="buttonfinal" value="CLEAR" id="button" name="button"/>
  </s:if>
  <input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
</div>
