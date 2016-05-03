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
 
/*
---------------------------------------------------------------------
 File Name	:  CommonMenus.js
 Author		:  Exilant
---------------------------------------------------------------------
*/

/*
----------------------------------------
Global Variables
----------------------------------------
*/
// variable used to build the title of the current page
var pageTitle = "";

// variable used to build customized Header of the current page
var pageHeader = "";

// variable used to store the IDs of the menutitles for the left side menu
var IDList = new Array();

// Variable to hold the name of the user who is currently logged in

var userID = ""

// Variable to hold that state of the Alert button

var alertButton = false;

// Variable to hold whether the user should even see the Alert

var showAlert = false;

ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false

/*
----------------------------------------
Global Paths
----------------------------------------
*/
var imgPath = "images/";

/* ----------------------------------------
   Function Name:  changeto
   Description:  Function changes the background colour of a cell
   Parameter(s): 1. highlightcolor: color to change the background to
   Used for the "flashing" Alert button.
*/
function doNothing(){
//does nothing
}


function changeto(){
	bootbox.alert("This is being reached");
	one.style.backgroundColor="#FFCE00";
	setInterval("changto()", 1200);
	one.style.backgroundColor="#DADADA";
	//setinterval(changeback(), 200);
	return;
}

// Show/Hide functions for non-pointer layer/objects
function show(id) {
        if (ns4) document.layers[id].visibility = "show"
        else if (ie4) document.all[id].style.visibility = "visible"
}

function hide(id) {
        if (ns4) document.layers[id].visibility = "hide"
        else if (ie4) document.all[id].style.visibility = "hidden"
}

function swap(idhide, idshow)
{
	//bootbox.alert("This is running");
	hide(idhide);
	show(idshow);
}

function swapall(idshow, hide1, hide2, hide3, hide4, hide5, hide6)
{
	//bootbox.alert("This is running");
	if (ns4) document.layers[hide1].visibility.all = "hide"
        else if (ie4) document.all[hide1].style.visibility = "hidden"
	if (ns4) document.layers[hide2].visibility.all = "hide"
        else if (ie4) document.all[hide2].style.visibility = "hidden"
	if (ns4) document.layers[hide3].visibility.all = "hide"
        else if (ie4) document.all[hide3].style.visibility = "hidden"
	if (ns4) document.layers[hide4].visibility.all = "hide"
        else if (ie4) document.all[hide4].style.visibility = "hidden"
	if (ns4) document.layers[hide5].visibility.all = "hide"
        else if (ie4) document.all[hide5].style.visibility = "hidden"
	if (ns4) document.layers[hide6].visibility.all = "hide"
        else if (ie4) document.all[hide6].style.visibility = "hidden"

	show(idshow);
}

function swapall2(idshow, hide1, hide2)
{
	//bootbox.alert("This is running");
	if (ns4) document.layers[hide1].visibility.all = "hide"
        else if (ie4) document.all[hide1].style.visibility = "hidden"
	if (ns4) document.layers[hide2].visibility.all = "hide"
        else if (ie4) document.all[hide2].style.visibility = "hidden"
	show(idshow);
}

function CheckAll(formName)
{
	//bootbox.alert("This function is running " + document.IncludeForm1.selectAll.checked)
	//bootbox.alert(formName.elements.length);

	for (var i=0;i<formName.elements.length;i++)
	{
		var e = formName.elements[i];
		if ((e.name != 'selectAll') && (e.type=='checkbox'))
		e.checked = formName.selectAll.checked;
	}
}

/* ----------------------------------------
   Function Name:  changeback
   Description:  Function changes the background colour of a cell
   Parameter(s): 1. highlightcolor: color to change the background to
   Used for the "flashing" Alert button.
   NOT Used
*/
function changeback(){
	one.style.backgroundColor="#DADADA";
	setinterval(changeto(), 200);
}

/* ----------------------------------------
   Function Name:  newImage
   Description:  Function takes a url and creates an image object.
   Parameter(s): 1. Arg: url used to create image
*/

function newImage(arg) {
	if (document.images) {
		rslt = new Image();
		rslt.src = arg;
		return rslt;
	}
}


/* ----------------------------------------
   Function Name:  changeImages
   Description:  Function used to switch images during mouseover events.  It assigns every 2nd arg as the source of the the arg preceeding it.
   Parameter(s): Takes an unlimited number of paired arguments.  1. Image Name   2. Path to an Image
*/

function changeImages() {
	if (document.images && (preloadFlag == true)) {
		for (var i=0; i<changeImages.arguments.length; i+=2) {
			document[changeImages.arguments[i]].src = changeImages.arguments[i+1];
		}
	}
}


/* ----------------------------------------
   Function Name:  preloadImages
   Description:  Function preloads mouseover images and assigns flag so changeimage is never called unless all images are loaded.
   Parameter(s): None.
*/

var preloadFlag = false;

function preloadImages() {
	if (document.images) {
		preloadFlag = true;
	}
}


