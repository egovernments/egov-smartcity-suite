#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------
/*
 * $Id: validation.js 632335 2008-02-29 15:00:18Z jeromy $
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

function clearErrorMessages(form) {
    clearErrorMessagesXHTML(form);
}

function clearErrorMessagesXHTML(form) {

    // get field table
    var table;
    for (var i = 0; i < form.childNodes.length; i++) {
        if (form.childNodes[i].tagName != null && form.childNodes[i].tagName.toLowerCase() == 'table') {
            table = form.childNodes[i];
            break;
        }
    }

    if (table == null) {
        return;
    }

    // clear out any rows with an "errorFor" attribute
    var rows = table.rows;
    if (rows == null){
        return;
    }

    var rowsToDelete = new Array();
    for(var i = 0; i < rows.length; i++) {
        var r = rows[i];
        // allow blank errorFor values on dojo markup
        if (r.getAttribute("errorFor") != null) {
            rowsToDelete.push(r);
        }
    }

    // now delete the rows
    for (var i = 0; i < rowsToDelete.length; i++) {
        var r = rowsToDelete[i];
        table.deleteRow(r.rowIndex);
        //table.removeChild(rowsToDelete[i]); 
    }
}

function clearErrorLabels(form) {
    clearErrorLabelsXHTML(form);
}

function clearErrorLabelsXHTML(form) {
    // set all labels back to the normal class
    var elements = form.elements;
    for (var i = 0; i < elements.length; i++) {

        var parentEl = elements[i];
        // search for the parent table row, abort if the form is reached
        // the form may contain "non-wrapped" inputs inserted by Dojo
        while (parentEl.nodeName.toUpperCase() != "TR" && parentEl.nodeName.toUpperCase() != "FORM") {
            parentEl = parentEl.parentNode;
        }
        if (parentEl.nodeName.toUpperCase() == "FORM") {
            parentEl = null;
        }

         //if labelposition is 'top' the label is on the row above
        if(parentEl && parentEl.cells) {
          var labelRow = parentEl.cells.length > 1 ? parentEl : StrutsUtils.previousElement(parentEl, "tr");
          if (labelRow) {
              var cells = labelRow.cells;
              if (cells && cells.length >= 1) {
                  var label = cells[0].getElementsByTagName("label")[0];
                  if (label) {
                      label.setAttribute("class", "label");
                      label.setAttribute("className", "label"); //ie hack cause ie does not support setAttribute
                  }
              }
          }
        }
    }

}

function addError(e, errorText) {
    addErrorXHTML(e, errorText);
}

function addErrorXHTML(e, errorText) {
    try {
        var row = (e.type ? e : e[0]);
        while(row.nodeName.toUpperCase() != "TR") {
            row = row.parentNode;
        }
        var table = row.parentNode;
        var error = document.createTextNode(errorText);
        var tr = document.createElement("tr");
        var td = document.createElement("td");
        var span = document.createElement("span");
        td.align = "center";
        td.valign = "top";
        td.colSpan = 2;
        span.setAttribute("class", "errorMessage");
        span.setAttribute("className", "errorMessage"); //ie hack cause ie does not support setAttribute
        span.appendChild(error);
        td.appendChild(span);
        tr.appendChild(td);
        tr.setAttribute("errorFor", e.id);
        table.insertBefore(tr, row);

        // update the label too
        //if labelposition is 'top' the label is on the row above
        var labelRow = row.cells.length > 1 ? row : StrutsUtils.previousElement(tr, "tr");
        var label = labelRow.cells[0].getElementsByTagName("label")[0];
        label.setAttribute("class", "errorLabel");
        label.setAttribute("className", "errorLabel"); //ie hack cause ie does not support setAttribute
    } catch (e) {
        alert(e);
    }
}
