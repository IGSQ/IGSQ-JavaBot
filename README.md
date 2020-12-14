# IGSQBot Developer Edition
 
## TODO
**Features that are still to be fully implemented**
 - [ ] Verification Rewrite
 - [ ] GUIGenerator
 - [ ] Project Refactor
 - [ ] Logging
 - [ ] Help / Modhelp
 - [ ] Alias / Decline
 - [ ] Move from Arrays to ArrayLists and Deprecate ArrayUtils#append / ArrayUtils#depend

## Known Issues
**Known bugs or issues, these will get patched in the next minor update.**
 - GUIGenerator#menu & GUIGenerator#confirmation can take extended amounts of time to register

## Developer Notes
**Error Handling**

This project implements an error handler which is dependent on
the config option `BOT.error` within the `config` YML file, without this set to a valid channel ID, which the bot can write into, 
the handler will default to console only logging.

**Building and Running**

In order to run this project, you need JDK 14 or higher.
You can use the Maven `pom.xml` to build the project in to a JAR.
You must point to `your-jdk-install/bin/java.exe` to execute the JAR correctly.

In order to connect to discord successfully, you must put a valid bot token (see https://discord.com/developers/applications)
in `BOT.token` within the `config` YML file, which is auto generated on first launch.

**Example .bat file to run the JAR**
```
@echo off
"C:\Program Files\Java\jdk-14.0.2\bin\java.exe" -jar "your JAR here"
pause
```
