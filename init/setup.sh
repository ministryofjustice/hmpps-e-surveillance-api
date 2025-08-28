#!/bin/bash

readonly SNS_TOPIC_FILE_UPLOAD="file-upload-topic"
readonly SNS_TOPIC_EVENTS="person-id-topic"

readonly PEOPLE_AND_EVENTS_BUCKET="people-and-events-bucket"
echo "Creating S3 bucket, SNS topic in LocalStack..."

awslocal s3 mb s3://$PEOPLE_AND_EVENTS_BUCKET
awslocal sns create-topic --name $SNS_TOPIC_FILE_UPLOAD
awslocal sns create-topic --name $SNS_TOPIC_EVENTS

TOPIC_ARN_FILE_UPLOAD=$(awslocal --endpoint-url=http://localhost:4566 sns list-topics --query 'Topics[0].TopicArn' --output text)
echo "TOPIC_ARN for S3: ${TOPIC_ARN_FILE_UPLOAD}"

TOPIC_ARN_EVENTS=$(awslocal --endpoint-url=http://localhost:4566 sns list-topics --query 'Topics[1].TopicArn' --output text)
echo "TOPIC_ARN for Events: ${TOPIC_ARN_EVENTS}"

echo "Resources created"

echo "Setting notification configuration"
awslocal s3api put-bucket-notification-configuration \
  --bucket $PEOPLE_AND_EVENTS_BUCKET \
  --notification-configuration '{
    "TopicConfigurations": [
      {
        "TopicArn": "'"$TOPIC_ARN_FILE_UPLOAD"'",
        "Events": ["s3:ObjectCreated:*"]
      }
    ]
  }'
  echo "AWS setup Done...."

echo "Subscribing to ${$TOPIC_ARN_S3}"
awslocal sns subscribe \
  --topic-arn  $TOPIC_ARN_S3 \
  --protocol http \
  --notification-endpoint http://host.docker.internal:8080/ingest/

echo "Subscribing to ${TOPIC_ARN_EVENTS}"
awslocal sns subscribe \
  --topic-arn  $TOPIC_ARN_EVENTS \
  --protocol http \
  --notification-endpoint http://host.docker.internal:8080/ingest/events