#-------------------------------------------------------------------------------
# /**
#  * eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  */
#-------------------------------------------------------------------------------
/*
	HelpSmith Web Help System 1.4
	Copyright (c) 2008-2009 Divcom Software
	http://www.helpsmith.com/
*/

var treeList = [];
   
/* treeNode object */

function treeNode(treeData, tree, index, absIndex, parent) {	
	this.tree = tree;
	this.index = index;
	this.absIndex = absIndex; 
	this.parent = parent;
	this.level = parent.level + 1;
	this.expanded = tree.isNodeExpanded(absIndex);
	this.nodes = [];
	this.title = treeData[0];
	this.closedIcon = treeData[1];
	if (treeData[2] == '') {
		this.openedIcon = this.closedIcon;
	}
	else {
		this.openedIcon = treeData[2];
	}
	this.link = treeData[3];
	
	this.addNode = addNode;
	this.printNode = printNode;
	this.unselect = unselect;
	this.select = select;
	this.isLast = function () {
		return this.index == this.parent.nodes.length - 1;
	}
	this.setExpandState = setExpandState;
	this.scrollIntoView = scrollIntoView;

	// add children
	for (var i = 4; i < treeData.length; i++) {
		var d = treeData[i];
		if (!d) return;
		this.addNode(d, tree, this);
	}	
}

function getLevelIdentImages(node) {
	var ident = [];
	var p = node.parent;
	for (var i = node.level; i > 0; i--) {
		ident[i] = '<img src="' + (p.isLast() ? treeLineImages.EMPTY : treeLineImages.I) + '" border="0" alt="">';
		p = p.parent;
	}
	return ident.join('');
}

function getPlusMinusImgSrc(node) {	
	if (!node.absIndex) {
		// is a first node in the tree
		if (node.parent.nodes.length == 1) {
			// no siblings			
			return node.expanded ? treeLineImages.OMINUS : treeLineImages.OPLUS;
		}
		else {
			// has siblings
			return node.expanded ? treeLineImages.RMINUS : treeLineImages.RPLUS;
		}		
	}
	else {
		if (node.isLast()) {
			return node.expanded ? treeLineImages.LMINUS : treeLineImages.LPLUS;
		}
		else {
			return node.expanded ? treeLineImages.TMINUS : treeLineImages.TPLUS;
		}
	}	
}

function getLineImgSrc(node) {	 
	if (!node.absIndex) {
		// is a first node in the tree
		return (node.parent.nodes.length == 1) ? treeLineImages.O : treeLineImages.R;
	}
	else {
		return node.isLast() ? treeLineImages.L : treeLineImages.T;		
	}
}

function getNodeIconSrc(node) {
	return node.expanded ? node.openedIcon : node.closedIcon;
}

function printNodeBody(node) {
	var outStr = '<img id="ni' + node.absIndex + '" src="' + getNodeIconSrc(node) + '" border="0" alt="">&nbsp;';
	if (node.link != '') {
		outStr += '<a id="nb' + node.absIndex + '" class="link" title="' + node.title + '" href="' + node.link +
			'" onclick="treeList[' + node.tree.index + '].selectNode(' + node.absIndex + ');"';
		if (node.tree.options.target != '') {
			outStr += ' target="' + node.tree.options.target + '"';
		}
	}
	else {
		outStr += '<span id="nb' + node.absIndex + '" class="header" title="' + node.title + '"';
	}
	outStr += ' ondblclick="treeList[' + node.tree.index + '].toggleNode(' + node.absIndex + ');"';
	
	if (node.link != '') {
		outStr += '>' + node.title + '</a>';
	}
	else {
		outStr += '>' + node.title + '</span>';
	}
	return outStr;
}

function printNode() {
	var outStr = '<div>' + getLevelIdentImages(this);	
	if (this.nodes.length) {
                // plus/minus image
		outStr += '<a href="javascript: treeList[' + this.tree.index + '].toggleNode(' + this.absIndex + ');">' +
			'<img id="pm' + this.absIndex + '" src="' + getPlusMinusImgSrc(this) + '" border="0" alt=""></a>';
		
		outStr += printNodeBody(this);

		// node children
		outStr += '<div id="ch' + this.absIndex + '" style="display: ' + (this.expanded ? 'block' : 'none') + ';">';
		for (var i = 0; i < this.nodes.length; i++) {
			var n = this.nodes[i];
			outStr += n.printNode();
		}
		outStr += '</div>';
	}
	else {		
		outStr += '<img src="' + getLineImgSrc(this) + '" border="0" alt="">';		
		outStr += printNodeBody(this);
	}       	
	return outStr += '</div>';
}

function unselect() {
	var d = document.getElementById('nb' + this.absIndex);
	if (d) {
		d.style.backgroundColor = 'transparent';
		this.tree.selectedNode = null;
	}
}

function select() {	
	if (this.tree.selectedNode) this.tree.selectedNode.unselect();

	// expand all parents (except of the tree)
	var p = this.parent;
	while (p.parent) {
		p.setExpandState(true);
		p = p.parent;
	}
	
	// highlight new selected node	
	var d = document.getElementById('nb' + this.absIndex);
	if (d) {
		d.style.backgroundColor = whColors.SelTopicNodeBackgroundColor;
		this.tree.selectedNode = this;
		this.scrollIntoView();
	}
}

