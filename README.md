# 항해99 프로젝트

현재 작업 진행 
> 1주차(CI/CD 배포 파이프라인 구축 - 빌드 환경 구축)

구현 내역
* Spring Actuator 추가(endpoint: "/actuator/health")
* 서버 Phase 에 따른 yml 분리(dev, staging, prod)
* CI/CD 배포를 위한 Github Action 추가
* Github Action에 Lint 과정 추가
* Github Action에 test 검증 과정 추가

### 컨벤션
1. custom setter나 backing property를 사용해서 setter에 접근제어자 제한이 가능하지만 setter를 public으로 열어두고 사용하지 않는 방식을 사용
