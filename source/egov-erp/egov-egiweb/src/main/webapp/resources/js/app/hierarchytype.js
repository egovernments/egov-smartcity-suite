$('#buttonCreate').click(function() {
	$('#hierarchyTypeViewForm').attr('action', '/egi/hierarchy-type/create');
})

$('#buttonEdit').click(function() {
	var url = '/egi//hierarchy-type/update/'+ $('#htName').val();
	$('#hierarchyTypeViewForm').attr('action', url);
})