# Phase 3 playbook — TFC11

**Modules:** `ftfc-l050` (F115L050), `ftfc-l030` (F115L030 timestamp slice), K wired via `InitialCheckServiceAdapter`

**Rules in tests:** RULE-005, RULE-008, RULE-009, RULE-010/012 via backend tests; K default constructor runs traced initial check.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Next:** Phase 4 — F115ICA0 / ICU0 / IMC0 helpers, or `/modernize-verify TFC11`.
