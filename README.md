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

* GET /notifications

  Returns a paginated list of notifications with optional search.


* GET /persons

  Returns a paginated list of persons with optional filters.
  
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

