/*#-------------------------------------------------------------------------------
	# eGov suite of products aim to improve the internal efficiency,transparency, 
	#    accountability and the service delivery of the government  organizations.
	# 
	#     Copyright (C) <2015>  eGovernments Foundation
	# 
	#     The updated version of eGov suite of products as by eGovernments Foundation 
	#     is available at http://www.egovernments.org
	# 
	#     This program is free software: you can redistribute it and/or modify
	#     it under the terms of the GNU General Public License as published by
	#     the Free Software Foundation, either version 3 of the License, or
	#     any later version.
	# 
	#     This program is distributed in the hope that it will be useful,
	#     but WITHOUT ANY WARRANTY; without even the implied warranty of
	#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	#     GNU General Public License for more details.
	# 
	#     You should have received a copy of the GNU General Public License
	#     along with this program. If not, see http://www.gnu.org/licenses/ or 
	#     http://www.gnu.org/licenses/gpl.html .
	# 
	#     In addition to the terms of the GPL license to be adhered to in using this
	#     program, the following additional terms are to be complied with:
	# 
	# 	1) All versions of this program, verbatim or modified must carry this 
	# 	   Legal Notice.
	# 
	# 	2) Any misrepresentation of the origin of the material is prohibited. It 
	# 	   is required that all modified versions of this material be marked in 
	# 	   reasonable ways as different from the original version.
	# 
	# 	3) This license does not grant any rights to any user of the program 
	# 	   with regards to rights under trademark law for use of the trade names 
	# 	   or trademarks of eGovernments Foundation.
	# 
	#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
jQuery(document)
		.ready(
				function($) {

					tableContainer1 = $("#escalation-table");

					$('#escalationSearch').click(function() {
						callajaxdatatable();
					});

					function callajaxdatatable() {
						$('.report-section').removeClass('display-hide');
						tableContainer1
								.dataTable({
									ajax : {
										url : "/pgr/escalation/resultList-update",
										data : {
											complaintTypeId : $('#complaintTypeId').val(),
											boundaryId : $('#boundaryId').val(),	
											boundaryTypeId : $('#boundary_type_id').val(),
											positionId : $('#positionId').val()
										}
									},
									"sPaginationType" : "bootstrap",
									"aLengthMenu" : [ [ 10, 25, 50, -1 ],
											[ 10, 25, 50, "All" ] ],
									"autoWidth" : false,
									"bDestroy": true,
									"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
									/*
									 * columns : [{ "mData" : "complaintType",
									 * "sTitle" : "Complaint Type" },{ "mData" :
									 * "designation", "sTitle" : "Designation"
									 * },{ "mData" : "noOfHours", "sTitle" :
									 * "No.of Hours" }]
									 */
									columns : [ {
										"data" : "complaintType"
									}, {
										"data" : "boundaryType"
									}, {
										"data" : "boundary"
									}, {
										"data" : "position"
									} ]
								});
						e.stopPropagation();
					}

				
					// Instantiate the Bloodhound suggestion engine
					// Complaint Type auto-complere
					var complaintType = new Bloodhound(
							{
								datumTokenizer : function(datum) {
									return Bloodhound.tokenizers
											.whitespace(datum.value);
								},
								queryTokenizer : Bloodhound.tokenizers.whitespace,
								remote : {
									url : '/pgr/complaint/escalationTime/complaintTypes?complaintTypeName=%QUERY',
									filter : function(data) {
										// Map the remote source JSON array to a
										// JavaScript object array
										return $.map(data, function(ct) {
											return {
												name : ct.name,
												value : ct.id
											};
										});
									}
								}
							});

					complaintType.initialize();

					$('#com_type').typeahead({
						hint : true,
						highlight : true,
						minLength : 3
					}, {
						displayKey : 'name',
						source : complaintType.ttAdapter()
					}).on('typeahead:selected', function(event, data) {
						$("#complaintTypeId").val(data.value);
					}).on('change', function(event, data) {
						if ($('#com_type').val() == '') {
							$("#complaintTypeId").val('');

						}
					});


	//Position auto-complete
	var position = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/pgr/complaint/escalation/position?positionName=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (pos) {
					return {
						name: pos.name,
						value: pos.id
					};
				});
			}
		}
	});
	
	position.initialize();
	$('#com_position').typeahead({
		hint: true,
		highlight: true,
		minLength: 3
		}, {
		displayKey: 'name',
		source: position.ttAdapter()
		}).on('typeahead:selected', function(event, data){            
			$("#positionId").val(data.value);    
	    }).on('change',function(event,data){
			if($('#com_position').val() == ''){
				$("#positionId").val('');
			}
	    }); 
	
	$("#boundary_type_id").change(function(){
		$('#com_boundry').typeahead('destroy');
		var b_id = $("#boundary_type_id").val();
		var boundaries = new Bloodhound(
				{
					datumTokenizer : function(datum) {
						return Bloodhound.tokenizers
								.whitespace(datum.value);
					},
					queryTokenizer : Bloodhound.tokenizers.whitespace,
					remote : {
						url : '/pgr/complaint/escalation/boundaries-by-type?boundaryName=%QUERY&boundaryTypeId='+ b_id,
						filter : function(data) {
							// Map the remote source JSON array to a
							// JavaScript object array
							return $.map(data, function(boundList) {
								return {
									name: boundList.name,
									value: boundList.id
								};
							});
						}
					}
				});
	boundaries.initialize();
	$('#com_boundry').typeahead({
		hint: true,
		highlight: true,
		minLength: 3
		}, {
		displayKey: 'name',
		source: boundaries.ttAdapter()
		}).on('typeahead:selected', function(event, data){            
			$("#boundaryId").val(data.value);    
	    }).on('change',function(event,data){
    		if($('#com_boundry').val() == ''){
    			$("#boundaryId").val('');
    		}
        });
	});

 });