$('#buttonCreate').click(function() {
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', '/egi/controller/hierarchy-type/create');
})

$('#buttonEdit').click(function() {
	var url = '/egi/controller/hierarchy-type/update/'+ $('#htName').val();
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', url);
})