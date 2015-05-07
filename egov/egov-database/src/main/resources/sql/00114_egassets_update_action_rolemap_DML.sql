update eg_action set url = replace(url, '!','-') where context_root='egassets';



--rollback update eg_action set url = replace(url, '-','!') where context_root='egassets';
