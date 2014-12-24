<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/fonts/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.7/datatable/assets/skins/sam/datatable.css"/>
<link href="${pageContext.request.contextPath}/cssnew/error.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="/egi/commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/element/element.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/connection/connection-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/datatable/datatable.js"></script>
<script type="text/javascript" src="/egi/commonyui/yui2.7/autocomplete/autocomplete-debug.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/helper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/budgetHelper.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/jsCommonMethods.js"></script>
 
<div class="topbar"><div class="egov"><img src="${pageContext.request.contextPath}/image/eGov.png" alt="eGov" width="54" height="58" /></div><div class="gov"><img src="${pageContext.request.contextPath}/image/Chennai_logo.jpg" width="54" height="58" /></div>
  <div class="mainheading"><%= request.getParameter("heading") %></div>
</div>
<div class="navibar">
	<div align="right">
  		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
           		<td>
           			<div align="left">
           				<ul id="tabmenu" class="tabmenu">
						<li><a href="#">Home</a></li>
        				<li><a href="#">Log out</a></li>
        				</ul>
        			</div>
        		</td>
           		<td width="63" align="right"><img src="${pageContext.request.contextPath}/image/print.gif" alt="Print" width="18" height="18" border="0" align="absmiddle" /> <a href="#">Print</a></td>
      			<td width="63" align="right"><img src="${pageContext.request.contextPath}/image/help.gif" alt="Help" width="18" height="18" border="0" align="absmiddle" /> <a href="#">Help</a></td>
    		</tr>
  		</table>
	</div>		
</div>
