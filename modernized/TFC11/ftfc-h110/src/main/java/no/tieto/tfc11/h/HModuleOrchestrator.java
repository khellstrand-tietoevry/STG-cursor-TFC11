package no.tieto.tfc11.h;

import no.tieto.tfc11.k.KModuleValidator;
import no.tieto.tfc11.stg.StgErrorMessageAdapter;
import no.tieto.tfc11.stg.StgTraceAdapter;

/**
 * Port of FTFCH110 A000 main path (Phase 1): always Perform K then L (RULE-002).
 * Final header status combines K and L outcomes; L is not skipped when K fails.
 */
public final class HModuleOrchestrator {

    private final KModuleValidator kModule;
    private final LModulePort lModule;
    private final StgTraceAdapter traceAdapter;
    private final StgErrorMessageAdapter errorMessageAdapter;

    public HModuleOrchestrator(KModuleValidator kModule, LModulePort lModule) {
        this(kModule, lModule, noopTrace(), passthroughErrors());
    }

    public HModuleOrchestrator(
            KModuleValidator kModule,
            LModulePort lModule,
            StgTraceAdapter traceAdapter,
            StgErrorMessageAdapter errorMessageAdapter) {
        this.kModule = kModule;
        this.lModule = lModule;
        this.traceAdapter = traceAdapter;
        this.errorMessageAdapter = errorMessageAdapter;
    }

    public HResult execute(HRequest request) {
        traceAdapter.trace(new StgTraceAdapter.TraceEvent("RTFCE110", "C001", request.functionCode()));
        var kResult = kModule.validate(request.kRequest());
        var lResult = lModule.load(request);
        if (!kResult.success()) {
            return HResult.fromModules(kResult, lResult, formatMessage(kResult.message()));
        }
        if (!lResult.success()) {
            return HResult.fromModules(kResult, lResult, formatMessage(lResult.message()));
        }
        traceAdapter.trace(new StgTraceAdapter.TraceEvent("R400CH01", "D001", "ok"));
        return HResult.ok(kResult, lResult);
    }

    private String formatMessage(String raw) {
        var resolved = errorMessageAdapter.resolve(new StgErrorMessageAdapter.ErrorMessageRequest(
                "STG", "TERM", "FM", raw, false));
        return resolved.outMessageText();
    }

    private static StgTraceAdapter noopTrace() {
        return new StgTraceAdapter((program, event) -> {});
    }

    private static StgErrorMessageAdapter passthroughErrors() {
        return new StgErrorMessageAdapter(
                (program, request) -> StgErrorMessageAdapter.ErrorMessageResult.passthrough(request));
    }

    private static String preferK(KModuleValidator.KResult k, LResult l) {
        if (!k.success()) {
            return k.message();
        }
        return l.message();
    }

    public interface LModulePort {
        LResult load(HRequest request);
    }

    public record HRequest(KModuleValidator.KRequest kRequest, String functionCode) {
        public static HRequest createDefaults() {
            return new HRequest(KModuleValidator.KRequest.forCreateDefaults(), "TFC11");
        }
    }

    public record LResult(boolean success, String statusCode, String message, boolean invoked) {
        static LResult ok() {
            return new LResult(true, "OK", "", true);
        }

        static LResult error(String code, String message) {
            return new LResult(false, code, message, true);
        }

        static LResult notInvoked() {
            return new LResult(true, "OK", "", false);
        }
    }

    public record HResult(
            boolean success,
            String statusCode,
            String message,
            boolean kInvoked,
            boolean lInvoked,
            KModuleValidator.KResult kResult,
            LResult lResult) {

        static HResult ok(KModuleValidator.KResult k, LResult l) {
            return new HResult(true, "OK", "", true, l.invoked(), k, l);
        }

        static HResult fromModules(KModuleValidator.KResult k, LResult l, String message) {
            boolean ok = k.success() && l.success();
            String code = !k.success() ? k.statusCode() : l.statusCode();
            return new HResult(ok, code, message, true, l.invoked(), k, l);
        }
    }
}
