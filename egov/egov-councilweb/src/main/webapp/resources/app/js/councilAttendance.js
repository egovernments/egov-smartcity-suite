$(document).ready(function() {
	
	jQuery( ".dateval" ).datepicker({ 
   	 format: 'dd/mm/yyyy',
   	 autoclose:true,
        onRender: function(date) {
     	    return date.valueOf() < now.valueOf() ? 'disabled' : '';
     	  }
	  }).on('changeDate', function(ev) {
		  var electiondate = jQuery('#electionDate').val();
		  var oathdate = jQuery('#oathDate').val();
		  if(electiondate && oathdate){
			  DateValidation1(electiondate , oathdate);
		  }
		 
	  }).data('datepicker');
	
	function DateValidation1(start , end){
	    if (start != "" && end != "") {
			var stsplit = start.split("/");
			var ensplit = end.split("/");
			
			start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
			end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
			
			return ValidRange(start, end);
		}else{
			return true;
		}
	}
	
	$(".councilcommitmem").change(function(){  
    	$hiddenName=$(this).data('change-to');
    	console.log( $hiddenName );
    	if($(this).is(':checked')){
    		$('input[name="'+$hiddenName+'"]').val(true);
    	}else{
    		$('input[name="'+$hiddenName+'"]').val(false);
    	}
    });

	$("#committeechk").change(function(){  
		if($(this).is(':checked')){
			$('#councilcommittee')
	        .find('> tbody > tr > td:first-child > input[type="checkbox"]')
	        .prop('checked', true);
			setHiddenValue(true);
		}else{
			$('#councilcommittee')
	        .find('> tbody > tr > td:first-child > input[type="checkbox"]')
	        .prop('checked', false);
			setHiddenValue(false);
		}
	});
	

	$("#buttonSubmit").click(function(e){ 
			var chkbxLength = $('.councilcommitmem:checked').length;
			if(chkbxLength <= 0){
				bootbox.alert('Please select atleast one value');
				return false;
			}
			return true;
	});  
	

	jQuery('#btnsearch').click(function(e) {
			
			callAjaxSearch();
		});

	$('form').keypress(function (e) {
	    if (e.which == 13) {
	    	e.preventDefault();
	    	callAjaxSearch();
	    }
	}); 


	
});

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}


function callAjaxSearch() {
	drillDowntableContainer = jQuery("#resultTable");	
	
	jQuery('.report-section').removeClass('display-hide');
		reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/council/councilmeeting/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
				"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
				"autoWidth" : false,
				"oTableTools" : {
					"sSwfPath" : "../../../>${action}../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ 
					               {
						             "sExtends": "pdf",
				                     "sPdfMessage": "",
				                     "sTitle": "Council Meeting Attendance Report",
				                     "sPdfOrientation": "landscape"
					                },
					                {
							             "sExtends": "xls",
				                         "sPdfMessage": "Council Meeting Attendance Report",
				                         "sTitle": "Council Meeting Attendance Report"
						             },
						             {
							             "sExtends": "print",
				                         "sTitle": "Council Meeting Attendance Report"
						             }],
			},
				aaSorting: [],				
				columns : [ { 
					"data" : "meetingDate", "sClass" : "text-left"},{ 
					"data" : "committeeType", "sClass" : "text-left",
					"render": function ( data, type, row, meta ) {
						return '<a target="_new" onclick="openPopup(\'/council/councilmeeting/attendance/search/view/'+ row.id +'\')">'+data+'</a>' 
					  }
					},
					{"data" : "totCommitteMemCount", "sClass" : "text-center"},
					{"data" : "noOfMembersPresent", "sClass" : "text-center"},
					{"data" : "noOfMembersAbsent", "sClass" : "text-center"},
					{ "data" : null, "target":-1,
					    sortable: false,
					    "render": function ( data, type, full, meta ) {          	
				          	return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
					    }
					}
					,{ "data": "id", "visible":false }
					]				
			});
}

function openPopup(url)
{
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}

$("#resultTable").on('click','tbody tr td  .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),6);
	openPopup('/council/councilmeeting/attendance/search/view' + '/'+id);
});

function setHiddenValue(flag)
{
	
	$('.councilcommitmem').each(function(){
		$hiddenName=$(this).data('change-to');
		$('input[name="'+$hiddenName+'"]').val(flag);
	});
	
}

