def cfPush(cfCommand, projectDir):
	print("Deploying project as  "+cfCommand + " projectDir=" + projectDir)
	statementStatus  = subprocess.call(cfCommand, shell=True)
	if statementStatus == 1 :
		sys.exit("Error deploying the project"+projectDir)

	return statementStatus

def deleteExistingApplications(config):
	deleteRequest = "cf delete -f -r " + config.predixbootAppName
	statementStatus  = subprocess.call(deleteRequest, shell=True)
	if statementStatus == 1 :
		print("Error deleting an application: " +deleteRequest)
	time.sleep(10)  # Delay for 10 seconds        

	deleteRequest = "cf delete -f -r " +config.almAppName
	statementStatus  = subprocess.call(deleteRequest, shell=True)
	if statementStatus == 1 :
		print("Error deleting an application: " +deleteRequest)
	time.sleep(10)  # Delay for 10 seconds

        deleteRequest = "cf delete-orphaned-routes -f"
        statementStatus = subprocess.call(deleteRequest, shell=True)
	if statementStatus == 1 :
		print("Error deleting orphaned routes: " +deleteRequest)
	time.sleep(10)  # Delay for 10 seconds
        
	return statementStatus

def deleteSingleApplications(appName):
	deleteRequest = "cf delete -f -r " + appName
	statementStatus  = subprocess.call(deleteRequest, shell=True)
	if statementStatus == 1 :
		print("Error deleting an application: " +deleteRequest)
	time.sleep(10)  # Delay for 10 seconds

        deleteRequest = "cf delete-orphaned-routes -f"
        statementStatus = subprocess.call(deleteRequest, shell=True)
	if statementStatus == 1 :
		print("Error deleting orphaned routes: " +deleteRequest)
	time.sleep(10)  # Delay for 10 seconds
        
	return statementStatus

def deleteExistingServices(config):
	print("Delete Services>? : "+config.allDeploy)
	if config.allDeploy in ('y','Y'):
		#delete UAA instance
		deleteRequest = "cf delete-service -f "
		statementStatus  = subprocess.call(deleteRequest+config.almUaaName, shell=True)
		if statementStatus == 1 :
			sys.exit("Error deleting an service instance: " +config.almUaaName)
		time.sleep(10)  # Delay
                
		statementStatus  = subprocess.call(deleteRequest+config.almAssetName, shell=True)
		if statementStatus == 1 :
			sys.exit("Error deleting an service instance: " +config.almAssetName)
		time.sleep(3)  # Delay
                
		statementStatus  = subprocess.call(deleteRequest+config.almPostgreName, shell=True)
		if statementStatus == 1 :
			sys.exit("Error deleting an service instance: " +config.almPostgreName)
		time.sleep(3)  # Delay
                
		statementStatus  = subprocess.call(deleteRequest+config.almRedisName, shell=True)
		if statementStatus == 1 :
			sys.exit("Error deleting an service instance: " +config.almRedisName)
		time.sleep(20)  # Delay
                
		return statementStatus

def createPredixUAASecurityService(config):
	if config.allDeploy in ('y','Y'):
		#create UAA instance
	    uaa_payload_filename = 'uaa_payload.json'
	    data = {}
	    data['adminClientSecret'] = config.uaaAdminSecret

	    with open(uaa_payload_filename, 'w') as outfile:
	        json.dump(data, outfile)
	        outfile.close()

		uaaJsonrequest = "cf cs "+config.predixUaaService+" "+config.predixUaaServicePlan +" "+config.almUaaName+ " -c " + os.getcwd()+'/'+uaa_payload_filename+' -t uaa'
		print(uaaJsonrequest)
		statementStatus  = subprocess.call(uaaJsonrequest, shell=True)
		if statementStatus == 1 :
			sys.exit("Error creating a uaa service instance")
		return statementStatus

