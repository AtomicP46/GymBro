package br.ifg.gymbro.services;

import br.ifg.gymbro.dto.LoginDTO;
import br.ifg.gymbro.dto.PersonalDTO;
import br.ifg.gymbro.model.Personal;
import br.ifg.gymbro.repository.PersonalRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonalService {
    private PersonalRepository personalRepository;

    public PersonalService(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    public Personal criarPersonal(PersonalDTO personalDTO) throws SQLException, IllegalArgumentException {
        // Validações
        if (personalDTO.getNome() == null || personalDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (personalDTO.getEmail() == null || personalDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (personalDTO.getSenha() == null || personalDTO.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        
        if (personalDTO.getLicenca() == null || personalDTO.getLicenca().trim().isEmpty()) {
            throw new IllegalArgumentException("Licença é obrigatória");
        }

        if (personalDTO.getFormacao() == null) {
            throw new IllegalArgumentException("Informação sobre formação em EF é obrigatória");
        }

        // Verificar se email já existe
        if (personalRepository.emailExiste(personalDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Criar hash da senha
        String senhaHash = BCrypt.hashpw(personalDTO.getSenha(), BCrypt.gensalt());

        // Criar personal
        Personal personal = new Personal(
            personalDTO.getNome(),
            personalDTO.getEmail(),
            senhaHash,
            personalDTO.getLicenca(),
            personalDTO.getFormacao()
        );

        return personalRepository.salvar(personal);
    }

    public Optional<Personal> autenticarPersonal(LoginDTO loginDTO) throws SQLException {
        if (loginDTO.getEmail() == null || loginDTO.getSenha() == null) {
            return Optional.empty();
        }

        Optional<Personal> personalOpt = personalRepository.buscarPorEmail(loginDTO.getEmail());
        
        if (personalOpt.isPresent()) {
            Personal personal = personalOpt.get();
            if (BCrypt.checkpw(loginDTO.getSenha(), personal.getSenhaHash())) {
                return Optional.of(personal);
            }
        }
        
        return Optional.empty();
    }

    public Optional<Personal> buscarPersonalPorId(Long id) throws SQLException {
        return personalRepository.buscarPorId(id);
    }

    public Optional<Personal> buscarPersonalPorEmail(String email) throws SQLException {
        return personalRepository.buscarPorEmail(email);
    }

    public List<Personal> listarTodosPersonais() throws SQLException {
        return personalRepository.buscarTodos();
    }

    public List<Personal> listarPersonaisPorFormacao(boolean temFormacao) throws SQLException {
        return personalRepository.buscarPorFormacao(temFormacao);
    }

    public Personal atualizarPersonal(Long id, PersonalDTO personalDTO) throws SQLException, IllegalArgumentException {
        Optional<Personal> personalOpt = personalRepository.buscarPorId(id);
        
        if (personalOpt.isEmpty()) {
            throw new IllegalArgumentException("Personal não encontrado");
        }

        Personal personal = personalOpt.get();
        
        if (personalDTO.getNome() != null && !personalDTO.getNome().trim().isEmpty()) {
            personal.setNome(personalDTO.getNome());
        }
        
        if (personalDTO.getEmail() != null && !personalDTO.getEmail().trim().isEmpty()) {
            // Verificar se o novo email já existe (exceto para o próprio personal)
            Optional<Personal> personalComEmail = personalRepository.buscarPorEmail(personalDTO.getEmail());
            if (personalComEmail.isPresent() && !personalComEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email já está em uso");
            }
            personal.setEmail(personalDTO.getEmail());
        }
        
        if (personalDTO.getLicenca() != null && !personalDTO.getLicenca().trim().isEmpty()) {
            personal.setLicenca(personalDTO.getLicenca());
        }

        if (personalDTO.getFormacao() != null) {
            personal.setFormacao(personalDTO.getFormacao());
        }

        personalRepository.atualizar(personal);
        return personal;
    }

    public void deletarPersonal(Long id) throws SQLException, IllegalArgumentException {
        Optional<Personal> personalOpt = personalRepository.buscarPorId(id);
        
        if (personalOpt.isEmpty()) {
            throw new IllegalArgumentException("Personal não encontrado");
        }

        personalRepository.deletar(id);
    }
}
