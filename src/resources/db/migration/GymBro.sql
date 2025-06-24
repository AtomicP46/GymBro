
-- Tabela Personal
CREATE TABLE Personal (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    licenca VARCHAR NOT NULL
);

-- Tabela User
CREATE TABLE "User" (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    peso FLOAT
);

-- Tabela Equipamento
CREATE TABLE Equipamento (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    pesoequip FLOAT
);

-- Tabela Treino
CREATE TABLE Treino (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    usuario_id INTEGER NOT NULL,
    personal_id INTEGER NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES "User"(id),
    FOREIGN KEY (personal_id) REFERENCES Personal(id)
);

-- Tabela Exercicios
CREATE TABLE Exercicios (
    id SERIAL PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    regiao VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    unilateral BOOLEAN,
    equipamento_id INTEGER,
    FOREIGN KEY (equipamento_id) REFERENCES Equipamento(id)
);

-- Tabela Treino_Exercicio
CREATE TABLE Treino_Exercicio (
    id SERIAL PRIMARY KEY NOT NULL,
    treino_id INTEGER NOT NULL,
    exercicio_id INTEGER NOT NULL,
    series INTEGER,
    repeticoes INTEGER,
    peso_usado FLOAT,
    anotacoes VARCHAR(100) NOT NULL,
    aquecimento BOOLEAN,
    FOREIGN KEY (treino_id) REFERENCES Treino(id),
    FOREIGN KEY (exercicio_id) REFERENCES Exercicios(id)
);
