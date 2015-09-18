/*#-------------------------------------------------------------------------------
	# eGov suite of products aim to improve the internal efficiency,transparency, 
	#    accountability and the service delivery of the government  organizations.
	# 
	#     Copyright (C) <2015>  eGovernments Foundation
	# 
	#     The updated version of eGov suite of products as by eGovernments Foundation 
	#     is available at http://www.egovernments.org
	# 
	#     This program is free software: you can redistribute it and/or modify
	#     it under the terms of the GNU General Public License as published by
	#     the Free Software Foundation, either version 3 of the License, or
	#     any later version.
	# 
	#     This program is distributed in the hope that it will be useful,
	#     but WITHOUT ANY WARRANTY; without even the implied warranty of
	#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	#     GNU General Public License for more details.
	# 
	#     You should have received a copy of the GNU General Public License
	#     along with this program. If not, see http://www.gnu.org/licenses/ or 
	#     http://www.gnu.org/licenses/gpl.html .
	# 
	#     In addition to the terms of the GPL license to be adhered to in using this
	#     program, the following additional terms are to be complied with:
	# 
	# 	1) All versions of this program, verbatim or modified must carry this 
	# 	   Legal Notice.
	# 
	# 	2) Any misrepresentation of the origin of the material is prohibited. It 
	# 	   is required that all modified versions of this material be marked in 
	# 	   reasonable ways as different from the original version.
	# 
	# 	3) This license does not grant any rights to any user of the program 
	# 	   with regards to rights under trademark law for use of the trade names 
	# 	   or trademarks of eGovernments Foundation.
	# 
	#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/

/*
 * Note : Property "selectedModeBndry" used to traverse forward and backward. 
 * 1.ondrilldown at each level(ie zone/ward/block/property) this property value gets updated with the concatenated values of mode and boundary.
 * 2. Value format : mode~boundaryId. 
 * 3. Ex: zone~1 (At 1st level), zone~1-ward~6-block~8-property~10 (At last level). 
 * 4. property~10 means show all properties under block with id 10 / block~8 means show all blocks under ward id 8 and so on.  		
 */

function calculateAmount(obj){
	
	/* var table= document.getElementById("dcbOnlinePaymentTable");
	 var rowobj= getRow(obj).rowIndex;
	 var nodes = jQuery('#dcbOnlinePaymentTable').find('input[type=checkbox]');

	  if(document.forms[0].payInstallment[rowobj-2]!=undefined && document.forms[0].payInstallment[rowobj-2].checked==true){	 
			for(var j=0;j<=rowobj-2;j++)
			{
				if(document.forms[0].payInstallment[j].checked==false){
					dom.get("shop_error").style.display = '';
					document.getElementById("shop_error").innerHTML ="Please choose its previos Installments. Random selection not allowed.";
					document.forms[0].payInstallment[rowobj-2].checked=false;
					document.getElementById("shop_error").focus();
					return false;
				}
			}*/
		
	 var table= document.getElementById("dcbOnlinePaymentTable");
	 var rowobj= getRow(obj).rowIndex;
	var val=$('#demand.egDemandDetails'+[rowobj-2]+'amount').val();
	  if(jQuery('#amount[rowobj-2]')!=undefined && jQuery('#amount[rowobj-2]').val() >0){	 
			for(var j=0;j<=rowobj-2;j++)
			{
				if(jQuery('#amount[j]').val() >0){
					
					alert("Please choose its previos Installments. Random selection not allowed.");
					//document.forms[0].amount[rowobj-2].checked=false;
					
					return false;
				}
			}
	  }/*else if(document.forms[0].amount[rowobj-2]!=undefined && document.forms[0].amount[rowobj-2].value !=""){
			for(var j=rowobj-2;j<nodes.length;j++){
				document.forms[0].amount[j].value=false;
				if(document.forms[0].isArrersServiceTax[j].value=="true"){
					document.forms[0].arrersServiceTaxAmount[j].value="";
				}
			}
	  } */

	 
	 
}