<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<jsp:include page="../voucher/vouchertrans-filter-new.jsp"/>
<tr>
	<td class="greybox"></td>
	<td class="greybox"><s:text name="function"/></td>
	<td class="greybox" ><s:textfield name="commonBean.functionName" id="commonBean.functionName" onkeyup="autocompletecodeFunctionHeader(this,event)" onblur="fillNeibrAfterSplitFunctionHeader(this)"  size="30"/>
	 <s:hidden name="commonBean.functionId" id="commonBean.functionId" /></td>
	<td class="greybox"></td>
	<td class="greybox"></td>
	<!--<td class="greybox"><s:text name="inward.serial.number"/></td>
	<td class="greybox"><s:textfield name="commonBean.inwardSerialNumber" id="commonBean.inwardSerialNumber"/></td>
--></tr>

<tr>
	<td class="bluebox"></td>
	<td class="bluebox"><s:text name="voucher.narration"/></td>
	<td class="bluebox" colspan="3"><s:textarea name="description" id="description"  cols="100"/><br/><span class="highlight2">Max. 1024 characters</span></td> 
</tr>
<tr id="budgetReappRow">
<td class="greybox"></td>
<td class="greybox"><s:text name="budget.reappropriation.number"/></td>
<td class="greybox"><s:textfield name="commonBean.budgetReappNo" id="commonBean.budgetReappNo"/></td>
<td class="greybox"></td>
<td class="greybox"></td>
</tr>


<tr>
<td class="greybox" colspan="6" style="text-align:center"/>
<div class="generatecheque">
<a onclick="hideShow()" href="#">
Show/Hide Details</a></div>
</tr>
<table>
<tr>
<td colspan="6">
<hr class="blankline"/>
</td>
</tr>
</table>
<center>
<div>
<table width="90%" cellspacing="0" cellpadding="0" border="0" class="tablebottom" align="center">
	<tr>
	<td class="bluebox3" colspan="5">
		<div class="billheadnew">
			<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody>
				<tr>
				<td class="bluebox"><s:text name="subledger.type"/></td>
				<td  class="bluebox" align="left" ><s:select name="commonBean.subledgerType" id="commonBean.subledgerType"  list="dropdownData.accountDetailTypeList" listKey="id" listValue="description" headerKey="" onchange="load_COA_Entities(this)" headerValue="---Choose---"/> 
				<td class="bluebox"><s:text name="bill.subtype"/><span class="mandatory">*</span></td>
				<td  class="bluebox" ><s:select name="commonBean.billSubType" list="dropdownData.billSubTypeList"     listKey="id" listValue="name" headerKey="" headerValue="----Choose----"   onchange="loadCheckList(this)"/></td>
				</tr>
				<tr>
			
				<td valign="top" class="greybox">
				<div  id="entitycode">Entity Code:</div> </td>
							<td class="greybox" ><span class="greybox"> <input type="text" name="detailCode"  id="detailCode"  autocomplete='off' onblur="splitEntities(this)">
							</span>
							<span id="genricimage"><img src="../images/plus1.gif"   onclick="openSearchWindow(this, 'subledger');" ></span>&nbsp;&nbsp;
							</td>
							<td colspan="2" class="greybox"><input type="text"  id="detailName" name="detailName" size="45"  />
							<input type="hidden" id="detailKey" name="detailKey"/>
						</td>
												
					</tr>
					<tr>
			<td class="bluebox"><s:text name="party.bill.number"/></td>
			<td class="bluebox"><s:textfield name="commonBean.partyBillNumber" id="commonBean.partyBillNumber"/></td>
			<td class="bluebox"><s:text name="party.bill.date"/></td>
			<s:date name='commonBean.partyBillDate' id="commonBean.partyBillDateId" format='dd/MM/yyyy'/>
			<td class="bluebox"><s:textfield name="commonBean.partyBillDate"  id="partyBillDate" onkeyup="DateFormat(this,this.value,event,false,'3')" value="%{commonBean.partyBillDateId}"/>
				<a href="javascript:show_calendar('cbill.partyBillDate');"	style="text-decoration: none">&nbsp;<img tabIndex="-1"
													src="${pageContext.request.contextPath}/image/calendaricon.gif"		border="0" /></A></td>
			
			</tr>
			<tr>
		<td class="blueborderfortd1" style="text-align:left"><s:text name="payto"/></td>
		<td class="blueborderfortd1" style="text-align:left;width:240" colspan="4" >
		<s:textfield name="commonBean.payto" id="commonBean.payto" size="55"/>
		</td>
		</tr>
				</tbody>
			</table>
		</div>
		</td>
		</tr>
		</table>
	</div>	
					<div class="yui-skin-sam" align="center">
						<div id="billDetailTable"></div>
					</div>
					
					<script>
				    makeVoucherDetailTable();
				    document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="90%";
					</script>
				<div class="yui-skin-sam" align="center">
					<div id="billDetailTableCredit"></div>
					</div>
				
					<script>
			   	makeVoucherDetailTableCredit();
			   	document.getElementById('billDetailTableCredit').getElementsByTagName('table')[0].width="90%";
				</script>
		
		<div class="yui-skin-sam" align="center">
		<div id="billDetailTableNet"></div>
		</div>
	
		<script>
		makeVoucherDetailTableNet();
		document.getElementById('billDetailTableNet').getElementsByTagName('table')[0].width="90%";
		</script>
