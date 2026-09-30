package no.tieto.tfc11.f115r;

/** Port of F115IPA0 (TF_Part_Amount). */
public final class PartAmountAccessService {

    private final PartAmountPort port;
    private final ExternalProgramPort external;

    public PartAmountAccessService(PartAmountPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public PartAmountResult select(ContractContext contract, int paSeqNo, String tableVersion) {
        external.noteCall("F115IPA0", "R115IPA0-Part-Amount");
        if (paSeqNo <= 0) {
            return PartAmountResult.error("AE", "PA-Seq-No required for IPA0");
        }
        return port.select(contract, paSeqNo, tableVersion);
    }

    public interface PartAmountPort {
        PartAmountResult select(ContractContext contract, int paSeqNo, String tableVersion);
    }

    public record PartAmountResult(boolean success, String status, String message) {
        public static PartAmountResult ok(String status) {
            return new PartAmountResult(true, status, "");
        }

        public static PartAmountResult error(String code, String message) {
            return new PartAmountResult(false, "", message);
        }
    }
}
