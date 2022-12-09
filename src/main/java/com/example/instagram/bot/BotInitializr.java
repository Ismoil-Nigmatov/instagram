package com.example.instagram.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 7:48 PM on 11/30/2022
 * @project botService
 */
@Component
@RequiredArgsConstructor
public class BotInitializr {
    private final TelegramBot fastFoodBot;

    @EventListener(ContextRefreshedEvent.class)
    public void init()throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(fastFoodBot);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
