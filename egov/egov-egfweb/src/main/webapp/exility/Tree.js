/*******************************************
/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
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
#-------------------------------------------------------------------------------*/
/* Author: RG Bhandi, Exilant Consulting
/  Ver 1.0 Oct - 2004
/
******************************************/

/*
 * Tree is implemented with two objects. ExilTree that holds all 'nodes' of the tree
 *  and ExilNode that keeps track of its parent and children.
 */
if (typeof(window.exilWindow) == 'undefined')window.exilWindow = window;

function ExilTree(div){ //div is the <div> DOM element where this tree is to be displayed
	//standard/default iamges
	this.imagesDir		= '/egi/resources/erp2/images/';
	this.collapsedGif	= this.imagesDir + 'plus.gif';
	this.expandedGif	= this.imagesDir + 'minus.gif';
	this.spaceGif		= this.imagesDir + 'space.gif';
	this.leafGif		= this.imagesDir + 'page.gif';
	this.folderGif		= this.imagesDir + 'folder.gif';
	this.folderOpenGif	= this.imagesDir + 'folderopen.gif';
	this.styleToShow= ''; //style.disply attribute
	this.styleToHide	= 'none';

	// configurable parameters. These can be modified with the <parameter> in XML
	this.indent			= 20; //each indent shifted by 20 pixels
	this.normalBackgroundColor	 	= 'white';
	this.highlightedbackgroundColor = 'blue';
	this.normalColor 	= 'navy';
	this.highlightedColor= 'white';
	this.fontFamily		= 'tahoma';
	this.fontSize		= '8pt';
	this.indent			= '20';
	this.searchIsRequired= 'FALSE';//making it a boolean had issues with loading from XML
	
	div.setAttribute('exilTree', this); //link the div and tree so that we can get one from the other
	this.div		= div;

	this.images		= new Object(); //stores images for different types of nodes	
	this.nodeCount	= 0;
	this.nodes		= new Object(); //contains all node objects as properties
	this.sortedNodes= null; //sorted on a need basis and kept.

	this.selectedNode= null;//points to the node user has clicked
	this.searchString =	""; //last search string
/* creates a new node.
 * id has to be unique. it is typically an internal code used by the system. id is not displayed. 
 * name is diaplyed. need not be unique, even for a parent. 
 * paentid id is optional. It is possible to first create nodes, and then 'link' them to create the tree
 * alternately, the parent id can be specified at the time of creating the node, even if the
 *   parent id is not created yet.
 *   This feature allows a server to just dump the rows of its table without worrying about the order
 * 
 */
 
this.createNode = function (type, id, name, parentid,actionId){
	
	if(!id || !name) return (null);
	
	var node		= this.nodes[id];
	if (node){ //node exists. design is not to generate error, but just modify it
		node.name	= name;
	}else{ //create  a new node
		node		= new ExilNode(this, type, id, name,actionId);
		node.tree	= this;
		this.nodes[id] = node;
		this.nodeCount ++;		
	}
	
	if (parentid)
		if ( parentid.toUpperCase() == 'NULL' || parentid == 0 || parentid == '') parentid = null;
	if(parentid) this.link(id,parentid);
	return (node);
}

/* 
 * link is used to create/remove parent-child relationship between nodes
 * existing link is removed if the new link can be created or parent id is null
 * parent node is created if required before linking
 */

this.link = function (childid, parentid){ 
	var child = this.nodes[childid];
	if (!child) return(false);
	
	var added	= false;
	var removed = false;
	var oldparent	= child.parent;

	if (parentid){
		
		var	parent			= this.nodes[parentid];
		if (!parent) parent = this.createNode('node', parentid,'new',null); //create in anticipation. Name will be attached later
		if(!child.canAttachTo(parent)) return(false); //wrong connection tried :-) 
		parent.children[childid] = child;
		parent.childCount++;
		child.parent = parent;
		added	= true;
	}
	
	if(oldparent){ //old link exists
		oldparent.children[childid] = null;
		oldparent.childCount--;
		if(!added) child.parent = null; //this case was to just remove the link
		removed = true;
	}
	
	return (added || removed); //returns true if something was done
}

/* Creates all the HTML DOM elements to display the tree. 
 * Initially, only the root nodes are visible, while the rest are all hidden
 * All the rows are created. Root nodes are set with display attribute as blocked while the rest with 'none'
 */

this.display = function (){	

	var div = this.div;
	div.innerHTML = ''; //zap existing content
	div.style.backgroundColor = this.normalBackgroundColor;
	div.style.color			= this.normalColor;
	div.style.fontFamily	= this.fontFamily;
	div.style.fontSize		= this.fontSize;
	div.onclick				= ExilTreeClick;
	div.ondblclick			= ExilTreeDblClick;
	div.onmouseover			= ExilTreeMouseOver;
	div.onmouseout			= ExilTreeMouseOut;

	if (this.searchIsRequired && this.searchIsRequired.toString().toUpperCase() != 'FALSE' ){
		var divform = exilWindow.document.createElement('DIV'); //for search fields and buttons
		div.appendChild(divform);
		
		var ele = exilWindow.document.createElement('INPUT');
		ele.divId = div.id;
		ele.id = 'searchField';
//////		ele.width = 15;
		ele.onchange = ExilTreeSearchEvent;
		divform.appendChild(ele);
		
		ele = exilWindow.document.createElement('INPUT');
		ele.type = 'button';
		ele.value = 'Search';
		ele.divId = div.id;
		ele.id = 'searchButton';
		ele.onclick = ExilTreeSearchEvent;
		divform.appendChild(ele);

		ele = exilWindow.document.createElement('INPUT');
		ele.type = 'button';
		ele.value = ' ++ ';
		ele.title = 'Expand All';
		ele.divId = div.id;
		ele.id = 'expandAll';
		ele.onclick = ExilTreeSearchEvent;
		divform.appendChild(ele);
		
		ele = exilWindow.document.createElement('INPUT');
		ele.type = 'button';
		ele.value = ' - - ';
		ele.title = 'Collapse All';
		ele.divId = div.id;
		ele.id = 'collapseAll';
		ele.onclick = ExilTreeSearchEvent;
		divform.appendChild(ele);
/*		
		var ihtm  = '<input width="15" onchange="ExilTreeSearchEvent(1," 
			 +"'" +div.id +"','" +this.value +')">&nbsp;';
		ihtm += '<input type="button" value="Search" name="searchNode" id="searchNode" onclick="ExilTreeSearchEvent(2,"'
			 +"'" +div.id +"'" +')">&nbsp;';
		ihtm += '<input type="button" value=" - - " name="collapseAll" id="collapseAll" onclick="ExilTreeSearchEvent(3,"'
			 +"'" +div.id +"'" +')">&nbsp;';
		ihtm += '<input type="button" value="  ++  " name="expandAll" id="expandAll" onclick="ExilTreeSearchEvent(4,"'
			 +"'" +div.id +"'" +')">&nbsp;';
		divform.innerHTML = ihtm;
*/
	}
	
	var tbl		= exilWindow.document.createElement('TABLE');
	tbl.setAttribute('border','0');
	tbl.setAttribute('cellSpacing','0');
	div.appendChild(tbl);
	
	var tbody = exilWindow.document.createElement('TBODY');
	tbl.appendChild(tbody);	
	
	var node = null;
	for (var a in this.nodes){
		node	= this.nodes[a];
		if (node && node.parent == null)node.display(tbody, 0); //display only the root nodes. They in turn will display the branches below them
	}
}

/*
 * If a selected node is hidden (when one of its ancestors collapses :-)
 *  the selection is shifted to the first ancestor that is visible.
 */

this.shiftSelection = function (){
	if (!this.selectedNode ) return true;
	if (this.selectedNode.displayed) return true;
	var p = this.selectedNode.parent;
	while (p && !p.displayed) p = p.parent; //root parent is guaranteed to be displayed always
	p.select(false); //false -> selected node should not be expanded,
}

/*
 * Expands or collpases the entire tree
 * expand = true means expand, else collapse
 */

this.expandCollapse = function (expand){
	var n = null;
	for (var a in this.nodes){
		n = this.nodes[a];
		if (!n.parent) n.expandCollapse(expand, true); //root nodes are expanded/collpased with cascade effect
	}
}

/*
 * Sorting order used is 'depth-first'. It would be the order in which nodes are displayed
 *		when every node is expanded.
 */

this.sort = function (){
	if (this.sortedNodes)return(true);
	
	this.sortedNodes = new Array(this.nodeCount);
	var idx = 0;
	var p = null;
	for(var a in this.nodes){ //for each root nodes
		p = this.nodes[a];
		if (p.parent == null){
			this.sortedNodes[idx] = p;
			p.sortedIndex = idx;
			idx++;
			this.sortBranch(p,idx);
		}
	}
}
/*
 * Sorting order used is 'depth-first'. It would be the order in which nodes are displayed
 *		when every node is expanded.
 */

this.sortBranch = function (parent,idx){
	var node = null;
	for(var a in parent.children){ //for each root nodes
		node = parent.children[a];
		this.sortedNodes[idx] = node;
		node.sortedIndex = idx;
		idx++;
		if (node.childCount > 0) idx = this.sortBranch(node,idx);
	}
	return(idx);
}

/*
 * Searches for a string in the name of nodes, starting from the selected node.
 * if no match is found till end of tree, user is warned that the search will restart from beginning
 * if no match is found in the entire doc, user is informed
 */

this.search = function (strin){

	if (strin) this.searchString = strin;
	else strin = this.searchString;
	
	if (strin == ''){
		bootbox.alert('No search string specified');
		return(false);
	}
	
	str = new String(strin);
	str = str.toUpperCase();
	
	if (!this.sortedNodes) this.sort();
	var node = null;
	var idx = (this.selectedNode)? this.selectedNode.sortedIndex + 1 : 0;
	var restarted = false;
	while (true){ //will come out based on user input
		while (idx < this.nodeCount){
			node = this.sortedNodes[idx];
			if (node.name.toString().toUpperCase().indexOf(str) >= 0){
				node.select(false);
				return(true);
			}
			idx++;
		}
		//if we reach here, we have reached the end
		if (restarted){
			bootbox.alert('No match found');
			return(false);
		}
		alert ('Reached end of tree. Going to start from beginning of tree');
		restarted = true;
		idx = 0;
	}
	return(false);
}

this.addImg = function (typ,img){
	var imgobj = exilWindow.document.createElement('IMG');
	imgobj.src = this.imagesDir+img;
	this.images[typ] = imgobj;
}
} // end of ExilTree object definition

