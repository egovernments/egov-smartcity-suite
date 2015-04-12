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
	var id = $("#boundaryTypeSelect").val();
	if(id ==''){
		alert('Please select the Boundary Type !');
		return false;
	}
	else{
		$.ajax({
			type: "GET",
			url: contextPath + "/controller/addChildBoundaryType/isChildPresent",
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

<<<<<<< Updated upstream
$('#boundaryTypeCreateBtn').click(function() {
	$('#boundaryTypeViewform').attr('method', 'get');
	$('#boundaryTypeViewform').attr('action', '/egi/controller/create-boundaryType');
})

$('#boundaryTypeUpdateBtn').click(function() {
	var url = '/egi/controller/boundaryType/update/'+ $('#boundaryTypeId').val();
	$('#boundaryTypeViewform').attr('method', 'get');
	$('#boundaryTypeViewform').attr('action', url);
})
=======
$('#buttonCreate').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val();
	$('#boundaryViewForm').attr('method', 'get');
	$('#boundaryViewForm').attr('action', '/egi/controller/create-boundary'+pathVars);
})

$('#buttonEdit').click(function() {
	var pathVars = "/" + $('#btnHierarchyType').val() + "," + $('#btnBoundaryType').val() + "," + $('#btnBoundary').val();
	$('#boundaryViewForm').attr('method', 'get');
	$('#boundaryViewForm').attr('action', '/egi/controller/update-boundary'+pathVars);
})
>>>>>>> Stashed changes
