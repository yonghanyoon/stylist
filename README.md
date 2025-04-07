## 빌드, 테스트, 실행 방법

### 환경 요구사항
- **Java:** JDK 17 이상
- **빌드 도구:** Gradle
- **프레임워크:** Spring Boot
- **API 문서:** Swagger UI

### 빌드
프로젝트 루트 디렉토리에서 다음 명령어를 실행하여 프로젝트를 빌드합니다:

```bash
./gradlew clean build
cd build/libs
java -jar stylist-0.0.1-SNAPSHOT.jar
```
### 테스트
```bash
./gradlew test
```

# 카테고리별 최저가격 조회 API

## 개요
고객이 각 카테고리별로 최저 가격을 가진 브랜드와 상품 가격, 합산 총액을 조회하는 API  

## 설명
- 각 카테고리마다 최저 가격을 가진 상품 정보를 조회합니다.  
- 특정 카테고리에 상품이 존재하지 않으면 `CustomNotFoundException`을 발생시키며, 모든 카테고리에는 반드시 상품이 존재해야 합니다.

## 엔드포인트
**GET** `/api/v1/products/categories`

## 요청 (Request)
- 없음

## 응답 (Response)
### 응답 예시

```json
{
  "details": [
    {
      "categoryName": "상의",
      "brandName": "C",
      "price": 10000
    },
    {
      "categoryName": "아우터",
      "brandName": "E",
      "price": 5000
    },
    {
      "categoryName": "바지",
      "brandName": "D",
      "price": 3000
    },
    {
      "categoryName": "스니커즈",
      "brandName": "A",
      "price": 9000
    },
    {
      "categoryName": "가방",
      "brandName": "A",
      "price": 2000
    },
    {
      "categoryName": "모자",
      "brandName": "D",
      "price": 1500
    },
    {
      "categoryName": "양말",
      "brandName": "I",
      "price": 1700
    },
    {
      "categoryName": "액세서리",
      "brandName": "F",
      "price": 1900
    }
  ],
  "totalAmount": 34100
}
```
## 에러 응답

### 404 Not Found

**상황:**  
특정 카테고리에 대해 상품 데이터가 존재하지 않을 경우

**응답 예시:**

```json
{
  "status": 404,
  "message": "해당 카테고리의 상품을 찾을 수 없습니다. category: 상의"
}
```
**상황:**  
특정 Id의 브랜드 데이터가 존재하지 않을 경우

**응답 예시:**

```json
{
  "status": 404,
  "message": "브랜드를 찾을 수 없습니다. BrandId: 1"
}
```

---

# 단일 브랜드 최저 총액 조회 API

## 개요
고객이 단일 브랜드로 전체 카테고리의 상품을 구매할 경우,
각 카테고리별 최저가 상품의 가격과 총액을 계산하여,
최저 총액을 가진 브랜드의 정보를 조회하는 API

## 엔드포인트
**GET** `/api/v1/products/brands/lowest`

## 설명
- 각 브랜드에 대해 모든 카테고리에서 상품이 존재하는지 확인한 후,  
  해당 브랜드의 각 카테고리 상품 중 최저 가격을 선택하여 총합을 계산합니다.
- 여러 브랜드 중 모든 카테고리에 상품을 제공하는 브랜드들 중에서,  
  총액이 가장 낮은 브랜드의 정보를 반환합니다.
- 만약 모든 브랜드 중에서 모든 카테고리에 상품이 존재하는 브랜드를 찾지 못하면,  
  `CustomNotFoundException`을 발생시킵니다.

## 요청 (Request)
- 없음

## 응답 (Response)
### 응답 예시

```json
{
  "brandName": "D",
  "category": [
    { "categoryName": "상의", "price": 10100 },
    { "categoryName": "아우터", "price": 5100 },
    { "categoryName": "바지", "price": 3000 },
    { "categoryName": "스니커즈", "price": 9500 },
    { "categoryName": "가방", "price": 2500 },
    { "categoryName": "모자", "price": 1500 },
    { "categoryName": "양말", "price": 2400 },
    { "categoryName": "액세서리", "price": 2000 }
  ],
  "totalAmount": 36100
}
```
## 에러 응답

### 404 Not Found

**상황:**  
모든 카테고리에 대해 상품을 제공하는 단일 브랜드를 찾지 못한 경우

