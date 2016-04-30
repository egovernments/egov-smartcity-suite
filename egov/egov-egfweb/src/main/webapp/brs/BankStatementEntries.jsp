<!--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java"%>
<%@ page import="com.exilant.eGov.src.transactions.brs.*,
		org.apache.log4j.Logger,
		org.egov.commons.CChartOfAccounts,
		org.egov.infstr.commons.*,java.text.SimpleDateFormat,
		java.util.ArrayList"
		
%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.TreeSet" %>
<html>
<head>
	
	<title>Bank Statement Entries</title>
	
	<script>
	
	function getAccountNumbers()
	{
		document.forms("bankStatementEntriesForm").action = "../brs/BankStatementEntries.do?submitType=getAccountNumbers";
		if(document.bankStatementEntriesForm.bankId.options[document.bankStatementEntriesForm.bankId.selectedIndex].value != 0)
		{
			
			document.forms("bankStatementEntriesForm").submit();
		}
	
	}
	function getDetails()
	{
		document.forms("bankStatementEntriesForm").action = "../brs/BankStatementEntries.do?submitType=getDetails";
		if(document.bankStatementEntriesForm.accId.options[document.bankStatementEntriesForm.accId.selectedIndex].value != 0)
		{
			document.forms("bankStatementEntriesForm").submit();
		}
		else
			bootbox.alert("Select Account No")
		
	}
	
	function ButtonPress(arg)
	{
		if(arg == "savenew")
		{
			if(document.bankStatementEntriesForm.bankId.options[document.bankStatementEntriesForm.bankId.selectedIndex].value == 0)
			{
				bootbox.alert("Select Bank");
				return;
			}
			
			if(document.bankStatementEntriesForm.accId.options[document.bankStatementEntriesForm.accId.selectedIndex].value == 0)
			{
				bootbox.alert("Select Account Number");
				return;
			}
			var table= document.getElementById("gridBankEntry");
			for(var i=1;i<table.rows.length;i++)
			{
				if(getControlInBranch(table.rows[i],'refNo').value=="")
				{
					bootbox.alert("Enter Reference No:");
					getControlInBranch(table.rows[i],'refNo').focus();
					return;
				}
				if(getControlInBranch(table.rows[i],'type').options[getControlInBranch(table.rows[i],'type').selectedIndex].value == 0)
				{
					bootbox.alert("Select Type");
					return;
				}
				var val=getControlInBranch(table.rows[i],'entrydate').value;
				if(val != null && val != "" && val.length>0)
				{					
					var dat=validateDate(val);
					if (!dat) 
					{
						bootbox.alert('Invalid date format : Enter Date as dd/mm/yyyy');
						getControlInBranch(table.rows[i],'entrydate').focus();
						return;
					}
				}
				else
				{	
					bootbox.alert("Enter Date");
					getControlInBranch(table.rows[i],'entrydate').focus();
					return;
				}
				if(getControlInBranch(table.rows[i],'amount').value=="")
				{
					bootbox.alert("Enter Amount");
					getControlInBranch(table.rows[i],'amount').focus();
					return;
				}
				if(getControlInBranch(table.rows[i],'remarks').value=="")
				{
					bootbox.alert("Enter Remarks");
					getControlInBranch(table.rows[i],'remarks').focus();
					return;
				}
				if(getControlInBranch(table.rows[i],'postTxn').checked == true)
				{
					if(getControlInBranch(table.rows[i],'accountCodeId').options[getControlInBranch(table.rows[i],'accountCodeId').selectedIndex].value == 0)
					{
						bootbox.alert("Select Account Code");
						return;
					}
					if(document.bankStatementEntriesForm.fundId.options[document.bankStatementEntriesForm.fundId.selectedIndex].value == 0)
					{
						bootbox.alert("Select Fund ");
						return;
					}
					
				}
			}
			document.forms[0].fundId.disabled=false;
			document.forms("bankStatementEntriesForm").action = "../brs/BankStatementEntries.do?submitType=saveDetails";		
			document.forms("bankStatementEntriesForm").submit();
		}
		if(arg == "view")
		{
			getDetails();
		}
		if(arg == "close")
		{
			window.close();
		}
		
	}
	function onClickCancel()
	{
		window.location="../brs/BankStatementEntries.jsp";	
	}
	function addRow()
	{

		var tbl = document.getElementById('gridBankEntry');
		var tbody=tbl.tBodies[0];
		var lastRow = tbl.rows.length;

		var rowObj = document.getElementById('detailsRow').cloneNode(true);

		tbody.appendChild(rowObj);
		document.forms[0].bankEntryId[lastRow-1].value="";
		document.forms[0].refNo[lastRow-1].value="";
		document.forms[0].entrydate[lastRow-1].value="";
		document.forms[0].amount[lastRow-1].value="";
		document.forms[0].remarks[lastRow-1].value="";
			
		
	}
	function deleteRow()
	{

	  var tbl = document.getElementById('gridBankEntry');
	  var lastRow = (tbl.rows.length)-1;

		if(lastRow ==1)
		{
			bootbox.alert("This row can not be deleted");
			return false;
		 }
		else
		{
			tbl.deleteRow(lastRow);


			return true;
		}
		
	}
	var myrowId;
	function selected(e)
	{
		myrowId=e.rowIndex;
	}
	function keyPressed() 
	{

	  var F2 = 113;
	  var tbl = document.getElementById('gridBankEntry');
	  var rCount=tbl.rows.length-1;
	  if ((event.keyCode == F2) && (myrowId==rCount)) 
	  {
		addRow();

	  }
	}
	function setUpdated(obj)
	{
		var rowobj=getRow(obj);
		var table= document.getElementById("gridBankEntry");
		getControlInBranch(table.rows[rowobj.rowIndex],'isUpdated').value="yes";
	}
	function clearData()
	{
		
		var target="<%=(request.getAttribute("alertMessage"))%>";
		if(target!="null")
		{
			bootbox.alert("<%=request.getAttribute("alertMessage")%>");
			<%	
			if(request.getAttribute("alertMessage") != null)
			{
				session.removeAttribute("accNumberList2");
				request.setAttribute("alertMessage",null);
			}
			%> 
			document.bankStatementEntriesForm.bankId.options.selectedIndex=0;
			document.bankStatementEntriesForm.accId.options.selectedIndex=0;
			document.bankStatementEntriesForm.type.options.selectedIndex=0;
			document.bankStatementEntriesForm.accountCodeId.options.selectedIndex=0;
		}
		
		<%
		ArrayList al1=(ArrayList)request.getAttribute("brsEntries");
		if(al1!=null && al1.size()==0)
		{
		%>
			document.getElementById('searchBe').style.display ='none';
			document.getElementById('beDetail').style.display ='block';
			document.getElementById('fundDetail').style.display ='block'
			document.getElementById('departmentfunctionary').style.display ='block'
			document.bankStatementEntriesForm.type.options.selectedIndex=0;
			document.bankStatementEntriesForm.accountCodeId.options.selectedIndex=0;
			var table= document.getElementById("gridBankEntry");
			getControlInBranch(table.rows[1],'refNo').focus();
		<%
		}
		else
		{
		%>
		document.bankStatementEntriesForm.bankId.focus();
		<%
		}
		if(al1!=null && al1.size()>0)
		{
		%>	
		
			document.getElementById('searchBe').style.display ='none';
			document.getElementById('beDetail').style.display ='block';
			document.getElementById('fundDetail').style.display ='block'
			document.getElementById('departmentfunctionary').style.display ='block'
		<%
		}
		if(al1 == null)
		{
		%>
			document.bankStatementEntriesForm.type.options.selectedIndex=0;
			document.bankStatementEntriesForm.accountCodeId.options.selectedIndex=0;
		<%
		}
		%>
	}
	function markForTxn(obj)
	{
		var rowobj=getRow(obj);
		var table= document.getElementById("gridBankEntry");
		if(getControlInBranch(table.rows[rowobj.rowIndex],'postTxn').checked == true)
		{
			getControlInBranch(table.rows[rowobj.rowIndex],'passVoucher').value = "yes";
		}
		else
		{
			getControlInBranch(table.rows[rowobj.rowIndex],'passVoucher').value = "no";
		}
		
	
	}
	function selUnSelAll()
	{
		var table=document.getElementById('gridBankEntry');
		var chkBox,hchkBox;
		hchkBox=getControlInBranch(table.rows[0],"selectChq");
		for(var i=1;i<table.rows.length;i++)
		{
			
			chkBox=getControlInBranch(table.rows[i],"postTxn");
			if(hchkBox.checked)
			{
			  chkBox.checked=true;
			  getControlInBranch(table.rows[i],'passVoucher').value = "yes";
			}
			else
			{
			 chkBox.checked=false;
			 getControlInBranch(table.rows[i],'passVoucher').value = "no";
			}

		}
	}
	function highlight(obj)
	{
		if(!obj.selectedIndex == 0)
			bootbox.alert("cfg");
		
	}
	</script>
	
