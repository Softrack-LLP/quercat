select t.code, count(*) from user_mfs um
left join "USER" u on u.id = um.id
left join reference t on t.id = u.type_id
where u.created_on > :date_created_on_min and u.created_on < :date_created_on_max
group by t.code
