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
	
	var fileformats = ['jpg', 'jpeg', 'gif', 'png' ];
	var myCenter;
	
	var fileinputid = ['file1'];//assigning file id
	var filefilled = {};//image fullfilled array
	var removedarray = [];
	var fileid;
	
	$('#triggerFile').click(function(){
		console.log(removedarray.length);
		if(removedarray.length == 0 || removedarray.length == 3)
		{
			var keys=Object.keys(filefilled);
			fileid = fileinputid[keys.length];
			console.log("File ID normal:"+fileid);
			}else{
			fileid = removedarray[0];
			console.log("File ID removal:"+fileid);
		}
	    
		
		$('#'+fileid).trigger("click");
	});
	
	$('.remove-img').click(function(){
		console.log("Removal");
		delete filefilled[$(this).attr('data-file-id')];
		if ($.inArray($(this).attr('data-file-id'), removedarray) !== -1)//check removed file id already exists, if exists leave as such or push it
		{
			
			}else{
			removedarray.push($(this).attr('data-file-id'));
			removedarray.sort();
			console.log("sorted removed array"+removedarray);
		}
		
		console.log("File filled array:"+JSON.stringify(filefilled));
		$('#'+$(this).attr('data-file-id')).val('');
		$('#triggerFile').removeAttr('disabled');
		if($(this).attr('data-file-id') == 'file1')
		{
			$('#file1block, .preview-cross1, #preview1').hide();
			$('#preview1').removeAttr("src");
			$('#filename1').html('');
		}
		
	});
	
	$('#file1, #file2, #file3').on('change.bs.fileinput',function(e)
	{
		/*validation for file upload*/
		myfile= $( this ).val();
		var ext = myfile.split('.').pop();
		if($.inArray(ext, fileformats) > -1){
			//do something    
			}else{
				bootbox.alert(ext+" file format is not allowed");
			return;
		}
		
		//bootbox.alert('ext'+ext);
		
		if(e.target.files.length>0)
		{
			//filefilled[$(this).attr('id')]=this.files[0].name;
			//console.log("File filled array:"+JSON.stringify(filefilled));
			readURL(this, this.files[0].name);
			$('#triggerFile').attr('disabled','disabled');
			//var index = removedarray.indexOf(fileid);
		}
	});
	
	function readURL(input, filename) {
		//console.log("Key:"+fileid);
		//console.log("Key value is:"+filefilled[fileid]);
		filename = ((filename.length > 15) ? filename.substring(0,13)+".." : filename);
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				if(fileid == 'file1')
				{
					$('#file1block, .preview-cross1, #preview1').show();
					$('.preview-cross1').attr('data-file-id',fileid);
					$('#preview1').attr('src', e.target.result).width(100);
					$('#filename1').html(filename);
				}
			}
			
			reader.readAsDataURL(input.files[0]);
		}
	}
	
});