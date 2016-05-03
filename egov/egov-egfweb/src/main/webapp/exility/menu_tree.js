/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
/*******************************************
/ Author: R G Bhandi
/  Ver 1.0 Nov - 2003
/
/ This tree utility is designed for a server to dump the tree structure, as stored in the database,
/	manipulate and upload the changes back to the server. 
/ This version focuses only on the concept. It has to be enhanced for look and feel
/ IE has its own definition of what can be dragged, and where it can be dropped.
/   only editable text area are valid drop targets 
/ Concept used here are:
/	1. drag-drops are disabled by 'cancel'ing onbeforeselect event. 
/	2. Mouseown, mousemove and mouseup events are used to simulate drag-drop
/ 
/******************************************/

/**********
 Calling the delete node function. The criteria for deletion is,
 
 1. The node should not be the only node in tree
 2. The node should not be the only root node
 3. The node should not have any grandchildren
 4. Deleting a node with children will also delete all the children
 
 The function deleteClicked is added for this purpose and 
 NamTreeDeleteNode of NamTree is modified
 
 Sushma -- 01-Jul-2004
 
  
 *******/


// string constants used		
var dirImg			= '/egi/resources/erp2/images/';  //directory images
var cRootGif		= dirImg + 'root.gif';
var cCollapsedGif	= dirImg + 'plus.gif'; //gifs before the node name
var cExpandedGif	= dirImg + 'minus.gif';
var cLeafGif		= dirImg + 'page.gif';
var cFolder			= dirImg + 'folder.gif';
var cFolderOpen		= dirImg + 'folderopen.gif';	
var cLineGif		= dirImg + 'line.gif';
var cJoinBottom		= dirImg + 'joinbottom.gif';
var cJoinGif		= dirImg + 'join.gif';
var cSpaceGif		= dirImg + 'space.gif';
var cSpaces			= 0;
var cNormalBackground = 'white';
var cSelectedBackgrount = 'navy';

var gTree	= null;  //I have avoided using global variable. but , I did not put my mind on event functions on context menu

//*******  Pushpendra (04-Oct-04)
//var isLeaf = 0;		
var curNodeId = 1;  //for searching the node
var curNodeStr = '';  //String or Substring searching for next
var curScrollPosition = 0;  
//******/

function NamTree(div){ //div is the <div> element where this tree is to be displayed
	div.NamTree		= this;
	gTree			= this;	
	
	this.div		= div;
	this.selectedNode= null;
	this.nodes		= new Object(); //contains all node objects as properties
	this.nodeCount	= 0;
	this.actions	= new Array(); //all actions/operations. Used for Undo. This can also be used to transmit to server
	this.deletedNodes= new Array(); //for undoing deletes
	this.beingDragged= false;
	
	this.hrefTarget = "";
	//methods
	/*this.createNode	= NamTreeCreateNode;
	this.link		= NamTreeLink;
	this.deleteNode	= NamTreeDeleteNode;
	this.undo		= NamTreeUndo;
	this.display	= NamTreeDisplay;
	//** Pushpendra  **
	this.displayCollapsed	= NamTreeDisplayCollapsed;
	this.loadXML			= NamTreeLoadXML;
	this.createNodes		= NamTreeCreateNodes;	
	*/
}
/****      methods of NamTree     ****/
NamTree.prototype.createNode		= NamTreeCreateNode;
NamTree.prototype.link				= NamTreeLink;
NamTree.prototype.deleteNode		= NamTreeDeleteNode;
NamTree.prototype.undo				= NamTreeUndo;
NamTree.prototype.display			= NamTreeDisplay;
//Pushpendra (06-Oct-04)
NamTree.prototype.displayCollapsed	= NamTreeDisplayCollapsed;
NamTree.prototype.loadXML			= NamTreeLoadXML;
NamTree.prototype.createNodes		= NamTreeCreateNodes;
NamTree.prototype.show				= NamTreeShow;
/*************************/

// creates a new node.
// most of the time, parent is also known at the time of creation. Hence, this function optionally accepts a parent id
// If parentid is specified, the link is created, after creating a skelatal parent node if required.

