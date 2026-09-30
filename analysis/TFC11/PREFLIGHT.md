# Preflight — TFC11

**Date:** 2026-09-30  
**Target stack:** Java 21 Spring Boot  
**Legacy path:** `legacy/TFC11/` → `legacy/STG/TFC11/` (directory in workspace)

## Answers (human preflight — headless defaults)

### 1. Scope

**Answer (working default for this repo):** Import-locked **min + one-hop** slice of TFC11 (Create Customer H/K/L + helpers). Not the full mainframe estate. **51** external literal `CALL` targets have no `.src` in-tree; **40** distinct COPY members referenced but not shipped (89 reference sites in `corpus-manifest.json`).

### 2. Build & test locally

**Answer:** COBOL does not compile fully locally (EXEC SQL / dialect); Java target stack can run `mvn test` once `modernized/TFC11/` exists. Java 21 + Maven 3.9 verified on host.

### 3. Bespoke build infrastructure

**Answer:** None in this repo; legacy assumes mainframe DB2/CICS runtime.

### 4. Prior attempts

**Answer:** Parity/modernization work exists elsewhere; **this repo is self-contained** — do not depend on external checkouts during runs.

### 5. Off limits

**Answer:** Everything under `legacy/**` is frozen. Generated analysis and `modernized/` only.

## Check 6 — Scope boundary

Standalone git repo. **Outbound:** external CALL and COPY dependencies documented in `corpus-manifest.json`. **F115L050** and F115I* helper **sources are in-tree** (contrast with early TFC10 preflight). **Inbound:** none identified in-repo.

## Summary table

| Check | Status | Finding |
|-------|--------|---------|
| 0 Answers | ⚠️ | Defaults recorded; confirm with SME if needed |
| 1 Stack | ✅ | 16 `.src`, 28 `.copy`, ~23.6k lines; fixed-format COBOL |
| 2 Analysis tools | ✅ | python3 3.14; use wc/find (no scc/cloc required) |
| 3 Build toolchain | ⚠️ | GnuCOBOL 3.2: H/K/L syntax-only fails on EXEC SQL/dialect; expect trace-based Java proof |
| 4 Source completeness | ⚠️ | 16 programs in-tree; **51** external CALL programs missing; **40** missing COPY members |
| 5 Optional context | ⚠️ | Git history minimal; no APM |
| 6 Scope | ⚠️ | min+onehop slice; COPY closure incomplete |
| 7 Legacy protection | ✅ | `.cursor/rules` + `AGENTS.md` boundary |

## Verdict

| Command | Verdict |
|---------|---------|
| assess, map, extract-rules | **Ready-with-gaps** (missing CALL/COPY documented) |
| brief | **Ready** after discovery artifacts |
| transform | **Ready-with-gaps** — use ports/adapters for external CALL/COPY gaps per brief |
| verify | **Ready-with-gaps** (dual COBOL run unlikely locally) |
| harden | **Ready-with-gaps** |

## Legacy expansion log (approved imports)

| Date | Artifact | Note |
|------|----------|------|
| 2026-09-30 | Initial import | 16 programs + 28 copybooks from `cob2jav-tfc11-path-c` (see root `README.md`) |

**Next:** `modernize-assess TFC11`
