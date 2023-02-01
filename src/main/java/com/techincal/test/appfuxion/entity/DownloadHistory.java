package com.techincal.test.appfuxion.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name="DOWNLOAD_HISTORY")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadHistory {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="FILE_NAME")
    private String fileName;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Lob
    @Column(name="DATA_FILE")
    private byte[] dataFile;

}
