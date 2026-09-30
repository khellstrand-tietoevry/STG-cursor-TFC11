package no.tieto.tfc11.f115i;

/**
 * Coordinates F115IMC0 / F115ICU0 / F115ICA0 for L-module customer-create prep (Phase 4).
 * External literal CALL variants remain on {@link MainContractService.ExternalProgramPort}.
 */
public final class CustomerCreateBoundary {

    private final MainContractService mainContractService;
    private final CustomerService customerService;
    private final ContractAmountService contractAmountService;

    public CustomerCreateBoundary(
            MainContractService mainContractService,
            CustomerService customerService,
            ContractAmountService contractAmountService) {
        this.mainContractService = mainContractService;
        this.customerService = customerService;
        this.contractAmountService = contractAmountService;
    }

    public BoundaryResult prepare(ContractKey key, String customerId, int paSeqNo) {
        var mc = mainContractService.select(key, MainContractService.TableVersion.ACTUAL);
        if (!mc.success()) {
            return BoundaryResult.error(mc.message());
        }
        if (mc.inProgress()) {
            mc = mainContractService.select(key, MainContractService.TableVersion.WORK);
            if (!mc.success()) {
                return BoundaryResult.error(mc.message());
            }
        }
        var customer = customerService.readCustomer(key, customerId);
        if (!customer.success()) {
            return BoundaryResult.error(customer.message());
        }
        if (paSeqNo > 0) {
            var amount = contractAmountService.readContractAmount(key, paSeqNo);
            if (!amount.success()) {
                return BoundaryResult.error(amount.message());
            }
            return BoundaryResult.ok(mc.status(), customer.customerType(), amount.status());
        }
        return BoundaryResult.ok(mc.status(), customer.customerType(), mc.status());
    }

    public record BoundaryResult(
            boolean success, String mainStatus, String customerType, String amountStatus, String message) {

        public static BoundaryResult ok(String mainStatus, String customerType, String amountStatus) {
            return new BoundaryResult(true, mainStatus, customerType, amountStatus, "");
        }

        public static BoundaryResult error(String message) {
            return new BoundaryResult(false, "", "", "", message);
        }
    }
}
