/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
$(document)
		.ready(
				function() {
					$("#dragdiv li").draggable({
						helper : "clone",
						cursor : "move",
						revert : "invalid"
					});

					initDroppable($("#message"));

					function initDroppable($elements) {
						$elements.droppable({
							activeClass : "ui-state-default",
							hoverClass : "ui-drop-hover",
							accept : ":not(.ui-sortable-helper)",

							over : function(event, ui) {
							},
							drop : function(event, ui) {
								var $this = $(this);
								$this.val($this.val() + "{{"
										+ ui.draggable.text().trim() + "}}");
							}
						});
					}

					$('#category')
							.change(
									function() {
										$
												.ajax({
													url : '/api/draft/getCategoriesForModule/'
															+ $('#category')
																	.val(),
													dataType : 'json',
													type : 'GET',
													// This is query string i.e.
													// country_id=123
													success : function(data) {
														$('#subCategory').empty(); // clear
																				// the
																				// current
																				// elements
																				// in
																				// select
																				// box
														$('#subCategory')
																.append(
																		$('<option value="">Select</option>'));
														$
																.each(
																		data.result,
																		function(
																				i,
																				obj) {
																			$(
																					'#subCategory')
																					.append(
																							$('<option value="'
																									+ obj.id
																									+ '">'
																									+ obj.name
																									+ '</option>'));
																		});
													},
													error : function(jqXHR,
															textStatus,
															errorThrown) {
														// alert(errorThrown);
													}
												});
									});

					$('#subCategory')
							.change(
									function() {
										$
												.ajax({
													url : '/api/draft/getParametersForCategory/'
															+ $('#subCategory')
																	.val(),
													dataType : 'json',
													type : 'GET',
													// This is query string i.e.
													// country_id=123
													success : function(data) {
														$('#allItems').empty(); // clear
																				// the
																				// current
																				// elements
																				// in
																				// select
																				// box
														$
																.each(
																		data.result,
																		function(
																				i,
																				obj) {
																			$(
																					'#allItems')
																					.append(
																							$('<li class="li eachParameter" id="node">'
																									+ obj.name
																									+ '</li>'));
																		});

														$("#dragdiv li")
																.draggable(
																		{
																			helper : "clone",
																			cursor : "move",
																			revert : "invalid"
																		});
													},
													error : function(jqXHR,
															textStatus,
															errorThrown) {
														// alert(errorThrown);
													}
												});
									});
					$(".btn-primary").click(
							function(event) {
								if ($("form").valid()) {
									document.getElementById("updateDraftForm")
											.submit();
								} else {
									event.preventDefault();
								}
								return true;
							});

				});