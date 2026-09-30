#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../../.." && pwd)"
cd "$ROOT/modernized/TFC11"
mvn -q test
echo "Harden verify: mvn test OK ($(date -u +%Y-%m-%dT%H:%M:%SZ))"
