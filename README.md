# hmpps-e-surveillance-api

[![repo standards badge](https://img.shields.io/badge/endpoint.svg?&style=flat&logo=github&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-e-surveillance-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-report/hmpps-e-surveillance-api "Link to report")
[![Docker Repository on ghcr](https://img.shields.io/badge/ghcr.io-repository-2496ED.svg?logo=docker)](https://ghcr.io/ministryofjustice/hmpps-e-surveillance-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-e-surveillance-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

### About HMPPS e-Surveillance API

A Spring Boot REST API for managing file uploads, triggering notifications, and querying person records. It integrates with Amazon S3 for presigned upload URLs and uses GOV.UK Notify for sending SMS and email alerts.

#### Technologies

* Spring Boot

* Spring Data JPA, Postgres

* Amazon AWS / localstack

* GOV.UK Notify

* Kotlin

#### GOV.UK Notify Integration

This application uses GOV.UK Notify to send SMS and email notifications for violation events. It uses two separate API keys for different purposes:

**Primary Notify Client (Live Key)**
- Used for: Manual notifications triggered via `/trigger-notification` endpoint
- Configuration: `NOTIFY_API_KEY` environment variable
- Behavior: Sends real SMS and emails to actual recipients
- Used by: `NotifyService` with `@Qualifier("primaryNotifyClient")`

**Ingestion Notify Client (Sandbox Key)**
- Used for: Automated notifications during event processing/file ingestion
- Configuration: `NOTIFY_API_KEY_INGESTION` environment variable
- Behavior: Dry-run mode using GOV.UK Notify sandbox key - does NOT send real notifications
- Used by: `EventsProcessorService` with `@Qualifier("ingestionNotifyClient")`
- Purpose: Test notification templates during event ingestion without sending to real users

Both clients require corresponding template IDs to be configured in `application.yml` for each violation type (tampering, curfew, exclusion zone, battery low).

#### API end points

* POST /trigger-notification

    Triggers a notification (SMS and Email) for a violation event.

    Request Body:

    ```json
    {
        "ppGivenName": "Officer",
        "ppFamilyName": "Smith",
        "givenName": "John",
        "familyName": "Doe",
        "violationType": "BATTERY_LOW",
        "phoneNumber": "07000000000",
        "email": "john.doe@example.com"
    }
    ```

* GET /notifications

  Returns a paginated list of notifications with optional search.


* GET /persons

  Returns a paginated list of persons with optional filters.

#### Testing the API

This API can be tested using the web dashboard instead of Postman:

**UI Dashboard:** [hmpps-e-surveillance-ui](https://github.com/ministryofjustice/hmpps-e-surveillance-ui)

The dashboard provides a user-friendly interface to:
- Upload person and event CSV files
- Trigger manual notifications
- View persons and notifications
- Monitor file processing status
  
## Running the application locally

The application comes with a `dev` spring profile that includes default settings for running locally. This is not
necessary when deploying to kubernetes as these values are included in the helm configuration templates -
e.g. `values-dev.yaml`.

There is also a `docker-compose.yml` that can be used to run a local instance of the template in docker and also an
instance of HMPPS Auth (required if your service calls out to other services using a token).

```bash
docker compose pull && docker compose up
```

will build the application and run it and HMPPS Auth within a local docker instance.

### Running the application in Intellij

```bash
docker compose pull && docker compose up --scale hmpps-e-surveillance-api=0
```

will just start a docker instance of HMPPS Auth, surveillance-db, and localstack. The application should then be started with a `dev` active profile
in Intellij.

On startup, it creates following resources in localstack:
* SNS topic with name `file-upload-topic`
* S3 bucket with name `people-and-events-bucket`
* SQS with queue names `fileuploadqueue` and `personidqueue`

### Environment Variables

The following environment variables are required:

**GOV.UK Notify Configuration:**
- `NOTIFY_API_KEY` - Live API key for sending real notifications via `/trigger-notification` endpoint
- `NOTIFY_API_KEY_INGESTION` - Sandbox API key for testing during event ingestion (dry-run mode)
- `NOTIFY_TEMPLATE_TAMPERING_SMS` - Template ID for tampering SMS
- `NOTIFY_TEMPLATE_TAMPERING_EMAIL` - Template ID for tampering email
- `NOTIFY_TEMPLATE_CURFEW_SMS` - Template ID for curfew violation SMS
- `NOTIFY_TEMPLATE_CURFEW_EMAIL` - Template ID for curfew violation email
- `NOTIFY_TEMPLATE_EXCLUSION_SMS` - Template ID for exclusion zone SMS
- `NOTIFY_TEMPLATE_EXCLUSION_EMAIL` - Template ID for exclusion zone email
- `NOTIFY_TEMPLATE_BATTERY_SMS` - Template ID for battery low SMS
- `NOTIFY_TEMPLATE_BATTERY_EMAIL` - Template ID for battery low email

For local development, set these in `application-local.yml` or as environment variables.

### Run tests
```bash
./gradlew test
```
