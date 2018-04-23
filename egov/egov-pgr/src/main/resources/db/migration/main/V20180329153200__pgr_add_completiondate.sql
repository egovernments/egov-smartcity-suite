alter table egpgr_complaint add column completiondate timestamp without time zone;
update egpgr_complaint cm set completiondate = completedcomp.dates
from (select comp.id as ids,st.lastmodifieddate as dates
      from egpgr_complaint comp,eg_wf_states st where st.id=comp.state_id and st.status=2) completedcomp
      where completedcomp.ids=cm.id;