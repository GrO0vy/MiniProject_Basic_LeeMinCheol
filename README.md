# 멋사 마켓 서비스 만들기
## 3.1 ♻️멋사마켓
🥕당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.

사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 거래 플랫폼의 백엔드를 만드는 프로젝트입니다.

## 회원가입
URL : `POST /users/register`

```json
{
    "userId": "lmc2819",
    "userPassword": "123",
    "passwordCheck": "123",
    "phone": "010-1111-2222",
    "email": "lmc@naver.com",
    "address": "인천광역시"
}
```

회원가입을 하는 URL 요청
- userId, userPassword, passwordCheck 를 필수적으로 입력받도록한다.
- 패스워드와 패스워드 확인이 다르면 회원가입이 되지 않도록한다.
- 아이디가 중복된 유저가 있으면 회원가입이 되지 않도록한다.


## 로그인
URL : `POST /users/login?inputId= & inputPw= `

로그인을 하는 URL 요청
- 로그인 성공 시 jwt 발급
- 로그인 성공 시 로그인을 유지하기 위해서 jwt 를 로컬 스토리지에 저장


## UI 구현

### 메인 UI 구현
- URL `GET localhost:8080`
- 로그인이 안된 상태라면 회원가입, 로그인 게시글 조회가 가능하도록 설정
- 로그인이 된 상태라면 글쓰기, 게시글 조회가 가능하도록 설정

  
![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/0bf5e4fb-6503-4f92-b981-e542d135ba32)

![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/d8f37275-6166-4b90-b104-1fc27fd8389a)

  

### 회원가입 폼을 통해서도 회원가입이 가능하도록 UI 구현
- URL : `GET /form/register-form`
- 회원가입 버튼 클릭시 회원가입 요청을 보냄
  
![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/07cd09f2-86e3-464f-9f14-bbf02c5383b6)



### 로그인 폼을 통해서도 로그인이 가능하도록 UI 구현
- URL : `GET /form/login-form`
- 로그인 버튼 클릭시 로그인 요청을 보냄
  
![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/eb05a1a0-c08e-445e-a16c-1a01dd47a71f)



### 글 쓰기 폼을 통해서 글을 작성할 수 있도록 UI 구현
- URL : `GET /form/writing-article-form`
- 작성완료 버튼 클릭시 글 등록 요청을 보냄
  
![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/a34f53f1-68d8-4b14-9574-be961afa5fd5)



### 전체 글 조회 UI 구현
- URL : `GET /items`
- 메인 화면에서 전체 글 보기 버튼 클릭 시 조회 가능

![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/95a41bf6-6ec2-40ea-886d-d1eb6501eea5)



### 글 상세보기 UI 구현
- URL : `GET /items/{itemId}`
- 전체 글 조회에서 조회하고 싶은 글의 제목을 클릭하면 상세보기 가능

![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/96317279-42f3-4d00-9688-6c71dc960b34)



### 글에 대한 댓글 조회 UI 구현
- URL : `GET /items/{itemId}/comments`
- 글 상세페이지에서 댓글 보기 버튼을 클릭 시 해당 글에 대한 댓글을 조회할 수 있음
  
![image](https://github.com/likelion-backend-5th/Project_1_LeeMinCheol/assets/89755903/fc14aa0d-d0ef-4bee-bbbe-7fc6897e6e55)



## 권한 설정
- 게시글 조회, 댓글 조회는 누구나 접근 가능하도록 설정
- 회원가입, 로그인은 인증되지 않은 사용자만 접근 가능하도록 설정
- 나머지 요청은 인증된 사용자만 접근 가능하도록 설정
- 특히 게시물/댓글/제안 에 대한 수정, 삭제에 대한 요청은 인증된 사용자 중 해당 엔티티의 작성자만이 요청에 대한 접근이 가능하도록 설정정



## 물품 관리

### ● CREATE
URL : `POST /items`

  #### ResquestBody
  ```json
  {
    "title": "중고 노트북 팝니다",
    "description": "20129년 맥북 프로 13인치 모델입니다",
    "minPriceWanted": 1000000,
  }
  ```
  물품을 등록하는 URL 요청
  - 제목, 설명, 최소가격, 작성자, 비밀번호를 필수로 입력해야한다.
  - 물품이 등록되면 물품의 상태는 판매중 상태가 되어야한다.
  - 이미지 등록은 물품 등록과 동시에 이루어질수 없다.
  - 필수 항목이 입력되지 않으면 오류 메세지를 띄운다.
  - 로그인 된 사용자만 물품을 등록할 수 있도록한다.


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
  - 쿼리 파라미터를 전달하지 않으면 페이지 번호는 0, 페이지 크기는 10이 전달되어 전체 물품을 조회한다.


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
    "minPriceWanted": 1000000
  }
  ```
  등록된 물품정보를 수정하는 URL 요청
  - 제목, 설명, 최소 가격을 물품을 등록한 작성자만 수정할 수 있도록한다.
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


  등록된 물품을 삭제하는 URL 요청
  - 물품을 등록한 작성자만 삭제할 수 있도록한다.
  - 해당 글에 달린 모든 댓글을 삭제한다.
  - 해당 물품의 이미지와 이미지 디렉토리를 삭제한다.
  - 해당 물품에 등록된 모든 구매제안을 삭제한다.
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
URL : `POST items/{itemId}/comments`

  #### ResquestBody
  ```json
  {
    "writer": "jeeho.edu",
    "password": "qwerty1234",
    "content": "할인 가능하신가요?"
  }  
  ```
  댓글을 등록하는 URL 요청
  - 등록된 물품에 대해서만 댓글을 달 수 있다.
  - 하나의 물품에 여러 댓글이 달릴 수 있다.


  ※ 댓글 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "댓글이 등록되었습니다."
  }
  ```

  ※ 댓글 등록 실패 시 결과 ( ResponseBody ) - 존재하지 않는 물품에 댓글을 달려고 시도할 때
  ```json
  {
    "timestamp": "2023-07-26T13:57:34.821+00:00",
    "status": 404,
    "error": "Not Found",
    "trace": ...
    "message": "404 NOT_FOUND",
    "path": "/items/2/comments"
  }
  ```


