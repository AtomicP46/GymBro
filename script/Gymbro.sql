-- =====================================================
-- SCRIPT DE CRIAÇÃO DO BANCO DE DADOS GYMBRO (VERSÃO CORRIGIDA)
-- Sistema de Gerenciamento de Academia
-- PostgreSQL + Docker
-- =====================================================

-- 1. Criação do banco de dados
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
-- 2. CRIAÇÃO DOS TIPOS ENUM
-- =====================================================

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

CREATE TYPE tipo_criador AS ENUM (
  'USUARIO',
  'PERSONAL'
);

-- =====================================================
-- 3. CRIAÇÃO DAS TABELAS
-- =====================================================

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
    CONSTRAINT chk_licenca_nao_formado 
        CHECK (formado = TRUE OR (formado = FALSE AND licenca IS NOT NULL AND LENGTH(TRIM(licenca)) > 0))
);

CREATE TABLE equipamento (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE treinos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_hora_inicio TIMESTAMP,
    data_hora_fim TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    personal_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_treino_aluno 
        FOREIGN KEY (usuario_id) REFERENCES alunos(id) ON DELETE CASCADE,
    CONSTRAINT fk_treino_personal 
        FOREIGN KEY (personal_id) REFERENCES personal(id) ON DELETE SET NULL,
    CONSTRAINT chk_data_fim_posterior 
        CHECK (data_hora_fim IS NULL OR data_hora_fim > data_hora_inicio)
);

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
    CONSTRAINT uk_treino_exercicio 
        UNIQUE (treino_id, exercicio_id)
);

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
    CONSTRAINT uk_plano_exercicio 
        UNIQUE (plano_id, exercicio_id)
);

-- =====================================================
-- 4. ÍNDICES
-- =====================================================

CREATE INDEX idx_alunos_email ON alunos(email);
CREATE INDEX idx_alunos_nome ON alunos(nome);

CREATE INDEX idx_personal_email ON personal(email);
CREATE INDEX idx_personal_nome ON personal(nome);
CREATE INDEX idx_personal_formado ON personal(formado);

CREATE INDEX idx_exercicio_nome ON exercicio(nome);
CREATE INDEX idx_exercicio_regiao ON exercicio(regiao);
CREATE INDEX idx_exercicio_tipo ON exercicio(tipo);
CREATE INDEX idx_exercicio_equipamento ON exercicio(equipamento_id);

CREATE INDEX idx_treinos_usuario ON treinos(usuario_id);
CREATE INDEX idx_treinos_personal ON treinos(personal_id);
CREATE INDEX idx_treinos_data_inicio ON treinos(data_hora_inicio);

CREATE INDEX idx_treino_exercicio_treino ON treino_exercicio(treino_id);
CREATE INDEX idx_treino_exercicio_exercicio ON treino_exercicio(exercicio_id);

CREATE INDEX idx_plano_criador ON plano(criador_id, tipo_criador);
CREATE INDEX idx_plano_publico ON plano(publico);
CREATE INDEX idx_plano_data_criacao ON plano(data_criacao);

CREATE INDEX idx_plano_exercicio_plano ON plano_exercicio(plano_id);
CREATE INDEX idx_plano_exercicio_exercicio ON plano_exercicio(exercicio_id);

-- =====================================================
-- 5. TRIGGERS PARA ATUALIZAÇÃO DE TIMESTAMPS
-- =====================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

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
-- 6. INSERÇÃO DE DADOS INICIAIS (SEEDS)
-- =====================================================

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

