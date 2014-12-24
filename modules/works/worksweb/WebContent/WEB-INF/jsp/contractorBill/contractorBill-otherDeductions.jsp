<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %> 
<style type="text/css">

</style>
<script src="<egov:url path='js/works.js'/>"></script>
<script>
function validateOtherDeductions(elem,recordId){
	if(!validateForStdDeduction())
		return false; 
	if(!validateForCustDeduction())
		return false; 
	if(!validateForRetMoney())
		return false; 
		
	return true;
}
</script>
<div class="errorstyle" id="otherDeductions_error" style="display:none;"></div>
<tr>
  	<td align="left"><span class="epstylewk"><s:text name="contractorBill.subheader.otherdedcution"/>:</span></td>
  	<td colspan="3">&nbsp;</td>
</tr>
<tr><td>&nbsp;</td></tr>
<%@ include file="contractorBill-standardDeductions.jsp"%>
<tr><td>&nbsp;</td></tr>	
<%@ include file="contractorBill-customDeductions.jsp"%>
<tr><td>&nbsp;</td></tr>	
<%@ include file="contractorBill-retentionMoney.jsp"%>