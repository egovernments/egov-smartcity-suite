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
jQuery.noConflict();
var maskingTimeForDownloads = 30000;//30 seconds
var displayTagExportPDF = 'd-5394226-e=5';
var displayTagExportExcel = 'd-5394226-e=2';
function doLoadingMask() {
	jQuery('.loader-class').modal('show', {backdrop: 'static'});
}

function undoLoadingMask() {
	jQuery('.loader-class').modal('hide');
}


jQuery(document).ready(function() {
		jQuery( "form" ).submit(function( event ) {
		doLoadingMask();
		});
});


jQuery(document).click(function() {
	if(this.activeElement.href) {
		var name = this.activeElement.href;
		if(name.indexOf(displayTagExportExcel)!=-1 || name.indexOf(displayTagExportPDF)!=-1){
			doLoadingMask();
			setTimeout(function() {
				undoLoadingMask();
			}, maskingTimeForDownloads)
		}
	}
	if(this.activeElement.href){
		var href = this.activeElement.href;
		if(href.indexOf("&page=")!=-1 ){
			doLoadingMask();
		}
	}
	if(this.activeElement.defaultValue){
		var name = this.activeElement.defaultValue;
		name = name.toUpperCase();
		if(name.indexOf("EXCEL")!=-1 || name.indexOf("PDF")!=-1 ){
			setTimeout(function() {
				undoLoadingMask();
			}, maskingTimeForDownloads)
		}
	}
	
});


function ajaxSubmit(formId,formUrl,event)
{
		document.getElementById("resultDiv").style.display="none";
		var formObj = jQuery(document.getElementById(formId));
		var formURL = formUrl;
		var formData = new FormData(document.getElementById(formId));
		jQuery.ajax({
		    url: formURL,
		    data:  formData,
		    type : 'POST',
		    async : false,
			datatype : 'text',  
			processData: false, 
			contentType: false,
			   	
			success: function(data)
			   {
			     document.getElementById("resultDiv").innerHTML=data;
			     document.getElementById("resultDiv").style.display="block";
			     undoLoadingMask();
			   },
			error: function(jqXHR, textStatus, errorThrown)
			  {
			  	 undoLoadingMask();
			  }         
		});
	    event.preventDefault();

}
