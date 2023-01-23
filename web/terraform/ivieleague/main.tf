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
    bucket = "ivieleague-deployment-states"
    key    = "samfront"
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
  deployment_name     = "samfront"
  domain_name         = "samf.ivieleague.com"
  domain_name_zone    = "ivieleague.com"
}
