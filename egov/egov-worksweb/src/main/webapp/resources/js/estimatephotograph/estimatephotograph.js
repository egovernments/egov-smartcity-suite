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

$(document).ready(function(){
	var ledId = $('#ledId').val();
			//Example 2
			$("#before").filer({
				fileMaxSize: null,
				extensions: ["jpg", "jpeg", "png"],
				changeInput: '<div class="jFiler-input-dragDrop"><div class="jFiler-input-inner"><div class="jFiler-input-icon"><i class="icon-jfi-cloud-up-o"></i></div><div class="jFiler-input-text"><h3>Drag&Drop files here</h3> <span style="display:inline-block; margin: 15px 0">or</span></div><a class="jFiler-input-choose-btn blue">Browse Files</a></div></div>',
				showThumbs: true,
				theme: "dragdropbox",
				templates: {
					box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
					item: '<li class="jFiler-item">\
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
											{{fi-image}}\
										</div>\
										<div class="jFiler-item-assets jFiler-row">\
											<ul class="list-inline pull-left">\
												<li>{{fi-progressBar}}</li>\
											</ul>\
											<ul class="list-inline pull-right">\
												<li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
											</ul>\
										</div>\
									</div>\
								</div>\
							</li>',
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
									{{fi-image}}\
                            </div>\
							<div class="jFiler-item-assets jFiler-row">\
			                    <ul class="list-inline pull-right">\
			                        <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
			                    </ul>\
			                </div>\
                        </div>\
                    </div>\
                </li>',
					progressBar: '<div class="bar"></div>',
					itemAppendToEnd: true,
					canvasImage: true,
					removeConfirmation: true,
					_selectors: {
						list: '.jFiler-items-list',
						item: '.jFiler-item',
						progressBar: '.bar',
						remove: '.jFiler-item-trash-action'
					}
				},
				dragDrop: {
					dragEnter: null,
					dragLeave: null,
					drop: null,
					dragContainer: null,
				},
				files : before_files,
				uploadFile: {
					url: '/egworks/estimatephotograph/upload/'+ ledId + "/" + $('#beforeId').val(),
					data: {},
					synchron: true,
					type: 'POST',
					enctype: 'multipart/form-data',
					beforeSend: function(){},
					success: function(data, itemEl, listEl, boxEl, newInputEl, inputEl, id){
						var parent = itemEl.find(".jFiler-jProgressBar").parent(),
							filerKit = inputEl.prop("jFiler");
						
						var file_id ;
						
						$.each(JSON.parse(data), function(key, value){
							file_id = value['key'];
						});
						
		        		filerKit.files_list[id].key = file_id;

						itemEl.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-success\"><i class=\"icon-jfi-check-circle\"></i> Success</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					error: function(el){
						var parent = el.find(".jFiler-jProgressBar").parent();
						el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-error\"><i class=\"icon-jfi-minus-circle\"></i> Error</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					statusCode: null,
					onProgress: null,
					onComplete: null
				},
				addMore: true,
				allowDuplicates: false,
				onRemove: function(itemEl, file, id, listEl, boxEl, newInputEl, inputEl){
					var filerKit = inputEl.prop("jFiler"),
				        file_id = filerKit.files_list[id].key;
					
						//for deleting already uploaded images as well
						if(filerKit.files_list[id].key){
							file_id = filerKit.files_list[id].key;
				       }else{
				    	   file_id = filerKit.files_list[id].file['key'];
				       }

				    $.post('/egworks/estimatephotograph/update/'+ ledId + "/" + $('#beforeId').val() + "/" + file_id);
				},
				dialogs: {
					alert: function(text) {
						return alert(text);
					},
					confirm: function (text, callback) {
						confirm(text) ? callback() : null;
					}
				},
				captions: {
					button: "Choose Files",
					feedback: "Choose files To Upload",
					feedback2: "files were chosen",
					drop: "Drop file here to Upload",
					removeConfirmation: "Are you sure you want to remove this file?",
					allowDuplicates: "Duplicate files not allowed",
					errors: {
						filesLimit: "Only {{fi-limit}} files are allowed to be uploaded.",
						filesType: "Only Images are allowed to be uploaded.",
						filesSize: "{{fi-name}} is too large! Please upload file up to {{fi-maxSize}} MB.",
						filesSizeAll: "Files you've choosed are too large! Please upload files up to {{fi-maxSize}} MB."
					}
				}
			});
			
			//On process
			$("#during").filer({
				fileMaxSize: null,
				extensions: ["jpg", "jpeg", "png"],
				changeInput: '<div class="jFiler-input-dragDrop"><div class="jFiler-input-inner"><div class="jFiler-input-icon"><i class="icon-jfi-cloud-up-o"></i></div><div class="jFiler-input-text"><h3>Drag&Drop files here</h3> <span style="display:inline-block; margin: 15px 0">or</span></div><a class="jFiler-input-choose-btn blue">Browse Files</a></div></div>',
				showThumbs: true,
				theme: "dragdropbox",
				templates: {
					box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
					item: '<li class="jFiler-item">\
								<div class="jFiler-item-container">\
									<div class="jFiler-item-inner">\
										<div class="jFiler-item-thumb">\
											<div class="jFiler-item-status"></div>\
											{{fi-image}}\
										</div>\
										<div class="jFiler-item-assets jFiler-row">\
											<ul class="list-inline pull-left">\
												<li>{{fi-progressBar}}</li>\
											</ul>\
											<ul class="list-inline pull-right">\
												<li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
											</ul>\
										</div>\
									</div>\
								</div>\
							</li>',
					itemAppend: '<li class="jFiler-item">\
                        <div class="jFiler-item-container">\
                        <div class="jFiler-item-inner">\
                            <div class="jFiler-item-thumb">\
									{{fi-image}}\
                            </div>\
							<div class="jFiler-item-assets jFiler-row">\
			                    <ul class="list-inline pull-right">\
			                        <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
			                    </ul>\
			                </div>\
                        </div>\
                    </div>\
                </li>',
					progressBar: '<div class="bar"></div>',
					itemAppendToEnd: true,
					canvasImage: true,
					removeConfirmation: true,
					_selectors: {
						list: '.jFiler-items-list',
						item: '.jFiler-item',
						progressBar: '.bar',
						remove: '.jFiler-item-trash-action'
					}
				},
				dragDrop: {
					dragEnter: null,
					dragLeave: null,
					drop: null,
					dragContainer: null,
				},
				files : during_files,
				uploadFile: {
					url: '/egworks/estimatephotograph/upload/'+ ledId + "/" + $('#duringId').val(),
					data: {},
					synchron: true,
					type: 'POST',
					enctype: 'multipart/form-data',
					beforeSend: function(){},
					success: function(data, itemEl, listEl, boxEl, newInputEl, inputEl, id){
						var parent = itemEl.find(".jFiler-jProgressBar").parent(),
							filerKit = inputEl.prop("jFiler");
						
						var file_id ;
						
						$.each(JSON.parse(data), function(key, value){
							file_id = value['key'];
						});
						
		        		filerKit.files_list[id].key = file_id;

						itemEl.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-success\"><i class=\"icon-jfi-check-circle\"></i> Success</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					error: function(el){
						var parent = el.find(".jFiler-jProgressBar").parent();
						el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-error\"><i class=\"icon-jfi-minus-circle\"></i> Error</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					statusCode: null,
					onProgress: null,
					onComplete: null
				},
				addMore: true,
				allowDuplicates: false,
				onRemove: function(itemEl, file, id, listEl, boxEl, newInputEl, inputEl){
					var filerKit = inputEl.prop("jFiler"),
				        file_id = filerKit.files_list[id].key;
					
						//for deleting already uploaded images as well
						if(filerKit.files_list[id].key){
							file_id = filerKit.files_list[id].key;
				       }else{
				    	   file_id = filerKit.files_list[id].file['key'];
				       }

				    $.post('/egworks/estimatephotograph/update/'+ ledId + "/" + $('#beforeId').val() + "/" + file_id);
				},
				dialogs: {
					alert: function(text) {
						return alert(text);
					},
					confirm: function (text, callback) {
						confirm(text) ? callback() : null;
					}
				},
				captions: {
					button: "Choose Files",
					feedback: "Choose files To Upload",
					feedback2: "files were chosen",
					drop: "Drop file here to Upload",
					removeConfirmation: "Are you sure you want to remove this file?",
					allowDuplicates: "Duplicate files not allowed",
					errors: {
						filesLimit: "Only {{fi-limit}} files are allowed to be uploaded.",
						filesType: "Only Images are allowed to be uploaded.",
						filesSize: "{{fi-name}} is too large! Please upload file up to {{fi-maxSize}} MB.",
						filesSizeAll: "Files you've choosed are too large! Please upload files up to {{fi-maxSize}} MB."
					}
				}
			});

			//after
			$("#after").filer({
				fileMaxSize: null,
				extensions: ["jpg", "jpeg", "png"],
				changeInput: '<div class="jFiler-input-dragDrop"><div class="jFiler-input-inner"><div class="jFiler-input-icon"><i class="icon-jfi-cloud-up-o"></i></div><div class="jFiler-input-text"><h3>Drag&Drop files here</h3> <span style="display:inline-block; margin: 15px 0">or</span></div><a class="jFiler-input-choose-btn blue">Browse Files</a></div></div>',
				showThumbs: true,
				theme: "dragdropbox",
				templates: {
						box: '<ul class="jFiler-items-list jFiler-items-grid"></ul>',
						item: '<li class="jFiler-item">\
									<div class="jFiler-item-container">\
										<div class="jFiler-item-inner">\
											<div class="jFiler-item-thumb">\
												<div class="jFiler-item-status"></div>\
												{{fi-image}}\
											</div>\
											<div class="jFiler-item-assets jFiler-row">\
												<ul class="list-inline pull-left">\
													<li>{{fi-progressBar}}</li>\
												</ul>\
												<ul class="list-inline pull-right">\
													<li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
												</ul>\
											</div>\
										</div>\
									</div>\
								</li>',
						itemAppend: '<li class="jFiler-item">\
	                        <div class="jFiler-item-container">\
	                        <div class="jFiler-item-inner">\
	                            <div class="jFiler-item-thumb">\
										{{fi-image}}\
	                            </div>\
								<div class="jFiler-item-assets jFiler-row">\
				                    <ul class="list-inline pull-right">\
				                        <li><a class="icon-jfi-trash jFiler-item-trash-action"></a></li>\
				                    </ul>\
				                </div>\
	                        </div>\
	                    </div>\
	                </li>',
					progressBar: '<div class="bar"></div>',
					itemAppendToEnd: true,
					canvasImage: true,
					removeConfirmation: true,
					_selectors: {
						list: '.jFiler-items-list',
						item: '.jFiler-item',
						progressBar: '.bar',
						remove: '.jFiler-item-trash-action'
					}
				},
				dragDrop: {
					dragEnter: null,
					dragLeave: null,
					drop: null,
					dragContainer: null,
				},
				files : after_files,
				uploadFile: {
					url: '/egworks/estimatephotograph/upload/'+ ledId + "/" + $('#afterId').val(),
					data: {},
					synchron: true,
					type: 'POST',
					enctype: 'multipart/form-data',
					beforeSend: function(){},
					success: function(data, itemEl, listEl, boxEl, newInputEl, inputEl, id){
						var parent = itemEl.find(".jFiler-jProgressBar").parent(),
							filerKit = inputEl.prop("jFiler");
						
						var file_id ;
						
						$.each(JSON.parse(data), function(key, value){
							file_id = value['key'];
						});
						
		        		filerKit.files_list[id].key = file_id;

						itemEl.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-success\"><i class=\"icon-jfi-check-circle\"></i> Success</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					error: function(el){
						var parent = el.find(".jFiler-jProgressBar").parent();
						el.find(".jFiler-jProgressBar").fadeOut("slow", function(){
							$("<div class=\"jFiler-item-others text-error\"><i class=\"icon-jfi-minus-circle\"></i> Error</div>").hide().appendTo(parent).fadeIn("slow");
						});
					},
					statusCode: null,
					onProgress: null,
					onComplete: null
				},
				addMore: true,
				allowDuplicates: false,
				onRemove: function(itemEl, file, id, listEl, boxEl, newInputEl, inputEl){
					var filerKit = inputEl.prop("jFiler"),
				        file_id = filerKit.files_list[id].key;
					
						//for deleting already uploaded images as well
						if(filerKit.files_list[id].key){
							file_id = filerKit.files_list[id].key;
				       }else{
				    	   file_id = filerKit.files_list[id].file['key'];
				       }

				    $.post('/egworks/estimatephotograph/update/'+ ledId + "/" + $('#beforeId').val() + "/" + file_id);
				},
				dialogs: {
					alert: function(text) {
						return alert(text);
					},
					confirm: function (text, callback) {
						confirm(text) ? callback() : null;
					}
				},
				captions: {
					button: "Choose Files",
					feedback: "Choose files To Upload",
					feedback2: "files were chosen",
					drop: "Drop file here to Upload",
					removeConfirmation: "Are you sure you want to remove this file?",
					allowDuplicates: "Duplicate files not allowed",
					errors: {
						filesLimit: "Only {{fi-limit}} files are allowed to be uploaded.",
						filesType: "Only Images are allowed to be uploaded.",
						filesSize: "{{fi-name}} is too large! Please upload file up to {{fi-maxSize}} MB.",
						filesSizeAll: "Files you've choosed are too large! Please upload files up to {{fi-maxSize}} MB."
					}
				}
			});
		})
		
function openLOA(workOrderId) {
	window.open("/egworks/letterofacceptance/view/" + workOrderId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function openLineEstimate(lineEstimateId) {
	window.open("/egworks/lineestimate/view/" + lineEstimateId, '', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}