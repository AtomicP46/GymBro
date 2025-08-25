-- Tabela de Alunos
CREATE TABLE alunos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    peso DECIMAL(5,2) NOT NULL,
    CONSTRAINT chk_email_format_aluno 
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_peso_positivo 
        CHECK (peso > 0),
    CONSTRAINT chk_data_nascimento_passado_aluno 
        CHECK (data_nascimento < CURRENT_DATE)
);

CREATE INDEX idx_alunos_email ON alunos(email);
CREATE INDEX idx_alunos_data_nascimento ON alunos(data_nascimento);

COMMENT ON TABLE alunos IS 'Entidade Aluno sem herança, inclui campos de usuário e peso';
COMMENT ON COLUMN alunos.peso IS 'Peso do aluno em kg';

-- Tabela de Personais
CREATE TABLE personal (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    formado BOOLEAN NOT NULL,
    codigo_validacao VARCHAR(50),
    link_validacao VARCHAR(500),
    licenca VARCHAR(100),
    CONSTRAINT chk_email_format_personal 
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_data_nascimento_passado_personal 
        CHECK (data_nascimento < CURRENT_DATE)
);

CREATE INDEX idx_personal_email ON personal(email);
CREATE INDEX idx_personal_data_nascimento ON personal(data_nascimento);

COMMENT ON TABLE personal IS 'Entidade Personal sem herança, inclui campos de usuário e certificação';
COMMENT ON COLUMN personal.formado IS 'Indica se o personal é formado em educação física';
COMMENT ON COLUMN personal.codigo_validacao IS 'Código de validação do diploma';
COMMENT ON COLUMN personal.link_validacao IS 'Link de validação do diploma';
COMMENT ON COLUMN personal.licenca IS 'Licença obrigatória para atuação';