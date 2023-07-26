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


  ※ 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
      "message": "등록이 완료되었습니다"
  }
  ```

  ※ 등록 실패 시 결과 ( ResponseBody )
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


  ※ 페이지 조회 시 결과 ( ResponseBody )
    
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


  ※ 전체 조회 시 결과
    
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

### ● READ ( 단일 물품 조회 )
URL : `GET /items/{itemId}`


  단일 물품정보를 조회하는 URL 요청
  - URL 경로에 물품 인덱스를 입력 받아 그것을 이용해 단일 물품을 조회 할 수 있도록한다.


  ※ 조회 성공 시 결과 ( ResponseBody )
  ```json
  {
    "title": "중고 맥북 팝니다",
    "description": "2019년 맥북 프로 13인치 모델입니다",
    "minPriceWanted": 1000000,
    "status": "판매중"
  }
  ```

### ● UPDATE
URL : `PUT /items/{itemId}`

  #### ResquestBody
  ```json
  {
    "title": "중고 노트북 팝니다",
    "description": "2019년 맥북 프로 13인치 모델입니다",
    "minPriceWanted": 1000000,
    "writer": "lee.dev",
    "password": "1qaz2wsx"
  }
  ```
  등록된 물품정보를 수정하는 URL 요청
  - 제목, 설명, 최소 가격을 물품을 등록한 작성자만 수정할 수 있도록한다.
  - 작성자와 비밀번호를 입력해서 수정 요청을 보내는 사용자가 작성자가 맞는지 확인한다.
  - 작성자 정보가 일치하지 않으면 400 에러를 띄운다.


  ※ 수정 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "물품이 수정되었습니다"
  }
  ```

  ※ 수정 실패 시 결과 ( ResponseBody ) - 작성자, 비밀번호가 일치하지 않을 때
  ```json
  {
    "timestamp": "2023-07-26T04:27:35.956+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": ...
    "message": "400 BAD_REQUEST",
    "path": "/items/1"
  }
  ```

### ● UPDATE ( 물품 이미지 등록 )
URL : `PUT /items/{itemId}/images`
 #### ResquestBody
 ![image](https://github.com/likelion-backend-5th/MiniProject_Basic_LeeMinCheol/assets/89755903/4d8127d7-a943-466c-9f86-b211f94a0c93)


 등록된 물품의 이미지를 등록하는 URL 요청
  - RequestBody 에 form-data 를 포함해서 요청을 전달 ( 이미지 파일, 작성자, 비밀번호 )
  - 작성자와 비밀번호가 맞는지 확인하고 작성자 정보가 일치하지 않으면 400 에러를 띄운다.
  - 작성자와 비밀번호가 일치하면 이미지를 저장하고 이미지 경로를 물품 Entity 에 저장한다.
  - 등록된 물품에 한에서만 이미지 등록을 진행할 수 있다.

  ※ 이미지 등 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "이미지가 등록되었습니다."
  }
  ```

  ※ 이미지 등록록 실패 시 결과 ( ResponseBody ) - 작성자, 비밀번호가 일치하지 않을 때
  ```json
  {
    "timestamp": "2023-07-26T04:27:35.956+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": ...
    "message": "400 BAD_REQUEST",
    "path": "/items/1"
  }
  ```
    

### ● DELETE
URL : `DELETE /items/{itemId}`

  #### ResquestBody
  ```json
  {
    "writer": "lee.dev",
    "password": "1qaz2wsx"
  }
  ```
  등록된 물품을 삭제하는 URL 요청
  - 물품을 등록한 작성자만 삭제할 수 있도록한다.
  - 작성자와 비밀번호를 입력해서 수정 요청을 보내는 사용자가 작성자가 맞는지 확인한다.
  - 해당 글에 달린 모든 댓글을 삭제한다.
  - 해당 물품의 이미지와 이미지 디렉토리를 삭제한다.
  - 작성자 정보가 일치하지 않으면 400 에러를 띄운다.


  ※ 삭제 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "물품을 삭제했습니다."
  }
  ```

  ※ 삭제 실패 시 결과 ( ResponseBody ) - 작성자, 비밀번호가 일치하지 않을 때
  ```json
  {
    "timestamp": "2023-07-26T04:33:34.507+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": ...
    "message": "400 BAD_REQUEST",
    "path": "/items/1"
  }
  ```


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
