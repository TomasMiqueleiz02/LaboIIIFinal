package ar.edu.utn.frbb.tup.service;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

    public class BanelcoServiceTest {

        private final BanelcoService banelcoService = new BanelcoService();
        @Test
        public void testAcreditar() {
            // Test para asegurarse de que no se lanzan excepciones y el método se ejecuta sin errores
            double monto = 1000.0;
            long cuentaDestinoNumero = 12345678L;

            // Este test asegura que el método no falla, dado que no realiza acciones observables
            try {
                banelcoService.acreditar(monto, cuentaDestinoNumero);
            } catch (Exception e) {
                throw new AssertionError("El método acreditar lanzó una excepción inesperada.", e);
            }
        }
    }


