# Phase 5 playbook — TFC11

**Modules:** `ftfc-f115r`, `ftfc-l-ext`, K wiring for **RULE-015**

| Java | COBOL | Role |
|------|-------|------|
| `SysCodeRelationService` | F115ISR0 | TF_Sys_Code_Rel select |
| `SysCodePropertyService` | F115ISC0 | Status property read |
| `PartAmountAccessService` | F115IPA0 | Part amount select |
| `RelatedPartyService` | F115IRP0 | Related party select |
| `TastPartService` | F115ITP0 | Tast part count (L prep) |
| `FindCurrencyService` | F115L280 | Account → currency (F203I010 port) |
| `ShadowContractService` | F115L140 | Shadow generate/accept slice |
| `SwiftAddressService` | F115L240 | SWIFT address extract slice |
| `KFindCurrencyAdapter` | FTFCK110 E510 | After initial check, when `accountNoForLookup > 0` |

External literal `CALL`s (F115IPAR, F728IG*, F116I020, …) are recorded on `ExternalProgramPort`, not executed.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Next:** Phase 6 — BPOXING0, F791I060, F7918030, F791TRAC adapters + final verify/harden.
