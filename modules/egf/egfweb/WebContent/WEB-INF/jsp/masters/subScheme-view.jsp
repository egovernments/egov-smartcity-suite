<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>
	<head>
		<title><s:if test="%{showMode=='edit'}">
				<s:text name="subscheme.modify" />
			</s:if> <s:else>
				<s:text name="subScheme.add" />
			</s:else></title>
		<script type="text/javascript">
		function validate(){
			if(document.getElementById('name').value == null || document.getElementById('name').value==''){
				alert("Please enter Name");
				return false;
			}
			if(document.getElementById('code').value == null || document.getElementById('code').value==''){
				alert("Please enter Code");
				return false;
			}
			if(document.getElementById('validfrom').value == null || document.getElementById('validfrom').value==''){
				alert("Please enter Valid From Date");
				return false;
			}
			if(document.getElementById('validto').value == null || document.getElementById('validto').value==''){
				alert("Please enter Valid To Date");
				return false;
			}
			if(isNaN(document.getElementById('initialEstimateAmount').value)){
				alert("Please enter valid Initial Eastimate Amount");
				return false;
			}
			return true;
		}
		
	function disableControls(isDisable) {
		for ( var i = 0; i < document.subSchemeForm.length; i++)
			document.subSchemeForm.elements[i].disabled = isDisable;
		document.getElementById('Close').disabled = false;
		var id; 
		var obj;
		
		// Calendar ids are of the form calendar1,calendar2 etc.
			for (i = 0; i <= 5; i++) {
				id = "calendar" + i;
				obj=document.getElementById(id);
				if(isDisable)
					obj.removeAttribute('href');
			}

	}	
</script>

	</head>
	<body name="subSchemeView">

		<jsp:include page="../budget/budgetHeader.jsp" />
		<s:actionmessage theme="simple" />
		<div class="formmainbox">
			<div class="subheadnew">
				<s:if test="%{showMode=='edit'}">
					<s:text name="subscheme.modify" />
				</s:if>
				<s:else>
					<s:text name="subScheme.add" />
				</s:else>
			</div>
		</div>
		<s:actionerror />
		<s:fielderror />
		<s:form name="subSchemeForm" action="subScheme"  theme="simple">
			<s:hidden name="showMode" />
			<s:hidden name="id" />
		<s:push value="model">
				<%@include file="subScheme-form.jsp"%>
				<div class="buttonbottom" style="padding-bottom: 10px;">
					<s:if test="%{showMode=='edit'}">
						<s:submit name="Save" value="Save" method="save"
							onclick="javascript: return validate();" cssClass="buttonsubmit" />
					</s:if>
					<input type="button" id="Close" value="Close"
						onclick="javascript:window.close()" class="button" />
				</div>
		<script>
			<s:if test="%{isactive==true}">
			    document.getElementById("isActive").checked="checked";
			</s:if>
			<s:if test="%{showMode=='edit'}">
			 	disableControls(false);
			 	<s:if test="%{clearValues==true}">
			 		alert('<s:text name="subscheme.saved.successfully" />');
			 		window.close();
			 	</s:if>	
			</s:if>
			<s:else>
				disableControls(true);
			</s:else>
		</script>
			</s:push>
			<s:token/>
		</s:form>
		<script>

	</script>
	</body>
</html>