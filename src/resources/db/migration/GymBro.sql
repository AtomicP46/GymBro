-- Script completo para criar o banco de dados Gymbro no PostgreSQL
-- Execute este script no seu PostgreSQL para criar todas as tabelas necessárias

-- Criar o banco de dados (execute separadamente se necessário)
-- CREATE DATABASE Gymbro;

-- Conectar ao banco Gymbro antes de executar os comandos abaixo
-- \c Gymbro;

-- =====================================================
-- TABELA USER
-- =====================================================
CREATE TABLE IF NOT EXISTS "User" (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    peso FLOAT
);

-- Índice para melhorar performance de busca por email
CREATE INDEX IF NOT EXISTS idx_user_email ON "User"(email);

-- Comentários para documentação
COMMENT ON TABLE "User" IS 'Tabela para armazenar informações dos usuários';
COMMENT ON COLUMN "User".id IS 'Identificador único do usuário';
COMMENT ON COLUMN "User".nome IS 'Nome completo do usuário';
COMMENT ON COLUMN "User".email IS 'Email único do usuário para login';
COMMENT ON COLUMN "User".senha IS 'Hash da senha do usuário (BCrypt)';
COMMENT ON COLUMN "User".peso IS 'Peso do usuário em quilogramas';

-- =====================================================
-- TABELA PERSONAL
-- =====================================================
CREATE TABLE IF NOT EXISTS Personal (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    licenca VARCHAR NOT NULL,
    formacao BOOLEAN NOT NULL DEFAULT FALSE
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_personal_email ON Personal(email);
CREATE INDEX IF NOT EXISTS idx_personal_formacao ON Personal(formacao);

-- Comentários para documentação
COMMENT ON TABLE Personal IS 'Tabela para armazenar informações dos personal trainers';
COMMENT ON COLUMN Personal.id IS 'Identificador único do personal';
COMMENT ON COLUMN Personal.nome IS 'Nome completo do personal';
COMMENT ON COLUMN Personal.email IS 'Email único do personal para login';
COMMENT ON COLUMN Personal.senha IS 'Hash da senha do personal (BCrypt)';
COMMENT ON COLUMN Personal.licenca IS 'Número do diploma (se tem formação) ou licença de personal';
COMMENT ON COLUMN Personal.formacao IS 'Indica se possui formação em Educação Física';

-- =====================================================
-- TABELA EQUIPAMENTO
-- =====================================================
CREATE TABLE IF NOT EXISTS Equipamento (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    pesoequip FLOAT
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_equipamento_nome ON Equipamento(nome);
CREATE INDEX IF NOT EXISTS idx_equipamento_peso ON Equipamento(pesoequip);

-- Constraints para validação
ALTER TABLE Equipamento ADD CONSTRAINT chk_nome_not_empty CHECK (LENGTH(TRIM(nome)) > 0);
ALTER TABLE Equipamento ADD CONSTRAINT chk_peso_positivo CHECK (pesoequip IS NULL OR pesoequip >= 0);

-- Comentários para documentação
COMMENT ON TABLE Equipamento IS 'Tabela para armazenar informações dos equipamentos da academia';
COMMENT ON COLUMN Equipamento.id IS 'Identificador único do equipamento';
COMMENT ON COLUMN Equipamento.nome IS 'Nome do equipamento';
COMMENT ON COLUMN Equipamento.pesoequip IS 'Peso do equipamento em quilogramas (opcional)';

-- =====================================================
-- TABELA EXERCICIOS
-- =====================================================
CREATE TABLE IF NOT EXISTS Exercicios (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    regiao VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    unilateral BOOLEAN NOT NULL DEFAULT FALSE,
    equipamento_id INTEGER,
    FOREIGN KEY (equipamento_id) REFERENCES Equipamento(id) ON DELETE SET NULL
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_exercicios_nome ON Exercicios(nome);
CREATE INDEX IF NOT EXISTS idx_exercicios_regiao ON Exercicios(regiao);
CREATE INDEX IF NOT EXISTS idx_exercicios_tipo ON Exercicios(tipo);
CREATE INDEX IF NOT EXISTS idx_exercicios_unilateral ON Exercicios(unilateral);
CREATE INDEX IF NOT EXISTS idx_exercicios_equipamento ON Exercicios(equipamento_id);

-- Constraints para validação
ALTER TABLE Exercicios ADD CONSTRAINT chk_nome_exercicio_not_empty CHECK (LENGTH(TRIM(nome)) > 0);
ALTER TABLE Exercicios ADD CONSTRAINT chk_regiao_not_empty CHECK (LENGTH(TRIM(regiao)) > 0);
ALTER TABLE Exercicios ADD CONSTRAINT chk_tipo_not_empty CHECK (LENGTH(TRIM(tipo)) > 0);

-- Constraint para validar regiões permitidas
ALTER TABLE Exercicios ADD CONSTRAINT chk_regiao_valida 
CHECK (regiao IN ('Cardio', 'Abdomen', 'Biceps', 'Triceps', 'Peito', 'Costas', 'Ombro', 'Pernas'));

-- Constraint para validar tipos permitidos
ALTER TABLE Exercicios ADD CONSTRAINT chk_tipo_valido 
CHECK (tipo IN (
    'Força Peso e Repetições',
    'Força Peso e Tempo',
    'Peso do Corpo com Peso e Repetições',
    'Peso do Corpo e Repetições',
    'Peso do corpo e Tempo',
    'Peso do corpo com assistência de peso e repetições',
    'Cardio com tempo distância e Calorias ou Kcal',
    'Outros'
));

-- Comentários para documentação
COMMENT ON TABLE Exercicios IS 'Tabela para armazenar informações dos exercícios da academia';
COMMENT ON COLUMN Exercicios.id IS 'Identificador único do exercício';
COMMENT ON COLUMN Exercicios.nome IS 'Nome do exercício';
COMMENT ON COLUMN Exercicios.regiao IS 'Região do corpo trabalhada pelo exercício';
COMMENT ON COLUMN Exercicios.tipo IS 'Tipo/categoria do exercício';
COMMENT ON COLUMN Exercicios.unilateral IS 'Indica se o exercício é unilateral (true) ou bilateral (false)';
COMMENT ON COLUMN Exercicios.equipamento_id IS 'ID do equipamento usado no exercício (opcional)';

-- =====================================================
-- TABELA TREINO
-- =====================================================
CREATE TABLE IF NOT EXISTS Treino (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_hora_inicio TIMESTAMP,
    data_hora_fim TIMESTAMP,
    usuario_id INTEGER NOT NULL,
    personal_id INTEGER,
    FOREIGN KEY (usuario_id) REFERENCES "User"(id) ON DELETE CASCADE,
    FOREIGN KEY (personal_id) REFERENCES Personal(id) ON DELETE SET NULL
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_treino_usuario ON Treino(usuario_id);
CREATE INDEX IF NOT EXISTS idx_treino_personal ON Treino(personal_id);
CREATE INDEX IF NOT EXISTS idx_treino_data_inicio ON Treino(data_hora_inicio);
CREATE INDEX IF NOT EXISTS idx_treino_data_fim ON Treino(data_hora_fim);

-- Constraints para validação
ALTER TABLE Treino ADD CONSTRAINT chk_nome_treino_not_empty CHECK (LENGTH(TRIM(nome)) > 0);
ALTER TABLE Treino ADD CONSTRAINT chk_data_fim_after_inicio 
CHECK (data_hora_fim IS NULL OR data_hora_inicio IS NULL OR data_hora_fim >= data_hora_inicio);

-- Comentários para documentação
COMMENT ON TABLE Treino IS 'Tabela para armazenar informações dos treinos realizados';
COMMENT ON COLUMN Treino.id IS 'Identificador único do treino';
COMMENT ON COLUMN Treino.nome IS 'Nome/descrição do treino';
COMMENT ON COLUMN Treino.data_hora_inicio IS 'Data e hora de início do treino';
COMMENT ON COLUMN Treino.data_hora_fim IS 'Data e hora de finalização do treino';
COMMENT ON COLUMN Treino.usuario_id IS 'ID do usuário que realizou o treino';
COMMENT ON COLUMN Treino.personal_id IS 'ID do personal que acompanhou o treino (opcional)';

-- =====================================================
-- TABELA TREINO_EXERCICIO
-- =====================================================
CREATE TABLE IF NOT EXISTS Treino_Exercicio (
    id SERIAL PRIMARY KEY NOT NULL,
    treino_id INTEGER NOT NULL,
    exercicio_id INTEGER NOT NULL,
    series INTEGER,
    repeticoes INTEGER,
    peso_usado FLOAT,
    anotacoes TEXT,
    aquecimento BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (treino_id) REFERENCES Treino(id) ON DELETE CASCADE,
    FOREIGN KEY (exercicio_id) REFERENCES Exercicios(id) ON DELETE CASCADE
);

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_treino_exercicio_treino ON Treino_Exercicio(treino_id);
CREATE INDEX IF NOT EXISTS idx_treino_exercicio_exercicio ON Treino_Exercicio(exercicio_id);

-- Constraints para validação
ALTER TABLE Treino_Exercicio ADD CONSTRAINT chk_series_positivo 
CHECK (series IS NULL OR series > 0);
ALTER TABLE Treino_Exercicio ADD CONSTRAINT chk_repeticoes_positivo 
CHECK (repeticoes IS NULL OR repeticoes > 0);
ALTER TABLE Treino_Exercicio ADD CONSTRAINT chk_peso_positivo 
CHECK (peso_usado IS NULL OR peso_usado >= 0);

-- Comentários para documentação
COMMENT ON TABLE Treino_Exercicio IS 'Tabela para relacionar treinos com exercícios e suas execuções';
COMMENT ON COLUMN Treino_Exercicio.id IS 'Identificador único da relação treino-exercício';
COMMENT ON COLUMN Treino_Exercicio.treino_id IS 'ID do treino';
COMMENT ON COLUMN Treino_Exercicio.exercicio_id IS 'ID do exercício realizado';
COMMENT ON COLUMN Treino_Exercicio.series IS 'Número de séries planejadas/realizadas';
COMMENT ON COLUMN Treino_Exercicio.repeticoes IS 'Número de repetições por série';
COMMENT ON COLUMN Treino_Exercicio.peso_usado IS 'Peso utilizado no exercício (kg)';
COMMENT ON COLUMN Treino_Exercicio.anotacoes IS 'Observações sobre a execução do exercício';
COMMENT ON COLUMN Treino_Exercicio.aquecimento IS 'Indica se o exercício é de aquecimento';

-- =====================================================
-- DADOS DE EXEMPLO (OPCIONAL)
-- =====================================================

-- Inserir alguns equipamentos de exemplo
INSERT INTO Equipamento (nome, pesoequip) VALUES 
    ('Halteres 5kg', 5.0),
    ('Halteres 10kg', 10.0),
    ('Halteres 15kg', 15.0),
    ('Halteres 20kg', 20.0),
    ('Barra Olímpica', 20.0),
    ('Kettlebell 16kg', 16.0),
    ('Kettlebell 24kg', 24.0),
    ('Esteira', NULL),
    ('Bicicleta Ergométrica', NULL),
    ('Banco Supino', 25.0),
    ('Leg Press', 150.0),
    ('Smith Machine', 200.0),
    ('Cabo de Aço', NULL),
    ('Elíptico', NULL),
    ('Barra Fixa', NULL)
ON CONFLICT DO NOTHING;

-- Inserir alguns exercícios de exemplo
INSERT INTO Exercicios (nome, regiao, tipo, unilateral, equipamento_id) VALUES 
    ('Supino Reto', 'Peito', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Barra Olímpica' LIMIT 1)),
    ('Supino Inclinado com Halteres', 'Peito', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 15kg' LIMIT 1)),
    ('Rosca Direta', 'Biceps', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Barra Olímpica' LIMIT 1)),
    ('Rosca Martelo', 'Biceps', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 10kg' LIMIT 1)),
    ('Rosca Concentrada', 'Biceps', 'Força Peso e Repetições', true, (SELECT id FROM Equipamento WHERE nome = 'Halteres 10kg' LIMIT 1)),
    ('Tríceps Testa', 'Triceps', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 10kg' LIMIT 1)),
    ('Tríceps Francês', 'Triceps', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 15kg' LIMIT 1)),
    ('Desenvolvimento de Ombros', 'Ombro', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 10kg' LIMIT 1)),
    ('Elevação Lateral', 'Ombro', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Halteres 5kg' LIMIT 1)),
    ('Remada Curvada', 'Costas', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Barra Olímpica' LIMIT 1)),
    ('Puxada na Barra Fixa', 'Costas', 'Peso do Corpo e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Barra Fixa' LIMIT 1)),
    ('Agachamento Livre', 'Pernas', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Barra Olímpica' LIMIT 1)),
    ('Leg Press', 'Pernas', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Leg Press' LIMIT 1)),
    ('Agachamento Corpo Livre', 'Pernas', 'Peso do Corpo e Repetições', false, NULL),
    ('Afundo', 'Pernas', 'Peso do Corpo e Repetições', true, NULL),
    ('Prancha', 'Abdomen', 'Peso do corpo e Tempo', false, NULL),
    ('Abdominal Supra', 'Abdomen', 'Peso do Corpo e Repetições', false, NULL),
    ('Corrida', 'Cardio', 'Cardio com tempo distância e Calorias ou Kcal', false, (SELECT id FROM Equipamento WHERE nome = 'Esteira' LIMIT 1)),
    ('Bicicleta', 'Cardio', 'Cardio com tempo distância e Calorias ou Kcal', false, (SELECT id FROM Equipamento WHERE nome = 'Bicicleta Ergométrica' LIMIT 1)),
    ('Swing com Kettlebell', 'Pernas', 'Força Peso e Repetições', false, (SELECT id FROM Equipamento WHERE nome = 'Kettlebell 16kg' LIMIT 1))
ON CONFLICT DO NOTHING;

-- =====================================================
-- MENSAGEM DE SUCESSO
-- =====================================================
DO $$
BEGIN
    RAISE NOTICE '✅ Banco de dados Gymbro criado com sucesso!';
    RAISE NOTICE '📊 Tabelas criadas: User, Personal, Equipamento, Exercicios, Treino, Treino_Exercicio';
    RAISE NOTICE '🏋️ Dados de exemplo inseridos para equipamentos e exercícios';
    RAISE NOTICE '🚀 Sistema pronto para uso!';
END $$;
