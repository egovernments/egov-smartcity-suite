var $schemeId = 0;
var $subSchemeId = 0;
var $fundSourceId = 0;
var $fundId = 0;
$(document).ready(function(){
	$schemeId = $('#schemeId').val();
	$subSchemeId = $('#subSchemeId').val();
	$fundSourceId = $('#fundSourceId').val();
	$fundId = $('#fundId').val();
	if($fundId)
		$("#fund").val($fundId).prop('selected','selected');
	if($fundSourceId)
		$("#fundSource").val($fundSourceId).prop('selected','selected');
	loadScheme($('#fund').val());
	loadSubScheme($schemeId);
	var functionName = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/EGF/common/ajaxfunctionnames?name=%QUERY',
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						code:ct.split("~")[0].split("-")[0],
						name : ct.split("~")[0].split("-")[1],
						id : ct.split("~")[1],
						codeName:ct
					};
				});
			}
		}
	});

	functionName.initialize();
$('#function').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'codeName' ,
		source : functionName.ttAdapter()
	}).on('typeahead:selected', function (event, data) {
		$(".cfunction").val(data.id);
	});
	
});
$('#function').blur(function () {
	if($('.cfunction').val()=="")
	{
		 bootbox.alert("Please select function from dropdown values",function() {
			 	$('#function').val("");
			});
	}
});

function loadScheme(fundId){
	if (!fundId) {
		$('#scheme').empty();
		$('#scheme').append($('<option>').text('Select from below').attr('value', ''));
		$('#subScheme').empty();
		$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getschemesbyfundid",
			data : {
				fundId : fundId
			},
			async : true
		}).done(
				function(response) {
					$('#scheme').empty();
					$('#scheme').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($schemeId && $schemeId==value.id)
						{
								selected="selected";
						}
						$('#scheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});

	}
}

function loadSubScheme(schemeId){
	if (!schemeId) {
		$('#subScheme').empty();
		$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getsubschemesbyschemeid",
			data : {
				schemeId : schemeId
			},
			async : true
		}).done(
				function(response) {
					$('#subScheme').empty();
					$('#subScheme').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($subSchemeId && $subSchemeId==value.id)
						{
								selected="selected";
						}
						$('#subScheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});
		
	}
}

$('#fund').change(function () {
	$schemeId = "";
	$subSchemeId = "";
	$('#scheme').empty();
	$('#scheme').append($('<option>').text('Select from below').attr('value', ''));
	$('#subScheme').empty();
	$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
	loadScheme($('#fund').val());
});


$('#scheme').change(function () {
	$('#subScheme').empty();
	$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
	loadSubScheme($('#scheme').val());
});