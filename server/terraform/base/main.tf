terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "~> 4.30"
    }
    local = {
      source = "hashicorp/local"
      version = "~> 2.2"
    }
    random = {
      source = "hashicorp/random"
      version = "~> 3.1.0"
    }
    archive = {
      source = "hashicorp/archive"
      version = "~> 2.2.0"
    }
    mongodbatlas = {
      source = "mongodb/mongodbatlas"
      version = "~> 1.4"
    }
  }
  required_version = "~> 1.0"
}

##########
# main
##########

resource "aws_api_gateway_account" "main" {
  cloudwatch_role_arn = aws_iam_role.cloudwatch.arn
}

resource "aws_iam_role" "cloudwatch" {
  name = "LKTemplate${var.deployment_name}"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": ["apigateway.amazonaws.com", "lambda.amazonaws.com"]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF
}
resource "aws_iam_role_policy" "cloudwatch" {
  name = "LKTemplate${var.deployment_name}_policy"
  role = aws_iam_role.cloudwatch.id

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogGroup",
                "logs:CreateLogStream",
                "logs:DescribeLogGroups",
                "logs:DescribeLogStreams",
                "logs:PutLogEvents",
                "logs:GetLogEvents",
                "logs:FilterLogEvents"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}


##########
# jwt
##########
resource "random_password" "jwt" {
  length           = 32
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

##########
# database
##########
resource "mongodbatlas_project" "database" {
  name   = "LKTemplate${var.deployment_name}database"
  org_id = var.database_org_id

  is_collect_database_specifics_statistics_enabled = true
  is_data_explorer_enabled                         = true
  is_performance_advisor_enabled                   = true
  is_realtime_performance_panel_enabled            = true
  is_schema_advisor_enabled                        = true
}
resource "mongodbatlas_project_ip_access_list" "database" {
  project_id   = mongodbatlas_project.database.id
  cidr_block = "0.0.0.0/0"
  comment    = "Anywhere"
}
resource "random_password" "database" {
  length           = 32
  special          = true
  override_special = "-_"
}
resource "mongodbatlas_serverless_instance" "database" {
  project_id   = mongodbatlas_project.database.id
  name         = "LKTemplate${var.deployment_name}database"

  provider_settings_backing_provider_name = "AWS"
  provider_settings_provider_name = "SERVERLESS"
  provider_settings_region_name = replace(upper(var.deployment_location), "-", "_")
}
resource "mongodbatlas_database_user" "database" {
  username           = "LKTemplate${var.deployment_name}database-main"
  password           = random_password.database.result
  project_id         = mongodbatlas_project.database.id
  auth_database_name = "admin"

  roles {
    role_name     = "readWrite"
    database_name = "default"
  }

  roles {
    role_name     = "readAnyDatabase"
    database_name = "admin"
  }

}

##########
# files
##########
####
# files: FilesSettings
####

resource "aws_s3_bucket" "files" {
  bucket_prefix = "lktemplate-${var.deployment_name}-files"
  force_destroy = var.debug
}
resource "aws_s3_bucket_cors_configuration" "files" {
  bucket = aws_s3_bucket.files.bucket

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["PUT", "POST"]
    allowed_origins = ["*"]
    expose_headers  = ["ETag"]
    max_age_seconds = 3000
  }

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["GET", "HEAD"]
    allowed_origins = ["*"]
  }
}
resource "aws_s3_bucket_policy" "files" {
  bucket = aws_s3_bucket.files.id
  policy = <<POLICY
{
    "Version": "2012-10-17",
    "Statement": [
      {
          "Sid": "PublicReadGetObject",
          "Effect": "Allow",
          "Principal": "*",
          "Action": [
             "s3:GetObject"
          ],
          "Resource": [
             "arn:aws:s3:::${aws_s3_bucket.files.id}/*"
          ]
      }
    ]
}
POLICY
}
resource "aws_s3_bucket_acl" "files" {
  bucket = aws_s3_bucket.files.id
  acl    = var.files_expiry == null ? "public-read" : "private"
}
resource "aws_iam_policy" "files" {
  name        = "LKTemplate-${var.deployment_name}-files"
  path = "/LKTemplate/${var.deployment_name}/files/"
  description = "Access to the LKTemplate-${var.deployment_name}_files bucket"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "s3:*",
        ]
        Effect   = "Allow"
        Resource = [
            "${aws_s3_bucket.files.arn}",
            "${aws_s3_bucket.files.arn}/*",
        ]
      },
    ]
  })
}
resource "aws_iam_role_policy_attachment" "files" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.files.arn
}

##########
# email
##########
resource "aws_iam_user" "email" {
  name = "LKTemplate-${var.deployment_name}-email-user"
}

resource "aws_iam_access_key" "email" {
  user = aws_iam_user.email.name
}

