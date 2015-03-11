var tableContainer1;
var tableContainer2;
var tableContainer3;
$(document).ready(function()
{	
	$('.cit-notify').show();
	
	$('.citizen').click(function(){
		
		$('.citizen-all').hide();
		
		if($(this).attr('data-title') == "show-notify"){
			$('.cit-notify').show();
			$('#hp-citizen-title').html('Notifications');
			}else if($(this).attr('data-title') == "show-penreq"){
			$('.cit-pen-req').show();
			$('#hp-citizen-title').html('Pending Request');
			}else if($(this).attr('data-title') == "show-newreq"){
			$('.cit-new-req').show();
			$('#hp-citizen-title').html('New Request');
			}else if($(this).attr('data-title') == "show-newreq1"){
			$('.cit-new-req1').show();
			$('#hp-citizen-title').html('New Request 1');
			}else if($(this).attr('data-title') == "show-account"){
			$('.cit-my-account').show();
			$('#hp-citizen-title').html('My Account');
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
	
	tableContainer3 = $("#prop_sample"); 
	
	tableContainer3.dataTable({
		"sPaginationType": "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"columns": [
		{ "width": "15%" },
		{ "width": "20%" },
		{ "width": "50%" },
		{ "width": "20%" }
		],
		"autoWidth": false
	});
	
	$('#prop_samplesearch').keyup(function(){
		tableContainer3.fnFilter(this.value);
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
	var service_list;
	
	service_list = '<a class="list-group-item open-popup" href="citizencomplaintview.html?status=closed"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">Register Grievance</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">View Grievance</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
	$('.service-list-group').html(service_list);
	
	
	$('a.bootcards-summary-item').click(function(){
		if($(this).attr('data-newrequest') == "grievance"){
			$('.service-title').html('Grievance Redressal');$('.service-list-group').html();
			service_list = '<a class="list-group-item open-popup" href="citizencomplaintview.html?status=closed"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">Register Grievance</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">View Grievance</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "property"){
			$('.service-title').html('Property Tax');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">File new Assessment</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">Pay property Tax</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">Link property to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-doc-text pull-left"></i> <h4 class="list-group-item-heading">Search Property</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "bpa"){
			$('.service-title').html('Building plan Approval');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Application for additional construction</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Application for demolition and reconstruction</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Application for demolition only</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Application for new building permit</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "bandeath"){
			$('.service-title').html('Birth and Death');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Apply for birth/death certificate</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Search for birth/death records</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Apply for name inclusion</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Record correction</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link birth/death record to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "tradelic"){
			$('.service-title').html('Trade Licence');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Apply for new Licence</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Renew Licence</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link licence to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "proftax"){
			$('.service-title').html('Professional Tax');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">File new assessment</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Pay professional tax</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link profession to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "comptax"){
			$('.service-title').html('Company Tax');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">File new assessment</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Pay company tax</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link company to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "shops"){
			$('.service-title').html('Shops');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Pay Fees</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link shop to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "advert"){
			$('.service-title').html('Advertisement');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Pay Fees</h4> <p class="list-group-item-text">For registering grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Link hoarding to my account</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
			}else if($(this).attr('data-newrequest') == "others"){
			$('.service-title').html('Others');
			service_list = '<a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Pay Fees</h4> <p class="list-group-item-text">Online payment for challans</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a> <a class="list-group-item" href="javascript:void(0)"> <div class="row"> <div class="col-sm-10 col-xs-10"> <i class="entypo-mobile pull-left"></i> <h4 class="list-group-item-heading">Apply for road cut</h4> <p class="list-group-item-text">For viewing grievances</p> </div> <div class="col-sm-2 col-xs-2"> <i class="entypo-right-open-mini pull-right"></i> </div> </div> </a>';
			$('.service-list-group').html(service_list);
		}
	});
	
	$('a.bootcards-summary-item.myaccount').click(function(){
		$('.hide-myaccount').hide();
		if($(this).attr('data-myaccount')== 'property'){
			$('#property-show').show();
		}else if($(this).attr('data-myaccount')== 'licence'){
			$('#licence-show').show();
		}else if($(this).attr('data-myaccount')== 'proftax'){
			$('#protax-show').show();
		}else if($(this).attr('data-myaccount')== 'shops'){
			$('#shops-show').show();
		}else if($(this).attr('data-myaccount')== 'hoardings'){
			$('#hoardings-show').show();
		}
	});
	
	$('#prop_sample tbody').on('click', 'tr', function () {
		if($(this).hasClass('apply-background'))
		{
			$(this).removeClass('apply-background');
		}else{
			$('#prop_sample tbody tr').removeClass('apply-background');
			$(this).addClass('apply-background');
		}
		
    } );
	
});