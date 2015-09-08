<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {
     "milestoneexistsOrNot":"<s:property value="%{milestoneexistsOrNot}" />",
     "woWorkCommenced":"<s:property value="%{woWorkCommenced}" />"
    }    
    ] 
  }
}
