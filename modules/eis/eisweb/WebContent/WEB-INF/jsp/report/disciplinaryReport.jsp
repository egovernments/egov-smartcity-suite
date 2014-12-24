<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>

<html>

<head>
	<title>Departmental Enquiries Report</title>

<script language="JavaScript"  type="text/JavaScript">
var windAry3 = new Array();
var winCntAry3 = new Array();
var wincntr3 = 0; 
function openWindow(viewUrl) {
	var windowName = "EgovDisciplinaryView"; //+viewUrl;
	window.open(viewUrl, windowName, "width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");
	/* if (viewUrl !== null) {
		if (windAry3[windowName] && !windAry3[windowName].closed) {
			windAry3[windowName].focus();
		} else {
			winCntAry3[wincntr3++] = windAry3[windowName] = window.open(viewUrl, windowName, "width=" + (window.screen.width - 200) + ",height=" + (window.screen.height - 100) + ",top=0,left=0,resizable=yes,scrollbars=yes");
		}
	} else {
		msgDialogue.setHeader("Info!");
		msgDialogue.setBody("Please select a record to view !");
		msgDialogue.cfg.setProperty("icon", YAHOO.widget.SimpleDialog.ICON_INFO);
		msgDialogue.show();
	} */
}
	
</script>   

</head>

<body>

	
	<s:if test="%{hasErrors()}">
	    <div id="errorstyle" class="errorstyle" >
	      <s:actionerror/>
	      <s:fielderror/>
	    </div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
	    <div class="messagestyle">
		 <s:actionmessage theme="simple"/>

	    </div>
	</s:if>
					
	<s:form name="disciplinaryReportForm" action ="disciplinaryReport"  theme="simple">	
		<center>
			<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
				<tr>
			<td colspan="5" class="headingwk">
				<div class="arrowiconwk">
					<img src="../common/image/arrow.gif" />
				</div>
				<div class="headplacer">Pending Departmental Enquiries Report</div>
			</td>
		</tr>


		<tr>
			<td class="greyboxwk" >From Date <font color="red">*</font></td>
			<td class="greybox2wk">

				<s:textfield cssClass="selectwk grey"  name="fromDate" id="fromDate"  onblur="validateDateFormat(this);" onfocus="" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img  src="${pageContext.request.contextPath}/common/image/calendar.png"  border=0></a>


			</td>	

			<td class="greyboxwk"> To Date <font color="red">*</font></td>
			<td class="greybox2wk">
				<s:textfield cssClass="selectwk grey"  name="toDate" id="toDate" onblur="validateDateFormat(this);" onfocus="" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
				<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img  src="${pageContext.request.contextPath}/common/image/calendar.png"  border=0></a>
			</td>
		    </tr>



		<tr>
			<td colspan="4">
				<div class="buttonholderwk" align="center">

					<s:submit name="action" value="Search" cssClass="buttonfinal" method="report" />
				</div>
			</td>
		</tr>
		</table>

		<s:if test="%{results != null}" >
				<table style="width: 810;" align="center" cellpadding="0" cellspacing="0" border="0" >
				<tr>
			  <td colspan="4" >
					<div class="tbl3-container" id="tbl-container">
					<display:table name="results" uid="currentRowObject" class="its" export="true" style="width:790px" requestURI=""  >

						<display:caption class="headerbold">Departmental Enquiries pending for date range <fmt:formatDate pattern="dd/MM/yyyy" value="${fromDate}" /> to 
						<fmt:formatDate pattern="dd/MM/yyyy" value="${toDate}" /></display:caption>
						
						<display:column style="border:0;" title="Date" property="chargeMemoDate" format="{0,date,dd/MM/yyyy}"></display:column>
						<display:column style="border:0;" title="Memo Number">
						<s:url id="viewUrl" value="/disciplinaryPunishment/disciplinaryPunishment!loadToViewOrModify.action">
						  <s:param name="disciplinaryPunishment.id" value="#attr.currentRowObject.id"/>
						  <s:param name="mode" value="view"/>
					        </s:url>
					        <a href="#" onclick=openWindow('${viewUrl}') ><s:property value="#attr.currentRowObject.chargeMemoNo" /></a>
						</display:column>
						<display:column style="border:0;" title="Code" property="employeeId.employeeCode"></display:column>
						<display:column style="border:0;" title="Name" property="employeeId.employeeName"></display:column>
						
						<display:column style="border:0;" title="Department" > 
						<s:if test="#attr.currentRowObject.employeeId.getAssignment(#attr.currentRowObject.chargeMemoDate) != null" >
						<s:property value="#attr.currentRowObject.employeeId.getAssignment(#attr.currentRowObject.chargeMemoDate).deptId.deptName" />
						</s:if>
						<s:else>
						NOT AVAILABLE
						</s:else>
						</display:column> 
						<display:column style="border:0;" title="Enquiry Officer" property="enquiryOfficersNames"></display:column>
						<display:column style="border:0;" title="Allegation" property="natureOfAlligations"></display:column>
						
			
						
						<div STYLE="display:table-header-group;">			      
							<display:setProperty name="basic.show.header" value="true" />
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:setProperty name="basic.msg.empty_list_row">
							<tr class="empty"><td colspan="{0}">No records found.</td></tr> 																					  		
							</display:setProperty>
							<display:setProperty name="export.pdf.filename" value="DepartmentalEnquiriesReport.pdf" />
							<display:setProperty name="export.excel.filename" value="DepartmentalEnquiriesReport.xls"/>
							<display:setProperty name="export.csv" value="false"/>
							<display:setProperty name="export.xml" value="false"/>

						</div>
					</display:table >
				    </div>

			</td>
		    </tr>
		    </table>
		 </s:if>



	    </center>
	</s:form>
				
</body>
</html>