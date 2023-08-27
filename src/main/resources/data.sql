-- パスワードは「demo」をハッシュ化したもの
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('taro-yamada', '太郎', '山田', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('aaaa', 'Aaa', 'Aaa', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('bbbb', 'Bbb', 'Bbb', 'USER', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
INSERT INTO usr (user_id, first_name, last_name, role_name, password)
VALUES ('cccc', 'Ccc', 'Ccc', 'ADMIN', '$2a$10$oxSJl.keBwxmsMLkcT9lPeAIxfNTPNQxpeywMrF7A3kVszwUTqfTK')/;
--
INSERT INTO meeting_room (room_name) VALUES ('北千住')/;
INSERT INTO meeting_room (room_name) VALUES ('上野')/;
INSERT INTO meeting_room (room_name) VALUES ('東日本橋')/;
INSERT INTO meeting_room (room_name) VALUES ('新宿')/;
INSERT INTO meeting_room (room_name) VALUES ('九段下')/;
INSERT INTO meeting_room (room_name) VALUES ('東京')/;
INSERT INTO meeting_room (room_name) VALUES ('有楽町')/;

-- Stored Procedure
DROP FUNCTION IF EXISTS REGISTER_RESERVABLE_ROOMS()/;
CREATE OR REPLACE FUNCTION REGISTER_RESERVABLE_ROOMS()-- CREATE OR REPLACE FUNCTIONは、新しい関数の作成、または既存定義の置換のどちらかを行う
  RETURNS--戻り値
    INT AS $$--「as $$」はストアドの始点 
DECLARE--変数宣言
  total INT;
  i INT4;
  id INT4;
BEGIN--ストアドの処理開始
  total := 0;
  FOR id IN SELECT room_id
            FROM meeting_room LOOP
    i := 0;
    FOR i IN 0..6 LOOP--実行日の3日前～3日先までinsert
      INSERT INTO reservable_room (reserved_date, room_id)
      VALUES (CURRENT_DATE + i - 3, id);--実行日の3日前からinsert
    END LOOP;
    total := total + i;
  END LOOP;
  RETURN total;--ストアドの戻り値
END;--ストアドの処理終了
$$ LANGUAGE plpgsql--ストアドの終点と、このストアドがPL/pgSQLで記述されていることを指定
/;
SELECT REGISTER_RESERVABLE_ROOMS() /;
COMMIT /;