package no.tieto.tfc11.stg;

/** Adapter for F791I060 (FTFCK110 E620-Country-Code). Copybook: R791I060.copy */
public final class CountryCodeAdapter {

    private final CountryBackend backend;

    public CountryCodeAdapter(CountryBackend backend) {
        this.backend = backend;
    }

    public CountryLookupResult select(String isoCodeX2) {
        if (isoCodeX2 == null || isoCodeX2.isBlank()) {
            return CountryLookupResult.ok();
        }
        return backend.select("F791I060", isoCodeX2.trim());
    }

    public interface CountryBackend {
        CountryLookupResult select(String program, String isoCodeX2);
    }

    public record CountryLookupResult(boolean success, String statusCode, String messageCode) {
        public static CountryLookupResult ok() {
            return new CountryLookupResult(true, "OK", "");
        }

        public static CountryLookupResult error(String statusCode, String messageCode) {
            return new CountryLookupResult(false, statusCode, messageCode);
        }
    }
}
