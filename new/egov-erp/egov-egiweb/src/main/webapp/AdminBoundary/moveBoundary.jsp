<%@page session="false"%>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<meta http-equiv="Pragma" content="no-cache">
		<title>Move Boundary </title>
		
		<link href="../commonyui/yui2.7/reset-fonts/reset-fonts.css" rel="stylesheet" type="text/css"></link>
		<link href="../commonyui/yui2.7/fonts/fonts.css" rel="stylesheet" type="text/css"></link>
		<link href="../commonyui/yui2.7/menu/assets/skins/sam/menu.css" rel="stylesheet" type="text/css"></link>
		<link href="../commonyui/yui2.7/treeview/assets/skins/sam/treeview.css" type="text/css" rel="stylesheet"></link>		
		<link href="../AdminBoundary/moveBoundary.css" type="text/css" rel="stylesheet"></link>

		<script src="../commonyui/yui2.7/yahoo-dom-event/yahoo-dom-event.js" type="text/javascript"></script>
		<script src="../commonyui/yui2.7/connection/connection-min.js" type="text/javascript"></script>		
		<script src="../commonyui/yui2.7/container/container_core-min.js" type="text/javascript"></script>
		<script src="../commonyui/yui2.7/menu/menu-min.js" type="text/javascript"></script>		
		<script src="../commonyui/yui2.7/treeview/treeview-min.js" type="text/javascript"></script>
		<script src="../commonjs/ajaxCommonFunctions.js" type="text/javascript"></script>
		<script src="../AdminBoundary/moveBoundary.js?random=<%=Math.random()%>" type="text/javascript"></script>
	</head>
	<body class="yui-skin-sam" id="body" oncontextmenu="return false;">
		<span class="title">Move Boundary</span>
		<table width="100%" height="20px">
			<tr height="6px"><td width="100%" class="bordertop"></td></tr>
			<tr height="10px"><td width="100%" class="borderbtm"></td></tr>
		</table>
		<div class="fld1" tooltip="confirm">
			<span class="lgnd1" >Boundary List</span>
			<div id="tree"></div>
		</div>
		<div class="shiftconfbox">
			<center id="shiftconfbox">
				<table cellpadding="10"  cellspacing="5" class="shifttbl">
					<tr height="100px">
						<td>Move to</td>
						<td> : </td>
						<td>
							<select id="bndrycombox" >
								<option value="0">---Choose a Boundary---</option>
							</select>
						</td>
					</tr>
					<tr height="60px">
						<td colspan="3" align="right">
							<input type="button" id="movebtn"  value="  Move  "  onclick="moveBoundary()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="cnclbtn" value=" Cancel " onclick="hideConfBox();">
							<input type="hidden" name="boundaryId" id="boundaryId" value=""/>
							<input type="hidden" name="boundaryName" id="boundaryName" value=""/>
						</td>
					</tr>
				</table>
			</center>			
		</div>
		<div class="info">Right click on the Boundary Name in the Boundary List to select an operation</div>	
	</body>   
</html>