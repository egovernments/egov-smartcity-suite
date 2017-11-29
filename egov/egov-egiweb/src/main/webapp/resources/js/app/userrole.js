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

function populateUserRoles(dropdown) {
	populaterolesSelect({
		username : dropdown.value
	});
}

$(document).ready(
		function() {
			if ($('#currentroles').size > 0) {
				jQuery("#currentroles option[value!='']").each(function() {
					var currRolVal = jQuery(this).val();
					jQuery("#roles option[value!='']").each(function() {
						if (jQuery(this).val() == currRolVal)
							jQuery(this).prop('selected', true);
					});
				});
			}

			var userlist = new Bloodhound({
				datumTokenizer : function(datum) {
					return Bloodhound.tokenizers.whitespace(datum.value);
				},
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				remote : {
					url : '/egi/userRole/ajax/userlist?userName=%QUERY',
					filter : function(data) {
						// Map the remote source JSON array to a JavaScript
						// object array
						return $.map(data, function(u) {
							return {
								name : u.Text,
								value : u.Value
							};
						});
					}
				}
			});

			userlist.initialize();

			var user_typeahead = $('#user_name').typeahead({
				hint : true,
				highlight : true,
				minLength : 3
			}, {
				displayKey : 'name',
				source : userlist.ttAdapter()
			}).on('typeahead:selected', function(event, data) {
				populateUserRoles(this);
			});
			typeaheadWithEventsHandling(user_typeahead, '#usernameId',
					'#rolesSelect');

			$('#multiselect > option:selected').each(
					function() {
						var opt = '<option value="' + $(this).val() + '">'
								+ $(this).text() + '</option>';
						$('#multiselect_to').append(opt);
						$(this).remove();

			});
			
			$('#multiselect').removeAttr('name');

		});