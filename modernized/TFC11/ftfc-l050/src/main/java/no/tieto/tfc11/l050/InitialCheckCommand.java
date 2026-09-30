package no.tieto.tfc11.l050;

/** R115L050-Initial-Check key fields (FTFCK110 E100-Check-Status). */
public record InitialCheckCommand(
        String function,
        String medium,
        String financialInstitutionNo,
        String contractType,
        int contractNo,
        String operationType,
        String lastUpdateTimestamp,
        boolean checkForUpdate,
        int paSeqNo) {

    public InitialCheckCommand(
            String function,
            String medium,
            String financialInstitutionNo,
            String contractType,
            int contractNo,
            String operationType,
            String lastUpdateTimestamp,
            boolean checkForUpdate) {
        this(function, medium, financialInstitutionNo, contractType, contractNo, operationType, lastUpdateTimestamp, checkForUpdate, 0);
    }
}
