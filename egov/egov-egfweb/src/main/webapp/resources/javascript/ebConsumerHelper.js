/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
# accountability and the service delivery of the government  organizations.
#   
#  Copyright (C) <2015>  eGovernments Foundation
#   
#  The updated version of eGov suite of products as by eGovernments Foundation 
#  is available at http://www.egovernments.org
#   
#  This program is free software: you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation, either version 3 of the License, or
#  any later version.
#   
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#   
#  You should have received a copy of the GNU General Public License
#  along with this program. If not, see http://www.gnu.org/licenses/ or 
#  http://www.gnu.org/licenses/gpl.html .
#   
#  In addition to the terms of the GPL license to be adhered to in using this
#  program, the following additional terms are to be complied with:
#   
# 1) All versions of this program, verbatim or modified must carry this 
#    Legal Notice.
#   
# 2) Any misrepresentation of the origin of the material is prohibited. It 
#    is required that all modified versions of this material be marked in 
#    reasonable ways as different from the original version.
#   
# 3) This license does not grant any rights to any user of the program 
#    with regards to rights under trademark law for use of the trade names 
#    or trademarks of eGovernments Foundation.
#   
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
   function validate(){
        if (!validateForm_eBConsumerForm()) {
        	undoLoadingMask();
    		return false;
            }
        
           
    	return true;
    }
  function validateSearch(){
	  var consumerNo = document.getElementById('code').value;
	  var accountNo = document.getElementById('name').value;
	  var targetArea = document.getElementById('targetArea').value;
	  var region = document.getElementById('region').value;
	  var ward = document.getElementById('ward').value;
	  var oddOrEvenBilling = document.getElementById('oddOrEvenBilling').value;
	  if(consumerNo == "" && accountNo == "" && targetArea == "" && ward == "" && oddOrEvenBilling == "" && region == ""){
		  bootbox.alert("You need to select at least one search criteria");
		  return false;
	  }
	  return true;
  		
  }
   function validateCodeAndName(check){
    	if(check=='true'){
    		document.getElementById('code').readOnly = true;
    		document.getElementById('name').readOnly = true;
    	}
    }
    
    function checkuniquenessconsumerno(){
    	document.getElementById('uniquecode').style.display ='none';
		var consumerNumber = document.getElementById('code').value;
		var id = document.getElementById('id').value;
		populateuniquecode({code:consumerNumber,id:id});		
    }
    function checkuniquenessaccountno(){
    	document.getElementById('uniquename').style.display ='none';
		var accountNo = document.getElementById('name').value;
		var id = document.getElementById('id').value;
		populateuniquename({name:accountNo,id:id});		
    }
       
    var path="../..";
	var oAutoCompEntityForJV;
	function autocompleteConsumerNumbers(obj)
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/common/eBCommon!ajaxLoadEBConsumerNumbers.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&consumerNumber="+document.getElementById("code").value;
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
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   }
	}
	function splitConsumerNumber(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("code").value=entity_array[0].split("`-`")[0];
			}
		}

	}
	function autocompleteAccountNumbers(obj)
	{
	  	   oACDS = new YAHOO.widget.DS_XHR(path+"/EGF/common/eBCommon!ajaxLoadEBAccountNumbers.action", [ "~^"]);
		   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
		   oACDS.scriptQueryParam = "startsWith";
		   oAutoCompEntityForJV = new YAHOO.widget.AutoComplete(obj.name,'codescontainer',oACDS);
		   oAutoCompEntityForJV.doBeforeSendQuery = function(sQuery){
			   loadWaitingImage(); 
			   return sQuery+"&accountNumber="+document.getElementById("name").value;
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
		           oContainer.style.width=300;
		           YAHOO.util.Dom.setXY(oContainer,pos);
		           return true;
		   }
	}
	function splitAccountNumber(obj) 
	{	
		var entity=obj.value;
		if(entity.trim()!="")
		{
			var entity_array=entity.split("`~`");
			if(entity_array.length==2)
			{
				document.getElementById("name").value=entity_array[0].split("`-`")[0];
			}
		}

	}
		
	function urlLoad(id,showMode) {
		if(showMode=='edit')
			 url = "../master/eBConsumer!beforeEdit.action?id="+id;
		else          
			 url = "../master/eBConsumer!beforeView.action?id="+id; 
		window.open(url,'eBConsumerView','resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	
	function setTargetArea(){
	    var formObj = jQuery(this);
	    var formURL = '/EGF/common/eBCommon!ajaxLoadTargetAreaByWard.action?wardId='+document.getElementById("ward").value;
	    var formData = new FormData(document.getElementById("eBConsumerForm"));
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
	        document.getElementById("targetArea").value =data;
	    },
	     error: function(jqXHR, textStatus, errorThrown)
	     {
	     }         
	    });
	}
	
	
