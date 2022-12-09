package com.example.instagram.dto.tiktokImage;

import java.util.List;
import lombok.Data;

@Data
public class TiktokImage{
	private List<String> cover;
	private List<String> images;
	private List<String> music;
	private String postType;
	private List<String> video;
}