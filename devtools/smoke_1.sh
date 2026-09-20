#!/usr/bin/env bash
set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"
DEVICES="${BASE_URL}/api/devices"

http GET "${DEVICES}"

http POST "${DEVICES}" name="Device 1" brand="Brand 1"

http POST "${DEVICES}" name="Device 2" brand="Brand 1"

http GET "${DEVICES}"

http GET "${DEVICES}/1"

http GET "${DEVICES}/1000"
