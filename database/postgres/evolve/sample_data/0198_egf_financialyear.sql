#UP

UPDATE financialyear set ISACTIVEFORPOSTING=1 where financialyear='2010-11';
#DOWN
UPDATE financialyear set isactiveforposting=0 where financialyear='2010-11';
