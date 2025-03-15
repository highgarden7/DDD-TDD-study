## 1. 개발 환경
- **OS**: macOS 13.3.1
- **IDE**: IntelliJ IDEA Ultimate 2023.3.4
- **JDK**: Java 17 (Corretto 17)
- **Framework**: Spring Boot 3.4.2
- **Build Tool**: Gradle 8.5
- **Database**: H2 Database (In-Memory)
- **Testing**: JUnit 5

---

## 2. 개발 진행 흐름

### 2.1 DDD 방법론을 적용하여 프로젝트 분석

#### 2.1.1 비즈니스 요구사항 분석
- 회원가입 기능
- 로그인 기능
- 송금 견적서 제공
  - 원화(KRW)를 기준으로 **금액과 타겟 화폐 종류**를 받아 송금 견적서 제공
  - 견적서 정보 및 환율 정보 수집 후 견적서 연산
- 송금 접수 기능
  - 제공된 견적서를 바탕으로 송금 접수
- 회원의 거래 이력 확인

#### 2.1.2 도메인 분류

##### 1) 회원 관리 (User Context)
- 책임: 회원 정보 관리
- 핵심 엔티티: User

##### 2) 인증 (Authentication Context)
- 책임: 로그인 및 인증 토큰 발급/검증
- 특징:
  - UserRepository를 통해 사용자의 비밀번호 검증
  - JWT 기반 인증 토큰 관리

##### 3) 견적 (Quote Context)
- 책임: 송금 견적 생성 (환율 반영)
- 핵심 엔티티: Quote
- 외부 연동:
  - 환율 정보 수집 (Upbit API)
    `https://crix-api-cdn.upbit.com/v1/forex/recent?codes=FRX.KRWJPY,FRX.KRWUSD`

##### 4) 송금 관리 (Transfer Context)
- 책임: 송금 접수 및 요청 처리
- 핵심 엔티티: Transfer
- 비즈니스 규칙: 견적서를 기반으로 송금 요청을 생성해야 함

##### 5) 거래 이력 (History Context)
- 책임: 회원의 송금 이력 관리
- 핵심 엔티티: History

#### 2.1.3 컨텍스트 매핑 (Context Mapping)

| Source Context | Target Context | 관계 |
|---------------|---------------|------|
| User Context | Authentication Context | 회원가입 및 로그인 요청 |
| User Context | Quote Context | 회원이 송금 견적서를 요청 |
| Quote Context | Transfer Context | 견적서를 기반으로 송금 요청 |
| User Context | History Context | 회원이 자신의 송금 이력을 조회 |

#### 2.1.4 애그리게이트 루트
- User
- Quote
- Transfer
- History

#### 2.1.5 API 엔드포인트

| 기능 | HTTP Method | Endpoint |
|------|------------|----------|
| 회원가입 | POST | `/v1/api/user/signup` |
| 로그인 | POST | `/v1/api/user/login` |
| 송금 견적 요청 | POST | `/v1/api/transfer/quote` |
| 송금 요청 | POST | `/v1/api/transfer/request` |
| 거래 이력 조회 | GET | `/v1/api/transfer/list` |

---

### 2.2 환경설정
- Spring Initializr를 사용해 기본 프로젝트 생성
- DDD 원칙에 따라 Layered Architecture 적용

---

### 2.3 프로젝트 구조

```plaintext
v1/
├── domain/             # 도메인 계층
│   ├── user/           # 회원 관리 (User Context)
│   ├── quote/          # 송금 견적 (Quote Context)
│   ├── transfer/       # 송금 관리 (Transfer Context)
│   └── history/        # 거래 이력 (History Context)
│
├── application/        # 응용 서비스 계층
│   ├── user/           # 회원 관련 응용 서비스
│   ├── auth/           # 인증 관련 응용 서비스
│   ├── quote/          # 견적 관련 응용 서비스
│   ├── transfer/       # 송금 관련 응용 서비스
│   └── history/        # 거래 이력 응용 서비스
│
├── infrastructure/     # 인프라 계층
│   ├── user/           # 회원 레포지토리
│   ├── quote/          # 견적서 레포지토리
│   ├── transfer/       # 송금 레포지토리
│   └── history/        # 거래 레포지토리
│
├── representation/       # REST API 컨트롤러
│   ├── user/           # 회원 API 컨트롤러
│   │   └── dto/        # 요청/응답 DTO
│   └── transfer/       # 송금 API 컨트롤러
│       └── dto/        # 요청/응답 DTO
│
├── config/             # 환경 설정 (Spring Security, JWT 등)
│
Application.java        # Spring Boot 메인 애플리케이션
```
---

