package no.tieto.tfc11.k;

import no.tieto.tfc11.stg.CountryCodeAdapter;
import no.tieto.tfc11.stg.PongGeoCodeAdapter;

/** E600-Check-Geo-Country-Code slice from FTFCK110. */
final class KGeographyValidator {

    static final String MSG_NO_GEO_COUNTRY = "TF-SY-NO-GEO-COUNTRY";

    private final PongGeoCodeAdapter pongGeoCodeAdapter;
    private final CountryCodeAdapter countryCodeAdapter;

    KGeographyValidator(PongGeoCodeAdapter pongGeoCodeAdapter, CountryCodeAdapter countryCodeAdapter) {
        this.pongGeoCodeAdapter = pongGeoCodeAdapter;
        this.countryCodeAdapter = countryCodeAdapter;
    }

    java.util.Optional<KModuleValidator.KResult> validate(KModuleValidator.KRequest request) {
        for (KModuleValidator.GeoCountryItem item : request.envelope().geoCountryItems()) {
            var geo = validateGeo(item);
            if (geo.isPresent()) {
                return geo;
            }
            var country = validateCountry(item);
            if (country.isPresent()) {
                return country;
            }
            var missing = validateSelectedRequiresGeoOrCountry(item);
            if (missing.isPresent()) {
                return missing;
            }
        }
        return java.util.Optional.empty();
    }

    private java.util.Optional<KModuleValidator.KResult> validateGeo(KModuleValidator.GeoCountryItem item) {
        if (item.typeIndicator() != 'U' || item.geoCodeIndicator() != 'U') {
            return java.util.Optional.empty();
        }
        String geo = item.geoCodeDetail() == null ? "" : item.geoCodeDetail().trim();
        if (geo.isEmpty() || "0".equals(geo)) {
            return java.util.Optional.empty();
        }
        var result = pongGeoCodeAdapter.select(geo);
        if (!result.success()) {
            return java.util.Optional.of(KModuleValidator.KResult.error("AE", result.messageCode()));
        }
        return java.util.Optional.empty();
    }

    private java.util.Optional<KModuleValidator.KResult> validateCountry(KModuleValidator.GeoCountryItem item) {
        if (item.typeIndicator() != 'U' || item.countryCodeIndicator() != 'U') {
            return java.util.Optional.empty();
        }
        String country = item.countryCodeDetail() == null ? "" : item.countryCodeDetail().trim();
        if (country.isEmpty()) {
            return java.util.Optional.empty();
        }
        var result = countryCodeAdapter.select(country);
        if (!result.success()) {
            return java.util.Optional.of(KModuleValidator.KResult.error(result.statusCode(), result.messageCode()));
        }
        return java.util.Optional.empty();
    }

    private java.util.Optional<KModuleValidator.KResult> validateSelectedRequiresGeoOrCountry(
            KModuleValidator.GeoCountryItem item) {
        if (item.typeIndicator() != 'U' || !"Y".equals(item.selectedDetail())) {
            return java.util.Optional.empty();
        }
        if (item.countryCodeIndicator() != 'U' || item.geoCodeIndicator() != 'U') {
            return java.util.Optional.empty();
        }
        boolean countryBlank = item.countryCodeDetail() == null || item.countryCodeDetail().isBlank();
        boolean geoZero = "0".equals(item.geoCodeDetail() == null ? "" : item.geoCodeDetail().trim());
        if (countryBlank && geoZero) {
            return java.util.Optional.of(KModuleValidator.KResult.error("AE", MSG_NO_GEO_COUNTRY));
        }
        return java.util.Optional.empty();
    }
}
