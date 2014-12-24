<%@ taglib prefix="s" uri="/struts-tags"%>
<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
					<div class="headplacer">
						<s:text name='asset.details' />
					</div>
					</td>
				</tr>
				</table>
			  <table  align="center" width="100%" cellpadding="0" cellspacing="0">
				
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="asset.code" /></td>
						<td width="25%" class="whitebox2wk">  <s:property value="%{asset.code}" /> </td>
						<td width="25%" class="whiteboxwk"> <s:text name="asset.name" /></td>
						<td width="25%" class="whitebox2wk">  <s:property value="%{asset.name}" /> </td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="asset.currentcap.value" /></td>
						<td width="25%" class="greybox2wk"> 
							<s:textfield name="assetCommonBean.assetValueAsOnDate" id="assetValueAsOnDate" readonly="true"
							style="text-align:right" value="%{assetCommonBean.assetValueAsOnDate}" tabIndex="-1"/></td>
						<td width="25%" class="greyboxwk"> </td>
						<td width="25%" class="greybox2wk">  </td>
						
					</tr>
			  </table><br>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
					<div class="headplacer">
						<s:text name='asset.reval.detail' />
					</div>
					</td>
				</tr>
			</table>
			
			<table align="center" width="100%" cellpadding="0" cellspacing="0">	
				
				<tr>
					<td width="25%" class="whiteboxwk"> <s:text name="asset.reval.typeOfChange" /></td>
					<td width="25%" class="whitebox2wk"> 
						<s:select id="typeOfChange" name="assetRevalBean.typeOfChange" cssClass="selectwk" list="dropdownData.typOfChangeList" listKey="type" listValue="type" 
					headerKey="-1"  headerValue="%{getText('list.default.select')}" value="%{assetRevalBean.typeOfChange}"  />
						
					</td>
					<td width="25%" class="whiteboxwk"> <s:text name="asset.reval.amount" /> <span class="mandatory">*</span></td>
					<td width="25%" class="whitebox2wk"> 
					    <s:textfield name="assetRevalBean.revalAmt" id="revalAmt" maxlength="18" style="text-align:right;"  onkeyup="validateDecimal(this);" onblur="validateDigitsAndDecimal(this);amtAfterReval();"/>
					</td>
					
				</tr>
			<tr>
				<td width="25%" class="greyboxwk"><s:text name="asset.reval.date" /></td>
				<td width="25%" class="greybox2wk">
				<s:date name="assetRevalBean.revalDate" id="revalDateId" format="dd/MM/yyyy" />
				<s:textfield name="assetRevalBean.revalDate" id="revalDate" value="%{revalDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="onRevalDateChange('%{asset.id}');" size="10"/>
				<a href="javascript:show_calendar('assetRevalActForm.revalDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
					<td width="25%" class="greyboxwk"> <s:text name="asset.reval.user" /></td>
					<td width="25%" class="greybox2wk"> 
					    <s:textfield name="assetRevalBean.revaluatedBy" id="revaluatedBy" readOnly="true"/>
					</td>
					
				</tr>
				<tr>
					<td width="25%" class="whiteboxwk"> <s:text name="asset.reval.reason" /></td>
					<td width="25%" class="whitebox2wk"> 
					   <s:textarea name="assetRevalBean.description" id="description" cols="45" rows="3" />
					  
					</td>
					<td width="25%" class="whiteboxwk"> <s:text name="asset.reval.currrevalamt" /></td>
					<td width="25%" class="whitebox2wk"> 
					    <s:textfield  id="valAfterReval" name ="assetRevalBean.valAfterReval"maxlength="18" readOnly="true" style="text-align:right;" tabIndex="-1"/>
					</td>
				</tr>

			</table>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
					<div class="headplacer">
						<s:text name='asset.account.detail' />
					</div>
					</td>
				</tr>
			</table>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">	
				<tr>
					<td width="25%" class="greyboxwk"> <s:text name="asset.acc.code" /></td>
					<td width="25%" class="greybox2wk"> 
						<font  style='font-weight:bold'> 
     							 <s:property value="%{asset.assetCategory.assetCode.glcode}" />
   						</font>
					   
					</td>
					<td width="25%" class="greyboxwk"> <s:text name="asset.reval.acc.code" /></td>
					<td width="25%" class="greybox2wk"> 
						<font  style='font-weight:bold'> 
     							 <s:property value="%{asset.assetCategory.revCode.glcode}" />
   						</font>
					   
					</td>
				</tr>
				<tr>
					<td width="25%" class="whiteboxwk"><s:text name="asset.fixed.wrtnAccCode"/></td>
					<td width="25%" class="whitebox2wk"> 
						<s:select id="wrtnOffAccCode" name="assetRevalBean.wrtnOffAccCodeId" cssClass="selectwk" list="dropdownData.writtenoffacccodelist" listKey="id" listValue="glcode" 
					headerKey="-1"  headerValue="%{getText('list.default.select')}" value="%{assetRevalBean.wrtnOffAccCodeId}"  />
					   
					</td>
					<td width="25%" class="whiteboxwk"><s:text name="voucher.function"/></td>
					<td width="25%" class="whitebox2wk"> 
						<s:select id="functionId" name="function" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name" 
					headerKey="-1"  headerValue="%{getText('list.default.select')}" value="%{function.id}"  />
					   
					</td>
					</tr>
				
				<jsp:include page="voucherParam.jsp" />
			</table><br>
			