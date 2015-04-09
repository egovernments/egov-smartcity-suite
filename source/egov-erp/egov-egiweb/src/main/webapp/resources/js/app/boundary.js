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

