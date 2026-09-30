package no.tieto.tfc11.k;

import java.util.List;

import no.tieto.tfc11.l050.InitialCheckDependencies;
import no.tieto.tfc11.l050.InitialCheckService;
import no.tieto.tfc11.lext.ExternalProgramPort;
import no.tieto.tfc11.lext.FindCurrencyService;
import no.tieto.tfc11.stg.CountryCodeAdapter;
import no.tieto.tfc11.stg.PongGeoCodeAdapter;

/**
 * Port of FTFCK110 control validation: C100 mandatory fields, party type check, F115L050 initial check (Phase 3).
 */
public final class KModuleValidator {

    public static final String MSG_GL_MISSING_FIELD = "GL-MISSING-FIELD&";
    public static final String MSG_TYPE_MISSING = "TF-SY-TYPE-MISSING";

    private final InitialCheckPort initialCheckPort;
    private final FindCurrencyPort findCurrencyPort;
    private final KGeographyValidator geographyValidator;

    public KModuleValidator() {
        this(
                new InitialCheckServiceAdapter(
                        InitialCheckService.withTracedBackend(InitialCheckDependencies.happyStub())),
                new KFindCurrencyAdapter(new FindCurrencyService(
                        acct -> FindCurrencyService.AccountSnapshot.found("NOK", "", true),
                        ExternalProgramPort.noop())),
                defaultGeographyValidator());
    }

    public KModuleValidator(InitialCheckPort initialCheckPort) {
        this(initialCheckPort, FindCurrencyPort.noopOk(), defaultGeographyValidator());
    }

    public KModuleValidator(InitialCheckPort initialCheckPort, FindCurrencyPort findCurrencyPort) {
        this(initialCheckPort, findCurrencyPort, defaultGeographyValidator());
    }

    public KModuleValidator(
            InitialCheckPort initialCheckPort,
            FindCurrencyPort findCurrencyPort,
            KGeographyValidator geographyValidator) {
        this.initialCheckPort = initialCheckPort;
        this.findCurrencyPort = findCurrencyPort;
        this.geographyValidator = geographyValidator;
    }

    private static KGeographyValidator defaultGeographyValidator() {
        return new KGeographyValidator(
                new PongGeoCodeAdapter((program, code) -> PongGeoCodeAdapter.GeoLookupResult.fromBackend(true)),
                new CountryCodeAdapter((program, iso) -> CountryCodeAdapter.CountryLookupResult.ok()));
    }

    public KResult validate(KRequest request) {
        if (request == null) {
            return KResult.error("AE", "Missing request");
        }
        var mandatory = validateMandatoryFields(request.envelope());
        if (mandatory.isPresent()) {
            return mandatory.get();
        }
        var typeMissing = validateSelectedPartyTypes(request.envelope());
        if (typeMissing.isPresent()) {
            return typeMissing.get();
        }
        var initial = initialCheckPort.run(request);
        if (initial.error()) {
            return KResult.error(initial.statusCode(), initial.message());
        }
        if (request.envelope().accountNoForLookup() > 0) {
            var currency = findCurrencyPort.find(request);
            if (currency.error()) {
                return KResult.error(currency.statusCode(), currency.message());
            }
        }
        var geography = geographyValidator.validate(request);
        if (geography.isPresent()) {
            return geography.get();
        }
        return KResult.ok();
    }

    static java.util.Optional<KResult> validateMandatoryFields(FunctionEnvelope envelope) {
        List<MandatoryRule> rules = List.of(
                new MandatoryRule(envelope.finInstNoIndicator(), envelope.finInstNo(), "Fin-Inst-No"),
                new MandatoryRule(envelope.contractTypeIndicator(), envelope.contractType(), "Kontrakt-Type"),
                new MandatoryRule(envelope.contractNoIndicator(), envelope.contractNo() == null ? "" : String.valueOf(envelope.contractNo()), "Kontrakt-Nr"),
                new MandatoryRule(envelope.lastSavedDateIndicator(), envelope.lastSavedDate(), "Sist-Lagret-Dato"),
                new MandatoryRule(envelope.operationTypeIndicator(), envelope.operationType(), "Operasjons-Art"));
        for (MandatoryRule rule : rules) {
            if (rule.indicator() != 'U' && isBlank(rule.value())) {
                return java.util.Optional.of(KResult.error("AE", MSG_GL_MISSING_FIELD + ":" + rule.label()));
            }
        }
        return java.util.Optional.empty();
    }

