<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp"%>
<html>

<head>
<title>Housing StartUp Index Report</title>

<sj:head jqueryui="true" jquerytheme="redmond" loadAtOnce="true" />

<link rel="stylesheet" type="text/css"
	href="<c:url value='/common/css/jquery/jquery.multiselect.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/common/css/jquery/jquery.multiselect.filter.css'/>" />
<script type="text/javascript"
	src="<c:url value='/common/js/jquery/jquery.multiselect.min.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/common/js/jquery/jquery.multiselect.filter.js'/>"></script>


<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(function() {

		jQuery("#serviceTypeList").multiselect();
		jQuery("#locboundaryid").hide();
		jQuery("#AreaArea").hide();
		jQuery("#StreetStreet").hide();
		jQuery("#Ward").removeAttr("onchange");

	});
	function dateValidate() {
		var wardnum = document.getElementById('Ward').value;
		var zonenum=document.getElementById('Zone').value;
		if (wardnum != null && wardnum != -1) {
			document.getElementById('adminboundaryid').value = wardnum;
		} 
		else if(wardnum==-1)
		{
		document.getElementById('adminboundaryid').value=zonenum;
		}
		var todaysDate = getTodayDate();
		//alert("validate "+todaysDate);
		var fromdate = document.getElementById('applicationFromDate').value;
		var todate = document.getElementById('applicationToDate').value;
		 var orderissuedfromDate=document.getElementById('orderissuedFromDate').value;
	 		var orderissuedtoDate = document.getElementById('orderissuedToDate').value;
		if ((fromdate != null && fromdate != "" && fromdate != undefined)
				||(todate != null && todate != "" && todate != undefined)) {
		
			if (compareDate(fromdate, todaysDate) == -1) {
				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.todaysDate.validate" />';
				document.getElementById('applicationFromDate').value = "";
				document.getElementById('applicationFromDate').focus();
				return false;
			}
			if (compareDate(todate, todaysDate) == -1) {

				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="toDate.todaysDate.validate" />';

				document.getElementById('applicationToDate').value = "";
				document.getElementById('applicationToDate').focus();
				return false;
			}
			if (compareDate(fromdate, todate) == -1) {

				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="fromDate.toDate.validate" />';
				document.getElementById('applicationFromDate').value = "";
				document.getElementById('applicationToDate').value = "";
				document.getElementById('applicationFromDate').focus();
				return false;
			}

		}
		if ((orderissuedfromDate != null && orderissuedfromDate != "" && orderissuedfromDate != undefined)
				||(orderissuedtoDate != null && orderissuedtoDate != "" && orderissuedtoDate != undefined)) {
			
			if (compareDate(orderissuedfromDate, todaysDate) == -1) {
				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="orderissuedfromDate.todaysDate.validate" />';
				document.getElementById('orderissuedFromDate').value = "";
				document.getElementById('orderissuedFromDate').focus();
				return false;
			}
			if (compareDate(orderissuedtoDate, todaysDate) == -1) {

				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="orderissuedtoDate.todaysDate.validate" />';

				document.getElementById('orderissuedToDate').value = "";
				document.getElementById('orderissuedToDate').focus();
				return false;
			}
			if (compareDate(orderissuedfromDate, orderissuedtoDate) == -1) {

				dom.get("searchRecords_error").style.display = '';
				document.getElementById("searchRecords_error").innerHTML = '<s:text name="orderissuedfromDate.toDate.validate" />';
				document.getElementById('orderissuedFromDate').value = "";
				document.getElementById('orderissuedToDate').value = "";
				document.getElementById('orderissuedFromDate').focus();
				return false;
			}

		}
		if (!checkAllfields()){
				return false;
		}

	}
	function resetValues() {

		document.getElementById('serviceTypeList').value = "-1";
		document.getElementById('adminboundaryid').value = "-1";
		document.getElementById('Zone').value = "-1";
		document.getElementById('Ward').value = "-1";
		document.getElementById('applicationFromDate').value = "";
		document.getElementById('applicationToDate').value = "";
		document.getElementById('orderissuedFromDate').value = "";
		document.getElementById('orderissuedToDate').value = "";
		document.getElementById('appMode').value="-1" ; 
		jQuery('#serviceTypeList').multiselect('uncheckAll');
		document.getElementById("tableData").style.display='none';
	
	}
	function closeWindow() {
		window.close();
	}

	function checkAllfields(){
			  if(document.getElementById('adminboundaryid').value ==-1 && document.getElementById('applicationFromDate').value=="" && document.getElementById('applicationToDate').value=="" &&
						 document.getElementById("serviceTypeList").value =="" && document.getElementById('appMode').value==-1 && document.getElementById('orderissuedFromDate').value=="" && document.getElementById('orderissuedToDate').value=="")
				{                     
					dom.get("searchRecords_error").style.display = '';
		   			document.getElementById("searchRecords_error").innerHTML = 'At Least one information has to be provided';	
		   			return false;         
		   			}
	   			return true;
			   	
		 		
		} 
