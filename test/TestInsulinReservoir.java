import models.InsulinReservoir;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
