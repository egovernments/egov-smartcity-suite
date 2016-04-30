<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false"%>
<%@ attribute name="assetId" required="false" %>
<%@ attribute name="assetObjName" required="false" %>
<%@ attribute name="adminboundaryid" required="false" %>
<%@ attribute name="locboundaryid" required="false" %>
<%@ attribute name="adminBndryVarId" required="false" %>
<%@ attribute name="locBndryVarId" required="false" %>

<%@ attribute name="isreadonly" required="false" %>

<%@ tag import="org.egov.bpa.utils.BoundaryUtil,
		org.egov.infra.admin.master.entity.Boundary,
		org.egov.infra.admin.master.entity.BoundaryType,
		org.springframework.web.context.WebApplicationContext,
		org.springframework.web.context.support.WebApplicationContextUtils,
		java.util.List,
		java.util.Map" %>
		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs/struts-html.tld" prefix="html" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<!--
  eGov suite of products aim to improve the internal efficiency,transparency, 
     accountability and the service delivery of the government  organizations.
  
      Copyright (C) <2015>  eGovernments Foundation
  
      The updated version of eGov suite of products as by eGovernments Foundation 
      is available at http://www.egovernments.org
  
      This program is free software: you can redistribute it and/or modify
      it under the terms of the GNU General Public License as published by
      the Free Software Foundation, either version 3 of the License, or
      any later version.
  
      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.
  
      You should have received a copy of the GNU General Public License
      along with this program. If not, see http://www.gnu.org/licenses/ or 
      http://www.gnu.org/licenses/gpl.html .
  
      In addition to the terms of the GPL license to be adhered to in using this
      program, the following additional terms are to be complied with:
  
  	1) All versions of this program, verbatim or modified must carry this 
  	   Legal Notice.
  
  	2) Any misrepresentation of the origin of the material is prohibited. It 
  	   is required that all modified versions of this material be marked in 
  	   reasonable ways as different from the original version.
  
  	3) This license does not grant any rights to any user of the program 
  	   with regards to rights under trademark law for use of the trade names 
  	   or trademarks of eGovernments Foundation.
  
    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<%
	Integer locationBndryId = null;//todo phionix from Integer to Long
  	Integer adminBoundaryId = null;
	Long l_assetId = null;
	Boolean isReadOnly = false;
	if(adminboundaryid!=null && !adminboundaryid.equals("") && !adminboundaryid.equals("-1")) {
		adminBoundaryId = Integer.valueOf(adminboundaryid);
	}
	if(locboundaryid!=null && !locboundaryid.equals("") && !locboundaryid.equals("-1")) {
		locationBndryId = Integer.valueOf(locboundaryid);
	}
	if(assetId!=null && !assetId.equals("")) {
		l_assetId = Long.valueOf(assetId);
		request.setAttribute("assetId", l_assetId);
	}
	if(isreadonly!=null && isreadonly.equals("true")) {
		isReadOnly = true;
	}
	if (adminBndryVarId == null || "".equals(adminBndryVarId))
		adminBndryVarId = "adminboundaryid";
	if (locBndryVarId == null || "".equals(locBndryVarId))
		locBndryVarId = "locboundaryid";
		
	
	WebApplicationContext webCtx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	BoundaryUtil bndryUtil = (BoundaryUtil)webCtx.getBean("boundaryUtil");
	Map<BoundaryType, List<Boundary>> bndryTypeValuesMap;
	Map<String, Long> selectedVals;
	
	
	 	bndryTypeValuesMap =  bndryUtil.getMapofBndryTypeAndValues((long)adminBoundaryId, (long)locationBndryId);
	 	selectedVals = bndryUtil.getSelectedBndryTypeValueMap((long)adminBoundaryId, (long)locationBndryId);
	 
	request.setAttribute("bndryTypeValuesMap",bndryTypeValuesMap);
	request.setAttribute("selectedVals",selectedVals);
	
	if (isReadOnly) {
		request.setAttribute("selectDisabled","disabled='true'");
	}
