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
function replaceStatus() {
	var status = document.getElementById('status').innerHTML;
	if(status == 1)
		status = status.replace("1", "ACTIVE");
	status = status.replace("0", "INACTIVE");
    document.getElementById("status").innerHTML = status;
}

function modifyMilestoneTemplateData() {
	var id = document.getElementById('id').value;
	window.location = 'milestoneTemplate-edit.action?mode=edit&id='+id;
}

function createNewMilestoneTemplate() {
	window.location = "milestoneTemplate-newform.action";
}

function validateFormBeforeSubmit() {
		if (document.getElementById("code").value == '') {
	    	var message = document.getElementById('templateCode').value;
	        showMessage('milestonetemplateerror', message);
	        return false;
	
	    }
        if (document.getElementById("name").value == '') {
        	var message = document.getElementById('templateName').value;
            showMessage('milestonetemplateerror', message);
            return false;

        }
        if (document.getElementById("templateDescription").value == '') {
        	var message = document.getElementById('templateDesc').value;
            showMessage('milestonetemplateerror', message);
            return false;
        }
        if (document.getElementById("typeOfWork").value == '' || document.getElementById("typeOfWork").value == '-1') {
        	var message = document.getElementById('selectTypeOfWork').value;
            showMessage('milestonetemplateerror', message);
        	return false;
        }
        
        var stageOrderNo = document.getElementsByClassName("slnowk");
        for(var i = 0; i < stageOrderNo.length; i++)
        {
           if(stageOrderNo.item(i).value == '') {
           	var message = document.getElementById('stageOrderNo').value;
            showMessage('milestonetemplateerror', message);
        	   return false;
           }
        }
        
        var description = document.getElementsByClassName("selectmultilinewk");
        for(var i = 0; i < description.length; i++)
        {
           if(description.item(i).value == '') {
           	var message = document.getElementById('description').value;
            showMessage('milestonetemplateerror', message);
        	   return false;
           }
        }
        
        var percentage = document.getElementsByClassName("selectamountwk");
        for(var i = 0; i < percentage.length; i++){
           if(percentage.item(i).value == '') {
           	var message = document.getElementById('tempPercentage').value;
            showMessage('milestonetemplateerror', message);
        	   return false;
           }
        }
        
        var totalValue = document.getElementById('totalValue').innerHTML;
        if(totalValue != 100){
        	var message = document.getElementById('validateTotalPercentage').value;
        	showMessage('milestonetemplateerror', message);
        	return false;
        }
        	
        return true;
}

function modifyMilestoneTemplate(){
	var id=document.getElementById('selectedCode').value;
	if (id == '' || id == null) {
		var message = document.getElementById('selectMilestoneTemplate').value;
        showMessage('milestonetemplateerror', message);
        window.scrollTo(0, 0);
    } else{
	   window.open("milestoneTemplate-edit.action?mode=edit&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
}

function viewMilestoneTemplate(){
	var id=document.getElementById('selectedCode').value;
	if (id == '' || id == null) {
		var message = document.getElementById('selectMilestoneTemplate').value;
        showMessage('milestonetemplateerror', message);
		window.scrollTo(0, 0);
    } else{
	window.open("milestoneTemplate-edit.action?mode=view&id="+id+"&sourcepage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
    }
}