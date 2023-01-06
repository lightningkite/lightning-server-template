##########
# Inputs
##########

variable "lambda_memory_size" {
    type = number
    default = 1024
}
variable "lambda_timeout" {
    type = number
    default = 30
}

##########
# Outputs
##########


##########
# Resources
##########

resource "aws_s3_bucket" "lambda_bucket" {
  bucket_prefix = "lktemplate-templateapi-lambda-bucket"
  force_destroy = true
}
resource "aws_s3_bucket_acl" "lambda_bucket" {
  bucket = aws_s3_bucket.lambda_bucket.id
  acl    = "private"
}
resource "aws_iam_policy" "lambda_bucket" {
  name        = "LKTemplate-templateapi-lambda_bucket"
  path = "/lktemplate/templateapi/lambda_bucket/"
  description = "Access to the LKTemplate-templateapi_lambda_bucket bucket"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "s3:GetObject",
        ]
        Effect   = "Allow"
        Resource = [
            "${aws_s3_bucket.lambda_bucket.arn}",
            "${aws_s3_bucket.lambda_bucket.arn}/*",
        ]
      },
    ]
  })
}
resource "aws_iam_role_policy_attachment" "lambda_bucket" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.lambda_bucket.arn
}

resource "aws_iam_role" "main_exec" {
  name = "LKTemplate-templateapi-main-exec"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Sid    = ""
      Principal = {
        Service = "lambda.amazonaws.com"
      }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "dynamo" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.dynamo.arn
}
resource "aws_iam_role_policy_attachment" "main_policy_exec" {
  role       = aws_iam_role.main_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
resource "aws_iam_role_policy_attachment" "main_policy_vpc" {
  role       = aws_iam_role.main_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaVPCAccessExecutionRole"
}

resource "aws_iam_policy" "dynamo" {
  name        = "LKTemplate-templateapi-dynamo"
  path = "/lktemplate/templateapi/dynamo/"
  description = "Access to the LKTemplate-templateapi_dynamo tables in DynamoDB"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "dynamodb:*",
        ]
        Effect   = "Allow"
        Resource = ["*"]
      },
    ]
  })
}
resource "aws_iam_policy" "lambdainvoke" {
  name        = "LKTemplate-templateapi-lambdainvoke"
  path = "/lktemplate/templateapi/lambdainvoke/"
  description = "Access to the LKTemplate-templateapi_lambdainvoke bucket"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "lambda:InvokeFunction",
        ]
        Effect   = "Allow"
        Resource = "*"
      },
    ]
  })
}
resource "aws_iam_role_policy_attachment" "lambdainvoke" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.lambdainvoke.arn
}
locals {
  lambda_source = "../../build/dist/lambda.zip"
}
resource "aws_s3_object" "app_storage" {
  bucket = aws_s3_bucket.lambda_bucket.id

  key    = "lambda-functions.zip"
  source = local.lambda_source

  source_hash = filemd5(local.lambda_source)
}
resource "aws_s3_object" "app_settings" {
  bucket = aws_s3_bucket.lambda_bucket.id

  key    = "settings.json"
  content = jsonencode({
    cache = {
        url = "dynamodb://${var.deployment_location}/LKTemplate_templateapi"
    }
    jwt = {
        expirationMilliseconds = var.jwt_expirationMilliseconds 
        emailExpirationMilliseconds = var.jwt_emailExpirationMilliseconds 
        secret = random_password.jwt.result
    }
    oauth_github = var.oauth_github
    exceptions = var.exceptions
    oauth_apple = var.oauth_apple
    general = {
        projectName = var.display_name
        publicUrl = "https://${var.domain_name}"
        wsUrl = "wss://ws.${var.domain_name}"
        debug = var.debug
        cors = var.cors
    }
    database = {
      url = "mongodb+srv://LKTemplatetemplateapidatabase-main:${random_password.database.result}@${replace(mongodbatlas_serverless_instance.database.connection_strings_standard_srv, "mongodb+srv://", "")}/default?retryWrites=true&w=majority"
    }
    webUrl = var.webUrl
    stripe = var.stripe
    logging = var.logging
    files = {
        storageUrl = "s3://${aws_s3_bucket.files.id}.s3-${aws_s3_bucket.files.region}.amazonaws.com"
        signedUrlExpiration = var.files_expiry
    }
    metrics = var.metrics
    email = {
        url = "smtp://${aws_iam_access_key.email.id}:${aws_iam_access_key.email.ses_smtp_password_v4}@email-smtp.us-west-2.amazonaws.com:587" 
        fromEmail = "noreply@${var.domain_name}"
    }
    notifications = var.notifications
    oauth_google = var.oauth_google
  })
}
resource "aws_lambda_function" "main" {
  function_name = "LKTemplate-templateapi-main"

  s3_bucket = aws_s3_bucket.lambda_bucket.id
  s3_key    = aws_s3_object.app_storage.key

  runtime = "java11"
  handler = "com.lightningkite.template.AwsHandler"
  
  memory_size = "${var.lambda_memory_size}"
  timeout = var.lambda_timeout
  # memory_size = "1024"

  source_code_hash = filebase64sha256(local.lambda_source)

  role = aws_iam_role.main_exec.arn
  
  snap_start {
    apply_on = "PublishedVersions"
  }

  
  
  
  environment {
    variables = {
      LIGHTNING_SERVER_SETTINGS_BUCKET = aws_s3_object.app_settings.bucket
      LIGHTNING_SERVER_SETTINGS_FILE = aws_s3_object.app_settings.key
    }
  }
  
  depends_on = [aws_s3_object.app_storage]
}

resource "aws_cloudwatch_log_group" "main" {
  name = "LKTemplate-templateapi-main-log"
  retention_in_days = 30
}

