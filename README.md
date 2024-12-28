# LuckyWeeky

---
![image.png](image.png)

> **삼성 청년 소프트웨어 아카데미 12기**  
> 개발기간 : 3주 (2024.11.04 ~ 11.26)

---

## 배포주소
[https://luckyweeky.store](https://luckyweeky.store)

---

## 팀 소개
(팀원 및 역할 소개 등)

---

## 프로젝트 소개

**음성 혹은 텍스트를 통해 할 일을 입력하면, 주간 계획을 AI로 제안받는 서비스입니다.**

기존 캘린더 서비스에서 사용자는 특정 목표 달성을 위해 단계별 일정을 직접 고민하고 수동으로 입력해야 한다는 번거로움이 있었습니다.  
**LuckyWeeky**는 **생성형 AI**와 **음성 인식** 기능을 결합하여 **개인화된 주간 일정**을 빠르게 추천함으로써,  
사용자의 시간을 절약하고 **음성 명령**으로 편리하게 계획을 등록할 수 있도록 지원합니다.

---

# 시작 가이드

## 환경
- **JDK 17+**
- **Jakarta EE 10**
- **Tomcat 10.1+**
- **MySQL 8.x**
- **Node.js 16+** (프론트엔드용)

## 설치

설치 전 주의사항: **AWS 계정**, **Naver Cloud Platform 계정**, **OpenAI 계정**이 필요합니다.

```bash
# 1. 프로젝트 클론 및 빌드
$ git clone https://github.com/jja6312/LuckyWeeky_server.git
$ cd LuckyWeeky_server
$ mvn clean package

# 2. Tomcat 배포
$ cp target/ROOT.war /path/to/tomcat/webapps/
$ /path/to/tomcat/bin/startup.sh  # 톰캣 실행

# 3. MySQL DB 설정
CREATE DATABASE luckyweeky CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- (1) 사용자 테이블
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    oauth_provider VARCHAR(50),
    oauth_id VARCHAR(255),
    birth_date DATE,
    profile_image_key VARCHAR(255),
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- (2) 사용자 토큰 테이블
CREATE TABLE usertoken (
    user_id BIGINT PRIMARY KEY,
    token VARCHAR(1000) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (3) 사용자 솔트 테이블
CREATE TABLE usersalt (
    user_id BIGINT PRIMARY KEY,
    salt VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (4) 메인 일정 테이블
CREATE TABLE mainschedule (
    main_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    color CHAR(7),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (5) 서브 일정 테이블
CREATE TABLE subschedule (
    sub_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    main_schedule_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    is_completed TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (main_schedule_id) REFERENCES mainschedule(main_schedule_id) ON DELETE CASCADE
);

# 4. src/main/webapp/WEB-INF/local-secrets.json 파일 생성 및 환경 변수 적용
{
  "AWS_ACCESS_KEY": "YOUR_AWS_ACCESS_KEY",
  "AWS_SECRET_KEY": "YOUR_AWS_SECRET_KEY",
  "BUCKET_NAME": "YOUR_S3_BUCKET_NAME",
  "DB_URL": "jdbc:mysql://localhost:3306/luckyweekydb",
  "DB_USERNAME": "root",
  "DB_PASSWORD": "1234",
  "MACHINE_ID": "1",
  "OPENAI_API_KEY": "YOUR_OPENAI_API_KEY",
  "GPT_API_ENDPOINT": "https://api.openai.com/v1/chat/completions",
  "CLOVA_API_URL": "https://naveropenapi.apigw.ntruss.com/recog/v1/stt",
  "CLOVA_ACCESS_KEY": "YOUR_CLOVA_ACCESS_KEY",
  "CLOVA_SECRET_KEY": "YOUR_CLOVA_SECRET_KEY",
  "SECREATKEY": "pdFfOvKx3mEmfhkZcTWd5WNCaHxeInRK"
}
```

## 기술 스택🐈

| Category | Technologies                                                                                                                     |
|----------|----------------------------------------------------------------------------------------------------------------------------------|
| **FE**   | [![React Badge](https://img.shields.io/badge/React-61DAFB.svg?style=flat&logo=react&logoColor=white)]() [![TailwindCSS Badge](https://img.shields.io/badge/Tailwind_CSS-38B2AC.svg?style=flat&logo=Tailwind%20CSS&logoColor=white)]() [![Zustand Badge](https://img.shields.io/badge/Zustand-8AA3FF.svg?style=flat&logo=Zustand&logoColor=white)]() |
| **BE**   | [![Servlet Badge](https://img.shields.io/badge/Servlet-EB7A07.svg?style=flat&logo=openjdk&logoColor=white)]() [![MyBatis Badge](https://img.shields.io/badge/MyBatis-B32137.svg?style=flat&logo=data:image/svg+xml;base64,PHN2ZyBoZWlnaHQ9IjMwMCIgdmlld0JveD0iMCAwIDI2NSAzMDAiIHdpZHRoPSIyNjUiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PC9zdmc+&logoColor=white)]() [![Docker Badge](https://img.shields.io/badge/Docker-2496ED.svg?style=flat&logo=Docker&logoColor=white)]() |
| **DB**   | [![MySQL Badge](https://img.shields.io/badge/MySQL-4479A1.svg?style=flat&logo=mysql&logoColor=white)]()                                                               |
| **Infra**| [![AWS Badge](https://img.shields.io/badge/AWS-232F3E.svg?style=flat&logo=amazon-aws&logoColor=white)]() [![Terraform Badge](https://img.shields.io/badge/Terraform-7B42BC.svg?style=flat&logo=terraform&logoColor=white)]() |
| **APM**  | [![Prometheus Badge](https://img.shields.io/badge/Prometheus-E6522C.svg?style=flat&logo=prometheus&logoColor=white)]() [![Grafana Badge](https://img.shields.io/badge/Grafana-F46800.svg?style=flat&logo=grafana&logoColor=white)]() [![JMeter Badge](https://img.shields.io/badge/JMeter-D22128.svg?style=flat&logo=apache-jmeter&logoColor=white)]() |


---

# 화면 구성

| 0.로그인      | 1.회원가입      | 2.메인화면      | 3_1일정생성      | 3_2일정조회      | 3_3일정수정      | 4_1AI일정생성      | 4_2AI일정생성완료      | 4_3AI일정수정      | 4_4AI일정재요청      | 4_5AI일정재요청완료      | 대표이미지      |
|--------------|----------------|----------------|------------------|-----------------|-----------------|--------------------|------------------------|--------------------|----------------------|---------------------------|----------------|
|              |                |                |                  |                 |                 |                    |                        |                    |                      |                           |                |


---

## 주요 기능 📦

- **AI 기반 일정 생성**  
  음성 또는 텍스트 입력을 통해 데이터를 자동 분석하여 개인화된 주간 일정을 생성합니다.

- **일별 To-Do 리스트 자동 생성**  
  생성된 주간 일정을 바탕으로 사용자의 To-Do 리스트를 일별로 자동 작성하여 효율적인 관리가 가능합니다.

---

# 아키텍처

## 클라우드 아키텍처
(AWS, EC2, ECS, S3, RDS 등 다이어그램)

## UML: 유스케이스 다이어그램
(유스케이스 다이어그램 이미지)

## UML: 시퀀스 다이어그램
(시퀀스 다이어그램 이미지)

## UML: 플로우 차트
(플로우 차트 이미지)

## ERD
(ERD 이미지)

---

## 디렉토리 구조

```bash
LuckyWeeky_server/
├── Dockerfile                 # Docker 설정 파일
├── ecs-task-definition.json   # AWS ECS 태스크 정의 파일
├── pom.xml                    # Maven 프로젝트 설정 파일
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/ssafy/luckyweeky/
│   │   │       ├── common/            # 공통 모듈 (DispatcherServlet, 필터, 유틸리티)
│   │   │       │   ├── DispatcherServlet.java   # 커스텀 DispatcherServlet
│   │   │       │   ├── filter/
│   │   │       │   │   ├── AuthFilter.java      # 인증 필터
│   │   │       │   │   ├── CORSFilter.java      # CORS 처리 필터
│   │   │       │   │   └── JwtAuthenticationFilter.java # JWT 인증 필터
│   │   │       │   ├── config/
│   │   │       │   │   ├── XmlBeanFactory.java  # XML 기반 Bean 관리
│   │   │       │   │   └── XmlParser.java       # XML 파싱 유틸리티
│   │   │       │   └── util/
│   │   │       │       ├── OpenCrypt.java       # 암호화 유틸리티
│   │   │       │       └── RequestJsonParser.java # JSON 요청 파서
│   │   │       ├── schedule/          # 일정 관리 모듈
│   │   │       │   ├── application/   # 서비스, DTO
│   │   │       │   │   ├── dto/
│   │   │       │   │   │   ├── ScheduleDto.java # 일정 DTO
│   │   │       │   │   │   └── SubScheduleDto.java # 서브 일정 DTO
│   │   │       │   │   └── service/
│   │   │       │   │       └── ScheduleService.java # 일정 관련 서비스
│   │   │       │   ├── domain/        # 엔티티, 레포지토리
│   │   │       │   │   ├── model/
│   │   │       │   │   │   ├── MainScheduleEntity.java # 메인 일정 엔티티
│   │   │       │   │   │   └── SubScheduleEntity.java  # 서브 일정 엔티티
│   │   │       │   │   └── repository/
│   │   │       │   │       └── MainScheduleMapper.java # MyBatis 매퍼
│   │   │       │   └── presentation/
│   │   │       │       └── ScheduleController.java # 일정 관련 API 컨트롤러
│   │   │       ├── user/              # 사용자 관리 모듈
│   │   │       │   ├── application/
│   │   │       │   │   └── service/
│   │   │       │   │       └── UserService.java # 사용자 서비스
│   │   │       │   ├── domain/
│   │   │       │   │   ├── model/
│   │   │       │   │   │   └── UserEntity.java  # 사용자 엔티티
│   │   │       │   │   └── repository/
│   │   │       │   │       └── UserMapper.java  # 사용자 MyBatis 매퍼
│   │   │       │   └── presentation/
│   │   │       │       └── UserController.java  # 사용자 API 컨트롤러
│   │   │       ├── scheduleAi/        # AI 기반 일정 추천 모듈
│   │   │       │   ├── application/
│   │   │       │   │   ├── dto/
│   │   │       │   │   │   └── CreateAiScheduleRequestDTO.java # AI 일정 요청 DTO
│   │   │       │   │   └── service/
│   │   │       │   │       ├── ChatgptService.java   # ChatGPT 서비스
│   │   │       │   │       └── ScheduleAiService.java # AI 일정 추천 서비스
│   │   │       │   └── presentation/
│   │   │       │       └── ScheduleAiController.java # AI 일정 추천 API 컨트롤러
│   │   └── resources/
│   │       ├── mappers/               # MyBatis 매퍼 XML
│   │       │   ├── MainScheduleMapper.xml
│   │       │   └── UserMapper.xml
│   │       └── mybatis-config.xml     # MyBatis 전역 설정
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── beans/                 # Bean 설정
│       │   │   ├── controller.xml
│       │   │   └── model.xml
│       │   └── web.xml                # 서블릿 설정
│       └── META-INF/                  # 메타데이터
└── target/
    └── LuckyWeeky_server.war          # 배포 가능한 WAR 파일
```
