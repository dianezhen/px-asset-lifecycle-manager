#######################################
# Begin Main script
#######################################
import sys
import almAppConfig as config
import almApp
import traceback

print ('environment : '+config.environment)
print ('continueFrom=' + config.continueFrom)
print ('only=' + config.only)
print("****************** Installing ALM Application ******************")
try:

	config.retryCount=0
	if config.only not in (''):
		if config.only in ('buildALMApp'):
			almApp.buildALMApp(config)
	        if config.only in ('deployALMAppDelete'):
		        almApp.deployALMAppDelete(config)
		if config.only in ('deployALMAppCreateUAA'):
			almApp.deployALMAppCreateUAA(config)
		if config.only in ('deployALMAppCreateAsset'):
			almApp.deployALMAppCreateAsset(config)
                        almApp.updateClientScopes(config)
		if config.only in ('deployALMAppCreateRedis'):
			almApp.deployALMAppCreateRedis(config)
		if config.only in ('deployALMAppCreatePostgre'):
			almApp.deployALMAppCreatePostgre(config)
		if config.only in ('deployALMAppFinalPrep'):
			almApp.deployALMAppFinalPrep(config)
	
		almApp.sanityChecks(config)
                almApp.cleanupALMAppDeploy(config)
	else :
		if config.continueFrom in ('all'):
			almApp.buildALMApp(config)
			if config.newDeployment in ('y','Y'):
				almApp.deployALMAppDelete(config)
			almApp.deployALMAppCreateUAA(config)
			almApp.deployALMAppCreateRedis(config)
			almApp.deployALMAppCreatePostgre(config)
			almApp.deployALMAppCreateAsset(config)
                        almApp.updateClientScopes(config)                        
			almApp.deployALMAppFinalPrep(config)
			almApp.sanityChecks(config)
                        almApp.cleanupALMAppDeploy(config)

		if config.continueFrom in ('continue','buildALMApp'):
			config.continueFrom = 'continue'
			almApp.buildALMApp(config)
		if config.newDeployment in ('y','Y'):
			if config.continueFrom in ('continue','deployALMAppDelete'):
				config.continueFrom = 'continue'
				almApp.deployALMAppDelete(config)
		if config.continueFrom in ('continue','deployALMAppCreateUAA'):
			config.continueFrom = 'continue'
			almApp.deployALMAppCreateUAA(config)
		if config.continueFrom in ('continue','deployALMAppCreateRedis'):
			config.continueFrom = 'continue'
			almApp.deployALMAppCreateRedis(config)
		if config.continueFrom in ('continue','deployALMAppCreatePostgre'):
			config.continueFrom = 'continue'
			almApp.deployALMAppCreatePostgre(config)
		if config.continueFrom in ('continue','deployALMAppCreateAsset'):
			config.continueFrom = 'continue'
			almApp.deployALMAppCreateAsset(config)
		if config.continueFrom in ('continue','updateClientScopes'):
			config.continueFrom = 'continue'                        
                        almApp.updateClientScopes(config)                        
		if config.continueFrom in ('continue','deployALMAppFinalPrep'):
			config.continueFrom = 'continue'
			almApp.deployALMAppFinalPrep(config)
		
	almApp.sanityChecks(config)
        almApp.cleanupALMAppDeploy(config)        

	print("*******************************************")
	print("**************** SUCCESS!! ****************")
	print("*******************************************")
	print ('Visit your live ALM App in the browser: '+ config.uiUrl)
except:
	print()
	print traceback.print_exc()
	print()
	if config.only in (''):
		print ('Exception when running ' + config.current + '.  After repairing the problem, use "--continueFrom ' + config.current + '" switch to resume the install') 
	print
	sys.exit(2)


