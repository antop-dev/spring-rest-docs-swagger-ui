# spring-rest-docs-swagger-ui

![Java 11](https://img.shields.io/badge/Java-11-blue?style=flat-square)
![Spring 2.5.4](https://img.shields.io/badge/Spring_Framework-2.5.4-blue?style=flat-square)
![Spring REST Docs 2.0.5](https://img.shields.io/badge/Spring_REST_Docs-2.0.5-blue?style=flat-square)
![Swagger-UI 3](https://img.shields.io/badge/Swagger_UI-3.0.0-blue?style=flat-square)

### 개요

Spring REST Docs와 Swagger의 장점만을 사용해보자!

![Flow](https://i.imgur.com/yBFJfRW.png)

1. restdocs-api-spec을 이용하여 테스트 코드를 작성한다. Spring REST Docs 일부 클래스를 랩핑한 클래스를 사옹하여 작성한다..
2. `gradlew openapi3`로 OpenApi3 스팩에 맞는 파일을 생성한다.
3. WebJar로 패키징된 **swagger-ui**를 정상적으로 띄울 수 있도록 한다.
    1. swagger-ui 가 정상적으로 동작할 수 있도록 아래 3개 URL을 제공한다.
        - `/swagger-resources`
        - `/swagger-resources/configuration/ui`
        - `/swagger-resources/configuration/security`
    2. 생성된 openapi3 파일을 제공한다.
4. /swagger-ui/ 로 접속하면 OpenAPI3 기반으로 UI가 보여지게 된다.

### 실행

실행: `gradlew test` → `gradlew openapi3` → `gradlew bootRun`

API 문서 위치: http://localhost:8080/docs

![1](https://i.imgur.com/NVXLw52.png)

각 제목의 **테스트** 링크 클릭 또는 http://localhost:8080/swagger-ui

![2](https://i.imgur.com/lJDwUNk.png)

### 후기

1. [Asciidoctor](https://docs.asciidoctor.org/asciidoc/latest/) 문법을 잘 알아야 한다.
2. 각 정보<sup>`snippet`</sup>를 커스터마이징<sup>`customize`</sup/>하려면 [Mustache](https://mustache.github.io/mustache.5.html) 문법을
   알아야 한다.
3. 실패(`4xx`, `5xx`)에 대한 테스트 케이스를 통합해줘서 보여주면 좋겠다.
4. Spring REST Docs를 주로(필드, 입출력 명세) 보고 간단히 테스트 용도로 Swagger-UI를 적용하는 방안으로 가야 할 것 같다.
5. Swagger-UI 입출력 스키마를 보는 것은  

### Reference

* [Swagger와 Spring Restdocs의 우아한 조합 (by OpenAPI Spec)](https://taetaetae.github.io/posts/a-combination-of-swagger-and-spring-restdocs/)
* [Spring REST Docs API specification Integration](https://github.com/ePages-de/restdocs-api-spec)
* [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)
* [Spring Rest Docs를 이용한 API 문서 만들기](https://jaehun2841.github.io/2019/08/04/2019-08-04-spring-rest-docs/)
* [Java］Spring REST Docs HTML이 생성되지 않을때](https://blog.hodory.dev/2019/12/04/spring-rest-docs-with-gradle-not-working-html5/)
* [https://velog.io/@max9106/Spring-Spring-rest-docs를-이용한-문서화](https://velog.io/@max9106/Spring-Spring-rest-docs%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%AC%B8%EC%84%9C%ED%99%94)
* [Spring Boot - rest docs 사용방법과 자동 목차생성(spring boot restdocs 설정](https://galid1.tistory.com/736)
* [Spring Rest Docs 적용](https://techblog.woowahan.com/2597/)
