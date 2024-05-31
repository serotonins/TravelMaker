package com.ssafy.gumibom.domain.pamphlet.entity;

import com.ssafy.gumibom.domain.record.entity.Record;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
//@NoArgsConstructor
//@AllArgsConstructor
public abstract class Pamphlet {

    @Id @GeneratedValue
    @Column(name = "pamphlet_id")
    private Long id;

    protected String title;
    protected Integer love;
    protected LocalDateTime createTime;

    public abstract void addRecord(Record record);
    public abstract void removeRecord(Record record);
}
