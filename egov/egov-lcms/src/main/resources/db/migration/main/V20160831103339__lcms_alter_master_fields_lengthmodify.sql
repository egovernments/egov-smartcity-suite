----advocate master table
Alter table eglc_advocate_master  Alter column name TYPE character varying(100) ;
Alter table eglc_advocate_master  Alter column contactphone TYPE character varying(20) ;
Alter table eglc_advocate_master  Alter column email TYPE character varying(64) ;

----advocate master audit table
Alter table eglc_advocate_master_aud  Alter column name TYPE character varying(100) ;
Alter table eglc_advocate_master_aud  Alter column contactphone TYPE character varying(20) ;
Alter table eglc_advocate_master_aud  Alter column email TYPE character varying(64) ;

----petition master table
Alter table eglc_petitiontype_master Alter column petitiontype TYPE character varying(128) ;
Alter table eglc_petitiontype_master Alter column code TYPE character varying(25) ;

----petition master audit table
Alter table eglc_petitiontype_master_aud Alter column petitiontype TYPE character varying(128) ;
Alter table eglc_petitiontype_master_aud Alter column code TYPE character varying(25) ;

----casetype master table
Alter table eglc_casetype_master  Alter column notes TYPE character varying(256) ;
Alter table eglc_casetype_master  Alter column code TYPE character varying(25) ;
Alter table eglc_casetype_master  Alter column casetype TYPE character varying(50) ;

----casetype master audit table
Alter table eglc_casetype_master_aud  Alter column code TYPE character varying(25) ;
Alter table eglc_casetype_master_aud  Alter column casetype TYPE character varying(50) ;

----courttype master table
Alter table eglc_courttype_master Alter column code TYPE character varying(25) ;
Alter table eglc_courttype_master Alter column courttype TYPE character varying(50) ;

----courttype master audit table
Alter table eglc_courttype_master_aud Alter column code TYPE character varying(25) ;
Alter table eglc_courttype_master_aud Alter column courttype TYPE character varying(50);

----court master table
Alter table eglc_court_master Alter column name TYPE character varying(100) ;

----court master audit table
Alter table eglc_court_master_aud Alter column name TYPE character varying(100) ;

----governmentdepartment master table
Alter table eglc_governmentdepartment Alter column code TYPE character varying(25) ;
Alter table eglc_governmentdepartment Alter column name TYPE character varying(128) ;

----governmentdepartment master audit table
Alter table eglc_governmentdepartment_aud Alter column code TYPE character varying(25) ;
Alter table eglc_governmentdepartment_aud Alter column name TYPE character varying(128) ;

----judgmenttype master table
Alter table eglc_judgmenttype_master Alter column code TYPE character varying(25) ;

----judgmenttype master audit table
Alter table eglc_judgmenttype_master_aud Alter column code TYPE character varying(25) ;

----interimtype master table
Alter table eglc_interimtype_master Alter column code TYPE character varying(25) ;

----interimtype master audit table
Alter table eglc_interimtype_master_aud Alter column code TYPE character varying(25) ;






