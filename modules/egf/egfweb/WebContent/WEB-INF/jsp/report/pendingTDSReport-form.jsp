<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<div class="formmainbox">
<div class="formheading"></div>
<div class="subheadnew">TDS Report</div>

<s:form action="pendingTDSReport" theme="simple" name="pendingTDSReport">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
	    <td class="greybox" width="10%">Recovery Code:<span class="mandatory">*</span></td>
	    <td class="greybox">
	    	<s:select name="recovery" id="recovery" list="dropdownData.recoveryList" listKey="id" listValue="type" headerKey="-1" headerValue="----Choose----" />
	    </td>
		<td class="greybox" width="10%">As On Date:<span class="mandatory">*</span></td>
		<td class="greybox">
			<s:textfield name="asOnDate" id="asOnDate" cssStyle="width:100px" value='%{getFormattedDate(asOnDate)}' onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('pendingTDSReport.asOnDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)<br/>
		</td>
	</tr>
	<tr>
	    <td class="bluebox" width="10%">Fund:<span class="mandatory">*</span></td>
	    <td class="bluebox">
	    	<s:select name="fund" id="fund" list="dropdownData.fundList" listKey="id" listValue="name" headerKey="-1" headerValue="----Choose----" />
	    </td>
	    <td class="bluebox" width="10%">Department:</td>
	    <td class="bluebox">
	    	<s:select name="department" id="department" list="dropdownData.departmentList" listKey="id" listValue="deptName" headerKey="-1" headerValue="----Choose----" />
	    </td>
	</tr>
	<tr>
	    <td class="greybox" width="10%">Party Name:</td>
	    <td class="greybox">
	    	<input type="text" name="partyName" id="partyName" onclick="loadEntities()" autocomplete="off" onkeyup="autocompleteEntities(this,event)" onblur="splitValues(this)"/>
	    	<div id="codescontainer"></div>
	    </td>
	    <td class="greybox" width="10%">&nbsp;</td>
	    <td class="greybox">&nbsp;</td>
	</tr>
	<tr>
	    <td class="bluebox" width="10%"><span id="showRemittedEntrieslabel">Include Remittance Info:</span></td>
	    <td class="bluebox">
	    	<s:checkbox name="showRemittedEntries" id="showRemittedEntries"/>
	    </td>
	    <td class="bluebox">&nbsp;</td>
	    <td class="bluebox">&nbsp;</td>
	</tr>
</table>
<br/>
<div class="buttonbottom">
  <input type="button" value="Search" class="buttonsubmit" onclick="return getData()"/>
  &nbsp;
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>
<s:hidden name="detailKey" id="detailKey"></s:hidden>
</s:form>
</div>

<div id="results">
</div>
