update egwtr_pipesize set sizeininch  =3 where code='3 Inch';
update egwtr_pipesize set sizeininch  =4 where code='4 Inch';

--rollback update egwtr_pipesize set sizeininch  =2 where code='3 Inch';
--rollback update egwtr_pipesize set sizeininch  =2 where code='4 Inch';
