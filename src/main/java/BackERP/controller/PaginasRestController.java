package BackERP.controller;

import BackERP.models.segpaginas;
import BackERP.repository.RepositoryPaginas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PaginasRestController {

    @Autowired
    private RepositoryPaginas repag;

    @GetMapping("pagina")
    public List<segpaginas> getPaginas(){

        return repag.findAll();
    }

    @PostMapping("grabarPagina")
    public String grabarPagina(@RequestBody segpaginas pagina){

        repag.save(pagina);

        return "Grabado";
    }
    @PutMapping("editarPagina/{id_Pagina}")
    public String editarPagina(@PathVariable long id_pagina, @RequestBody segpaginas pagina){
        segpaginas updatePagina = repag.findById(pagina.getIdPagina()).get();
        updatePagina.setURL(pagina.getURL());
        updatePagina.setFechaModificacion(pagina.getFechaModificacion());
        updatePagina.setHoraModificacion(pagina.getHoraModificacion());
        updatePagina.setIdUsuarioModificacion(pagina.getIdUsuarioModificacion());
        repag.save(updatePagina);

        return "Editado";
    }

    @DeleteMapping("eliminarPagina/{id_pagina}")
    public String eliminarPagina(@PathVariable long id_pagina){
        System.out.println("eliminar");
        segpaginas updatePagina = repag.findById(id_pagina).get();
        updatePagina.setEstado(0);
        repag.save(updatePagina);
        return "Eliminado";
    }

}
