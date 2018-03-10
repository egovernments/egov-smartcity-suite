
ALTER SEQUENCE SEQ_EGPGR_FEEDBACK_REASON RENAME TO SEQ_EGPGR_IVRS_FEEDBACK_REASON;

ALTER TABLE EGPGR_FEEDBACK_REASON RENAME TO EGPGR_IVRS_FEEDBACK_REASON; 

ALTER SEQUENCE SEQ_EGPGR_QUALITYREVIEW RENAME TO SEQ_EGPGR_IVRS_FEEDBACK_REVIEW;

ALTER TABLE EGPGR_QUALITYREVIEW RENAME TO EGPGR_IVRS_FEEDBACK_REVIEW;

ALTER TABLE EGPGR_IVRS_FEEDBACK_REVIEW
ADD COLUMN reopenCount bigint default 0,ADD COLUMN reviewCount bigint default 0 ,DROP  COLUMN  feedbackdate ;

ALTER TABLE EGPGR_IVRS_FEEDBACK_REASON ADD COLUMN toBeReopened boolean;

UPDATE EGPGR_IVRS_FEEDBACK_REASON SET
toBeReopened = ( CASE  WHEN (name in ('Complaint closed without resolution','Complaint addressed partially')) THEN true  ELSE false END );


CREATE SEQUENCE SEQ_EGPGR_IVRS_RATING;

CREATE TABLE  EGPGR_IVRS_RATING
(
  id bigint NOT NULL PRIMARY KEY,
  name character varying(64),
  weight bigint NOT NULL,
  discription character varying(100),
  requiredReview boolean,
  version numeric DEFAULT 0
);


CREATE SEQUENCE SEQ_EGPGR_IVRS_FEEDBACK;

CREATE TABLE  EGPGR_IVRS_FEEDBACK
(
  id bigint NOT NULL PRIMARY KEY,
  complaint bigint NOT NULL,
  ivrsRating bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  CONSTRAINT unq_ivrsfeedback_complaint UNIQUE (complaint),
  CONSTRAINT fk_eg_ivrs_feedback_rating FOREIGN KEY (ivrsRating)
  REFERENCES EGPGR_IVRS_RATING (id)
);


CREATE SEQUENCE SEQ_IVRS_FEEDBACK_REVIEW_HISTORY;

CREATE TABLE  EGPGR_IVRS_FEEDBACK_REVIEW_HISTORY
(
  id bigint NOT NULL PRIMARY KEY,
  feedbackReview bigint NOT NULL,
  complaint bigint NOT NULL,
  rating bigint NOT NULL,
  feedbackReason bigint NOT NULL,
  detail character varying(128),
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  version bigint DEFAULT 0,
  CONSTRAINT fk_egpgr_feedbackReason FOREIGN KEY (feedbackReason)
  REFERENCES EGPGR_IVRS_FEEDBACK_REASON (id),
  CONSTRAINT fk_ivrs_rating FOREIGN KEY (rating)
  REFERENCES EGPGR_IVRS_RATING (id),
  CONSTRAINT fk_ivrs_feedback_review FOREIGN KEY (feedbackReview)
  REFERENCES EGPGR_IVRS_FEEDBACK_REVIEW (id)
);

INSERT INTO EGPGR_IVRS_RATING (id,name,weight,discription,requiredReview,version) values
(NEXTVAL('SEQ_EGPGR_IVRS_RATING'),'1',5,'Good','f',0),
(NEXTVAL('SEQ_EGPGR_IVRS_RATING'),'2',3,'Average','f',0),
(NEXTVAL('SEQ_EGPGR_IVRS_RATING'),'3',1,'Bad','t',0);

UPDATE EGPGR_IVRS_FEEDBACK_REVIEW
SET rating = ( CASE  WHEN (rating in (5,4)) THEN 1 WHEN (rating in (3,2)) THEN 2 ELSE  (3) END );

ALTER TABLE EGPGR_IVRS_FEEDBACK_REVIEW  DROP CONSTRAINT fk_egpgr_feedback_reason,
ADD CONSTRAINT fk_egpgr_ivrs_feedbackrating FOREIGN KEY (rating) REFERENCES EGPGR_IVRS_RATING (id),
ADD CONSTRAINT fk_egpgr_ivrs_feedback_reason FOREIGN KEY (feedbackReason) REFERENCES EGPGR_IVRS_FEEDBACK_REASON (id);


INSERT INTO egpgr_configuration values (nextval('seq_egpgr_configuration'), 'IVRS_REOPEN_COUNT', '2',
'Number of time Complaint reopening allowed',1,now(),1,now(),0);

UPDATE eg_action SET url='/complaint/ivrs/feedbackreview/search' where name ='Search Rated Grievance';

UPDATE eg_action SET url='/complaint/ivrs/feedbackreview' , name='Complaint Feedback Review' where name ='Complaint Feedback';

UPDATE eg_action SET url='/complaint/ivrs/feedbackreason/create' , displayname='Create Feedback Reason' where name ='Complaint Feedback Reason';

INSERT INTO EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES
(NEXTVAL('SEQ_EG_MODULE'),'IVRS','f','',(select id from eg_module where name ='PGR'),'IVRS',null);

update eg_action set parentmodule =(select id from eg_module  where name ='IVRS') where name='Search Rated Grievance';

update eg_action set parentmodule =(select id from eg_module  where name ='IVRS') where name='Complaint Feedback Reason';

update eg_action set parentmodule =(select id from eg_module  where name ='IVRS') where name='Complaint Feedback Review';

