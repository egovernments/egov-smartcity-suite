<div class="errorstyle" id="contractcert_error" style="display: none;"></div>
<s:if test="%{model.id==null}">
<div id="contractCertTable">
</div>
</s:if>
<s:else>
<div id="contractCertTable">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td colspan="8" align="center" class="headingwk" style="border-left-width: 0px">
      		<s:text name="bill.nameofWork" /> : <s:property value="%{contractCertInfo.workName}"/>
    </td>
</tr>
<tr>
	<td class="whiteboxwk"><s:text name="bill.estNo" /> : </td>
    <td class="whitebox2wk"><s:property value="%{contractCertInfo.estimateNo}"/></td>
   
</tr>
<tr>
<td class="whiteboxwk"><s:text name="bill.conName" /> : </td>        
<td class="whitebox2wk"><s:property value="%{contractCertInfo.contractorName}"/></td>

</tr>

<tr>
<td colspan="4">
 <table cellspacing="0" cellpadding="4" border="1" width="100%">
    <col width="10"></col>
    <col width="65"></col>
    <col width="40"></col>
    <col width="45"></col>
    <col width="45"></col>
    <col width="43"></col>
    <col width="50"></col>
    <col width="102"></col>
    <tbody>
      <tr valign="top">
        <th width="10" rowspan="2"><p align="center">SL No</p> </th>
       <th width="265" rowspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="bill.descwork" /></p> </th>
        <th width="46" rowspan="2"  style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="contractcert.executionrate" /></p></th>
        <th width="100" colspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="contractcert.sincelastbill" /></p></th>
        <th width="100" colspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="contractcert.uptodate" /></p></th>
        <th width="102" rowspan="2" style="WORD-BREAK:BREAK-ALL"><p align="center"><s:text name="mbpdf.remarks" /></p></th>
   	</tr>
      <tr valign="top">
       <th width="43"><p align="center"><s:text name="bill.qty" /></p></th>
        <th width="45"><p align="center"><s:text name="bill.amount" /></p></th>
      	<th width="43"><p align="center"><s:text name="bill.qty" /></p></th>
        <th width="45"><p align="center"><s:text name="bill.amount" /></p></th>
      </tr>
      <s:iterator var="comList" value="contractCertDetailInfo" status="s"> 
      <tr valign="top">
        <td width="10"><p align="center"><s:property value='%{#s.count}' /></p></td>
        <td width="65" style="WORD-BREAK:BREAK-ALL"><p align="left"><s:if test="%{workOrderActivity.activity.schedule!=null}">
        <s:property value="%{workOrderActivity.activity.schedule.description}"/>
        </s:if>
        <s:else>
        <s:property value="%{workOrderActivity.activity.nonSor.description}"/>
        </s:else></p></td>
       <td width="46" style="WORD-BREAK:BREAK-ALL"><p align="right"><s:property value="%{executionRate}"/></p></td>
       
        <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right"><s:property value="%{lastExecutionQuantity}"/></p></td>
       <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right">
        <s:text name="contractor.format.number" >
            <s:param name="value" value="%{lastExecutionAmount}"/> 
         </s:text>
        </p></td>
        <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right"><s:property value="%{executionQuantity}"/></p></td>
         <td width="50" style="WORD-BREAK:BREAK-ALL"><p align="right">
        <s:text name="contractor.format.number" >
            <s:param name="value" value="%{executionAmount}"/> 
         </s:text>
        </p></td>
       
      
        
       <td width="10%" >
       <textarea name="contractCertRemarks" id="contractCertRemarks" rows="5" cols="15" class="selectwk" maxlength="255" onkeyup="return ismaxlength(this)" ><s:property value="%{workOrderActivity.remarks}" /> </textarea>
       
      </td>
      </tr>
     </s:iterator>
    </tbody>
  </table> 
  </td>
</tr>
</table>
</div>
</s:else>
