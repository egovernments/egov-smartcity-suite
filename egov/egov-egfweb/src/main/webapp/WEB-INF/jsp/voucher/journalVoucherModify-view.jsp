#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
	
		<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
		<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
		<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Journal Voucher -Modify</title>
<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:9049;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	.yui-skin-sam tr.yui-dt-odd{background-color:#FFF;}
</style>
</head>

	<script>
		path="${pageContext.request.contextPath}";
		var totaldbamt=0,totalcramt=0;
		
		
		
		var makeVoucherDetailTable = function() {
			<s:if test='%{isRestrictedtoOneFunctionCenter == true}'>      
		var voucherDetailColumns = [ 
			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
			{key:"function",hidden:true,label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","hidden")},
			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
		</s:if>
		<s:else>
		var voucherDetailColumns = [ 
     			{key:"functionid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".functionIdDetail","hidden")},
     			{key:"function",label:'Function Name',width:90, formatter:createTextFieldFormatterForFunctionJV(VOUCHERDETAILLIST,".functionDetail","text")}, 
     			{key:"glcodeid",hidden:true,width:90, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeIdDetail","hidden")},
     			{key:"glcode",label:'Account Code <span class="mandatory">*</span>',width:100, formatter:createTextFieldFormatterJV(VOUCHERDETAILLIST,".glcodeDetail","text")},
     			{key:"accounthead", label:'Account Head',width:250,formatter:createLongTextFieldFormatterJV(VOUCHERDETAILLIST,".accounthead")},				
     			{key:"debitamount",label:'Debit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".debitAmountDetail","updateDebitAmountJV()")}, 
     			{key:"creditamount",label:'Credit Amount',width:90, formatter:createAmountFieldFormatterJV(VOUCHERDETAILLIST,".creditAmountDetail","updateCreditAmountJV()")},
     			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
     			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
     		];
		</s:else>    
	    var voucherDetailDS = new YAHOO.util.DataSource(); 
		billDetailsTable = new YAHOO.widget.DataTable("billDetailTable",voucherDetailColumns, voucherDetailDS);
		billDetailsTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1});
				updateAccountTableIndex();
			}
			if (column.key == 'Delete') { 	
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
					updateDebitAmountJV();updateCreditAmountJV();
					check();
				}
				else{
					alert("This row can not be deleted");
				}
			}
			
			        
		});
		<s:iterator value="billDetailslist" status="stat">
				billDetailsTable.addRow({SlNo:billDetailsTable.getRecordSet().getLength()+1,
					"functionid":'<s:property value="functionIdDetail"/>',
					"function":'<s:property value="functionDetail"/>',
					"glcodeid":'<s:property value="glcodeIdDetail"/>',
					"glcode":'<s:property value="glcodeDetail"/>',
					"accounthead":'<s:property value="accounthead"/>',
					"debitamount":'<s:text name="format.number" ><s:param value="%{debitAmountDetail}"/></s:text>',
					"creditamount":'<s:text name="format.number" ><s:param value="%{creditAmountDetail}"/></s:text>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateGridPJV('functionIdDetail',index,'<s:property value="functionIdDetail"/>');
				updateGridPJV('functionDetail',index,'<s:property value="functionDetail"/>');
				updateGridPJV('glcodeIdDetail',index,'<s:property value="glcodeIdDetail"/>');
				updateGridPJV('glcodeDetail',index,'<s:property value="glcodeDetail"/>');
				updateGridPJV('accounthead',index,'<s:property value="accounthead"/>');
				updateGridPJV('debitAmountDetail',index,'<s:text name="format.number" ><s:param value="%{debitAmountDetail}"/></s:text>');
				updateGridPJV('creditAmountDetail',index,'<s:text name="format.number" ><s:param value="%{creditAmountDetail}"/></s:text>');
				totaldbamt = totaldbamt+parseFloat('<s:text name="format.number" ><s:param value="%{debitAmountDetail}"/></s:text>');
				totalcramt = totalcramt+parseFloat('<s:text name="format.number" ><s:param value="%{creditAmountDetail}"/></s:text>');
				updateAccountTableIndex();
			</s:iterator>
				
	
		var tfoot = billDetailsTable.getTbodyEl().parentNode.createTFoot();
		var tr = tfoot.insertRow(-1);
		var th = tr.appendChild(document.createElement('th'));
		th.colSpan = 5;
		th.innerHTML = 'Total&nbsp;&nbsp;&nbsp;';
		th.align='right';
		var td = tr.insertCell(-1);
		td.width="90"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totaldbamount' name='totaldbamount' readonly='true' tabindex='-1'/>";
		var td = tr.insertCell(-1);
		td.width="90"
		td.align="right"
		td.innerHTML="<input type='text' style='text-align:right;width:100px;'  id='totalcramount' name='totalcramount' readonly='true' tabindex='-1'/>";
		document.getElementById('totaldbamount').value=totaldbamt.toFixed(2);
		document.getElementById('totalcramount').value=totalcramt.toFixed(2);
	}
	
	var glcodeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.glcodeList">
	    glcodeOptions.push({label:'<s:property value="glcode"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	var detailtypeOptions=[{label:"--- Select ---", value:"0"}];
	<s:iterator value="dropdownData.detailTypeList">
	    detailtypeOptions.push({label:'<s:property value="name"/>', value:'<s:property value="id"/>'})
	</s:iterator>
	
	var makeSubLedgerTable = function() {
		var subledgerColumns = [ 
			{key:"glcode",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".subledgerCode","hidden")},
			{key:"glcode.id",label:'Account Code <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV(SUBLEDGERLIST,"loaddropdown(this)"),  dropdownOptions:glcodeOptions},
			{key:"detailTypeName",hidden:true,width:90, formatter:createSLTextFieldFormatterJV(SUBLEDGERLIST,".detailTypeName","hidden")},
			{key:"detailType.id",label:'Type <span class="mandatory">*</span>',width:90, formatter:createDropdownFormatterJV1(SUBLEDGERLIST),dropdownOptions:detailtypeOptions},
			{key:"detailCode",label:'Code <span class="mandatory">*</span>',width:120, formatter:createSLDetailCodeTextFieldFormatterJV(SUBLEDGERLIST,".detailCode","splitEntitiesDetailCode(this)", ".search", "openSearchWindowFromJV(this)")},
			{key:"detailKeyId",hidden:true,width:100, formatter:createSLHiddenFieldFormatterJV(SUBLEDGERLIST,".detailKeyId")},
			{key:"detailKey",label:'Name',width:180, formatter:createSLLongTextFieldFormatterJV(SUBLEDGERLIST,".detailKey","")},
			{key:"amount",label:'Amount',width:90, formatter:createSLAmountFieldFormatterJV(SUBLEDGERLIST,".amount")},
			{key:'Add',label:'Add',formatter:createAddImageFormatter("${pageContext.request.contextPath}")},
			{key:'Delete',label:'Delete',formatter:createDeleteImageFormatter("${pageContext.request.contextPath}")}
		];
	    var subledgerDS = new YAHOO.util.DataSource(); 
		subLedgersTable = new YAHOO.widget.DataTable("subLedgerTable",subledgerColumns, subledgerDS);
		subLedgersTable.on('cellClickEvent',function (oArgs) {
			var target = oArgs.target;
			var record = this.getRecord(target);
			var column = this.getColumn(target);
			if (column.key == 'Add') { 
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1});
				updateSLTableIndex();
				check();
			}
			if (column.key == 'Delete') { 			
				if(this.getRecordSet().getLength()>1){			
					this.deleteRow(record);
					allRecords=this.getRecordSet();
					for(var i=0;i<allRecords.getLength();i++){
						this.updateCell(this.getRecord(i),this.getColumn('SlNo'),""+(i+1));
					}
				}
				else{
					alert("This row can not be deleted");
				}
			}        
		});
		<s:iterator value="subLedgerlist" status="stat">
				subLedgersTable.addRow({SlNo:subLedgersTable.getRecordSet().getLength()+1,
					"glcode":'<s:property value="subledgerCode"/>',
					"glcode.id":'<s:property value="glcode.id"/>',
					"detailType.id":'<s:property value="detailType.id"/>',
					"detailTypeName":'<s:property value="detailTypeName"/>',
					"detailCode":'<s:property value="detailCode"/>',
					"detailKeyId":'<s:property value="detailKeyId"/>',
					"detailKey":'<s:property value="detailKeyEscSpecChar"/>',
					"debitAmount":'<s:text name="format.number" ><s:param value="%{debitAmount}"/></s:text>',
					"creditAmount":'<s:text name="format.number" ><s:param value="%{creditAmount}"/></s:text>'
				});
				var index = '<s:property value="#stat.index"/>';
				updateSLGridPJV('subledgerCode',index,'<s:property value="subledgerCode"/>');
				updateGridSLDropdownJV('glcode.id',index,'<s:property value="glcode.id"/>','<s:property value="subledgerCode"/>');
				updateGridSLDropdownJV('detailType.id',index,'<s:property value="detailType.id"/>','<s:property value="detailTypeName"/>');
				updateSLGridPJV('detailCode',index,'<s:property value="detailCode"/>');
				updateSLGridPJV('detailKeyId',index,'<s:property value="detailKeyId"/>');
				updateSLGridPJV('detailKey',index,'<s:property value="detailKeyEscSpecChar"/>');
				updateSLGridPJV('amount',index,'<s:text name="format.number" ><s:param value="%{amount}"/></s:text>');
				updateSLTableIndex();
			</s:iterator>
		
	}
	
	</script>
<body onload="onLoadTask()">

<s:form action="journalVoucherModify" theme="simple" name="jvmodifyform" >
<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Journal voucher -Modify" />
			</jsp:include>
			
			<span class="mandatory">
				<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Journal Voucher</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ;font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>
<input type="hidden" name="selectedDate" id="selectedDate">

	<table border="0" width="100%">
	<tr>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="bluebox" width="22%"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox" width="22%">
			<table width="100%">
			<tr>
			<td style="width:25%"><input type="text" name="voucherNumberPrefix" id="voucherNumberPrefix" readonly="true"  style="width:100%"/></td> 
			<td style="width:75%"><s:textfield name="voucherNumber" id="voucherNumber" /></td>
			</tr>
			</table>
			</td>
		</s:if>
		<s:else>
			<td class="bluebox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:textfield name="voucherNumber" id="voucherNumber" readonly="true" /></td>
		</s:else>
		<td class="bluebox"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="bluebox"><s:date name="voucherDate" id="voucherDateId" format="dd/MM/yyyy"/>
			<s:textfield  name="voucherDate" id="voucherDate" value="%{voucherDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('jvmodifyform.voucherDate');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
		</td>
		<jsp:include page="voucherSubType.jsp"/>
		<jsp:include page="vouchertrans-filter.jsp"/>
	
	<tr>
			<td class="greybox"><s:text name="voucher.narration" /></td>
			<td class="greybox" colspan="3"><s:textarea  id="narration" name="description" style="width:580px" onblur="checkVoucherNarrationLen(this)"/></td>
		</tr>	
	</table>
	</div>
	<br/>
	<div id="labelAD" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Account Details</th></table>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
     </div>
     <script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
	 <div id="codescontainer"></div>
	 <br/>
	 	<div id="labelSL" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Sub-Ledger Details</th></table>
	 	</div>
	 	
		<div class="yui-skin-sam" align="center">
	       <div id="subLedgerTable"></div>
	     </div>
		<script>
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>
		
		<br/>
		<div class="subheadsmallnew"/></div>
		<div class="mandatory" align="left">* Mandatory Fields</div>
		
		<div class="buttonbottom" style="padding-bottom:10px;" align="center">
		<table border="0" width="100%"><tr></tr>
			<tr align="center">
				<td/>
					<input type="button" value="Close" onclick="javascript:window.close()" class="button" />
				<td>
				
			</tr>
		</table>
	</div>
	<br/>
</div>
</div>
<div id="codescontainer"></div>

</s:push>
</s:form>

<script>               

	function onLoadTask()
	{
		document.getElementById('vType').value='<s:property value="voucherTypeBean.voucherSubType"/>';
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
		{
		
			for(var i=0;i<document.forms[0].length;i++)
			{
				if(document.forms[0].elements[i].value != 'Close'){
					document.forms[frmIndex].elements[i].disabled =true;
					}					
			}		
		}
	}

</script>
</body>

</html>
