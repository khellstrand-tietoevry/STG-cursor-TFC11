package no.tieto.tfc11.stg;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StgBoundaryAdaptersTest {

    @Test
    void f791tracRecordsTraceEvent() {
        List<String> areas = new ArrayList<>();
        var adapter = new StgTraceAdapter((program, event) -> areas.add(event.traceArea()));
        adapter.trace(new StgTraceAdapter.TraceEvent("RTFCE110", "C001", "input"));
        assertEquals(List.of("RTFCE110"), areas);
    }

    @Test
    void f7918030ResolvesMessage() {
        var adapter = new StgErrorMessageAdapter((program, req) ->
                new StgErrorMessageAdapter.ErrorMessageResult(
                        req.messageCode(), "Resolved:" + req.messageText()));
        var result = adapter.resolve(new StgErrorMessageAdapter.ErrorMessageRequest(
                "user", "term", "FM", "raw", false));
        assertEquals("Resolved:raw", result.outMessageText());
    }

    @Test
    void bpoxing0RejectsNonNumericGeo() {
        var adapter = new PongGeoCodeAdapter((p, code) -> PongGeoCodeAdapter.GeoLookupResult.ok());
        var result = adapter.select("XX");
        assertFalse(result.success());
        assertEquals(PongGeoCodeAdapter.MSG_DBOAAC_NF, result.messageCode());
    }

    @Test
    void bpoxing0SelectsNumericGeo() {
        var adapter = new PongGeoCodeAdapter((p, code) -> PongGeoCodeAdapter.GeoLookupResult.fromBackend(true));
        assertTrue(adapter.select("47").success());
    }

    @Test
    void f791i060PropagatesBackendError() {
        var adapter = new CountryCodeAdapter((p, iso) ->
                CountryCodeAdapter.CountryLookupResult.error("AE", "FM"));
        var result = adapter.select("NO");
        assertFalse(result.success());
        assertEquals("FM", result.messageCode());
    }
}
