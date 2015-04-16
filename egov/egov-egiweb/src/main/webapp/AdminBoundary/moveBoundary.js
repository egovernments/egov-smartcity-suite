
/*private constructor to make the Boundary Tree*/
YAHOO.example.treeExample = function () {
	var tree,currentIconMode;
	function changeIconMode() {
		var newVal = parseInt(this.value);
		if (newVal != currentIconMode) {
			currentIconMode = newVal;
		}
		buildTree();
	}
	/*Loading child nodes*/
	function loadNodeData(node, fnLoadComplete) {
		var valComment = document.all ? document.getElementById(node.labelElId).lastChild.innerHTML : (document.getElementById(node.labelElId).innerHTML.toString().substr(document.getElementById(node.labelElId).innerHTML.toString().indexOf("<!--"),document.getElementById(node.labelElId).innerHTML.toString().length));
		var jsonObj = getValueFromComment(valComment);
		var sUrl = "../AdminBoundary/moveBoundary.do?target=AJAX&boundaryId=" + parseInt(jsonObj.value) + "&mode=load_tree";
		var callback = {success:function (oResponse) {
			var responseResult = eval("(" + oResponse.responseText + ")");
			if (responseResult.name.length == 0) {
				oResponse.argument.fnLoadComplete();
				return false;
			}
			for (var i = 0, j = responseResult.name.length; i < j; i++) {
				new YAHOO.widget.TextNode(responseResult.name[i] + "<!--{value:" + responseResult.value[i] + "}-->", node, false);
			}
			oResponse.argument.fnLoadComplete();
		}, failure:function (oResponse) {
			oResponse.argument.fnLoadComplete();
		}, argument:{"node":node, "fnLoadComplete":fnLoadComplete}, timeout:7000};
		YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	}

	/*Loading Parent Boundary Name in Combobox*/
	var oCurrentTextNode;
	function shiftBoundary() {
		document.getElementById("shiftconfbox").style.display = "block";
		var nodeVal = document.all ? oCurrentTextNode.lastChild.innerHTML.toString() : (oCurrentTextNode.innerHTML.toString().substr(oCurrentTextNode.innerHTML.toString().indexOf("<!--"),oCurrentTextNode.innerHTML.toString().length));
		var jsonObj = getValueFromComment(nodeVal);
		document.getElementById("boundaryId").value = jsonObj.value;
		document.getElementById("boundaryName").value = document.all ? oCurrentTextNode.innerText : (oCurrentTextNode.innerHTML.toString().substr(0,oCurrentTextNode.innerHTML.toString().indexOf("<!--")));
		var url = "../AdminBoundary/moveBoundary.do?target=AJAX&boundaryId=" + parseInt(jsonObj.value) + "&mode=load_move_data";
		var boundary = getBoundary(url);
		var combo = document.getElementById("bndrycombox");
		combo.length = 0;
		combo.options[0] = new Option("--Choose a Boundary--", 0);
		for (var i = 1, j = 0; i <= boundary.name.length; i++, j++) {
			combo.options[i] = new Option(boundary.name[j], boundary.value[j]);
		}
	}
	/*Defunctional for now to delete a boundary*/
	function deleteBoundary() {
		alert("Sorry, Development under progress");
	}
	/*Called when we right click the mouse on top of tree node*/
	function onTriggerContextMenu(p_oEvent) {
		var oTarget = this.contextEventTarget;
		var valComment = document.all ? oTarget.parentNode.lastChild.lastChild.innerHTML : oTarget.parentNode.lastChild.innerHTML.toString().substr(oTarget.parentNode.lastChild.innerHTML.toString().indexOf("<!--"),oTarget.parentNode.lastChild.innerHTML.toString().length); 
		var jsonObj = getValueFromComment(valComment);
		if (jsonObj.id === "parent") {
			this.cancel();
		}
		
		if (oTarget) {
			oCurrentTextNode = oTarget;
		} else {
			this.cancel();
		}
	}
	/*Create a new Tree with Top Parent Boundary, the child boundry will be loaded when you click on Parent Boundary*/
	function buildTree() {
        tree = new YAHOO.widget.TreeView("tree");
		tree.setDynamicLoad(loadNodeData, currentIconMode);
		var root = tree.getRoot();
		var rootTree = getBoundary("../AdminBoundary/moveBoundary.do?target=AJAX&boundaryId=&mode=load_tree");
		for (var i = 0, j = rootTree.name.length; i < j; i++) {
			var tempNode = new YAHOO.widget.TextNode(rootTree.name[i] + "<!--{id:\"parent\", value:" + rootTree.value[i] + "}-->", root, false);
		}
		tree.draw();
		var oContextMenu = new YAHOO.widget.ContextMenu("mytreecontextmenu", {trigger:"tree", lazyload:true, itemdata:[{text:"Shift Boundary", onclick:{fn:shiftBoundary}}, {text:"Delete Boundary", onclick:{fn:deleteBoundary}}, ]});
		oContextMenu.subscribe("triggerContextMenu", onTriggerContextMenu);
	}
	/*To retrieve The value corresponding to a menu tree which is saved inside a comment as json string*/
	function getValueFromComment(_hidtext) {
		var innerJson = _hidtext.toString();
		innerJson = innerJson.replace("<!--", "");
		innerJson = innerJson.replace("-->", "");
		var jsonObj = eval("(" + innerJson + ")");
		return jsonObj;
	}
	return {init:function () {
		YAHOO.util.Event.on(["mode0", "mode1"], "click", changeIconMode);
		var el = document.getElementById("mode1");
		if (el && el.checked) {
			currentIconMode = parseInt(el.value);
		} else {
			currentIconMode = 0;
		}
		buildTree();
	}};
}();

/*Ajax requester to get the Boundaries*/
function getBoundary(url) {
	var request = initiateRequest();
	request.open("GET", url, false);
	request.send(null);
	if (request.status === 200 && request.readyState === 4) {
		var response = eval(request.responseText);
		return response;
	}
}

/*Called when Move button click, to Move a child boundary to another Parent Boundary*/
function moveBoundary() {
	try {
		var parentBoundaryId = document.getElementById("bndrycombox").value;
		var boundaryId = document.getElementById("boundaryId").value;
		if (parseInt(parentBoundaryId) !== 0 && boundaryId != "") {
			var url = "../AdminBoundary/moveBoundary.do?target=AJAX&boundaryId=" + boundaryId + "&parentBoundaryId=" + parentBoundaryId + "&mode=move_boundary";
			var request = initiateRequest();
			request.open("GET", url, false);
			request.send(null);
			if (request.status === 200 && request.readyState === 4) {
				var response = eval(request.responseText);
				if (response.status === "moved") {
					window.alert(document.getElementById("boundaryName").value + " has been successfully moved to " + document.getElementById("bndrycombox").options[document.getElementById("bndrycombox").selectedIndex].text);
					document.getElementById("boundaryId").value = "";
					document.getElementById("boundaryName").value = "";
					document.getElementById("shiftconfbox").style.display = "none";				
					window.location.href='../AdminBoundary/moveBoundary.do';
					window.location.reload();
				} else {
					window.alert(response.message);
				}			
			}

			
		} else {
			throw new Error("Please select a Boundary");
		}
	}
	catch (e) {
		alert(e.message);
	}
}


/*To hide the move input screen*/
function hideConfBox() {
	document.getElementById("shiftconfbox").style.display = "none";
}

//once the DOM has fully loaded, Set up Boundary tree:
YAHOO.util.Event.onDOMReady(YAHOO.example.treeExample.init, YAHOO.example.treeExample, true);