data "aws_iam_policy_document" "email" {
  statement {
    actions   = ["ses:SendRawEmail"]
    resources = ["*"]
  }
}

resource "aws_iam_policy" "email" {
  name = "LKTemplate-${var.deployment_name}-email-policy"
  description = "Allows sending of e-mails via Simple Email Service"
  policy      = data.aws_iam_policy_document.email.json
}

resource "aws_iam_user_policy_attachment" "email" {
  user       = aws_iam_user.email.name
  policy_arn = aws_iam_policy.email.arn
}


##########
# HTTP
##########

variable "public_http_url" {
  default = null
}
resource "aws_apigatewayv2_api" "http" {
  name = "LKTemplate-${var.deployment_name}-http"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_stage" "http" {
  api_id = aws_apigatewayv2_api.http.id

  name = "LKTemplate-${var.deployment_name}-gateway-stage"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.http_api.arn

    format = jsonencode({
      requestId               = "$context.requestId"
      sourceIp                = "$context.identity.sourceIp"
      requestTime             = "$context.requestTime"
      protocol                = "$context.protocol"
      httpMethod              = "$context.httpMethod"
      resourcePath            = "$context.resourcePath"
      routeKey                = "$context.routeKey"
      status                  = "$context.status"
      responseLength          = "$context.responseLength"
      integrationErrorMessage = "$context.integrationErrorMessage"
      }
    )
  }
}

resource "aws_apigatewayv2_integration" "http" {
  api_id = aws_apigatewayv2_api.http.id

  integration_uri    = aws_lambda_function.main.invoke_arn
  integration_type   = "AWS_PROXY"
  integration_method = "POST"
}

resource "aws_cloudwatch_log_group" "http_api" {
  name = "LKTemplate-${var.deployment_name}-http-gateway-log"

  retention_in_days = 30
}

resource "aws_apigatewayv2_route" "http" {
    api_id = aws_apigatewayv2_api.http.id
    route_key = "$default"
    target    = "integrations/${aws_apigatewayv2_integration.http.id}"
}

resource "aws_lambda_permission" "api_gateway_http" {
  statement_id  = "AllowExecutionFromAPIGatewayHTTP"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.main.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.http.execution_arn}/*/*"
}

##########
# WebSockets
##########
variable "public_ws_url" {
  default = null
}

resource "aws_apigatewayv2_api" "ws" {
  name = "LKTemplate-${var.deployment_name}-gateway"
  protocol_type = "WEBSOCKET"
  route_selection_expression = "constant"
}

resource "aws_apigatewayv2_stage" "ws" {
  api_id = aws_apigatewayv2_api.ws.id

  name = "LKTemplate-${var.deployment_name}-gateway-stage"
  auto_deploy = true

  access_log_settings {
    destination_arn = aws_cloudwatch_log_group.ws_api.arn

    format = jsonencode({
      requestId               = "$context.requestId"
      sourceIp                = "$context.identity.sourceIp"
      requestTime             = "$context.requestTime"
      protocol                = "$context.protocol"
      httpMethod              = "$context.httpMethod"
      resourcePath            = "$context.resourcePath"
      routeKey                = "$context.routeKey"
      status                  = "$context.status"
      responseLength          = "$context.responseLength"
      integrationErrorMessage = "$context.integrationErrorMessage"
      }
    )
  }
}

resource "aws_apigatewayv2_integration" "ws" {
  api_id = aws_apigatewayv2_api.ws.id

  integration_uri    = aws_lambda_function.main.invoke_arn
  integration_type   = "AWS_PROXY"
  integration_method = "POST"
}

resource "aws_cloudwatch_log_group" "ws_api" {
  name = "LKTemplate-${var.deployment_name}-ws-gateway-log"

  retention_in_days = 30
}

resource "aws_apigatewayv2_route" "ws_connect" {
    api_id = aws_apigatewayv2_api.ws.id

    route_key = "$connect"
    target    = "integrations/${aws_apigatewayv2_integration.ws.id}"
}
resource "aws_apigatewayv2_route" "ws_default" {
    api_id = aws_apigatewayv2_api.ws.id

    route_key = "$default"
    target    = "integrations/${aws_apigatewayv2_integration.ws.id}"
}
resource "aws_apigatewayv2_route" "ws_disconnect" {
    api_id = aws_apigatewayv2_api.ws.id

    route_key = "$disconnect"
    target    = "integrations/${aws_apigatewayv2_integration.ws.id}"
}

resource "aws_lambda_permission" "api_gateway_ws" {
  statement_id  = "AllowExecutionFromAPIGatewayWS"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.main.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${aws_apigatewayv2_api.ws.execution_arn}/*/*"
}

