# 독서 기반 SNS, BookNuts
![아이템포스터_9해조](https://user-images.githubusercontent.com/78673570/180656664-afff889d-366d-4bb4-b677-ec8d18505f39.jpg)

## 🥜 프로젝트 소개
기존의 개방형 SNS에서 확증편향/혐오/가짜뉴스 등으로 인해 사람들의 발언이 위축되고 있다는 문제를 발견하였습니다. 이러한 SNS 생태계의 어두운 면을 해결하고자 ‘책’이라는 매개를 꺼내들게 되었고, 이를 기반으로 개인의 고유한 가치를 추구하는 SNS 생태계를 구축하고자 하였습니다.

<br>

핵심 기능은 다음과 같습니다.
1. 누구나 자유롭게 의견을 개진할 수 있는 토론장
2. 자신의 지적 경험을 연재하는 시리즈 발행
3. 기본적인 커뮤니티 기능 (게시글/댓글/공감 등)

<br><br>

## 🛠 기술 스택
- `Spring Boot(Java)` 를 사용하여 API 를 개발하였고, `JPA` 기술을 사용하였습니다.
- 데이터베이스로는 `AWS RDS(MySQL)` 를 사용하였고, `AWS EC2` 를 통해 서버 배포를 진행하였습니다.
- 이미지 업로드를 위해 `AWS S3` 버킷을 사용하였습니다.
- 협업 및 코드 버전 관리를 위해 `Github`, API 문서 관리를 위해 `Postman`과 `Notion`을 사용하였습니다.
- `Github Actions`, `CodeDeploy` 를 사용해 CI/CD 파이프라인을 구축하였습니다.

<br><br>

## 👩‍💻 개발 내용
- 회원 - JWT + Spring Security를 통한 로그인
- Access Token + Refresh Token 구현
- 커뮤니티 기능 (게시글, 댓글, 공감 등)
- 토론 관련 기능 개발 (토론장 개설, 참여, 상태 변경)
- 시리즈 - 게시글 그룹핑을 통한 시리즈 발행/추가/삭제
- 아카이브 - 게시글 아카이빙/추가/삭제
- 팔로우 & 팔로잉
- 이미지 업로드 (프로필, 아카이브, 시리즈, 토론 커버 이미지)

<br><br>

## 📑 문서
- [API Documentation] [booknuts-apis](https://documenter.getpostman.com/view/18461572/UzJQpDsB)

<br><br>

## 🧩 서비스 아키텍처
![image](https://user-images.githubusercontent.com/78673570/181485191-465f53b6-a250-4c52-b04c-57c17c5f01a5.png)

### CI/CD Pipeline
1. 개발 완료 후 main 브랜치에 Push (or Pull Request 전송)
2. Github Actions 동작
3. main 브랜치 Build 
4. jar 파일 AWS S3에 업로드
5. Code Deploy 실행
6. AWS EC2의 Code Deploy가 AWS S3에 업로드된 파일을 가져와 배포
7. 자동화 배포 (deploy.sh 을 통해 jar 파일 실행)

<br><br>

## 💾 ERD Diagram
![image](https://user-images.githubusercontent.com/78673570/181485477-3b45ecf7-e193-456f-ba14-a8d3e9c8c6ac.png)

<br><br>

## 🔍 화면 구성
![image](https://user-images.githubusercontent.com/78673570/181485727-8c2afbb3-a090-4538-900f-21cc9efdca03.png)
![image](https://user-images.githubusercontent.com/78673570/181485741-26fcacf6-287b-4799-9dde-40898c92153f.png)

<br><br>

## 📌 Commit Message Convention
- ➕ [ADD] : FEAT 이외의 부수적인 코드 추가 및 라이브러리 추가, 새로운 파일 생성
- ✅ [MOD] : 코드 수정 및 내부 파일 수정
- ✨ [FEAT] : 새로운 기능 구현
- 🔀 [MERGE] : 다른 브랜치 MERGE 시 사용
- 🔨 [FIX] : 버그 및 오류 해결
- 🗑️ [DEL] : 쓸모없는 코드나 파일 삭제
- 📝 [DOCS] : README나 WIKI 등의 문서 개정
- ✏️ [CORRECT] : 문법의 오류나 타입의 변경, 이름 변경 등의 작은 수정
- 🚚 [MOVE] : 프로젝트 내 파일이나 코드의 이동
- ⏪️ [RENAME] : 파일 이름 변경
- ♻️ [REFACTOR] : 전면 수정
- 🛠 [SETTING] : 기타 설정 시
