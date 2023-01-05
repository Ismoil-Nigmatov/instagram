package com.example.instagram.bot;

import com.example.instagram.dto.instagram.Response;
import com.example.instagram.dto.tiktokImage.TiktokImage;
import com.example.instagram.entity.Info;
import com.example.instagram.entity.User;
import com.example.instagram.repository.InfoRepository;
import com.example.instagram.repository.UserRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 7:47 PM on 11/29/2022
 * @project instagram
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;

    private final InfoRepository infoRepository;
    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (userRepository.existsById(String.valueOf(update.getMessage().getChatId()))) {
            User user = userRepository.findById(String.valueOf(update.getMessage().getChatId())).orElseThrow(RuntimeException::new);
            if (user.getQuota() != 0L) {
                Long quota = user.getQuota();
                user.setQuota(quota - 1);
                userRepository.save(user);
            } else {
                execute(SendMessage.builder().text("You have no quotas left, in order to continue using the bot, please buy quotas. You can buy here @ismoil_10_60").chatId(String.valueOf(update.getMessage().getChatId())).build());
                return;
            }
        }//state of checking quotas
        else {
            User user = new User();
            user.setChatId(String.valueOf(update.getMessage().getChatId()));
            user.setFirstName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setUserName(update.getMessage().getFrom().getUserName());
            userRepository.save(user);

        }

        if (update.hasMessage()) {
            if (update.getMessage().getText().equals("/start")) {
                execute(telegramBotService.sendMessage(update));
            }

            if ((update.getMessage().getText().startsWith("https://www.instagram.com/")) || (update.getMessage().getText().startsWith("https://instagram.com"))) {

                try {
                    execute(SendMessage.builder().text("Wait.Please...").chatId(String.valueOf(update.getMessage().getChatId())).build());

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://instagram-downloader-download-instagram-videos-stories.p.rapidapi.com/index?url=" + update.getMessage().getText()))
                            .header("X-RapidAPI-Key", "8196fbbdfamsh6929d53aa1ecf11p13dd2ejsn2599b79bab13")
                            .header("X-RapidAPI-Host", "instagram-downloader-download-instagram-videos-stories.p.rapidapi.com")
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();

                    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                    String body = response.body();


                    if (body.contains("[")){
                        Response instagram = new Gson().fromJson(body, com.example.instagram.dto.instagram.Response.class);

                        Info info=new Info();
                        info.setMedia(instagram.getMedia());
                        infoRepository.save(info);

                        List<MultipartFile> multipartFiles = telegramBotService.sendInstagramCarousel(update, info);
                        List<InputMedia> inputMediaList=new ArrayList<>();

                        for (MultipartFile multipartFile : multipartFiles) {
                            if (multipartFile.getContentType().contains("video")){
                                InputMedia inputMedia=new InputMediaVideo();
                                InputStream inputStream = multipartFile.getInputStream();
                                inputMedia.setMedia(inputStream,multipartFile.getOriginalFilename());
                                inputMediaList.add(inputMedia);
                            }else {
                                InputMedia inputMedia=new InputMediaPhoto();
                                InputStream inputStream= multipartFile.getInputStream();
                                inputMedia.setMedia(inputStream,multipartFile.getOriginalFilename());
                                inputMediaList.add(inputMedia);
                            }
                        }

                        execute(SendMediaGroup.builder().medias(inputMediaList).chatId(String.valueOf(update.getMessage().getChatId())).build());
                    }
                    else {
                        com.example.instagram.dto.instagramImage.Response instagram = new Gson().fromJson(body,com.example.instagram.dto.instagramImage.Response.class);

                        Info info = new Info();
                        info.setMedia(List.of(instagram.getMedia()));
                        infoRepository.save(info);

                        if (instagram.getType().equals("Post-Image")) {
                            InputStream inputStream = telegramBotService.sendInstagramImage(update, info);
                            execute(SendPhoto.builder().chatId(String.valueOf(update.getMessage().getChatId())).photo(new InputFile(inputStream, "photo")).build());
                        }
                        else if ((instagram.getType().equals("Post-Video"))) {
                            InputStream inputStream = telegramBotService.sendInstagramVideo(update, info);
                            execute(SendVideo.builder().chatId(String.valueOf(update.getMessage().getChatId())).video(new InputFile(inputStream, "video")).build());
                        }
                        else if (instagram.getType().equals("Story-Video")) {
                            InputStream inputStream = telegramBotService.sendInstagramVideo(update, info);
                            execute(SendVideo.builder().chatId(String.valueOf(update.getMessage().getChatId())).video(new InputFile(inputStream, "story")).build());
                        }
                        else execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId())).text("For now we can't download it ").build());
                    }
                }catch (Exception e){
                    log.error(String.valueOf(e));
                    e.printStackTrace();
                    execute(SendMessage.builder().chatId(String.valueOf(update.getMessage().getChatId())).text("Failed to download").build());
                }//video

            }

            if (update.getMessage().getText().startsWith("https://vt.tiktok.com/")||(update.getMessage().getText().startsWith("https://www.tiktok.com/"))) {

                execute(SendMessage.builder().text("Wait, Please...").chatId(String.valueOf(update.getMessage().getChatId())).build());

                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/index?url="+update.getMessage().getText()))
                            .header("X-RapidAPI-Key", "8196fbbdfamsh6929d53aa1ecf11p13dd2ejsn2599b79bab13")
                            .header("X-RapidAPI-Host", "tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com")
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    String body = response.body();

                    if (body.contains("images")){
                        TiktokImage tiktokImage=new Gson().fromJson(body,TiktokImage.class);

                        Info info=new Info();
                        info.setImages(tiktokImage.getImages());
                        infoRepository.save(info);

                        List<InputMedia> inputStreams = telegramBotService.sendTiktokImage(update, info);

                        execute(SendMediaGroup.builder().chatId(String.valueOf(update.getMessage().getChatId())).medias(inputStreams).build());
                    }else {
                        com.example.instagram.dto.tiktok.Response tiktok = new Gson().fromJson(body, com.example.instagram.dto.tiktok.Response.class);

                        Info info = new Info();
                        info.setVideo(tiktok.getVideo().get(0));
                        infoRepository.save(info);

                        InputStream inputStream = telegramBotService.sendTiktokVideo(update, info);
                        execute(SendVideo.builder().video(new InputFile(inputStream, "video")).chatId(String.valueOf(update.getMessage().getChatId())).build());
                    }
                } catch (Exception e) {
                    log.error(String.valueOf(e));
                    execute(SendMessage.builder().text("Failed to download video").chatId(String.valueOf(update.getMessage().getChatId())).build());
                }//video
            }
        }
    }
}