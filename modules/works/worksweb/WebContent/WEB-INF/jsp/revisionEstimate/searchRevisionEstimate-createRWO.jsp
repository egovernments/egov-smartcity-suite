<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<link href="<egov:url path='/css/displaytag.css'/>" rel="stylesheet" type="text/css" />
<html>
<head>

<title>Search Revision Estimate</title>
</head>

<body class="yui-skin-sam">
	<div class="errorstyle" id="workOrder_error" style="display: none;"></div>
	<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
	<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
			
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>

	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="searchRevisionEstimate!listOfRevEst.action?source=createRevWorkOrder" theme="simple" name="searchRevisionEstimateForm">
		<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
		<s:push value="model">
		
			<table id="formTable" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										 Search
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
				<tr>
				
				<td class=whiteboxwk  width="25%" >From Date:</td>
				<td class="whitebox2wk" width="25%" > <s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
				<s:textfield name="fromDate" id="fromDate" cssClass="selectwk" value="%{fromDateFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
				<a href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/image/calendar.png"
				alt="Calendar" width="16" height="16" border="0"align="absmiddle" />
				</a></td>
				<td class="whiteboxwk" width="25%" >To Date:</td>	
				<td class="whitebox2wk" width="25%">
				<s:date name="toDate" var="toDateFormat"format="dd/MM/yyyy" />
				<s:textfield name="toDate" id="toDate" value="%{toDateFormat}" cssClass="selectwk" onfocus="javascript:vDateType='3';"onkeyup="DateFormat(this,this.value,event,false,'3')" />
				<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');" onmouseover="window.status='Date Picker';return true;"
				onmouseout="window.status='';return true;"> <img src="${pageContext.request.contextPath}/image/calendar.png" alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
				</a></td>
				</tr>
				<tr>
					<td width="25%" class="greyboxwk"><s:text name="revisionEstimate.search.estimateNo" /></td>
					<td width="25%" class="greybox2wk">
						<div id="revEstnum_autocomplete" style="width:15em;padding-bottom:1em;"> 
							<div><s:textfield  name="estimateNumber" id="estimateNumber"/></div>
						  	<span id="revEstnumResults"></span>
						</div>
						<egov:autocomplete name="revEstnum" width="50" field="estimateNumber" url="searchRevisionEstimate!ajaxFilterRE.action" results="revEstnumResults" paramsFunction="revEstnumSearchParameters"  />
					</td>
					<td class="greyboxwk">&nbsp;</td><td class="greyboxwk">&nbsp;</td>
				</tr>
				
				</table>
				</table>
			<table width="100%">
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="search" value="SEARCH" cssClass="buttonadd" method="listOfRevEst" />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
				</table>
							<s:if test="%{source=='createRevWorkOrder'}">
								<tr>
									<td><%@ include file='searchRevisionEstimates-createList.jsp'%></td>
								</tr>
							</s:if>
							<s:else>	
								<jsp:include page="searchRevisionEstimate-list.jsp" />
							</s:else>
						</s:push>
					</table>
				</div>
			</div>
		</div>
	</div>
</s:form>
	<script type="text/javascript">
	function revEstnumSearchParameters(){
		return "estimateNumber="+dom.get('estimateNumber').value;	
	}

		function gotoCreateRWO(){
		var table=document.getElementById('currentRow');
		var lastRow = table.rows.length-1;
		var orgWorkOrderId="";
		var revEstId="";
		
		if(lastRow==1){
			if(document.getElementById("radio").checked)
				revEstId=document.getElementById("revEstimateId").value;							
		}
		else{
			for(i=0;i<lastRow;i++){			
				if(document.forms[0].radio[i].checked){
					revEstId=document.forms[0].revEstimateId[i].value;						
				}
			}
		}

		if(revEstId!=''){
			window.open('${pageContext.request.contextPath}/revisionEstimate/revisionWorkOrder!newform.action?revEstimateId='+revEstId+'&sourcepage=createRWO','_self');
		}
		else{
			dom.get("workOrder_error").style.display='';
			document.getElementById("workOrder_error").innerHTML='<s:text name="revisionEstimate.select.row" />';
			window.scroll(0, 0);
			return false;
		  }
		  dom.get("workOrder_error").style.display='none';
		  document.getElementById("workOrder_error").innerHTML=''; 

		  
		}
	</script>
</body>
</html>