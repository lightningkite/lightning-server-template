##########
# Inputs
##########

variable "cors" {
    type = object({ allowedDomains = list(string), allowedHeaders = list(string) })
    default = null
}
variable "display_name" {
    type = string
    default = "LKTemplate"
}

##########
# Outputs
##########


##########
# Resources
##########


