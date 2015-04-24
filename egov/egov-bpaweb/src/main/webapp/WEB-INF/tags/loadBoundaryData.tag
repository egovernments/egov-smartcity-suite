<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false"%>
<%@ attribute name="assetId" required="false" %>
<%@ attribute name="assetObjName" required="false" %>
<%@ attribute name="adminboundaryid" required="false" %>
<%@ attribute name="locboundaryid" required="false" %>
<%@ attribute name="adminBndryVarId" required="false" %>
<%@ attribute name="locBndryVarId" required="false" %>

<%@ attribute name="isreadonly" required="false" %>

<%@ tag import="org.egov.bpa.utils.BoundaryUtil,
		org.springframework.web.context.support.WebApplicationContextUtils,
		org.springframework.web.context.WebApplicationContext,
		java.util.Map,
		java.util.Set,
		java.util.List,
		org.egov.lib.admbndry.Boundary,
		org.egov.lib.admbndry.BoundaryType" %>
		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<%
	Integer locationBndryId = null;
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
	Map<String, Integer> selectedVals;
	
	
	 	bndryTypeValuesMap =  bndryUtil.getMapofBndryTypeAndValues(adminBoundaryId, locationBndryId);
	 	selectedVals = bndryUtil.getSelectedBndryTypeValueMap(adminBoundaryId, locationBndryId);
	 
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

