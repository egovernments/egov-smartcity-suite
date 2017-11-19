/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces, 
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any 
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines, 
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

jQuery('#btnsearch').click(function(e) {
		
		callAjaxSearch();
	});

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
					url : "/council/councilmember/ajaxsearch/"+$('#mode').val(),      
					type: "POST",
					"data":  getFormData(jQuery('form'))
				},
				/*"fnRowCallback": function (row, data, index) {
						$(row).on('click', function() {
				console.log(data.id);
				window.open('/council/councilmember/'+ $('#mode').val() +'/'+data.id,'','width=800, height=600,scrollbars=yes');
			});
				 },*/
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
"data" : "electionWard", "sClass" : "text-left"} ,{ 
"data" : "designation", "sClass" : "text-left"} ,{ 
"data" : null,
"render": function ( data, type, full, meta ) {
	if(data.designation=='Co-Option'){
		return data.category;
	}else{
		return data.partyAffiliation;
	}
},
"sClass" : "text-left"} ,{ 
"data" : "name", "sClass" : "text-left"},{ 
"data" : "status", "sClass" : "text-left"}
,{ "data" : null, "target":-1,
	
    sortable: false,
    "render": function ( data, type, full, meta ) {
        var mode = $('#mode').val();
   	 if(mode == 'edit')
       	 return '<button type="button" class="btn btn-xs btn-secondary edit"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>';
        else
       	return '<button type="button" class="btn btn-xs btn-secondary view"><span class="glyphicon glyphicon-tasks"></span>&nbsp;View</button>';
    }
}
,{ "data": "id", "visible":false }
]				
			});
			}

$("#resultTable").on('click','tbody tr td  .view',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),6);
	window.open('/council/councilmember/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	
});

$("#resultTable").on('click','tbody tr td  .edit',function(event) {
	var id = reportdatatable.fnGetData($(this).parent().parent(),6);
	window.open('/council/councilmember/'+ $('#mode').val() +'/'+id,'','width=800, height=600,scrollbars=yes');
	
});
$(document).ready(function() {
	$( "#category" ).hide();
	$( "#dateofjoining" ).hide();
	
	showFieldsByDesignation($("#designation option:selected").text());
	
	$( "#designation" ).change(function() {
		var design = $( "#designation option:selected" ).text();
		showFieldsByDesignation(design);
	});
	
	
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

	function ValidRange(start, end) {
		var retvalue = false;
	    var startDate = Date.parse(start);
	    var endDate = Date.parse(end);
		
	    // Check the date range, 86400000 is the number of milliseconds in one day
	    var difference = (endDate - startDate) / (86400000 * 7);
	    if (difference < 0) {
			bootbox.alert("Oath date should be greater than Election date");
			$('#oathDate').val('').datepicker("refresh");
			
			} else {
			retvalue = true;
		}
	    return retvalue;
	}
	
var fileformatsinclude = ['pdf','jpeg','jpg','png','gif'];
	
	jQuery('.upload-file').change(function(e){
		/*validation for file upload*/
		myfile= jQuery( this ).val();
		var ext = myfile.split('.').pop();
        validate_file(fileformatsinclude, ext, jQuery(this));

		var fileInput = jQuery(this);
   		var maxSize = 2097152; //file size  in bytes(2MB)
		if(fileInput.get(0).files.length){
			var fileSize = this.files[0].size; // in bytes
			var charlen = (this.value.split('/').pop().split('\\').pop()).length;
			if(charlen > 50){
				bootbox.alert('Document name should not exceed 50 characters!');
				fileInput.replaceWith(fileInput.val('').clone(true));
				return false;			
			} 
			else if(fileSize > maxSize){
				bootbox.alert('File size should not exceed 2 MB!');
				fileInput.replaceWith(fileInput.val('').clone(true));
				return false;
			}			
		}
	});
	
	function validate_file(fileformat, ext, obj){
		if(jQuery.inArray(ext.toLowerCase(), fileformat) == -1){
            bootbox.alert("Supported file formats are "+fileformat+" only.");
            obj.val('');
            return false;
		}
	}
	
});

function showFieldsByDesignation(design)
{
	if(design == 'Co-Option'){
		$('.hide-input-fields').show();
		$('#dateofjoining').hide();
		$('#category').show();
		$('#party').hide();
		$('#dateOfJoining').hide();
		$('.toggle-madatory').find("span").removeClass( "mandatory" );
		$('.toggle-madatory-caste').find("span").addClass( "mandatory" );
		$('.addremoverequired').removeAttr( "required" );
		$('#category').attr("required","true");
		$('#caste').attr("required","true");
		
	} else if(design == 'Special Officer'){
		$( '#dateofjoining' ).show();
		$('#party').show();
		$('.hide-input-fields').hide();
		$('.date-toggle-mandatory').find("span").addClass( "mandatory" );
		$('.toggle-madatory').find("span").removeClass( "mandatory" );
		$('.toggle-madatory-caste').find("span").removeClass( "mandatory" );
		$('.addremoverequired').removeAttr( "required" );
		$('#caste').removeAttr( "required" );
	} else {
		$('.hide-input-fields').show();
		$('#dateofjoining').hide();
		$( "#party" ).show();
		$( "#category" ).hide();
		$('.toggle-madatory').find("span").addClass( "mandatory" );
		$('.addremoverequired').attr("required","true");
	}
}


jQuery('#emailId').blur(function(e) {
	var emailId = jQuery('#emailId').val();
	var objRegExp  = /^[a-z0-9]([a-z0-9_\-\.]*)@([a-z0-9_\-\.]*)(\.[a-z]{2,3}(\.[a-z]{2}){0,2})$/i;
    if(emailId!="")
      {
	      if(!objRegExp.test(emailId))
		      {
		      bootbox.alert('Please Enter Valid Email Address');
		      jQuery('#emailId').val('')
		      return false;
		      }
	      return true;
      }
    
});
