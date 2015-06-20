update eg_action set name='Define Escalation',displayname='Define Escalation' where name='Search Escalation' and contextroot='pgr';

update eg_action set enabled=true where name='View Escalation' and contextroot='pgr';