INSERT INTO exercicio (nome, regiao, tipo, unilateral, equipamento_id) VALUES
  -- Cardio
  ('Corrida na Esteira',       'CARDIO', 'CARDIO_TEMPO_DISTANCIA_CALORIAS', false, 3),
  ('Bicicleta Ergométrica',    'CARDIO', 'CARDIO_TEMPO_DISTANCIA_CALORIAS', false, 4),
  ('Burpees',                  'CARDIO', 'CARDIO_TEMPO_DISTANCIA_CALORIAS', false, 10),

  -- Peito
  ('Supino Reto',              'PEITO',  'FORCA_PESO_REPETICOES', false, 2),
  ('Supino Inclinado',         'PEITO',  'FORCA_PESO_REPETICOES', false, 2),
  ('Flexão de Braço',          'PEITO',  'FORCA_PESO_REPETICOES', false, 10),
  ('Crucifixo com Halteres',   'PEITO',  'FORCA_PESO_REPETICOES', false, 1),

  -- Costas
  ('Puxada na Polia',          'COSTAS', 'FORCA_PESO_REPETICOES', false, 7),
  ('Remada Curvada',           'COSTAS', 'FORCA_PESO_REPETICOES', false, 2),
  ('Barra Fixa',               'COSTAS', 'FORCA_PESO_REPETICOES', false, 10),

  -- Pernas
  ('Agachamento',              'PERNAS', 'FORCA_PESO_REPETICOES', false, 2),
  ('Leg Press',                'PERNAS', 'FORCA_PESO_REPETICOES', false, 8),
  ('Afundo',                   'PERNAS', 'FORCA_PESO_REPETICOES', true,  1),
  ('Extensão de Pernas',       'PERNAS', 'FORCA_PESO_REPETICOES', false, 8),

  -- Ombros
  ('Desenvolvimento com Halteres','OMBRO','FORCA_PESO_REPETICOES', false, 1),
  ('Elevação Lateral',         'OMBRO',  'FORCA_PESO_REPETICOES', false, 1),
  ('Elevação Frontal',         'OMBRO',  'FORCA_PESO_REPETICOES', false, 1),

  -- Bíceps
  ('Rosca Direta',             'BICEPS', 'FORCA_PESO_REPETICOES', false, 1),
  ('Rosca Martelo',            'BICEPS', 'FORCA_PESO_REPETICOES', false, 1),
  ('Rosca na Polia',           'BICEPS', 'FORCA_PESO_REPETICOES', false, 7),

  -- Tríceps
  ('Tríceps Testa',            'TRICEPS','FORCA_PESO_REPETICOES', false, 1),
  ('Tríceps na Polia',         'TRICEPS','FORCA_PESO_REPETICOES', false, 7),
  ('Mergulho',                 'TRICEPS','FORCA_PESO_REPETICOES', false, 10),

  -- Abdômen
  ('Abdominal Tradicional',    'ABDOMEN','FORCA_PESO_REPETICOES', false, 10),
  ('Prancha',                  'ABDOMEN','PESO_CORPO_TEMPO',         false, 10),
  ('Abdominal Oblíquo',        'ABDOMEN','FORCA_PESO_REPETICOES', false, 10);

INSERT INTO personal (nome, email, senha_hash, data_nascimento, formado, licenca) VALUES
  ('João Silva', 'joao.personal@gymbro.com', '$2a$10$example.hash.here', '1985-03-15', true, 'CREF-123456');

INSERT INTO alunos (nome, email, senha_hash, data_nascimento, peso) VALUES
  ('Maria Santos', 'maria.aluna@gymbro.com', '$2a$10$example.hash.here', '1990-07-20', 65.5);

INSERT INTO plano (nome, descricao, criador_id, tipo_criador, publico, observacoes) VALUES
  ('Treino Iniciante - Corpo Todo', 
   'Plano básico para iniciantes focado em todos os grupos musculares', 
    1, 'PERSONAL', true, 'Ideal para quem está começando na academia');

INSERT INTO plano_exercicio (plano_id, exercicio_id, series, repeticoes, peso_sugerido, ordem) VALUES
  (1,  6,  3, 12, 0, 1),   -- Flexão de Braço
  (1, 11,  3, 10, 0, 2),   -- Barra Fixa
  (1, 12,  3, 15, 0, 3),   -- Agachamento
  (1, 15,  3, 12, 5, 4),   -- Desenvolvimento com Halteres
  (1, 18,  3, 12, 8, 5),   -- Rosca Direta
  (1, 24,  3, 15, 0, 6);   -- Abdominal Tradicional

-- =====================================================
-- 7. VIEWS
-- =====================================================

CREATE VIEW v_exercicios_completos AS
SELECT 
    e.id,
    e.nome,
    e.regiao,
    e.tipo,
    e.unilateral,
    eq.nome      AS equipamento_nome,
    eq.disponivel AS equipamento_disponivel
FROM exercicio e
LEFT JOIN equipamento eq ON e.equipamento_id = eq.id;

CREATE VIEW v_treinos_completos AS
SELECT 
    t.id,
    t.nome,
    t.data_hora_inicio,
    t.data_hora_fim,
    a.nome   AS aluno_nome,
    a.email  AS aluno_email,
    p.nome   AS personal_nome,
    CASE 
      WHEN t.data_hora_fim IS NOT NULL THEN 'FINALIZADO'
      WHEN t.data_hora_inicio IS NOT NULL THEN 'EM_ANDAMENTO'
      ELSE 'AGENDADO'
    END AS status
FROM treinos t
JOIN alunos a ON t.usuario_id = a.id
LEFT JOIN personal p ON t.personal_id = p.id;

-- =====================================================
-- 8. COMENTÁRIOS
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
-- 9. VERIFICAÇÕES FINAIS
-- =====================================================

SELECT schemaname, tablename, tableowner
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY tablename;

SELECT indexname, tablename, indexdef
FROM pg_indexes 
WHERE schemaname = 'public'
ORDER BY tablename, indexname;

COMMIT;