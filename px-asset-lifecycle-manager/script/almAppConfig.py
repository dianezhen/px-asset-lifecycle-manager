import os
import subprocess
import sys, getopt
import re

def checkRequirements():
    try:
        cfTarget = subprocess.check_output(["cf", "target"])
        print (cfTarget)
        user = cfTarget.split('User:')[1].split('Org:')[0]
        org = cfTarget.split('Org:')[1].split('Space:')[0]
        space = cfTarget.split('Space')[1]
        print("cf login detected")
        return (user.strip(), org, space)
    except subprocess.CalledProcessError as e:
        sys.exit("Please login to CF.")

#global org
#global space
#global user
global instanceAppender
global BASE_DIR
global BASE_PREDIX_DIR
global almAppName
global almUaaName
global almAcsName
global almAssetName
global almRedisName
global almPostgreName
global predixbootAppName
global predixUaaService
global predixAssetService
global predixRedisService
global predixPostgreService
global predixUaaServicePlan
global predixAssetServicePlan
global predixRedisServicePlan
global predixPostgreServicePlan
global almAppClientId
global almAppSecret
global uaaAdminSecret
global clientAuthorities
global clientScope
global masterDir
global projectDir
global predixProject
global environment
global mvnsettings
global mavenRepo
global allDeploy
global continueFrom
global artifactoryrepo
global artifactoryuser
global artifactorypass
global uaaIssuerId
global newDeployment
global uiUrl

try:
    #set defaults
    instanceAppender = ""
    #mvnsettings = "~/.m2/settings.xml"
    from os.path import expanduser
    homeDir = expanduser("~")

    mvnsettings=homeDir + "/.m2/settings.xml"

    masterDir = os.getcwd();

    #mvnsettings = ""
    pullsubmodules = 'y'
    mavenRepo = ""
    environment = "DEV"
    allDeploy = "y"
    continueFrom = "all"    
    only = ""
    newDeployment = "N"
    fastinstall = 'n'
    artifactoryrepo = ""
    artifactoryuser = ""
    artifactorypass = ""
    #override with arguments
    opts, args = getopt.getopt(sys.argv[1:],"e:i:s:p:r:a:v:c:o:f:x:y:z:",["environment=","instanceAppender=","mvnsettings=","pullsubmodules=","mavenrepo=","allDeploy=","continueFrom=","only=", "fastinstall=", "artifactoryrepo=", "artifactoryuser=", "artifactorypass="])
except getopt.GetoptError:
    print 'Exception when parsing : '+sys.argv[0]+' -e (R2/PROD) -i <Instance appender> -s <mvnsettings>'
    sys.exit(2)
for opt, arg in opts:
    print ('opt=' + opt + ' arg=' + arg)
    if opt == '-h':
        print sys.argv[0]+' -e (R2/PROD) -g <Github User> -i <Instance appender> -s <Maven settings file>'
        sys.exit()
    elif opt in ("-i", "--instanceappender"):
        instanceAppender = arg
    elif opt in ("-e", "--environment"):
        environment = arg
    elif opt in ("-s", "--mvnsettings"):
        mvnsettings = arg
    elif opt in ("-p","--pullsubmodules"):
        pullsubmodules = arg
    elif opt in ("-r","--mavenrepo"):
        mavenRepo = arg
    elif opt in ("-a","--alldeploy"):
        allDeploy = arg
    elif opt in ("-v","--verbose"):
        verbose = true;
    elif opt in ("-c","--continueFrom"):
        continueFrom = arg;
    elif opt in ("-o","--only"):
        only = arg;
    elif opt in ("-f","--fastinstall"):
        fastinstall = arg;
    elif opt in ("-x","--artifactoryrepo"):
        artifactoryrepo = arg;
    elif opt in ("-y","--artifactoryuser"):
        artifactoryuser = arg;
    elif opt in ("-z","--artifactorypass"):
        artifactorypass = arg;

#if mvnsettings == "":
#        print sys.argv[0]+' -e (R2/PROD) -g <Github User> -i <Instance appender> -s <Maven settings file>'
#        print 'Maven settings file is a mandatory argument.'
#        sys.exit()

# check check login
user, org, space = checkRequirements()
if len(instanceAppender) == 0:
    instanceAppender = user.strip().split("@")[0].replace('.', '_')
print ('using Appender', instanceAppender)

# check or create a directory for Reference application
BASE_DIR = os.getcwd()
BASE_PREDIX_DIR = "PredixApps"

# Reference App Service Instance Names
almUaaName = "alm_uaa_"+instanceAppender
almAcsName = "alm_acs_"+instanceAppender
almAssetName = "alm_asset_"+instanceAppender
almPostgreName = "alm_postgre_"+instanceAppender
almRedisName = "alm_redis_"+instanceAppender
almAppName = "alm_app_"+instanceAppender

#Repo Name
predixbootJSRRepoName = "\\boot"

# Predix Application Names
print ('instanceAppender=' + instanceAppender)
predixbootAppName = "boot-temp-" + instanceAppender
uiAppName = "px-asset-lifecycle-manager"

if environment == 'PROD':
    # Predix Service Instance Name for VPC
    predixUaaService = "predix-uaa"
    predixAcsService = "predix-acs"
    predixAssetService = "predix-asset"
    predixPostgreService = "postgres"
    predixRedisService = "redis"

    predixUaaServicePlan = "Tiered"
    predixAcsServicePlan = "Tiered"
    predixAssetServicePlan = "Tiered"
    predixPostgreServicePlan = "shared"
    predixRedisServicePlan = "shared-vm"
    artifactoryrepo = "https://artifactory.predix.io/artifactory/PREDIX-EXT"
elif environment == 'DEV':
    # Predix Service Instance Name for VPC
    predixUaaService = "predix-uaa"
    predixAcsService = "predix-acs"
    predixAssetService = "predix-asset"
    predixPostgreService = "postgres"
    predixRedisService = "redis-5"

    predixUaaServicePlan = "Tiered"
    predixAcsServicePlan = "Tiered"
    predixAssetServicePlan = "Tiered"
    predixPostgreServicePlan = "shared-nr"
    predixRedisServicePlan = "shared-vm"
    artifactoryrepo = "https://artifactory.predix.io/artifactory/PREDIX-EXT"
else :
    # Predix Service Instance Name for sysint
    predixUaaService = "predix-uaa-sysint"
    predixAcsService = "predix-acs-sysint"
    predixAssetService = "predix-asset-sysint"
    predixPostgreService = "rdpg"
    predixRedisService = "p-redis"

    predixUaaServicePlan = "free"
    predixAcsServicePlan = "free"
    predixAssetServicePlan = "Beta"
    predixPostgreServicePlan = "Free"
    predixRedisServicePlan = "shared-vm"

#Reference application client id
almAppClientId = "alm_app_client"
almAppSecret = "alm@pp5ecret"
#UAA Admin Account
uaaAdminSecret = "alm_uaa_secret"
clientGrantType = ["authorization_code","client_credentials","refresh_token","password"]
clientAuthorities = ["openid","acs.policies.read","acs.policies.write","acs.attributes.read","acs.attributes.write","uaa.resource","uaa.none"]
clientScope = ["uaa.none","openid","acs.policies.read","acs.policies.write","acs.attributes.read","acs.attributes.write"]

projectDir = "predix-microservice-templates"
predixProject = projectDir+".git"
#UAA User account for logging in to RMD Ref App
almUser1 = "alm_user_1"
almUser1Pass = "ALM_user_1"
#Admin User that is allowed to add asset data
almAdmin1 = "alm_admin_1"
almAdmin1Pass = "ALM_admin_1"
