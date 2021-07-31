# TGLastFMBot
A simple Telegram bot that fetches information from Last FM.

This project uses the [TelegramBots](https://github.com/rubenlagus/TelegramBots) library and my own Last FM wrapper library [lfm4j](https://github.com/desmond27/lfm4j).

The lfm4j library mentioned above is NOT in the Maven repository and therefore you will have to clone it and run `mvn install` to get this project to find it.

Also, before building, edit the application.properties file in the resources directory and add the following details:

|Property name |Description |
|--- |--- |
|TELEGRAM_BOT_TOKEN| Get from [BotFather](https://telegram.im/BotFather) when creating your bot in Telegram. |
|TELEGRAM_BOT_NAME| Whatever you set in BotFather after creating your bot. |
|TELEGRAM_CREATOR_ID| Your Telegram id (not handle). You get get it from [userinfobot](https://t.me/userinfobot). |
|LAST_FM_API_KEY| Your Last FM API key. Get it after creating an API account [here](https://www.last.fm/api/account/create). |
|LAST_FM_API_SECRET| Same as the API key above. |
