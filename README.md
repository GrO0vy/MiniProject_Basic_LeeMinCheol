# 멋사 마켓 서비스 만들기
## 3.1 ♻️멋사마켓
🥕당근마켓, 중고나라 등을 착안하여 중고 제품 거래 플랫폼을 만들어보는 미니 프로젝트입니다.

사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 거래 플랫폼의 백엔드를 만드는 프로젝트입니다.

## 물품 관리

### Create

URL : POST http://localhost:8080/items

{

    "title": "중asd북 팝니다",
    
    "description": "20129년 맥북 프로 13인치 모델입니다",
    
    "minPriceWanted": 1000000,
    
    "writer": "lee.dev",
    
    "password": "1qaz2wsx"
    
}

result >

{

    "message": "등록이 완료되었습니다"
    
}

### READ

URL : http://localhost:8080/items?page=0&size=2

result > 

{
    "content": [
        {
        
            "id": 1,
            
            "title": "중asd북 팝니다",
            
            "description": "20129년 맥북 프로 13인치 모델입니다",
            
            "minPriceWanted": 1000000,
            
            "status": "판매중"
            
        },
        
        {
        
            "id": 2,
            
            "title": "중asd북 팝니다",
            
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
        
        "pageSize": 2,
        
        "pageNumber": 0,
        
        "paged": true,
        
        "unpaged": false
        
    },
    
    "last": false,
    
    "totalPages": 2,
    
    "totalElements": 3,
    
    "size": 2,
    
    "number": 0,
    
    "sort": {
    
        "empty": true,
        
        "sorted": false,
        
        "unsorted": true
        
    },
    
    "first": true,
    
    "numberOfElements": 2,
    
    "empty": false
    
}

URL : GET /items{itemId}

result >

{

    "title": "중asd북 팝니다",
    
    "description": "20129년 맥북 프로 13인치 모델입니다",
    
    "minPriceWanted": 1000000,
    
    "status": "판매중"
    
}


### UPDATE

URL : PUT /items{itemId}

{

    "title": "중고 맥북 팝니다",
    
    "description": "2029년 맥북 프로 13인치 모델입니다",
    
    "minPriceWanted": 1100000,
    
    "writer": "jeeho.dev",
    
    "password": "1qaz2wsx"
    
}

{

    "message": "물품이 수정되었습니다."
    
}

URL : PUT /items{itemId}/images

form-data >

image:    image.png (file)

writer:   jeeho.dev

password: 1qaz2wsx


result >

{

    "message": "이미지가 등록되었습니다."
    
}



### DELETE

URL : DELETE /items{itemId}

{

    "writer": "jeeho.dev",
    
    "password": "1qaz2wsx"
    
}

result >

{

    "message": "물품을 삭제했습니다."
    
}
