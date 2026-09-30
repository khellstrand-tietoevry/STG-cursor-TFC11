package no.tieto.tfc11.h;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** RULE-001 / RULE-017: legacy source anchors (frozen COBOL in repo). */
class LegacySourcePolicyTest {

    private static final Path REPO_LEGACY_H =
            Path.of("../../../legacy/STG/TFC11/src/FTFCH110.src").normalize();

    @Test
    void rule001_ftfch110DeclaresCommaDecimalPoint() throws Exception {
        var text = java.nio.file.Files.readString(REPO_LEGACY_H);
        assertTrue(
                text.contains("Decimal-Point Is Comma"),
                "FTFCH110 must keep DECIMAL-POINT IS COMMA per legacy policy");
    }

    @Test
    void rule017_ftfch110MustNotBeHandEditedBannerPresent() throws Exception {
        var text = java.nio.file.Files.readString(REPO_LEGACY_H);
        assertTrue(
                text.contains("MUST NOT BE MODIFIED BY HUMAN HANDS"),
                "Generator/hand-edit banner must remain in legacy H source");
    }
}
