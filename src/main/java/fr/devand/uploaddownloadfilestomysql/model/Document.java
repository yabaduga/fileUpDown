package fr.devand.uploaddownloadfilestomysql.model;


import jdk.jfr.DataAmount;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 512, nullable = false, unique = true)
    private String name;

    private long size;

    @Column(name="upload_time")
    private Date uploadTime;

    private String path;

    private byte[] content;

    public Document(long id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }
}