function NamTreeCreateNode(id, name, parentid, nodeid){
	if(!id || !name) return (false);
	
	var node		= this.nodes[id];
	if (node){ //node exists. change the name
		node.name	= name;
		node.nodeid	= nodeid;
	}else{ //create  a new node
		node		= new NamNode(id, name, nodeid);
		node.tree	= this;
		this.nodes[id] = node;
		this.nodeCount ++;
	}
	if (parentid)this.link(id,parentid,true);	
	
	return(node);
}

// link is used to creats/remove parent-child relationship between nodes
//if parentid is omotted, this works as remove link.
// if parentis is specified, parent node is created if required before linking
// if parent is specified, the existing link is removed only if teh new link can be created

function NamTreeLink (childid, parentid, canbeundone){ 
	var child = this.nodes[childid];
	
	if (!child) return(false);
	
	var added	= false;
	var removed = false;
	var oldparent	= child.parent;

	if (parentid){
		var	parent			= this.nodes[parentid];
		if (!parent) parent = this.createNode(parentid,'new',null,''); //create in anticipation. Name will be attached later
		if(!child.canAttachTo(parent)) return(false);
		parent.children[childid] = child;
		child.parent = parent;
		parent.childCount++;
		added	= true;
	}
	
	var oldparentid	= null;
	if(oldparent){ //old link exists
		oldparent.children[childid] = null;
		oldparent.childCount--;
		oldparentid = oldparent.id;
		if(!added) child.parent = null;
		removed = true;
	}
	
	if (added || removed){
		if (canbeundone)this.actions.push(new NamAction(1, childid, parentid, oldparentid));
		return(true);
	
	}else{
		return (false);
	}
}


//Modified to delete the node with children -- Sushma
function NamTreeDeleteNode (id){
	
	var node = this.nodes[id];
	var child	= null;
	
	if (!node)return (false);
	
	var pid = null;
	
	if(node.parent){
		pid = node.parent.id;
		this.link(id,null,true); //remove link if exists
	}
	
	// To check if this node has any children -- Sushma
	if(node.childCount > 0) {
		for(child in this.children){
			// Call the delete function recursively to delete all the children from node
			if(child) gTree.deleteNode(child.id);
		}
	}
	
	this.nodes[id] = null;
	this.nodeCount --;
	this.deletedNodes.push(node);
	this.actions.push(new NamAction(2,id, pid, pid));
	return (true);
}


function NamTreeUndo(){
	var act = this.actions.pop();
	if (!act) return (false);
	
	switch(act.type){
	
	case 1: //link
		this.link(act.nodeId, act.oldParentId, false); //attach to its old parent
		break;
		
	case 2: //deleted: put it back
		var popNode = this.deletedNodes.pop();
		this.nodes[act.nodeId] = popNode;
		if (popNode.childCount > 0) {
			if (popNode.childCount > 1)
				gTree.nodeCount = gTree.nodeCount + popNode.childCount + 1 ;
			else
				gTree.nodeCount += 2;
		} else
			gTree.nodeCount++;
		
		if(this.oldParentId){ // it was linked.
			this.undo();
		}
	}
	this.display();
}

function NamTreeDisplay (){	
	
		var div = this.div;
		div.innerHTML = ''; //clean the table body first
	
		var tbl		= document.createElement('TABLE');
		tbl.setAttribute('border','0');
		tbl.setAttribute('cellSpacing','0');
		//tbl.cellSpacing = 20;
		div.appendChild(tbl);
		
		var tbody = document.createElement('TBODY');
		tbl.appendChild(tbody);			
		
		for (var a in this.nodes){
			node	= this.nodes[a];
			if (node && !(node.parent))node.display(tbody, ''); //display only the root nodes. They in turn will display the branches below them
		}
		
		//display related attributes of this have to be reset
		this.selectedNode = null;
		this.beingDragged = false;
		SetCursor(0);	
}

function NamTreeShow(xmlObj)
{
	this.createNode(n,xmlObj.tagName,''); //create Root
	this.createNodes(xmlObj);				
	//tree.display();  /* Expnaded by default*/
	this.displayCollapsed();
}

