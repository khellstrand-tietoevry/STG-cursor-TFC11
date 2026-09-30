package no.tieto.tfc11.l;

import java.util.List;

/**
 * Port of FTFCL110 preparation path (Phase 2): D100 sys-code relation and party checks.
 * External CALLs (F115ISR0, F115ITP0, F115ICU0, …) sit behind ports per DECISIONS.md.
 */
public final class LModuleLoader {

    public static final String MSG_STATUS_NOT_UNIQUE = "TF-SY-STATUS-NOT-UNIQUE";
    public static final String MSG_NO_PART = "TF-SY-NO-PART";

    private final SysCodeRelationPort sysCodeRelationPort;
    private final PartyPresencePort partyPresencePort;

    public LModuleLoader(SysCodeRelationPort sysCodeRelationPort, PartyPresencePort partyPresencePort) {
        this.sysCodeRelationPort = sysCodeRelationPort;
        this.partyPresencePort = partyPresencePort;
    }

    public LResult load(LRequest request) {
        if (request == null || isBlank(request.operationType())) {
            return LResult.error("AE", "Missing operation type for L preparations");
        }
        var rel = sysCodeRelationPort.readSysCodeRelation(request);
        if (rel.error()) {
            return LResult.error("AE", rel.message());
        }
        if (rel.totalRows() > 1) {
            return LResult.error("AE", MSG_STATUS_NOT_UNIQUE);
        }
        var party = partyPresencePort.verifyParties(request);
        if (party.error()) {
            return LResult.error("AE", party.message());
        }
        return LResult.ok();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record PartyRow(String selectedDetail, String operationTypeDetail, String typeValue) {}

    public record LRequest(
            String financialInstitutionNo,
            String operationType,
            List<PartyRow> partyRows) {}

    public record LResult(boolean success, String statusCode, String message) {
        public static LResult ok() {
            return new LResult(true, "OK", "");
        }

        public static LResult error(String code, String message) {
            return new LResult(false, code, message);
        }
    }

    public interface SysCodeRelationPort {
        SysCodeRelResult readSysCodeRelation(LRequest request);
    }

    public record SysCodeRelResult(boolean error, int totalRows, String message) {
        public static SysCodeRelResult ok(int totalRows) {
            return new SysCodeRelResult(false, totalRows, "");
        }

        public static SysCodeRelResult fail(String message) {
            return new SysCodeRelResult(true, 0, message);
        }
    }

    public interface PartyPresencePort {
        PartyCheckResult verifyParties(LRequest request);
    }

    public record PartyCheckResult(boolean error, String message) {
        public static PartyCheckResult ok() {
            return new PartyCheckResult(false, "");
        }

        public static PartyCheckResult fail(String message) {
            return new PartyCheckResult(true, message);
        }
    }
}
