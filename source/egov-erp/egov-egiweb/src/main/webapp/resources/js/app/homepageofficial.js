$(document).ready(function()
{	
	tableContainer1 = $("#official_inbox"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"ajax": "inbox",
        "columns": [
            { "data": "date","width": "20%" },
            { "data": "sender","width": "15%" },
            { "data": "task","width": "20%" },
            { "data": "status","width": "20%" },
            { "data": "details","width": "20%" },
            { "data": "id","visible": false, "searchable": false },
            { "data": "link","visible": false, "searchable": false }
        ]
	});
	
	$('#inboxsearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	$("#official_inbox tbody").on('click','tr',function(event) {
		window.open(tableContainer1.fnGetData(this,6),'_blank','width=760,height=650');
	});
});
/*
 * { "data":[{"id":"30#1","sender":"Unknown / Admin","date":"09/04/2015 05:22 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-tj3h6","link":"/pgr/complaint-update?id\u003d48"},{"id":"23#1","sender":"Unknown / Admin","date":"31/03/2015 08:50 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-Y0QBo","link":"/pgr/complaint-update?id\u003d41"},{"id":"22#1","sender":"Unknown / Admin","date":"31/03/2015 07:31 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-yvyjm","link":"/pgr/complaint-update?id\u003d40"},{"id":"21#1","sender":"Unknown / Admin","date":"31/03/2015 07:24 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-57xOq","link":"/pgr/complaint-update?id\u003d39"},{"id":"20#1","sender":"Unknown / Admin","date":"31/03/2015 07:17 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-3zZYS","link":"/pgr/complaint-update?id\u003d38"},{"id":"19#1","sender":"Unknown / Admin","date":"31/03/2015 11:03 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-6RNTy","link":"/pgr/complaint-update?id\u003d37"},{"id":"18#1","sender":"Unknown / Admin","date":"31/03/2015 10:31 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-bGOMD","link":"/pgr/complaint-update?id\u003d36"},{"id":"17#1","sender":"Unknown / Admin","date":"31/03/2015 10:18 AM","task":"Complaint","status":"Registered","details":"CRN : CRN-OlTX4","link":"/pgr/complaint-update?id\u003d33"},{"id":"15#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-K5O10","link":"/pgr/complaint-update?id\u003d30"},{"id":"13#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-LmxHD","link":"/pgr/complaint-update?id\u003d26"},{"id":"14#1","sender":"Unknown / Admin","date":"30/03/2015 07:34 PM","task":"Complaint","status":"Registered","details":"CRN : CRN-XpTSH","link":"/pgr/complaint-update?id\u003d27"}]}
 */