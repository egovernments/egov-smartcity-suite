$('#buttonCreate').click(function() {
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', 'create');
})

$('#buttonEdit').click(function() {
	var url = 'update/'+ $('#htName').val();
	$('#hierarchyTypeViewForm').attr('method', 'get');
	$('#hierarchyTypeViewForm').attr('action', url);
})