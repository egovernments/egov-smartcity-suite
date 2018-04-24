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
var tableContainer;
jQuery(document).ready(function() {
	$('#searchSurveyApplication-header').hide();
	
$('#searchSurveyApplication').click(function(){
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		var applicationNo=$("#applicationNo").val();
		var assessmentNo=$("#assessmentNo").val();
		var applicationType=$("#applicationType").val();
		var applicationStatus=$("#applicationStatus").val();
		if (fromDate != "") {
			var stsplit = fromDate.split("/");
			fromDate = stsplit[2] + "-" + stsplit[1] + "-"
			+ stsplit[0];
			
		}
		if(toDate!=""){
			var ensplit = toDate.split("/");
			toDate = ensplit[2] + "-" + ensplit[1] + "-"
					+ ensplit[0];
		}
		
		
		$('#searchSurveyApplication-header').show();
		

		
		$("#search-table").dataTable({
			destroy:true,
			
			ajax : {
				url : "/ptis/survey/appSearch/applicationdetails" ,
            	"contentType": "application/json",
                "type": "POST",
                "data": function ( d ) {
                  return JSON.stringify({"applicationType":applicationType,"applicationNo":applicationNo,"assessmentNo":assessmentNo,"applicationStatus":applicationStatus,"fromDate": fromDate, "toDate": toDate});
                },
                "dataSrc":""
			},
			searchable:true,
			columns: [
			{ data: function(row) {
				 var data = '<a href="' + row.appViewUrl + '">' + row.applicationNo + '</a>';
				 return data; }},
		    { data: function(row){
		    	return row.applicationType}},
             { data: function ( row) { 	
                return row.assessmentNo}},
           { data: function (row) { 	
                    return row.applicationDate}},
            { data: function (row) { 	
                    return row.address}},
            { data: function (row) { 	
                    return row.applicationStatus}},
            { data: function (row) { 	
                    return row.functionaryName}},
                    
			]
			
		});
		
		
});



});