package no.tieto.tfc11.k;

import no.tieto.tfc11.l050.InitialCheckCommand;

final class KInitialCheckMapper {

    private KInitialCheckMapper() {}

    static InitialCheckCommand fromKRequest(KModuleValidator.KRequest request) {
        var e = request.envelope();
        int contractNo = e.contractNo() == null ? 0 : e.contractNo();
        return new InitialCheckCommand(
                e.function(),
                e.medium(),
                e.finInstNo(),
                e.contractType(),
                contractNo,
                e.operationType(),
                e.lastUpdateTimestamp(),
                e.checkForUpdate(),
                e.paSeqNo());
    }
}
