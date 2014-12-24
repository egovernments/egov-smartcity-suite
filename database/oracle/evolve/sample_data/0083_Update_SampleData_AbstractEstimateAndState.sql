#UP

UPDATE EG_WF_STATES SET created_date=sysdate,modified_date=sysdate WHERE TYPE='AbstractEstimate';

#DOWN
