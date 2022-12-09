package com.example.instagram.bot;

import com.example.instagram.entity.Info;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 7:52 PM on 11/29/2022
 * @project instagram
 */

@Service
public interface TelegramBotService {
    SendMessage sendMessage(Update update);

    InputStream sendInstagramVideo(Update update,Info info);

    InputStream sendInstagramImage(Update update,Info info);

    //////////////////////////////////////////////////////////////////////INSTAGRAM

    InputStream sendTiktokVideo(Update update, Info info);

    List<InputMedia> sendTiktokImage(Update update, Info info);

    //////////////////////////////////////////////////////////////////////TIKTOK
}