</head>
<body bgcolor="#ffffff" onload="clearData()" onKeyDown ="CloseWindow(window.self);" onKeyUp="keyPressed()">
	<% 	Logger logger = Logger.getLogger(getClass().getName()); 
		logger.info(">>       INSIDE JSP   >>");
	%>
<br>

<html:form  action="/brs/BankStatementEntries.do" >
<table align="center" class="tableStyle">
<tr ><td colspan="4">&nbsp;</td>
<tr>
<td class="labelcell" align="right" width="25%">Bank And Branch <SPAN class="leadon">*</SPAN></td>
<td class="smallfieldcell" align="center" width="25%">
			<html:select  property="bankId" onchange="getAccountNumbers()" styleClass="bigcombowidth">
			<html:option value='0'>----choose----</html:option>
			<%
				HashMap bankId =(HashMap)session.getAttribute("bankBranchList");
				if((bankId!=null)&&(!bankId.isEmpty()))
				{
				TreeSet ts=new TreeSet(bankId.keySet());
				if((ts!=null)&&(!ts.isEmpty()))
				{
					Iterator bankIdIterator = ts.iterator();
					while (bankIdIterator.hasNext())
					{
						String keyVal=(String)bankIdIterator.next();
			%>
					<html:option value='<%= keyVal %>'><%= bankId.get(keyVal) %></html:option>

			<%
					}
				}
			    }
			%>
			</html:select>

		</td>
		<td class="labelcell" align="right" width="25%">Account Number<SPAN class="leadon">*</SPAN></td>
		<td class="smallfieldcell" align="center" width="25%">
			<html:select  property="accId" styleClass="combowidth">
			<html:option value='0'>----choose----</html:option>
			<%
				HashMap accountId =(HashMap)session.getAttribute("accNumberList2");
				if((accountId!=null)&&(!accountId.isEmpty()))
				{
				TreeSet ts=new TreeSet(accountId.keySet());
				if((ts!=null)&&(!ts.isEmpty()))
				{
					Iterator accountIdIterator = ts.iterator();
					while (accountIdIterator.hasNext())
					{
					String keyVal=(String)accountIdIterator.next();
			%>
					<html:option value='<%= keyVal %>'><%= accountId.get(keyVal) %></html:option>

			<%
					}
				}
				}

			%>
			</html:select>
		</td>
	</tr>
	<tr id="fundDetail" name="fundDetail"  style="DISPLAY: none"> 
		<td class="labelcell" align="right">Fund<SPAN class="leadon">*</SPAN></td>
		<td class="smallfieldcell" align="center"><html:select  property="fundId" styleClass="combowidth" >
		<option value='0'>----choose----</option> 
		<%
			HashMap fundId =(HashMap)request.getAttribute("fudList");
			String defaultFund=(String)request.getAttribute("defaultFund");
		   if((fundId!=null)&&(!fundId.isEmpty()))
		   {
			TreeSet ts=new TreeSet(fundId.keySet());
			if((ts!=null)&&(!ts.isEmpty()))
			{
				Iterator fundIdIterator = ts.iterator();
				while (fundIdIterator.hasNext())
				{
					String keyVal=(String)fundIdIterator.next();
					if(keyVal.equalsIgnoreCase(defaultFund))
					{
		%>
					<option value='<%= keyVal %>' selected="selected"><%= fundId.get(keyVal) %></option>

		<%			
					}
					else
					{
		%>
					<option value='<%= keyVal %>'><%= fundId.get(keyVal) %></option>	
		<%
					}
				}
			}
		    }
		%>
		</html:select>
		</td>
		<td class="labelcell" align="right">Fund Source</td>
		<td class="smallfieldcell" align="center">
		<html:select  property="fundSourceId" styleClass="combowidth"> 
		<html:option value='0'>----choose----</html:option>
		<%
			HashMap fundSourceId =(HashMap)request.getAttribute("fudSourceList");
			if((fundSourceId!=null)&&(!fundSourceId.isEmpty()))
			{
			TreeSet ts=new TreeSet(fundSourceId.keySet());
			if((ts!=null)&&(!ts.isEmpty()))
			{
				Iterator fsIdIterator = ts.iterator();
				while (fsIdIterator.hasNext())
				{
					String keyVal=(String)fsIdIterator.next();
		%>
				<html:option value='<%= keyVal %>'><%= fundSourceId.get(keyVal) %></html:option>

		<%
				}
			}
		    }
		%>
		</html:select>
		</td>
		
	</tr>
	<tr id="departmentfunctionary" name="deptfunc" style="DISPLAY: none" >
	<td class="labelcell" align="right">Department <SPAN class="leadon">*</SPAN></td>
	<td class="smallfieldcell" align="center">
		<html:select styleClass="combowidth" property="departmentId"  >
		<c:forEach var="dept" items="${departmentList}" > 
			<html:option value="${dept.id}">${dept.deptName} </html:option>
		</c:forEach>
		
		</html:select>
	</td>
	<td class="labelcell" align="right">Functionary</td>
		<td class="smallfieldcell" align="center">
			<html:select styleClass="combowidth" property="functionaryId"  >
				<html:option value=''>----choose----</html:option>
			<c:forEach var="functionary" items="${functionaryList}" > 
				<html:option value="${functionary.id}">${functionary.name}</html:option>
			</c:forEach>
			
			</html:select>
	</td>
