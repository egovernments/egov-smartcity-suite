<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ page import="java.util.*"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page buffer="16kb"%>

<html>
<head>
<style type="text/css">
#designation {
	position: absolute;
	left: 11em;
	width: 9%
}

#designation .yui-ac-content {
	position: absolute;
	width: 80%;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#designation .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 40%;
	background: #a0a0a0;
	z-index: 9049;
}

#designation ul {
	padding: 5px 0;
	width: 80%;
}

#designation li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#designation li.yui-ac-highlight {
	background: #ff0;
}

#designation li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-dt-liner .yui-ac-input {
	position: relative;
	width: 100%;
	min-width: 150px;
}

#position {
	position: absolute;
	left: 11em;
	width: 9%
}

#position .yui-ac-content {
	position: absolute;
	width: 80%;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 9050;
}

#position .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 40%;
	background: #a0a0a0;
	z-index: 9049;
}

#position ul {
	padding: 5px 0;
	width: 80%;
}

#position li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#position li.yui-ac-highlight {
	background: #ff0;
}

#position li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-dt-liner .yui-ac-input {
	position: relative;
	width: 100%;
	min-width: 150px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="PositionReport.title" /></title>
<script language="JavaScript" type="text/javascript">
	var selectedEmpCode1;
	var selectedPos;
	var yuiflag1 = new Array();
	var oAutoCompPos;
	var oAutoCompDesg;
	var dom = YAHOO.util.Dom;
	function initiateRequest() {
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}

	function loadDesignation() {
		var type = 'getDesignation';
		var url = "${pageContext.request.contextPath}/pims/searchDesignationAjax.jsp?type="
				+ type;
		var req2 = initiateRequest();
		req2.onreadystatechange = function() {
			if (req2.readyState == 4) {
				if (req2.status == 200) {

					var codes2 = req2.responseText;
					var a = codes2.split("^");
					var codes = a[0];
					empCodeArray = codes.split("+");
					selectedEmpCode1 = new YAHOO.widget.DS_JSArray(empCodeArray);
				}
			}
		};
		req2.open("GET", url, true);
		req2.send(null);

	}

	function autocompleteForDesignation(obj, event) {
		// set designation of dropdown
		var src = obj;
		var target = document.getElementById('designation');
		var posSrc = findPos(src);
		target.style.left = posSrc[0];

		target.style.top = posSrc[1] + 25;
		if (obj.name == 'designationName')
			target.style.left = posSrc[0] + 0;
		target.style.width = 500;
		var currRow = getRow(obj);
		var coaCodeObj = obj;
		if (yuiflag1[currRow.rowIndex] == undefined) {
			//40 --> Down arrow, 38 --> Up arrow
			if (event.keyCode != 40) {

				if (event.keyCode != 38) {
					oAutoCompDesg = new YAHOO.widget.AutoComplete(coaCodeObj,
							'designation', selectedEmpCode1);
					oAutoCompDesg.queryDelay = 0;
					oAutoCompDesg.useShadow = true;
					oAutoCompDesg.maxResultsDisplayed = 15;
					oAutoCompDesg.useIFrame = true;
				}

			}
			yuiflag1[currRow.rowIndex] = 1;

		}
	}

	function fillDesgAfterSplit(obj, neibrObjName) {
		var currRow = getRow(obj);
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj = getControlInBranch(currRow, neibrObjName);
		//neibrObj=document.getElementsByName(neibrObjName)[currRow.rowIndex-1];
		var temp = obj.value;
		temp = temp.split("`-`");

		if (temp[1] != '' && temp[1] != undefined) {
			neibrObj.value = temp[1];
		} 
		else{
			neibrObj.value = "";
			}
	
		obj.value = temp[0];
	}

	function loadPositions() {

		if (document.getElementById("departmentId").value != null
				&& document.getElementById("desgEnterId").value != null) {
			var deptSelected = document.getElementById("departmentId").value;
			var designationSelected = document.getElementById("desgEnterId").value;
			var type = 'getAllPosition';
			var url = "${pageContext.request.contextPath}/pims/searchForPositionAjax.jsp?deptSelected="
					+ deptSelected
					+ "&designationSelected="
					+ designationSelected + "&type=" + type;

			var req2 = initiateRequest();

			req2.onreadystatechange = function() {
				if (req2.readyState == 4) {
					if (req2.status == 200) {

						var codes2 = req2.responseText;
						var a = codes2.split("^");
						var codes = a[0];
						positionsArray = codes.split("+");
						selectedPos = new YAHOO.widget.DS_JSArray(
								positionsArray);
					}
				}
			};
			req2.open("GET", url, true);
			req2.send(null);
		} else {

			return false;
		}

	}
	function autocompleteForPosition(obj, event) {
		// set position of dropdown

		var src = obj;
		var target = document.getElementById('position');
		var posSrc = findPos(src);
		target.style.left = posSrc[0];

		target.style.top = posSrc[1] + 25;

		if (obj.name == 'positionName')
			target.style.left = posSrc[0] + 0;
		target.style.width = 500;
		var currRow = getRow(obj);
		var coaCodeObj = obj;
		if (yuiflag1[currRow.rowIndex] == undefined) {
			//40 --> Down arrow, 38 --> Up arrow
			if (event.keyCode != 40) {

				if (event.keyCode != 38) {
					oAutoCompPos = new YAHOO.widget.AutoComplete(coaCodeObj,
							'position', selectedPos);
					oAutoCompPos.queryDelay = 0;
					oAutoCompPos.useShadow = true;
					oAutoCompPos.maxResultsDisplayed = 20;
					oAutoCompPos.useIFrame = true;
				}

			}
			yuiflag1[currRow.rowIndex] = 1;

		}
	}

	function fillPositionAfterSplit(obj, neibrObjName) {

		var currRow = getRow(obj);
		yuiflag1[currRow.rowIndex] = undefined;
		neibrObj = getControlInBranch(currRow, neibrObjName);
		//neibrObj=document.getElementsByName(neibrObjName)[currRow.rowIndex-1];
		var temp = obj.value;
		temp = temp.split("`-`");
		if (temp[1] != '' && temp[1] != undefined) {
			neibrObj.value = temp[1];
		}
		else{
			neibrObj.value = "";
			}
		obj.value = temp[0];

	}

	function clearPos(){

		if (document.getElementById("positionName").value != "") {
			document.getElementById("positionName").value = "";
		}
	}

	function checkOnSubmit() {

		if (document.getElementById("departmentId").value == "") {
			showError('Please select department');
			return false;
		}
		if (document.getElementById("designationName").value == "") {
			showError('Please select designation');
			return false;
		}

		if (document.getElementById("givenDate").value == "") {
			showError('Please enter the date');
			return false;
		}
		if (document.getElementById("positionName").value == "") {
			document.getElementById("posId").value = "";
		}
		if(document.getElementById("desgEnterId").value == "" && document.getElementById("designationName").value != ""){
			showError('Please enter valid designation');
			document.getElementById("designationName").value="";
			return false;
			}
		if(document.getElementById("posId").value == "" && document.getElementById("positionName").value != ""){
			showError('Please enter valid position');
			document.getElementById("positionName").value="";
			return false;
			}

		return true;
	}

	function showError(msg) {
		document.getElementById("show_error").style.display = 'none';
		if (document.getElementById("fieldError") != null)
			document.getElementById("fieldError").style.display = 'none';
		dom.get("show_error").style.display = '';
		document.getElementById("show_error").innerHTML = msg;
	}
