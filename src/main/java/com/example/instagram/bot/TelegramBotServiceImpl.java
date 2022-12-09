package com.example.instagram.bot;

import com.example.instagram.dto.instagram.Response;
import com.example.instagram.entity.*;
import com.example.instagram.repository.InfoRepository;
import com.example.instagram.repository.UserRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 7:54 PM on 11/29/2022
 * @project instagram
 */
@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final UserRepository userRepository;

    private final InfoRepository infoRepository;

    @Override
    public SendMessage sendMessage(Update update) {
        return SendMessage.builder().text("Hello Dear , This bot was made for you to download a video, image , story from instagram , tiktok (no watermark) . Please send the url").chatId(String.valueOf(update.getMessage().getChatId())).build();
    }

    @Override
    public InputStream sendInstagramVideo(Update update,Info info){

        byte[] video = restTemplate().getForObject(info.getMedia(), byte[].class);

        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);
        user.setLastRequest(video);
        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        return new ByteArrayInputStream(video);
    } //for reels , one video and story

    @Override
    public InputStream sendInstagramImage(Update update, Info info) {
        byte[] image = restTemplate().getForObject(info.getMedia(), byte[].class);

        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);
        user.setLastRequest(image);
        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        return new ByteArrayInputStream(image);
    }

    //////////////////////////////////////////////////////////////////////INSTAGRAM

    @Override
    public InputStream sendTiktokVideo(Update update,Info info){

        byte[] video = restTemplate().getForObject(info.getVideo(), byte[].class);

        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);
        user.setLastRequest(video);
        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        return new ByteArrayInputStream(video);
    }

    @Override
    public List<InputMedia> sendTiktokImage(Update update, Info info) {
        List<byte[]> imageList=new ArrayList<>();

        List<String> images = info.getImages();

        List<InputStream> inputStreams=new ArrayList<>();

        List<InputMedia> inputMediaList=new ArrayList<>();

        for (String image : images) {
            byte[] video = restTemplate().getForObject(image, byte[].class);
            imageList.add(video);
        }

        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);
        user.setLastRequest(imageList.get(imageList.size()-1));
        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        for (byte[] video : imageList) {
            InputStream inputStream=new ByteArrayInputStream(video);
            inputStreams.add(inputStream);
        }

        for (InputStream inputStream : inputStreams) {
            InputMedia inputMedia=new InputMediaPhoto();
            inputMedia.setMedia(inputStream,"photo "+inputStream);
            inputMediaList.add(inputMedia);
        }
        return inputMediaList;
    }

    //////////////////////////////////////////////////////////////////////TIKTOK
}