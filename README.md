# IGSQBot Developer Edition
[![See latest version](https://img.shields.io/badge/download-0.0.1-blue)](https://github.com/IGSQ/IGSQ-Bot/releases)
[![GitHub license](https://img.shields.io/badge/license-GNU%20AGP-lightgrey)](https://github.com/IGSQ/IGSQ-Bot/tree/Dev/LICENSE)

## TODO
**Features that are still to be implemented**

### **COMPLETED**
 - [x] Per guild prefixing
 - [x] Project Refactor
 - [x] Move from Arrays to ArrayLists and Deprecate ArrayUtils#append / ArrayUtils#depend
 - [x] Input catching on commands
 - [x] Logging expansion (log voice channel stuff) (completed primitive)
 - [x] Change all command invokes to more proper names
 - [x] Change EmbedUtils to new prefix system (removed prefixes from errors)
 - [x] Move to InterfacedEventManager as to run event processing async
 - [x] Change cache to ExpiringMap(MessageID, Message)
 - [x] GuildConfigCache#close - unneeded
 - [x] Remove EventWaiter where possible in favour of MessageDataCache
 - [x] Move all events.commands to MessageDataCache
 - [x] Kill the callback hell
 - [x] Rework MessageCache to use hashmaps + refactor
 - [x] Remove raw Yaml links in place of caches / methods
 - [x] Fully implement the logger - needs review
 - [x] GUIGenerator further implementation (use callbacks / catch exceptions)
 - [x] Catch 2FA DMs
 - [x] Change CooldownHandler to use Strings vs Longs
 - [x] ReactionRoles
 - [x] Setters for GuildConfig
 - [x] Emote constants, success, failure
 - [x] Check verified role in LinkCommand
 - [x] Class#wipe for config classes - removes all config options
 - [x] Remove raw Database calls (Marked with TODO)
 - [x] Add Database#isOnline
 - [x] Catch empty bot.server option in SyncMinecraft
 - [x] Use MessageChannel where possible - only remaining on in CommandHandler (Required)
 - [x] Custom message class (also added user class)
 - [x] OnGuildRemove removing guild config (+ unavailableGuild)
 - [x] Filename Enum
 - [x] Check required tables in testDatabase
 - [x] Invite command
 - [x] Refactor events to not include 'Event'

### **TeaEyes**
 - [ ] Help / Modhelp - added functionality, still needs content
 - [ ] Implement .help to use the COMMANDS array for its information
 - [ ] Alias / Decline (within the setup command)

### **arynxd**
 - [ ] Finish verification

 - [ ] Redo Channel / Role / User parsing
 - [ ] ReactionRole#clear for a message
 
 - [ ] Info command
 - [ ] Muting
   
 - [ ] PREPARE FOR PR

### **Future work**
 - [ ] Rewrite the event waiter
 - [ ] Translation
 - [ ] XP system
 - [ ] Implement slash commands when they release
 - [ ] Attempt to make my own ExpiringMap
 - [ ] Complex args parser


## Known Issues
**Known bugs or issues, these will get patched in the next minor update.**
 
 - [x] GUIGenerator#menu & GUIGenerator#confirmation can take extended amounts of time to register (fixed but untested)
 - [x] discord_accounts table returning errors - fixed with additional error catching
 - [x] 2FA not functioning as intended - unintended feature

## Building and Running

**In order to run this project, you need JDK 14 or higher.**

Cloning the project and running a Maven build with the `pom.xml` will build the project into a JAR with all required dependencies.

When running the JAR, JDK 14's `java.exe` must be pointed to (see the example bat).

In order to connect to discord successfully, you must put a valid bot token (see https://discord.com/developers/applications)
in `bot.token` within the `config` YML file, which is automatically generated on first launch.

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
