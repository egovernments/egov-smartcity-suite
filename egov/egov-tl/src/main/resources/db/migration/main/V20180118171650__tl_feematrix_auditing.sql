CREATE TABLE egtl_feematrix_aud
(
    id integer,
    rev integer,
    revtype numeric,
    natureofbusiness bigint,
    licenseapptype bigint,
    licensecategory bigint,
    subcategory bigint,
    feetype bigint,
    financialyear bigint,
    effectivefrom timestamp without time zone,
    effectiveto timestamp without time zone
);

CREATE TABLE egtl_feematrix_detail_aud
(
    id integer,
    rev integer,
    revtype numeric,
    feematrix bigint NOT NULL,
    uomfrom bigint NOT NULL,
    uomto bigint NOT NULL,
    amount double precision NOT NULL
);