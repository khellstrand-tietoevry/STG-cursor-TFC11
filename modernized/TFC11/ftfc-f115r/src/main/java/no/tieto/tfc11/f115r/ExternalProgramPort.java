package no.tieto.tfc11.f115r;

public interface ExternalProgramPort {
    void noteCall(String program, String usingCopybook);

    static ExternalProgramPort noop() {
        return (program, copybook) -> {};
    }
}
