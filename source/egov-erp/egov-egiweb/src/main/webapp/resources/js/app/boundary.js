function populateBoundaryTypes(dropdown) {
	populateboundaryTypeSelect({
		hierarchyTypeId : dropdown.value
	});
}

function populateBoundaries(dropdown) {
	populateboundaries({
		boundaryTypeId : dropdown.value
	});
}

$('#buttonView').click(function() {
	$('#boundarySearchForm').attr('action', 'list-boundaries');
})

$('#buttonCreate').click(function() {
	$('#boundarySearchForm').attr('action', 'create-boundary');
})

$('#buttonUpdate').click(function() {
	$('#boundarySearchForm').attr('action', 'update-boundary');
})

/**
 * Ajax validation to check for child boundary type for a parent  
 */
function checkForChild(){
	var id = $("#boundaryTypeSelect").val();
	if(id ==''){
		alert('Please select the Boundary Type !');
		return false;
	}
	else{
		$.ajax({
			type: "GET",
			url: "addChildBoundaryType/isChildPresent",
			data:{'parentId' : id },
			dataType: "json",
			success: function (response) {
				if(response == true){
					alert('Child already exists!');
					return false;
				}
				else{
					$("#boundaryTypeSearch").submit();
					return true;
				}
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}
} 

$('#boundaryTypeCreateBtn').click(function() {
	$('#boundaryTypeViewform').attr('method', 'get');
	$('#boundaryTypeViewform').attr('action', 'create-boundaryType');
})

$('#boundaryTypeUpdateBtn').click(function() {
	var url = 'boundaryType/update/'+ $('#boundaryTypeId').val();
	$('#boundaryTypeViewform').attr('method', 'get');
	$('#boundaryTypeViewform').attr('action', url);
})

$('#buttonCreate').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val();
	$('#boundaryViewForm').attr('method', 'get');
	$('#boundaryViewForm').attr('action', 'create-boundary'+pathVars);
})

$('#buttonEdit').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val() + "," + $('#btnBoundary').val();
	$('#boundaryViewForm').attr('method', 'get');
	$('#boundaryViewForm').attr('action', 'update-boundary'+pathVars);
})


function checkForRootNode() {
	var hierarchyTypeId = $('#hierarchyTypeSelect').val();
	var boundaryTypeId = $('#boundaryTypeSelect').val();
	
	$.ajax({
		type: "GET",
		url: "check-is-root",
		data: { 
			'hierarchyTypeId' : hierarchyTypeId,
			'boundaryTypeId' : boundaryTypeId
		},
		dataType: "json",
		success: function (response) {
			if(response == false){
				alert('Sorry! You can\'t create root for the Child Boundary!');
				return false;
			}
			return true;
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
}


