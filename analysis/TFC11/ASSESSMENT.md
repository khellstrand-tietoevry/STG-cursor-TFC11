# Assessment — TFC11

**Date:** 2026-09-30  
**Pattern:** **Transform** → Java 21 / Spring Boot  
**Estate size:** Medium-small (16 programs, 28 copybooks, ~21.2k lines in `src/`)

## Summary

TFC11 **Create Customer** (`TFC11/01 - Opplegg parter` in module headers) is a **Servo H/K/L** STG flow:

- **FTFCH110** (H, ~382 lines) — orchestrates K then L, trace/error plumbing (`F791TRAC`, `F7918030`).
- **FTFCK110** (K, ~2.2k lines) — validation and control; calls **F115L050** initial check and party/amount helpers.
- **FTFCL110** (L, ~2.5k lines) — load/persist customer and party data; heavy use of in-tree **F115ICU0**, **F115IMC0**, **F115IRP0**, **F115ITP0**, **F115L140**, etc.

Thirteen **one-hop helper** programs (F115I* and F115L*) are **in-repo** — unlike early TFC10, where many DB helpers started outside the tree. Helpers are large (roughly 1.1k–1.6k lines each) and encapsulate DB/business boundaries via `CALL` + linkage copybooks.

Envelope: **RTFCE110** (function I/O), **R400CH02** (STG header), shared error copybooks (`R7918030`, `R791TRAC`, `R7919999` references).

## Complexity drivers

| Driver | Detail |
|--------|--------|
| **Large K and L** | K ~2.2k / L ~2.5k lines — many branches, trace paragraphs, and conditional `CALL`s. |
| **Decimal comma** | `Decimal-Point Is Comma` on H/K/L — preserve numeric formatting in Java parity tests. |
| **Helper fan-out** | L-module alone calls ICU0, IMC0, IRP0, ITP0, L030, L140, ISR0 repeatedly. |
| **External boundaries** | **51** external programs without `.src` (per preflight); **191** external CALL edges in manifest. |
| **Incomplete COPY closure** | **40** missing copybook members (89 unresolved COPY sites) — rule mining may need ports or later legacy import. |
| **Initial check in-tree** | **F115L050** present (~563 lines) — K-gate can be traced without a mid-pipeline import (TFC10 needed Phase 5 import). |

No `EXEC SQL` in current `src/*.src` (DB access likely in called modules or embedded in helpers via further CALLs).

## Security / debt (high level)

- Implicit DB2/mainframe access through F115I* helpers and external CALL targets.
- Trace/error copybooks — avoid logging sensitive customer/party fields in Java adapters.
- **BPOXING0** / **F791I060** geography-style calls from K — treat as external boundary ports until sources are imported.

## Recommendation

Proceed with **transform** in phases aligned to the call graph (see [`ARCHITECTURE.mmd`](ARCHITECTURE.mmd)):

1. **H + K skeleton** (orchestration + K without full helper depth)  
2. **L** read/write path  
3. **F115L030 / F115L050** (timestamp + initial check — K gate)  
4. **F115ICA0 / F115ICU0 / F115IMC0** (customer/main-contract DB helpers)  
5. Remaining helpers (**IPA0, IRP0, ISC0, ISR0, ITP0, L140, L240, L280**)

Use **dependency ports** for the 51 missing external programs until brief approves legacy expansion. Expect **trace-based** verification (local COBOL compile incomplete per preflight).

## Next command

`modernize-map TFC11`
