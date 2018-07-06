/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
var _URL = window.URL || window.webkitURL;
$(document)
		.ready(
				function() {

					var latLang = ($("#eventLocation").val()).split(":");

					var map, geocoder;
					var lat, lng, address;
					var myCenter = new google.maps.LatLng(latLang[0],
							latLang[1]);

					function initialize() {

						// marker=new google.maps.Marker();

						// mapprop();
						var latLng, mapOptions = {
							zoom : 10,
							timeout : 500,
							mapTypeControl : false,
							navigationControl : false,
						};

						var userLocationFound = function(position) {
							latLng = {
								lat : latLang[0],
								lng : latLang[1]
							};
							// Set current locaion to map
							var userLatLng = new google.maps.LatLng(latLng.lat,
									latLng.lng);
							lat = latLng.lat;
							lng = latLng.lng;

							getAddress(lat, lng);

							map.setCenter(userLatLng);

							mapcenterchangeevent();

						}

						var userLocationNotFound = function() {
							var citylat, citylng;
							// Assign static point to map
							if (!citylat || !citylng) {
								citylat = 20.5937;
								citylng = 78.9629;
							}

							latLng = {
								lat : citylat, // fallback lat
								lng : citylng
							// fallback lng
							};
							setlatlong(citylat, citylng);
							mapcenterchangeevent();

						}

						geocoder = new google.maps.Geocoder();
						map = new google.maps.Map(document
								.getElementById("normal"), mapOptions);

						var GeoMarker = new GeolocationMarker(map);
						$('<div/>').addClass('centerMarker').appendTo(
								map.getDiv());

						navigator.geolocation.getCurrentPosition(
								userLocationFound, userLocationNotFound,
								mapOptions);

						setTimeout(function() {
							if (!latLng) {
								userLocationNotFound();
							}
						}, mapOptions.timeout + 500); // Wait extra second

						searchBar(map);

					}

					function searchBar(map) {

						var input = (document.getElementById('pac-input'));

						var autocomplete = new google.maps.places.Autocomplete(
								input);
						autocomplete.bindTo('bounds', map);

						autocomplete
								.addListener(
										'place_changed',
										function() {
											var place = autocomplete.getPlace();
											if (!place.geometry) {
												window
														.alert("Autocomplete's returned place contains no geometry");
												return;
											}

											// If the place has a geometry, then
											// present it on a map.
											if (place.geometry.viewport) {
												map
														.fitBounds(place.geometry.viewport);
											} else {
												map
														.setCenter(place.geometry.location);
												map.setZoom(17); // Why 17?
												// Because
												// it looks
												// good.
											}

											var address = '';
											if (place.address_components) {
												address = [
														(place.address_components[0]
																&& place.address_components[0].short_name || ''),
														(place.address_components[1]
																&& place.address_components[1].short_name || ''),
														(place.address_components[2]
																&& place.address_components[2].short_name || '') ]
														.join(' ');
											}

										});

					}

					function mapcenterchangeevent() {
						google.maps.event.addListener(map, 'center_changed',
								function() {
									var location = map.getCenter();
									getAddress(location.lat(), location.lng());
								});
					}

					function setlatlong(citylat, citylng) {
						var userLatLng = new google.maps.LatLng(citylat,
								citylng);
						lat = citylat;
						lng = citylng;

						getAddress(lat, lng);

						map.setCenter(userLatLng);
					}

					function getAddress(lat, lng) {
						var geocoder = new google.maps.Geocoder;
						geocoder.geocode({
							'location' : {
								lat : lat,
								lng : lng
							}
						}, function(results, status) {
							if (status === 'OK') {
								if (results[0]) {
									address = results[0].formatted_address;
								}
							}
						});
					}

					google.maps.event
							.addDomListener(window, 'load', initialize);

					google.maps.event.addDomListener(window, "resize",
							resizingMap());

					$('#modal-6').on('show.bs.modal', function() {
						// Must wait until the render of the modal appear, thats
						// why we use the resizeMap and NOT resizingMap!! ;-)
						// complaint registration map
						resizeMap();
					});

					function resizeMap() {
						if (typeof map == "undefined")
							return;
						setTimeout(function() {
							resizingMap();
						}, 400);
					}

					function resizingMap() {
						if (typeof map == "undefined")
							return;
						var center = map.getCenter();
						google.maps.event.trigger(map, "resize");
						map.setCenter(center);
					}

					$('.btn-save-location').click(function() {
						var location = map.getCenter();
						lat = location.lat();
						lng = location.lng();
						var geocoder = new google.maps.Geocoder;
						geocoder.geocode({
							'location' : {
								lat : lat,
								lng : lng
							}
						}, function(results, status) {
							if (status === 'OK') {
								if (results[0]) {
									address = results[0].formatted_address;
									// $('#location').typeahead('val', address);
									$('#address').val(address);
								}
							}
						});
						$("#lat").val(lat);
						$("#lng").val(lng);
						$("#eventLocation").val(lat + ':' + lng);
					});

					if ($("#paidHid").val() === "Yes") {
						$("#paid")[0].checked = true;
						$("#costLabel").show();
						$("#costDiv").show();
					} else {
						$("#paid")[0].checked = false;
					}

					$("#paid").on("change", function(event) {
						// alert(($(this).is(':checked')));
						if ($(this).is(':checked')) {
							// $(this).trigger("change");
							$("#costLabel").show();
							$("#costDiv").show();
						} else {
							$("#costLabel").hide();
							$("#costDiv").hide();
						}
						event.preventDefault();
					});

					$(".btn-primary")
							.click(
									function(event) {

										if ($("form").valid()) {
											if ($("#mode").val() === "update") {
												var start = $("#startDt").val();
												var end = $("#endDt").val();
												var stsplit = start.split("/");
												var ensplit = end.split("/");

												start = stsplit[1] + "/"
														+ stsplit[0] + "/"
														+ stsplit[2];
												end = ensplit[1] + "/"
														+ ensplit[0] + "/"
														+ ensplit[2];
												if (!validateStartDateAndEndDate(
														start, end)) {
													return false;
												}
												var startHH = $("#startHH")
														.val();
												var startMM = $("#startMM")
														.val();
												var endHH = $("#endHH").val();
												var endMM = $("#endMM").val();
												if (validateEqualStartDateAndEndDate(
														start, end)
														&& startHH === endHH
														&& startMM === endMM) {
													bootbox
															.alert("Invalid date time range. Start Date, Start Time cannot be equal to End Date, End Time!");
													$("#endHH").val("");
													$("#endMM").val("");
													return false;
												}

												if (jQuery("#paid").is(
														":checked")) {
													if ($("#cost").val() == "") {
														bootbox
																.alert("Please provide cost!");
														$("#cost").val("");
														return false;
													}
												}
											}

											document.getElementById(
													"updateEventform").submit();
										} else {
											event.preventDefault();
										}
										return true;
									});

					$("input[type=file]")
							.change(
									function() {
										var val = $(this).val().toLowerCase(), regex = new RegExp(
												"(.*?)\.(jpg|jpeg|bmp|gif|png)$");

										if (!(regex.test(val))) {
											$(this).val("");
											bootbox
													.alert("Sorry, "
															+ $(this).val()
															+ " is invalid, allowed extensions are: jpg, jpeg, bmp, gif or png");
										}
										var img = new Image();
										img.src = _URL
												.createObjectURL($(this)[0].files[0]);
										img.onload = function() {
											if (this.width < 100
													|| this.height < 100) {
												$(this).val("");
												bootbox
														.alert("Your image is too small, it must be equal to or more than 100x100");
											}
										}
									});

				});

