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

function PreloadImages() {
	var d = document;
	if (d.images) {                          
		var imageArray = new Array();
		var a = PreloadImages.arguments;
		for (var i = 0; i < a.length; i++) {
			imageArray[i] = new Image;
			imageArray[i].src = a[i];			
		}
	}
}

function setCookie(name, value, expiredays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie = escape(name) + '=' + escape(value) +
		((expiredays) ? '; expires=' + exdate.toGMTString() : '');
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

function compareStrings(s1, s2) {
	if (s1 < s2) { 
		return -1;
	}
	else if (s1 > s2) {
		return 1;
	}
	else {
		return 0;
	}
}

function get_getQuery() {
	var get = unescape(location.search.substring(1));
	var params = new Array();
	if (get != '') {
		var pairs = new Array();
		pairs = get.split('&');
		for (var i = 0; i < pairs.length; i++) {			
			params[i] = pairs[i].split('=');
		}
	}
	return params;
}

function getParamValue(name, params) {
	var lname = name.toLowerCase();	
	for (i = 0; i < params.length; i++) {
		if (lname == params[i][0].toLowerCase()) {
			return params[i][1];
		}
	}
	return '';
}

function findFrameEx(frameName, startFrame) {
	if (startFrame.frames.length == 0) return null;
	var lName = frameName.toLowerCase();
	for (var i = 0; i < startFrame.frames.length; i++) {
		var curFrame = startFrame.frames[i];
		if (lName == curFrame.name.toLowerCase()) {
			return curFrame;
		}
		else {
			var f = findFrameEx(frameName, curFrame);
			if (f) return f;
		}
	}
	return null;
}

function findFrame(frameName) {
	return findFrameEx(frameName, top);
}

function extractFileName(fullPath) {	
	var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
	var fileName = fullPath.substring(startIndex);
	if (fileName.indexOf('\\') === 0 || fileName.indexOf('/') === 0) {
		fileName = fileName.substring(1);
	}
	return fileName;
}

function removeBookmark(link) {
	if (!link || (link == '')) return '';
	var idx = link.lastIndexOf('#');
	if (idx >= 0) {
		return link.substring(idx, 0);
	}
	return link;
}
