<%@ include file="/includes/taglibs.jsp" %>
	<tr>
		<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="Site Details"/></span></div></td>
	</tr>
  	<tr>
  		<td class="greybox" width="13%">&nbsp;</td>
        <td class="greybox" width="13%"  ><s:text name="plotAreaSqft" /> : <span class="mandatory" >*</span></td>
        
    <s:if test="%{userRole!='PORTALUSERSURVEYOR'}">
      <td class="greybox" width="26%" style="font-weight:bold;font-size:13px">
        <s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" /></td>
		
        </s:if>
        <s:else>
        
         <td class="greybox" width="26%" > <s:property value="%{regnDetails.sitalAreasqft}"/></td>
        <td style="display: none;" > <s:textfield id="plotAreaInSqft" name="regnDetails.sitalAreasqft" value="%{regnDetails.sitalAreasqft}" maxlength="9" onblur="convertSQFTtoSQMT()" /></td>
		
        </s:else>
		<td class="greybox">&nbsp;</td>
		<td class="greybox" width="20%"   ><s:text name="plotAreaSqmt" /> :</td>
		<td class="greybox" style="font-weight:bold;font-size:13px"><s:textfield id="plotAreaInSqmt"  style ="border: none" name="regnDetails.sitalAreasqmt" value="%{regnDetails.sitalAreasqmt}" maxlength="9"  readonly="true"  /></td>
      	<s:hidden id="regnDetails.id" name="regnDetails.id" /></td>
	</tr>  
             
 <script>

 function convertSQFTtoSQMTforDisplay(){} 

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