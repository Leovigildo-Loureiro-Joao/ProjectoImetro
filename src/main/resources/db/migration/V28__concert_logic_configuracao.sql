alter TABLE configuracoes
    DROP COLUMN if exists  extra_test_q

alter TABLE configuracoes
    DROP COLUMN if exists  desaf_test_q

alter TABLE configuracoes
    ADD COLUMN if not exists curto_test_q INTEGER not null DEFAULT 5;