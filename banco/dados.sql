-- Dados iniciais do sistema de atendimento médico (dados FICTÍCIOS).
-- Mínimo exigido: 1 secretária, 2 médicos, 3 pacientes e 4 consultas.
-- Senha de todos os usuários: 123456
-- As senhas já estão em hash BCrypt (formato $2a$, custo 10), aceito tanto pelo
-- jBCrypt do projeto de POO quanto pelo bcryptjs da API REST.
USE atendimento_medico;

INSERT INTO usuarios (nome, email, senha, perfil, crm, especialidade, cpf, telefone) VALUES
('Carla Mendes',    'carla@clinica.com',  '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'secretaria', NULL,            NULL,            NULL,          NULL),
('Dra. Ana Souza',  'ana@clinica.com',    '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'medico',     'CRM-PB 12345',  'Clínica Geral', NULL,          NULL),
('Dr. Bruno Lima',  'bruno@clinica.com',  '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'medico',     'CRM-PB 54321',  'Cardiologia',   NULL,          NULL),
('Diego Santos',    'diego@email.com',    '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'paciente',   NULL,            NULL,            '52998224725', '(83) 91234-5678'),
('Elisa Rocha',     'elisa@email.com',    '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'paciente',   NULL,            NULL,            '11144477735', '(83) 99876-5432'),
('Fernanda Alves',  'fernanda@email.com', '$2a$10$t7pS81Qj3/hxDRM7AQAqsup3G1O9tYRhzfbNVU7PIt6L6SMOisHj.', 'paciente',   NULL,            NULL,            '39053344705', '(83) 98888-1111');

-- ids gerados acima: 1 = secretária | 2 e 3 = médicos | 4, 5 e 6 = pacientes
INSERT INTO consultas (paciente_id, medico_id, data_hora, status, observacoes) VALUES
(4, 3, '2026-11-10 14:00:00', 'realizada', 'Pressão arterial normal. Retorno em 6 meses.'),
(5, 2, '2026-11-12 09:00:00', 'cancelada', NULL),
(4, 2, '2026-12-01 09:00:00', 'agendada',  NULL),
(6, 3, '2026-12-01 10:00:00', 'agendada',  NULL);
