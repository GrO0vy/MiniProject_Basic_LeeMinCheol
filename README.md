# 멋사 마켓 서비스 만들기
## 3.1 ♻️멋사마켓
🥕당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.

사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 거래 플랫폼의 백엔드를 만드는 프로젝트입니다.

## 물품 관리

### ● CREATE
URL : `POST /items`

  #### ResquestBody
  ```json
  {
    "title": "중고 노트북 팝니다",
    "description": "20129년 맥북 프로 13인치 모델입니다",
    "minPriceWanted": 1000000,
    "writer": "lee.dev",
    "password": "1qaz2wsx"
  }
  ```
  물품을 등록하는 URL 요청
  - 제목, 설명, 최소가격, 작성자, 비밀번호를 필수로 입력해야한다.
  - 물품이 등록되면 물품의 상태는 판매중 상태가 되어야한다.
  - 이미지 등록은 물품 등록과 동시에 이루어질수 없다.
  - 필수 항목이 입력되지 않으면 오류 메세지를 띄운다.


  - 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
      "message": "등록이 완료되었습니다"
  }
  ```

  - 등록 실패 시 결과 ( ResponseBody )
  ```json
  {
      "message": "필수 항목을 모두 입력해주세요"
  }
  ```

### ● READ ( 전체, 페이지 조회 )
URL : `GET /items?page=1 & size=1`

 
  등록된 물품을 조회하는 URL 요청
  - 페이지 번호와 한 페이지의 크기를 입력해서 해당 페이지의 물품들을 조회 할 수 있다.
  - 쿼리 파라미터를 전달하지 않으면 페이지 번호는 0, 페이지 크기는 Integer.MAX_VALUE 가 전달되어 전체 물품을 조회한다.


  
  
  - 페이지 조회 시 결과 ( ResponseBody )
    요청 URL: GET http://localhost:8080/items?page= 0&size= 1
  ```json
  {
    "content": [
        {
            "id": 1,
            "title": "중고 노트북 팝니다",
            "description": "20129년 맥북 프로 13인치 모델입니다",
            "minPriceWanted": 1000000,
            "status": "판매중"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageSize": 1,
        "pageNumber": 0,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 1,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 1,
    "empty": false
  }
  ```


  - 전체 조회 시 결과
    요청 URL: GET http://localhost:8080/items
  ```json
  {
      {
    "content": [
        {
            "id": 1,
            "title": "중고 노트북 팝니다",
            "description": "20129년 맥북 프로 13인치 모델입니다",
            "minPriceWanted": 1000000,
            "status": "판매중"
        },
        {
            "id": 2,
            "title": "새 노트북 팝니다",
            "description": "20129년 맥북 프로 13인치 모델입니다",
            "minPriceWanted": 1000000,
            "status": "판매중"
        },
        {
            "id": 3,
            "title": "쓰던던 노트북 팝니다",
            "description": "20129년 맥북 프로 13인치 모델입니다",
            "minPriceWanted": 1000000,
            "status": "판매중"
        },
        {
            "id": 4,
            "title": "안 쓴 노트북 팝니다",
            "description": "20129년 맥북 프로 13인치 모델입니다",
            "minPriceWanted": 1000000,
            "status": "판매중"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageSize": 2147483647,
        "pageNumber": 0,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 4,
    "totalPages": 1,
    "last": true,
    "size": 2147483647,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 4,
    "empty": false
  }
}
  ```


### ● UPDATE

### ● DELETE


## 댓글 관리

### ● CREATE

### ● READ

### ● UPDATE

### ● DELETE


## 구매제안 관리

### ● CREATE

### ● READ

### ● UPDATE

### ● DELETE