</script>
</head>
<body>
	<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form action="housingStartUpIndexReport"
		name="housingStartUpIndexReportForm" theme="simple">

		<div class="formheading" /></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">

				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="label.fromdate" /></td>
					<td class="bluebox"><sj:datepicker
							value="%{applicationFromDate}" id="applicationFromDate"
							name="applicationFromDate" displayFormat="dd/mm/yy"
							showOn="focus" maxlength="10" /></td>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="label.todate" /></td>
					<td class="bluebox"><sj:datepicker
							value="%{applicationToDate}" id="applicationToDate"
							name="applicationToDate" displayFormat="dd/mm/yy" showOn="focus"
							maxlength="10" /></td>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">&nbsp;</td>
							<td class="bluebox">&nbsp;</td>
				</tr>
				<tr>
					<egov:loadBoundaryData adminboundaryid="${adminboundaryid}"
						 adminBndryVarId="adminboundaryid"
						/>
					<s:hidden id="adminboundaryid" name="adminboundaryid"
						value="%{adminboundaryid}" />
					
				</tr>
				<tr>
					<td class="bluebox">&nbsp;</td>
					<td class="bluebox"><s:text name="Service.Type" /></td>
					<td width="21%" class="bluebox"><s:select id="serviceTypeList"
							name="serviceTypeList" multiple="true"
							list="dropdownData.serviceTypeListadd" listKey="id"
							listValue="code+'-'+description" value="%{serviceTypeList}" /></td>
					<td class="bluebox">&nbsp;</td>
					
		<td class="bluebox" ><s:text name="applMode" /> </td>
		<td class="bluebox"><s:select list="{'General','Green Channel'}" id="appMode"
         name="appMode" value="%{appMode}" headerKey="-1" 
         headerValue="----choose-----"  />
          </td>
            <td class="bluebox" width="13%">&nbsp;</td>
              <td class="bluebox" width="13%">&nbsp;</td>
               <td class="bluebox" >&nbsp;</td>
      
				</tr>
				<tr> 
					<td class="greybox">&nbsp;</td>
					<td class="greybox" width="15%"><s:text name="label.permitissueddate" /></td>
					<td class="greybox"><sj:datepicker
							value="%{orderissuedFromDate}" id="orderissuedFromDate"
							name="orderissuedFromDate" displayFormat="dd/mm/yy"
							showOn="focus" maxlength="10" /></td>
					<td class="greybox">&nbsp;</td>
					<td class="greybox"  width="15%"><s:text name="label.permitissuedtodate" /></td>
					<td class="greybox"><sj:datepicker
							value="%{orderissuedToDate}" id="orderissuedToDate"
							name="orderissuedToDate" displayFormat="dd/mm/yy" showOn="focus"
							maxlength="10" /></td>
							<td class="greybox">&nbsp;</td>
							<td class="greybox">&nbsp;</td>
							<td class="greybox">&nbsp;</td>
				</tr>
			</table>
			<div class="buttonbottom" align="center">
				<table>
					<tr>
						<td><s:submit cssClass="buttonsubmit" id="search"
								name="search" value="Search" method="searchResult"
								onclick="return dateValidate();" /></td>
						<td><input type="button" id="reset" name="reset"
							class="button" value="Reset" onclick="return resetValues();" /></td>
						<td><input type="button" name="close" id="close"
							class="button" value="Close" onclick="window.close();" /></td>
					</tr>
				</table>
			</div>
		<div id="tableData">
		<s:if test="%{searchMode=='result'}">
				<div class="infostyle" id="search_error" style="display: none;"></div>
				<s:if test="{regResultList!=null}">
				<div id="displaytbl">
				
				<div class="headingwk" align="center">
				<tr width="100%" >
                    	<div style="font-size: 13;font-color: red; margin-top: 4px;" align="center">
                            <s:text name="hsuireport.heading"/>
                        </div>
                     </tr>
				</div>
				 <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                        <td class="headingwk" >
                            <div class="arrowiconwk">
                                <img src="../common/image/arrow.gif" />
                            </div>
                            <div style="font-size: 13;font-color: red; margin-top: 4px;" align="left" width="80%">
                            <s:text name="hsuireport.subheading"/></div>
                            </td>
                            <td class="headingwk" >
                          	<div style="font-size: 13;font-color: red; margin-top: 4px;" align="center" width="20%">
                            <s:text name="hsuireport.subbuilding"/></div>      
                            
                         </td>
                    </tr>
                    
                </table>
                
					<display:table name="regResultList" export="true" requestURI="" id="registrationId" class="its" uid="currentRowObject" pagesize="25">
					<div STYLE="display: table-header-group" align="center">
								<display:setProperty name="basic.show.header" value="true" />
								<display:setProperty name="export.xml" value="false" />
								<display:setProperty name="export.pdf" value="true" />
								<display:setProperty name="export.excel" value="true" />
								<display:setProperty name="export.pdf.filename"
									value="citizenMisReport.pdf" />
								<display:setProperty name="export.excel.filename"
									value="citizenMisReport.xls" />
								<display:setProperty name="export.csv" value="false" />



								<display:column title=" Sl No" style="text-align:center;">
								<s:property value="#attr.currentRowObject_rowNum" />
							</display:column>

								<display:column class="hidden" headerClass="hidden" media="html">
									<s:hidden id="registrationId" name="registrationId"
										value="%{#attr.currentRowObject.id}" />
								</display:column>

								<display:column title="Zone (1)"
									style="text-align: center;"
									property="registration.adminboundaryid.parent.name" />
									
								<display:column title="Ward (2)" style="width: 5%;text-align: center;"
									property="registration.adminboundaryid.name" />
							<display:column title="Location of Building(Address) (3)"
									style="text-align: left;" property="registration.bpaSiteAddress" />
									
								<display:column title="Pincode of Location (4)"
									style="text-align: center;"
									property="registration.bpaSitePincode" />
									
								<display:column title="Applicant Name (5)"
									style="text-align: left;"
									property="registration.owner.firstName" />
								
								<display:column title="Applicant Address(6)"
									style="text-align: left;" property="registration.applicantAddress1" />
								
								<display:column title="Mobile Number(7)"
									style="width: 5%;text-align: center;" property="registration.owner.mobilePhone" />
								<display:column title="Building Permit No (8)"
									style="width: 5%;text-align: center;" property="registration.baNum" />

								
								<display:column title="Permit Issue Date (dd/mm/yy) (9)"
									style="text-align: center;" 
									property="issuedDate" />
									
								<display:column title="Whether Building Contains Single-01 Multiple-02(10)"
									style="text-align: center;"
									property="unitClass" />
									
								<display:column title="If Mutiple No.of Housing Units(11)"
									style="text-align: center;"
									property="apprBuiding.unitCount" />
				
								<display:column title="No. of Storeys in the Building (12)"
									style="text-align: center;"
									property="apprBuiding.floorCount" />
								
								<display:column title="Whether Basement is a part of housing unit (13)"
									style="text-align: center;"
									property="base" /> 
									
								<display:column title="Height of the Building(in Mtrs) (14)"
									style="text-align:center;" property="apprBuiding.buildingHeight" />
								
								<display:column title="Total Floor Area of the Building (in Square Mtrs)(15)"
									style="text-align: center;"
									property="apprBuiding.totalFloorArea" />
								
								
							
							</div>
						</display:table>
						</div>
					</s:if>
					</s:if>
				</div>
	
	</s:form>
</body>
</html>