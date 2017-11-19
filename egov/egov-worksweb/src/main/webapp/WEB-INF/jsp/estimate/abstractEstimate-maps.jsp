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
<html>
<title><s:text name='page.title.estimate'/></title>
<script src="../resources/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=${sessionScope.googleApiKey}"></script>
<script type="text/javascript" src="/egworks/resources/js/gmap3.min.js"></script>
<script type="text/javascript">
var MODE = '<s:property value="%{mapMode}" />';
jQuery.noConflict();
jQuery(document).ready(function() {
	generatemaps();
});

function getcurrentlats(){
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successFunction, errorFunction);
	} else {
 		alert('It seems like Geolocation, which is required for this page, is not enabled in your browser. Please use a browser which supports it.');
	}
}
function errorFunction(){
	alert("Error");
}

function successFunction(position) {
 jQuery('#latitude').val(position.coords.latitude);
 jQuery('#longitude').val(position.coords.longitude);
 createMap(document.getElementById('latitude').value,document.getElementById('longitude').value,true);
}

function returnBackToParent(mode) { 
	var values = new Array();
	if(mode=='clear')
	{
		window.opener.updateLocation(null);
	}
	else
	{
		var latVal=document.getElementById("latitude").value; 
		var longVal=document.getElementById("longitude").value;
		var addrVal = document.getElementById("address").value;
		values[0]=latVal;
		values[1]=longVal;
		values[2]=addrVal;
		window.opener.updateLocation(values);
	}		
	window.close();
}

function generatemaps() {

	var estLat = '<s:property value="%{latitude}" />';
	var estLong = '<s:property value="%{longitude}" />';
	if(estLat=='' && estLong=='')
	{
		getcurrentlats();
	}
	else
	{
		if(MODE=='edit')
			createMap(estLat,estLong,true);
		else
			createMap(estLat,estLong,false);
	}		
}
function createMap(paramLat,paramLong,paramDraggable)
{
	jQuery('#map_canvas')
	.gmap3({
				action : 'init',
				options : {
					center : [ paramLat, paramLong ],
					zoom : 14
				}
			},
			{
				action : 'addMarkers',
				markers : [ {
					lat : paramLat,
					lng : paramLong
				} ],
				marker : {
					options : {
						draggable : paramDraggable
					},
					events : {
						dragend : function(marker) {

							var position = marker.getPosition();
							var lat = position.lat();
							var lng = position.lng();
							jQuery("#latitude").val(lat);
							jQuery("#longitude").val(lng);
							
							jQuery(this)
									.gmap3(
											{
												action : 'getAddress',
												latLng : [ lat, lng ],
												callback : function(
														results) {
													var map = jQuery(
															this)
															.gmap3(
																	'get'), infowindow = jQuery(
															this)
															.gmap3(
																	{
																		action : 'get',
																		name : 'infowindow'
																	}), content = results
															&& results[1] ? results
															&& results[1].formatted_address
															: 'no address';
															jQuery('#address').val(content);
													if (infowindow) {
														infowindow
																.open(
																		map,
																		marker);
														infowindow
																.setContent(content);
																
													} else {
														jQuery(this)
																.gmap3(
																		{
																			action : 'addinfowindow',
																			anchor : marker,
																			options : {
																				content : content
																			}
																		});
													}
												}
											});
						}
					}
				}
			});
			
			jQuery("#map_canvas").gmap3({
				action:'getAddress',
				latLng: [paramLat, paramLong],
				callback:function(results){
				content = results && results[1] ? results && results[1].formatted_address : 'No Address';
				jQuery('#address').val(content);
				}
			});
			var zonelayerUrl = '<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()%>'+'/images/coczone.kml';
			jQuery('#map_canvas').gmap3(
					  { action: 'addKmlLayer',
					    url: zonelayerUrl,
					    options:{
					      suppressInfoWindows: true
					    },
					    tag:'chk1'
					  }
					);	
}
</script>
<title>Abstract Estimate</title>
<body >
	<s:form name="estimateMaps">
		<s:push value="model">
		<s:hidden id="latitude" name="latitude" />
		<s:hidden id="longitude" name="longitude" />
		<s:hidden id="address" name="address" />
		<div id="map_canvas" style="position: relative; width: 95%; height: 70%; z-index: 0; margin:0 auto;"  ></div>
		<br/>
		<div align="center" style="position: relative;  z-index: 0;">
			<s:if test="%{mapMode=='edit'}">
				<input type="button" class="btn btn-primary"  value="Capture" id="captureButton" name="captureButton" onclick="returnBackToParent('')"/>&nbsp;
				<input type="button" class="btn btn-default" value="Clear" id="clearButton" name="clearButton" onclick="returnBackToParent('clear')"/>&nbsp;
			</s:if>	
			<input type="button" class="btn btn-default"  value="Close" id="closeButton" name="closeButton" onclick="window.close();"/>
		</div>
		</s:push>
	</s:form>
</body>
</html>