/* ----------------------------------------
   Function Name:  newWindow
   Description:  Function takes a URl as an argument an opens it in a new window.
   Parameter(s): 1. URL: url to be displayed in the new window.
*/

function newWindow(URL) {
	new_window = window.open(URL,"newwin","height=450,width=860,scrollbars,resizable");
	new_window.focus();

}

function newWindow(URL, width, height) {
	var strArg = "height=" + height + ",width=" + width + ",scrollbars,resizable"

	new_window = window.open(URL,"newwin", strArg);
	new_window.focus();

}
/* ----------------------------------------
   Function Name:  helpWindow
   Description:  Function takes a URl as an argument an opens it in a new window.
   Parameter(s): 1. URL: url to be displayed in the new window.
*/

function helpWindow(URL) {
	help_window = window.open(URL,"helpwin","height=450,width=750,scrollbars,resizable");
	help_window.focus();
}


function setOptionalHeader(header) {

	pageHeader = header;
}

/* ----------------------------------------
   Function Name:  createCommonMenus
   Description:  Function creates the common headers and sidemenus for any applicable pages.
   Parameter(s): 1. currentMenuTitle: current menu title of page user is viewing
                 2. currentMenuItem: current menu item of page user is viewing
				 3. UserName: optional parameter that may vary the home page based on user
*/

function createCommonMenus(currentMenuTitle, currentMenuItem, userName) {

	// if no userName has been specified, then assign it to an empty string
	if (!userName) userName="";
	
	document.write("<TABLE height=100% cellSpacing=0 cellpadding=0 width=100% border=0>");
  	document.write("<FORM NAME='commonMenu'>");
	document.write("<TR valign='top'>");
    	document.write("    <TD width=100% colspan=3>");

	/* main header */
	createHeader(userName);

	document.write("     </TD>");
	document.write("</TR>");

	/* messages and admin */
	/*
  	document.write("<TR valign='top'>");
    	document.write("    <TD width=100% colspan=3>");

	createMsgAdmin();

	document.write("     </TD>");
	document.write("</TR>");
	*/

	/* SideMenus */
  	document.write("<TR valign='top'>");
   	document.write("    <TD width=185 height=100%>");

	document.write("		<TABLE width=185 cellpadding=2 cellspacing=1 border=0>");
	document.write("		<TR valign='top'>");
	document.write("	     	<TD align='left'>");

	createSideMenu(currentMenuTitle, currentMenuItem, userName);

	document.write("     	 	</TD>");
	document.write("		</TR>");
	document.write("		</TABLE>");
	document.write("     </TD>");

	/* dividing line */
	document.write("    <TD align=left width=10 height=100% background=" + imgPath + "separator.gif>");
	document.write("    &nbsp;</TD>");


	document.write("    <TD width=100%>");

	/* display the title of the main content area of the page */
	displayPageTitle(userName);
	
	// can now begin main area page content
	document.write("</FORM>");
}


/* ----------------------------------------
   Function Name:  createHeader
   Description:  Function creates the Hedaer area of the page.
   Parameter(s): None
*/

