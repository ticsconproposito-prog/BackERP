package BackERP.controller;

import BackERP.models.segusuarios;
import BackERP.repository.RepositoryUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UsuariosRestController {


    @Autowired
    private RepositoryUsuarios reusr;

    @GetMapping("usuarios")
    public List<segusuarios> getUsuarios(){

        return reusr.findAll();
    }

    @PostMapping("grabarUsuario")
    public String grabarUsuario(@RequestBody segusuarios Usuario){

        reusr.save(Usuario);

        return "Grabado";
    }
    @PutMapping("editarUsuario/{id_Usuario}")
    public String editarUsuario(@PathVariable long id_Usuario, @RequestBody segusuarios Usuario){
        segusuarios updateUsuario = reusr.findById(id_Usuario).get();
        updateUsuario.setIdEmpleado(Usuario.getIdEmpleado());
        updateUsuario.setUsuario(Usuario.getUsuario());
        updateUsuario.setComentario(Usuario.getComentario());
        updateUsuario.setIdUsuarioModificacion(Usuario.getIdUsuarioModificacion());
        reusr.save(updateUsuario);

        return "Editado";
    }

    @DeleteMapping("eliminarUsuario/{id_Usuario}")
    public String eliminarUsuario(@PathVariable long id_Usuario){
        System.out.println("eliminar");
        segusuarios updateUsuario = reusr.findById(id_Usuario).get();
        updateUsuario.setEstado(0);
        reusr.save(updateUsuario);
        return "Eliminado";
    }


}
