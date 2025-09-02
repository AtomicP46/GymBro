-- =====================================================
-- CRIAÇÃO DOS TIPOS ENUM
-- =====================================================

-- Cria o enum regiao_corpo
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

-- Cria o enum tipo_exercicio
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

-- Cria o enum tipo_criador
CREATE TYPE tipo_criador AS ENUM (
  'USUARIO',
  'PERSONAL'
);
