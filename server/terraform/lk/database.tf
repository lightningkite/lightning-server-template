##########
# Inputs
##########

variable "database_org_id" {
    type = string
}

##########
# Outputs
##########


##########
# Resources
##########

resource "mongodbatlas_project" "database" {
  name   = "LKTemplatetemplateapidatabase"
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
  name         = "LKTemplatetemplateapidatabase"

  provider_settings_backing_provider_name = "AWS"
  provider_settings_provider_name = "SERVERLESS"
  provider_settings_region_name = replace(upper(var.deployment_location), "-", "_")
}
resource "mongodbatlas_database_user" "database" {
  username           = "LKTemplatetemplateapidatabase-main"
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

