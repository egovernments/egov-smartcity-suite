<%@ page contentType="text/json" %>
<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %> 
{
"ResultSet": {
    "Result":[
    <s:iterator var="s" value="subledgerDetails" status="status">
    {"subledgerCode":"<s:property value="serviceAccountDetail.glCodeId.glcode"/>",
    "glcodeId":"<s:property value="serviceAccountDetail.glCodeId.id"/>",
    "detailTypeId":"<s:property value="detailType.id"/>",
    "detailTypeName":"<s:property value="detailType.name"/>",
    "detailCode":"<s:property value="detailCode"/>",
    "detailKeyId":"<s:property value="detailKeyId"/>",
    "detailKey":"<s:property value="detailKey" />",
    "amount":"<s:property value="%{amount}" />"
    }<s:if test="!#status.last">,</s:if>
    </s:iterator>       
    ]
  }
}