function NamTreeDisplayCollapsed()
{
	for(var index in this.nodes)
	{
		node	= this.nodes[index];							
		if(node.parent != null && node.parent.parent == null)
		{					
			node.collapseAll();
			this.display();				
			//HideContext();
		}
	}		
}

function NamAction(type, nodeid, newpid, oldpid){//at this time, this has only attributes. Methhods to be added later
	this.type		 = type; //1=link, 2=delete
	this.nodeId		 = nodeid;
	this.newParentId = newpid;
	this.oldParentId = oldpid;
}


/*************************
// A node points to its parent, as well as keeps a list of all children.
// Instead of using a collection class, property list is used for convinience. 
// for example, children is defined as an object, and each child is attached as a property with name = its id
//
**************************/

function NamNode(id, name, nodeid){
	this.parent		= null;
	this.id			= id; 
	this.name		= name;
	this.isLeaf	= 0;
	this.nodeid		= nodeid;
	this.childCount	= 0;
	this.children	= new Object(); //children and parent form the tree structure
	this.expanded	= true;
	this.element	= null;
	
	/*this.canAttachTo= NamNodeCanAttachTo;
	this.expandAll	= NamNodeExpandAll;
	this.collapseAll= NamNodeCollapseAll;
	this.alternate	= NamNodeAlternate;
	this.display	= NamNodeDisplay;*/
}
/*** methods of NamNode  ***/
NamNode.prototype.canAttachTo	= NamNodeCanAttachTo;
NamNode.prototype.expandAll		= NamNodeExpandAll;
NamNode.prototype.collapseAll	= NamNodeCollapseAll;
NamNode.prototype.alternate		= NamNodeAlternate;
NamNode.prototype.display		= NamNodeDisplay;

function NamNodeCanAttachTo(node){// returns True/false
	if (!node) return (true);//attaching to non-node means making it root
	if (this == node) return(false); //can't attach to myself
	if (node.children[this.id]) return(false); //already a child
	
	// check if 'this' is an ascendent	of node. In that case, this attach will lead to cyclical network. So not possible
	p = node.parent;
	while (p){ //keep going up till you reach the root
		if (p == this) return (false);
		p = p.parent;
	}
	return(true);
}

function NamNodeExpandAll(){
	this.expanded	= true;
	if (this.childCount == 0)return(true);

	var child	= null;
	for(var a in this.children){
		child	= this.children[a];
		if(child) child.expandAll();
	}
	return(true);
}

function NamNodeCollapseAll(){
	this.expanded	= false;
	if (this.childCount == 0)return(true);

	var child		= null;
	for(var a in this.children){
		child		= this.children[a];
		if(child) child.collapseAll();
	}
}

function NamNodeAlternate(){ //collapse if it is expanded, expand if it is collapsed
	this.expanded	= !(this.expanded);
}			

function NamNodeDisplay(tbody, spaces){ //attaches TR to the  TBODY

	//to check leaf or not
	//var leaf=0;
	//it would be simple to use innerHTML, but DOM elements are used for effeciency sake, as well as to demonstrate use of such a technique
	var tr = document.createElement('TR'); //add TR to TBODY
	tbody.appendChild(tr);	
	
	var td = document.createElement('TD'); //add TD to TR		
	tr.appendChild(td);	
	
	var img1 = document.createElement('IMG');
	td.appendChild(img1);
	img1.NamNode = this;
	img1.src = cSpaceGif;	
	img1.width = spaces.length*20;
	img1.height=10;
	
	var img1 = document.createElement('IMG');
	td.appendChild(img1);
	img1.NamNode = this;	

	if (this.childCount > 0)
	{ //parent node
		img1.NamAlternate = true; //event handler will look for this property to expand/collapse
		img1.src = (this.expanded)? cExpandedGif : cCollapsedGif;

		var img2 = document.createElement('IMG');
		img2.NamNode = this;
		td.appendChild(img2);
		if(spaces.length==0)		
			img2.src = cRootGif;			
		else
			img2.src = (this.expanded)?cFolderOpen:cFolder;
			
		this.isLeaf = 0;
	}
	else
	{
		this.isLeaf = 1;		
		img1.src = cSpaceGif;
		img1.width = 20;
		
		var img2 = document.createElement('IMG');
		img2.NamNode = this;
		td.appendChild(img2);		
		img2.src = cLeafGif;		//extra for scrolling
	}
	
	txt = document.createElement('SPAN');

    	txt.className = "NodeText";
	txt.innerHTML = this.name;
	
	txt.NamNode = this;
	this.element = txt;
	td.appendChild(txt);
	
	if (this.childCount > 0 && this.expanded){
		spaces += cSpaces; //shift display to next level
		var child = null;
		for (var a in this.children){
			child = this.children[a];
				if (child) child.display(tbody,spaces);
		}
	}
}

