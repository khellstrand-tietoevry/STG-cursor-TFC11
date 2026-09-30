package no.tieto.tfc11.f115r;

/** Port of F115ISC0 (status property on TF_Sys_Code). */
public final class SysCodePropertyService {

    private final PropertyPort port;
    private final ExternalProgramPort external;

    public SysCodePropertyService(PropertyPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public PropertyResult readProperty(String statusCode) {
        external.noteCall("F115ISC0", "R115ISC0-Sys-Code");
        if (isBlank(statusCode)) {
            return PropertyResult.error("AE", "Missing status for ISC0");
        }
        return port.readProperty(statusCode);
    }

    public interface PropertyPort {
        PropertyResult readProperty(String statusCode);
    }

    public record PropertyResult(boolean success, String property, String message) {
        public static PropertyResult ok(String property) {
            return new PropertyResult(true, property, "");
        }

        public static PropertyResult error(String code, String message) {
            return new PropertyResult(false, "", message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
