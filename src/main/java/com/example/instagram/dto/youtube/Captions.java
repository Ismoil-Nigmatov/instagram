package com.example.instagram.dto.youtube;

import java.util.List;
import lombok.Data;

@Data
public class Captions{
	private List<CaptionTracksItem> captionTracks;
	private List<TranslationLanguagesItem> translationLanguages;
}