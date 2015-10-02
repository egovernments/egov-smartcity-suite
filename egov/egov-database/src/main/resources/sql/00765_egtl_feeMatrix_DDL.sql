ALTER TABLE egtl_mstr_fee_type ADD code VARCHAR(12);
ALTER TABLE egtl_mstr_fee_type ADD feeProcessType bigint;
ALTER TABLE egtl_mstr_fee_type ADD column createddate TIMESTAMP without TIME zone;
ALTER TABLE egtl_mstr_fee_type ADD column createdby   bigint;
ALTER TABLE egtl_mstr_fee_type ADD column lastmodifieddate    TIMESTAMP without TIME zone;
ALTER TABLE egtl_mstr_fee_type ADD column lastmodifiedby   bigint ;
ALTER TABLE EGTL_MSTR_APP_TYPE ADD column version   bigint;
ALTER TABLE EGTL_MSTR_FEE_TYPE ADD column version   bigint;

DROP TABLE egtl_mstr_fee_matrix;
DROP sequence seq_egtl_mstr_fee_matrix;

CREATE TABLE egtl_feematrix
  (
    id bigint,
    natureofbusiness bigint,
    licenseAppType bigint,
    licenseCategory bigint,
    subCategory bigint,
    feeType bigint,
    unitOfMeasurement bigint,
    uniqueno    VARCHAR(32),
    createddate TIMESTAMP without TIME zone,
    createdby bigint,
    lastmodifieddate TIMESTAMP without TIME zone,
    lastmodifiedby bigint,
    version bigint
  );
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT PK_EGTL_FEEMATRIX PRIMARY KEY (id) ;
  
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_NB FOREIGN KEY (natureofbusiness) REFERENCES egtl_mstr_business_nature (ID);
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_LAT FOREIGN KEY (licenseAppType) REFERENCES egtl_mstr_app_type (ID);
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_LC FOREIGN KEY (licenseCategory) REFERENCES egtl_mstr_category (ID);
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_LSC FOREIGN KEY (subCategory) REFERENCES egtl_mstr_sub_category (ID);
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_FT FOREIGN KEY (feeType) REFERENCES egtl_mstr_fee_type (ID);
ALTER TABLE ONLY EGTL_FEEMATRIX ADD CONSTRAINT FK_EGTL_FM_UOM FOREIGN KEY (unitOfMeasurement) REFERENCES egtl_mstr_unitofmeasure (ID);
CREATE sequence seq_egtl_feematrix;
  
CREATE TABLE egtl_feematrix_detail
    (
      id bigint,
      feematrix bigint,
      uomFrom bigint,
      uomTo bigint,
      percentage bigint,
      fromDate DATE,
      toDate   DATE,
      amount double precision,
      version bigint
    );
    
ALTER TABLE ONLY EGTL_FEEMATRIX_Detail ADD CONSTRAINT FK_EGTL_FMD_FM FOREIGN KEY (feematrix) REFERENCES egtl_feematrix (ID);
CREATE sequence seq_egtl_feematrix_detail;
