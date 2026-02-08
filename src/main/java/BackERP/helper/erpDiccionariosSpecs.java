package BackERP.helper;

import BackERP.models.erpdiccionarios;
import org.springframework.data.jpa.domain.Specification;

public class erpDiccionariosSpecs {



    public static Specification<erpdiccionarios> diccionarioContains(String diccionario) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(diccionario, LikeHelper.MatchMode.ANYWHERE, false);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("diccionario")), pattern, '\\');
        };
    }


    public static Specification<erpdiccionarios> indiceContiene(String indice) {
        return (root, query, cb) -> {
            // Búsqueda flexible entre palabras: "aceite 10w40" -> "%aceite%10w40%"
            String pattern = LikeHelper.buildLikePattern(indice, LikeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("indice")), pattern, '\\');
        };
    }

    public static Specification<erpdiccionarios> valorContiene(String valor) {
        return (root, query, cb) -> {
            String pattern = LikeHelper.buildLikePattern(valor, LikeHelper.MatchMode.ANYWHERE, true);
            if (pattern == null) return cb.conjunction();
            return cb.like(cb.lower(root.get("valor")), pattern, '\\');
        };
    }

    public static Specification<erpdiccionarios> estadoEquals(int estado) {
        return (root, query, cb) ->
                cb.equal(root.get("estado").as(Integer.class), estado);

    }
}
