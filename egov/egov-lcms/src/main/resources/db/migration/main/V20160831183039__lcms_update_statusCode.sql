
update egw_status set code='JUDGEMENT_IMPL' where moduletype='Legal Case' and code='Judgment_Implemented';
update egw_status set code='CLOSED',description='Closed' where moduletype='Legal Case' and code='Close Case';