package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        meetingService.add(meeting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") Long id, @RequestBody Meeting meeting) {
        Meeting existing = meetingService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>("Meeting does not exist", HttpStatus.NOT_FOUND);
        }

        existing.setTitle(meeting.getTitle());
        existing.setDate(meeting.getDate());

        meetingService.update(existing);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
        Meeting existing = meetingService.findById(id);
        if (existing == null) {
            return new ResponseEntity<>("Meeting does not exist", HttpStatus.NOT_FOUND);
        }

        meetingService.delete(existing);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}