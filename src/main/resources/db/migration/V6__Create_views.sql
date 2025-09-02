-- =====================================================
-- VIEWS ÚTEIS PARA CONSULTAS
-- =====================================================

-- View para listar exercícios com equipamentos
CREATE VIEW v_exercicios_completos AS
SELECT 
    e.id,
    e.nome,
    e.regiao,
    e.tipo,
    e.unilateral,
    eq.nome as equipamento_nome,
    eq.disponivel as equipamento_disponivel
FROM exercicio e
LEFT JOIN equipamento eq ON e.equipamento_id = eq.id;

-- View para treinos com informações do usuário
CREATE VIEW v_treinos_completos AS
SELECT 
    t.id,
    t.nome,
    t.data_hora_inicio,
    t.data_hora_fim,
    a.nome as aluno_nome,
    a.email as aluno_email,
    p.nome as personal_nome,
    CASE 
        WHEN t.data_hora_fim IS NOT NULL THEN 'FINALIZADO'
        WHEN t.data_hora_inicio IS NOT NULL THEN 'EM_ANDAMENTO'
        ELSE 'AGENDADO'
    END as status
FROM treinos t
JOIN alunos a ON t.usuario_id = a.id
LEFT JOIN personal p ON t.personal_id = p.id;