<div id="codescontainer"></div>

<div>
<table width="90%" cellspacing="0" cellpadding="0" border="0" class="tablebottom" align="center">
		<tr align="center">
	
		<td  align="center" class="blueborderfortd1" style="text-align:center"><input type="button" name="Done" onclick="updateTabels()" class="buttongeneral" value="Done" align="middle"/>
		</td>
	
	</tr>
		</table>
</div>
<div>

<table width="90%" cellspacing="0" cellpadding="0" border="0"  align="center">
<tr>
	<td colspan="5">
<hr/>
	</td>
</tr>
</table>
</div>
<div id="summary">
				<div class="yui-skin-sam" align="center">
						<div id="billDetailTableFinal"></div>
					</div>
					
					<script>
				    makeVoucherDetailTableFinal();
				    document.getElementById('billDetailTableFinal').getElementsByTagName('table')[0].width="90%";
					</script>
					<div class="yui-skin-sam" align="center">
					<div id="billDetailTableCreditFinal"></div>
					</div>
				
					<script>
			   	makeVoucherDetailTableCreditFinal();
			   	document.getElementById('billDetailTableCreditFinal').getElementsByTagName('table')[0].width="90%";
				</script>
		
			

		<div class="yui-skin-sam" align="center">
		<div id="billDetailTableNetFinal"></div>
		</div>
	
		<script>
		makeVoucherDetailTableNetFinal();
		document.getElementById('billDetailTableNetFinal').getElementsByTagName('table')[0].width="90%";
		</script>

<div>
<table width="90%" cellspacing="0" cellpadding="0" border="0" align="center">
<tr>
	<td colspan="5">
	<hr>
	</td>
</tr>
</table>
</div>
	
<div>	
<table class="tablebottom" width="90%" cellspacing="0" cellpadding="0" border="0">
<tbody>
<tr>
<th colspan="5">
<div class="subheadsmallnew"><s:text name="subledger.details"/></div>
</th>
</tr>
</tbody></table>  
</div>
	<div class="yui-skin-sam" align="center">
		<div id="billDetailTableSubledger"></div>
		</div>
		<script>
		makeVoucherDetailTableSubledger();
		document.getElementById('billDetailTableSubledger').getElementsByTagName('table')[0].width="90%";
		</script>

</div>	
 </center>
 
  <s:hidden  id="scriptName" value="cbill.nextUser"/>
  <s:hidden name="commonBean.docNumber" id="docNumber" />
	 
	 
