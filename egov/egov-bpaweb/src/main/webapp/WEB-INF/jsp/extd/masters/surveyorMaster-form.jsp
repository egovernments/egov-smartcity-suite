<head>
<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />
<script>
jQuery.noConflict();
jQuery(document).ready(function(){
	 
	jQuery('#fromDate').datepicker({ dateFormat: 'dd/mm/yy'});
	jQuery('#fromDate').datepicker('getDate');   
	  jQuery('#fromDate').datepicker( "option", "dateFormat", "dd/mm/yy" );
	  jQuery('#toDate').datepicker({ dateFormat: 'dd/mm/yy'});
	  jQuery('#toDate').datepicker('getDate');   
	    jQuery('#toDate').datepicker( "option", "dateFormat", "dd/mm/yy" );	
		
	
	});

	function addGrid(tableId,trId)
	{
	jQuery("[id^=fromDate]").each(function(index){
		 jQuery(this).datepicker('destroy');
		 });
	jQuery("[id^=toDate]").each(function(index){
		 jQuery(this).datepicker('destroy');
		 });
	var tbl = document.getElementById(tableId);
	if(tableId=='grid')
	{
		var rowObj = dom.get(trId).cloneNode(true);
		addRow(tbl,rowObj);
		var lastRow = tbl.rows.length;
		document.forms[0].boundaryId[lastRow-2].value=-1;
		document.forms[0].surveyorClass[lastRow-2].value=-1;
		document.forms[0].status[lastRow-2].value=-1;
		document.forms[0].fromDate[lastRow-2].value="";
		document.forms[0].toDate[lastRow-2].value="";
	}

	 jQuery("[id=fromDate]").each(function(index){
		 jQuery(this).datepicker({
		  dateFormat: 'dd/mm/yy',
		 beforeShow: function(date) {
		            jQuery("[id=fromDate]").each(function(index){
		               jQuery(this).attr('id','newid');
		            });
		            jQuery(this).attr('id','fromDate');
		        },
		  onClose: function(date) {
		            jQuery("[id=newid]").each(function(index){
		               jQuery(this).attr('id','fromDate');
		            });
		          
		        }       
		});
		 });
	 jQuery("[id=toDate]").each(function(index){
		 jQuery(this).datepicker({
		  dateFormat: 'dd/mm/yy',
		 beforeShow: function(date) {
		            jQuery("[id=toDate]").each(function(index){
		               jQuery(this).attr('id','newid');
		            });
		            jQuery(this).attr('id','toDate');
		        },
		  onClose: function(date) {
		            jQuery("[id=newid]").each(function(index){
		               jQuery(this).attr('id','toDate');
		            });
		          
		        }       
		});
		 });
	}
	function addRow(tableObj,rowObj)
	{
    	var tbody=tableObj.tBodies[0];
		tbody.appendChild(rowObj);
	} 	

	function validateName( strValue)
		{
			       var iChars = "^+";
			
			       for (var i = 0; i < strValue.value.length; i++)
			       {
			       	if (iChars.indexOf(strValue.value.charAt(i)) != -1)
			       	{
						alert("Please remove the special characters ^ and/or +.");
						strValue.focus();
						return false;
			       	}
			       }
			
  			}

	function removecmdaRow(obj)
	{ 
	  
  	var tb1=document.getElementById("grid");
     var lastRow = (tb1.rows.length)-1;
     var curRow=getRow(obj).rowIndex;
     dom.get("surveyor_error").style.display='none';
     if(lastRow ==1 )
   	{
  		 dom.get("surveyor_error").style.display='none';
  		 document.getElementById("surveyor_error").innerHTML='This row can not be deleted';
			 dom.get("surveyor_error").style.display='';
   	     return false;
     }
   	else
   	{
   		 
        	var updateserialnumber=curRow;
    		
			tb1.deleteRow(curRow);
		
	      	return true;
  	 }
	}

	function fnValidatePAN(strValue) {
	  
    var panPat = /^([a-zA-Z]{5})(\d{4})([a-zA-Z]{1})$/;
    if (strValue.value!=null && strValue.value!="" && strValue.value.search(panPat) == -1) {
        alert("Invalid Pan Number");
        strValue.value="";
        strValue.focus();
        return false;
   	 } 
	return true;
	}



	function validation(){
	var fromdate=document.getElementById('fromDate').value;
	var todate=document.getElementById('toDate').value;
	if( jQuery('#code').val()==""){
	alert("Surveyor Code is mandatory");
	return false;

	}
	if( jQuery('#name').val()==""){
		alert("Surveyor Name is mandatory");
		return false;

		}
	if( jQuery('#correspondenceAddress').val()==""){
		alert("Correspondence Address is mandatory");
		return false;

		}
	if( jQuery('#officeAddress').val()==""){
		alert("Office Address is mandatory");
		return false;

		}
	if( jQuery('#mobileNumber').val()==""){
		alert("Mobile Number is mandatory");
		return false;

		}
	if( jQuery('#email').val()==""){
		alert("Email Id is mandatory");
		return false;

		}
	
	if(!validateFloorDetail())
	{ 
	return false;
	}
	if(!uniqueFloorName())
    {
	return false;
	}
	
	}

	function validateFloorDetail()
	{

	var tableObj=document.getElementById('grid');
	var lastRow = tableObj.rows.length;
	var zoneName,surveyorClass,status,fromDate,toDate;
	var i;
	if(lastRow>2)
	{
	
	for(i=0;i<lastRow-1;i++)
	{
    	
		zoneName=document.forms[0].boundaryId[i].value;
     	surveyorClass=document.forms[0].surveyorClass[i].value;
     	status=document.forms[0].status[i].value;
     	fromDate=document.forms[0].fromDate[i].value;
     	toDate=document.forms[0].toDate[i].value;
     	
      	 if(!validateFloorLines(zoneName,surveyorClass,status,fromDate,toDate,i+1))
        	   return false;
 		}
		
		return true;
	}
	else
	{
		zoneName=document.getElementById('boundaryId').value;
	    surveyorClass=document.getElementById('surveyorClass').value;
	    status=document.getElementById('status').value;
	    fromDate=document.getElementById('fromDate').value;
	    toDate=document.getElementById('toDate').value;
	    if(!validateFloorLines(zoneName,surveyorClass,status,fromDate,toDate,1))
	       return false;
	     else
	       return true;
	    
	}
	}

	function validateFloorLines(zoneName,surveyorClass,status,fromDate,toDate,row)
	{
	dom.get("surveyor_error").style.display='none';
	var todaysDate=getTodayDate();
	 	if(zoneName=="" || zoneName==-1)
 	{
	     	
    	document.getElementById("surveyor_error").innerHTML='';
			document.getElementById("surveyor_error").innerHTML='Enter Zone Number for row'+" :"+row;
			dom.get("surveyor_error").style.display='';
			return false;
    }
	 	if(status=="" || status==-1)
	 	{
	     	
	    	document.getElementById("surveyor_error").innerHTML='';
				document.getElementById("surveyor_error").innerHTML='Enter Surveyor Status for row'+" :"+row;
				dom.get("surveyor_error").style.display='';
				return false;
	    }
	 	if(fromDate=="" || fromDate==null)
	 	{
	     	
	    	document.getElementById("surveyor_error").innerHTML='';
				document.getElementById("surveyor_error").innerHTML='Enter From Date for row'+" :"+row;
				dom.get("surveyor_error").style.display='';
				return false;
	    }
	 	if(surveyorClass=="" || surveyorClass==-1)
	 	{
	     	
	    	document.getElementById("surveyor_error").innerHTML='';
				document.getElementById("surveyor_error").innerHTML='Enter Surveyour Class for row'+" :"+row;
				dom.get("surveyor_error").style.display='';
				return false;
	    }
	 	if(toDate=="" || toDate==null)

	 	{
	     	
	    	document.getElementById("surveyor_error").innerHTML='';
				document.getElementById("surveyor_error").innerHTML='Enter To Date for row'+" :"+row;
				dom.get("surveyor_error").style.display='';
				return false;
	    }
	 	if((fromDate!=null && fromDate!="" && fromDate!=undefined) ||( toDate!=null && toDate!="" && toDate!=undefined )){
			if(compareDate(fromDate,todaysDate) == -1)
			{						  	 	
				 dom.get("surveyor_error").style.display = '';
				 document.getElementById("surveyor_error").innerHTML = 'Fromdate should not be greater than Todays date for row'+" :"+row;			  					 
				 fromDate.value="";
				
				 return false;
			}
			
			if(compareDate(fromDate,toDate) == -1)
			{		
					
				dom.get("surveyor_error").style.display = '';
				 document.getElementById("surveyor_error").innerHTML = 'From Date Should Not be greater than ToDate for row'+" :"+row;			  
				 fromDate.value="";
				 toDate.value="";    
				
				 return false;
			}
		
		}
	 
	 	if(status=="BlackList" || status =="Debarred")
		{
			if(jQuery('#narration').val()=="")
			{
				dom.get("surveyor_error").style.display = '';
				document.getElementById("surveyor_error").innerHTML = 'Please Enter Remarks in Narration';			  
				jQuery('#narration').focus();
				return false;
			}
		}
	 	
	 	  return true;
	}
 /*
 * To check the Unique Floore Name for rows...
 */
 	function uniqueFloorName()
	{
 	var tableObj=document.getElementById('grid');
 	var lastRow = tableObj.rows.length;
 	var zoneName,zoneName1;
 	var i,j;
 	if(lastRow>2)
 	{
		for(i=0;i<lastRow-2;i++)
		{
			
    	 	zoneName=document.forms[0].boundaryId[i].value;
    	 	for(j=i+1;j<lastRow-1;j++)
    		 {
     	    	zoneName1=document.forms[0].boundaryId[j].value;
     	    	
          	 	
        		if(zoneName1 == zoneName ){
        		dom.get("surveyor_error").style.display='';
       	    	document.getElementById("surveyor_error").innerHTML='Zone number cannot be same for row :'+(j+1) +" and row :" + (i+1);
       	        dom.get("surveyor_error").style.display='';
  				return false;
            	 }
        	 }
     		}
    	}
 	
		return true;
	}
 	function checkNotSpecial(obj)
 	{

 	var iChars = "!@$%^*+=[']`~';{}|\":<>?#_./";
 	for (var i = 0; i < obj.value.length; i++)
 	{
 		if ((iChars.indexOf(obj.value.charAt(i)) != -1)||(obj.value.charAt(0)==" "))
 	{
     dom.get("surveyor_error").style.display='';
 	document.getElementById("surveyor_error").innerHTML='Special characters are not allowed'
 	obj.value="";
 	obj.focus();
 	return false;
 	}
    }
 	return true;
 	}
 	function checkbankNameEntry()
 	{
	 var bankid =document.getElementById('bankId').options[document.getElementById('bankId').selectedIndex].value;
	
	 if(bankid==-1 || bankid=="")
		{
			alert("Please Select Bank ");
			 document.getElementById('bankId').focus();
			return;
		}
	 }

	//on change of bank  attached bank account no and IFsc code is get clear
 	function  loadBankBranchValuesByBankId()
 	{
 	 var bankObj = document.getElementById('bankId');
      var bank = bankObj.options[bankObj.selectedIndex].value;

 	
 	 document.getElementById('surveyorifsc').value="";
 	 document.getElementById('accountNumber').value="";	
	}

 	function validateEmail()
 	{	
 	dom.get("surveyor_error").style.display='none';
 	var x=document.getElementById("email").value;
 	if(x!=""){
 	 	var at="@";
 		var atpos=x.indexOf(at);
 		var dotpos=x.lastIndexOf(".");
 		
 		if (atpos<1 || dotpos<atpos+2 || dotpos+2>=x.length || (x.indexOf(at,(atpos+1))!=-1))
 		{
 			dom.get("surveyor_error").style.display = '';
 			document.getElementById("surveyor_error").innerHTML = '<s:text name="invalid.email.address" />';
 			document.getElementById("email").value="";
 		  return false;
 		}
 	}
 	return true;
 	}


 	function validateMobileNumber(obj)
 	{

 	var text = obj.value;
 	if(text!=''){
 		
 		if(text.length!=10)
 		{		
 			obj.value="";
 			dom.get("surveyor_error").style.display = '';
 			document.getElementById("surveyor_error").innerHTML = '<s:text name="invalid.mobileno.length" />';
 			return false;
 	
 		}
 	validatePhoneNumber(obj);
 	}
 	return true;
 	}

 	function validatePhoneNumber(obj){
 	var text = obj.value;
 	if(text!=""){
 		
 	var msg='<s:text name="invalid.mobileno" />';
 	
 	if(isNaN(text))
 	{
 		dom.get("surveyor_error").style.display = '';
 		document.getElementById("surveyor_error").innerHTML = msg;
 		obj.value="";
 		return false;
 	}
 	if(text<=0)
 	{
 		dom.get("surveyor_error").style.display = '';
 		document.getElementById("surveyor_error").innerHTML = msg;
 		obj.value="";
 		return false;
 	}
 	if(text.replace(".","~").search("~")!=-1)
 	{
 		dom.get("surveyor_error").style.display = '';
 		document.getElementById("surveyor_error").innerHTML = '<s:text name="period.notallowed" />';
 		obj.value='';
 		return false;
 	}
 	if(text.replace("+","~").search("~")!=-1)
 	{
 		dom.get("surveyor_error").style.display = '';
 		document.getElementById("surveyor_error").innerHTML = '<s:text name="plus.notallowed" />';
 		obj.value='';
 		return false;
 	}
 	}
 	return true;
 	}
 	
 	 
