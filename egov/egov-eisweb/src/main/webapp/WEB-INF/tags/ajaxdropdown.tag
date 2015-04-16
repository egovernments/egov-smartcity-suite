<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="fields" required="true"%>
<%@ attribute name="dropdownId" required="true"%>
<%@ attribute name="url" required="true"%>
<%@ attribute name="optionAttributes" required="false"%>
<%@ attribute name="selectedValue" required="false" %>
<%@ attribute name="afterSuccess" required="false" %>
<%@ attribute name="contextToBeUsed" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
<%
String[] optionAttributeList=new String[0];
if(optionAttributes!=null && !"".equals(optionAttributes.trim())){
    optionAttributeList=optionAttributes.split(",");
    for(int i=0;i<optionAttributeList.length;i++){
        optionAttributeList[i]=optionAttributeList[i].trim();
    }
}
%>
${id}SuccessHandler=function(req,res){
  ${id}Dropdown=dom.get("${dropdownId}");
  var resLength =res.results.length+1;
  var dropDownLength = ${id}Dropdown.length;
  for(i=0;i<res.results.length;i++){
    ${id}Dropdown.options[i+1]=new Option(res.results[i].Text,res.results[i].Value);
    if(res.results[i].Value=='<%=selectedValue%>')   ${id}.Dropdown.selectedIndex = i;
    <%for(int i=0;i<optionAttributeList.length;i++){%>
    ${id}Dropdown.options[i+1].<%=optionAttributeList[i]%>=res.results[i].<%=optionAttributeList[i]%>;
    <%}%>
  }
 while(dropDownLength>resLength)
 {
     ${id}Dropdown.options[res.results.length+1] = null;
      dropDownLength=dropDownLength-1;
 }
  <% if(afterSuccess!=null && !afterSuccess.trim().equals("")){%>
     ${afterSuccess}(req,res)
  <%}%>
}
${id}FailureHandler=function(){
  alert('Unable to load ${dropdownId}');
}

function populate${dropdownId}(params){
   <% if(contextToBeUsed!=null && !contextToBeUsed.trim().equals("")){%>
   		<c:set var="contextRoot" value="${contextToBeUsed}" />
   <% } else  {%>
   		<c:set var="contextRoot" value="${pageContext.request.contextPath}" />
   <% } %>
   makeJSONCall(${fields},'${contextRoot}/${url}',params,${id}SuccessHandler,${id}FailureHandler) ;
}
</script>