def getVcapJsonForPredixBoot (config):
	print("getVcapJsonForPredixBoot (config)")
	predixBootEnv = subprocess.check_output(["cf", "env" ,config.predixbootAppName])
	systemProvidedVars=predixBootEnv.split('System-Provided:')[1].split('No user-defined env variables have been set')[0]
	config.formattedJson = "[" + systemProvidedVars.replace("\n","").replace("'","").replace("}{","},{") + "]"
	print ("formattedJson=" + config.formattedJson)


def deployAndBindUAAToPredixBoot(config):
	print("Deploying to boot app CF...")
        os.chdir(config.masterDir + config.predixbootJSRRepoName)
	pushStatus = cfPush('cf push','boot-temp')
	print("Deployment to CF done.")
        os.chdir(config.masterDir)
		        
	statementStatus  = subprocess.call("cf bs "+config.predixbootAppName +" " + config.almUaaName , shell=True)
	if statementStatus == 1 :
			sys.exit("Error binding a uaa service instance to boot ")

	statementStatus  = subprocess.call("cf restage "+config.predixbootAppName, shell=True)
	if statementStatus == 1 :
			sys.exit("Error restaging a uaa service instance to boot")


def getUAAAdminToken(config):
	adminRealm = "admin:"+config.uaaAdminSecret
	adminRelmKey = base64.b64encode(adminRealm)

	headers = ' -H "Authorization: Basic '+adminRelmKey+'\" -H \"Content-Type: application/x-www-form-urlencoded\" '
	queryClientCreds= "grant_type=client_credentials"
	uaaAdminClientTokenCurl = "curl -X GET '"+config.uaaIssuerId+"?"+queryClientCreds+"'"+headers

	print ("*****************")
	print (" UAA Client GET client ADMIN token "+uaaAdminClientTokenCurl)
	print ("*****************")
	getAdminClientTokenResponse  = subprocess. check_output(uaaAdminClientTokenCurl, shell=True)
	getAdminClientTokenResponseJson = json.loads(getAdminClientTokenResponse)

  	print("Admin Token is "+getAdminClientTokenResponseJson['token_type']+" "+getAdminClientTokenResponseJson['access_token'])
  	return (getAdminClientTokenResponseJson['token_type']+" "+getAdminClientTokenResponseJson['access_token'])


def createClientIdAndAddUser(config):
	# setup the UAA login
	adminToken = processUAAClientId(config,config.UAA_URI+"/oauth/clients","POST")


def processUAAClientId (config,uuaClientURL,method):
	adminToken = getUAAAdminToken(config)
	if not adminToken :
		sys.exit("Error getting admin token from the UAA instance ")

	# create a client id
	print("****************** Creating client id ******************")
	print(config.clientScope)
	print(config.clientScopeList)

	createClientIdBody = {"client_id":"","client_secret":"","scope":[],"authorized_grant_types":[],"authorities":[],"autoapprove":["openid"]}
	createClientIdBody["client_id"] = config.almAppClientId
	createClientIdBody["client_secret"] = config.almAppSecret
	createClientIdBody["scope"] = config.clientScopeList
	createClientIdBody["authorized_grant_types"] = config.clientGrantType
	createClientIdBody["authorities"] = config.clientAuthoritiesList

	createClientIdBodyStr = json.dumps(createClientIdBody)

	headers = ' -H "Authorization:'+adminToken+'\" -H \"Content-Type: application/json\" '
	#uaaCreateClientCurl = 'curl -X '+method+' "'+uuaClientURL+'" -d "'+createClientIdBodyStr+'"'+headers
	uaaCreateClientCurl = "curl -X "+method+" '"+uuaClientURL+"' -d '"+createClientIdBodyStr+"'"+headers
	print ("*****************")
	print (" UAA Client Command"+uaaCreateClientCurl)
	print ("*****************")

	clientResponse  = subprocess. check_output(uaaCreateClientCurl, shell=True)
	statementStatusJson = json.loads(clientResponse)


	if statementStatusJson.get('error'):
		statementStatus = statementStatusJson['error']
		statementStatusDesc = statementStatusJson['error_description']
	else :
		statementStatus = 'success'
		statementStatusDesc = 'success'

	if statementStatus == 'success' or  'Client already exists' in statementStatusDesc :
		print("Success creating or reusing the Client Id")
	else :
		sys.exit("Error Processing ClientId on UAA "+statementStatusDesc )

	return adminToken


