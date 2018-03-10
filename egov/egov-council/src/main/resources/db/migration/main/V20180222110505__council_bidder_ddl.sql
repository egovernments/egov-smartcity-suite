create table egcncl_preamble_bidder(
 id bigint,
 preamble bigint,
 bidder bigint,
 tendertype varchar(25) ,
 percentage double precision DEFAULT 0,
 Amount bigint,
 position varchar(10),
 isAwarded boolean,
 CONSTRAINT fk_egcncl_preamble_bidder FOREIGN KEY (preamble)
      REFERENCES egcncl_preamble (id),
 CONSTRAINT fk_egcncl_contractor_bidder FOREIGN KEY (bidder)
      REFERENCES egw_contractor (id)
);
CREATE SEQUENCE seq_egcncl_preamble_bidderdetails;

ALTER TABLE egcncl_preamble_bidder add constraint pk_preambel_bidder primary key (id);

ALTER TABLE egcncl_preamble  ADD COLUMN referenceNumber character varying(256);
ALTER TABLE egcncl_preamble  ADD COLUMN typeofpreamble varchar (25);
ALTER TABLE egcncl_preamble  ADD COLUMN addtionalGistOfPreamble varchar (10000);

