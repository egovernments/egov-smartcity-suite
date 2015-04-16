var tableContainer1;
var tableContainer2;
$(document).ready(function()
{
	var map;
	
	function initialize() {
		//var myLatlng1 = new google.maps.LatLng(53.65914, 0.072050);
		
		var mapOptions = {
			zoom: 10,
			//center: myLatlng1,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(document.getElementById('govtviewmap'),
		mapOptions);
		
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(function (position) {
				initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
				map.setCenter(initialLocation);
			});
		}
	}
	initialize();
	
	tableContainer1 = $("#govt_complaints"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row add-border'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#govtcomplaintsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	tableContainer2 = $("#govt_workorder"); 
	
	tableContainer2.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#govtworkordersearch').keyup(function(){
		tableContainer2.fnFilter(this.value);
	});
});