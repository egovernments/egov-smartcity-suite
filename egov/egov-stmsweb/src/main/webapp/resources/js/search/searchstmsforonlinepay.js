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

var tableContainer;
jQuery(document).ready(
		function($) {
			tableContainer = $("#aplicationSearchResults");
			document.onkeydown = function(evt) {
				var keyCode = evt ? (evt.which ? evt.which : evt.keyCode)
						: event.keyCode;
				if (keyCode == 13) {
					submitButton();
				}
			}

			$('.slide-history-menu').click(
					function() {
						$(this).parent().find('.history-slide').slideToggle();
						if ($(this).parent().find('#toggle-his-icon').hasClass(
								'fa fa-angle-down')) {
							$(this).parent().find('#toggle-his-icon')
									.removeClass('fa fa-angle-down').addClass(
											'fa fa-angle-up');
							// $('#see-more-link').hide();
						} else {
							$(this).parent().find('#toggle-his-icon')
									.removeClass('fa fa-angle-up').addClass(
											'fa fa-angle-down');
							//$('#see-more-link').show();
						}
					});
		});

$(".btnSearch").click(function(event) {
	$('#searchSewerageapplication').show();

	submitButton();

});

var prevdatatable;
var oTable;
function submitButton() {

	oTable = $("#aplicationSearchResults");
	if (prevdatatable) {
		prevdatatable.fnClearTable();
		$('#aplicationSearchResults thead tr').remove();
	}

	prevdatatable = oTable
			.dataTable({
				dom : "<'row'<'col-xs-4 pull-right'f>r>t<'row add-margin'<'col-md-3 col-xs-6'i><'col-md-2 col-xs-6'l><'col-md-2 col-xs-6 text-right'B><'col-md-5 col-xs-6 text-right'p>>",
				processing : true,
				serverSide : true,
				sort : true,
				filter : true,
				"searching" : false,
				"autoWidth" : false,
				"bDestroy" : true,
				ajax : {
					url : "/stms/citizen/search/onlinepayment",
					type : 'POST',
					data : function(args) {
						return {
							"args" : JSON.stringify(args),
							"consumerNumber" : $('#consumerNumber').val(),
							"shscNumber" : $('#shscNumber').val(),
						};
					},
				},
				columns : [
						{
							title : "Application Number",
							data : "applicationNumber"
						},
						{
							title : "pt assesmentno",
							data : "assessmentNumber",
							visible : false
						},
						{
							title : 'S.H.S.C Number',
							data : 'shscNumber'
						},
						{
							title : "Applicant Name",
							data : "applicantName"
						},
						{
							title : "Actions",
							"data" : "",
							"target" : -1,
							"defaultContent" : '<button type="button" class="btn btn-xs btn-secondary fa-demandCollection"><span class="glyphicon glyphicon-edit"></span>&nbsp;View DCB</button>&nbsp;<button type="button" class="btn btn-xs btn-secondary paynow"><span class="glyphicon glyphicon-edit "></span>&nbsp;Pay Now</button>'
						}

				],
			});
}

$("#aplicationSearchResults").on(
		'click',
		'tbody tr td .fa-demandCollection',
		function(e) {
			var consumerno = oTable.fnGetData($(this).parent().parent(), 0);
			var assessmentno = oTable.fnGetData($(this).parent().parent(), 1);
			window.open("/stms/citizen/search/sewerageRateReportView/"
					+ consumerno + '/' + assessmentno + '', '_blank',
					'width=900, height=700, top=300, left=150,scrollbars=yes')
		});

$("#aplicationSearchResults").on('click', 'tbody tr td .paynow', function(e) {
	var consumerno = oTable.fnGetData($(this).parent().parent(), 0);
	var assessmentno = oTable.fnGetData($(this).parent().parent(), 1);
	var url = '/stms/citizen/search/sewerageGenerateonlinebill/';
	openPopupPage(url + consumerno + '/' + assessmentno);
});

function openPopupPage(relativeUrl) {
	OpenWindowWithPost(relativeUrl,
			"width=1000, height=600, left=100, top=100, resizable=yes, scrollbars=yes");
}

function OpenWindowWithPost(url, windowoption) {
	var form = document.createElement("form");
	form.setAttribute("action", url);
	form.setAttribute("method", "post");
	document.body.appendChild(form);
	form.submit();
}
