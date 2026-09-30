package no.tieto.tfc11.f115i;

/** Port of F115ICA0 contract/part amount access (R115ICA0). */
public final class ContractAmountService {

    private final ContractAmountPort port;
    private final MainContractService.ExternalProgramPort externalPrograms;

    public ContractAmountService(
            ContractAmountPort port, MainContractService.ExternalProgramPort externalPrograms) {
        this.port = port;
        this.externalPrograms = externalPrograms;
    }

    public AmountResult readContractAmount(ContractKey key, int paSeqNo) {
        externalPrograms.noteCall("F115ICA0", "R115ICA0");
        if (paSeqNo < 0) {
            return AmountResult.error("AE", "Invalid PA-Seq-No for ICA0");
        }
        externalPrograms.noteCall("F115IMCR", "external-variant");
        return port.readAmount(key, paSeqNo);
    }

    public interface ContractAmountPort {
        AmountResult readAmount(ContractKey key, int paSeqNo);
    }

    public record AmountResult(boolean success, String status, String message) {
        public static AmountResult ok(String status) {
            return new AmountResult(true, status, "");
        }

        public static AmountResult error(String code, String message) {
            return new AmountResult(false, "", message);
        }
    }
}
