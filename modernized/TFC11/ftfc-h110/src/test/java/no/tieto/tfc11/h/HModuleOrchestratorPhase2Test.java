package no.tieto.tfc11.h;

import no.tieto.tfc11.k.KModuleValidator;
import no.tieto.tfc11.l.LModuleLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HModuleOrchestratorPhase2Test {

    @Test
    void rule001_endToEndWithRealLAdapter() {
        var loader = new LModuleLoader(
                req -> LModuleLoader.SysCodeRelResult.ok(1),
                req -> LModuleLoader.PartyCheckResult.ok());
        var h = new HModuleOrchestrator(new KModuleValidator(), new LModuleAdapter(loader));
        var result = h.execute(HModuleOrchestrator.HRequest.createDefaults());
        assertTrue(result.success());
        assertTrue(result.lInvoked());
    }
}
