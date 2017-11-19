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
var wardDataTable; 
var slDetailTableIndex = 0;  
var TARGETWARDLIST = "targetAreaMappingsResultList";

function checkAlpaNumeric(obj){
	if(obj.value != ""){
		var num = obj.value;
		var objRegExp  = /^([a-zA-Z0-9]+)$/i;
	if(!objRegExp.test(num)){
		bootbox.alert('Please enter valid code for target area');  
		obj.value = "";
		obj.focus();
	}
}
} 
function validate(){
	if(!validateForm_targetArea()){ 
		undoLoadingMask();
		return false;
		}
	
	  document.getElementById("code").disabled = false;    
	  return true;
	}

function checkCodeUnique(){ 
	var code = document.getElementById("code").value.trim();  
    var mode = document.getElementById("mode").value;
    if (mode == "new") {
	populatecodeUniqueCheck({code:code}); 
    }
    else {
    var id = document.getElementById("id").value;
    populatecodeUniqueCheck({code:code,id:id}); 
    }
    	
	
}

function getData(){
	doLoadingMask();
	document.getElementById("resultDiv").style.display="none";
    var formObj = jQuery(this);
    var formURL = '/EGF/master/targetArea!ajaxSearch.action';
    var formData = new FormData(document.getElementById("targetAreaForm"));
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
}

function urlLoad(id,showMode) {
	if(showMode=='edit')
		 url = "../master/targetArea!beforeEdit.action?id="+id;
	else          
		 url = "../master/targetArea!beforeView.action?id="+id; 
	window.open(url,'targetAreaView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
}

var path="../..";
var oAutoCompEntityForJV;
function autocompleteCode(obj)
{
  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/common/eBCommon!ajaxLoadTargetAreaCodes.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
	   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&targetAreaCode="+document.getElementById("code").value;
	   } 
	   oAutoCompEntityForJV.queryDelay = 0.5;
	   oAutoCompEntityForJV.minQueryLength = 3;
	   oAutoCompEntityForJV.prehighlightClassName = "yui-ac-prehighlight"; 
	   oAutoCompEntityForJV.useShadow = true;
	   oAutoCompEntityForJV.forceSelection = true;
	   oAutoCompEntityForJV.maxResultsDisplayed = 20;
	   oAutoCompEntityForJV.useIFrame = true;
	   oAutoCompEntityForJV.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width = 150;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   }
}
function splitCode(obj) 
{	
	var entity = obj.value;
	if (entity.trim() != "")
	{ 
		var entityValue = entity.trim(); 
		var entityArray = entityValue.split("~");   
		if (entityArray.length != 0)   
		{
			document.getElementById("code").value = entityArray[0].split("~");  
			document.getElementById("name").value = entityArray[1].trim();      
		}
	}

}

function updateGridTargetArea(tableName,field,index,value){
	document.getElementById(tableName+'['+index+'].'+field).value=value;
}
    

