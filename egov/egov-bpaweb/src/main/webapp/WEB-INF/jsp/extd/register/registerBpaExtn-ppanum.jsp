<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[ 
    {
   <s:if test="%{ppanum == true}">  
         "value":"success"
   </s:if>
   <s:else>
           "value" : "error",
           "message": "Please Enter Valid Existing Plan Submission Number/Existing Building Permit Number "      
   </s:else>}
    ]
  }
}