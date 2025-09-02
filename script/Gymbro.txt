-- =====================================================
-- SCRIPT DE CRIAÇÃO DO BANCO DE DADOS GYMBRO
-- Sistema de Gerenciamento de Academia
-- PostgreSQL + Docker
-- =====================================================

-- Criação do banco de dados
CREATE DATABASE gymbro_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'pt_BR.UTF-8'
    LC_CTYPE = 'pt_BR.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectar ao banco criado
\c gymbro_db;

-- =====================================================
-- CRIAÇÃO DOS TIPOS ENUM
-- =====================================================

-- Cria o novo enum regiao_corpo
CREATE TYPE regiao_corpo AS ENUM (
  'CARDIO',
  'ABDOMEN',
  'BICEPS',
  'TRICEPS',
  'PEITO',
  'COSTAS',
  'OMBRO',
  'PERNAS'
);

-- Cria o novo enum tipo_exercicio
CREATE TYPE tipo_exercicio AS ENUM (
  'FORCA_PESO_REPETICOES',
  'FORCA_PESO_TEMPO',
  'PESO_CORPO_COM_PESO_REPETICOES',
  'PESO_CORPO_REPETICOES',
  'PESO_CORPO_TEMPO',
  'PESO_CORPO_ASSISTENCIA_REPETICOES',
  'CARDIO_TEMPO_DISTANCIA_CALORIAS',
  'OUTROS'
);

-- Cria o novo enum tipo_criador
CREATE TYPE tipo_criador AS ENUM (
  'USUARIO',
  'PERSONAL'
);


-- =====================================================
-- CRIAÇÃO DAS TABELAS PRINCIPAIS
-- =====================================================

-- Tabela de Alunos (herda de Usuario)
CREATE TABLE alunos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    peso DECIMAL(5,2) NOT NULL CHECK (peso > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Personal Trainers (herda de Usuario)
CREATE TABLE personal (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    formado BOOLEAN NOT NULL DEFAULT FALSE,
    codigo_validacao VARCHAR(50),
    link_validacao VARCHAR(500),
    licenca VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint: licença obrigatória para não formados
    CONSTRAINT chk_licenca_nao_formado 
        CHECK (formado = TRUE OR (formado = FALSE AND licenca IS NOT NULL AND LENGTH(TRIM(licenca)) > 0))
);

-- Tabela de Equipamentos
CREATE TABLE equipamento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Exercícios
CREATE TABLE exercicio (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    regiao regiao_corpo NOT NULL,
    tipo tipo_exercicio NOT NULL,
    unilateral BOOLEAN NOT NULL,
    equipamento_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_exercicio_equipamento 
        FOREIGN KEY (equipamento_id) REFERENCES equipamento(id) ON DELETE SET NULL
);

-- Tabela de Treinos
CREATE TABLE treinos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_hora_inicio TIMESTAMP,
    data_hora_fim TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    personal_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints para relacionamentos
    CONSTRAINT fk_treino_aluno 
        FOREIGN KEY (usuario_id) REFERENCES alunos(id) ON DELETE CASCADE,
    CONSTRAINT fk_treino_personal 
        FOREIGN KEY (personal_id) REFERENCES personal(id) ON DELETE SET NULL,
    
    -- Constraint: data fim deve ser posterior ao início
    CONSTRAINT chk_data_fim_posterior 
        CHECK (data_hora_fim IS NULL OR data_hora_fim > data_hora_inicio)
);

-- Tabela de Exercícios do Treino (relacionamento N:N)
CREATE TABLE treino_exercicio (
    id BIGSERIAL PRIMARY KEY,
    treino_id BIGINT NOT NULL,
    exercicio_id BIGINT NOT NULL,
    series INTEGER CHECK (series > 0),
    repeticoes INTEGER CHECK (repeticoes > 0),
    peso_usado REAL CHECK (peso_usado >= 0),
    anotacoes TEXT,
    aquecimento BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_treino_exercicio_treino 
        FOREIGN KEY (treino_id) REFERENCES treinos(id) ON DELETE CASCADE,
    CONSTRAINT fk_treino_exercicio_exercicio 
        FOREIGN KEY (exercicio_id) REFERENCES exercicio(id) ON DELETE CASCADE,
    
    -- Constraint única para evitar duplicatas
    CONSTRAINT uk_treino_exercicio 
        UNIQUE (treino_id, exercicio_id)
);

