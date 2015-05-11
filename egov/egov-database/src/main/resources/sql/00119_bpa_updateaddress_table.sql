alter table egbpaextnd_address drop coulmn addresstypeid;
alter table egbpaextnd_address drop coulmn stateid;


alter table egbpaextnd_address add  addresstypeid  character varying(512) NOT NULL; 
alter table egbpaextnd_address add  stateid  character varying(512) ;


