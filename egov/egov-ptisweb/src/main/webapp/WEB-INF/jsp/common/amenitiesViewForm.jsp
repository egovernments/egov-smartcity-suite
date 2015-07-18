<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" class="checkbox-section">
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="lift"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.lift == true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>
		<td class="greybox" width="20%"><s:text name="electricity"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.electricity == true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>
    </tr>
    <tr>
    <td class="greybox" width="5%">&nbsp;</td>
			<td class="greybox" width="20%"><s:text name="toilets"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.toilets == true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>

        <td class="greybox" width="20%"><s:text name="attachbathroom"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.attachedBathRoom == true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>
	</tr>
	 <tr>
	 <td class="greybox" width="5%">&nbsp;</td>
			<td class="greybox" width="20%"><s:text name="watertap"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.waterTap == true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>

        <td class="greybox" width="20%"><s:text name="waterharvesting"></s:text>:</td>
		<td class="greybox" width="20%">
		<s:if test="%{propertyDetail.waterHarvesting== true}">
				<span class="bold"> Yes</span>
			</s:if> <span class="bold"> <s:else>No</s:else></span></td>
	</tr>

</table>