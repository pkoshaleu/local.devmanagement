# Device management service

## Goal

Develop a REST API capable of persisting and managing device resources.

Functionality:
- Create a new device;
- Fully and/or partially update an existing device;
- Fetch a single device;
- Fetch all devices;
- Fetch devices by brand;
- Fetch devices by state;
- Delete a single device.

## Building and Execution

### Prerequisites

- Java 21.x
- Maven 3.9.x
- Docker 20.10 for db dependencies
- `httpie` to run examples below

### Run it

Before execution of service, start and create database with `docker compose up` command or the following command:

```shell
./db/runme.sh
```

Run service with Maven:

```shell
mvn spring-boot:run
```

Alternatively, use the Jib plugin to build a Docker image:

```shell
mvn jib:dockerBuild
```

Building to the registry ie `jib:build` will push image to `docker.io`.

## Working with API

The service lives by default at 8080 port and exposes endpoints under `/api/devices`.

### GET /api/devices and GET /api/devices/{id}

Return list of devices or one specific.

```shell
http GET localhost:8080/api/devices/1
http GET localhost:8080/api/devices brand==JDR
```

List of devices end-point accepts three optional query parameters. Which will be combined with AND:
- `name` case-insensitive prefix match;
- `brand` exact match;
- `state` one of `AVAILABLE`, `IN_USE`, or `INACTIVE`.

### POST /api/devices

Create a new device:

```shell
http POST localhost:8080/api/devices name=PlanR brand=JDR
```

`name` and `brand` fields are required and must be between 1 and 255 characters long.

### PATCH /api/devices/{id}

Partially update a device:

```shell
http PATCH localhost:8080/api/devices/1 brand="C-B-G"
```

Send only fields you want to change, one of/all of `name` and `brand`.
Expect `404` if the device does not exist, or `409` if it is in `IN_USE` state.

### PUT /api/devices/{id}/state

Lend or return a device:

```shell
http PUT localhost:8080/api/devices/1/state state=IN_USE
```

`state` is required. See above for possible values.

Expect `404` if the device does not exist, or `409` if the
transition is not allowed by the rules.

### DELETE /api/devices/{id}

Delete specific device from management. Must not be in `IN_USE` state to success.

Expect `404` if the device does not exist, or `409` if the
deletion is not allowed by the rules.

### Errors

Errors follow RFC 7807. A typical error response:

```json
{
  "title": "Not Found",
  "status": 404,
  "detail": "Device with id:99 not found",
  "type": "about:blank"
}
```

## What to do next
- Proper user management. Mutation operation like update or delete should be guarded
  with at least role-based access control policy;
- Rate limiting access policies;
- Database schema management. At this moment it is a part of repository, but as
  it has own release cycle, it should enjoy own private space;
- Domain `state` is mixing device availability and landing status. Better to represent
  it with two separated statuses;
- Ledger with availability and landing statuses update history;
- Reasonable observations metrics beyond exposed by default.
- Humanize Open API generated documentation.

## Known issues
- Due to local environment constraints tests expected to see Docker 20.10 and Docker API 1.41