# 프로젝트 실행하기

## app-api 프로젝트 실행하기

1. 프로젝트 디렉토리로 이동

```shell
cd {project-directory}
```

2. 프로젝트 빌드하기

```shell
./gradlew :app-api
```

3. 프로젝트 실행하기

```shell
java -jar -Dspring.profiles.active=local  app-api/build/libs/app-api-0.0.1-SNAPSHOT.jar
```

- 프로젝트 실행 전에 mysql이 실행되어 있어야한다.
- 로컬에서 테스트 시에는 docker를 사용해 mysql을 실행한다. 실행방법은 [로컬에서 docker로 MYSQL 컨테이너 띄우기](#로컬에서-docker로-mysql-컨테이너-띄우기) 참고.

## 로컬에서 docker로 MYSQL 컨테이너 띄우기

1. 프로젝트 디렉토리로 이동

```shell
cd {project-directory}
```

2. 도커 컨테이너 실행

```shell
docker-compose up -d
```

- mysql database를 위한 volume을 프로젝트 디렉토리 내에 생성한다.
- ai_review 데이터베이스가 만들어지고 username `review` password `review`로 접속 가능하다.

# API 설계 규칙
- URL을 구성할 때 맨 앞에 버전을 명시한다 ex) /v1/...
- 여러 단어로 구성된 경우 dash를 사용한다 ex) /v1/review-cards
- URL의 Entity를 나타내는 구문은 복수형으로 한다 ex) /v1/articles
- 조회 조건은 쿼리 파라미터를 사용한다 ex) /v1/user/1/review-cards?state=done
- 리소스 만으로 특정 동작을 나타내기 어려울때 특정 행위를 나타내는 단어를 url에 추가하는 것을 허용한다.
  - ex) /v1/user/review-cards/share
  - GET, POST, PUT, DELETE 로 처리하기 애매한 경우 Control-URL를 사용한다.
- 모든 성공한 응답은 HTTP Status 200을 보낸다.
- 모든 실패한 응답은 HTTP Status로 실패를 나타내고, http 메시지 body에 에러 코드와 에러 메시지를 담아서 상세한 내용을 전달한다.
    ```json
    {
      "errorCode" : "RESOURCE_NOT_FOUND",
      "errorMessage" : "유저 id = 1000 가 존재하지 않습니다."
    }
    ```
  - 단, 인증이 실패하였을 때는 ```HTTP 401 Unauthorized``` 에러가 반환되고, 에러 메시지는 전달하지 않는다.
- PUT 메소드는 전체 리소스를 업데이트할때 사용하고, PATCH 메소드는 리소스의 일부를 업데이트할때 사용한다.
- PATCH 메소드로 특정 필드를 업데이트 할때, 수정할 필드만 http 메시지 바디에 담아서 요청하고, 값이 있는 필드만 수정한다.
- 쿼리 파라미터나 http 메시지 바디로 전달되는 데이터의 유효성은 dto에서 체크하여 빠르게 실패할 수 있도록 한다. 
  - 유효성 검증에 실패한 경우 400 BAD REQUEST를 전달한다.
- DELETE 메소드는 멱등성을 가지도록 특정 자원을 지정해서 지우도록 한다(/v1/users/1가능. /v1/users/last 불가능.)
- API 요청 결과가 여러개였을때 그 최대 limit을 지정해야한다.
  - limit을 쿼리 파라미터로 받고, 서버에서 limit으로 받을 수 있는 값의 범위를 지정한다.
  - limit이 null일 경우의 디폴트 값을 지정해둔다.