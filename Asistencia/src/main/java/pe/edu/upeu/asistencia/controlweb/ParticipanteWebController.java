package pe.edu.upeu.asistencia.controlweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

@Controller
@RequestMapping(path = {"/participantes","/"})
public class ParticipanteWebController {

    @Autowired
    private ParticipanteServicioI participanteService;


    @GetMapping
    public String list(Model model) {
        model.addAttribute("participantes", participanteService.findAll());
        model.addAttribute("view", "participantes/lista");
        model.addAttribute("title", "Lista de Participantes");
        return "layout"; // plantilla Thymeleaf
    }
    @GetMapping("/new")
    public String nuevo(Model model) {
        model.addAttribute("participante", new Participante());
        model.addAttribute("carreras", Carrera.values());
        model.addAttribute("tipos", TipoParticipante.values());
        model.addAttribute("view", "participantes/form");
        model.addAttribute("title", "Nuevo Participante");
        return "layout";
    }



    @PostMapping("/save")
    public String save(@ModelAttribute Participante participante) {
        System.out.println("save()"+participante.toString());
        participanteService.save(participante);
        return "redirect:/participantes";
    }

    @GetMapping("/edit/{dni}")
    public String editar(@PathVariable String dni, Model model) {
        model.addAttribute("participante", participanteService.findById(dni));
        model.addAttribute("carreras", Carrera.values());
        model.addAttribute("tipos", TipoParticipante.values());
        model.addAttribute("view", "participantes/form");
        model.addAttribute("title", "Editar Participante");
        return "layout";
    }

    @GetMapping("/delete/{dni}")
    public String delete(@PathVariable String dni) {
        participanteService.delete(dni);
        return "redirect:/participantes";
    }
}
