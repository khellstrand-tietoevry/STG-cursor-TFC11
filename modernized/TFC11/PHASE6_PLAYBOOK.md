# Phase 6 playbook — TFC11 (STG boundaries)

**Module:** `ftfc-stg` — external infrastructure adapters (no `.src` in legacy tree)

| Adapter | Program | Wired from |
|---------|---------|------------|
| `StgTraceAdapter` | F791TRAC | `HModuleOrchestrator` (RTFCE110 / R400CH01 trace points) |
| `StgErrorMessageAdapter` | F7918030 | H error text on K/L failure |
| `PongGeoCodeAdapter` | BPOXING0 | `KGeographyValidator` (E610) |
| `CountryCodeAdapter` | F791I060 | `KGeographyValidator` (E620) |

K **`FunctionEnvelope.geoCountryItems`** carries E600 item rows; geography runs after initial check, currency, per FTFCK110 E000-Control-Base order slice.

**Verify:**

```bash
cd modernized/TFC11 && mvn test
```

**Next:** `/modernize-verify TFC11` (full brief scope) and optional `/modernize-harden TFC11`.
