# ftfc-stg — transformation notes

Programs **F791TRAC**, **F7918030**, **BPOXING0**, and **F791I060** have no executable `.src` under `legacy/STG/TFC11/`. Copybook **BPOXING0** and **R791I060** are in-tree; behavior is approximated via injectable backends.

Live mainframe parity requires importing program sources into `legacy/` and extending adapters — not sibling-repo runtime dependencies (see `DECISIONS.md`).
