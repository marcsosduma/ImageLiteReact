package br.duma.demo.domain.service;

import java.util.List;
import java.util.Optional;

import br.duma.demo.domain.entity.Image;
import br.duma.demo.domain.enums.ImageExtension;

public interface ImageService {

    Image save(Image image);
    Optional<Image> getById(String id);    
    List<Image> search(ImageExtension extension, String query);
}
