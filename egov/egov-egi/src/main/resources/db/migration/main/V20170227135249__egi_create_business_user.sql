CREATE TABLE EG_BUSINESSUSER(
  id bigint primary key,
  CONSTRAINT fk_businessuser_user FOREIGN KEY (id)
      REFERENCES eg_user (id)
);