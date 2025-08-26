GymBro Training Management System
Build Status
Coverage
License
Gerencie todos os aspectos de uma academia: alunos, personals, exercícios, equipamentos, planos e treinos. Desenvolvido em Java Spring Boot com PostgreSQL e uma interface web responsiva em Thymeleaf.

Sumário
- Recursos
- Pré-requisitos
- Instalação
- Configuração
- Execução
- API REST
- Interface Web
- Estrutura do Projeto
- Testes
- Contribuição
- Licença

Recursos
- Cadastro e gestão de Usuários, Alunos, Personais, Exercícios, Equipamentos, Planos e Treinos
- Autenticação com JWT (/auth/login)
- Interface web responsiva usando Thymeleaf e Bootstrap 5
- API REST completa para integração com clientes externos
- Perfis de ambiente: default (localhost) e docker
- Cobertura de testes com JUnit 5, Mockito e JaCoCo

Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

Instalação
- Clone o repositório
git clone https://github.com/usuario/gymbro.git
cd gymbro
- Configure variáveis de ambiente (opcional)
export DB_USER=postgres
export DB_PASSWORD=MEp@706504
export DB_NAME=Gymbro
export DB_PORT=5432
- Inicie o banco de dados com Docker
docker-compose up -d db
- Ajuste o perfil Spring Boot em application.properties
spring.profiles.active=docker



Configuração
A aplicação suporta dois perfis:
- default: conecta ao localhost:5432/Gymbro
- docker: conecta ao container Docker
Os parâmetros de conexão residem em src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:Gymbro}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:MEp@706504}



Execução
Localmente
mvn clean install
mvn spring-boot:run


Com Docker Compose
# Sobe app e banco juntos
docker-compose up --build

# Para e remove containers
docker-compose down


A aplicação ficará disponível em http://localhost:8080.

API REST
Autenticação
- POST /auth/login
Corpo:
{
  "username": "admin",
  "password": "admin"
}


Usuários
- GET  /usuarios — listar
- POST /usuarios — criar
Personals
- POST /personals — criar
Alunos
- POST /alunos — criar
Para mais endpoints, consulte a documentação Swagger (quando habilitada) ou a interface Web.


Interface Web
Observação, a interface web segue indisponivel no momento, estando esta para ser implementada posteriormente 

Acesse o painel completo em http://localhost:8080/web/.
Credenciais
- Administrador: admin / admin
- Usuário comum: user / user
Seções principais
- Dashboard — visão geral
- Alunos — cadastro, edição, exclusão
Gerencia majoritariamente os usuários comuns, alunos ou frequentadores de um ambiente de musculação

- Personals — gestão de CREF e especialidades
Referente a personal trainers, que podem associar-se a usuários para vizualizar e gerenciar seus treinos

- Exercícios — tipo, equipamento
Referente aos detalhes dos exercicios realizados na academia

- Equipamentos — controle de disponibilidade
Referente aos equipamentos disponiveis na sua academia

- Planos — objetivos e duração
Referente a planos de treinos prontos

- Treinos — associar alunos e exercícios
Referente a treinos individuais realizados por um usuário na academia


Estrutura do Projeto
src/
├── main/
│   ├── java/com/gymbro/
│   │   ├── controller/     # REST e WebControllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Interfaces de acesso a dados
│   │   └── service/        # Lógica de negócio
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/gymbro/    # Testes unitários e de integração



Testes
Executar todos os testes
mvn test


Relatório de cobertura (JaCoCo)
mvn clean test jacoco:report
# Abra target/site/jacoco/index.html


Testes específicos
mvn test -Dtest=AlunoServiceTest



Contribuição
- Fork do repositório
- Crie uma branch: git checkout -b feature/nova-funcionalidade
- Commit das alterações: git commit -m "Adiciona X"
- Push para branch: git push origin feature/nova-funcionalidade
- Abra um Pull Request no GitHub
Por favor, siga as convenções de código e inclua testes para novas funcionalidades.

Licença
Este é um projeto não licenciado, feito com fins académicos de aprendizado e desenvolvimento de habilidades com programação orientada a objetos

