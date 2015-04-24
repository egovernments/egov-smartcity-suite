<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />

 <SCRIPT>
  jQuery.noConflict();

</SCRIPT>
	<s:if  test="%{registration.challanDetails==null && registration.signDetails==null && registration.orderDetails==null 
	 								&& registration.rejectOrdPrepDet==null}">
	 	<div class="errorstyle"><span class="bold"><s:text name="noOrdDet"/></span></div>
	 </s:if>
 <div align="center" id="orddetdiv">
 	<table width="100%" border="0" cellspacing="0" cellpadding="0" id="orderTbl">
 		<tr>
 			<td>
 			<s:if  test="%{registration.challanDetails!=null}">
 				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="signDet" >
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ChallanNoticeSentDate"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="ChallanSentDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.challanDetails.statusdate" format="dd/MM/yyyy" />
						</td>
						<td class="bluebox"><s:text name="ChallanSentDateRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.challanDetails.remarks}"/>
							
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr> 
				</table>
			</s:if>
 			<s:if  test="%{registration.signDetails!=null}">
 				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="signDet" >
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="signatureDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="signatureDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.signDetails.statusdate" format="dd/MM/yyyy"/>
						</td>
						<td class="bluebox"><s:text name="signatureRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.signDetails.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr> 
				</table>
			</s:if>
			<s:if  test="%{registration.orderDetails!=null}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="ordPrepDet">  
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ordPrepDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="orderDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.orderDetails.statusdate" format="dd/MM/yyyy"/>
						</td>
						<td class="bluebox"><s:text name="orderRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.orderDetails.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
			</s:if>
			<s:if  test="%{registration.orderIssueDet!=null}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="ordIssDet">   
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="ordIssueDtl"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="orderIssDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.orderIssueDet.statusdate" format="dd/MM/yyyy"/>
						</td>
						<td class="bluebox"><s:text name="orderIssRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.orderIssueDet.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
			</s:if>
			<s:if  test="%{registration.rejectOrdPrepDet!=null}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rejOrdDet">
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="rejectOrdDetails"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="rejectPrepDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.rejectOrdPrepDet.statusdate" format="dd/MM/yyyy"/>
						</td>
						<td class="bluebox"><s:text name="rejectPrepRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.rejectOrdPrepDet.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
			</s:if>
			<s:if  test="%{registration.rejectOrdIssDet!=null}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" id="rejIssDet">   
					<tr>
						<td colspan="6"><div class="headingbg"><span class="bold"><s:text name="rejectOrdIssDet"/></span></div></td>
					</tr>
					<tr>
						<td class="bluebox" width="13%">&nbsp;</td>
						<td class="bluebox" width="18%"><s:text name="rejectIssDate" /> :</td>
						<td class="bluebox">
							<s:date name="registration.rejectOrdIssDet.statusdate" format="dd/MM/yyyy"/>
						</td>
						<td class="bluebox"><s:text name="rejectIssRemarks" /> :</td>
						<td class="bluebox">
							<s:property value="%{registration.rejectOrdIssDet.remarks}"/>
						</td>
						<td class="bluebox">&nbsp;</td>
					</tr>
				</table>
			</s:if>
			</td></tr>   
		</table>
 </div>
 
