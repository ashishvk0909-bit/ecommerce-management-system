# Ecommerce Management System (Backend) 🛒

A detailed, developer-friendly README for the **Ecommerce Management System** backend — built with **Spring Boot**, JWT security, JPA/Hibernate and a layered architecture (controllers → services → repositories). This README explains the code structure, key components, endpoints, sample requests, and how to run & extend the project. It's written so any developer can read the code and quickly understand what's going on.

---

## Table of Contents
- [Project summary](#project-summary)
- [Key controllers (what they do)](#key-controllers-what-they-do)
- [Main DTOs & Models (explanations)](#main-dtos--models-explanations)
- [Security overview (JWT + filter + roles)](#security-overview-jwt--filter--roles)
- [Important endpoints & sample requests (curl/Postman)](#important-endpoints--sample-requests-curlpostman)
- [Database & configuration](#database--configuration)
- [How to run locally](#how-to-run-locally)


---

## Project summary
This backend implements common e-commerce features:
- Product management (including images stored as `byte[]` blobs).
- Category management (admin-only).
- Coupon creation and validation.
- Cart operations, checkout (placing orders), and applying coupons.
- Wishlist and review endpoints.
- Order management with `OrderStatus` and admin APIs to view/update orders.
- JWT-based authentication with `ADMIN` and `CUSTOMER` roles.

The app follows a controller → service → repository pattern. Controllers are thin HTTP adapters; services hold business logic, and models are JPA entities with helper DTO converters.

---

## Key controllers (what they do)

### Admin controllers
- **`AdminCategoryController`**
  - `POST /api/admin/categories` — create a new category.
  - `GET /api/admin/categories` — list all categories.

- **`AdminCouponController`**
  - `POST /api/admin/coupons` — create a coupon.
  - `GET /api/admin/coupons` — list coupons.

- **`AdminOrderController`**
  - `GET /api/admin/orders/placed` — list placed / shipped / delivered orders.
  - `PATCH /api/admin/orders/{orderId}/status?status=Shipped` — change order status.

- **`AdminProductController`**
  - `POST /api/admin/products` — create product (multipart form).
  - `GET /api/admin/products` — list products.
  - `GET /api/admin/products/search?name=...` — search by name.
  - `GET /api/admin/products/{id}` — get by id.
  - `DELETE /api/admin/products/{productId}` — delete.
  - `PUT /api/admin/products/{productId}` — update product.

### Auth controller
- **`AuthenticationController`**
  - `POST /api/auth/signup` — register user.
  - `POST /api/auth/signin` — login (returns JWT).
  - Creates admin user automatically if not present.

### Customer controllers
- **`CartController`**
  - `POST /add` — add product to cart.
  - `GET /user/{userId}` — get pending cart.
  - `POST /apply-coupon/{userId}/{couponCode}` — apply coupon.
  - `POST /increase` / `POST /decrease` — update quantities.
  - `POST /place` — place order (checkout).
  - `GET /orders/{userId}` — fetch user orders.

- **`ReviewController`**
  - `GET /order/{orderId}` — get products for review.
  - `POST /` — submit review (multipart).

- **`WishlistController`**
  - `POST /` — add to wishlist.
  - `GET /user/{userId}` — get wishlist for user.

---

## Main DTOs & Models (explanations)

### DTOs
- **`ProductDto`** — product data transfer (with image upload).
- **`CategoryDto`** — category data.
- **`AddProductInCartDto`** — add product to cart request.
- **`CartItemDto`** — cart item response.
- **`OrderDto`** — complete order response.
- **`PlaceOrderDto`** — checkout request.
- **`ReviewDto`** — review request/response.
- **`WishlistDto`** — wishlist entry.
- **Auth DTOs** — signup, signin, JWT response, user info.

### Models
- **`Product`**, **`Category`**, **`CartItems`**, **`Order`**, **`Coupon`**, **`Wishlist`**, **`Review`**, **`User`**.
- **Enums**: `OrderStatus { Pending, Placed, Shipped, Delivered }`, `UserRole { ADMIN, CUSTOMER }`.

---

## Security overview (JWT + filter + roles)

- **`JwtUtil`** — generates/validates JWT with roles.
- **`JwtRequestFilter`** — reads token, validates, sets authentication.
- **`SecurityConfig`** — secures APIs:
  - `/api/auth/**` open.
  - `/api/admin/**` restricted to `ADMIN`.
  - JWT filter + BCrypt password encoding.
- Admin user auto-created at startup (`admin@gmail.com` / `admin`).

---

## Important endpoints & sample requests (curl/Postman)

### 🔐 Authentication

**Login (Signin)**
```http
POST /api/auth/signin
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "password"
}
```
**Response**
```
{
  "token": "<JWT token>",
  "roleTable": "CUSTOMER"
}
```
Use the token in protected endpoints:
```
Authorization: Bearer <token>
```

### 🗂️ Admin APIs

**Create Category**

```
POST /api/admin/categories
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Electronics",
  "description": "Phones, laptops and accessories"
}
```

**Create Product (multipart)**

```
curl -X POST "http://localhost:8080/api/admin/products" \
  -H "Authorization: Bearer <admin-token>" \
  -F "name=iPhone 15" \
  -F "price=99900" \
  -F "description=Latest iPhone" \
  -F "categoryId=1" \
  -F "imageFile=@/path/to/image.jpg"
````

### 🛒 Cart APIs

**Add Product**

```
POST /api/customer/cart/add
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 2,
  "productId": 10
}
```

**Apply Coupon**

```
POST /api/customer/cart/apply-coupon/{userId}/{couponCode}
Authorization: Bearer <token>
```

**Place Order**

```
POST /api/customer/cart/place
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 2,
  "address": "221B Baker Street",
  "orderDescription": "Gift for John"
}
```
 
### ⭐ Review API

**Submit Review**
```
curl -X POST "http://localhost:8080/api/customer/reviews" \
  -H "Authorization: Bearer <token>" \
  -F "rating=5" \
  -F "description=Great!" \
  -F "productId=10" \
  -F "userId=2" \
  -F "img=@/path/to/photo.jpg"
```

---

### Database & configuration**

Edit src/main/resources/application.properties:
```
spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
 ⚠️ Notes:

Use Flyway/Liquibase for production migrations.

Store images externally (e.g., S3) instead of blobs for scalability.

---

### How to run locally

Clone repo:
```
git clone <your-repo-url>
cd EcommerceManagementSystem
```

Configure DB in application.properties.

Build & run:
```
./mvnw clean package
java -jar target/EcommerceManagementSystem-0.0.1-SNAPSHOT.jar
```

Or run directly in your IDE with Spring Boot.

---
