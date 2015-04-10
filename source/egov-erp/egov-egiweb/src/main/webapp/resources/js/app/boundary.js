/**
 * 
 */


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

function showBoundariesDiv() {
	$('#boundariesDiv').show();
}

$('#buttonView').click(function() {
	$('#boundarySearchForm').attr('action', '/egi/controller/list-boundaries');
})

$('#buttonCreate').click(function() {
	$('#boundarySearchForm').attr('action', '/egi/controller/create-boundary');
})

$('#buttonUpdate').click(function() {
	$('#boundarySearchForm').attr('action', '/egi/controller/update-boundary');
})

/**
 * Ajax validation to check for child boundary type for a parent  
 */
function checkForChild(){
	var name = $("#boundaryTypes").val();
	
	$.ajax({
		type: "GET",
		url: contextPath + "/controller/addChildBoundaryType/isChildPresent",
		data:{'parentName' : name },
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