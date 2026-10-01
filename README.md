# TFC11 — Legacy COBOL (Create Customer)

> **Archived.** Maintenance continues in
> [tfi-translation-parity `STG/STG-cursor-TFC11`](https://github.com/tietoevryfs/tfi-translation-parity/tree/main/STG/STG-cursor-TFC11).
> See [`ARCHIVED.md`](ARCHIVED.md).

Standalone copy of the legacy COBOL source for mainframe **TFC11** (create
customer), extracted for modernization analysis with the Cursor
**code-modernization** plugin.

## Layout

```
legacy/STG/TFC11/
├── src/                 16 COBOL programs (.src) — H/K/L entry and one-hop helpers
├── copybooks/           28 copybooks (.copy) — record layouts and includes
└── corpus-manifest.json Call-graph and COPY metadata for the imported closure
analysis/                Plugin discovery output (assess, map, rules, brief, …)
modernized/              Transformed or uplifted code (empty until a build track runs)
```

This mirrors the `legacy/$system` convention expected by **code-modernization**
commands (`/modernize-preflight`, `/modernize-assess`, `/modernize-map`, …):
the system name is **`STG/TFC11`**, so its code lives at `legacy/STG/TFC11/`.

Nothing under `legacy/` should be edited — it is a frozen copy of the original
mainframe source, not a working copy.

## Workspace boundary

After the initial import, **all further work stays inside this repo.** Agents and
humans should not use other checkouts (parity testing, cob2jav, tfi-workspace
siblings, etc.) as inspiration or as a source of missing files. If something is
needed, copy it in explicitly and record it here. See `AGENTS.md`.

## Provenance

Copied from `cob2jav-tfc11-path-c/STG/TFC11/` (commit
`a6921b7b9793a1841a80c7af742d4e6cf0cd1cb0`) in the **tfi-workspace** container:

| Source | Contents |
|--------|----------|
| `min/src`, `min/copybook` | Canonical H/K/L entry: `FTFCH110`, `FTFCK110`, `FTFCL110` + `RTFCE110` |
| `onehop/src`, `onehop/copybook` | One-hop helper programs and their copybooks (byte-locked in `import-lock.json`) |

That folder is the **import-locked closure** used for the standalone TFC11
Path C Java translation lane. It is not the full mainframe inventory; transitive
COPY books referenced but not shipped in that closure are listed under
`corpus-manifest.json` → `unresolved_copybooks`.

The sibling repo `tfi-java-cobol-parity-testing` holds a wider executable lane
(mocks, DB adapters, and parity scenarios). That material is intentionally **not**
copied here; this repo is legacy COBOL only.

## Programs in this snapshot

**Entry (H/K/L):** `FTFCH110`, `FTFCK110`, `FTFCL110`

**One-hop helpers:** `F115ICA0`, `F115ICU0`, `F115IMC0`, `F115IPA0`, `F115IRP0`,
`F115ISC0`, `F115ISR0`, `F115ITP0`, `F115L030`, `F115L050`, `F115L140`,
`F115L240`, `F115L280`

## Suggested next commands

```
/modernize-preflight TFC11
/modernize-assess TFC11
/modernize-map TFC11
```

**System label:** `TFC11` · **Legacy:** `legacy/TFC11/` → `legacy/STG/TFC11/`
