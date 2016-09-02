jQuery('#btnsearch').click(function(e) {
		
		callAjaxSearch();
	});

/*jQuery('#add-sumoto').click(function(e) {
	
	loadDepartmentlist();
});*/

$('form').keypress(function (e) {
    if (e.which == 13) {
    	e.preventDefault();
    	callAjaxSearch();
    }
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
					url : "/council/councilmeeting/searchmeeting-tocreatemom",      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth": false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [{"sExtends" : "xls"},
						           {"sExtends" :"pdf"},
						           {"sExtends" : "print"}]
				},
				aaSorting: [],				
				columns : [ { 
"data" : "committeeType", "sClass" : "text-left"} ,{ 
"data" : "meetingNumber", "sClass" : "text-left"} ,{ 
"data" : "meetingDate", "sClass" : "text-left"},{
"data" : "meetingLocation", "sClass" : "text-left"},{
"data" : "meetingTime", "sClass" : "text-left"}
,{ "data" : null, "target":-1,
	
    sortable: false,
    "render": function ( data, type, full, meta ) {
          	
          	return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;Create MOM</button>';
    }
}
,{ "data": "id", "visible":false }
]				
			});
			}



$("#resultTable").on('click','tbody tr td  .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),6);
	//window.open('/council/agenda/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	//window.open('/council/councilmeeting/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	window.open('/council/councilmom/new' + '/'+id,'','width=800, height=600,scrollbars=yes');
});


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

	
});


String.prototype.compose = (function (){
	   var re = /\{{(.+?)\}}/g;
	   return function (o){
	       return this.replace(re, function (_, k){
	           return typeof o[k] != 'undefined' ? o[k] : '';
	       });
	   }
}());

var tbody = $('#sumotoTable').children('tbody');
var table = tbody.length ? tbody : $('#sumotoTable');

var row = '<tr>'+
/* '<td><span class="sno">{{sno}}</span></td>'+ '<td><input type="text" class="form-control" data-unique name="meetingMOMs[{{idx}}].preamble.preambleNumber" {{readonly}} value="{{pnoTextBoxValue}}"/></td>'+*/
 
 '<td><select name="meetingMOMs[{{idx}}].preamble.department" class="form-control"> <option value="">Loading...</option></select></td>'+
 /*'<td><select multiple="multiple" name="meetingMOMs[{{idx}}].preamble.wardNumber" class="form-control"> <option value="">Loading...</option></select></td>'+*/
 '<td><input type="textarea" class="form-control" name="meetingMOMs[{{idx}}].preamble.gistOfPreamble" {{readonly}} value="{{gistTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="meetingMOMs[{{idx}}].preamble.sanctionAmount" {{readonly}} value="{{amountTextBoxValue}}"/></td>'+
 '<td><input type="text" class="form-control" name="meetingMOMs[{{idx}}].resolutionDetail" {{readonly}} value="{{amountTextBoxValue}}"/></td>'+
 '<td><select name="meetingMOMs[{{idx}}].resolutionStatus" class="form-control"><option value="">Loading...</option></select></td>'+
 /*'<td><input type="hidden" class="form-control" name="meetingMOMs[{{idx}}].preamble.id" {{readonly}} value="{{departmentId}}"/>'+*/
'</tr>';
jQuery('#add-sumoto').click(function(){
	$('.agenda-section').show();
	
	var idx=$(tbody).data('existing-len')+$(tbody).find('tr').length;
	var sno=idx+1;
	
	console.log('Row Idx', idx);
	
//Add row
	var row={
	       'sno': sno,
	       'idx': idx
	   };
	addRowFromObject(row);
	loadDepartmentlist("meetingMOMs["+idx+"].preamble.department");
	loadResolutionlist("meetingMOMs["+idx+"].resolutionStatus");
	loadWardnumberlist("meetingMOMs["+idx+"].preamble.wardNumber");
	///meetingMOMs[idx].resolutionStatus
});

//ajax call 
function loadDepartmentlist(selectBoxName){

console.log('selectboxName', selectBoxName);

 $.ajax({
		url: "/council/councilmom/departmentlist",     
		type: "GET",
		async: false,
		dataType: "json",
		success: function (response) {
		    console.log("success", response);
			$('select[name="'+selectBoxName+'"]').empty();
			$('select[name="'+selectBoxName+'"]').append($("<option value=''>Select </option>"));
			$.each(response.departmentLists, function(index, departmentLists) {
				$('select[name="'+selectBoxName+'"]').append($('<option>').val(departmentLists.id).text(departmentLists.name));
			});
		}, 
		error: function (response) {
			console.log("failed");
		}
	});
 
 
}

function loadResolutionlist(selectBoxNameResolution){

	 $.ajax({
			url: "/council/councilmom/resolutionlist",     
			type: "GET",
			async: false,
			dataType: "json",
			success: function (response) {
			    console.log("success", response);
				$('select[name="'+selectBoxNameResolution+'"]').empty();
				$('select[name="'+selectBoxNameResolution+'"]').append($("<option value=''>Select </option>"));
				$.each(response.resolutionLists, function(index, resolutionLists) {
					$('select[name="'+selectBoxNameResolution+'"]').append($('<option>').val(resolutionLists.id).text(resolutionLists.code));
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	 
	 
	}

function loadWardnumberlist(selectBoxNameWard){

	 $.ajax({
			url: "/council/councilmom/wardlist",     
			type: "GET",
			async: false,
			dataType: "json",
			success: function (response) {
			    console.log("success", response);
				$('select[name="'+selectBoxNameWard+'"]').empty();
				$('select[name="'+selectBoxNameWard+'"]').append($("<option value=''>Select </option>"));
				$.each(response.wardLists, function(index, wardLists) {
					$('select[name="'+selectBoxNameWard+'"]').append($('<option>').val(wardLists.id).text(wardLists.name));
				});
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	 
	 
	}

function addRowFromObject(rowJsonObj)
{
	console.log("Row composed", row.compose(rowJsonObj));
	table.append(row.compose(rowJsonObj));
}


/*function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}*/

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

function setHiddenValue(flag)
{
	
	$('.councilcommitmem').each(function(){
		$hiddenName=$(this).data('change-to');
		$('input[name="'+$hiddenName+'"]').val(flag);
	});
	
}

$(document).ready(function() {
    $(".councilcommitmem").change(function(){  
    	$hiddenName=$(this).data('change-to');
    	console.log( $hiddenName );
    	if($(this).is(':checked')){
    		$('input[name="'+$hiddenName+'"]').val(true);
    	}else{
    		$('input[name="'+$hiddenName+'"]').val(false);
    	}
    });

});

$("#buttonSubmit").click(function(e){ 
		var chkbxLength = $('.councilcommitmem:checked').length;
		if(chkbxLength <= 0){
			bootbox.alert('Please enter attendance details');
			return false;
		}
		return true;
});  