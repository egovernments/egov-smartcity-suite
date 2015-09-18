<%@page contentType="text/json" %>
<%@taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>  
{
"ResultSet": {
    "Result":[
    {"Value":"<s:property value="%{categoryExists}" />",
     "validationMessage":"<s:property value="%{validationMessage}" />"}
    ]
  }
}