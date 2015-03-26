var tableContainer1;
var tableContainer2;
var tableContainer3;
$(document).ready(function()
{
	tableContainer1 = $("#contractor_complaints"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#contcomplaintsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	tableContainer2 = $("#contractor_tenders"); 
	
	tableContainer2.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#contendersearch').keyup(function(){
		tableContainer2.fnFilter(this.value);
	});
	
	tableContainer3 = $("#contractor_workorder"); 
	
	tableContainer3.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
	});
	
	$('#contwosearch').keyup(function(){
		tableContainer3.fnFilter(this.value);
	});
	
	$('.contractor').click(function(){
		
		$('.contractor-all').hide();
		
		if($(this).attr('data-title') == "show-complaints"){
			$('.contractor-complaints').show();
			$('#hp-contractor-title').html('Complaints');
			}else if($(this).attr('data-title') == "show-tenders"){
			$('.contractor-tenders').show();
			$('#hp-contractor-title').html('Tenders');
			}else if($(this).attr('data-title') == "show-worders"){
			$('.contractor-worders').show();
			$('#hp-contractor-title').html('Work Orders');
		}
	});
	
	
});

function openewindow(url)
{
	window.open(url,'mywindow','width=768,height=700');
}