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
function goToPath(){
	if(dom.get("department").options[dom.get("department").selectedIndex].value>0){
	window.open("${pageContext.request.contextPath}/estimate/searchEstimate.action?execDept="+
	dom.get("department").options[dom.get("department").selectedIndex].value+"&source=wp"+"&wpdate="+dom.get("wpDate").value,"",
	 			"height=600,width=900,scrollbars=yes,left=0,top=0,status=yes");
	}
	else
	{
		dom.get("wp_error").style.display='';
		dom.get("wp_error").innerHTML='<s:text name="select.dept"/>';
		return false;
	}
	 dom.get("wp_error").style.display='none';
	 dom.get("wp_error").innerHTML='';
}
function update(elemValue) {
	var eId="";
	var estIds = elemValue.toString().split(",");
	var eleLen =estIds.length;
	for(var i=0;i<eleLen;i++)
	{
		if(eId=="")
			eId=estIds[i];
		else
			eId=eId+'`~`'+estIds[i];
	}
	if(dom.get("estimateListTable")!=null){
	if(dom.get("estimateListTable").rows.length>1){
		var len =dom.get("estimateListTable").rows.length;
		if(len>2){
			for(var i=0;i<len-1;i++)
			{
				if(eId=="")
					eId=document.forms[0].estId[i].value;
				else
					eId=eId+'`~`'+document.forms[0].estId[i].value;
			}
	 	 }
	 	 else{
	 	 	if(eId=="")
				eId=document.forms[0].estId.value;
			else
				eId=eId+'`~`'+document.forms[0].estId.value;
	 	 }
	  }
	}
	var actionUrl = '${pageContext.request.contextPath}/tender/ajaxWorksPackage-estimateList.action';
	var params    = 'estId=' + eId;
	var updatePage = 'worksPackage_estimatelist';
	<s:if test="%{sourcepage=='inbox' 
		&& (model.egwStatus!=null && model.egwStatus.code=='REJECTED' || model.egwStatus.code=='NEW')}">
		updatePage='worksPackage_estimatelist1';
	</s:if>
	var ajaxCall = new Ajax.Updater(updatePage,actionUrl,
	{parameters:params}		
	);
}
function deleterow(obj)
{
	var tbl = dom.get('estimateListTable');
	var rowNumber=getRow(obj).rowIndex;
	var currRow1=getRow(obj);
	dom.get('totalAmount').value = roundTo(eval(dom.get('totalAmount').value-getControlInBranch(currRow1,'wvIncldTaxes').value));
	tbl.deleteRow(rowNumber);
	if(dom.get("estimateListTable")!=null){
		if(dom.get("estimateListTable").rows.length>0){
		var len =dom.get("estimateListTable").rows.length;
		if(len>2){
			for(var i=0;i<len-1;i++)
			{
				document.forms[0].slNo[i].value=eval(i+1);
			}
	 	 }
	 	 else{
	 	 	document.forms[0].slNo.value=1;
	 	 }
	  }
	}
}
</script>
<table id="wpDetailsTable" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          	<td colspan="5" class="headingwk"><div class="arrowiconwk"><img src="/egworks/resources/erp2/images/arrow.gif" />
          	</div><div class="headplacer"><s:text name="wp.detils"/></div></td>
          	<td align="right" class="headingwk" style="border-left-width: 0px">
       		<a id="addHref" href="#" onclick="goToPath();return false;">
       		<img border="0" alt="Add Estimates" src="/egworks/resources/erp2/images/add.png" /></a>
       		</td>
       	</tr>
       	<td width="5%" class="tablesubheadwk">
			<s:text name='estimate.search.slno' />
		</td>
		<td width="15%" class="tablesubheadwk">
			<s:text name='estimate.search.estimateNo' />
		</td>
		<td width="40%" class="tablesubheadwk" style="WORD-BREAK:BREAK-ALL">
			<s:text name='estimate.search.name' />
		</td>
		<td width="10%" class="tablesubheadwk">
			<s:text name='estimate.search.estimateDate' />
		</td>
		<td width="15%" class="tablesubheadwk">
			<s:text name='estimate.search.total' />
		</td>
		<td width="10%" class="tablesubheadwk">
			Delete
		</td>
	</tr>	
</table>
<s:if test="%{abstractEstimateList.isEmpty()}">
	<div  id="worksPackage_estimatelist">
       	</div> 
</s:if>
<s:else>
<div  id="worksPackage_estimatelist1">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="estimateListTable" name="estimateListTable">
	<s:iterator var="e" value="abstractEstimateList" status="s">
	<tr>
		<td width="5%"  class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="text" name="slNo" id="slNo" readonly="true" disabled="true" size="1" value='<s:property value='%{#s.index+1}' />'/>
		</td>
		<td width="15%" class="whitebox3wk">&nbsp;&nbsp;&nbsp;&nbsp;
			<s:property value='%{estimateNumber}' /><s:hidden name="estId" id="estId" value="%{id}"/>
		</td>
		<td width="40%" class="whiteboxwkwrap" style="WORD-BREAK:BREAK-ALL">
			<s:property value='%{name}' />
		</td>
		<td width="10%" class="whitebox3wk">&nbsp;&nbsp; 
			<s:property value='%{estimateDate}' />
		</td>
		<td width="15%" class="whitebox3wk">
			<div align="right">
				<s:property value='%{workValueIncludingTaxes.formattedString}' />
				<s:hidden name="wvIncldTaxes" id="wvIncldTaxes" value="%{workValueIncludingTaxes.formattedString}"/>
			</div>
		</td>
		<td align="right" width="10%" class="headingwk" style="border-left-width: 0px">
       		<a id="delHref" href="#" onclick="deleterow(this)">
       		<img border="0" alt="Delete Estimates" src="/egworks/resources/erp2/images/cancel.png" /></a>
       	</td>
	</tr>
</s:iterator>	
<tr><td colspan="5" style="background-color:#F4F4F4;" align="right"><b>Total:</b>&nbsp;
<input type="text" size="8" name="totalAmount" id="totalAmount" value="<s:property value="%{worktotalValue.formattedString}" />" 
class="amount" readonly="readonly"/></td>				
<td colspan="5" style="background-color:#F4F4F4;">&nbsp;</td>
</tr>	
</table>
</div> 
</s:else>
