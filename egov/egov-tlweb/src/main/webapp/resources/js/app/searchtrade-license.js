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

var reportdatatable;
var drillDowntableContainer = $("#tblSearchTrade");
$(document).ready(function() {
	// Instantiate the application number Bloodhound suggestion engine
	var applicationnoengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=ApplicationNumber',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	applicationnoengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#applicationNumber').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: applicationnoengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
    });
	
	// Instantiate the license number Bloodhound suggestion engine
	var licensenoengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=LicenseNumber',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	// Initialize the Bloodhound suggestion engine
	licensenoengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#licenseNumber').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: licensenoengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
    });
	// Instantiate the old license number Bloodhound suggestion engine
	var oldlicensenoengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=OldLicenseNumber',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	// Initialize the Bloodhound suggestion engine
	oldlicensenoengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#oldLicenseNumber').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: oldlicensenoengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
    });
	
	// Instantiate the trade title Bloodhound suggestion engine
	var tradetitleengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=TradeTitle',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	tradetitleengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#tradeTitle').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: tradetitleengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
    });
	// Instantiate the trade owner Bloodhound suggestion engine
	var tradeownerengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=TradeOwnerName',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	tradeownerengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#tradeOwnerName').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: tradeownerengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
    });
	
	// Instantiate the property assessment Bloodhound suggestion engine
	var propassessnoengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=PropertyAssessmentNo',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	
    // Initialize the Bloodhound suggestion engine
    propassessnoengine.initialize();
    
 // Instantiate the Typeahead UI
	$('#propertyAssessmentNo').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: propassessnoengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
  });
	