    static java.util.Optional<KResult> validateSelectedPartyTypes(FunctionEnvelope envelope) {
        for (PartyRow row : envelope.partyRows()) {
            if (row.selectedIndicator() == 'U'
                    && row.typeIndicator() != 'U'
                    && "Y".equals(row.selectedDetail())) {
                return java.util.Optional.of(KResult.error("AE", MSG_TYPE_MISSING));
            }
        }
        return java.util.Optional.empty();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record MandatoryRule(char indicator, String value, String label) {}

    public interface InitialCheckPort {
        InitialCheckOutcome run(KRequest request);

        static InitialCheckPort noopOk() {
            return request -> InitialCheckOutcome.ok();
        }
    }

    /** RULE-015: E510-Account-No → CALL F115L280. */
    public interface FindCurrencyPort {
        FindCurrencyOutcome find(KRequest request);

        static FindCurrencyPort noopOk() {
            return request -> FindCurrencyOutcome.ok("NOK");
        }
    }

    public record FindCurrencyOutcome(boolean error, String statusCode, String message, String currency) {
        public static FindCurrencyOutcome ok(String currency) {
            return new FindCurrencyOutcome(false, "OK", "", currency);
        }

        public static FindCurrencyOutcome error(String statusCode, String message) {
            return new FindCurrencyOutcome(true, statusCode, message, "");
        }
    }

    public record InitialCheckOutcome(boolean error, String statusCode, String message) {
        public static InitialCheckOutcome ok() {
            return new InitialCheckOutcome(false, "OK", "");
        }

        public static InitialCheckOutcome error(String statusCode, String message) {
            return new InitialCheckOutcome(true, statusCode, message);
        }
    }

    public record PartyRow(char selectedIndicator, char typeIndicator, String selectedDetail, String typeValue) {}

    public record GeoCountryItem(
            char typeIndicator,
            char geoCodeIndicator,
            String geoCodeDetail,
            char countryCodeIndicator,
            String countryCodeDetail,
            String selectedDetail) {}

    public record FunctionEnvelope(
            char finInstNoIndicator,
            String finInstNo,
            char contractTypeIndicator,
            String contractType,
            char contractNoIndicator,
            Integer contractNo,
            char lastSavedDateIndicator,
            String lastSavedDate,
            char operationTypeIndicator,
            String operationType,
            List<PartyRow> partyRows,
            String function,
            String medium,
            int paSeqNo,
            String lastUpdateTimestamp,
            boolean checkForUpdate,
            long accountNoForLookup,
            List<GeoCountryItem> geoCountryItems) {

        public static FunctionEnvelope happyCreateDefaults() {
            return new FunctionEnvelope(
                    'U',
                    "123456789012345678",
                    'U',
                    "01",
                    'U',
                    1000101,
                    'U',
                    "2024-06-15",
                    'U',
                    "CREATE",
                    List.of(),
                    "TFC11",
                    "01",
                    0,
                    "2024-06-15-12.00.00.000000",
                    true,
                    0L,
                    List.of());
        }
    }

    public record KRequest(FunctionEnvelope envelope) {
        public static KRequest forCreateDefaults() {
            return new KRequest(FunctionEnvelope.happyCreateDefaults());
        }
    }

    public record KResult(boolean success, String statusCode, String message) {
        static KResult ok() {
            return new KResult(true, "OK", "");
        }

        static KResult error(String code, String message) {
            return new KResult(false, code, message);
        }
    }
}
