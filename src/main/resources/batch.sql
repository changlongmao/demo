SELECT CONCAT(
               'ALTER TABLE ',
               TABLE_NAME,
               ' CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;'
           )
FROM information_schema.`TABLES`
WHERE TABLE_SCHEMA = 'zdwp_oa';