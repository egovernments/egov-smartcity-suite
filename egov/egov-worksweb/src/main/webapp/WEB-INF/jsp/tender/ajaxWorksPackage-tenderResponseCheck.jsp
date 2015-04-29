<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  

{
"ResultSet": {
    "Result":[
    {"istenderResponsePresent":"<s:property value="%{tenderResponseCheck}" />",
     "tnNumber":"<s:property value="%{tenderNegotiationNo}" />"	
    }    
    ]
  }
}