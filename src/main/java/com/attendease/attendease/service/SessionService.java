package com.attendease.attendease.service;

import com.attendease.attendease.entity.Session;
import com.attendease.attendease.repository.SessionRepository;
import com.attendease.attendease.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SubjectRepository subjectRepository;

    public SessionService(
            SessionRepository sessionRepository,
            SubjectRepository subjectRepository) {
        this.sessionRepository = sessionRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Session not found with ID: " + id));
    }

    public Session createSession(Session session) {
        if (session.getSubject() == null ||
                session.getSubject().getId() == null) {
            throw new RuntimeException("Subject ID is required");
        }

        Long subjectId = session.getSubject().getId();

        var subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found with ID: " + subjectId));

        session.setSubject(subject);

        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, Session updatedSession) {
        Session existingSession = getSessionById(id);

        existingSession.setSessionDate(updatedSession.getSessionDate());
        existingSession.setSessionName(updatedSession.getSessionName());

        return sessionRepository.save(existingSession);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id);
        sessionRepository.delete(session);
    }
}