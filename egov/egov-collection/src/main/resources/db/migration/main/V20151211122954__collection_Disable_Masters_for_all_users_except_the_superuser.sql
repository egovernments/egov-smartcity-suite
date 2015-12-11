DELETE
FROM eg_roleaction
WHERE roleid IN
  (SELECT id FROM eg_role WHERE name NOT IN ('Super User')
  )
AND actionid IN
  (SELECT id
  FROM eg_action
  WHERE parentmodule IN
    (SELECT id FROM eg_module WHERE displayname = 'Master'
    )
  OR parentmodule IN
    (SELECT id
    FROM eg_module
    WHERE parentmodule IN
      (SELECT id FROM eg_module WHERE displayname = 'Master'
      )
    )
  );
