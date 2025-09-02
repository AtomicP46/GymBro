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
    equipamento_id BIGINT NULL,
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