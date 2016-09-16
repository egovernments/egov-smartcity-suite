update eg_module set parentmodule = (select id from eg_module where name='Council Management Transaction') where name='Council MOM' and contextroot='council';

update eg_action set parentmodule = (select id from eg_module where name='Council Management Transaction') where name='RetrieveSmsAndEmailForMeeting' and contextroot='council';

update eg_action set parentmodule = (select id from eg_module where name='Council Management Transaction') where name='SearchAttendanceForMeeting' and contextroot='council';