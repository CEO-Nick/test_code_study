INSERT INTO users (id, email, nickname, address, certification_code, status, last_login_at)
VALUES (1, 'kok202@naver.com', 'kok202', 'Seoul', 'aaaaaaaa-aaaaa-aaaaa-aaa-aaa-aaaaa', 'ACTIVE', 0);

INSERT INTO users (id, email, nickname, address, certification_code, status, last_login_at)
VALUES (2, 'kok303@naver.com', 'kok303', 'Seoul', 'aaaaaaaa-aaaaa-aaaaa-aaa-aaa-aaaaa', 'PENDING', 0);

insert into posts (id, content, created_at, modified_at, user_id)
values (1, 'test post 1', '11111111', '22222222', 1);

insert into posts (id, content, created_at, modified_at, user_id)
values (2, 'test post 2', '11111111', '22222222', 2);