function createHeader(userName) {

	document.write("    	<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
	document.write("    	   	<tr bgcolor='#FFCC00'>");
	document.write("    	 	    <td valign='top' rowspan='2' width='220'><img src='" + imgPath + "rbfglogo.gif' width='220' height='50' border='0' alt='Royal Bank Financial Group'></td>");
	document.write("    		    <td valign='top' rowspan='2'>&nbsp;</td>");
	document.write("    		    <td valign='top' align='right'><a href='#'><img src='images/rbfghome.gif' width='85' height='25' border='0' alt='RBFG Home'></a><a href='#'><img src='images/legal.gif' width='43' height='25' border='0' alt='Legal'></a><a href='#'><img src='images/francais.gif' width='58' height='25' border='0' alt='Francais'></a></td>");
	document.write("    		    <td valign='top' width='160' rowspan='2'><img src='images/leafs.gif' width='160' height='50' border='0' alt=''></td>");
	document.write("    		</tr>");
	document.write("    		<tr bgcolor='#FFCC00'>");
	document.write("    		    <td valign='top' align='right'><img src='images/yellowblank.gif' width='8' height='25' border='0' alt=''>");

	// Customize the home page link by user
	if (userName == "Client1")
	{
		document.write("<a href='wcm-home.htm'>");
		document.write("<img src='images/home.gif' width='46' height='25' border='0' alt='WCM Home'></a><a href='#'><img src='images/mail.gif' width='39' height='25' border='0' alt='Mail to & from Bank'></a>");
		//<A HREF='#' onMouseOver=popUp('HM_Menu1',event) onMouseOut=popDown('HM_Menu1')><img src='images/quiklink.gif' border='0' alt='Quick Links to Tools'></a>
		document.write("<a href='userpreferences.htm'>");
	}
	else if (userName == "Client2")
	{
		document.write("<a href='WCM-ca-home.htm'>");
		document.write("<img src='images/home.gif' width='46' height='25' border='0' alt='WCM Home'></a><a href='#'><img src='images/mail.gif' width='39' height='25' border='0' alt='Mail to & from Bank'></a>");
		//<A HREF='#' onMouseOver=popUp('HM_Menu2',event) onMouseOut=popDown('HM_Menu2')><img src='images/quiklink.gif' border='0' alt='Quick Links to Tools'></a>
		document.write("<a href='ca-userpreference.htm'>");
	}
	else if (userName == "RBFG")
	{
		document.write("<a href='WCM-bu-home.htm'>");
		document.write("<img src='images/home.gif' width='46' height='25' border='0' alt='WCM Home'></a><a href='#'><img src='images/mail.gif' width='39' height='25' border='0' alt='Mail to & from Bank'></a>");
		//<A HREF='#' onMouseOver=popUp('HM_Menu3',event) onMouseOut=popDown('HM_Menu3')><img src='images/quiklink.gif' border='0' alt='Quick Links to Tools'></a>
		document.write("<a href='bu-userpreferences.htm'>");
	}
	else
	{
		document.write("<a href='wcm-home.htm'>");
		document.write("<img src='images/home.gif' width='46' height='25' border='0' alt='WCM Home'></a><a href='#'><img src='images/mail.gif' width='39' height='25' border='0' alt='Mail to & from Bank'></a>");
		//<A HREF='#' onMouseOver=popUp('HM_Menu1',event) onMouseOut=popDown('HM_Menu1')><img src='images/quiklink.gif' border='0' alt='Quick Links to Tools'></a>
		document.write("<a href='userpreferences.htm'>");
	}

	document.write("<img src='images/preferences.gif' border='0' alt='User Preferences'></a><a href=javascript:helpWindow('demo-script.htm')><img src='/egi/resources/erp2/images/help.gif' width='41' height='25' border='0' alt='Online Services'></a><a href=\"javascript:resetState();window.location.href='../logout.jsp';\"><img src='images/logout.gif' width='54' height='25' border='0' alt='Logout'></a></td>");

	document.write("    		</tr>");
	document.write("    	</table>");
}


/* ----------------------------------------
  -- NO LONGER USED --
   Function Name:  createMsgAdmin
   Description:  Function creates the Message and Admin areas of the page.
   Parameter(s): None
*/

function createMsgAdmin() {


	document.write("<TABLE class='subheaderbg' width=100% height=36 cellpadding=2 cellspacing=1 border=0>");
	document.write("<TR>");
    	document.write("    <TD>");

	/* Message Area */
	document.write("		<TABLE class='messageareabg' width=100% height='30' cellpadding=0 cellspacing=0 border=0>");
	document.write("		<TR align=middle nowrap>");
	document.write("	    	<TD class='welcometext' align='center' width='40%'>");
	document.write("	     		&nbsp;&nbsp;Welcome&nbsp;Jerome&nbsp;!");
	document.write("     	     </TD>");
	document.write("	     	<TD class='messagetext' align='center' width='60%'>");
	document.write("	     		You have <a class='messagenumlink' href='#'>5 Messages</a>");
	document.write("     	     </TD>");
	document.write("		</TR>");
	document.write("		</TABLE>");

    	document.write("    </TD>");
   	document.write("    <TD width=400>");

    	/* Admin Area */
	document.write("		<TABLE width=100% height='32' cellpadding=0 cellspacing=0 border=0>");
	document.write("		<TR>");
	document.write("	     	<TD align='left'>&nbsp;&nbsp;");
	document.write("	     		<a class='adminlink' href='wcm-settings.htm'>Web Cash Settings</a>&nbsp;&nbsp;");
	document.write("	     		<a class='adminlink' href='admin-userlist.htm'>Manage Users</a>&nbsp;&nbsp;");
	document.write("	     		<a class='adminlink' href='#'>Audit Log</a>&nbsp;&nbsp;");
	document.write("	     		<a class='adminlink' href='#'>Preferences</a>&nbsp;&nbsp;");
	document.write("     	     </TD>");
	document.write("		</TR>");
	document.write("		</TABLE>");
    	document.write("    </TD>");
	document.write("</TR>");
	document.write("</TABLE>");

}

/* ----------------------------------------
   Function Name:  createSideMenu
   Description:  Function creates the Left Side Menu based on the username used to log in.
   Parameter(s): None
*/

function createSideMenu(currentMenuTitle, currentMenuItem, userName) {

	if (userName == "Client1")
		buildSideMenu(currentMenuTitle, currentMenuItem);
	else if (userName == "Client2")
		buildClient2SideMenu(currentMenuTitle, currentMenuItem);
	else if (userName == "RBFG")
		buildBankSideMenu(currentMenuTitle, currentMenuItem);
	else
		buildSideMenu(currentMenuTitle, currentMenuItem);

}



/* ----------------------------------------
   Function Name:  buildSideMenu
   Description:  Function creates the side menus for a user, a regular client user
   Parameter(s): None
*/

function buildSideMenu(currentMenuTitle, currentMenuItem) {

	var menutext1 = new Array(6);
	menutext1[0] = new Array("#.htm","ACH Payments","sidemenutitlenew");
	menutext1[1] = new Array("ach-selectserviceac.htm","Create Online Payment","sidemenuitem");
	menutext1[2] = new Array("#","Inquire Online Payments","sidemenuitem");
	menutext1[3] = new Array("#","Create Payment File","sidemenuitem");
	menutext1[4] = new Array("#","Inquire Payment Records","sidemenuitem");
	menutext1[5] = new Array("#","Host Reports","sidemenuitem");

	var menutext2 = new Array(4);
	menutext2[0] = new Array("#","Wire Payments","sidemenutitlenew");
	menutext2[1] = new Array("wires-create.htm","Create Wire","sidemenuitem");
	menutext2[2] = new Array("#","Inquire Wires","sidemenuitem");
	menutext2[3] = new Array("#","Manage Templates","sidemenuitem");

/*  ken took this away
	var menutext3 = new Array(3);
	menutext3[0] = new Array("#","Account Transfers","sidemenutitlenew");
	menutext3[1] = new Array("cu-ATCreateTransfer.htm","Create Transfer","sidemenuitem");
	menutext3[2] = new Array("#","Inquire Transfer","sidemenuitem");
*/ 
	var menutext3 = new Array(6);
	menutext3[0] = new Array("cu-ATHomePage.htm","Account Transfers","sidemenutitlenew");
	menutext3[1] = new Array("cu-ATCreateTransfer.htm","Create","sidemenuitem");
	menutext3[2] = new Array("cu-ATApprove.htm","Approve","sidemenuitem");
	menutext3[3] = new Array("cu-ATRelease.htm","Release","sidemenuitem");
	menutext3[4] = new Array("cu-ATModify.htm","Modify","sidemenuitem");
	menutext3[5] = new Array("cu-ATImport.htm","Import","sidemenuitem");
	menutext3[6] = new Array("cu-ATReports.htm","Reports","sidemenuitem");
	

	var menutext4 = new Array(3);
	menutext4[0] = new Array("#","Balance Reporting","sidemenutitlenew");
	menutext4[1] = new Array("bt-choosebalancereport.htm","View Balances","sidemenuitem");
	menutext4[2] = new Array("bt-choosetransactionreport.htm","View Transactions","sidemenuitem");

	//var menutext5 = new Array(3);
	//menutext5[0] = new Array("#","Future 1","sidemenutitlenew");
	//menutext5[1] = new Array("#","Future 1 Option 1","sidemenuitem");
	//menutext5[2] = new Array("#","Future 1 Option 2","sidemenuitem");

	//var menutext6 = new Array(3);
	//menutext6[0] = new Array("#","Future 2","sidemenutitlenew");
	//menutext6[1] = new Array("#","Future 2 Option 1","sidemenuitem");
	//menutext6[2] = new Array("#","Future 2 Option 2","sidemenuitem");

	var menutext7 = new Array(2);
	menutext7[0] = new Array("#","Administration","sidemenutitlenew");
	menutext7[1] = new Array("#","View Broadcasts","sidemenuitem");

	var menutext8 = new Array(2);
	menutext8[0] = new Array("#","Preferences","sidemenutitlenew");
	menutext8[1] = new Array("cu-userpreference.htm","User Preferences","sidemenuitem");

	// identify current title and selected item
	if (currentMenuTitle) {
		if (eval("menutext" + currentMenuTitle)) {
			eval("menutext" + currentMenuTitle + "[0][4]=\"open\"");

			if (currentMenuItem != 0) {
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][4]=\"selected\"");
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][5]=\"sidemenuselected\"");
			}
		}
	}

	// create and display the menu items

	createSideMenuEntry(menutext4,4);
	createSideMenuEntry(menutext3,3);
	createSideMenuEntry(menutext1,1);
	createSideMenuEntry(menutext2,2);
	//createSideMenuEntry(menutext5,5);
	//createSideMenuEntry(menutext6,6);
	createSideMenuEntry(menutext7,7);
	createSideMenuEntry(menutext8,8);
}


