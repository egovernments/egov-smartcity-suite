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

var param_types = {unknown: 0, context: 1, id: 2};

function getParam(query, a) {
	a['value'] = getParamValue('context', query);
	if (a['value'] != '') {
		a['type'] = param_types.context;
	}
	else {
		a['value'] = getParamValue('id', query);
		if (a['value'] != '') {
			a['type'] = param_types.id;			
		}
		else {
			a['type'] = param_types.unknown;
		}
	}	
}

function findTopic(a) {	
	var value = a['value'].toLowerCase();
	for (var i = 0; i < topic_data.length; i++) {
		switch (a['type']) {
			case param_types.context:
				if (value == topic_data[i][0]) {
					return topic_data[i][2];
				}
			break;
			case param_types.id:
				if (value == topic_data[i][1].toLowerCase()) {
					return topic_data[i][2];
				}
			break;
		}
	}
	return '';
}

function getTopic(query, defaultTopic, unknownTopic) {	
	var a = new Array();
	getParam(query, a);
	if (a['type'] != param_types.unknown) {
		var t = findTopic(a);
		if (t == '') {
			return unknownTopic;
		}
		else {
			return t;
		}
	}
	return defaultTopic;
}