def updateClientIdAuthorities(config):
	processUAAClientId(config,config.UAA_URI+"/oauth/clients/"+config.almAppClientId,"PUT")

def getTokenFromUAA(config):
	url = config.uaaIssuerId
	oauthRelam = config.almAppClientId+":"+config.almAppSecret
	authKey = base64.b64encode(oauthRelam)
	print ( authKey)

	headers = ' -H "Authorization: Basic '+authKey+'\" -H \"Content-Type: application/x-www-form-urlencoded\" '
	queryClientCreds= "grant_type=client_credentials"

	uaaClientTokenCurl = "curl -X GET '"+config.uaaIssuerId+"?"+queryClientCreds+"'"+headers
	print ("*****************")
	print (" UAA Client GET client token "+uaaClientTokenCurl)
	print ("*****************")
	getClientTokenResponse  = subprocess. check_output(uaaClientTokenCurl, shell=True)
	getClientTokenResponseJson = json.loads(getClientTokenResponse)
	print("Client Token is "+getClientTokenResponseJson['token_type']+" "+getClientTokenResponseJson['access_token'])
	return (getClientTokenResponseJson['token_type']+" "+getClientTokenResponseJson['access_token'])

def createAsssetInstance(config,almPredixAssetName ,predixAssetName ):
	getPredixUAAConfigfromVcaps(config)
	asset_payload_filename = 'asset_payload.json'
	uaaList = [config.uaaIssuerId]
	data = {}
	data['trustedIssuerIds'] = uaaList
	with open(asset_payload_filename, 'w') as outfile:
		json.dump(data, outfile)
		print(data)
		outfile.close()

		assetJsonrequest = "cf cs "+predixAssetName+" "+config.predixAssetServicePlan +" "+almPredixAssetName+" -t asset -c "+os.getcwd()+'/' +asset_payload_filename
		print ("Creating Service cmd "+assetJsonrequest)
		statementStatus  = subprocess.call(assetJsonrequest, shell=True)
		if statementStatus == 1 :
			sys.exit("Error creating a assset service instance")

def createRedisInstance(config,almPredixRedisName,predixRedisName ):
	getPredixUAAConfigfromVcaps(config)
#	redis_payload_filename = 'redis_payload.json'
#	uaaList = [config.uaaIssuerId]
#	data = {}
#	data['trustedIssuerIds'] = uaaList
#	with open(redis_payload_filename, 'w') as outfile:
#		json.dump(data, outfile)
#		print(data)
#		outfile.close()

	redisJsonrequest = "cf cs "+predixRedisName+" "+config.predixRedisServicePlan +" "+config.almRedisName
	print ("Creating Service cmd "+redisJsonrequest)
	statementStatus  = subprocess.call(redisJsonrequest, shell=True)
	if statementStatus == 1 :
		sys.exit("Error creating a redis service instance")			

def createPostgreInstance(config,almPredixPostgreName,predixPostgreName ):
	getPredixUAAConfigfromVcaps(config)
#	postgre_payload_filename = 'postgre_payload.json'
#	uaaList = [config.uaaIssuerId]
#	data = {}
#	data['trustedIssuerIds'] = uaaList
#	with open(postgre_payload_filename, 'w') as outfile:
#		json.dump(data, outfile)
#		print(data)
#		outfile.close()

	postgreJsonrequest = "cf cs "+predixPostgreName+" "+config.predixPostgreServicePlan +" "+almPredixPostgreName
	print ("Creating Service cmd "+postgreJsonrequest)
	statementStatus  = subprocess.call(postgreJsonrequest, shell=True)
	if statementStatus == 1 :
		sys.exit("Error creating a redis service instance")	
		
