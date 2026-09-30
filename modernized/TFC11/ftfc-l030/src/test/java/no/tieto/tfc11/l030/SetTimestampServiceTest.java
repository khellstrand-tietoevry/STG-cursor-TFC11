package no.tieto.tfc11.l030;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetTimestampServiceTest {

    @Test
    void acceptsNonBlankTimestamp() {
        var result = new SetTimestampService().apply("2024-06-15-12.00.00.000000");
        assertTrue(result.success());
        assertEquals("2024-06-15-12.00.00.000000", result.value());
    }

    @Test
    void rejectsBlankTimestamp() {
        assertFalse(new SetTimestampService().apply(" ").success());
    }
}