resource "aws_iam_policy" "api_gateway_ws" {
  name        = "LKTemplate-${var.deployment_name}-api_gateway_ws"
  path = "/LKTemplate/${var.deployment_name}/api_gateway_ws/"
  description = "Access to the LKTemplate-${var.deployment_name}_api_gateway_ws management"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "execute-api:ManageConnections"
        ]
        Effect   = "Allow"
        Resource = "*"
      },
    ]
  })
}
resource "aws_iam_role_policy_attachment" "api_gateway_ws" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.api_gateway_ws.arn
}

##########
# Alarms
##########
resource "aws_sns_topic" "emergency" {
  name = "LKTemplate-${var.deployment_name}_emergencies"
}
resource "aws_sns_topic_subscription" "emergency_primary" {
  topic_arn = aws_sns_topic.emergency.arn
  protocol  = "email"
  endpoint  = var.emergencyContact
}
resource "aws_cloudwatch_metric_alarm" "emergency_invocations" {
  alarm_name                = "LKTemplate-${var.deployment_name}_emergency_invocations"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "1"
  metric_name               = "Invocations"
  namespace                 = "AWS/Lambda"
  period                    = "60"
  statistic                 = "Sum"
  threshold                 = "${var.emergencyInvocationsPerMinuteThreshold}"
  alarm_description         = ""
  insufficient_data_actions = []
  dimensions = {
    FunctionName = aws_lambda_function.main.function_name
  }
  alarm_actions = [aws_sns_topic.emergency.arn]
}
resource "aws_cloudwatch_metric_alarm" "emergency_compute" {
  alarm_name                = "LKTemplate-${var.deployment_name}_emergency_compute"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "1"
  metric_name               = "Duration"
  namespace                 = "AWS/Lambda"
  period                    = "60"
  statistic                 = "Sum"
  threshold                 = "${var.emergencyComputePerMinuteThreshold}"
  alarm_description         = ""
  insufficient_data_actions = []
  dimensions = {
    FunctionName = aws_lambda_function.main.function_name
  }
  alarm_actions = [aws_sns_topic.emergency.arn]
}
resource "aws_cloudwatch_metric_alarm" "panic_invocations" {
  alarm_name                = "LKTemplate-${var.deployment_name}_panic_invocations"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "1"
  metric_name               = "Invocations"
  namespace                 = "AWS/Lambda"
  period                    = "60"
  statistic                 = "Sum"
  threshold                 = "${var.panicInvocationsPerMinuteThreshold}"
  alarm_description         = ""
  insufficient_data_actions = []
  dimensions = {
    FunctionName = aws_lambda_function.main.function_name
  }
  alarm_actions = [aws_sns_topic.emergency.arn]
}
resource "aws_cloudwatch_metric_alarm" "panic_compute" {
  alarm_name                = "LKTemplate-${var.deployment_name}_panic_compute"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "1"
  metric_name               = "Duration"
  namespace                 = "AWS/Lambda"
  period                    = "60"
  statistic                 = "Sum"
  threshold                 = "${var.panicComputePerMinuteThreshold}"
  alarm_description         = ""
  insufficient_data_actions = []
  dimensions = {
    FunctionName = aws_lambda_function.main.function_name
  }
  alarm_actions = [aws_sns_topic.emergency.arn]
}
resource "aws_cloudwatch_event_rule" "panic" {
  name        = "LKTemplate-${var.deployment_name}_panic"
  description = "Throttle the function in a true emergency."
  event_pattern = jsonencode({
    source = ["aws.cloudwatch"]
    "detail-type" = ["CloudWatch Alarm State Change"]
    detail = {
      alarmName = [
        aws_cloudwatch_metric_alarm.panic_invocations.alarm_name,
        aws_cloudwatch_metric_alarm.panic_compute.alarm_name
      ]
      previousState = {
        value = ["OK", "INSUFFICIENT_DATA"]
      }
      state = {
        value = ["ALARM"]
      }
    }
  })
}
resource "aws_cloudwatch_event_target" "panic" {
  rule      = aws_cloudwatch_event_rule.panic.name
  target_id = "lambda"
  arn       = aws_lambda_function.main.arn
  input     = "{\"panic\": true}"
  retry_policy {
    maximum_event_age_in_seconds = 60
    maximum_retry_attempts = 1
  }
}
resource "aws_lambda_permission" "panic" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.main.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.panic.arn
}

resource "aws_iam_role_policy_attachment" "panic" {
  role       = aws_iam_role.main_exec.name
  policy_arn = aws_iam_policy.panic.arn
}

resource "aws_iam_policy" "panic" {
  name        = "LKTemplate-${var.deployment_name}-panic"
  path = "/LKTemplate/${var.deployment_name}/panic/"
  description = "Access to self-throttle"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "lambda:PutFunctionConcurrency",
        ]
        Effect   = "Allow"
        Resource = [aws_lambda_function.main.arn]
      },
    ]
  })
}

