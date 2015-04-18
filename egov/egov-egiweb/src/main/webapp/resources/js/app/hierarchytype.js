$('#buttonCreate').click(function() {
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', '/egi/hierarchytype/create');
})

$('#buttonEdit').click(function() {
	var url = '/egi/hierarchytype/update/'+ $('#htName').val();
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', url);
})
