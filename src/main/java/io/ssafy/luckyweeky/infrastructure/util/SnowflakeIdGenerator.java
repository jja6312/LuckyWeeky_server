package io.ssafy.luckyweeky.infrastructure.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.ssafy.luckyweeky.dispatcher.DispatcherServlet;

import java.io.File;

public class SnowflakeIdGenerator {
    private static SnowflakeIdGenerator instance; // 싱글톤 인스턴스

    private final long epoch = 1609459200000L; // 기준 시간 (2021-01-01 00:00:00 UTC)
    private final long machineIdBits = 10L;   // 머신 ID 비트 수
    private final long maxMachineId = (1L << machineIdBits) - 1; // 최대 머신 ID (1023)
    private final long sequenceBits = 12L;   // 시퀀스 번호 비트 수
    private final long sequenceMask = (1L << sequenceBits) - 1; // 최대 시퀀스 번호 (4095)

    private final long machineIdShift = sequenceBits;
    private final long timestampShift = sequenceBits + machineIdBits;

    private final long machineId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // private 생성자
    private SnowflakeIdGenerator() {
        // 1. .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .directory(DispatcherServlet.getWebInfPath()+ File.separatorChar)
                .filename(".env") // 파일 이름 지정 (기본값: ".env")
                .load();
        String machineIdStr = dotenv.get("MACHINE_ID");
        if (machineIdStr == null) {
            throw new IllegalArgumentException("Machine ID not set");
        }
        this.machineId = Long.parseLong(machineIdStr);
    }

    // 싱글톤 인스턴스 반환 메서드
    public static synchronized SnowflakeIdGenerator getInstance() {
        if (instance == null) {
            instance = new SnowflakeIdGenerator();
        }
        return instance;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            // 같은 밀리초에 요청이 들어오면 시퀀스 번호 증가
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 시퀀스 번호가 초과되면 다음 밀리초로 이동
                currentTimestamp = waitUntilNextMillis(currentTimestamp);
            }
        } else {
            // 새로운 밀리초에서는 시퀀스 번호 초기화
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        // Snowflake ID 생성
        return ((currentTimestamp - epoch) << timestampShift) |
                (machineId << machineIdShift) |
                sequence;
    }

    private long waitUntilNextMillis(long currentMillis) {
        while (currentMillis <= lastTimestamp) {
            currentMillis = System.currentTimeMillis();
        }
        return currentMillis;
    }
}
