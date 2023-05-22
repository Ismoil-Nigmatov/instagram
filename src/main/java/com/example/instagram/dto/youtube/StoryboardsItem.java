package com.example.instagram.dto.youtube;

import java.util.List;
import lombok.Data;

@Data
public class StoryboardsItem{
	private String thumbsCount;
	private int storyboardCount;
	private String columns;
	private String width;
	private String interval;
	private String rows;
	private List<String> url;
	private String height;
}