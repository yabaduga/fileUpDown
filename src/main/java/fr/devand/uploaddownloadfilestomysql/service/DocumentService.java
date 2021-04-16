package fr.devand.uploaddownloadfilestomysql.service;

import fr.devand.uploaddownloadfilestomysql.model.Document;
import fr.devand.uploaddownloadfilestomysql.repository.DocumentRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;


    public List<Document> findAll(){
        return documentRepository.findAll();};
}
