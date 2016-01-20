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
//Must be included in onload script for document. Locates and loads Pagemanager
//Once this is called, you should also include resetPageManager on pageunload

function loadPageManager(){
	var win = window;
	while (true) { //not going to be an infinite loop
		if (win.PageManager){
			window.exilWindowSaved = win.exilWindow;
			win.exilWindow = window;
			window.pageManagerWindow = win;
			window.PageManager = win.PageManager;
			window.PageValidator = win.PageValidator;
			return;
		}
		if (win.opener)win = win.opener;
		else if (win.parent && win.parent != win) win = win.parent;
		else {
			var script;
			var head = document.getElementsByTagName('head')[0];
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/ExilityParameters.js';
			head.appendChild(script);
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/PageManager.js';
			head.appendChild(script);
			script = document.createElement('script');
			script.language = 'javascript';
			script.src = '../Exility/PageValidator.js';
			head.appendChild(script);
bootbox.alert('You had not loaded Exility. Loading them as I show this. Take few secods to press OK so that Exility gets time to get loaded');

			return;
		}
	}
}

function unloadPageManager(){
	if (window.pageManagerWindow && window.exilWindowSaved){
		window.pageManagerWindow.exilWindow = window.exilWindowSaved
	}
}