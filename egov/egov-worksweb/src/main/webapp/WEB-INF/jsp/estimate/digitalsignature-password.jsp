<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  

<script>

function showSignaturePwdField(obj) {
	if(obj.checked) {
		document.getElementById("signPwdLabel").style.display="";
		document.getElementById("signPwdField").style.display="";
		document.getElementById("signaturePassword").disabled = false;
		document.getElementById("digitalSign").value = true;
	}
	else {
		document.getElementById("signPwdLabel").style.display="none";
		document.getElementById("signPwdField").style.display="none";
		document.getElementById("signaturePassword").disabled = true;
		document.getElementById("digitalSign").value = false;
	}
}



</script>


<table width="100%" border="0" cellspacing="0" cellpadding="0">            
  	<tr>
	 	<td class="whiteboxwk"><s:text name ="Digitally Sign" /></td>
 	 	<td class="whitebox2wk"><input type="checkbox" id="digitalSignCheckbox" onchange="showSignaturePwdField(this)" />
 	 		<s:hidden name="digitalSign" id="digitalSign"/> </td>
 	 	<td class="whiteboxwk" id="signPwdLabel" style="display:none"><s:text name ="Signature Password" /></td>
 	 	<td class="whitebox2wk" id="signPwdField" style="display:none"><s:password name="signaturePassword"  id="signaturePassword" maxlength="30" cssClass="input"  /><td>
	 </tr>	 
  	 <tr><td colspan="4" class="shadowwk"> </td></tr>		
</table>   

     