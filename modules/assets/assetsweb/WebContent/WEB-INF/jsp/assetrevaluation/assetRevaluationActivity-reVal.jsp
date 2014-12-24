<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">  
  <style type="text/css">

#yui-dt0-bodytable,#yui-dt1-bodytable,#yui-dt2-bodytable {
	Width: 100%;
}

</style>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() +"/build/button/assets/button.css" %>" type="text/css">
<script src="<egov:url path='js/assetRevaluation.js'/>"></script>	

<html>
	<head> <title>- <s:text name="page.title.assset.revaluation" /></title></head> 
	<body id="home" >

		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<div id="jserrorid" class="errorstyle" style="display:none" >
			<p class="error-block" id="lblError1" ></p>
		</div>
		<s:if test="%{hasActionMessages()}">
			<font  style='color: green ; font-weight:bold '> 
     					<s:actionmessage/>
   				</font>
		</s:if>
		
<s:form action="assetRevaluationActivity" theme="simple" name="assetRevalActForm">
			<div class="errorstyle" id="asset_error" style="display:none;"></div>
			<s:push value="model">
				<div class="navibarshadowwk">
				</div>
				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2"><div></div>
							</div>
							<div class="rbcontent2">
								<div class="datewk">
									<span class="bold">Today</span>
									<egov:now />
								</div>
								
								<jsp:include page="assetRevaluationActivity-form.jsp" />			
						<table align="center" width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4" class="headingwk">
						<div class="arrowiconwk">
							<img src="${pageContext.request.contextPath}/image/arrow.gif" />
						</div>
					<div class="headplacer">
						<s:text name='asset.approver.detail' />
					</div>
					</td>
				</tr>
			</table>
			<div align="center">
				<table border="0" width="100%">
				<tr>
					<td  class="bluebox">Comments</td> 
					<td  class="bluebox" ><s:textarea name="comments" id="comments" cols="150" rows="3"/></td>
				</tr>
			<br/>
			</table>
		</div>
			<table width="100%">
				<tr>
					<td colspan="4" class="shadowwk"></td>
				</tr>
				<tr>
            				<td colspan="4"><div align="right" class="mandatory" style="font-size:11px;padding-right:20px;">* <s:text 						name="default.message.mandatory" />
            				</div>
					</td>
          			</tr>
			</table>
			<table align="center">
			
				<tr>
					<td >
						<div class="buttonholderwk">
						<s:submit cssClass="buttonfinal" value="SAVE" id="save" method="create" onClick="return validate();"/>
						<input type="reset" id="Reset" value="CANCEL" class="buttonfinal"/>
						<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="button" onclick="window.close();" />
						</div>
					</td>
				</tr>
			</table>
			
			</div>
			<div class="rbbot2">
			<div></div>
			</div>
		</div>
	</div>
</div>
				
<s:hidden name="asset.id" id="assetid" />
<s:hidden name="actionName" id="actionName"></s:hidden>					
</s:push>
</s:form>
<script>
	function validate(){
		document.getElementById('actionName').value = "approve";
		document.getElementById('lblError1').innerHTML = "";
		var typOfReval = document.getElementById('typeOfChange').value;
		var revalamt = document.getElementById('revalAmt').value;
		if(typOfReval == -1 ){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError1').innerHTML = "Please Select Type of change";
			return false;
		}
		else if(typOfReval == "Decrease" && document.getElementById('wrtnOffAccCode').value == -1){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError1').innerHTML = "Please Select fixed assets written off account code";
			return false;
		}
		else if(revalamt == "" ){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError1').innerHTML = "Please enter revaluation amount";
			return false;
		}else if(parseFloat(revalamt) <= 0){
			document.getElementById('jserrorid').style.display='block';
			document.getElementById('lblError1').innerHTML = "Revaluation amount shoud be greater than zero";
			return false;
		}else{
			var msg = validateMIS();
			if(msg != "") {
				document.getElementById('jserrorid').style.display='block';
				document.getElementById('lblError1').innerHTML =msg;
				return false;
			}
		}
		
	}


</script>
</body>
</html>