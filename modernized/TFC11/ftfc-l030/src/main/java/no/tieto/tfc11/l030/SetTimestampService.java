package no.tieto.tfc11.l030;

/** Port of F115L030 R115L030-Set-Timestamp (Phase 3 slice). */
public final class SetTimestampService {

    public TimestampResult apply(String requestedTimestamp) {
        if (requestedTimestamp == null || requestedTimestamp.isBlank()) {
            return TimestampResult.error("AE", "Missing timestamp for L030");
        }
        return TimestampResult.ok(requestedTimestamp.trim());
    }

    public record TimestampResult(boolean success, String value, String message) {
        public static TimestampResult ok(String value) {
            return new TimestampResult(true, value, "");
        }

        public static TimestampResult error(String code, String message) {
            return new TimestampResult(false, "", message);
        }
    }
}
