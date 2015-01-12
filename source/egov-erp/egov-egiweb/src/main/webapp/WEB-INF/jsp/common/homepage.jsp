<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/includes/taglibs.jsp"%>
<%@page import="org.egov.infstr.commons.Module,org.egov.infstr.models.Favourites"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>

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
	List moduleBeanList = null;
	List selfServiceList = null;
	List favouriteList  = null;
	Module module = null;	

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
			if(homePageInfoMap.get("lng") != null){ 
				lng = (Float) homePageInfoMap.get("lng"); 
			}
			if(homePageInfoMap.get("lat") != null){
			 	lat = (Float)homePageInfoMap.get("lat"); 	
			}
			if(homePageInfoMap.get("toplevelbndryName") != null){
				toplevelbndryName=(String)homePageInfoMap.get("toplevelbndryName"); 	
			}
			
			if(homePageInfoMap.get("cityNameHeading") != null){
				cityNameHeading = (String)homePageInfoMap.get("cityNameHeading");
			}
						
			moduleBeanList = (List) homePageInfoMap.get("moduleBeanList");
			favouriteList  = (List) homePageInfoMap.get("favouriteList");
			selfServiceList = (List) homePageInfoMap.get("selfServiceList");
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
		<title>eGov Urban Portal</title>
		<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css"></link>
		<link href="../html/common/accordion/style.css" rel="stylesheet" type="text/css" />
		<link href="../html/common/css/portal.css" rel="stylesheet" type="text/css" />
		<script>
			var _portalTime = null;
			var moduleNames = new Array();
			var baseUrls = new Array ();
			function finishLoading() {
				_portalTime = window.setInterval(finishLoadings, 1);
			}
			function finishLoadings() {
				if (!document.getElementById("inboxframe").contentWindow.document.readyState || document.getElementById("inboxframe").contentWindow.document.readyState == "complete") {
					window.clearInterval(_portalTime);
					document.getElementById("loading").style.display = "none";
					document.getElementById("loadingMask").style.display = "none";
				}
			}
		</script>				
	</head>
	<!-- Uncomment when google map up 
	<body onload="load(),onloadctrmap(lat,lng),loadStates();" onunload="GUnload();" onresize="resizeMapDiv()">
	 -->
	<body onload="loadImage();MM_preloadImages()" class="yui-skin-sam">
		<div id="loadingMask"></div>
		<div id="loading">
		    <div class="loading-indicator"><img src="../images/loading.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/> eGov Urban Portal <br /><span id="loading-msg">Loading styles and images...</span></div>
		</div>
		<form name="dashboard" id="form">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" >
				<tr>
					<td>
						<div class="topbar" style="height: 60px;">
							<div class="logochennai">
								<a href="javascript:void(0);" onclick="PopupNewWindow('<%=cityUrl%>','<%=toplevelbndryName%>',850,600);">
									<img src="<%=imgLeftLogo%>" align="left"  width="53" height="49" border="0" />
								</a>
							</div>
							<div class="mainlogo">
								<a href="javascript:void(0);" onclick="PopupCenter('http://www.egovernments.org','eGovernments',850,600);">
									<img src="<c:url value="/images/egov-logo.gif"/>"  width="53" height="49" border="0" /> 
								</a>
							</div>
							<div class="mainheadingnew">
								<font size="6px" id="header_font"><%=cityNameHeading%>
								</font>
							</div>
						</div>								
						<div class="navibarportal">																		
							<div>
								<div style="margin-top:2px;width:400px;position:absolute;"><span class="fonthome"  onclick="showMainMenu()" style="margin-left:5px;display:none;cursor:pointer" id="homebtn">Home &nbsp;&nbsp;<img  src="../images/dline.gif" style="cursor:default;">&nbsp;&nbsp;</span><span class="fonthome" style=";display: inline;"><font style="font-weight:lighter">&nbsp;Welcome</font>&nbsp; <%=Character.toTitleCase(userName.charAt(0))+ userName.substring(1,userName.length())%></span></div>				
								<div class="signoutbox" style="position:relative;">
									<li style="display:inline;">
									<a href="javascript:void(0);"  onmouseover="showHelpMenu(true);" class="fonthome noline">Help&nbsp;&nbsp;</a><img onmouseover="showHelpMenu(true);" src="../images/arrowdown.gif" style="cursor:pointer;top:2px;position:relative"/>
									<ul class="helpmenu" id="helpmenu" onmouseout="showHelpMenu(false)">									
									</ul>
									</li>&nbsp;&nbsp;<img  src="../images/dline.gif" style="cursor:default;">&nbsp;&nbsp;
									<a href="javascript:void(0);" onclick="PopupNewWindow('/egi/admin/directChgPassword.do?actionid=24','Change Password',850,600);" class="fonthome noline">Change Password</a> &nbsp;&nbsp;<img  src="../images/dline.gif" style="cursor:default;">&nbsp;&nbsp;
									<a href="javascript:void(0);" onclick="showFeedback(true);" class="fonthome noline">Feedback</a>&nbsp;&nbsp;<img  src="../images/dline.gif" style="cursor:default;">&nbsp;&nbsp;
									<a href="javascript:void(0);" onclick="javascript:closeChildWindows();window.location='<%=request.getContextPath()%>/logout.do';" target="_parent" class="fonthome noline">Sign out</a>
								</div>
							</div>
						</div>
						<div class="formmainboxnew">
							<img src="../images/hide.gif" onclick="showSidebar(this)" title="Hide" class="showhide" border="0"></img>									
							<table id="portaltable" width="100%" border="0" cellspacing="0" cellpadding="0" >
								<tr>
									<td  id="sidebar" width="265px" bgcolor="#93C8E9" valign="top" height="480px">
										<div  class="rightpersonalboxplain">
											<div id="accordion1" >
												<dl class="accordion" id="slider1">
													<dt style="background-image: none;background-color: #f0f0f0;border:1px solid #CCC;color:#416a9c" id="inboxbtn" onclick="showInbox(this)" onmouseover="this.style.backgroundColor='#92b4d5'" onmouseout="this.style.backgroundColor='#f0f0f0'"><img src="../images/email.gif" border="0"/>&nbsp;&nbsp;Inbox</dt>
													<dt style="background-image: none;background-color: #f0f0f0;border:1px solid #CCC;border-top:0px;color:#416a9c" id="draftbtn" onclick="showInbox(this)" onmouseover="this.style.backgroundColor='#92b4d5'" onmouseout="this.style.backgroundColor='#f0f0f0'"><img src="../images/draft.png" />&nbsp;&nbsp;Drafts</dt>
													<dt style="background-image: none;background-color: #f0f0f0;border:1px solid #CCC;border-top:0px;color:#416a9c" id="notificationbtn" onclick="showInbox(this)" onmouseover="this.style.backgroundColor='#92b4d5'" onmouseout="this.style.backgroundColor='#f0f0f0'"><img src="../images/new-file.png" border="0" width="18" height="18"/>&nbsp;&nbsp;Notifications</dt>
												</dl>
											</div>
											<% if(selfServiceList != null && selfServiceList.size() > 0 ) { %>
											<br />
											<div id="accordion5" class="accordiondiv">
												<dl class="accordion" id="slider5">
													<dt style= "background-color:#416a9c;font-size:14px;font-family: Arial;font-weight: bold;color: #CCC;" ><img src="../images/selfservice.png" style="margin-right:6px" height="16" width="16"/><%=request.getAttribute("selfServiceHeader") %></dt>
													<dd id="selfservice">
													<span id="selfservicespan">
														<% 
														   	for(int i=0; i<selfServiceList.size();i++){  
												         		Module selfService = (Module)selfServiceList.get(i);
												         		String url = "/"+selfService.getContextRoot()+selfService.getBaseUrl(); 
												         		%>
																<a id='ss#<%=selfService.getId()%>'  name='fave' class="buttonforaccord lineheight"  href='javascript:void(0);' onclick= "PopupCenter('<%=url%>','portalApp<%=selfService.getId()%>', 850,600)"><%=selfService.getModuleName() %></a>
																<br/>      		
												         		<%
												         	}
														
														%>
														</span> 												
													</dd>
												</dl>
											</div>												
											<% } %>
											<br />
											<div id="accordion2" class="accordiondiv">
												<dl class="accordion" id="slider2">
													<dt style= "background-color:#416a9c;font-size:14px;font-family: Arial;font-weight: bold;color: #CCC;" id="favdt"><img src="../images/fav.gif" style="margin-right:6px"/>Favourites</dt>
													<dd id="favourte">
														<span id="favourtespan">
														<% 
														if(favouriteList!=null && favouriteList.size()>0){
												         	for(int i=0; i<favouriteList.size();i++){  
												         		Module favourites = (Module)favouriteList.get(i);
												         		String url = "/"+favourites.getBaseUrl();
												         		%>
																<a id='fav#<%=favourites.getId()%>'  name='fave' class="buttonforaccord lineheight"  href='javascript:void(0);' onclick= "PopupCenter('<%=url%>','portalApp<%=favourites.getId()%>', 850,600)"><%=favourites.getModuleName() %></a>
																<br/>      		
												         		<%
												         	}
														}
														%>
														</span> 
													</dd>
												</dl>
											</div>
											<br/>											
											<div id="accordion3" class="accordiondiv" style="display:none">
												<div class="headleftnavi pointer" title="Back to Applications" id="accordion4head" onclick='showMainMenu()'></div>
												<dl class="accordion" id="slider3">
												</dl>
											</div>
											<div id="accordion4" class="accordiondiv">
												<div class="headleftnavi" class="pointer"><img src="../images/application.gif" style="margin-right:6px"/>Applications</div>
												<dl class="accordion" id="slider4">
												<%	 
												 String moduleName="";
												 String moduleDescription = "";
												 String baseUrl="";
												 String urlHref="";
												 String moduleId="0";											 
												 if(moduleBeanList!=null && moduleBeanList.size()>0){
											         	for(int i=0; i<moduleBeanList.size();i++){         	
											         		module = (Module) moduleBeanList.get(i);
										         			if(module != null){       
										         		  		if(module.getModuleName()!= null && !module.getModuleName().trim().equals("")){
										         					moduleName = module.getModuleName();
										         				}
										         		  		if(module.getModuleDescription() != null && !module.getModuleDescription().trim().equals("")) {
										         		  			moduleDescription = module.getModuleDescription();
										         		  		}
										         				if(module.getBaseUrl()!=null && !module.getBaseUrl().trim().equals("")){
																	baseUrl=module.getBaseUrl();
																	application.setAttribute(baseUrl, module.getModuleDescription());
																}
																if(module.getId()!=null){						
																	moduleId=module.getId().toString();
																}	
																															
																urlHref = "/"+baseUrl;
										         			}  
											         		
											         		if(moduleName != null && !moduleName.trim().equals("")) {
												 	%>
												 	<script>
												 	moduleNames['<%=i%>'] =  '<%=moduleDescription%>';
												 	baseUrls['<%=i%>'] =  '<%=baseUrl%>';
												 	</script>
												 	<dt style="background-image: none" onclick="prepareApplicationBar('<%=moduleId%>','<%=moduleDescription%>','<%=urlHref%>')"><%=moduleDescription%></dt>
													<%
											         		}
	 													}
													 }
												%>
												</dl>
											</div>
											<br />												
										</div>
									</td>
									<td valign="top" style="padding-left:8px">
										<iframe src="../workflow/inbox.action" onload="finishLoading()" id="inboxframe" style="min-height:480px;" name="inboxframe" width="100%"  height="480" scrolling="no" frameborder="0"></iframe>
									</td>
								</tr>
							</table>
						</div>
						<div class="footer" align="center" >
							<span class="egovfooter"><a href="http://www.egovernments.org/" style="font-size:11px;">eGovernments</a> Urban Portal @ <%= homePageInfoMap.get("productManifest").toString()%></span>
						</div>
						<div class="feedback" align="center" id="feedback">
							<div id="feedmask" class="feedmask"><span style="position:absolute;left:200px;top:150px;"><img src="../images/loading.gif" width="32" height="32"/>&nbsp;&nbsp;Please wait...</span></div>
							<fieldset id="feedset" style="background: url('../images/bgs.jpg') ;">
							<table style="margin-top:10px">
								<tr>
									<td style="color:white;text-align:left">
										Subject
									</td>
								</tr>
								<tr>
									<td>
										<input type="text" id="subject" size="78" maxlength="100"/>
									</td>
								</tr>
								<tr>
									<td  style="color:white;text-align:left">
										Message
									</td>									
								</tr>
								<tr>
									<td >
										<textarea rows="10" cols="80" id="message"></textarea>
									</td>									
								</tr>
								<tr>
									<td colspan="2" align="right">
										<a style="float: left;font-size: smaller;margin-left: 3px;margin-top: 5px;" href="javascript:void(0);" onclick="PopupNewWindow('http://bugzilla.egovernments.org','Feedback Reporter',850,600);showFeedback(false);;">Report in Bugzilla</a><input type="button" class="buttongeneral" value="Send" onclick="sendFeedback()"/>&nbsp;&nbsp;<input type="button" class="buttongeneral" value="Close" onclick="showFeedback(false);"/>
									</td>
								</tr>
							</table>
							</fieldset>
						</div>
						<input type="hidden" id="BASEURL" value=""/>
					</td>
				</tr>
			</table>						
		</form>	
		
	</body>
</html>

<script type="text/javascript">
var loadingMsg = document.getElementById('loading-msg');
loadingMsg.innerHTML = 'Loading Core API...';
</script>

<script type="text/javascript" src="../commonyui/yui2.7/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/event/event-min.js"></script>  
<script type="text/javascript" src="../commonyui/yui2.7/connection/connection-min.js"></script> 
<script type="text/javascript" src="../commonyui/yui2.7/container/container_core-min.js"></script>	
<script type="text/javascript">loadingMsg.innerHTML = 'Loading Portal Components...';</script>	
<script type="text/javascript" src="../commonyui/yui2.7/menu/menu-min.js"></script>
<script type="text/javascript" src="../html/common/accordion/script.js"></script>
<script type="text/javascript">loadingMsg.innerHTML = 'Loading Inbox...';</script>
<script src="../html/common/js/portal.js" type="text/javascript"></script>

<script type="text/javascript">
	loadingMsg.innerHTML = 'Initializing Urban Portal...';
	var lat = '<%=lat%>';
	var lng = '<%=lng%>';	
</script>