/*************************
// A node points to its parent, as well as keeps a list of all children.
// Instead of using a collection class, property list is used for convinience. 
// for example, children is defined as an object, and each child is attached as a property with name = its id
//
**************************/

function ExilNode(tree, type, id, name,actionId){
	this.tree		= tree;
	this.parent		= null;
	this.type		= type;
	this.id			= id; 
	this.name		= name;
	this.actionId		= actionId;
	this.childCount	= 0;
	this.children	= new Object(); //children and parent form the tree structure
	this.expanded	= false;
	this.displayed	= false; //is this node physixcally displaye on the screen? (is it visible?)
	this.treeIndex	= 0; //when all nodes are sorted by their order in the depth-first order
	this.tr			= null; //TR element where this node is diaplyed
	this.alternateImg= null; //IMG element that shows +/-DOM element
	this.folderImg	= null; //IMG element for the folder
	this.textNode	= null; //doc element that shows the name
	this.attributes = new Object(); //stores all user defined atributes using method add()

this.setAttribute = function (attName, attValue){
	this.attributes[attName] = attValue;
}

this.getAttribute = function (attName){
	return this.attributes[attName];
}

this.canAttachTo = function (node){// returns True/false
	if (!node) return (true);//attaching to non-node means making it root
	if (this == node) return(false); //can't attach to myself
	if (node.children[this.id]) return(false); //already a child
	
	// check if node is a desscendant of 'this' to avoid cyclical network.
	var p = node.parent;
	while (p){ //keep going up till you reach the root
		if (p == this) return (false);
		p = p.parent;
	}
	return(true);
}
/*
 * returns true if this is a descendant of the node
 */
this.isDescendantOf = function (node){
	var id = node.id;
	var p = this.parent;
	while (p){
		if (p.id == id)return true;
		p = p.parent;
	}
	return false;
}

/* common method to expand/collapse. 
 * expand = true implies expanf, else collapse
 * cascade = true means cascade expand/collapse
 */
this.expandCollapse = function (expand,cascade){
	if (this.childCount == 0) return(true); //nothing to expand/collapse
	var tree = this.tree;
	this.expanded = expand; //expand is true/false
	if (expand){//expanded
		this.alternateImg.src	= tree.expandedGif;
	}else{//collapsed
		this.alternateImg.src	= tree.collapsedGif;
	}
	
	var child	= null;
	for(var a in this.children){
		child	= this.children[a];
		child.show(expand);
		if (cascade) child.expandCollapse(expand,cascade);
	}
}

/* common method to show/hide
 * show = true/false
 * this method takes care of showing/hiding the branch below
 */
this.show = function (show){

	// if this is already displayed, then no work in show
	if(this.displayed && show) return (true);
	if(!this.displayed && !show) return (true); //similar situation for hide
	
	var tree = this.tree;
	this.displayed	= show;
	this.tr.style.display = (show)? tree.styleToShow : tree.styleToHide ; //set the right display attribute

	if (this.childCount == 0 || this.expanded == false) return(true); //no need to look at children

	for(var a in this.children){
		this.children[a].show(show);
	}
	return(true);
}

/*collapse if it is expanded, expand if it is collapsed */
this.alternate = function (){ 
	this.expandCollapse(!(this.expanded),false); //small trick. flag is set based on current state of the node
	if (this.expanded == false) this.tree.shiftSelection(); //shifts selection if it is not shown
}			

/* selects a node (normally on click) When a node is selected:
 *   1. this node is highilighted
 *   2. node that was selected earlier is shown as normal
 *   3. If the selected node is a folder, is expanded if required (as in windows explorer)
 *	 4. If this is not visible, 'explode' the branch above so that this is visible
 *   5. If this is part of the frame, a function by name TreeNodeSelected() is invoked from the
 *			'details' exilWindow.document with id, name and parent id as parameters
 */
this.select = function (expand){
	var tree = this.tree;
	var old =tree.selectedNode;
	if (old && old.id == this.id) { // same node is selected again

	}else{
		var st = this.textNode.style;
		st.backgroundColor	= tree.highlightedbackgroundColor;
		st.color			= tree.highlightedColor;
		st.textDecorationUnderline = false;
		
		if(old){
			st = old.textNode.style;
			st.backgroundColor  = tree.normalBackgroundColor;
			st.color			= tree.normalColor;
		}
	}
	tree.selectedNode = this;
	if (this.displayed == false) this.explode();	
	if (expand && this.childCount > 0 && this.expanded == false){
		this.expandCollapse(true,false); // true for expandiing, and false for cascading
	}
	//and finally, see if you can get this into visible zone
	ScrollForElement (this.tr);
	// if (typeof this.tr.scrollIntoView) this.tr.scrollIntoView(false);
}
/*creates a TR element and attaches itself to tbody
 *level indicates how far to indentation level for the node in the tree
 * This is a fairly long method, but not complex. it essentially creates all elements required for the TR 
 */

this.display = function (tbody, level){
	var tree = this.tree;
	//it would be simple to use innerHTML, but DOM elements are used for effeciency sake
	var tr = exilWindow.document.createElement('TR'); //add TR to TBODY
	tbody.appendChild(tr);
	this.tr = tr;
	
	var st = tr.style;
	st.backgroundColor = tree.normalBackgroundColor;
	st.fontFamily	= tree.fontFamily;
	st.fontSize		= tree.fontSize;
	st.color		= tree.normalColor;

	if (this.parent){ //only root nodes are dispalyed in the beginning
		st.display = tree.styleToHide;
		this.displayed	 = false;
	}else{
		st.display = tree.styleToShow;
		this.displayed	 = true;
	}
	
	var td = exilWindow.document.createElement('TD'); //add TD to TR		
	tr.appendChild(td);	
	
	//blank gif to indent the node
	var img = exilWindow.document.createElement('IMG');
	img.src = tree.spaceGif;
	img.width = level*tree.indent;
	img.height=1;
	td.appendChild(img);
	
	// + or - or indent gif
	img = exilWindow.document.createElement('IMG');
	td.appendChild(img);
	img.setAttribute('exilNode', this);	//this property identifies the element for event processing

	if (this.childCount > 0){ //parent. put +-
		img.setAttribute('exilAlternate', true); //event handler will look for this property to expand/collapse
		img.src = tree.collapsedGif;
		this.alternateImg =  img;
	}else{
		img.src = tree.spaceGif;
		img.width = tree.indent;
	}
	
	if (this.type && tree.images[this.type]) img = tree.images[this.type].cloneNode(false);
	else {
		img = exilWindow.document.createElement('IMG'); //leaf gif
		img.src = (this.childCount > 0) ? tree.folderGif : tree.leafGif;
	}
	img.setAttribute('exilNode', this);
	td.appendChild(img);

	txt = exilWindow.document.createElement('SPAN');//and finally the text
    txt.className = "NodeText";
    txt.style.cursor = "default";
	txt.innerHTML = this.name;
	txt.title	= this.name;
	txt.setAttribute('exilNode', this);
	td.appendChild(txt);
	this.textNode = txt;
	
	if (this.childCount== 0	)return (true);
	//display children as well
	level++; //Indent to next level
	for (var a in this.children)this.children[a].display(tbody,level);
	return(true);
}
/* explodes parent to ensure that it is visible.
 * cascades the effect if the parent's parent is not expanded..
 * recursion is used to expand the nodes in the ancestral order
 */

this.explode = function (){
	if (this.displayed) return(true);
	
	var p = this.parent;
	if (!p.displyed) p.explode(); //can't do anyhting till parent is visible
								// and remember, root node is always visible, and hence this
								//recursion ends at the most at root
	// situation here is that this node is not visible, but the parent is.
	// this implies that the parent is collapsed. So, expand it.
	p.expandCollapse(true, false); //non-cascading expand
}

}// end of ExilNode object definition