// event handlers for the tree

function NamTreeMouseDown(div){
	var ele		= event.srcElement;
	var node	= ele.NamNode;
	
	if(!node) return (true);//not our domain
	window.status = node.name;
	var tr		= node.tree;

	if (ele.NamAlternate){ //+- gif
		//collapsing
		node.alternate();
		tr.display();
		event.cancelBubble = true;
	}else{	
		var oldnode = tr.selectedNode;
		if (oldnode && oldnode.element) oldnode.element.style.color = 'navy';
		if (oldnode && oldnode.element) oldnode.element.style.backgroundColor = cNormalBackground;
	}
   
	tr.selectedNode = node;
	
	ele	= node.element;
	ele.style.backgroundColor = cSelectedBackgrount;
	ele.style.color = 'white';
	//window.open(gTree.hrefTarget + '?id=' + node.nodeid,"mainFrame");
	openPage(node.nodeid);
	//return(false);
}

function openPage(nodeid)
{
bootbox.alert(nodeid);
	if(nodeid == null);
	else
	{
		switch(nodeid)
		{			
			case "1":
				window.open("acc_tree.htm","leftFrame");	 break;
			case "30":window.open("BankAdd.htm","mainFrame");	 break;
			case "3001":
			case "3002":
			case "3003":
			case "3004":
				window.open("BankEnquiry.htm","mainFrame");	 break;
			case "31":window.open("useradd.htm","mainFrame");	 break;
			case "3101":
			case "3102":
			case "3103":
			case "3104":
				window.open("UserEnquiry.htm","mainFrame");	 break;
                        case "32":
				window.open("OrgStructure.htm","mainFrame");	 break;
			case "3201":
			case "3202":
			case "3203":
				window.open("Zones.htm","mainFrame");	 break;
			case "33":
				window.open("Supplier.htm","mainFrame");	 break;
			case "34":
				window.open("Contractor.html","mainFrame");	 break;
			case "35":window.open("BillCollectorAdd.htm","mainFrame");break;
			case "3501":
			case "3502":
				window.open("BillCollectorAdd.htm","mainFrame");	 break;
			case "36":window.open("FundAdd.htm","mainFrame");	 break;
			case "3601":
			case "3602":
			case "3603":
			case "360101":
			case "360102":
			case "360201":
			case "360202":
			case "360301":
			case "360302":
			case "360303":
			case "360304":
				window.open("FundAdd.htm","mainFrame");	 break;
			case "37":window.open("PropertyTaxAllocation.htm","mainFrame");	 break;
			case "3701":window.open("PickList.htm","mainFrame");	 break;
                        case "3702":window.open("TaxCodeAdd.htm","mainFrame");	 break;
			case "38": window.open("Department.htm","mainFrame"); break;
			case "39": window.open("CompanyDetail.htm","mainFrame"); break;
			case "4":window.open("ReceiptSearch.htm","mainFrame");	 break;				 
			case "4001":
				window.open("PT_Field_Header.htm","mainFrame");	 break;
			case "4002":
				window.open("PT_Office_Header.htm","mainFrame");	 break;
			case "4003":
				window.open("PT_Bank_Header.htm","mainFrame");	 break;
			case "4101":
				window.open("OT_Field_Header.htm","mainFrame");	 break;
			case "4102":
				window.open("OT_Office_Header.htm","mainFrame");	 break;
			case "4104":
				window.open("OT_Check_Header.htm","mainFrame");	 break;
			case "4201":
				window.open("MiscReceipt.htm","mainFrame");	 break;
			
			case "5":
				window.open("PaymentSearch.htm","mainFrame");	 break;
			case "50":
				window.open("DirectBankPayment.htm","mainFrame");	 break;
			case "51":
				window.open("DirectCashPayment.htm","mainFrame");	 break;
			case "60":
				window.open("SupplierJournal.htm","mainFrame");	 break;
			case "61":
				window.open("ContractorJournal.htm","mainFrame");	 break;
			case "62":
				window.open("JV_SalaryGeneral.htm","mainFrame");	 break;
			case "63":
				window.open("AdvanceJournal.htm","mainFrame");	 break;
			case "64":
				window.open("SubledgerBankPayment_Header.htm","mainFrame");	 break;
			case "65":
				window.open("SubledgerCashPayment_Header.htm","mainFrame");	 break;
			case "70":
				window.open("JV_General.htm","mainFrame");	 break;
			case "7101":
				window.open("JV_Contra_BToB.htm","mainFrame");	 break;
			case "7102":
				window.open("JV_Contra_BToC.htm","mainFrame");	 break;
			case "7103":
				window.open("JV_Contra_CToB.htm","mainFrame");	 break;
			case "7104":
				window.open("JV_Contra_FToF.htm","mainFrame");	 break;
			case "72":
				window.open("PayInSlip.htm","mainFrame");	 break;
			case "80":
				window.open("ChartOfAccounts.pdf","mainFrame");	 break;
			case "81":
				window.open("ReceiptAndPayment_Abstract.pdf","mainFrame");	 break;
			case "82":
				window.open("ReceiptAndPayment_Detail.pdf","mainFrame");	 break;
			case "83":
				window.open("BudgetEstimates.pdf","mainFrame");	 break;
			default:
				window.open("Home.htm","mainFrame");	 break;

		}
	}
}

