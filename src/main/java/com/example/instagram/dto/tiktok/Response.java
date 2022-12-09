package com.example.instagram.dto.tiktok;

import java.util.List;
import lombok.Data;

@Data
public class Response{
	private List<String> cover;
	private List<String> music;
	private List<String> video;
}