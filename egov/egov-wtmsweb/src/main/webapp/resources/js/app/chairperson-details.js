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

$(document).ready(function(){
	loadingTable();	
	
	//add chairperson
	$('#buttonid').click(function() {
		  if ($( "#chairPersonDetailsform" ).valid())
		  {
			var name = $('input').val();
		       if (name != ''){
		        $.ajax({
		            url: '/wtms/application/ajax-activeChairPersonExistsAsOnCurrentDate',
		            type: "GET",
		            data: {
		            	name: name
		            },
		            dataType : 'json',
		            success: function (response) {
		    			console.log("success"+response);
		    			if(response==true){
			    				addChairPerson();
			    				loadingTable();
			    			}
		    			else{
		    				overwritechairperson();
		    				loadingTable();
		    			}
		    		},error: function (response) {
		    			console.log("failed");
		    		}
		        });
		        
		       }
		  }
		});
});

function loadingTable()
{
	tableContainer = $("#chairperson-table");
	tableContainer.dataTable({
		//processing : true,
		serverSide : true,
		type : 'GET',
		sort : true,
		filter : true,
		responsive : true,
		destroy : true,
		"sPaginationType" : "bootstrap",
		"sDom" : "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
		"autoWidth" : false,
		 ajax : "/wtms/application/ajax-chairpersontable",
		 columns : [ {
				"sTitle" : "S.no",
			},
			{
				"mData" : "chairPerson",
				"sTitle" : "Chair Person Name",
			}, 
			 {
				"mData" : "fromDate",
				"sTitle" : "From Date",
			},{
				"mData" : "toDate",
				"sTitle" : "To Date"
			},{
				"mData" : "status",
				"sTitle" : "Status"
			}],
			"fnInitComplete": function(oSettings, json) {
				$('#chairperson-table tbody tr:eq(0) td:last').addClass('error-msg view-content');
			},
			"fnRowCallback" : function(nRow, aData, iDisplayIndex){
                $("td:first", nRow).html(iDisplayIndex +1);
               return nRow;
            }
	});
}

function overwritechairperson()
{
	 if(confirm("On entered date chairperson name is already present, do you want to overwrite it?"))
	 {
		 addChairPerson();
		 
	 }
	 else{
		 console.log("not added");
		 
	 }
}

function addChairPerson()
{
	 $.ajax({
            url: '/wtms/application/ajax-addChairPersonName',
            type: "GET",
            data: {
            	name: $('#name').val()
            },
            dataType : 'json'
        });
	 
	 bootbox.alert("Chair person name updated in drop down successfully");
   
	}