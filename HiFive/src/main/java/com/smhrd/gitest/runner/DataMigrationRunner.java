package com.smhrd.gitest.runner;

import com.smhrd.gitest.service.DataMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 시작 시 데이터 마이그레이션 로직을 딱 한 번 실행하는 클래스입니다.
 * 데이터 마이그레이션이 완료된 후에는 이 클래스를 삭제하거나 @Component를 주석처리하세요.
 */
//@Component // ★★★ 1. (핵심!) 주석을 해제하여, 이 일꾼을 다시 "고용"합니다. ★★★
@RequiredArgsConstructor
public class DataMigrationRunner implements CommandLineRunner {

    private final DataMigrationService dataMigrationService;

    @Override
    public void run(String... args) throws Exception {
        // 기존 '법정동 채우기' 작업은 이제 필요 없으므로 주석 처리합니다.
        // dataMigrationService.migrateLegalDongData();
        
        // ★★★ 2. (핵심!) 우리가 진짜 원하는 '가게-태그 관계 만들기' 작업을 지시합니다. ★★★
        dataMigrationService.migrateStoreTags();
    }
}