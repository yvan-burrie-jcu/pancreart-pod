import jcu.cp3407.pancreart.model.PowerSupply;
import org.junit.jupiter.api.Test;

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