function NamTreeMouseMove(div){	
	/*var tr	= div.NamTree;
	if (!tr) return(true); //not on a tree. get out of here.
	var dragging	= tr.beingDragged;
	
	if (event.button == 0){ //normal mouse move (no button pressed)
		tr.beingDragged = false; //play it safe
		ChangeCursor(-1);
		return (true);
	}
	
	// this is a drag
	var ele		= event.srcElement;
	var node	= ele.NamNode;
	if (node && !dragging){ //see if this is the first move for the node to grag
		if (node == tr.selectedNode){ //yes the mouse is on the node on which it was depressed
			tr.beingDragged = true;
			dragging		= true;
			if (node.childCount>0)SetCursor(2);
			else SetCursor(0);
		}
	}
	if (dragging){

		if( node  && tr.selectedNode.canAttachTo(node)){
			ChangeCursor(0);
		}else{
			ChangeCursor(1);
		}	
	}
	return (true);
	*/
}

function NamTreeMouseUp(div){
	var tr	= div.NamTree;
	if(!tr) return (true);
	
	node = event.srcElement.NamNode;
	
	if(tr.beingDragged){ //drop it
		ChangeCursor(-1);
		if (node){ //it is dropped on a node
			var linkOK = tr.link(tr.selectedNode.id, node.id,true);
			if (linkOK){
				tr.display();
			}
		}
	}
	tr.beingDragged = false;
}

function NamTreeMouseOut(div){	
	var ele		= event.srcElement;
	var node	= ele.NamNode;
	var node	= event.srcElement.NamNode;
	
	if(!node) return (true);//not our domain

	var tr		= node.tree;
	var oldnode = tr.selectedNode;
	if(oldnode != null)
	{
		if(node.id != oldnode.id)		
			node.element.style.color = 'navy';
		else
			oldnode.element.style.color = 'white';			
	}
	else
		node.element.style.color = 'navy';
		
	node.element.style.fontSize="12";
}

function NamTreeMouseOver(div){	
	
	var node = event.srcElement.NamNode;
	
	if (node){
		node.element.style.textDecorationUnderline = false;
		node.element.style.cursor="hand";
		node.element.style.color="red";		
		node.element.style.fontSize="13";
		
		var tr		= node.tree;
		var oldnode = tr.selectedNode;
		if(oldnode != null )			
			if(node.id == oldnode.id)		
				node.element.style.color = 'white';
	}	
	
	
	//var oldnode = tr.selectedNode;
	//bootbox.alert(oldnode.id);
	//if(oldnode != null )
			
		//if(node.id == oldnode.id)		
			//node.element.style.color = 'white';
	
	//
}

