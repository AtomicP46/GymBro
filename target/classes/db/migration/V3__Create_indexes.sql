-- =====================================================
-- CRIAÇÃO DOS ÍNDICES PARA PERFORMANCE
-- =====================================================

-- Índices para tabela alunos
CREATE INDEX idx_alunos_email ON alunos(email);
CREATE INDEX idx_alunos_nome ON alunos(nome);

-- Índices para tabela personal
CREATE INDEX idx_personal_email ON personal(email);
CREATE INDEX idx_personal_nome ON personal(nome);
CREATE INDEX idx_personal_formado ON personal(formado);

-- Índices para tabela exercicio
CREATE INDEX idx_exercicio_nome ON exercicio(nome);
CREATE INDEX idx_exercicio_regiao ON exercicio(regiao);
CREATE INDEX idx_exercicio_tipo ON exercicio(tipo);
CREATE INDEX idx_exercicio_equipamento ON exercicio(equipamento_id);

-- Índices para tabela treinos
CREATE INDEX idx_treinos_usuario ON treinos(usuario_id);
CREATE INDEX idx_treinos_personal ON treinos(personal_id);
CREATE INDEX idx_treinos_data_inicio ON treinos(data_hora_inicio);

-- Índices para tabela treino_exercicio
CREATE INDEX idx_treino_exercicio_treino ON treino_exercicio(treino_id);
CREATE INDEX idx_treino_exercicio_exercicio ON treino_exercicio(exercicio_id);

-- Índices para tabela plano
CREATE INDEX idx_plano_criador ON plano(criador_id, tipo_criador);
CREATE INDEX idx_plano_publico ON plano(publico);
CREATE INDEX idx_plano_data_criacao ON plano(data_criacao);

-- Índices para tabela plano_exercicio
CREATE INDEX idx_plano_exercicio_plano ON plano_exercicio(plano_id);
CREATE INDEX idx_plano_exercicio_exercicio ON plano_exercicio(exercicio_id);
