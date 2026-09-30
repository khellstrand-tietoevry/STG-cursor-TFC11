package no.tieto.tfc11.h;

import no.tieto.tfc11.l.LModuleLoader;

import java.util.List;

/** Bridges H orchestrator to FTFCL110 loader (Phase 2). */
public final class LModuleAdapter implements HModuleOrchestrator.LModulePort {

    private final LModuleLoader loader;

    public LModuleAdapter(LModuleLoader loader) {
        this.loader = loader;
    }

    @Override
    public HModuleOrchestrator.LResult load(HModuleOrchestrator.HRequest request) {
        var envelope = request.kRequest().envelope();
        List<LModuleLoader.PartyRow> parties = envelope.partyRows().stream()
                .map(row -> new LModuleLoader.PartyRow(row.selectedDetail(), envelope.operationType(), row.typeValue()))
                .toList();
        var lRequest = new LModuleLoader.LRequest(
                envelope.finInstNo(), envelope.operationType(), parties);
        var result = loader.load(lRequest);
        if (result.success()) {
            return HModuleOrchestrator.LResult.ok();
        }
        return HModuleOrchestrator.LResult.error(result.statusCode(), result.message());
    }
}
