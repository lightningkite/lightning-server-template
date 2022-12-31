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
  }
  required_version = "~> 1.0"
}

module "Base" {
  source              = "../base"
  # main
  deployment_location = var.deployment_location
  deployment_name = var.deployment_name
  debug = var.debug
  lambda_in_vpc = var.lambda_in_vpc
  ip_prefix = var.ip_prefix
  # jwt
  jwt_expirationMilliseconds = var.jwt_expirationMilliseconds
  jwt_emailExpirationMilliseconds = var.jwt_emailExpirationMilliseconds
  # oauth_github
  oauth_github = var.oauth_github
  # exceptions
  exceptions = var.exceptions
  # oauth_apple
  oauth_apple = var.oauth_apple
  # general
  cors = var.cors
  display_name = var.display_name
  # database
  database_min_capacity = var.database_min_capacity
  database_max_capacity = var.database_max_capacity
  database_auto_pause = var.database_auto_pause
  # stripe
  stripe = var.stripe
  # logging
  logging = var.logging
  # files
  files_expiry = var.files_expiry
  # metrics
  metrics = var.metrics
  # email
  email_sender = "noreply@${var.domain_name}"
  # notifications
  notifications = var.notifications
  # oauth_google
  oauth_google = var.oauth_google
  # HTTP
  public_http_url = "https://${var.domain_name}"
  # WebSockets
  public_ws_url = "wss://ws.${var.domain_name}"
  # Alarms
  emergencyInvocationsPerMinuteThreshold = var.emergencyInvocationsPerMinuteThreshold
  emergencyComputePerMinuteThreshold = var.emergencyComputePerMinuteThreshold
  panicInvocationsPerMinuteThreshold = var.panicInvocationsPerMinuteThreshold
  panicComputePerMinuteThreshold = var.panicComputePerMinuteThreshold
  emergencyContact = var.emergencyContact
  # Main
  lambda_memory_size = var.lambda_memory_size
  lambda_timeout = var.lambda_timeout
}
##########
# main
##########
data "aws_route53_zone" "main" {
  name = var.domain_name_zone
}

##########
# email
##########
resource "aws_ses_domain_identity" "email" {
  domain = var.domain_name
}
resource "aws_ses_domain_mail_from" "email" {
  domain           = aws_ses_domain_identity.email.domain
  mail_from_domain = "mail.${var.domain_name}"
}
resource "aws_route53_record" "email_mx" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = aws_ses_domain_mail_from.email.mail_from_domain
  type    = "MX"
  ttl     = "600"
  records = ["10 feedback-smtp.${var.deployment_location}.amazonses.com"] # Change to the region in which `aws_ses_domain_identity.example` is created
}
resource "aws_route53_record" "email" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = "_amazonses.${var.domain_name}"
  type    = "TXT"
  ttl     = "600"
  records = [aws_ses_domain_identity.email.verification_token]
}
resource "aws_ses_domain_dkim" "email_dkim" {
  domain = aws_ses_domain_identity.email.domain
}
resource "aws_route53_record" "email_spf_mail_from" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = aws_ses_domain_mail_from.email.mail_from_domain
  type    = "TXT"
  ttl     = "300"
  records = [
    "v=spf1 include:amazonses.com -all"
  ]
}
resource "aws_route53_record" "email_spf_domain" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = aws_ses_domain_identity.email.domain
  type    = "TXT"
  ttl     = "300"
  records = [
    "v=spf1 include:amazonses.com -all"
  ]
}
resource "aws_route53_record" "email_dkim_records" {
  count   = 3
  zone_id = data.aws_route53_zone.main.zone_id
  name    = "${element(aws_ses_domain_dkim.email_dkim.dkim_tokens, count.index)}._domainkey.${var.domain_name}"
  type    = "CNAME"
  ttl     = "300"
  records = [
    "${element(aws_ses_domain_dkim.email_dkim.dkim_tokens, count.index)}.dkim.amazonses.com",
  ]
}
resource "aws_route53_record" "email_route_53_dmarc_txt" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = "_dmarc.${var.domain_name}"
  type    = "TXT"
  ttl     = "300"
  records = [
    "v=DMARC1;p=quarantine;pct=75;rua=mailto:${var.reporting_email}"
  ]
}

##########
# HTTP
##########
resource "aws_acm_certificate" "http" {
  domain_name   = var.domain_name
  validation_method = "DNS"
}
resource "aws_route53_record" "http" {
  zone_id = data.aws_route53_zone.main.zone_id
  name = tolist(aws_acm_certificate.http.domain_validation_options)[0].resource_record_name
  type = tolist(aws_acm_certificate.http.domain_validation_options)[0].resource_record_type
  records = [tolist(aws_acm_certificate.http.domain_validation_options)[0].resource_record_value]
  ttl = "300"
}
resource "aws_acm_certificate_validation" "http" {
  certificate_arn = aws_acm_certificate.http.arn
  validation_record_fqdns = [aws_route53_record.http.fqdn]
}
resource aws_apigatewayv2_domain_name http {
  domain_name = var.domain_name
  domain_name_configuration {
    certificate_arn = aws_acm_certificate.http.arn
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"
  }
  depends_on = [aws_acm_certificate_validation.http]
}
resource aws_apigatewayv2_api_mapping http {
  api_id      = module.Base.http.api_id
  domain_name = aws_apigatewayv2_domain_name.http.domain_name
  stage       = module.Base.http.id
}
resource aws_route53_record httpAccess {
  type    = "A"
  name    = aws_apigatewayv2_domain_name.http.domain_name
  zone_id = data.aws_route53_zone.main.id
    alias {
      evaluate_target_health = false
      name                   = aws_apigatewayv2_domain_name.http.domain_name_configuration[0].target_domain_name
      zone_id                = aws_apigatewayv2_domain_name.http.domain_name_configuration[0].hosted_zone_id
    }
}

##########
# WebSockets
##########
resource "aws_acm_certificate" "ws" {
  domain_name   = "ws.${var.domain_name}"
  validation_method = "DNS"
}
resource "aws_route53_record" "ws" {
  zone_id = data.aws_route53_zone.main.zone_id
  name = tolist(aws_acm_certificate.ws.domain_validation_options)[0].resource_record_name
  type = tolist(aws_acm_certificate.ws.domain_validation_options)[0].resource_record_type
  records = [tolist(aws_acm_certificate.ws.domain_validation_options)[0].resource_record_value]
  ttl = "300"
}
resource "aws_acm_certificate_validation" "ws" {
  certificate_arn = aws_acm_certificate.ws.arn
  validation_record_fqdns = [aws_route53_record.ws.fqdn]
}
resource aws_apigatewayv2_domain_name ws {
  domain_name = "ws.${var.domain_name}"
  domain_name_configuration {
    certificate_arn = aws_acm_certificate.ws.arn
    endpoint_type   = "REGIONAL"
    security_policy = "TLS_1_2"
  }
  depends_on = [aws_acm_certificate_validation.ws]
}
resource aws_apigatewayv2_api_mapping ws {
  api_id      = module.Base.ws.api_id
  domain_name = aws_apigatewayv2_domain_name.ws.domain_name
  stage       = module.Base.ws.id
}
resource aws_route53_record wsAccess {
  type    = "A"
  name    = aws_apigatewayv2_domain_name.ws.domain_name
  zone_id = data.aws_route53_zone.main.id
    alias {
      evaluate_target_health = false
      name                   = aws_apigatewayv2_domain_name.ws.domain_name_configuration[0].target_domain_name
      zone_id                = aws_apigatewayv2_domain_name.ws.domain_name_configuration[0].hosted_zone_id
    }
}

