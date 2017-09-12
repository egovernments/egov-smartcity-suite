/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

$(document).ready(function(){
	$('#reassign').click(function() {
			$('#reassignApprovalPosition').find('option:gt(0)').remove();
			var result=[];
			$.ajax({
				url : "/wtms/application/reassign",
				type : "GET",
				dataType : "json",
				success : function(data) {
					$.each(data, function(i) {
						 var obj = {};
		                    obj['id'] = i;
		                    obj['text'] = data[i];
		                    result.push(obj);
					});
					$.each(result, function(i) {
						$('#reassignApprovalPosition').append($('<option>').text(result[i].text).attr('value', result[i].id));
					})
					$('.reassign-screen').modal('show', {backdrop : 'static'});
				},
			});
	});
	
	$('#reassignclose').click(function(){
		window.parent.close();
	});
	
	$('#ReassignSubmit').click(function(){
		if($('#reassignApprovalPosition').val()!="") {
			var applicationId = $('#waterconnectiondetailid').val();
			var name=$('#reassignApprovalPosition option:selected').text().split(/-/)[0];
		$.ajax({
			url : "/wtms/application/reassign",
			type : "POST",
			data : {
				approvalPositionId : $('#reassignApprovalPosition').val(),
				applicationId	: applicationId
			},
				success : function(response) {
					$('#reassign-form').hide();
					$('#success-div').show();
					if(response) {
						$('#successmsg').text(" Successfully Reassigned to "+name);
					}
					else {
						$('#successmsg').text(" Could not be reassigned");
					}
				}
		});
	}
		else {
				showMessage('reassignment_error', 'Please Select a value');
		}
	});
	
});

function showMessage(id, msg) {
	$('#'+id).show();
	$('#'+id).text(msg);
}