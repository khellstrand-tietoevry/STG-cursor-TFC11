package no.tieto.tfc11.k;

import no.tieto.tfc11.lext.FindCurrencyService;

/** Maps K envelope fields to F115L280 (RULE-015). */
final class KFindCurrencyAdapter implements KModuleValidator.FindCurrencyPort {

    private final FindCurrencyService findCurrencyService;

    KFindCurrencyAdapter(FindCurrencyService findCurrencyService) {
        this.findCurrencyService = findCurrencyService;
    }

    @Override
    public KModuleValidator.FindCurrencyOutcome find(KModuleValidator.KRequest request) {
        var envelope = request.envelope();
        var result = findCurrencyService.find(new FindCurrencyService.FindCurrencyRequest(
                envelope.function(),
                envelope.medium(),
                envelope.finInstNo(),
                envelope.accountNoForLookup(),
                true));
        if (!result.success()) {
            return KModuleValidator.FindCurrencyOutcome.error("AE", result.message());
        }
        return KModuleValidator.FindCurrencyOutcome.ok(result.currency());
    }
}
