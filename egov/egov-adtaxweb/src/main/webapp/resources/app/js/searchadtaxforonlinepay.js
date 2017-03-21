/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
	try{
		$.fn.dataTable.moment( 'DD/MM/YYYY' );
	}catch(e){
		
	}
	
	$("#paynow").click(function(e){ 
		var url = '/adtax/citizen/search/generateonlinebill/'+ $('#hoardingid').val();
		$('#hoardingformview').attr('method', 'post');
		$('#hoardingformview').attr('action', url);
		$('#hoardingformview').attr('name', 'myform');
		document.forms["myform"].submit();

	});
	
	var agency = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/adtax/citizen/search/active-agencies?name=%QUERY',
			filter: function (data) {
				return $.map(data, function (ct) {
					return {
						name: ct.name,
						value: ct.id
					};
				});
			}
		}
	});
	
   agency.initialize(); // Instantiate the Typeahead UI
	 
   var agency_typeahead=$('#agencyTypeAhead').typeahead({
	   hint:true,
	   highlight:true,
	   minLength:1
   },
   {
	   displayKey : 'name',
	   source: agency.ttAdapter()
   });
   typeaheadWithEventsHandling(agency_typeahead,'#agencyId');
   
	var datadcbtbl = $('#search-dcbresult-table');
	$('#search-dcb').click(function(e){
		datadcbtbl.dataTable({
			"ajax": {url:"/adtax/reports/search-for-dcbreport?"+$("#hoardingsearchform").serialize(),
				type:"POST"
			},
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"columns" : [
			  { "data" : "id","visible" : false, "searchable": false},
		      { "data" : "advertisementNumber", "title":"Advertisement No."},
			  { "data" : "agencyName", "title": "Agency"},
			  { "data" : "ownerDetail", "title": "Owner Details"},
			  { "data" : "","title": "Actions", "target":-1,"defaultContent": '<button type="button" class="btn btn-xs btn-secondary fa-demandCollection"><span class="glyphicon glyphicon-edit"></span>&nbsp;View DCB</button>&nbsp;<button type="button" class="btn btn-xs btn-secondary paynow"><span class="glyphicon glyphicon-edit "></span>&nbsp;Pay Now</button>'}			 
			  ]
		});
		e.stopPropagation();
	});
	
	$("#search-dcbresult-table").on('click','tbody tr td .fa-demandCollection',function(e) {
		var hoardingId = datadcbtbl.fnGetData($(this).parent().parent(),0);
		window.open("getHoardingDcb/"+hoardingId, ''+hoardingId+'', 'width=900, height=700, top=300, left=150,scrollbars=yes')
	});
	
	$("#search-dcbresult-table").on('click','tbody tr td .paynow',function(e) {
		var hoardingId = datadcbtbl.fnGetData($(this).parent().parent(),0);
		var url = '/adtax/citizen/search/generateonlinebill/';
		openPopupPage(url+hoardingId);
	});
	
	
	
	
	
	
	function openPopupPage(relativeUrl)
	{
	 OpenWindowWithPost(relativeUrl, "width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes");
	}
	 
	 
	function OpenWindowWithPost(url, windowoption)
	{
	 var form = document.createElement("form");
	 form.setAttribute("action", url);
	 form.setAttribute("method", "post");
	 document.body.appendChild(form);
	 form.submit();
	}

		
});

