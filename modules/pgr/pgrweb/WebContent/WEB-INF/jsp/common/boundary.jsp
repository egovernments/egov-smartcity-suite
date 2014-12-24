<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>

				<tr><s:if test="%{shouldShowBoundatyField('zone')}">
						<td width="25%" class="greyboxwk"> Zone  </td>
						<td width="25%" class="greybox2wk">
						<s:select id="zoneId" name="zone" cssClass="selectwk" list="dropdownData.zoneList" listKey="id" listValue="name" 
					headerKey="-1"  headerValue="----Choose----" value="%{zone}" onChange="setupAjaxWards(this);" />
						</td></s:if>
						<s:if test="%{shouldShowBoundatyField('ward')}">
						<egov:ajaxdropdown id="populateWard" fields="['Text','Value']" dropdownId='ward'url='common/ajaxPgr!populateWard.action' />
						<td width="25%" class="greyboxwk">Ward </td>
						<td width="25%" class="greybox2wk">
						<s:select id="ward" name="ward" cssClass="selectwk" list="dropdownData.wardList" listKey="id" listValue="name" 
					value="%{ward}" headerKey="-1" headerValue="----Choose----"onChange="setupAjaxAreas(this);" />
						</td></s:if>
						
				</tr>
				<tr>
				<s:if test="%{shouldShowBoundatyField('area')}">
					 <egov:ajaxdropdown id="populateArea"fields="['Text','Value']" dropdownId='area'
							url='common/ajaxPgr!populateArea.action' />	
						<td width="25%" class="whiteboxwk"> Area  </td>
						<td width="25%" class="whitebox2wk">
						<s:select headerKey="-1" headerValue="----Choose----" name="area" id="area" value="%{area}" 						
						cssClass="selectwk" list="dropdownData.areaList" listKey="id" listValue='name' />
						</td></s:if>
						<s:if test="%{shouldShowBoundatyField('street')}">
						<egov:ajaxdropdown id="populateStreet" fields="['Text','Value']" dropdownId='street'
						 url='common/ajaxPgr!populateStreets.action' />
						<td width="25%" class="whiteboxwk">Street </td>
						<td width="25%" class="whitebox2wk">
						<s:select headerKey="-1" headerValue="----Choose----" name="street" value="%{street}"
					id="street" cssClass="selectwk"list="dropdownData.streetList" listKey="id" listValue='name'/>
						</td></s:if>
						
			</tr>
<script>
	function setupAjaxWards(elem){
   		 zone_id=elem.options[elem.selectedIndex].value;
    		populateward({zoneId:zone_id});
	}
	function setupAjaxAreas(elem){
   		 ward_id=elem.options[elem.selectedIndex].value;
   			 populatearea({wardId:ward_id});
    		populatestreet({wardId:ward_id});
	}
	
</script>
				