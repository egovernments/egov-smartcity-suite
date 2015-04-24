<%@ include file="/includes/taglibs.jsp" %>
	<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Details"/></span></div></td>
	</tr>
  	<tr>
  		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"><s:text name="plotAreaSqft" /> : <span class="mandatory" >*</span></td>
        <td class="greybox" width="26%"><s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" /></td>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="plotAreaSqmt" /> : <span class="mandatory" >*</span></td>
        <td class="greybox"><s:textfield id="plotAreaInSqmt" name="regnDetails.sitalAreasqmt" value="%{regnDetails.sitalAreasqmt}" maxlength="9"  readonly="true"  /></td>
		<s:hidden id="regnDetails.id" name="regnDetails.id" />
	</tr> 
             
 <script>
 

	function convertSQFTtoSQMT(){
		var sqft= document.getElementById('plotAreaInSqft').value;
		if (!isNaN(sqft)){
				var sqmt=(sqft*0.092903);
				document.getElementById('plotAreaInSqmt').value=sqmt;
				callAdmissionFeeAmount();
			}
		else{
			alert("Enter Numeric values");
			document.getElementById('plotAreaInSqft').value="";
			document.getElementById('plotAreaInSqmt').value="";
			}
	}

	function convertSQMTtoSQFT(){
		var sqmt= document.getElementById('plotAreaInSqmt').value;
		if(!isNaN(sqmt)){
			var sqft=(sqmt*10.7639);
			document.getElementById('plotAreaInSqft').value=sqft;
			callAdmissionFeeAmount();
		}
	else{
		alert("Enter Numeric values");
		document.getElementById('plotAreaInSqft').value="";
		document.getElementById('plotAreaInSqmt').value="";
		}
	}
	
 </script>