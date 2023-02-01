package com.techincal.test.appfuxion.repository;

import com.techincal.test.appfuxion.entity.DownloadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DownloadHistoryRepository extends JpaRepository<DownloadHistory, Long> {


    @Query(value = "SELECT ID, FILE_NAME, CREATED_DATE, DATA_FILE FROM DOWNLOAD_HISTORY", nativeQuery = true)
    List<DownloadHistory> getAllData();

    @Query(value="SELECT ID, FILE_NAME, CREATED_DATE, DATA_FILE FROM DOWNLOAD_HISTORY " +
            "WHERE ID = :id", nativeQuery = true)
    DownloadHistory getDataById(@Param("id") Long id);

    @Query(value="DELETE FROM DOWNLOAD_HISTORY WHERE ID = :id", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteById(@Param("id") Long id);

}
