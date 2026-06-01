# Spring HTTP NPE Demo

Plain HTTP Spring Boot app that intentionally throws a NullPointerException at `/trigger-npe`.

## Local run

```bash
mvn clean package
java -jar target/spring-http-npe-demo-1.0.0.jar
```

Test:

```bash
curl http://localhost:8080/health
curl http://localhost:8080/trigger-npe
```

Expected log:

```text
ERROR DevOpsAgentTestFailure: simulated NullPointerException in plain HTTP Spring Boot application
java.lang.NullPointerException
```

## Docker run

```bash
mvn clean package
docker build -t spring-http-npe-demo .
docker run --rm -p 8080:8080 spring-http-npe-demo
```

## Deploy to EC2 using CloudFormation

Build and upload JAR:

```bash
mvn clean package
aws s3 cp target/spring-http-npe-demo-1.0.0.jar s3://YOUR_BUCKET_NAME/spring-http-npe-demo-1.0.0.jar --region eu-central-1
```

Deploy:

```bash
aws cloudformation deploy \
  --stack-name spring-http-npe-demo \
  --template-file cloudformation-ec2-http.yaml \
  --capabilities CAPABILITY_NAMED_IAM \
  --parameter-overrides \
    ProjectName=spring-http-npe-demo \
    JarS3Bucket=YOUR_BUCKET_NAME \
    JarS3Key=spring-http-npe-demo-1.0.0.jar \
    AllowedHttpCidr=0.0.0.0/0 \
  --region eu-central-1
```

Get URL:

```bash
aws cloudformation describe-stacks \
  --stack-name spring-http-npe-demo \
  --region eu-central-1 \
  --query "Stacks[0].Outputs" \
  --output table
```

Then call:

```bash
curl http://EC2_PUBLIC_DNS:8080/health
curl http://EC2_PUBLIC_DNS:8080/trigger-npe
```