### ● READ
URL : `GET items/{itemId}/comments?page=0&size=10`

  댓글을 조회하는 URL 요청
  - 등록된 물품에 달린 댓글을 조회 할 수 있고 모든 사용자가 조회 가능하다.
  - 페이지 번호와 크기를 전달해주지 않으면 기본적으로 하나의 페이지에 10개의 댓글을 보여주고 0번째 페이지를 보여준다.
  

  ※ 댓글 조회 성공 시 결과 ( ResponseBody )
  ```json
  {
    "content": [
        {
            "id": 1,
            "content": "할인 가능하신가요?"
        },
        {
            "id": 2,
            "content": "조금 더 할인 가능하신가요?"
        },
        {
            "id": 3,
            "content": "할인 가능하신가요? 너무 비쌉니다다"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 3,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalElements": 3,
    "totalPages": 1,
    "size": 3,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
  }
  ```


### ● UPDATE ( 댓글 내용수정 )
URL : `PUT items/{itemId}/comments/{commentId}`

  #### ResquestBody
  ```json
  {
    "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다"
  } 
  ```
  댓글 내용을 수정하는 URL 요청
  - 댓글 작성자와 비밀번호가 일치해야 댓글을 수정할 수 있다. ( 물품 등록자가 아닐 때 )
  - 댓글을 수정할 때는 반드시 내용과 작성자, 비밀번호를 입력해서 올바른 사용자인지 확인한다.


  ※ 댓글 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "댓글이 수정되었습니다"
  }
  ```

  ※ 댓글 등록 실패 시 결과 ( ResponseBody ) - 존재하지 않는 댓글을 수정하려고 시도할 때
  ```json
  {
    "timestamp": "2023-07-26T13:57:34.821+00:00",
    "status": 404,
    "error": "Not Found",
    "trace": ...
    "message": "404 NOT_FOUND",
    "path": "/items/2/comments"
  }
  ```


### ● UPDATE ( 댓글의 답글 달기 )
URL : `PUT items/{itemId}/comments/{commentId}`

  #### ResquestBody
  ```json
  {
    "reply": "안됩니다."
  }
  ```
  댓글의 답글을 다는 URL 요청 ( 댓글 내용 수정 URL 요청과 동일한 요청 )
  - 물품 등록자이고 비밀번호가 일치해야 댓글의 답글을 추가 할 수 있다.
  - 댓글 ( comment ) 엔티티에는 reply 컬럼이 있는데 해당 내용을 전달해서 답글을 추가한다.


  ※ 답글 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "댓글에 답글이 추가되었습니다."
  }
  ```
  ```json
  {
    "content": [
        {
            "id": 1,
            "content": "할인 가능하신가요?",
            "reply": "안됩니다."
        },
        {
            "id": 2,
            "content": "할인 가능하신가요? 1000000 정도면 고려 가능합니다"
        },
        {
            "id": 3,
            "content": "할인 가능하신가요? 너무 비쌉니다다"
        }
    ],
  }
  ```

  ※ 답글 등록 실패 시 결과 ( ResponseBody ) - 물품 등록자 정보가 일치하지 않을 
  ```json
  {
    "timestamp": "2023-07-26T14:15:44.134+00:00",
    "status": 400,
    "error": "Bad Request",
    "trace": ...
    "message": "400 BAD_REQUEST",
    "path": "/items/1/comments/2"
  }
  ```


