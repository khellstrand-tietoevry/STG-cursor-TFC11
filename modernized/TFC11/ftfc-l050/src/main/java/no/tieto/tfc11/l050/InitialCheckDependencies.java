package no.tieto.tfc11.l050;

/** Out-of-tree CALL targets for F115L050 (F115IMC0, F115ISR0, F115ISC0, F115IPA0, …). */
public interface InitialCheckDependencies {

    MainContractSnapshot readMainContract(InitialCheckCommand command, String tableVersion);

    String readPartAmountStatus(InitialCheckCommand command, String tableVersion);

    boolean validateStatusOperationRelation(InitialCheckCommand command, String status);

    String readStatusProperty(String status);

    record MainContractSnapshot(String status, boolean inProgress) {}

    static InitialCheckDependencies happyStub() {
        return new InitialCheckDependencies() {
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
                return true;
            }

            @Override
            public String readStatusProperty(String status) {
                return "STATUS-PROP";
            }
        };
    }
}
