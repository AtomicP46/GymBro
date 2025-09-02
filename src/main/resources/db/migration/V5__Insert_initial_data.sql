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
('Corrida na Esteira', 'CARDIO', 'CARDIO_TEMPO_DISTANCIA_CALORIAS', false, 3),
('Bicicleta Ergométrica', 'CARDIO', 'CARDIO_TEMPO_DISTANCIA_CALORIAS', false, 4),
('Burpees', 'CARDIO', 'PESO_CORPO_REPETICOES', false, 10),

-- Peito
('Supino Reto', 'PEITO', 'FORCA_PESO_REPETICOES', false, 2),
('Supino Inclinado', 'PEITO', 'FORCA_PESO_REPETICOES', false, 2),
('Flexão de Braço', 'PEITO', 'PESO_CORPO_REPETICOES', false, 10),
('Crucifixo com Halteres', 'PEITO', 'FORCA_PESO_REPETICOES', false, 1),

-- Costas
('Puxada na Polia', 'COSTAS', 'FORCA_PESO_REPETICOES', false, 7),
('Remada Curvada', 'COSTAS', 'FORCA_PESO_REPETICOES', false, 2),
('Barra Fixa', 'COSTAS', 'PESO_CORPO_REPETICOES', false, 10),

-- Pernas
('Agachamento', 'PERNAS', 'FORCA_PESO_REPETICOES', false, 2),
('Leg Press', 'PERNAS', 'FORCA_PESO_REPETICOES', false, 8),
('Afundo', 'PERNAS', 'FORCA_PESO_REPETICOES', true, 1),
('Extensão de Pernas', 'PERNAS', 'FORCA_PESO_REPETICOES', false, 8),

-- Ombros
('Desenvolvimento com Halteres', 'OMBRO', 'FORCA_PESO_REPETICOES', false, 1),
('Elevação Lateral', 'OMBRO', 'FORCA_PESO_REPETICOES', false, 1),
('Elevação Frontal', 'OMBRO', 'FORCA_PESO_REPETICOES', false, 1),

-- Bíceps
('Rosca Direta', 'BICEPS', 'FORCA_PESO_REPETICOES', false, 1),
('Rosca Martelo', 'BICEPS', 'FORCA_PESO_REPETICOES', false, 1),
('Rosca na Polia', 'BICEPS', 'FORCA_PESO_REPETICOES', false, 7),

-- Tríceps
('Tríceps Testa', 'TRICEPS', 'FORCA_PESO_REPETICOES', false, 1),
('Tríceps na Polia', 'TRICEPS', 'FORCA_PESO_REPETICOES', false, 7),
('Mergulho', 'TRICEPS', 'PESO_CORPO_REPETICOES', false, 10),

-- Abdômen
('Abdominal Tradicional', 'ABDOMEN', 'PESO_CORPO_REPETICOES', false, 10),
('Prancha', 'ABDOMEN', 'PESO_CORPO_TEMPO', false, 10),
('Abdominal Oblíquo', 'ABDOMEN', 'PESO_CORPO_REPETICOES', false, 10);

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
