package com.example.instagram.dto.instagram;

import lombok.Data;

import java.util.List;

@Data
public class Response{
	private String thumbnail;
	private String Type;
	private String API;
	private List<String> media;
	private String title;
}