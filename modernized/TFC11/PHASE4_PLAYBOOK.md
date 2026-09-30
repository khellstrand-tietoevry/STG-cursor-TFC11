# Phase 4 playbook — TFC11

**Module:** `ftfc-f115i` — **F115IMC0**, **F115ICU0**, **F115ICA0** behind ports

| Java service | COBOL | Copybook / role |
|--------------|-------|-----------------|
| `MainContractService` | F115IMC0 | R115IMC0-Main-Contract (ACTUAL/WORK select) |
| `CustomerService` | F115ICU0 | R115ICU0-Customer |
| `ContractAmountService` | F115ICA0 | R115ICA0 (+ external F115IMCR variant noted) |
| `CustomerCreateBoundary` | L-module prep slice | Orchestrates the three in order |

External programs without `.src` in `legacy/` are recorded via `ExternalProgramPort`, not executed.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Next:** Phase 5 — remaining one-hop helpers (IPA0, IRP0, ISC0, ISR0, ITP0, L140, L240, L280).
