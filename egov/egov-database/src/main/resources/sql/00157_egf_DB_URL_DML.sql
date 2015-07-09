--DB URL Changes

UPDATE eg_action SET url = replace(url, '!', '-') WHERE url LIKE '%!%' and context_root='EGF';
