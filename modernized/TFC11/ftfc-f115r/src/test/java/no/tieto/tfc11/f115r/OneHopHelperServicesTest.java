package no.tieto.tfc11.f115r;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OneHopHelperServicesTest {

    private static ContractContext ctx() {
        return new ContractContext("123456789012345678", "01", 1000101);
    }

    @Test
    void isr0SelectRecordsExternalVariant() {
        List<String> calls = new ArrayList<>();
        var external = (ExternalProgramPort) (p, c) -> calls.add(p);
        var service = new SysCodeRelationService(
                q -> SysCodeRelationService.RelationResult.ok(1), external);
        var result = service.select(new SysCodeRelationService.RelationQuery(
                ctx(), "ST", "OPEN", "ST", "CREATE", "VALID-ST-OP"));
        assertTrue(result.success());
        assertEquals(1, result.rowCount());
        assertTrue(calls.contains("F115ISR0"));
    }

    @Test
    void isc0ReadsStatusProperty() {
        var service = new SysCodePropertyService(
                code -> SysCodePropertyService.PropertyResult.ok("PERM-OPEN"), ExternalProgramPort.noop());
        assertEquals("PERM-OPEN", service.readProperty("OPEN").property());
    }

    @Test
    void ipa0RequiresPositivePaSeqNo() {
        var service = new PartAmountAccessService(
                (c, pa, tv) -> PartAmountAccessService.PartAmountResult.ok("OPEN"),
                ExternalProgramPort.noop());
        assertFalse(service.select(ctx(), 0, "ACTUAL").success());
        assertTrue(service.select(ctx(), 2, "ACTUAL").success());
    }

    @Test
    void irp0SelectsRelatedParty() {
        var service = new RelatedPartyService(
                (c, role) -> RelatedPartyService.RelatedPartyResult.ok(1), ExternalProgramPort.noop());
        assertTrue(service.select(ctx(), "BEN").success());
    }

    @Test
    void itp0CountsTastParties() {
        var service = new TastPartService(
                (c, tv) -> TastPartService.TastPartResult.ok(3), ExternalProgramPort.noop());
        assertEquals(3, service.countParties(ctx(), "ACTUAL").rowCount());
    }
}
