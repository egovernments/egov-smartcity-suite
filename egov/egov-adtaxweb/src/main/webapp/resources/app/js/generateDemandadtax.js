$(document).ready(function(){var prevdatatable;
var prevdatatable;

$('#search').click(function(e){
	oTable= $('#adtax_generatedem');
	
	if(prevdatatable)
	{
		prevdatatable.fnClearTable();
		$('#adtax_generatedem thead tr').remove();
	}
	
	//oTable.fnClearTable();
		prevdatatable = oTable.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"bDestroy": true,
		"ajax": "/adtax/hoarding/genareteDemand-list?"+$("#hoardingsearchform").serialize(),
		"columns" : [
{ "data": "hordingIdsSearchedByAgency","visible": false, "searchable": false },
					  { "data" : "advertisementNumber", "title":"Advertisement No."},
					  { "data" : "applicationNumber", "title": "Application No."},
					  { "data" : "applicationFromDate", "title": "Application Date"},
					  { "data" : "agencyName", "title": "Agency"},
					  { "data" : "", "title": "Actions","target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;GenerateDemand</button>&nbsp;'}

					  ],
					  "aaSorting": [[4, 'asc']] 
			});
	
	e.stopPropagation();
});

$("#adtax_generatedem").on('click','tbody tr td .collect-hoardingWiseFee',function(event) {
	var hoardingId = oTable.fnGetData($(this).parent().parent(),0);
	window.open("/adtax/hoarding/generateDemand/"+hoardingId, ''+hoardingId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')

});

});

