insert into normal_partner.users values('admin', '$2a$10$fX4GfBxI60srfpwlmkJrSeZHoNlWw6a1RwU4oL9YviESLNJCBKZEe', curdate(), 'a', now(), 1, null);
insert into normal_partner.users values('frontdesk', '$2a$10$gC65uEWz.qc.a9vXTdHyLuQ8GjJ9NGM5btnUJzcckmHnnxKyM988S', curdate(), 'f', now(), 1, null);

insert into normal_partner.settings(registration_open, suspense_date) values(1, '2013-12-31');