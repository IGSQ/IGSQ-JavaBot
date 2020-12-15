# IGSQBot Developer Edition
## TODO
**Features that are still to be fully implemented**
 - [x] Verification Rewrite (to be reviewed)
 - [ ] GUIGenerator further implementation
 - [x] Project Refactor
 - [ ] Logging expansion
 - [ ] Help / Modhelp
 - [ ] Alias / Decline
 - [x] Move from Arrays to ArrayLists and Deprecate ArrayUtils#append / ArrayUtils#depend
 - [x] Input catching
 - [ ] Implement .help to use the COMMANDS array for its information
 - [x] Per guild prefixing

## Known Issues
**Known bugs or issues, these will get patched in the next minor update.**
 
- [x] GUIGenerator#menu & GUIGenerator#confirmation can take extended amounts of time to register

## Building and Running

**In order to run this project, you need JDK 14 or higher.**

Cloning the project and running a Maven build with the `pom.xml` will build the project into a JAR with all required dependencies.

When running the JAR, JDK 14's `java.exe` must be pointed to (see the example bat).

In order to connect to discord successfully, you must put a valid bot token (see https://discord.com/developers/applications)
in `bot.token` within the `config` YML file, which is auto generated on first launch.

**Example .bat file**

```
@echo off
"C:\Program Files\Java\jdk-14.0.2\bin\java.exe" -jar "your JAR here"
pause
```

## Developer Notes
**Error Handling**

This project implements an error handler which is dependent on
the config option `bot.error` within the `config` YML file, without this set to a valid channel ID, which the bot can write into, 
the handler will default to console only logging.
