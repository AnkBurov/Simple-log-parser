# Simple log parser #
Parser scans log files in specified directory. In each scanned file if parser sees specified error string, then all messages of that call with specified regexp identifier will be parsed into found log file

## Installation and configuration ##
* Install latest JRE from http://java.com. 
* Download latest release of this application in any folder.
* Copy log files which need to be parsed in specified log directory
* Open in cmd or bash this directory and configure settings in etc/config.properties. More information about each parameter in config.properties in comment sections,
* Run command "java -jar simplelogparcermaven-1.0.jar"
* Output information will be in foundLogPath. Application log will be updated if any exceptions happen. 

## Release notes ##
### 1.00 ###
* Release.