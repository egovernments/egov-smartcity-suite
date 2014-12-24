<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
  	  <td align="left"  class="blueborderfortdnew"><s:property value="%{billnumber}" /></td>
      <td class="blueborderfortdnew"><s:date name="%{billdate}" format="dd/MM/yyyy"/></td>
      <td align="left"  class="blueborderfortdnew"><s:property value="%{egBillregistermis.payto}"/></td>
      <td style="text-align:right"  class="blueborderfortdnew"><s:property value="%{passedamount - deductionAmtMap.get(id)}" /></td>
      <td style="text-align:right"  class="blueborderfortdnew"><s:property value="%{paidAmtMap.get(id)}" /></td>
      <td style="text-align:right"  class="blueborderfortdnew"><s:property value="%{passedamount-paidAmtMap.get(id)-deductionAmtMap.get(id)}" /></td>
