import models.InsulinPump;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestInsulinPump extends InsulinPump {
    @Test
    void testDefaults() {
        // Initial pump state test
        assertTrue(InsulinPump.isPumpWorking());

        // Test pump state change
        InsulinPump.setPumpWorking(false);
        assertFalse(InsulinPump.isPumpWorking());
        InsulinPump.setPumpWorking(true);
        assertTrue(InsulinPump.isPumpWorking());

        // Test insulin delivery result
        assertTrue(InsulinPump.deliverInsulin(10));

        // Test pump state change and how delivery is affected
        InsulinPump.setPumpWorking(false);
        assertFalse(InsulinPump.isPumpWorking());
        assertFalse(InsulinPump.deliverInsulin(10));
    }
}
