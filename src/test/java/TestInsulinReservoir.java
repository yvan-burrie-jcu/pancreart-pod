import jcu.cp3407.pancreart.model.InsulinReservoir;
import org.junit.jupiter.api.Test;

public class TestInsulinReservoir extends InsulinReservoir {
    @Test
    void testDefaults() {
        // Test that insulin is present
        assertTrue(InsulinReservoir.isInsulinPresent());

        // Test that insulin can be removed
        InsulinReservoir.removeInsulin();
        assertFalse(InsulinReservoir.isInsulinPresent());

        // Test that insulin can be replaced
        InsulinReservoir.replaceInsulin();
        assertTrue(InsulinReservoir.isInsulinPresent());
    }
}