/* ----------------------------------------
   Function Name:  buildClient2SideMenu
   Description:  Function creates the side menus for the user Client2 (client admin)
   Parameter(s): None
*/

function buildClient2SideMenu(currentMenuTitle, currentMenuItem) {

	var menutext1 = new Array(10);
	menutext1[0] = new Array("#","ACH Payments","sidemenutitlenew");
	menutext1[1] = new Array("#","Create Online Payment","sidemenuitem");
	menutext1[2] = new Array("#","Manage Online Payments","sidemenuitem");
	menutext1[3] = new Array("#","Inquire Online Payments","sidemenuitem");
	menutext1[4] = new Array("ach-alert-nomsg.htm","Approve Online Payments","sidemenuitem");
	menutext1[5] = new Array("#","Create Payment File","sidemenuitem");
	menutext1[6] = new Array("#","Manage ACH Record Set","sidemenuitem");
	menutext1[7] = new Array("#","Inquire Payment Records","sidemenuitem");
	menutext1[8] = new Array("#","Approve Payment File","sidemenuitem");
	menutext1[9] = new Array("#","Host Reports","sidemenuitem");

	var menutext2 = new Array(5);
	menutext2[0] = new Array("#","Wire Payments","sidemenutitlenew");
	menutext2[1] = new Array("#","Create Wire","sidemenuitem");
	menutext2[2] = new Array("wires-alert.htm","Approve Wires","sidemenuitem");
	menutext2[3] = new Array("#","Inquire Wires","sidemenuitem");
	menutext2[4] = new Array("#","Manage Templates","sidemenuitem");

	var menutext3 = new Array(4);
	menutext3[0] = new Array("#","Account Transfers","sidemenutitlenew");
	menutext3[1] = new Array("#","Create Transfer","sidemenuitem");
	menutext3[2] = new Array("transfer-alert-nomsg.htm","Approve Transfers","sidemenuitem");
	menutext3[3] = new Array("#","Inquire Transfer","sidemenuitem");

	var menutext4 = new Array(3);
	menutext4[0] = new Array("#","Balance Reporting","sidemenutitlenew");
	menutext4[1] = new Array("ca-bt-choosebalancereport.htm","View Balances","sidemenuitem");
	menutext4[2] = new Array("ca-bt-choosetransactionreport.htm","View Transactions","sidemenuitem");

	//var menutext5 = new Array(3);
	//menutext5[0] = new Array("#","Future 1","sidemenutitlenew");
	//menutext5[1] = new Array("#","Future 1 Option 1","sidemenuitem");
	//menutext5[2] = new Array("#","Future 1 Option 2","sidemenuitem");

	//var menutext6 = new Array(3);
	//menutext6[0] = new Array("#","Future 2","sidemenutitlenew");
	//menutext6[1] = new Array("#","Future 2 Option 1","sidemenuitem");
	//menutext6[2] = new Array("#","Future 2 Option 2","sidemenuitem");

	var menutext7 = new Array(5);
	menutext7[0] = new Array("#","Administration","sidemenutitlenew");
	menutext7[1] = new Array("ca-home-permission.htm","Manage User","sidemenuitem");
	menutext7[2] = new Array("ca-preferences.htm","Client Preferences","sidemenuitem");
	menutext7[3] = new Array("cu-auditlog-selection.htm","Audit Log","sidemenuitem");
	menutext7[4] = new Array("#","View Broadcasts","sidemenuitem");

	var menutext8 = new Array(2);
	menutext8[0] = new Array("#","Preferences","sidemenutitlenew");
	menutext8[1] = new Array("ca-userpreference.htm","User Preferences","sidemenuitem");

	// identify current title and selected item
	if (currentMenuTitle) {
		if (eval("menutext" + currentMenuTitle)) {
			eval("menutext" + currentMenuTitle + "[0][4]=\"open\"");

			if (currentMenuItem != 0) {
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][4]=\"selected\"");
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][5]=\"sidemenuselected\"");
			}
		}
	}

	// create and display the menu items
	createSideMenuEntry(menutext4,4);
	createSideMenuEntry(menutext3,3);
	createSideMenuEntry(menutext1,1);
	createSideMenuEntry(menutext2,2);
	//createSideMenuEntry(menutext5,5);
	//createSideMenuEntry(menutext6,6);
	createSideMenuEntry(menutext7,7);
	createSideMenuEntry(menutext8,8);
}



