package no.tieto.tfc11.f115i;

/** Port of F115ICU0 customer DB access (R115ICU0-Customer). */
public final class CustomerService {

    private final CustomerPort port;
    private final MainContractService.ExternalProgramPort externalPrograms;

    public CustomerService(CustomerPort port, MainContractService.ExternalProgramPort externalPrograms) {
        this.port = port;
        this.externalPrograms = externalPrograms;
    }

    public CustomerResult readCustomer(ContractKey key, String customerId) {
        externalPrograms.noteCall("F115ICU0", "R115ICU0-Customer");
        if (isBlank(customerId)) {
            return CustomerResult.error("AE", "Missing customer id for ICU0");
        }
        return port.read(key, customerId);
    }

    public interface CustomerPort {
        CustomerResult read(ContractKey key, String customerId);
    }

    public record CustomerResult(boolean success, String customerType, String message) {
        public static CustomerResult ok(String customerType) {
            return new CustomerResult(true, customerType, "");
        }

        public static CustomerResult error(String code, String message) {
            return new CustomerResult(false, "", message);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
