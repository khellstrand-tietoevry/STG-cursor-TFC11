# Phase 1 playbook — TFC11

**Modules:** `ftfc-k110` (FTFCK110), `ftfc-h110` (FTFCH110)

**Deferred to Phase 3:** Full **F115L050** trace (`ftfc-l050`); K uses `InitialCheckPort` stub defaulting to OK.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Rules covered in tests:** RULE-002, RULE-003, RULE-004, RULE-006 (port propagation).

**Next transform phase:** `ftfc-l110` (FTFCL110) — run `/modernize-transform TFC11` again or scoped follow-up per brief Phase 2.
