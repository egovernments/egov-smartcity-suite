<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

    
	 <th class="bluebgheadtdnew">Sl No</th>  
    <th class="bluebgheadtdnew">Bill Number</th>
    <th class="bluebgheadtdnew">Bill Date</th> 
    <th class="bluebgheadtdnew">Bill Voucher Number</th>
    <th class="bluebgheadtdnew">Bill Voucher Date</th>  
    <th class="bluebgheadtdnew">Payee Name</th>
    <th class="bluebgheadtdnew">Net Amount</th>
    <th class="bluebgheadtdnew">Earlier Payment</th> 
    <th class="bluebgheadtdnew">Payable Amount</th> 
    <s:if test="%{!isFieldMandatory('fund')}"><th class="bluebgheadtdnew">Fund</th></s:if>
    <s:if test="%{shouldShowHeaderField('department')}"><th class="bluebgheadtdnew">Department</th></s:if>
    <s:if test="%{shouldShowHeaderField('functionary')}"><th class="bluebgheadtdnew">Functionary</th></s:if>
    <s:if test="%{shouldShowHeaderField('fundsource')}"><th class="bluebgheadtdnew">Fund Source</th></s:if>
    <s:if test="%{shouldShowHeaderField('scheme')}"><th class="bluebgheadtdnew">Scheme</th></s:if>
    <s:if test="%{shouldShowHeaderField('subscheme')}"><th class="bluebgheadtdnew">Sub scheme</th></s:if>
    <s:if test="%{shouldShowHeaderField('field')}"><th class="bluebgheadtdnew">Field</th></s:if>

 