create sequence seq_egpt_bhudhar_exempted_localities;
CREATE TABLE egpt_bhudhar_exempted_localities
(
  id bigint NOT NULL,
  boundarynum bigint,
  CONSTRAINT egpt_bhudhar_exempted_localities_pkey PRIMARY KEY (id)
);

