package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) String key) {

        Collection participants;

        if (sortBy != null) {
            participants = participantService.getAll(sortBy, sortOrder, key);
        } else if (key != null) {
            participants = participantService.getAll(key);
        } else {
            participants = participantService.getAll();
        }

        return new ResponseEntity<>(participants, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(participant, HttpStatus.OK);
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        Participant foundParticipant = participantService.findByLogin(participant.getLogin());
        if (foundParticipant != null) {
            return new ResponseEntity<>("Already exist", HttpStatus.CONFLICT);
        }

        participantService.add(participant);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login){
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity<>("Participant don't exist",HttpStatus.NOT_FOUND);
        }

        participantService.delete(participant);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipant(
            @PathVariable("id") String login,
            @RequestBody Participant participant){

        Participant foundParticipant = participantService.findByLogin(login);

        if (foundParticipant == null) {

            return new ResponseEntity<>("Participant don't exist",HttpStatus.NOT_FOUND);
        }


        participantService.update(foundParticipant, participant.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}