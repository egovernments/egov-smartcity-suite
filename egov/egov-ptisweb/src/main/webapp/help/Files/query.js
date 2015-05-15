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
