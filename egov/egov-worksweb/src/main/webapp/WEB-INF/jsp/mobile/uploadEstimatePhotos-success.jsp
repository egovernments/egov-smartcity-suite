<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<!DOCTYPE html>
<html>
<head>
<title>Upload Successful</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">  
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">   
<link rel="stylesheet" href="../css/jquerymobile/jquery.mobile-1.3.1.min.css" />
<script src="../js/jquery-1.7.2.min.js"></script>
<script src="../js/jquerymobile/jquery.mobile-1.3.1.min.js"></script>
<style>
@media screen and (min-width: 20em) {
	   .my-custom-class th.ui-table-priority-1,
	   .my-custom-class td.ui-table-priority-1 {
	     display: table-cell;
	   }
	}
	/* Show priority 2 at 480px (30em x 16px) */
	@media screen and (min-width: 30em) {
	   .my-custom-class  th.ui-table-priority-2,
	   .my-custom-class td.ui-table-priority-2 {
	     display: table-cell;
	   }
	}
</style>
</head>
<body >
	<s:form action="uploadEstimatePhotos!search.action" enctype="multipart/form-data" theme="simple" name="tablet" id="tablet" data-ajax="false">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>
		<div id="uploadfiles" data-role="page" data-add-back-btn="false" class="pageclass">
			<div data-theme="b" data-role="header" data-position="fixed">
				<h5>Upload Successful</h5>
			</div>
			<fieldset class="ui-grid-a"  align="center" >
				<div>
					<img src="../css/jquerymobile/images/greetick.png"  style="height: 24px;width: 24px" />&nbsp;&nbsp;&nbsp;<s:property value="successMessage" />
				</div>
				<div class="ui-block-a">
					<button type="submit" data-mini="true" id="submit" data-theme="b">Search</button>
				</div>
				<div class="ui-block-b">
					<button type="button" data-mini="true" id="cancel" onclick="window.close();" data-theme="d">Close</button>
				</div>
			</fieldset>
		</div>
	</s:form>
</body>
</html>