##########
# Schedule paymentCheck
##########
resource "aws_cloudwatch_event_rule" "scheduled_task_paymentCheck" {
  name                = "LKTemplate-${var.deployment_name}_paymentCheck"
  schedule_expression = "rate(1440 minutes)"
}
resource "aws_cloudwatch_event_target" "scheduled_task_paymentCheck" {
  rule      = aws_cloudwatch_event_rule.scheduled_task_paymentCheck.name
  target_id = "lambda"
  arn       = aws_lambda_function.main.arn
  input     = "{\"scheduled\": \"paymentCheck\"}"
}
resource "aws_lambda_permission" "scheduled_task_paymentCheck" {
  statement_id  = "scheduled_task_paymentCheck"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.main.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.scheduled_task_paymentCheck.arn
}

##########
# Schedule cleanupUploads
##########
resource "aws_cloudwatch_event_rule" "scheduled_task_cleanupUploads" {
  name                = "LKTemplate-${var.deployment_name}_cleanupUploads"
  schedule_expression = "rate(1440 minutes)"
}
resource "aws_cloudwatch_event_target" "scheduled_task_cleanupUploads" {
  rule      = aws_cloudwatch_event_rule.scheduled_task_cleanupUploads.name
  target_id = "lambda"
  arn       = aws_lambda_function.main.arn
  input     = "{\"scheduled\": \"cleanupUploads\"}"
}
resource "aws_lambda_permission" "scheduled_task_cleanupUploads" {
  statement_id  = "scheduled_task_cleanupUploads"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.main.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.scheduled_task_cleanupUploads.arn
}

##########
# Main
##########
resource "aws_s3_bucket" "lambda_bucket" {
  bucket_prefix = "lktemplate-${var.deployment_name}-lambda-bucket"
  force_destroy = true
}
resource "aws_s3_bucket_acl" "lambda_bucket" {
  bucket = aws_s3_bucket.lambda_bucket.id
  acl    = "private"
}
resource "aws_iam_policy" "lambda_bucket" {
  name        = "LKTemplate-${var.deployment_name}-lambda_bucket"
  path = "/LKTemplate/${var.deployment_name}/lambda_bucket/"
  description = "Access to the LKTemplate-${var.deployment_name}_lambda_bucket bucket"
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
  name = "LKTemplate-${var.deployment_name}-main-exec"

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

resource "aws_iam_policy" "dynamo" {
  name        = "LKTemplate-${var.deployment_name}-dynamo"
  path = "/LKTemplate/${var.deployment_name}/dynamo/"
  description = "Access to the LKTemplate-${var.deployment_name}_dynamo tables in DynamoDB"
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
  name        = "LKTemplate-${var.deployment_name}-lambdainvoke"
  path = "/LKTemplate/${var.deployment_name}/lambdainvoke/"
  description = "Access to the LKTemplate-${var.deployment_name}_lambdainvoke bucket"
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
        url = "dynamodb://${var.deployment_location}/LKTemplate-${var.deployment_name}_${var.deployment_name}"
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
        publicUrl = var.public_http_url == null ? aws_apigatewayv2_stage.http.invoke_url : var.public_http_url
        wsUrl = var.public_ws_url == null ? aws_apigatewayv2_stage.ws.invoke_url : var.public_ws_url
        debug = var.debug
        cors = var.cors
    }
    database = {
      url = "mongodb+srv://LKTemplate${var.deployment_name}database-main:${random_password.database.result}@${replace(mongodbatlas_serverless_instance.database.connection_strings_standard_srv, "mongodb+srv://", "")}/default?retryWrites=true&w=majority"
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
        fromEmail = var.email_sender
    }
    notifications = var.notifications
    oauth_google = var.oauth_google
  })
}
resource "aws_lambda_function" "main" {
  function_name = "LKTemplate-${var.deployment_name}-main"

  s3_bucket = aws_s3_bucket.lambda_bucket.id
  s3_key    = aws_s3_object.app_storage.key

  runtime = "java11"
  handler = "com.lightningkite.template.AwsHandler"

  memory_size = "${var.lambda_memory_size}"
  timeout = var.lambda_timeout
  # memory_size = "1024"

  source_code_hash = filebase64sha256(local.lambda_source)

  role = aws_iam_role.main_exec.arn

  environment {
    variables = {
      LIGHTNING_SERVER_SETTINGS_BUCKET = aws_s3_object.app_settings.bucket
      LIGHTNING_SERVER_SETTINGS_FILE = aws_s3_object.app_settings.key
    }
  }

  depends_on = [aws_s3_object.app_storage]
}

resource "aws_cloudwatch_log_group" "main" {
  name = "LKTemplate-${var.deployment_name}-main-log"
  retention_in_days = 30
}

