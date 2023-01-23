terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.1.0"
    }
    archive = {
      source  = "hashicorp/archive"
      version = "~> 2.2.0"
    }
  }
  required_version = "~> 1.0"
}

variable "debug" {
  default = true
}
variable "domain_name_zone" {
}
variable "domain_name" {
}
variable "deployment_name" {
  default = "example"
}
variable "deployment_location" {
  default = "us-west-2"
}
provider "aws" {
  alias  = "acm"
  region = "us-east-1"
}

data "aws_route53_zone" "main" {
  name = var.domain_name_zone
}

resource "aws_acm_certificate" "web" {
  provider          = aws.acm
  domain_name       = var.domain_name
  validation_method = "DNS"
}
resource "aws_route53_record" "web" {
  zone_id = data.aws_route53_zone.main.zone_id
  name    = tolist(aws_acm_certificate.web.domain_validation_options)[0].resource_record_name
  type    = tolist(aws_acm_certificate.web.domain_validation_options)[0].resource_record_type
  records = [tolist(aws_acm_certificate.web.domain_validation_options)[0].resource_record_value]
  ttl     = "300"
}
resource "aws_acm_certificate_validation" "web" {
  provider                = aws.acm
  certificate_arn         = aws_acm_certificate.web.arn
  validation_record_fqdns = [aws_route53_record.web.fqdn]
}
resource "aws_route53_record" "web_cloudfront" {
  name    = var.domain_name
  zone_id = data.aws_route53_zone.main.zone_id
  type    = "A"

  alias {
    name                   = aws_cloudfront_distribution.main.domain_name
    zone_id                = aws_cloudfront_distribution.main.hosted_zone_id
    evaluate_target_health = true
  }
}


resource "aws_cloudfront_origin_access_identity" "oai" {
  comment = "OAI for ${var.domain_name}"
}
resource "aws_cloudfront_distribution" "main" {
  enabled             = true
  aliases             = [var.domain_name]
  default_root_object = "index.html"
  origin {
    domain_name = replace(module.Base.bucket.website_endpoint, "http://", "")
    origin_id   = module.Base.bucket.id

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"
      origin_ssl_protocols   = ["TLSv1", "TLSv1.1", "TLSv1.2", "SSLv3"]
    }
  }
  default_cache_behavior {
    allowed_methods        = ["GET", "HEAD", "OPTIONS", "PUT", "POST", "PATCH", "DELETE"]
    cached_methods         = ["GET", "HEAD", "OPTIONS"]
    target_origin_id       = module.Base.bucket.id
    viewer_protocol_policy = "redirect-to-https" # other options - https only, http

    forwarded_values {
      headers      = []
      query_string = true

      cookies {
        forward = "all"
      }
    }

  }

  restrictions {
    geo_restriction {
      restriction_type = "whitelist"
      locations        = ["IN", "US", "CA"]
    }
  }

  viewer_certificate {
    acm_certificate_arn      = aws_acm_certificate.web.arn
    ssl_support_method       = "sni-only"
    minimum_protocol_version = "TLSv1.2_2018"
  }
}


module "Base" {
  source              = "../base"
  deployment_location = var.deployment_location
  deployment_name     = var.deployment_name

}