// Instantiate the mobile number Bloodhound suggestion engine
	var mobilenoengine = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url:'../domain/commonTradeLicenseAjax-populateData.action?searchParamValue=%QUERY&searchParamType=MobileNo',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data.ResultSet.Result, function (cl) {
					return {
						name: cl.label,
						value: cl.value
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	mobilenoengine.initialize();
	
	// Instantiate the Typeahead UI
	$('#mobileNo').typeahead({
		  hint: false,
		  highlight: true,
		  minLength: 1
		}, {
		displayKey: 'name',
		source: mobilenoengine.ttAdapter()
	}).on('typeahead:selected', function(){            
		
 });
	
	$('#subCategory').select2({
			placeholder: "Select",
			width:'100%'
		});

	$('#category').change(function() {
		    var val = $(this).val();
		    var results = [];
		    $.ajax({
		        type: "GET",
		        url: '../licensesubcategory/subcategories-by-category?name=&categoryId=' + val,
		        dataType: "json",
		        success: function(data) {
		            $.each(data, function(i) {
		                var obj = {};
		                obj['id'] = data[i]['id']
		                obj['text'] = data[i]['name'];
		                results.push(obj);
		            });
		            $("#subCategory").empty();
	            	$("#subCategory").append("<option value=''>Select</option>");
		            $("#subCategory").select2({
		                allowClear: true,
		                placeholder: "Select",
		                width:'100%',
		                data: results
		            });
		        },
		        error: function() {
		        	bootbox.alert('something went wrong on server');
		        }
		    });
		});
		

$("#btnsearch").click(
		function() {
			
			var valid = 0;
			
			$('form').find('input[type=text],input[type="checkbox"], select').each(function(){
				if($(this).attr('type') == 'checkbox'){
					if($(this).is(":checked")) valid+=1;
				}else{
					if($(this).val() != "") valid+=1;
				}
	        });
			
			if(valid > 0){
            	$('.report-section').show();
    			var applicationNumber = $('#applicationNumber').val();
    			var licenseNumber = $('#licenseNumber').val();
    			var oldLicenseNumber=$('#oldLicenseNumber').val();
    			var category=$('#category').val();
    			var subCategory=$('#subCategory').val();
    			var tradeTitle= $('#tradeTitle').val();
    			var tradeOwnerName=$('#tradeOwnerName').val();
    			var propertyAssessmentNo=$('#propertyAssessmentNo').val();
    			var mobileNo=$('#mobileNo').val();
    	    	var ownerName = $('#ownerName').val();
    	    	var status = $('#status').val();
                var expiryYear=$('#expiryYear').val();
    			
    		   reportdatatable = drillDowntableContainer
    					.dataTable({
    						ajax : {
    							url : "../search/searchtrade-search",      
    							data : {
    								applicationNumber : applicationNumber,
    								licenseNumber : licenseNumber,
    								oldLicenseNumber : oldLicenseNumber,
    								categoryId : category,
    								subCategoryId : subCategory,
    								tradeTitle : tradeTitle,
    								tradeOwnerName : tradeOwnerName ,
    								propertyAssessmentNo : propertyAssessmentNo ,
    								mobileNo : mobileNo ,
    								ownerName : ownerName,
    								statusId : status,
    								expiryYear : expiryYear
    								
    							}
    						},
    						"bDestroy" : true,
    						"autoWidth": false,
    						"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
    						aaSorting: [],				
    						columns : [ {
    							"data" : function(row){
    									return { name:row.applicationNumber, id:row.licenseId };
    							},
    							"render" : function(data, type, row) {
    								return '<a href="javascript:void(0);" onclick="goToView('+row.licenseId+');" data-hiddenele="licenseId" data-eleval="'
    										+ data.id + '">' + data.name + '</a>';
    							},
    							"sTitle" : "Application Number"
    						}, {
    							"data" : "tlNumber",
    							"sTitle" : "TL Number"
    						}, {
    							"data" : "oldTLNumber",
    							"sTitle" : "Old TL Number"
    						}, {
    							"data" : "category",
    							"sTitle" : "Category"
    						}, {
    							"data" : "subCategory",
    							"sTitle" : "Sub-Category"
    						}, {
    							"data" : "tradeTitle",
    							"sTitle" : "Title of Trade"
    						}, {
    							"data" : "tradeOwner",
    							"sTitle" : "Trade Owner"
    						}, {
    							"data" : "mobileNumber",
    							"sTitle" : "Mobile Number"
    						}, {
    							"data" : "propertyAssmntNo",
    							"sTitle" : "Property Assessment Number"
    						}, {
    							"data" : "expiryYear",
    							"sTitle" : "Financial Year"
    						}, {
    							"data" : "status",
    							"sTitle" : "Status"
    						},{
    							"data" : "ownerName",
    							"sTitle" : "Owner Name"
    						},{
    							"sTitle" : "Actions",
    				        	  "render" : function(data,type,row) {
    				        		  var option = "<option>Select from Below</option>";
    				        		  $.each(JSON.parse(row.actions),function(key,value){
    		    			             option+= "<option>"+value.key+"</option>";
    		    			         });
    				        		  return ('<select class="dropchange" id="recordActions" onchange="goToAction(this,'+row.licenseId+')" >'+option+'</select>');
    				        	   }
    								}]				
    					});
            }else{
            	$('.report-section').hide();
            	bootbox.alert('Atleast one search criteria is mandatory!');
            }
			
			
		}
   );
});
function goToView(id) {
	window.open("../viewtradelicense/viewTradeLicense-view.action?id="+id, '', 'scrollbars=yes,width=1000,height=700,status=yes');
} 

function goToAction(obj, id){
	if(obj.options[obj.selectedIndex].innerHTML=='View Trade')
		window.open("../viewtradelicense/viewTradeLicense-view.action?id="+id,  'vt'+id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Modify Legacy License')
		window.open("../entertradelicense/update-form.action?model.id="+id, 'mll'+id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Collect Fees')
		window.open("/tl/integration/licenseBillCollect.action?licenseId="+id, 'cf'+id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Print Certificate' || obj.options[obj.selectedIndex].innerHTML=='Print Provisional Certificate')
		window.open("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id="+id,  'gc'+id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Renew License')
		window.open("../newtradelicense/newTradeLicense-beforeRenew.action?model.id="+id,  'rl'+id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if (obj.options[obj.selectedIndex].innerHTML == 'Generate Demand Notice')
		window.open("/tl/demandnotice/report?licenseId=" + id, 'dn' + id, 'scrollbars=yes,width=1000,height=700,status=yes');
	else if (obj.options[obj.selectedIndex].innerHTML == 'Closure')
		window.open("../viewtradelicense/viewTradeLicense-closure.action?id="+id,  'vt'+id, 'scrollbars=yes,width=1000,height=700,status=yes');

}