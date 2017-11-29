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

jQuery(document).ready(function($){
	
});

function setSlabName(){
	var usage = $('#usage').val();
	var fromvolume = $("#fromVolume").val();
	var tovolume = $('#toVolume').val();
	
	if(usage=="" || fromVolume=="" || toVolume==""){
		return false;
	}
	var slabName = usage.substring(0,3)+"_"+fromvolume+"_"+tovolume;
	$('#slabName').val(slabName);
} 

$('#save').on('click', function(){
	if($("#usageSlabMasterform").valid()){
	var usage=$("#usage").val();
	var slabName = $('#slabName').val();
	var fromVolume = $("#fromVolume").val();
	var toVolume = $("#toVolume").val();
	if(toVolume!="" && Number(fromVolume)>Number(toVolume)){
		bootbox.alert("From Volume can not be greater than To Volume");
		return false;
	}
	if(!validateUsageSlabOverLap(usage, slabName, fromVolume, toVolume)){
		return false;
	}
	if(!validateUsageSlabGap(usage, slabName, fromVolume, toVolume)){
		return false;
	}
	else {
		return true;
	}
	}
	else 
		return false;
});

$('#editSave').on('click', function(){
	if(!checkRateExists())
			return false;
	return true;
});

function checkRateExists(){
	var isSlabExists = false;
	var slabName = $('#slabName').val();
	$.ajax({
		url : '/wtms/masters/usageslab-rate-exists-ajax',
		type : 'GET',
		data : {
			slabName
		},
		async : false,
		dataType : "json",
		success : function(response){
			if(response){
				bootbox.alert("Active rate exists for the slab "+slabName+". Please close the rate and then deactivate slab");
				return false;
			}
			else{
				
				isSlabExists = true;
			}
		}
	
	});
	return isSlabExists;
}

$('#search').on('click',function(){
		var usage = $("#usage").val();
		var fromVolume = $("#fromVolume").val();
		var toVolume = $("#toVolume").val();
		var active =  $("#isActive").val();
		var slabName = $('#slabName').val();
		var drillDownTableContainer = jQuery('#resultTable');
		oTable = drillDownTableContainer.DataTable({
			ajax : {
				url : "/wtms/masters/usageslab-view/"+jQuery('#mode').val(),
				type : 'POST',
				data : function(args){
					return {"args" : JSON.stringify(args),
						"usage" : usage,
						"fromVolume" : fromVolume,
						"toVolume" : toVolume,
						"active" : active,
						"slabName" : slabName
					};
				}
			},
			 processing : true,
	         serverSide : true,
	         sort : true,
	         filter : true,
	         "searching" : false,
	         responsive : true,
			 destroy : true,
			 'autoWidth': false,
			 "order": [[1, 'asc']],
			 "aLengthMenu" : [ [ 10, 25, 50, -1 ],
	            [ 10, 25, 50, "All" ] ],
	         "sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
	         columns : [ { 
						"mData" : "usage", "name" : "usage", "sClass" : "text-center", "sTitle" : "Usage"} ,{ 
						"mData" : "slabName", "name" : "slabName", "sClass" : "text-center", "sTitle" : "Slab Name"} ,{ 
						"mData" : "fromVolume", "name" : "fromVolume", "sClass" : "text-center", "sTitle" : "From Volume"} ,{ 
						"mData" : "toVolume", "name" : "toVolume", "sClass" : "text-center", "sTitle" : "To Volume"} ,{ 
						"mData" : "active", "name" : "active", "sClass" : "text-center", "sTitle" : "Active" },{
						"mData" : "id" , "orderable" : false, "sortable" : false, "searchable" : false, "visible" : false}]				
		});
	
});


$('#resultTable').on('click', 'tbody tr', function(obj){
	if($('#mode').val()==="edit"){
		window.open('/wtms/masters/usageslab-'+$('#mode').val()+"/"+oTable.row(this).data().id,'', 'width=800, height=600');
	}
});

$('#isActive').on('change', function(){
	$('#isActive').val($(this).is(':checked')? true : false);
});

jQuery('#addnewid').on('click',function(e){
	window.open('/wtms/masters/usageslab-create', '_self');
});

function validateUsageSlabOverLap(usage, slabName, fromVolume, toVolume){
	var isOverlap = false;
	$.ajax({
		url : '/wtms/masters/usageslab-overlap-ajax',
		type : "GET",
		data : {
			usage,
			slabName,
			fromVolume,
			toVolume
		},
		async : false,
		dataType : "json",
		success : function(response){
			if(response!="" && response.usage!=null){
				bootbox.alert("Given from and to values overlap with slab "+response.slabName);
			}
			else{
				isOverlap = true;
				return isOverlap;
			}
		}
	});
	
	return isOverlap;
}

function validateUsageSlabGap(usage, slabName, fromVolume, toVolume){
	var isSlabGap = false;
	$.ajax({
		url : "/wtms/masters/usageslab-gap-ajax",
		type : "GET",
		data : {
			usage,
			slabName,
			fromVolume,
			toVolume
		},
		async : false,
		dataType : "json",
		success : function(response){
				if(response.usage!=null && response.toVolume==null){
					bootbox.alert("Usage slab creation not allowed. Please deactivate current slab "+response.slabName+" and create new slab.");
				}
				else if (response.usage!=null && fromVolume>response.toVolume && response.toVolume!=(Number(fromVolume)-1)){
					if((Number(response.toVolume)+1) === (Number(fromVolume)-1)){
						bootbox.alert("Slab not defined for volume "+(Number(response.toVolume)+1));
					}
					else{
						bootbox.alert("Slab not defined between volume "+(Number(response.toVolume)+1)+" and "+(Number(fromVolume)-1));
					}
				}
				else if(toVolume==="" && response.toVolume===(Number(fromVolume)-1)){
						isSlabGap = true;
						return isSlabGap;
				}
				else if(response.usage!=null && toVolume<response.fromVolume && (Number(toVolume)+1)!=response.fromVolume){
					if((Number(toVolume)+1) === (Number(response.fromVolume-1))){
						bootbox.alert("Slab not defined for volume "+(Number(response.fromVolume-1)));
					}
					else if (toVolume!=""){
						bootbox.alert("Slab not defined between volume "+(Number(toVolume)+1)+" and "+(Number(response.fromVolume)-1));
					}
					
				}
				else{
					isSlabGap = true;
					return isSlabGap;
				}
		}
	});
	
	return isSlabGap;
}
