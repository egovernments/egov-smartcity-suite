alter table voucherheader add column createddate datetime;
create sequence SEQ_EGEIS_POST_CREATION;

drop sequence EG_WF_TYPES_SEQ;
create sequence SEQ_EG_WF_TYPES;

drop sequence EG_WF_STATES_seq;
create sequence seq_EG_WF_STATES;

delete from eg_wf_types;

alter table eg_wf_states rename column modified_by to lastmodifiedby;
alter table eg_wf_states rename column created_by to createdby;
alter table eg_wf_states rename column created_date to createddate;
alter table eg_wf_states rename column modified_date to lastmodifieddate;
alter table eg_wf_states rename column next_action to nextaction;
alter table eg_wf_state_history rename column modified_by to lastmodifiedby;
alter table eg_wf_state_history rename column created_by to createdby;
alter table eg_wf_state_history rename column created_date to createddate;
alter table eg_wf_state_history rename column modified_date to lastmodifieddate;
alter table eg_wf_state_history rename column next_action to nextaction;
alter table eg_wf_types rename column id_type to id;
alter table eg_wf_types rename column module_id to module;
alter table eg_wf_types rename column wf_type to type;
alter table eg_wf_types rename column wf_link to link;
alter table eg_wf_types rename column created_by to createdby;
alter table eg_wf_types rename column created_date to createddate;
alter table eg_wf_types rename column modified_by to lastmodifiedby;
alter table eg_wf_types rename column modified_date to lastmodifieddate;
alter table eg_wf_types rename column render_yn to renderyn;
alter table eg_wf_types rename column group_yn to groupyn;
alter table eg_wf_types rename column display_name to displayname;
alter table eg_wf_types rename column full_qualified_name to typefqn;