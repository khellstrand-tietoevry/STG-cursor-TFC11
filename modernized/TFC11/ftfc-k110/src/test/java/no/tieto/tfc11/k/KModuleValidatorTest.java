package no.tieto.tfc11.k;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KModuleValidatorTest {

    @Test
    void rule003_rejectsMissingFinInstWhenIndicatorRequiresValue() {
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                ' ',
                "",
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                List.of(),
                base.function(),
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of());
        var result = new KModuleValidator().validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertTrue(result.message().contains(KModuleValidator.MSG_GL_MISSING_FIELD));
    }

    @Test
    void rule004_rejectsSelectedPartyWithoutType() {
        var row = new KModuleValidator.PartyRow('U', ' ', "Y", "");
        var envelope = withParties(List.of(row));
        var result = new KModuleValidator().validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertEquals(KModuleValidator.MSG_TYPE_MISSING, result.message());
    }

    @Test
    void rule006_propagatesInitialCheckError() {
        var validator = new KModuleValidator(
                req -> KModuleValidator.InitialCheckOutcome.error("AE", "FM"));
        var result = validator.validate(KModuleValidator.KRequest.forCreateDefaults());
        assertFalse(result.success());
        assertEquals("AE", result.statusCode());
        assertEquals("FM", result.message());
    }

    @Test
    void acceptsHappyPathWithTracedInitialCheck() {
        var result = new KModuleValidator().validate(KModuleValidator.KRequest.forCreateDefaults());
        assertTrue(result.success());
    }

    @Test
    void rule015_invokesFindCurrencyWhenAccountPresent() {
        var calls = new java.util.concurrent.atomic.AtomicInteger(0);
        var findCurrency = (KModuleValidator.FindCurrencyPort) req -> {
            calls.incrementAndGet();
            return KModuleValidator.FindCurrencyOutcome.ok("EUR");
        };
        var validator = new KModuleValidator(KModuleValidator.InitialCheckPort.noopOk(), findCurrency);
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                base.partyRows(),
                base.function(),
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                12345678901L,
                List.of());
        var result = validator.validate(new KModuleValidator.KRequest(envelope));
        assertTrue(result.success());
        assertEquals(1, calls.get());
    }

    @Test
    void rule007_rejectsBlankMediumInEnvelope() {
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                base.partyRows(),
                base.function(),
                " ",
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of());
        var result = new KModuleValidator().validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertEquals("C102", result.statusCode());
    }

    @Test
    void rule005_initialCheckRejectsBlankFunctionInEnvelope() {
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                base.partyRows(),
                " ",
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of());
        var result = new KModuleValidator().validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertEquals("C102", result.statusCode());
    }

    @Test
    void e600_rejectsSelectedPartyWithoutGeoOrCountry() {
        var item = new KModuleValidator.GeoCountryItem('U', 'U', "0", 'U', " ", "Y");
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                base.partyRows(),
                base.function(),
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of(item));
        var result = new KModuleValidator().validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertEquals(KGeographyValidator.MSG_NO_GEO_COUNTRY, result.message());
    }

    @Test
    void e620_propagatesF791i060Error() {
        var geography = new KGeographyValidator(
                new no.tieto.tfc11.stg.PongGeoCodeAdapter(
                        (p, c) -> no.tieto.tfc11.stg.PongGeoCodeAdapter.GeoLookupResult.ok()),
                new no.tieto.tfc11.stg.CountryCodeAdapter(
                        (p, iso) -> no.tieto.tfc11.stg.CountryCodeAdapter.CountryLookupResult.error("AE", "FM")));
        var validator = new KModuleValidator(
                KModuleValidator.InitialCheckPort.noopOk(),
                KModuleValidator.FindCurrencyPort.noopOk(),
                geography);
        var item = new KModuleValidator.GeoCountryItem('U', 'U', "0", 'U', "XX", "N");
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        var envelope = new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                base.partyRows(),
                base.function(),
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of(item));
        var result = validator.validate(new KModuleValidator.KRequest(envelope));
        assertFalse(result.success());
        assertEquals("FM", result.message());
    }

    private static KModuleValidator.FunctionEnvelope withParties(List<KModuleValidator.PartyRow> rows) {
        var base = KModuleValidator.FunctionEnvelope.happyCreateDefaults();
        return new KModuleValidator.FunctionEnvelope(
                base.finInstNoIndicator(),
                base.finInstNo(),
                base.contractTypeIndicator(),
                base.contractType(),
                base.contractNoIndicator(),
                base.contractNo(),
                base.lastSavedDateIndicator(),
                base.lastSavedDate(),
                base.operationTypeIndicator(),
                base.operationType(),
                rows,
                base.function(),
                base.medium(),
                base.paSeqNo(),
                base.lastUpdateTimestamp(),
                base.checkForUpdate(),
                0L,
                List.of());
    }
}