### ● DELETE
URL : `DELETE /items/{itemId}/comment/{commentId}`


  등록된 댓글을 삭제하는 URL 요청
  - 댓글을 등록한 작성자만 삭제할 수 있도록한다.
  - 작성자 정보가 일치하지 않으면 400 에러를 띄운다.
  - 없는 댓글을 삭제하려고 하는 경우 404 에러를 띄운다.


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
    "path": "/items/1/comment/2"
  }
  ```


  ※ 삭제 실패 시 결과 ( ResponseBody ) - 없는 댓글을 삭제하려고 할 때 
  ```json
  {
    "timestamp": "2023-07-26T14:19:09.708+00:00",
    "status": 404,
    "error": "Not Found",
    "trace":...
    "message": "404 NOT_FOUND",
    "path": "/items/1/comments/2"
  }
  ```


## 구매제안 관리


### ● CREATE
URL : `POST items/{itemId}/proposal`

  #### ResquestBody
  ```json
  {
    "suggestedPrice": 1000000
  }
  ```
  구메 제안을 등록하는 URL 요청
  - 등록된 물품에 대해서만 구매 제안을 요청할 수 있다.
  - 하나의 물품에 여러 구매 제안이 등록 될 수 있다.


  ※ 댓글 등록 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "구매 제안이 등록되었습니다."
  }
  ```


### ● READ
URL : `GET items/1/proposals?page&size=5&writer=jeeho.edu&password=qwerty1234`

![image](https://github.com/likelion-backend-5th/MiniProject_Basic_LeeMinCheol/assets/89755903/df631ace-cbb2-4556-bffb-85cd43dd87bd)

  구매 제안을 조회하는 URL 요청
  - 해당 물품에 대해서 자기 자신이 등록한 구매 제안만을 조회 할 수 있다.
  - 페이지 별로 조회 할 수 있고 페이지의 크기를 조절할 수 있다.
  - 물풍 등록자는 모든 구매 제안을 확인할 수 있다.
  

  ※ 댓글 조회 성공 시 결과 ( ResponseBody )
  ```json
  {
    "content": [
        {
            "id": 1,
            "suggestedPrice": 1000000,
            "status": "제안"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 5,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 1,
    "size": 5,
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

### ● UPDATE
URL : `PUT items/{itemId}/proposals/{proposalId}`

  
  제안 내용을 수정하는 URL 요청 ( 댓글 내용 수정 URL 요청과 동일한 요청 )
  - 제안 등록자이면 제안의 내용을 수정할 수 있다.
  - 물품 등록자이면 특정 제안을 선택해서 수락/거절 할 수 있다. ( 제안의 상태를 제안에서 수락/거절 으로 바꾼다. )
  - 제안 등록자는 제안의 상태가 수락 일 때 구매를 확정 지을 수 있다. ( 제안의 상태를 수락에서 확정으로 바꾼다. )
  - 구매가 확정되면 나머지 제안의 상태는 거절이되고 물품의 상태는 판매완료가 된다.


  #### ResquestBody
  ```json
  {
    "writer": "jeeho.edu",
    "password": "qwerty1234",
    "suggestedPrice": 1100000
  }
  ```

  ※ 제안 내용 수정 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "제안이 수정되었습니다."
  }
  ```


   #### ResquestBody
  ```json
  {
    "status": "수락"
  }
  ```

  ※ 제안 내용 수락/거절 시 결과 ( ResponseBody )
  ```json
  {
    "message": "제안의 상태가 변경되었습니다."
  }
  ```


   #### ResquestBody
  ```json
   {
    "status": "확정"
  }
  ```

  ※ 구매 확정 시 결과 ( ResponseBody )
  ```json
  {
    "message": "구매가 확정되었습니다."
  }
  ```
  ```json
  {
    "content": [
        {
            "id": 1,
            "suggestedPrice": 1000000,
            "status": "확정"
        },
        {
            "id": 2,
            "suggestedPrice": 1000000,
            "status": "거절"
        },
        {
            "id": 3,
            "suggestedPrice": 1000000,
            "status": "거절"
        },
        {
            "id": 4,
            "suggestedPrice": 1000000,
            "status": "거절"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 5,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 4,
    "size": 5,
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
  ```

  
### ● DELETE
URL : `DELETE /items/{itemId}/proposals/{proposalId}`


  등록된 구매 제안을 삭제하는 URL 요청
  - 작성자와 비밀번호를 입력해서 수정 요청을 보내는 사용자가 작성자가 맞는지 확인 한 후 작성자만 삭제할 수 있도록한다.

  
  ※ 삭제 성공 시 결과 ( ResponseBody )
  ```json
  {
    "message": "제안을 삭제했습니다."
  }
  ```
