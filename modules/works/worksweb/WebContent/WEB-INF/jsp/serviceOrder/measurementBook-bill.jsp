<script type="text/javascript" src="/EGF/javascript/voucherHelper.js"></script>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



<jsp:include page="billDetailTable.jsp"/>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
            <tr>
            	<td width="25%" class="greyboxwk"> Fund : </td>
            	<td width="25%" class="greybox2wk"> <s:property value="%{getFundName()}" /> </td>
            	<td width="25%" class="greyboxwk"> Department : </td>
            	<td width="25%" class="greybox2wk"><s:property value="%{getDeptName()}" /> </td>
            </tr>
     			<tr>
				<td width="25%" class="whiteboxwk">Bill Date: <span class="mandatory">*</span> </td>
				<td width="25%" class="whitebox2wk">
				<s:date name="cbill.billdate" id="billdateId" format="dd/MM/yyyy" />
				<s:textfield name="cbill.billdate" id="billdate" value="%{measurementDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" size="10" onblur="loadMBDate(this);"/>
				<a href="javascript:show_calendar('mBookBillForm.billdate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
				<td width="25%" class="whiteboxwk">Bill SubType: <span class="mandatory">*</span> </td>
				<td width="25%" class="whitebox2wk">
				<s:select name="cbill.egBillregistermis.egBillSubType.id" id="egBillSubTypeId" list="dropdownData.billSubTypeList"   listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----"   /></td>
				</tr>
				<tr>
					<td class="greyboxwk" width="25%"  ><s:text name="so.comments"></s:text></td>
					<td class="greybox2wk" width="25%"  ><s:textarea name="cbill.narration" id="narration" cols="40" rows="2"  onblur="checkLength(this)" ></s:textarea></td><td class="greyboxwk"></td><td class="greybox2wk"></td>
				</tr>
			</table>
			<s:hidden name="cbill.egBillregistermis.payto"></s:hidden>
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
		<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										Approver Details
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
	<s:if test='%{! "END".equalsIgnoreCase(nextLevel)}'>
		<%@include file="workFlow.jsp"%>
</s:if>
<script>
 



 var allsuspects=document.getElementsByTagName("link");
 for (var i=allsuspects.length; i>=0; i--){ 
  if (allsuspects[i] && allsuspects[i].getAttribute("href")!=null && (allsuspects[i].getAttribute("href").indexOf("autocomplete.css")!=-1 ||
	allsuspects[i].getAttribute("href").indexOf("skin.css")!=-1 || allsuspects[i].getAttribute("href").indexOf("menu.css")!=-1))
   allsuspects[i].parentNode.removeChild(allsuspects[i]) 
 }
populateslDropDown();

function loadMBDate(dateObj){
	if(dom.get('measurementDate').value == ''){
		
		dom.get('measurementDate').value = dateObj.value
	}
	
}
</script>