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
var reportdatatable;
jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery("#applicationNumber").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: applicationNoType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#applicationNumber").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#applicationNumber").val(ui.item.value);
		 }
	});
	
	jQuery("#licenseNumber").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: licenseNoType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#licenseNumber").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#licenseNumber").val(ui.item.value);
		 }
	});
	
	jQuery("#oldLicenseNumber").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: oldLicenseNoType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#oldLicenseNumber").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#oldLicenseNumber").val(ui.item.value);
		 }
	});
	
	jQuery("#tradeTitle").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: tradeTitleType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#tradeTitle").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#tradeTitle").val(ui.item.value);
		 }
	});
	
	jQuery("#tradeOwnerName").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: ownerNameType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#tradeOwnerName").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#tradeOwnerName").val(ui.item.value);
		 }
	});
	
	jQuery("#propertyAssessmentNo").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: propertyNoType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#propertyAssessmentNo").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#propertyAssessmentNo").val(ui.item.value);
		 }
	});
	
	
	jQuery("#mobileNo").autocomplete({
		source: function(request,response) {
				var url='../domain/commonTradeLicenseAjax-populateData.action';
				jQuery.getJSON(url,{searchParamValue: request.term,searchParamType: mobileNoType},function(data){
					response(data.ResultSet.Result);
					if(data.ResultSet.Result==null || data.ResultSet.Result==""){
						jQuery("#mobileNo").val('');
					}
		      })	
		},
	    minLength: 2,
	    select: function( event, ui ) {
			jQuery("#mobileNo").val(ui.item.value);
		 }
	});
	
	jQuery(':input').inputmask();
	drillDowntableContainer = jQuery("#tblSearchTrade");
	jQuery('#btnsearch').click(function(e) {
		dom.get("tradeSearchError").style.display='none';
        dom.get("tradeSearchError").innerHTML='';
        var applicationNumber = jQuery('#applicationNumber').val();
    	var licenseNumber = jQuery('#licenseNumber').val();
    	var oldLicenseNumber=jQuery('#oldLicenseNumber').val();
    	var category=jQuery('#category').val();
    	var subCategory=jQuery('#subCategory').val();
    	var tradeTitle= jQuery('#tradeTitle').val();
    	var tradeOwnerName=jQuery('#tradeOwnerName').val();
    	var propertyAssessmentNo=jQuery('#propertyAssessmentNo').val();
    	var mobileNo=jQuery('#mobileNo').val();
		
		if ((applicationNumber == null || applicationNumber == "")  &&
				(licenseNumber == null || licenseNumber == "") && (oldLicenseNumber == null || oldLicenseNumber == "") &&
				(category == '-1') && (subCategory == '-1') &&
				(tradeTitle == null || tradeTitle == "") && (tradeOwnerName == null || tradeOwnerName == "") &&
				(propertyAssessmentNo == null || propertyAssessmentNo == "") && (mobileNo == null || mobileNo == "")) {
			dom.get("tradeSearchError").style.display='';
	        dom.get("tradeSearchError").innerHTML='Cannot Search. Atleast One Search Criteria is Mandatory.';
			return false;
		}
		
		callAjaxForSearchTrade();
	});
	
});

function goToView(obj) {
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']')
	.val(jQuery(obj).data('eleval'));   
	window.open("../viewtradelicense/viewTradeLicense-view.action?id="+jQuery('#licenseId').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
} 

function goToAction(obj){
	jQuery('input[name=' + jQuery(obj).data('hiddenele') + ']')
	.val(jQuery(obj).data('eleval')); 
	if(obj.options[obj.selectedIndex].innerHTML=='View Trade')
		window.open("../viewtradelicense/viewTradeLicense-view.action?id="+jQuery('#licenseId').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Modify Legacy License')
		window.open("../entertradelicense/update-form.action?model.id="+jQuery('#licenseId').val(),"_self");
	else if(obj.options[obj.selectedIndex].innerHTML=='Collect Fees')
		window.open("/tl/integration/licenseBillCollect.action?licenseId="+jQuery('#licenseId').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Print Certificate')
		window.open("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id="+jQuery('#licenseId').val(), '', 'scrollbars=yes,width=1000,height=700,status=yes');
	else if(obj.options[obj.selectedIndex].innerHTML=='Renewal Notice')
		window.open("../renew/tradeRenewalNotice-renewalNotice.action?model.id="+jQuery('#licenseId').val(),"_self");
	else if(obj.options[obj.selectedIndex].innerHTML=='Renew License')
		window.open("../newtradelicense/newTradeLicense-beforeRenew.action?model.id="+jQuery('#licenseId').val(),"_self");
	else if(obj.options[obj.selectedIndex].innerHTML=='Modify License')
		window.open("../newtradelicense/editTradeLicense-beforeEdit.action?model.id="+jQuery('#licenseId').val(),"_self");
}

  
function callAjaxForSearchTrade() {
	var applicationNumber = jQuery('#applicationNumber').val();
	var licenseNumber = jQuery('#licenseNumber').val();
	var oldLicenseNumber=jQuery('#oldLicenseNumber').val();
	var category=jQuery('#category').val();
	var subCategory=jQuery('#subCategory').val();
	var tradeTitle= jQuery('#tradeTitle').val();
	var tradeOwnerName=jQuery('#tradeOwnerName').val();
	var propertyAssessmentNo=jQuery('#propertyAssessmentNo').val();
	var mobileNo=jQuery('#mobileNo').val();
		
	jQuery('.report-section').removeClass('display-hide');
	reportdatatable = drillDowntableContainer
			.dataTable({
				ajax : {
					url : "/tl/search/searchTrade-search.action",      
					data : {
						applicationNumber : applicationNumber,
						licenseNumber : licenseNumber,
						oldLicenseNumber : oldLicenseNumber,
						categoryId : category,
						subCategoryId : subCategory,
						tradeTitle : tradeTitle,
						tradeOwnerName : tradeOwnerName ,
						propertyAssessmentNo : propertyAssessmentNo ,
						mobileNo : mobileNo 
					}
				},
				"sPaginationType" : "bootstrap",
				"bDestroy" : true,
				"autoWidth": false,
				"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-3'i><'col-xs-3 col-right'l><'col-xs-3 col-right'<'export-data'T>><'col-xs-3 text-right'p>>",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"oTableTools" : {
					"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
					"aButtons" : [ ]
				},
				aaSorting: [],				
				columns : [ {
							"data" : function(row, type, set, meta){
									return { name:row.applicationNumber, id:row.licenseId };
							},
							"render" : function(data, type, row) {
								return '<a href="javascript:void(0);" onclick="goToView(this);" data-hiddenele="licenseId" data-eleval="'
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
							"sTitle" : "Sub Category"
						}, {
							"data" : "tradeTittle",
							"sTitle" : "Tittle of Trade"
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
							"sTitle" : "Actions",
				        	  "render" : function(data,type,row) {
				        		  var showActions = row.actions; 
				        		  var option = "<option>Select from Below</option>";
				        		  jQuery.each(JSON.parse(row.actions),function(key,value){
	        			             option+= "<option>"+value.key+"</option>";
	        			         });
				        		  console.log("Option Text"+option); 
				        		  return ('<select class="dropchange" id="recordActions" data-hiddenele="licenseId" data-eleval="'
											+ row.licenseId + '" onChange="goToAction(this);" >'+option+'</select>');
				        	   }
						}]				
			});
}