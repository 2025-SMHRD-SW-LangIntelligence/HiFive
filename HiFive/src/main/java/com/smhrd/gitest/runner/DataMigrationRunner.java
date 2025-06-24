package com.smhrd.gitest.runner;

import com.smhrd.gitest.service.DataMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 시작 시 데이터 마이그레이션 로직을 딱 한 번 실행하는 클래스입니다.
 * 데이터 마이그레이션이 완료된 후에는 이 클래스를 삭제하거나 @Component를 주석처리하세요.
 */
//@Component // 이 클래스를 스프링이 관리하는 컴포넌트로 등록
@RequiredArgsConstructor
public class DataMigrationRunner implements CommandLineRunner {

    private final DataMigrationService dataMigrationService;

    @Override
    public void run(String... args) throws Exception {
        // 애플리케이션이 시작되면 이 부분이 자동으로 실행됩니다.
        dataMigrationService.migrateLegalDongData();
    }
}