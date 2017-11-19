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

$(document).ready(function(){
	
	initializeTooltips();
	
	$('.files-upload-container').each(function(idx) {
		
		var existingFilesLen=$(this).find('.files-viewer a[data-id]').length;
		var availableFilesLen=$(this).find('input:file').length;
		
		if(existingFilesLen === availableFilesLen && $(this).find('.file-add[data-unlimited-files="true"]').length == 0) {
			$(this).find('.file-add').hide();
		}
		
	});
	
});

function getNewFileViewer(fileName) {
	return $('<div class="file-viewer" title="'+ fileName +'" data-toggle="tooltip"><a class="delete" href="javascript:void(0);"></a></div>');
}

$(document).on('change','.files-upload-container input:file',function(e) {
	
	var allowedExtenstion=$(this).closest('.files-upload-container').data('allowed-extenstion');
	var maxFileSize = $(this).closest('.files-upload-container').data('file-max-size');
	
	$(this).parent().find('.error').remove();
	
	$filesViewerContainer=$(this).parent().find('.files-viewer');
	$addFileBtn=$filesViewerContainer.find('.file-add');
	$fileInput=$(this);
	
	input=e.target;
	
	if (input.files && input.files[0]) {
		var reader = new FileReader();
		var fileName = input.files[0].name;
		var extension = fileName.split('.').pop().toLowerCase();
		
		var isMaxLimitReached=false;
		
		if(maxFileSize){
			isMaxLimitReached = parseInt(maxFileSize)*1024*1000 < input.files[0].size;
		}
		
		if(allowedExtenstion && allowedExtenstion.toUpperCase().split(',').indexOf(extension.toUpperCase()) < 0){
			bootbox.alert('Please upload '+allowedExtenstion.toUpperCase());
			$(this).val('');
			return;
		}
		else if(isMaxLimitReached){
			bootbox.alert('File size should not exceed 2 MB!');
			$(this).val('');
			return;
		}
		
        reader.onload = function (e) {
        	$fileViewer=getNewFileViewer(fileName);
        	
        	
        	$addFileBtn.before($fileViewer);
        	//$filesViewerContainer.append($fileViewer);
        	
        	if(['jpg', 'jpeg', 'png', 'gif', 'tiff'].indexOf(extension)>=0){
        		$fileViewer.css({
            		'background-image':'url(' + e.target.result + ')',
            		'background-position':'center',
            		'background-size':'cover'
            	});
        	} else {
        		if(['pdf'].indexOf(extension)>=0) {
        			$fileViewer.append('<i class="fa fa-file-pdf-o" aria-hidden="true"></i>');
        		} else if(['txt'].indexOf(extension)>=0) {
        			$fileViewer.append('<i class="fa fa-file-text-o" aria-hidden="true"></i>');
        		} else if(['doc','docx','rtf'].indexOf(extension)>=0) {
        			$fileViewer.append('<i class="fa fa-file-word-o" aria-hidden="true"></i>');
        		} else if(['zip'].indexOf(extension)>=0) {
        			$fileViewer.append('<i class="fa fa-file-archive-o" aria-hidden="true"></i>');
        		} else if(['xls','xlsx'].indexOf(extension)>=0) {
        			$fileViewer.append('<i class="fa fa-file-excel-o" aria-hidden="true"></i>');
        		}
        		else{
        			$fileViewer.append('<i class="fa fa-file-o" aria-hidden="true"></i>');
        		}
        	}
        	
        	$fileViewer.data('fileInput', $fileInput);
        	if($fileInput.data('isLast')){
        		$addFileBtn.hide();
        	}
        	
        	initializeTooltips();
        }
        reader.readAsDataURL(input.files[0]);
	}																																																																																																																																																																																																																																																																																																																																																																																																																																																																																														
});

$(document).on('click','.file-add',function() {
		
	if($(this).data('unlimited-files') && $(this).data('file-input-name')) {
	
		var isFileInputNotAvailable=true;
		$(this).parent().find('input:file').each(function(idx) {
			if(!$(this).val()){
				$(this).trigger('click');
				isFileInputNotAvailable=false;
				return false;
			}
		});
		
		if(isFileInputNotAvailable){
			$file=$('<input type="file" class="hide" name="'+ $(this).data('file-input-name') +'">');
			$(this).closest(".files-upload-container").append($file);
			$file.trigger('click');
		}
	}
	else {
		
		$fileUploadContainer=$(this).closest('.files-upload-container');
		
		var length=$fileUploadContainer.find('input:file').length;
		var existingFiles=$(this).parent().find('a[data-id]').length;
		$fileUploadContainer.find('input:file').each(function(idx){
			if(!$(this).val() && idx>existingFiles-1){
				$(this).data('isLast', idx===length-1);
				$(this).trigger('click');
				return false;
			}
		});
	}
	
});

function addDeletedFileId(fileId){
	var deleteFileIds=$('#deletedFilestoreIds').val();
	var deletedIds=[];
	
	if(deleteFileIds)
	 deletedIds=deleteFileIds.split(',');
	
	deletedIds.push(fileId);
	$("#deletedFilestoreIds").val(deletedIds.join());
}

//delete event
$(document).on('click','.file-viewer a.delete',function(){
	if($(this).data('id')){
		var id=$(this).data('id');
		addDeletedFileId(id);
	}
	else{
	  $fileopen = $(this).parent().data('fileInput');
	  $fileopen.val('');
	}
	$(this).closest('.files-upload-container').find('.file-add').show();
	removeTooltip($(this).parent());
	$(this).parent().remove();
});

$(document).on('click','div.file-viewer',function(e){
    if(e.target !== e.currentTarget) return;
    var url = $(this).css("background-image")
    if(url && url!=='none'){
		url=url.replace(/.*\s?url\([\'\"]?/, '').replace(/[\'\"]?\).*/, '');
	    showImage(url);
    }
    else{
      bootbox.alert('No preview available');
    }
    
});

function showImage(url) {
	$('#imgModel').show();
	$('#previewImg').attr('src', url);
}

$(document).on('click','span.closebtn',function(){
	$('#imgModel').hide();
});

function validateUploadFilesMandatory(){
	var isValid=true;
	
	$('.files-upload-container[required]').each(function(idx){
		if($(this).find('div.file-viewer').length === 0){
			if(isValid){
				isValid=false;
				if (typeof focusToTabElement === 'function') {
					focusToTabElement(this);
				}
			}
				
			$errorLabel=$('<label class="error">Required</label>');
			$(this).append($errorLabel);
		}
		else{
			$(this).find('.error').remove();
		}
	});
	return isValid;
}
	
function initializeTooltips(){
	try{
		$('*[data-toggle="tooltip"]').tooltip()
	}
	catch(exception){
		console.log('Error', 'Tooltip js not found');
	}
}

function removeTooltip($elem){
	try{
		$elem.tooltip('destroy');
	}
	catch(exception){
		console.log('Error', 'Tooltip js not found');
	}
}





