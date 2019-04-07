-- 创建表
drop table if exists file_index;
create table if not exists file_index
(
name varchar (256) not null comment '文件名称',
path varchar (2048) not null comment '文件路径',
depth int not null comment '文件深度',
file_type varchar (46) not null comment '文件类型'
);