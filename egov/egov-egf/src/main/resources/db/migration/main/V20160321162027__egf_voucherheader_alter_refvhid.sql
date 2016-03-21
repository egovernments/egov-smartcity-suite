ALTER TABLE voucherheader RENAME COLUMN refcgno TO refvhId;

ALTER TABLE voucherheader ALTER COLUMN refvhId TYPE bigint using refvhId::bigint;
