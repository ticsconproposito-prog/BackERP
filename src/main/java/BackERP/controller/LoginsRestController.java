package BackERP.controller;

import BackERP.helper.segLoginsSpecs;
import BackERP.models.segLogins;
import BackERP.repository.RepositorySegLogins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginsRestController {

    @Autowired
    private RepositorySegLogins repoSegLogins;

    @GetMapping("segLogins")
    public List<segLogins> getSegLogins(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String estadoConexion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idLogin,asc") String sort
    ) {
        String[] sortParts = sort.split(",", 2);
        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Sort s = Sort.by(dir, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, s);

        Specification<segLogins> spec = Specification
                .where(segLoginsSpecs.estadoEquals(1))
                .and(segLoginsSpecs.idUsuarioEquals(idUsuario))
                .and(segLoginsSpecs.estadoConexionContains(estadoConexion));

        return repoSegLogins.findAll(spec, pageable).getContent();
    }

    @PostMapping("grabarSegLogin")
    public int grabarSegLogin(@RequestBody segLogins login) {
        login.setFechaModificacion(LocalDate.now());
        login.setHoraModificacion(LocalTime.now());
        login.setEstado(1);
        segLogins savedLogin = repoSegLogins.save(login);

        return savedLogin.getIdLogin();
    }

    @PutMapping("editarSegLogin/{idLogin}")
    public String editarSegLogin(@PathVariable int idLogin, @RequestBody segLogins login) {
        segLogins updateLogin = repoSegLogins.findById(idLogin).get();
        updateLogin.setEstadoConexion(login.getEstadoConexion());
        updateLogin.setIdUsuario(login.getIdUsuario());
        updateLogin.setIdUsuarioModificacion(login.getIdUsuarioModificacion());
        updateLogin.setFechaModificacion(LocalDate.now());
        updateLogin.setHoraModificacion(LocalTime.now());
        repoSegLogins.save(updateLogin);
        return "Editado";
    }

    @DeleteMapping("eliminarSegLogin/{idLogin}")
    public String eliminarSegLogin(@PathVariable int idLogin, @RequestBody segLogins login) {
        segLogins updateLogin = repoSegLogins.findById(idLogin).get();
        updateLogin.setIdUsuarioModificacion(login.getIdUsuarioModificacion());
        updateLogin.setFechaModificacion(LocalDate.now());
        updateLogin.setHoraModificacion(LocalTime.now());
        updateLogin.setEstado(0);
        repoSegLogins.save(updateLogin);
        return "Eliminado";
    }
}
