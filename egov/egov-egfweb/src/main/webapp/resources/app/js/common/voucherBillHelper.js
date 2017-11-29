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

var $schemeId = 0;
var $subSchemeId = 0;
var $fundSourceId = 0;
var $fundId = 0;
$(document).ready(function(){
	$schemeId = $('#schemeId').val();
	$subSchemeId = $('#subSchemeId').val();
	$fundSourceId = $('#fundSourceId').val();
	$fundId = $('#fundId').val();
	if($fundId)
		$("#fund").val($fundId).prop('selected','selected');
	if($fundSourceId)
		$("#fundSource").val($fundSourceId).prop('selected','selected');
	loadScheme($('#fund').val());
	loadSubScheme($schemeId);
	var functionName = new Bloodhound({
		datumTokenizer : function(datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		remote : {
			url : '/EGF/common/ajaxfunctionnames?name=%QUERY',
			filter : function(data) {
				return $.map(data, function(ct) {
					return {
						code:ct.split("~")[0].split("-")[0],
						name : ct.split("~")[0].split("-")[1],
						id : ct.split("~")[1],
						codeName:ct
					};
				});
			}
		}
	});

	functionName.initialize();
$('#function').typeahead({
		hint : true,
		highlight : true,
		minLength : 3
	}, {
		displayKey : 'codeName' ,
		source : functionName.ttAdapter()
	}).on('typeahead:selected', function (event, data) {
		$(".cfunction").val(data.id);
	});
	
});
$('#function').blur(function () {
	if($('.cfunction').val()=="")
	{
		 bootbox.alert("Please select function from dropdown values",function() {
			 	$('#function').val("");
			});
	}
});

function loadScheme(fundId){
	if (!fundId) {
		$('#scheme').empty();
		$('#scheme').append($('<option>').text('Select from below').attr('value', ''));
		$('#subScheme').empty();
		$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getschemesbyfundid",
			data : {
				fundId : fundId
			},
			async : true
		}).done(
				function(response) {
					$('#scheme').empty();
					$('#scheme').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($schemeId && $schemeId==value.id)
						{
								selected="selected";
						}
						$('#scheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});

	}
}

function loadSubScheme(schemeId){
	if (!schemeId) {
		$('#subScheme').empty();
		$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
		return;
	} else {
		
		$.ajax({
			method : "GET",
			url : "/EGF/common/getsubschemesbyschemeid",
			data : {
				schemeId : schemeId
			},
			async : true
		}).done(
				function(response) {
					$('#subScheme').empty();
					$('#subScheme').append($("<option value=''>Select from below</option>"));
					$.each(response, function(index, value) {
						var selected="";
						if($subSchemeId && $subSchemeId==value.id)
						{
								selected="selected";
						}
						$('#subScheme').append($('<option '+ selected +'>').text(value.name).attr('value', value.id));
					});
				});
		
	}
}

$('#fund').change(function () {
	$schemeId = "";
	$subSchemeId = "";
	$('#scheme').empty();
	$('#scheme').append($('<option>').text('Select from below').attr('value', ''));
	$('#subScheme').empty();
	$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
	loadScheme($('#fund').val());
});


$('#scheme').change(function () {
	$('#subScheme').empty();
	$('#subScheme').append($('<option>').text('Select from below').attr('value', ''));
	loadSubScheme($('#scheme').val());
});