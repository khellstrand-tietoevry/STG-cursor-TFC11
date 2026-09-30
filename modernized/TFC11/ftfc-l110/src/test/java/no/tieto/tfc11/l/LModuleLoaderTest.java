package no.tieto.tfc11.l;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LModuleLoaderTest {

    private static LModuleLoader happyLoader() {
        return new LModuleLoader(
                req -> LModuleLoader.SysCodeRelResult.ok(1),
                req -> LModuleLoader.PartyCheckResult.ok());
    }

    @Test
    void rule013_rejectsNonUniqueStatusMapping() {
        var loader = new LModuleLoader(
                req -> LModuleLoader.SysCodeRelResult.ok(2),
                req -> LModuleLoader.PartyCheckResult.ok());
        var result = loader.load(request("CREATE"));
        assertFalse(result.success());
        assertEquals(LModuleLoader.MSG_STATUS_NOT_UNIQUE, result.message());
    }

    @Test
    void rule014_rejectsMissingParty() {
        var loader = new LModuleLoader(
                req -> LModuleLoader.SysCodeRelResult.ok(1),
                req -> LModuleLoader.PartyCheckResult.fail(LModuleLoader.MSG_NO_PART));
        var result = loader.load(request("CREATE"));
        assertFalse(result.success());
        assertEquals(LModuleLoader.MSG_NO_PART, result.message());
    }

    @Test
    void loadSucceedsWhenPreparationsOk() {
        assertTrue(happyLoader().load(request("CREATE")).success());
    }

    @Test
    void rejectsMissingOperationType() {
        var result = happyLoader().load(request(" "));
        assertFalse(result.success());
        assertEquals("AE", result.statusCode());
    }

    private static LModuleLoader.LRequest request(String operationType) {
        return new LModuleLoader.LRequest("123456789012345678", operationType, List.of());
    }
}
