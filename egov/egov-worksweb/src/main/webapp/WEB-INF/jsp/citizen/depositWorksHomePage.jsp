<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/includes/taglibs.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
 <%
	Float lat = 0.0f;
	Float lng = 0.0f;
	String userName = "";
	String imgLeftLogo ="";
	String cityurl = "defaulturl";
	String cityUrl = "defaulturl";
	String toplevelbndryName = "";
	String cityNameHeading = "City Corporation";
	Map homePageInfoMap = null;	
	if(session.getAttribute("cityurl")!=null){
		cityurl=(String)session.getAttribute("cityurl");		
	}	
	if(cityurl != null && !cityurl.trim().equals("")){
		cityUrl="http://www."+cityurl+".gov.in";	
	}
	
	if(request.getAttribute("homePageInfoMap") != null){		
		homePageInfoMap = (Map)request.getAttribute("homePageInfoMap"); 
		if(homePageInfoMap!=null){ 
			userName = (String)homePageInfoMap.get("userName"); 		
			imgLeftLogo = (String)homePageInfoMap.get("imgLeftLogo"); 		
			
			if(homePageInfoMap.get("toplevelbndryName") != null){
				toplevelbndryName=(String)homePageInfoMap.get("toplevelbndryName"); 	
			}
			
			if(homePageInfoMap.get("cityNameHeading") != null){
				cityNameHeading = (String)homePageInfoMap.get("cityNameHeading");
			}
			
		} 
	}
	
