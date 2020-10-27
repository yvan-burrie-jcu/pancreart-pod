import jcu.cp3407.pancreart.model.InsulinReservoir;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestInsulinReservoir extends InsulinReservoir {
    @Test
    void testDefaults() {
        // Test that insulin is present
        assertTrue(InsulinReservoir.isInsulinPresent());

        // Test that insulin can be removed
        assertEquals(100, InsulinReservoir.getRemainingInsulin());
        InsulinReservoir.removeInsulin(5);
        assertEquals(95, InsulinReservoir.getRemainingInsulin());
        assertTrue(InsulinReservoir.isInsulinPresent());

        // Test that insulin can be replaced
        InsulinReservoir.replaceInsulin();
        assertTrue(InsulinReservoir.isInsulinPresent());
    }
}