function validateStartDateAndEndDate(start, end) {
	var startDate = Date.parse(start);
	var endDate = Date.parse(end);
	if (startDate > endDate) {
		bootbox
				.alert("Invalid date range. Start Date cannot be after End Date!");
		$("#endDate").val("");
		return false;
	} else {
		return true;
	}
}

function validateEqualStartDateAndEndDate(start, end) {
	var startDate = Date.parse(start);
	var endDate = Date.parse(end);
	if (startDate === endDate) {
		return true;
	} else {
		return false;
	}
}

function checkcreateform() {
	var val = $("#file").val().toLowerCase(), regex = new RegExp(
			"(.*?)\.(jpg|jpeg|bmp|gif|png)$");
	if (!(regex.test(val))) {
		$("#file").val("");
		bootbox
				.alert("Sorry, "
						+ $(this).val()
						+ " is invalid, allowed extensions are: jpg, jpeg, bmp, gif or png");
		return false;
	}
	var img = new Image();
	img.src = URL.createObjectURL($("#file")[0].files[0]);
	img.onload = function() {
		if (this.width < 100 || this.height < 100) {
			$("#file").val("");
			bootbox
					.alert("Your image is too small, it must be equal to or more than 100x100");
			return false;
		}

		return true;
	}
}

// values to our textfields so that we can save the location.
function markerLocation() {
	// Get location.
	var currentLocation = marker.getPosition();
	// Add lat and lng values to a field that we can save.
	console.log(currentLocation.lat());
	console.log(currentLocation.lng());
	$("#lat").val(currentLocation.lat());
	$("#lng").val(currentLocation.lng());
	$("#eventLocation")
			.val(currentLocation.lat() + ':' + currentLocation.lng());
}