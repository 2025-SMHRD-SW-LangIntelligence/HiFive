package com.smhrd.gitest.runner;

import com.smhrd.gitest.service.DataMigrationService; // ★★★ DataMigrationService import
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 시작 시 데이터 마이그레이션 로직을 딱 한 번 실행하는 클래스입니다.
 * 데이터 마이그레이션이 완료된 후에는 이 클래스를 삭제하거나 @Component를 주석처리하세요.
 */
//@Component
@RequiredArgsConstructor
public class DataMigrationRunner implements CommandLineRunner {

    private final DataMigrationService dataMigrationService;

    @Override
    public void run(String... args) throws Exception {
        // 기존 작업들은 모두 주석 처리합니다.
        // dataMigrationService.migrateCoordinates();
        // dataMigrationService.migrateLegalDongData();
        
        // ★★★ 새로운 가게-태그 마이그레이션 작업을 호출합니다! ★★★
        dataMigrationService.migrateStoreTags();
    }
}