$schemeId = 0;
$subSchemeId = 0;
$fundSourceId = 0;

$(document).ready(function(){
	$schemeId = $('#schemeId').val();
	$subSchemeId = $('#subSchemeId').val();
	$fundSourceId = $('#fundSourceId').val();
	loadScheme($('#fund').val());
	loadSubScheme($schemeId);
	loadFundSource($subSchemeId);
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
	var functionName_typeahead = $('#function').typeahead({
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
		$('#fundSource').empty();
		$('#fundSource').append($('<option>').text('Select from below').attr('value', ''));
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
					var output = '<option value="">Select from below</option>';
					$.each(response, function(index, value) {
						var selected="";
						if($schemeId)
						{
							if($schemeId==value.id)
							{
								selected="selected";
							}
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
					var output = '<option value="">Select from below</option>';
					$.each(response, function(index, value) {
						var selected="";
						if($subSchemeId)
						{
							if($subSchemeId==value.id)
							{
								selected="selected";
							}
						}
						$('#subScheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});
		
	}
}

function loadFundSource(subSchemeId){
	if (!subSchemeId) {
		$('#fundSource').empty();
		$('#fundSource').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getfundsourcesbysubschemeid",
			data : {
				subSchemeId : subSchemeId
			},
			async : true
		}).done(
				function(response) {
					$('#fundSource').empty();
					$('#fundSource').append($("<option value=''>Select from below</option>"));
					var output = '<option value="">Select from below</option>';
					$.each(response, function(index, value) {
						var selected="";
						if($fundSourceId)
						{
							if($fundSourceId==value.id)
							{
								selected="selected";
							}
						}
						$('#fundSource').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});
		
	}
}

$('#fund').change(function () {
	$schemeId = "";
	$subSchemeId = "";
	$fundSourceId = "";
	loadScheme($('#fund').val());
});


$('#scheme').change(function () {
	loadSubScheme($('#scheme').val());
});

$('#subScheme').change(function () {
	loadFundSource($('#subScheme').val());
});