/******************************** Event handling functions ****************/
/* I wish I could have a better way of passing the tree, 
 * To avoid declaring tree as a global variable (and hence keep the option of more than one tree in doc)
 *  I have passed the id of the div, through which I can locate the tree...
 *  Since they all in turn call a specific method of tree, I clubbed the events for search related events
 */

function ExilTreeSearchEvent(evt){

	evt = (exilWindow.event)? exilWindow.event : evt; // IE does not pass event as an object, but 'event' itself is available
								// Netscape passes event as an argument
	var ele = (evt.srcElement) ? evt.srcElement : evt.target; //IE==srcElement, Netscape==target
	var tree = exilWindow.document.getElementById(ele.getAttribute('divid')).getAttribute('exilTree');
	switch (ele.id){
	case 'searchField': //string changed
		tree.searchString = ele.value;
		break;
	case 'searchButton': //search button
		tree.search();
		break;
	case 'expandAll':  //expand all
		tree.expandCollapse(true);
		break;
	case 'collapseAll'://collapse all 
		tree.expandCollapse(false);
		break;
	}
	
	if (evt.cancelBubble) evt.cancelBuuble = true; //IE
	else if(typeof evt.stopPropagation != "undefined")evt.stopPropagation();//Netscape
}
/*
 * all clicks (other than search ) are handled centrally at the div level
*/
function ExilTreeClick(evt){
	evt = (exilWindow.event)? exilWindow.event : evt;
	var ele = (evt.srcElement) ? evt.srcElement : evt.target;
	var node = ele.getAttribute('exilNode');
	if (!node) return; //not of our concern

	var tree = node.tree;
	
	//it is a tree element. find out which one and act accordingly
	if (ele.getAttribute('exilAlternate')){ //it is the +/- gif
		node.alternate();
	}else{
		if (tree.selectedNode && tree.selectedNode.id == node.id && node.textNode == ele){ //one more click on a selected node. Treat this as dbl click
			if (exilWindow.NodeOnTreeDblClicked) NodeOnTreeDblClicked(node);
		}else{
			node.select(true); //true ensures that the node will be expanded on selection
		}
	}
	if (evt.cancelBubble) evt.cancelBuuble = true;
	else if(typeof evt.stopPropagation != "undefined")evt.stopPropagation();
	//does the user want to do something??
	if (exilWindow.NodeOnTreeClicked) NodeOnTreeClicked(node);
}

