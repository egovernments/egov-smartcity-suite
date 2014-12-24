<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    	{
    		"responseFlag":"<s:property value="%{validateFlag}"/>"
    	}
    ]
  }
}