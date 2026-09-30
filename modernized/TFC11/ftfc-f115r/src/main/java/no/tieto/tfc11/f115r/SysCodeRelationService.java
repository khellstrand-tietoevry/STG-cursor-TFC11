package no.tieto.tfc11.f115r;

/** Port of F115ISR0 (TF_Sys_Code_Rel). */
public final class SysCodeRelationService {

    private final RelationPort port;
    private final ExternalProgramPort external;

    public SysCodeRelationService(RelationPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public RelationResult select(RelationQuery query) {
        external.noteCall("F115ISR0", "R115ISR0-Sys-Code-Rel");
        if (query == null || isBlank(query.relationType())) {
            return RelationResult.error("AE", "Missing relation type for ISR0");
        }
        return port.select(query);
    }

    public interface RelationPort {
        RelationResult select(RelationQuery query);
    }

    public record RelationQuery(
            ContractContext contract,
            String fromStatusType,
            String fromStatusCode,
            String toStatusType,
            String toStatusCode,
            String relationType) {}

    public record RelationResult(boolean success, int rowCount, String message) {
        public static RelationResult ok(int rowCount) {
            return new RelationResult(true, rowCount, "");
        }

        public static RelationResult error(String code, String message) {
            return new RelationResult(false, 0, message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
