# Intent — TFC11 (Create Customer)

Recorded: 2026-09-30 (Cursor + code-modernization plugin)

## Track

**Transform** — rewrite module-by-module to **Java 21 / Spring Boot 3.x** while legacy keeps running conceptually (strangler-fig).

## Parity

**Match legacy behavior exactly, including known quirks.** Equivalence is proven with characterization tests and recorded comparisons where COBOL cannot run locally.

## Target stack

Java 21 (OpenJDK), Spring Boot 3.x, Maven multi-module under `modernized/TFC11/`.

## Legacy

- **Label:** `TFC11`
- **Path:** `legacy/TFC11/` → `legacy/STG/TFC11/` (directory in workspace)
- **Frozen:** no edits under `legacy/**`; analysis and Java under `analysis/TFC11/` and `modernized/`.

## Scope note

This tree is the **import-locked min + one-hop closure** around H/K/L (`FTFCH110`, `FTFCK110`, `FTFCL110`) and immediate helpers. **89** transitive COPY references are documented in `corpus-manifest.json` → `unresolved_copybooks`. External **CALL** targets without `.src` in-tree are boundaries until explicitly imported.

## Orchestration

```yaml
max_parallel_subagents: 5
```

Honor in every plugin step:

> Cap parallel Task subagents at 5 for this project.

## Entry chain

`FTFCH110` (H) → `FTFCK110` (K) / `FTFCL110` (L)
