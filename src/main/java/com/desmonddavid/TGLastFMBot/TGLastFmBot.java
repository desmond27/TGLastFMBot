package com.desmonddavid.TGLastFMBot;

import com.desmonddavid.lfm4j.Lfm4J;
import com.desmonddavid.lfm4j.common.utils.ClientType;
import com.desmonddavid.lfm4j.track.response.Track;
import com.desmonddavid.lfm4j.user.LfmUserService;
import com.desmonddavid.lfm4j.user.response.recentTracks.RecentTracks;
import com.desmonddavid.lfm4j.user.response.userInfo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class TGLastFmBot extends AbilityBot {

    private static final String BOT_TOKEN = ApplicationProperties.TELEGRAM_BOT_TOKEN;
    private static final String BOT_USERNAME = ApplicationProperties.TELEGRAM_BOT_NAME;

    private static final Logger logger = LoggerFactory.getLogger(TGLastFmBot.class);

    public static void main(String[] args) {

        // Init LFM4J
        Lfm4J.init(ApplicationProperties.LAST_FM_API_KEY, ApplicationProperties.LAST_FM_API_SECRET, ClientType.DESKTOP);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TGLastFmBot());
        } catch (TelegramApiException e) {
            logger.error("An exception occurred when initializing the Telegram bot API.", e);
        }
    }

    public TGLastFmBot() {
        super(BOT_TOKEN, BOT_USERNAME);
    }

    public Ability getUserDetails() {
        return Ability
                .builder()
                .name("user")
                .info("Gets user's Last FM info.")
                .input(1)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String lastFmUsername = ctx.arguments()[0];
                    logger.info("Getting last fm details for user: " + lastFmUsername);
                    User user = LfmUserService.getInfo(lastFmUsername);
                    String message = "Info for user "+lastFmUsername+
                            "\nProfile URL: "+user.getUrl()+
                            "\nPlaycount: "+user.getPlaycount()+
                            "\nCountry: "+user.getCountry();
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability getUserNowPlaying() {
        return Ability
                .builder()
                .name("now")
                .info("Gets user's currently playing or last played track.")
                .input(1)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    String lastFmUsername = ctx.arguments()[0];
                    logger.info("Getting last played details for user: " + lastFmUsername);
                    RecentTracks recentTracks = LfmUserService.getRecentTracks(lastFmUsername, 1, 1, null, null, null);
                    List<Track> trackList = recentTracks.getTracks();
                    Track currentTrack = trackList.get(0);
                    String message;
                    String imageUrl = currentTrack.getImages().stream().filter(i -> i.getSize().equals("extralarge")).findAny().get().getText();
                    if(currentTrack.getAttributes() != null && currentTrack.getAttributes().get("nowplaying").equals("true")) {
                        message = "*"+lastFmUsername+"* is listening to";
                    }
                    else {
                        message = "*"+lastFmUsername+"* was listening to";
                    }
                    //\n[]("+imageUrl+")
                    message += "\n\uD83C\uDFB5 "+currentTrack.getName()+
                            "\n\uD83D\uDC65 "+currentTrack.getArtist().getText()+
                            "\n\uD83D\uDCC0 "+currentTrack.getAlbum().getText()+
                            "\n[\u200B]("+imageUrl+")";
                    logger.info("Message to be sent: "+message);
                    silent.sendMd(message, ctx.chatId());
                })
                .build();
    }

    @Override
    public long creatorId() {
        return ApplicationProperties.TELEGRAM_CREATOR_ID;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void onClosing() {
        super.onClosing();
    }
}
