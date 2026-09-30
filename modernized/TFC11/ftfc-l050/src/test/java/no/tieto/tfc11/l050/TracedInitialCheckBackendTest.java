package no.tieto.tfc11.l050;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TracedInitialCheckBackendTest {

    @Test
    void rule008_rejectsInvalidStatusOperationRelation() {
        var deps = new InitialCheckDependencies() {
            @Override
            public MainContractSnapshot readMainContract(InitialCheckCommand command, String tableVersion) {
                return new MainContractSnapshot("OPEN", false);
            }

            @Override
            public String readPartAmountStatus(InitialCheckCommand command, String tableVersion) {
                return "OPEN";
            }

            @Override
            public boolean validateStatusOperationRelation(InitialCheckCommand command, String status) {
                return false;
            }

            @Override
            public String readStatusProperty(String status) {
                return "PROP";
            }
        };
        var result = new TracedInitialCheckBackend(deps).execute(
                new InitialCheckCommand("TFC11", "01", "123456789012345678", "01", 1, "CREATE", "ts", true, 0));
        assertTrue(result.error());
        assertEquals("F102", result.statusCode());
    }

    @Test
    void rule012_skipsPropertyCheckForReadShow() {
        var deps = new InitialCheckDependencies() {
            @Override
            public MainContractSnapshot readMainContract(InitialCheckCommand command, String tableVersion) {
                return new MainContractSnapshot("OPEN", false);
            }

            @Override
            public String readPartAmountStatus(InitialCheckCommand command, String tableVersion) {
                return "OPEN";
            }

            @Override
            public boolean validateStatusOperationRelation(InitialCheckCommand command, String status) {
                return false;
            }

            @Override
            public String readStatusProperty(String status) {
                return "PROP";
            }
        };
        var command = new InitialCheckCommand(
                "TFR01", "01", "123456789012345678", "01", 1, "SHOW", "ts", true, 0);
        var result = new TracedInitialCheckBackend(deps).execute(command);
        assertFalse(result.error());
    }

    @Test
    void rule009_blocksPermanentStatusWhenMainContractInProgress() {
        var deps = new InitialCheckDependencies() {
            @Override
            public MainContractSnapshot readMainContract(InitialCheckCommand command, String tableVersion) {
                return new MainContractSnapshot("OPEN", true);
            }

            @Override
            public String readPartAmountStatus(InitialCheckCommand command, String tableVersion) {
                return "OPEN";
            }

            @Override
            public boolean validateStatusOperationRelation(InitialCheckCommand command, String status) {
                return true;
            }

            @Override
            public String readStatusProperty(String status) {
                return "PERMANENT-PERM";
            }
        };
        var result = new TracedInitialCheckBackend(deps).execute(
                new InitialCheckCommand("TFC11", "01", "123456789012345678", "01", 1, "CREATE", "ts", true, 0));
        assertTrue(result.error());
        assertEquals("G001", result.statusCode());
    }
}