def getPredixUAAConfigfromVcaps(config):
	if not hasattr(config,'uaaIssuerId') :
		getVcapJsonForPredixBoot(config)
		d = json.loads(config.formattedJson)
		config.uaaIssuerId =  d[0]['VCAP_SERVICES'][config.predixUaaService][0]['credentials']['issuerId']
		config.UAA_URI = d[0]['VCAP_SERVICES'][config.predixUaaService][0]['credentials']['uri']
		uaaZoneHttpHeaderName = d[0]['VCAP_SERVICES'][config.predixUaaService][0]['credentials']['zone']['http-header-name']
		uaaZoneHttpHeaderValue = d[0]['VCAP_SERVICES'][config.predixUaaService][0]['credentials']['zone']['http-header-value']
		print("****************** UAA configured As ******************")
		print ("\n uaaIssuerId = " + config.uaaIssuerId + "\n UAA_URI = " + config.UAA_URI + "\n "+uaaZoneHttpHeaderName+" = " +uaaZoneHttpHeaderValue+"\n")
		print("****************** ***************** ******************")


def bindService(applicationName , almServiceInstanceName):
	statementStatus  = subprocess.call("cf bs "+applicationName +" " + almServiceInstanceName , shell=True)
	if statementStatus == 1 :
		sys.exit("Error binding a "+almServiceInstanceName+" service instance to boot ")


def restageApplication(applicationName):
	statementStatus  = subprocess.call("cf restage "+applicationName, shell=True)
	if statementStatus == 1 :
		sys.exit("Error restaging a uaa service instance to boot")

def getAssetURLandZone(config):
	if not hasattr(config,'ASSET_ZONE') :
		assetUrl = ''
		assetZone =''
#		d = json.loads(config.formattedJson)
#		assetZone = d[0]['VCAP_SERVICES'][config.predixAssetService][0]['credentials']['instanceId']
#		assetUrl = d[0]['VCAP_SERVICES'][config.predixAssetService][0]['credentials']['uri']
		config.ASSET_ZONE = assetZone
		config.ASSET_URI = assetUrl

def getClientAuthoritiesforAssetService(config):
        getPredixUAAConfigfromVcaps(config)
        getVcapJsonForPredixBoot(config)
	d = json.loads(config.formattedJson)

	config.assetScopes = config.predixAssetService+".zones."+d[0]['VCAP_SERVICES'][config.predixAssetService][0]['credentials']['instanceId']+".user"
        print("*** ASSET SCOPE = " + config.assetScopes + " ***")
	config.clientAuthoritiesList.append(config.assetScopes)
	config.clientScopeList.append(config.assetScopes)

def configureManifest(config, manifestLocation):
	# create a backup
	if os.path.isfile(manifestLocation + "/manifest.yml"):
		shutil.copy(manifestLocation+"/manifest.yml", manifestLocation+"/manifest.yml.bak")
	# copy template as manifest
	shutil.copy(manifestLocation+"/manifest.yml.template", manifestLocation+"/manifest.yml")
	s = open(manifestLocation+"/manifest.yml").read()

        s = s.replace('<APP_NAME>', config.almAppName)
	s = s.replace('<ASSET_SERVICE>', config.almAssetName)
	s = s.replace('<UAA_SERVICE>', config.almUaaName)
	s = s.replace('<UAA_CLIENTID>', config.almAppClientId)
	s = s.replace('<UAA_CLIENTSECRET>', config.almAppSecret)
	s = s.replace('<POSTGRES_SERVICE>', config.almPostgreName)
	s = s.replace('<REDIS_SERVICE>', config.almRedisName)

	f = open(manifestLocation+"/manifest.yml", 'w')
	f.write(s)
	f.close()
	with open(manifestLocation+'/manifest.yml', 'r') as fin:
		print (fin.read())

