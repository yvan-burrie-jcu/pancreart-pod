import models.PowerSupply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPowerSupply extends PowerSupply {
    @Test
    void testDefaults() {
        // Test initial battery level
        assertEquals(Level.HIGH, PowerSupply.batteryLevel());

        // Test battery level change through all values
        PowerSupply.setBatteryLevel();
        assertEquals(Level.MEDIUM, PowerSupply.batteryLevel());
        PowerSupply.setBatteryLevel();
        assertEquals(Level.LOW, PowerSupply.batteryLevel());
        PowerSupply.setBatteryLevel();
        assertEquals(Level.HIGH, PowerSupply.batteryLevel());
    }
}
