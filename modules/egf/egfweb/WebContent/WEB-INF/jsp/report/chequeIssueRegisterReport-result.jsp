<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>

<s:if test="%{chequeIssueRegisterList.size()>0}">
<table width="99%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="bluebox"> 
	<table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tablebottom">
<tr>
<td>
<div class="subheadsmallnew"><span class="subheadnew"><s:property value="ulbName"/></span></div>
</td>
</tr>
	<tr>
        <th>
			<div class="reportheader">
			Cheque issue register report for <b><s:property value="formattedBankName"/></b> with <br/>account no:<b><s:property value="accountNumber.accountnumber"/></b> from <b><s:property value="fromDate"/></b> to <b><s:property value="toDate"/></b>			
			</div>
		</th>
      </tr>
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
              
              <tr>
                <th class="bluebgheadtd" width="2%" >Sl No</th>
                <th class="bluebgheadtd" width="2%" >Cheque Number</th>
                <th class="bluebgheadtd" width="8%" >Cheque Date</th>
                <th class="bluebgheadtd" width="10%" >Name of the Payee</th>
                <th class="bluebgheadtd" width="10%" >Cheque Amount(Rs)</th>
                <th class="bluebgheadtd" width="10%" >Nature of Payment</th>
				<th class="bluebgheadtd" width="10%" >Cheque Status</th>
				<th class="bluebgheadtd" width="15%" >Payment Order No. &amp; Date</th>
				<th class="bluebgheadtd" width="15%" >Bank Payment Voucher No. &amp; Date</th>
			  </tr>
		<s:iterator value="chequeIssueRegisterList" status="stat">
		<tr>
			<td class="blueborderfortd"><div align="center"><s:property value="#stat.index+1"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="chequeNumber"/>&nbsp;</div></td>
			<td class="blueborderfortd"><s:property value="chequeDate"/>&nbsp;</td>
			<td class="blueborderfortd"><div align="center"><s:property value="payTo"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="right"><s:text name="format.number"><s:param name="value" value="chequeAmount"/></s:text>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="voucherName"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="chequeStatus"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="billNumberAndDate"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="voucherNumberAndDate"/>&nbsp;</div></td>
		</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	  <tr>
		<td>
		<div class="excelpdf">
		<a href='${pageContext.request.contextPath}/report/chequeIssueRegisterReport!generateXls.action?fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&accountNumber.id=<s:property value="accountNumber.id"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&functionary.id=<s:property value="functionary.id"/>&fund.id=<s:property value="fund.id"/>&field.id=<s:property value="field.id"/>&bank=<s:property value="bank"/>'>Excel</a> <img align="absmiddle" src="${pageContext.request.contextPath}/images/excel.png"> | 
		<a href='${pageContext.request.contextPath}/report/chequeIssueRegisterReport!generatePdf.action?fromDate=<s:property value="fromDate"/>&toDate=<s:property value="toDate"/>&accountNumber.id=<s:property value="accountNumber.id"/>&department.id=<s:property value="department.id"/>&function.id=<s:property value="function.id"/>&functionary.id=<s:property value="functionary.id"/>&fund.id=<s:property value="fund.id"/>&field.id=<s:property value="field.id"/>&bank=<s:property value="bank"/>'>PDF</a> <img align="absmiddle" src="${pageContext.request.contextPath}/images/pdf.png"></div>		
		</td>
	  </tr>
	  
	</table></td>
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
