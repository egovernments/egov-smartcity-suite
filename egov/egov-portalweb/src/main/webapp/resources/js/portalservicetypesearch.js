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

var reportdatatable;
var drillDowntableContainer = $("#tblSearchPortalService");
jQuery(document).ready(function() {

$('#moduleId').change(function(){
	jQuery.ajax({
		url: "/portal/portalservicetype/ajaxboundary-servicesbymodule.action",
		type: "GET",
		data: {
			moduleId : jQuery('#moduleId').val()
		},
		cache: false,
		dataType: "json",
		success: function (response) {
			jQuery('#applicationType').html("");
			jQuery('#applicationType').append("<option value=''>Select</option>");
			jQuery.each(response, function(index, value) {
				jQuery('#applicationType').append($('<option>').text(value).attr('value', index));
			});
		}, 
		error: function (response) {
			jQuery('#applicationType').html("");
			jQuery('#applicationType').append("<option value=''>Select</option>");
		}
	});
});


$("#btnsearch").click(
        function () {
        	var moduleId = $("#moduleId").val();
    		var applicationType = $("#applicationType option:selected").text();

    		$('.report-section').show();
                var id = $('#id').val();
                var sla = $('#sla').val();
                var isActive = $('#isActive').val();
                var userService = $('#userService').val();
                var businessUserService =  $('#businessUserService').val();
                var count = 1;
                reportdatatable = drillDowntableContainer
                    .dataTable({
                        ajax: {
                            url: "/portal/portalservicetype/search",
                            type : "POST",
                            data: {
                            	id: id,
                            	module: moduleId,
                            	name: applicationType,
                            	sla: sla,
                            	isActive: isActive,
                            	userService:userService,
                            	businessUserService:businessUserService
                            }
                        },
                        "bDestroy": true,
                        "autoWidth": false,
                        aaSorting: [],
                        columns: [{
                            "data": function () {
                                return count++;
                            },
                            "sTitle": "SLNo."
                        }, {
                            "data": "module",
                            "sTitle": "Module"
                        }, {
                            "data": "name",
                            "sTitle": "Service Name"
                        }, {
                            "data": "sla",
                            "sTitle": "SLA"
                        }, {
                            "data": "isActive",
                            "sTitle": "Is Active"
                        }, {
                            "data": "userService",
                            "sTitle": "User Service"
                        },{
                            "data": "businessUserService",
                            "sTitle": "Business User Service"
                        },{
                            "sTitle": "Action",
                            "render": function (data, type, row) {
                            	 return '<a href="javascript:void(0);" onclick="goToAction(' + row.id + ');" data-hiddenele="id">' + 'Modify' + '</a>'; 
                          }
                        }]
                    });
        }
    );
});

function goToAction(id) {
	window.open('/portal/portalservicetype/update/'
			+ id,
			'_blank','width=1000, height=1000, scrollbars=yes', false);
}

function submitForm() {
	if(document.getElementById('inputsla').value == "")
        document.getElementById('inputsla').value = 0;
}
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}
