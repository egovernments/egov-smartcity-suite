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
	var ptassessmentno= oTable.fnGetData($(this).parent().parent(), 1);
	var shscnumber= oTable.fnGetData($(this).parent().parent(), 2); 
	
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

var prevdatatable;
function submitButton() {
	
	oTable = $("#aplicationSearchResults");
	if(prevdatatable){
		prevdatatable.fnClearTable();
		$('#aplicationSearchResults thead tr').remove();
	}
	/*$.post("/stms/collectfee/search/",$('#sewerageSearchRequestForm').serialize())
	.done(function(searchResult) {*/
	prevdatatable = oTable.dataTable({
		dom: "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
		processing: true,
        serverSide: true,
        sort: true,
        filter: true,
        "searching": false,
        "autoWidth": false,
        "bDestroy": true,
	
		 ajax : {

				url : "/stms/collectfee/search/",
				type : 'POST',
				data : function(args) {
					return {
						"args" : JSON.stringify(args),
						"consumerNumber":$('#consumerNumber').val(),
						"shscNumber" : $('#shscNumber').val(),
						"applicantName" : $('#applicantName').val(),
						"mobileNumber":$('#mobileNumber').val(),
					 	"revenueWard": $('#app-mobno').val(),
					 	"doorNumber" : $('#app-appcodo').val()
												
					};
				},
			},	
			
		columns : [
					  { title : "Application Number", data:"applicationNumber"},
					  { title : "pt assesmentno", data: "assessmentNumber", visible: false},
					  {title : 'S.H.S.C Number',class : 'row-detail', data : 'shscNumber',
			        	   "render": function ( data, type, row, meta ) {
			        		   
					            return '<a onclick="openPopup(\'/stms/existing/sewerage/view/'+row.applicationNumber+'/'+row.assessmentNumber+'\')" href="javascript:void(0);">'+data+'</a>';} },
					  { title : "Applicant Name", data: "applicantName"},
					  { title : "Application Type", data: "applicationType"},
					  { title : "Property type", data: "propertyType"},
					  { title : "Revenue ward", data: "revenueWard"},
					  { title : "Address", data: "address"},
					  { title : "Application Status", data: "applicationStatus"},
					  { title : "Actions","sortable":false,
						  render: function(data, type, row){
							  var option ="<option>Select from below</option>";
							  if(row.actions!=null){
								  $.each(row.actions, function(key,value){
									  option+= "<option value="+key+">"+value+"</option>";
								  });
							  }
							  return ('<select class="actiondropdown" data-consumer-no="'+row.applicationNumber+'" >'+option+'</select>');
						  }
					  }
					  ],
					  "aaSorting": [[4, 'asc']] 
				});
}

$("#viewDCB").click(function(){
	var appNumber = document.getElementById("applNumber").value;
	var assessmentNum=document.getElementById("assessmentNo").value;
	window.open("/stms/reports/sewerageRateReportView/"+appNumber+"/"+assessmentNum, '_blank', "width=800, height=600, scrollbars=yes");
});
  
