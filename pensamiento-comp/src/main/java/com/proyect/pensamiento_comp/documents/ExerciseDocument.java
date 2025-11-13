package com.proyect.pensamiento_comp.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * MongoDB representation of an Exercise. This document can be used for
 * querying exercises independent of activities or with reverse lookups.
 */
@Document(collection = "exercises")
public class ExerciseDocument {

    @Id
    private String id; // Mongo ObjectId
    
    @Indexed(unique = true)
    private String title;

    @Field("sql_exercise_id")
    private Long relationalId; 
    private String description;
    private String difficulty;
    private String programming_language;

    public ExerciseDocument() {}

    public ExerciseDocument(Long relationalId, String title, String description, String difficulty, String programming_language) {
        this.relationalId = relationalId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.programming_language = programming_language;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getRelationalId() { return relationalId; }
    public void setRelationalId(Long relationalId) { this.relationalId = relationalId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getProgramming_language() { return programming_language; }
    public void setProgramming_language(String programming_language) { this.programming_language = programming_language; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