#####################################################################################################
############################### main methods ###############################
#####################################################################################################

def buildALMApp(config):
	try:
		config.current='buildALMApp'
		print("Fast install set = " + config.fastinstall)
                print("Build using maven setting : "+config.mvnsettings +" Maven Repo : "+config.mavenRepo)
		if config.fastinstall != 'y' :
			print("Compiling code...")
			if config.mavenRepo != "":
				os.removedirs(config.mavenRepo)
				#statementStatus  = subprocess.call("rm -rf "+config.mavenRepo, shell=True)
				if config.mvnsettings == "":
					statementStatus  = subprocess.call("mvn clean package -Dmaven.test.skip=true -Dmaven.repo.local="+config.mavenRepo, shell=True)
				else:
					statementStatus  = subprocess.call("mvn clean package -s "+config.mvnsettings+" -Dmaven.repo.local="+config.mavenRepo, shell=True)
			else:
				 #statementStatus  = subprocess.call("rm -rf ~/.m2/repository/com/ge/predix/", shell=True)
				if config.mvnsettings == "":
				 	statementStatus  = subprocess.call("mvn clean package -Dmaven.test.skip=true", shell=True)
				else:
				 	statementStatus  = subprocess.call("mvn clean package -Dmaven.test.skip=true -s "+config.mvnsettings, shell=True)
		 	if statementStatus != 0:
				print("Maven build failed.")
				sys.exit(1);
		config.retryCount=0
	except:
		print traceback.print_exc()
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			buildALMApp(config)
		else :
			raise

def deployALMAppDelete(config):
	try:
		print("****************** Installing deployALMAppDelete ******************")
		config.current='deployALMAppDelete'
		# Deleting existing Applications and Services
		deleteExistingApplications(config)
		deleteExistingServices(config)
		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			time.sleep(10)  # Delay
			deployALMAppDelete(config)
		else :
			raise

def cleanupALMAppDeploy(config):
	try:
		print("****************** Cleaning Up ALMAppDeployment Artifacts ******************")
		config.current='cleanupALMAppDeploy'
		# Deleting existing Applications and Services
		deleteSingleApplications(config.predixbootAppName)
		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			time.sleep(10)  # Delay
			cleanupALMAppDeploy(config)
		else :
			raise

                
def getAuthorities(config):
	if not hasattr(config,'clientAuthoritiesList') :
		config.clientAuthoritiesList = list(config.clientAuthorities)
		config.clientScopeList = list(config.clientScope)

def deployALMAppCreateUAA(config):
	try :
		print("****************** Running deployALMAppCreateUAA ******************")
		config.current='deployALMAppCreateUAA'
		
		# these two are modified by some other functions.
		getAuthorities(config)

		createPredixUAASecurityService(config)
		time.sleep(10)

		#Bind to Predix Boot
		deployAndBindUAAToPredixBoot(config)
		getPredixUAAConfigfromVcaps(config)

		if config.allDeploy in ('y','Y'):
			#Create Client Id and Users
			createClientIdAndAddUser(config)
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			deployALMAppCreateUAA(config)
		else :
			raise

def deployALMAppCreateRedis(config):
	try:
		print("****************** Running deployALMAppCreateRedis******************")
		config.current='deployALMAppCreateRedis'

		if config.allDeploy in ('y','Y'):
			# create a Redis Service
			print("****************** Predix Redis  ******************")
			createRedisInstance(config,config.almRedisName,config.predixRedisService)

		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			deployALMAppCreateRedis(config)
		else :
			raise

