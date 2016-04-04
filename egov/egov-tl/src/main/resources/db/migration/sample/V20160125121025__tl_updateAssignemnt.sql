update egeis_assignment set designation=(select id from eg_designation where name='Commissioner'
),department=(select id from eg_department where name='Health') where employee in(select id from eg_user where username='mussavir'
);



update egeis_assignment set designation=(select id from eg_designation where name='Sanitary inspector'),department=(select id from eg_department where name='Health')
 where employee in(select id from eg_user where username='iffath'
);