### 3. TDD 를 적용한 회원가입 기능 개발 
##### 3.1 상세 요구사항
- 회원의 유니크 값은 userId (이메일 형식)
- 암호화가 필요한 값은 암호화 (password, idValue)
- idType (REG_NO, BUSINESS_NO)
- REG_NO 면 주민등록번호로 암호화
- BUSINESS_NO 면 사업자 등록번호로 암호화
##### 3.2 UserRepository 테스트코드 작성
- 데이터가 DB에 저장
- DB에 저장된 데이터 조회
##### 3.3 UserService 테스트코드 작성
- 회원가입이 성공적으로 이루어지는 경우
- userId로 조회 가능한지 확인
##### 3.4 UserFacade 테스트코드 작성
- 회원가입이 성공적으로 이루어지는 경우
- 이미 존재하는 이메일로 회원가입을 시도하면 예외 발생
- 비밀번호가 암호화되어 저장되는지 확인
- idValue가 암호화되어 저장되는지 확인

---
### 4. 로그인 기능 개발 
##### 4.1 상세 요구사항
- 인증토큰의 유효시간은 발급 후 30분
##### 4.2 테스트 시나리오
- 올바른 이메일과 비밀번호 입력 시 로그인 성공
- 존재하지 않는 이메일 입력 시 로그인 실패
- 비밀번호 불일치 시 로그인 실패
##### 4.3 AuthService 테스트코드 작성
- 로그인 성공
- 존재하지 않는 이메일로 로그인 시도 시 예외 발생
- 비밀번호 불일치 시 로그인 실패
##### 4.4 로그인 처리 및 security 설정
- WebSecurityConfig 에서 security 설정 진행
- 인증정보 활용을 위한 CustomUserDetails 및 CustomUserDetailService
- 매 요청 시 인증정보를 확인하기 위한 JwtAuthenticationFilter 설정

---
### 5. 송금 견적 기능 개발
##### 5.1 상세 요구사항
- 송금 견적서는 원화(KRW)를 기준으로 **금액과 타겟 화폐 종류**를 받아 송금 견적서를 제공 (금융계산의 정확한 연산을 위한 BigDecimal 사용)
- 견적서 정보 및 환율 정보 수집 후 견적서 연산
- 견적서는 재사용 불가능
##### 5.2 QuoteService 테스트코드 작성
- 송금 견적서 생성 성공
- 송금 견적서 생성 실패
- 견적서 사용여부 확인
##### 5.3 외부 API 연동 테스트코드 작성
- 환율 정보 수집 성공
##### 5.4 QuoteFacade 테스트코드 작성
- 송금 견적서 생성 성공
- 송금 견적서 생성 실패

---
### 6. 송금 요청 기능 개발
##### 6.1 상세 요구사항
- 송금 요청은 제공된 견적서를 바탕으로 송금 접수
- 견적서 만료시간이 지났을 시 *QUOTE_EXPIRED* 에러
- 개인 회원은 하루에 $1000 이상의 금액을 송금할 수 없음
- 법인 회원은 하루에 $5000 이상의 금액을 송금할 수 없음
- 이외 에러에 대해서 *UNKNOWN_ERROR* 로 전달
##### 6.2 TransferService 테스트코드 작성
- 송금 요청 생성 성공
##### 6.3 TransferFacade 테스트코드 작성
- 송금 요청 생성 성공
- 견적서 재사용 시 송금실패
- 견적서 만료시간이 지나 송금요청 실패
- 개인 회원이 하루에 $1000 이상의 금액을 송금할 시 송금요청 실패
- 법인 회원이 하루에 $5000 이상의 금액을 송금할 시 송금요청 실패

---
### 7. 송금 이력 조회 기능 개발
##### 7.1 상세 요구사항
- 회원의 거래 이력을 조회
##### 7.2 HistoryService 테스트코드 작성
- 회원의 거래 이력 조회 성공
##### 7.3 TransferFacade 테스트코드 추가
- 송금 요청 생성 성공시 거래이력 생성

---
### 8. 추가 리팩토링
- 에러코드 정의 및 GlobalExceptionHandler 추가
- API 명세 추가 OpenAPI 3.0

