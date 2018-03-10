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
jQuery('#searchservice').click(function(e) {
	console.log($('#moduleName').val());
	if($('#moduleName').val()=='' ||  $('#assessmentNo').val()==''){
		bootbox.alert("please enter both service name and Identifier Number");
	e.preventDefault();
	}
	else
		callAjaxViewSearch();
	
	
	
});

var reportdatatable;
function callAjaxViewSearch(){
	$.post("/portal/citizen/searchresult", $('#serviceSearchRequestForm').serialize())
	.done(function (response) {
		var data= []; 
		data.push(JSON.parse(response).data);
		console.log(JSON.parse(response).data)
		console.log(data);
		tableContainer=$('#SearchResults');
		reportdatatable = tableContainer.dataTable({
			destroy:true,
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"autoWidth": false,
			data:data,
			columns: [
			{title: 'Consumer Number', data: "consumerNo"},
			{title: 'Owner Name', data: "ownerName"},
			{title: 'Service Name', data: "serviceName","bVisible": true},
			
			{title:'action', "data" : null, "sClass" : "text-center", "target":-1,"bVisible": true,
			    sortable: false,
			    "render": function ( data, type, full, meta ) {
			    	var consumerNo = data.consumerNo;
			    	if(consumerNo!="")
			       	return '<button type="button" id ="viewbutton" class="btn btn-primary viewbutton"><i class="fa fa-plus" aria-hidden="true"></i>&nbsp;&nbsp;LINK</button>';
			    }
			}]
			
		});
		
	});
}


$("#SearchResults").on('click','tbody tr td  .viewbutton',function(event) {
	var moduleName = reportdatatable.fnGetData($(this).parent().parent(),2);
	var applicantName = reportdatatable.fnGetData($(this).parent().parent(),1);
	var consumerCode = reportdatatable.fnGetData($(this).parent().parent(),0);
	if(consumerCode=='' || consumerCode === null){
		bootbox.alert("you can't link the application which is under process")
		
	}
	else{
	bootbox.confirm({
						    message: "Do you want to link\n" +
						    		"consumer Number " + consumerCode  + 
						    		" with applicant Name "+ applicantName +
						    		"\nto your account",
						    buttons: {
						        confirm: {
						            label: 'Yes',
						            className: 'btn-primary'
						        },
						        cancel: {
						            label: 'No',
						            className: 'btn-danger'
						        }
						    },
						    callback: function(result) {
								if (result) {
									openPopupPage("/portal/citizen/linkconnection/",consumerCode,moduleName,applicantName);
								} else {
									event.stopPropagation();
									event.preventDefault();
									
								}
							}
						});
	
	}
	
});

function openPopupPage(relativeUrl,consumerCode,moduleName,applicantName)
{
 var param = { 'consumerCode' : consumerCode, 'moduleName': moduleName ,'applicantName':applicantName };
 OpenWindowWithPost(relativeUrl, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes", param);
}
 

function OpenWindowWithPost(url, windowoption,params)
{
	 var form = document.createElement("form");
	 form.setAttribute("action", url);
	// form.setAttribute("target", name);
	 form.setAttribute("method", "post");
	 for (var i in params)
	 {
	   if (params.hasOwnProperty(i))
	   {
	     var input = document.createElement('input');
	     input.type = 'hidden';
	     input.name = i;
	     input.value = params[i];
	     form.appendChild(input);
	   }
	 }
	 document.body.appendChild(form);
	 form.submit();
	}


