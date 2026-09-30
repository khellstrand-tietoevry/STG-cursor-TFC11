package no.tieto.tfc11.stg;

/** Adapter for external program F791TRAC (FTFCH110 X999-Control-Trace). */
public final class StgTraceAdapter {

    private final TraceBackend backend;

    public StgTraceAdapter(TraceBackend backend) {
        this.backend = backend;
    }

    public void trace(TraceEvent event) {
        backend.record("F791TRAC", event);
    }

    public interface TraceBackend {
        void record(String program, TraceEvent event);
    }

    public record TraceEvent(String traceArea, String tracePoint, String payloadSummary) {}
}
