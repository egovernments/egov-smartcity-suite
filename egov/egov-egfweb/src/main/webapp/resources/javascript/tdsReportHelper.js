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
function validateData(){
	if(document.getElementById('asOnDate').value ==''){
		bootbox.alert("Please enter a valid date")
		return false;
	}
	var asOnDate =  Date.parse(document.getElementById('asOnDate').value);
	if(asOnDate == ''){
		bootbox.alert("Please enter a valid date")
		return false;
	}
	var recovery =  document.getElementById('recovery').value;
	if(recovery == -1){
		bootbox.alert("Please select a Recovery Code")
		return false;
	}
	var fund =  document.getElementById('fund').value;
	if(fund == -1){
		bootbox.alert("Please select a Fund")
		return false;
	}
	return true;	
}
var entitiesArray;
var entities;
function loadEntities(){
	var element = document.getElementById("recovery").value;
	if(element != -1){
		var	url = "/EGF/report/pendingTDSReport-ajaxLoadEntites.action?recoveryId="+element;
		var req2 = initiateRequest();
		req2.onreadystatechange = function(){
		  if (req2.readyState == 4){
			  if (req2.status == 200){
				var entity=req2.responseText;
				var a = entity.split("^");
				var eachEntity = a[0];
				entitiesArray=eachEntity.split("+");
				entities = new YAHOO.widget.DS_JSArray(entitiesArray);
			  }
		  }
	 	};
		req2.open("GET", url, true);
		req2.send(null);
	}
}
var oAutoCompEntity;
function autocompleteEntities(obj,myEvent){
	var src = obj;	
	var target = document.getElementById('codescontainer');	
	var posSrc=findPos(src); 
	target.style.width="600px";	
	var coaCodeObj=obj;
	var key = window.event ? window.event.keyCode : myEvent.charCode;  
	if(key != 40 ){
		if(key != 38 ){
			oAutoCompEntity = new YAHOO.widget.AutoComplete(coaCodeObj,'codescontainer', entities);
			oAutoCompEntity.queryDelay = 0;
			oAutoCompEntity.prehighlightClassName = "yui-ac-prehighlight";
			oAutoCompEntity.useShadow = true;
			oAutoCompEntity.maxResultsDisplayed = 15;
			oAutoCompEntity.useIFrame = true;
			if(entities){
				entities.applyLocalFilter = true;
				entities.queryMatchContains = true;
			}
			oAutoCompEntity.minQueryLength = 0;
			oAutoCompEntity.formatResult = function(oResultData, sQuery, sResultMatch) {
				var data = oResultData.toString();
			    return data.split("`~`")[0];
			};
		}
	}
}

function splitValues(element){
	list = element.value.split("`~`")
	document.getElementById("partyName").value = list[0];
	document.getElementById("detailKey").value = list[1];
}
