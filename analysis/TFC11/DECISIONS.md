# Decisions — TFC11

## F115L050 initial-check gate (RULE-005, RULE-006)

**Status:** Closed (review 2026-09-30). K-module **CALLs F115L050**; `F115L050.src` is in `legacy/STG/TFC11/src/`. **Decision:** P0 parity — `ftfc-l050` / backend dependencies must trace imported COBOL (not a permanent stub).

## F115L050 update-timestamp (RULE-016)

**Status:** Closed (review 2026-09-30). **Decision:** Java matches legacy **main path** — do **not** invoke E000 from C000 while Perform E000 remains commented in source. Re-enable only if legacy source changes in-repo.

## TFC11 party / type validation (RULE-004)

**Status:** Closed (review 2026-09-30). **Decision:** **TF-SY-TYPE-MISSING** is P0 for transform; cover selected-party rows in K-module tests before L-module depth.

## In-repo F115I* helpers vs external CALL variants

**Status:** Open for transform depth. **F115ICA0, F115ICU0, F115IMC0, F115IPA0, F115IRP0, F115ISC0, F115ISR0, F115ITP0** have `.src` in this repo. Helpers still **CALL** external programs (e.g. F115IPC0, F115ICAR, F115IMCR) without local `.src`. **Decision:** implement in-repo helper logic where sourced; use **ports/adapters** for external literal CALL targets until imported into `legacy/`.

## Missing COPY members (89 unresolved)

**Status:** Open for rule mining pass 2. **Decision:** Proceed with brief/transform using **Ready-with-gaps** posture; bulk-import copybooks into `legacy/STG/TFC11/copybooks/` only if assess/map blocks P0 rules (see preflight).

## Geography / infrastructure CALLs (BPOXING0, F791I060, F7918030, F791TRAC)

**Status:** Closed for transform Phase 6 (2026-09-30). **Decision:** **External boundaries** in `ftfc-stg` with injectable backends; H/K wired per playbook. Import `.src` into `legacy/` only via expansion log if live COBOL parity is required later.

## H then L orchestration (RULE-002)

**Status:** Closed (review 2026-09-30). **Decision:** Java H orchestrator **always** invokes K then L in order (match FTFCH110); do not short-circuit L in H when K sets warning/error — propagate via shared error area and header status handling.
