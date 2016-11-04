ALTER TABLE eglc_hearings DROP COLUMN status;

ALTER TABLE eglc_hearings  ALTER COLUMN isseniorstandingcounselpresent DROP NOT NULL;
