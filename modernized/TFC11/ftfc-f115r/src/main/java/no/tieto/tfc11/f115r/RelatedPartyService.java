package no.tieto.tfc11.f115r;

/** Port of F115IRP0 (TF_Related_Party). */
public final class RelatedPartyService {

    private final RelatedPartyPort port;
    private final ExternalProgramPort external;

    public RelatedPartyService(RelatedPartyPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public RelatedPartyResult select(ContractContext contract, String roleType) {
        external.noteCall("F115IRP0", "R115IRP0-Related-Party");
        if (isBlank(roleType)) {
            return RelatedPartyResult.error("AE", "Missing role type for IRP0");
        }
        return port.select(contract, roleType);
    }

    public interface RelatedPartyPort {
        RelatedPartyResult select(ContractContext contract, String roleType);
    }

    public record RelatedPartyResult(boolean success, int rowCount, String message) {
        public static RelatedPartyResult ok(int rowCount) {
            return new RelatedPartyResult(true, rowCount, "");
        }

        public static RelatedPartyResult error(String code, String message) {
            return new RelatedPartyResult(false, 0, message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
