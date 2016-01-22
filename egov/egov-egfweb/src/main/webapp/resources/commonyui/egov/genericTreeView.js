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
		var sUrl = "/egi/commonyui/egov/genericScreenAjax.jsp?parentId="+jsonObj.value+"&xmlConfigName="+xmlConfigName+"&qryName="+categoryName+"&qryLevel=intermediatequery";
		var callback = {success:function (oResponse) {
			 	var response=oResponse.responseText; 
			 	if (response.length > 1) {
		        	var result = response.split("^");
			   		var id = result[0].split("+");
			   		var name = result[1].split("+");
			   		var narration = result[2].split("+");
			   		for (var i = 0, j = name.length; i < j; i++) {
						new YAHOO.widget.TextNode(name[i] + "<!--{value:" + id[i] + ",name:'"+name[i]+"',desc:'"+narration[i]+"'}-->", node, false);
					}
				} 
	        	oResponse.argument.fnLoadComplete();
						
		}, failure:function (oResponse) {
			oResponse.argument.fnLoadComplete();
		}, argument:{"node":node, "fnLoadComplete":fnLoadComplete}, timeout:7000};
		YAHOO.util.Connect.asyncRequest("GET", sUrl, callback);
	}

	function sendData() {
		var data = document.all ? oCurrentTextNode.lastChild.innerHTML.toString() : (oCurrentTextNode.innerHTML.toString().substr(oCurrentTextNode.innerHTML.toString().indexOf("<!--"),oCurrentTextNode.innerHTML.toString().length));
		var jsonObj = getValueFromComment(data);
		var mozillaFirefox= document.all ? false : true;
		if(mozillaFirefox){
		
			var wind=window.opener;
			wind.idValue=jsonObj.value;
			wind.nameValue=jsonObj.name;
			wind.descValue=jsonObj.desc;
			try {
				wind.assignValues(wind.document.forms[0].ModuleTreeMenu);
			} catch (e) {}
		} else {
			var wind=dialogArguments;
			wind.idValue=jsonObj.value;
			wind.nameValue=jsonObj.name;
			wind.descValue=jsonObj.desc;
		}
		window.close();
	};
	var oCurrentTextNode;
	function onTriggerContextMenu(p_oEvent) {
		var oTarget = this.contextEventTarget;
		if (oTarget) {
			oCurrentTextNode = oTarget;
		} else {
			this.cancel();
		}
	}
	/*Create a new Tree with Top Parent Boundary, the child boundry will be loaded when you click on Parent Boundary*/
	function buildTree() {
		tree = new YAHOO.widget.TreeView("menutree");
		tree.setDynamicLoad(loadNodeData, currentIconMode);
		var link = "/egi/commonyui/egov/genericScreenAjax.jsp?xmlConfigName="+xmlConfigName+"&qryName="+categoryName+"&qryLevel=parentquery";
		var callback = { 
				success:function(req) {
					var result = req.responseText.split("^");
					var id = result[0].split("+");
			   		var name = result[1].split("+");
			   		var narration = result[2].split("+");
					var root = tree.getRoot();
					for(var i = 0; i < id.length; i++) {
						var parentnode = new YAHOO.widget.TextNode(name[i]+"<!--{value:" + id[i] + ",name:'"+name[i]+"',desc:'"+narration[i]+"'}-->", root, false);
					}
					tree.draw();
					var ctxtMenu = new YAHOO.widget.ContextMenu("ctxtmenu", {trigger:"menutree", lazyload:true, itemdata:[{text:"    Select    ", onclick:{fn:sendData}}]});
					ctxtMenu.subscribe("triggerContextMenu", onTriggerContextMenu);
				}, failure:function(req) {
					bootbox.alert('An error occured, Please try again');
				}, timeout: 300000 
		};
		YAHOO.util.Connect.asyncRequest('GET', link, callback);
	};
	
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


//once the DOM has fully loaded, Set up Boundary tree:
YAHOO.util.Event.onDOMReady(YAHOO.example.treeExample.init, YAHOO.example.treeExample, true);
