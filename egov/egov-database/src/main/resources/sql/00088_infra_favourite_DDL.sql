ALTER SEQUENCE EG_FAVOURITES_SEQ RENAME TO SEQ_EG_FAVOURITES;

ALTER TABLE EG_FAVOURITES RENAME COLUMN USER_ID TO USERID;
ALTER TABLE EG_FAVOURITES RENAME COLUMN ACTION_ID TO ACTIONID;
ALTER TABLE EG_FAVOURITES RENAME COLUMN FAV_NAME TO NAME;
ALTER TABLE EG_FAVOURITES RENAME COLUMN CTX_NAME TO CONTEXTROOT;
ALTER TABLE EG_FAVOURITES ADD COLUMN version BIGINT;