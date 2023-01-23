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
terraform {
  backend "s3" {
    bucket = "lightningkite-terraform"
    key    = "template/front"
    region = "us-west-2"
  }
}
provider "aws" {
  region = "us-west-2"
}
provider "aws" {
  alias  = "acm"
  region = "us-east-1"
}
module "domain" {
  source              = "../domain"
  deployment_location = "us-west-2"
  deployment_name     = "templatefront"
  domain_name         = "template.cs.lightningkite.com"
  domain_name_zone    = "cs.lightningkite.com"
}
