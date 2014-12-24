<%int i=1;%>
<%int rcptDtlCnt=0; %>
<s:iterator value="%{modelPayeeList}" status="rowpayeestatus">
 <s:iterator value="%{receiptHeaders}" status="receiptheaderrowstatus"> <!--  iterate through receipt headers -->
	 <tr>
    	<td width="100%" class="greyboxwithlink" >
    		<table width="100%" cellpadding="0" cellspacing="0" border="0" align="left">
		<tr>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.billnumber"/></span></td>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.additionalinfo"/></span></td>
			<td width="20%"><span class="boldblue"><s:text name="billreceipt.consumerNo"/></span></td>
			<td width="30%"><span class="boldblue"><s:text name="billreceipt.payeename.description"/></span></td>
			<td width="10%">&nbsp;</td>
		</tr>
		<tr>
			<td class="textholder"><s:property value="%{referencenumber}"/></td>
			<td class="textholder"><s:property value="%{displayMsg}"/></td>
			<td class="textholder"><s:property value="%{consumerCode}"/></td>
			<td class="textholderl"><s:property value="%{payeename}"/>,<s:property value="%{referenceDesc}"/></td>
			<td class="textholderl"><div id="bobcontent<s:property value="#receiptheaderrowstatus.index + 1" />-title" class="billdetailaction"><s:text name="billreceipt.accountdetails"/></div></td>	
		</tr>
		</table>
        </td>
 	</tr>

 	<tr>
	  <td>
	    <div class="switchgroup1" id="bobcontent<s:property value="#receiptheaderrowstatus.index + 1" />">
		      <table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		        <tr>
		          <td class="blueborderfortd"><div class="billscroller">
		            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom" id="accountdetailtable<%=i%>" name="accountdetailtable<%=i%>" >
		              <tr>
		                <th class="bluebgheadtd" width="14%" ><s:text name="billreceipt.accountdetails.accountcode"/></th>
		                <th class="bluebgheadtd" width="38%" ><s:text name="billreceipt.accountdetails.name"/></th>
		                <th class="bluebgheadtd" width="37%" ><s:text name="billreceipt.accountdetails.description"/></th>
		                <th class="bluebgheadtd" width="11%" ><s:text name="billreceipt.payement.amounttobecollected"/></th>
		                <!--th class="bluebgheadtd" width="11%" ><s:text name="billreceipt.payement.actualamountcollected"/></th-->
			      </tr>
		                <s:iterator value="%{receiptDetails}" status="rowreceiptdetailstatus">
			              	<tr>

				                <td class="blueborderfortd"><s:property  value="%{accounthead.glcode}" />
				                </td>
				                <td class="blueborderfortd"><s:property  value="%{accounthead.name}" /></td>
				                <td class="blueborderfortd"><s:property  value="%{description}" /></td>

				                <td class="blueborderfortd">
				                	<div align="center">
				                	<input id="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" name="receiptDetailList[<%=rcptDtlCnt%>].cramountToBePaid" value='<s:property value="%{cramountToBePaid}"/>' type="text" readonly="true" disabled="disabled" size="12"/></div></td>
								<td class="blueborderfortd">
									<div align="center">
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].cramount" value="0" type="text" id="receiptDetailList[<%=rcptDtlCnt%>].cramount"  size="12" onblur='checkandcalculatecredittotal(<%=rcptDtlCnt%>,this);'/>
									<!-- the below two fields are included to be matched in the action -->
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" id="receiptDetailList[<%=rcptDtlCnt%>].ordernumber" value='<s:property value="ordernumber"/>' />
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" id="receiptDetailList[<%=rcptDtlCnt%>].receiptHeader.referencenumber" value='<s:property value="referencenumber"/>'/>
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].dramount" id="receiptDetailList[<%=rcptDtlCnt%>].dramount" />
									<input type="hidden" name="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" id="receiptDetailList[<%=rcptDtlCnt%>].isActualDemand" value='<s:property value="isActualDemand"/>' />	
								</div>
								</td>
			              	</tr>
			              	<%rcptDtlCnt=rcptDtlCnt+1;%>
		                </s:iterator> <!--  Finished iterating through the account heads (receipt detail) -->
		            </table> <!-- End of accountdetailtable i -->
	            </div>
	           </td>
	          </tr>
	          <%i=i+1;%>
	        </table> <!-- End of table enclosing all account detail tables -->
	      </div></td>
	  </tr>
  </s:iterator><!-- end of iterating through receipt headers -->
</s:iterator> <!--  iterating through receipt payee list(model) end -->
