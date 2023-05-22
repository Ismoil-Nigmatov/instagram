package com.example.instagram.dto.youtube;

import java.util.List;
import lombok.Data;

@Data
public class YouTube{
	private List<ThumbnailItem> thumbnail;
	private boolean isLiveContent;
	private List<FormatsItem> formats;
	private List<String> keywords;
	private String lengthSeconds;
	private String description;
	private boolean isPrivate;
	private String title;
	private String expiresInSeconds;
	private List<StoryboardsItem> storyboards;
	private Captions captions;
	private boolean isUnpluggedCorpus;
	private boolean allowRatings;
	private String id;
	private Object viewCount;
	private List<AdaptiveFormatsItem> adaptiveFormats;
	private String channelId;
	private String status;
	private String channelTitle;
}