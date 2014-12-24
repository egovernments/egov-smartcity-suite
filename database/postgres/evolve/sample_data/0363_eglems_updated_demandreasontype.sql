#UP

update EGLEMS_DEMANDREASONTYPE set isactive=0 where reasonname in('ARREARS','ADVANCE','ADVANCEDEPOSIT');


#DOWN

update EGLEMS_DEMANDREASONTYPE set isactive=1 where reasonname in('ARREARS','ADVANCE','ADVANCEDEPOSIT');