function NamTreeRightClick()
{

	var node = gTree.selectedNode;
	if (node){	
		var a = document.getElementById('contextMenu').style;
		
		/***********************************/
		/* pop up at the place mouse click */
		/***********************************/
		
		var posx = 0, posy = 0;		
		var e = window.event;		
		posx = e.clientX + document.body.scrollLeft;
		posy = e.clientY + document.body.scrollTop;			
		
		a.left = e.clientX>150 ? posx-130 : posx;
		a.top  = e.clientY>345 ? posy-108 : posy;
		
		a.width = 130;	a.height = 100;		
		
		a.visibility = 'visible';		
		/***********************************/		
		
		var f = document.frm ;		
		//f.nodeName.value = node.name;
		//f.nodeName.focus();
		if(node.childCount > 0){
			f.expandAll.enabled = true;
		}else{
			f.expandAll.enabled = false;
		}
	}else{
		alert ('First select a node by clicking on that, and then right-click for a menu');
	}
	return(false);
}

gCursors	= new Array();;//cursor styles
gBase		= 0; // 0=leaf 2=node
gCurrentCursor= -1; //points to gCursors that is currently active 

function SetCursor(base){ //called as preparation for drag or to reset
	if(gCursors.length == 0){
		gCursors.push(document.getElementById('okLeaf').style);
		gCursors.push(document.getElementById('notokLeaf').style);
		gCursors.push(document.getElementById('okNode').style);
		gCursors.push(document.getElementById('notokNode').style);
	}
	gBase = base; 
}

function ChangeCursor(typ){ //called when nod is dragged
	if (typ == -1){ //off
		if(gCurrentCursor >= 0) {
			gCursors[gCurrentCursor].visibility = 'hidden';
			gCurrentCursor = -1;
		}
		return(true);
	}
	
	var i = gBase + typ;
	if (gCurrentCursor >=0 && i != gCurrentCursor){
		gCursors[gCurrentCursor].visibility = 'hidden';
	}

	var a		= gCursors[i];
	a.visibility= 'visible';
	a.left		= event.clientX +2;// if there is no gap, mouse will move on this div.
	a.top		= event.clientY +2;
	
	gCurrentCursor = i;
	return(true);
}

var gTimerId	= null;

function ContextMouseMove(){
	if (gTimerId) window.clearTimeout(gTimerId);
}

function ContextMouseOut(){
	gTimerId = setTimeout("HideContext();",500);
}

function HideContext(){
	var a = document.getElementById('contextMenu').style;
	a.visibility = 'hidden';
}

function ExpandAll(){
	var tr   = gTree;
	var node = tr.selectedNode;
	if (node.childCount > 0){
		node.expandAll();
		tr.display();
	}
	HideContext();
}

function CollapseAll(){	
	var tr   = gTree;
	var node = tr.selectedNode;

	if (node.childCount > 0){
		node.collapseAll();
		tr.display();
	}
	HideContext();
}

function Undo(){
	gTree.undo();
	HideContext();
}

function BtnClicked(){
	var n = parseInt(document.frm.nbrNodes.value);
	if (n>999){
		n = 999;
		document.frm.nbrNodes.value;
	}
	var div = document.getElementById('treeDiv');
	var t = new NamTree(div);
	for(var i=1; i<=n; i++) t.createNode(i, 'Node'+i, '', '');
	t.display();
	
}

//debugging tools
function AlertProperties(o){
	var s = '';
	for (var a in o) s+= a +'=' +o[a] +'\n';
	alert (s);
}

function WinProperties(o){
	var s = '';
	for (var a in o) s+= a +'=' +o[a] +'<br>';
	var win = window.open();
	win.document.write(s);
	win.document.end();
}

// To get XML document as input Draw the tree for the XML document structure

// stack to hold the previous parent Node

var top=0;
var stack = new Array(10);