/* ----------------------------------------
   Function Name:  buildBankSideMenu
   Description:  Function creates the side menus for a bank user
   Parameter(s): None
*/

function buildBankSideMenu(currentMenuTitle, currentMenuItem) {

	var menutext1 = new Array(6);
	menutext1[0] = new Array("#","Administration","sidemenutitlenew");
	menutext1[1] = new Array("bu-manage-client.htm","Manage Client","sidemenuitem");
	menutext1[2] = new Array("bu-manage-clientuser.htm","Manage Client User","sidemenuitem");
	menutext1[3] = new Array("bank-admin-home.htm","Manage Bank User","sidemenuitem");
	menutext1[4] = new Array("#","Broadcast Messages","sidemenuitem");
	menutext1[5] = new Array("bu-auditlog.htm","Audit Log","sidemenuitem");
	
	var menutext2 = new Array(2);
	menutext2[0] = new Array("#","Preferences","sidemenutitlenew");
	menutext2[1] = new Array("bu-userpreferences.htm","User Preferences","sidemenuitem");

	// identify current title and selected item
	if (currentMenuTitle) {
		if (eval("menutext" + currentMenuTitle)) {
			eval("menutext" + currentMenuTitle + "[0][4]=\"open\"");

			if (currentMenuItem != 0) {
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][4]=\"selected\"");
				eval("menutext" + currentMenuTitle + "[" + currentMenuItem + "][5]=\"sidemenuselected\"");
			}
		}
	}

	// create and display the menu items
	createSideMenuEntry(menutext1,1);
	createSideMenuEntry(menutext2,2);
}


