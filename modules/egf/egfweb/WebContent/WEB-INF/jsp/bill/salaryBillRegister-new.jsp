<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<html>  
<head>  
    <title><s:text name="bill.salarybill.register"/></title>
    <link href="common/css/budget.css" rel="stylesheet" type="text/css" />
	<link href="common/css/commonegov.css" rel="stylesheet" type="text/css" />
	<style type="text/css">
	#codescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#codescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:20000;}
	#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:19999;}
	.yui-skin-sam .yui-ac-input{width:100%;}
	#codescontainer ul {padding:5px 0;width:100%;}
	#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#codescontainer li.yui-ac-highlight {background:#ff0;}
	#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
	.yui-skin-sam tr.yui-dt-odd{background-color:#FFF;}

	#detailcodescontainer {position:absolute;left:11em;width:9%;text-align: left;}
	#detailcodescontainer .yui-ac-content {position:absolute;width:350px;border:1px solid #404040;background:#fff;overflow:hidden;z-index:20000;}
	#detailcodescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:300px;background:#a0a0a0;z-index:19999;}
	#detailcodescontainer ul {padding:5px 0;width:100%;}
	#detailcodescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#detailcodescontainer li.yui-ac-highlight {background:#ff0;}
	#detailcodescontainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/payment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
	<SCRIPT type="text/javascript">
	function onLoadTask(){
		var close = '<s:property value="close"/>';
		var message = '<s:property value="message"/>';
		if(message!=""){
			alert(message);
		}
		if(close=='true'){
			window.close();
		}
	}
	</SCRIPT>
</head> 
	<body onload="onLoadTask();loadDropDownCodesFunction();">  
		<s:form action="salaryBillRegister" theme="simple" name="salaryBill">  
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="bill.salarybill.register"/></div>
				<%@ include file='salaryBillRegister-form.jsp'%>
				<div class="buttonbottom">
					<s:submit method="saveAndNew" type="submit" cssClass="buttonsubmit" id="saveAndNew" name="saveAndNew" value="Save & New" onclick="return validate();"/>
					<s:submit method="saveAndClose" type="submit" cssClass="buttonsubmit" id="button" value="Save & Close" />
					<input type="submit" name="button3" id="button3" value="Cancel" class="button" />
					<input type="submit" name="button2" id="button2" value="Close" class="button" />
				</div>
			</div>
		</s:form>  
	</body>  
</html>
