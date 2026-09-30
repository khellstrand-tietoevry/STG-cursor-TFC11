package no.tieto.tfc11.h;

import no.tieto.tfc11.k.KModuleValidator;
import no.tieto.tfc11.stg.StgErrorMessageAdapter;
import no.tieto.tfc11.stg.StgTraceAdapter;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class HModuleOrchestratorTest {

    @Test
    void rule002_invokesLAfterKEvenWhenKFails() {
        AtomicBoolean lCalled = new AtomicBoolean(false);
        var k = new KModuleValidator(
                req -> KModuleValidator.InitialCheckOutcome.error("AE", "FM"));
        var h = new HModuleOrchestrator(k, req -> {
            lCalled.set(true);
            return HModuleOrchestrator.LResult.ok();
        });
        var result = h.execute(HModuleOrchestrator.HRequest.createDefaults());
        assertFalse(result.success());
        assertTrue(lCalled.get(), "L must run after K per FTFCH110");
        assertTrue(result.lInvoked());
    }

    @Test
    void rule002_happyPathWhenKAndLOk() {
        var h = new HModuleOrchestrator(new KModuleValidator(), req -> HModuleOrchestrator.LResult.ok());
        var result = h.execute(HModuleOrchestrator.HRequest.createDefaults());
        assertTrue(result.success());
        assertTrue(result.kInvoked());
        assertTrue(result.lInvoked());
    }

    @Test
    void hModuleTracesInputAndUsesF7918030OnFailure() {
        var traceAreas = new java.util.ArrayList<String>();
        var trace = new StgTraceAdapter((program, event) -> traceAreas.add(event.traceArea()));
        var errors = new StgErrorMessageAdapter((program, req) ->
                new StgErrorMessageAdapter.ErrorMessageResult(req.messageCode(), "STG:" + req.messageText()));
        var k = new KModuleValidator(
                req -> KModuleValidator.InitialCheckOutcome.error("AE", "FM"));
        var h = new HModuleOrchestrator(k, req -> HModuleOrchestrator.LResult.ok(), trace, errors);
        var result = h.execute(HModuleOrchestrator.HRequest.createDefaults());
        assertFalse(result.success());
        assertEquals("STG:FM", result.message());
        assertTrue(traceAreas.contains("RTFCE110"));
    }
}
