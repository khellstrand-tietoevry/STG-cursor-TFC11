# Hardening — TFC11

**Command:** `/modernize-harden TFC11`  
**Date:** 2026-09-30  
**Policy:** Cap parallel Task subagents at **5**.

## Objective

Close **verification gaps** called out in [`VERIFICATION.md`](VERIFICATION.md) without expanding product scope: add regression tests, a repeatable verify script, and refresh equivalence metadata.

## Actions taken

| Item | Hardening |
|------|-----------|
| **RULE-007** (blank medium) | `InitialCheckServiceTest.rule007_rejectsBlankMedium`, `KModuleValidatorTest.rule007_rejectsBlankMediumInEnvelope` |
| **RULE-012** (READ+SHOW skip) | `TracedInitialCheckBackendTest.rule012_skipsPropertyCheckForReadShow` |
| **RULE-001** (decimal comma) | `LegacySourcePolicyTest.rule001_ftfch110DeclaresCommaDecimalPoint` — reads frozen `legacy/STG/TFC11/src/FTFCH110.src` |
| **RULE-017** (H not hand-edited) | `LegacySourcePolicyTest.rule017_ftfch110MustNotBeHandEditedBannerPresent` |
| Repeatable gate | [`scripts/run-harden-verify.sh`](scripts/run-harden-verify.sh) → `mvn test` in reactor |

## Verify

```bash
bash analysis/TFC11/scripts/run-harden-verify.sh
```

Or:

```bash
cd modernized/TFC11 && mvn test
```

Log (post-harden): [`equivalence/mvn-logs/mvn-test-20260930-harden.log`](equivalence/mvn-logs/mvn-test-20260930-harden.log)

## Out of scope (unchanged)

- Dual COBOL/Java execution (no COBOL toolchain in repo).
- Import of **51** external `.src` programs or bulk COPY resolution (**89** unresolved).
- Flyway / production deployment — transform track only.

## Post-harden P0 matrix

See updated [`equivalence/rule_coverage.json`](equivalence/rule_coverage.json): **9/9 P0 PROVEN**, **46** Maven tests, **0** failures.

**Note:** RULE-001 is **PROVEN** as *legacy policy preserved in frozen source*, not as Java numeric formatting behavior (Java stack uses standard types).

## Session closure

- [`REPORT.html`](REPORT.html) · [`CURSOR_SESSION_EXPORT.md`](CURSOR_SESSION_EXPORT.md)
