------------------START------------------
alter table egadtax_hoarding rename column revenueboundary  to location  ;
alter table egadtax_hoarding rename column adminboundry  to ward ;
alter table egadtax_hoarding add column block   bigint  ;
alter table egadtax_hoarding add column electionward   bigint ;
alter table egadtax_hoarding add column street   bigint  ;

------------------END------------------