function ExilTreeDblClick(evt){
	evt = (exilWindow.event)? exilWindow.event : evt;
	var ele = (evt.srcElement) ? evt.srcElement : evt.target;
	var node = ele.getAttribute('exilNode');
	if (!node) return; //not of our concern
	if (node.textNode != ele) return false; //double allowed only on text node

	if(exilWindow.NodeOnTreeDblClicked) exilWindow.NodeOnTreeDblClicked(node);
	if (evt.cancelBubble) evt.cancelBuuble = true;
	else if(evt.stopPropagation)evt.stopPropagation();
	
	//prevent default action from browser (like text selection etc..)
	if(typeof evt.returnValue != 'undefined') evt.returnValue = false;
	else if(evt.preventDefault) evt.preventDefault();
	return false;
}

/*
 * functions to handle scrolling : to bring a selected element into visible region
 */
  
function GetScrolledParent(ele){ //returns a parent element for which scrolling is active. null if no scrolling
	var p = ele.parentElement;
	while(p){
		if (p.scrollHeight > p.offsetHeight) return p;// we could have checked for overflow, but this test
													 // ensure that the region is indeed being scrolled
		p = p.parentElement;
	}
	return null; //no scrolling parent..
}

function GetRelativeTop(ele, pele){ //get the cumulative top. Rturned value represents the top of ele relative to pele
	var top = parseInt(ele.offsetTop)+ parseInt(ele.clientTop);
	var p = ele.offsetParent;
	while (p && p !=pele){
		top += parseInt(p.offsetTop)+ parseInt(p.clientTop);
		p = p.offsetParent;
	}
	return top;
}

