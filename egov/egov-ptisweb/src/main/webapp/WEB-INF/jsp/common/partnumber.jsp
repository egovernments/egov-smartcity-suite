<%@ include file="/includes/taglibs.jsp" %>

<td> 
	<s:text name="partNo" /> :
</td>
<td> 
	<egov:ajaxdropdown id="partNoAjaxDropdown" fields="['Text','Value']"
		dropdownId="partNumbers" url="common/ajaxCommon!partNumbersByWard.action" />
	<s:select id="partNumbers" headerKey="-1"
		headerValue="%{getText('default.select')}"
		list="dropdownData.partNumbers" cssClass="selectnew" name="partNo"
		value="%{partNo}" style="width: 100px"/>
</td>
