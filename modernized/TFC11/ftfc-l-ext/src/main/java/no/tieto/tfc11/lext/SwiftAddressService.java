package no.tieto.tfc11.lext;

/** Port slice of F115L240 SWIFT address extract (R115L240, F728IGA0 family). */
public final class SwiftAddressService {

    private final CustomerSwiftPort port;
    private final ExternalProgramPort external;

    public SwiftAddressService(CustomerSwiftPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public SwiftResult extract(SwiftRequest request) {
        external.noteCall("F115L240", "R115L240");
        if (request == null || isBlank(request.customerId())) {
            return SwiftResult.error("AE", "Customer id required for L240");
        }
        external.noteCall("F728IGA0", "R728IGA0");
        return port.readSwiftAddress(request);
    }

    public interface CustomerSwiftPort {
        SwiftResult readSwiftAddress(SwiftRequest request);
    }

    public record SwiftRequest(String finInstNo, String customerId, String roleType) {}

    public record SwiftResult(boolean success, String swiftAddress, String message) {
        public static SwiftResult ok(String swiftAddress) {
            return new SwiftResult(true, swiftAddress, "");
        }

        public static SwiftResult error(String code, String message) {
            return new SwiftResult(false, "", message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
