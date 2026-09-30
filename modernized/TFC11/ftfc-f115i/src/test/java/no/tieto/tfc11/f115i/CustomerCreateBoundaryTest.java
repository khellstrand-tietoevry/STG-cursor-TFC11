package no.tieto.tfc11.f115i;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerCreateBoundaryTest {

    private static ContractKey key() {
        return new ContractKey("123456789012345678", "01", 1000101);
    }

    @Test
    void happyPathUsesActualMainContractAndCustomer() {
        var boundary = boundary(false);
        var result = boundary.prepare(key(), "CUST-1", 0);
        assertTrue(result.success());
        assertEquals("OPEN", result.mainStatus());
        assertEquals("PERSON", result.customerType());
    }

    @Test
    void readsWorkTableWhenMainContractInProgress() {
        var boundary = boundary(true);
        var result = boundary.prepare(key(), "CUST-1", 0);
        assertTrue(result.success());
        assertEquals("WORK-OPEN", result.mainStatus());
    }

    @Test
    void readsContractAmountWhenPaSeqNoPositive() {
        var boundary = boundary(false);
        var result = boundary.prepare(key(), "CUST-1", 3);
        assertTrue(result.success());
        assertEquals("PART-OK", result.amountStatus());
    }

    @Test
    void recordsExternalImcVariantForAmountRead() {
        List<String> calls = new ArrayList<>();
        var external = (MainContractService.ExternalProgramPort) (program, copybook) -> calls.add(program);
        var mc = new MainContractService(
                (k, v) -> MainContractService.MainContractResult.ok("OPEN", false), external);
        var cu = new CustomerService(
                (k, id) -> CustomerService.CustomerResult.ok("PERSON"), external);
        var ca = new ContractAmountService(
                (k, pa) -> ContractAmountService.AmountResult.ok("PART-OK"), external);
        var boundary = new CustomerCreateBoundary(mc, cu, ca);
        boundary.prepare(key(), "CUST-1", 1);
        assertTrue(calls.contains("F115IMCR"));
    }

    private static CustomerCreateBoundary boundary(boolean inProgress) {
        var external = MainContractService.ExternalProgramPort.noop();
        var mc = new MainContractService(
                (k, v) -> {
                    if (v == MainContractService.TableVersion.WORK) {
                        return MainContractService.MainContractResult.ok("WORK-OPEN", false);
                    }
                    return MainContractService.MainContractResult.ok("OPEN", inProgress);
                },
                external);
        var cu = new CustomerService(
                (k, id) -> CustomerService.CustomerResult.ok("PERSON"), external);
        var ca = new ContractAmountService(
                (k, pa) -> ContractAmountService.AmountResult.ok("PART-OK"), external);
        return new CustomerCreateBoundary(mc, cu, ca);
    }

}