def deployALMAppCreatePostgre(config):
	try:
		print("****************** Running deployALMAppCreatePostgre******************")
		config.current='deployALMAppCreatePostgre'

		if config.allDeploy in ('y','Y'):
			# create a Postgre Service
			print("****************** Predix Redis  ******************")
			createPostgreInstance(config,config.almPostgreName,config.predixPostgreService)

		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			deployALMAppCreatePostgre(config)
		else :
			raise

			
def updateClientAuthoritiesAssetService(config):
	getClientAuthoritiesforAssetService(config)

def deployALMAppCreateAsset(config):
	try:
		print("****************** Running deployALMAppCreateAsset ******************")
		config.current='deployALMAppCreateAsset'

		if config.allDeploy in ('y','Y'):
			# create a Asset Service
			print("****************** Predix Asset  ******************")
			createAsssetInstance(config,config.almAssetName,config.predixAssetService)

	                statementStatus  = subprocess.call("cf bs "+config.predixbootAppName +" " + config.almAssetName , shell=True)
	                if statementStatus == 1 :
		                sys.exit("Error binding a uaa service instance to boot ")

	                statementStatus  = subprocess.call("cf restage "+config.predixbootAppName, shell=True)
                        if statementStatus == 1 :
			        sys.exit("Error restaging a uaa service instance to boot")


		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			deployALMAppCreateAsset(config)
		else :
			raise

def updateClientScopes(config) :
        try:
                print("****************** Adding Asset Scope to Client ******************")
		config.current='updateClientScopes'
		getAuthorities(config)                
                updateClientAuthoritiesAssetService(config)
		updateClientIdAuthorities(config)
                
                config.retryCount = 0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			updateClientScopes (config)
		else :
			raise                
                

def deployALMAppFinalPrep(config):
	try:
		print("****************** Running deployALMAppFinalPrep ******************")
		config.current='deployALMAppFinalPrep'

                configureManifest(config, config.masterDir)
                os.chdir(config.masterDir)
                pushStatus = cfPush('cf push -f manifest.yml', config.almAppName)
                print("deployment to CF Done")

		config.retryCount=0
	except:
		print(traceback.print_exc())
		print()
		print ('Exception when running ' + config.current + '.  Retrying')
		config.retryCount = config.retryCount + 1
		if config.retryCount <= 1 :
			deployALMAppFinalPrep(config)
		else :
			raise

def sanityChecks(config):
	config.current='sanityChecks'
	# Sanity checks:
	jsonrequest = "cf apps | grep "+config.instanceAppender
	statementStatus  = subprocess.call(jsonrequest, shell=True)

	jsonrequest = "cf s | grep "+ config.instanceAppender
	statementStatus  = subprocess.call(jsonrequest, shell=True)

	cfTarget= subprocess.check_output(["cf", "app",config.almAppName])
	print (cfTarget)
	config.uiUrl="https://"+cfTarget.split('urls:')[1].strip().split('last uploaded:')[0].strip()        


	getPredixUAAConfigfromVcaps(config)
	getAssetURLandZone(config)

	print ('uaaAdmin= ' + config.uaaAdminSecret)
	print ('clientId= ' + config.almAppClientId)
	print ('clientSecret= ' + config.almAppSecret)
	print ('almUser= ' + config.almUser1)
	print ('almUserPass= ' + config.almUser1Pass)
	print ('almAdmin= ' + config.almAdmin1)
	print ('almAdminPass= ' + config.almAdmin1Pass)
	print ('client basic auth= ' + base64.b64encode(config.almAppClientId+":"+config.almAppSecret))

#######################################
# Begin Main script
#######################################
import subprocess
import sys
import traceback
import os
import json
import base64
import random
import string
import shutil
import time
import argparse
import re
import xml.dom.minidom
import base64
try:
	from urllib2 import Request, urlopen
	from urllib2 import URLError, HTTPError
except ImportError:
	from urllib.request import Request, urlopen
	from urllib.error import URLError, HTTPError


from xml.dom.minidom import parse
