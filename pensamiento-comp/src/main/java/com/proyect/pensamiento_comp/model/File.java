package com.proyect.pensamiento_comp.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DEPRECATED: This JPA entity is not used anymore.
 * File references are now stored in MongoDB within submissions.
 * Keeping this class for reference only.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity  // COMMENTED OUT - File is now in MongoDB only
// @Table(name = "FILE")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", sequenceName = "FILE_SEQ", allocationSize = 1)
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private Date uploadDate;
    
    @ManyToOne
    @JoinColumn(name = "SUBMISSION_id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_id")
    private Activity activity;

}
