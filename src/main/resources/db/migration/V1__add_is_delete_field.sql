alter table Products add column is_delete boolean default false;
alter table Orders add column is_delete boolean default false;
alter table OrderDetails add column is_delete boolean default false;
alter table Stock add column is_delete boolean default false;