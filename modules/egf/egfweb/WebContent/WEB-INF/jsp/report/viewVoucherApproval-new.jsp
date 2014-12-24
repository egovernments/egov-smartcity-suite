<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@page contentType="text/html"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link href="<egov:url path='/cssnew/displaytag.css'/>" rel="stylesheet" type="text/css" />
<html>  
<head>  
    <title><s:text name="view.voucher.report" /></title>  
</head>
	<body>  
		<s:form action="viewVoucherApproval" name="viewVoucherApproval" theme="simple" >
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<font  style='color: red ; font-weight:bold '> 
			<p class="error-block" id="lblError" ></p></font>
				<div class="formmainbox">
					<div class="subheadnew">
						<s:text name="view.voucher.report" />
					</div>
				</div>
			<table align="center" width="100%" cellpadding="0" cellspacing="0">
				                                                                    
				
			<tr>
				<td  class="greybox"> <s:text name="voucher.fromdate" /><span class="mandatory">*</span></td>
				<td  class="greybox"><s:date name="fromDate" id="fromDateId" format="dd/MM/yyyy"/> 
				<s:textfield name="fromDate" id="fromDate" value="%{fromDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('viewVoucherApproval.fromDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img  src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
				</td>
				<td  class="greybox"><s:text name="voucher.todate" /><span class="mandatory">*</span></td>
				<td  class="greybox"><s:date name="toDate" id="toDateId" format="dd/MM/yyyy"/> 
				<s:textfield name="toDate" id="toDate" value="%{toDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('viewVoucherApproval.toDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)</td>
			</tr>
			
			<tr>
				<td class="bluebox" ><s:text name="voucher.type"/> <span class="mandatory">*</span></td>	
				<td class="bluebox"><s:select name="vortype" id="vortype" list="dropdownData.voucherTypeList" headerKey="" headerValue="----Choose----" onChange= "loadvoucherName(this)"/></td>
				<egov:ajaxdropdown fields="['Text','Value']"url="report/viewVoucherApproval!ajaxLoadVoucherNames.action" dropdownId="vorname" id="vortype" />
				<td class="bluebox" ><s:text name="voucher.name"/> 
				<egov:ajaxdropdown id="nametypeid"fields="['Text','Value']" dropdownId="nametypeid" url="/ViewVoucherApproval!ajaxLoadVoucherNames.action" />
				<td class="bluebox"><s:select name="vorname" id="vorname" list="dropdownData.voucherNameList" headerKey="" headerValue="----Choose----"/></td>
			</tr>
				
			 	
			</table>
			<div  class="buttonbottom">
				<s:submit method="list" value="Search"  cssClass="buttonsubmit" />	
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
			
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
		
			<s:if test="%{searchResult.fullListSize != 0}">
			<tr align="center">
				<td >
				<display:table name="searchResult" export="true"  id="searchResultid" uid="currentRowObject" cellpadding="0" cellspacing="0"
				requestURI="" sort="external"  class="its" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
									
				<display:column  title="Sl.No" style="width:3%;text-align:center">
						<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
				</display:column>			
					
				<display:column  title="Voucher Number" style="width:8%;text-align:center" property="voucherNumber" />
				<display:column  title="Voucher Type" style="width:8%;text-align:center" property="voucherType"  />
				<display:column  title="Department" style="width:8%;text-align:center" property="department" />	
				<display:column  title="Approved Date" style="width:5%;text-align:right" property="approvedDate" />	
				<display:column  title="Net Amount" style="width:5%;text-align:right" property="netAmount"   />					
				<display:column  title="Status" style="width:5%;text-align:right" property="status"/>	
					<display:caption  media="pdf">
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Voucher Approval Report
				   </display:caption>
				   <display:setProperty name="export.pdf" value="true" />
				   <display:setProperty name="export.pdf.filename" value="VoucherApproval-Report.pdf" /> 
				   <display:setProperty name="export.excel" value="false" />
				   <display:setProperty name="export.csv" value="false" />	
				   <display:setProperty name="export.xml" value="false" />	
				
		</display:table>
			
		</s:if>
		<s:elseif test="%{searchResult.fullListSize == 0}">
					<tr><td colspan="7" align="center"><font color="red">No record Found.</font></td>
																	
					</tr>
			</s:elseif>
		</table>
		</s:form>
		
	<script>
	function loadvoucherName(vortype)
	{
	 populatevorname({vortype:vortype.value})
	}
	
	</script>	
	</body>  
</html>