function ScrollForElement(ele){ //ensure that ele is within the vsisble area by scrolling if required
	var pele = GetScrolledParent(ele);
	if (pele){ //there is a scrollable parent
		var mytop = GetRelativeTop(ele, pele); //ele top from parent
		var scrtop = parseInt(pele.scrollTop); //position to which the top is scrolled to 
		var scrhite = parseInt(pele.clientHeight); // height of the scrolling region
		var scrbot = scrtop + scrhite; //bottom
		if ( mytop < scrtop || mytop > scrbot){ //not visible
			if (mytop < scrhite) pele.scrollTop = 0; //it is in the first page
			else pele.scrollTop = parseInt(mytop - scrhite/2); //bring it in the middle
		}
	}
}
/*
 * handles mouseover/out event. mouse out = true/false implies the event type 
 */
 
function ExilTreeMouseOver(evt){
	ExilTreeMouseInOut(evt, false);
}

function ExilTreeMouseOut(evt){
	ExilTreeMouseInOut(evt, true);
}

function ExilTreeMouseInOut(evt, mouseout){	
	evt = (exilWindow.event)? exilWindow.event : evt;
	var ele = (evt.srcElement) ? evt.srcElement : evt.target;
	
	var node = ele.getAttribute('exilNode');
	if (!node) return true; // not a node element 
	
	var tree = node.tree;
	if (tree.selectedNode && node.id == tree.selectedNode.id){ //to be handled properly
		 return true; // no change for the seletced node
	}
	var st = ele.style;
	if (mouseout){ // change font back to normal
		st.textDecorationUnderline = false;
		st.color=tree.normalColor;	
	}else{
		st.textDecorationUnderline = true;
		st.color="red";		
	}
}	