package br.duma.demo.infra.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import br.duma.demo.domain.entity.Image;
import br.duma.demo.domain.enums.ImageExtension;
import br.duma.demo.infra.repository.specs.ImageSpecs;

import static br.duma.demo.infra.repository.specs.ImageSpecs.*;
import static org.springframework.data.jpa.domain.Specification.anyOf;
import static org.springframework.data.jpa.domain.Specification.where;
import static br.duma.demo.infra.repository.specs.GenericSpecs.*;

public interface ImageRepository extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image>{

    default List<Image> findByExtensionAndNameOrTagsLike(ImageExtension extension, String query){
        Specification<Image> conjunction = where(conjunction());
        Specification<Image> spec = Specification.where( conjunction );

        if(extension != null){
            spec = spec.and(ImageSpecs.extensionEqual(extension));
        }
        if(StringUtils.hasText(query)){
            Specification<Image> nameLike = nameLike(query);
            Specification<Image> tagLike  = tagsLike(query);
            Specification<Image> nameOrTagsLike = anyOf(nameLike, tagLike);
            spec = spec.and(nameOrTagsLike);
        }
        return findAll(spec);        
    }
}