%>	

	
		
		
		   <s:iterator var="bndryType"  value="#request.bndryTypeValuesMap.keySet()" status="rowstatus">
		   	<s:if test="((#rowstatus.index)%4) < 2">
		   		<s:set name="cssTDClass" value="%{'greybox'}" />
		   	</s:if>
		   	<s:else>
		   		<s:set name="cssTDClass" value="%{'bluebox'}" />
		   	</s:else>
		   	<s:if test="#bndryType.name == 'Ward'">
		   		<s:set name="crossHier" value="%{'1'}" />
		   	</s:if>
		   	<s:else>
		   		<s:set name="crossHier" value="%{'0'}" />
		   	</s:else>
		   	
		   	<s:if test="((#rowstatus.index)%2) == 0">
				<tr id="<s:property value="#bndryType.name" /><s:property value="#bndryType.name" />">
		   	</s:if>
		
				<td class="${cssTDClass}" >&nbsp;</td>
				<td class="${cssTDClass}" ><s:property value="#bndryType.name" /></td>
				<td class="${cssTDClass}">
					 <select id="${bndryType.name}" <s:property value="#request.selectDisabled"/> name="${bndryType.name}" onchange="loadLeafLevelData(this,'${bndryType.firstChild.name}','${bndryType.heirarchyType.name}','${crossHier}')">
					   <option value="-1">----Choose---</option>
			
					    <s:iterator var="boundary" value="#request.bndryTypeValuesMap.get(#bndryType)">
						<s:if test="#request.selectedVals.containsValue(#boundary.id)">
						<option value="${boundary.id}" selected="true" ><s:property value="#boundary.name" />
						</s:if>
						<s:else>
						<option value="${boundary.id}" ><s:property value="#boundary.name" /> 
						</s:else>
					    </s:iterator>
					</select>
					
					 
				</td>
			<s:if test="((#rowstatus.index)%2) == 1">
				</tr>
		   	</s:if>
			<s:if test="(#rowstatus.last == true) && (#rowstatus.index%2 == 0)">
				<td class="${cssTDClass}" >&nbsp;</td>
				<td class="${cssTDClass}" >&nbsp;</td>
				<td class="${cssTDClass}" >&nbsp;</td>
				</tr>
		   	</s:if>
		   </s:iterator>  
	
		     			  


<script type="text/javascript">

 
 
function loadLeafLevelData(obj,values,heirarchyType, crossHier)
{
	if(heirarchyType=='ADMINISTRATION')
		{  document.getElementById('${adminBndryVarId}').value=obj.value;
		}else
		{   document.getElementById('${locBndryVarId}').value=obj.value;
	}
	
	var url;
	if (crossHier == '1')
	{
		url = "<%=request.getContextPath()%>"+"/common/ajaxCommon!getCrossHierarchyBoundaries.action?boundaryId="+obj.value;
		values = 'Area';
	}
	else
	{
		url = "<%=request.getContextPath()%>"+"/common/ajaxCommon!getChildBoundaries.action?boundaryId="+obj.value;
	}
      	var request = initiateRequest();
	request.open("GET", url, false);
	request.send(null);
	if (request.readyState == 4)
	{
		if (request.status == 200)
		{
			var response=request.responseText;
			var tempvalues=response.split("!$");
			var id = tempvalues[0].split("^");
			if(values!=null && values!=""){
				Dropdown=dom.get(values);
				if(id.length >0 && values!=null)
				{
				    Dropdown.options.length = 0;
					Dropdown.options[0] = new Option("---Choose---","-1");
					for(var i = 1 ; i <= id.length  ; i++)
					{
					var name = id[i-1].split("+");
					var ids= name[0].split(",");
					if(ids!=null && ids!=""){
					var names= name[1].split(",");
					Dropdown.options[i] = new Option(names,ids);
					}
					}
				}
			}													
		}
		 else
			alert("Error occurred while getting getBoundaryData..");
	}
	  
								       	
}
</script>

