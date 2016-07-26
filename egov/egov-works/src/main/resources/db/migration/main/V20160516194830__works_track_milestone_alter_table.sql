ALTER TABLE egw_track_milestone_activity RENAME COLUMN completetion_date TO completiondate;

ALTER TABLE egw_track_milestone RENAME COLUMN total TO totalpercentage;
ALTER TABLE egw_track_milestone ALTER COLUMN totalpercentage TYPE double precision;
ALTER TABLE egw_track_milestone ALTER COLUMN projectcompleted DROP DEFAULT;
ALTER TABLE egw_track_milestone ALTER COLUMN projectcompleted TYPE boolean USING CASE projectcompleted WHEN '1' THEN true ELSE false END;
ALTER TABLE egw_track_milestone ALTER COLUMN projectcompleted SET DEFAULT false;

--rollback ALTER TABLE egw_track_milestone ALTER COLUMN projectcompleted DROP DEFAULT;
--rollback ALTER TABLE egw_track_milestone ALTER COLUMN projectcompleted TYPE smallint;
--rollback ALTER TABLE egw_track_milestone ALTER COLUMN totalpercentage TYPE bigint;
--rollback ALTER TABLE egw_track_milestone RENAME COLUMN totalpercentage TO total;
--rollback ALTER TABLE egw_track_milestone_activity RENAME COLUMN completiondate TO completetion_date;