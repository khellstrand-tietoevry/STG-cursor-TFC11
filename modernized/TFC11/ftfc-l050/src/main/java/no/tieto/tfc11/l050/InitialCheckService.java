package no.tieto.tfc11.l050;

/** Port of F115L050 initial-check gate. Orchestrates C100 guards then backend C000 flow. */
public final class InitialCheckService {

    private final F115L050BackendPort backendPort;

    public InitialCheckService(F115L050BackendPort backendPort) {
        this.backendPort = backendPort;
    }

    public static InitialCheckService withTracedBackend(InitialCheckDependencies dependencies) {
        return new InitialCheckService(new TracedInitialCheckBackend(dependencies));
    }

    public InitialCheckResult run(InitialCheckCommand command) {
        if (command == null) {
            return InitialCheckResult.error("AE", "Missing initial-check command");
        }
        InitialCheckResult rule005 = validateFunctionAndMedium(command);
        if (rule005 != null) {
            return rule005;
        }
        if (command.financialInstitutionNo() == null || command.financialInstitutionNo().isBlank()) {
            return InitialCheckResult.error("AE", "Missing financial institution");
        }
        if (command.operationType() == null || command.operationType().isBlank()) {
            return InitialCheckResult.error("AE", "Missing operation type");
        }
        F115L050BackendPort.BackendResult backend = backendPort.execute(command);
        if (backend.error()) {
            return InitialCheckResult.error(backend.statusCode(), backend.message());
        }
        return InitialCheckResult.ok(backend.currentStatusProperty());
    }

    /** RULE-005: C100 — blank Function or Medium → paragraph C102 / TF-SY-FUNC-AND-MEDIUM. */
    static InitialCheckResult validateFunctionAndMedium(InitialCheckCommand command) {
        boolean functionBlank = command.function() == null || command.function().isBlank();
        boolean mediumBlank = command.medium() == null || command.medium().isBlank();
        if (functionBlank || mediumBlank) {
            return InitialCheckResult.error("C102", "TF-SY-FUNC-AND-MEDIUM");
        }
        return null;
    }

    public record InitialCheckResult(boolean error, String statusCode, String message, String currentStatusProperty) {
        public boolean success() {
            return !error;
        }

        public static InitialCheckResult ok(String currentStatusProperty) {
            return new InitialCheckResult(false, "OK", "", currentStatusProperty == null ? "" : currentStatusProperty);
        }

        public static InitialCheckResult error(String code, String message) {
            return new InitialCheckResult(true, code, message, "");
        }
    }

    public interface F115L050BackendPort {
        BackendResult execute(InitialCheckCommand command);

        record BackendResult(boolean error, String statusCode, String message, String currentStatusProperty) {
            public static BackendResult ok(String currentStatusProperty) {
                return new BackendResult(false, "OK", "", currentStatusProperty == null ? "" : currentStatusProperty);
            }

            public static BackendResult error(String code, String message) {
                return new BackendResult(true, code, message, "");
            }
        }
    }
}
