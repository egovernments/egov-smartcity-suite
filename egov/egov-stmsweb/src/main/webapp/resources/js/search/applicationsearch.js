/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
var isFlag=false;
var applicationType;
jQuery(document).ready(function($) {
		
		$('#toggle-searchmore').click(function () {
	        if ($(this).html() == "More..") {
	            $(this).html('Less..');
	            $('.show-search-more').show();
	        } else {
	            $(this).html('More..');
	            $('.show-search-more').hide();
	        }
	    });	
	 
		var applicationnoengine = new Bloodhound({
	        datumTokenizer: function (datum) {
	            return Bloodhound.tokenizers.whitespace(datum.value);
	        },
	        queryTokenizer: Bloodhound.tokenizers.whitespace,
	        remote: {
	            url: 'autocomplete',
	            replace: function (url, query) {
	                return url + '?searchParamValue=' + query + '&searchParamType=applicationNumber';
	            },
	            filter: function (data) {
	                return $.map(data, function (applicationNumber) {
	                    return {
	                        name: applicationNumber
	                    }
	                });
	            }
	        }
	    });
	 applicationnoengine.initialize();
	 $('#applicationNumber').typeahead({
	        hint: false,
	        highlight: true,
	        minLength: 3
	    }, {
	        displayKey: 'name',
	        source: applicationnoengine.ttAdapter()
	    }).on('typeahead:selected', function () {

	    });
	 
		 var applicationnoengine = new Bloodhound({
		        datumTokenizer: function (datum) {
		            return Bloodhound.tokenizers.whitespace(datum.value);
		        },
		        queryTokenizer: Bloodhound.tokenizers.whitespace,
		        remote: {
		            url: 'autocomplete',
		            replace: function (url, query) {
		                return url + '?searchParamValue=' + query + '&searchParamType=shscNo';
		            },
		            filter: function (data) {
		                return $.map(data, function (shscNo) {
		                    return {
		                        name: shscNo
		                    }
		                });
		            }
		        }
		    });
		 
		 applicationnoengine.initialize();
		 $('#shscNumber').typeahead({
		        hint: false,
		        highlight: true,
		        minLength: 3
		    }, {
		        displayKey: 'name',
		        source: applicationnoengine.ttAdapter()
		    }).on('typeahead:selected', function () {

		    });
		 
		var applicationnoengine = new Bloodhound({
		        datumTokenizer: function (datum) {
		            return Bloodhound.tokenizers.whitespace(datum.value);
		        },
		        queryTokenizer: Bloodhound.tokenizers.whitespace,
		        remote: {
		            url: 'autocomplete',
		            replace: function (url, query) {
		                return url + '?searchParamValue=' + query + '&searchParamType=propertyId';
		            },
		            filter: function (data) {
		                return $.map(data, function (proeprtyId) {
		                    return {
		                        name: proeprtyId
		                    }
		                });
		            }
		        }
		    });
		
		 applicationnoengine.initialize();
		 $('#propertyId').typeahead({
		        hint: false,
		        highlight: true,
		        minLength: 3
		    }, {
		        displayKey: 'name',
		        source: applicationnoengine.ttAdapter()
		    }).on('typeahead:selected', function () {

		    });
		 
		var applicationnoengine = new Bloodhound({
		        datumTokenizer: function (datum) {
		            return Bloodhound.tokenizers.whitespace(datum.value);
		        },
		        queryTokenizer: Bloodhound.tokenizers.whitespace,
		        remote: {
		            url: 'autocomplete',
		            replace: function (url, query) {
		                return url + '?searchParamValue=' + query + '&searchParamType=applicantName';
		            },
		            filter: function (data) {
		                return $.map(data, function (applicantName) {
		                    return {
		                        name: applicantName
		                    }
		                });
		            }
		        }
		    });
		 applicationnoengine.initialize();
		 $('#applicantName').typeahead({
		        hint: false,
		        highlight: true,
		        minLength: 3
		    }, {
		        displayKey: 'name',
		        source: applicationnoengine.ttAdapter()
		    }).on('typeahead:selected', function () {

		    });
		 
	$("#fromDate,#toDate").datepicker({
		autoclose : true
	}).on(
			'changeDate',
			function(ev) {
				var fromDate = jQuery('#fromDate').val();
				var toDate = jQuery('#toDate').val();
				if (fromDate != "" && toDate != "") {
					var stsplit = fromDate.split("/");
					var ensplit = toDate.split("/");
					fromDate = stsplit[1] + "/" + stsplit[0] + "/"
							+ stsplit[2];
					toDate = ensplit[1] + "/" + ensplit[0] + "/"
							+ ensplit[2];
					var start = Date.parse(fromDate);
					var end = Date.parse(toDate);
					var difference = (end - start) / (86400000 * 7);
					if (difference < 0) {
						jQuery('#todateerror').show();
						jQuery('#toDate').val("");
					} else {
						jQuery('#todateerror').hide();
					}
				}
			});

	$(".btnSearch").click(function(event){
		$('#searchSewerageapplication').show();
		$('#table_container').show();
	   event.preventDefault();

	 $('#aplicationSearchResults').dataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
			"autoWidth": true,
			destroy:true,
			processing: true,
		    serverSide: true,
	        sort: true,
	        filter: true,
	        "searching": false,
	        "order": [[0, 'asc']],
	        "autoWidth": true,
	        "bDestroy":true,
	        responsive:true,
			ajax : {
					   url:"/stms/search/applications?"+$("#applicationSearchForm").serialize(),
					   type : 'POST',
					   data: function ( args ) {
						   return{
							   "args": JSON.stringify(args)
						   };	        
						}
					},	
					
			columns : [		
						{
						    "data": function (row, type, set, meta) {
						        return {
						            name: row.shscNumber,
						            id: row.applicationId
						        };
						    },
						    "render": function (data, type, row) {
						    	return '<a onclick="window.open(\'/stms/existing/sewerage/view/'
						    	+row.applicationNumber+'/'+row.propertyId+'\')" href="javascript:void(0);">'
						    	+data.name+'</a>';
						    },
						    "sTitle": "S.H.S.C Number",
						    "name": "shscNo"
						},
						  { sTitle : "Application Number", name:"ApplicationNumber", data:"applicationNumber"},
						  { sTitle : "Application Type", name:"ApplicationType", data: "applicationType"},
						  { sTitle : "Application Date", name:"ApplicationDate", data: "applicationDate"},
						  { sTitle : "Application Status", name:"ApplicationStatus" , data: "applicationStatus"},
						  { sTitle : "Connection Status", name:"ConnectionStatus", data: "connectionStatus"},
						  { sTitle : "Property type", name:"PropertyType", data: "propertyType"},
						  { sTitle : "Applicant Name", name:"ApplicantName", data: "ownerName"},
						  { sTitle : "Process Owner", name:"ProcessOwner", data: "processOwner"},
						  { sTitle : "Revenue ward", name:"revenueWard", data: "revenueWard"},
						  {"defaultContent": "<a></a>",
							  "render": function ( data, type, row, meta ) {
								 return  '<a onclick="window.open(\'/stms/reports/sewerageRateReportView/'+row.applicationNumber+'/'+row.propertyId+'\')" href="javascript:void(0);">'+"View DCB"+'</a>';
							 },
						  "sTitle": "Action",
						  "name": "Action"
						  }
					],
			});
 });
});


