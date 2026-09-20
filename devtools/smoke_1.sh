#!/usr/bin/env bash
set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"
DEVICES="${BASE_URL}/api/devices"

echo "== 1 =="
http -v GET "${DEVICES}"

http -v POST "${DEVICES}" name="Device 1" brand="Brand 1"

http -v POST "${DEVICES}" name="Device 2" brand="Brand 1"

echo "== 2 =="

http -v GET "${DEVICES}"

http -v GET "${DEVICES}/1"

echo "== 3 =="

http -v PATCH "${DEVICES}/2" brand="Brand 2"

http -v GET "${DEVICES}"

echo "== 4 =="

http -v GET "${DEVICES}/1000"
