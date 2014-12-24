<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    {"Amount":"<s:property value="%{sdDeductedAmount}" />"}    
    ]
  }
}