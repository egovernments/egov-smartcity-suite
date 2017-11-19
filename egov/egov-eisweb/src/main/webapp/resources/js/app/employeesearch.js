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

$(document).ready(function(){
	tableContainer1 = $("#employee-table"); 
	$("#searchbtn").click(function (){
		tableContainer1.dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[5,10, 25, 50, -1], [5,10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"ajax": "ajaxemployees?"+$("#searchEmployeeForm").serialize(),
			"columns": [
			            { "data": "slno","width": "5%" },
						{ "data": "name","width": "10%" },
						  {
					        
                            "data" : function(row, type, set, meta){
	                     	return { name:row.code, id:row.id };
                         },
                            "render" : function(data, type, row) {
	                         return '<a href="javascript:void(0);" onclick="goToView(this);" data-hiddenele="code" data-eleval="'
			                + data.name + '">' + data.name + '</a>';
                           },
                    },
						{ "data": "department","width": "15%" },
						{ "data": "designation","width": "15%" },
						{ "data": "position","width": "15%" },
						{ "data": "daterange","width": "20%" }
			]
		});
	
});
	
	
	$('#searchemployee').keyup(function(){
		tableContainer1.fnFilter(this.value);
	});


});

function goToView(obj) {
	var mode = $('#mode').val();
	var url = "/eis/employee/view/";
	if(mode=="update")
		url = "/eis/employee/update/";
	else if (mode=="updatecontact")
		url = "/eis/employee/updatecontact/"; 
	window.open(url+jQuery(obj).data('eleval'), '', 'scrollbars=yes,width=1000,height=700,status=yes');
} 

