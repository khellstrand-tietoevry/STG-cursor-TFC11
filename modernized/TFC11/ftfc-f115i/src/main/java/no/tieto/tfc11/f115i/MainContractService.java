package no.tieto.tfc11.f115i;

/** Port of F115IMC0 main-contract DB access (select ACTUAL/WORK). */
public final class MainContractService {

    private final MainContractPort port;
    private final ExternalProgramPort externalPrograms;

    public MainContractService(MainContractPort port, ExternalProgramPort externalPrograms) {
        this.port = port;
        this.externalPrograms = externalPrograms;
    }

    public MainContractResult select(ContractKey key, TableVersion version) {
        externalPrograms.noteCall("F115IMC0", "R115IMC0-Main-Contract");
        if (key == null || isBlank(key.finInstId())) {
            return MainContractResult.error("AE", "Missing fin-inst for IMC0");
        }
        return port.select(key, version);
    }

    public enum TableVersion {
        ACTUAL,
        WORK
    }

    public interface MainContractPort {
        MainContractResult select(ContractKey key, TableVersion version);
    }

    public interface ExternalProgramPort {
        void noteCall(String program, String usingCopybook);

        static ExternalProgramPort noop() {
            return (program, copybook) -> {};
        }
    }

    public record MainContractResult(boolean success, String status, boolean inProgress, String message) {
        public static MainContractResult ok(String status, boolean inProgress) {
            return new MainContractResult(true, status, inProgress, "");
        }

        public static MainContractResult error(String code, String message) {
            return new MainContractResult(false, "", false, message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
