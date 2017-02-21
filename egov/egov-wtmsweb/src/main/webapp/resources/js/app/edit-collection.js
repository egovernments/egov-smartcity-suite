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


$(document).ready(function(e){
	
	$('.read-only').attr('readOnly','readOnly');
	
});


		function calculateAmount(obj){
	
	 var table= document.getElementById("dcbOnlinePaymentTable");
	 var rowobj= getRow(obj).rowIndex;
		/*if(obj.value !=undefined)
		{
			if( obj.value -(document.forms[0].actualCollection[rowobj].value) < 0 )
				{
				document.forms[0].actualCollection[rowobj].value="";
				return false;
				}
		}*/
	if(document.forms[0].actualAmount[rowobj-2]!=undefined && document.forms[0].actualAmount[rowobj-2].value !=undefined){	 
			for(var j=0;j<=rowobj-2;j++)
			{
				if(document.forms[0].actualAmount[j].value ==""){
					
					bootbox.alert("Please choose its previos Installments. Random selection not allowed.");
					obj.value="";
					return false;
				}
			}
 		}
	 	
		}
		

		function calculateCollectionAmount(obj){
	
	 var table= document.getElementById("dcbOnlinePaymentTable");
	 var rowobj= getRow(obj).rowIndex;
	if(obj.value !=undefined)
	{
		/*var rate = $(obj).parent('td').prev('td').text();
		var values=$(obj).parent('td').prev('td').attr("value");
		if(document.forms[0].actualAmount[rowobj].value=="")
		{
			bootbox.alert("Please Enter Demand Amount for This row");
			obj.value="";
			return false;
			//need to fix .. get previous td value n do validation
		}*/
		
		/*if((document.forms[0].actualAmount[rowobj].value) -(obj.value) <0 )
			{
			bootbox.alert(" Collection Amount should not be greater than Demand amount");
			obj.value="";
			return false;
			}*/
	}
	if(document.forms[0].actualCollection[rowobj-2]!=undefined && document.forms[0].actualCollection[rowobj-2].value !=undefined){	 
			for(var j=0;j<=rowobj-2;j++)
			{
				if(document.forms[0].actualCollection[j].value == ""){
					bootbox.alert("Please choose its previos Installments. Random selection not allowed.");
					obj.value="";
					return false;
				}
			}
 		}
		}
		
		$('#submitButtonId').click(function(e){
			var i= 0;
			var j=0;
			 var installmentsecond = $("#current2HalfInstallment").val();
			  var installmentfirst =$("#current1HalfInstallment").val();
			  
			  var collectedAmount=0;
			  
			  var isSuccess=true;
			  
			var prevInstallmentAmt=-1;
			var prevActualAmt=-1;
			  
			$("#dcbOnlinePaymentTable tr.item").each(function() {
				i++;
			  $this = $(this);
			  var actamount = parseInt($this.find("#actualAmount").val());
			  var actcollection = parseInt($this.find("#actualCollection").val());
			  var installment = $this.find("#installment").val();
			  
			  $actualCollectionTextbox=$this.find("#actualCollection");
			  
			  
			  if(actamount == 0 && actcollection == 0){
				  j++;
			  }
			  
			  if(actcollection > actamount){
				  bootbox.alert('Collection should not be greater than actual amount');
				  e.preventDefault();
				  isSuccess=false;
				  return false;
			  }

			  var oldVal=0;
			  if($actualCollectionTextbox.data('collected-amount'))
			  {
				 
				  oldVal=parseInt($actualCollectionTextbox.data('collected-amount')); 
				  console.log("coming -> "+oldVal +" -> "+actcollection);
				  if(actcollection<oldVal)
				  {
					  bootbox.alert('Collection amount entered for installment '+installment +' is less than already existing collected amount');
					  e.preventDefault();
					  isSuccess=false;
					  return false;
				  }
			  }
			  
			  collectedAmount =collectedAmount+(actcollection-oldVal);
			
			  prevInstallmentAmt = $actualCollectionTextbox.val();
			  prevActualAmt=actamount;
			  
		   });
			
		   
			
			
		   if(isSuccess){
			   if(parseInt($('#receiptamount').val())==0)
				   {
				   bootbox.alert('Receipt amount should be greater than 0');
					  e.preventDefault();
				   return false;
				   }
			   if(parseInt($('#receiptamount').val()) !== collectedAmount)
			   {
				   bootbox.alert('Receipt amount should match with total collection amount');
					  e.preventDefault();
				   return false;
			   }
		   }
			
		   return isSuccess;
			
		});
	 
	 
