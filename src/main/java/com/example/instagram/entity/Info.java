package com.example.instagram.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 8:23 PM on 11/30/2022
 * @project instagram
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Info {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> media;

    @Column(length = 10000)
    private String video;

    @Column(length = 10000)
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<String> images;

    @Column(length = 10000)
    private String youtubeURL;
}
