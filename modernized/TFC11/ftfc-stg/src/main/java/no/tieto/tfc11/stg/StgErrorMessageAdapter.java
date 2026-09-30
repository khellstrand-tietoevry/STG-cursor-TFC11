package no.tieto.tfc11.stg;

/** Adapter for F7918030 (Z000-ErrorMessage in FTFCH110 and helpers). */
public final class StgErrorMessageAdapter {

    private final ErrorMessageBackend backend;

    public StgErrorMessageAdapter(ErrorMessageBackend backend) {
        this.backend = backend;
    }

    public ErrorMessageResult resolve(ErrorMessageRequest request) {
        return backend.resolve("F7918030", request);
    }

    public interface ErrorMessageBackend {
        ErrorMessageResult resolve(String program, ErrorMessageRequest request);
    }

    public record ErrorMessageRequest(
            String logonId,
            String terminalId,
            String messageCode,
            String messageText,
            boolean logYes) {}

    public record ErrorMessageResult(String outMessageCode, String outMessageText) {
        public static ErrorMessageResult passthrough(ErrorMessageRequest request) {
            return new ErrorMessageResult(request.messageCode(), request.messageText());
        }
    }
}