/* ----------------------------------------
   Function Name:  createSideMenuEntry
   Description:  takes an array that has a menu entry with its sub items and displays it.
   Parameter(s): 1. menutext - 2 dimensional array with urls and captions of menu title and sub items.
   				- 3. class for menu title/sub item
*/

function createSideMenuEntry(menutext,entrynum) {

 	for (i=0; i < menutext.length; i++) {

			if (i == 0) {
				vname = "m" + entrynum;

				eval("var " + vname + "= new Menu(); " +
    	  			vname + ".name=\"" + vname + "\"; " +				
				vname + ".url=\"http://www.yahoo.com\";" +
				vname + ".styleclass=\"" + menutext[i][2] + "\";" +
		    	  	vname + ".caption=\"" + menutext[i][1] + "\";");


				addIDListEntry(vname);
				opened=getCookie(vname);
				if (opened == "true")
					eval(vname + ".isOpened=true;");


				if (menutext[i][4] == "open") {
					eval(vname + ".isOpened=true;");

					// pageTitle is a global variable used to build the title of the current page
					pageTitle=menutext[i][1];
				}

			}
			else{
				var vname="l" + entrynum;
 				eval("var " + vname + "= new MenuItem(); " +
			    	vname + ".url=\"" + menutext[i][0] + "\";" +
				vname + ".styleclass=\"" + menutext[i][2] + "\";" +
		        	vname + ".caption=\"" + menutext[i][1] + "\";" );

				if (menutext[i][4] == "selected") {
					eval(vname + ".selected=true;" +
					vname + ".styleselclass=\"" + menutext[i][5] + "\";");

					// pageTitle is a global variable used to build the title of the current page
					pageTitle += " - " + eval(vname + ".caption");
				}

    				eval("m" + entrynum + ".addMenuItem(l" + entrynum + ")");
			}
	}

	eval( "m" + entrynum + ".display()");
}



/* ----------------------------------------
   Function Name:  addIDListEntry
   Description:  adds an item to the global varibale array for side menu IDs
   Parameter(s): 1. idname : id of left side menu title

*/

function addIDListEntry(idname) {

	IDList[IDList.length] = idname;
}


function menuSelection(value)
{
	self.location=value;
}

/* ----------------------------------------
   Function Name:  displayPageTitle
   Description:  Function creates the page title that appears at the top of the current page based on the current menu item captions.
   Parameter(s): None.
*/

