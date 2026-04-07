package BackERP.helper;


import BackERP.models.erpClientes;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.Arrays;


public class erpClientesSpecs {

  public static Specification<erpClientes> nombreClienteContains(String nombre) {
    return (root, query, cb) -> {
      if (nombre == null || nombre.trim().isEmpty()) {
        return cb.conjunction();
      }

      String[] palabras = nombre.toLowerCase().trim().split("\\s+");

      Predicate[] predicates = Arrays.stream(palabras)
        .map(p -> cb.like(
          cb.lower(root.get("nombreCliente")),
          "%" + likeHelper.escapeForLike(p) + "%",
          '\\'
        ))
        .toArray(Predicate[]::new);

      return cb.and(predicates);
    };
  }

  public static Specification<erpClientes> nitClienteContains(String nit) {
    return (root, query, cb) -> {
      if (nit == null || nit.trim().isEmpty()) {
        return cb.conjunction();
      }

      return cb.like(
        cb.lower(root.get("nit")),  nit.toLowerCase() + "%"
      );
    };
  }



  public static Specification<erpClientes> DPIPasaporteClienteContains(String documentoIdentificacion) {
    return (root, query, cb) -> {
      if (documentoIdentificacion == null || documentoIdentificacion.trim().isEmpty()) {
        return cb.conjunction(); // no aplica filtro si viene vacío
      }

      // Autocompletado: coincidencias que empiezan con lo ingresado
      return cb.like(
        cb.lower(root.get("documentoIdentificacion")),
        documentoIdentificacion.toLowerCase() + "%"
      );
    };
  }


  public static Specification<erpClientes> idCLienteContains(Integer id) {
        return (root, query, cb) ->{
            if (id == null) {
                return null; // <-- al devolver null, no se agrega restricción }
            }
            return cb.equal(root.get("idCLiente"), id);
        };
    }

    public static Specification<erpClientes> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }

}
