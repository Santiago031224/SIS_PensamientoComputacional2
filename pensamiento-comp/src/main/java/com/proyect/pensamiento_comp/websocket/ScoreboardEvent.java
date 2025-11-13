package com.proyect.pensamiento_comp.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event object sent through WebSocket to update scoreboard in real-time
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreboardEvent {
    
    /**
     * Type of event
     */
    private EventType eventType;
    
    /**
     * Student ID affected by the event
     */
    private Long studentId;
    
    /**
     * Student name
     */
    private String studentName;
    
    /**
     * Points awarded in this event (can be null for non-score events)
     */
    private Double pointsAwarded;
    
    /**
     * Total accumulated points for the student
     */
    private Double totalPoints;
    
    /**
     * Activity ID related to the event (if applicable)
     */
    private Long activityId;
    
    /**
     * Activity title
     */
    private String activityTitle;
    
    /**
     * Group ID
     */
    private Long groupId;
    
    /**
     * Timestamp when the event occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * Additional message or description
     */
    private String message;
    
    /**
     * Types of scoreboard events
     */
    public enum EventType {
        SCORE_UPDATED,      // When a score is assigned or updated
        SUBMISSION_GRADED,  // When a submission is graded
        ACTIVITY_STARTED,   // When an activity starts
        ACTIVITY_ENDED,     // When an activity ends
        STUDENT_RANKED,     // When student ranking changes
        CODE_REDEEMED,      // When a point code is redeemed
        GENERAL_UPDATE      // General scoreboard refresh
    }
    
    /**
     * Creates a score update event
     */
    public static ScoreboardEvent scoreUpdated(Long studentId, String studentName, 
                                                Double pointsAwarded, Double totalPoints, 
                                                Long activityId, String activityTitle, 
                                                Long groupId) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.SCORE_UPDATED);
        event.setStudentId(studentId);
        event.setStudentName(studentName);
        event.setPointsAwarded(pointsAwarded);
        event.setTotalPoints(totalPoints);
        event.setActivityId(activityId);
        event.setActivityTitle(activityTitle);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(String.format("%s earned %.1f points", studentName, pointsAwarded));
        return event;
    }
    
    /**
     * Creates a submission graded event
     */
    public static ScoreboardEvent submissionGraded(Long studentId, String studentName, 
                                                    Double pointsAwarded, Double totalPoints, 
                                                    Long activityId, String activityTitle, 
                                                    Long groupId) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.SUBMISSION_GRADED);
        event.setStudentId(studentId);
        event.setStudentName(studentName);
        event.setPointsAwarded(pointsAwarded);
        event.setTotalPoints(totalPoints);
        event.setActivityId(activityId);
        event.setActivityTitle(activityTitle);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(String.format("Submission graded: %s received %.1f points", studentName, pointsAwarded));
        return event;
    }
    
    /**
     * Creates a code redeemed event
     */
    public static ScoreboardEvent codeRedeemed(Long studentId, String studentName, 
                                                Double pointsAwarded, Double totalPoints, 
                                                Long activityId, String activityTitle, 
                                                Long groupId) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.CODE_REDEEMED);
        event.setStudentId(studentId);
        event.setStudentName(studentName);
        event.setPointsAwarded(pointsAwarded);
        event.setTotalPoints(totalPoints);
        event.setActivityId(activityId);
        event.setActivityTitle(activityTitle);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(String.format("%s redeemed code for %.1f points", studentName, pointsAwarded));
        return event;
    }
    
    /**
     * Creates a general update event
     */
    public static ScoreboardEvent generalUpdate(Long groupId, String message) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.GENERAL_UPDATE);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(message);
        return event;
    }
    
    /**
     * Creates an activity started event
     */
    public static ScoreboardEvent activityStarted(Long activityId, String activityTitle, Long groupId) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.ACTIVITY_STARTED);
        event.setActivityId(activityId);
        event.setActivityTitle(activityTitle);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(String.format("Activity started: %s", activityTitle));
        return event;
    }
    
    /**
     * Creates an activity ended event
     */
    public static ScoreboardEvent activityEnded(Long activityId, String activityTitle, Long groupId) {
        ScoreboardEvent event = new ScoreboardEvent();
        event.setEventType(EventType.ACTIVITY_ENDED);
        event.setActivityId(activityId);
        event.setActivityTitle(activityTitle);
        event.setGroupId(groupId);
        event.setTimestamp(LocalDateTime.now());
        event.setMessage(String.format("Activity ended: %s", activityTitle));
        return event;
    }
}
