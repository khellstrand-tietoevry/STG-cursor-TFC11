package no.tieto.tfc11.lext;

/**
 * Port of F115L280 find-currency flow (C000 read account via F203I010, external rate helpers on C100).
 */
public final class FindCurrencyService {

    public static final String MSG_ACCOUNT_ENDED = "TF-SY-ACCOUNT-ENDED";
    public static final String MSG_KKS_ACCOUNT = "TF-SY-KKS-ACCOUNT";

    private final AccountReadPort accountReadPort;
    private final ExternalProgramPort external;

    public FindCurrencyService(AccountReadPort accountReadPort, ExternalProgramPort external) {
        this.accountReadPort = accountReadPort;
        this.external = external;
    }

    public FindCurrencyResult find(FindCurrencyRequest request) {
        external.noteCall("F115L280", "R115L280-Find-Currency");
        if (request == null || request.accountNo() <= 0) {
            return FindCurrencyResult.error("AE", "Account number required");
        }
        external.noteCall("F203I010", "R203I010-Contract");
        var account = accountReadPort.readAccount(request.accountNo());
        if (account.notFound()) {
            return handleRkAccountFallback(request);
        }
        if (!account.active() && !request.function().startsWith("TFR")) {
            return FindCurrencyResult.error("AE", MSG_ACCOUNT_ENDED);
        }
        return FindCurrencyResult.ok(account.currency(), account.customerId());
    }

    private FindCurrencyResult handleRkAccountFallback(FindCurrencyRequest request) {
        external.noteCall("F782IKN0", "R782IKN0");
        if (!request.function().startsWith("TFR") && !request.rkAccountActive()) {
            return FindCurrencyResult.error("AE", MSG_ACCOUNT_ENDED);
        }
        return FindCurrencyResult.ok("NOK", "");
    }

    public interface AccountReadPort {
        AccountSnapshot readAccount(long accountNo);
    }

    public record AccountSnapshot(boolean notFound, boolean active, String currency, String customerId) {
        public static AccountSnapshot found(String currency, String customerId, boolean active) {
            return new AccountSnapshot(false, active, currency, customerId);
        }

        public static AccountSnapshot missing() {
            return new AccountSnapshot(true, false, "", "");
        }
    }

    public record FindCurrencyRequest(
            String function, String medium, String finInstNo, long accountNo, boolean rkAccountActive) {}

    public record FindCurrencyResult(boolean success, String currency, String customerId, String message) {
        public static FindCurrencyResult ok(String currency, String customerId) {
            return new FindCurrencyResult(true, currency, customerId, "");
        }

        public static FindCurrencyResult error(String code, String message) {
            return new FindCurrencyResult(false, "", "", message);
        }
    }
}
