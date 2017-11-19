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
 function onBodyLoad(){
	 var showMode = document.getElementById('showMode').value;
	 if(showMode == "result"){
	 dom.get('msgdiv').style.display='block';
	 }
 }

function validate(){
        if (!validateForm_dishonoredChequeForm()) {
        	undoLoadingMask();
    		return false;
        }
        var strtDate = document.getElementById('startDate').value;
        var endDate = document.getElementById('endDate').value;
        var currDate = new Date();
        var currentDate = currDate.getDate() + "/" + (currDate.getMonth()+1) + "/" + currDate.getFullYear() ;
     	
     	/*To check whether Start Date is Greater than End Date*/
     	if( compareDate(formatDate6(strtDate),formatDate6(endDate)) == -1 )
     	{
     		bootbox.alert('Start Date cannot be greater than End Date');
 	    	document.getElementById('startDate').value='';
 	    	document.getElementById('endDate').value='';
 	    	document.getElementById('startDate').focus();
 	    	return false;
     	}	
     	   /*to check whether the End Date is greater than the Current Date*/
     	if( compareDate(formatDate6(currentDate),formatDate6(endDate)) == 1 )
     	{
     		bootbox.alert('End Date cannot be greater than Current Date');
     		document.getElementById('endDate').value='';
     		document.getElementById('endDate').focus();	
     		return false;	
    	}
     	doLoadingMask();
    	document.getElementById("resultDiv").style.display="none";
        var formObj = jQuery(document.getElementById("dishonoredChequeForm"));
        var formURL = '/EGF/report/dishonoredChequeReport!ajaxSearch.action';
        var formData = new FormData(document.getElementById("dishonoredChequeForm"));
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
            return true;
           
    	
    }
	
	function viewVoucher(vid){
		var url = '../voucher/preApprovedVoucher!loadvoucherview.action?vhid='+vid;
		window.open(url,'Search','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	