function pushx(x1)
{
  top++;
  stack[top]=x1;
  return;
}

function popx()
{
 var x1 = stack[top];
 top--;
 return x1;
}  
  

var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");


function NamTreeLoadXML(xmlFile) 
{
 var xmlObj;
 xmlDoc.async="false"; 
 xmlDoc.onreadystatechange=verify; 
 xmlDoc.load(xmlFile); 
 xmlObj=xmlDoc.documentElement; 
 return xmlObj;
}


function verify() 
{ 
 // 0 Object is not initialized 
 // 1 Loading object is loading data 
 // 2 Loaded object has loaded data 
 // 3 Data from object can be worked with 
 // 4 Object completely initialized 
 if (xmlDoc.readyState != 4) 
 { 
   return false; 
 } 
}

var n=1;
var inp=1;

function NamTreeCreateNodes(obj)
{  
  if(obj.hasChildNodes())
  { 
    pushx(inp); 
    inp=n;
    for(var i=0;i<obj.childNodes.length;i++) 
     {	 	
		//bootbox.alert(obj.childNodes(i).getAttribute("id") + obj.childNodes(i).getAttribute("name"));
         this.createNode(++n,obj.childNodes(i).getAttribute("name"),inp,obj.childNodes(i).getAttribute("id"));
          this.createNodes(obj.childNodes[i]); 
     }  
     inp = popx();      
  }
  else return; 
}

function SearchNode(flag)
{
	var str = new String();
	str = document.frm.searchName.value;
	var nodename;
	var PNode, mainnode, nodes;	
	nodes = gTree.nodeCount;
	var a=1;
	if(gTree.selectedNode == null)
		a=1;
	else
		a = gTree.selectedNode.id+1;
		
	/*if(flag)
	{
		a=curNodeId;
		if(curNodeStr)
			str = curNodeStr;		
	}*/
	
	if (str)
	{
		for (a ; a<=nodes ; a++)
		{	
			node	= gTree.nodes[a];			
			nodename = node.name.toLowerCase();
			str = str.toLowerCase();						
			//bootbox.alert(node.id);
			if(nodename.indexOf(str) >= 0)			
			{
				curNodeId = node.id;
				curNodeStr = str;
				curNodeId++;
				PNode = node;
				mainnode = node;				
				var tr = node.tree;
				
				//node.alternate();
				//node.expanded = true;
				//tr.display();
				while(PNode.parent != null)
				{
					PNode = PNode.parent;					
					node = PNode;
					tr = node.tree;
					//node.alternate();
					node.expanded = true;
					tr.display();
					//PNode = PNode.parent;					
				}
				//gTree.nodes[1].expanded = gTree.nodes[1].expanded?:true;
				
				if(!gTree.nodes[1].expanded) 
				{	
					gTree.nodes[1].expanded = true;
					tr.display();
				}
				mainnode.element.style.backgroundColor = 'navy';
				mainnode.element.style.color = 'white';
				var tr		= node.tree;
				tr.selectedNode = mainnode;
				window.status = mainnode.name;
				window.open(gTree.hrefTarget + '?id=' + mainnode.nodeid,"mainFrame");
				
				/******************************/
				/* scrolling to the searched node */
				/******************************/
				
				var cells = document.getElementsByTagName('TD');				
				var  len = cells.length, i=1;	//o'th row contains nothing, it is used for spacing only			
				
				str1 = cells[i++].childNodes[3].innerText;							
				str1 = str1.toLowerCase();		
				
				while(nodename != str1 || i <= curScrollPosition)
				{	
					//bootbox.alert(len + ', ' + nodename + ', ' + str1);					
					str1 = cells[i++].childNodes[3].innerText;														
					str1 = str1.toLowerCase();
				}		
								
				window.scrollTo(0, i*20);
				curScrollPosition = i;				
				/******************************/
				
				break;
			}//if closed
			//if(node.id == nodes) bootbox.alert("done ");
			if(node.id == nodes) {gTree.selectedNode=null; bootbox.alert("Not Found");curScrollPosition=0;}
		}//for closed			
	}
	else
	{
		bootbox.alert('Search string is null');
	}
}