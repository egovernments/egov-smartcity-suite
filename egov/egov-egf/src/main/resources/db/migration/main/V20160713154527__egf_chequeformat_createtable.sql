CREATE TYPE ctype AS ENUM ('pd', 'general');
CREATE TYPE amountformat AS ENUM ('indian', 'international');
CREATE TYPE dateformat AS ENUM ('DD-Mon-YYYY', 'DDMMYYYY');

create table chequeformat (id bigint not null,chequename varchar,chequetype ctype,chequelength double precision ,chequewidth double precision,
accountPayeeCoordinate varchar,dateFormat dateformat,dateCoordinate varchar,
payeenamelength double precision,payeeNameCoordinate varchar,amountNumberingFormat amountformat,amountInWordsFirstLineCoordinate varchar,amountInWordsFirstLineLength double precision,
amountInWordsSecondLineLength double precision,amountInWordsSecondLineCoordinate varchar,amountlength double precision,
amountCoordinate varchar,formatStatus boolean default true,version numeric default 0,
PRIMARY KEY (id)
);

create sequence seq_chequeformat
INCREMENT BY 1 
START WITH 1 ;
