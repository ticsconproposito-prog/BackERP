package BackERP.controller;

import BackERP.models.segperfiles;
import BackERP.repository.RepositoryPerfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PerfilesRestController {

    @Autowired
    private RepositoryPerfiles reper;

    @GetMapping("perfiles")
    public List<segperfiles> getPerfiles(){

        return reper.findAll();
    }

    @PostMapping("grabarPerfil")
    public String grabarPerfil(@RequestBody segperfiles Perfil){

        reper.save(Perfil);

        return "Grabado";
    }
    @PutMapping("editarPerfil/{id_Perfil}")
    public String editarPerfil(@PathVariable long id_Perfil, @RequestBody segperfiles Perfil){
        segperfiles updatePerfil = reper.findById(id_Perfil).get();
        updatePerfil.setNombrePerfil(Perfil.getNombrePerfil());
        updatePerfil.setFechaModificacion(Perfil.getFechaModificacion());
        updatePerfil.setHoraModificacion(Perfil.getHoraModificacion());
        updatePerfil.setIdUsuarioModificacion(Perfil.getIdUsuarioModificacion());
        reper.save(updatePerfil);

        return "Editado";
    }

    @DeleteMapping("eliminarPerfil/{id_Perfil}")
    public String eliminarPerfil(@PathVariable long id_Perfil){
        System.out.println("eliminar");
        segperfiles updatePerfil = reper.findById(id_Perfil).get();
        updatePerfil.setEstado(0);
        reper.save(updatePerfil);
        return "Eliminado";
    }
}
