package com.gymbro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymbro.dto.AlunoDTO;
import com.gymbro.dto.EquipamentoDTO;
import com.gymbro.dto.ExercicioDTO;
import com.gymbro.dto.PersonalDTO;
import com.gymbro.dto.PlanoDTO;
import com.gymbro.dto.TreinoDTO;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.service.AlunoService;
import com.gymbro.service.EquipamentoService;
import com.gymbro.service.ExercicioService;
import com.gymbro.service.PersonalService;
import com.gymbro.service.PlanoService;
import com.gymbro.service.TreinoService;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ExercicioService exercicioService;

    @Autowired
    private TreinoService treinoService;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private EquipamentoService equipamentoService;
        @ModelAttribute("equipamentos")
    public List<EquipamentoDTO> populaEquipamentos() {
        return equipamentoService.listarTodos();
    }

    @Autowired
    private PlanoService planoService;

    // Página inicial
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // === ALUNOS ===
    @GetMapping("/alunos")
    public String listarAlunos(Model model) {
        try {
            List<AlunoDTO> alunos = alunoService.listarTodos();
            model.addAttribute("alunos", alunos);
            return "alunos/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de alunos: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/alunos/novo")
    public String novoAluno(Model model) {
        try {
            model.addAttribute("aluno", new AlunoDTO());
            return "alunos/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de aluno: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/alunos/salvar")
    public String salvarAluno(@ModelAttribute AlunoDTO aluno) {
        if (aluno.getId() != null) {
            alunoService.atualizarAluno(aluno.getId(), aluno);
        } else {
            alunoService.criarAluno(aluno);
        }
        return "redirect:/web/alunos";
    }

    @GetMapping("/alunos/editar/{id}")
    public String editarAluno(@PathVariable Long id, Model model) {
        try {
            AlunoDTO aluno = alunoService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
            model.addAttribute("aluno", aluno);
            return "alunos/form";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar aluno: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/alunos/excluir/{id}")
    public String deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
        return "redirect:/web/alunos";
    }

    // === EXERCÍCIOS ===
    @GetMapping("/exercicios")
    public String listarExercicios(Model model) {
        try {
            List<ExercicioDTO> exercicios = exercicioService.listarTodos().stream()
                    .map(this::convertToExercicioDTO)
                    .collect(Collectors.toList());
            model.addAttribute("exercicios", exercicios);
            return "exercicios/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de exercícios: " + e.getMessage());
            return "error/500";
        }
    }


    @GetMapping("/exercicios/novo")
    public String novoExercicio(Model model) {
        try {
            model.addAttribute("exercicio", new ExercicioDTO());
            // CORREÇÃO: popula a lista de equipamentos antes de renderizar o form
            List<EquipamentoDTO> equipamentos = equipamentoService.listarTodos();
            model.addAttribute("equipamentos", equipamentos);
            return "exercicios/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de exercício: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/exercicios/salvar")
    public String salvarExercicio(@ModelAttribute ExercicioDTO exercicio) {
        if (exercicio.getId() != null) {
            exercicioService.atualizarExercicio(exercicio.getId(), exercicio);
        } else {
            exercicioService.criarExercicio(exercicio);
        }
        return "redirect:/web/exercicios";
    }

    @GetMapping("/exercicios/editar/{id}")
    public String editarExercicio(@PathVariable Long id, Model model) {
        try {
            ExercicioDTO exercicio = convertToExercicioDTO(exercicioService.buscarPorId(id));
            model.addAttribute("exercicio", exercicio);
            // CORREÇÃO: popula a lista de equipamentos para o dropdown
            List<EquipamentoDTO> equipamentos = equipamentoService.listarTodos();
            model.addAttribute("equipamentos", equipamentos);
            return "exercicios/form";
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Exercício não encontrado com ID: " + id);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar exercício: " + e.getMessage());
            return "error/500";
        }
    }


    @GetMapping("/exercicios/excluir/{id}")
    public String deletarExercicio(@PathVariable Long id) {
        exercicioService.deletarExercicio(id);
        return "redirect:/web/exercicios";
    }

    // === TREINOS ===
    @GetMapping("/treinos")
    public String listarTreinos(Model model) {
        try {
            List<TreinoDTO> treinos = treinoService.listarTodosTreinos().stream()
                    .map(this::convertToTreinoDTO)
                    .collect(java.util.stream.Collectors.toList());
            model.addAttribute("treinos", treinos);
            return "treinos/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de treinos: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/treinos/novo")
    public String novoTreino(Model model) {
        try {
            model.addAttribute("treino", new TreinoDTO());
            List<AlunoDTO> alunos = alunoService.listarTodos();
            model.addAttribute("alunos", alunos);
            return "treinos/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de treino: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/treinos/salvar")
    public String salvarTreino(@ModelAttribute TreinoDTO treino) {
        if (treino.getId() != null) {
            treinoService.atualizarTreino(treino.getId(), treino);
        } else {
            treinoService.criarTreino(treino);
        }
        return "redirect:/web/treinos";
    }

    @GetMapping("/treinos/editar/{id}")
    public String editarTreino(@PathVariable Long id, Model model) {
        try {
            TreinoDTO treino = convertToTreinoDTO(treinoService.buscarTreinoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado com ID: " + id)));
            model.addAttribute("treino", treino);
            List<AlunoDTO> alunos = alunoService.listarTodos();
            model.addAttribute("alunos", alunos);
            return "treinos/form";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar treino: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/treinos/excluir/{id}")
    public String deletarTreino(@PathVariable Long id) {
        treinoService.deletarTreino(id);
        return "redirect:/web/treinos";
    }

    // === PERSONAIS ===
    @GetMapping("/personais")
    public String listarPersonais(Model model) {
        try {
            List<PersonalDTO> personais = personalService.listarTodos();
            model.addAttribute("personais", personais);
            return "personais/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de personais: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/personais/novo")
    public String novoPersonal(Model model) {
        try {
            model.addAttribute("personal", new PersonalDTO());
            return "personais/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de personal: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/personais/salvar")
    public String salvarPersonal(@ModelAttribute PersonalDTO personal) {
        if (personal.getId() != null) {
            personalService.atualizarPersonal(personal.getId(), personal);
        } else {
            personalService.criarPersonal(personal);
        }
        return "redirect:/web/personais";
    }

    @GetMapping("/personais/editar/{id}")
    public String editarPersonal(@PathVariable Long id, Model model) {
        try {
            PersonalDTO personal = personalService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personal não encontrado com ID: " + id));
            model.addAttribute("personal", personal);
            return "personais/form";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar personal: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/personais/excluir/{id}")
    public String deletarPersonal(@PathVariable Long id) {
        personalService.deletarPersonal(id);
        return "redirect:/web/personais";
    }

    // === EQUIPAMENTOS ===
    @GetMapping("/equipamentos")
    public String listarEquipamentos(Model model) {
        try {
            List<EquipamentoDTO> equipamentos = equipamentoService.listarTodos();
            model.addAttribute("equipamentos", equipamentos);
            return "equipamentos/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de equipamentos: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/equipamentos/novo")
    public String novoEquipamento(Model model) {
        try {
            model.addAttribute("equipamento", new EquipamentoDTO());
            return "equipamentos/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de equipamento: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/equipamentos/salvar")
    public String salvarEquipamento(@ModelAttribute EquipamentoDTO equipamento) {
        if (equipamento.getId() != null) {
            equipamentoService.atualizarEquipamento(equipamento.getId(), equipamento);
        } else {
            equipamentoService.criarEquipamento(equipamento);
        }
        return "redirect:/web/equipamentos";
    }

    @GetMapping("/equipamentos/editar/{id}")
    public String editarEquipamento(@PathVariable Long id, Model model) {
        try {
            EquipamentoDTO equipamento = equipamentoService.buscarPorId(id);
            model.addAttribute("equipamento", equipamento);
            return "equipamentos/form";
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Equipamento não encontrado com ID: " + id);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar equipamento: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/equipamentos/excluir/{id}")
    public String deletarEquipamento(@PathVariable Long id) {
        equipamentoService.deletarEquipamento(id);
        return "redirect:/web/equipamentos";
    }

    // === PLANOS ===
    @GetMapping("/planos")
    public String listarPlanos(Model model) {
        try {
            List<PlanoDTO> planos = planoService.listarTodos().stream()
                    .map(this::convertToPlanoDTO)
                    .collect(java.util.stream.Collectors.toList());
            model.addAttribute("planos", planos);
            return "planos/lista";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar lista de planos: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/planos/novo")
    public String novoPlano(Model model) {
        try {
            model.addAttribute("plano", new PlanoDTO());
            List<PersonalDTO> personais = personalService.listarTodos();
            model.addAttribute("personais", personais);
            return "planos/form";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar formulário de plano: " + e.getMessage());
            return "error/500";
        }
    }

    @PostMapping("/planos/salvar")
    public String salvarPlano(@ModelAttribute PlanoDTO plano, @RequestParam(required = false) Long personalId) {
        if (personalId != null) {
            plano.setPersonalId(personalId);
        }
        
        if (plano.getId() != null) {
            planoService.atualizarPlano(plano.getId(), plano, plano.getCriadorId(), plano.getTipoCriador());
        } else {
            planoService.criarPlano(plano);
        }
        return "redirect:/web/planos";
    }

    @GetMapping("/planos/editar/{id}")
    public String editarPlano(@PathVariable Long id, Model model) {
        try {
            PlanoDTO plano = convertToPlanoDTO(planoService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id)));
            model.addAttribute("plano", plano);
            List<PersonalDTO> personais = personalService.listarTodos();
            model.addAttribute("personais", personais);
            return "planos/form";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar plano: " + e.getMessage());
            return "error/500";
        }
    }

    @GetMapping("/planos/excluir/{id}")
    public String deletarPlano(@PathVariable Long id) {
        com.gymbro.model.Plano plano = planoService.buscarPorId(id).orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com ID: " + id));
        planoService.excluirPlano(id, plano.getCriadorId(), plano.getTipoCriador());
        return "redirect:/web/planos";
    }

    // Helper methods for conversion
    private ExercicioDTO convertToExercicioDTO(com.gymbro.model.Exercicio exercicio) {
        try {
            if (exercicio == null) {
                throw new IllegalArgumentException("Exercício não pode ser nulo");
            }
            
            ExercicioDTO dto = new ExercicioDTO();
            dto.setId(exercicio.getId());
            dto.setNome(exercicio.getNome());
            dto.setRegiao(exercicio.getRegiao() != null ? exercicio.getRegiao().getDescricao() : "");
            dto.setTipo(exercicio.getTipo() != null ? exercicio.getTipo().getDescricao() : "");
            dto.setUnilateral(exercicio.getUnilateral());
            dto.setEquipamentoId(exercicio.getEquipamento() != null ? exercicio.getEquipamento().getId() : null);
            return dto;
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao converter exercício para DTO: " + e.getMessage(), e);
        }
    }

    private TreinoDTO convertToTreinoDTO(com.gymbro.model.Treino treino) {
        try {
            if (treino == null) {
                throw new IllegalArgumentException("Treino não pode ser nulo");
            }
            
            TreinoDTO dto = new TreinoDTO();
            dto.setId(treino.getId());
            dto.setNome(treino.getNome());
            dto.setDescricao(treino.getNome());
            dto.setDataHoraInicio(treino.getDataHoraInicio());
            dto.setDataHoraFim(treino.getDataHoraFim());
            dto.setAlunoId(treino.getUsuarioId());
            dto.setPersonalId(treino.getPersonalId());
            return dto;
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao converter treino para DTO: " + e.getMessage(), e);
        }
    }

    private PlanoDTO convertToPlanoDTO(com.gymbro.model.Plano plano) {
        try {
            if (plano == null) {
                throw new IllegalArgumentException("Plano não pode ser nulo");
            }
            
            PlanoDTO dto = new PlanoDTO();
            dto.setId(plano.getId());
            dto.setNome(plano.getNome());
            dto.setDescricao(plano.getDescricao());
            dto.setCriadorId(plano.getCriadorId());
            dto.setTipoCriador(plano.getTipoCriador());
            dto.setPublico(plano.getPublico());
            dto.setObservacoes(plano.getObservacoes());
            return dto;
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao converter plano para DTO: " + e.getMessage(), e);
        }
    }
}
