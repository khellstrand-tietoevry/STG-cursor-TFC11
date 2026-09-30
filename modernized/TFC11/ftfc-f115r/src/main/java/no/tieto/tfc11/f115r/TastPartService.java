package no.tieto.tfc11.f115r;

/** Port of F115ITP0 (TF_Tast_Part) — used from FTFCL110. */
public final class TastPartService {

    private final TastPartPort port;
    private final ExternalProgramPort external;

    public TastPartService(TastPartPort port, ExternalProgramPort external) {
        this.port = port;
        this.external = external;
    }

    public TastPartResult countParties(ContractContext contract, String tableVersion) {
        external.noteCall("F115ITP0", "R115ITP0-Tast-Part");
        return port.countParties(contract, tableVersion);
    }

    public interface TastPartPort {
        TastPartResult countParties(ContractContext contract, String tableVersion);
    }

    public record TastPartResult(boolean success, int rowCount, String message) {
        public static TastPartResult ok(int rowCount) {
            return new TastPartResult(true, rowCount, "");
        }

        public static TastPartResult fail(String message) {
            return new TastPartResult(false, 0, message);
        }
    }
}
