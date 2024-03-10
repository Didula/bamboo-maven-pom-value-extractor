# Maven POM value extractor plugin for Bamboo

This plugin can extract groupId, artifactId and version by default. You can also define custom values.
Extracted values can be made available as plan, job or result bamboo variables.

The original repo for this project is currently hosted at https://bitbucket.org/dehringer/bamboo-maven-pom-extractor-plugin
However the original plugin was not working with latest Bamboo when I tried to use it. 
Therefore, I fixed issues by recreating the entire plugin using the latest Atlas SDK.
I don't own any original code or design. 
You can find the original Atlassian marketplace link to the plugin by visiting the above bitbucket link.
This version isn't published anywhere. Simply build it and install manually.


To build this plugin,

* Install Atlassian atlas framework.
* Checkout this repo and navigate to root.
* Package the plugin using command
    ``atlas-package``
* Get the .jar file from the target and install it on the Bamboo from "Manage Apps"



Full documentation on atlassian atlas framework is always available at:

https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK
