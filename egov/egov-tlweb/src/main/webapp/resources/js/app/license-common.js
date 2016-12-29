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

function numbersonly(myfield, e) {
	var key;
	var keychar;

	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13)
			|| (key == 27))
		return true;

	// numbers
	else if ((("0123456789").indexOf(keychar) > -1))
		return true;
	else
		return false;
}

function setupAjaxDivision(elem) {
	zone_id = elem.options[elem.selectedIndex].value;
	populatedivision({
		zoneId : zone_id
	});
}

function setupLicenseeAjaxDivision(elem) {
	zone_id = elem.options[elem.selectedIndex].value;
	populatelicenseedivision({
		zoneId : zone_id
	});
}
function refreshInbox() {		
	if(opener && opener.top.document.getElementById('inboxframe')) {
		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	} else if (opener && opener.opener && opener.opener.top.document.getElementById('inboxframe')) {
		opener.opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
	}
}

function numbersforamount(myfield, e)
{
	var key;
	var keychar;

	if (window.event)
		key = window.event.keyCode;
	else if (e)
		key = e.which;
	else
		return true;
	keychar = String.fromCharCode(key);

	// control keys
	if ((key==null) || (key==0) || (key==8) || 
			(key==9) || (key==13) || (key==27) )
		return true;

	// numbers
	else if (((".0123456789").indexOf(keychar) > -1))
		return true;
	else
		return false;
}


jQuery(document).ready(function () {
	
	jQuery('#subCategory').select2({
		placeholder: "Select",
		width:'100%'
	});
	jQuery('#category').change(function() {
	    var val = jQuery(this).val();
	    var results = [];
	    jQuery.ajax({
	        type: "GET",
	        url: '../licensesubcategory/subcategories-by-category?name=&categoryId=' + val,
	        dataType: "json",
	        success: function(data) {
	        	jQuery.each(data, function(i) {
	                var obj = {};
	                obj['id'] = data[i]['id']
	                obj['text'] = data[i]['name'];
	                results.push(obj);
	            });
	            jQuery("#subCategory").empty();
            	jQuery("#subCategory").append("<option value=''>Select</option>");
	            jQuery("#subCategory").select2({
	                placeholder: "Select",
	                width:'100%',
	                data: results
	            });
	        },
	        error: function() {
	        	bootbox.alert('something went wrong on server');
	        }
	    });
	});
});
