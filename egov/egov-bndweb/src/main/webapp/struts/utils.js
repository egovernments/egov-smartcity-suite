/*-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
# accountability and the service delivery of the government  organizations.
# 
# Copyright (C) <2015>  eGovernments Foundation
# 
# The updated version of eGov suite of products as by eGovernments Foundation
# is available at http://www.egovernments.org
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program. If not, see http://www.gnu.org/licenses/ or
# http://www.gnu.org/licenses/gpl.html .
# 
# In addition to the terms of the GPL license to be adhered to in using this
# program, the following additional terms are to be complied with:
# 
# 1) All versions of this program, verbatim or modified must carry this
#    Legal Notice.
# 
# 2) Any misrepresentation of the origin of the material is prohibited. It
#    is required that all modified versions of this material be marked in
#    reasonable ways as different from the original version.
# 
# 3) This license does not grant any rights to any user of the program
#    with regards to rights under trademark law for use of the trade names
#    or trademarks of eGovernments Foundation.
# 
# In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
/*
 * $Id: utils.js 590812 2007-10-31 20:32:54Z apetrelli $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var StrutsUtils = {};

// gets an object with validation errors from string returned by 
// the ajaxValidation interceptor
StrutsUtils.getValidationErrors = function(data) {
  if(data.indexOf("/* {") == 0) {
    return eval("( " + data.substring(2, data.length - 2) + " )");
  } else {
    return null;
  }  
};

StrutsUtils.clearValidationErrors = function(form) {
  var firstNode = StrutsUtils.firstElement(form);
  var xhtml = firstNode.tagName.toLowerCase() == "table";
  
  if(xhtml) {
    clearErrorMessagesXHTML(form);
    clearErrorLabelsXHTML(form);
  } else {
    clearErrorMessagesCSS(form);
    clearErrorLabelsCSS(form);
  }
};  

// shows validation errors using functions from xhtml/validation.js
// or css_xhtml/validation.js
StrutsUtils.showValidationErrors = function(form, errors) {
  StrutsUtils.clearValidationErrors(form, errors);

  var firstNode = StrutsUtils.firstElement(form);
  var xhtml = firstNode.tagName.toLowerCase() == "table";  
  if(errors.fieldErrors) {
    for(var fieldName in errors.fieldErrors) {
      for(var i = 0; i < errors.fieldErrors[fieldName].length; i++) {
        if(xhtml) {
          addErrorXHTML(form.elements[fieldName], errors.fieldErrors[fieldName][i]);
        } else {
          addErrorCSS(form.elements[fieldName], errors.fieldErrors[fieldName][i]);
        }  
      }
    }
  }
};

StrutsUtils.firstElement  = function(parentNode, tagName) {
  var node = parentNode.firstChild;
  while(node && node.nodeType != 1){
    node = node.nextSibling;
  }
  if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
    node = StrutsUtils.nextElement(node, tagName);
  }
  return node;  
};

StrutsUtils.nextElement = function(node, tagName) {
  if(!node) { return null; }
  do {
    node = node.nextSibling;
  } while(node && node.nodeType != 1);

  if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
    return StrutsUtils.nextElement(node, tagName);
  }
  return node;  
};

StrutsUtils.previousElement = function(node, tagName) {
  if(!node) { return null; }
  if(tagName) { tagName = tagName.toLowerCase(); }
  do {
    node = node.previousSibling;
  } while(node && node.nodeType != 1);
  
  if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
    return StrutsUtils.previousElement(node, tagName);
  }
  return node;  
};

StrutsUtils.addOnLoad = function(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      oldonload();
      func();
    }
  }
};

StrutsUtils.addEventListener = function(element, name, observer, capture) {
  if (element.addEventListener) {
    element.addEventListener(name, observer, false);
  } else if (element.attachEvent) {
    element.attachEvent('on' + name, observer);
  }
};