**응답 예시:**

```json
{
  "status": 404,
  "message": "모든 카테고리에 상품을 제공하는 브랜드를 찾을 수 없습니다."
}
```

---

# 특정 카테고리 최저/최고 가격 조회 API

## 개요
고객이 특정 카테고리의 상품 가격 정보를 조회할 때,  
해당 카테고리에서 최저 가격과 최고 가격을 제공하는 브랜드 및 해당 가격 정보를 반환하는 API

## 설명
- 요청된 카테고리명을 기준으로 카테고리를 조회합니다.
- 해당 카테고리에 속한 모든 상품을 조회하여,
  - 최저 가격과 최고 가격을 계산합니다.
  - 최저 가격과 최고 가격에 해당하는 상품들의 브랜드 정보를 조회하여,  
    각각의 브랜드 및 가격 정보를 반환합니다.
- 카테고리나 상품, 또는 브랜드가 존재하지 않는 경우 `CustomNotFoundException`을 발생시킵니다.

## 엔드포인트
**GET** `/api/v1/products/categories/{categoryName}`

## 요청 (Request)
- **Path Variable:**
  - `categoryName` (String): 조회할 카테고리 이름  
    예: `/api/v1/products/categories/상의`

## 응답 (Response)
### 응답 예시

```json
{
  "category": "상의",
  "minPrice": [
    { "brand": "C", "price": 10000 }
  ],
  "maxPrice": [
    { "brand": "I", "price": 11400 }
  ]
}
```

## 에러 응답

### 404 Not Found

**상황:**  
요청한 카테고리를 찾을 수 없는 경우

**응답 예시:**

```json
{
  "status": 404,
  "message": "카테고리를 찾을 수 없습니다. categoryName: 무신사"
}
```

**상황:**  
해당 카테고리에 상품 데이터가 존재하지 않는 경우

**응답 예시:**

```json
{
  "status": 404,
  "message": "해당 카테고리의 상품을 찾을 수 없습니다. category: 상의"
}
```

**상황:**  
해당 카테고리에서 최저/최고 가격을 계산할 상품 데이터가 존재하지 않을 경우

**최저 가격 계산 실패 응답 예시:**

```json
{
  "status": 404,
  "message": "최저 가격을 계산할 상품이 없습니다."
}
```

**최고 가격 계산 실패 응답 예시:**

```json
{
  "status": 404,
  "message": "최고 가격을 계산할 상품이 없습니다."
}
```

# 운영자(ADMIN) API

## 개요
운영자가 브랜드와 상품 정보를 관리(등록, 수정, 삭제)할 수 있는 API
- **브랜드 등록 시:**
  - 새로운 브랜드와 함께 각 카테고리별 상품 정보도 반드시 함께 등록해야 합니다.
- **브랜드 수정 시:**
  - 브랜드의 이름을 변경할 수 있습니다. (상품 정보는 별도 처리)
- **브랜드 삭제 시:**
  - 해당 브랜드와 그에 속한 모든 상품이 삭제됩니다.
- **상품 등록/수정 시:**
  - 유효한 브랜드와 카테고리 정보가 필요하며, 잘못된 정보(존재하지 않는 카테고리)는 예외로 처리됩니다.
- **상품 삭제 시:**
  - 삭제 전 해당 브랜드의 해당 카테고리에는 최소 1개 이상의 상품이 남아 있어야 합니다.

모든 요청 및 응답은 DTO를 통해 처리되며, 도메인 엔티티는 직접 노출되지 않습니다.

## 엔드포인트

### 브랜드 관련 API
- **POST** `/api/v1/admin/brands`  
  → 브랜드 등록

- **PUT** `/api/v1/admin/brands/{brandId}`  
  → 브랜드 수정

- **DELETE** `/api/v1/admin/brands/{brandId}`  
  → 브랜드 삭제

### 상품 관련 API
- **POST** `/api/v1/admin/products`  
  → 상품 등록  
  *(요청 시 `brandId`는 Request Parameter로 전달)*

- **PUT** `/api/v1/admin/products/{productId}`  
  → 상품 수정  
  *(요청 시 `brandId`는 Request Parameter로 전달)*

- **DELETE** `/api/v1/admin/products/{productId}`  
  → 상품 삭제