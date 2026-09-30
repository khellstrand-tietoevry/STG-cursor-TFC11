# Confirmed rules — TFC11

Rules settled during review (see [`RULE_REVIEWS.json`](RULE_REVIEWS.json)):

| Rule | Title |
|------|--------|
| RULE-002 | H invokes K then L in sequence — unconditional Perform; status via S000 |
| RULE-004 | Selected party requires type — TF-SY-TYPE-MISSING (P0) |
| RULE-005 | K-module initial check calls F115L050 — P0; backend port in transform |
| RULE-006 | K propagates F115L050 errors — FM / AE / abend mapping |
| RULE-013 | L-module status not unique — test with ftfc-l110 |
| RULE-014 | L-module no party — TF-SY-NO-PART |
| RULE-016 | Update-timestamp check disabled on F115L050 main path — match commented COBOL |

All other **P0** rules in [`BUSINESS_RULES.md`](BUSINESS_RULES.md) remain **High** confidence and need no SME gate for the brief.
