package com.ssafy.gumibom.domain.pamphlet.entity;

import com.ssafy.gumibom.domain.meeting.entity.Meeting;
import com.ssafy.gumibom.domain.record.entity.MeetingRecord;
import com.ssafy.gumibom.domain.record.entity.Record;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "M")
public class MeetingPamphlet extends Pamphlet {

    @OneToOne
    @JoinColumn(name = "meetingPamphlet")
    private Meeting meeting;

    @OneToMany(mappedBy = "meetingPamphlet")
    private List<MeetingRecord> meetingRecords = new ArrayList<>();

    @Override
    public void addRecord(Record record) {
        this.meetingRecords.add((MeetingRecord) record);
    }

    @Override
    public void removeRecord(Record record) {
        this.meetingRecords.remove((MeetingRecord) record);
    }

}