function setExpandState(expanded) {
	this.expanded = expanded;

	// plus/minus image	
	var pmImg = document.getElementById('pm' + this.absIndex);
	if (pmImg) {
		pmImg.src = getPlusMinusImgSrc(this);
	}
	
	// show/hide node children
	var chDiv = document.getElementById('ch' + this.absIndex);
	if (chDiv) {
		chDiv.style.display = expanded ? 'block' : 'none';
	}

	// node icon
	var nodeIcon = document.getElementById('ni' + this.absIndex);
	if (nodeIcon) {
		nodeIcon.src = expanded ? this.openedIcon : this.closedIcon;
	}

	this.tree.saveNodeExpandStates();
}

function scrollIntoView() {
	var d = document.getElementById('nb' + this.absIndex);
	if (d) {
		var nodeTop = d.offsetTop;
		var wndOffset = (window.pageYOffset) ? window.pageYOffset : document.body.scrollTop;
		var wndHeight = (window.innerHeight) ? window.innerHeight : document.body.offsetHeight;
		var wndClientHeight = document.body.clientHeight;
		if ((nodeTop - wndOffset) > wndHeight) {
			window.scrollTo(0, nodeTop + (wndHeight - wndClientHeight) + d.offsetHeight - wndHeight);
		}
		else if (nodeTop < wndOffset) {
			window.scrollTo(0, nodeTop);
		}
	}
}

/* Tree object */

function Tree(treeData) {
	this.options = {
		target: ''	
	}
	this.level = -1;
	this.nodes = [];
	this.nodeList = [];
	this.selectedNode = null;
	this.addNode = addNode;
	this.toString = toString;	
	this.absIndex = -1;                     
	this.toggleNode = toggleNode;	
	this.unselectNode = unselectNode;
	this.selectNode = selectNode;
	this.selectNodeByLink = selectNodeByLink;
	this.saveNodeExpandStates = saveNodeExpandStates;
	this.isNodeExpanded = isNodeExpanded;
	this.setCookie = setCookie;
	this.getCookie = getCookie;

	// add self to the global treeList array
	this.index = treeList.length;
	treeList[this.index] = this;

	this.nodeExpandStates = [];
	var s = this.getCookie('tree' + this.index);
	if (s) this.nodeExpandStates = s.split('*');	

	// root-level children
	for (var i = 0; i < treeData.length; i++) { 
		this.addNode(treeData[i], this, this);
	}
}

function toString() {
	var outStr = '<div class="tree">';	
	for (var i = 0; i < this.nodes.length; i++) {
		var n = this.nodes[i];
		outStr += n.printNode();
	}                    
	outStr += '</div>';
	return outStr;
}

function toggleNode(absNodeIndex) {
	var node = this.nodeList[absNodeIndex];
	if (node) {
		node.setExpandState(!node.expanded);		
	}	
}

function unselectNode(absNodeIndex) {
	var node = this.nodeList[absNodeIndex];
	if (node) {
		node.unselect();
		return true;
	}
	return false;
}

function selectNode(absNodeIndex, forceUnselect) {
	var node = this.nodeList[absNodeIndex];
	if (node) {
		node.select();
		return true;
	}
	else if (forceUnselect && this.selectedNode) {
		this.selectedNode.unselect();
		return true;
	}
	return false;
}

function selectNodeByLink(link, ignoreBookmarks, forceUnselect) {
	if (link && (link != ''))  {
		for (var i = 0; i < this.nodeList.length; i++) {
			var node = this.nodeList[i];
			if ((!ignoreBookmarks && (node.link == link)) || 
				(ignoreBookmarks && (removeBookmark(node.link) == removeBookmark(link)))) {
				node.select();
				return true;
			}
		}
	}
	if (forceUnselect) this.selectNode(-1, true);		
	return false;
}

function saveNodeExpandStates() {
	var s = '';
	for (var i = 0; i < this.nodeList.length; i++) {
		var node = this.nodeList[i];
		if (node.expanded) {
			if (s) s += '*'; // index separator
			s += node.absIndex;
		}
	}	
	this.setCookie('tree' + this.index, s);	
}

function isNodeExpanded(absNodeIndex) {
	for (var i = 0; i < this.nodeExpandStates.length; i++) {
		if (absNodeIndex == this.nodeExpandStates[i]) {
			return true;
		}
	}
	return false;
}

function getCookie(name, value) {	
	document.cookie = escape(name) + '=' + escape(value);
} 

function getCookie(name) { 
	var e_name = escape(name);
	var value = '';
	if (document.cookie.length > 0) {
		var start = document.cookie.indexOf(e_name + '=');
		if (start >= 0) {
			start = start + (e_name + '=').length;
			var end = document.cookie.indexOf(';', start);
			if (end < 0) end = document.cookie.length;
			value = unescape(document.cookie.substring(start, end));
		}
	}
	return value;
}

/* common methods */

function addNode(treeData, tree, parent) {	
	var idx = this.nodes.length;	
	var node = new treeNode(treeData, tree, idx, ++tree.absIndex, parent);	
	this.nodes[idx] = node;
	tree.nodeList[node.absIndex] = node;
}
