CREATE TABLE ffa_user
(
 id SERIAL PRIMARY KEY,
 name varchar(100) NOT NULL,
 password varchar(50) NOT NULL ,
 created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
 created_by int(11) NOT NULL,
 updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
 updated_by int(11) NOT NULL,
 deleted boolean default 0 NOT NULL
);