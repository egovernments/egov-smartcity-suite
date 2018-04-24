<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

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
