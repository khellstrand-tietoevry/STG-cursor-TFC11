package no.tieto.tfc11.lext;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LExtServicesTest {

    @Test
    void rule015_l280FindsCurrencyFromAccountRead() {
        var service = new FindCurrencyService(
                acct -> FindCurrencyService.AccountSnapshot.found("EUR", "CUST-9", true),
                ExternalProgramPort.noop());
        var result = service.find(new FindCurrencyService.FindCurrencyRequest(
                "TFC11", "01", "123456789012345678", 12345678901L, true));
        assertTrue(result.success());
        assertEquals("EUR", result.currency());
    }

    @Test
    void l280RkFallbackUsesNokWhenAccountMissing() {
        List<String> calls = new ArrayList<>();
        var external = (ExternalProgramPort) (p, c) -> calls.add(p);
        var service = new FindCurrencyService(
                acct -> FindCurrencyService.AccountSnapshot.missing(), external);
        var result = service.find(new FindCurrencyService.FindCurrencyRequest(
                "TFC11", "01", "123456789012345678", 999L, true));
        assertTrue(result.success());
        assertEquals("NOK", result.currency());
        assertTrue(calls.contains("F203I010"));
    }

    @Test
    void l280RejectsInactiveAccountForNonTfrFunction() {
        var service = new FindCurrencyService(
                acct -> FindCurrencyService.AccountSnapshot.found("NOK", "C", false),
                ExternalProgramPort.noop());
        var result = service.find(new FindCurrencyService.FindCurrencyRequest(
                "TFC11", "01", "123456789012345678", 1L, true));
        assertFalse(result.success());
        assertEquals(FindCurrencyService.MSG_ACCOUNT_ENDED, result.message());
    }

    @Test
    void l140ShadowApplyRecordsIpc0() {
        List<String> calls = new ArrayList<>();
        var external = (ExternalProgramPort) (p, c) -> calls.add(p);
        var service = new ShadowContractService(
                req -> ShadowContractService.ShadowResult.ok(), external);
        assertTrue(service.apply(new ShadowContractService.ShadowRequest(
                "123456789012345678", "01", 1000101, "GENERATE")).success());
        assertTrue(calls.contains("F115IPC0"));
    }

    @Test
    void l240ExtractsSwiftAddress() {
        var service = new SwiftAddressService(
                req -> SwiftAddressService.SwiftResult.ok("DEUTDEFF"), ExternalProgramPort.noop());
        var result = service.extract(new SwiftAddressService.SwiftRequest("123456789012345678", "CUST-1", "BEN"));
        assertEquals("DEUTDEFF", result.swiftAddress());
    }
}
