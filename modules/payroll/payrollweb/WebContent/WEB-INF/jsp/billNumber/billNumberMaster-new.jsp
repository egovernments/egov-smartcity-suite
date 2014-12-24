<%@ include file="/includes/taglibs.jsp" %>
<html>
	<head>
		<title>
			<s:text name="billNoMaster"/>
		</title>
	</head>
<body>
	<s:form name="billNumberMaster" theme="simple">
	<s:actionerror/>
	<table width="98%" border="0"  cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="4" class="headingwk" >
				<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
            	<div class="headplacer"><s:text name="billNoMaster"/></div>
			</td>
        </tr>
        <tr>
        	<td class="whiteboxwk" width="15%">&nbsp;</td>
			<td class="whiteboxwk" width="20%"><span class="mandatory">*</span>
				<s:text name="dept">:</s:text>
			</td>
			<td class="whiteboxwk" width="20%" >
				<s:select list="dropdownData.deptList" headerKey="-1" headerValue="%{getText('default.select')}"
					listKey="id" listValue="deptName" name="deptId" value="%{deptId}" ></s:select>
			</td>
			<td class="whiteboxwk">&nbsp;</td>
       	</tr>
       	<tr>
       		<td width="15%">&nbsp;</td>
			<td>&nbsp;</td>
			<td>
				<s:submit name="Create/Modify" value="Create/Modify" method="createForm" cssClass="buttonfinal"></s:submit>
				<s:submit name="View" value="View" method="view" cssClass="buttonfinal"></s:submit>
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</s:form>
</body>
</html>