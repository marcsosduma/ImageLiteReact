package br.duma.demo.domain.enums;

import org.springframework.http.MediaType;

import lombok.Getter;

import java.util.Arrays;

public enum ImageExtension {

    PNG(MediaType.IMAGE_PNG),
    GIF(MediaType.IMAGE_GIF),
    JPEG(MediaType.IMAGE_JPEG),
    BMP(MediaType.parseMediaType("image/bmp")),
    WEBP(MediaType.parseMediaType("image/webp")),
    TIFF(MediaType.parseMediaType("image/tiff")),
    SVG(MediaType.parseMediaType("image/svg+xml")),
    ICO(MediaType.parseMediaType("image/x-icon"));

    @Getter
    private final MediaType mediaType;

    ImageExtension(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static ImageExtension from(MediaType mediaType) {
        return Arrays.stream(values())
                .filter(ie -> ie.mediaType.includes(mediaType))
                .findFirst()
                .orElse(null);
    }

    public static ImageExtension ofName(String name){
        return Arrays.stream(values())
                .filter(is -> is.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}