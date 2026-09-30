# Phase 2 playbook — TFC11

**Module:** `ftfc-l110` (FTFCL110)

**Wiring:** `LModuleAdapter` in `ftfc-h110` replaces Phase 1 stub `LModulePort` lambdas for production-style runs.

**Rules in tests:** RULE-013 (`TF-SY-STATUS-NOT-UNIQUE`), RULE-014 (`TF-SY-NO-PART`), end-to-end H+K+L happy path.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Next:** Phase 3 — `ftfc-l050` / F115L050 initial-check trace (`/modernize-transform TFC11`).
