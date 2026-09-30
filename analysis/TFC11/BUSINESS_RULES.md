# Business Rules — TFC11

First extraction pass: **17** confirmed rules (**9** P0, **6** P1, **2** P2). Sources under `legacy/STG/TFC11/`. Machine-readable: [`rules_result.json`](rules_result.json).

| ID | Name | Category | Priority | Source | Confidence |
|---|---|---|---|---|---|
| RULE-001 | Decimal point is comma | Policy | P0 | `legacy/STG/TFC11/src/FTFCH110.src:20` | High |
| RULE-002 | H invokes K then L in sequence | Policy | P0 | `legacy/STG/TFC11/src/FTFCH110.src:101-102` | High |
| RULE-003 | K mandatory envelope fields | Validation | P0 | `legacy/STG/TFC11/src/FTFCK110.src:220-272` | High |
| RULE-004 | Selected party requires type | Validation | P0 | `legacy/STG/TFC11/src/FTFCK110.src:315-323` | High |
| RULE-005 | K-module initial check calls F115L050 | Validation | P0 | `legacy/STG/TFC11/src/FTFCK110.src:1001-1027` | High |
| RULE-006 | K propagates F115L050 errors | Validation | P0 | `legacy/STG/TFC11/src/FTFCK110.src:1029-1039` | High |
| RULE-007 | F115L050 requires function and medium | Validation | P0 | `legacy/STG/TFC11/src/F115L050.src:164-173` | High |
| RULE-008 | Invalid status and operation-type combination | Validation | P0 | `legacy/STG/TFC11/src/F115L050.src:335-349` | High |
| RULE-009 | Permanent status blocks in-progress main contract | Validation | P0 | `legacy/STG/TFC11/src/F115L050.src:420-430` | High |
| RULE-010 | F115L050 PA-Seq-No zero skips amount reads | Validation | P1 | `legacy/STG/TFC11/src/F115L050.src:106-112` | High |
| RULE-011 | F115L050 ACTUAL vs WORK main contract read | Lifecycle | P1 | `legacy/STG/TFC11/src/F115L050.src:95-103` | High |
| RULE-012 | F115L050 skips property check for READ+SHOW | Policy | P1 | `legacy/STG/TFC11/src/F115L050.src:137-142` | High |
| RULE-013 | L-module status not unique | Validation | P1 | `legacy/STG/TFC11/src/FTFCL110.src:253` | Medium |
| RULE-014 | L-module no party on contract | Validation | P1 | `legacy/STG/TFC11/src/FTFCL110.src:366` | Medium |
| RULE-015 | K calls find-currency F115L280 | Lifecycle | P1 | `legacy/STG/TFC11/src/FTFCK110.src:1855` | High |
| RULE-016 | F115L050 update-timestamp check disabled | Policy | P2 | `legacy/STG/TFC11/src/F115L050.src:119-123` | Medium |
| RULE-017 | H-module must not be hand-edited | Policy | P2 | `legacy/STG/TFC11/src/FTFCH110.src:10` | High |

## P0 highlights

### RULE-003 — K mandatory envelope fields

When RTFCE110 input indicators show a field is required (not `'U'`), missing **Fin-Inst-No**, **Contract-Type**, **Contract-No**, **Last-Saved-Date**, or **Operation-Type** yields **`GL-MISSING-FIELD&`** and **`Z700-Error-AE`**.

### RULE-004 — Selected party requires type

For each item row: if **Selected-I** is `'U'`, **Type-I** is not `'U'`, and **Selected-D** is `'Y'`, K raises **`TF-SY-TYPE-MISSING`**.

### RULE-005 / RULE-006 — Initial check gate

K fills **R115L050-Initial-Check** and **CALL**s **F115L050**. Abend/error from L050 maps to K **E101/E102** with message code **FM**.

### RULE-007–009 — F115L050 core validations

Same family as TFC10 initial check: **function/medium**, **status vs operation-type**, **permanent status + in-progress main contract** (with **TFD05** exemption on RULE-009).

## Gaps for a second pass

- Deep rules inside **F115ICA0/ICU0/IMC0/IRP0/ITP0** helpers (DB `CALL`s to external F115* variants).
- **BPOXING0** / **F791I060** geography rules on K-module.
- Rules blocked by **missing COPY** members (89 unresolved references) — import copybooks or accept port-only semantics.

## Next command

`modernize-review TFC11`
