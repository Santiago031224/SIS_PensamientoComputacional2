package com.proyect.documents;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.proyect.pensamiento_comp.documents.ActivityDocument;
import com.proyect.pensamiento_comp.documents.repository.ActivityDocumentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = com.proyect.pensamiento_comp.PensamientoCompApplication.class)
@TestPropertySource(properties = {
        "app.mongo.enabled=true"
})
@Disabled("Requires running MongoDB instance; enable locally by starting Mongo and removing @Disabled")
class ActivityDocumentRepositoryTest {

    @Autowired
    private ActivityDocumentRepository repository;

    @Test
    void saveAndFindByRelationalId() {
        ActivityDocument doc = new ActivityDocument();
        doc.setRelationalId(1L);
        doc.setTitle("Title");
        doc.setDescription("Desc");
        doc.setProfessorId(10L);
        doc.setExercises(List.of(new ActivityDocument.ExerciseEmbed(5L, "Exercise title", "Exercise desc", "MEDIUM", 1, 10)));
        repository.save(doc);

        ActivityDocument fetched = repository.findByRelationalId(1L).orElse(null);
        assertThat(fetched).isNotNull();
        assertThat(fetched.getProfessorId()).isEqualTo(10L);
        assertThat(fetched.getExercises()).hasSize(1);
        //assertThat(fetched.getExercises().get(0).getExerciseId()).isEqualTo(5L);
    }
}
