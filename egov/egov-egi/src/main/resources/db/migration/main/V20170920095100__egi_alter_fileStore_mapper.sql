
--alter fileStore Mapper
alter table eg_filestoremap add column createdDate timestamp without time zone not null default now();