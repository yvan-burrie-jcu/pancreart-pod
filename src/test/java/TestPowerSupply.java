import jcu.cp3407.pancreart.model.PowerSupply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPowerSupply extends PowerSupply {
    @Test
    void testDefaults() {
        // Test initial battery level
        assertEquals(100, PowerSupply.batteryLevel());

        // Test battery level change through all values
        int currentBatteryLevel = PowerSupply.batteryLevel();
        for (int i = 0; i < 20; i++) {
            currentBatteryLevel -= 5;
            PowerSupply.setBatteryLevel();
            assertEquals(currentBatteryLevel, PowerSupply.batteryLevel());
            System.out.println(currentBatteryLevel);
        }
    }
}
