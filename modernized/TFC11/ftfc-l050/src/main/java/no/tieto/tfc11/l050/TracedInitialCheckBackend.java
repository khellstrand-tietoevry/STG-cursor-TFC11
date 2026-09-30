package no.tieto.tfc11.l050;

/**
 * C000 flow traced from {@code legacy/STG/TFC11/src/F115L050.src} (E000 disabled on main path per RULE-016).
 */
public final class TracedInitialCheckBackend implements InitialCheckService.F115L050BackendPort {

    private final InitialCheckDependencies dependencies;

    public TracedInitialCheckBackend(InitialCheckDependencies dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public BackendResult execute(InitialCheckCommand command) {
        String tableVersion = "ACTUAL";
        InitialCheckDependencies.MainContractSnapshot mainContract =
                dependencies.readMainContract(command, tableVersion);
        if (mainContract.inProgress()) {
            tableVersion = "WORK";
            mainContract = dependencies.readMainContract(command, tableVersion);
        }

        String wsStatus;
        if (command.paSeqNo() == 0) {
            wsStatus = mainContract.status();
        } else {
            wsStatus = dependencies.readPartAmountStatus(command, tableVersion);
        }

        if (!shouldSkipPropertyCheck(command)) {
            if (!dependencies.validateStatusOperationRelation(command, wsStatus)) {
                return BackendResult.error("F102", "TF-SY-INVALID-STATUS-OPERTYP");
            }
        }

        String property = dependencies.readStatusProperty(wsStatus);
        if (property == null) {
            property = "";
        }

        if (!shouldSkipPropertyCheck(command)) {
            if (property.contains("PERM")
                    && mainContract.inProgress()
                    && !"TFD05".equals(command.function())) {
                return BackendResult.error("G001", "TF-SY-MC-INPROG-Y");
            }
        }

        return BackendResult.ok(property);
    }

    /** RULE-012: Function(3:1) = 'R' and OPERATION-TYPE = SHOW skips G000 property gate. */
    static boolean shouldSkipPropertyCheck(InitialCheckCommand command) {
        String function = command.function();
        if (function == null || function.length() < 3) {
            return false;
        }
        char thirdChar = function.charAt(2);
        String operation = command.operationType() == null ? "" : command.operationType().trim();
        return thirdChar == 'R' && "SHOW".equalsIgnoreCase(operation);
    }
}
