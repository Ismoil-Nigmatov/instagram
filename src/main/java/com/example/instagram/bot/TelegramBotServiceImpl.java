package com.example.instagram.bot;

import com.example.instagram.entity.Info;
import com.example.instagram.entity.User;
import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 7:54 PM on 11/29/2022
 * @project instagram
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotServiceImpl implements TelegramBotService {

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final UserRepository userRepository;

    @Override
    public SendMessage sendMessage(Update update) {
        return SendMessage.builder().text("Hello Dear , This bot was made for you to download a video, image , story from instagram , tiktok (no watermark) . Please send the url").chatId(String.valueOf(update.getMessage().getChatId())).build();
    }

    @Override
    public InputStream sendInstagramVideo(Update update,Info info){

        byte[] video = restTemplate().getForObject(info.getMedia().get(0), byte[].class);

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
        byte[] image = restTemplate().getForObject(info.getMedia().get(0), byte[].class);

        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);
        user.setLastRequest(image);
        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        return new ByteArrayInputStream(image);
    }

    @Override
    public List<MultipartFile> sendInstagramCarousel(Update update, Info info) throws IOException {
        List<String> media = info.getMedia();
        List<MultipartFile> images = new ArrayList<>();
        User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(null);

        for (String s : media) {
            byte[] forObject = restTemplate().getForObject(s, byte[].class);
            user.setLastRequest(forObject);
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(forObject));
            String mimeType = URLConnection.guessContentTypeFromStream(is);

            if (Objects.isNull(mimeType))mimeType="video/mp4";

            MultipartFile multipartFile = new MockMultipartFile("data",s,mimeType,forObject);

            images.add(multipartFile);
        }

        List<Info> infos = user.getInfos();
        infos.add(info);
        user.setInfos(infos);
        userRepository.save(user);

        return images;
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


    @Override
    public InputStream sendYoutubeVideo(Update update, Info info) {

        try {
            URL url = new URL(info.getYoutubeURL());
            URLConnection connection = url.openConnection();

            InputStream inputStream = connection.getInputStream();
            System.out.println(inputStream.available());
            return inputStream;
        }catch (Exception e){
            log.error(e.toString());
        }
        return null;
    }
}