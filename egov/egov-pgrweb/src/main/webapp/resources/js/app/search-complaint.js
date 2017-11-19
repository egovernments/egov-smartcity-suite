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
 $(document).ready(
		function() {
			if ($('#isMore').val() == "true") {
				$(".form-group.show-searchcomp-more.display-hide").removeClass("display-hide");
			}
		});
 
var tableContainer;
	
    $('#toggle-searchcomp').click(function () {
        if ($(this).html() == "More..") {
            $(this).html('Less..');
            $('.show-searchcomp-more').show();
			} else {
            $(this).html('More..');
            $('.show-searchcomp-more').hide();
		}
		
	});
    
    function validateEmail($email) {
	  var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	  return emailReg.test( $email );
	}
    
    tableContainer1 = $("#complaintSearchResults"); 
    
    $('#searchComplaints').click(function () {
    	
    	if(DateValidation($('#start_date').val(), $('#end_date').val())){
    		
    		if( validateEmail($('#ct-email').val())) { 
    			var urlStr="";
            	if($('#currentLoggedUser').val()=='anonymous'){
               		urlStr="/pgr/complaint/citizen/anonymous/search";
            	}
            	else{
            		urlStr="/pgr/complaint/search";
            	}
            	
            	$.post(urlStr,$('#searchComplaintForm').serialize())
            	.done(function (searchResult) {

        			tableContainer1 = $('#complaintSearchResults').dataTable({
        				destroy:true,
        				"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 col-right'p>>",
        				"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        				"autoWidth": false,
        				"oTableTools": {
        					"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
        					"aButtons": [
        					             { "sExtends": "xls",
        					                 "mColumns": [0,1,2, 3, 4,5.6]},
        					             { "sExtends": "pdf",
        					                 "mColumns": [0,1,2, 3, 4,5.6]},
        					             { "sExtends": "print",
        					                 "mColumns": [0,1,2, 3, 4,5.6]},
        					             ]
        				},
        				searchable:true,
        				data: searchResult.content,
        				columns: [
        				{title: 'Complaint Number', data: 'crn'},
        				{title: 'Grievance Type', data: 'complaintTypeName'},
        				{title: 'Name', data: 'complainantName'},
        				{title: 'Location', data: 'wardName'},
        				{title: 'Status', data: 'complaintStatusName'},
        				{title: 'Department', data: 'departmentName'},
        				{title: 'Registration Date',
        					render: function (data, type, full) {
        						if(full!=null && full!= undefined && full.createdDate != undefined) {
        							var regDateSplit = full.createdDate.split("T")[0].split("-");		
        							return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
        						}
        						else return "";
        			    	}
        				},
        				{data:'assigneeId',visible: false}
        				]
        			});
        		});
    		}else{
    			bootbox.alert('Enter valid Email ID!');
    		}
    	}
	});
    
   tableContainer = $("#csearch").dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": ["copy", "csv", "xls", "pdf", "print"]
		}
	});

	$("#complaintSearchResults").on('click','tbody tr',function(event) {
		
		var crn=tableContainer1.fnGetData(this,0);
		var currentOwner=tableContainer1.fnGetData(this,7);
		var currentStatus=tableContainer1.fnGetData(this,4);
		var CurrentPosition=$('#employeeposition').val();
		var isgorole= $('#isgorole').val();
		if(((currentStatus == 'COMPLETED' && isgorole == 'false') || currentStatus == 'REJECTED'|| currentStatus == 'WITHDRAWN') && CurrentPosition != "0")
			window.open("/pgr/complaint/view/"+crn);
		else if (currentOwner == CurrentPosition || isgorole == 'true')
			window.open("/pgr/complaint/update/"+crn);
		else
			window.open("/pgr/complaint/view/"+crn);
	});
	
    $("#when_date").change(function () {
        populatedate($('#when_date').val());
	});
	
    function populatedate(id) {
        var d = new Date();
        var quarter = getquarter(d);
        var start, end;
        switch (id) {
			
			case "lastsevendays":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 7)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "lastthirtydays":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 30)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "lastninetydays":
			$("#end_date").datepicker("setDate", d);
			start = new Date(d.setDate((d.getDate() - 90)));
			var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
			$("#start_date").datepicker("setDate", start);
			break;
			
			case "today":
			$("#end_date").datepicker("setDate", d);
			$("#start_date").datepicker("setDate", d);
			break;
			
			case "all":
			$("#end_date").val("");
			$("#start_date").val("");
			break;
			
		}
	}
	
	function getquarter(d) {
		if (d.getMonth() >= 0 && d.getMonth() <= 2) {
            quarter = 4;
			} else if (d.getMonth() >= 3 && d.getMonth() <= 5) {
            quarter = 1;
			} else if (d.getMonth() >= 6 && d.getMonth() <= 8) {
			quarter = 2;
			} else if (d.getMonth() >= 9 && d.getMonth() <= 11) {
			quarter = 3;
		}
		
        return quarter;
	}

	
	$("form").submit(function(event){
		if($("select#when_date option:selected").index() == 0){
			
			}else{
			
			if($('#start_date').val() != '' && $('#end_date').val() != ''){
				var start = $('#start_date').val();
				var end = $('#end_date').val();
				
				if (start != "" && end != "") {
					var stsplit = start.split("/");
					var ensplit = end.split("/");
					
					start = stsplit[1] + "/" + stsplit[0] + "/" + stsplit[2];
					end = ensplit[1] + "/" + ensplit[0] + "/" + ensplit[2];
					
					ValidRange(start, end);
				}
				}else{
					bootbox.alert("Select the date");
			}
			
			event.preventDefault();
		}
	});
	
	$("#region").dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		//"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-xs-6 col-md-3 col-left'i><'col-xs-6 col-md-3 text-right col-left'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-xs-12 col-md-3 col-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": ["xls", "pdf", "print"]
		},
		"footerCallback": function ( row, data, start, end, display ) {
		    var api = this.api(), data;
		    updateTotalFooter(2, api);
		    updateTotalFooter(3, api);
		    updateTotalFooter(4, api);
		    updateTotalFooter(5, api);
		    updateTotalFooter(6, api);
		},
                "aoColumnDefs": [ {
		      "aTargets": [2,3,4,5,6],
		      "mRender": function ( data, type, full ) {
			return formatNumberInr(data);
		      }
		} ]
	});
	
	
	$("#department").dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": ["xls", "pdf", "print"]
		},
		"footerCallback": function ( row, data, start, end, display ) {
		    var api = this.api(), data;
		    updateTotalFooter(2, api);
		    updateTotalFooter(3, api);
		    updateTotalFooter(4, api);
		    updateTotalFooter(5, api);
		    updateTotalFooter(6, api);
		},
                "aoColumnDefs": [ {
		      "aTargets": [2,3,4,5,6],
		      "mRender": function ( data, type, full ) {
			return formatNumberInr(data);
		      }
		} ]
	});

    $('#department_wrapper').hide();
    
    $('#region a').click(function(e){
       $('#region_wrapper').hide();
       $('#department_wrapper').show();
       $('.panel-title').html('Department Wise Report');
    });
	
    $("#agingtable").dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egi/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": ["xls", "pdf", "print"]
		},
		"footerCallback": function ( row, data, start, end, display ) {
		    var api = this.api(), data;
		    updateTotalFooter(2, api);
		    updateTotalFooter(3, api);
		    updateTotalFooter(4, api);
		    updateTotalFooter(5, api);
		    updateTotalFooter(6, api);
		    updateTotalFooter(7, api);
		},
                "aoColumnDefs": [ {
		      "aTargets": [2,3,4,5,6,7],
		      "mRender": function ( data, type, full ) {
			return formatNumberInr(data);
		      }
		} ]
	});
    
    $("#complaintgrouptable").dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"autoWidth": false,
		"oTableTools": {
			"sSwfPath": "../../../../../../egov-egiweb/src/main/webapp/resources/global/swf/copy_csv_xls_pdf.swf",
			"aButtons": [
                                {
				    "sExtends": "csv",
				    "sFileName": "TableTools - *.csv"
				},
                                {
				    "sExtends": "pdf",
				    "sPdfMessage": "Your custom message would go here."
				},
                               "print"]
		},
		"footerCallback": function ( row, data, start, end, display ) {
		    var api = this.api(), data;
		    updateTotalFooter(1, api);
		    updateTotalFooter(2, api);
		    updateTotalFooter(3, api);
		    updateTotalFooter(4, api);
		    updateTotalFooter(5, api);
		    updateTotalFooter(6, api);
		},
                "aoColumnDefs": [ {
		      "aTargets": [1,2,3,4,5,6],
		      "mRender": function ( data, type, full ) {
			return formatNumberInr(data);
		      }
		} ]
	});
    
    function updateTotalFooter(colidx, api)
    {
    	// Remove the formatting to get integer data for summation
        var intVal = function ( i ) {
            return typeof i === 'string' ?
                i.replace(/[\$,]/g, '')*1 :
                typeof i === 'number' ?
                    i : 0;
        };

        // Total over all pages
        total = api
            .column(colidx)
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            } );

        // Total over this page
        pageTotal = api
            .column( colidx, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
        $( api.column( colidx ).footer() ).html(
            '<b>'+formatNumberInr(pageTotal) +' ('+ formatNumberInr(total) +')</b>'
        );
    }



//inr formatting number
function formatNumberInr(x){
	   if(x)
	   {
			x=x.toString();
			var afterPoint = '';
			if(x.indexOf('.') > 0)
			   afterPoint = x.substring(x.indexOf('.'),x.length);
			x = Math.floor(x);
			x=x.toString();
			var lastThree = x.substring(x.length-3);
			var otherNumbers = x.substring(0,x.length-3);
			if(otherNumbers != '')
			    lastThree = ',' + lastThree;
			var res = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree + afterPoint;
		    return res;
	   }
	   return x;
}
						
