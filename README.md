# Spring으로 구현하는 Auth Server

## 기능 목록
* 프로젝트 세팅 및 Member 엔티티 구현
    * 프론트 엔드 : node v16.13.1, yarn 1.22.17, typescript, react, package.json과 tsconfig 세팅
    * 백 엔드 : java 1.8, spring-boot, pom.xml에 dependency 세팅
    * TODO: 일반 먼저 구현 후 DB 타입 설정 varchar(255)에서 조건에 맞게 변경
* 회원가입 기능 구현
    * Member 모델, 레포지토리 구현
    * AuthService, AuthServiceImpl 구현
    * MemberController 컨트롤러에 signUp 메소드 구현
* 회원가입 화면 구현
    * SignUpContainer와 SignUpPresenter 구현
* 시간차 JWT를 사용한 로그인 기능 구현
    * JwtUtil, CookieUtil 서비스 구현
    * MemberController 컨트롤러에 login 메소드 추가
    * RedisUtil 서비스 구현
* 로그인 화면 구현
    * MainContainer와 MainPresenter 구현
* Spring Security 커스터마이즈하고 axios 설정하여 CORS 회피 하기
    * 프론트 엔드: Endpoint와 axios 설정
    * 백 엔드: JwtRequestFilter 구현
* 회원가입 이메일 인증 기능(비밀번호 찾기 기능) 구현
    * RequestVerityEmail 모델 구현
    * EmailService, EmailServiceImpl 구현
* TODO: 사용자 역할 설정하는 admin 페이지 구현
    * AdminController 구현
    * admin 화면 구현(타임 리프)
