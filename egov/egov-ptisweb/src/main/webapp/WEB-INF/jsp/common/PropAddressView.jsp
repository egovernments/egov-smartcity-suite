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

<%@ include file="/includes/taglibs.jsp" %>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCgxY6DqJ4TxnRfKjlZR8SfLSQRtOSTxEU"></script>
<script type="text/javascript" src="<cdn:url value='/resources/js/app/property-map.js?rnd=${app_release_no}'/>"></script>
<div id="PropAddressDiv">
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold"><s:text name="PropertyAddress"/></span></div></td>
	</tr>
	
  <tr>
  	<td class="greybox">&nbsp;</td>
  	<td class="greybox"><s:text name="locality"/> : </td>
	<td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.locality.name}" /> </span>
	</td>
    <td class="greybox" colspan="2">&nbsp;</td>
  </tr>

	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="zone"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.zone.name}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="revwardno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.ward.name}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="blockno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.area.name}" /> </span></td>
	    <td class="greybox" width="10%"><s:text name="Street"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.Street.name}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="elec.wardno"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.propertyID.electionBoundary.name}" /> </span></td>
	    <td class="greybox"><s:text name="doorno"></s:text></td>
	    <td class="greybox">
	    	<span class="bold">
	    		<s:if test='%{houseNumber == ""}'>N/A</s:if>
	    		<s:else><s:property default="N/A" value="%{houseNumber}"/></s:else>
	    	</span>
	    </td>	
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="enumerationblock"/> : </td>
	    <td class="greybox"><span class="bold">N/A</td>
	    <td class="greybox"><s:text name="PinCode"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{pinCode}" /> </span></td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
	    <td class="greybox"><s:text name="longitude"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.longitude}" /> </span></td>
	    <td class="greybox"><s:text name="latitude"/> : </td>
	    <td class="greybox"><span class="bold"><s:property default="N/A" value="%{basicProperty.latitude}" /> </span></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td class="greybox"><s:text name="parcelid"/> : </td>
	    <s:if test="%{basicProperty.parcelId != null && !basicProperty.parcelId.isEmpty()}">
	    	<td class="greybox"><span class="bold"><s:property value="%{basicProperty.parcelId}" /> </span></td>
	    </s:if>
	    <s:else>
	    	<td class="greybox"><span class="bold">NA</span></td>
	    </s:else>
	    <s:if test="%{basicProperty.latitude != null && basicProperty.longitude != null}">
			<td>&nbsp;</td>
			<td><input type="button" name="showMap" id="show-map"
				value="View On Map" class="buttonsubmit" data-toggle="modal" data-target="#myModal" /></td>
		</s:if>
	</tr>
</div>
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Property On Google Map</h4>
      </div>
			<div id="map-canvas" style="height:500px;width:500pxpx;"></div>      
			<div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>
<script type="text/javascript">
	var lat = parseFloat('<s:property value="%{basicProperty.latitude}"/>');
    var lng = parseFloat('<s:property value="%{basicProperty.longitude}"/>');
	jQuery('#show-map').on('click',initialize(lat, lng));
</script>

