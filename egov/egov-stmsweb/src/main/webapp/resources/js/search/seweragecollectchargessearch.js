/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2016>  eGovernments Foundation
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



var tableContainer;
jQuery(document).ready(function($) {
	tableContainer = $("#aplicationSearchResults");
	 document.onkeydown=function(evt){
		 var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	if(keyCode == 13){
		submitButton();	
	}
	 }
	 
	 $('.slide-history-menu').click(function(){
			$(this).parent().find('.history-slide').slideToggle();
			if($(this).parent().find('#toggle-his-icon').hasClass('fa fa-angle-down'))
			{
				$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
				//$('#see-more-link').hide();
				}else{
				$(this).parent().find('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
				//$('#see-more-link').show();
			}
		}); 
});
	

$(".btn-primary").click(function(event){
	$('#searchSewerageapplication').show();
	var consumerNumber=$('#consumerNumber').val();
	var shscNumber=$('#shscNumber').val();
	var applicantName=$('#applicantName').val();
	var mobileNumber=$('#mobileNumber').val();
	var wardName=$('#app-mobno').val();
	var doorNo=$('#app-appcodo').val();
	
	if(consumerNumber=='' && shscNumber ==''  && applicantName == ''  && mobileNumber == ''
			&& wardName == ''  && doorNo == '') {
				bootbox.alert("Please Enter Atleast One Input Value For Searching");
				return false;
			}
			else {
				submitButton();
				return true;
			}
		event.preventDefault();
	});



$(document).on('change', 'select.actiondropdown', function() {
	var ptassessmentno= tableContainer.fnGetData($(this).parent().parent(), 1);
	var shscnumber= tableContainer.fnGetData($(this).parent().parent(), 2); 
	
	if($(this).find(":selected").text()=="Change number of seats"){
		var changeincloseturl=$(this).val();
		var consumerno= $(this).data('consumer-no');
		jQuery.ajax({
			url: "/stms/ajaxconnection/check-application-inworkflow/"+shscnumber,     
			type: "GET",
			data: {
				shscnumber : shscnumber,
			},
			dataType: "text",
			success: function (response) {
				console.log("success"+response);
				if(response!='""'){
				bootbox.alert(response);
				return false;
				}
				else{
					callurl(changeincloseturl, consumerno,ptassessmentno,shscnumber);     
				}
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
		}  
	else{  
		if($(this).find(":selected").index()>0){
			callurl($(this).val(), $(this).data('consumer-no'),ptassessmentno,shscnumber);
			
		}
	}
});

function callurl(url, consumernumber, ptassessmentno, shscnumber){
	
	url=url.replace('{consumerno}', consumernumber);
	url=url.replace('{assessmentno}', ptassessmentno);
	url=url.replace('{shscNumber}', shscnumber);
    openPopup(url)
}

function openPopup(url){
	window.open(url,'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
}

function submitButton() {
	
	tableContainer = $("#aplicationSearchResults");
	$('#searchResultDiv').show();
	$.post("/stms/collectfee/search/",$('#sewerageSearchRequestForm').serialize())
	.done(function(searchResult) {
	tableContainer.dataTable({
	destroy : true,
	"sPaginationType" : "bootstrap",
	"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
	"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
	"autoWidth" : false,
	searchable : true,   
	data : searchResult,
	columns : [{title : 'Application Number', data : "document.resource.searchable.consumernumber", "visible": true},
	           {title : 'pt assesmentno', data : "document.resource.clauses.ptassesmentno", "visible": false},
	           {title : 'S.H.S.C Number',class : 'row-detail', data : 'document.resource.searchable.shscnumber',
	        	   "render": function ( data, type, row, meta ) {
	        		   
			            return '<a onclick="openPopup(\'/stms/collectfee/search/view/'+row.document.resource.searchable.consumernumber+'/'+row.document.resource.clauses.ptassesmentno+'\')" href="javascript:void(0);">'+data+'</a>';} },
			   {title : 'Applicant Name',data : 'document.resource.searchable.consumername'},
			   {title : 'Application Type',render: function (data, type, full) {
					if(full.document.resource.clauses.type == 'Change In Closets') {
						return data = 'Modify Sewerage Connection';
					} else{
						return full.document.resource.clauses.type;
					}
					}},
	           {title : 'Property type',data : 'document.resource.clauses.propertytype'},
	           {title : 'Revenue ward',data : 'document.resource.clauses.revwardname'},
	           {title : 'Address',data : 'document.resource.searchable.address'},
	           {title : 'Application Status',data : 'document.resource.searchable.status'},
	           {"title" : "Actions","sortable":false,
	           render : function(data, type, row) {
	        	   var option = "<option>Select from Below</option>";
	        	   if(row.actions != null){
	        		   $.each(row.actions, function(key, value){
		        		   option+= "<option value="+key+">"+value+"</option>";
		        	   });
	        	   }
	        	   
				   return ('<select class="actiondropdown" data-consumer-no="'+ row.document.resource.searchable.consumernumber +'">'+ option +'</select>');
	           }}],
	           "aaSorting": [[9, 'asc']]
	});
	
	if(tableContainer.fnGetData().length > 1000) {
		$('#searchResultDiv').hide();
		$('#search-exceed-msg').show();
	} else {
		$('#search-exceed-msg').hide();
		$('#searchResultDiv').show();
	}
	})
}

$("#viewDCB").click(function(){
	var appNumber = document.getElementById("applNumber").value;
	var assessmentNum=document.getElementById("assessmentNo").value;
	window.open("/stms/reports/sewerageRateReportView/"+appNumber+"/"+assessmentNum, '_blank', "width=800, height=600, scrollbars=yes");
});
  
