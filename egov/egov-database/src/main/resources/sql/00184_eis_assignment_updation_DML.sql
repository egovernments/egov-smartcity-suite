UPDATE egeis_assignment SET isprimary=true,employee=oldemployee;

--rollback UPDATE egeis_assignment SET isprimary=null,employee=null;
