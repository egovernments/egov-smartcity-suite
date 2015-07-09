#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script>
var PAIDCERTIFICATE = '<s:property value="@org.egov.bnd.utils.BndConstants@PAIDCERTIFICATE"/>';
var FREECERTIFICATE = '<s:property value="@org.egov.bnd.utils.BndConstants@FREECERTIFICATE"/>';
var CERTIFICATEFEE='<s:property value="@org.egov.bnd.utils.BndConstants@CERTIFICATEFEE"/>';
var SEARCHFEE='<s:property value="@org.egov.bnd.utils.BndConstants@SEARCHFEE"/>';
function validateForm()
{
		if(document.getElementById("applicantName").value==null ||
					trimAll(document.getElementById("applicantName").value)=="")
			{
			alert('<s:text name="applicantName.validate"/>' );
			return false;
			}
		if(document.getElementById("no_Of_copies").value==null || trimAll(document.getElementById("no_Of_copies").value)=="")
			{
			alert('<s:text name="numberOfCopies.validate"/>' );
			return false;
			}
	
	 validateAmountDetails();
	 
	return validateAmountDetailsInPaidCertificate();
	
	
}
function validateAmountDetailsInPaidCertificate()
{
var transTypeFlag = document.getElementsByName("transType");
var tableObj = dom.get('feeTypeTbl');
	for(var i=0;i<parseInt(tableObj.rows.length)-2;i++){
		var amount = dom.get('amount'+i).value;
		var description = dom.get('description'+i).value;
		if(!isNaN(amount) && amount!=null && amount>0)
		 {

			break;
		}else {
			//check in case of paid certificate, search and certificate fees are mandatory
			if(transTypeFlag[1].checked && description!=null && (description==CERTIFICATEFEE || description==SEARCHFEE)){
				alert('<s:text name="enterSearchCertificateFee.validation"/>' );
				return false;
			}
		}
		
		transTypeFlag[0].disabled=false;
		transTypeFlag[1].disabled=false;
	}
	
return true;	
}
function validateAmountDetails()
{
var transTypeFlag = document.getElementsByName("transType");
var tableObj = dom.get('feeTypeTbl');
	var atleastOneFieldSelected=false;
	for(var i=0;i<parseInt(tableObj.rows.length)-2;i++){
		var amount = dom.get('amount'+i).value;
		if(!isNaN(amount) && amount!=null && amount>0)
		 {
			atleastOneFieldSelected=true;
			return true;
		}
	}
	
	if(!atleastOneFieldSelected)
	{
		if(transTypeFlag[1].checked){
			alert('<s:text name="selectAnyOneField.validation"/>' );
			return false;
		}else
		{
			return true;
		}
	}
	return atleastOneFieldSelected;
}
function calculateTotal()
	{
		var tableObj = dom.get('feeTypeTbl');
    	var tot = 0;
  		var noofcopies = dom.get('no_Of_copies').value;
    	for(var i=0;i<parseInt(tableObj.rows.length)-2;i++){
		    	var amount = dom.get('amount'+i).value;
		    	var description = dom.get('description'+i).value;
    		if(!isNaN(amount) && amount!=null && noofcopies!=null && noofcopies!="" && noofcopies>0 && trimAll(amount)!=""){
	    		if(description!=null && description==CERTIFICATEFEE)
	    		{
	    		 tot = tot+ noofcopies *( parseFloat(dom.get('amount'+i).value));
	    		}  	else
	    		{
	    		 tot = tot+parseFloat(dom.get('amount'+i).value);
	    		 }			
    		}
    	}
    	
    	dom.get('totalAmount').value = tot;
				
	}
	
	function  resetCertificateType()
	{ 
	var transTypeFlag = document.getElementsByName("transType");
		
		if(transTypeFlag[0].checked)
		{
		 	transTypeFlag[0].disabled=true;
		 	transTypeFlag[1].disabled=true;
		 	resetAmountDetailsForFreeCertificate();
		 }else if(transTypeFlag[1].checked)
		 {
			 transTypeFlag[0].disabled=true;
		 	transTypeFlag[1].disabled=false;
		 
		 }
	}
	function onChangeOfCertificateType()
	{
		var transTypeFlag = document.getElementsByName("transType");
			
			//alert(document.getElementsByName("transTypeTemp").value);
	
		if(transTypeFlag[0].checked)
		{
		 	//transTypeFlag[0].disabled=true; 
		 	//transTypeFlag[1].disabled=true;
		 	resetAmountDetailsForFreeCertificate();
		 }else if(transTypeFlag[1].checked)
		 {
			// transTypeFlag[0].disabled=false;
		 	//transTypeFlag[1].disabled=false;
		 	if(!transTypeFlag[0].disabled)
		 	resetAmountDetailsForPaidCertificate();
		 }
	}
	
	
	function resetAmountDetailsForPaidCertificate()
	{
	
		var noofcopies = dom.get('no_Of_copies').value;
			dom.get('no_Of_copies').value=1;
		 	dom.get('no_Of_copies').readOnly=false;
		
		var tableObj = dom.get('feeTypeTbl');
    	for(var i=0;i<parseInt(tableObj.rows.length)-2;i++){
		   var amount = dom.get('amount'+i).value;
    		if(!isNaN(amount) && amount!=null && noofcopies!=null && noofcopies!="" && noofcopies>0 && trimAll(amount)!=""){
	    		dom.get('amount'+i).value=0;
	    		dom.get('amount'+i).readOnly=false;
    		}
    	}
    	dom.get('totalAmount').value = 0;
	}
	function resetAmountDetailsForFreeCertificate()
	{
	
		var noofcopies = dom.get('no_Of_copies').value;
			dom.get('no_Of_copies').value=1;
		 	dom.get('no_Of_copies').readOnly=true;
		var tableObj = dom.get('feeTypeTbl');
    	
	 	for(var i=0;i<parseInt(tableObj.rows.length)-2;i++){
		   		dom.get('amount'+i).value=0;
	    		dom.get('amount'+i).readOnly=true;
    	}
    	dom.get('totalAmount').value = 0;
	}
