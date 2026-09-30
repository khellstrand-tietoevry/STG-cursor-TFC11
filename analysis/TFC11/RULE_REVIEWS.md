# Rule reviews — TFC11

Recorded via `/code-modernization:modernize-review TFC11` (flagged + medium-confidence scope). Answers merge into the brief behavior contract; `BUSINESS_RULES.md` is not edited.

| Rule | Verdict | When | Title | Note |
|------|---------|------|-------|------|
| RULE-002 | confirmed | 2026-09-30T11:02:00+02:00 | H invokes K then L in sequence | K then L always Perform'd; errors via R7919999 / S000. |
| RULE-004 | confirmed | 2026-09-30T11:02:00+02:00 | Selected party requires type | P0 TF-SY-TYPE-MISSING for selected rows. |
| RULE-005 | confirmed | 2026-09-30T11:02:00+02:00 | K-module initial check calls F115L050 | P0; trace in ftfc-l050 backend port. |
| RULE-006 | confirmed | 2026-09-30T11:02:00+02:00 | K propagates F115L050 errors | FM / E101-E102 mapping. |
| RULE-013 | confirmed | 2026-09-30T11:02:00+02:00 | L-module status not unique | Test when building ftfc-l110. |
| RULE-014 | confirmed | 2026-09-30T11:02:00+02:00 | L-module no party on contract | TF-SY-NO-PART characterization. |
| RULE-016 | confirmed | 2026-09-30T11:02:00+02:00 | F115L050 update-timestamp disabled | Do not enable E000 on main path in Java. |

**No open `discuss` verdicts** after this pass. Remaining P0 rules (001, 003, 007–009, etc.) were **High** confidence on first extract and need no SME gate for brief approval.
