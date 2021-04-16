package fr.devand.uploaddownloadfilestomysql;

import fr.devand.uploaddownloadfilestomysql.model.Document;
import fr.devand.uploaddownloadfilestomysql.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UploaddownloadfilestomysqlApplicationTests {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Rollback(false)
    void testInserDocument() throws IOException {
        File file = new File("E:\\_docplatetest\\scrumchecklist.pdf");
        Document document= new Document();
        document.setName(file.getName());

        byte[] bytes=Files.readAllBytes(file.toPath());
        document.setContent(bytes);
        long fileSize= bytes.length;
        document.setSize(fileSize);
        document.setUploadTime(new Date());

        Document savedDoc = documentRepository.save(document);

        Document existingDoc = testEntityManager.find(Document.class, savedDoc.getId());

        assertThat(existingDoc.getSize()).isEqualTo(fileSize);

    }

}