</script>
</head>
<fieldset>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<s:token />
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><label for="surveyorCode"><s:text
						name="surveyor.code" /><span class="mandatory">*</span>:</label></td>
			<td class="bluebox"><s:textfield label="surveyorCode" id="code"
					name="model.code" maxlength="20" onblur="checkNotSpecial(this);" /></td>
			<td class="bluebox"><label for="surveyorName"><s:text
						name="surveyor.name" /><span class="mandatory">*</span>:</label>
			<td class="bluebox"><s:textfield label="surveyorName" id="name"
					name="name" maxlength="100" onblur="checkNotSpecial(this);" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="corrAdd"><s:text
						name="surveyor.corrAdd" /><span class="mandatory">*</span>:</label></td>
			<td colspan="1" class="greybox"><s:textarea label="title"
					cols="40" rows="5" name="model.correspondenceAddress"
					id="correspondenceAddress" maxlength="200" /></td>
			<td class="greybox"><label for="offAdd"><s:text
						name="surveyor.offAdd" /><span class="mandatory">*</span>:</label></td>
			<td colspan="1" class="greybox"><s:textarea label="title"
					cols="40" rows="5" maxlength="200" name="model.officeAddress"
					id="officeAddress" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><label for="surveyorCode"><s:text
						name="surveyor.email" /><span class="mandatory">*</span>:</label></td>
			<td class="bluebox"><s:textfield label="surveyorEmail"
					id="email" name="email" maxlength="128" onblur="validateEmail();" /></td>
			<td class="bluebox"><label for="surveyorName"><s:text
						name="surveyor.mobile" /><span class="mandatory">*</span>:</label>
			<td class="bluebox"><s:textfield label="surveyorMobile"
					id="mobileNumber" name="mobileNumber" maxlength="10"
					onblur="validateMobileNumber(this)" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="surveyorPan"><s:text
						name="surveyor.pan" />:</label></td>
			<td class="greybox"><s:textfield label="surveyorPan"
					name="model.panNumber" maxlength="10"
					onblur="return fnValidatePAN(this);" /></td>
			<td class="greybox"><label for="surveyorTin"><s:text
						name="surveyor.tin" />:</label>
			<td class="greybox"><s:textfield label="surveyorTin"
					name="model.tinNumber" maxlength="11" onblur="validateName(this);" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><label for="bank"><s:text
						name="surveyor.bank" />:</label></td>
			<td class="bluebox"><s:select headerKey="-1"
					headerValue="%{getText('surveyor.default.select')}"
					name="model.bank.id" id="bankId" cssClass="selectwk"
					list="dropdownData.bankList" listKey="id" listValue="name"
					onchange="loadBankBranchValuesByBankId();" /></td>
			<td class="bluebox"><label for="surveyorifsc"><s:text
						name="surveyor.ifsc" />:</label>
			<td class="bluebox"><s:textfield label="surveyorifsc"
					id="surveyorifsc" name="model.bankIFSC" maxlength="20"
					onclick="checkbankNameEntry()" onblur="checkNotSpecial(this);" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="surveyorbankaccount"><s:text
						name="surveyor.bankaccount" />:</label></td>
			<td class="greybox"><s:textfield label="surveyorbankaccount"
					id="accountNumber" name="model.accountNumber" maxlength="20"
					onclick="checkbankNameEntry()" onblur="checkNotSpecial(this);" /></td>
			<td class="greybox"><label for="isEnabled"><s:text
						name="surveyor.enabled" />:</label></td>
			<td class="greybox"><s:checkbox label="isEnabled" id="isEnabled"
					name="model.isEnabled" /></td>
		</tr>
		<tr>
			<td class="bluebox">&nbsp;</td>
			<td class="bluebox"><label for="narration"><s:text
						name="surveyor.narration" />:</label></td>
			<td colspan="2" class="bluebox"><s:textarea label="narration"
					cols="70" rows="8" maxlength="200" name="model.narration"
					id="narration" /></td>
			<td class="bluebox">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="5" class="bluebox"></td>
		</tr>
		<tr>
			<td colspan="5"><div class="subheadsmallnew"
					id="surveyordetails">
					<label for="surveyordetails"><s:text
							name="surveyor.details" /></label>
				</div></td>
		</tr>
		<tr>
			<td colspan="5">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="tablebottom" name="grid" id="grid">
					<tr>
						<th class="bluebgheadtd" width="20%"><label for="zone"><s:text
									name="surveyor.department" /><span class="mandatory">*</span></th>
						<th class="bluebgheadtd" width="10%"><label for="class"><s:text
									name="surveyor.class" /><span class="mandatory">*</span></label></th>
						<th class="bluebgheadtd" width="20%"><label for="status"><s:text
									name="surveyor.status" /><span class="mandatory">*</span></label></th>
						<th class="bluebgheadtd" width="20%"><label for="fromdate"><s:text
									name="surveyor.fromdate" /><span class="mandatory">*</span></label></th>
						<th class="bluebgheadtd" width="20%"><label for="fromdate"><s:text
									name="surveyor.todate" /><span class="mandatory">*</span></label></th>
						<th class="bluebgheadtd" width="10%"><label for="adddel"><s:text
									name="surveyor.add.del" /></label></th>
					</tr>
					<s:if test="surveyorList==null || surveyorList.size()==0">
						<tr id="detailsRow1">
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="boundaryId" id="boundaryId" cssClass="selectwk"
										list="dropdownData.boundaryList" listKey="id" listValue="name" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="surveyorClass" id="surveyorClass" cssClass="selectwk"
										list="dropdownData.surveyorClassList" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="surveyorStatus" id="status" cssClass="selectwk"
										list="dropdownData.surveyorStatusList" />
								</div>
							</td>

							<td class="blueborderfortd"><div align="center">
									<s:textfield name="fromDate" id="fromDate" readonly="true"
										displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" />
								</div></td>
							<td class="blueborderfortd"><div align="center">
									<s:textfield name="toDate" id="toDate" readonly="true"
										displayFormat="dd/mm/yy" showOn="focus" cssClass="tablerow" />
								</div></td>



							<td id="pet" class="blueborderfortd">
								<p align="center">
									<img src="${pageContext.request.contextPath}/images/addrow.gif"
										alt="Add" width="18" height="18" border="0"
										onclick="addGrid('grid','detailsRow1')" /><img
										src="${pageContext.request.contextPath}/images/removerow.gif"
										alt="Remove" width="18" height="18" border="0"
										onclick="removecmdaRow(this);" />
								</p>
							</td>
						</tr>
					</s:if>
					<s:else>
						<s:iterator value="(surveyorList.size).{#this}"
							status="srvyrstatus">
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="surveyorList[%{#srvyrstatus.index}].boundary.id"
										id="boundaryId" cssClass="selectwk"
										list="dropdownData.boundaryList" listKey="id"
										listValue="name" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="surveyorList[%{#srvyrstatus.index}].surveyorClass"
										id="surveyorClass" cssClass="selectwk"
										list="dropdownData.surveyorClassList" />
								</div>
							</td>
							<td class="blueborderfortd">
								<div align="center">
									<s:select headerKey="-1"
										headerValue="%{getText('surveyor.default.select')}"
										name="surveyorList[%{#srvyrstatus.index}].status" id="status"
										cssClass="selectwk" list="dropdownData.surveyorStatusList" />
								</div>
							</td>

							<td class="blueborderfortd"><div align="center">
									<s:textfield
										name="surveyorList[%{#srvyrstatus.index}].fromDate"
										id="fromDate" readonly="true" displayFormat="dd/mm/yy"
										showOn="focus" cssClass="tablerow" />
								</div></td>
							<td class="blueborderfortd"><div align="center">
									<s:textfield name="surveyorList[%{#srvyrstatus.index}].toDate"
										id="toDate" readonly="true" displayFormat="dd/mm/yy"
										showOn="focus" cssClass="tablerow" />
								</div></td>

							<td id="pet" class="blueborderfortd">
								<p align="center">
									<img src="${pageContext.request.contextPath}/images/addrow.gif"
										alt="Add" width="18" height="18" border="0"
										onclick="addGrid('grid','detailsRow1')" /><img
										src="${pageContext.request.contextPath}/images/removerow.gif"
										alt="Remove" width="18" height="18" border="0"
										onclick="removecmdaRow(this)" />
								</p>
							</td>
							</tr>
						</s:iterator>
					</s:else>
				</table>
			</td>
		</tr>
	</table>
</fieldset>
