alter table egbpaextnd_address drop  addresstypeid;
alter table egbpaextnd_address drop  stateid;


alter table egbpaextnd_address add  addresstypeid  character varying(512) NOT NULL; 
alter table egbpaextnd_address add  stateid  character varying(512) ;


