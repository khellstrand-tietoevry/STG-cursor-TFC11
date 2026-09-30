package no.tieto.tfc11.lext;

/** Port slice of F115L140 shadow generate/accept (R115L140-Shadow). */
public final class ShadowContractService {

    private final ShadowActionPort port;
    private final ExternalProgramPort external;

    public ShadowContractService(ShadowActionPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public ShadowResult apply(ShadowRequest request) {
        external.noteCall("F115L140", "R115L140-Shadow");
        if (request == null || isBlank(request.action())) {
            return ShadowResult.error("AE", "Shadow action required");
        }
        external.noteCall("F115IPC0", "R115IPC0");
        return port.apply(request);
    }

    public interface ShadowActionPort {
        ShadowResult apply(ShadowRequest request);
    }

    public record ShadowRequest(String finInstNo, String contractType, int contractNo, String action) {}

    public record ShadowResult(boolean success, String message) {
        public static ShadowResult ok() {
            return new ShadowResult(true, "");
        }

        public static ShadowResult error(String code, String message) {
            return new ShadowResult(false, message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
