package no.tieto.tfc11.stg;

/** Adapter for BPOXING0 (FTFCK110 E610-Geo-Code). Copybook: BPOXING0.copy */
public final class PongGeoCodeAdapter {

    public static final String MSG_DBOAAC_NF = "DB-OAAC-NF";

    private final PongGeoBackend backend;

    public PongGeoCodeAdapter(PongGeoBackend backend) {
        this.backend = backend;
    }

    public GeoLookupResult select(String pongGeoCode) {
        if (pongGeoCode == null || pongGeoCode.isBlank()) {
            return GeoLookupResult.invalid(MSG_DBOAAC_NF);
        }
        if (isNumericGeo(pongGeoCode)) {
            return backend.select("BPOXING0", pongGeoCode);
        }
        if (!"0".equals(pongGeoCode.trim())) {
            return GeoLookupResult.invalid(MSG_DBOAAC_NF);
        }
        return GeoLookupResult.ok();
    }

    private static boolean isNumericGeo(String value) {
        try {
            return Long.parseLong(value.trim()) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public interface PongGeoBackend {
        GeoLookupResult select(String program, String pongGeoCode);
    }

    public record GeoLookupResult(boolean success, String messageCode) {
        public static GeoLookupResult ok() {
            return new GeoLookupResult(true, "");
        }

        public static GeoLookupResult invalid(String messageCode) {
            return new GeoLookupResult(false, messageCode);
        }

        public static GeoLookupResult fromBackend(boolean stateOk) {
            return stateOk ? ok() : invalid("E611");
        }
    }
}
