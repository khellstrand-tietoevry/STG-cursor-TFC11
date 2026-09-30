# Verification — TFC11 (full brief, Phases 1–6)

**Command:** `/modernize-verify TFC11`  
**Date:** 2026-09-30  
**Policy:** Cap parallel Task subagents at **5**.

## Verdict

| Gate | Result |
|------|--------|
| Maven reactor (`modernized/TFC11`) | **PASS** — **41** tests, **0** failures |
| [`MODERNIZATION_BRIEF.md`](MODERNIZATION_BRIEF.md) Phases 1–6 | **COMPLETE** (Java modules + playbooks) |
| P0 business rules ([`BUSINESS_RULES.md`](BUSINESS_RULES.md)) | **PASS WITH GAPS** — **7 PROVEN**, **2 PARTIAL**, **1 DEFERRED** |
| P1 **RULE-015** (F115L280) | **PROVEN** (K + `ftfc-l-ext`) |
| STG boundaries (BPOXING0, F791I060, F7918030, F791TRAC) | **PROVEN** at adapter level (`ftfc-stg` + H/K wiring) |
| Dual COBOL/Java execution | **NOT RUN** (no COBOL runtime in this repo) |

**Overall:** **ACCEPTED FOR MODERNIZATION BRIEF** — trace-based characterization covers H/K/L, in-repo one-hop helpers, and STG boundary adapters. Remaining gaps are documented and accepted per [`DECISIONS.md`](DECISIONS.md) / preflight.

**Post-harden (2026-09-30):** see [`HARDENING.md`](HARDENING.md) — **46** tests, **9/9 P0 PROVEN** (RULE-001 via legacy source anchor).

## Commands executed

```bash
cd modernized/TFC11 && mvn test
```

Log: [`equivalence/mvn-logs/mvn-test-20260930-verify-final.log`](equivalence/mvn-logs/mvn-test-20260930-verify-final.log)

## Reactor modules (11)

`ftfc-l050`, `ftfc-l030`, `ftfc-f115i`, `ftfc-f115r`, `ftfc-l-ext`, `ftfc-stg`, `ftfc-k110`, `ftfc-l110`, `ftfc-h110` (+ parent POM)

## Evidence artifacts

| Artifact | Purpose |
|----------|---------|
| [`equivalence/cases.json`](equivalence/cases.json) | Scenario inventory → JUnit + legacy anchors |
| [`equivalence/rule_coverage.json`](equivalence/rule_coverage.json) | Rule-level matrix (updated post Phase 6) |
| Phase playbooks | [`PHASE1_PLAYBOOK.md`](../../modernized/TFC11/PHASE1_PLAYBOOK.md) … [`PHASE6_PLAYBOOK.md`](../../modernized/TFC11/PHASE6_PLAYBOOK.md) |

## P0 rule summary

| Rule | Status | Notes |
|------|--------|-------|
| RULE-001 | DEFERRED | Decimal comma not asserted |
| RULE-002 | PROVEN | H always runs K then L |
| RULE-003 | PROVEN | GL-MISSING-FIELD |
| RULE-004 | PROVEN | TF-SY-TYPE-MISSING |
| RULE-005 | PROVEN | F115L050 gate |
| RULE-006 | PROVEN | Initial-check FM/AE propagation |
| RULE-007 | PARTIAL | Blank function → C102; blank medium untested |
| RULE-008 | PROVEN | F102 status/operation |
| RULE-009 | PROVEN | G001 permanent + in-progress MC |

## Known gaps (accepted)

- **51** external `CALL` programs without `.src` — backend ports only.
- **89** unresolved COPY references — no COBOL compile proof.
- **RULE-012** READ+SHOW skip — code present, no dedicated JUnit.
- **RULE-001** numeric comma policy — not characterized.
- STG adapters approximate mainframe programs; no imported `.src` for F791TRAC / F7918030 / BPOXING0 / F791I060.

## Next steps

1. Optional: **`/modernize-harden TFC11`** (hardening pass per plugin).
2. Import additional legacy `.src` / copybooks only if a gap blocks production parity.
3. Plugin closure: [`REPORT.html`](REPORT.html), [`CURSOR_SESSION_EXPORT.md`](CURSOR_SESSION_EXPORT.md).