</script>
</head>

<body onload="loadDesignation();">
	<div class="mandatory" id="show_error" style="display: none;"></div>

	<div class="formmainbox">
		<div class="insidecontent">
			<div class="rbroundbox2">
				<div class="rbtop2">
					<div></div>
				</div>

				<div></div>
				<s:form theme="simple">
					<center>
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tbody>
								<tr>
									<td colspan="8" class="headingwk">
										<div class="arrowiconwk">
											<img src="../common/image/arrow.gif" />
										</div>
										<p>
										<div class="headplacer">Search Employees</div>
										</p>
									</td>
								</tr>
							</tbody>
						</table>
					</center>

					<table border="0" width="95%" cellpadding="0" cellspacing="0">

						<tbody>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="whiteboxwk"><span class="mandatory">*</span>
								<s:text name="department.name" /></td>
								<td class="whitebox2wk"><s:select name="departmentId"
										id="departmentId" list="dropdownData.departmentList"
										listKey="id" listValue="deptName" headerKey=""
										headerValue="------Select------" /></td>
										
								<td class="whiteboxwk"><span class="mandatory">*</span>
								<s:text name="designation.name" /></td>
								<td class="whitebox2wk"><div id="designation"></div> <s:textfield
										cssClass="selectwk" id="designationName"
										name="designationName" value="%{designationName}"
										onblur="fillDesgAfterSplit(this,'desgEnterId');clearPos();"
										onkeyup="autocompleteForDesignation(this,event);" /> <s:hidden
										type="hidden" name="desgEnterId" id="desgEnterId"></s:hidden>

								</td>
							</tr>

							<tr>
								<td class="greyboxwk"><span class="mandatory">*</span>
								<s:text name="valid.date" /></td>
								<td class="greybox2wk"><s:date name="givenDate"
										var="givenDateValue" format="dd/MM/yyyy" /> <s:textfield
										name="givenDate" id="givenDate" value="%{givenDateValue}"
										onkeyup="DateFormat(this,this.value,event,false,'3')"
										onblur="validateDateFormat(this);"/></td>

								<td class="greyboxwk"><s:text name="position.name"></s:text></td>
								<td class="greybox2wk"><div id="position"></div>
									<div>
										<s:textfield cssClass="selectwk" name="positionName"
											id="positionName" value="%{positionName}"
											onblur="fillPositionAfterSplit(this,'posId');"
											onkeyup="autocompleteForPosition(this,event);"
											onmouseover="loadPositions();" />
										<s:hidden type="hidden" name="posId" id="posId"
											value="%{posId}" />
									</div></td>
							</tr>


							<tr>
								<td colspan="4"><div align="right" class="mandatory">*
										Mandatory Fields</div></td>
							</tr>
						</tbody>
					</table>



					<table align="center">
						<tr>&nbsp;
						</tr>

						<tr>
							<td colspan="4" align="center"><s:submit value="SUBMIT"
									cssClass="buttonfinal" onclick="return checkOnSubmit();"
									method="execute" /> <input type="button" name="close"
								id="close" value="CLOSE" onclick="window.close();"
								class="buttonfinal" /></td>
						</tr>

					</table>
				</s:form>
			</div>

			<br>
			<s:if test="positionsList != null">
				<br>
				<table align="center" width="100%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td class="headingwk" colspan="5">
							<div class="arrowiconwk">
								<img src="../common/image/arrow.gif" />
							</div>
							<div align="left" style="margin-top: 4px;">Position Details
								Report</div>
						</td>
					</tr>
				</table>
				<display:table name="positionsList" id="currentRowObject"
					requestURI="" style="width: 750;" export="true" defaultsort="2" 
					pagesize="20" sort="list" class="its">

					<br>
					<display:caption style="text-align: center">
								Positions Detail Report for the Department: <s:property value="deptName" /> and Designation:
							<s:property value="%{designationName}" />
					</display:caption>
					
					<br>
					<display:column title="Sl No">
						<s:property
							value="%{#attr.currentRowObject_rowNum + (page-1)*pageSize}" />
					</display:column>

					<display:column title="Position">
						<c:out value="${currentRowObject[0]}" />
					</display:column>


					<display:column title="Employee Code">
						<c:if test="${currentRowObject[1]!= null}">
							<c:out value="${currentRowObject[1]}" />
						</c:if>
						<c:if test="${currentRowObject[1]== null}">
							<c:out value="-" />
						</c:if>
					</display:column>

					<display:column title="Employee Name">
						<c:if test="${currentRowObject[2]!= null}">
							<c:out value="${currentRowObject[2]}" />
						</c:if>
						<c:if test="${currentRowObject[2]== null}">
							<c:out value="-" />
						</c:if>

					</display:column>
					<display:column title="From Date">
						<fmt:formatDate value="${currentRowObject[3]}"
							pattern="dd/MM/yyyy" var="fromdatevar" />
						<c:out value="${fromdatevar}" />
					</display:column>

					<display:column title="To Date">
						<fmt:formatDate value="${currentRowObject[4]}"
							pattern="dd/MM/yyyy" var="todatevar" />
						<c:out value="${todatevar}" />
					</display:column>

					<display:column title="Primary">
						<c:if test="${currentRowObject[5]=='Y'}">
							<c:out value="Y" />
						</c:if>
					</display:column>

					<display:column title="Temporary">
						<c:if test="${currentRowObject[5]=='N'}">
							<c:out value="Y" />
						</c:if>
					</display:column>


					<div STYLE="display: table-header-group;">
					<display:setProperty name="basic.show.header" value="true" />
						<display:setProperty name="export.pdf.filename"      
							value="positionsDetailReport.pdf" />
						<display:setProperty name="export.excel.filename"
							value="positionsDetailReport.xls" />
						<display:setProperty name="export.csv" value="false" />
						<display:setProperty name="export.xml" value="false" />

					</div>

				</display:table>
			</s:if>

		</div>
	</div>
</body>
</html>
