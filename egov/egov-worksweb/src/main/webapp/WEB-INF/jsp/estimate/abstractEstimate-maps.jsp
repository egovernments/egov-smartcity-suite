<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<html>
<title><s:text name='page.title.estimate'/></title>
<script src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="/egworks/js/gmap3.min.js"></script>
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
	 ///jQuery('#latitude').val('13.078364');
	 ///jQuery('#longitude').val('80.251694');
	 ///createMap(document.getElementById('latitude').value,document.getElementById('longitude').value,true);
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
		<div id="map_canvas" style="position: relative; left: 0px; top: 0px; width: 100%; height: 90%; z-index: 0;"  ></div>
		<br/>
		<div align="center" style="position: relative;  z-index: 0;">
			<s:if test="%{mapMode=='edit'}">
				<input type="button"  value="CAPTURE" id="captureButton" name="captureButton" onclick="returnBackToParent('')"/>
				<input type="button"  value="CLEAR" id="clearButton" name="clearButton" onclick="returnBackToParent('clear')"/>
			</s:if>	
			<input type="button"  value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
		</div>
		</s:push>
	</s:form>
</body>
</html>