-- Tabela de Planos de Treino
CREATE TABLE plano (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    criador_id BIGINT NOT NULL,
    tipo_criador tipo_criador NOT NULL,
    publico BOOLEAN DEFAULT FALSE,
    observacoes VARCHAR(1000),
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Exercícios do Plano (relacionamento N:N)
CREATE TABLE plano_exercicio (
    id BIGSERIAL PRIMARY KEY,
    plano_id BIGINT NOT NULL,
    exercicio_id BIGINT NOT NULL,
    series INTEGER CHECK (series > 0),
    repeticoes INTEGER CHECK (repeticoes > 0),
    peso_sugerido REAL CHECK (peso_sugerido >= 0),
    observacoes TEXT,
    ordem INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_plano_exercicio_plano 
        FOREIGN KEY (plano_id) REFERENCES plano(id) ON DELETE CASCADE,
    CONSTRAINT fk_plano_exercicio_exercicio 
        FOREIGN KEY (exercicio_id) REFERENCES exercicio(id) ON DELETE CASCADE,
    
    -- Constraint única para evitar duplicatas
    CONSTRAINT uk_plano_exercicio 
        UNIQUE (plano_id, exercicio_id)
);

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

-- =====================================================
-- TRIGGERS PARA ATUALIZAÇÃO AUTOMÁTICA DE TIMESTAMPS
-- =====================================================

-- Função para atualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Aplicar trigger em todas as tabelas relevantes
CREATE TRIGGER update_alunos_updated_at BEFORE UPDATE ON alunos 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_personal_updated_at BEFORE UPDATE ON personal 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_equipamento_updated_at BEFORE UPDATE ON equipamento 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_exercicio_updated_at BEFORE UPDATE ON exercicio 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_treinos_updated_at BEFORE UPDATE ON treinos 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_plano_updated_at BEFORE UPDATE ON plano 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- INSERÇÃO DE DADOS INICIAIS (SEEDS)
-- =====================================================

-- Equipamentos básicos
INSERT INTO equipamento (nome, descricao, disponivel) VALUES
('Halteres', 'Conjunto de halteres de diversos pesos', true),
('Barra Olímpica', 'Barra padrão olímpica 20kg', true),
('Esteira', 'Esteira elétrica para corrida', true),
('Bicicleta Ergométrica', 'Bicicleta para exercícios cardiovasculares', true),
('Banco Reto', 'Banco para exercícios de supino', true),
('Banco Inclinado', 'Banco ajustável para exercícios inclinados', true),
('Cabo de Aço', 'Sistema de cabos e polias', true),
('Leg Press', 'Máquina para exercícios de pernas', true),
('Smith Machine', 'Máquina Smith para exercícios guiados', true),
('Peso Corporal', 'Exercícios usando apenas o peso do corpo', true);

-- Exercícios básicos por região
INSERT INTO exercicio (nome, regiao, tipo, unilateral, equipamento_id) VALUES
-- Cardio
('Corrida na Esteira', 'CARDIO', 'CARDIO', false, 3),
('Bicicleta Ergométrica', 'CARDIO', 'CARDIO', false, 4),
('Burpees', 'CARDIO', 'CARDIO', false, 10),

-- Peito
('Supino Reto', 'PEITO', 'FORCA', false, 2),
('Supino Inclinado', 'PEITO', 'FORCA', false, 2),
('Flexão de Braço', 'PEITO', 'FORCA', false, 10),
('Crucifixo com Halteres', 'PEITO', 'FORCA', false, 1),

-- Costas
('Puxada na Polia', 'COSTAS', 'FORCA', false, 7),
('Remada Curvada', 'COSTAS', 'FORCA', false, 2),
('Barra Fixa', 'COSTAS', 'FORCA', false, 10),

-- Pernas
('Agachamento', 'PERNAS', 'FORCA', false, 2),
('Leg Press', 'PERNAS', 'FORCA', false, 8),
('Afundo', 'PERNAS', 'FORCA', true, 1),
('Extensão de Pernas', 'PERNAS', 'FORCA', false, 8),

-- Ombros
('Desenvolvimento com Halteres', 'OMBRO', 'FORCA', false, 1),
('Elevação Lateral', 'OMBRO', 'FORCA', false, 1),
('Elevação Frontal', 'OMBRO', 'FORCA', false, 1),

-- Bíceps
('Rosca Direta', 'BICEPS', 'FORCA', false, 1),
('Rosca Martelo', 'BICEPS', 'FORCA', false, 1),
('Rosca na Polia', 'BICEPS', 'FORCA', false, 7),

-- Tríceps
('Tríceps Testa', 'TRICEPS', 'FORCA', false, 1),
('Tríceps na Polia', 'TRICEPS', 'FORCA', false, 7),
('Mergulho', 'TRICEPS', 'FORCA', false, 10),

-- Abdômen
('Abdominal Tradicional', 'ABDOMEN', 'FORCA', false, 10),
('Prancha', 'ABDOMEN', 'RESISTENCIA', false, 10),
('Abdominal Oblíquo', 'ABDOMEN', 'FORCA', false, 10);

-- Personal Trainer de exemplo
INSERT INTO personal (nome, email, senha_hash, data_nascimento, formado, licenca) VALUES
('João Silva', 'joao.personal@gymbro.com', '$2a$10$example.hash.here', '1985-03-15', true, 'CREF-123456');

-- Aluno de exemplo
INSERT INTO alunos (nome, email, senha_hash, data_nascimento, peso) VALUES
('Maria Santos', 'maria.aluna@gymbro.com', '$2a$10$example.hash.here', '1990-07-20', 65.5);

-- Plano de treino exemplo
INSERT INTO plano (nome, descricao, criador_id, tipo_criador, publico, observacoes) VALUES
('Treino Iniciante - Corpo Todo', 'Plano básico para iniciantes focado em todos os grupos musculares', 1, 'PERSONAL', true, 'Ideal para quem está começando na academia');

-- Exercícios do plano exemplo
INSERT INTO plano_exercicio (plano_id, exercicio_id, series, repeticoes, peso_sugerido, ordem) VALUES
(1, 6, 3, 12, 0, 1),    -- Flexão de Braço
(1, 11, 3, 10, 0, 2),   -- Barra Fixa
(1, 12, 3, 15, 0, 3),   -- Agachamento
(1, 15, 3, 12, 5, 4),   -- Desenvolvimento com Halteres
(1, 18, 3, 12, 8, 5),   -- Rosca Direta
(1, 24, 3, 15, 0, 6);   -- Abdominal Tradicional

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

-- =====================================================
-- COMENTÁRIOS NAS TABELAS
-- =====================================================

COMMENT ON DATABASE gymbro_db IS 'Sistema de Gerenciamento de Academia GymBro';

COMMENT ON TABLE alunos IS 'Tabela de alunos da academia';
COMMENT ON TABLE personal IS 'Tabela de personal trainers';
COMMENT ON TABLE equipamento IS 'Tabela de equipamentos da academia';
COMMENT ON TABLE exercicio IS 'Tabela de exercícios disponíveis';
COMMENT ON TABLE treinos IS 'Tabela de treinos realizados pelos alunos';
COMMENT ON TABLE treino_exercicio IS 'Relacionamento entre treinos e exercícios';
COMMENT ON TABLE plano IS 'Tabela de planos de treino';
COMMENT ON TABLE plano_exercicio IS 'Relacionamento entre planos e exercícios';

-- =====================================================
-- SCRIPT CONCLUÍDO
-- =====================================================

-- Verificar se todas as tabelas foram criadas
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY tablename;

-- Verificar se todos os índices foram criados
SELECT 
    indexname,
    tablename,
    indexdef
FROM pg_indexes 
WHERE schemaname = 'public'
ORDER BY tablename, indexname;

COMMIT;
