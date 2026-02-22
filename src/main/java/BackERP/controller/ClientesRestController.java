package BackERP.controller;


import BackERP.helper.erpClientesSpecs;
import BackERP.models.erpclientes;
import BackERP.repository.RepositoryClientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ClientesRestController {
    @Autowired
    private RepositoryClientes repcli;

    @GetMapping("clientes")
    public Page<erpclientes> getClientes(
            @RequestParam(required = false) Integer tipoDocumento,
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) String documentoCliente,
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idCliente,asc") String sort
    ) {

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpclientes> spec = Specification
                .where(erpClientesSpecs.estadoEquals(1))
                .and(erpClientesSpecs.idCLienteContains(idCliente))
                .and(erpClientesSpecs.nombreClienteContains(nombreCliente));
        // Aquí aplicas la condición
        if (tipoDocumento != null) {
            if (tipoDocumento == 1) {
                spec = spec.and(erpClientesSpecs.nitClienteContains(documentoCliente));
            } else {
                spec = spec.and(erpClientesSpecs.DPIPasaporteClienteContains(documentoCliente));
            }
        }

        return repcli.findAll(spec, pageable);
    }

    @PostMapping("grabarCliente")
    public String grabarCliente(@RequestBody erpclientes clientes){

        // valor por defecto
        clientes.setFechaModificacion(LocalDate.now());
        clientes.setHoraModificacion(LocalTime.now());
        clientes.setEstado(1);
        repcli.save(clientes);

        return "Grabado";
    }
    @PutMapping("editarCliente/{idCliente}")
    public String editarCliente(@PathVariable long idCliente, @RequestBody erpclientes clientes){


        erpclientes erpclientes = repcli.findById(idCliente).get();
        erpclientes.setNombreCliente(clientes.getNombreCliente());
        erpclientes.setDireccionFisica(clientes.getDireccionFisica());
        erpclientes.setCorreoElectronico(clientes.getCorreoElectronico());
        erpclientes.setNombreFacturacion(clientes.getNombreFacturacion());
        erpclientes.setNit(clientes.getNit());
        erpclientes.setTelefono1(clientes.getTelefono1());
        erpclientes.setTelefono2(clientes.getTelefono2());
        erpclientes.setCreditoAutorizado(clientes.getCreditoAutorizado());
        erpclientes.setDeudaActual(clientes.getDeudaActual());
        erpclientes.setFechaModificacion(LocalDate.now());
        erpclientes.setHoraModificacion(LocalTime.now());
        erpclientes.setDocumentoIdentificacion(clientes.getDocumentoIdentificacion());
        erpclientes.setIdUsuarioModificacion(clientes.getIdUsuarioModificacion());
        repcli.save(erpclientes);

        return "Editado";
    }

    @DeleteMapping("eliminarCliente/{idCliente}")
    public String eliminarCliente(@PathVariable long idCliente){
        System.out.println("eliminar");
        erpclientes erpclientes = repcli.findById(idCliente).get();
        erpclientes.setEstado(0);
        repcli.save(erpclientes);
        return "Eliminado";
    }



}