function displayPageTitle(userName) {

	document.write(" 		<table background=" + imgPath + "titlebg3.gif width='100%' cellpadding='0' cellspacing='0' border='0' style='height: 33px'>");
	document.write(" 		<tr>");

	// pageTitle is a global variable built in sidemenu creation (createSideMenuEntry)
	document.write("			<td align='left' class='pagetitle'>&nbsp;&nbsp;");

	if ( pageHeader == "" )	{

		if (pageTitle == "") {

			document.write(" 			Home"); /*width='100%'*/
			/*
			if (userName == "Client1")
				document.write("John");
			else if (userName == "Client2")
				document.write("Mary");
			else if (userName == "RBFG")
				document.write("Administrator");
			else
				document.write("John");
			*/
			document.write("			</td>");
			document.write("<script language='JavaScript1.2' src='javascript/hierArrays.js' type='text/javascript'><\/script>");
			document.write("<script language='JavaScript1.2' src='javascript/hierMenus.js' type='text/javascript'><\/script>");

		}else{
			document.write(pageTitle + "</td>");
		}

	}

	else {
		document.write(pageHeader + "</td>");
	}
	pageHeader = "";


	document.write(" 				<td align='center' class='messagetext'>User:&nbsp;");
	if (userName == "Client1")
	{		
			document.write("John Smith, SomeCorp");
			document.write("</td><td align='center' class='messagetext' valign='middle'>Quick Links&nbsp;");
			document.write("<style='height: 800px; scrollbar: no'>");
			document.write("<select name='quickLink' class='normalsmalltext' onChange='javascript:menuSelection(document.commonMenu.quickLink[this.selectedIndex].value)'>");
						
			document.write("<option value='#.htm' class='labeltext'><b>Balance Reporting</b></option>");
			document.write("<option value='bt-choosebalancereport.htm'>&nbsp;&nbsp;View Balances</option>");
			document.write("<option value='bt-choosetransactionreport.htm'>&nbsp;&nbsp;View Transactions</option>");
			document.write("<option value='cu-ATHomePage.htm' class='labeltext'><b>Account Transfers</b></option>");
			document.write("<option value='cu-ATCreateTransfer.htm'>&nbsp;&nbsp;Create</option>");
			document.write("<option value='cu-ATApprove.htm'>&nbsp;&nbsp;Approve</option>");
			document.write("<option value='cu-ATRelease.htm'>&nbsp;&nbsp;Release</option>");
			document.write("<option value='cu-ATModify.htm'>&nbsp;&nbsp;Modify</option>");
			document.write("<option value='cu-ATImport.htm'>&nbsp;&nbsp;Import</option>");
			document.write("<option value='cu-ATReports.htm'>&nbsp;&nbsp;Reports</option>");
			document.write("<option value='#.htm' class='labeltext'><b>ACH Payments</b></option>");
			document.write("<option value='ach-selectserviceac.htm'>&nbsp;&nbsp;Create Online Payment</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Inquire Online Payments</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Create Payment File</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Inquire Payment Records</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Host Reports</option>");
			document.write("<option value='#.htm' class='labeltext'><b>Wire Payments</b></option>");
			document.write("<option value='wires-create.htm'>&nbsp;&nbsp;Create Wire</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Inquire Wires</option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;Manage Templates</option>");
			document.write("<option value='#.htm' class='labeltext'><b>Administration</b></option>");
			document.write("<option value='#.htm'>&nbsp;&nbsp;View Broadcasts</option>");
			document.write("<option value='#.htm' class='labeltext'><b>Preferences</b></option>");
			document.write("<option value='cu-userpreference.htm'>&nbsp;&nbsp;User Preferences</option></select></style>");
	}
	else if (userName == "Client2")
	{
			document.write("Mary Wilson, SomeCorp");
			document.write("</td><td align='center' class='messagetext' valign='middle'>Quick Links&nbsp;");
			document.write("<select name='quickLink' class='normalsmalltext' onChange='javascript:menuSelection(document.commonMenu.quickLink[this.selectedIndex].value)'><option value='#.htm' class='labeltext'><b>Balance Reporting</b></option><option value='ca-bt-choosebalancereport.htm'>&nbsp;&nbsp;View Balances</option><option value='ca-bt-choosetransactionreport.htm'>&nbsp;&nbsp;View Transactions</option><option value='#.htm' class='labeltext'><b>Account Transfers</b></option><option value='#.htm'>&nbsp;&nbsp;Create Transfer</option><option value='transfer-alert-nomsg.htm'>&nbsp;&nbsp;Approve Transfers</option><option value='#.htm'>&nbsp;&nbsp;Inquire Transfer</option><option value='#.htm' class='labeltext'><b>ACH Payments</b></option><option value='#.htm'>&nbsp;&nbsp;Create Online Payment</option><option value='#.htm'>&nbsp;&nbsp;Manage Online Payments</option><option value='#.htm'>&nbsp;&nbsp;Inquire Online Payments</option><option value='ach-alert-nomsg.htm'>&nbsp;&nbsp;Approve Online Payments</option><option value='#.htm'>&nbsp;&nbsp;Create Payment File</option><option value='#.htm'>&nbsp;&nbsp;Manage ACH Record Set</option><option value='#.htm'>&nbsp;&nbsp;Inquire Payment Records</option><option value='#.htm'>&nbsp;&nbsp;Approve Payment File</option><option value='#.htm'>&nbsp;&nbsp;Host Reports</option><option value='#.htm' class='labeltext'><b>Wire Payments</b></option><option value='wires-create.htm'>&nbsp;&nbsp;Create Wire</option><option value='wires-alert.htm'>&nbsp;&nbsp;Approve Wires</option><option value='#.htm'>&nbsp;&nbsp;Inquire Wires</option><option value='#.htm'>&nbsp;&nbsp;Manage Templates</option><option value='#.htm' class='labeltext'><b>Administration</b></option><option value='ca-home-permission.htm'>&nbsp;&nbsp;Manage User</option><option value='ca-preferences.htm'>&nbsp;&nbsp;Client Preferences</option><option value='cu-auditlog-selection.htm'>&nbsp;&nbsp;Audit Log</option><option value='#.htm'>&nbsp;&nbsp;View Broadcasts</option><option value='#.htm' class='labeltext'><b>Preferences</b></option><option value='ca-userpreference.htm'>&nbsp;&nbsp;User Preferences</option></select>");
	}	
	else if (userName == "RBFG")
	{
			document.write("Administrator, Royal Bank");
			document.write("</td><td align='center' class='messagetext' valign='middle'>Quick Links&nbsp;");
			document.write("<select name='quickLink' class='normalsmalltext' onChange='javascript:menuSelection(document.commonMenu.quickLink[this.selectedIndex].value)'><option value='#.htm' class='labeltext'><b>Administration</b></option><option value='bu-manage-client.htm'>&nbsp;&nbsp;Manage Client</option><option value='bu-manage-clientuser.htm'>&nbsp;&nbsp;Manage Client User</option><option value='bank-admin-home.htm'>&nbsp;&nbsp;Manage Bank User</option><option value='#.htm'>&nbsp;&nbsp;Broadcast Messages</option><option value='bu-auditlog.htm'>&nbsp;&nbsp;Audit Log</option><option value='#.htm' class='labeltext'><b>Preferences</b></option><option value='cu-userpreference.htm'>&nbsp;&nbsp;User Preferences</option></select>");
	}
	else
	{
			document.write("John Smith, SomeCorp");
			document.write("</td><td align='center' class='messagetext' valign='middle'>Quick Links&nbsp;");
			document.write("<select name='quickLink' class='normalsmalltext' onChange='javascript:menuSelection(document.commonMenu.quickLink[this.selectedIndex].value)'><option value='#.htm' class='labeltext'><b>Balance Reporting</b></option><option value='bt-choosebalancereport.htm'>&nbsp;&nbsp;View Balances</option><option value='bt-choosetransactionreport.htm'>&nbsp;&nbsp;View Transactions</option><option value='#.htm' class='labeltext'><b>Account Transfers</b></option><option value='cu-ATCreateTransfer.htm'>&nbsp;&nbsp;Create Transfer</option><option value='#.htm'>&nbsp;&nbsp;Inquire Transfer</option><option value='#.htm' class='labeltext'><b>ACH Payments</b></option><option value='ach-selectserviceac.htm'>&nbsp;&nbsp;Create Online Payment</option><option value='#.htm'>&nbsp;&nbsp;Inquire Online Payments</option><option value='#.htm'>&nbsp;&nbsp;Create Payment File</option><option value='#.htm'>&nbsp;&nbsp;Inquire Payment Records</option><option value='#.htm'>&nbsp;&nbsp;Host Reports</option><option value='#.htm' class='labeltext'><b>Wire Payments</b></option><option value='wires-create.htm'>&nbsp;&nbsp;Create Wire</option><option value='#.htm'>&nbsp;&nbsp;Inquire Wires</option><option value='#.htm'>&nbsp;&nbsp;Manage Templates</option><option value='#.htm' class='labeltext'><b>Administration</b></option><option value='#.htm'>&nbsp;&nbsp;View Broadcasts</option><option value='#.htm' class='labeltext'><b>Preferences</b></option><option value='cu-userpreference.htm'>&nbsp;&nbsp;User Preferences</option></select>");
	}
	document.write("					</td>");
	//document.write("					<td align='right' class='quicklinkbuttonleft'><A HREF='#' onMouseOver=popUp('HM_Menu3',event) onMouseOut=popDown('HM_Menu3')>Quick Links</A></TD>");
	//document.write("					<td align='left' class='quicklinkbuttonright'><A HREF='#' onMouseOver=popUp('HM_Menu3',event) onMouseOut=popDown('HM_Menu3')><img src='" + imgPath + "down_arrow.gif' border='0'></a></TD>");
	//document.write("					</td>");

	if (showAlert == true){
		if (alertButton == true){
			//bootbox.alert("This is being reached");
			document.write("					<td id='one' align='right'><a href='wcm-home-client2-nomsg.htm'><img src='" + imgPath + "alert_button.gif' border='0'></a>");
			document.write("					</td>");
		}
		else{
			document.write("					<td align='center' class='quicklinkbutton'><a style='text-decoration: none; COLOR: #000000' href='wcm-home-client2-nomsg.htm'>Alerts</a>");
			document.write("					</td>");
		}
	}

	document.write(" 		</tr>");
	document.write(" 		</table>");
}

