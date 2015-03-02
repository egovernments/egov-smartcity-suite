var tableContainer1;
var tableContainer2;
var tableContainer3;
$(document).ready(function()
{	
	$('.cit-pen-req').show();
	
	$('.citizen').click(function(){
		
		$('.citizen-all').hide();
		
		if($(this).attr('data-title') == "show-newreq"){
			$('.cit-new-req').show();
			}else if($(this).attr('data-title') == "show-notify"){
			$('.cit-notify').show();
			}else if($(this).attr('data-title') == "show-penreq"){
			$('.cit-pen-req').show();
			}else if($(this).attr('data-title') == "show-newreq2"){
			$('.cit-new-req2').show();
			}
	});
	
	tableContainer1 = $("#citizen_notification"); 
	
	tableContainer1.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"columns": [
		{ "width": "10%" },
		{ "width": "20%" },
		{ "width": "30%" },
		{ "width": "40%" }
		],
		"autoWidth": false
	});
	
	$('#notifysearch').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});
	
	tableContainer2 = $("#citizen_penreq"); 
	
	tableContainer2.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"columns": [
		{ "width": "15%" },
		{ "width": "20%" },
		{ "width": "15%" },
		{ "width": "20%" },
		{ "width": "30%" }
		],
		"autoWidth": false
	});
	
	$('#penreqsearch').keyup(function(){
		tableContainer2.fnFilter(this.value);
	});
	
	$('#service_category').change(function(){
		service1.changeservice($('#service_category').val());
	});
	
	
	var service = function () {
		console.log('instance created');
	};
	
	service.prototype.changeservice = function(elem) {
		if(elem == "1"){
			$('#service').html('<option value="">Register Grievance</option> <option value="">View Complaint</option> ');
		}else if(elem == "2")
		{
			$('#service').html('<option value="">File New Assessment</option> <option value="">Pay Property Tax</option> <option value="">Link Property to My Account</option> <option value="">Search Property</option>');
			}else if(elem == "3"){
			$('#service').html('<option value="">Application for Additional Construction</option> <option value="">Application for Demolition and Reconstruction</option> <option value="">Application for Demolition only</option> <option value="">Application for New Building permit</option> <option value="">Application for Reclassification</option>');
			}else if(elem == "4"){
			$('#service').html('<option value="">Apply for Birth/Death Certificate</option> <option value="">Search for Birth/Death Records</option> <option value="">Apply for Name Inclusion</option> <option value="">Record Correction</option> <option value="">Link Birth/Death Record to My Account</option>');
			}else if(elem == "5"){
			$('#service').html('<option value="">Apply for a New License</option> <option value="">Renew License</option> <option value="">Link License to My Account</option>');
			}else if(elem == "6"){
			$('#service').html('<option value="">File New Assessment</option> <option value="">Pay Professional Tax</option> <option value="">Link Profession to My Account</option>');
			}else if(elem == "7"){
			$('#service').html('<option value="">File New Assessment</option> <option value="">Pay Company Tax</option> <option value="">Link Company to My Account</option>');
			}else if(elem == "8"){
			$('#service').html('<option value="">Pay Fees</option> <option value="">Link Shop to My Account</option>');
			}else if(elem == "9"){
			$('#service').html('<option value="">Pay Fees</option> <option value="">Link Hoarding to My Account</option>');
			}else if(elem == "10"){
			$('#service').html('<option value="">Online Payment for Challans</option> <option value="">Apply for Road Cut</option>');
		}
	};
	
	var service1 = new service();
	
	/* $('a[target^="_new"]').click(function(e) {
		// to open in good size for user
		var width = window.innerWidth /0.66 ; 
		// define the height in 
		var height = width * window.innerWidth / window.innerHeight; 
		// Ratio the hight to the width as the user screen ratio
		window.open(this.href, 'newwindow', 'width=900, height=700, top=300, left=350');
		return false;
		
	}); */
	
});