</tr>
	<tr ><td colspan="4">&nbsp;</td></tr>
	</table>
	<div id="searchBe" name="searchBe">
		<table align=center >
		<tr >
			<td ><html:button value="Add/View Bank Entry" property="b1" onclick="ButtonPress('view')" styleClass="button"/></td>
			<td><html:button styleClass="button" value="Cancel" property="b4" onclick="onClickCancel()" /></td>
		<td><html:button styleClass="button" value="Close" property="b3" onclick="ButtonPress('close')" /></td>
		<tr>
		</table>
	</div>
	<div id="beDetail" name="beDetail"  style="DISPLAY: none">
	<div class="tbl-containerLessHeight" id="divSpec">
	<table border=1 align="center" cellpadding="0" cellspacing="0" id="gridBankEntry" name="gridBankEntry" >
	<tbody>
		<tr style="HEIGHT: 22px">
			<td class="thStlyle" width="7%"><div align="center">Ref&nbsp;No</div></td>
			<td class="thStlyle" width="10%"><div align="center">Type</div></td>
			<td class="thStlyle" width="16%"><div align="center">Date</div></td>
			<td class="thStlyle" width="16%"><div align="center">Amount</div></td>
			<td class="thStlyle" width="26%"><div align="center">Remarks</div></td>
			<td class="thStlyle" width="10%"><div align="center">A/c Head</div></td>
			<td class="thStlyle" width="7%"><div align="center"><input type="checkbox" value="Select"  ID="selectChq" NAME="selectChq" onClick="selUnSelAll()">Create Voucher</div></td>
		</tr>
		<%
			ArrayList al=(ArrayList)request.getAttribute("brsEntries");
			if(al!=null && al.size()>0)
			{
				BrsEntries be=null;
				SimpleDateFormat sdf =new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date dt;
				for (Iterator itr = al.iterator();itr.hasNext();)
				{
				 be=(BrsEntries)itr.next();
		%>
				<tr id="detailsRow" name="detailsRow" onClick="selected(this);">
				<td class="smallfieldcell" >
				<html:hidden  property="bankEntryId" value="<%= be.getId() %>"/>
				<html:hidden  property="instrumentHeaderId" value="<%= be.getInstrumentHeaderId()%>"/>
				<html:hidden  property="isUpdated" value="no"/>
				<html:text  property="refNo" value="<%= be.getRefNo() %>" size="12" maxlength="15" onchange="setUpdated(this)"/>
				</td> 
				<td class="smallfieldcell" ><html:select  property="type" style="width:90 px" onchange="setUpdated(this)">
					<html:option value='0'>--Choose--</html:option>
					<%
					 if(be.getType().equalsIgnoreCase("R"))
					 {
					 %>
					<option value='R' selected="selected">&nbsp;Receipt&nbsp;</option>
					<option value='P'>&nbsp;Payment&nbsp;</option>
					<%
					}
					 else
					{
					%>
					<option value='R' >&nbsp;Receipt&nbsp;</option>
					<option value='P' selected="selected">&nbsp;Payment&nbsp;</option>>
					<%
					}
					%>
					</html:select>
				</td> 	
				<%
				dt=new Date();
				dt = sdf.parse(be.getTxnDate());
				String recEntryDate = formatter.format(dt);
				
				%>
				<td class="smallfieldcell" ><html:text  property="entrydate"  value="<%= recEntryDate %>"  maxlength="15" onkeyup="DateFormat(this,this.value,event,false,'3')" onchange="setUpdated(this)"/></td> 
				<td class="smallfieldcell" ><html:text  property="amount"  style="text-align:right" value="<%= be.getTxnAmount() %>"  maxlength="18" onchange="setUpdated(this)"/></td> 
				<td class="smallfieldcell" ><html:text  property="remarks"   value="<%= be.getRemarks() %>"  maxlength="100" onchange="setUpdated(this)"/></td> 
				<td class="smallfieldcell"><html:select  property="accountCodeId"  onchange="setUpdated(this)" styleClass="bigcombowidth">
					<html:option value='0'>--Choose--</html:option>
					<%
					ArrayList relList =(ArrayList)session.getAttribute("accountCodeListForDetails");
					CChartOfAccounts c;
					if(relList!=null)
					{
						Iterator myIterator = relList.iterator();
						while (myIterator.hasNext())
						{
							c= (CChartOfAccounts)myIterator.next();
							if(c.getId().toString().equals(be.getGlCodeId()))
							{
							%>
							<option value='<%=c.getId().toString() %>' selected="selected"><%=c.getGlcode() %>   ---  <%=c.getName()%> </option>
							<%
				       			}else
				       			{	
				       			%>
							<option value='<%=c.getId().toString() %>' ><%=c.getGlcode() %>   ---  <%=c.getName()%> </option>
							<%}
						}
					}	
							
					%>
					</html:select>
				</td>
				<td class="smallfieldcell" width="7%" align="center"><input type=checkbox id="postTxn" name="postTxn" onclick="markForTxn(this)" onchange="setUpdated(this)"/>
				<html:hidden property="passVoucher" value="no"/></td> 
				</tr>
			<%	
				}
			}
			else
			{
			%>
			<tr id="detailsRow" name="detailsRow" onClick="selected(this);">
			<td class="smallfieldcell" width="7%">
			<html:hidden  property="bankEntryId" value=""/>
			<html:text  property="refNo" value="" size="12" maxlength="15" />
			</td> 
			<td class="smallfieldcell" width="10%"><html:select  property="type" style="width:90 px">
				<html:option value='0'>--Choose--</html:option>
				<html:option value='R'>&nbsp;Receipt&nbsp;</html:option>
				<html:option value='P'>&nbsp;Payment&nbsp;</html:option>
				</html:select>
			</td> 	
			<td class="smallfieldcell" width="15%"><html:text  property="entrydate" value=""  maxlength="18" onkeyup="DateFormat(this,this.value,event,false,'3')"/></td> 
			<td class="smallfieldcell" width="15%"><html:text  property="amount" value="" style=";text-align:right" maxlength="15" /></td> 
			<td class="mediumfieldcell" width="26%"><html:text  property="remarks" value=""  maxlength="100"/></td> 
			<td class="smallfieldcell" width="10%"><html:select  property="accountCodeId" styleClass="bigcombowidth" onmouseover="this.parentNode.title  =this.options[this.selectedIndex].text;"> 
				<html:option value='0'>--Choose--</html:option>
				<%
				ArrayList relList =(ArrayList)session.getAttribute("accountCodeListForDetails");
				CChartOfAccounts c;
				if(relList!=null)
				{
					Iterator myIterator = relList.iterator();
					while (myIterator.hasNext())
					{
						c= (CChartOfAccounts)myIterator.next();

						{%>


							<html:option value='<%=c.getId().toString() %>'><%=c.getGlcode() %> - <%=c.getName()%> </html:option>


						<%}
					}
				}
				%>
				</html:select>
			</td>
			<td class="smallfieldcell" width="7%" align="center"><input type=checkbox id="postTxn" name="postTxn" onclick="markForTxn(this)"/>
			<html:hidden property="passVoucher" value="no"/></td> 
		</tr>
		<% 
		 }
		 %>
	</tbody>
	</table>
	<table>
		<tr>
		 <td colspan=3>
		 	<div id="addDeleteRecords">
		 	<table>
		 	<tr>
		 	<td colspan=3><input class="button" type="button" name="addDetail"   value="Add Row" onclick="javascript:addRow();" />
		 		<input class="button" type="button"   value="Delete"  name="deleteDetail" onclick="javascript:return deleteRow();" />
		 		
		 	</td>		              	 
			</tr></table>
			</div>
		</td>		              	 
		</tr>
	</table>
	<table align=center>
	<tr></td>&nbsp;</td></tr>
	<tr class="row1">
		<td><html:button styleClass="button" value="Save" property="b2" onclick="ButtonPress('savenew')" /></td>
		<td><html:button styleClass="button" value="Cancel" property="b4" onclick="onClickCancel()" /></td>
		<td><html:button styleClass="button" value="Close" property="b3" onclick="ButtonPress('close')" /></td>
	<tr>
	</table>
	</div>
</html:form>
</body>
</html>
