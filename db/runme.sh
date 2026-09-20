#!/usr/bin/env bash
set -u

CONTAINER_NAME="device-db"
IMAGE="postgres:17"
HOST_PORT=9876
DB_USER="device"
DB_PASSWORD="device"
DB_NAME="device"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_DIR="$SCRIPT_DIR/sql"

if docker ps -a --format '{{.Names}}' | grep -qx "$CONTAINER_NAME"; then
  echo "Drop container '$CONTAINER_NAME'..."
  docker rm -f "$CONTAINER_NAME" >/dev/null
fi

docker run \
  --name "$CONTAINER_NAME" \
  -e POSTGRES_USER="$DB_USER" \
  -e POSTGRES_PASSWORD="$DB_PASSWORD" \
  -e POSTGRES_DB="$DB_NAME" \
  -p "$HOST_PORT:5432" \
  -v "$SQL_DIR:/docker-entrypoint-initdb.d:ro" \
  "$IMAGE" >/dev/null