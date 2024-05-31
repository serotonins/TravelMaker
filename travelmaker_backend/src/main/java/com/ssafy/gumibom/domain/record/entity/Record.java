package com.ssafy.gumibom.domain.record.entity;

import com.ssafy.gumibom.domain.pamphlet.entity.Pamphlet;
import com.ssafy.gumibom.global.common.Emoji;
import com.ssafy.gumibom.global.util.EmojiConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor
public abstract class Record {

    @Id @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    private String title;
    private LocalDateTime createTime;
    private String imgUrl;
    private String videoUrl;
    private String videoThumbnailUrl;
    private String text;

    @Convert(converter = EmojiConverter.class)
    private Emoji emoji;

    protected void setRecord(String title, String imgUrl, String videoUrl, String videoThumbnailUrl, String text, Emoji emoji) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.videoUrl = videoUrl;
        this.videoThumbnailUrl = videoThumbnailUrl;
        this.text = text;
        this.createTime = LocalDateTime.now();
        this.emoji = emoji;
    }

    protected abstract void setPamphlet(Pamphlet pamphlet);
}
