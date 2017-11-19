<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<script>
function setupAdvanceAdjustmentValues(){

	if(dom.get("totalAdvancePaid").value=='')
		dom.get("totalAdvancePaid").value=0;
	if(dom.get("totalPendingBalance").value=='')
		dom.get("totalPendingBalance").value=0;
	if(dom.get('currenbilldeduction').value=='')
		dom.get("currenbilldeduction").value=0;
	return true;
}
	totalPenBalLoadHandler = function(req,res){
	  results=res.results;
	  dom.get("totalAdvancePaid").value=results[0].totalAdvancePaid;
	  dom.get("totalAdvancePaid").value=roundTo(dom.get("totalAdvancePaid").value);
	  dom.get("totalPendingBalance").value=results[0].totalPendingBalance;
	  dom.get("totalPendingBalance").value=roundTo(dom.get("totalPendingBalance").value);
	  document.getElementById('bal').style.display='';	
	   document.getElementById('disp').value="yes";
	   document.getElementById("currenbilldeduction").value="0.00";	
	   if(dom.get("totalPendingBalance").value==0.0){	   	
	   	document.getElementById('currenbilldeduction').readOnly=true;
	   }
	 }
	
	totalPenBalLoadFailureHandler= function(){
	    dom.get("accountDetails_error").style.display='';
		dom.get("accountDetails_error").innerHTML='<s:text name="contractorbill.advanceadjustment.unable" />';
	}
	
	
	function calculateAdvanceAdjustment(){
		workOrderId = dom.get("workOrderId").value;
		workOrderEstimateId = dom.get("workOrderEstimateId").value;
		billDate = dom.get("billdate").value;
		billId = '<s:property value="%{id}" />'
		makeJSONCall(["totalAdvancePaid","totalPendingBalance"],'${pageContext.request.contextPath}/contractorBill/ajaxContractorBill!calculateAdvanceAdjustment.action',
	    {billDate:billDate,workOrderEstimateId:workOrderEstimateId,billId:billId},totalPenBalLoadHandler,totalPenBalLoadFailureHandler) ;
	   
	}
	
	function validateNumbers(element){		
	 		dom.get('errorcurrenbilldeduction').style.display='none';
	 		if(isNaN(document.getElementById("currenbilldeduction").value)){
	 			dom.get('errorcurrenbilldeduction').style.display='';
			  	dom.get("accountDetails_error").style.display='';     
				dom.get("accountDetails_error").innerHTML='<s:text name="contractorbill.common.invalidnumber" />';
				document.getElementById("currenbilldeduction").focus();
				return false;
			}
			dom.get("currenbilldeduction").value=roundTo(dom.get("currenbilldeduction").value);
			if(parseFloat(document.getElementById("currenbilldeduction").value) < 0.0){
				dom.get('errorcurrenbilldeduction').style.display='';
			 	dom.get("accountDetails_error").style.display='';     
				dom.get("accountDetails_error").innerHTML='<s:text name="contractorbill.common.invalidnumber" />';
				document.getElementById("currenbilldeduction").focus();
				return false;		
			}	
			
			if(parseFloat(document.getElementById("currenbilldeduction").value) > parseFloat(document.getElementById("totalPendingBalance").value)){
				dom.get('errorcurrenbilldeduction').style.display='';
				dom.get("accountDetails_error").style.display='';     
				dom.get("accountDetails_error").innerHTML='<s:text name="contractorbill.currenbilldeduction.greaterthan.totalPendingBalance" />';
				document.getElementById("currenbilldeduction").focus();
				return false;
			}
					
			if(parseFloat(document.getElementById("currenbilldeduction").value) > parseFloat(document.getElementById("billamount").value)){
				dom.get('errorcurrenbilldeduction').style.display='';
				dom.get("accountDetails_error").style.display='';     
				dom.get("accountDetails_error").innerHTML='<s:text name="contractorbill.currenbilldeduction.greaterthan.billamount" />';
				document.getElementById("currenbilldeduction").focus();
				return false;
			}
			calculateTotal();
			
			dom.get('errorcurrenbilldeduction').style.display='none';
			dom.get("accountDetails_error").innerHTML='';
			dom.get("accountDetails_error").style.display='none';     
	      return true;
	}
</script>

<tr>
  	<td colspan="3" class="headingwk" style="border-right-width: 0px"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" /></div>
   		<div class="headplacer"><s:text name="contractorBill.subheader.advanceadjustment" /></div>
	</td>
	<td align="right" class="headingwk" style="border-left-width: 0px">
	 <s:if test="%{sourcepage=='search'}">
	 <script>
	 calculateAdvanceAdjustment();
	 </script>
	 </s:if>
	<div id="addbtn">
		<a href="#" onclick="calculateAdvanceAdjustment();return false;"><img border="0" alt="Add Row" src="/egworks/resources/erp2/images/add.png" /></a>
	</div>
  	</td>
</tr>

<tr id="bal">
	<td class="greyboxwk" colspan="4">
		<table cellspacing="0" border="0">
			<tr>
				<td class="greyboxwk"><s:text name='contractorBill.advanceadjustment.totaladvancepaid'/>: </td>
			    <td class="greybox2wk"><input type="text" name="totalAdvancePaid" id="totalAdvancePaid" value='<s:property value="totalAdvancePaid" />'  readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>
				<td class="greyboxwk"><s:text name='contractorBill.advanceadjustment.totalPendingBalance'/>: </td>
			    <td class="greybox2wk"><input type="text" name="totalPendingBalance" id="totalPendingBalance" value='<s:property value="%{totalPendingBalance}" />'  readonly="readonly" tabIndex="-1" class="selectamountwk" /></td>
			    <td class="greyboxwk"><s:text name='contractorBill.advanceadjustment.currenbilldeduction'/>: </td>        
			    <td class="greybox2wk">	
					<input type="text" name="advaceAdjustmentCreditAmount" id="currenbilldeduction" value='<s:property value="%{advaceAdjustmentCreditAmount}"/>' tabIndex="-1" class="selectamountwk"  onblur='validateNumbers(this);'/>
					<span id='errorcurrenbilldeduction' style='display:none;color:red;font-weight:bold'>&nbsp;x</span>	
					<s:hidden name="disp" id="disp" />
			    </td>
			</tr>
	    </table>
    </td>  
</tr>

