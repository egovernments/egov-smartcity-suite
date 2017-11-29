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
var response = $('#photographStages').val();
var before_files = [];
var after_files = [];
var during_files = [];

$.each(JSON.parse(response), function(key, value){
	if(key == "before"){
		before_files = value;
	}
	if(key == "after"){
		after_files = value;
	}
	if(key == "during"){
		during_files = value;
	}
});

//console.log(JSON.stringify(before_files));

if(before_files.length === 0)
	$('#before_empty').show();

if(during_files.length === 0)
	$('#during_empty').show();

if(after_files.length === 0)
	$('#after_empty').show();

var ledId = $('#ledId').val();
//Example 2
$(document).ready(function(){
$("#before").filer({
	changeInput: '<div></div>',
	showThumbs: true,
	theme: "dragdropbox",
	files : before_files,
	templates: {
		box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
				itemAppend: '<li class="jFiler-item">\
				                           <div class="jFiler-item-container">\
				                               <div class="jFiler-item-inner">\
				                                   <div class="jFiler-item-thumb">\
													<div class="jFiler-item-status"></div>\
													<div class="jFiler-item-thumb-overlay">\
								                        <div class="jFiler-item-info">\
								                        	<div style="display:table-cell;vertical-align: middle;">\
								<span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
								</div>\
								                    </div>\
								                    </div>\
					 									<div id="links"><a href="{{fi-url}}" data-gallery>{{fi-image}}</a></div>\
				                                   </div>\
				                               </div>\
				                           </div>\
				                       </li>',
				_selectors: {
				list: '.jFiler-items-list',
				item: '.jFiler-item',
				}
				}
});

$("#during").filer({
	changeInput: '<div></div>',
	showThumbs: true,
	theme: "dragdropbox",
	files : during_files,
	templates: {
		box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
				itemAppend: '<li class="jFiler-item">\
				                           <div class="jFiler-item-container">\
				                               <div class="jFiler-item-inner">\
				                                   <div class="jFiler-item-thumb">\
													<div class="jFiler-item-status"></div>\
													<div class="jFiler-item-thumb-overlay">\
								                        <div class="jFiler-item-info">\
								                        	<div style="display:table-cell;vertical-align: middle;">\
								<span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
								</div>\
								                    </div>\
								                    </div>\
					 									<div id="links"><a href="{{fi-url}}" data-gallery>{{fi-image}}</a></div>\
				                                   </div>\
				                               </div>\
				                           </div>\
				                       </li>',
				_selectors: {
				list: '.jFiler-items-list',
				item: '.jFiler-item',
				}
				}
});

$("#after").filer({
	changeInput: '<div></div>',
	showThumbs: true,
	theme: "dragdropbox",
	files : after_files,
	templates: {
		box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
				itemAppend: '<li class="jFiler-item">\
				                           <div class="jFiler-item-container">\
				                               <div class="jFiler-item-inner">\
				                                   <div class="jFiler-item-thumb">\
													<div class="jFiler-item-status"></div>\
													<div class="jFiler-item-thumb-overlay">\
								                        <div class="jFiler-item-info">\
								                        	<div style="display:table-cell;vertical-align: middle;">\
								<span class="jFiler-item-title"><b title="{{fi-name}}">{{fi-name | limitTo: 25}}</b></span>\
								</div>\
								                    </div>\
								                    </div>\
					 									<div id="links"><a href="{{fi-url}}" data-gallery>{{fi-image}}</a></div>\
				                                   </div>\
				                               </div>\
				                           </div>\
				                       </li>',
				_selectors: {
				list: '.jFiler-items-list',
				item: '.jFiler-item',
				}
				}
});
});

function openLineEstimate(lineEstimateId) {
	window.open("/egworks/lineestimate/view/" + lineEstimateId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}
function openLOA(workOrderId) {
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

