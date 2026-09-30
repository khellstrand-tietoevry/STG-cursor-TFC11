# Cursor session export — TFC11

Portable closure record for the **code-modernization** plugin. Use this file to resume work in a new chat or share status without re-running discovery.

**Repository:** `STG-cursor-TFC11` (workspace root)  
**System label:** `TFC11` / `STG/TFC11`  
**Legacy path:** `legacy/TFC11/` → `legacy/STG/TFC11/`  
**Session date:** 2026-09-30 (UTC+2)  
**Track:** Transform → Java 21 / Spring Boot 3.x under `modernized/TFC11/`

## Orchestration policy

```yaml
max_parallel_subagents: 5
```

Every plugin step in this project should honor:

> Cap parallel Task subagents at 5 for this project.

## Final status

| Area | State |
|------|--------|
| Discovery (preflight → brief) | **Complete** |
| Transform (Phases 1–6) | **Complete** |
| Verify | **Complete** ([`VERIFICATION.md`](VERIFICATION.md)) |
| Harden | **Complete** ([`HARDENING.md`](HARDENING.md)) |
| Maven tests | **46**, **0** failures |
| P0 rules | **9/9 PROVEN** ([`equivalence/rule_coverage.json`](equivalence/rule_coverage.json)) |
| HTML report | [`REPORT.html`](REPORT.html) |

## Commands executed (in order)

1. `/modernize-preflight TFC11`
2. `/modernize-assess TFC11`
3. `/modernize-map TFC11`
4. `/modernize-extract-rules TFC11`
5. `/modernize-review TFC11`
6. `/modernize-brief TFC11` → approved
7. `/modernize-transform TFC11` — Phases 1–6 (multiple sessions)
8. `/modernize-verify TFC11` — interim (Phase 4) and final (Phase 6)
9. `/modernize-harden TFC11`
10. Session closure — `REPORT.html`, this export

Chronological detail: [`../../RUN_SEQUENCE.md`](../../RUN_SEQUENCE.md) (steps 0–15).

## Key artifacts (read first when resuming)

| Purpose | Path |
|---------|------|
| Intent / stack | [`INTENT.md`](INTENT.md) |
| Approved plan | [`MODERNIZATION_BRIEF.md`](MODERNIZATION_BRIEF.md) |
| Binding decisions | [`DECISIONS.md`](DECISIONS.md), [`CONFIRMED.md`](CONFIRMED.md) |
| Rules | [`BUSINESS_RULES.md`](BUSINESS_RULES.md), [`rules_result.json`](rules_result.json) |
| Topology | [`topology.json`](topology.json), [`call-graph.mmd`](call-graph.mmd) |
| Java reactor | [`../../modernized/TFC11/pom.xml`](../../modernized/TFC11/pom.xml) |
| Phase playbooks | [`../../modernized/TFC11/PHASE1_PLAYBOOK.md`](../../modernized/TFC11/PHASE1_PLAYBOOK.md) … [`PHASE6_PLAYBOOK.md`](../../modernized/TFC11/PHASE6_PLAYBOOK.md) |
| Equivalence | [`equivalence/cases.json`](equivalence/cases.json), [`equivalence/rule_coverage.json`](equivalence/rule_coverage.json) |
| Verify / harden | [`VERIFICATION.md`](VERIFICATION.md), [`HARDENING.md`](HARDENING.md) |
| Repeatable test gate | [`scripts/run-harden-verify.sh`](scripts/run-harden-verify.sh) |

## Transform map (H / K / L)

```
FTFCH110  →  ftfc-h110  (HModuleOrchestrator; always K then L — RULE-002)
FTFCK110  →  ftfc-k110  (KModuleValidator; L050, L280, E600 geo)
FTFCL110  →  ftfc-l110  (LModuleLoader; party / sys-code ports)
```

Notable Java choices documented in playbooks and [`DECISIONS.md`](DECISIONS.md):

- H **always** invokes L after K, even when K fails (RULE-002).
- F115L050 main path skips E000 update-timestamp check (RULE-016).
- External CALL/COPY gaps → **ports**, not sibling-repo runtime deps.

## Verify commands

```bash
cd modernized/TFC11 && mvn test
```

```bash
bash analysis/TFC11/scripts/run-harden-verify.sh
```

Latest green log: [`equivalence/mvn-logs/mvn-test-20260930-harden.log`](equivalence/mvn-logs/mvn-test-20260930-harden.log).

## Accepted corpus gaps (do not “fix” silently)

- **51** external programs called but no `.src` in `legacy/STG/TFC11/`
- **89** unresolved COPY references — see `legacy/STG/TFC11/corpus-manifest.json`
- **Dual COBOL/Java execution** not in this repo; parity is trace-based characterization

Import missing COBOL into `legacy/` and refresh manifest if a future change requires it — do not read external checkouts at runtime ([`AGENTS.md`](../../AGENTS.md)).

## Suggested next work (if any)

Production integration (outside this plugin brief) might include:

- Wire `modernized/TFC11` modules into a Spring Boot application boundary
- Import selected external `.src` for live STG adapter parity
- Second-pass rule mining in F115I* / L140 depth

For **continued plugin work** on this repo, typical entry points:

```text
/modernize-status TFC11
```

```text
/modernize-verify TFC11
```

after any code change under `modernized/`.

## Frozen boundaries

- **Do not edit** `legacy/**`
- Write analysis under `analysis/TFC11/`, Java under `modernized/`

---

*Export generated for plugin-style session closure · STG-cursor-TFC11 · TFC11 Create Customer.*
