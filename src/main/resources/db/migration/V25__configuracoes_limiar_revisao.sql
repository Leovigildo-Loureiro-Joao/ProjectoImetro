alter TABLE configuracoes_teste_adaptativo_niveis
  ADD COLUMN if not exists limiar_acerto double precision not null  DEFAULT 0.8;

alter TABLE configuracoes_teste_adaptativo_niveis
  ADD COLUMN if not exists limiar_erro double precision not null  DEFAULT 0.5;

alter TABLE configuracoes_teste_adaptativo_niveis
  ADD COLUMN if not exists revisao_med  double precision  not null  DEFAULT 0.6;

alter TABLE configuracoes_teste_adaptativo_niveis
  ADD COLUMN if not exists tot_erro_revisao  INTEGER not null DEFAULT 2;


UPDATE configuracoes_teste_adaptativo_niveis
SET limiar_acerto=0.45,limiar_erro=0.4,revisao_med=0.25, tot_erro_revisao=4
WHERE codigo ='FACIL';


UPDATE configuracoes_teste_adaptativo_niveis
SET limiar_acerto=0.55,limiar_erro=0.35,revisao_med=0.35, tot_erro_revisao=3
WHERE codigo ='MEDIO';


UPDATE configuracoes_teste_adaptativo_niveis
SET limiar_acerto=0.75,limiar_erro=0.3,revisao_med=0.4, tot_erro_revisao=2
WHERE codigo ='DIFICIL';


UPDATE configuracoes_teste_adaptativo_niveis
SET limiar_acerto=0.8,limiar_erro=0.2,revisao_med=0.25, tot_erro_revisao=1
WHERE codigo ='EXPERT';