%> 
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<style>
			#loadingMask {position:absolute; left:0; top:0;width:100%;height:100%; z-index:20000;background-color:#FFF; }
			#loading{ position:absolute; left:45%; top:40%; padding:2px; z-index:20001; height:auto;width:500px; }
		    #loading a { color:#225588; }
		    #loading .loading-indicator{  background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto; }
		    #loading-msg { font: normal 10px arial,tahoma,sans-serif;height:auto; }		    
		</style>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>eGov <s:text name='depositworks.header.message' /> </title>
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo/yahoo-min.js"></script> 
		<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script> 
		<script type="text/javascript" src="/egi/commonyui/yui2.7/event/event-min.js"></script>  
		<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script> 
		<script type="text/javascript" src="/egi/commonyui/yui2.7/container/container_core-min.js"></script>	 
		<script type="text/javascript" src="/egi/commonyui/yui2.7/menu/menu-min.js"></script>
		<script type="text/javascript" src="/egi/html/common/accordion/script.js"></script>
		
		<script type="text/javascript" src="<egov:url path='/js/works.js'/>"></script>
		<script type="text/javascript" src="<egov:url path='/js/helper.js'/>"></script>
		<link href="/egi/commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css"></link>
		<link href="/egi/html/common/accordion/style.css" rel="stylesheet" type="text/css" />
		<link href="/egi/html/common/css/portal.css" rel="stylesheet" type="text/css" />
				
	</head>
	<script>

	var windows = new Array();
	var winCnt = new Array();
	var gblWinCnt = 0;
	function PopupCenter(pageURL, title, w, h) {
		var left = (screen.width / 2) - (w / 2);
		var top = (screen.height / 2) - (h / 2);
		if (windows[title] && !windows[title].closed) {
			windows[title].focus();
		} else {
			winCnt[gblWinCnt++] = windows[title] = window.open(pageURL, title, "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=" + w + ", height=" + h + ", top=" + top + ", left=" + left);
		}
	}
	
	function PopupNewWindow(pageURL, title, w, h) {
		var load = window.open(pageURL, "", "scrollbars=yes,menubar=yes,height=500,width=600,resizable=yes,toolbar=yes,location=no,status=yes,alwaysLowered=yes");
	}

	function closeChildWindows() {
		for (var i = 0; i < winCnt.length; i++) {
			try {
				winCnt[i].close();
			}
			catch (e) {
			}
		}		
	}
	</script>
	
	<body class="yui-skin-sam">
		<form name="dashboard" id="form">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" >
				<tr>
					<td>
						<div class="commontopyellowbg" style="height: 80px;">
							<div class="logochennai">
								<a href="javascript:void(0);" onclick="PopupCenter('<%=cityUrl%>','<%=toplevelbndryName%>',850,600);">
									<img src="<%=imgLeftLogo%>" align="left"  width="60" height="58" border="0" />
								</a>
							</div>
							<div class="mainlogo">
								<a href="javascript:void(0);" onclick="PopupCenter('http://www.egovernments.org','eGovernments',850,600);">
									<img src="<c:url value="/images/egov-logo.gif"/>"  width="60" height="58" border="0" /> 
								</a>
							</div>
							<div class="mainheadingnew" style="color:black"> 
								<font size="5px" id="header_font"> 
								<s:text name='depositworks.header.message' /> 
								</font>
								</br> 
								<div style="margin-top:10px;" align="center">
								<font size="4px" >		
								<s:text name='depositworks.citizeninterface.message' />  
								</font>
								</div>
							</div>
						</div>					 			
						<div class="navibarportal">																		
							<div> 
								<div style="margin-top:2px;position:absolute;">
								<span class="fonthome" onclick="" id="homebtn">
								<a href="#"  class="fonthome noline">Home</a> &nbsp;&nbsp;<img  src="/egi/images/dline.gif" 
								style="cursor:default;">&nbsp;&nbsp;</span><span class="fonthome" style=";display: inline;"> 
								<font style="font-weight:lighter">&nbsp;Welcome</font>&nbsp;<%=userName%> </span>
								</div>  
								<div align="right"> 
								<span>
								<a href="javascript:void(0);" onclick="javascript:closeChildWindows();window.location='/portal/modifyRegistration/modifyRegistration!viewOrEdit.action';" target="_parent" class="fonthome noline">Modify Account</a>&nbsp;&nbsp;
								<a href="javascript:void(0);" onclick="javascript:closeChildWindows();window.location='/portal/modifyRegistration/modifyRegistration!changePassword.action';" target="_parent" class="fonthome noline">Change Password</a>&nbsp;&nbsp;
								<a href="javascript:void(0);" onclick="javascript:closeChildWindows();window.location='/portal/logout.jsp';" target="_parent" class="fonthome noline">Log out</a>&nbsp;&nbsp;
								<img  src="/egi/images/dline.gif" style="cursor:default;">&nbsp;&nbsp;&nbsp;
								<s:text name="message.today" />
								</span> <egov:now/></div>				
								 
							</div> 
						</div>
						<div class="formmainboxnew"> 
							<div style="position:absolute; vertical-align:top;">   
							<span><img src="/egi/images/hide.gif" onclick="showSidebar(this)" title="Hide" class="showhide" border="0"></img></span>
							</div>	
							<table id="portaltable" width="100%" border="0" cellspacing="0" cellpadding="0" >							
								<tr>
									<td  id="sidebar" width="265px" bgcolor="#93C8E9" valign="top" height="480px">
										<div  class="rightpersonalboxplain">																			
											
											<div id="accordion4" class="accordiondiv">											
												<div class="headleftnavi" class="pointer"><img src="/egi/images/application.gif" style="margin-right:6px"/>Deposit Works</div>
												<dl class="accordion" id="slider4">
												 	<dt style="background-image: none" onclick="window.open('${pageContext.request.contextPath}/citizen/depositWorks!newform.action?citizenId='+'<s:property value="citizenId"/>', '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes')" >
														 <a name='applyRoadCut'> <s:text name='depositworks.menu.applyroadcut' /></a> 
													</dt>
													
													<dt style="background-image: none" onclick="window.open('${pageContext.request.contextPath}/citizen/depositWorks!search.action?citizenId='+'<s:property value="citizenId"/>', '','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes')" >
														 <a name='searchApplicationRequest'> <s:text name='depositworks.menu.searchapplication' /> </a>  
													</dt>
																									 	
																								
												</dl>
											</div>
											<br />												
										</div>
									</td>
									<td valign="top" style="padding-left:8px;text-align: center;" class="headingwk">
									<br/>
									<div>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr><td><%@ include file='depositWorksHomePage-searchresults.jsp'%></td></tr>
										</table>
									</div>
									</td>
								</tr>
							</table>
						</div>
						<div class="footer" align="center" >
							<span class="egovfooter"><a href="http://www.egovernments.org/" style="font-size:11px;">eGovernments Foundation</a></span>
						</div>
						
					</td>
				</tr>
			</table>						
		</form>	
		
	</body>
</html>