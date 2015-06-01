UPDATE eg_position SET name=replace(name,'_','-');

--rollback UPDATE eg_position SET name=replace(name,'-','_');