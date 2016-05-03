<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
			<tr>
				<td width="25%" class="greyboxwk"><s:text name="asset.location.zone" /></td>
				<td width="25%" class="greybox2wk">
					<s:select id="zoneId" name="zoneId" cssClass="selectwk" list="dropdownData.zoneList" listKey="id" listValue="name" 
					headerKey="-1"  headerValue="%{getText('list.default.select')}" value="%{zoneId}" onChange="setupAjaxWards(this);" /></td>
				<egov:ajaxdropdown id="populateWard" fields="['Text','Value']" dropdownId='assetward'url='assetmaster/ajaxAsset-populateWard.action' />
				<td width="25%" class="greyboxwk"><s:text name="asset.location.ward" /></td>
				<td width="25%" class="greybox2wk">
					<s:select id="assetward" name="ward" cssClass="selectwk" list="dropdownData.wardList" listKey="id" listValue="name" 
					value="%{ward.id}" headerKey="-1" headerValue="%{getText('list.default.select')}"onChange="setupAjaxAreas(this);" />
				</td>
			</tr>
			<tr>
				 <egov:ajaxdropdown id="populateStreet" fields="['Text','Value']" dropdownId='street' url='assetmaster/ajaxAsset-populateStreets.action' selectedValue="%{ward.id}" />
				<td width="25%" class="whiteboxwk"><s:text name="asset.location.street" /></td>
				<td width="25%" class="whitebox2wk" >
					<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="street" value="%{street.id}"
					id="street" cssClass="selectwk"list="dropdownData.streetList" listKey="id" listValue='name'/>
				</td>
				<egov:ajaxdropdown id="populateArea"fields="['Text','Value']" dropdownId='assetarea'
				url='assetmaster/ajaxAsset-populateArea.action' />	
				<td width="25%" class="whiteboxwk"><s:text name="asset.location.area" /></td>
				<td width="25%" class="whitebox2wk" >
				<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="area" id="assetarea" value="%{area.id}" 						cssClass="selectwk" list="dropdownData.areaList" listKey="id" listValue='name'onChange="setupAjaxLoc(this);" />
				</td>
				</tr>	
			<tr>
				<egov:ajaxdropdown id="populateLocation" fields="['Text','Value']" dropdownId='location'
				url='assetmaster/ajaxAsset-populateLocations.action' selectedValue="%{area.id}" />
				<td width="11%" class="greyboxwk"><s:text name="asset.location" /></td>
				<td width="21%" class="greybox2wk" colspan="3">
				<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="location"id="location"value="%{location.id}" 						cssClass="selectwk"list="dropdownData.locationList" listKey="id" listValue='name'/>
				</td>
				</tr>


<script>
	function setupAjaxWards(elem){
   		 zone_id=elem.options[elem.selectedIndex].value;
    		populateassetward({zoneId:zone_id});
	}
	function setupAjaxAreas(elem){
   		 ward_id=elem.options[elem.selectedIndex].value;
   		 populateassetarea({wardId:ward_id});
    		populatestreet({wardId:ward_id});
	}
	function setupAjaxLoc(elem)
	{
   		 area_id=elem.options[elem.selectedIndex].value;
    		 populatelocation({areaId:area_id});
	}
</script>
