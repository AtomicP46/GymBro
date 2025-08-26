package com.gymbro.controller;

import com.gymbro.dto.*;
import com.gymbro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private PlanoService planoService;

    // Página inicial
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // === ALUNOS ===
    @GetMapping("/alunos")
    public String listarAlunos(Model model) {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        model.addAttribute("alunos", alunos);
        return "alunos/lista";
    }

    @GetMapping("/alunos/novo")
    public String novoAluno(Model model) {
        model.addAttribute("aluno", new AlunoDTO());
        return "alunos/form";
    }

    @PostMapping("/alunos/salvar")
    public String salvarAluno(@ModelAttribute AlunoDTO aluno) {
        if (aluno.getId() != null) {
            alunoService.atualizar(aluno.getId(), aluno);
        } else {
            alunoService.criar(aluno);
        }
        return "redirect:/web/alunos";
    }

    @GetMapping("/alunos/editar/{id}")
    public String editarAluno(@PathVariable Long id, Model model) {
        AlunoDTO aluno = alunoService.buscarPorId(id);
        model.addAttribute("aluno", aluno);
        return "alunos/form";
    }

    @GetMapping("/alunos/excluir/{id}")
    public String deletarAluno(@PathVariable Long id) {
        alunoService.deletar(id);
        return "redirect:/web/alunos";
    }

    // === EXERCÍCIOS ===
    @GetMapping("/exercicios")
    public String listarExercicios(Model model) {
        List<ExercicioDTO> exercicios = exercicioService.listarTodos();
        model.addAttribute("exercicios", exercicios);
        return "exercicios/lista";
    }

    @GetMapping("/exercicios/novo")
    public String novoExercicio(Model model) {
        model.addAttribute("exercicio", new ExercicioDTO());
        return "exercicios/form";
    }

    @PostMapping("/exercicios/salvar")
    public String salvarExercicio(@ModelAttribute ExercicioDTO exercicio) {
        if (exercicio.getId() != null) {
            exercicioService.atualizar(exercicio.getId(), exercicio);
        } else {
            exercicioService.criar(exercicio);
        }
        return "redirect:/web/exercicios";
    }

    @GetMapping("/exercicios/editar/{id}")
    public String editarExercicio(@PathVariable Long id, Model model) {
        ExercicioDTO exercicio = exercicioService.buscarPorId(id);
        model.addAttribute("exercicio", exercicio);
        return "exercicios/form";
    }

    @GetMapping("/exercicios/excluir/{id}")
    public String deletarExercicio(@PathVariable Long id) {
        exercicioService.deletar(id);
        return "redirect:/web/exercicios";
    }

    // === TREINOS ===
    @GetMapping("/treinos")
    public String listarTreinos(Model model) {
        List<TreinoDTO> treinos = treinoService.listarTodos();
        model.addAttribute("treinos", treinos);
        return "treinos/lista";
    }

    @GetMapping("/treinos/novo")
    public String novoTreino(Model model) {
        model.addAttribute("treino", new TreinoDTO());
        List<AlunoDTO> alunos = alunoService.listarTodos();
        model.addAttribute("alunos", alunos);
        return "treinos/form";
    }

    @PostMapping("/treinos/salvar")
    public String salvarTreino(@ModelAttribute TreinoDTO treino) {
        if (treino.getId() != null) {
            treinoService.atualizar(treino.getId(), treino);
        } else {
            treinoService.criar(treino);
        }
        return "redirect:/web/treinos";
    }

    @GetMapping("/treinos/editar/{id}")
    public String editarTreino(@PathVariable Long id, Model model) {
        TreinoDTO treino = treinoService.buscarPorId(id);
        model.addAttribute("treino", treino);
        List<AlunoDTO> alunos = alunoService.listarTodos();
        model.addAttribute("alunos", alunos);
        return "treinos/form";
    }

    @GetMapping("/treinos/excluir/{id}")
    public String deletarTreino(@PathVariable Long id) {
        treinoService.deletar(id);
        return "redirect:/web/treinos";
    }

    // === PERSONAIS ===
    @GetMapping("/personais")
    public String listarPersonais(Model model) {
        List<PersonalDTO> personais = personalService.listarTodos();
        model.addAttribute("personais", personais);
        return "personais/lista";
    }

    @GetMapping("/personais/novo")
    public String novoPersonal(Model model) {
        model.addAttribute("personal", new PersonalDTO());
        return "personais/form";
    }

    @PostMapping("/personais/salvar")
    public String salvarPersonal(@ModelAttribute PersonalDTO personal) {
        if (personal.getId() != null) {
            personalService.atualizar(personal.getId(), personal);
        } else {
            personalService.criar(personal);
        }
        return "redirect:/web/personais";
    }

    @GetMapping("/personais/editar/{id}")
    public String editarPersonal(@PathVariable Long id, Model model) {
        PersonalDTO personal = personalService.buscarPorId(id);
        model.addAttribute("personal", personal);
        return "personais/form";
    }

    @GetMapping("/personais/excluir/{id}")
    public String deletarPersonal(@PathVariable Long id) {
        personalService.deletar(id);
        return "redirect:/web/personais";
    }

    // === EQUIPAMENTOS ===
    @GetMapping("/equipamentos")
    public String listarEquipamentos(Model model) {
        List<EquipamentoDTO> equipamentos = equipamentoService.listarTodos();
        model.addAttribute("equipamentos", equipamentos);
        return "equipamentos/lista";
    }

    @GetMapping("/equipamentos/novo")
    public String novoEquipamento(Model model) {
        model.addAttribute("equipamento", new EquipamentoDTO());
        return "equipamentos/form";
    }

    @PostMapping("/equipamentos/salvar")
    public String salvarEquipamento(@ModelAttribute EquipamentoDTO equipamento) {
        if (equipamento.getId() != null) {
            equipamentoService.atualizar(equipamento.getId(), equipamento);
        } else {
            equipamentoService.criar(equipamento);
        }
        return "redirect:/web/equipamentos";
    }

    @GetMapping("/equipamentos/editar/{id}")
    public String editarEquipamento(@PathVariable Long id, Model model) {
        EquipamentoDTO equipamento = equipamentoService.buscarPorId(id);
        model.addAttribute("equipamento", equipamento);
        return "equipamentos/form";
    }

    @GetMapping("/equipamentos/excluir/{id}")
    public String deletarEquipamento(@PathVariable Long id) {
        equipamentoService.deletar(id);
        return "redirect:/web/equipamentos";
    }

    // === PLANOS ===
    @GetMapping("/planos")
    public String listarPlanos(Model model) {
        List<PlanoDTO> planos = planoService.listarTodos();
        model.addAttribute("planos", planos);
        return "planos/lista";
    }

    @GetMapping("/planos/novo")
    public String novoPlano(Model model) {
        model.addAttribute("plano", new PlanoDTO());
        List<PersonalDTO> personais = personalService.listarTodos();
        model.addAttribute("personais", personais);
        return "planos/form";
    }

    @PostMapping("/planos/salvar")
    public String salvarPlano(@ModelAttribute PlanoDTO plano, @RequestParam(required = false) Long personalId) {
        if (personalId != null) {
            PersonalDTO personal = personalService.buscarPorId(personalId);
            plano.setPersonal(personal);
        }
        
        if (plano.getId() != null) {
            planoService.atualizar(plano.getId(), plano);
        } else {
            planoService.criar(plano);
        }
        return "redirect:/web/planos";
    }

    @GetMapping("/planos/editar/{id}")
    public String editarPlano(@PathVariable Long id, Model model) {
        PlanoDTO plano = planoService.buscarPorId(id);
        model.addAttribute("plano", plano);
        List<PersonalDTO> personais = personalService.listarTodos();
        model.addAttribute("personais", personais);
        return "planos/form";
    }

    @GetMapping("/planos/excluir/{id}")
    public String deletarPlano(@PathVariable Long id) {
        planoService.deletar(id);
        return "redirect:/web/planos";
    }
}