/* ----------------------------------------
   Function Name:  setState
   Description:  sets the state of the side menu titles (open/closed). Only for IE.
   Parameter(s): None

*/

function setState() {

  	if (navigator.appName=="Microsoft Internet Explorer"){
	    for (i=0; i < IDList.length; i++) {

		var menuChild = document.all[IDList[i] + "child"];
 		if (menuChild != null) {

			if (menuChild.style.display == "") {
				setCookie(IDList[i],true,365);
			}
			else{
				setCookie(IDList[i],false,365);
			}
		}
	    }
		setCookie("showAlert", showAlert, 365);
		setCookie("alertButton", alertButton, 365);
	}
}



/* ----------------------------------------
   Function Name:  resetState
   Description:  resets the state of the side menu titles (open/closed). Only for IE.
   Parameter(s): None

*/

function resetState() {

  	if (navigator.appName=="Microsoft Internet Explorer"){

		var start=0;
		var finish=0;

		for (i=0; finish != -1; i++) {

			finish = document.cookie.indexOf("=",start);
			idName = document.cookie.substring(start,finish);
			start=finish + 1;
			delCookie(idName);
		}
		delCookie("showAlert");
		delCookie("alertButton");
	}
}


/* ----------------------------------------
   Function Name:  createFooter
   Description:  Function creates the common footers for any applicable pages.
   Parameter(s): None
*/

function createFooter() {

	document.write("	<TABLE width=100% cellpadding=0 cellspacing=0 border=0>");
	document.write("	<TR>");
	document.write("	     <TD class='copyright' align='center'>");
	document.write("	     	<hr class='hrdivide' noshade>Copyright &copy; 2001 Royal Bank of Canada<br><br>");
	document.write("	     </TD>");
	document.write("	</TR>");
	document.write("	</TABLE>");

	document.write("     </TD>");
	document.write("</TR>");
	document.write("</TABLE>");
}

// -->