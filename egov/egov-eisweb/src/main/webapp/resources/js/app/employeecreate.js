$(document).ready(function(){
	
	// Instantiate the Bloodhound suggestion engine
	var designation = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/eis/employee/ajax/designations?designationName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (designation) {
					return {
						name: designation.name,
						value: designation.id
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	designation.initialize();
	// Instantiate the Typeahead UI
	$('#designationName').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: designation.ttAdapter()
	}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
		$("#designationId").val(data.value);    
    });
	
	
	function setDesignationId(obj) {
		$("#designationId").val(obj)
	}
	
	//Position auto-complete
	$("#designationName").blur(function(){
		$('#positionName').typeahead('destroy');
		var deptId = $("#deptId").val();
		var desigId =$("#designationId").val();
		var positions = new Bloodhound(
				{
					datumTokenizer : function(datum) {
						return Bloodhound.tokenizers
								.whitespace(datum.value);
					},
					queryTokenizer : Bloodhound.tokenizers.whitespace,
					remote : {
						url : '/eis/employee/ajax/positions?deptId='+ deptId+'&desigId='+desigId+'&positionName=%QUERY',
						filter : function(data) {
							// Map the remote source JSON array to a
							// JavaScript object array
							return $.map(data, function(position) {
								return {
									name: position.name,
									value: position.id
								};
							});
						}
					}
				});
		positions.initialize();
	$('#positionName').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
		}, {
		displayKey: 'name',
		source: positions.ttAdapter()
		}).on('typeahead:selected typeahead:autocompleted typeahead:matched', function(event, data){
			$("#positionId").val(data.value);    
	    });
	});
	
	
	function setPositionId(obj) {
		$("#positionId").val(obj)
	}
	
	$("#isHodYes").click(function () {
		$('#hodDeptDiv').show();
	});
	
	$("#isHodNo").click(function () {
		$('#hodDeptDiv').hide();
	});
	
});
