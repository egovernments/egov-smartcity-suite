<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<s:if test="%{billRegisterList.size()>0}">
<br/>
<table width="99%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="blueborderfortd">
		<div>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablebottom">
              <tr>
            	<td colspan="7">
				<div class="subheadsmallnew"><strong>List of Salary Bills</strong></div></td>
          	</tr>
              <tr>
                <th class="bluebgheadtd" width="2%" >Sl No</th>
                <th class="bluebgheadtd" width="8%" >Bill/File Number</th>
                <th class="bluebgheadtd" width="10%" >Bill Date</th>
                <th class="bluebgheadtd" width="10%" >Department</th>
                <th class="bluebgheadtd" width="10%" >Month</th>
				<th class="bluebgheadtd" width="10%" >Net Pay</th>
				<th class="bluebgheadtd" width="10%" >Bill Status</th>
			  </tr>
		<s:iterator value="billRegisterList" status="stat" var="p">
		<tr>
			<td class="blueborderfortd"><s:property value="#stat.index+1"/>&nbsp;</td>
			<td class="blueborderfortd"><div align="center"><a href='../bill/salaryBillRegister!view.action?billregisterId=<s:property value="id"/>'><s:property value="billnumber"/>&nbsp;</a></div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="billdate"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="egBillregistermis.egDepartment.deptName"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="egBillregistermis.month"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="billamount"/>&nbsp;</div></td>
			<td class="blueborderfortd"><div align="center"><s:property value="billstatus"/>&nbsp;</div></td>
		</tr>
		</s:iterator>
			</table>
        </div></td>
      </tr>
	</table></td>
	</tr>
	<tr>
		<input name="button" type="button" class="buttonsubmit" id="non-printable" value="Print" onclick="window.print()"/>&nbsp;&nbsp;
	</tr>
</table>
</s:if>
<s:else>No data found</s:else>
