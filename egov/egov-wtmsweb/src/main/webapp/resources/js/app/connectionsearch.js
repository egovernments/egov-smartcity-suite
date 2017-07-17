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
var tableContainer;
jQuery(document).ready(function($) {
	
		$.fn.dataTable.moment( 'DD/MM/YYYY' );
		
		$(":input").inputmask();
		tableContainer = $("#aplicationSearchResults");
		var cscUserRole = $('#cscUserRole').val();
		var ulbUserRole = $('#ulbUserRole').val();
		var superUserRole = $('#superUserRole').val();
		var approverUserRole = $('#approverUserRole').val();
		var operatorRole = $('#operatorRole').val();
		var citizenRole = $('#citizenRole').val();
		var billcollector=$('#billcollectionRole').val();
		var administratorRole =$('#administratorRole').val();
		 /*document.onkeydown=function(evt){
			 var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
		if(keyCode == 13){
			submitButton();	
		}
		 }*/
		$('#searchapprvedapplication').click(function() {
			submitButton();
		});
														
					$("#aplicationSearchResults").on('change','tbody tr td .dropchange',
							function() {
							var consumerNumber = tableContainer.fnGetData($(this).parent().parent(), 1);
							var applicationTypeCode = tableContainer.fnGetData($(this).parent().parent(), 3);
										if (this.value == 0) {
											var url = '/wtms/application/view/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} else if (this.value == 1) {
											var url = '/wtms/application/addconnection/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} else if (this.value == 2) {
											if (consumerNumber != '') {
												var url = '/wtms/application/changeOfUse/'+ consumerNumber;
												$('#waterSearchRequestForm').attr('method', 'get');
												$('#waterSearchRequestForm').attr('action', url);
												window.location = url;
											}
											} else if (this.value == 9) {
												var url = '/wtms/application/close/'+ consumerNumber;
												$('#waterSearchRequestForm').attr('method', 'get');
												$('#waterSearchRequestForm').attr('action', url);
												window.location = url;
											} 
											else if (this.value == 4) {
										} else if (this.value == 5) {
										} else if (this.value == 6) {
											var url = '/wtms/application/generatebill/'+ consumerNumber+"?applicationTypeCode="+applicationTypeCode;
											$('#waterSearchRequestForm').attr('method', 'post');
											$('#waterSearchRequestForm').attr('action', url);
											$('#waterSearchRequestForm').attr('name', 'myform');
											document.forms["myform"].submit();
										} else if (this.value == 7) {
										}
										else if (this.value == 8) {
											var url = '/wtms/application/meterentry/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 
										else if (this.value == 10) {
											var url = '/wtms/application/reconnection/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 
										if (this.value == 11) {
											var url = '/wtms/viewDcb/consumerCodeWis/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 
										if (this.value == 13) {
											var url = '/wtms/application/editDemand/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 
										if (this.value == 14) {
											var url = '/wtms/application/newConnection-editExisting/'+ consumerNumber;
											$('#waterSearchRequestForm').attr('method', 'get');
											$('#waterSearchRequestForm').attr('action', url);
											window.location = url;
										} 

									});

						$('#aplicationSearchResults').on('click','tbody tr td.row-detail',
									function() {
										var consumerNumber = tableContainer.fnGetData(this);
										var url = '/wtms/application/view/'+ consumerNumber;
										$('#waterSearchRequestForm').attr('method', 'get');
										$('#waterSearchRequestForm').attr('action', url);
										window.open(url, 'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');
									});

					$('#aplicationSearchResults').on('click','tbody tr td.row-detail',
									function() {
										var consumerNumber = tableContainer.fnGetData(this);
										var url = '/wtms/application/view/'+ consumerNumber;
										$('#waterSearchRequestForm').attr('method', 'get');
										$('#waterSearchRequestForm').attr('action', url);
										window.open(url, 'window','scrollbars=yes,resizable=yes,height=700,width=800,status=yes');

									});

					$('#searchwatertax').keyup(function() {
						tableContainer.fnFilter(this.value);
					});
					
					$(".btn-danger").click(function(event){
						$('#searchResultDiv').hide();
					});

				});
/*function redirecttoOnline(obj)
{
	$("#aplicationSearchResults").on('click','tbody tr',function(event) {
	var consumerNumber = tableContainer.fnGetData($(this).parent().parent(), 1);
	var applicationTypeCode = tableContainer.fnGetData($(this).parent().parent(), 3);
	bootbox.alert('consumerNumber'+ consumerNumber);
	var url = '/wtms/application/generatebill/'+ consumerNumber+"?applicationTypeCode="+applicationTypeCode;
	$('#waterSearchRequestForm').attr('method', 'get');
	$('#waterSearchRequestForm').attr('action', url);
	window.location = url;		
	});
}*/
$("#aplicationSearchResults").on('click','tbody tr td .collect-hoardingWiseFee',function(event) {
	var consumerNumber = tableContainer.fnGetData($(this).parent().parent(), 1);
	var applicationTypeCode = tableContainer.fnGetData($(this).parent().parent(), 3);
	var url = '/wtms/application/generatebill/'+ consumerNumber+"?applicationTypeCode="+applicationTypeCode;
	$('#waterSearchRequestForm').attr('method', 'post');
	$('#waterSearchRequestForm').attr('action', url);
	$('#waterSearchRequestForm').attr('name', 'myform');
	document.forms["myform"].submit();
	//window.open("generatebill/hoarding/"+hoardingNo, ''+hoardingNo+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')

});

$(document).on("keypress", 'form', function (e) {
    var code = e.keyCode || e.which;
    if (code == 13) {
        e.preventDefault();
        return false;
    }
});

function submitButton()
{
	tableContainer = $("#aplicationSearchResults");
	var cscUserRole = $('#cscUserRole').val();
	var ulbUserRole = $('#ulbUserRole').val();
	var superUserRole = $('#superUserRole').val();
	var approverUserRole = $('#approverUserRole').val();
	var operatorRole = $('#operatorRole').val();
	var citizenRole = $('#citizenRole').val();
	var billcollector=$('#billcollectionRole').val();
	
	
	$('#searchResultDiv').show();
	$.post("/wtms/search/waterSearch/",$('#waterSearchRequestForm').serialize())
	.done(function(searchResult) {
	console.log(JSON.stringify(searchResult));
	tableContainer.dataTable({
	destroy : true,
	"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
	"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
	"autoWidth" : false,
	"oTableTools" : {
		"sSwfPath" : "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
		"aButtons" : [ 
		               {
			             "sExtends": "pdf",
			             "mColumns": [0,1,2,4,5,6,9],
                         "sPdfMessage": "",
                         "sTitle": "Water Connection Report",
                         "sPdfOrientation": "landscape"
		                },
		                {
				             "sExtends": "xls",
				             "mColumns": [0,1,2,4,5,6,9],
                             "sPdfMessage": "Water Connection Report",
                             "sTitle": "Water Connection Report"
			             },
			             {
				             "sExtends": "print",
				             "mColumns": [0,1,2,4,5,6,9],
                             "sPdfMessage": "",
                             "sTitle": "Water Connection Report"
			             }],
		
	},
	searchable : true,
	data : searchResult,
	columns : [{title : 'Applicant Name',data : 'applicantName'},
    {title : 'H.S.C Number',class : 'row-detail',data : 'consumerCode',               
     "render": function ( data, type, full, meta ) {
         return '<div style="font-style: italic;text-decoration: underline;" class="view-content">'+data+'</div>';} },
    {title : 'Assessment Number',data : 'propertyid'}, 
    {title : 'Address',data : 'address'},
    {title : 'apptype',data : 'applicationcode',"bVisible" : false},
    {title : 'legacy',data : 'islegacy',"bVisible" : false},
    {title : 'Usage Type',data : 'usage'},
    {title : 'Property Tax Due',class : 'text-right',
 	   render: function (data, type, full) {
 		   if(citizenRole != 'true') {
					return full.propertyTaxDue;
				}
				else return "";
 	   }
 	},
	   {title : 'Status',data : 'status'},
    {title : 'conntype',data : 'connectiontype',"bVisible" : false},
   // {title : 'conndate',data : 'createdDate',"bVisible" : false},
    {title : 'Water Charge Due',class : 'text-right',data : 'waterTaxDue'},
    {title : 'Actions',
 	   render : function(data,type,full) {
 		     
 			   if (full != null && full != undefined && full.applicationcode != undefined &&
 				   (full.applicationcode == 'ADDNLCONNECTION' )) {
 			   if (full.status == 'ACTIVE' ) {
 				   if ( citizenRole== 'true'   ) { 
 					   if(full.waterTaxDue > 0)
 					   return ('<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Pay</button>&nbsp;');   
 					   else{
 						   return ('');   
 					   }
 				   }
 				   else if ( (billcollector!=null &&  billcollector!="" && (ulbUserRole == null || ulbUserRole =="" ) && (cscUserRole==null || cscUserRole=="") )  && full.waterTaxDue > 0  ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
 				   }
 				   else if (cscUserRole!=null && cscUserRole !="" && (billcollector==null ||  billcollector=="")  ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="2">Change of use</option></select>');
 				   }
 				   else if (cscUserRole!=null && cscUserRole !="" && billcollector!=null &&  billcollector!="" && full.waterTaxDue > 0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="2">Change of use</option><option value="6">Collect Charge</option></select>');
 				   }
 				   else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") ) &&  full.waterTaxDue==0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="2">Change of use</option><option value="11">View DCB Screen</option></select>');
       				}
 				   
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ) && full.connectiontype =='METERED'&&  (full.closureType==null || full.closureType=="") && full.waterTaxDue > 0  ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if ((( billcollector==null || billcollector=="") && ( ulbUserRole!=null &&  ulbUserRole!="")) && full.connectiontype =='METERED'&&  (full.closureType==null || full.closureType=="")   ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ) && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") && (full.waterTaxDue > 0||full.waterTaxDue == 0)) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option><option value="6">Collect Charge</option></select>');
 				   }
 				   else if (((billcollector==null ||  billcollector=="") && ( ulbUserRole!=null &&  ulbUserRole!=""))  && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( (billcollector==null ||  billcollector=="") && ulbUserRole!=null &&  ulbUserRole!="")) && full.connectiontype =='METERED'&&  (full.closureType==null || full.closureType=="")  && full.waterTaxDue==0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( (billcollector==null ||  billcollector=="") && ulbUserRole!=null &&  ulbUserRole!="")) && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") && full.waterTaxDue==0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   
 				  
 				   
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector==""))) && full.closureType =='P') {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 				   }
 				   
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector=="")) )  && full.closureType =='T') {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">Reconnection</option></select>');
 				   }
 				   else if(cscUserRole!=null && cscUserRole!=""){
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option></select>');

 				   }
 				   
 				   else if(((superUserRole!=null && superUserRole!=""  ) || (administratorRole!=null && administratorRole!="")) && full.islegacy ==true) {
     				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option><option value="13">Add/Edit DCB</option><option value="14">Modify Connection Details</option</select>');
     			   }
     			   else if(superUserRole!=null && superUserRole!="" ){
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 			   } else if (((cscUserRole!=null && cscUserRole!="" )||( ulbUserRole!=null &&  ulbUserRole!="") )
 					   && full.status == 'DISCONNECTED') {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="10">Reconnection</option></select>');
 			   }
 			   else if(superUserRole!=null && full.status == 'DISCONNECTED') {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   }
 			   
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!=""))&& full.status == 'CLOSED' && full.closureType=='T'  ) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="10">ReConnection</option></select>');
				   }
				   
 		   }
 		   if (full != null&& full != undefined&& full.applicationcode != undefined
 				   && full.applicationcode == 'NEWCONNECTION') {
 			   if (full.status == 'ACTIVE') {
 				   if ( citizenRole== 'true'   ) { 
 					   return ('<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Pay</button>&nbsp;');   
 					  
 				   }
 				   else if ((billcollector!=null &&  billcollector!="" && (ulbUserRole == null || ulbUserRole =="") && (cscUserRole==null || cscUserRole==""))    && full.waterTaxDue > 0  ) {
 					  
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
 				   }
 				   else if (((billcollector==null ||  billcollector=="") && cscUserRole!=null &&  cscUserRole!="") ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Additional connection</option><option value="2">Change of use</option></select>');
 				   }
 				   else if ((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") && full.waterTaxDue > 0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="6">Collect Charge</option></select>');
 				   }
 				  
 				   
 				   else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") ) &&  full.waterTaxDue==0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((billcollector==null ||  billcollector=="") &&  ulbUserRole!=null &&  ulbUserRole!="")  && full.connectiontype =='METERED' && (full.closureType==null || full.closureType=="") ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ) && full.connectiontype =='METERED' &&  (full.closureType==null || full.closureType=="") && full.waterTaxDue > 0 ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   
 				   else if (((billcollector!=null &&  billcollector!="") &&  ulbUserRole!=null &&  ulbUserRole!="") && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") && (full.waterTaxDue > 0||full.waterTaxDue == 0)) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((billcollector==null ||  billcollector=="") &&  ulbUserRole!=null &&  ulbUserRole!="") && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") ) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( (billcollector==null ||  billcollector=="") && ulbUserRole!=null &&  ulbUserRole!="")) && full.connectiontype =='METERED' &&  (full.closureType==null || full.closureType=="") && full.waterTaxDue ==0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector==""))) && full.connectiontype !='METERED' && (full.closureType==null || full.closureType=="") && full.waterTaxDue == 0) {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="1">Additional connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 				   }
 				  else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector=="")))  && full.closureType =='T') {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">Reconnection</option></select>');
 				   }
 				  else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector==""))) && full.closureType =='P') {
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 				   }
 				  else if(((superUserRole!=null && superUserRole!=""  ) || (administratorRole!=null && administratorRole!="")) && full.islegacy ==true) {
     				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option><option value="13">Add/Edit DCB</option><option value="14">Modify Connection Details</option</select>');
     			   }
 				   else if(superUserRole!=null && superUserRole!="" ){
 					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option></select>');
 				   }

 			   } else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") ||( cscUserRole!=null &&  cscUserRole!=""))  && full.status == 'DISCONNECTED') {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="10">Reconnection</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector==""))) && full.status == 'CLOSED' && full.closureType=='T'  ) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">ReConnection</option></select>');
				   }
 			   else if ((((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && (billcollector==null ||  billcollector==""))) || (superUserRole!=null && superUserRole!="")) && full.status == 'DISCONNECTED') {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   }
 		   }	
 		   if (full != null&& full != undefined && full.applicationcode != undefined &&
 				   full.applicationcode == 'CHANGEOFUSE') {
 			  
 			   if ( citizenRole== 'true'   ) { 
					  return ('<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Pay</button>&nbsp;');   
					   
				   }
 			   else if ((billcollector!=null &&  billcollector!="" && (ulbUserRole == null || ulbUserRole =="") && (cscUserRole==null || cscUserRole=="")) && full.waterTaxDue > 0  ) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
				   }
 			   else if ((cscUserRole!=null &&  cscUserRole!="" && (billcollector==null ||  billcollector=="") )  && full.status == 'ACTIVE' && full.waterTaxDue > 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option></select>');
 			   }
 			   else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") )  && full.status == 'ACTIVE' && full.waterTaxDue > 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
 			   }
 			   else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") )  && full.status == 'ACTIVE' && full.waterTaxDue == 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ) && full.status == 'ACTIVE' && full.connectiontype =='METERED' && (full.closureType==null || full.closureType=="") && full.waterTaxDue > 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (( ulbUserRole!=null &&  ulbUserRole!="") && full.status == 'ACTIVE' && full.connectiontype =='METERED' && (full.closureType==null || full.closureType=="") ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if ((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="")  &&  full.status == 'ACTIVE' && full.connectiontype !='METERED'  && (full.closureType==null || full.closureType=="") && (full.waterTaxDue > 0||full.waterTaxDue == 0)) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="9">Closure of Connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (( ulbUserRole!=null &&  ulbUserRole!="" ) &&  full.status == 'ACTIVE' && full.connectiontype !='METERED'  && (full.closureType==null || full.closureType=="") ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="2">Change of use</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector==""))) && full.status == 'ACTIVE' && full.connectiontype =='METERED' && (full.closureType==null || full.closureType=="") && full.waterTaxDue == 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (ulbUserRole!=null && ulbUserRole!="" && ( billcollector==null ||  billcollector=="") &&  full.status == 'ACTIVE' && full.connectiontype !='METERED'  && (full.closureType==null || full.closureType=="") && full.waterTaxDue == 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="9">Closure of Connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||(  ( billcollector==null ||  billcollector=="") && ulbUserRole!=null &&  ulbUserRole!=""))  && full.closureType =='T') {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">Reconnection</option></select>');
				   }
 			   
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||(  ( billcollector==null ||  billcollector=="") && ulbUserRole!=null &&  ulbUserRole!=""))  && full.closureType =='P') {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
				   }
 			   else if(superUserRole!=null && superUserRole!=""){
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if(((superUserRole!=null && superUserRole!=""  ) || (administratorRole!=null && administratorRole!="")) && full.islegacy ==true) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option><option value="13">Add/Edit DCB</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector==""))) && full.status == 'CLOSED' && full.closureType=='T' ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">ReConnectionn</option></select>');
 			   }
 			  
 		   } 
 		   if (full != null&& full != undefined && full.applicationcode != undefined &&
 				   full.applicationcode == 'RECONNECTION') {
 			   if ( citizenRole== 'true'   ) { 
					   return ('<button type="button" class="btn btn-xs btn-secondary collect-hoardingWiseFee"><span class="glyphicon glyphicon-edit"></span>&nbsp;Pay</button>&nbsp;');   
					   
				   }
 			   else if ((billcollector!=null &&  billcollector!="" && (ulbUserRole == null || ulbUserRole =="") && (cscUserRole==null || cscUserRole=="")) && full.waterTaxDue > 0  ) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
				   }
 			   else if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") )  && full.status == 'ACTIVE' && full.waterTaxDue > 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ) && full.status == 'ACTIVE' && full.connectiontype =='METERED'  && full.waterTaxDue > 0 ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector=="")) && full.status == 'ACTIVE' && full.connectiontype =='METERED'   ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") )  && full.status == 'ACTIVE' && full.closureType =='T' && full.waterTaxDue > 0) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="6">Collect Charge</option><option value="11">View DCB Screen</option></select>');
				   }
 			   else if (( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector==""))  && full.status == 'ACTIVE' && full.closureType =='T' ) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option></select>');
				   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector==""))) && full.status == 'ACTIVE' && full.connectiontype =='METERED' && full.waterTaxDue == 0) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="8">Enter Meter Reading</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector=="")))  && full.status == 'ACTIVE' && full.closureType =='T' && full.waterTaxDue == 0) {
					   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
				   }
 			   else if(superUserRole!=null && superUserRole!=""){
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option></select>');
 			   }
 			   else if(((superUserRole!=null && superUserRole!=""  ) || (administratorRole!=null && administratorRole!="")) && full.islegacy ==true) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="11">View DCB Screen</option><option value="13">Add/Edit DCB</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ( billcollector==null ||  billcollector=="") &&  ulbUserRole!=null &&  ulbUserRole!="")) && full.status == 'CLOSED' && full.closureType=='T' ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   }
 		   } 
 		   if (full != null&& full != undefined && full.applicationcode != undefined &&
 				   full.applicationcode == 'CLOSINGCONNECTION') {
 			   if (((cscUserRole!=null &&  cscUserRole!="" && billcollector!=null &&  billcollector!="") ||( cscUserRole!=null &&  cscUserRole!=""))  && full.status == 'CLOSED' ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option>option value="0">View water tap connection</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector==""))) && full.status == 'CLOSED' && full.closureType =='P' ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   }
 			  
 			   else if(superUserRole!=null && superUserRole!=""){
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   }
 			   else if (((ulbUserRole!=null &&  ulbUserRole!="" && billcollector!=null &&  billcollector!="") ||( ulbUserRole!=null &&  ulbUserRole!="" && ( billcollector==null ||  billcollector=="")))&& full.status == 'CLOSED' && full.closureType=='T' ) {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option><option value="10">Reconnection</option></select>');
 			   }
 		   } 
 		   
 		   if (full!=null && full !=undefined ){
 			   if((full.status == 'CLOSED'|| full.status == 'HOLDING'|| (approverUserRole!=null && approverUserRole!=""))) { // Assistant
 				   // Engineer,Commitioner
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="0">View water tap connection</option></select>');
 			   } else if (operatorRole!=null &&operatorRole!="") { // Collection
 				   // Operator
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option><option value="6">Collect Charge</option></select>');
 			   } 
 			   else
 			   {
 				   return ('<select class="dropchange" id="additionconn" ><option>Select from Below</option></select>');

 			   }
 		   }

 	   
 	   }

		} 
		        ],
				
	});
	
	if(tableContainer.fnGetData().length > 1000){
		$('#searchResultDiv').hide();
		$('#search-exceed-msg').show();
	/*	$('#search-exceed-count').html(tableContainer.fnGetData().length);*/
	}else{
		$('#search-exceed-msg').hide();
		$('#searchResultDiv').show();
	}
	
	
	})
}
jQuery('.patternvalidationclass').on("input", function(){
	//This will allow you to enter alphabet with space, dot(.), and comma(,).  (eg. datapattern="alphabetwithspaceanddotandcomma")
	var regexp_alphabetdotcomma = /[^a-zA-Z1-9 .,]/g;
	if(jQuery($(this)).val().match(regexp_alphabetdotcomma)){
	jQuery($(this)).val( jQuery($(this)).val().replace(regexp_alphabetdotcomma,'') );
	}

	});
