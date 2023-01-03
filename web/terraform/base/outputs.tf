output "bucket" {
  value = {
    id: aws_s3_bucket.files.id,
    arn: aws_s3_bucket.files.arn,
    bucket: aws_s3_bucket.files.bucket,
    bucket_domain_name: aws_s3_bucket.files.bucket_domain_name,
    bucket_prefix: aws_s3_bucket.files.bucket_prefix,
    bucket_regional_domain_name: aws_s3_bucket.files.bucket_regional_domain_name,
    website: aws_s3_bucket.files.website,
    website_domain: aws_s3_bucket.files.website_domain,
    website_endpoint: aws_s3_bucket.files.website_endpoint,
  }
}