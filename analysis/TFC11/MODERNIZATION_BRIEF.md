# Modernization brief — TFC11

**Target:** Java 21 / Spring Boot 3.x  
**Track:** Transform (strangler-fig)  
**Status:** **Approved** (2026-09-30 — transform Phase 1 executed)  
**Legacy:** `legacy/TFC11/` → `legacy/STG/TFC11/`  
**System label:** `TFC11` (Create Customer / opplegg parter)

## Intent summary

Transform the Servo **H/K/L** chain **FTFCH110 → FTFCK110 → FTFCL110** and in-repo one-hop helpers to Java modules under `modernized/TFC11/`, preserving legacy behavior including quirks (see [`INTENT.md`](INTENT.md)). External CALL/COPY gaps documented in preflight/map are handled via **ports/adapters** per [`DECISIONS.md`](DECISIONS.md) unless sources are imported into `legacy/`.

## Phase plan

| Phase | Scope | Exit criteria |
|-------|--------|---------------|
| 1 | **FTFCK110** + **FTFCH110** (K + H skeleton) | `cd modernized/TFC11 && mvn test` green; **RULE-002** H→K→L order; **RULE-003/004** mandatory-field and type-missing tests on K |
| 2 | **FTFCL110** (L core path) | L invoked after K; **RULE-013/014** characterized where L paragraphs are ported |
| 3 | **F115L050** + **F115L030** | **RULE-005–009**, **010–012**, **016** — traced `ftfc-l050` backend deps (no E000 on main path); K **RULE-006** FM propagation |
| 4 | **F115ICU0**, **F115IMC0**, **F115ICA0** | Customer/main-contract helper modules; external F115* `CALL`s behind ports per DECISIONS |
| 5 | **F115IPA0**, **F115IRP0**, **F115ISC0**, **F115ISR0**, **F115ITP0**, **F115L140**, **F115L240**, **F115L280** | Remaining one-hop helpers; **RULE-015** (L280) wired from K |
| 6 | Boundaries + verify | **BPOXING0**, **F791I060**, **F7918030**, **F791TRAC** as adapters; run **`modernize-verify TFC11`**; optional **`modernize-harden TFC11`** |

**Note:** **F115L050** is already in `legacy/` (no TFC10-style mid-pipeline import). Re-map / re-extract-rules only if legacy expansion log adds programs or copybooks.

## Behavior contract

- All **P0** rules in [`BUSINESS_RULES.md`](BUSINESS_RULES.md) must be referenced by **executed tests** before any module is marked **PROVEN**.
- Review settlements in [`CONFIRMED.md`](CONFIRMED.md) and [`RULE_REVIEWS.json`](RULE_REVIEWS.json) are binding for transform.
- **`legacy/**` is frozen**; Java and analysis outputs only under `modernized/` and `analysis/TFC11/`.
- Cap parallel Task subagents at **5** unless you approve more in chat.

## Maven layout (target)

Reactor root: `modernized/TFC11/pom.xml` with modules such as `ftfc-h110`, `ftfc-k110`, `ftfc-l110`, `ftfc-l050`, … (names finalized in Phase 1 playbook).

## Risks accepted for this brief

| Risk | Mitigation |
|------|------------|
| 51 external CALL programs without `.src` | Ports/adapters; document in module `TRANSFORMATION_NOTES.md` |
| 89 unresolved COPY references | Ready-with-gaps; import copybooks only if a phase blocks P0 tests |
| No local COBOL execution | **PARTLY PROVEN** via trace-based tests + optional future `equivalence/cases.json` |

## Approval block

- [x] Full phased plan approved for transform (2026-09-30)
- [x] SME closed flagged items in [`DECISIONS.md`](DECISIONS.md) / [`RULE_REVIEWS.json`](RULE_REVIEWS.json) (2026-09-30 review)

## Next command (after you approve)

```text
/modernize-transform TFC11
Cap parallel Task subagents at 5 for this project.
```

Optional before transform: regenerate summary with `/modernize-status TFC11` or plugin `REPORT.html` when your Cursor bundle provides it.
