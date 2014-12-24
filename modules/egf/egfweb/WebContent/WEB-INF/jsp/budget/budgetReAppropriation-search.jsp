<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>  
<head>  
    <title><s:text name="budgetReAppropriation.search"/></title>
</head>
	<body>  
		<s:form action="budgetReAppropriation" theme="simple" >
			<jsp:include page="budgetHeader.jsp"/>
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="budgetReAppropriation.search"/></div>
			<table align="center" width="80%" cellpadding="0" cellspacing="0">
				<jsp:include page="budgetReAppropriation-filter.jsp"/>
				<tr>
					<td class="greybox">&nbsp;</td>
					<td  class="greybox"><s:text name="budgetdetail.budgetGroup"/></td>
					<td  class="greybox"><s:select list="dropdownData.budgetGroupList"  listKey="id" listValue="name" name="budgetDetail.budgetGroup.id" headerKey="0" headerValue="--- Select ---" value="budgetGroup.id" id="budgetReAppropriation_budgetGroup"></s:select></td>
					
					<td  class="greybox"><s:text name="budget.reappropriation.type"/></td>
					<td  class="greybox"><s:select list="#{'B':'Both','A':'Addition','R':'Reduction'}"  name="type" id="type"></s:select></td>
				</tr>
				<tr/>
				<tr><td>&nbsp;</td></tr>
			</table>
			<div class="buttonbottom" style="padding-bottom:10px;position:relative">
			<s:submit method="search" value="Search" cssClass="buttonsubmit" onclick="return checkMandatory()"/>
			<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			<br/>
			<div id="listid" style="display:none">
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		        <tr>  
		            <th class="bluebgheadtd">Sl.No.</th>  
		            <s:if test="%{shouldShowHeaderField('fund')|| shouldShowGridField('fund')}"><th class="bluebgheadtd"><s:text name="fund"/></th></s:if>
		            <s:if test="%{shouldShowHeaderField('executingDepartment')|| shouldShowGridField('executingDepartment')}"><th class="bluebgheadtd">Executing Department</th></s:if>
		            <s:if test="%{shouldShowField('function')|| shouldShowGridField('function')}"><th class="bluebgheadtd"><s:text name="function"/></th></s:if>
		            <s:if test="%{shouldShowHeaderField('functionary')|| shouldShowGridField('functionary')}"><th class="bluebgheadtd"><s:text name="functionary"/></th></s:if>
		            <s:if test="%{shouldShowHeaderField('scheme')|| shouldShowGridField('scheme')}"><th class="bluebgheadtd"><s:text name="scheme"/></th></s:if>
		            <s:if test="%{shouldShowHeaderField('subScheme')|| shouldShowGridField('subScheme')}"><th class="bluebgheadtd"><s:text name="subscheme"/></th></s:if>
		            <s:if test="%{shouldShowHeaderField('boundary')|| shouldShowGridField('boundary')}"><th class="bluebgheadtd"><s:text name="field"/></th></s:if>
		            <th class="bluebgheadtd">Seq No</th>
		            <th class="bluebgheadtd">Sanctioned Budget(Rs)</th>
		            <th class="bluebgheadtd">Addition Amount Sought(Rs)</th>
		            <th class="bluebgheadtd">Reduction Amount Sought(Rs)</th>  
		            <th class="bluebgheadtd">Cumulative Amount(Rs)</th>  
		        </tr>  
		        <c:set var="trclass" value="greybox"/>
		        <c:set var="budgetdetailid" value="0"/>
				    <s:iterator var="p" value="reAppropriationList" status="s">  
				    <tr>  
				    	<c:if test='${budgetdetailid!=budgetDetail.id}'><c:set var="totalAmt" value="${budgetDetail.approvedAmount}"/></c:if>
				    	<td class="<c:out value="${trclass}"/>">  
				            <s:property value="#s.index+1" />
				        </td>
			            <s:if test="%{shouldShowHeaderField('fund')|| shouldShowGridField('fund')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.fund.name}" /></td></s:if>
			            <s:if test="%{shouldShowHeaderField('executingDepartment')|| shouldShowGridField('executingDepartment')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.executingDepartment.deptName}" /></td></s:if>
			            <s:if test="%{shouldShowField('function')|| shouldShowGridField('function')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.function.name}" /></td></s:if>
			            <s:if test="%{shouldShowHeaderField('functionary')|| shouldShowGridField('functionary')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.functionary.name}" /></td></s:if>
			            <s:if test="%{shouldShowHeaderField('scheme')|| shouldShowGridField('scheme')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.scheme.name}" /></td></s:if>
			            <s:if test="%{shouldShowHeaderField('subScheme')|| shouldShowGridField('subScheme')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.subScheme.name}" /></td></s:if>
			            <s:if test="%{shouldShowHeaderField('boundary')|| shouldShowGridField('boundary')}"><td class="<c:out value="${trclass}"/>"><s:property value="%{budgetDetail.boundary.name}" /></td></s:if>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <s:property value="%{reAppropriationMisc.sequenceNumber}" />
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">
				        	<s:text name="format.number"><s:param value="%{budgetDetail.approvedAmount}"/></s:text>  
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <s:text name="format.number"><s:param value="%{additionAmount}"/></s:text>
				        </td>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				        	<s:text name="format.number"><s:param value="%{deductionAmount}"/></s:text>
				        </td>
				       	<c:if test='${additionAmount==null || additionAmount==0 || additionAmount==0.0}'><c:set var="totalAmt" value="${totalAmt-deductionAmount}"/></c:if>
				        <c:if test='${deductionAmount==null || deductionAmount==0 || deductionAmount==0.0}'><c:set var="totalAmt" value="${totalAmt+additionAmount}"/></c:if>
				        <td style="text-align:right"  class="<c:out value="${trclass}"/>">  
				            <c:out value="${totalAmt}"/>.00
				        </td>
				        <c:choose>
					        <c:when test="${trclass=='greybox'}"><c:set var="trclass" value="bluebox"/></c:when>
					        <c:when test="${trclass=='bluebox'}"><c:set var="trclass" value="greybox"/></c:when>
				        </c:choose>
				        <c:set var="budgetdetailid" value="${budgetDetail.id}"/>
				    </tr>  
				    </s:iterator>
				    <s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>  
				</table>  
			</div>
			<br/>
			<br/>
			<div id="msgdiv" style="display:none">
				<table align="center" class="tablebottom" width="80%">
					<tr><th class="bluebgheadtd" colspan="7"><s:text name="no.data.found"/></td></tr>
				</table>
			</div>
			<br/>
			<br/>
		</s:form>  
		<script>
			<s:if test="%{reAppropriationList.size==0}">
				dom.get('msgdiv').style.display='block';
			</s:if>
			<s:if test="%{reAppropriationList.size!=0}">
				dom.get('msgdiv').style.display='none';
				dom.get('listid').style.display='block';
			</s:if>
			
			function checkMandatory()
			{
				if(document.getElementById('financialYear').value==0)
				{
					alert('Please Select Financial Year');
					return false;
				}
				if(document.getElementById('budgetReAppropriation_fund').value==0 && document.getElementById('budgetReAppropriation_executingDepartment').value==0 && document.getElementById('budgetReAppropriation_function').value==0)
				{
					alert('Please select Fund or Department or Functionary');
					return false;
				}
				return true;
			}
		</script>
	</body>  
</html>
