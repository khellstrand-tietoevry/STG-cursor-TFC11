package no.tieto.tfc11.l050;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitialCheckServiceTest {

    private static InitialCheckCommand sampleCommand() {
        return new InitialCheckCommand(
                "TFC11", "01", "123456789012345678", "01", 1000101, "CREATE", "2024-06-15-12.00.00.000000", true, 0);
    }

    @Test
    void rule007_rejectsBlankMedium() {
        var service = InitialCheckService.withTracedBackend(InitialCheckDependencies.happyStub());
        var bad = new InitialCheckCommand("TFC11", " ", "123456789012345678", "01", 1, "CREATE", "ts", true, 0);
        var result = service.run(bad);
        assertTrue(result.error());
        assertEquals("C102", result.statusCode());
        assertEquals("TF-SY-FUNC-AND-MEDIUM", result.message());
    }

    @Test
    void rule005_rejectsBlankFunction() {
        var service = InitialCheckService.withTracedBackend(InitialCheckDependencies.happyStub());
        var bad = new InitialCheckCommand(" ", "01", "123456789012345678", "01", 1, "CREATE", "ts", true, 0);
        var result = service.run(bad);
        assertTrue(result.error());
        assertEquals("C102", result.statusCode());
        assertEquals("TF-SY-FUNC-AND-MEDIUM", result.message());
    }

    @Test
    void rule005_tracedBackendHappyPath() {
        var service = InitialCheckService.withTracedBackend(InitialCheckDependencies.happyStub());
        var result = service.run(sampleCommand());
        assertTrue(result.success());
        assertEquals("STATUS-PROP", result.currentStatusProperty());
    }
}
