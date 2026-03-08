package BackERP.helper;

public class likeHelper {


        private likeHelper() {}

        /**
         * Escapa los caracteres especiales del patrón LIKE: \, %, _
         * @return cadena segura para usar en LIKE con ESCAPE '\'
         */
        public static String escapeForLike(String input) {
            if (input == null) return null;
            // Importante: primero escapar la barra invertida
            String s = input
                    .replace("\\", "\\\\")  // \  -> \\
                    .replace("%", "\\%")    // %  -> \%
                    .replace("_", "\\_");   // _  -> \_
            return s;
        }

        public enum MatchMode { ANYWHERE, START, END, EXACT }

        /**
         * Construye el patrón LIKE con el modo de coincidencia.
         * No modifica el contenido (mantiene símbolos), solo agrega % según el modo.
         * Aplica escape y toLowerCase.
         *
         * Si quieres búsquedas “entre palabras”, setea flexibleSpaces=true para convertir
         * espacios a % y permitir coincidencias intermedias.
         */
        public static String buildLikePattern(String raw, MatchMode mode, boolean flexibleSpaces) {
            if (raw == null) return null;
            String s = raw.toLowerCase().trim();
            if (s.isEmpty()) return null;

            if (flexibleSpaces) {
                // Colapsa espacios múltiples y los convierte en % para mayor flexibilidad
                s = s.replaceAll("\\s+", " ");
                s = s.replace(" ", "%");
            }

            s = escapeForLike(s);

            return switch (mode) {
                case EXACT    -> s;
                case START    -> s + "%";
                case END      -> "%" + s;
                case ANYWHERE -> "%" + s + "%";
            };
        }
    }


