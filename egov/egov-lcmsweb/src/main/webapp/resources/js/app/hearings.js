/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
$(document).ready(function() {
	$('#buttonid').click(function(){
	if(!validateHearingDate())
	{
	return false;
	}else{
		document.forms["hearingsform"].submit();
	}
	});
	var assignPosition = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers
					.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/lcms/ajax/positions',
			replace : function(url, uriEncodedQuery) {
				return url + '?positionName=' + uriEncodedQuery
						+ '&departmentName='
						+ $("#departmentName").val();

			},
			filter : function(data) {
				return $.map(data, function(position) {
					return {
						name : position.name,
						value : position.id

					};
				});
			}
		}
	});
	
	assignPosition.initialize();
	var typeaheadobj = $('#positionName').typeahead({
		hint : false,
		highlight : false,
		minLength : 1
	}, {
		displayKey : 'name',
		source : assignPosition.ttAdapter()
	});
	$('#btnclose').click(function(){
		bootbox.confirm({
		    message: 'Information entered in this screen will be lost if you close this page ? Please confirm if you want to close. ',
		    buttons: {
		        'cancel': {
		            label: 'No',
		            className: 'btn-default pull-right'
		        },
		        'confirm': {
		            label: 'Yes',
		            className: 'btn-danger pull-right'
		        }
		    },
		    callback: function(result) {
		        if (result) {
		             window.close();
		        }
		    }
		});
					
   });
	$(document).on('click',"#emp_delete_row",function (){
		var table = document.getElementById('employeeDetails');
	    var rowCount = table.rows.length;
	    var counts = rowCount;
	    var k = 2;
	    var m;
			$(this).closest('tr').remove();		
			
			jQuery("#employeeDetails tr:eq(1) td span[alt='AddF']").show();
			//starting index for table fields
			var idx=0;
			
			//regenerate index existing inputs in table row
			jQuery("#employeeDetails tr:not(:first)").each(function() {
				jQuery(this).find("input, select").each(function() {
				   jQuery(this).attr({
				      'id': function(_, id) {  
				    	  return id.replace(/\[.\]/g, '['+ idx +']'); 
				       },
				      'name': function(_, name) {
				    	  return name.replace(/\[.\]/g, '['+ idx +']'); 
				      },
				   });
				  });
				
				idx++;
			});
	});
	$("#addid").click(function(){
		addResRow();
	});
	
	var  rowobj = '<tr class=""> <td class="text-right"><input type="text" class="form-control table-input text-left" data-pattern="alphanumerichyphenbackslash" name="positionTemplList[0].name" id="positionTemplList[0].name" maxlength="50"></td> <td class="text-center"><a href="javascript:void(0);" class="btn-sm btn-default" id="emp_delete_row" ><i class="fa fa-trash"></i></a></td> </tr>';
	var i = 1;
	
	function addResRow()
	{     
		  
	  $('#employeeDetails tbody').append(rowobj);
	  
	  $('#employeeDetails tbody tr:last').find('input').val($("#positionName").val());
	}	
});
function edit(hearingId){    
	var url = '/lcms/hearing/edit/'+hearingId
	window.location = url;
   }
$('#createnewhearings').click(function() {
	var lcNumber = $('#lcNumber').val();
	var url = '/lcms/hearing/new/?lcNumber='+lcNumber;
	$('#hearingsform').attr('method', 'get');
	$('#hearingsform').attr('action', url);
	window.location = url;
});

function validateHearingDate() {
var hearingdate = $('#hearingDate').val();
var casedate = $('#caseDate').val();
var casedate1 = new Date(casedate);
var casedate=( casedate1.getDate()+ '/' + (casedate1.getMonth() + 1) + '/' + casedate1.getFullYear());
if (compareDate(hearingdate, casedate) == 1) {
bootbox.alert("Hearing date should be greater than case date.");
$(this).val("");
return false;
} else {
return true;
}
}

function compareDate(dt1, dt2) {
	var d1, m1, y1, d2, m2, y2, ret;
	dt1 = dt1.split('/');
	dt2 = dt2.split('/');
	ret = (eval(dt2[2]) > eval(dt1[2])) ? 1
	: (eval(dt2[2]) < eval(dt1[2])) ? -1
	: (eval(dt2[1]) > eval(dt1[1])) ? 1
	: (eval(dt2[1]) < eval(dt1[1])) ? -1	// decimal points
	: (eval(dt2[0]) > eval(dt1[0])) ? 1
	: (eval(dt2[0]) < eval(dt1[0])) ? -1
	: 0;
	return ret;
	}
			
	
				
