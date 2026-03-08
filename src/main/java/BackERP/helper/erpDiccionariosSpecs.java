package BackERP.helper;

import BackERP.models.erpDiccionarios;
import org.springframework.data.jpa.domain.Specification;

public class erpDiccionariosSpecs {



    public static Specification<erpDiccionarios> diccionarioContains(String diccionario) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(diccionario, likeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("diccionario")), pattern, '\\');
        };
    }


    public static Specification<erpDiccionarios> indiceContiene(String indice) {
        return (root, query, cb) -> {
            // Búsqueda flexible entre palabras: "aceite 10w40" -> "%aceite%10w40%"
            String pattern = likeHelper.buildLikePattern(indice, likeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("indice")), pattern, '\\');
        };
    }

    public static Specification<erpDiccionarios> valorContiene(String valor) {
        return (root, query, cb) -> {
            String pattern = likeHelper.buildLikePattern(valor, likeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("valor")), pattern, '\\');
        };
    }

    public static Specification<erpDiccionarios> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
