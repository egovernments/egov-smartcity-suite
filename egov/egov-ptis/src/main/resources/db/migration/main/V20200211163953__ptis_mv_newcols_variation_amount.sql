ALTER TABLE egpt_mv_propertyinfo ADD column curfirsthalf_writeoff double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column curfirsthalf_courtverdict double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column cursecondhalf_writeoff double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column cursecondhalf_courtverdict double precision DEFAULT 0;


ALTER TABLE egpt_mv_propertyinfo ADD column arrpen_writeoff double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column arrpen_courtverdict double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column curpen_writeoff double precision DEFAULT 0;
ALTER TABLE egpt_mv_propertyinfo ADD column curpen_courtverdict double precision DEFAULT 0;


ALTER TABLE egpt_mv_propertyinfo RENAME column writeoff_amount TO arr_writeoff;
ALTER TABLE egpt_mv_propertyinfo RENAME column courtcase_amount TO arr_courtverdict;