function trimAll(sString) 
	{
		while (sString.substring(0,1) == ' ')
		{
		sString = sString.substring(1, sString.length);
		}
		while (sString.substring(sString.length-1, sString.length) == ' ')
		{
		sString = sString.substring(0,sString.length-1);
		}
	   return sString;
	}	
function checkSpecialCharsInName(obj)
	  {
	  	
		 var iChars = "!@$%^*+=[]\\;{}|\":<>?1234567890"; 
	
	      for (var i = 0; i < obj.value.length; i++) 
		{
			     
	         if (iChars.indexOf(obj.value.charAt(i)) != -1) 
	         {
				alert("The box has special characters.Like !@$%^*+=[']\\';{}|\":<>?1234567890  \nThese are not allowed.\n" );
				obj.focus();obj.clear();
				return false;
		  }
		 }
	   
	   	
	   	return true;
	   }
function validateNumber(obj)
		{
		if(obj!=null && obj.value!=null && trimAll(obj.value)!=""){
					
					if(!valFee(obj))
						obj.value=0;
			}else
			obj.value=0;
			
		}
function onChangeOfNoOfCopies(obj)
{
	if(obj!=null && obj.value!=null && trimAll(obj.value)!=""){
					if(!isValid(obj.value))
						obj.value="";
					if(!valFee(obj))
						obj.value="";
			}
}		
function isValid(parm)
			{
			var val = '0123456789';
			
			if (parm == "") 
			{
			  
			   alert('<s:text name="selectWholeNumber.validation"/>' );
				return false;
			}
			
			
			for (i=0; i<parm.length; i++)
			{
				if (val.indexOf(parm.charAt(i),0) == -1)
				   {
				   	   alert('<s:text name="selectWholeNumber.validation"/>' );
				   	return false;
				}
			}
			return true;
			}
	
	function valFee(obj)
		{
		
		var iChars = "`!@#$%^&*()+=-[]\\\';,./{}|\":<>?/^[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]*$/";
			for (var i = 0; i < (obj).value.length; i++)
			{
			   if (iChars.indexOf((obj).value.charAt(i)) != -1) 
			    {
				 alert('<s:text name="selectWholeNumber.validation"/>' );
				obj.value="";
				obj.focus();
				return false;
			   }
			   
			}
		return true;	
			
		}

						   
</script>
