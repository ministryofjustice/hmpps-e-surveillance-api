#!/bin/bash

readonly SNS_TOPIC_S3_NAME="surv-topic-s3"
readonly SNS_TOPIC_EVENTS_NAME="surv-topic-events"

readonly S3_BUCKET_NAME="people-and-events-bucket"
echo "Creating S3 bucket, SNS topic in LocalStack..."

awslocal s3 mb s3://$S3_BUCKET_NAME
awslocal sns create-topic --name $SNS_TOPIC_S3_NAME
awslocal sns create-topic --name $SNS_TOPIC_EVENTS_NAME

TOPIC_ARN_S3=$(awslocal --endpoint-url=http://localhost:4566 sns list-topics --query 'Topics[0].TopicArn' --output text)
echo "TOPIC_ARN for S3: ${TOPIC_ARN_S3}"

TOPIC_ARN_EVENTS=$(awslocal --endpoint-url=http://localhost:4566 sns list-topics --query 'Topics[1].TopicArn' --output text)
echo "TOPIC_ARN for Events: ${TOPIC_ARN_EVENTS}"

echo "Resources created"

echo "Setting notification configuration"
awslocal s3api put-bucket-notification-configuration \
  --bucket $S3_BUCKET_NAME \
  --notification-configuration '{
    "TopicConfigurations": [
      {
        "TopicArn": "'"$TOPIC_ARN_S3"'",
        "Events": ["s3:ObjectCreated:*"]
      }
    ]
  }'
  echo "AWS setup Done...."
