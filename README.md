# px-asset-lifecycle-manager

Predix Asset Lifecycle Manager App
==================================

The Predix Asset Lifecycle Manager app provides a UI integration into the Predix Asset service to instantiate, manager and decommission assets.  The application includes a Domain Schema input to allow the users to upload JSON schemas that will be used by the app for form generation and input validation.

To install

1. Prepare your environment.  You will need:
   <ol><li>Cloud Foundry CLI</li>
   <li>Maven</li>
   <li>Python</li>
   <li>On Windows you will need a Cygwin install with CURL</li>
    </ol>

2. Clone the repository to your machine

	  git clone https://github.comXXXXXXXXXXX/px-asset-lifecycle-manager.git
	  cd px-asset-lifecycle-manager

3. Run the install script. (You will be required to login to cloud foundry)
  ** Note, you can change environment attribute assignments in the script/almAppConfig.py file.

  python script/installAlmApp.py

4. Visit your app and start uploading your domain schemas and domain instances.
