package fr.devand.uploaddownloadfilestomysql.repository;


import fr.devand.uploaddownloadfilestomysql.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
