<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="receiptDetailsforbillApportioning" status="status">  
    {"OrderNumber":"<s:property value="%{ordernumber}" />",
    "CreditAmount":"<s:property value="%{cramount}" />",
    "DebitAmount":"<s:property value="%{dramount}" />",
    "CrAmountToBePaid":"<s:property value="%{cramountToBePaid}" />"	
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}

