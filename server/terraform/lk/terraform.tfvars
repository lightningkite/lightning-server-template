
deployment_location = "us-west-2"
domain_name         = "templateapi.cs.lightningkite.com"
domain_name_zone    = "cs.lightningkite.com"
debug               = true
database_org_id = "6323a65c43d66b56a2ea5aea"
cors = {
  allowedDomains = ["*"]
  allowedHeaders = ["*"]
}
reporting_email = "joseph@lightningkite.com"
metrics = {
  url = "db://database"
  trackingByEntryPoint = [
    "executionTime"
  ]
  trackingTotalsOnly = []
}
exceptions = {
  url = "sentry://https://d965bf7aacd944b1ab7fad7bc7638195@sentry9.lightningkite.com/74"
}
emergencyContact = "joseph@lightningkite.com"
emergencyInvocationsPerMinuteThreshold = 1000
emergencyComputePerMinuteThreshold = 100000
panicInvocationsPerMinuteThreshold = 5000
panicComputePerMinuteThreshold = 500000
webUrl = "https://template.cs.lightningkite.com"
