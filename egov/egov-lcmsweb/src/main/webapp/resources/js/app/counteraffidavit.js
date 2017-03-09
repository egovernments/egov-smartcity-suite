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
jQuery(document).ready(
		function($) {
			$('#buttonid').click(function() {
				formValidation();
			
				
			});
		
		});

	
function formValidation(){
	var pwrduedate = $("#pwrDueDate").val();
	var pwrapprovaldate = $("#pwrApprovalDate").val();
	var counteraffidavitduedate = $("#counterAffidavitDueDate").val();
	var counteraffidavitapprovaldate = $("#counterAffidavitApprovalDate").val();
	var eofficenum = $("#eoffice").val();
	var cafilingdate = $("#caFilingDate").val();
	if (((pwrduedate == null || pwrduedate == "") && (pwrapprovaldate == null || pwrapprovaldate == "") && 
			(counteraffidavitduedate== null || counteraffidavitduedate== "") && 
			(counteraffidavitapprovaldate== null || counteraffidavitapprovaldate=="") && (eofficenum== null || eofficenum =="")
			&& (cafilingdate == null || cafilingdate==""))) {
		bootbox
				.alert('Please enter one mandatory field');
		return false;
	}
	//eOfficeNumValidation();
  if ($( "#legalcasecaform" ).valid())
	  {
	  document.forms[0].submit();
	  }
  		}

	
function eOfficeNumValidation(){
	var txt = $('#eoffice').val();
	var re = /^[ A-Za-z0-9/-]*$/
	if (re.test(txt)) {
	}
	else {
	alert('It Should accept two special charcters / and -');
	return false;
	}
	
}
			
			/*var modeval=$("#mode").val();
			var $tableBody = $('#caffidavitdetails').find("tbody"),
		    $trLast = $tableBody.find("tr:last");
			
			if(modeval =='counteredit'){
			 $("#caffidavitdetails").find("*").attr("disabled", "disabled");
			}

			$('#ca_add_row').click(function(){
				
				addEmployeeRow();
			});
			
		});
	var modeval=$("#mode").val();
	if(modeval =='counteredit'){
	 $("#caffidavitdetails").find("*").attr("disabled", "disabled");
	}
	$('#buttonsubmitid').click(function(){
	var modeval=$("#mode").val();
	if(modeval =='countercreate'){
	var dept=$("#departmentId").val();
	var positionval=$("#positionId").val();
	
	if( dept ==undefined || dept ==""){
		bootbox.alert('Assigned Department is mandatory');
		return false;
	}
	else if(positionval ==undefined || positionval=="")
		{
		bootbox.alert('Assigned position is mandatory');
		return false;
		}
	else{
		$("#caffidavitdetails").find("*").removeAttr('disabled');
		$("#caffidavitdetails").find("*").attr('disabled',false);
		document.forms["legalCaseAdvocateform"].submit();
	}
	}
	else{
		$("#caffidavitdetails").find("*").removeAttr('disabled');
		$("#caffidavitdetails").find("*").attr('disabled',false);
		document.forms["legalCaseAdvocateform"].submit();
	}


});
var count = $("#caffidavitdetails tbody  tr").length - 1;

function addEmployeeRow()
{
	var $tableBody = $('#caffidavitdetails').find("tbody"),
    $trLast = $tableBody.find("tr:last"),
    $trfirst = $tableBody.find("tr:first"),
    $trNew = $trLast.clone();
	count++;
	alert(count +'count');
		$trNew.find("input").each(function(){
	        $(this).attr({
	        	'name': function(_, name) { return name.replace(/\[.\]/g, '['+ count +']'); } ,
	        	'id': function(_, id) { return id.replace(/\[.\]/g, '['+ count +']'); }
	        });
	    });
		$trLast.after($trNew);
		$trfirst.find('input').val('');
		 $("#caffidavitdetails tbody tr:gt(0)").each(function( index ) {
			 $(this).find('a').hide();
			 $(this).attr("disabled", "disabled");
		 });
		 
		
}
*/