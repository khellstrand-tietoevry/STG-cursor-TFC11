package no.tieto.tfc11.k;

import no.tieto.tfc11.l050.InitialCheckService;

public final class InitialCheckServiceAdapter implements KModuleValidator.InitialCheckPort {

    private final InitialCheckService service;

    public InitialCheckServiceAdapter(InitialCheckService service) {
        this.service = service;
    }

    @Override
    public KModuleValidator.InitialCheckOutcome run(KModuleValidator.KRequest request) {
        var result = service.run(KInitialCheckMapper.fromKRequest(request));
        if (result.success()) {
            return KModuleValidator.InitialCheckOutcome.ok();
        }
        return KModuleValidator.InitialCheckOutcome.error(result.statusCode(), result.message());
    }
}
